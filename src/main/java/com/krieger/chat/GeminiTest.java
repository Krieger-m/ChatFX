package com.krieger.chat;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiTest {
    public static void main(String[] args) {

        String modelId = "gemini-2.0-flash-001";

        try (Client c = new Client()) {
            GenerateContentResponse content = c.models.generateContent(modelId,"how are you doing?", null);

            System.out.println(content.text());
        }

    }
}
