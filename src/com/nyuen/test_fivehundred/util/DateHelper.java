package com.nyuen.test_fivehundred.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;

public class DateHelper
{
    private static SimpleDateFormat iso8601formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    
    public static Date parseISO8601(String date)
    {
        try {
            Date result = iso8601formatter.parse(date);
            return result;
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public static String DateDifference(Date a) {
        long ms = System.currentTimeMillis() - a.getTime();
                
        StringBuffer text = new StringBuffer("");
        if (ms > DAY) {
          text.append(ms / DAY);
          if(ms / DAY <= 1)
              text.append(" day ");
          else
              text.append(" days ");
          ms %= DAY;
        }else if (ms > HOUR) {
          text.append("About ");
          text.append(ms / HOUR);
          if(ms / HOUR <= 1)
              text.append(" hour ");
          else
              text.append(" hours ");
          ms %= HOUR;
        }else if (ms > MINUTE) {
          text.append(ms / MINUTE);
          if(ms / MINUTE <= 1)
              text.append(" minute ");
          else
              text.append(" minutes ");
          ms %= MINUTE;
        }else if (ms > SECOND) {
          text.append("Less than a minute ");
          ms %= SECOND;
        }
        
        text.append("ago");
        
        return text.toString();
    }
    
}
