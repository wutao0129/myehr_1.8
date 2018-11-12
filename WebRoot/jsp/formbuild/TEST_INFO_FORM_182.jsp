<%@ page language="java" import="java.util.*" import="com.myehr.common.util.LangUtil" pageEncoding="UTF-8"%>
<%@ include file="/common/commontestForDrag.jsp" %>
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
<script type="text/javascript" src="js/testInfo_182.js?v18.0.0.1"></script>
<style type="text/css">
    /*这行很重要!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
    html,body{min-height: 100%;}
.kuangClass{border-radius: 10px;}

.bubbly-button {
  font-family: 'Helvetica', 'Arial', sans-serif;
  display: inline-block;
  font-size: 1em;
  padding: 1em 2em;
  margin-top: 10px;
  margin-bottom: 10px;
  margin-right:20px;
  -webkit-appearance: none;
  appearance: none;
  background-color: #ff0081;
  color: #fff;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  position: relative;
  transition: transform ease-in 0.1s, box-shadow ease-in 0.25s;
  box-shadow: 0 2px 25px rgba(255, 0, 130, 0.5);
}

.bubbly-button:focus {
  outline: 0;
}
.bubbly-button:before, .bubbly-button:after {
  position: absolute;
  content: '';
  display: block;
  width: 140%;
  height: 100%;
  left: -20%;
  z-index: -1000;
  transition: all ease-in-out 0.5s;
  background-repeat: no-repeat;
}
.bubbly-button:before {
  display: none;
  top: -75%;
  background-image: radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, transparent 20%, #ff0081 20%, transparent 30%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, transparent 10%, #ff0081 15%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%);
  background-size: 10% 10%, 20% 20%, 15% 15%, 20% 20%, 18% 18%, 10% 10%, 15% 15%, 10% 10%, 18% 18%;
}
.bubbly-button:after {
  display: none;
  bottom: -75%;
  background-image: radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, transparent 10%, #ff0081 15%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%), radial-gradient(circle, #ff0081 20%, transparent 20%);
  background-size: 15% 15%, 20% 20%, 18% 18%, 20% 20%, 15% 15%, 10% 10%, 20% 20%;
}
.bubbly-button:active {
  transform: scale(0.9);
  background-color: #e60074;
  box-shadow: 0 2px 25px rgba(255, 0, 130, 0.2);
}
.bubbly-button.animate:before {
  display: block;
  animation: topBubbles ease-in-out 0.75s forwards;
}
.bubbly-button.animate:after {
  display: block;
  animation: bottomBubbles ease-in-out 0.75s forwards;
}

@keyframes topBubbles {
  0% {
    background-position: 5% 90%, 10% 90%, 10% 90%, 15% 90%, 25% 90%, 25% 90%, 40% 90%, 55% 90%, 70% 90%;
  }
  50% {
    background-position: 0% 80%, 0% 20%, 10% 40%, 20% 0%, 30% 30%, 22% 50%, 50% 50%, 65% 20%, 90% 30%;
  }
  100% {
    background-position: 0% 70%, 0% 10%, 10% 30%, 20% -10%, 30% 20%, 22% 40%, 50% 40%, 65% 10%, 90% 20%;
    background-size: 0% 0%, 0% 0%,  0% 0%,  0% 0%,  0% 0%,  0% 0%;
  }
}
@keyframes bottomBubbles {
  0% {
    background-position: 10% -10%, 30% 10%, 55% -10%, 70% -10%, 85% -10%, 70% -10%, 70% 0%;
  }
  50% {
    background-position: 0% 80%, 20% 80%, 45% 60%, 60% 100%, 75% 70%, 95% 60%, 105% 0%;
  }
  100% {
    background-position: 0% 90%, 20% 90%, 45% 70%, 60% 110%, 75% 80%, 95% 70%, 110% 10%;
    background-size: 0% 0%, 0% 0%,  0% 0%,  0% 0%,  0% 0%,  0% 0%;
  }
}
.bubbly-button-color1{
	background-color: #ff0081;
}
.bubbly-button-color2{
	background-color: #660099;
}
.bubbly-button-color3{
	background-color: #9933FF;
}
.bubbly-button-color4{
	background-color: #00FF00;
}
.bubbly-button-color0{
	background-color: #3366FF;
}
.widget{
	margin-top:0px;
	padding:5px 20px;
}
.fixed-table-body{
	height:125px;
}
.searchClass{margin: 7px 0;padding:0;}
.label{margin-right:2px}
.bootstrap-select:not([class*="col-"]):not([class*="form-control"]):not(.input-group-btn){width:160px;margin:0 0 0 5px;}
</style>
</head>
<body class="gray-bg">
<div class="container-fluid">
	<div class="row" style="margin-left:0;position:relative" id="TEST_INFO_FORM">
		<div class="col-md-6 col-sm-6 col-lg-6">
			<div class="row" style="margin-left:0;height:180px;"  id="EMP_V_EMPBASIC_LLIST">
				
			</div>
			<h4 style="font-weight:700;text-align:center;">人员检索</h4>
			<div class="row" style="background-color:#FFF;margin:10px 0px 0 15px;border-radius:10px;height:130px;padding:0 0 0 20px"  id="EmpIndex">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">模糊查询</label>                        
						<input id="EMPCODE" name="EMPCODE" placeholder="姓名,工号,籍贯" style="width:160px;height:29px;margin:0 0 0 5px;" title="姓名/工号" type="text" class="form-control">
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">机&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp构</label>                        
						<select id="COMPID" title="机构" name="COMPID"  columnId="85135" class="selectpicker bla bla bli" multiple data-live-search="true" emptyText=""  style="float:left;width:200px;" showNullItem="true" textField="text" valueField="id" DictName="sql56" dataField1 = "sql"  nullItemText="" dataField="data_form_46781" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">部&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp门</label>                      
						<select id="DEPID" title="部门" name="DEPID"  columnId="85136" class="selectpicker bla bla bli" multiple data-live-search="true" emptyText=""  style="float:left;width:200px;" showNullItem="true" textField="text" valueField="id" DictName="sql6" dataField1 = "sql"  nullItemText="" dataField="data_form_46781" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">职务等级</label>                      
						<select id="JOBRANK" title="职务等级" name="JOBRANK"  columnId="85226" class="selectpicker bla bla bli" multiple data-live-search="true" emptyText=""  style="float:left;width:200px;" showNullItem="true" textField="text" valueField="id" DictName="emp_joblevel" dataField1 = "dict"  nullItemText="" dataField="data_form_46781" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				 <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">学&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp历</label>                      
						<select id="MAXEDUCATION" title="学历" name="MAXEDUCATION"  columnId="85205" class="selectpicker bla bla bli" multiple data-live-search="true" emptyText=""  style="float:left;width:200px;" showNullItem="true" textField="text" valueField="id" DictName="HighestEducation" dataField1 = "dict"  nullItemText="" dataField="data_form_46781" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				 <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">职称级别</label>                      
						<select id="TITLELEVEL" title="职称级别" name="TITLELEVEL"  columnId="85211" class="selectpicker bla bla bli" multiple data-live-search="true" emptyText=""  style="float:left;width:200px;" showNullItem="true" textField="text" valueField="id" DictName="emp_TITLEGRADE" dataField1 = "dict"  nullItemText="" dataField="data_form_46781" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<label style="float:left">性&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp别</label>                      
						<select id="GENDER" title="性别" name="GENDER"  columnId="85170" class="form-control" emptyText=""  style="float:left;width:160px;margin-left:5px" showNullItem="true" textField="text" valueField="id" DictName="emp_SEX" dataField1 = "dict"  nullItemText="" dataField="data_form_85170" multiSelect="true"initParam = "_">
						</select>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 searchClass">
					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
						<input style="float:left;height:29px" value="查询" type="button" class="btn btn-primary" onclick="searchEmpInfo()"/>                    
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-6 col-lg-6 kuangClass" style="padding-left:0px">
			<div class="row" style="margin-left:0;height:175px;background-color:#FFF;margin-right: 10px;border-radius:10px;position:relative"  id="">
				<h4 style="font-weight:700;text-align:center;">人事政策</h4><input class="form-control" type="text" style="width:200px;position:absolute;top:10px;right:20px;" placeholder="标题" id="ANNOUNCEMENT_TITLE" onchange="loaddata()"/>
				<div class="col-md-12 col-sm-12 col-lg-12 " style="overflow:auto;padding:0 10px 10px 10px">
					<ul id="content" style="height:110px;overflow-y:hidden;margin-left:140px">
					</ul>
					<input type="button"class="btn btn-primary" style="position:absolute;bottom:10px;right:35px" onclick="showNews()" value="更多"/>
				</div>
			</div>
			<h4 style="font-weight:700;text-align:center;">流程管理</h4>
			<div class="row" style="margin:15 -5px 0 -15;height:150px;position:relative"  id="knobManage">
			</div>
		</div>
		<div style="display:none">
		<table id="waitActList" style="display:none">
		</table>
		<table id="overActList" style="display:none">
		</table>
		</div>
		<!--
		<div class="col-md-6 col-sm-6 col-lg-6 kuangClass" id="" style="">
			<div class="row" style="background-color:#FFF;margin-right:10px;height:292px;border-radius:10px"  id="content1">
				<ul class="nav nav-tabs" id="myTab">
					<li class="active"><a href="#Tab_1">待批流程</a></li>
					<li ><a href="#Tab_2">已批流程</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="Tab_1">
						<div class="container-fluid">
							<div style="margin-top:5px;">
								<div class="BTNGROUP_1041" style="margin:0 auto; display:inline-block;height: 35px;">
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="select"  class="btn btn-primary" value=选择 onclick="saveMenuIcon()"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="approve"  class="btn btn-primary" value=审批 onclick="approve()"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="batchApprove"  class="btn btn-primary" value=批量审批 onclick="batchApprove()"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="actClaim"  class="btn btn-primary" value=签收 onclick="actClaim()"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="actPicture"  class="btn btn-primary" value=流程图 onclick="actPicture('task')"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="actPictureLink"  class="btn btn-primary" value=流程图路线 onclick="actPicture('link')"/>
									</div>
									<div style="margin-right:10px;display:inline-block">
										<input type="button" id="addorg_list"  class="btn btn-primary" value=审批历史 onclick="actHis()"/>
									</div>
								</div>
								<table id="waitActList">
								</table>
							</div>
						</div>
					</div>
					<div class="tab-pane " id="Tab_2">
						<div class="container-fluid">
							<div style="margin-top:5px;">
								<div style="margin-right:10px;display:inline-block">
									<input type="button" id="actPicture"  class="btn btn-primary" value=流程图 onclick="actPicture('task')"/>
								</div>
								<div style="margin-right:10px;display:inline-block">
									<input type="button" id="actPictureLink"  class="btn btn-primary" value=流程图路线 onclick="actPicture('link')"/>
								</div>
								<div id="grid_org_list_filter" style="float:right;display:inline-block;position:relative;">
									<input id="FILTER_1041" name="FILTER_1041" placeholder="流程编码,流程名称" style="padding: 6px 30px 6px 10px;" title="流程编码,流程名称" type="text" class="form-control" oninput="grid_org_list_fun11()" />
									<i class="icon-search" style="position: absolute;right: 12px;top: 2px;cursor: pointer;"></i>
								</div>
							</div>
							<table id="overActList">
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="col-md-6 col-sm-6 col-lg-6 kuangClass" style="padding-left:0px;position:absolute;bottom:0" >
		<div class="row" style="margin-left:30px;background-color:#FFF;margin-right:0;height:200px;border-radius:10px;padding:20px"  id="actMenu">
			<h4 style="margin-top:-5px;font-weight:700;text-align:center">流程发起</h4>
			<button class="bubbly-button bubbly-button-color0 actMenu" onclick="testFunction(this)" title="收入证明">收入证明</button>
		</div>
		</div>
		-->
	</div>

	<div class="row" style="padding:0 12px">
		<h4 style="font-weight:700;text-align:center">提醒</h4>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4639">
				<table id="emp_countempzong_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_countempzong">
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4464">
				<table id="emp_depcount_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_depcount">
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4465">
				<table id="emp_compcount_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_compcount">
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4397">
				<table id="emp_newemp_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_newemp">
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4399">
				<table id="emp_newlizhi_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_newlizhi">
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-lg-2">
			<div class="box-content" id="4400">
				<table id="emp_tuix_List">
				</table>
				<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
				<div class="row" style="margin-left:0" id="emp_tuix">
				</div>
			</div>
		</div>
	</div>
	<div style="position:relative;height:800px">
		<span class="fa fa-angle-left" id="prev" onclick="changeshowPieForm('prev')" style="position:absolute;left:10px;top:50%;font-size:60px;display:none;font-weight:900"></span>
		<span class="fa fa-angle-right" id="next" onclick="changeshowPieForm('next')" style="position:absolute;right:10px;top:50%;font-size:60px;font-weight:900"></span>
		<div style="width:100%;height:450px;overflow-y:hidden;margin-bottom:20px">
			<div class="row-fluid TUpage TUpage1" style="padding:0 27px;height:450px;margin-bottom:20px">
				<div class="span6">
					<div class="box-content" id="4431">
						<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
						<div id="comp_tye_zhutu_chart" style="width:100%;height:450px;"></div>
					</div>
				</div>
				<div class="span6">
					<div class="box-content" id="4437">
						<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
						<div id="TJ_V_COMPID_ZHUTU_chart" style="width:100%;height:450px;"></div>
					</div>
				</div>
			</div>
			<div class="row-fluid TUpage TUpage2" style="padding:0 27px;height:450px;margin-bottom:20px">
				<div class="span6">
					<div class="box-content" id="4437">
						<div id="activity_pane" style="position:absolute;left:500px;top:70px"></div>
						<div id="JOBRANK_YEAROLDS_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
				<div class="span6" style="display:inline-block">
					<div class="box-content" id="4433">
						<div id="EMP_V_WORKYEARS_TU_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
			</div>
		</div>
		
		<div style="width:100%;height:400px;overflow-y:hidden">
			<div class="row-fluid TUpage TUpage1" style="padding:0 27px;height:450px">
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4433">
						<div id="EMP_V_JOBORDE_BT_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4390">
						<div id="YEAROLDS_BI_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4388">
						<div id="emp_maxty_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
			</div>
			<div class="row-fluid TUpage TUpage2" style="padding:0 27px;height:450px">
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4385">
						<div id="sexy_women_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4385">
						<div id="EMP_POLITICALSTATUS_TU_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
				<div class="span4" style="display:inline-block">
					<div class="box-content" id="4388">
						<div id="EMP_V_ASDWA_ZHUTU_chart" style="width:100%;height:450px;margin-bottom:10px"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<script>

	var _formId_4464='4464';
var heightGadedata_4464 =[];
var containerParam_4464 ={};
containerParam_4464 ={};
function changeHeightGadedata_4464(data){
	heightGadedata_4464 = data;
}
	var _formId_4465='4465';
var heightGadedata_4465 =[];
var containerParam_4465 ={};
containerParam_4465 ={};
function changeHeightGadedata_4465(data){
	heightGadedata_4465 = data;
}

var _formId_4386='4386';
var heightGadedata_4386 =[];
var containerParam_4386 ={};
containerParam_4386 ={};
function changeHeightGadedata_4386(data){
	heightGadedata_4386 = data;
}
var _formId_4389='4389';
var heightGadedata_4389 =[];
var containerParam_4389 ={};
containerParam_4389 ={};
function changeHeightGadedata_4389(data){
	heightGadedata_4389 = data;
}
var _formId_4450='4450';
var realFormId='4401';
var USERID;var heightGadedata_4450 =[];
var containerParam_4450 ={};
containerParam_4450 ={};
function changeHeightGadedata_4450(data){
	heightGadedata_4450 = data;
}
var _formId_4397='4397';
var realFormId='4401';
var heightGadedata_4397 =[];
var containerParam_4397 ={};
containerParam_4397 ={};
function changeHeightGadedata_4397(data){
	heightGadedata_4397 = data;
}
	var _formId_4399='4399';
var realFormId='4401';
var heightGadedata_4399 =[];
var containerParam_4399 ={};
containerParam_4399 ={};
function changeHeightGadedata_4399(data){
	heightGadedata_4399 = data;
}
	var _formId_4639='4639';
var realFormId='4401';
var heightGadedata_4639 =[];
var containerParam_4639 ={};
containerParam_4639 ={};
function changeHeightGadedata_4639(data){
	heightGadedata_4639 = data;
}
var _formId_4400='4400';
var realFormId='4401';
var heightGadedata_4400 =[];
var containerParam_4400 ={};
containerParam_4400 ={};
function changeHeightGadedata_4400(data){
	heightGadedata_4400 = data;
}
	var _formId_4404='4404';
var realFormId='4401';
var heightGadedata_4404 =[];
var containerParam_4404 ={};
containerParam_4404 ={};
function changeHeightGadedata_4404(data){
	heightGadedata_4404 = data;
}
	var _formId_4407='4407';
var realFormId='4401';
var heightGadedata_4407 =[];
var containerParam_4407 ={};
containerParam_4407 ={};
function changeHeightGadedata_4407(data){
	heightGadedata_4407 = data;
}
var heightGadedata_4402 =[];
var containerParam_4402 ={};
containerParam_4402 ={};
function changeHeightGadedata_4402(data){
	heightGadedata_4402 = data;
}
var heightGadedata_4436 =[];
var containerParam_4436 ={};
containerParam_4436 ={};
function changeHeightGadedata_4436(data){
	heightGadedata_4436 = data;
}
var _formId_4432='4432';
var heightGadedata_4432 =[];
var containerParam_4432 ={};
containerParam_4432 ={};
function changeHeightGadedata_4432(data){
	heightGadedata_4432 = data;
}
var heightGadedata_4438 =[];
var containerParam_4438 ={};
containerParam_4438 ={};
function changeHeightGadedata_4438(data){
	heightGadedata_4438 = data;
}
var heightGadedata_4444 =[];
var containerParam_4444 ={};
containerParam_4444 ={};
function changeHeightGadedata_4444(data){
	heightGadedata_4444 = data;
}
var _formId_3862='3862';
var realFormId='${param.formId}';
var heightGadedata_3862 =[];
var containerParam_3862 ={};
containerParam_3862 ={};

var heightGadedata_4459 =[];
var containerParam_4459 ={};
containerParam_4459 ={};
function changeHeightGadedata_4459(data){
	heightGadedata_4459 = data;
}
var _formId_4459='4459';

var heightGadedata_4415 =[];
var containerParam_4415 ={};
containerParam_4415 ={};
function changeHeightGadedata_4415(data){
	heightGadedata_4415 = data;
}
var _formId_4415='4415';

function changeHeightGadedata_3862(data){
	heightGadedata_3862 = data;
}
var _formId_4386='4386';
var heightGadedata_4386 =[];
var containerParam_4386 ={};
containerParam_4386 ={};
function changeHeightGadedata_4386(data){
	heightGadedata_4386 = data;
}
var heightGadedata_4386 =[];
var containerParam_4386 ={};
containerParam_4386 ={};
function changeHeightGadedata_4386(data){
	heightGadedata_4386 = data;
}
var heightGadedata_4413 =[];
var containerParam_4413 ={};
containerParam_4413 ={};
function changeHeightGadedata_4413(data){
	heightGadedata_4413 = data;
}
var _formId_4413='4413';
var _formId_4386='4386';

var heightGadedata_4461 =[];
var containerParam_4461 ={};
containerParam_4461 ={};
function changeHeightGadedata_4461(data){
	heightGadedata_4461 = data;
}
var _formId_4461='4461';
$(function() {
	loadDataStart();
	loadDataStart_4667();
	loaddata();
	loadDataStart_4464();
	loadDataStart_4465();
	loadDataStart_4397();
	loadDataStart_4399();
	loadDataStart_4639();
	loadDataStart_4400();
	querys_4402();
	queryAllData_4431();
	querys_4436();
	queryAllData_4437();
	queryAllData_4460();
	queryAllData_4433();
	queryAllData_4390();
	queryAllData_4388();
	queryAllData_4385();
	queryAllData_4416();
	queryAllData_4414();
	queryAllData_4462();
	getEmpPhotoSYS();
	cardSelectInitFunctionx("");
	cardDateWidInitFunction();
	showDate_KNOB();


});

var _formId_4434='4434';
var _formId_4438='4438';
var _formId_4444='4444';


var filterParam = {};
var queryParams;
function grid_TEST_INFO_FORM_fun(pageReqeust){
	var _filterdata={};
	if(document.getElementById('grid_TEST_INFO_FORM_filter')){
	}
	var paramsMap = {};
	var requestParam={};
		requestParam = {};
	pageReqeust.containerParam=containerParam_3862;
	pageReqeust.paramsMap=paramsMap;
	pageReqeust.requestParam=requestParam;
	pageReqeust.filter=_filterdata;
	pageReqeust.userIds=getQueryString('empIds');
	pageReqeust.formId='4450';
	pageReqeust.isView=null;
	pageReqeust.offset=Currentpage_3862*pagesize_3862;
	pageReqeust.limit=pagesize_3862;
	if(heightGadedata_3862!=null&&heightGadedata_3862!=undefined){
		pageReqeust.heightGrade = heightGadedata_3862;
	}
	queryParams = JSON.stringify(pageReqeust);
	return queryParams;
}
function grid_TEST_INFO_FORM_fun11_3862(){
	loadDataStart();
}
function getEmpPhotoSYS(){
	var objs = $("#Employee_Photo_WT");
	if(objs.length>0){
		$.ajax({ 
			   url:'/myehr/FileList/getEmpPhotoSYS.action?EMPID=<%=empid%>',
			   type:'post',
			   cache: false,
			   async: false, 
			   success: function (data){ 
				   $("#Employee_Photo_WT").attr("src",data[0]);
			   }
		   });
	}
}
var containerParam_3862={};
var xx = {};
var main_Datas = [];

function loadDataStart(){
	$.ajax({
		url: "/myehr/form/cardformInitData.action",
		type: 'post',
		data:grid_TEST_INFO_FORM_fun(xx),//HTTP请求类型
		contentType: 'application/json;charset=utf-8',
		cache: false,
		async: false,
		success: function (data) {
			Currentpage_3862++;
			console.log(data);
			var arr=data.rows;
			main_Datas=data.rows;
			showDate_3862(arr);
		  }
	});
}
function showDate_3862(arr) {
	for (var i = arr.length - 1,j=0; i >= 0; i--,j++) {
		var TESTINFOR_china_name = arr[i].TESTINFOR_CHINA_NAME;
		if(arr[i].TESTINFOR_CHINA_NAME==null){
			 TESTINFOR_china_name = '';
		}
		var TESTINFOR_eng_name = arr[i].TESTINFOR_ENG_NAME;
		if(arr[i].TESTINFOR_ENG_NAME==null){
			 TESTINFOR_eng_name = '';
		}
		var TESTINFOR_zhuzhi = arr[i].TESTINFOR_ZHUZHI;
		if(arr[i].TESTINFOR_ZHUZHI==null){
			 TESTINFOR_zhuzhi = '';
		}
		var TESTINFOR_xuli = arr[i].TESTINFOR_XULI;
		if(arr[i].TESTINFOR_XULI==null){
			 TESTINFOR_xuli = '';
		}
		var TESTINFOR_biyeyuanxiao = arr[i].TESTINFOR_BIYEYUANXIAO;
		if(arr[i].TESTINFOR_BIYEYUANXIAO==null){
			 TESTINFOR_biyeyuanxiao = '';
		}
		var TESTINFOR_ruxueshijian = arr[i].TESTINFOR_RUXUESHIJIAN;
		if(arr[i].TESTINFOR_RUXUESHIJIAN!=null){
			 TESTINFOR_ruxueshijian = new Date(TESTINFOR_ruxueshijian).Format("yyyy/MM/dd");
		}
		var TESTINFOR_biyeshijian = arr[i].TESTINFOR_BIYESHIJIAN;
		if(arr[i].TESTINFOR_BIYESHIJIAN==null){
			 TESTINFOR_biyeshijian = '';
		}
		var TESTINFOR_gongsi = arr[i].TESTINFOR_GONGSI;
		if(arr[i].TESTINFOR_GONGSI==null){
			 TESTINFOR_gongsi = '';
		}
		var TESTINFOR_bumen = arr[i].TESTINFOR_BUMEN;
		if(arr[i].TESTINFOR_BUMEN==null){
			 TESTINFOR_bumen = '';
		}

		var carousel_indicators = '';
		if(j==0){
			carousel_indicators = $(
				'<li data-target="#myCarousel" data-slide-to="0" class="active"></li>'
			);
		}else{
			carousel_indicators = $(
				'<li data-target="#myCarousel" data-slide-to="'+j+'" ></li>'
			);
		}
		$('.carousel-indicators').append(carousel_indicators);
		
		var carousel_inner = '';
		if(j==0){
			carousel_inner = $(
				'<div class="item active">'+
				'	<a href="#">'+
				'		<img  style="margin:0 auto" src="http://116.62.243.28:9875/'+arr[i].TESTINFOR_ZHAOPIAN+'" alt="'+arr[i].TESTINFOR_CHINA_NAME+'">'+
				'		<div class="carousel-caption">'+arr[i].TESTINFOR_CHINA_NAME+'</div>'+
				'	</a>'+
				'</div>'
			);
		}else{
			carousel_inner = $(
				'<div class="item" >'+
				'	<a href="#">'+
				'		<img  style="margin:0 auto" src="http://116.62.243.28:9875/'+arr[i].TESTINFOR_ZHAOPIAN+'" alt="'+arr[i].TESTINFOR_CHINA_NAME+'">'+
				'		<div class="carousel-caption">'+arr[i].TESTINFOR_CHINA_NAME+'</div>'+
				'	</a>'+
				'</div>'
			);
		}
		
		$('.carousel-inner').append(carousel_inner);
		
	}
}
	

	//发起,待批
	function grid_act_list_fun(){
		var pageReqeust = {};
		var _filterdata={};
		_filterdata={searchValue:''};
		var userId = '<%=userId%>';
		var paramsMap = {userId:userId};
		var requestParam={};
		requestParam = {flowType:'2'};
		pageReqeust.paramsMap=paramsMap;
		pageReqeust.requestParam=requestParam;
		pageReqeust.filter=_filterdata;
		pageReqeust.formId=1111;
		pageReqeust.isView=null;
		pageReqeust.limit = '1000';
		pageReqeust.offset = '0';
		return JSON.stringify(pageReqeust);
	}
	
//发起流程
function startProcess(e){
	var formId = $(e).attr("formId");
	var id = $(e).attr("actId");
	var key = $(e).attr("actKey");
	var title = $(e).attr("title");
	var businessId = selectBussinessIdByFormId(formId);
	var url='/myehr/form/toForm.action?formId='+formId+'&businessId='+businessId+'&id='+id+'&key='+key+'&flowAction=start';
	var title = title;
	parent.layer.open({
		type: 2,
		title:title,
		shadeClose: true,
		shade: 0.8,
		maxmin:true,
		zIndex:9999,
		area: ['950px', '500px'],
		content: url,
		success:function(layero,index){
		$('.layui-layer-max').click();
		}
	});
}
//获取流程任务号
function selectBussinessIdByFormId(formId){
	var dataId = '';
	$.ajax({  
		url: '/myehr/form/selectBussinessIdByFormId.action?formId='+formId,  
		type: 'POST', 
		cache: false,
	    async: false,		
		success: function (data) {  
			dataId = data;
		}
	});
	return dataId;
}

//待批已批多Tab
var waitActListTotal;

var overActListTotal;
//已批流程列表


function grid_waitActList_fun(pageReqeust){
	var _filterdata={};
	var searchValue = '';
	_filterdata={searchValue:searchValue};
	var userId = '<%=userId%>';
	var paramsMap = {userId:userId,flowCode:'1'};
	var requestParam={};
	pageReqeust.paramsMap=paramsMap;
	pageReqeust.requestParam=requestParam;
	pageReqeust.filter=_filterdata;
	pageReqeust.formId=1111;
	pageReqeust.isView=null;
	return JSON.stringify(pageReqeust);
}
function grid_waitActList_fun1(){
	$('#waitActList').bootstrapTable('refresh');
}
function grid_overActList_fun(pageReqeust){
	var _filterdata={};
	var searchValue = '';
	_filterdata={searchValue:searchValue};
	var userId = '<%=userId%>';
	var paramsMap = {userId:userId,flowCode:'1'};
	var requestParam={};
	pageReqeust.paramsMap=paramsMap;
	pageReqeust.requestParam=requestParam;
	pageReqeust.filter=_filterdata;
	pageReqeust.formId=1111;
	pageReqeust.isView=null;
	return JSON.stringify(pageReqeust);
}
function grid_overActList_fun1(){
	$('#overActList').bootstrapTable('refresh');
}

//登陆者信息
var _formId_4667='4667';
var realFormId='4667';
var EMPID;var heightGadedata_4667 =[];
var containerParam_4667 ={};
containerParam_4667 ={};
function changeHeightGadedata_4667(data){
	heightGadedata_4667 = data;
}
var filterParam = {};
var queryParams;

function grid_emp_employee_gridbycard_fun(pageReqeust){
	var _filterdata={};
	if(document.getElementById('grid_emp_employee_gridbycard_filter')){
	}
	var paramsMap = {};
	var requestParam={};
	if(''==''&&'session'=='parameter'){
		if(EMPID==undefined){
			requestParam = {EMPID:''};
		}else{
			requestParam = {};
		}
	}else{
		requestParam = {EMPID:'1'};
	}
	pageReqeust.containerParam=containerParam_4667;
	pageReqeust.paramsMap=paramsMap;
	pageReqeust.requestParam=requestParam;
	pageReqeust.filter=_filterdata;
	pageReqeust.userIds=getQueryString('empIds');
	pageReqeust.formId='4450';
	pageReqeust.isView=null;
	pageReqeust.offset=Currentpage_4667*pagesize_4667;
	pageReqeust.limit=pagesize_4667;
	if(heightGadedata_4667!=null&&heightGadedata_4667!=undefined){
		pageReqeust.heightGrade = heightGadedata_4667;
	}
	queryParams = JSON.stringify(pageReqeust);
	return queryParams;
}
function grid_emp_employee_gridbycard_fun11_4667(){
	loadDataStart();
}
//当前页和一次显示多少条数据
var Currentpage_4667 = 0;
var pagesize_4667 = 8;
var Currentpage_3862 = 0;
var pagesize_3862 = 8;

var containerParam_4667={};
var xx = {};
var main_Datas = [];
function loadDataStart_4667(){
	$.ajax({
		url: "/myehr/form/cardformInitData.action",
		type: 'post',
		data:grid_emp_employee_gridbycard_fun(xx),//HTTP请求类型
		contentType: 'application/json;charset=utf-8',
		cache: false,
		async: false,
		success: function (data) {
			Currentpage_4667++;
			console.log(data);
			var arr=data.rows;
			main_Datas=data.rows;
			showDate_4667(arr);
		  }
	});
}
function showDate_4667(arr) {
	for (var i = arr.length - 1; i >= 0; i--) {
		var EMPVEMPBASIC_CNAME = arr[i].EMPVEMPBASIC_CNAME;
		if(arr[i].EMPVEMPBASIC_CNAME==null){
			 EMPVEMPBASIC_CNAME = '';
		}
		var EMPVEMPBASIC_COMPID = arr[i].EMPVEMPBASIC_COMPID;
		if(arr[i].EMPVEMPBASIC_COMPID==null){
			 EMPVEMPBASIC_COMPID = '';
		}
		var EMPVEMPBASIC_DEPID = arr[i].EMPVEMPBASIC_DEPID;
		if(arr[i].EMPVEMPBASIC_DEPID==null){
			 EMPVEMPBASIC_DEPID = '';
		}
		var EMPVEMPBASIC_JOBID = arr[i].EMPVEMPBASIC_JOBID;
		if(arr[i].EMPVEMPBASIC_JOBID==null){
			 EMPVEMPBASIC_JOBID = '';
		}
		var EMPVEMPBASIC_GENDER = arr[i].EMPVEMPBASIC_GENDER;
		if(arr[i].EMPVEMPBASIC_GENDER==null){
			 EMPVEMPBASIC_GENDER = '';
		}
		var EMPVEMPBASIC_BIRTHDAY = arr[i].EMPVEMPBASIC_BIRTHDAY;
		if(arr[i].EMPVEMPBASIC_BIRTHDAY!=null){
			 EMPVEMPBASIC_BIRTHDAY = new Date(EMPVEMPBASIC_BIRTHDAY).Format("yyyy-MM-dd");
		}
		var EMPVEMPBASIC_WORKEMAIL = arr[i].EMPVEMPBASIC_WORKEMAIL;
		if(arr[i].EMPVEMPBASIC_WORKEMAIL==null){
			 EMPVEMPBASIC_WORKEMAIL = '';
		}
		var EMPVEMPBASIC_MOBILE = arr[i].EMPVEMPBASIC_MOBILE;
		if(arr[i].EMPVEMPBASIC_MOBILE==null){
			 EMPVEMPBASIC_MOBILE = '';
		}
			var cellObj = $(
				'<div class="col-md-12 col-sm-12 col-lg-12">'+
				'    <div class="contact-box" style="border-radius:10px;margin-bottom:10px;max-height: 180px;">'+
				'            <div class="col-sm-4">'+
				'                <div class="text-center">'+
				'                    <img width="100" style="margin:0 auto" alt="image" id="Employee_Photo_WT" class="img-circle m-t-xs img-responsive" >'+
				'                    <div class="m-t-xs font-bold" ></div>'+
				'                </div>'+
				'            </div>'+
				'            <div class="col-sm-8">'+
				'                <h4 style="margin-top:0;overflow:hidden;height:25px;color:#000000;" title="'+EMPVEMPBASIC_CNAME+'"><strong>'+EMPVEMPBASIC_CNAME+'</strong></h4>'+
				'                <div class="row" id="row" style="width:300px">'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">公司:'+ EMPVEMPBASIC_COMPID +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">部门:'+ EMPVEMPBASIC_DEPID +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">岗位:'+ EMPVEMPBASIC_JOBID +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">性别:'+ EMPVEMPBASIC_GENDER +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">出生日期:'+ EMPVEMPBASIC_BIRTHDAY +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">工作邮箱:'+ EMPVEMPBASIC_WORKEMAIL +' </label>'+
				 '					</div>'+
				 '				</div>'+
				 '				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">'+
				 '					<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:200px">'+
				 '						<label style="float:left;font-size:12px;font-weight:400;">手机号码:'+ EMPVEMPBASIC_MOBILE +' </label>'+
				 '					</div>'+
				 '				</div>'+
				'                </div>'+
				'            </div>'+
				'            <div class="clearfix"></div>'+
				'        </a>'+
				'    </div>'+
				'</div>');
			$('#EMP_V_EMPBASIC_LLIST').append(cellObj);
	}
}

function openTable(e){
	var url = '';
	var title = '';
	if(e=='alreadyAct'){
		url = '/myehr/jsp/act/actManage/hsiActList.jsp';
		title = '已批流程';
	}else if(e=='waitAct'){
		//url = '/myehr/jsp/act/actManage/toStartManager.jsp';
		url = '/myehr/jsp/act/actManage/toDoList.jsp?flowCode=FlowRoot';
		title = '待批流程';
	}else if(e=='startAct'){
		url = '/myehr/jsp/act/actManage/todoManager.jsp';
		title = '发起流程';
	}
	layer.open({
		type: 2,
		title: title,
		shadeClose: true,
		shade: 0.8,
		maxmin:true,
		area: ['85%', '85%'],
		content: url, //iframe的url
		success:function(layero,index){
			$('.layui-layer-max').click();
	    },
		end:function(){
			refresh();
		}
	})

}

//信息统计
function showDate_KNOB() {
		var nums = [];
		$.ajax({
			url: "/myehr/act/task/getTotalModel.action",
			type: 'post',
			cache: false,
			async: false,
			success: function (data) {
				nums = data;
			 }
		});
		var cellObj = $('<div class="col-md-4 col-sm-4 col-lg-4"  >'+
						'	 <div style="width:100%;height:100%;z-index:999;position:absolute;" onclick="openTable(\'waitAct\')"></div>'+
						'    <div class="widget style1 navy-bg" style="background-color:#fff">'+
						'        <div class="row" id="row">'+
						'            <div style="text-align:center">'+
						'				<input class="knob" value="'+nums[2]+'">'+
						'				<div style="margin-top:10px"><span style="text-align:center;display:block;font-size:12;color:#000;font-weight:400;">待批流程</span></div>'+
						'            </div>'+
						'        </div>'+
						'    </div>'+
						'</div>'+
						'<div class="col-md-4 col-sm-4 col-lg-4"  >'+
						'	 <div style="width:100%;height:100%;z-index:999;position:absolute;" onclick="openTable(\'alreadyAct\')"></div>'+
						'    <div class="widget style1 navy-bg" style="background-color:#fff" >'+
						'        <div class="row" id="row">'+
						'            <div style="text-align:center">'+
						'				<input class="knob2" value="'+nums[1]+'">'+
						'				<div style="margin-top:10px"><span style="text-align:center;display:block;font-size:12;color:#000;font-weight:400;">已批流程</span></div>'+
						'            </div>'+
						'        </div>'+
						'    </div>'+
						'</div>'+
						'<div class="col-md-4 col-sm-4 col-lg-4"  >'+
						'	 <div style="width:100%;height:100%;z-index:999;position:absolute;" onclick="openTable(\'startAct\')"></div>'+
						'    <div class="widget style1 navy-bg" style="background-color:#fff">'+
						'        <div class="row" id="row">'+
						'            <div style="text-align:center">'+
						'				<input class="knob3" value="'+nums[0]+'">'+
						'				<div style="margin-top:10px"><span style="text-align:center;display:block;font-size:12;color:#000;font-weight:400;">可发起流程数</span></div>'+
						'            </div>'+
						'        </div>'+
						'    </div>'+
						'</div>');
	$('#knobManage').append(cellObj);
	$(".knob").knob({
		max: 50,
		min: 0,
		width:90,
		height:90,
		readonly:"readonly",
		thickness: .1,
		fgColor: 'rgb(135, 206, 235)',
		bgColor: '#F5F5F5',
		'release':function(e){
		},
		'change' : function (v) { console.log(v); }
    });
	$(".knob2").knob({
		max: 500,
		min: 0,
		width:90,
		readonly:"readonly",
		height:90,
		thickness: .1,
		fgColor: '#FF5809',
		bgColor: '#F5F5F5',
		'release':function(e){
		},
		'change' : function (v) { console.log(v); }
    });
	$(".knob3").knob({
		max: 50,
		min: 0,
		width:90,
		height:90,
		readonly:"readonly",
		thickness: .1,
		fgColor: '#AAAAFF',
		bgColor: '#F5F5F5',
		'release':function(e){
		},
		'change' : function (v) { console.log(v); }
    });
}
function testFunction(e){
	var url='/myehr/jsp/formbuild/att_leave_test_add.jsp';
	parent.layer.open({
		type: 2,
		title:'收入证明的工单',
		shadeClose: true,
		shade: 0.8,
		maxmin:true,
		zIndex:9999,
		area: ['1150px', '500px'],
		content: url,
		success:function(layero,index){
		$('.layui-layer-max').click();
		}
	});
}
var TUpageNum = 1;
function changeshowPieForm (e){
	$(".TUpage").css("display","none");
	if(e=='prev'){
		TUpageNum = TUpageNum-1;
		$(".TUpage"+TUpageNum).css("display","block");
	}else if(e=='next'){
		TUpageNum = TUpageNum+1;
		$(".TUpage"+TUpageNum).css("display","block");
	}
	if(TUpageNum==1){
		$("#prev").css("display","none");
		$("#next").css("display","block");
	}else if(TUpageNum==2){
		$("#next").css("display","none");
		$("#prev").css("display","block");
	}
}

function searchEmpInfo(){
	var EMPCODE = $("#EMPCODE").val();
	var EMPCODEval = "";
	var CNAMEval = "";
	var ORIGINval = "";
	if(EMPCODE!=''&&EMPCODE!=null){
		EMPCODEval = EMPCODE;
		CNAMEval = EMPCODE;
		ORIGINval = EMPCODE;
	}
	var COMPID = $("#COMPID").val();
	var COMPIDval = "";
	if(COMPID==''||COMPID==null){
	}else{
		for(var i=0;i<COMPID.length;i++){
			COMPIDval+=COMPID[i]+',';
		}
		COMPIDval = COMPIDval.substring(0,COMPIDval.length-1);
	}
	var DEPID = $("#DEPID").val();
	var DEPIDval = ""
	if(DEPID==''||DEPID==null){
	}else{
		for(var i=0;i<DEPID.length;i++){
			DEPIDval+=DEPID[i]+',';
		}
		DEPIDval = DEPIDval.substring(0,DEPIDval.length-1);
	}
	var GENDER = $("#GENDER").val();
	var GENDERval = "";
	if(GENDER==''||GENDER==null){
	}else{
		GENDERval = GENDER;
	}
	var MAXEDUCATION = $("#MAXEDUCATION").val();
	var MAXEDUCATIONval = "";
	if(MAXEDUCATION==''||MAXEDUCATION==null){
	}else{
		for(var i=0;i<MAXEDUCATION.length;i++){
			MAXEDUCATIONval+=MAXEDUCATION[i]+',';
		}
		MAXEDUCATIONval = MAXEDUCATIONval.substring(0,MAXEDUCATIONval.length-1);
	}
	var TITLELEVEL = $("#TITLELEVEL").val();
	var TITLELEVELval = "";
	if(TITLELEVEL==''||TITLELEVEL==null){
	}else{
		for(var i=0;i<TITLELEVEL.length;i++){
			TITLELEVELval+=TITLELEVEL[i]+',';
		}
		TITLELEVELval = TITLELEVELval.substring(0,TITLELEVELval.length-1);
	}
	var EMPTYPE = $("#EMPTYPE").val();
	var EMPTYPEval = "";
	if(EMPTYPE==''||EMPTYPE==null){
	}else{
		for(var i=0;i<EMPTYPE.length;i++){
			EMPTYPEval+=EMPTYPE[i]+',';
		}
		EMPTYPEval = EMPTYPEval.substring(0,EMPTYPEval.length-1);
	}
		var JOBRANK=$("#JOBRANK").val();
	var JOBRANKval="";
		if(JOBRANK==''||JOBRANK==null){
	}else{
		for(var i=0;i<JOBRANK.length;i++){
			JOBRANKval+=JOBRANK[i]+',';
		}
		JOBRANKval = JOBRANKval.substring(0,JOBRANKval.length-1);
	}
      var url = '/myehr/jsp/workDesk/emp_emp_list_zm.jsp?formId=4468&ORIGIN='+ORIGINval+'&EMPCODE='+EMPCODEval+'&CNAME='+CNAMEval+'&COMPID='+COMPIDval+'&DEPID='+DEPIDval+'&GENDER='+GENDERval+'&MAXEDUCATION='+MAXEDUCATIONval+'&TITLELEVEL='+TITLELEVELval+'&EMPTYPE='+EMPTYPEval+'&JOBRANK='+JOBRANKval;	var title = '人员检索';
	layer.open({
		type: 2,
		title: title,
		shadeClose: true,
		shade: 0.8,
		area: ['85%', '85%'],
		content: url, //iframe的url
		success:function(layero,index){
	    },
		end:function(){
			refresh();
		}
	})
}

</script>
</body>
</html>