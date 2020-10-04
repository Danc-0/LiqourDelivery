package com.danc.winesapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.danc.winesapi.Adapter.ProductAdapter;
import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.Product;
import com.danc.winesapi.SQLite.CartItemOpenHelper;
import com.danc.winesapi.UIFragments.MainFragment;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Retrofit;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    CartItemOpenHelper mDb;

    TextView item_count;
    LinearLayout Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item_count = findViewById(R.id.item_count);
        recyclerView = findViewById(R.id.recycler_view);
        Cart = findViewById(R.id.cart);
        Cart.setOnClickListener(this);

        mDb = new CartItemOpenHelper(this);

        init();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));

    }

    public void init() {
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedHexColor = intent.getStringExtra("Item Position");
            item_count.setText(receivedHexColor);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cart:
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            break;
        }
    }
}