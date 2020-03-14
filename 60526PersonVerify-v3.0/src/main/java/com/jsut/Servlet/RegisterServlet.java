package com.jsut.Servlet;

import com.jsut.Utils.ConnectDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static LocalDate startDate = LocalDate.of(2020, Month.MARCH, 6);//基准时间

    private ResultSet resultSet;

    public void success(){
        try {
            URL cb;
            File f = new File(ConnectDatabase.basePath+"success.wav"); //引号里面的是音乐文件所在的绝对路径
            cb = f.toURL();
            AudioClip aau;
            aau = Applet.newAudioClip(cb);//加载音频
            aau.play(); //播放音频
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void faliure(){
        try {
            URL cb;
            File f = new File(ConnectDatabase.basePath+"failure.wav"); //引号里面的是音乐文件所在的绝对路径
            cb = f.toURL();
            AudioClip aau;
            aau = Applet.newAudioClip(cb);//加载音频
            aau.play(); //播放音频
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    int getDateDif(){
        LocalDate today=LocalDate.now();
        String dateString=today.toString();
        dateString=dateString.replace("-","");
        int dif=(int)(today.toEpochDay()-startDate.toEpochDay());
        return dif;
    }

    static String getRegisterTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());					//放入Date类型数据
        String time=""+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
        return time;
    }


    private boolean queryById(String id) throws SQLException {
        LocalDate today=LocalDate.now();
        String dateString=today.toString();
        dateString=dateString.replace("-","");
        String columnName="Date"+dateString;
        resultSet= ConnectDatabase.statement.executeQuery("select * from UniversityPerson where id='"+id+"'");
        if(resultSet.next()) {
            String resiter=resultSet.getString(columnName);//register
            System.out.println("上一次的签到时间:"+resiter);
            if(resiter==null)resiter="";
            String column= LocalDate.now().toString().replace("-","");
            System.out.println("update UniversityPerson set Date"+column+"="+"'"+resiter+"  "+getRegisterTime()+"' where id='"+id+"'");
            ConnectDatabase.statement.executeUpdate("update UniversityPerson set Date"+column+"="+"'"+resiter+"  "+getRegisterTime()+"' where id='"+id+"'");
            System.out.println("通过!");
            success();
            return true;
        }
        else{
            System.out.println("不通过");
            faliure();
            return false;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id=req.getParameter("id");
        System.out.println("目前签到的id为:"+id);
        resp.setContentType("text/html;charset=utf-8");
        try {
            boolean result=queryById(id);
            if(result){
                resp.getWriter().write("签到成功!");
            }
            else{
                resp.getWriter().write("签到失败，查无此人!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("发生系统错误，签到失败，请联系管理员!");
        }

    }
}
