package com.example.criptabit;

import Connect.CommonUtils;
import Connect.Constants;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.argb;
import static android.graphics.Color.rgb;

public class StockChart extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {


    private static final String TAG = "StockChart";

    private LineChart mChart;
    ArrayList<Entry> yValues = new ArrayList<>();
    Toolbar toolbar;

    TextView sym, name, price, vol, marketcap, percent, range;
    String stockName, stockSymbol, stockPrice, stockVolume, stockMarketCap, stockPercent, stockRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove status bar
        setContentView(R.layout.activity_stock_chart);

        stockSymbol = getIntent().getStringExtra("Symbol");
        stockName = getIntent().getStringExtra("Name");

        new StockChart.GetSearchAsyncTask().execute("market/get-charts?symbol=" + stockSymbol + "&interval=5m&range=1d&region=US", null);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(symbol);

        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setOnChartGestureListener(StockChart.this);
        mChart.setOnChartValueSelectedListener(StockChart.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);  //for pinch zoom
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(stockName);
        toolbar.setSubtitle(stockSymbol);

        //<----------------------New code-------------------->
        stockPrice = getIntent().getStringExtra("Price");
        stockVolume = getIntent().getStringExtra("Volume");
        stockMarketCap = getIntent().getStringExtra("MarketCap");
        stockPercent = getIntent().getStringExtra("Percentage");
        stockRange = getIntent().getStringExtra("Range");

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        vol = findViewById(R.id.vol);
        marketcap = findViewById(R.id.marketcap);
        percent = findViewById(R.id.pchange);
        range = findViewById(R.id.range);


        name.setText(stockName);
        price.setText(stockPrice);
        vol.setText(stockVolume);
        marketcap.setText(stockMarketCap);
        percent.setText(stockPercent);
        range.setText(stockRange);
        //<-----ends here------->


    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private class GetSearchAsyncTask extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtils.checkInternetStatus(StockChart.this)) {
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

            kProgressHUD = KProgressHUD.create(StockChart.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();

            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject chart = obj.getJSONObject("chart");
                    JSONArray results = chart.getJSONArray("result");
                    JSONObject indicators = results.getJSONObject(0).getJSONObject("indicators");
                    JSONArray quote = indicators.getJSONArray("quote");
                    JSONArray close = quote.getJSONObject(0).getJSONArray("close");

                    for (int i = 0; i < close.length(); i++) {
                        yValues.add(new Entry(i, (float) close.getDouble(i)));
                    }

                    LineDataSet set1 = new LineDataSet(yValues, "Price Variation over past 75 days");

                    set1.setFillAlpha(110);
                    set1.setColor(rgb(203, 30, 98));

                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setLineWidth(2f);
                    set1.setDrawValues(true);
                    set1.setCircleColor(Color.BLACK);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    LineData data = new LineData(dataSets);
                    mChart.setData(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}