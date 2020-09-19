package com.danc.winesapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.danc.winesapi.Adapter.CartItemAdapter;
import com.danc.winesapi.SQLite.CartItemOpenHelper;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;

    CartItemOpenHelper mDb;
    ArrayList<String> title, images, price, description, quantity;
    CartItemAdapter adapter;

    TextView amount_total;

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

        getAllData();

        recyclerView = findViewById(R.id.recyclerv_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CartItemAdapter(this, title, images, price, description, quantity);
        recyclerView.setAdapter(adapter);

        getTotals();


    }

    void getAllData() {
        Cursor cursor = mDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Failed to Fetch the data", Toast.LENGTH_SHORT).show();

        } else {
            while (cursor.moveToNext()) {
                Log.d(TAG, "getAllData: Number of items " + cursor.getCount());
                Toast.makeText(this, "Number of Items " + cursor.getCount(), Toast.LENGTH_SHORT).show();
//                getTotals();
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

    int getTotals() {
        Cursor cursor = mDb.calculateTotals();
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
            Log.d(TAG, "getTotals: TotalValue " + total);
            amount_total.setText(String.valueOf(total));
        } else {
            amount_total.setText(String.valueOf(0));
            amount_total.setVisibility(View.INVISIBLE);
        }
        return total;
    }
}