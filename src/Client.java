import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

import Object.*;
import Util.Log;
import com.google.gson.Gson;

import javax.tools.Tool;

/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Client {
    public static void main(String[] a) {
        Socket socket = new Socket();
        try {

            socket.connect(new InetSocketAddress("1772w8j773.imwork.net", 19074));
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream.write(("customer!@#order!@#get!@#"+5).getBytes());
            byte[] info = new byte[1024];
            int len = inputStream.read(info);
            byte[] cut = new byte[len];
            System.arraycopy(info,0,cut,0,len);
            Log.d(new String(cut,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//private static String name = null;//上线名
//    private static SocketChannel sc = null;
//    private static Selector selector = null;
//
//    public static void main(String[] args) throws Exception {
//        System.out.print("程序以命令形式运行\n1.输入 'to 人名 : 要说的话' 发送信息(人名写为'all'则是公聊)\n2.输入 'online list' 查看在线名单\n3.输入 'check 人名' 查看聊天记录\n" +
//                "====================\n输入用户名:");
//        Scanner input = new Scanner(System.in);
//        name = input.nextLine();
//        System.out.println("name is " + name);
//        //启动
//        client();
//    }

//    private static void client() {
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        final SocketChannel socketChannel;
//        try {
//            //准备
//            selector = Selector.open();
//            socketChannel = SocketChannel.open();
//            socketChannel.configureBlocking(false);
//            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8076));
//            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024 * 10));
//            sc = socketChannel;
//            Gson gson = new Gson();
//            Customer customer = new Customer();
//            customer.setName("吉哥");
//            customer.setPassword("qweqweq");
//            //连上服务器
//            if (socketChannel.finishConnect())
//            {
//
//                buffer.clear();
//                //发送登陆信息
//                buffer.put(("customer!@#login!@#" +gson.toJson(customer)).getBytes());
//                buffer.flip();
//                try {
//                    socketChannel.write(buffer);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //开线程读键盘输入
////                new Thread(() -> {
////                    while (true) {
////                        Scanner in = new Scanner(System.in);
////                        //包装命令
////                        String info = handleCommand(in.nextLine());
////                        //如果是查看记录的命令，则处理完跳到下一次循环
////                        if (checkRecord(info)) continue;
////                        Charset charset = Charset.forName("UTF-8");
////                        try {
////                            //发送
////                            socketChannel.write(charset.encode(info));
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////
////                    }
////                }).start();
//                //监听读事件
//                while (true) {
//                    if (selector.select(2000) == 0) continue;
//                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
//                    while (iter.hasNext()) {
//                        SelectionKey key = iter.next();
//                        if (key.isReadable()) {
//                            //处理读事件
//                            handleRead(key);
//                        }
//                        iter.remove();
//                    }
//                }
//
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    //处理读事件
//    private static void handleRead(SelectionKey key) throws IOException {
//        SocketChannel sc = (SocketChannel) key.channel();
//        ByteBuffer buf = (ByteBuffer) key.attachment();
//        sc.read(buf);
//        buf.flip();
//        //将信息转为字符串
//        String info = byteBufferToString(buf);
//        System.out.println(info);
//        //保存记录
//        saveRecord(info);
//        buf.clear();
//    }
//    //包装命令
//    private static String handleCommand(String info) {
//
//        if (isOrderContain(info, "to : ")) {
//            return name + " " + info;
//        }
//
//        return info;
//    }
//    //判断字符串是否顺序包含
//    private static boolean isOrderContain(String text, String target) {
//        if (text == null || target == null) {
//            return false;
//        }
//        char[] targetArray = target.toCharArray();
//        String textCopy = new String(text.toCharArray());
//        for (int i = 0; i < targetArray.length; i++) {
//            if (textCopy.indexOf(targetArray[i]) == -1) {
//                return false;
//            }
//            if ((textCopy = textCopy.substring(textCopy.indexOf(targetArray[i]) + 1)).equals("") && i != targetArray.length - 1) {
//                return false;
//            }
//
//        }
//
//        return true;
//    }
//    //buffer转字符串
//    private static String byteBufferToString(ByteBuffer buffer) {
//        CharBuffer charBuffer;
//        try {
//            Charset charset = Charset.forName("UTF-8");
//            charBuffer = charset.decode(buffer);
//            buffer.flip();
//            return charBuffer.toString();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    //保存记录
//    private static void saveRecord(String info) {
//        if (isOrderContain(info, " to  : ")) {
//            String[] infoArray = info.split(" ");
//            //公共聊天不记录
//            if (infoArray[2].equals("all")) return;
//            //私聊
//            if (infoArray[0].equals(name)) {
//                writeFile(name + " " + infoArray[2] + ".txt", info);
//            }
//            if (infoArray[2].equals(name)) {
//                writeFile(name + " " + infoArray[0] + ".txt", info);
//            }
//
//
//        }
//    }
//    //写入文件
//    private static void writeFile(String file, String content) {
//        BufferedWriter writer = null;
//        try {
//            //写入
//            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
//            writer.write(content + "\r\n");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //关闭writer
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    //查看记录
//    private static boolean checkRecord(String info) {
//        //若是查看命令
//        if (isOrderContain(info, "check ")) {
//            //查看文件是否存在
//            String[] infoArray = info.split(" ");
//            File record = new File(name + " " + infoArray[1] + ".txt");
//            if (!record.exists()) {
//                //不存在
//                System.out.println("record not found");
//            } else {
//                //存在则读取
//                try {
//                    RandomAccessFile raFile = new RandomAccessFile(name + " " + infoArray[1] + ".txt", "r");
//                    String line;
//                    System.out.println("=====record=====");
//                    do {
//                        line = raFile.readLine();
//                        if (line == null) {
//                            System.out.println("=====record=====");
//                        } else System.out.println(line);
//
//                    } while (line != null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return true;
//        }
//        return false;
//    }
}
