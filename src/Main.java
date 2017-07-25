import DataBase.OsDataBase;
import Object.*;
import Util.Log;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Main implements Server.OnEventListener {
    private static Server mServer;

    public static void main(String[] a) {
        Main main = new Main();
        main.init();
        mServer.selector();
    }

    private void init() {
        mServer = new Server();
        mServer.setmOnEventListener(this);
        mServer.init();
    }

    @Override
    public void onAcceptable(SelectionKey key) {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sc = ssChannel.accept();
            sc.configureBlocking(false);
            sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadable(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        String info = null;
        String feedback = null;
        Gson gson = new Gson();
        try {
            if (sc.read(buf) == -1) {
                sc.close();
                return;
            }
            buf.flip();
            Charset charset = Charset.forName("UTF-8");
            info = charset.decode(buf).toString();
            buf.clear();
            Log.d("info", info);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sc.close();
                for (Long aLong : mServer.onlineSupplierMap.keySet()) {
                    if (!mServer.onlineSupplierMap.get(aLong).isOpen()) {
                        mServer.onlineSupplierMap.remove(aLong);

                    }
                }
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }
        if (info != null) {
            String[] infoArray = info.split("!@#");
            switch (infoArray[0]) {
                case "customer":
                    switch (infoArray[1]) {
                        case "supplier":
                            feedback = gson.toJson(Supplier.getAll());
                            Log.d("获取商家表", feedback);
                            break;
                        case "order":
                            switch (infoArray[2]) {
                                case "cancel":
                                    Order order = Order.getById(Long.parseLong(infoArray[3]));
                                    order.setStatus(Order.CANCEL_BY_CUSTOMER);
                                    order.update();
                                    feedback = Order.CANCEL_BY_CUSTOMER + "";
                                    Log.d("取消订单", feedback);
                                    break;
                                case "get":
                                    List<Order> list = Order.getByCustomerId(Long.parseLong(infoArray[3]));
                                    for (Order order1 : list) {
                                        order1.setCustomerName(Customer.getById(order1.getCustomerId()).getName());
                                        order1.setSupplierName(Supplier.getById(order1.getSupplierId()).getName());
                                        if (Dish.getById(order1.getDishId()) == null) {
                                            order1.setDishName("菜式已删除");
                                        } else {

                                            order1.setDishName(Dish.getById(order1.getDishId()).getName());
                                        }
                                    }
                                    feedback = "" + gson.toJson(list);
                                    Log.d("获取订单", feedback);
                                    break;
                                case "rate":
                                    Order order3 = Order.castfromJson(infoArray[3]);
                                    order3.update();
                                    Dish dish = Dish.getById(order3.getDishId());
                                    if (dish != null) {
                                        dish.setGradeCount(dish.getGradeCount() + 1);
                                        dish.setGradeSum(dish.getGradeSum() + order3.getStar());
                                        dish.update();
                                    }
                                    feedback = "" + Customer.SUCCESS;
                                    break;
                                case "sure":
                                    Order order4 = Order.getById(Long.parseLong(infoArray[3]));
                                    order4.setStatus(Order.DONE);
                                    order4.update();
                                    feedback = ""+Customer.SUCCESS;
                                    break;
                                default:
                                    Charset charset = Charset.forName("UTF-8");
                                    Order order1 = Order.castfromJson(infoArray[2]);
                                    order1.save();
                                    feedback = "" + Customer.SUCCESS;
                                    try {
                                        SocketChannel channel = mServer.onlineSupplierMap.get(order1.getSupplierId());
                                        if (channel != null) {
                                            channel.write(charset.encode(gson.toJson(order1)));
                                            Log.d("订单提醒已发送", gson.toJson(order1));
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                            break;
                        case "dish":
                            feedback = gson.toJson(Dish.getBySupplierId(Long.parseLong(infoArray[2])));
                            Log.d("获取dish表", feedback);
                            break;
                        case "register":
                            feedback = "" + Customer.castFromJson(infoArray[2]).register();
                            Log.d("注册", feedback);
                            break;
                        case "password":
                            Customer.castFromJson(infoArray[2]).update();
                            feedback = Customer.SUCCESS + "";
                            Log.d("修改密码", feedback);
                            break;
                        case "login":
                            feedback = Customer.castFromJson(infoArray[2]).login();
                            Log.d("登陆", feedback);
                            break;
                        case "logout":
                            feedback = "" + Customer.castFromJson(infoArray[2]).logout();
                            Log.d("登出", feedback);
                            break;
                    }
                    break;
                case "supplier":
                    switch (infoArray[1]) {
                        case "register":
                            feedback = "" + Supplier.castFromJson(infoArray[2]).register();
                            Log.d("注册", feedback);
                            break;
                        case "login":
                            feedback = Supplier.castFromJson(infoArray[2]).login();
                            Log.d("登陆", feedback);
                            break;
                        case "logout":
                            feedback = "" + Supplier.castFromJson(infoArray[2]).logout();
                            Log.d("登出", feedback);
                            break;
                        case "dish":
                            switch (infoArray[2]) {
                                case "add":
                                    Dish.castfromJson(infoArray[3]).save();
                                    feedback = "" + Dish.SUEECSS;
                                    Log.d("添加dish", feedback);
                                    break;
                                case "delete":
                                    Dish.getById(Long.parseLong(infoArray[3])).delete();
                                    feedback = "" + Dish.SUEECSS;
                                    Log.d("删除dish", feedback);
                                    break;
                                case "set":
                                    Dish.castfromJson(infoArray[3]).update();
                                    feedback = "" + Dish.SUEECSS;
                                    Log.d("设置dish", feedback);
                                    break;
                                case "get":
                                    feedback = gson.toJson(Dish.getBySupplierId(Long.parseLong(infoArray[3])));
                                    Log.d("获取dish表", feedback);
                                    break;
                            }
                            break;
                        case "order":
                            switch (infoArray[2]) {
                                case "accept":
                                    Order order = Order.getById(Long.parseLong(infoArray[3]));
                                    order.setStatus(Order.DOING);
                                    order.update();
                                    feedback = "" + Order.DOING;
                                    Log.d("接收订单", feedback);
                                    break;
                                case "get":
                                    List<Order> list = Order.getBySupplierId(Long.parseLong(infoArray[3]));
                                    for (Order order1 : list) {
                                        order1.setCustomerName(Customer.getById(order1.getCustomerId()).getName());
                                        order1.setSupplierName(Supplier.getById(order1.getSupplierId()).getName());
                                        if (Dish.getById(order1.getDishId()) == null) {
                                            order1.setDishName("菜式已删除");
                                        } else {

                                            order1.setDishName(Dish.getById(order1.getDishId()).getName());
                                        }
                                    }
                                    feedback = "" + gson.toJson(list);
                                    Log.d("获取订单", feedback);
                                    break;
                            }
                            break;
                        case "password":
                            Supplier.castFromJson(infoArray[2]).update();
                            feedback = Supplier.SUCCESS + "";
                            Log.d("修改密码", feedback);
                            break;
                        case "map":
                            mServer.onlineSupplierMap.put(Long.parseLong(infoArray[2]), sc);
                            return;

                    }
                    break;
            }
            Charset charset = Charset.forName("UTF-8");
            try {
                ByteBuffer buffer = charset.encode(feedback);
                Log.d("feedback", buffer);
                sc.write(buffer);
                Log.d("feedback已发送");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
