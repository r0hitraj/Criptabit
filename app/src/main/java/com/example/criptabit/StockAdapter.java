package com.example.criptabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class StockAdapter extends ArrayAdapter<com.example.criptabit.StockItem> {

    public StockAdapter(Context context, List<com.example.criptabit.StockItem> stockItems) {
        super(context, 0, stockItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_stock_item, parent, false);
        }


        com.example.criptabit.StockItem currentObj = getItem(position);


        //Symbol
        TextView symbol = listItemView.findViewById(R.id.textview_symbol);
        symbol.setText(currentObj.getSymbol());


        //price
        TextView price = listItemView.findViewById(R.id.textview_price);
        price.setText("$ " + currentObj.getPrice());


        //volume
        TextView volume = listItemView.findViewById(R.id.textview_volume);
        volume.setText(currentObj.getVolume());


        //MarketCap
        TextView marketCap = listItemView.findViewById(R.id.textview_marketcap);
        marketCap.setText(currentObj.getMarketCap());

        //<----------------New Code ----------------->
        //PercentChange
        TextView percentChange = listItemView.findViewById(R.id.textview_percentchange);
        //string -> int
        double value;
        value = Double.parseDouble(String.valueOf(currentObj.getPercentage()));
        String formattedValue = formatMagnitude(value);



        if(value < 0)
            percentChange.setTextColor(0xFFED0606);
        else
            percentChange.setTextColor(0xFF04CF5A);

        percentChange.setText(formattedValue + "%");

        //<--------------------Ends here---------------------->

        //Range
        TextView range = listItemView.findViewById(R.id.textview_range);
        range.setText(currentObj.getRange());

        return listItemView;

    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.000");
        return magnitudeFormat.format(magnitude);
    }



}
