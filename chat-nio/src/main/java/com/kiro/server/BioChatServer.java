package com.kiro.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Xufangmin
 * @create 2021-08-15-13:48
 */
public class BioChatServer {
    private int DEFAULT_PORT = 8888;
    private final Map<Integer, Writer> map = new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public void addClient(Socket socket) throws IOException {
        if(socket != null){
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            map.put(socket.getPort(), writer);
            System.out.println("Client["+socket.getPort()+"]:Online");
        }
    }

    public void removeClient(Socket socket) throws IOException {
        if(socket != null){
            if(map.containsKey(socket.getPort())){
                map.get(socket.getPort()).close();
                map.remove(socket.getPort());
            }
            System.out.println("Client["+socket.getPort()+"]:Offline");
        }
    }

    public void sendMessage(Socket socket, String msg) throws IOException {
        for(int port : map.keySet()){
            if(port != socket.getPort()){
                Writer writer = map.get(port);
                writer.write(msg + "\n");
                writer.flush();
            }
        }
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println("Server Start,The Port is:"+DEFAULT_PORT);
            while(true){
                Socket socket = serverSocket.accept();
                threadPool.execute(new ChatHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioChatServer chatServer = new BioChatServer();
        chatServer.start();
    }
}
