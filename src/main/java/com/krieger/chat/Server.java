package com.krieger.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Server {

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
        String logPath = "chat/src/main/resources/server_log.txt";
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
            case "time":
                out.println("Serverzeit: \t" + LocalDateTime.now());
                System.out.println("-> sending time info to client.");
                break;
            case "dir":
                out.println("Arbeitsverzeichnis: \t" + System.getProperty("user.dir"));
                System.out.println("-> sending directory info to client.");
                break;
            case "list":
                Files.list(Paths.get(System.getProperty("user.dir")))
                        .forEach(path -> out.println(path.getFileName()));
                System.out.println("-> sending file list to client.");
                break;
            default:
                out.println("Unbekannter Befehl: " + command);
                System.err.println("Unknown command received: " + command);
                break;
        }
        out.println("END");
    }
}
