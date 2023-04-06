package API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Requester {

    private final String api_key;
    private String url;

    public Requester(String api_key, String URL){
        this.api_key = api_key;
        this.url = URL;
    }


    public JsonNode GetRequest(Parameter... params) {

        StringBuilder queries = new StringBuilder();
        for (Parameter param : params) {
            queries.append(String.format("&%s=%s", param.getParameter(), param.getValue()));
        }
        final String req_url = String.format("%s?key=%s%s", url, api_key, queries);
        System.out.println(req_url);
        try {
            URL link = new URL(req_url);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            String line = "";
            Scanner scanner = new Scanner(link.openStream());
            while (scanner.hasNextLine()) {
                line += scanner.nextLine();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, JsonNode.class);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
