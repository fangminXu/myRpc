package com.kiro.client;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Xufangmin
 * @create 2021-08-15-13:50
 */

@AllArgsConstructor
public class UserInputHandler implements Runnable {
    private BioChatClient client;

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String input = reader.readLine();
                client.sendToServer(input);
                if(input.equals("quit")){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
