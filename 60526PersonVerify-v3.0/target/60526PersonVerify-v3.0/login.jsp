<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page  isELIgnored = "false" %>
<html>
<head>
    <title>登陆页面</title>
</head>
<body>
<form action="/login" method="post">
    <label for="username">用户名:</label><input type="text" name="username" id="username"><br>
    <label for="password">密码:</label><input type="password" name="password" id="password"><br>
    <input type="submit" value="提交">
    <h4 style="color: red">${loginMessage}<h4>
</form>
</body>
</html>