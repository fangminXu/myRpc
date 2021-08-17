package com.kiro.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 入门，实现单聊
 * @author Xufangmin
 * @create 2021-08-14-17:13
 */
public class Client {
    public static void main(String[] args) {
        //客户端
        final String DEFAULT_CLIENT_HOST = "127.0.0.1";
        final int DEFAULT_CLLIENT_PORT = 8888;

        try(Socket socket = new Socket(DEFAULT_CLIENT_HOST, DEFAULT_CLLIENT_PORT)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
            String msg = null;
            while(true){
                String input = userReader.readLine();
                writer.write(input + '\n');
                writer.flush();
                msg = reader.readLine();
                System.out.println(msg);
                if(input.equals("quit")){
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
