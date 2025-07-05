package com.krieger.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static Socket establishConnection(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println("\nVerbunden mit ChatFX_Server: " + host + ":" + port);
        return socket;
    }

    public static void closeSocket(Socket s) {
        if (s != null && !s.isClosed()) {
            try {
                s.close();
            } catch (IOException e) {
                System.out.println("Socket closing error ..." + e.getMessage());
            }
        }
    }

    // The following methods are for console-based client usage and are not used in the JavaFX GUI.
    // They are kept for possible CLI testing or future extension.
    public static void commandLoop(BufferedReader console, PrintWriter out, BufferedReader in) throws IOException {
        String command;
        while (true) {
            command = readUserCommand(console);
            out.println(command);
            if ("exit".equalsIgnoreCase(command)) {
                printServerResponse(in);
                break;
            }
            printServerResponse(in);
        }
        System.out.println("Verbindung beendet.");
    }

    public static String readUserCommand(BufferedReader console) throws IOException {
        System.out.println("--------------------------------------");
        System.out.print("> ");
        return console.readLine();
    }

    public static void printServerResponse(BufferedReader in) throws IOException {
        String response;
        while ((response = in.readLine()) != null && !response.equals("END")) {
            System.out.println("ChatFX_Server: " + response);
            if ("Verbindung wird beendet.".equals(response)) {
                break;
            }
        }
    }
}