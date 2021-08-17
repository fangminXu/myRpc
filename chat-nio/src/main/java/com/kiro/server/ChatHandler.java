package com.kiro.server;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

/**
 * 为每个sockey创建线程执行服务端
 * @author Xufangmin
 * @create 2021-08-15-13:49
 */

@AllArgsConstructor
public class ChatHandler implements Runnable{

    private BioChatServer chatServer;
    private Socket socket;


    @Override
    public void run() {
        try {
            chatServer.addClient(socket);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = null;
            while((msg = reader.readLine()) != null){
                String sendmsg = "Client[" + socket.getPort() + "]:" + msg;
                System.out.println(sendmsg);
                chatServer.sendMessage(socket, sendmsg);
                if(msg.equals("quit")){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                chatServer.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
