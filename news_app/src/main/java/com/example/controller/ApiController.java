package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiController {

    public JSONArray getNews(String category) {
        try {
            String apiKey = "fa6c5c4e0d7242d5a67856d12d1678b6"; 
            String urlStr =
                    "https://newsapi.org/v2/top-headlines/sources?category=" + category +
                            "&apiKey=" + apiKey;

            URL url = new URL(urlStr); // Create a URL object with the constructed URL string
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open a connection to the URL
            conn.setRequestMethod("GET"); // Set the request method to GET

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder(); // Create a StringBuilder to hold the response
            String inputLine; // Variable to hold each line of the response

            while ((inputLine = in.readLine()) != null) { // Read each line of the response
                response.append(inputLine); // Append the line to the response StringBuilder
            }
            in.close(); // Close the BufferedReader

            JSONObject jsonResponse = new JSONObject(response.toString()); // Parse the response string into a JSONObject
            JSONArray sources = jsonResponse.getJSONArray("sources"); // Get the "sources" array from the JSON response

            return sources;
        } catch (Exception e) {
            // Handle exception if needed
        }
        return null;
    }
}
