package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * 总结一下，使用 NIO Buffer 的步骤如下:
 *
 * 一：将数据写入到 Buffer 中；
 *
 * 二：调用 Buffer.flip()方法，将 NIO Buffer 转换为读模式；
 *
 * 三：从 Buffer 中读取数据；
 *
 * 四：调用 Buffer.clear() 或 Buffer.compact()方法，将 Buffer 转换为写模式。
 *
 * 当我们将数据写入到 Buffer 中时，Buffer 会记录我们已经写了多少的数据；当我们需要从 Buffer 中读取数据时，必须调用 Buffer.flip()将 Buffer 切换为读模式。
 */
@Slf4j
public class NioTestBuffer {

    static IntBuffer byteBuffer=null;

    /**
     * Logger.info("分配内存");
     *
     *         allocatTest();
     *
     *         Logger.info("写入");
     *         putTest();
     *
     *         Logger.info("翻转");
     *
     *         flipTest();
     *
     *         Logger.info("读取");
     *         getTest();
     *
     *         Logger.info("重复读");
     *         rewindTest();
     *         reRead();
     *
     *         Logger.info("make&reset写读");
     *
     *         afterReset();
     *        Logger.info("清空");
     *
     *         clearDemo();
     */

    @Test
    void tes1(){
        Buffer byteBuffer = IntBuffer.allocate(20);
        log.info("------------after allocate------------------");
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void tes2(){
        byteBuffer = IntBuffer.allocate(20);
        for (int i = 0; i < 5; i++) {
            ((IntBuffer) byteBuffer).put(i);
        }
        log.info("------------after allocate------------------");
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void tes3(){
        tes2();
        byteBuffer.flip();
        log.info("------------after flip ------------------");
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void tes4(){
        tes3();
        for (int i = 0; i < 2; i++)
        {
            int j = byteBuffer.get();
            log.info("j = " + j);
        }
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());
        log.info("------------after &getTest 3------------------");
        for (int i = 0; i < 3; i++)
        {
            int j = byteBuffer.get();
            log.info("j = " + j);
        }
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void test5(){
        tes4();
        byteBuffer.rewind();
        log.info("------------after rewind ------------------");
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void test6(){
        test5();
        log.info("------------after reRead------------------");
        for (int i = 0; i < 5; i++)
        {
            int j = byteBuffer.get();
            log.info("j = " + j);
            if (i == 2)
            {
                byteBuffer.mark();
                log.info("mark j = " + j);
            }
        }
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
    @Test
    void test7(){
        test6();
        log.info("------------after reset------------------");
        byteBuffer.reset();
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());
        log.info("i:{}",byteBuffer.get());
    }
    @Test
    void test8(){
        test7();
        log.info("------------after clear------------------");
        byteBuffer.clear();
        log.info("position=" + byteBuffer.position());
        log.info("limit=" + byteBuffer.limit());
        log.info("capacity=" + byteBuffer.capacity());

    }
}
