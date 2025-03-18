package com.example.vacationscheduler.util;

import android.app.DatePickerDialog;
import android.view.View;

import com.example.vacationscheduler.UI.VacationDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Converters {

    public static String myFormat = "MM/dd/yy";
    public static SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public static Date parseDateFromString(String value) {
        Date date;
        try {
            if (value == null || value.isEmpty()) {
                date = new Date(); // Default to the current date if value is null or empty
            } else {
                date = sdf.parse(value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


    public static String getStringFromDate(Date date){
        String value = "";
        value = sdf.format(date);
        return value;
    }
}
