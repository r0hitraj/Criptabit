package com.example.criptabit;

import Connect.CommonUtilsCoins;
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

import com.example.criptabit.StockItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cryptomarket extends AppCompatActivity {

    Button mButton;
    TextInputLayout mEdit;
    private String query;
    ListView CoinListView;
    ArrayList<CoinItem> CoinList;
    CoinAdapter mAdapter;
    String ALLSYMBOLS = "BTC,BCH,BSV,ETH,LTC,DOGE,DASH,XRP,XMR,ADA,ZEC,XIN,XTZ";
    String sym,m1hr,m24hr,m1day,price,highday,lowday,highhr,lowhr,high24hr,low24hr,totalsupply,avalsupply,marketcap,vol,marketsource;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove status bar

        setContentView(R.layout.activity_cryptomarket);

        mButton = (Button) findViewById(R.id.searchbutton);
        mEdit = findViewById(R.id.searchtext);
        CoinListView = findViewById(R.id.listview);



        new Cryptomarket.AsyncTaskImpl().execute("pricemultifull?fsyms=" + ALLSYMBOLS +
                "&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&tsyms=USD", null);


        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        query = mEdit.getEditText().getText().toString();
                        query = query.toUpperCase();
                        mEdit.getEditText().setText("");
                        if (!query.equals(""))
                            new Cryptomarket.GetSearchAsyncTask().execute("pricemultifull?fsyms=" + query +
                                    "&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&tsyms=USD", null);

                    }
                });
        CoinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 sym = mAdapter.getItem(i).getSymbol();
                m1hr=mAdapter.getItem(i).getM1hr();
                m24hr=mAdapter.getItem(i).getM24hr();
                m1day=mAdapter.getItem(i).getM1day();
                price=mAdapter.getItem(i).getPriceUsd();
                highday=mAdapter.getItem(i).getHighprice1day();
                lowday=mAdapter.getItem(i).getLowprice1day();
                highhr=mAdapter.getItem(i).getHighprice1hr();
                lowhr=mAdapter.getItem(i).getLowprice1hr();
                high24hr=mAdapter.getItem(i).getHighhprice24hr();
                low24hr=mAdapter.getItem(i).getLowprice24hr();
                totalsupply=mAdapter.getItem(i).getTotalSupply();
                avalsupply=mAdapter.getItem(i).getAvaliableSupply();
                marketcap=mAdapter.getItem(i).getMarketCap();
                vol=mAdapter.getItem(i).getVolume();
                marketsource=mAdapter.getItem(i).getMarketSource();


                Intent intent = new Intent(Cryptomarket.this, CryptoChart.class);
                intent.putExtra("Symb", sym);
                intent.putExtra("M1hr", m1hr);
                intent.putExtra("M24hr", m24hr);
                intent.putExtra("M1day", m1day);
                intent.putExtra("Price", price);
                intent.putExtra("High1day", highday);
                intent.putExtra("Low1day", lowday);
                intent.putExtra("High24hr", high24hr);
                intent.putExtra("Low24hr", low24hr);
                intent.putExtra("High1hr", highhr);
                intent.putExtra("Low1hr", lowhr);
                intent.putExtra("Totalsupply", totalsupply);
                intent.putExtra("Availsupply", avalsupply);
                intent.putExtra("Marketcap", marketcap);
                intent.putExtra("Vol", vol);
                intent.putExtra("Source",marketsource);

                startActivity(intent);

            }
        });
    }

    private class GetSearchAsyncTask extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtilsCoins.checkInternetStatus(Cryptomarket.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtilsCoins.executeWebRequest(reqUrl, reqBody);
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

            kProgressHUD = KProgressHUD.create(Cryptomarket.this)
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
                    JSONObject display = obj.getJSONObject("DISPLAY");

                    JSONObject raw = obj.getJSONObject("RAW");

                    CoinList = new ArrayList<>();
                    for (int i = 0; i < 1; i++) {
                        JSONObject sym1 = raw.getJSONObject(query); //extra added line for reetriving symbol in char
                        JSONObject currentObj1 = sym1.getJSONObject("USD"); //added extra line for retrival of symbol in char\

                        JSONObject sym = display.getJSONObject(query);
                        JSONObject currentObj = sym.getJSONObject("USD");

                        CoinItem coinItem = new CoinItem();
                        coinItem.symbol = currentObj1.getString("FROMSYMBOL");
                        coinItem.marketCap = currentObj.getString("MKTCAP");
                        coinItem.priceUsd = currentObj.getString("PRICE");
                        coinItem.volume = currentObj.getString("SUPPLY");
                        coinItem.m1hr = currentObj.getString("CHANGEPCTHOUR");
                        coinItem.m24hr = currentObj.getString("CHANGEPCT24HOUR");
                        coinItem.m1day= currentObj.getString("CHANGEPCTDAY");
                        coinItem.url = currentObj.getString("IMAGEURL");
                        coinItem.time = currentObj.getString("LASTUPDATE");
                        coinItem.avaliableSupply = currentObj.getString("SUPPLY");
                        coinItem.totalSupply = currentObj.getString("TOTALVOLUME24H");
                        coinItem.highhprice24hr = currentObj.getString("HIGH24HOUR");
                        coinItem.lowprice24hr = currentObj.getString("LOW24HOUR");
                        coinItem.highprice1day = currentObj.getString("HIGHDAY");
                        coinItem.lowprice1day = currentObj.getString("LOWDAY");
                        coinItem.highprice1hr = currentObj.getString("HIGHHOUR");
                        coinItem.lowprice1hr = currentObj.getString("LOWHOUR");
                        coinItem.marketSource=currentObj.getString("MARKET");
                        CoinList.add(coinItem);
                    }
                    mAdapter = new CoinAdapter(getApplicationContext(), CoinList);
                    CoinListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class AsyncTaskImpl extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtilsCoins.checkInternetStatus(Cryptomarket.this)) {
                try {

                    String reqUrl = args[0];
                    String reqBody = args[1];

                    return CommonUtilsCoins.executeWebRequest(reqUrl, reqBody);
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

            kProgressHUD = KProgressHUD.create(Cryptomarket.this)
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

            String symbol_arr[] = {"BTC", "BCH", "BSV", "ETH", "LTC", "DOGE", "DASH", "XRP", "XMR", "ADA", "ZEC", "XIN", "XTZ"};

            if (!result.matches("Error")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    JSONObject raw1 = obj1.getJSONObject("RAW");

                    JSONObject obj = new JSONObject(result);
                    JSONObject raw= obj.getJSONObject("DISPLAY"); //change to "DISPLAY"

                    CoinList = new ArrayList<>();
                    for (int i = 0; i < 13; i++) {
                        JSONObject sym = raw.getJSONObject(symbol_arr[i]);
                        JSONObject currentObj = sym.getJSONObject("USD");

                        JSONObject sym1 = raw1.getJSONObject(symbol_arr[i]);

                        JSONObject currentObj1 = sym1.getJSONObject("USD");

                        CoinItem coinItem = new CoinItem();
                        coinItem.symbol = currentObj1.getString("FROMSYMBOL");
                        coinItem.marketCap = currentObj.getString("MKTCAP");
                        coinItem.priceUsd = currentObj.getString("PRICE");
                        coinItem.volume = currentObj.getString("SUPPLY");
                        coinItem.m1hr = currentObj.getString("CHANGEPCTHOUR");
                        coinItem.m24hr = currentObj.getString("CHANGEPCT24HOUR");
                        coinItem.m1day= currentObj.getString("CHANGEPCTDAY");
                        coinItem.url = currentObj.getString("IMAGEURL");
                        coinItem.time = currentObj.getString("LASTUPDATE");
                        coinItem.avaliableSupply = currentObj.getString("SUPPLY");
                        coinItem.totalSupply = currentObj.getString("TOTALVOLUME24H");
                        coinItem.highhprice24hr = currentObj.getString("HIGH24HOUR");
                        coinItem.lowprice24hr = currentObj.getString("LOW24HOUR");
                        coinItem.highprice1day = currentObj.getString("HIGHDAY");
                        coinItem.lowprice1day = currentObj.getString("LOWDAY");
                        coinItem.highprice1hr = currentObj.getString("HIGHHOUR");
                        coinItem.lowprice1hr = currentObj.getString("LOWHOUR");
                        coinItem.marketSource=currentObj.getString("MARKET");
                        CoinList.add(coinItem);

                    }
                    mAdapter = new CoinAdapter(getApplicationContext(), CoinList);
                    CoinListView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}