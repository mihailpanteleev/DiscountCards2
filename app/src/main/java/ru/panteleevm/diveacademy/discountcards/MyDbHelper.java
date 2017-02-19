package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper{
    public MyDbHelper(Context context) {
        super(context, "cards.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_person = "CREATE TABLE " + MyDb.TBL_PERSON + " ("+
                MyDb.COL_ID         + "      INTEGER    PRIMARY KEY AUTOINCREMENT,"+
                MyDb.COL_NAME       + "     TEXT (100),"+
                MyDb.COL_LAST_NAME  + " TEXT (100),"+
                MyDb.COL_BIRTH_DATE + "    TEXT (10),"+
                "CONSTRAINT uk_person UNIQUE ("+
                    MyDb.COL_NAME       + ","+
                    MyDb.COL_LAST_NAME  + ","+
                    MyDb.COL_BIRTH_DATE +
                ")"+
        ");";
        db.execSQL(create_person);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
