<%@ page language="java" import="java.util.*" import="com.myehr.common.util.LangUtil" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%
	HttpSession s = request.getSession();
	String userName=(String)session.getAttribute("userName");
	String userCode=(String)session.getAttribute("userCode");
	String softwareTitle=(String)session.getAttribute("softwareTitle");
	String empid=session.getAttribute("empid")+"";
	String userId=session.getAttribute("userid")+"";
%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
.secondCard{
	min-height:700px;
}
	.personInfo{
		padding:0 0 50px 0 !important;
		position:relative;
		margin-bottom:20px;
		flex:none !important;
	}
	.personBackPic{
		border-bottom: 3px solid #5cc0ff;
		width:100%;
	}
	.personBackPic img{
		width:100%;
	}
	.goodSay{
		position: absolute;
		top: 0;
		background-color: rgba(0, 0, 0, 0.6);
		width: 100%;
		height: 100%;
		padding: 1.2em 1em 0 1em;
		color: white;
		text-align: center;
		opacity: 0;
		-webkit-transition-property: all;
		-moz-transition-property: all;
		-o-transition-property: all;
		transition-property: all;
		-webkit-transition-duration: 0.3s;
		-moz-transition-duration: 0.3s;
		-o-transition-duration: 0.3s;
		transition-duration: 0.3s;
		-webkit-transition-timing-function: ease-in;
		-moz-transition-timing-function: ease-in;
		-o-transition-timing-function: ease-in;
		transition-timing-function: ease-in;
	}
	.personPhoto{
		width:100px;
		height:100px;
		margin:0 auto;
		position:absolute;
		bottom:3px;
		right:0;
		left:0;
	}
	.personPhoto img{
		width: 100%;
		height:100%;
		border: 3px solid #5cc0ff;
		-webkit-border-radius: 50%;
		-moz-border-radius: 50%;
		-ms-border-radius: 50%;
		-o-border-radius: 50%;
		border-radius: 50%;
		overflow: hidden;
		position: relative;
	}
	.personContent{
		margin-bottom:30px;
	}
	.contentsx{
		width:200px;
		margin:0 auto;
	}
	.contentx{
		display:block;
		text-align:center;
	}
	.photoUpload{
		position: absolute;
		cursor: pointer;
		width: 100%;
		height: 100%;
		background-color: #3498db;
		top: 0;
		font-size: 2.8em;
		text-align: center;
		color: white;
		line-height: 64px;
		opacity: 0;
		font-family: 'FontAwesome';
		font-weight: 300;
		border: 3px solid #5cc0ff;
		-webkit-border-radius: 50%;
		-moz-border-radius: 50%;
		-ms-border-radius: 50%;
		-o-border-radius: 50%;
		border-radius: 50%;
		-webkit-transition-property: all;
		-moz-transition-property: all;
		-o-transition-property: all;
		transition-property: all;
		-webkit-transition-duration: 0.5s;
		-moz-transition-duration: 0.5s;
		-o-transition-duration: 0.5s;
		transition-duration: 0.5s;
		-webkit-transition-timing-function: ease;
		-moz-transition-timing-function: ease;
		-o-transition-timing-function: ease;
		transition-timing-function: ease;
	}

	.menuIconx{
		height:100px;
	}
	.menuIcon{
		height:100%;
		cursor: pointer;
	}
	.menuIcon i{
		font-size: 32 !important;
		width: 32;
		margin: 0 auto;
		display: flex;
	}
	.menuIcon p{
		margin: 0 auto;
		text-align: center;
	}
	.customMenu2{
		margin-right:0px !important;
	}
</style>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/js/fullcalendar/custom.css" type="text/css"></link>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/js/fullcalendar/fullcalendar.css" type="text/css"></link>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/js/fullcalendar/fullcalendar.print.css" type="text/css"></link>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/js/fullcalendar/animate.css" type="text/css"></link>
  <link rel="stylesheet" href="../icon/style.css?111" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath }/common/css/gridbycard/bootstrap.min.css" type="text/css"></link>
