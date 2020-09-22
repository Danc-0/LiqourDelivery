package com.danc.winesapi.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.R;
import com.danc.winesapi.SQLite.CartItemOpenHelper;
import com.danc.winesapi.SQLite.ItemContractClass;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private static final String TAG = "CartItemAdapter";
    private Context context;
    private ArrayList id, title, imageUrl, price, originalPrice, quantity;

    public CartItemAdapter(Context context,
                           ArrayList id,
                           ArrayList title,
                           ArrayList imageUrl1,
                           ArrayList price,
                           ArrayList initialPrice,
                           ArrayList quantity) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl1;
        this.price = price;
        this.originalPrice = initialPrice;
        this.quantity = quantity;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.itemId.setText(String.valueOf(id.get(position)));
        holder.title.setText(String.valueOf(title.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.initialPrice.setText(String.valueOf(originalPrice.get(position)));
        holder.quantity.setText(String.valueOf(quantity.get(position)));
        Picasso.get().load(String.valueOf(imageUrl.get(position))).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemId, title, price, initialPrice, quantity;
        ImageView image;
        RelativeLayout increaseQuantity, decreaseQuantity, deleteItem;
        CartItemOpenHelper mDb = new CartItemOpenHelper(context);
        String id;

        int currentQty;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            itemId = itemView.findViewById(R.id.itemId);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            initialPrice = itemView.findViewById(R.id.original_price);
            quantity = itemView.findViewById(R.id.quantity);

            increaseQuantity = itemView.findViewById(R.id.increase_quantity);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
            deleteItem = itemView.findViewById(R.id.delete_item);

            increaseQuantity.setOnClickListener(this);
            decreaseQuantity.setOnClickListener(this);
            deleteItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.increase_quantity:
                    currentQty = Integer.parseInt(quantity.getText().toString());
                    currentQty++;
                    id = itemId.getText().toString();
                    quantity.setText(String.valueOf(currentQty));
//                    updateDb(id);
                    break;

                case R.id.decrease_quantity:
                    currentQty = Integer.parseInt(quantity.getText().toString());
                    if (currentQty >= 0) {
                        quantity.setText(String.valueOf(currentQty));
                        {
                            currentQty--;
                            if (currentQty == 0) {
//                                deleteData();
                                quantity.setVisibility(View.GONE);
                            } else {
//                                updateDb(id);
                                quantity.setText(String.valueOf(currentQty));
                                break;
                            }
                        }
                    }


            }
        }
        private void deleteData() {
            String id = itemId.getText().toString();
            mDb.deleteOneRow(id);

        }
    }
}

//        public void updateDb(String row_id) {
//            CartItemOpenHelper dbHelper = new CartItemOpenHelper(context);
//            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(ItemContractClass.CartItemDetails.COLUMN_QUANTITY, String.valueOf(currentQty));
//            sqLiteDatabase.update(ItemContractClass.CartItemDetails.TABLE_NAME, contentValues, "_id=?", new String[]{row_id});

//            getQuantityTotals();
////            getPriceTotals();
//        }
//
//    }

//    int getPriceTotals() {
//        Cursor cursor = mDb.calculatePriceTotals();
//        int total = 0;
//        if (cursor.moveToFirst()) {
//            total = cursor.getInt(cursor.getColumnIndex("Total"));
//            Log.d(TAG, "getTotals: TotalValue " + total);
//
//        }
//        return total;
//    }
//
//    int getQuantityTotals() {
//        Cursor cursor = mDb.calculateQuantityTotals();
//        int total = 0;
//        if (cursor.moveToFirst()) {
//            total = cursor.getInt(cursor.getColumnIndex("Total"));
//            Log.d(TAG, "getTotals: TotalValue " + total);
//
//        }
//        return total;
//    }

