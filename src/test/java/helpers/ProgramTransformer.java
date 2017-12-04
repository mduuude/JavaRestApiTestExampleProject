package helpers;

import model.Program;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramTransformer {

    /**
     * @return ArrayList programsList
     */
    public static List<Program> getProgramsList(JSONArray array, boolean isChannel) throws ParseException {
        List<Program> programsList = new ArrayList<Program>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject innerObject = array.getJSONObject(i);

            Program program = new Program();

            String title;
            long start, end;

            if (isChannel) {
                JSONObject titleObject = innerObject.getJSONObject("title");
                title = titleObject.getString("content");

                start = convertToUnixTimestamp(innerObject.getString("start"));
                end = convertToUnixTimestamp(innerObject.getString("stop"));
            } else {
                title = innerObject.getString("title");
                start = innerObject.getLong("start_timestamp");
                end = innerObject.getLong("end_timestamp");
            }

            program.setTitle(title);
            program.setStart(start);
            program.setEnd(end);

            programsList.add(program);
        }

        return programsList;
    }

    private static long convertToUnixTimestamp(String dateString) throws ParseException {

//        String dateString = "2017-12-04 17:06:34 +0200";
//        String dateString = "Dec 4, 2017 4:10:00 PM";";
        String format = "yyyy-MM-dd hh:mm:ss z";
//        String format = "MMM d, yyyy h:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateString);
        long unixTime = date.getTime() / 1000;

        return unixTime;
    }
}
