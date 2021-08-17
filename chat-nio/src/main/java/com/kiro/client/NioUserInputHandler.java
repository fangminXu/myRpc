package com.kiro.client;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Xufangmin
 * @create 2021-08-15-15:42
 */

@AllArgsConstructor
public class NioUserInputHandler implements Runnable {

    private NioChatClient nioChatClient;

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try{
                String input = reader.readLine();
                nioChatClient.send(input);
                if(input.equals("quit")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
