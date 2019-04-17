package com.hy.base.bio;

import com.hy.base.BaseConstans;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/17
 * Time: 15:36
 * Project: nettydemo
 */
public class Servver {

    public static void main(String[] args) throws IOException {
        run();
    }

    private static ServerSocket serverSocket = null;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static void run() throws IOException {
        serverSocket = new ServerSocket(BaseConstans.PORT);
        while (true){
            Socket socket = serverSocket.accept();
            executorService.execute(new ServerRunnable(socket));
        }
    }

    private static class ServerRunnable implements Runnable {
        private Socket socket;
        public ServerRunnable(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                if(socket != null){
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg;
                    while ((msg = bufferedReader.readLine()) != null){
                        System.out.println(msg);
                        printWriter.println((new Date()).toString() + "哈哈，收到了消息"+msg);
                        printWriter.flush();
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
