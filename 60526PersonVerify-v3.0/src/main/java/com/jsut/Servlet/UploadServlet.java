package com.jsut.Servlet;

import com.jsut.Utils.ConnectDatabase;
import com.jsut.Utils.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/upload")
@MultipartConfig()
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part=req.getPart("myfile");
        String filename=part.getSubmittedFileName();
        String suffix=filename.substring(filename.lastIndexOf("."));
        if(suffix.equals(".xls")){
            InputStream inputStream=part.getInputStream();
            FileOutputStream outputStream=new FileOutputStream("D:\\student.xls");
            System.out.println(outputStream);
            System.out.println("请求接受");
            byte[] bytes=new byte[1024];
            int length=0;
            while((length=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
            }
            outputStream.close();
            inputStream.close();
            ExcelUtil.importData("D:\\student.xls");
            req.getSession().removeAttribute("list");
            req.getSession().setAttribute("list", ConnectDatabase.getPersonData());
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write("数据导入成功！");
        }else{
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write("文件大小为:"+part.getSize()+"请上传xls文件");
        }
    }
}
