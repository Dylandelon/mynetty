package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class NioTestFileChannel {

    /**
     * FileChannel 是操作文件的Channel，我们可以通过 FileChannel 从一个文件中读取数据，也可以将数据写入到文件中。
     *
     * 注意，FileChannel 不能设置为非阻塞模式。
     *
     * 操作一：打开 FileChannel通道
     * @throws FileNotFoundException
     */

    static FileChannel inChannel = null;
    @BeforeAll
    static void test1() throws FileNotFoundException {
        /**
         * 操作一：打开 FileChannel通道
         */
        RandomAccessFile aFile     = new RandomAccessFile("test.txt","rw");

        inChannel = aFile.getChannel();


    }
    @Test
    void test2() throws IOException {
        /**
         * 操作二：读取数据
         */
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        log.info("read:{}",buf.asCharBuffer().get());

    }
    @Test
    void test3() throws IOException {
        /**
         * 操作三：写入数据
         */
        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);

        buf.clear();

        buf.put(newData.getBytes());

        buf.flip();

        while(buf.hasRemaining())

        {

            inChannel.write(buf);

        }
        /**
         * 操作五：强制刷新磁盘
         */
        inChannel.force(true);

        /**
         * 操作四：关闭
         */
        inChannel.close();


    }

}
