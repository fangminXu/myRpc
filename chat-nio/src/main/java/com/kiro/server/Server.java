package com.kiro.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Xufangmin
 * @create 2021-08-14-17:19
 */
public class Server {
    public static void main(String[] args) {
        final int DEFAULT_SERVER_PORT = 8888;
        try(ServerSocket serverSocket = new ServerSocket(DEFAULT_SERVER_PORT)){
            System.out.println("ServerSocket Start, The Port is:" + DEFAULT_SERVER_PORT);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client[" + socket.getPort() + "]Online");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String msg = null;
                while((msg = reader.readLine()) != null){
                    System.out.println("Client[" + socket.getPort() + "]:" + msg);
                    writer.write("Server:" + msg + " return.\n");
                    writer.flush();
                    if(msg.equals("quit")){
                        System.out.println("Client[" + socket.getPort() + "]:Offline");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