<link rel="stylesheet" href="xx.css" type="text/css"></link>
<link rel="stylesheet" href="../icon/iconfont.css" type="text/css"></link>
<script type="text/javascript" src="../../js/example/js/jquery-1.10.2.min.js"></script>
<link  href="../icon/cropper.css" rel="stylesheet">
<script src="../icon/cropper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/common/css/gridbycard/bootstrap.min.js"></script>
  <script src="<%= request.getContextPath() %>/js/fullcalendar/content.js" type="text/javascript"></script>
  <script src="<%= request.getContextPath() %>/js/fullcalendar/jquery-ui.custom.min.js" type="text/javascript"></script>
  <script src="<%= request.getContextPath() %>/js/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
  <script src="<%= request.getContextPath() %>/js/fullcalendar/date-format.js" type="text/javascript"></script>
<body style="font-size:14px ">
<div class="page-wrapper" style="display: block;">
	<!-- ============================================================== -->
	<!-- Bread crumb and right sidebar toggle -->
	<!-- ============================================================== -->
	<div class="page-breadcrumb">
		<div class="row">
			<div class="col-5 align-self-center">
				<h4 class="page-title">Dashboard</h4>
			</div>
			<div class="col-7 align-self-center">
				<div class="d-flex align-items-center justify-content-end">
					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item">
								<a href="#">Home</a>
							</li>
							<li class="breadcrumb-item active" aria-current="page">Dashboard</li>
						</ol>
					</nav>
				</div>
			</div>
		</div>
	</div>
	<!-- ============================================================== -->
	<!-- End Bread crumb and right sidebar toggle -->
	<!-- ============================================================== -->
	<!-- ============================================================== -->
	<!-- Container fluid  -->
	<!-- ============================================================== -->
	
	<div class="container-fluid">
		<!-- ============================================================== -->
		<!-- Sales chart -->
		<!-- ============================================================== -->
		<div class="card-group">
			<div class="card">
				<div class="card-body">
					<div class="row">
						<div class="col-md-12">
							<div class="d-flex no-block align-items-center">
								<div>
									<i class="mdi mdi-emoticon font-20 text-muted"></i>
									<p class="font-16 m-b-5">New Clients</p>
								</div>
								<div class="ml-auto">
									<h1 class="font-light text-right">23</h1>
								</div>
							</div>
						</div>
						<div class="col-12">
							<div class="progress">
								<div class="progress-bar bg-info" role="progressbar" style="width: 75%; height: 6px;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Column -->
			<!-- Column -->
			<div class="card">
				<div class="card-body">
					<div class="row">
						<div class="col-md-12">
							
						</div>
						<div class="col-12">
							<div class="progress">
								<div class="progress-bar bg-success" role="progressbar" style="width: 60%; height: 6px;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Column -->
			<!-- Column -->
			<div class="card">
				<div class="card-body">
					<div class="row">
						<div class="col-md-12">
							<div class="d-flex no-block align-items-center">
								<div>
									<i class="mdi mdi-currency-eur font-20 text-muted"></i>
									<p class="font-16 m-b-5">New Invoices</p>
								</div>
								<div class="ml-auto">
									<h1 class="font-light text-right">157</h1>
								</div>
							</div>
						</div>
						<div class="col-12">
							<div class="progress">
								<div class="progress-bar bg-purple" role="progressbar" style="width: 65%; height: 6px;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Column -->
			<!-- Column -->
			<div class="card">
				<div class="card-body">
					<div class="row">
						<div class="col-md-12">
							
						</div>
						<div class="col-12">
							<div class="progress">
								<div class="progress-bar bg-danger" role="progressbar" style="width: 70%; height: 6px;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- ============================================================== -->
		<!-- Sales chart -->
		<!-- ============================================================== -->
		<!-- ============================================================== -->
		<!-- Email campaign chart -->
		<!-- ============================================================== -->
		<div class="row">
		
		
		
			<div class="col-lg-3 col-md-6">
				<div class="card secondCard">
					<div class="card-body personInfo">
						<!--背景-->
						<div id="campaign" class=" personBackPic">
							<img class="" src="http://localhost:9999/其他/up.jpg" alt="background"/>
							<div class="goodSay">
								<h3>@WT</h3>
								<p>没有什么人能打败你,除了你自己!!!安排</p>
							</div>
						</div>
						<!--头像-->
						<div id="personPhoto" class="personPhoto">
							<img src="http://116.62.243.28:9875/人员照片/系统管理员_sysadmin.jpg" alt="personPhoto"/>
							<div class="photoUpload">
								<div class="iconfont icon-sheshengxiao" style="font-size:40px;line-height:100px">
							
								</div>
							</div>
						</div>
					</div>
					<!--文本信息-->
					<div id="personContent" class="personContent">
						<div class="contentsx">
							<div class="contentx">
								<p>SYSADMIN</p>
							</div>
							<div class="contentx">
								<p>栗东东</p>
							</div>
							<div class="contentx">
								<p>20多</p>
							</div>
							<div class="contentx">
								<p>陕西洛川</p>
							</div>
							<div class="contentx">
								<p>实施骨干</p>
							</div>
							<div class="contentx">
								<p>14288888888</p>
							</div>
							<div class="contentx">
								<p>675438912@qq.com</p>
							</div>
							<div class="contentx">
								<p>九月九日</p>
							</div>
							<div class="contentx">
								<p>单身</p>
							</div>
							<div class="contentx">
								<p>自觉心是进步之母，自贱心是堕落之源，故自觉心不可无，自贱心不可有</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			
			
			
			
			
		<!--
		</div>
			
		<div class="row">
			<!-- column -->
			<div class="col-lg-6 waitProcessParent">
				<div class="card secondCard">
					<div class="card-body">
						<h4 class="card-title">待办流程</h4>
					</div>
					<div class="comment-widgets scrollable ps-container ps-theme-default waitProcess" style="height:450px;" data-ps-id="bdc7b288-a9a2-7cdc-e8cf-e439e93ee41f">
						<!-- Comment Row -->
					</div>
				</div>
			</div>
			<!-- column -->
			<div class="col-lg-6 customMenuParent">
				<div class="card customMenu secondCard">
					
				</div>
			</div>
			
			<!-- column -->
			<div class="col-lg-3">
				<div class="card secondCard">
					<div class="card-body">
						<div class="d-flex align-items-center p-b-15">
							<div>
								<h4 class="card-title m-b-0">To Do List</h4>
							</div>
							<div class="ml-auto">
								<div class="dl">
									<select class="custom-select border-0 text-muted">
										<option value="0" selected="">August 2018</option>
										<option value="1">May 2018</option>
										<option value="2">March 2018</option>
										<option value="3">June 2018</option>
									</select>
								</div>
							</div>
						</div>
						<div class="todo-widget scrollable ps-container ps-theme-default" style="height:422px;" data-ps-id="f4dfdb5c-9716-69e8-3d17-9405ad0b8617">
							<ul class="list-task todo-list list-group m-b-0"  id="allTaskList" data-role="tasklist">
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck">
										<label class="custom-control-label todo-label" for="customCheck">
											<span class="todo-desc">Simply dummy text of the printing and typesetting</span> <span class="badge badge-pill badge-success float-right">Project</span>
										</label>
									</div>
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck1">
										<label class="custom-control-label todo-label" for="customCheck1">
											<span class="todo-desc">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been.</span><span class="badge badge-pill badge-danger float-right">Project</span>
										</label>
									</div>
									
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck2">
										<label class="custom-control-label todo-label" for="customCheck2">
											<span class="todo-desc">Ipsum is simply dummy text of the printing</span> <span class="badge badge-pill badge-info float-right">Project</span>
										</label>
									</div>
									
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck3">
										<label class="custom-control-label todo-label" for="customCheck3">
											<span class="todo-desc">Simply dummy text of the printing and typesetting</span> <span class="badge badge-pill badge-info float-right">Project</span>
										</label>
									</div>
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck4">
										<label class="custom-control-label todo-label" for="customCheck4">
											<span class="todo-desc">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been.</span> <span class="badge badge-pill badge-purple float-right">Project</span>
										</label>
									</div>
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck5">
										<label class="custom-control-label todo-label" for="customCheck5">
											<span class="todo-desc">Ipsum is simply dummy text of the printing</span> <span class="badge badge-pill badge-success float-right">Project</span>
										</label>
									</div>
								</li>
								<li class="list-group-item todo-item" data-role="task">
									<div class="custom-control custom-checkbox">
										<input type="checkbox" class="custom-control-input" id="customCheck6">
										<label class="custom-control-label todo-label" for="customCheck6">
											<span class="todo-desc">Simply dummy text of the printing and typesetting</span> <span class="badge badge-pill badge-primary float-right">Project</span>
										</label>
									</div>
								</li>
							</ul>
						<div class="ps-scrollbar-x-rail" style="left: 0px; bottom: 0px;"><div class="ps-scrollbar-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps-scrollbar-y-rail" style="top: 0px; right: 3px;"><div class="ps-scrollbar-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
					</div>
				</div>
			</div>
			
			
			<div class="col-lg-6 col-md-6 col-sm-12">
				<div class="card">
					<div class="card-body">
					<h4 class="card-title">新闻制度</h4>
						<ul class=" nav nav-tabs  m-a-15 justify-content-end m-b-15" id="myTab">
							<li class=" nav-item "> <a href="#content" class="nav-link " data-toggle="tab" aria-expanded="false"><h5>人事政策</h5></a> </li>
							<li class="nav-item "> <a href="#contenta" class="nav-link" data-toggle="tab" aria-expanded="false"><h5>娱乐新闻</h5></a> </li>
							<li class="nav-item "> <a href="#contentb" class="nav-link" data-toggle="tab" aria-expanded="flase"><h5>焦点新闻</h5></a> </li>
							<input class="form-control" type="hidden" style="width:200px;position:absolute;top:10px;right:20px;" placeholder="标题" id="ANNOUNCEMENT_TITLE" onchange="loaddata()"/>
						</ul>
						<br> 
						<br> 
						<div class="tab-content br-n pn">
							<div id="content" class="tab-pane active" aria-expanded="true">
								<div class="row">
								   <div class="col-md-4"> <img src="../icon/1.png" class="img-responsive thumbnail m-r-15"> </div>
									<ul class="col-md-8" id="contenaa" ></ul>
								</div>
							</div>
							<div id="contenta" class="tab-pane " aria-expanded="false">
								<div class="row">
									<ul class="col-md-8" id="contentaa"></ul>
									 <div class="col-md-4"> <img src="../icon/2.png" class="img-responsive thumbnail m-r-15"> </div>
									 </div>
							</div>
							<div id="contentb" class="tab-pane " aria-expanded="false">
								<div class="row">
									<div class="col-md-4"> <img src="../icon/3.png" class="img-responsive thumbnail m-r-15"> </div>
								   
									<ul class="col-md-8" id="contentbb"></ul>
								</div>
							</div>
							 <br> 
							<input type="button"class="btn btn-dribbble waves-effect btn-rounded waves-light" style="position:absolute;bottom:5px;right:20px" onclick="showNews()" value="More"/>
							
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-lg-3 col-md-6 col-sm-12">
				<div class="card">
					<div class="card-body">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h4 class="card-title">工作日历</h4>
							</div>
							<div class="ibox-content">
								<div id="calendar"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			
		</div>
		<!-- ============================================================== -->
		<!-- Recent comment and chats -->
		<!-- ============================================================== -->
	</div>
	<!-- ============================================================== -->
	<!-- End Container fluid  -->
	<!-- ============================================================== -->
	<!-- ============================================================== -->
	<!-- footer -->
	<!-- ============================================================== -->
	<footer class="footer text-center">
		All Rights Reserved by Nice admin. Designed and Developed by
		<a href="https://wrappixel.com">WrapPixel</a>.
	</footer>
	<!-- ============================================================== -->
	<!-- End footer -->
	<!-- ============================================================== -->
