package pers.cly.test_net.socket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CLY on 2017/7/11.
 */
public class MyServerSocket {
    public static void main(String[] arg){
        /**
         * 根据tcp协议可知，所谓套接字（socket）是指一个由“ip地址+端口”组成的组合。
         * 而每一台主机的ip地址都是确定的，不需要我们来指定，
         * 所以构建服务器socket的第一步就是确定端口
         */
        try {
            int port = 233;//端口号
            int queueLength = 50;//最大入站连接
            InetAddress bindAddress = InetAddress.getByName("127.0.0.1");//只监听该ip的指定端口
            ExecutorService pool = Executors.newFixedThreadPool(50);//创建一个最大容量为50的线程池，为每一个入站连接分配一条线程。

            //创建一个端口为“233”的服务器socket
            ServerSocket serverSocket = new ServerSocket(port);

            //创建一个端口为233的服务器socket，且队列一次最多能保存50个入站连接
            //ServerSocket serverSocket = new ServerSocket(port,queueLength);

            //假设一台主机有多个ip地址，则服务器socket会默认在所有ip地址的指定端口上监听，但可以指定其只监听指定ip的端口。
            //ServerSocket serverSocket = new ServerSocket(port,queueLength,bindAddress);

            while (true){
                //accept()调用会阻塞，会一直等到有客户端连接到指定socket端口为止。
                final Socket connection = serverSocket.accept();

                //线程池中拿取一条线程来处理socket连接。然后主程序运行下一个循环，继续等待下一个客户端的访问。
                pool.execute(new Runnable() {
                    //测试：将当前时间写入流中
                    public void run() {
                        try {
                            Writer writer = new OutputStreamWriter(connection.getOutputStream());
                            String nowData = new Date().toString();
                            writer.write(nowData);
                            writer.flush();
                            System.out.println(nowData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                //关闭socket连接
                                connection.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}