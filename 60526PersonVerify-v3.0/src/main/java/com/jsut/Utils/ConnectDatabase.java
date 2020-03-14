package com.jsut.Utils;

import com.jsut.Bean.Person;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class ConnectDatabase {

    private static Connection con = null;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://sh-cdb-n79dcwku.sql.tencentcdb.com:61267/Fanfan";
    private static String user = "root";
    private static String password = "zjm42393";//String password = 数据库密码;
    public static LocalDate startDate = LocalDate.of(2020, Month.MARCH, 6);//基准时间
    public static Statement statement=null;
    public static String basePath=ConnectDatabase.class.getClassLoader().getResource("").getPath();


    static{
        try {
            //加载驱动程序
            Class.forName(driver);
            //连接mysql
            con = DriverManager.getConnection(url,user,password);
            //判断数据库是否连接成功
            if(!con.isClosed()) {
                System.out.println("mysql连接成功");
                statement=con.createStatement();
            }
        }catch(ClassNotFoundException e) {
            System.out.println("载入JDBC驱动类出错");
            e.printStackTrace();
        }catch(SQLException e) {
            System.out.println("mysql连接出错");
            e.printStackTrace();
        }finally {

        }
    }

    public static int getDatedif() {
        LocalDate today=LocalDate.now();
        String dateString=today.toString();
        dateString=dateString.replace("-","");
        return (int)(today.toEpochDay()-startDate.toEpochDay());
    }

    public static void checkDate(){//检查日期 如果换天了 需要加一列
        LocalDate today=LocalDate.now();
        String dateString=today.toString();
        dateString=dateString.replace("-","");
        String columnName="Date"+dateString;
        int dif=(int)(today.toEpochDay()-startDate.toEpochDay());
        try {
            ResultSet resultSet=statement.executeQuery("select  * from UniversityPerson");
            if(resultSet.findColumn(columnName)>0)return;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ALTER TABLE UniversityPerson ADD COLUMN Date"+dateString+" text;");
            try {
                statement.executeUpdate("ALTER TABLE UniversityPerson ADD COLUMN "+columnName+" text;");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void deleteAll(){
        try {
            statement.execute("delete from UniversityPerson where 1=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        deleteAll();
    }


    public static void closeConnect(){
        try {
            con.close();
            System.out.println("mysql关闭成功");
        }catch(SQLException e) {
            System.out.println("关闭数据库连接出错");
            e.printStackTrace();
            con = null;
        }
    }

    public static ArrayList<Person> getPersonData(){
        checkDate();
        ArrayList<Person> list=new ArrayList<Person>();
        LocalDate today=LocalDate.now();
        String dateString=today.toString();
        dateString=dateString.replace("-","");
        try {
            ResultSet resultSet=statement.executeQuery("select * from UniversityPerson");
            while(resultSet.next()){
                Person person=new Person();
                person.setId(resultSet.getString(1));
                person.setName(resultSet.getString(2));
                person.setSex(resultSet.getString(3));
                person.setSchool(resultSet.getString(4));
                person.setDorm(resultSet.getString(5));
                person.setType(resultSet.getString(6));
                person.setRegister(resultSet.getString("Date"+dateString));
                System.out.println("person:"+person);
                list.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static boolean deleteById(String id){
        checkDate();
        try {
            statement.execute("delete from UniversityPerson where id="+id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
