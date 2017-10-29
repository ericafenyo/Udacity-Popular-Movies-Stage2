package com.example.eric.popularmovies.Utils;

/**
 *
 * Created by eric on 17/10/2017.
 * coverts raw TMDb release date to a more meaning one
 */

public class DateUtils {

    //@params: date--- TMDb release date (YYY-MM-DD)
    public String getDate(String date) {
        String year = getYear(date);
        String day = getDay(date);
        String month = convectMonth(getMonth(date));

        return month + " " +day + ", " + year ;
    }
    //@params: months --- returned value of [TMDb release date (YYY-MM-DD)].substring(5,7);
    public String convectMonth(String months) {

        if (months.equals("01")) {
            return "January";
        } else if (months.equals("02")) {
            return "February";
        } else if (months.equals("03")) {
            return "March";
        } else if (months.equals("04")) {
            return "April";
        } else if (months.equals("05")) {
            return "May";
        } else if (months.equals("06")) {
            return "June";
        } else if (months.equals("07")) {
            return "July";
        } else if (months.equals("08")) {
            return "August";
        } else if (months.equals("09")) {
            return "September ";
        } else if (months.equals("10")) {
            return "October";
        } else if (months.equals("11")) {
            return "November";
        } else if (months.equals("12")) {
            return "December";
        }else {
            return "lol";
        }

    }

    //@params: date --- TMDb release date (YYY-MM-DD)

    public String getDay(String date) {
        return date.substring(8);
    }

    public String getYear(String date) {
        return date.substring(0,4);
    }

    public String getMonth(String date) {
        return date.substring(5,7);
    }

}
