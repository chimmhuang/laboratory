package com.chimm.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author huangshuai
 * @date 2020/05/22
 */
public class NioServer {

    public static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();

        // serverSocketChannel 关注 连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                int number = selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(selectionKey -> {

                    final SocketChannel client;

                    try {
                        if (selectionKey.isAcceptable()) {
                            // 由于上方的注册事件为 SelectionKey.OP_ACCEPT ， 所以当 selectionKey.isAcceptable() 为 true 时，我们就能判断该 channel 一定是 ServerSocketChannel
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            // 已经发生连接了，这里的 accept()，不会存在阻塞情况
                            client = server.accept();
                            client.configureBlocking(false);

                            // SocketChannel 关注 读事件
                            client.register(selector, SelectionKey.OP_READ);


                            String key = "【" + UUID.randomUUID() + "】";
                            clientMap.put(key, client);
                        } else if (selectionKey.isReadable()) {
                            client = (SocketChannel) selectionKey.channel();

                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);

                            if (count > 0) {
                                readBuffer.flip();
                                Charset charset = Charset.forName("UTF-8");
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array());

                                System.out.println(client + ": " + receivedMessage);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
