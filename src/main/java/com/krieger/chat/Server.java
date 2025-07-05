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
    static final String MODEL_ID = "gemini-2.0-flash-001";

    public static String generateAiResponse(String query) {
        try (Client c = new Client()) {
            GenerateContentResponse response = c.models.generateContent(MODEL_ID, query, null);
            String text = response.text();
            System.out.println("AI response: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("AI error: " + e.getMessage());
            return null;
        }
    }

    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(4999)) {
            System.out.println("\nChat_Server gestartet. Warte auf Verbindung...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Fehler bei der Client-Kommunikation: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Serverfehler: " + e.getMessage());
        }
    }

    public static String wrapText(String text, int maxLineLength) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < text.length()) {
            sb.append(text, index, Math.min(index + maxLineLength, text.length()));
            sb.append(System.lineSeparator());
            index += maxLineLength;
        }
        return sb.toString();
    }

    private static void handleClient(Socket clientSocket) {
        String logPath = "src/main/resources/com/krieger/chat/server_log.txt";
        File logFile = new File(logPath);
        logFile.getParentFile().mkdirs(); // Ensure directory exists
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true))
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
            System.err.println("Fehler bei der Client-Kommunikation: " + e.getMessage());
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
                System.out.println("Chat message received: " + command);
                String aiResponse = generateAiResponse(command);
                if (aiResponse == null || aiResponse.isEmpty()) {
                    out.println("Keine Antwort vom Server.");
                } else {
                    out.println(aiResponse);
                }
                break;
        }
       
    }

    public static void main(String[] args) {
        startServer();
    }
}