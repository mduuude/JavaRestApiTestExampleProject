import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class main {
    public static void main(String[] args) throws ParseException {


//        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//        long ts = dateFormat.parse("2017-12-04 12:20:00 +0200").getTime()/1000;

//        DateTime dateTime = new DateTime("2017-12-04 12:20:00");
//        long unix = dateTime.getMillis()/1000;

//        String dateString = "2017-12-04 17:10:00 +0200";
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
//        Date date = dateFormat.parse(dateString);
//        long unixTime = date.getTime()/1000;
//        System.out.println(unixTime); //<- prints 1352504418

        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

// next day
        date.add(Calendar.DAY_OF_MONTH, 1);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//        long ts = dateFormat.parse("2017-12-04 16:06:34 +0100").getTime()/1000;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(date.getTimeInMillis() / 1000);



//        java.util.Date time2 =new java.util.Date((time)*1000);
//        System.out.println(unixTime);
    }
}
