package com.example.criptabit;

import Connect.CommonUtils;
import Connect.Constants;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Stockmarket extends AppCompatActivity {

    Button mButton;
    TextInputLayout mEdit;
    private String query;
    ListView stockListView;
    StockAdapter mAdapter;
    ArrayList<StockItem> stockList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove status bar
        setContentView(R.layout.activity_stockmarket);

        mButton = (Button) findViewById(R.id.searchbutton);
        mEdit = findViewById(R.id.searchtext);
        stockListView = findViewById(R.id.listview);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Stock Market");

        new Stockmarket.AsyncTaskImpl().execute("market/v2/get-quotes?region=US&symbols=" +
                "AMD%2CIBM%2CAAPL%2CAMZN%2CAXP%2CDIS%2CMSFT%2CLUV%2CA%2CT%2CGLD%2CAMC%2CALL%2CADPT%2CACB", null);

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        query = mEdit.getEditText().getText().toString();
                        query = query.toUpperCase();
                        mEdit.getEditText().setText("");
                        if (!query.equals(""))
                            new Stockmarket.GetSearchAsyncTask().execute("market/v2/get-quotes?region=US&symbols=" + query, null);

                    }
                });

        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //<-------------New code------------->
                String symbol = mAdapter.getItem(i).getSymbol();
                String name = mAdapter.getItem(i).getStockname();
                String price = mAdapter.getItem(i).getPrice();
                String volume = mAdapter.getItem(i).getVolume();
                String marketcap = mAdapter.getItem(i).getMarketCap();
                String percentage = mAdapter.getItem(i).getPercentage();
                String range = mAdapter.getItem(i).getRange();

                Intent intent = new Intent(Stockmarket.this, StockChart.class);
                intent.putExtra("Symbol", symbol);
                intent.putExtra("Name", name);
                intent.putExtra("Price", price);
                intent.putExtra("Volume", volume);
                intent.putExtra("MarketCap", marketcap);
                intent.putExtra("Percentage", percentage);
                intent.putExtra("Range", range);

                //<------------ends here--------------------->

                startActivity(intent);

            }
        });

    }

    private class AsyncTaskImpl extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtils.checkInternetStatus(Stockmarket.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtils.executeWebRequest(reqUrl, reqBody);
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), Constants.ERROR_MSG, Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "Error";
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), Constants.NO_INTERNET, Snackbar.LENGTH_LONG).show();
                return "Error";
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            kProgressHUD = KProgressHUD.create(Stockmarket.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();

            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject objec = obj.getJSONObject("quoteResponse");
                    JSONArray arr = objec.getJSONArray("result");
                    stockList = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject currentObj = arr.getJSONObject(i);
                        StockItem stockItem = new StockItem();
                        stockItem.Symbol = currentObj.getString("symbol");
                        stockItem.Stockname = currentObj.getString("shortName");
                        stockItem.MarketCap = currentObj.getString("marketCap");
                        stockItem.Price = currentObj.getString("regularMarketPrice");
                        stockItem.Volume = currentObj.getString("regularMarketVolume");
                        stockItem.Range = currentObj.getString("regularMarketDayRange");
                        stockItem.Percentage = currentObj.getString("regularMarketChangePercent");
                        stockList.add(stockItem);

                    }
                    mAdapter = new StockAdapter(getApplicationContext(), stockList);
                    stockListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class GetSearchAsyncTask extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtils.checkInternetStatus(Stockmarket.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtils.executeWebRequest(reqUrl, reqBody);
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), Constants.ERROR_MSG, Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                    return "Error";
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), Constants.NO_INTERNET, Snackbar.LENGTH_LONG).show();
                return "Error";
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            kProgressHUD = KProgressHUD.create(Stockmarket.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();

            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject objec = obj.getJSONObject("quoteResponse");
                    JSONArray arr = objec.getJSONArray("result");
                    stockList = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject currentObj = arr.getJSONObject(i);
                        StockItem stockItem = new StockItem();
                        stockItem.Symbol = currentObj.getString("symbol");
                        stockItem.Stockname = currentObj.getString("shortName");
                        stockItem.MarketCap = currentObj.getString("marketCap");
                        stockItem.Price = currentObj.getString("regularMarketPrice");
                        stockItem.Volume = currentObj.getString("regularMarketVolume");
                        stockItem.Range = currentObj.getString("regularMarketDayRange");
                        stockItem.Percentage = currentObj.getString("regularMarketChangePercent");
                        stockList.add(stockItem);

                    }
                    mAdapter = new StockAdapter(getApplicationContext(), stockList);
                    stockListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}