package com.mridx.accountsmanage.lib;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimesAgo {


    public TimesAgo() {
    }

    public String getTimeAgo(String date, Context context) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        Date past = null;
        try {
            past = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();

        long diffSec = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long diffMin = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long diffHour = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long diffDay = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
        long diffMonth = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
        long diffYear = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

        /*
        if (diffMin < 59) {
            return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minute ago";
            //return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hour ago";
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <= 23 ) {
            return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hour ago";
           // return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago";
        } else if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) <= 364) {
            return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " day ago";
            //return "1 year ago";
        }
        */
        /*if (diffSec < 59) {
            return "Just Now";
        } else if (diffMin < 2) {
            return "just Now";
        } else if (diffMin <= 59) {
            return diffMin + " minutes ago";
        } else if (diffHour < 2) {
            return diffHour + " hour ago";
        } else if (diffHour <= 23) {
            return diffHour + " hours ago";
        } else if (diffDay < 2) {
            return diffDay + " day ago";
        } else if (diffDay <= 29) {
            return diffDay + " days ago";
        } else if (diffDay <= 59) {
            return "1 month ago";
        } else if (diffDay > 60 && diffDay < 363) {
            return (diffDay / 30) + " months ago";
        } else if (diffDay < 365) {
            return 1 + " year ago";
        } else if (diffDay > 365) {
            return "1 year ago";
        } else {
            return null;
        }*/
/*
        if (diffSec < 59) {
            return context.getResources().getString(R.string.justNow);
        } else if (diffMin < 2) {
            return context.getResources().getString(R.string.justNow);
        } else if (diffMin <= 59) {
            return diffMin + " " + context.getResources().getString(R.string.minsAgo);
        } else if (diffHour < 2) {
            return diffHour + " " + context.getResources().getString(R.string.hourAgo);
        } else if (diffHour <= 23) {
            return diffHour + " " + context.getResources().getString(R.string.hoursAgo);
        } else if (diffDay < 2) {
            return diffDay + " " + context.getResources().getString(R.string.dayAgo);
        } else if (diffDay <= 29) {
            return diffDay + " " + context.getResources().getString(R.string.daysAgo);
        } else if (diffDay <= 59) {
            return 1 + " " + context.getResources().getString(R.string.monthAgo);
        } else if (diffDay > 60 && diffDay < 363) {
            return (diffDay / 30) + " " + context.getResources().getString(R.string.monthsAgo);
        } else if (diffDay < 365) {
            return 1 + " " + context.getResources().getString(R.string.yearAgo);
        } else if (diffDay > 365) {
            return "1 year ago";
        } else {
            return null;
        }*/

        return null;
    }

    public String getSimpleDate(String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date past = null;
        try {
            past = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(past);

    }

    public String getSDate(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String dateInString = "15-10-2015 10:20:56";
        SimpleDateFormat m = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        SimpleDateFormat x = new SimpleDateFormat("EEE dd MMM yyyy", Locale.US);
        SimpleDateFormat xa = new SimpleDateFormat("EEE dd MMM", Locale.US);
        long n = Long.parseLong(date);
        String simple = null;
        simple = xa.format(n);
        /*try {
            simple = String.valueOf(sdf.parse(date));
            return simple;
        } catch (ParseException e) {
            e.printStackTrace();
            return simple;
        }*/
        return simple;
    }




    public String getDate(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String dateInString = "15-10-2015 10:20:56";
        SimpleDateFormat m = new SimpleDateFormat("dd/MM/yyyy");
        String simple = null;
        try {
            Date s = sdf.parse(date);
            simple = m.format(s);
            // simple = sdf.parse(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simple;
    }

    public String getTime(String timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String time = null;
        long t = Long.parseLong(timestamp);
        time = simpleDateFormat.format(t);
        return time;
    }


}
