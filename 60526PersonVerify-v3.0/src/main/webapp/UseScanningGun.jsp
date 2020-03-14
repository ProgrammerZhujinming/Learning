<%--
  Created by IntelliJ IDEA.
  User: ZJM
  Date: 2020/3/8
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page  isELIgnored = "false" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>扫描签到</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script>
        $(document).onload=setInterval("getId()",500);

        function getId(){
            var id=$(".inputId").val();
            //alert(id);
            if(id.length>0){
                $.ajax({
                    url:"/register",
                    type:"POST",
                    data:{"id":id} ,
                    success: function(data) {
                        $(".inputId").val("");
                    },
                    error: function(data) {
                        alert("请求未送达"+data);
                    }
                })
            }
        }
    </script>
</head>
<body>
<input type="text" class="inputId" name="id">
</body>

</html>
