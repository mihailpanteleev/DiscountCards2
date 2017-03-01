package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
import android.content.CursorLoader;

/**
 * предок для всех загрузчиков из ActivityOne
 * в предка встроил статические поля для фильтра по имени-фамилии-году рождения
 * все потомки будут отбирать результаты по одному фильтру
 * Created by 7-64 on 20.02.2017.
 */

class OneCursorLoader extends CursorLoader {
    static String nameFilter, lastNameFilter;
    MyDb db;

    OneCursorLoader(Context context, MyDb _db) {
        super(context);
        db = _db;
    }

    static void setFilters(String name, String lastName) {
        lastNameFilter = lastName;
        nameFilter = name;
    }
}
