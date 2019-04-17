package com.hy.base.bio;

import com.hy.base.BaseConstans;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/17
 * Time: 15:36
 * Project: nettydemo
 */
public class Cline {
    public static void main(String[] args) {
        run();
    }

    private static Socket socket;
    private static PrintWriter printWriter;
    private static void run() {
        try {
            socket = new Socket(BaseConstans.HOST, BaseConstans.PORT);
            new Thread(new ReadMsg(socket)).start();
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true){
                printWriter.println((new Scanner(System.in).next()));
                printWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static class ReadMsg implements Runnable {
        private Socket socket;
        public ReadMsg(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                if(socket != null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg;
                    while ((msg = bufferedReader.readLine()) != null){
                        System.out.println(msg);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
