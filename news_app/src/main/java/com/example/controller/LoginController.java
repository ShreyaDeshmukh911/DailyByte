package com.example.controller;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class LoginController {

    public Result login(String email, String password) {
        try {
            String apiKey = "AIzaSyBI1eTYgK-JXYlfCAysl0Ns7taFH9PkO0s"; // Replace with your actual Firebase API key

            // Construct the URL for Firebase Authentication REST API
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey);

            // Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // Set the request method to POST
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // Set content type
            conn.setDoOutput(true); // Enable output for sending data

            // Create a JSON object to hold the request data
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email); // Add the email
            jsonRequest.put("password", password); // Add the password
            jsonRequest.put("returnSecureToken", true); // Request secure token

            // Send request data
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Handle response
            if (conn.getResponseCode() == 200) {
                return new Result(true, "Login successful.");
            } else {
                return new Result(false, "Failed to log in user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Failed to log in user: " + e.getMessage());
        }
    }

    public static class Result {
        public final boolean success; // Indicates if the operation was successful
        public final String message;  // Message providing additional info

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
