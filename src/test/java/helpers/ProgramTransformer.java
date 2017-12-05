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
     * Returns list of Program objects from channel programs schedule or from Megogo API response
     *
     * @param {JSONArray} arrayOfPrograms
     * @param {boolean}   isChannel
     * @return
     * @throws ParseException
     */
    public static List<Program> getProgramsList(JSONArray arrayOfPrograms, boolean isChannel) throws ParseException {
        List<Program> programsList = new ArrayList<Program>();

        for (int i = 0; i < arrayOfPrograms.length(); i++) {
            JSONObject innerObject = arrayOfPrograms.getJSONObject(i);

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

    /**
     * Returns UNIX timestamp of string date
     *
     * @param {String} dateString
     * @return {long}
     * @throws ParseException
     */
    private static long convertToUnixTimestamp(String dateString) throws ParseException {

        String format = "yyyy-MM-dd HH:mm:ss z";
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateString);
        long unixTime = date.getTime() / 1000;

        return unixTime;
    }
}
