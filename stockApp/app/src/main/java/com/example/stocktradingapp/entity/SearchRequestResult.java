package com.example.stocktradingapp.entity;

public class SearchRequestResult {
    private String name;
    private String ticker;
    private String openFIGIComposite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getOpenFIGIComposite() {
        return openFIGIComposite;
    }

    public void setOpenFIGIComposite(String openFIGIComposite) {
        this.openFIGIComposite = openFIGIComposite;
    }
}
