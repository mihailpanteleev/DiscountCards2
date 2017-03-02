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
// названия таблиц
    static final String TBL_PERSON = "person";
    static final String TBL_CARD = "card";
    static final String TBL_CARD_LEVEL = "cardLevel";
    static final String TBL_BUY = "buy";
// названия колонок
    static final String COL_ID = "_id";
    static final String COL_NAME = "name";
    static final String COL_LAST_NAME = "lastName";
    static final String COL_BIRTH_DATE = "birth";
    static final String COL_DISCOUNT = "discount";
    static final String COL_FROM_SUM = "fromSum";
    static final String COL_TO_SUM = "toSum";
    static final String COL_PREFIX = "prefix";
    static final String COL_SERIAL = "serial";
    static final String COL_PERSON_ID = "personId";
    static final String COL_LEVEL_ID = "levelId";
    static final String COL_AMOUNT = "amount";
    static final String COL_PRICE = "price";
    static final String COL_SUM = "sum";
// алиасы
    static final String ALIAS_DATA = "data";
    private SQLiteDatabase db;


    MyDb(Context context) {
        MyDbHelper dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    long addPerson(String name, String lastName, String birthDate){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_BIRTH_DATE, birthDate);
        return db.insert(TBL_PERSON, null, values);
    }

    Cursor getFilteredNames(String lastName) {
        if ((lastName!=null)&&(!lastName.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, COL_LAST_NAME+"=?", new String[]{lastName}, COL_NAME, null, null, null);
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_NAME}, null, null, COL_NAME, null, null, null);
    }

    Cursor getFilteredLastNames(String name) {
        if ((name!=null)&&(!name.isEmpty()))
            return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, COL_NAME+"=?", new String[]{name}, COL_LAST_NAME, null, null, null);
        return db.query(false, TBL_PERSON, new String[]{COL_ID, COL_LAST_NAME}, null, null, COL_LAST_NAME, null, null, null);
    }

    Cursor getFilteredPersonData(String name, String lastName) {
        String select = "SELECT "+COL_ID+", printf(\"%s %s, %s\", "+COL_NAME+", "+COL_LAST_NAME+", "+COL_BIRTH_DATE+") as "+ALIAS_DATA+" FROM "+TBL_PERSON;
        if ((name!=null)&&(lastName!=null)&&(!name.isEmpty())&&(!lastName.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? AND "+COL_LAST_NAME+"=?";
            return db.rawQuery(_select, new String[]{name,lastName});
        }
        if ((name!=null)&&(!name.isEmpty())){
            String _select = select+" WHERE "+COL_NAME+"=? ";
            return db.rawQuery(_select, new String[]{name});
        }
        if ((lastName!=null)&&(!lastName.isEmpty())){
            String _select = select+" WHERE "+COL_LAST_NAME+"=?";
            return db.rawQuery(_select, new String[]{lastName});
        }
        return db.rawQuery(select, null);
    }
    String getPersonStringById(long id) {
        Cursor cursor = db.query(false, TBL_PERSON, new String[]{COL_NAME, COL_LAST_NAME, COL_BIRTH_DATE}, COL_ID+"=?", new String[]{Long.toString(id)}, null, null, null, null);
        String personData = "";
        if (cursor.moveToFirst()){
            int nameIdx = cursor.getColumnIndex(COL_NAME);
            int lastNameIdx = cursor.getColumnIndex(COL_LAST_NAME);
            int birthIdx = cursor.getColumnIndex(COL_BIRTH_DATE);
            personData = cursor.getString(nameIdx)+" "+cursor.getString(lastNameIdx)+", "+cursor.getString(birthIdx);
        }
        cursor.close();
        return personData;
    }
    Cursor getCardDataByPersonId(long personId) {
        return db.rawQuery("SELECT card._id, serial, levelId, name, discount, prefix FROM card JOIN cardLevel ON card.levelId=cardLevel._id WHERE card.personId=?", new String[]{Long.toString(personId)});
//        return db.query(false, TBL_CARD, new String[]{COL_ID, COL_SERIAL, COL_LEVEL_ID}, COL_PERSON_ID+"=?", new String[]{Long.toString(personId)}, null, null, null, null);
    }
    String getGrandTotal(long personId){
        String sum = "";
        Cursor cursor = db.rawQuery("SELECT sum(sum) as sum FROM buy WHERE personId=?",new String[]{Long.toString(personId)});
        if (cursor.moveToFirst()){
            int sumIdx = cursor.getColumnIndex("sum");
            sum = cursor.getString(sumIdx);
        }
        cursor.close();
        if (sum==null) sum="0";
        return sum;
    }
    Cursor getPurchaseListById(long personId){
//        SELECT _id, name, price, amount, sum FROM buy WHERE personId=1
        return db.query(false, TBL_BUY, new String[]{COL_ID, COL_NAME, COL_PRICE, COL_AMOUNT, COL_SUM}, COL_PERSON_ID+"=?", new String[]{Long.toString(personId)}, null, null, null, null);
    }
}
