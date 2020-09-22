package com.danc.winesapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danc.winesapi.Adapter.CartItemAdapter;
import com.danc.winesapi.Mpesa.Mpesa2Activity;

import com.danc.winesapi.SQLite.CartItemOpenHelper;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;

    CartItemOpenHelper mDb;
    ArrayList<String> title, images, price, description, quantity;
    CartItemAdapter adapter;

    TextView amount_total, amount_quantity;
    String regex = "^.*[\\(\\)].*$";
    RelativeLayout proceedToCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_cart);

        mDb = new CartItemOpenHelper(this);
//        id = new ArrayList<>();
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

        adapter = new CartItemAdapter(this, title, images, price, description, quantity);
        recyclerView.setAdapter(adapter);

        getPriceTotals();


    }

    void getAllData() {
        Cursor cursor = mDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Failed to Fetch the data", Toast.LENGTH_SHORT).show();

        } else {
            while (cursor.moveToNext()) {
                Log.d(TAG, "getAllData: Number of items " + cursor.getCount());
                Toast.makeText(this, "Number of Items " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                getQuantityTotals();
//                id.add(cursor.getString(0));
//                productActivity.item_count.setText(cursor.getCount());
                title.add(cursor.getString(1));
                images.add(cursor.getString(2));
                price.add(cursor.getString(3));
                description.add(cursor.getString(4));
                quantity.add(cursor.getString(5));

            }
        }
    }

    int getPriceTotals() {
        Cursor cursor = mDb.calculatePriceTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            Log.d(TAG, "getTotals: TotalValue " + total);

            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher();
            amount_total.setText(String.valueOf(total));
        } else {
            amount_total.setText(String.valueOf(0));
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
            amount_quantity.setText(String.valueOf(0));
            amount_quantity.setVisibility(View.INVISIBLE);
        }
        return total;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proceed_to_checkout:
                launchPurchaseFragment();
                Intent intent = new Intent(this, Mpesa2Activity.class);
                startActivity(intent);
                deleteAll();
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

    public boolean deleteAll(){
        Cursor cursor = mDb.clearSQLite();

        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}