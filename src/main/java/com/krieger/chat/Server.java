package com.krieger.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class Server {

    static String modelId = "gemini-2.0-flash-001";
    static GenerateContentResponse response;

    public static String generateAiResponse(String query){
            //NOTE API KEY NEEDS TO BE DELETED BEFORE UPLOADING
            // not here to lecture you about sharing of sensible data or credentials but this can get expensive :D
        try (Client c = Client.builder().apiKey("YOUR API KEY HERE").build();) {
            // hier muss der eigene API key eingesetzt werden ^


            response = c.models.generateContent(modelId,query, null);

            System.out.println(response.text());
            return response.text();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(4999)) {
            System.out.println("\nChat_Server gestartet. Warte auf Verbindung...");
            try (Socket clientSocket = serverSocket.accept()) {
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Serverfehler: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        String logPath = "src/main/resources/com/krieger/chat/server_log.txt";
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                PrintWriter logWriter = new PrintWriter(new FileWriter(logPath, true))
        ) {
            System.out.println("Chat_Client verbunden: " + clientSocket.getInetAddress().getHostAddress());
            String command;
            while ((command = in.readLine()) != null) {
                logCommand(logWriter, command);
                if ("exit".equalsIgnoreCase(command)) {
                    out.println("Verbindung wird beendet.");
                    out.println("END");
                    break;
                }
                processCommand(command, out);
            }
        } catch (IOException e) {
            System.err.println("Fehler bei der A1_Client-Kommunikation: " + e.getMessage());
        }
    }

    private static void logCommand(PrintWriter logWriter, String command) {
        logWriter.println(LocalDateTime.now() + " - " + command);
        logWriter.flush();
    }

    private static void processCommand(String command, PrintWriter out) throws IOException {
        System.out.println();
        switch (command.toLowerCase()) {
            case "srvr-time":
                out.println("Serverzeit: \t" + LocalDateTime.now());
                System.out.println("-> sending time info to client.");
                break;
            case "srvr-dir":
                out.println("Arbeitsverzeichnis: \t" + System.getProperty("user.dir"));
                System.out.println("-> sending directory info to client.");
                break;
            case "srvr-list":
                Files.list(Paths.get(System.getProperty("user.dir")))
                        .forEach(path -> out.println(path.getFileName()));
                System.out.println("-> sending file list to client.");
                break;
            default:
                //out.println("Unbekannter Befehl: " + command);
                System.err.println("Chat message received: " + command);
                break;
        }
        out.println("END");
    }
}