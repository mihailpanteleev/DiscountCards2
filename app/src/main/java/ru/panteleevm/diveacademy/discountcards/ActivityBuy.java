package ru.panteleevm.diveacademy.discountcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityBuy extends AppCompatActivity {
//    final static String EXTRA_BUY_RESULT = "ru.panteleevm.diveacademy.discountcards.EXTRA_BUY_RESULT";
    private MyDb myDb;
    private long personId;
    private int discountValue;
    private int price = 0;
    private int quantity = 1;

    private TextView personData;
    private TextView discount;
    private TextView discounted;

    private EditText nameEdit;
    private EditText priceEdit;
    private EditText quantityEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new MyDb(this);
        initViews();
        fillViews();
        setEditBehavior();

    }

    private void setEditBehavior() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String priceText = priceEdit.getText().toString();
                if (!priceText.isEmpty())
                    price = Integer.parseInt(priceText);
                else
                    price = 0;
                String quantityText = quantityEdit.getText().toString();
                if (!quantityText.isEmpty())
                    quantity = Integer.parseInt(quantityText);
                else
                    quantity = 0;

                if (price!=0 && quantity != 0) {
                    int newPrice =  price * quantity * (100-discountValue)/100;
                    discounted.setText(String.valueOf(newPrice));
                }
            }
        };
        priceEdit.addTextChangedListener(watcher);
        quantityEdit.addTextChangedListener(watcher);
    }

    private void fillViews() {
        Intent intent = getIntent();
        personId = intent.getLongExtra(ActivityOne.EXTRA_PERSON_ID, -1);
        personData.setText(myDb.getPersonStringById(personId));
        discountValue = intent.getIntExtra(ActivityDiverActions.EXTRA_DISCOUNT_VALUE, 0);
        discount.setText(String.valueOf(discountValue));
    }

    private void initViews() {
        personData =    (TextView)findViewById(R.id.tv_person_data);
        discount =      (TextView)findViewById(R.id.tv_discount);
        discounted =    (TextView)findViewById(R.id.tv_discounted);

        nameEdit =      (EditText)findViewById(R.id.et_name);
        priceEdit =     (EditText)findViewById(R.id.et_price);
        quantityEdit =  (EditText)findViewById(R.id.et_quantity);
    }
    public void processBuy(View view){
        String name = nameEdit.getText().toString();
        String priceText = priceEdit.getText().toString();
        if (!priceText.isEmpty())
            price = Integer.parseInt(priceText);
        else
            price = 0;
        String quantityText = quantityEdit.getText().toString();
        if (!quantityText.isEmpty())
            quantity = Integer.parseInt(quantityText);
        else
            quantity = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());

        if (!name.isEmpty() && price!=0 && quantity!=0){
            int newPrice =  price * quantity * (100-discountValue)/100;
            Boolean result = -1 != myDb.addBuy(personId, name, quantity, price, newPrice, dateString);
            Intent intent = new Intent();
            setResult(result ? RESULT_OK : RESULT_CANCELED, intent);
            finish();
        }
    }
}
