package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


public class ActivityDiverActions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    final static String EXTRA_DISCOUNT_VALUE = "ru.panteleevm.diveacademy.discountcards.discount_value";

    private MyDb myDb;
    private long personId;
    private long cardId;
    private int cardLevel;
    private String cardSerialNumber = "";

    private int discount_;

    private TextView personData;
    private TextView cardSerialPrefix;
    private TextView cardSerial;
    private TextView discount;
    private TextView sum;
    private ListView purchaseList;
    private SimpleCursorAdapter purchaseAdapter;
    private PurchaseCursorLoader purchaseCursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diver_actions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDb = new MyDb(this);
        initViews();
        fillViews();
    }

    private void fillViews() {
        Intent intent = getIntent();
        personId = intent.getLongExtra(ActivityOne.EXTRA_PERSON_ID, -1);
        personData.setText(myDb.getPersonStringById(personId));
        fillCardData();
        fillGrandTotal();

        View headerPurchaseList = getLayoutInflater().inflate(R.layout.header_purchase_list, null);
        purchaseList.addHeaderView(headerPurchaseList, null, false);

        String[] from = new String[]{MyDb.COL_NAME, MyDb.COL_PRICE, MyDb.COL_AMOUNT, MyDb.COL_SUM};
        int[] to = new int[]{R.id.name, R.id.price, R.id.amount, R.id.sum};
        purchaseAdapter = new SimpleCursorAdapter(this,R.layout.purchase_list_item, null, from, to, 0);
        purchaseList.setAdapter(purchaseAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void fillGrandTotal() {
        sum.setText(myDb.getGrandTotal(personId));
    }

    private void fillCardData() {
        Cursor cursor = myDb.getCardDataByPersonId(personId);
        if (cursor.moveToFirst()){
            int idIdx = cursor.getColumnIndex(MyDb.COL_ID);
            int serialIdx = cursor.getColumnIndex(MyDb.COL_SERIAL);
            int levelIdx = cursor.getColumnIndex(MyDb.COL_LEVEL_ID);
//            int levelNameIdx = cursor.getColumnIndex(MyDb.COL_NAME);
            int discountIdx = cursor.getColumnIndex(MyDb.COL_DISCOUNT);
            int prefixIdx = cursor.getColumnIndex(MyDb.COL_PREFIX);

            cardId = cursor.getLong(idIdx);
            cardSerialPrefix.setText(cursor.getString(prefixIdx));
            cardSerialNumber = cursor.getString(serialIdx);
            cardSerial.setText(cardSerialNumber);
            cardLevel = cursor.getInt(levelIdx);
            discount.setText(cursor.getString(discountIdx));
            discount_ = cursor.getInt(discountIdx);
        } else {
            cardSerial.setText(R.string.no_card);
            discount.setText(R.string.no_card);
        }
        cursor.close();
    }

    private void initViews() {
        personData = (TextView) findViewById(R.id.tv_person_data);
        cardSerialPrefix = (TextView) findViewById(R.id.tv_prefix);
        cardSerial = (TextView) findViewById(R.id.tv_card_serial);
        discount = (TextView) findViewById(R.id.tv_discount);
        sum = (TextView) findViewById(R.id.tv_grand_total);
        purchaseList = (ListView) findViewById(R.id.lv_purchase_list);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_diver_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_buy) {
            buy();
        } else if (id == R.id.nav_edit_card) {
            editCard();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            purchaseCursorLoader.forceLoad();
            fillGrandTotal();
        }
    }

    private void buy() {
        Intent intent = new Intent(this, ActivityBuy.class);
        intent.putExtra(ActivityOne.EXTRA_PERSON_ID, personId);
        intent.putExtra(EXTRA_DISCOUNT_VALUE, discount_);
        startActivityForResult(intent, 0);
    }

    private void editCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_edit_card, null);
        builder.setView(v);
        final EditText etSerial = (EditText)v.findViewById(R.id.et_serial);
        final RadioButton rbGold = (RadioButton)v.findViewById(R.id.rb_gold);
        final RadioButton rbSilver = (RadioButton)v.findViewById(R.id.rb_silver);
        final RadioButton rbYellow = (RadioButton)v.findViewById(R.id.rb_yellow);

        if (!cardSerialNumber.isEmpty())
            etSerial.setText(cardSerialNumber);
        switch (cardLevel) {
            case 1:
                rbYellow.setChecked(true);
                break;
            case 2:
                rbSilver.setChecked(true);
                break;
            case 3:
                rbGold.setChecked(true);
                break;
            default:
        }

        builder.setNegativeButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String serial = etSerial.getText().toString();
                int level = 0;
                if (rbYellow.isChecked()) level = 1;
                else if (rbSilver.isChecked()) level = 2;
                else if (rbGold.isChecked()) level = 3;
                if (!serial.isEmpty() && level!=0){
                    if (cardId != 0) {
                        if (myDb.updateCard(serial, level, cardId))
                            fillCardData();
                        return;
                    }
                    long id = myDb.addCard(serial, personId, level);
                    if (id!=-1){
                        fillCardData();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        purchaseCursorLoader = new PurchaseCursorLoader(this, myDb, personId);
        return purchaseCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        purchaseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        purchaseAdapter.swapCursor(null);
    }

    private static class PurchaseCursorLoader extends CursorLoader {
        MyDb db;
        long personId;

        PurchaseCursorLoader(Context context, MyDb db, long personId) {
            super(context);
            this.db = db;
            this.personId = personId;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getPurchaseListById(personId);
        }
    }

}
