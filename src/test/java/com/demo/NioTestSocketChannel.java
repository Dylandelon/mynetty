package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 有两种Socket通道，一个是客户端的SocketChannel，一个是负责服务器端的Socket通道ServerSocketChannel。SocketChannel与OIO中的Socket类对应，ServerSocketChannel对应于OIO中的ServerSocket类相NIO。
 *
 *阻塞执行完再返回，异步，直接返回
 */
@Slf4j
public class NioTestSocketChannel {


    /**
     * 两种Socket通道新增的通道都支持阻塞和非阻塞两种模式。在阻塞模式下的通道的创建、关闭、读写操作如下：
     *
     */
    static SocketChannel socketChannel = null;
    static ServerSocketChannel serverSocketChannel = null;
    static DatagramChannel channel = null;
    @BeforeAll
    static void test0() throws IOException{
        serverSocketChannel = ServerSocketChannel.open();

    }
    @BeforeAll
    static void test() throws IOException{
        socketChannel = SocketChannel.open();

    }
    @BeforeAll
    static void test00() throws IOException{
        channel = DatagramChannel.open();

    }
    @Test
    void test1() throws IOException {

        /**
         * 操作一：创建
         * 这个是客户端的创建。当一个服务器端的ServerSocketChannel 接受到连接请求时，也会返回一个 SocketChannel 对象。
         */


//        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        /**
         * 操作二：读取
         * 如果 read()返回 -1，那么表示连接中断了.
         */

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = socketChannel.read(buf);
        log.info("连接返回read:{}",bytesRead);

        /**
         * 操作三：写入数据 tcp双向长连接
         */

        String newData = "New String to write to file..." + System.currentTimeMillis();


        buf.clear();

        buf.put(newData.getBytes());

        buf.flip();

        while(buf.hasRemaining()) {

            socketChannel.write(buf);

        }
        /**
         * 操作四：关闭
         */
        socketChannel.close();

    }

    /**
     * 在非阻塞模式，我们可以设置 SocketChannel 为异步模式，这样我们的 connect，read，write 都是异步的了
     */

    @Test
    void test2() throws IOException {
        /**
         * 操作一：连接
         */
        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));


        while(! socketChannel.finishConnect() ){

            //wait，or do something else...
            log.info("asyn wait");

        }
        log.info("asyn connected");
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = socketChannel.read(buf);
        String newData = "New String to write to file..." + System.currentTimeMillis();


        buf.clear();

        buf.put(newData.getBytes());

        buf.flip();
        while(buf.hasRemaining()) {

            socketChannel.write(buf);

        }
        /**
         * 在异步模式中，或许连接还没有建立，socketChannel.connect 方法就返回了，因此我们不断的自旋，检查当前是否是连接到了主机。
         * 操作二：非阻塞读写
         *
         * 在异步模式下，读写的方式是一样的.
         *
         * 在读取时，因为是异步的，因此我们必须检查 read 的返回值，来判断当前是否读取到了数据.
         */

    }

    /**
     * ServerSocketChannel 顾名思义，是用在服务器为端的，可以监听客户端的 TCP 连接
     */

    @Test
    void test3() throws IOException {
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        /**
         * 我们可以使用ServerSocketChannel.accept()方法来监听客户端的 TCP 连接请求，accept()方法会阻塞，
         * 直到有连接到来，当有连接时，这个方法会返回一个 SocketChannel 对象:
         */
        while(true){

            SocketChannel socketChannel =

                    serverSocketChannel.accept();
            log.info("syn server");

            //do something with socketChannel...
//            serverSocketChannel.close();

        }

    }

    /**
     * 在非阻塞模式下，accept()是非阻塞的，因此如果此时没有连接到来，那么 accept()方法会返回null:
     */
    @Test
    void test4() throws IOException {
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        serverSocketChannel.configureBlocking(false);

        while(true){

            SocketChannel socketChannel =

                    serverSocketChannel.accept();

            if(socketChannel != null){

                //do something with socketChannel...
                log.info("syn server connecting");

            }
//            log.info("asyn server");

        }

    }

    /**
     * DatagramChannel 是用来处理 UDP 连接的
     * @throws SocketException
     */

    @Test
    void test5() throws SocketException {
        /**
         * 操作一：打开
         */
//        DatagramChannel channel = DatagramChannel.open();

        channel.socket().bind(new InetSocketAddress(9999));


    }
    @Test
    void test6() throws IOException {
        test5();

        /**
         * 操作二：读取数据
         */

        ByteBuffer buf = ByteBuffer.allocate(48);

        buf.clear();

        channel.receive(buf);


    }
    @Test
    void test7() throws IOException {

        /**
         * 操作三：发送数据
         */

        String newData = "New String to write to file..."

                + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);

        buf.clear();

        buf.put(newData.getBytes());

        buf.flip();

        int bytesSent = channel.send(buf,new InetSocketAddress("localhost",9999));


    }
}
