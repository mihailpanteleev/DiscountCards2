package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyDbHelper extends SQLiteOpenHelper{
    MyDbHelper(Context context) {
        super(context, "cards.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PERSON = "CREATE TABLE " + MyDb.TBL_PERSON + " ("+
                MyDb.COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MyDb.COL_NAME       + " TEXT (100), "+
                MyDb.COL_LAST_NAME  + " TEXT (100), "+
                MyDb.COL_BIRTH_DATE + " TEXT (10), "+
                "CONSTRAINT uk_person UNIQUE ("+
                    MyDb.COL_NAME       + ", "+
                    MyDb.COL_LAST_NAME  + ", "+
                    MyDb.COL_BIRTH_DATE +
                ")"+
        ");";
        db.execSQL(CREATE_PERSON);

        final String CREATE_CARD_LEVEL = "CREATE TABLE "+MyDb.TBL_CARD_LEVEL+" (" +
                MyDb.COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyDb.COL_NAME       + " TEXT (16) UNIQUE, " +
                MyDb.COL_DISCOUNT   + " DECIMAL (2, 2), " +
                MyDb.COL_FROM_SUM   + " DECIMAL (10, 2), " +
                MyDb.COL_TO_SUM     + " DECIMAL (10, 2), " +
                MyDb.COL_PREFIX     + " TEXT (3) UNIQUE" +
                ");";
        db.execSQL(CREATE_CARD_LEVEL);
        db.execSQL("INSERT INTO cardLevel (_id, name, discount, fromSum, toSum, prefix) VALUES (1, 'Yellow', 5, 0, 49999, 'YRM');");
        db.execSQL("INSERT INTO cardLevel (_id, name, discount, fromSum, toSum, prefix) VALUES (2, 'Silver', 10, 50000, 99999, 'SRM');");
        db.execSQL("INSERT INTO cardLevel (_id, name, discount, fromSum, toSum, prefix) VALUES (3, 'Gold', 15, 100000, 1000000, 'GRM');");

        final String CREATE_CARD = "CREATE TABLE "+MyDb.TBL_CARD+" (" +
                MyDb.COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyDb.COL_SERIAL     + " TEXT (10), " +
                MyDb.COL_PERSON_ID  + " INTEGER REFERENCES "+MyDb.TBL_PERSON+" ("+MyDb.COL_ID+"), " +
                MyDb.COL_LEVEL_ID   + " INT REFERENCES "+MyDb.TBL_CARD_LEVEL+" ("+MyDb.COL_ID+"), " +
                "CONSTRAINT uk_serial_and_level UNIQUE ("+
                MyDb.COL_SERIAL + ", " +
                MyDb.COL_LEVEL_ID +
                ")" +
        ");";
        db.execSQL(CREATE_CARD);

        final String CREATE_BUY = "CREATE TABLE "+MyDb.TBL_BUY+" (" +
                MyDb.COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyDb.COL_PERSON_ID  + " INTEGER REFERENCES "+MyDb.TBL_PERSON+" ("+MyDb.COL_ID+"), " +
                MyDb.COL_NAME       + " TEXT (127), " +
                MyDb.COL_AMOUNT     + " INTEGER, " +
                MyDb.COL_PRICE      + " DECIMAL (5, 2), " +
                MyDb.COL_SUM        + " DECIMAL (10, 2), " +
                MyDb.COL_DATE       + " TEXT (12)" +
        ");";
        db.execSQL(CREATE_BUY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*
        if ((oldVersion==1)&&(newVersion==2)){
        }
*/
    }
}
