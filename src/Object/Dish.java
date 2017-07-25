package Object;

import DataBase.OsDataBase;
import com.google.gson.Gson;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Dish extends OrderSysObject {
    private long objectId;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }
    private double cost;
    private double sellingPrice;
    private int gradeCount;
    private int gradeSum;
    private long supplierId;
    public static final int SUEECSS = 1;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(int gradeCount) {
        this.gradeCount = gradeCount;
    }

    public int getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(int gradeSum) {
        this.gradeSum = gradeSum;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    @Override

    public boolean save() {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("insert into Dish (cost, sellingPrice, gradeCount, gradeSum, supplierId, name) values (?,?,?,?,?,?)");
            ps.setDouble(1, cost);
            ps.setDouble(2, sellingPrice);
            ps.setInt(3, gradeCount);
            ps.setInt(4, gradeSum);
            ps.setLong(5, supplierId);
            ps.setString(6,name);
            return ps.executeUpdate() > 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete() {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("delete from Dish where objectId = ?");
            ps.setLong(1, getObjectId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update() {

        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("update Dish set cost = ?, sellingPrice = ?, gradeCount = ?, gradeSum = ?, name = ? where objectId = ?");
            ps.setDouble(1, cost);
            ps.setDouble(2, sellingPrice);
            ps.setInt(3, gradeCount);
            ps.setInt(4, gradeSum);
            ps.setString(5,name);
            ps.setLong(6, getObjectId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Dish> getAll() {
        List<Dish> list = new ArrayList<Dish>();
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Dish");
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setCost(resultSet.getDouble("cost"));
                dish.setGradeCount(resultSet.getInt("gradeCount"));
                dish.setGradeSum(resultSet.getInt("gradeSum"));
                dish.setSellingPrice(resultSet.getDouble("sellingPrice"));
                dish.setSupplierId(resultSet.getLong("supplierId"));
                dish.setObjectId(resultSet.getLong("objectId"));
                dish.setName(resultSet.getString("name"));
                list.add(dish);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static List<Dish> getBySupplierId(long supplierId) {
        List<Dish> list = new ArrayList<Dish>();
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Dish where supplierId = ?");
            ps.setLong(1, supplierId);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setCost(resultSet.getDouble("cost"));
                dish.setGradeCount(resultSet.getInt("gradeCount"));
                dish.setGradeSum(resultSet.getInt("gradeSum"));
                dish.setSellingPrice(resultSet.getDouble("sellingPrice"));
                dish.setSupplierId(resultSet.getLong("supplierId"));
                dish.setObjectId(resultSet.getLong("objectId"));
                dish.setName(resultSet.getString("name"));
                list.add(dish);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }
    public static Dish getById(long Id) {

        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Dish where objectId = ?");
            ps.setLong(1, Id);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setCost(resultSet.getDouble("cost"));
                dish.setGradeCount(resultSet.getInt("gradeCount"));
                dish.setGradeSum(resultSet.getInt("gradeSum"));
                dish.setSellingPrice(resultSet.getDouble("sellingPrice"));
                dish.setSupplierId(resultSet.getLong("supplierId"));
                dish.setObjectId(resultSet.getLong("objectId"));
                dish.setName(resultSet.getString("name"));
                return dish;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Dish castfromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Dish.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "cost=" + cost +
                ", sellingPrice=" + sellingPrice +
                ", gradeCount=" + gradeCount +
                ", gradeSum=" + gradeSum +
                ", supplierId=" + supplierId +
                ", name='" + name + '\'' +
                '}';
    }
}
