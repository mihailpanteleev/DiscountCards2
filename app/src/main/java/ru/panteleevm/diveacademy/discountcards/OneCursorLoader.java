package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
import android.content.CursorLoader;

/**
 * Created by 7-64 on 20.02.2017.
 */

class OneCursorLoader extends CursorLoader {
    MyDb db;
    protected String nameFilter, lastNameFilter, birthDateFilter;

    void setFilters(String nameFilter, String lastNameFilter, String birthDateFilter){
        this.birthDateFilter = birthDateFilter;
        this.lastNameFilter = lastNameFilter;
        this.nameFilter = nameFilter;
    }

    OneCursorLoader(Context context, MyDb _db) {
        super(context);
        db = _db;
    }
}
