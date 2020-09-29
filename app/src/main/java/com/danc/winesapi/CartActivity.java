package com.danc.winesapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.Adapter.CartItemAdapter;
import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.CheckOut;
import com.danc.winesapi.Models.OrderProducts;
import com.danc.winesapi.Mpesa.Mpesa2Activity;
import com.danc.winesapi.SQLite.CartItemOpenHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;

    CartItemOpenHelper mDb;
    ArrayList<String> id, title, images, price, description, quantity;
    String itemId;
    CartItemAdapter adapter;

    TextView amount_total, amount_quantity;
    RelativeLayout proceedToCheckout;
    ArrayList<String> product;

    String userEmail;
    ApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_cart);

        mDb = new CartItemOpenHelper(this);
        id = new ArrayList<>();
        title = new ArrayList<>();
        images = new ArrayList<>();
        price = new ArrayList<>();
        description = new ArrayList<>();
        quantity = new ArrayList<>();

        amount_total = findViewById(R.id.amount_total);
        amount_quantity = findViewById(R.id.amount_quantity);
        proceedToCheckout = findViewById(R.id.proceed_to_checkout);
        proceedToCheckout.setOnClickListener(this);

        getAllData();

        recyclerView = findViewById(R.id.recyclerv_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CartItemAdapter(this, id, title, images, price, description, quantity);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreate: Adapter details: " + adapter);
        getPriceTotals();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            userEmail = intent.getStringExtra("User Email");

        }
    };

    void getAllData() {
        Cursor cursor = mDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Please wait: ", Toast.LENGTH_SHORT).show();


        } else {
            while (cursor.moveToNext()) {
                getQuantityTotals();
                id.add(cursor.getString(1));
                title.add(cursor.getString(2));
                images.add(cursor.getString(3));
                price.add(cursor.getString(4));
                description.add(cursor.getString(5));
                quantity.add(cursor.getString(6));

                Log.d(TAG, "getAllData: I want this data: " + id);
                Log.d(TAG, "getAllData: I want this data set: " + quantity);

            }
        }
    }

    int getPriceTotals() {
        Cursor cursor = mDb.calculatePriceTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            Log.d(TAG, "getTotals: TotalValue " + total);

            amount_total.setText(String.valueOf(total));
        } else {
            amount_total.setVisibility(View.INVISIBLE);
        }
        return total;
    }

    int getQuantityTotals() {
        Cursor cursor = mDb.calculateQuantityTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            Log.d(TAG, "getTotals: TotalValue " + total);
            amount_quantity.setText(String.valueOf(total));
        } else {
            amount_quantity.setVisibility(View.INVISIBLE);
        }
        return total;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed_to_checkout:
                productCheckOut();
                break;
        }
    }

    private void launchPurchaseFragment() {
        String Amount = amount_total.getText().toString();
        Intent intent = new Intent(this, Mpesa2Activity.class);
        intent.putExtra("Amount", Amount);
        startActivity(intent);
        finish();

    }

    public void productCheckOut(){
        List<OrderProducts> products = new ArrayList<>();
        products.add(new OrderProducts("id", "quantity"));
        String email = "lunjalu@gmail.com";

        CheckOut checkOut1 = new CheckOut(email, products);

        Call<CheckOut> checkOut = apiClient.checkingOutOrder(checkOut1);
        checkOut.enqueue(new Callback<CheckOut>() {
            @Override
            public void onResponse(Call<CheckOut> call, Response<CheckOut> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Response Error: " + response.code());
                    Toast.makeText(CartActivity.this, "Response Error please try again", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    launchPurchaseFragment();
                    Toast.makeText(CartActivity.this, "Request Successful: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckOut> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Failed try again: ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}