package helpers;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class XmlParser {
    /**
     * Converts .xml response from given url in .json format and returns it
     *
     * @param {String} urlToGo
     * @return {JSONObject}
     * @throws IOException
     */
    public static JSONObject getJsonObjectOfXmlFromUrl(String urlToGo) throws IOException {

        URL url = new URL(urlToGo);
        InputStream input = url.openStream();

        String xmlStringOutput = IOUtils.toString(input, "utf-8");
        input.close();

        return XML.toJSONObject(xmlStringOutput);
    }
}
