import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class AssuredBase {

    @BeforeClass(groups = {"api"})
    public static void setup() {
        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://epg.megogo.net";
        }
        RestAssured.baseURI = baseHost;
    }
}
