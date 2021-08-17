package com.kiro.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author Xufangmin
 * @create 2021-08-15-15:30
 */
public class NioChatClient {
    private static final int BUFFER = 1024;
    private ByteBuffer read_buffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer write_buffer = ByteBuffer.allocate(BUFFER);

    private SocketChannel client;
    private Selector selector;

    private Charset charset = Charset.forName("UTF-8");

    private void start(){
        try{
            client = SocketChannel.open();
            selector = Selector.open();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_CONNECT);
            client.connect(new InetSocketAddress("127.0.0.1", 8888));
            while(true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey key : selectionKeys){
                    handle(key);
                }
                selectionKeys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClosedSelectorException e){

        }
    }

    private void handle(SelectionKey key) throws IOException {
        if(key.isConnectable()){
            SocketChannel client = (SocketChannel) key.channel();
            if(client.finishConnect()){
                new Thread(new NioUserInputHandler(this)).start();
            }
            client.register(selector, SelectionKey.OP_READ);
        }
        if(key.isReadable()){
            SocketChannel client = (SocketChannel) key.channel();
            String msg = receive(client);
            System.out.println(msg);
            if(msg.equals("quit")){
                key.cancel();
                selector.wakeup();
            }
        }
    }

    public String receive(SocketChannel client) throws IOException {
        read_buffer.clear();
        while (client.read(read_buffer) > 0);
        read_buffer.flip();
        return String.valueOf(charset.decode(read_buffer));
    }

    public void send(String msg) throws IOException {
        if(!msg.isEmpty()){
            write_buffer.clear();
            write_buffer.put(charset.encode(msg));
            write_buffer.flip();
            while (write_buffer.hasRemaining()){
                client.write(write_buffer);
            }
            if(msg.equals("quit")){
                selector.close();
            }
        }
    }

    public static void main(String[] args) {
        new NioChatClient().start();
    }
}
