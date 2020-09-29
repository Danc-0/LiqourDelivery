package com.danc.winesapi.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ItemContractClass {

    private ItemContractClass(){}

    public class CartItemDetails implements BaseColumns{
        public static final String TABLE_NAME = "Cart_Item_Table";
        public static final String COLUMN_IMAGE_URL = "imageUrls";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ITEM_ID = "itemId";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_ORIGINAL_PRICE = "originalPrice";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_EACH_TOTAL = "each_total";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + CartItemDetails.TABLE_NAME + " (" +
                        CartItemDetails._ID + " INTEGER PRIMARY KEY, " +
                        CartItemDetails.COLUMN_ITEM_ID + " TEXT, " +
                        CartItemDetails.COLUMN_TITLE + " TEXT," +
                        CartItemDetails.COLUMN_IMAGE_URL + " TEXT," +
                        CartItemDetails.COLUMN_PRICE + " INTEGER," +
                        CartItemDetails.COLUMN_ORIGINAL_PRICE + " INTEGER," +
                        CartItemDetails.COLUMN_QUANTITY + " INTEGER," +
                        CartItemDetails.COLUMN_EACH_TOTAL + " INTEGER)";


        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CartItemDetails.TABLE_NAME;

        public static final String QUERY_FROM_DB =
                "SELECT * FROM " + CartItemDetails.TABLE_NAME;

        public static final String QUERY_TOTAL_VALUES =
                "SELECT SUM (" + CartItemDetails.COLUMN_EACH_TOTAL + ") as Total FROM " + CartItemDetails.TABLE_NAME;

        public static final String QUERY_TOTAL_QUANTITY_VALUES =
                "SELECT SUM (" + CartItemDetails.COLUMN_QUANTITY + ") as Total FROM " + CartItemDetails.TABLE_NAME;

        public static final String CLEAR_DB =
                "DELETE * FROM " + CartItemDetails.TABLE_NAME;


    }
}
