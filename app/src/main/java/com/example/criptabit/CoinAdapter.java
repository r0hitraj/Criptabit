package com.example.criptabit;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CoinAdapter extends ArrayAdapter<CoinItem> {

    public CoinAdapter(Context context, List<CoinItem> coinItems){
        super(context,0,coinItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cryptolist, parent, false);
        }


        CoinItem currentObj = getItem(position);

        //Symbol
        TextView symbol = listItemView.findViewById(R.id.symbol);
        symbol.setText(currentObj.getSymbol());

        //volume
        TextView volume = listItemView.findViewById(R.id.volume);
        volume.setText(currentObj.getVolume());

        //price
        TextView price = listItemView.findViewById(R.id.price);
        price.setText(currentObj.getPriceUsd());


        //Time
        TextView time =  listItemView.findViewById(R.id.time);
        time.setText(currentObj.getTime());

        //increase in 1H (m1hr)
        TextView m1hr =  listItemView.findViewById(R.id.m1hr);
        m1hr.setText(currentObj.getM1hr());

        //increase in 24H
        TextView m2hr =  listItemView.findViewById(R.id.m24hr);
        m2hr.setText(currentObj.getM24hr());

        //Fav checkbox
//        TextView fav =  listItemView.findViewById(R.id.fav);
//        time.setText(currentObj.getTime());

//        ImageView icon =  listItemView.findViewById(R.id.icon);
//        time.setText(currentObj.getTime());
//        icon.setImageURI(currentObj.getmIconUrl());



        return listItemView;
    }


//    public String formatTime(Date obj) {
//
//        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
//        return (timeFormat.format(obj));
//    }


}