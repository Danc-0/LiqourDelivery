package com.danc.winesapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danc.winesapi.Models.Product;
import com.danc.winesapi.Mpesa.Mpesa2Activity;
import com.danc.winesapi.SQLite.CartItemOpenHelper;
import com.danc.winesapi.SQLite.ItemContractClass;
import com.squareup.picasso.Picasso;

import static com.danc.winesapi.SQLite.ItemContractClass.CartItemDetails.TABLE_NAME;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ItemActivity";
    Product product = new Product();
    ImageView productImage;
    TextView productTitle;
    TextView productDescription;
    TextView productPrice;
    TextView productOriginalPrice;
    TextView productQuantity;
    TextView itemCount;
    TextView numRates;
    RatingBar ratingBar;
    RelativeLayout addToCart, quantityBtn;

    String itemId;
    String cartItemImage;
    public int count = 0;

    int totalCount = 0;
    CartItemOpenHelper mDb;

    LinearLayout cart;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_product);

        productImage = findViewById(R.id.image);
        numRates = findViewById(R.id.num_ratings);
        ratingBar = findViewById(R.id.rating);
        productTitle = findViewById(R.id.title);
        productDescription = findViewById(R.id.description);
        productPrice = findViewById(R.id.price);
        productOriginalPrice = findViewById(R.id.original_price);
        productQuantity = findViewById(R.id.quantity);

        itemCount = findViewById(R.id.item_count);
        mDb = new CartItemOpenHelper(this);

        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(this);

        quantityBtn = findViewById(R.id.quantity_button);
        quantityBtn.setOnClickListener(this);

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(this::onClick);

        getSelectedData();
        getAllData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedHexColor = intent.getStringExtra("Item Position");
            productQuantity.setText(receivedHexColor);
        }
    };

    public void getSelectedData() {
        Intent intent = getIntent();
        Product selectedProduct = (Product) intent.getSerializableExtra("SelectedProduct");

        if (selectedProduct == null) {
            selectedProduct = new Product();
        }
        String imageUrlHead = getString(R.string.base_url);
        cartItemImage = imageUrlHead + selectedProduct.getImageUrl();

        this.product = selectedProduct;
//        itemId = selectedProduct.getId();
        productTitle.setText(selectedProduct.getProductName());

        Picasso.get().load(cartItemImage).into(productImage);

        productDescription.setText(selectedProduct.getProductDescription());
        productPrice.setText(selectedProduct.getProductPrice());
        productOriginalPrice.setText(selectedProduct.getInitialPrice());
        numRates.setText(selectedProduct.getRatingsNumbers());

        Float rates = Float.parseFloat(numRates.getText().toString());
        ratingBar.setRating(rates);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_to_cart:
                Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
                InsertIntoSQLiteDB();
                break;

            case R.id.quantity_button:
                ChooseDialog chooseDialog = new ChooseDialog();
                chooseDialog.show(getSupportFragmentManager(), "Quantity choice");
                break;

            case R.id.cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
        }
    }

    public boolean InsertIntoSQLiteDB() {
        String title = productTitle.getText().toString().trim();
        String imageUrl = cartItemImage;
        int price = Integer.parseInt(productPrice.getText().toString().trim());
        int originalPrice = Integer.parseInt(productOriginalPrice.getText().toString().trim());
        int quantity = Integer.parseInt(productQuantity.getText().toString().trim());

        int totalPerItem = price * quantity;

        CartItemOpenHelper dbOpenHelper = new CartItemOpenHelper(ItemActivity.this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemContractClass.CartItemDetails.COLUMN_TITLE, title);
        values.put(ItemContractClass.CartItemDetails.COLUMN_IMAGE_URL, imageUrl);
        values.put(ItemContractClass.CartItemDetails.COLUMN_PRICE, price);
        values.put(ItemContractClass.CartItemDetails.COLUMN_ORIGINAL_PRICE, originalPrice);
        values.put(ItemContractClass.CartItemDetails.COLUMN_QUANTITY, quantity);
        values.put(ItemContractClass.CartItemDetails.COLUMN_EACH_TOTAL, totalPerItem);

        long newRowID = db.insert(TABLE_NAME, null, values);

        if (newRowID == -1) {
            return false;

        } else {
            Toast.makeText(this, "Data Inserted Successful", Toast.LENGTH_SHORT).show();
            addToCounter();
            return true;
        }

    }

    public void addToCounter() {
        value = Integer.parseInt(itemCount.getText().toString());
        value++;
        if (value == 0) {
            itemCount.setVisibility(View.INVISIBLE);
        } else {
            itemCount.setText(String.valueOf(value));
        }
    }

    void getAllData() {
        Cursor cursor = mDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();

        } else if (value == 0) {
            while (cursor.moveToNext()) {
                Log.d(TAG, "getAllData: Number of items " + cursor.getCount());
                Toast.makeText(this, "Number of Items " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                itemCount.setText(String.valueOf(cursor.getCount()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}