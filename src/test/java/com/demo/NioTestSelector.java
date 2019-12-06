package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class NioTestSelector {

    @Test
    void test1() throws IOException {
        /**
         * 1、获取Selector选择器
         */
        Selector selector = Selector.open();
        // 2、获取通道

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3.设置为非阻塞

        serverSocketChannel.configureBlocking(false);

        // 4、绑定连接

        serverSocketChannel.bind(new InetSocketAddress(9999));

        // 5、将通道注册到选择器上,并制定监听事件为：“接收”事件

        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        // 6、采用轮询的方式，查询获取“准备就绪”的注册过的操作
        while (selector.select() > 0)
        {
            // 7、获取当前选择器中所有注册的选择键（“已经准备就绪的操作”）
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext())
            {
                // 8、获取“准备就绪”的时间
                SelectionKey selectedKey = selectedKeys.next();

                // 9、判断key是具体的什么事件
                if (selectedKey.isAcceptable())
                {
                    // 10、若接受的事件是“接收就绪” 操作,就获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 11、切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 12、将该通道注册到selector选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                else if (selectedKey.isReadable())
                {
                    // 13、获取该选择器上的“读就绪”状态的通道
                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();

                    // 14、读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    // 读入 写入
                    while ((length = socketChannel.read(byteBuffer)) != -1)
                    {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, length));
                        byteBuffer.clear();
                    }
                    socketChannel.close();
                }

                // 15、移除选择键
                selectedKeys.remove();
            }
        }

        // 7、关闭连接
        serverSocketChannel.close();

    }
    @Test
    void test2() throws IOException {
        InetSocketAddress address= new InetSocketAddress(InetAddress.getLocalHost(), 9999);


        // 1、获取通道（channel）
        SocketChannel socketChannel =  SocketChannel.open(address);
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);

        // 3、分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world  ".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        socketChannel.close();

    }
}
