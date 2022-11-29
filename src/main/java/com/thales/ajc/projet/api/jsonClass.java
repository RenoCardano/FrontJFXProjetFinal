package com.thales.ajc.projet.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class jsonClass {

    private static ObjectMapper objectMapper = getDefaultMapper();


    private static ObjectMapper getDefaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    public static JsonNode parse(String src) throws JsonProcessingException {
        return objectMapper.readTree(src);
    }

    public static <A> A fromJson(JsonNode node, Class<A> classe) throws JsonProcessingException {

        return objectMapper.treeToValue(node, classe);
    }

    /*
    Convertie un Object en JSON
     */
    public static JsonNode toJson(Object a) {
        return objectMapper.valueToTree(a);
    }

    /*
    Convertie un JSON to a String
     */
    public static String stringify(JsonNode node) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        return objectWriter.writeValueAsString(node);
    }

    /*
    private GluonObservableObject<Model> getWeather() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://api.openweathermap.org/%22)
                        .connectTimeout(10000)
                        .readTimeout(1000)
                        .path("data/2.5/weather")
                        .header("accept", "application/json")
                        .queryParam("appid", API_KEY)
                        .queryParam("q", CITY);

        return DataProvider.retrieveObject(client.createObjectDataReader(Model.class));
    }
     */


    public static void generateUrl(String urlString, String body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

    }


    /*
   Convertie un JSON to a String with indentation
    */
    public static String prettyPrint(JsonNode node) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        return objectWriter.writeValueAsString(node);
    }

    /*
    Cree une requete URL
     */


    /*
      Lie une URL et retourne un string
        */
    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }


    public static String getStringJson( Object objet) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try{
            return objectMapper.writeValueAsString(objet);
        } catch (JsonProcessingException e){
           e.printStackTrace();
        }
        return null;
    }
}
