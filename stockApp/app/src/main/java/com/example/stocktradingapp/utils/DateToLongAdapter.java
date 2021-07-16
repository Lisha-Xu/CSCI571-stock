package com.example.stocktradingapp.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToLongAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        if (date == null) {
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(date.getTime());
    }

    @Override
    public Date read(JsonReader in) throws IOException {

        SimpleDateFormat sdf=new SimpleDateFormat("MMMMMM dd,yyyy");

        String jsonToken = in.nextString();
        Date date = new Date();
        try {
            date = sdf.parse(jsonToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}