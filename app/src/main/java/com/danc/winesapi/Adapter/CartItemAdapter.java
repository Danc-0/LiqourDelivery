package com.danc.winesapi.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.R;
import com.danc.winesapi.SQLite.CartItemOpenHelper;
import com.danc.winesapi.SQLite.ItemContractClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private static final String TAG = "CartItemAdapter";
    private Context context;
    private ArrayList<String> id, productTitle, imageUrl, productPrice, originalPrice, productQuantity;

    private Context mContext;
    private Cursor mCursor;

    public CartItemAdapter(Context context, ArrayList id, ArrayList quantity, Cursor cursor) {
        mContext = context;
        this.id = id;
        this.productQuantity = quantity;
        mCursor = cursor;
    }

////    public CartItemAdapter(Context context,
//
////                           ArrayList title,
////                           ArrayList imageUrl1,
////                           ArrayList price,
////                           ArrayList initialPrice,
//                           ArrayList quantity) {
////        this.context = context;
////        this.id = id;
////        this.productTitle = title;
////        this.imageUrl = imageUrl1;
////        this.productPrice = price;
////        this.originalPrice = initialPrice;
////        this.productQuantity = quantity;
////
////    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String itemId = mCursor.getString(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_ITEM_ID));
        String title = mCursor.getString(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_TITLE));
        int price = mCursor.getInt(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_PRICE));
        int initialPrice = mCursor.getInt(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_ORIGINAL_PRICE));
        int quantity = mCursor.getInt(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_QUANTITY));

        String imageUrl = mCursor.getString(mCursor.getColumnIndex(ItemContractClass.CartItemDetails.COLUMN_IMAGE_URL));
        Picasso.get().load(imageUrl).into(holder.image);

        long id = mCursor.getLong(mCursor.getColumnIndex(ItemContractClass.CartItemDetails._ID));

        holder.itemId.setText(itemId);
        holder.title.setText(title);
        holder.price.setText(String.valueOf(price));
        holder.initialPrice.setText(String.valueOf(initialPrice));
        holder.quantity.setText(String.valueOf(quantity));
        holder.itemView.setTag(id);


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        TextView itemId, title, price, initialPrice, quantity;
        ImageView image;
        CartItemOpenHelper mDb = new CartItemOpenHelper(context);

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            itemId = itemView.findViewById(R.id.itemId);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            initialPrice = itemView.findViewById(R.id.original_price);
            quantity = itemView.findViewById(R.id.quantity);

        }
    }
}

//        public void updateDb(String row_id) {
//            CartItemOpenHelper dbHelper = new CartItemOpenHelper(context);
//            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(ItemContractClass.CartItemDetails.COLUMN_QUANTITY, String.valueOf(currentQty));
//            sqLiteDatabase.update(ItemContractClass.CartItemDetails.TABLE_NAME, contentValues, "_id=?", new String[]{row_id});
//
////            getQuantityTotals();
//////            getPriceTotals();
////        }
////
////    }

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

