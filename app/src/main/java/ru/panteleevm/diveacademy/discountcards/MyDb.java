package ru.panteleevm.diveacademy.discountcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Код для работы с базой данных программы - SQLite
 * Created by PanteleevM on 03.02.2017.
 */

class MyDb {
    private SQLiteDatabase db;
//    private Context context;

    static final String TBL_PERSON = "person";

    static final String COL_ID = "_id";
    static final String COL_NAME = "name";
    static final String COL_LAST_NAME = "lastName";
    static final String COL_BIRTH_DATE = "birth";
    static final String ALIAS_DATA = "data";

    MyDb(Context context) {
        MyDbHelper dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
//        this.context = context;
    }

    long addPerson(String name, String lastName, String birthDate){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_BIRTH_DATE, birthDate);
        return db.insert(TBL_PERSON, null, values);
    }
    Cursor getFilteredNames(String lastName, String birthDate){
        if ((lastName!=null)&&(birthDate!=null)&&(!lastName.isEmpty())&&(!birthDate.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, COL_LAST_NAME+"=? AND "+COL_BIRTH_DATE+"=?", new String[]{lastName,birthDate}, COL_NAME, null, null, null);
        if ((lastName!=null)&&(!lastName.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, COL_LAST_NAME+"=?", new String[]{lastName}, COL_NAME, null, null, null);
        if ((birthDate!=null)&&(!birthDate.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, COL_BIRTH_DATE+"=?", new String[]{birthDate}, COL_NAME, null, null, null);
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, null, null, COL_NAME, null, null, null);
    }
    Cursor getFilteredLastNames(String name, String birthDate){
        if ((name!=null)&&(birthDate!=null)&&(!name.isEmpty())&&(!birthDate.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, COL_NAME+"=? AND "+COL_BIRTH_DATE+"=?", new String[]{name,birthDate}, COL_LAST_NAME, null, null, null);
        if ((name!=null)&&(!name.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, COL_NAME+"=?", new String[]{name}, COL_LAST_NAME, null, null, null);
        if ((birthDate!=null)&&(!birthDate.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, COL_BIRTH_DATE+"=?", new String[]{birthDate}, COL_LAST_NAME, null, null, null);
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, null, null, COL_LAST_NAME, null, null, null);
    }
    Cursor getFilteredBirthDates(String name, String lastName){
        if ((name!=null)&&(lastName!=null)&&(!name.isEmpty())&&(!lastName.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_BIRTH_DATE}, COL_NAME+"=? AND "+COL_LAST_NAME+"=?", new String[]{name,lastName}, COL_BIRTH_DATE, null, null, null);
        if ((name!=null)&&(!name.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_BIRTH_DATE}, COL_NAME+"=?", new String[]{name}, COL_BIRTH_DATE, null, null, null);
        if ((lastName!=null)&&(!lastName.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_BIRTH_DATE}, COL_LAST_NAME+"=?", new String[]{lastName}, COL_BIRTH_DATE, null, null, null);
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_BIRTH_DATE}, null, null, COL_BIRTH_DATE, null, null, null);
    }
    Cursor getFilteredPersonData(String name, String lastName, String birthDate){
        String select = "SELECT "+COL_ID+", printf(\"%s %s, %s\", "+COL_NAME+", "+COL_LAST_NAME+", "+COL_BIRTH_DATE+") as "+ALIAS_DATA+" FROM "+TBL_PERSON;
        if ((name!=null)&&(lastName!=null)&&(birthDate!=null)&&(!name.isEmpty())&&(!lastName.isEmpty())&&(!birthDate.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? AND "+COL_LAST_NAME+"=? AND "+COL_BIRTH_DATE+"=?";
            return db.rawQuery(_select, new String[]{name,lastName,birthDate});
        }
        if ((name!=null)&&(lastName!=null)&&(!name.isEmpty())&&(!lastName.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? AND "+COL_LAST_NAME+"=?";
            return db.rawQuery(_select, new String[]{name,lastName});
        }
        if ((name!=null)&&(birthDate!=null)&&(!name.isEmpty())&&(!birthDate.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? AND "+COL_BIRTH_DATE+"=?";
            return db.rawQuery(_select, new String[]{name,birthDate});
        }
        if ((lastName!=null)&&(birthDate!=null)&&(!lastName.isEmpty())&&(!birthDate.isEmpty())){
            String _select = select+" WHERE "+COL_LAST_NAME+"=? AND "+COL_BIRTH_DATE+"=?";
            return db.rawQuery(_select, new String[]{lastName,birthDate});
        }
        if ((name!=null)&&(!name.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? ";
            return db.rawQuery(_select, new String[]{name});
        }
        if ((lastName!=null)&&(!lastName.isEmpty())){
            String _select = select+" WHERE "+COL_LAST_NAME+"=?";
            return db.rawQuery(_select, new String[]{lastName});
        }
        if ((birthDate!=null)&&(!birthDate.isEmpty())){
            String _select = select+" WHERE "+COL_BIRTH_DATE+"=?";
            return db.rawQuery(_select, new String[]{birthDate});
        }
        return db.rawQuery(select, null);
    }
}
