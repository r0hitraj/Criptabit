package com.example.criptabit;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Connect.CommonUtilsCoins;
import Connect.Constants;

public class CryptoChart extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {


    private static final String TAG = "CryptoChart";

    private LineChart mChart;
    ArrayList<Entry> yValues = new ArrayList<>();
    Toolbar toolbar;
    TextView    m1hr,m24hr,m1day,price,symb,vol,marketcap,availsupply,totalsupply,high1day,low1day,high1hr,low1hr,high24hr,low24hr,source;
    String    M1hr,M24hr,M1day,Price,Symb,Vol,Marketcap,Availsupply,Totalsupply,High1day,Low1day,High1hr,Low1hr,High24hr,Low24hr,Source;
    Button generateday,generatehr;
    TextInputLayout searchday,searchhr;
    String limitday,limithr="24",limit="Price Variation over past 24 hours";
    String dayText,hrText;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove status bar

        setContentView(R.layout.activity_crypto_chart);

        generateday=findViewById(R.id.generateday);
        searchday=findViewById(R.id.searchday);
        generatehr=findViewById(R.id.generatehr);
        searchhr=findViewById(R.id.searchhr);
        limitday=searchday.getEditText().getText().toString();
        limithr=searchhr.getEditText().getText().toString();

        String symbol = getIntent().getStringExtra("Symb");
        Symb=symbol;
        M1hr=getIntent().getStringExtra("M1hr");
        M24hr=getIntent().getStringExtra("M24hr");
        M1day=getIntent().getStringExtra("M1day");
        Price=getIntent().getStringExtra("Price");
        Vol=getIntent().getStringExtra("Vol");
        Marketcap=getIntent().getStringExtra("Marketcap");
        Availsupply=getIntent().getStringExtra("Availsupply");
        Totalsupply=getIntent().getStringExtra("Totalsupply");
        High1day=getIntent().getStringExtra("High1day");
        Low1day=getIntent().getStringExtra("Low1day");
        High1hr=getIntent().getStringExtra("High1hr");
        Low1hr=getIntent().getStringExtra("Low1hr");
        High24hr=getIntent().getStringExtra("High24hr");
        Low24hr=getIntent().getStringExtra("Low24hr");
        Source=getIntent().getStringExtra("Source");



        generateday.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        yValues.clear();
                        limitday=searchday.getEditText().getText().toString();
                        Toast.makeText(CryptoChart.this,"Click To Update ",Toast.LENGTH_LONG).show();
                        searchday.getEditText().setText("");
                        dayText="Price Variation over past "+limitday+" days";
                        limit=dayText;
                        if (!limitday.equals(""))
                            new CryptoChart.GetChart().execute("v2/histoday?fsym=" + symbol +
                                    "&tsym=USD&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&limit="+limitday, null);

                    }
                });

        generatehr.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        yValues.clear();
                        limithr=searchhr.getEditText().getText().toString();
                        Toast.makeText(CryptoChart.this,"Click To Update ",Toast.LENGTH_LONG).show();
                        hrText="Price Variation over past "+limithr+" hours";
                        limit=hrText;
                        searchhr.getEditText().setText("");
                        if (!limithr.equals(""))
                            new CryptoChart.GetChart().execute("v2/histohour?fsym=" + symbol +
                                    "&tsym=USD&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&limit="+limithr, null);

                    }
                });

        new CryptoChart.GetChart().execute("v2/histohour?fsym=" + symbol +
                "&tsym=USD&api_key=fa9844413727a02fe2322204004108e1bb499680eaebaa7cedd170f7f93aa463&limit=24", null);


    //for making line grph
        mChart = (LineChart) findViewById(R.id.linechart);

        mChart.setOnChartGestureListener(CryptoChart.this);
        mChart.setOnChartValueSelectedListener(CryptoChart.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(symbol);
        toolbar.setSubtitle("");
    //
        m1hr=findViewById(R.id.m1hr);
        m24hr=findViewById(R.id.m24hr);
        m1day=findViewById(R.id.m1day);
        vol=findViewById(R.id.vol);
        availsupply=findViewById(R.id.avalablesupply);
        totalsupply=findViewById(R.id.totalsupply);
        high1day=findViewById(R.id.high1day);
        low1day=findViewById(R.id.low1day);
        high1hr=findViewById(R.id.high1hr);
        low1hr=findViewById(R.id.low1hr);
        high24hr=findViewById(R.id.high24hr);
        low24hr=findViewById(R.id.low24hr);
        symb=findViewById(R.id.symbol);
        marketcap=findViewById(R.id.marketcap);
        price=findViewById(R.id.price);
        source=findViewById(R.id.datasrc);
        //
        //hooks
        m1hr.setText(M1hr);
        m24hr.setText(M24hr);
        m1day.setText(M1day);
        price.setText(Price);
        symb.setText(Symb);
        vol.setText(Totalsupply);
        marketcap.setText(Marketcap);
        availsupply.setText(Availsupply);
        totalsupply.setText(Totalsupply);
        high1day.setText(High1day);
        low1day.setText(Low1day);
        high1hr.setText(High1hr);
        low1hr.setText(Low1hr);
        high24hr.setText(High24hr);
        low24hr.setText(Low24hr);
        source.setText(Source);
        //


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

    private class GetChart extends AsyncTask<String, String, String> {


        KProgressHUD kProgressHUD;

        @Override
        protected String doInBackground(String... args) {
            if (CommonUtilsCoins.checkInternetStatus(CryptoChart.this)) {
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

            kProgressHUD = KProgressHUD.create(CryptoChart.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.1f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            kProgressHUD.dismiss();

            if (!result.matches("Error")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject object = obj.getJSONObject("Data");
                    JSONArray data = object.getJSONArray("Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject currentObj = data.getJSONObject(i);
                        yValues.add(new Entry(i, (Float.parseFloat(currentObj.getString("close")))));
                    }

                    LineDataSet set1 = new LineDataSet(yValues,limit);

                    set1.setFillAlpha(110);              //THESE FUNCTION CALL DOWN BELOW IS FOR DEFINING THE DESIGN OF GRAPH LIKE COLOR FONT AND ALL
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(Color.BLUE);
                    set1.setDrawValues(true);
                    set1.setCircleColor(Color.BLACK);
                    set1.setLineWidth(2f);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    LineData data2 = new LineData(dataSets);
                    mChart.setData(data2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}