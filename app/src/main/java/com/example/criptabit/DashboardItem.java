package com.example.criptabit;

public class DashboardItem {
    public String Symbol;
    public String MarketCap;
    public String Price;
    public String Volume ;
    public String Percentage;

    public DashboardItem() {
    }

    public DashboardItem(String symbol, String marketCap, String price, String volume, String percentage) {
        Symbol = symbol;
        MarketCap = marketCap;
        Price = price;
        Volume = volume;
        Percentage = percentage;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getMarketCap() {
        return MarketCap;
    }

    public void setMarketCap(String marketCap) {
        MarketCap = marketCap;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }


}
