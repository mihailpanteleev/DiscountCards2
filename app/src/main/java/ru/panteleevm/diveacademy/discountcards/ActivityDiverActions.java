package ru.panteleevm.diveacademy.discountcards;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class ActivityDiverActions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    final String NO_CARD = "No card";
    private MyDb myDb;
    private long personId;
    private long cardId;
    private long cardLevel;

    private int discount_;
    private int sum_;

    private TextView personData;
    private TextView cardSerial;
    private TextView discount;
    private TextView sum;
    private ListView purchaseList;
    private SimpleCursorAdapter purchaseAdapter;
    private LoaderManager loaderManager;
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
        Cursor cursor;
        cursor = myDb.getCardDataByPersonId(personId);
        if (cursor.moveToFirst()){
            int idIdx = cursor.getColumnIndex(MyDb.COL_ID);
            int serialIdx = cursor.getColumnIndex(MyDb.COL_SERIAL);
            int levelIdx = cursor.getColumnIndex(MyDb.COL_LEVEL_ID);
//            int levelNameIdx = cursor.getColumnIndex(MyDb.COL_NAME);
            int discountIdx = cursor.getColumnIndex(MyDb.COL_DISCOUNT);
            int prefixIdx = cursor.getColumnIndex(MyDb.COL_PREFIX);

            cardId = cursor.getLong(idIdx);
            String ser = cursor.getString(prefixIdx)+cursor.getString(serialIdx);
            cardSerial.setText(ser);
            cardLevel = cursor.getLong(levelIdx);
            discount.setText(cursor.getString(discountIdx));
            discount_ = cursor.getInt(discountIdx);
        } else {
            cardSerial.setText(NO_CARD);
            discount.setText(NO_CARD);
        }
        cursor.close();
        sum.setText(myDb.getGrandTotal(personId));

        String[] from = new String[]{MyDb.COL_NAME, MyDb.COL_PRICE, MyDb.COL_AMOUNT, MyDb.COL_SUM};
        int[] to = new int[]{R.id.name, R.id.price, R.id.amount, R.id.sum};
        purchaseAdapter = new SimpleCursorAdapter(this,R.layout.purchase_list_item, null, from, to, 0);
        purchaseList.setAdapter(purchaseAdapter);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    private void initViews() {
        personData =    (TextView)findViewById(R.id.tv_person_data);
        cardSerial =    (TextView)findViewById(R.id.tv_card_serial);
        discount =      (TextView)findViewById(R.id.tv_discount);
        sum =           (TextView)findViewById(R.id.tv_grand_total);
        purchaseList =  (ListView)findViewById(R.id.lv_purchase_list);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    static class PurchaseCursorLoader extends CursorLoader {
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
