<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="utf-8">
<%@ include file="/common/_meta.jsp"%>
<title>错误页面</title>
</head>
<body>

	<div>
		非常抱歉，正在维护，稍后恢复 <br> <span style="padding: 0 5px;"> <%
 	Exception exception = (Exception) request.getAttribute("exception");
 out.print(exception);
 %></span>
	</div>
</body>
</html>
