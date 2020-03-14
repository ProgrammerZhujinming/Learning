package com.jsut.Servlet;

import com.jsut.Utils.ConnectDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        if(username.equals("zjm")&&password.equals("123")){
            req.setAttribute("loginMessage","");
            req.getSession().setAttribute("list", ConnectDatabase.getPersonData());
            req.getRequestDispatcher("ManagerIndex.jsp").forward(req,resp);
        }
        else{
            req.setAttribute("loginMessage","用户名或密码错误，请重试");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

}
