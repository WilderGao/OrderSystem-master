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
public class Supplier extends OrderSysObject {
    private long objectId;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    private String name = null;
    private String password = null;
    private int status;
    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;
    public static final int ALREADY_ONLINE = 2;
    public static final int PASSWORD_ERR = 3;
    public static final int NOT_FOUND = 4;
    public static final int ALREADY_EXIST = 5;
    public static final int SUCCESS = 6;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean save() {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("insert into Supplier (name, password, status) values (?,?,?)");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setInt(3, status);
            return ps.executeUpdate() == 3;
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
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("update Supplier set password = ?,status = ? where objectId = ?");

            ps.setString(1, password);
            ps.setInt(2, status);
            ps.setLong(3, getObjectId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Supplier getByName(String name) {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Supplier where name = ?");
            ps.setString(1, name);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setObjectId(resultSet.getLong("objectId"));
                supplier.setName(resultSet.getString("name"));
                supplier.setPassword(resultSet.getString("password"));
                supplier.setStatus(resultSet.getInt("status"));
                return supplier;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Supplier");

            ResultSet resultSet = (ResultSet) ps.executeQuery();
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setObjectId(resultSet.getLong("objectId"));
                supplier.setName(resultSet.getString("name"));
                supplier.setPassword(resultSet.getString("password"));
                supplier.setStatus(resultSet.getInt("status"));
                list.add(supplier);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static Supplier getById(long objectId) {
        OsDataBase db = OsDataBase.getInstance();
        try {
            PreparedStatement ps = (PreparedStatement) db.getConnection().prepareStatement("select * from Supplier where objectId = ?");
            ps.setLong(1, objectId);
            ResultSet resultSet = (ResultSet) ps.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setObjectId(resultSet.getLong("objectId"));
                supplier.setName(resultSet.getString("name"));
                supplier.setPassword(resultSet.getString("password"));
                supplier.setStatus(resultSet.getInt("status"));
                return supplier;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Supplier castFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Supplier.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String login() {
        Supplier supplier = getByName(this.name);
        Gson gson = new Gson();
        //用户不存在
        if (supplier == null) {
            return NOT_FOUND + "!@#"+gson.toJson(this);
        }
        //密码不正确
        if (!supplier.password.equals(this.password)) {
            return PASSWORD_ERR + "!@#"+gson.toJson(this);
        }
        //已在别处登录
        if (supplier.status == ONLINE) {
            return ALREADY_ONLINE + "!@#"+gson.toJson(this);
        }
        //可以登录
        this.setObjectId(supplier.getObjectId());
        this.setStatus(ONLINE);
        this.update();
        return SUCCESS + "!@#"+gson.toJson(Supplier.getByName(this.name));
    }
    public int logout(){
        this.setStatus(0);
        this.update();
        return SUCCESS;
    }

    public int register() {
        Supplier supplier = getByName(this.name);
        //用户已经存在
        if (supplier != null) {
            return ALREADY_EXIST;
        }
        //注册
        this.save();
        return SUCCESS;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
