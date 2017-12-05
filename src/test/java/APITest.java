import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import model.Program;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static helpers.XmlParser.*;
import static helpers.ProgramTransformer.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class APITest extends AssuredBase {
    private final String urlIndex = "/channel";
    private final String urlCurrentTime = "/time";

    /**
     * List of channels used in verification
     *
     * @return {Object[][]}
     */
    @DataProvider(name = "channels list")
    public static Object[][] channels() {
        return new Object[][]{
                {"295", "http://www.vsetv.com/export/megogo/epg/3.xml"},
        };
    }

    /**
     * Checks if programs from Megogo API response equal to given channel programs schedule
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test(groups = {"api"}, dataProvider = "channels list")
    public void checkUsersResponseStatusAndCountTest(String externalId, String urlChannel) throws IOException, ParseException {
        Response response = given()
                .param("external_id", externalId)
                .when().get(urlIndex)
                .then().contentType(ContentType.JSON)
                .extract().response();

        JSONArray arrayOfMegogoPrograms = getMegogoPrograms(response);
        List<Program> megogoPrograms = getProgramsList(arrayOfMegogoPrograms, false);

        long currentTimestamp = getMegogoCurrentTimestamp();
        long lastProgramStart = megogoPrograms.get(megogoPrograms.size() - 1).getStart();
        List<Program> channelProgramsToCompare = getChannelProgramsToCompare(urlChannel, currentTimestamp, lastProgramStart);

        for (int i = 0; i < megogoPrograms.size(); i++) {
            Assert.assertEquals(megogoPrograms.get(i), channelProgramsToCompare.get(i));
        }
    }

    /**
     * Checks if Megogo API response structure equal to given JSON Scheme
     *
     * @throws ProcessingException
     * @throws IOException
     */
    @Test(groups = {"api"})
    public void checkResponseStructure() throws ProcessingException, IOException {
        given()
                .param("external_id", "295")
                .when().get(urlIndex)
                .then().assertThat().body(matchesJsonSchemaInClasspath("schema.json"))
        ;
    }

    /**
     * Returns programs from Megogo API response
     *
     * @param {JSONObject} responseObject
     * @return {JSONArray}
     * @throws IOException
     */
    private JSONArray getMegogoPrograms(Response responseObject) throws IOException {
        JSONObject jsonObject = new JSONObject(responseObject.asString());
        JSONObject dataObject = jsonObject.getJSONArray("data").getJSONObject(0);
        return dataObject.getJSONArray("programs");
    }

    /**
     * Returns channel programs from channel program schedule
     *
     * @param {JSONObject} responseObject
     * @return {JSONArray}
     * @throws IOException
     */
    private JSONArray getChannelPrograms(JSONObject responseObject) throws IOException {
        JSONObject programmObject = responseObject.getJSONObject("tv");
        return programmObject.getJSONArray("programme");
    }

    /**
     * Returns selection from channel programs schedule to compare by start and end time
     *
     * @param {String} urlChannel
     * @param {long}   startSelectionTime
     * @param {long}   endSelectionTime
     * @return {List <Program>}
     * @throws IOException
     * @throws ParseException
     */
    private List<Program> getChannelProgramsToCompare(String urlChannel, long startSelectionTime, long endSelectionTime) throws IOException, ParseException {
        JSONArray arrayOfChannelPrograms = getChannelPrograms(getJsonObjectOfXmlFromUrl(urlChannel));
        List<Program> channelPrograms = getProgramsList(arrayOfChannelPrograms, true);

        List<Program> channelProgramsToCompare = new ArrayList<Program>();

        for (Program channelProgram : channelPrograms) {
            if (channelProgram.getEnd() >= startSelectionTime && channelProgram.getStart() <= endSelectionTime) {
                channelProgramsToCompare.add(channelProgram);
            }
        }

        return channelProgramsToCompare;
    }

    /**
     * Returns current time in UNIX timestamp from Megogo API response
     *
     * @return {long}
     */
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