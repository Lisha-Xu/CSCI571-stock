package com.example.stocktradingapp.entity;

public class Stock {
    String stockName;
    String stockValue;
    float stockShares;
    String stockTrend;
    String stockDes;

    public Stock(String stockName, String stockValue, float stockShares, String stockTrend) {
        this.stockName = stockName;
        this.stockValue = stockValue;
        this.stockShares = stockShares;
        this.stockTrend = stockTrend;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockValue() {
        return stockValue;
    }



    public String getStockTrend() {
        return stockTrend;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
    }

    public void setStockTrend(String stockTrend) {
        this.stockTrend = stockTrend;
    }

    public float getStockShares() {
        return stockShares;
    }

    public void setStockShares(float stockShares) {
        this.stockShares = stockShares;
    }

    public String getStockDes() {
        return stockDes;
    }

    public void setStockDes(String stockDes) {
        this.stockDes = stockDes;
    }
}
