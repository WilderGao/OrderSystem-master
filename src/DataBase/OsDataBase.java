package DataBase;

import com.mysql.jdbc.Connection;

import Util.*;

import java.sql.DriverManager;


/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class OsDataBase {
    private static OsDataBase mOsDataBase = null;
    private static Connection mCon = null;

    private OsDataBase() {
    }

    public static OsDataBase getInstance() {

        if (mOsDataBase == null) {
            mOsDataBase = new OsDataBase();
            if (mOsDataBase.connect()) {
                Log.d("OsDataBase创建成功");
                return mOsDataBase;
            }
        }
        return mOsDataBase;
    }

    private boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            mCon = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/ordersystem", "root", "452tdjyninFF");
            Log.d("数据库链接成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public Connection getConnection(){
        return mCon;
    }
}
