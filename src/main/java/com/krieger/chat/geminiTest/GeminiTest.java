package com.krieger.chat.geminiTest;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.Scanner;

public class GeminiTest {

    public static void main(String[] args) {
        System.out.println("\n\nGemini-Test-CLI");

        Scanner s = new Scanner(System.in);
        System.out.println("----------------------------------------\n\n");
        System.out.println("type to start chatting...");

        String response = "";
        while(true){

            String str = s.next();
            response = generateAiResponse(str);
            System.out.println(response);
            if (str.equalsIgnoreCase("exit")) break;

        }

    }


    static String modelId = "gemini-2.0-flash-001";
    static GenerateContentResponse response;

    public static String generateAiResponse(String query){
        try (Client c = new Client()) {
        response = c.models.generateContent(modelId,query, null);

            //System.out.println(response.text());
            return response.text();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }





}
