package Object;

import DataBase.OsDataBase;
import com.google.gson.Gson;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Order extends OrderSysObject {
    private long objectId;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public static final int WAIT = 0;
    public static final int DOING = 1;
    public static final int DONE = 2;
    public static final int CANCEL_BY_CUSTOMER = 3;
    public static final int UNRATE = 0;
    public static final int RATED = 1;
    private long customerId;
    private long supplierId;
    private long dishId;
    private int status;
    private String date;
    private String customerName;


    private String supplierName;
    private String dishName;
    private int isRated;
    private int star;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getIsRated() {
        return isRated;
    }

    public void setIsRated(int isRated) {
        this.isRated = isRated;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean save() {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("insert into Oorder (customerId, supplierId, dishId, status, date) values (?,?,?,?,?)");
            ps.setLong(1, customerId);
            ps.setLong(2, supplierId);
            ps.setLong(3, dishId);
            ps.setInt(4, status);
            ps.setString(5, date);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean update() {

        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("update Oorder set status = ?, isRated = ? where objectId = ?");

            ps.setInt(1, status);
            ps.setInt(2, isRated);
            ps.setLong(3, getObjectId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Order> getByCustomerId(long customerId) {
        List<Order> list = new ArrayList<Order>();
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Oorder where customerId = ?");

            ps.setLong(1, customerId);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setCustomerId(resultSet.getLong("customerId"));
                order.setSupplierId(resultSet.getLong("supplierId"));
                order.setDate(resultSet.getString("date"));
                order.setDishId(resultSet.getLong("dishId"));
                order.setStatus(resultSet.getInt("status"));
                order.setObjectId(resultSet.getLong("objectId"));
                order.setIsRated(resultSet.getInt("isRated"));
                list.add(order);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static List<Order> getBySupplierId(long supplierId) {
        List<Order> list = new ArrayList<Order>();
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Oorder where supplierId = ?");

            ps.setLong(1, supplierId);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setCustomerId(resultSet.getLong("customerId"));
                order.setSupplierId(resultSet.getLong("supplierId"));
                order.setDate(resultSet.getString("date"));
                order.setDishId(resultSet.getLong("dishId"));
                order.setStatus(resultSet.getInt("status"));
                order.setObjectId(resultSet.getLong("objectId"));
                order.setIsRated(resultSet.getInt("isRated"));
                list.add(order);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static Order getById(long Id) {

        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Oorder where objectId = ?");

            ps.setLong(1, Id);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setCustomerId(resultSet.getLong("customerId"));
                order.setSupplierId(resultSet.getLong("supplierId"));
                order.setDate(resultSet.getString("date"));
                order.setDishId(resultSet.getLong("dishId"));
                order.setStatus(resultSet.getInt("status"));
                order.setObjectId(resultSet.getLong("objectId"));
                order.setIsRated(resultSet.getInt("isRated"));
                return order;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Order castfromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Order.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerId=" + customerId +
                ", supplierId=" + supplierId +
                ", dishId=" + dishId +
                ", status=" + status +
                ", date='" + date + '\'' +
                '}';
    }
}
