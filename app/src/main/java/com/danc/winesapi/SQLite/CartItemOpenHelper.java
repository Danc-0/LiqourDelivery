package com.danc.winesapi.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CartItemOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CartItems.db";
    public static final int DATABASE_VERSION = 5;
    Context context;

    public CartItemOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ItemContractClass.CartItemDetails.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ItemContractClass.CartItemDetails.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public Cursor readAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(ItemContractClass.CartItemDetails.QUERY_FROM_DB, null);
        }
        return cursor;
    }

    public Cursor calculatePriceTotals(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(ItemContractClass.CartItemDetails.QUERY_TOTAL_VALUES, null);
        }
        return cursor;
    }

    public Cursor calculateQuantityTotals(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(ItemContractClass.CartItemDetails.QUERY_TOTAL_QUANTITY_VALUES, null);
        }
        return cursor;
    }

    public Cursor clearSQLite(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(ItemContractClass.CartItemDetails.CLEAR_DB, null);

        }
        return cursor;
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(ItemContractClass.CartItemDetails.TABLE_NAME, "_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
