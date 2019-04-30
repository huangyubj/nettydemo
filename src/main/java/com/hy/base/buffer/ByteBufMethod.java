package com.hy.base.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/25
 * Time: 10:08
 * Project: nettydemo
 */
public class ByteBufMethod {
    public static void main(String[] args) {
//        1.检查 ByteBuf 是否有支持数组。
//        2.如果有的话，得到引用数组。
//        3.计算第一字节的偏移量。
//        4.获取可读的字节数。
//        一：访问非堆缓冲区 ByteBuf 的数组会导致UnsupportedOperationException， 可以使用 ByteBuf.hasArray()来检查是否支持访问数组。
        ByteBuf byteBuf = Unpooled.buffer(200);
        //二：直接缓冲区,不占用堆内存，减少高速缓存区对堆中的复制过程
        //内存空间的分配和释放上比堆缓冲区更复杂，另外一个缺点是如果要将数据传递给遗留代码处理，因为数据不是在堆上，你可能不得不作出一个副本
        ByteBuf directByf = Unpooled.directBuffer(1024);
//        三：复合缓冲区
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponents(byteBuf,directByf);
        compositeByteBuf.removeComponent(0);
        for (int i = 0; i < compositeByteBuf.numComponents(); i++) {
            System.out.println(compositeByteBuf.component(i).toString());;
        }
        if(byteBuf.hasArray()){
            byte[] bytes = byteBuf.array();
            int offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
            int length = byteBuf.readableBytes();
        }

    }
}
