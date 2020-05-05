package server;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.util.JdbcUtils;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class dao {
    private static DataSource dbo;
    private static Jedis code;
    private static HashMap<String, String> map;

    static {
        map = new HashMap<String, String>();
        code = new Jedis("localhost", 6379);
        code.auth("123456");
        Properties properties = new Properties();
        InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            properties.load(in);
            dbo = DruidDataSourceFactory.createDataSource(properties);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int dao_signup(String sql1, String sql2) {
        ResultSet resultSet;
        try {
            Connection dbo0 = dbo.getConnection();
            Statement statement = dbo0.createStatement();
            resultSet = statement.executeQuery(sql1);
            if (resultSet.next()) {
                dbo0.close();
                return -2;
            }
            statement.executeUpdate(sql2);
            dbo0.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public int dao_login(String ID, String password) {
        try {
            Connection dbo0 = dbo.getConnection();
            String sql = "select * from battle_of_fly where ID=? and password=?";
            PreparedStatement preparedStatement = dbo0.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, password);
            ResultSet resultSet;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                map.put("ID", resultSet.getString("ID"));
                map.put("lv", resultSet.getString("lv"));
                code.hmset(resultSet.getString("ID"), map);
                dbo0.close();
                return 1;
            }
            dbo0.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double find_info(String ID){
        double k = 0;
        try {
            Connection dbo0 = dbo.getConnection();
            String sql="select * from battle_of_fly where ID=?";
            PreparedStatement preparedStatement=dbo0.prepareStatement(sql);
            preparedStatement.setString(1,ID);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            k= resultSet.getDouble("lv");
            dbo0.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return k;
    }
    public void update_info(String ID,String point){
        try {
            Connection dbo0 = dbo.getConnection();
            String sql="update battle_of_fly set lv=lv+? where ID=?";
            PreparedStatement preparedStatement=dbo0.prepareStatement(sql);
            preparedStatement.setString(1,point);
            preparedStatement.setString(2,ID);
            preparedStatement.executeUpdate();
            dbo0.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}