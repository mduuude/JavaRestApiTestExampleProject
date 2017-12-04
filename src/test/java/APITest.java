import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import model.Program;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static helpers.XmlParser.*;
import static helpers.ProgramTransformer.*;

public class APITest extends AssuredBase {
    private final String urlIndex = "/channel";
    private final String urlCurrentTime = "/time";
    private final String urlChannel = "http://www.vsetv.com/export/megogo/epg/3.xml";


    @Test(groups = {"api"})
    public void checkUsersResponseStatusAndCountTest() throws IOException, ParseException {
        Response res = given()
                .param("external_id", "295")
                .when().get(urlIndex)
                .then().contentType(ContentType.JSON)
                .extract().response();

        JSONArray arrayOfMegogoPrograms = getMegogoPrograms(res);
        List<Program> megogoPrograms = getProgramsList(arrayOfMegogoPrograms, false);

        JSONArray arrayOfChannelPrograms = getChannelPrograms(getJsonObjectOfXmlFromUrl(urlChannel));
        List<Program> channelPrograms = getProgramsList(arrayOfChannelPrograms, true);
        List<Program> channelProgramsToCompare = new ArrayList<Program>();

        long firstProgramStart = megogoPrograms.get(0).getEnd();
        long lastProgramEnd = megogoPrograms.get(megogoPrograms.size() - 1).getEnd();

        for (Program channelProgram : channelPrograms) {
            if (channelProgram.getStart() >= firstProgramStart && channelProgram.getEnd() <= lastProgramEnd) {
                channelProgramsToCompare.add(channelProgram);
            }
        }

        System.out.println(megogoPrograms);
        System.out.println(channelProgramsToCompare);
    }

    private JSONArray getMegogoPrograms(Response responseObject) throws IOException {
        JSONObject jsonObject = new JSONObject(responseObject.asString());
        JSONObject dataObject = jsonObject.getJSONArray("data").getJSONObject(0);
        return dataObject.getJSONArray("programs");
    }

    private JSONArray getChannelPrograms(JSONObject responseObject) throws IOException {
        JSONObject programmObject = responseObject.getJSONObject("tv");
        return programmObject.getJSONArray("programme");
    }

    private long getMegogoCurrentTimestamp() {
        Response res = given()
                .when().get(urlCurrentTime)
                .then().contentType(ContentType.JSON)
                .extract().response();

        JSONObject jsonObject = new JSONObject(res.asString());
        JSONObject dataObject = jsonObject.getJSONObject("data");

        return dataObject.getLong("timestamp");
    }
}