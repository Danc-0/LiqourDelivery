package com.danc.winesapi.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CartItemOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CartItems.db";
    public static final int DATABASE_VERSION = 5;

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

    public Cursor calculateTotals(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(ItemContractClass.CartItemDetails.QUERY_TOTAL_VALUES, null);
        }
        return cursor;
    }
}
