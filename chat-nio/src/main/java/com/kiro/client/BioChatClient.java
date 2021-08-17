package com.kiro.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Xufangmin
 * @create 2021-08-15-13:49
 */
public class BioChatClient {
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;

    public void start(){
        try{
            socket = new Socket("127.0.0.1", 8888);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new UserInputHandler(this)).start();
            String msg = null;
            while((msg = receive()) != null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendToServer(String input) throws IOException {
        if (!socket.isOutputShutdown()){
            writer.write(input + "\n");
            writer.flush();
        }
    }

    public String receive() throws IOException {
        String msg = null;
        if(!socket.isInputShutdown()){
            msg = reader.readLine();
        }
        return msg;
    }

    public static void main(String[] args) {
        new BioChatClient().start();
    }
}
