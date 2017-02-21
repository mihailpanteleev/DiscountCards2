package ru.panteleevm.diveacademy.discountcards;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class ActivityOne extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private MyDb myDb;
    private CheckBox filterByName, filterByLastName, filterByBirth;
    private Spinner names, lastNames, birthDates;
    private SimpleCursorAdapter namesAdapter, lastNamesAdapter, birthDatesAdapter, personsAdapter;
    private LoaderManager loaderManager;
    private ListView personsListView;
    private String nameFilter, lastNameFilter, birthDayFilter;

    final int NAMES_CURSOR_LOADER = 0;
    final int LAST_NAMES_CURSOR_LOADER = 1;
    final int BIRTH_DATES_CURSOR_LOADER = 2;
    final int PERSON_DATA_CURSOR_LOADER = 3;

    private NamesCursorLoader namesCursorLoader;
    private LastNamesCursorLoader lastNamesCursorLoader;
    private BirthdaysCursorLoader birthdaysCursorLoader;
    private PersonDataCursorLoader personDataCursorLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        myDb = new MyDb(this);
        loaderManager = getLoaderManager();
        initViews();
        fillSpinners();
    }

    private void initViews() {
        filterByName = (CheckBox)findViewById(R.id.cb_filter_by_name);
        filterByLastName = (CheckBox)findViewById(R.id.cb_filter_by_last_name);
        filterByBirth = (CheckBox)findViewById(R.id.cb_filter_by_birth);
        names = (Spinner)findViewById(R.id.sp_name);
        lastNames = (Spinner)findViewById(R.id.sp_last_name);
        birthDates = (Spinner)findViewById(R.id.sp_birth_date);
        personsListView = (ListView)findViewById(R.id.lv_persons);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/

    public void onClickFilter(View view){
        if (filterByName.isChecked()){
            Object o = names.getSelectedItem();
            if (o instanceof Cursor) {
                Cursor cursor = (Cursor)o;
                int columnIndex = cursor.getColumnIndex(MyDb.COL_NAME);
                nameFilter = cursor.getString(columnIndex);
            }
        } else {
            nameFilter = null;
        }
        if (filterByLastName.isChecked()){
            Object o = lastNames.getSelectedItem();
            if (o instanceof Cursor) {
                Cursor cursor = (Cursor)o;
                int columnIndex = cursor.getColumnIndex(MyDb.COL_LAST_NAME);
                lastNameFilter = cursor.getString(columnIndex);
            }
        } else {
            lastNameFilter = null;
        }
        if (filterByBirth.isChecked()){
            Object o = birthDates.getSelectedItem();
            if (o instanceof Cursor) {
                Cursor cursor = (Cursor)o;
                int columnIndex = cursor.getColumnIndex(MyDb.COL_BIRTH_DATE);
                birthDayFilter = cursor.getString(columnIndex);
            }
        } else {
            birthDayFilter = null;
        }
        OneCursorLoader.setFilters(nameFilter, lastNameFilter, birthDayFilter);
        namesCursorLoader.forceLoad();
        lastNamesCursorLoader.forceLoad();
        birthdaysCursorLoader.forceLoad();
        personDataCursorLoader.forceLoad();
    }
    public void onClickAddPerson(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_person, null);
        builder.setView(v);
        final EditText editName = (EditText)v.findViewById(R.id.et_name);
        final EditText editLastName = (EditText)v.findViewById(R.id.et_last_name);
        final EditText editBirthYear = (EditText)v.findViewById(R.id.et_birth);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                String lastName = editLastName.getText().toString();
                String birthYear = editBirthYear.getText().toString();
                if (!name.isEmpty()&&!lastName.isEmpty()&&!birthYear.isEmpty()){
                    myDb.addPerson(name, lastName, birthYear);
                    loaderManager.getLoader(NAMES_CURSOR_LOADER).forceLoad();
                    loaderManager.getLoader(LAST_NAMES_CURSOR_LOADER).forceLoad();
                    loaderManager.getLoader(BIRTH_DATES_CURSOR_LOADER).forceLoad();
                    loaderManager.getLoader(PERSON_DATA_CURSOR_LOADER).forceLoad();
                }
            }
        });
        builder.create();
        builder.show();
    }

    private void fillSpinners() {

        int[] toTextView = new int[]{R.id.text};

        namesAdapter = new SimpleCursorAdapter(this, R.layout.text_item, null, new String[]{MyDb.COL_NAME}, toTextView, 0);
        names.setAdapter(namesAdapter);

        lastNamesAdapter = new SimpleCursorAdapter(this, R.layout.text_item, null, new String[]{MyDb.COL_LAST_NAME}, toTextView, 0);
        lastNames.setAdapter(lastNamesAdapter);

        birthDatesAdapter = new SimpleCursorAdapter(this, R.layout.text_item, null, new String[]{MyDb.COL_BIRTH_DATE}, toTextView, 0);
        birthDates.setAdapter(birthDatesAdapter);

        personsAdapter = new SimpleCursorAdapter(this, R.layout.text_item, null, new String[]{MyDb.ALIAS_DATA}, toTextView, 0);
        personsListView.setAdapter(personsAdapter);

        loaderManager.initLoader(NAMES_CURSOR_LOADER, null, this);
        loaderManager.initLoader(LAST_NAMES_CURSOR_LOADER, null, this);
        loaderManager.initLoader(BIRTH_DATES_CURSOR_LOADER, null, this);
        loaderManager.initLoader(PERSON_DATA_CURSOR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NAMES_CURSOR_LOADER:
                namesCursorLoader = new NamesCursorLoader(this, myDb);
                return namesCursorLoader;
            case LAST_NAMES_CURSOR_LOADER:
                lastNamesCursorLoader = new LastNamesCursorLoader(this, myDb);
                return lastNamesCursorLoader;
            case BIRTH_DATES_CURSOR_LOADER:
                birthdaysCursorLoader = new BirthdaysCursorLoader(this, myDb);
                return birthdaysCursorLoader;
            case PERSON_DATA_CURSOR_LOADER:
                personDataCursorLoader = new PersonDataCursorLoader(this, myDb);
                return personDataCursorLoader;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
                case NAMES_CURSOR_LOADER:
                    namesAdapter.swapCursor(data);
                    break;
                case LAST_NAMES_CURSOR_LOADER:
                    lastNamesAdapter.swapCursor(data);
                    break;
                case BIRTH_DATES_CURSOR_LOADER:
                    birthDatesAdapter.swapCursor(data);
                    break;
                case PERSON_DATA_CURSOR_LOADER:
                    personsAdapter.swapCursor(data);
                    break;
                default:
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class NamesCursorLoader extends OneCursorLoader {
        NamesCursorLoader(Context context, MyDb _db) {
            super(context, _db);
        }
        @Override
        public Cursor loadInBackground() {
            return db.getFilteredNames(lastNameFilter, birthDateFilter);
        }
    }
    static class LastNamesCursorLoader extends OneCursorLoader {
        LastNamesCursorLoader(Context context, MyDb _db) {
            super(context, _db);
        }
        @Override
        public Cursor loadInBackground() {
            return db.getFilteredLastNames(nameFilter, birthDateFilter);
        }
    }
    static class BirthdaysCursorLoader extends OneCursorLoader {
        BirthdaysCursorLoader(Context context, MyDb _db) {
            super(context, _db);
            db = _db;
        }
        @Override
        public Cursor loadInBackground() {
            return db.getFilteredBirthDates(nameFilter, lastNameFilter);
        }
    }
    static class PersonDataCursorLoader extends OneCursorLoader {
        PersonDataCursorLoader(Context context, MyDb _db) {
            super(context, _db);
        }
        @Override
        public Cursor loadInBackground() {
            return db.getFilteredPersonData(nameFilter, lastNameFilter, birthDateFilter);
        }
    }
}
