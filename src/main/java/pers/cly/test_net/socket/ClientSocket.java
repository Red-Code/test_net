package pers.cly.test_net.socket;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by CLY on 2017/7/11.
 */
public class ClientSocket {
    public static void main(String[] arg){
        int port = 233;//与之连接的服务端套接字的端口号
        String host = "127.0.0.1";//与之连接的服务端套接字ip地址
        Socket socket = null;
        try {
            //创建socket连接，即在该Socket构造函数返回之前，就会与远程主机建立连接，如果未能连接，则会抛出异常。
            socket = new Socket(host, port);

            //获取输出流，向服务器端发送当前时间
            OutputStream outputStream = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            String now_date = new Date().toString();
            writer.write("客户端当前时间为——"+now_date);
            writer.flush();
            ////关闭客户端的输出流。相当于给流中加入一个结束标记-1.这个样子服务器的输入流的reaLine方法就会读到一个-1，然后结束readLIne方法。
            socket.shutdownOutput();

            //获取输入流，并读取服务器端的响应信息
            InputStream inputStream= socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String server_info = null;
            while((server_info=br.readLine())!=null){
                System.out.println("服务端传过来的值："+server_info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果socket不为null，则释放掉它
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
