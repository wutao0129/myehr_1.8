<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>任务增加</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="${pageContext.request.contextPath }/jsp/task/addTask/css/style.css">

  </head>
  
  <body>
    <div class="wrapper">

		<header>
			<input type="text" autofocus placeholder="Enter a task..." id="item">
			<div id='animationDiv'>
				<button id="add">
					<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"viewBox="0 0 16 16" style="enable-background:new 0 0 16 16;" xml:space="preserve"><g><path class="fill" d="M16,8c0,0.5-0.5,1-1,1H9v6c0,0.5-0.5,1-1,1s-1-0.5-1-1V9H1C0.5,9,0,8.5,0,8s0.5-1,1-1h6V1c0-0.5,0.5-1,1-1s1,0.5,1,1v6h6C15.5,7,16,7.5,16,8z"/></g></svg>
				</button>
			</div>
		</header>
		
		<div class="container">
			<ul class="todo" id="todo"></ul>
			<hr class = "divider">
			<ul class="todo" id="completed"></ul>
		</div>
		<script src="${pageContext.request.contextPath }/jsp/task/addTask/js/mo.min.js"></script>
		<script src="${pageContext.request.contextPath }/jsp/task/addTask/js/index.js"></script>
		<script src="${pageContext.request.contextPath }/jsp/task/addTask/js/jquery.min.js"></script>
		
	</div>
	<script>
	window.onload=function(){
		queryTask();
	}
	</script>
  </body>
</html>
