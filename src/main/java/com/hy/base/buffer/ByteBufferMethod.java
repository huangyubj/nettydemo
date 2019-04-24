package com.hy.base.buffer;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/24
 * Time: 10:08
 * Project: nettydemo
 */
public class ByteBufferMethod {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        byte[] arr = new byte[]{'a','b','c','d','e','f','g'};
        buffer.put(arr);
        buffer.flip();//变为读模式，否则读出为空
        System.out.println("======================test get=========================");
        System.out.println((char)buffer.get()+"---------------get()使position会增加");
        System.out.println(buffer);
        System.out.println((char)buffer.get(4)+"----------------get(index)不会使position会增加");
        System.out.println(buffer);
        byte[] dst = new byte[2];
        buffer.get(dst, 0, 2);
        System.out.println(new String(dst)+"-----------------get(dst, offset, length)使position会增加");
        System.out.println(buffer);

        System.out.println("======================test put 通get一样=========================");
        ByteBuffer putbuffer = ByteBuffer.allocate(32);
        System.out.println(putbuffer.put((byte)'a')+"---------------put()使position会增加");
        System.out.println(putbuffer.put(4, (byte)'c')+"----------------put(index, byte)不会使position会增加");

        System.out.println("======================test reset=========================");
        System.out.println(buffer.clear()+"---------------clean()使position归0，数据仍然存在");
        System.out.println(buffer.position(3)+"---------------position(index)设置position");
        buffer.mark();
        buffer.position(10);
        System.out.println(buffer.reset()+"---------------需要标记reset()，使position复位到标记的位置，如buffer的7");

        System.out.println("======================test concat=========================");
        ByteBuffer concatbuffer = ByteBuffer.allocate(32);
        concatbuffer.put("abcdef".getBytes());
        concatbuffer.flip();
        concatbuffer.get();
        concatbuffer.get();
        concatbuffer.get();
        System.out.println(concatbuffer.toString()+"---------------concat 前，"+new String(concatbuffer.array()));
        System.out.println(concatbuffer.compact()+"---------------concat 后，"+new String(concatbuffer.array()));
        System.out.println("concat()拷贝未读的到起始处，并将position指向未读元素末尾");

        System.out.println("======================test memory=========================");
        System.out.println("初始内存："+Runtime.getRuntime().freeMemory());
        ByteBuffer buffer1 = ByteBuffer.allocateDirect(1024*1024);
        System.out.println("申请直接内存不会从堆内存中分配："+Runtime.getRuntime().freeMemory());
        ByteBuffer buffer2 = ByteBuffer.allocate(1024*1024);
        System.out.println("allocate内存会从堆内存中分配："+Runtime.getRuntime().freeMemory());
    }
}
