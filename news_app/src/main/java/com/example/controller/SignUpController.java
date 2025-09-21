package com.example.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;


public class SignUpController {

    
    public Result signUp(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return new Result(false, "Email and password cannot be empty.");
        }
        try {
            String apiKey = "AIzaSyBI1eTYgK-JXYlfCAysl0Ns7taFH9PkO0s"; // Replace with your actual Firebase API key
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("returnSecureToken", true);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }

            // Get response
            int responseCode = conn.getResponseCode();
            InputStream responseStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
            String response;
            try (Scanner scanner = new Scanner(responseStream)) {
                response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            if (responseCode == 200) {
                JSONObject jsonResponse = new JSONObject(response);
                System.out.println("Successfully created user: " + jsonResponse.getString("localId"));
                return new Result(true, "User created successfully.");
            } else {
                JSONObject errorResponse = new JSONObject(response);
                String errorMessage = errorResponse.has("error")
                        ? errorResponse.getJSONObject("error").getString("message")
                        : "Unknown error";
                return new Result(false, "Failed to create user: " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Exception occurred: " + e.getMessage());
        }
    }

    public static class Result {
        public final boolean success; // Indicates if the operation was successful
        public final String message;  // Message providing additional information about the operation

        public Result(boolean success, String message) { // Constructor to initialize the result
            this.success = success;
            this.message = message;
        }
    }
}
