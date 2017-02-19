package ru.panteleevm.diveacademy.discountcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by PanteleevM on 03.02.2017.
 */

public class MyDb {
    private SQLiteDatabase db;
//    private Context context;

    public static final String TBL_PERSON = "person";

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_LAST_NAME = "lastName";
    public static final String COL_BIRTH_DATE = "birth";
    public static final String ALIAS_DATA = "data";

    public MyDb(Context context) {
        MyDbHelper dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
//        this.context = context;
    }

    public long addPerson(String name, String lastName, String birthDate){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_BIRTH_DATE, birthDate);
        return db.insert(TBL_PERSON, null, values);
    }
    public Cursor getNames(){
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, null, null, COL_NAME, null, null, null);
    }
    public Cursor getLastNames(){
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, null, null, COL_LAST_NAME, null, null, null);
    }
    public Cursor getBirthDates(){
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_BIRTH_DATE}, null, null, COL_BIRTH_DATE, null, null, null);
    }
    public Cursor getPersonData(){
        return db.rawQuery("SELECT _id, printf(\"%s %s, %s\", "+COL_NAME+", "+COL_LAST_NAME+", "+COL_BIRTH_DATE+") as "+ALIAS_DATA+" FROM "+TBL_PERSON, null);
    }
}
