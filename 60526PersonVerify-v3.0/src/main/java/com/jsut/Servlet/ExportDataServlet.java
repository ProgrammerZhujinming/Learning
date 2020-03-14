package com.jsut.Servlet;

import com.jsut.Bean.Person;
import com.jsut.Utils.ConnectDatabase;
import com.sun.rowset.internal.Row;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/export")
public class ExportDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("接收到了请求");
        int i,row=0;
        WritableWorkbook workbook = Workbook.createWorkbook(new File("D:\\download.xls"));
        WritableSheet sheet = workbook.createSheet("学生教职工签到信息表", 0);

        try {
            sheet.addCell(new Label(0,0,"个人id"));
            sheet.addCell(new Label(1,0,"姓名"));
            sheet.addCell(new Label(2,0,"性别"));
            sheet.addCell(new Label(3,0,"学院"));
            sheet.addCell(new Label(4,0,"住址"));
            sheet.addCell(new Label(5,0,"类型"));
            int need=ConnectDatabase.getDatedif();
            for(i=1;i<=need;++i){
                String CreateDate=(ConnectDatabase.startDate.plusDays(i).toString());
                sheet.addCell(new Label(i+5,0,CreateDate));
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet=ConnectDatabase.statement.executeQuery("select * from UniversityPerson");
            while(resultSet.next()){
                ++row;
                ResultSetMetaData rsmd = resultSet.getMetaData() ;
                int columnCount = rsmd.getColumnCount();//ResultSet的总列数
                for(i=1;i<=columnCount;++i){
                    sheet.addCell(new Label(i-1,row,resultSet.getString(i)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }


        /*优化策略 把读出的所有的数据都存成String*/

        workbook.write();
        try {
            workbook.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream=new FileInputStream("D:\\download.xls");
        resp.setHeader("Content-Disposition", "attachment;filename=download.xls");
        int len=0;
        byte[] bytes=new byte[1024];
        ServletOutputStream servletOutputStream=resp.getOutputStream();
        while((len=fileInputStream.read(bytes))>0){
            servletOutputStream.write(bytes,0,len);
        }
        //关闭资源
        servletOutputStream.close();
        fileInputStream.close();
    }
}
