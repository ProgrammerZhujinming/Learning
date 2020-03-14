package com.jsut.Servlet;

import com.jsut.Utils.ConnectDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id=req.getParameter("id");
        System.out.println("要删除的人id为:"+id);
        resp.setContentType("text/html;charset=utf-8");
        if(ConnectDatabase.deleteById(id)){
            resp.getWriter().write("删除成功");
        }
        else resp.getWriter().write("删除失败");
    }
}