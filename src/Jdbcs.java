import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * @author fzkuji
 * @version 1.0.1
 * @ClassName Jdbcs.java
 * @Description 数据库连接类
 * @Param
 * @createTime 2019年10月09日 03:55:56
 */

public class Jdbcs {
    Connection con = null;
    Statement statement = null;
    ResultSet res = null;
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/RBAC?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    String name = "root";
    String passwd = "7895123";

    public Jdbcs() {
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, name, passwd);
            statement = con.createStatement();

        } catch (ClassNotFoundException e) {
            System.out.println("对不起，找不到这个Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //对用户信息的修改实际上就是对密码的修改
    public boolean update(String username1, String password1, String newpassword) {
        boolean judge = false;
        boolean s = compare(username1, password1);
        if (s) {
            String sql = "update users set password=\"" + newpassword + "\"where username=\"" + username1 + "\"";
            try {
                int a = statement.executeUpdate(sql);
                if (a == 1) {
                    JOptionPane.showMessageDialog(null, "密码修改成功！");
                    judge = true;
                }
                con.close();
                statement.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "用户不存在！");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "修改失败");
        }
        return judge;
    }

    //删除用户信息
    public void delete(String username, String password) {
        if (compare(username, password)) {
            JOptionPane.showMessageDialog(null, "已经完成删除");
        } else {
            return;
        }
        String sql = "delete from users where user_name=\"" + username + "\"";
        try {
            int a = statement.executeUpdate(sql);
            con.close();
            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "不存在该用户！");
            e.printStackTrace();
        }

    }

    //用户注册功能的实现，添加数据
    public void insert(String username, String password) {
        if (username == null || username.trim().length() <= 0) {
            JOptionPane.showMessageDialog(null, "注册账号为空，请重新输入！");
            return;
        }
        int a = 0;
        String sql_create = "insert into users(user_name,password) values(\"" + username + "\",\"" + password + "\")";
        try {
            a = statement.executeUpdate(sql_create);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "对不起该用户名已经有了！");
            e.printStackTrace();
        }

        int role_id = 3;
        int user_id = 0;
        String sql = "select user_id from users where user_name=\"" + username + "\"";
        try {
            res = statement.executeQuery(sql);
            res.next();
            user_id = res.getInt("user_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql_auth = "insert into user_role(user_id,role_id) values(\"" + user_id + "\",\"" + role_id + "\")";
        try {
            int b = statement.executeUpdate(sql_auth);
            con.close();
            statement.close();
            if (a == 1 && b == 1) {
                JOptionPane.showMessageDialog(null, "注册成功！");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "身份认证错误！");
            e.printStackTrace();
        }
    }

    //对比用户名和密码是不匹配
    public boolean compare(String username, String password) {
        boolean m = false;
        String sql = "select password from users where user_name=\"" + username + "\"";
        try {
            res = statement.executeQuery(sql);
            if (res.next()) {
                String pa = res.getString(1);
                if (pa.equals(password)) {
                    m = true;
                } else {
                    JOptionPane.showMessageDialog(null, "密码错误！");
                }
            } else {
                JOptionPane.showMessageDialog(null, "用户名不存在！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    //获取用户角色
    public int getrole(String username) {

        int role_id = 0;
        int user_id = 0;
        String sql = "select user_id from users where user_name=\"" + username + "\"";
        try {
            res = statement.executeQuery(sql);
            res.next();
            user_id = res.getInt("user_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql_1 = "select role_id from user_role where user_id=\"" + user_id + "\"";

        try {
            res = statement.executeQuery(sql_1);
            res.next();
            role_id = res.getInt(1);
            res.close();
            con.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role_id;
    }

}

