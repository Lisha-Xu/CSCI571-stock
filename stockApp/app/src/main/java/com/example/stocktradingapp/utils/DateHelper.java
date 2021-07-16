package com.example.stocktradingapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static Date String2Date(String dateStr) {
        try {


        StringBuilder result = new StringBuilder();
        String year = "";
        String month = "";
        String day = "";

        String[] timeStr = dateStr.split(", ");
        year = timeStr[1];
        String[] monthDay = timeStr[0].split(" ");
        day = monthDay[1];
        switch (monthDay[0]) {
            case "January":
                month = "1";
                break;
            case "February":
                month = "2";
                break;
            case "March":
                month = "3";
                break;
            case "April":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "June":
                month = "6";
                break;
            case "July":
                month = "7";
                break;
            case "August":
                month = "8";
                break;
            case "September":
                month = "9";
                break;
            case "October":
                month = "10";
                break;
            case "November":
                month = "11";
                break;
            case "December":
                month = "12";
                break;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        result.append(year).append(month).append(day);
        Date date = new Date();
        try {
            date = sdf.parse(result.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
        } catch (Exception e) {
            return new Date();
        }
    }


    public static String dateBeforeCurrent(Date date){
        long current = System.currentTimeMillis();
        long distance = current - date.getTime();
        if(distance < 60l * 60 * 1000){
            return distance/60/1000 + " minutes ago";
        }else if(distance < 24l * 60 * 60 * 1000){
            return distance/60/60/1000 + " hours ago";
        }else if(distance < 30l * 24 * 60 * 60 * 1000){
            return distance/24/60/60/1000 + " days ago";
        }else if(distance < 12l * 30 * 24 * 60 * 60 * 1000){
            return distance/30/24/60/60/1000 + " months ago";
        }else if(distance < 5l* 12 * 30 * 24 * 60 * 60 * 1000){
            return distance/12/30/24/60/60/1000 + " years ago";
        }
        return "long long ago";
    }
}
