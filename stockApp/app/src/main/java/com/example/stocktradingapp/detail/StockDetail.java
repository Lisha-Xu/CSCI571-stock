package com.example.stocktradingapp.detail;

import java.util.Date;
import java.util.List;

public class StockDetail {
    private String ticker;
    private String exchangeCode;
    private Date endDate;
    private String name;
    private Date startDate;
    private String description;
    private String bidPrice;
    private String low;
    private String bidSize;
    private String prevClose;
    private String last;
    private String askSize;
    private String volume;
    private String high;
    private String tngoLast;
    private String askPrice;
    private String open;
    private String mid;
    private String change;
    private String changePercent;
    private String flag;
    private List<News> news;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBidPrice() {
        return isNullReturn0(bidPrice);
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getLow() {
        return isNullReturn0(low);
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getBidSize() {
        return bidSize;
    }

    public void setBidSize(String bidSize) {
        this.bidSize = bidSize;
    }

    public String getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(String prevClose) {
        this.prevClose = prevClose;
    }

    public String getLast() {
        return isNullReturn0(last);
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getAskSize() {
        return askSize;
    }

    public void setAskSize(String askSize) {
        this.askSize = askSize;
    }

    public String getVolume() {

        return isNullReturn0(volume);
    }

    private String isNullReturn0(String str){
        if(str == null){
            return "0.0";
        }
        return str;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHigh() {
        return isNullReturn0(high);
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getTngoLast() {
        return tngoLast;
    }

    public void setTngoLast(String tngoLast) {
        this.tngoLast = tngoLast;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getOpen() {
        return isNullReturn0(open);
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getMid() {
        return isNullReturn0(mid);
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
