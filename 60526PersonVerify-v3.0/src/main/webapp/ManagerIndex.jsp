<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page  isELIgnored = "false" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件上传 jquery上传</title>

    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script>

        function DeleteTR(obj) {
            var table=obj.parentNode.parentNode.parentElement;
            var tr=obj.parentNode.parentNode;
            var conf=confirm("删除是不可恢复的，你确认要删除吗？");
            if(!conf)return;
            var List=tr.childNodes;
            var id=List[0].innerHTML;
            table.removeChild(tr);
            $.ajax({
                url:"/delete",
                type:"POST",
                data:{"id":"'"+id+"'"} ,
                success: function(data) {
                    alert(data);
                },
                error: function(data) {
                    alert("删除请求发生错误"+data);
                }
            })
        }

        function StartRegister() {
            //生成新窗口开始签到
        }

    </script>
</head>
<body>
<input type="file" name="uploadFile" id="uploadFile" value="导入数据"><br>
<a href="UseScanningGun.jsp">开始签到</a>
<div id="#personTable">
    <table border="1px" align="center">
        <tr><td>工号</td><td>姓名</td><td>性别</td><td>二级单位</td><td>办公地点</td><td>健康码颜色</td><td>工种</td><td>今日签到</td><td>操作</td></tr>
        <c:forEach items="${list}" var="person">
            <tr><td>${person.id}</td><td>${person.name}</td><td>${person.sex}</td><td>${person.school}</td><td>${person.dorm}</td><td>green</td><td>
                <c:if test="${person.type<=0}">
                    教职工
                </c:if>
                <c:if test="${person.type>0}">
                    学生
                </c:if>
            </td><td>${person.register}</td><td><a href="javascript:void(0);" onclick="DeleteTR(this)">删除</a> </td></tr>
        </c:forEach>
    </table>
</div>

<input type="button" name="Export" value="导出" onclick="Export()">



<script>
    function Export() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/export", true);
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhr.responseType = "blob";   //返回类型blob
        xhr.onload = function () {
            //定义请求完成的处理函数
            if (this.status === 200) {
                var blob = this.response;
                if(blob.size>0){
                    var reader = new FileReader();
                    reader.readAsDataURL(blob);   // 转换为base64，可以直接放入a标签href
                    reader.onload = function (e) {
                        // 转换完成，创建一个a标签用于下载
                        var a = document.createElement('a');
                        a.download = 'download.xls';
                        a.href = e.target.result;
                        $("body").append(a);    // 修复firefox中无法触发click
                        a.click();
                        $(a).remove();
                        window.location.reload();
                    }
                }else{
                    window.location.reload();
                }
            }
        };
        xhr.send("");
    }


    $("#uploadFile").on("change", function() {
        var formData = new FormData();                    // 创建一个form类型的数据
        formData.append("myfile",$("#uploadFile")[0].files[0]);     // 获取上传文件的数据
        $.ajax({
            url:"/upload",
            type:"POST",
            processData: false, // 将数据转换成对象，不对数据做处理，故 processData: false
            contentType: false,    // 不设置数据类型
            data: formData,
            success: function(data) {
                alert(data);
                createTable();
            },
            error: function(data) {
                alert("提交失败");
            }
        })
        function createTable() {
            var personTable=document.getElementById("#personTable");
            personTable.innerHTML="<table border=\"1px\" align=\"center\"><tr><td>工号</td><td>姓名</td><td>性别</td><td>二级单位</td><td>办公地点</td><td>健康码颜色</td><td>工种</td><td>今日签到</td><td>操作</td></tr><c:forEach items="${list}" var="person"> <tr><td>${person.id}</td><td>${person.name}</td><td>${person.sex}</td><td>${person.school}</td><td>${person.dorm}</td><td>green</td><td><c:if test="${person.type<=0}">教职工</c:if><c:if test="${person.type>0}">学生</c:if> </td><td>${person.register}</td><td><a href=\"javascript:void(0);\" onclick=\"DeleteTR(this)\">删除</a> </td></tr></c:forEach> </table>";
        }
    })
</script>
</body>
</html>

