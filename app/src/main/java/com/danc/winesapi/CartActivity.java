package com.danc.winesapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.Adapter.CartItemAdapter;
import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.CheckOut;
import com.danc.winesapi.Models.OrderProducts;
import com.danc.winesapi.Mpesa.MpesaActivity;
import com.danc.winesapi.Mpesa.MpesaActivity;
import com.danc.winesapi.SQLite.CartItemOpenHelper;
import com.danc.winesapi.SQLite.ItemContractClass;

import java.util.ArrayList;
import java.util.Iterator;
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
    ArrayList<String> id, quantity;
    String itemId;
    CartItemAdapter adapter;
    private SQLiteDatabase mDatabase;

    TextView emailAddress, amount_total, amount_quantity;
    RelativeLayout proceedToCheckout;
    ArrayList<String> product;

    ApiClient apiClient;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_cart);

        CartItemOpenHelper dbHelper = new CartItemOpenHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mDb = new CartItemOpenHelper(this);
        id = new ArrayList<>();
        quantity = new ArrayList<>();

        emailAddress = findViewById(R.id.email);
        amount_total = findViewById(R.id.amount_total);
        amount_quantity = findViewById(R.id.amount_quantity);
        proceedToCheckout = findViewById(R.id.proceed_to_checkout);
        proceedToCheckout.setOnClickListener(this);

        getAllData();

        recyclerView = findViewById(R.id.recyclerv_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
                adapter.notifyDataSetChanged();
                adapter.swapCursor(getAllItems());
            }
        }).attachToRecyclerView(recyclerView);


        adapter = new CartItemAdapter(this, id, quantity, getAllItems());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.swapCursor(getAllItems());

        getPriceTotals();
        getQuantityTotals();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);


    }

    private void removeItem(long id) {
        mDatabase.delete(ItemContractClass.CartItemDetails.TABLE_NAME,
                ItemContractClass.CartItemDetails._ID + "=" + id, null);
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ItemContractClass.CartItemDetails.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ItemContractClass.CartItemDetails.COLUMN_TIMESTAMP + " DESC"
        );
    }

    void getAllData() {
        Cursor cursor = mDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Please wait: ", Toast.LENGTH_SHORT).show();

        } else {
            while (cursor.moveToNext()) {

                id.add(cursor.getString(1));
                quantity.add(cursor.getString(6));

                getPriceTotals();
                getQuantityTotals();

            }
        }
    }

    public Cursor getPriceTotals() {
        Cursor cursor = mDb.calculatePriceTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            amount_total.setText(String.valueOf(total));
        } else {
            amount_total.setVisibility(View.INVISIBLE);
        }
        return cursor;
    }

    public Cursor getQuantityTotals() {
        Cursor cursor = mDb.calculateQuantityTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            amount_quantity.setText(String.valueOf(total));
        } else {
            amount_quantity.setVisibility(View.INVISIBLE);

        }
        return cursor;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed_to_checkout:
                productCheckOut();
                launchPurchaseFragment();
                break;
        }
    }

    private void launchPurchaseFragment() {
        String Amount = amount_total.getText().toString();
        Intent intent = new Intent(this, MpesaActivity.class);
        intent.putExtra("Amount", Amount);
        startActivity(intent);
        finish();

    }

    public void productCheckOut() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User_Email", Context.MODE_PRIVATE);
        String email  =sharedPreferences.getString("email", "");

        Iterator<String> itemsId = id.iterator();
        Iterator<String> itemsQuantity = quantity.iterator();
        while (itemsQuantity.hasNext() && itemsId.hasNext()) {
            String firstHope = itemsId.next();
            String finalQuantities = itemsQuantity.next();

            List<OrderProducts> products = new ArrayList<>();
            products.add(new OrderProducts(firstHope, finalQuantities));

            CheckOut checkOut1 = new CheckOut(email, products);

            Call<CheckOut> checkOut = apiClient.checkingOutOrder(checkOut1);
            checkOut.enqueue(new Callback<CheckOut>() {
                @Override
                public void onResponse(Call<CheckOut> call, Response<CheckOut> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(CartActivity.this, "Error please try again", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(CartActivity.this, "Proceed to Pay", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CheckOut> call, Throwable t) {
                    Toast.makeText(CartActivity.this, "Failed try again: ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQuantityTotals();
        getPriceTotals();
    }
}