</div>
<script>		
$(function() {
	var data = getAllData();
	initWaitLines(data);
	initMenu(data);
	initNewsData(data);
	inittaskdate(data);
	initWorkDate();
	$(".waitProcess,.customMenu2").hover(function(){
		$(this).css("overflow","auto");
	},function(){
		$(this).css("overflow","hidden");
	})
	$(".menuIcon").hover(function(){
		$(this).css("background-color","rgba(0,0,0,.03)");
	},function(){
		$(this).css("background-color","#FFF");
	})
	$(".personBackPic").hover(function(){
		$(this).find(".goodSay").css("opacity","0.8");
	},function(){
		$(this).find(".goodSay").css("opacity","0");
	})	
	$(".photoUpload").hover(function(){
		$(this).css("opacity","0.8");
	},function(){
		$(this).css("opacity","0");
	})	
	$('#myTab a').click(function (e) {
			e.preventDefault();
			var x = $(this)[0].innerHTML;
            $(this).tab('show');
	} )
	loaddata();
	
});
	
function initNewsData(data){
	var list = data.newsDatas;
	var count=0;
		var temp = '';
		var temps='';
		var tempa='';
		var tempb=''
		for(var j=0; j<list.length;j++){ 
		  if(list[j].state==1){
		  count++;
		 if(count<9){
			temp += '<li class="nav-item" style="height:30px;margin:5px 0;list-style:none;">'+
					'	<p style="float:left;">['+formatDateTime(list[j].createTime)+']</p><p style="float:left;overflow-x:hidden;"><a href="#" title="'+list[j].title+'" onclick="show('+list[j].id+')">'+list[j].title.substring(0,16)+'</a></p>'+
					'</li>';
		 }
		   $("#contenaa").html(temp);
		  }
			if(list[j].state==2){
		   
			temps += '<li class="nav-item" style="height:30px;margin:5px 0;list-style:none;">'+
					'	<p style="float:left;width:100px">['+formatDateTime(list[j].createTime)+']</p><p style="float:left;"><a  href="#" title="'+list[j].title+'" onclick="show('+list[j].id+')">'+list[j].title.substring(0,16)+'</a></p>'+
					'</li>';
		   }
		 $("#contentaa").html(temps);
		 if(list[j].state==3){
		   
			tempa += '<li class="nav-item" style="height:30px;margin:5px 0;list-style:none;">'+
					'	<p style="float:left;width:100px">['+formatDateTime(list[j].createTime)+']</p><p style=";float:left;"><a  href="#" title="'+list[j].title+'" onclick="show('+list[j].id+')">'+list[j].title.substring(0,16)+'</a></p>'+
					'</li>';
					 $("#contentbb").html(tempa);
		   }
		   if(list[j].state==4){
		   
			tempb += '<li class="nav-item" style="height:30px;margin:5px 0;list-style:none;">'+
					'	<p style="float:left;width:100px">['+formatDateTime(list[j].createTime)+']</p><p style="float:left;"><a href="#" onclick="show('+list[j].id+')">'+list[j].title+'</a></p>'+
					'</li>';
					 $("#contentxx").html(tempb);
		   }
		
		}
		
	}
	function formatDateTime(inputTime) {    
		var date = new Date(inputTime);  
		var y = date.getFullYear();    
		var m = date.getMonth() + 1;    
		m = m < 10 ? ('0' + m) : m;    
		var d = date.getDate();    
		d = d < 10 ? ('0' + d) : d;    
		var h = date.getHours();  
		h = h < 10 ? ('0' + h) : h;  
		var minute = date.getMinutes();  
		var second = date.getSeconds();  
		minute = minute < 10 ? ('0' + minute) : minute;    
		second = second < 10 ? ('0' + second) : second;   
		return y + '-' + m + '-' + d;   
	};  
	function show(id){
		var idx = id+1000;///myehr_0_SQLSERVER/WebRoot/jsp/announcement/announcementDetail.jsp
		parent.addTab(idx,"新闻详情","/myehr/jsp/announcement/announcementDetail.jsp?id="+id);
	}	
	function showNews(){
		var url = '/myehr/jsp/announcement/ShowAnnouncements.jsp';
		layer.open({
			type:2,
			closeBtn:1,
			shadeClose:true,
			area:['85%','85%'],
			maxmin:true,
			title:'公告',
			content:url,
			success:function(layero,index){			
			}
		 }); 
	}
	
	function getAllData(){
		var datas = {};
		$.ajax({
			url: "/myehr/dragconfig/getDragAllDatas.action",
			type: 'post',
			cache: false,
			async: false,
			success: function (data) {
				datas = data;
			 }
		});
		return datas;
	}
	
	function initWaitLines(e){
		var datas = e.waitDatas.rows; 
		if(datas.length==0){
			$(".waitProcessParent").css("display","none");
			return
		}
		for(var i = 0 ; i < datas.length&&i < 5 ; i++){
			var data = datas[i];
			var cell = '';
				cell =  '<div class="d-flex flex-row comment-row m-t-0">'+
						'	<div class="p-2">'+
						'		<img src="'+data.createrPhoto+'" onerror="src=\'http://localhost:9999/人员照片/系统管理员_sysadmin.jpg\'" alt="user" width="50" class="rounded-circle">'+
						'	</div>'+
						'	<div class="comment-text w-100">'+
						'		<h6 class="font-medium">'+data.title+'</h6>'+
						'		<span class="m-b-15 d-block">'+data.prevAssigneeName+'&nbsp&nbsp&nbsp&nbsp'+data.status+'</span>'+
						'		<div class="comment-footer">'+
						'			<span class="text-muted float-right">'+data.createAgo+'</span>'+
						'			<span class="label label-rounded label-primary">紧急</span>'+
						'			<span class="action-icons">'+
						'				<a href="javascript:void(0)" target="_blank" title="流程图">'+
						'					<i class="iconfont icon-liuchengtu "></i>'+
						'				</a>'+
						'				<a href="javascript:void(0)" title="通过">'+
						'					<i class="iconfont icon-pass-1-copy "></i>'+
						'				</a>'+
						'				<a href="javascript:void(0)" title="驳回">'+
						'					<i class="iconfont icon-notpass-1-copy " ></i>'+
						'				</a>'+
						'			</span>'+
						'		</div>'+
						'	</div>'+
						'</div>';
			$(".waitProcess").append(cell);
		}
		
	}
	
	function initMenu(datas){
		var str = datas.dragMenus['桌面流程发起'];
		if(str.length==''){
			$(".customMenuParent").css("display","none");
			return
		}
		var objs = JSON.parse(str);
			objs = objs.data;
		if(objs.length>=1){//带二级菜单
			for(var i=0 ; i<objs.length ; i++){
				var obj = objs[i];
				var cell = '';
				cell =  '<div class="card-body">'+
					    '	<h4 class="card-title">'+obj.menuName+'</h4>'+
						'</div>'+
						'<div class="comment-widgets scrollable ps-container ps-theme-default customMenu2 row data-ps-id="bdc7b288-a9a2-7cdc-e8cf-e439e93ee41f">';
				var sonObjs = obj.childrens;
				for(var k=0 ;k<sonObjs.length; k++){
					var sonObj = sonObjs[k];
					cell+=  '<div class="col-lg-3 col-md-4 col-sm-6 menuIconx" title="'+sonObj.menuName+'">'+
							'	<div class="menuIcon" >'+
							'		<i class=" iconfont icon-pass-1-copy "></i>'+
							'		<p >'+sonObj.menuName+'</p>'+
							'	</div>'+
							'</div>';
				}	
				cell+=  '</div>';
				$(".customMenu").append(cell);
			}
		}else{
			for(var i=0 ; i<objs.length ; i++){
				var data = objs[i];
				var cell = '';
				cell =  '<div class="col-lg-3 col-md-4 col-sm-6 menuIconx" title="'+data.menuName+'">'+
						'	<div class="menuIcon" >'+
						'		<i class=" iconfont icon-pass-1-copy "></i>'+
						'		<p >'+data.menuName+'</p>'+
						'	</div>'+
						'</div>';
				$(".customMenu2").append(cell);
				$(".customMenu2").append(cell);
			}
		}
	}

								
	function inittaskdate(data){
		var list = data.taskDatas;
		var liListHtml = '';
		for(var i=0;i<list.length;i++){
			var time = "";
			time = formatDateTime(list[i].sysTaskCreateTime);
			var sysTaskUrgencyHtml='';
			if(list[i].sysTaskUrgency=='1'){
				sysTaskUrgencyHtml="danger-element";
			}else if(list[i].sysTaskUrgency=='2'){
				sysTaskUrgencyHtml="warning-element";
			}else{
				sysTaskUrgencyHtml="success-element";
			}
			var approveHtml='';
			if(list[i].procTaskId!=null && list[i].procTaskId!=''){
				var obj = list[i];
				approveHtml='        <a href="javascript:void(0);" onclick ="approveProcess(this)" procFormId="'+obj.procFormId+'" procTaskId="'+obj.procTaskId+'" procInsId="'+obj.procInsId+'" procBusinessId="'+obj.procBusinessId+'" class="pull-right btn btn-xs btn-primary">审批</a>';
			}
			var openFormHtml='';
			if(list[i].sysTaskFormid!=null && list[i].sysTaskFormid!=''){
				var obj = list[i];
				var sysTaskFormid = obj.sysTaskFormid;
				var aa = sysTaskFormid.split(":");
			}
			liListHtml+='<li class="list-group-item todo-item '+sysTaskUrgencyHtml+'"data-role="task" style="background-color:#fff">'+
						'<div class="custom-control custom-checkbox">'+
						'<input type="checkbox" class="custom-control-input" id="customCheck'+i+'" onclick="openForm(this)" value="'+list[i].sysTaskId+'">'+
						'<label class="custom-control-label todo-label" for="customCheck'+i+'">'+
						'<div>'+
						'<span class="todo-desc float-left" style="width:400px;">'+
						'    '+list[i].sysTaskContent+
						'</span>'+time+
						'<span>'+
						'</div>'+
						'<span class="badge badge-pill badge-success "style="/* margin-right: 72px; */float: right;margin-top:-23px;" >完成</span>'+
						'</label>'+
						'    <h5 class="agile-detail">'+
						approveHtml+
						openFormHtml+
					//	'        <i class="fa fa-clock-o"></i>'+time+
						'    </h5></div>'+
						'</li>';
		}
		$("#allTaskList").html(liListHtml);
	}
							
								
	function openForm(obj){
		//$(obj).parent().children().children()[0];
		if($(obj).parent().children().children()[1].style.textDecorationStyle==""){
			$(obj).parent().children().find("span").css("text-decoration","line-through");
			$(obj).parent().children().find("span").css("text-decoration-color","red");
			var va = $(obj).value;
			$.ajax({
				url:'sysTask/checkSysTask.action?sysTaskId='+va,
				type:'post',
				cache: false,
				async: false,
				contentType: 'application/json;charset=utf-8',
				success: function (text) {
	
				}
			});
		}else{
			$(obj).parent().children().find("span").css("text-decoration","");
			var va = $(obj).parent().parent().children()[0].value;
				$.ajax({
				
					url:'sysTask/checkSysTask1.action?sysTaskId='+va,
					type:'post',
					cache: false,
					async: false,
					contentType: 'application/json;charset=utf-8',
					success: function (text) {
		
					}});
		}
	}							
								
	function tomore(){
		window.location.href="${pageContext.request.contextPath }/jsp/task/taskManage/agileBoard.jsp";
	}	

	
	
	var date = new Date();
    var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	var userId=1;
	
	var A0190 = getUrlParam().substring(getUrlParam().indexOf("=")+1);
	
	var width=window.screen.width;
	document.getElementById("mainContent").style.width=width + "px";
	
	var startDay;
	var selectStart;
	var selectEnd;
	var selectAllDay;

	function initWorkDate(data){
		var calendar = $('#calendar').fullCalendar({
			header: {
				left: 'prev,next',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			lang: 'zh-cn',
			buttonIcons: false, // show the prev/next text
			weekMode: 'liquid',
			editable: false,
			selectable: true,
			defaultView: 'month',
			droppable: true, // this allows things to be dropped onto the calendar !!!
			drop: function (date, allDay) { // this function is called when something is dropped
				// retrieve the dropped element's stored Event Object
				var originalEventObject = $(this).data('eventObject');
				
				// we need to copy it, so that multiple events don't have a reference to the same object
				var copiedEventObject = $.extend({}, originalEventObject);

				// assign it the date that was reported
				copiedEventObject.start = date;
				copiedEventObject.allDay = allDay;

				// render the event on the calendar
				// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
				$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

				// is the "remove after drop" checkbox checked?
				if ($('#drop-remove').is(':checked')) {
							// if so, remove the element from the "Draggable Events" list
					$(this).remove();
				}
			},
			selectHelper: true,
			select: function(start, end, allDay) {
				selectStart=start;
				selectEnd=end;
				selectAllDay=allDay;
				var aaa=formatDate(end);
				var bbb=aaa.substring(aaa.length-2,aaa.length-1);
				if(bbb==0){
					document.getElementById("updateWork").style.top='150px';
				}
				if(bbb==1){
					document.getElementById("updateWork").style.top='350px';
				}
				if(bbb==2){
					document.getElementById("updateWork").style.top='550px';
				}

				document.getElementById("updateWork").style.display="";
			},
			events: function(start,end,callback) {
				startDay=FormatDate(start);
				FormatDate(end);
				var workDatas = data.workDatas;
				var events = [];
				for(var i=0;i<json.length;i++){
					var date = new Date(json[i].begintime1).Format("yyyy-MM-dd hh:mm:ss");
					var id = new Date(json[i].begintime1).Format("yyyy-MM-dd");
					events.push({
						id:id,
						title: json.as_cname,
						start: date
					});
				}
				callback(events);
			}
		});
	}

	function formatDate(date){
		var y = date.getFullYear();  
		var m = date.getMonth() + 1;  
		m = m < 10 ? '0' + m : m;  
		var d = date.getDate();  
		d = d < 10 ? ('0' + d) : d;  
		var formatDate = y + '-' + m + '-' + d;  
		return formatDate;
	}
			
	function parserDate(date){
		return new Date(Date.parse(date.replace(/-/g, "/")));  
	}
	
	function getLocalTime(nS) {     
       return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");      
    }   
	
	function FormatDate (strTime) {
	    var date = new Date(strTime);
	    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	}
			
	function save(){
		var thisday=document.getElementById("thisDay").value;
		var title= document.getElementById("title").value;
		$.ajax({
	            url: '/myehr/calendar/save.action',
	            dataType: 'JSON',
	            data: "title=" + title + "&datetime=" + thisday,
	            success: function(json) { // 获取当前月的数据
	            	document.getElementById("updateWork").style.display="none";
	                //location.reload();
	            }
	        });
	}
	
	function save1(){
		var elm = document.getElementsByName("rest");
    	var title="";
    	var thisday=formatDate(selectStart);
    	for(var i=0;i<elm.length;i++){
    		if(elm[i].checked){
    			title=elm[i].value;
    			zhtitle= elm[i].title;
    		}
    	}
		
		if (title.length>0) {
			calendar.fullCalendar('removeEvents', [thisday]);
			if(zhtitle=='删除'){
				alert("删除成功！");
			}else{
				calendar.fullCalendar('renderEvent',
					{	
						id:thisday,
						title: zhtitle,
						start: selectStart,
						end: selectEnd,
						allDay: selectAllDay
					},
					true 
				);
			}
			
			//var kCalendar={'kguid':kguid,'itemCaption':title,'itemDate':thisday};
			$.ajax({
	            url: '/myehr/calendar/addWorkDay.action',
	            data: "kguid=" + kguid + "&itemCaption=" + title+ "&itemDate=" + thisday,
				cache: false,
				type: 'get',
				contentType: 'application/json;charset=utf-8',
	            success: function(data) { // 获取当前月的数据
	            	if(data!="1"){
	            		alert("添加失败");
	            	}
	            }
	        });
		}
		document.getElementById("updateWork").style.display="none";
		calendar.fullCalendar('unselect');
	}
	
	function closex(){
		document.getElementById("updateWork").style.display="none";
	}
	
	function init(){
		var strs= startDay.split("-");
		var thisstartyear =strs[0];
		var thisstartmonth =strs[1];
		var thisstartday =strs[2];
		if(thisstartday!=1){
			thisstartmonth=parseInt(thisstartmonth)+1;
		}
		var datetime;
		if(thisstartmonth<10){
			datetime=thisstartyear+"-0"+thisstartmonth;
		}else{
			datetime=thisstartyear+"-"+thisstartmonth;
		}
		$.ajax({
	            url: '/myehr/calendar/InitRestCalendar.action',
	            data: "kguid=" + kguid + "&itemDate=" + datetime,
		        cache: false,
		        contentType: 'application/json;charset=utf-8',
	            success: function(json) { // 获取当前月的数据
	            	alert("初始本月日历成功！");
	            	location.reload();
	            }
	    });
	}
	
	function deleteThisMonth(){
		var strs= startDay.split("-");
		var thisstartyear =strs[0];
		var thisstartmonth =strs[1];
		var thisstartday =strs[2];
		if(thisstartday!=1){
			thisstartmonth=parseInt(thisstartmonth)+1;
		}
		var datetime;
		if(thisstartmonth<10){
			datetime=thisstartyear+"-0"+thisstartmonth;
		}else{
			datetime=thisstartyear+"-"+thisstartmonth;
		}
		$.ajax({
	            url: '/myehr/calendar/ClearRestCalendar.action',
	            data: "kguid=" + kguid + "&itemDate=" + datetime,
		        cache: false,
		        contentType: 'application/json;charset=utf-8',
	            success: function(json) { // 获取当前月的数据
	            	alert("本月清空成功！");
	            	location.reload();
	            }
	    });
	}
	
	function getUrlParam(){
    	// 获取参数
	    var url = window.location.search;
	    // 匹配目标参数
	    var result = url.split("\&");
	    //返回参数值
	    return result ? decodeURIComponent(result[1]) : null;
	}

	
</script>
</body>
</html>