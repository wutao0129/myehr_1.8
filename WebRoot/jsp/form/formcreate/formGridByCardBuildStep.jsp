<%@ page language="java" import="java.util.*" import="com.myehr.common.util.LangUtil" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<%
String s = request.getParameter("author1");
System.out.println(s+"sssss=");

Map parammap=request.getParameterMap();  
		String b ="";
	    Set keSet=parammap.entrySet();  
	    for(Iterator itr=keSet.iterator();itr.hasNext();){  
	        Map.Entry me=(Map.Entry)itr.next();  
	        Object ok=me.getKey();  
	        Object ov=me.getValue();  
	        String[] value=new String[1];  
	        if(ov instanceof String[]){  
	            value=(String[])ov;  
	        }else{  
	            value[0]=ov.toString();  
	        }  
	        for(int k=0;k<value.length;k++){  
	            System.out.println(ok+"="+value[k]);  
	            StringBuilder bulid = new StringBuilder();
	            	b += bulid.append(ok+"="+value[k]).toString().trim()+"&";
	            	System.out.println(b);  
	        } 
	      }  
	      
	      System.out.println("b="+b);	     
%><html>
<head>
    <style type="text/css" href="./css/bootstrap.min.css"></style>
    <link rel="stylesheet" href="cssjs/animate.css" type="text/css"></link>
	<link rel="stylesheet" href="cssjs/jquery.steps.css" type="text/css"></link>
	<script type="text/javascript" src="cssjs/jquery.steps.min.js"></script>
	<script type="text/javascript" src="cssjs/formCardBuildStep.js"></script>
	<script type="text/javascript" src="cssjs/formGridBuildStep3.js"></script>
	<script type="text/javascript" src="cssjs/formGridBuildStep4.js"></script>
	<script type="text/javascript" src="cssjs/formGridBuildStep5.js"></script>
	<script type="text/javascript" src="cssjs/demo1.js"></script>
	<style type="text/css">
	.wrapper{width:98%;margin:0 auto;padding-top:10px}
	.row{margin:0;}
	.wizard > .content > .body{padding: 0.5% 2.5%;}
	.row.cell>div lable{font-weight:600}
	.fixed-table-container thead th .th-inner, .fixed-table-container tbody td .th-inner{font-weight:600}
	fieldset{height:100%;width:100%}
	.ztree li a{width:89%}
	#step2_part2 .fixed-table-body {height:100%}
	.wizard > .content > .body ul > li{list-style:none}
	#step2_part2 .clearfix{display:none}
	.row.cell>div{min-height:30px;margin:10px 0 0 0;}
	.wizard > .steps > ul > li{width:20%}
	.row.cell>div lable{width:118px}
	#form-p-4,#form-p-3,#form-p-2,#form-p-1{width:100%;height:97%}
	
	.sidebar{height: 100%;
            position: relative;
            border-radius: 5px;
            margin-bottom: 10px;
            padding: 10px;}
        .searchInput{position: relative;}
        .searchInfo{position: absolute;top: 0;right: 0;margin-right: 30px;}
        .searchInfoWrapper>div{display: inline-block;}
        .searchInfoWrapper>div a{cursor: pointer;}
        .searchInfoWrapper label {margin: 4px 5px 0;vertical-align: top;}
        .panel-heading {padding: 5px 15px;}
	</style>
</head>

<body>
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div id="form" class="wizard-big">
                    <h1>基本配置</h1>
                    <fieldset>
                        <div class="row">
                            <div id="form_step1" name="form_step1" class="col-sm-12" >
                                <div class="container-fluid" style="overflow-y:auto;">
                                    <div style="margin-top:5px;">
                                        <div style="margin:10px 0;">
	                                        <h3 style="display:block;font-size:18;text-align:left;height: 36px;line-height: 33px;padding-left: 16px;color: #666;cursor: pointer;">基础信息</h3>
	                                            <div class="row cell" id="form1" >
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">表单编码</lable>
	                                                        <input id="formDefCode" name="formDefCode" type="text" class="form-control" style="width:200px;" value="${param.fieldChinaName}"/>
	                                                        <input id="formDefType" name="formDefType" type="hidden" class="form-control"  required="true"  style="width:200px;"/>
	                                                        <input id="formDefId" name="formDefId" type="hidden" class="form-control"  required="true"  style="width:200px;"/>
	                                                        <input id="formDefFolderId" name="formDefFolderId" type="hidden" class="form-control"  required="true"  style="width:200px;"/>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">表单名称</lable>
	                                                        <input id="formDefName" name="formDefName" type="text" class="form-control" style="width:200px;" value="${param.controlType}"/>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">表单布局类型</lable>                        
	                                                        <select id="formDefLayoutType" title="表单布局类型" styleType="select" name="formDefLayoutType" disabled   class="form-control" style="width:200px;" textField="dictName" valueField="dictID" DictName="SYS_FORM_LAYOUT" dataField="dicts">
	                                                        </select>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">是否流程表单</lable>                        
	                                                        <select id="formDefIsProcess" title="是否流程表单" styleType="select" name="formDefIsProcess"  class="form-control" style="width:200px;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" dataField="dicts"></select>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">是否不受权限控制</lable>                      
	                                                        <select id="formAuthorityIsControl" title="是否不受权限控制" styleType="select" name="formAuthorityIsControl"  class="form-control" style="width:200px;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" dataField="dicts"></select>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">是否修改卡片</lable>                      
	                                                        <select id="isApp" title="是否修改卡片" styleType="select" name="isApp"  class="form-control" style="width:200px;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" dataField="dicts"></select>
	                                                    </div>
	                                                </div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
	                                                        <lable style="float:left">关联数据源</lable>                      
	                                                        <select id="formDefSource" title="关联数据源" styleType="select" name="formDefSource"  class="form-control" style="width:200px;" textField="dictName" valueField="dictID" DictName="SYS_FORM_DATASOURCE" dataField="dicts"></select>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "height:40px">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
	                                                        <lable style="float:left">权限SQL</lable>
	                                                        <textarea id="powerSql" name="powerSql" rows="2" class="form-control" style="width:50%;resize:none">
	                                                        </textarea>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "height:40px">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
	                                                        <lable style="float:left">排序SQL</lable>
	                                                        <textarea id="orderSql" name="orderSql" rows="2" class="form-control" style="width:50%;resize:none">
	                                                        </textarea>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "height:40px">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
	                                                        <lable style="float:left">表单描述</lable>
	                                                        <textarea id="formDefDesc" name="formDefDesc" rows="2" class="form-control" style="width:50%;resize:none">
	                                                        </textarea>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "height:40px">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
	                                                        <lable style="float:left">前置初始函数</lable>
	                                                        <textarea id="formDefInitQzJs" name="formDefInitQzJs" rows="2" class="form-control" style="width:50%;resize:none">
	                                                        </textarea>
	                                                    </div>
	                                                </div>
	                                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "height:40px">
	                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
	                                                        <lable style="float:left">前置初始SQL</lable>
	                                                        <textarea id="formDefInitQzSql" name="formDefInitQzSql" rows="2" class="form-control" style="width:50%;resize:none">
	                                                        </textarea>
	                                                    </div>
	                                                </div>
	                                            </div>
	                                        <div id="form2">
	                                          	<h3 style="display:block;font-size:18;text-align:left;height: 36px;line-height: 33px;padding-left: 16px;color: #666;cursor: pointer;">布局扩展信息</h3>
	                                            <div class="row cell"  >
	                                                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">是否分页</lable>
															<input id="formShowPager" name="formShowPager" type="checkbox" checked class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">是否显示序号</lable>
															<input id="formIsIndex" name="formIsIndex" type="checkbox" checked class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">是否显示表头</lable>
															<input id="formShowColumns" name="formShowColumns" type="checkbox" checked class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">是否允许多选</lable>
															<input id="formMultiSelect" name="formMultiSelect" type="checkbox" checked class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">单元格编辑</lable>
															<input id="formAllowCellEdit" name="formAllowCellEdit" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">单元格选择</lable>
															<input id="formAllowCellSelect" name="formAllowCellSelect" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">允许拖拽</lable>
															<input id="formAllowResize" name="formAllowResize" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">回车进入右单元格</lable>
															<input id="formEditNextOnEnter" name="formEditNextOnEnter" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">回车进入下单元格</lable>
															<input id="formEditNextRowCell" name="formEditNextRowCell" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">批量操作表单</lable>
															<input id="formTableIsHandsontable" name="formTableIsHandsontable" type="checkbox" class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">显示标题</lable>
															<input id="formShowTitle" name="formShowTitle" type="checkbox" checked class="checkbox"/>
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">冻结列数</lable>
															<input id="formFrozen" name="formFrozen" type="number" class="form-control" value="-1" style="width:200px;" />
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">表格高度</lable>
															<input id="formGridHeight" name="formGridHeight" type="number" class="form-control" style="width:200px;" />
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">每页显示条数</lable>
															<input id="formPageSize" name="formPageSize" type="number" class="form-control" value="10" style="width:200px;" />
														</div>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
														<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;">
															<lable style="float:left">页面尺寸集合</lable>
															<input id="formPageSizeList" name="formPageSizeList" type="text" placeholder="[10,20,50,100]" value="[10,20,50,100]" class="form-control" style="width:200px;" />
														</div>
													</div>
	                                            </div>
	                                        </div>
	                                        
	                                        <div id="form3">
	                                        	<h3 style="display:block;font-size:18;text-align:left;height: 36px;line-height: 33px;padding-left: 16px;color: #666;cursor: pointer;">显示配置JS参数</h3>
	                                        	<div class="row cell" style = "padding-left:15px">
	                                        		<div class="BTNGROUP_55" style="margin:0 auto; display:inline-block;min-height: 20px;">
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="saveJS"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"保存参数")%> onclick="saveJs()"/>
														</div>
													</div>
													<table id="jsList">
													</table>
												</div>
	                                        </div>
                                        </div>  
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    
                    <h1>字段配置</h1>
                    <fieldset>
                        <div class="row">
                            <div id="form_step2" name="form_step2" class="col-sm-12" >
                                <div class="container-fluid" style="">
                                    <div style="margin-top:5px;">
                                        <div style="margin:10px 0;height:100%">
                                        
	                                        <div id="step2_part1" style="height:100%;padding:10px;display:block;overflow:auto">
		                                        <h3 style="display:block;font-size:18;text-align:left;height: 36px;line-height: 33px;padding-left: 16px;color: #666;cursor: pointer;margin:0">自定义sql</h3>
		                                        <div class="row cell" style = "padding-left:15px">
		                                        	<!-- 
		                                        	<div id="Tree" class="nui-layout" style="height: 89%;position:relative;float:left;width: 17%;margin:0;">
														<div title="实体树" region="west" style="display:block;height:61%;margin-bottom:1%"   showHeader="false" showFilter="false" class="sub-sidebar" allowResize="false">
															<div class="form-control menuContent" id="entityTree" style="padding:5px;overflow:auto;width:98%;height:100%;margin:0 auto;">
																<ul id="entityTreeDemo" class="ztree"  ></ul>
															</div>
														</div>
														<div title="参数树" region="west" style="display:block;height:39%" bodyStyle="overflow:hidden;" showHeader="false" showFilter="false" class="sub-sidebar" allowResize="false">
															<div class="form-control menuContent" id="paramTree" style="padding:5px;overflow:auto;width:98%;height:100%;margin:0 auto;">
																<ul id="paramTreeDemo" class="ztree"  ></ul>
															</div>
														</div>
													</div>
													 -->
													 
													<div id="Tree" class="nui-layout" style="height: 89%;position:relative;float:left;width: 17%;margin:0;">
														<div class="zTreeDemoBackground">
													        <div class="tab" role="tabpanel">
													            <!-- Nav tabs -->
													            <ul class="nav nav-tabs" role="tablist">
													                <li role="presentation" class="active"><a href="#Section1" aria-controls="home" role="tab" data-toggle="tab"><span><i class="glyphicon glyphicon-globe"></i></span></a></li>
													                <li role="presentation"><a href="#Section2" aria-controls="profile" role="tab" data-toggle="tab"><span><i class="glyphicon glyphicon-search"></i></span></a></li>
													            </ul>
													            <!-- Tab panes -->
													            <div class="tab-content tabs" style="height:93%">
													                <div role="tabpanel" class="tab-pane panel panel-primary fade in active" id="Section1" style="height:100%;margin:0">
													                    <div class="panel-heading">实体树</div>
													                    <div class="sidebar panel-body">   
													                        <div class="searchInput">
													                            <input type="text"  id="key" class="empty form-control" placeholder="Search..." onkeyup="callNumber()">
													                            <i class="glyphicon glyphicon-search" style="position: absolute;right: 6px;top: 6px;cursor: pointer;"></i>
													                            <div class="searchInfo">  
													                                <label type="text" class="searchInfoWrapper"  id="resultKey">  
													                                    <div>  
													                                        <i id="clickUp" class="glyphicon glyphicon-arrow-up" onclick="clickUp()" style="top:4px;"></i>
													                                        <i id="clickDown" class="glyphicon glyphicon-arrow-down" onclick="clickDown()" style="top:4px;"></i>  
													                                    </div>  
													                                    <label id="number"></label>  
													                                </label>  
													                            </div>                  
													                        </div>  
													                        <ul id="entityTreeDemo" style="overflow-y: auto;overflow-x: hidden;height: 86%;margin-top: 10px;" class="ztree">
													                        </ul>
													                    </div>  
													                </div>
													                <div role="tabpanel" class="tab-pane panel panel-info fade" id="Section2"  style="height:100%;margin:0">
													                    <div class="panel-heading">参数树</div>
													                    <div class="sidebar panel-body">   
													                        <div class="searchInput">
													                            <input type="text"  id="key2" class="empty form-control" placeholder="Search..." onkeyup="callNumber2()">
													                            <i class="glyphicon glyphicon-search" style="position: absolute;right: 6px;top: 6px;cursor: pointer;"></i>
													                            <div class="searchInfo">  
													                                <label type="text" class="searchInfoWrapper" id="resultKey2">  
													                                    <div>  
													                                        <i id="clickUp2" class="glyphicon glyphicon-arrow-up" onclick="clickUp2()" style="top:4px;"></i>
													                                        <i id="clickDown2" class="glyphicon glyphicon-arrow-down" onclick="clickDown2()" style="top:4px;"></i>  
													                                    </div>  
													                                    <label id="number2"></label>  
													                                </label>  
													                            </div>                  
													                        </div>  
													                        <ul id="paramTreeDemo" style="overflow-y: auto;overflow-x: hidden;height: 80%;margin-top: 10px;" class="ztree">
													                        </ul>
													                    </div> 
													                </div>
													            </div>
													        </div>
														</div>
													</div> 
													 
													<div id="right_part_step2" style="float:left;width: 83%;margin:32px 0 0 0;">
														
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style = "">
															<div class="BTNGROUP_55" style="margin:0 auto; display:inline-block;min-height: 20px;">
															<div style="margin-right:10px;display:inline-block">
															    <input type="button" id="saveSql"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"保存SQL")%> onclick="saveSql()"/>
															</div>
															<div style="margin-right:10px;display:inline-block">
															    <input type="button" id="changeSql"  class="btn btn-info" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"转换SQL")%> onclick="changeSql()"/>
															</div>
															<div style="margin-right:10px;display:inline-block">
															    <input type="button" id="cleanSql"  class="btn btn-danger" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"清空")%> onclick="cleanSql()"/>
															</div>
														</div>
		                                                    <div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%">
		                                                        <textarea id="formSql"  name="formSql" class="form-control" style="width:100%;height:78%; resize:none; padding:15px 10px 15px 30px;"></textarea>
		                                                    </div>
		                                                </div>
													</div>	
												</div>
	                                        </div>
	                                        
	                                        <div id="step2_part2" style="height:45%;padding:10px;display:block;height:88%;">
	                                        	<h3 style="display:block;font-size:18;text-align:left;height: 36px;line-height: 33px;padding-left: 16px;color: #666;cursor: pointer;margin:0">字段明细</h3>
	                                        	<div class="row cell" style = "padding-left:15px;width:50%;float:left;">
													<div class="BTNGROUP_55" style="margin:0 auto; display:inline-block;min-height: 20px;" >
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="mapping"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"映射")%> onclick="mapping()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="addColumn"  class="btn btn-info" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"新增")%> onclick="addColumn()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="deleteColumn"  class="btn btn-danger" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"删除")%> onclick="deleteColumns()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="refreshColumns"  class="btn btn-info" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"刷新")%> onclick="refreshColumns()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="widgetCondition"  class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"属性")%> onclick="widgetCondition()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="batchAction"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"批量")%> onclick="batchUpdate()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="createGroup"  class="btn btn-info" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"分组")%> onclick="createGroup()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="formView"  class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"预览")%> onclick="formView()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="invert"  class="btn btn-danger" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"反选")%> onclick="reveseCheck()"/>
														</div>
														<div style="margin-right:10px;display:inline-block">
														    <input type="button" id="model"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"模板")%> onclick="model()"/>
														</div>
													</div>
													<table id="columnList">
													</table>
												</div>
												
												<div class="container-fluid" style="width:50%;float:left;border:none;margin-top:32px;background-color:#efefef">
													<ul class="nav nav-tabs" id="myTab">
														<li class="active"><a href="#Tab_1">字段信息</a></li>
														<li ><a href="#Tab_2" >控件信息</a></li>
														<li ><a href="#Tab_3" >链接信息</a></li>
													</ul>
													<div class="tab-content">
														<div class="tab-pane active" id="Tab_1">
															<div id="columnForm" class="" style="">
																<div class="row cell" style="line-height:25px;height:95%;overflow:auto;" >
																	<div id="columnInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联实体ID")%>: </lable>
																			<input id="formColumnEntityId" name="formColumnEntityId" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联实体编码")%>: </lable>
																			<input id="formEntityTableName" name="formEntityTableName" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联字段ID")%>: </lable>
																			<input id="formColumnColumnId" name="formColumnColumnId" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联字段编码")%>: </lable>
																			<input id="formFieldTableName" name="formFieldTableName" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="formColumnLable" name="formColumnLable" type="text" class="form-control"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"Label长度")%>: </lable>
																			<input id="FORM_COLUMN_LABEL_WIDTH" name="FORM_COLUMN_LABEL_WIDTH" type="text" class="form-control"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"控件类型")%>: </lable>
																			<select id="formColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"控件类型")%>" styleType="select" name="formColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段宽度")%>: </lable>
																			<input id="formColumnWidth" name="formColumnWidth" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段高度")%>: </lable>
																			<input id="formColumnHeight" name="formColumnHeight" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo10" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示方式")%>: </lable>
																			<select id="formColumnShowType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示方式")%>" styleType="select" name="formColumnShowType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_COLUMN_SHOW_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示顺序")%>: </lable>
																			<input id="formColumnSort" name="formColumnSort" type="text" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo11" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"对齐方式")%>: </lable>
																			<select id="formColumnAlign" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"对齐方式")%>" styleType="select" name="formColumnAlign"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_COLUMN_ALIGN" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo12" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否必填")%>: </lable>
																			<select id="formColumnRequired" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否必填")%>" styleType="select" name="formColumnRequired"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo13" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"跨字段数")%>: </lable>
																			<input id="formColumnColSpan" name="formColumnColSpan" type="number" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo14" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"最大字符数")%>: </lable>
																			<input id="formColumnMaxLength" name="formColumnMaxLength" type="text" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo15" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"最小字符数")%>: </lable>
																			<input id="formColumnMinLength" name="formColumnMinLength" type="text" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo16" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段值范围")%>: </lable>
																			<input id="formColumnColorCondition" name="formColumnColorCondition" type="text" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo17" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段颜色")%>: </lable>
																			<input id="formColumnColor" name="formColumnColor" type="color" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo22" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"所属分组")%>: </lable>
																			<select id="formGroupId" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"所属分组")%>" styleType="select" name="formGroupId"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="" nullItemText="请选择..." dataField="sql_form_0001" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo18" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"列类型")%>: </lable>
																			<select id="formColumnType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"列类型")%>" disabled styleType="select" name="formColumnType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_COLUMN_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo19" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"夸行数")%>: </lable>
																			<input id="formColumnRowSpan" name="formColumnRowSpan" type="number" class="form-control" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="columnInfo20" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否合计")%>: </lable>
																			<select id="formColumnTotal" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"列类型")%>" styleType="select" name="formColumnTotal"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="columnInfo21" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否让开发字段")%>: </lable>
																			<select id="formColumnIsDev" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"列类型")%>" styleType="select" name="formColumnIsDev"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																</div>		
																<div class="BTNGROUP BTNGROUP_1004" style="margin:0 0 0 15px; display:inline-block;height: 30px;">
																	<div style="margin-right:10px;display:inline-block">
																	    <input type="button" id="saveColumnInfo"  class="btn btn-primary" value=保存 onclick="saveColumnInfo()"/>
																	</div>
																</div>
															</div>
														</div>
														<div class="tab-pane" id="Tab_2">
															<div id="widgetForm" class="" style="">

																<div id="text" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetTextInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="textboxId" name="textboxId" type="hidden"/>
																			<input id="textboxColumnLable" name="textboxColumnLable" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>" readonly  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="textboxColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="textboxColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验类型")%>: </lable>
																			<select id="textboxCheckType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验类型")%>" styleType="select" name="textboxCheckType" onchange="textCheckTypeStep2()"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_DATA_CHECK_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义校验信息")%>: </lable>
																			<input id="textboxCheckSelf" name="textboxCheckSelf" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="textboxDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" styleType="select" onchange="textDataFromTypeStep2()" name="textboxDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源")%>: </lable>
																			<input id=textboxDataFromValue name="textboxDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"空文本提示")%>: </lable>
																			<input id="textboxEmptytext" name="textboxEmptytext" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextInfo8" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="textboxInitFun" name="textboxInitFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextInfo9" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验方案")%>: </lable>
																			<textarea id="textboxCheckFun" name="textboxCheckFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextInfo10" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值改变事件方案")%>: </lable>
																			<textarea id="textboxValuechangeFun" name="textboxValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextInfo11" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"点击事件方案")%>: </lable>
																			<textarea id="textboxClickFun" name="textboxClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextInfo12" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否设置链接")%>: </lable>
																			<input id="textboxIsLink" name="textboxIsLink" type="checkbox" class="checkbox"  style="width:15%;"/>
																		</div>
																	</div>
																</div>	
																
																<div id="combobox" class="row cell" style="border:none;line-height:25px;height:95%;overflow:auto;" >
																	<div id="widgetComboboxInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="comboboxId" name="comboboxId" type="hidden"/>
																			<input id="comboboxFormColumnLable" name="comboboxFormColumnLable" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="comboboxFormColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" name="comboboxFormColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" disabled="true" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	
																	<div id="widgetComboboxInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>: </lable>
																			<select id="comboboxGuiInitType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>" onchange="cboxGuiChanged()" styleType="select" name="comboboxGuiInitType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_FROM_TYPE" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo4" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>: </lable>
																			<!-- 自定义快捷下拉 -->
																			<textarea id="comboboxGuiInitValue1" name="comboboxGuiInitValue1" class="form-control comboboxGuiInitValue1" vtype=""  style="width:81%;resize:none;height:58px;display:none"></textarea>
																			<i class="glyphicon glyphicon-plus comboboxGuiInitValue1" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;display:none" onclick="inputText(this)" placeholder=""></i>
																			<!-- 字典式下拉 -->
																			<input type='text' class="form-control comboboxGuiInitValue2" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>"  style="width:69%;float:left" id="comboboxGuiInitName2">
																			<input type='hidden' class="form-control comboboxGuiInitValue2" id="comboboxGuiInitValue2">
																			<input type="button" id="comboboxGuiInitValue2"  class="btn btn-warning comboboxGuiInitValue2" style="float:left" value='选择字典' onclick="chooseDictStep2('combobox')"/>
																			<!-- SQL式下拉 -->
																			<input type='text' class="form-control comboboxGuiInitValue3" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>" value="--未编写SQL--"  style="width:69%;float:left;display:none" id="comboboxGuiInitValue3">
																			<input type='hidden' class="form-control" id="comboboxGuiInitExcsqlId">
																			<input type="button" id="comboboxGuiInitValue3"  class="btn btn-warning comboboxGuiInitValue3" style="float:left;display:none" value='编写SQL ' onclick="addSqlStep2('combobox')"/>
																			
																		</div>
																	</div>
																	
																	<div id="widgetComboboxInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="comboboxDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" onchange="comboboxDataFromTypeStep2()" styleType="select" name="comboboxDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始化来源")%>: </lable>
																			<input id="comboboxDataFromValue" name="comboboxDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"文本显示字段")%>: </lable>
																			<input id="comboboxTextfield" name="comboboxTextfield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值字段")%>: </lable>
																			<input id="comboboxValuefield" name="comboboxValuefield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据对象名称")%>: </lable>
																			<input id="comboboxDatafield" name="comboboxDatafield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo10" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"空值文本")%>: </lable>
																			<input id="comboboxEmptytext" name="comboboxEmptytext" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo11" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"空项文本")%>: </lable>
																			<input id="comboboxNullitemtext" name="comboboxNullitemtext" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo14" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"允许输入")%>: </lable>
																			<input id="comboboxAllowinput" name="comboboxAllowinput" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo15" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">		
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"带搜索框")%>: </lable>
																			<input id="comboboxIsSearch" name="comboboxIsSearch" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo16" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"允许多选")%>: </lable>
																			<input id="comboboxMultiselect" name="comboboxMultiselect" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo17" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="comboboxInitFun" name="comboboxInitFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo12" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格编辑方案")%>: </lable>
																			<textarea id="comboboxValuechangeFun" name="comboboxValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:58px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetComboboxInfo13" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格绘制方案")%>: </lable>
																			<textarea id="comboboxClickFun" name="comboboxClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	
																</div>
																
																<div id="date" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetDateInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="datepickerId" name="datepickerId" type="hidden"/>
																			<input id="dateFormColumnLable" name="dateFormColumnLable" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly=true  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="dateFormColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="dateFormColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	
																	<div id="widgetDateInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="datepickerDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" styleType="select" onchange="dateDataFromTypeStep2()" name="datepickerDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetDateInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源")%>: </lable>
																			<input id="datepickerDataFromValue" name="datepickerDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"日期格式")%>: </lable>
																			<select id="datepickerFormat" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"日期格式")%>" styleType="select" name="datepickerFormat"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_DATEFORMAT" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetDateInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"允许输入")%>: </lable>
																			<input id="datepickerAllowinput" name="datepickerAllowinput" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12" >
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示\"时间输入框\"")%>: </lable>
																			<input id="datepickerShowtime" name="datepickerShowtime" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示\"确定\"按钮")%>: </lable>
																			<input id="datepickerShowokbutton" name="datepickerShowokbutton" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo10" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示\"清除\"按钮")%>: </lable>
																			<input id="datepickerShowclearbutton" name="datepickerShowclearbutton" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	<div id="widgetDateInfo11" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"显示\"今天\"按钮")%>: </lable>
																			<input id="datepickerShowtodaybutton" name="datepickerShowtodaybutton" type="checkbox" class="checkbox"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:15%;"/>
																		</div>
																	</div>
																	
																	<div id="widgetDateInfo12" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="datepickerInitFun" name="datepickerInitFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px" placeholder="--通过JS代码给字段赋初值--"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetDateInfo13" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格编辑事件")%>: </lable>
																			<textarea id="datepickerValuechangeFun" name="datepickerValuechangeFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px" placeholder="--单元格值改变后执行JS--"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetDateInfo14" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格绘制方案")%>: </lable>
																			<textarea id="datepickerClickFun" name="datepickerClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px" placeholder="--单元格点击后执行JS--"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetDateInfo15" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义绘制方案")%>: </lable>
																			<textarea id="datepickerDrawdatefun" name="datepickerDrawdatefun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>	
																
																<div id="textarea" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetTextareaInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="textareaId" name="textareaId" type="hidden"/>
																			<input id="textareaFormColumnId" name="textareaFormColumnId" type="hidden"/>
																			<input id="textareaFormColumnLable" name="textareaFormColumnLable" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>" readonly  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="textareaGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="textareaColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验类型")%>: </lable>
																			<select id="textareaCheckType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验类型")%>" styleType="select" name="textareaCheckType" onchange="textareaCheckTypeStep2()"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_DATA_CHECK_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义校验信息")%>: </lable>
																			<input id="textareaCheckSelf" name="textareaCheckSelf" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="textareaDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" styleType="select" onchange="textareaDataFromTypeStep2()" name="textareaDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源")%>: </lable>
																			<input id="textareaDataFromValue" name="textareaDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo8" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="textareaInitFun" name="textareaInitFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo9" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据校验方案")%>: </lable>
																			<textarea id="textareaCheckFun" name="textareaCheckFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo10" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值改变事件方案")%>: </lable>
																			<textarea id="textareaValuechangeFun" name="textareaValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetTextareaInfo11" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"点击事件方案")%>: </lable>
																			<textarea id="textareaClickFun" name="textareaClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>
																
																<div id="radiolist" class="row cell" style="border:none;line-height:25px;height:95%;overflow:auto;" >
																	<div id="widgetRadiolistInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="radiolistId" name="radiolistId" type="hidden"/>
																			<input id="radiolistFormColumnLable" name="radiolistFormColumnLable" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="radiolistFormColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" name="radiolistFormColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" disabled="true" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																
																	<div id="widgetRadiolistInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>: </lable>
																			<select id="radiolistGuiInitType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>" onchange="rListGuiChanged()" styleType="select" name="radiolistGuiInitType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_FROM_TYPE" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo4" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>: </lable>
																			<!-- 自定义快捷下拉 -->
																			<textarea id="radiolistGuiInitValue1" name="radiolistGuiInitValue1" class="form-control radiolistGuiInitValue1" vtype=""  style="width:81%;resize:none;height:58px;display:none"></textarea>
																			<i class="glyphicon glyphicon-plus radiolistGuiInitValue1" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;display:none" onclick="inputText(this)" placeholder=""></i>
																			<!-- 字典式下拉 -->
																			<input type='text' class="form-control radiolistGuiInitName2" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>"  style="width:69%;float:left" id="radiolistGuiInitName2">
																			<input type='hidden' class="form-control radiolistGuiInitName2" id="radiolistGuiInitValue2">
																			<input type="button" id="radiolistGuiInitValue2"  class="btn btn-warning radiolistGuiInitName2" style="float:left" value='选择字典' onclick="chooseDictStep2('radiolist')"/>
																			<!-- SQL式下拉 -->
																			<input type='text' class="form-control radiolistGuiInitValue3" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>" value="--未编写SQL--"  style="width:69%;float:left;display:none" id="radiolistGuiInitValue3">
																			<input type="button" id="radiolistGuiInitValue3"  class="btn btn-warning radiolistGuiInitValue3" style="float:left;display:none" value='编写SQL ' onclick="addSqlStep2(this)"/>
																			
																		</div>
																	</div>
																
																	<div id="widgetRadiolistInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="radiolistDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" onchange="radiolistDataFromTypeStep2()" styleType="select" name="radiolistDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始化来源")%>: </lable>
																			<input id="radiolistDataFromValue" name="radiolistDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo10" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺方向")%>: </lable>
																			<select id="radiolistRepeatdirection" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺方向")%>" styleType="select" name="radiolistRepeatdirection"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="TILE_TYPE" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo11" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺显示项")%>: </lable>
																			<input id="radiolistRepeatitems" name="radiolistRepeatitems" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo14" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"布局方式")%>: </lable>
																			<select id="radiolistRepeatlayout" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"布局方式")%>" styleType="select" name="radiolistRepeatlayout"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="radiolistRepeatlayout" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"文本显示字段")%>: </lable>
																			<input id="radiolistTextfield" name="radiolistTextfield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值字段")%>: </lable>
																			<input id="radiolistValuefield" name="radiolistValuefield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据对象名称")%>: </lable>
																			<input id="radiolistDatafield" name="radiolistDatafield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo17" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="radiolistInitFun" name="radiolistInitFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo12" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格编辑方案")%>: </lable>
																			<textarea id="radiolistValuechangeFun" name="radiolistValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:58px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetRadiolistInfo13" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格绘制方案")%>: </lable>
																			<textarea id="radiolistClickFun" name="radiolistClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>

																<div id="checkboxlist" class="row cell" style="border:none;line-height:25px;height:95%;overflow:auto;" >
																	<div id="widgetCheckboxlistInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="checkboxlistId" name="checkboxlistId" type="hidden"/>
																			<input id="checkboxlistFormColumnLable" name="checkboxlistFormColumnLable" type="text" class="form-control" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="checkboxlistFormColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" name="checkboxlistFormColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" disabled="true" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																
																	<div id="widgetCheckboxlistInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>: </lable>
																			<select id="checkboxlistGuiInitType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化类型")%>" onchange="cListGuiChanged()" styleType="select" name="checkboxlistGuiInitType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_FROM_TYPE" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo4" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>: </lable>
																			<!-- 自定义快捷下拉 -->
																			<textarea id="checkboxlistGuiInitValue1" name="checkboxlistGuiInitValue1" class="form-control checkboxlistGuiInitValue1" vtype=""  style="width:81%;resize:none;height:58px;display:none"></textarea>
																			<i class="glyphicon glyphicon-plus checkboxlistGuiInitValue1" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;display:none" onclick="inputText(this)" placeholder=""></i>
																			<!-- 字典式下拉 -->
																			<input type='text' class="form-control checkboxlistGuiInitName2" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>"  style="width:69%;float:left" id="checkboxlistGuiInitName2">
																			<input type='hidden' class="form-control checkboxlistGuiInitName2" id="checkboxlistGuiInitValue2">
																			<input type="button" id="checkboxlistGuiInitValue2"  class="btn btn-warning checkboxlistGuiInitName2" style="float:left" value='选择字典' onclick="chooseDictStep2('checkboxlist')"/>
																			<!-- SQL式下拉 -->
																			<input type='text' class="form-control checkboxlistGuiInitValue3" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据初始化值")%>" value="--未编写SQL--"  style="width:69%;float:left;display:none" id="checkboxlistGuiInitValue3">
																			<input type="button" id="checkboxlistGuiInitValue3"  class="btn btn-warning checkboxlistGuiInitValue3" style="float:left;display:none" value='编写SQL ' onclick="addSqlStep2(this)"/>
																			
																		</div>
																	</div>
																
																	<div id="widgetCheckboxlistInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="checkboxlistDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" onchange="checkboxlistDataFromTypeStep2()" styleType="select" name="checkboxlistDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始化来源")%>: </lable>
																			<input id="checkboxlistDataFromValue" name="checkboxlistDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo10" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺方向")%>: </lable>
																			<select id="checkboxlistRepeatdirection" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺方向")%>" styleType="select" name="checkboxlistRepeatdirection"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="TILE_TYPE" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo11" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"平铺显示项")%>: </lable>
																			<input id="checkboxlistRepeatitems" name="checkboxlistRepeatitems" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo14" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"布局方式")%>: </lable>
																			<select id="checkboxlistRepeatlayout" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"布局方式")%>" styleType="select" name="checkboxlistRepeatlayout"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="radiolistRepeatlayout" value="dict" dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"文本显示字段")%>: </lable>
																			<input id="checkboxlistTextfield" name="checkboxlistTextfield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值字段")%>: </lable>
																			<input id="checkboxlistValuefield" name="checkboxlistValuefield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据对象名称")%>: </lable>
																			<input id="checkboxlistDatafield" name="checkboxlistDatafield" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" readonly style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo17" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="checkboxlistInitFun" name="checkboxlistInitFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo12" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格编辑方案")%>: </lable>
																			<textarea id="checkboxlistValuechangeFun" name="checkboxlistValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:58px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetCheckboxlistInfo13" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"单元格绘制方案")%>: </lable>
																			<textarea id="checkboxlistClickFun" name="checkboxlistClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:57px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>
																
																<div id="checkbox" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetCheckboxInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="checkboxId" name="checkboxId" type="hidden"/>
																			<input id="checkboxColumnLable" name="checkboxColumnLable" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>" readonly  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="checkboxColumnGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="checkboxColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="checkboxDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" styleType="select" onchange="checkboxDataFromTypeStep2()" name="checkboxDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源")%>: </lable>
																			<input id=checkboxDataFromValue name="checkboxDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"复选框显示名称")%>: </lable>
																			<input id="checkboxShowName" name="checkboxShowName" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo8" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="checkboxInitFun" name="checkboxInitFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo10" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值改变事件方案")%>: </lable>
																			<textarea id="checkboxValuechangeFun" name="checkboxValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetCheckboxInfo11" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"点击事件方案")%>: </lable>
																			<textarea id="checkboxClickFun" name="checkboxClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>	
																
																<div id="lookup" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetLookupInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="lookupId" name="lookupId" type="hidden"/>
																			<input id="lookupFormColumnId" name="lookupFormColumnId" type="hidden"/>
																			<input id="lookupFormColumnLable" name="lookupFormColumnLable" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>" readonly  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="lookupGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="lookupColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetLookupInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>: </lable>
																			<select id="lookupDataFromType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源类型")%>" styleType="select" onchange="lookupDataFromTypeStep2()" name="lookupDataFromType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_INIT_DATA_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetLookupInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值来源")%>: </lable>
																			<input id="lookupDataFromValue" name="lookupDataFromValue" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;"  style="width:60%;"/>
																		</div>
																	</div>
																	
																	<div id="widgetLookupInfo3" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口类型")%>: </lable>
																			<select id="lookupFormWinType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口类型")%>" styleType="select" onchange="lookupFormWinTypeStep2()" name="lookupFormWinType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_WIN_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetLookupInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookuptype2" >
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口标题")%>: </lable>
																			<input id="lookupWinTitle" name="lookupWinTitle" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" value="选择" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookuptype2">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口宽度")%>: </lable>
																			<input id="lookupWinWidth" name="lookupWinWidth" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" value="500" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo9" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookuptype2">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口高度")%>: </lable>
																			<input id="lookupWinHeight" name="lookupWinHeight" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" value="600" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo14" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookuptype2">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"文本显示字段")%>: </lable>
																			<input id="lookupShowProperty" name="lookupShowProperty" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" value="text" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo15" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookuptype2">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值字段")%>: </lable>
																			<input id="lookupValueProperty" name="lookupValueProperty" type="text" class="form-control"  vtype="illegalChar;maxLength:255;minLength:null;" value="id" style="width:60%;"/>
																		</div>
																		
																	</div>
																	<div id="widgetLookupInfo16" class="col-lg-12 col-md-12 col-sm-12 col-xs-12 lookupForm">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"动态表单")%>: </lable>
																			<input id="lookupDetailFormId" name="lookupDetailFormId" type="hidden"/>
																			<input type='text' class="form-control" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"动态表单")%>" readonly style="width:57%;float:left" id="lookupDetailFormName">
																			<input type="button" id="chooseFormStep2"  class="btn btn-primary" style="float:left" value='选择表单 ' onclick="chooseFormStep2('lookup')"/>
																			<input type="button" id="addParamStep2"  class="btn btn-warning" style="float:left" value='参数配置 ' onclick="addParamStep2('lookup')"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo20" class="col-lg-12 col-md-12 col-sm-12 col-xs-12 lookupUrl" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单URL")%>: </lable>
																			<input type='text' class="form-control" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单URL")%>"  style="width:57%;float:left" id="lookupFormUrl">
																			<input type="button" id="addParamStep2"  class="btn btn-warning" style="float:left" value='参数配置 ' onclick="addParamStep2('lookup')"/>
																		</div>
																	</div>
																	<div id="widgetLookupInfo17" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 lookupForm">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否自定义回显")%>: </lable>
																			<select id="lookupCustomDataback" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否自定义回显")%>" styleType="select" onchange="lookupCustomDatabackFunStep2()" name="lookupCustomDataback"  class="form-control"  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_COMMON_YES_NO"  dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetLookupInfo18" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据回显类型")%>: </lable>
																			<select id="lookupDatabackType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"数据回显类型")%>" styleType="select" onchange="" name="lookupDatabackType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_LOOKUP_DATABACK" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetLookupInfo19" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"回显值来源")%>: </lable>
																			<select id="lookupDatabackValue" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"回显值来源")%>" styleType="select" onchange="" name="lookupDatabackValue"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="" nullItemText="请选择..." dataField="sql_form_0000" initParam = "_">
																			</select>
																		</div>
																	</div>
																	
																	<div id="widgetLookupInfo13" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none;">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口自定义方案")%>: </lable>
																			<textarea id="lookupButtonclickFun" name="lookupButtonclickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	
																	<div id="widgetLookupInfo8" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none;">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"初始值初始方案")%>: </lable>
																			<textarea id="lookupInitFun" name="lookupInitFun" class="form-control"  vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	
																	<div id="widgetLookupInfo10" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"值改变事件方案")%>: </lable>
																			<textarea id="lookupValuechangeFun" name="lookupValuechangeFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetLookupInfo11" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"点击事件方案")%>: </lable>
																			<textarea id="lookupClickFun" name="lookupClickFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																	<div id="widgetLookupInfo12" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none;">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;position:relative;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义回显方案")%>: </lable>
																			<textarea id="lookupCustomDatabackFun" name="lookupCustomDatabackFun" class="form-control" vtype=""  style="width:81%;resize:none;height:60px"></textarea>
																			<i class="glyphicon glyphicon-plus" style="position: absolute;opacity: 0.5;right: 20px;top: 6px;cursor: pointer;" onclick="inputText(this)"></i>
																		</div>
																	</div>
																</div>
																
																<div id="fileupload" class="row cell" style="border:none;line-height:25px;display:none;height:95%;overflow:auto;" >
																	<div id="widgetFileuploadInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>: </lable>
																			<input id="fileuploadId" name="fileuploadId" type="hidden"/>
																			<input id="fileuploadFormColumnId" name="fileuploadFormColumnId" type="hidden"/>
																			<input id="fileuploadFormColumnLable" name="fileuploadFormColumnLable" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段显示名称")%>" readonly  style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetFileuploadInfo2" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>: </lable>
																			<select id="fileuploadGuiType" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"字段控件类型")%>" styleType="select" disabled=true name="fileuploadColumnGuiType"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_GUI_TYPE" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetFileuploadInfo3" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"文件上传类型")%>: </lable>
																			<!-- 
																			<input id="alltypeFileupload" name="alltypeFileupload" type="checkbox" class="checkbox"  style="width:3%"/>*&nbsp;&nbsp;
																			<input id="xlsFileupload" name="xlsFileupload" type="checkbox" class="checkbox"  style="width:3%"/>xls&nbsp;&nbsp;
																			<input id="xlsxFileupload" name="xlsxFileupload" type="checkbox" class="checkbox"  style="width:3%"/>xlsx&nbsp;&nbsp;
																			<input id="docFileupload" name="docFileupload" type="checkbox" class="checkbox"  style="width:3%"/>doc&nbsp;&nbsp;
																			<input id="txtFileupload" name="txtFileupload" type="checkbox" class="checkbox"  style="width:3%"/>txt&nbsp;&nbsp;
																			<input id="pdfFileupload" name="pdfFileupload" type="checkbox" class="checkbox"  style="width:3%"/>pdf&nbsp;&nbsp;
																			<input id="pptFileupload" name="pptFileupload" type="checkbox" class="checkbox"  style="width:3%"/>ppt&nbsp;&nbsp;
																			 -->
																			<select id="fileuploadUploadFiletypes" title="文件上传类型" name="fileuploadUploadFiletypes"  class="selectpicker bla bla bli" multiple data-live-search="true" style="" showNullItem="true" textField="dictName" valueField="dictID" DictName="SYS_FORM_FILEUPLOAD_UPLOAD_FILETYPES" dataField="dictList_form_0" multiSelect="true">
																			</select>
																		</div>
																	</div>
																	<div id="widgetFileuploadInfo4" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"上传附件最大数")%>: </lable>
																			<input id="fileuploadMaxFileCount" name="fileuploadMaxFileCount" type="number" class="form-control" placeholder="0代表无限制" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetFileuploadInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否自动上传")%>: </lable>
																			<input id="fileuploadAutoUpload" name="fileuploadAutoUpload" type="checkbox" class="checkbox" checked style="width:6%"/>
																		</div>
																	</div>
																</div>
																																
																<div class="BTNGROUP BTNGROUP_1004" style="margin:0 0 0 15px; display:inline-block;height: 30px;">
																	<div style="margin-right:10px;display:inline-block">
																	    <input type="button" id="saveWidget"  class="btn btn-primary" value=保存 onclick="saveWidget()"/>
																	</div>
																</div>
																
															</div>
														</div>
														<div class="tab-pane" id="Tab_3">
															<div id="widgetForm" class="" style="">
																<div id="textLink" class="row cell" style="border:none;line-height:25px;height:95%;overflow:auto;" >
																	<div id="widgetTextLinkInfo1" class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否动态表单")%>: </lable>
																			<input id="textboxLinkIsForm" name="textboxLinkIsForm" type="checkbox" onclick="linkIsFormStep2()" class="checkbox" checked  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否动态表单")%>" />
																			<input id="textboxLinkForm" name="textboxLinkForm" type="hidden"/>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo2" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="display:none">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义URL")%>: </lable>
																			<input type='text' class="form-control" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"自定义URL")%>"  style="width:57%;float:left" id="textboxLinkUrl1">
																			<input type="button" id="addParamStep2"  class="btn btn-warning" style="float:left" value='参数配置 ' onclick="addParamStep2(this)"/>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo3" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联表单")%>: </lable>
																			<input id="textlinkDetailFormId" name="textlinkDetailFormId" type="hidden"/>
																			<input type='text' class="form-control" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"关联表单")%>" readonly style="width:57%;float:left" id="textlinkDetailFormName">
																			<input type="button" id="chooseFormStep2"  class="btn btn-primary" style="float:left" value='选择表单 ' onclick="chooseFormStep2('textlink')"/>
																			<input type="button" id="addParamStep2"  class="btn btn-warning" style="float:left" value='参数配置 ' onclick="addParamStep2(this)"/>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo4" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口打开方式")%>: </lable>
																			<input id="textboxLinkWinOpenType1" name="textboxLinkWinOpenType" type="radio" onclick="layerJsp()" class="radio" value="1" checked  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"弹出层")%>" style="float:left"/><p style="float:left;width:20%">弹出层</p>
																			<input id="textboxLinkWinOpenType2" name="textboxLinkWinOpenType" type="radio" onclick="newJsp()" class="radio" value="2" title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"新页面")%>" style="float:left"/><p style="float:left;width:20%">新页面</p>
																		
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo8" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 jumpJsp">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"成功之后处理")%>: </lable>
																			<select id="textboxLinkSuccessDeal" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"成功之后处理")%>" styleType="select" name="textboxLinkSuccessDeal"  class="form-control" emptyText="请选择..."  style="width:60%;" textField="dictName" valueField="dictID" DictName="SYS_FORM_BUTTON_ADD_SUCCESS" nullItemText="请选择..." dataField="dict" initParam = "_">
																			</select>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo5" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 jumpJsp">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口标题")%>: </lable>
																			<input id="textboxLinkWinTitle" name="textboxLinkWinTitle" type="text" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口标题")%>" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo6" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 jumpJsp">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口宽度")%>: </lable>
																			<input id="textboxLinkWinWidth" name="textboxLinkWinWidth" type="number" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口宽度")%>" style="width:60%;"/>
																		</div>
																	</div>
																	<div id="widgetTextLinkInfo7" class="col-lg-6 col-md-6 col-sm-12 col-xs-12 jumpJsp">
																		<div class="ui search selection dropdown entitybox field-control" style="display: inline-block;width:100%;">
																			<lable style="float:left;font-size:12px"><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口高度")%>: </lable>
																			<input id="textboxLinkWinHeight" name="textboxLinkWinHeight" type="number" class="form-control"  title = "<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"窗口高度")%>" style="width:60%;"/>
																		</div>
																	</div>
																</div>
																<div class="BTNGROUP BTNGROUP_1004" style="margin:0 0 0 15px; display:inline-block;height: 30px;">
																	<div style="margin-right:10px;display:inline-block">
																	    <input type="button" id="saveWidget"  class="btn btn-primary" value=保存 onclick="saveWidget()"/>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
	                                        </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
					
                    <h1>过滤配置</h1>
                    <fieldset>
                        <div class="row">
                            <div id="form_step3" name="form_step3" class="col-sm-12" >
                                <div class="container-fluid" style="">
                                    <div style="margin-top:5px;">
                                    	<div style="margin:10px 0;height:100%">
                                    		<div class="BTNGROUP_55" style="margin:0 auto; display:block;height: 35px;">
												<div style="margin-right:10px;display:inline-block">
												    <input type="button" id="addColumnstep3"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"增加")%> onclick="addRowstep3()"/>
												</div>
												<div style="margin-right:10px;display:inline-block">
												    <input type="button" id="save_liststep3"  class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"保存")%> onclick="saveDatastep3()"/>
												</div>
											</div>
											<table id="tableListstep3">
											</table>
                                    	</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    
                    <h1>查询配置</h1>
                    <fieldset>
                        <div class="row">
                            <div id="form_step4" name="form_step4" class="col-sm-12" >
                                <div class="container-fluid" style="">
                                    <div style="margin-top:5px;">
                                    	<div style="height:100%;font-size:12px">
                                    		<table>
												<tr>
													<td style="padding-top: 10px">
														<div class="BTNGROUP_55" style="margin:0 auto; display:block;height: 35px;">
															<div style="margin-right:10px;display:inline-block">
																<input type="button" id="addColumnStep4"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"增加")%> onclick="addRowStep4()"/>
															</div>
															<div style="margin-right:10px;display:inline-block">
																<input type="button" id="save_listStep4"  class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"保存")%> onclick="saveDataStep4()"/>
															</div>
														</div>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<lable><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"每行字段数")%>: </lable>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<input id="SYS_SYSTEM_SCHEME.gridFilterRowCount" name="gridFilterRowCount" type="text" class="form-control"  vtype="illegalChar;maxLength:null;minLength:null;" style="width:200px;"/>
														<input id="SYS_SYSTEM_SCHEME.gridFilterId" name="gridFilterId" type="hidden" class="form-control"  style="width:200px;"/>
														<input id="SYS_SYSTEM_SCHEME.gridFilterFormDefId" name="gridFilterFormDefId" type="hidden" class="form-control"  style="width:200px;"/>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<lable><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"Label宽度")%>: </lable>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<input id="SYS_SYSTEM_SCHEME.gridFilterLableWidth" name="gridFilterLableWidth" type="text" class="form-control"  vtype="illegalChar;maxLength:null;minLength:null;" style="width:200px;"/>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<lable><%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"是否支持高级查询")%>: </lable>
													</td>
													<td style="padding-left:10px;font-size:14px">
														<input  id="SYS_SYSTEM_SCHEME.gridFilterHeightGrade"  name="gridFilterHeightGrade" type="checkbox" value="1"/>
													</td>
												</tr>
											</table>
											<table id="tableListStep4">
											</table>
                                    	</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                    
                    <h1>按钮配置</h1>
                    <fieldset>
                        <div class="row">
                            <div id="form_step5" name="form_step5" class="col-sm-12" >
                                <div class="container-fluid" style="">
                                    <div style="margin-top:5px;">
                                    	<div style="margin:10px 0;height:100%">
                                    		<div class="BTNGROUP_55" style="margin:0 auto; display:block;height: 35px;">
												<div style="margin-right:10px;display:inline-block">
												    <input type="button" id="addColumnstep5"  class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"增加")%> onclick="addRowstep5()"/>
												</div>
												<div style="margin-right:10px;display:inline-block">
												    <input type="button" id="save_liststep5"  class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"保存")%> onclick="saveDatastep5()"/>
												</div>
											</div>
											<table id="tableListstep5">
											</table>
                                    	</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
					
                </div>
            </div>
        </div>
    </div>
</body>
<script>
window.onload=function(){
var  author1 ="${param.author1}"; 
var b = "<%=b%>";

if(author1!=""){
			b=b.substring(0,b.indexOf("author1")-1);
			
	        var author = md5(b);
            var  author1 = "${param.author1}"; 
           

	if(!author==author1){
		alert("请求不一致");
		custom_close()
	}
}
}
function custom_close(){  

window.opener=null;  
window.open('','_self');  
window.close();  
 
}  
var formGridId = "0";
var formId = ${param.formDefId};
$(document).ready(function () {
    $("#form").steps({
        bodyTag: "fieldset",
        onStepChanging: function (event, currentIndex, newIndex) {
            if (currentIndex == 0) {//离开基础配置
            		
                return saveFormInfo();
            }
            if (currentIndex == 1) {//离开字段配置
            	query_tablestep3();
                return true;
            }
            if (currentIndex == 2) {//离开过滤配置
            	document.getElementById("SYS_SYSTEM_SCHEME.gridFilterFormDefId").value=formId;
            	if(formId==undefined || formId=='' ){
            		formId = '${param.formDefId}';
            	}
            	
            	if(formId!='' && formId!=null){
            		_initDataStep4(formId);
            	}
            	query_tableStep4();
            }
            if (currentIndex == 3) {//离开按钮配置
            	query_tablestep5();
            }
            
            // Start validation; Prevent going forward if false
            return true;
            
           
        },
        onStepChanged: function (event, currentIndex, priorIndex) {
        },
        onFinishing: function (event, currentIndex) {
            var form = $(this);


            // Start validation; Prevent form submission if false
            return true;
        },
        onFinished: function (event, currentIndex) {
            var form = $(this);
            setForm(formId);//修改完表单信息加入缓存
            CloseWebPage();
            //真正完成后调用方法
            // Submit form input
            //form.submit();
        }
    });
    var classes = [];
	if(''=='APP'){
		$("input").each(function(){
			if($(this).attr('dataField') != undefined){
				classes.push($(this)[0]);
			}
		})
	}else{
		classes = $("#form_step1 select");
	}
	for(var i=0;i<classes.length;i++){
		parame.id=classes[i].id;
		var type=$(classes[i]).attr('multiSelect');
		var dataField=$(classes[i]).attr('dataField');
		if(dataField.split("_")[2]=="0000"){
 			dataField = dataField+"|${param.formDefId}";
 		}
		parame.placeholder="";
		parame.jsonParam.dictTypeCode=$(classes[i]).attr('DictName');
		if(type=="true"){
			myehr_initSelectMore(parame,dataField);
		}else{
			myehr_initSelect(parame,dataField);
		}
	}	
		
    formInit();//表单类型初始化
    
    jsParamInit();
    
    $('#myTab a').click(function (e) {
		e.preventDefault();
		var x = $(this)[0].innerHTML;
		var data = $("#columnList").bootstrapTable("getSelections")[0];
		if(x=="控件信息"){
			if(data==undefined){
				layer.alert('请选中一条字段', {
					  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
					  skin: 'layer-ext-moon' 
					})
					return false;
			}else{
				$(this).tab('show');
				$("#textboxColumnLable").val(data.formColumnLable);
				$("#comboboxFormColumnLable").val(data.formColumnLable);
				$("#dateFormColumnLable").val(data.formColumnLable);
			}
			
		}if(x=="链接信息"){
			if(data==undefined||data.formColumnGuiType!='1'){
				layer.alert('非文本字段不能设置链接', {
					  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
					  skin: 'layer-ext-moon' 
					})
					return false;
			}else if(!$("#textboxIsLink").prop('checked')){
						layer.alert('请查看是否勾选设置链接', {
							  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
							  skin: 'layer-ext-moon' 
							})
					}else{
						$(this).tab('show');
					}
		}else{
			$(this).tab('show');
		}
	});
    
});

function query_tablestep3() {   
    $("#tableListstep3").bootstrapTable({  
        url : '/myehr/form/queryWhereColumn.action?formId='+formId, 
		height:'800',
        queryParams :'',
        undefinedText : '-',  
        striped : true, // 是否显示行间隔色  
        cache : false, // 是否使用缓存  
        clickToSelect: true,
        pagination : false,        
        uniqueId : "templateColumnId", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ 
		{  
            title : '操作',  
            field : 'formWhereId', // 字段  
            align: 'center',
            valign: 'middle',
            width:50,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input type=\"form-control\" name=\"formWhereId\" style=\"width:0px;padding:0px;border:none;\" readonly=true data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"操作\" value=\""+value+"\"/>"+
            		   "<i class=\"glyphicon glyphicon-remove\" style=\"position: absolute;opacity: 0.5;right: 5px;top: 5px;cursor: pointer;\" onclick=\"removestep3(this)\"></i><div>" ;
        	}
		},{
			
            title : '连接条件',  
            field : 'formWhereJoinRule', // 字段  
            align: 'center',
            valign: 'middle',
            width:150,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><select id=\"formWhereJoinRule"+index+"\" class=\"form-control\" name=\"formWhereJoinRule\" DictName=\"SYS_FORM_JOIN_RULE\" dataField=\"\" style=\"padding:0 24px 0 12px;\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"连接条件\" value=\""+value+"\"/>";
        	}
        },{  
            title : '左括号',  
            field : 'formWhereLeftBracket', // 字段  
            align: 'center',
            valign: 'middle',
            width:175,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" name=\"formWhereLeftBracket\" style=\"padding:0 24px 0 12px;\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"左括号\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '查询字段',  
            field : 'formWhereColumnId',  
            align : 'center',  
            valign : 'middle', 
			width:200,
			formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"formWhereColumnId"+index+"\" class=\"form-control\" name=\"formWhereColumnId\" DictName=\"\" dataField=\"sql_form_0000\" data-type=\"text\" data-pk=\""+row.Id+"\"  data-title=\"查询字段\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '过滤值类型',  
            field : 'formWhereValueType',  
            align : 'center',  
            valign : 'middle',  
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"formWhereValueType"+index+"\" class=\"form-control\"  name=\"formWhereValueType\" DictName=\"SYS_FORM_PARAM_TYPE\" dataField=\"\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"过滤值类型\" value=\""+value+"\"></selcct>" ;
        	}
        }, {  
            title : '过滤值',  
            field : 'formWhereValue',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formWhereValue\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"过滤值\" value=\""+value+"\"/>";
        	}
        }, {  
            title : '过滤规则',  
            field : 'formWhereCpRule',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"formWhereCpRule"+index+"\" class=\"form-control\"  name=\"formWhereCpRule\" DictName=\"SYS_FORM_WHERE_RULE\" dataField=\"\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"过滤规则\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '排序',  
            field : 'formWhereSort',  
            align : 'center',  
            valign : 'middle',
            width:150,
            formatter: function (value, row, index) {
            	if(value==undefined){
		        		value = "";
		        	}
            	return "<input class=\"form-control\"  name=\"formWhereSort\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"排序\" value=\""+value+"\"/>" ;
        	}
		}, {  
            title : '空值是否过滤',  
            field : 'formWhereNullIs',  
            align : 'center',  
            valign : 'middle',
            width:175,
            formatter: function (value, row, index) {
            	if(value==undefined){
		        		value = "";
		        	}
            	return "<select id=\"formWhereNullIs"+index+"\" class=\"form-control\"  name=\"formWhereNullIs\" DictName=\"SYS_COMMON_YES_NO\" data-type=\"text\" data-pk=\""+row.Id+"\" dataField=\"\" data-title=\"空值是否过滤\" value=\""+value+"\"></select>" ;
        	}
		}, {  
            title : '右括号',  
            field : 'formWhereRightBracket',  
            align : 'center',  
            valign : 'middle',
            width:175,
            formatter: function (value, row, index) {
            	if(value==undefined){
		        		value = "";
		        	}
            	return "<input class=\"form-control\"  name=\"formWhereRightBracket\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"右括号\" value=\""+value+"\"/>" ;
        	}
        }],
        onClickRow: function (row, $element) {
	                curRow = row;
	                
	            },
	            onLoadSuccess: function (aa, bb, cc) {
	                addListSelectstep3();
	            },
	            data:[],       
        responseHandler : function(res) { 
        	rowsNum = res.total;
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        }, 
    });
}

function query_tablestep5() {   
    $("#tableListstep5").bootstrapTable({  
        url : '/myehr/form/queryFormButton.action?formId='+formId, 
		height:'800',
        queryParams :'',
        undefinedText : '-',  
        striped : true, // 是否显示行间隔色  
        cache : false, // 是否使用缓存  
        clickToSelect: true,
        pagination : false,        
        uniqueId : "formButtonId", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ 
		{  
            title : '操作',  
            field : 'formButtonId', // 字段  
            align: 'center',
            valign: 'middle',
            width:75,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input type=\"form-control\" name=\"formButtonId\" style=\"width:0px;padding:0px;border:none\" readonly=true data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"操作\" value=\""+value+"\"/>"+
						"<i class=\"glyphicon glyphicon-edit\" style=\"position: absolute;opacity: 0.5;right: 25px;top: 5px;cursor: pointer;\" onclick=\"editButtonstep5(this)\"></i>"+
						"<i class=\"glyphicon glyphicon-remove\" style=\"position: absolute;opacity: 0.5;right: 5px;top: 5px;cursor: pointer;\" onclick=\"removestep5(this)\"></i><div>" ;
        	}
		},{
            title : '按钮类型名称',  
            field : 'formButtonType',  
            align : 'center',  
            valign : 'middle', 
			width:200,
			formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"formButtonType"+index+"\" name=\"formButtonType\" class=\"form-control\" DictName=\"SYS_FORM_BUTTON_TYPE\" onchange=\"changeButtonType(this)\"  data-title=\"按钮类型名称\" value=\""+value+"\"/>" ;
        	}
		}, {  
            title : '按钮显示名称',  
            field : 'formButtonName',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonName\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮显示名称\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '按钮编码',  
            field : 'formButtonCode',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonCode\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮编码\" value=\""+value+"\"/>";
        	}
        }, {  
            title : '按钮显示顺序',  
            field : 'formButtonSort',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonSort\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮显示顺序\" value=\""+value+"\"/>";
        	}
		 }, {  
            title : '后置触发',  
            field : 'HZESQL',  
            align : 'center',  
            valign : 'middle', 
            width:300,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\"  name=\"HZESQL\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"后置触发\" value=\""+value+"\"/>"+
						"<i class=\"glyphicon glyphicon-plus\" style=\"position: absolute;opacity: 0.5;right: 5px;top: 5px;cursor: pointer;\" onclick=\"editESQL(this,'HZ')\"></i></div>";
        	}
        }, {  
            title : '自定义图标',  
            field : 'formButtonIcon',  
            align : 'center',  
            valign : 'middle',
            width:150,
            formatter: function (value, row, index) {
            	if(value==undefined){
		        		value = "";
		        	}
            	return "<input class=\"form-control\"  name=\"formButtonIcon\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"自定义图标\" value=\""+value+"\"/>" ;
        	}
		}, {  
            title : '按钮宽度',  
            field : 'formButtonWidth',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonWidth\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮宽度\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '按钮高度',  
            field : 'formButtonHeight',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonHeight\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮高度\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '按钮样式',  
            field : 'formButtonCss',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonCss\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"按钮样式\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '警告框提示内容',  
            field : 'formButtonWarningInfo',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonWarningInfo\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"警告框提示内容\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '前置脚本方案',  
            field : 'formButtonCheckFun',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"formButtonCheckFun\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"前置脚本方案\" value=\""+value+"\"/>";
        	}
		}, {  
            title : '前置触发',  
            field : 'QZESQL',  
            align : 'center',  
            valign : 'middle', 
            width:300,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\"  name=\"QZESQL\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"前置触发\" value=\""+value+"\"/>"+
						"<i class=\"glyphicon glyphicon-plus\" style=\"position: absolute;opacity: 0.5;right: 5px;top: 5px;cursor: pointer;\" onclick=\"editESQL(this,'QZ')\"></i></div>";
        	}
        }],
        onClickRow: function (row, $element) {
	                curRow = row;
	                
	            },
	            onLoadSuccess: function (aa, bb, cc) {
	                addListSelectstep5();
	            },
	            data:[],       
        responseHandler : function(res) { 
        	rowsNum = res.total;
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        }, 
    });
}

function formInit(){
	var param = {};
	param.formId = '${param.formDefId}';
	$.ajax({
		url:'${pageContext.request.contextPath }/form/loadGridFormInfo.action',
		type:'post',
		data:JSON.stringify(param),
		contentType: 'application/json;charset=utf-8',
		cache: false,
		async: false,
		success: function (data) {
			$("#formDefId").val(data.param.formDefId);
			$("#formDefLayoutType").val(data.param.formDefLayoutType);
			$("#formDefFolderId").val(data.param.formDefFolderId);
			$("#formDefName").val(data.param.formDefName);
			$("#formDefCode").val(data.param.formDefCode);
			
		if(data.param.formDefIsProcess!=null&&data.param.formDefIsProcess!=""){
				$("#formDefIsProcess").val(data.param.formDefIsProcess);
			}else{
				$("#formDefIsProcess").val("N");
			}
			if(data.param.formAuthorityIsControl!=null&&data.param.formAuthorityIsControl!=""){
				$("#formAuthorityIsControl").val(data.param.formAuthorityIsControl);
			}else{
				$("#formAuthorityIsControl").val("N");
			}
			if(data.param.isApp!=null&&data.param.isApp!=""){
				$("#isApp").val(data.param.isApp);
			}else{
				$("#isApp").val("N");
			}
			
			if(data.param.formDefSource!=null&&data.param.formDefSource!=""){
				$("#formDefSource").val(data.param.formDefSource);
			}else{
				$("#formDefSource").val("default");
			}
			
			$("#powerSql").val(data.param.powerSql);
			$("#orderSql").val(data.param.orderSql);
			$("#formDefDesc").val(data.param.formDefDesc);
			formSql=data.param.formDefEntitySql;
			realSql=data.param.formDefSql;
			$("#formDefInitQzJs").val(data.param.formDefInitQzJs);
			$("#formDefInitQzSql").val(data.param.formDefInitQzSql);
			if(data.formGrid!=null){
				formGridId = data.formGrid.formGridId+"";
				if(data.formGrid.formShowPager=="false"){
					$("#formShowPager").prop('checked',false);
				}
				if(data.formGrid.formIsIndex=="false"){
					$("#formIsIndex").prop('checked',false);
				}
				if(data.formGrid.formShowColumns=="false"){
					$("#formShowColumns").prop('checked',false);
				}
				if(data.formGrid.formEditNextOnEnter=="true"){
					$("#formEditNextOnEnter").prop('checked',true);
				}
				if(data.formGrid.formEditNextRowCell=="true"){
					$("#formEditNextRowCell").prop('checked',true);
				}
				if(data.formGrid.formAllowResize=="true"){
					$("#formAllowResize").prop('checked',true);
				}
				if(data.formGrid.formAllowCellEdit=="true"){
					$("#formAllowCellEdit").prop('checked',true);
				}
				if(data.formGrid.formAllowCellSelect=="true"){
					$("#formAllowCellSelect").prop('checked',true);
				}
				if(data.formGrid.formMultiSelect=="false"){
					$("#formMultiSelect").prop('checked',false);
				}
				if(data.formGrid.formShowTitle=="false"){
					$("#formShowTitle").prop('checked',false);
				}
				if(data.formGrid.formTableIsHandsontable=="true"){
					$("#formTableIsHandsontable").prop('checked',true);
				}
				$("#formPageSize").val(data.formGrid.formPageSize);

				$("#formPageSizeList").val(data.formGrid.formPageSizeList);

				$("#formFrozen").val(data.formGrid.formFrozen);

				$("#formGridHeight").val(data.formGrid.formGridHeight);
			}
			
			}
		});
}

function saveFormInfo(){
	var param = {};
	var flag = false;
		param.formAuthorityIsControl = $("#formAuthorityIsControl").val();
		param.formDefCode = $("#formDefCode").val();				
		param.formDefDesc = $("#formDefDesc").val();
		param.formDefEntitySql = formSql;
		param.formDefFolderId = $("#formDefFolderId").val();
		param.formDefId = $("#formDefId").val();
		param.formDefInitQzJs = $("#formDefInitQzJs").val();
		param.formDefInitQzSql = $("#formDefInitQzSql").val();	
		param.formDefIsProcess = $("#formDefIsProcess").val();
		param.formDefLayoutType = $("#formDefLayoutType").val();
		param.formDefName = $("#formDefName").val();
		param.formDefSource = $("#formDefSource").val();
		param.formDefSql = realSql;
		param.isApp = $("#isApp").val();
		param.orderSql = $("#orderSql").val();
		param.powerSql = $("#powerSql").val();
	
	var sysFormconfigGrid = {};
	sysFormconfigGrid.formGridId = formGridId;
	sysFormconfigGrid.formGridFormId = '${param.formDefId}';
	sysFormconfigGrid.formShowPage = $("#formShowPager").prop('checked');
	if($("#formPageSize").val()!=null&&$("#formPageSize").val()!=""){
		sysFormconfigGrid.formPageSize = $("#formPageSize").val();
	}else{
		sysFormconfigGrid.formPageSize = '10';
	}
	if($("#formPageSizeList").val()!=null&&$("#formPageSizeList").val()!=""){
		sysFormconfigGrid.formPageSizeList = $("#formPageSizeList").val();
	}else{
		sysFormconfigGrid.formPageSizeList = '[10,20,50,100]';
	}
	sysFormconfigGrid.formIsIndex = $("#formIsIndex").prop('checked');
	sysFormconfigGrid.formShowColumns = $("#formShowColumns").prop('checked');
	sysFormconfigGrid.formEditNextOnEnter = $("#formEditNextOnEnter").prop('checked');
	sysFormconfigGrid.formEditNextRowCell = $("#formEditNextRowCell").prop('checked');
	sysFormconfigGrid.formAllowResize = $("#formAllowResize").prop('checked');
	sysFormconfigGrid.formAllowCellEdit = $("#formAllowCellEdit").prop('checked');
	sysFormconfigGrid.formAllowCellSelect = $("#formAllowCellSelect").prop('checked');
	sysFormconfigGrid.formMultiSelect = $("#formMultiSelect").prop('checked');
	sysFormconfigGrid.formShowTitle = $("#formShowTitle").prop('checked');
	sysFormconfigGrid.formIndexWidth = '50px';
	sysFormconfigGrid.formMultiWidth = '50px';
	
	if($("#formFrozen").val()!=null&&$("#formFrozen").val()!=""){
		sysFormconfigGrid.formFrozen = $("#formFrozen").val();
	}else{
		sysFormconfigGrid.formFrozen = '-1';
	}
	if($("#formGridHeight").val()!=null&&$("#formGridHeight").val()!=""){
		sysFormconfigGrid.formGridHeight = $("#formGridHeight").val();
	}else{
		sysFormconfigGrid.formGridHeight = '30';
	}
	
	sysFormconfigGrid.formTableIsHandsontable = $("#formTableIsHandsontable").prop('checked');
	var map = {};
	map.param = param;
	map.formGrid = sysFormconfigGrid;
	
	var signstr = JSON.stringify(sysFormconfigGrid);
	var rule ='\"' ;
	var regS = new RegExp(rule,'g');
	
	var rule2 =':' ;
	var regS2 = new RegExp(rule2,'g');
	signstr = signstr.replace(regS, '');
	signstr = signstr.replace(regS2, '=');
	console.log(JSON.stringify(sysFormconfigGrid));
	var sign = md5(signstr);
			if(updataForm(param)){
		$.ajax({
			url:'${pageContext.request.contextPath }/form/updataGridFormParamx.action?sign='+sign,
			type:'POST',
			data: JSON.stringify(sysFormconfigGrid),
		    cache: false,
		    contentType: 'application/json;charset=utf-8',
			async: false,
			success: function (data) {
					if(data==1){
						//alert("基础配置完成");
						flag = true;
				        loadStep_2();
					}
				}
			});
	}
	return flag;
}

function updataForm(param){
	var flag = false;
	var signstr = JSON.stringify(param);
	var rule ='\"' ;
	var regS = new RegExp(rule,'g');
	
	var rule2 =':' ;
	var regS2 = new RegExp(rule2,'g');
	signstr = signstr.replace(regS, '');
	signstr = signstr.replace(regS2, '=');
	console.log(JSON.stringify(param));
	var sign = md5(signstr);	$.ajax({
		url:'${pageContext.request.contextPath }/form/updataGridFormParam1.action?sign='+sign,
		type:'POST',
		data: JSON.stringify(param),
	    cache: false,
	    contentType: 'application/json;charset=utf-8',
		async: false,
		success: function (data) {
				if(data=='0'){
					layer.alert('保存失败!表单编码重复', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
				} else if(data=='2'){
					layer.alert('保存失败!表单中存在多个此表单编码', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
					$('#formList_step2').bootstrapTable('refresh'); 
				} else if(data=='1'){
					flag = true;
					layer.alert('保存成功', {
						  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
				}	
			}
		});
	return flag;
}


function jsParamInit(){
	$("#jsList").bootstrapTable({  
        url : '${pageContext.request.contextPath }/form/findJsParamList.action?paramTypeValue='+${param.formDefId}, 
		height:'200',
        queryParams :'',
        undefinedText : '-',  
        striped : true, // 是否显示行间隔色  
        cache : false, // 是否使用缓存  
        clickToSelect: true,
        pagination : false,        
        uniqueId : "paramId", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ 
		{  
		    title : '主键',  
		    field : 'paramId', 
		    visible:false
		},
        {  
            title : '操作',  
            field : 'paramType', 
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {            	
            	return "<div style=\"position:relative\">"+
            		   "<i class=\"glyphicon glyphicon-remove\" style=\"position: absolute;opacity: 0.5;right: 38px;top: -6px;cursor: pointer;\" onclick=\"remove(this)\"></i><div>" ;
        	}
        },{  
            title : '参数名称',  
            field : 'paramName', 
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '绑定表单ID',  
            field : 'paramTypeValue',  
            align : 'center',  
            valign : 'middle', 
            visible:false
        }, {  
            title : '参数值来源类型',  
            field : 'paramFromType',  
            align : 'center',  
            valign : 'middle',  
            formatter: function (value, row, index) {
            	var select1,select2,select3,select4;
            	if(value==undefined){
	        		value = "";
	        	}else if(value=="session"){
	        		select1="selected";
	        	}else if(value=="containerParam"){
	        		select2="selected";
	        	}else if(value=="parameter"){
	        		select3="selected";
	        	}else if(value=="constant"){
	        		select4="selected";
	        	}
            	return  "		<select id=\"formDefLayoutType\" title=\"表单布局类型\" styleType=\"select\" name=\"buttonParamFrom\" value=\""+value+"\"  class=\"form-control\" >\n"+
						"			<option value >请选择</option>\n"+
						"			<option value=\"session\" "+select1+">会话参数</option>\n"+
						"			<option value=\"containerParam\" "+select2+">容器参数</option>\n"+
						"			<option value=\"parameter\" "+select3+">请求参数</option>\n"+
						"			<option value=\"constant\" "+select4+">常量</option>\n"+					
						"		</select>\n";
        	}
        }, {  
            title : '参数值来源',  
            field : 'paramFromValue',  
            align : 'center',  
            valign : 'middle', 
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"buttonParamValue\" data-type=\"text\" title=\"参数值来源\" value=\""+value+"\"/>";
        	}
        }  	  
        ],
        onClickRow: function (row, $element) {
	                curRow = row;
	                
	            },
	            onLoadSuccess: function (aa, bb, cc) {
	                
	            },
	            data:[],       
        responseHandler : function(res) {  
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        }, 
    });
}

function saveJs(){
	var datas = $("#jsList").bootstrapTable("getData");
	var saveDatas = [];
	for(var i=0;i<datas.length;i++){
		var paramName = $("#jsList>tbody").find("tr").eq(i).find("input").eq(0).val();
		var paramFromValue = $("#jsList>tbody").find("tr").eq(i).find("input").eq(1).val();
		var paramFromType = $("#jsList>tbody").find("tr").eq(i).find("select").eq(0).val();
		if(paramName!=datas[i].paramName||paramFromValue!=datas[i].paramFromValue||paramFromType!=datas[i].paramFromType){		
		//实体可修改部分是否修改(有一处不同,就需修改)
			datas[i].paramName=paramName;
			datas[i].paramFromValue=paramFromValue;
			datas[i].paramFromType=paramFromType;
			datas[i].paramTypeValue='${param.formDefId}';
			datas[i].paramType = 'FORMCONFIG|JS_VAR';
			saveDatas.push(datas[i]);
		}
	}
	var url = "${pageContext.request.contextPath }/form/updateJsParam.action";//更新实体信息
	$.ajax({
		url:url,
		type:'post',
		cache: false,
		contentType: 'application/json;charset=utf-8',
		data:JSON.stringify(saveDatas),
		async: false,
		success: function (data) {				
			}
		});
	$('#jsList').bootstrapTable('refresh');
}

function remove(obj){
	var jsParam = {};
	jsParam.paramName = $(obj).parent().parent().parent().find("input").eq(0).val();
	$.ajax({
		url:'${pageContext.request.contextPath }/form/deleteJsParam.action',
		type:'post',
		cache: false,
		data:JSON.stringify(jsParam),
		contentType: 'application/json;charset=utf-8',
		async: false,
		success: function (data) {
			
			}
		});
	$('#jsList').bootstrapTable('refresh');
}

function loadTree(){
	$.ajax({  
        type: 'POST',  
        url: '${pageContext.request.contextPath }/EntityList/selectFullTree.action',  
        success: function (data) {  
        	$.fn.zTree.init($("#entityTreeDemo"), setting, data); 
        	document.getElementById("key").value = ""; //清空搜索框中的内容  
    	    //绑定事件  
    	    key = $("#key");  
    	    key.bind("focus", focusKey)  
    	        .bind("blur", blurKey)  
    	        .bind("propertychange", searchNode) //property(属性)change(改变)的时候，触发事件  
    	        .bind("input", searchNode); 
        }
    });
	initSelect();
	$.ajax({  
        type: 'POST',  
        url: '${pageContext.request.contextPath }/form/selectParamTree.action?paramTypeValue=${param.formDefId}',  
        success: function (data) {  
        	$.fn.zTree.init($("#paramTreeDemo"), setting, data); 
        	document.getElementById("key2").value = ""; //清空搜索框中的内容  
    	    //绑定事件  
    	    key2 = $("#key2");  
    	    key2.bind("focus", focusKey2)  
    	        .bind("blur", blurKey2)  
    	        .bind("propertychange", searchNode2) //property(属性)change(改变)的时候，触发事件  
    	        .bind("input", searchNode2); 
        }
    });
}

var map = new Map(); 

function changeSql(){
	var entitySql = document.getElementById('formSql').value;
	var content = "";
	$.ajax({
		url:'${pageContext.request.contextPath }/form/getRealSql.action?entitySql='+entitySql,
		type:'post',
		cache: false,
		async: false,
		success: function (data) {
			content = data;
		}
    });
	content = "<textarea id='xxx' rows='7' cols='63' >"+content+"</textarea>";
	layer.open({
		title:'输入内容',
		shadeClose: true,
		zIndex:9999,
		content: content,
		area: ['500px', '300px']
	});
}

function initColumns(){
	$("#columnList").bootstrapTable({  
        url : '${pageContext.request.contextPath }/form/findColumnList.action?formDefId=${param.formDefId}', 
		height : '578',
        queryParams :'',
        undefinedText : '-',  
        striped : true, // 是否显示行间隔色  
        cache : false, // 是否使用缓存  
		async: false,
        clickToSelect: true,
        pagination : false,        
        uniqueId : "formColumnId", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ 
		{  
		    field : 'state',  
		    checkbox : true,  
		    align : 'center',  
		    valign : 'middle'  
		},
		{   
		    field : 'formColumnId', 
		    visible:false
		},
        {   
            field : 'formColumnFormDefId', 
            visible:false
        },
        {   
            field : 'formColumnEntityId', 
            visible:false
        },
        {   
            field : 'formColumnColumnId', 
            visible:false
        },        
        {  
            title : '实体名称',  
            field : 'formEntityTablename', 
            align: 'center',
            valign: 'middle',
	        formatter: function (value, row, index) {
	            if(value==undefined){
	                value='-';
	            }
	            	  return "<a  class=\"eli wpx\" style=\"font-size:12px;text-decoration:none;color:black;\" name=\"formEntityTablename\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"实体名称\">"+value+"</a>";
	        }
        }, {  
            title : '字段名称',  
            field : 'formFieldTablename',  
            align : 'center',  
            valign : 'middle', 
            formatter: function (value, row, index) {
	            if(value==undefined){
	                value='-';
	            }
	            	  return "<a  class=\"eli wpx\" style=\"font-size:12px;text-decoration:none;color:black;\" name=\"formFieldTablename\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"字段名称\">"+value+"</a>";
	        }
        }, {  
            title : '字段显示名称',  
            field : 'formColumnLable',  
            align : 'center',  
            valign : 'middle',  
            formatter: function (value, row, index) {
	            if(value==undefined){
	                value='-';
	            }
	            	  return "<a  class=\"eli wpx\" style=\"font-size:12px;text-decoration:none;color:black;\" name=\"formColumnLable\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"字段显示名称\">"+value+"</a>";
	        }
        }, {  
            title : '控件类型',  
            field : 'formColumnGuiType',  
            align : 'center',  
            valign : 'middle',
		    visible:false,
	        formatter: function (value, row, index) {
	            if((value==undefined || value=="")&&value!=0){
	                value='-';
	            }
	            value = (value+'').replace(/ /g,'');
	            return "<a class=\"eli wpx\" style=\"font-size:12px;text-decoration:none;color:black;width:100px;\" name=\"formColumnGuiType\" data-type=\"select\" data-pk=\""+row.Id+"\" data-value=\""+value+"\" data-title=\"控件类型\" data-source=\"/myehr/dict/searchSysDictEntryTypeCode1.action?deleteMark=SYS_FORM_GUI_TYPE&realValue="+value+"+\"></a>";
	        }
        },{  
            title : '显示宽度',  
            field : 'formColumnWidth', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '显示高度',  
            field : 'formColumnHeight', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '显示方式',  
            field : 'formColumnShowType', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '显示顺序',  
            field : 'formColumnSort', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '对齐方式',  
            field : 'formColumnAlign', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '是否必填',  
            field : 'formColumnRequired', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '跨字段数',  
            field : 'formColumnColSpan', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '最大字符数',  
            field : 'formColumnMaxLength', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '最小字符数',  
            field : 'formColumnMinLength', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '字段值范围',  
            field : 'formColumnColorCondition', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '字段颜色',  
            field : 'formColumnColor', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '所属分组',  
            field : 'formColumnGroupId', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '列类型',  
            field : 'formColumnType', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '跨行数',  
            field : 'formColumnRowSpan', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '是否合计',  
            field : 'formColumnTotal', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '是否开发字段',  
            field : 'formColumnIsDev', 
            align: 'center',
            valign: 'middle',  
		    visible:false,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" title=\"参数名称\" type=\"text\" name=\"buttonParamName\" style=\"padding:0 24px 0 12px;\" value=\""+value+"\"/>" ;
        	}
        }
        
        ],
        onClickRow: function (row, $element) {
		        	curRow = row;
		        	
		            $('#columnList').bootstrapTable('uncheckAll');//checkBox单选
		            $("#myTab").find("li").eq(0).attr("class","active");
		        	$("#myTab").find("li").eq(1).attr("class","");
		        	$("#myTab").find("li").eq(2).attr("class","");
		        	$("#Tab_1").attr("class","tab-pane active");
		        	$("#Tab_2").attr("class","tab-pane");
		        	$("#Tab_3").attr("class","tab-pane");
		            showExpandInfo(row);//获取并显示字段详细信息(信息在row中)
		            showWidgetInfo(row);//获取字段控件详细信息(根据row中字showWidgetInfo段Id去后台查)
	                
	            },      
        responseHandler : function(res) {  
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        },
        onLoadSuccess : function(){
        	$("#columnList tbody>tr").each(function(){
        		if($(this).attr("data-uniqueid")==chooseMark){
        			//$(this).find('input:checkbox').attr("checked","true");
        			$(this).find('td').eq(1).click();
        		}
        	})
        }
    });
}
function mapping(){
	layer.msg('确定要映射？将覆盖原字段', {
		  time: 0 //不自动关闭
		  ,btn: ['确定', '再想想']
		  ,success: function(layero){
				layero.find('.layui-layer-btn').css('text-align', 'center');
			}
		  ,yes: function(index){
		    layer.close(index);
		    var entitySql = document.getElementById('formSql').value;
			if(formDefId==null||formDefId=='null'){
				layer.alert('表单定义ID参数错误', {
					  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
					  skin: 'layer-ext-moon' //
					})
				return;
			}
			if(entitySql==null||entitySql==''){
				layer.alert('请编写实体sql', {
					  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
					  skin: 'layer-ext-moon' //
					})
				return ;
			}
			$.ajax({
		        url: "${pageContext.request.contextPath }/form/findColumnListSteps_2.action?formDefId=${param.formDefId}&entitySql="+entitySql,
		        type: 'POST',
		        cache: false,
		        async: false,
		        success: function (text) {
		        	var data = {};
		        	data.rows = text[1];
		        	data.total = 47;
		            if(1==1) {
		            	realSql = text[1];
		            	formSql = text[0];
		            	$("#columnList").bootstrapTable('refresh');
		            } else {
		            	 layer.alert('字段映射失败，请检查自定义SQl是否正确', {
							  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
							  skin: 'layer-ext-moon' 
							})
		            }
		        }
		    });

		  }
		});
}



function showWidgetInfo(dataForm){
	var type = dataForm.formColumnGuiType;
	var columnId = dataForm.formColumnId;
	if(type==undefined||type==null||type==""){
		changeWidgetShow(1);
	}else{
		changeWidgetShow(type);
	}
	$("#textboxColumnLable").val(dataForm.formColumnLable);
	if(type=='1'){
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getTextInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showTextInfoStep2(data);
				}
			});
	}else if(type=='2'){
		$("#comboboxFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getComboboxInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showComboboxInfoStep2(data);
				}
			});
	}else if(type=='6'){
		$("#dateFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getDateInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showDateInfoStep2(data);
				}
			});
	}else if(type=='8'){
		$("#textareaFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getTextareaInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showTextareaInfoStep2(data);
				}
			});
	}else if(type=='3'){
		$("#radiolistFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getRadiolistInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showRadiolistInfoStep2(data);
				}
			});
	}else if(type=='4'){
		$("#checkboxlistFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getCheckboxlistInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showCheckboxlistInfoStep2(data);
				}
			});
	}else if(type=='5'){
		$("#checkboxColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getCheckboxInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showCheckboxInfoStep2(data);
				}
			});
	}else if(type=='7'){
		$("#lookupFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getLookupInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showLookupInfoStep2(data);
				}
			});
	}
	else if(type=='9'){
		$("#fileuploadFormColumnLable").val(dataForm.formColumnLable);
		$.ajax({
			url:'${pageContext.request.contextPath }/form/getFileuploadInfoByColumnId.action?columnId='+columnId,
			type:'post',
			cache: false,
			async: false,
			success: function (data) {
					showFileuploadInfoStep2(data);
				}
			});
	}
}

//保存控件信息
function saveWidget(){
	var formColumnId = $("#columnList").bootstrapTable("getSelections")[0].formColumnId;
	var widgetType = $("#formColumnGuiType").val();
	var url="";
	if(widgetType=='1'){
		var sysFormTextbox = {};
		url = "${pageContext.request.contextPath }/form/saveTextBox.action";
		sysFormTextbox.textboxFormColumnId=formColumnId;
		sysFormTextbox.textboxId=$("#textboxId").val();
		sysFormTextbox.textboxCheckType=$("#textboxCheckType").val();
		sysFormTextbox.textboxCheckSelf=$("#textboxCheckSelf").val();
		sysFormTextbox.textboxDataFromType=$("#textboxDataFromType").val();
		sysFormTextbox.textboxDataFromValue=$("#textboxDataFromValue").val();
		sysFormTextbox.textboxCheckFun=$("#textboxCheckFun").val();
		sysFormTextbox.textboxInitFun=$("#textboxInitFun").val();
		sysFormTextbox.textboxValuechangeFun=$("#textboxValuechangeFun").val();
		sysFormTextbox.textboxClickFun=$("#textboxClickFun").val();
		sysFormTextbox.textboxEmptytext=$("#textboxEmptytext").val();
		sysFormTextbox.textboxIsLink=$("#textboxIsLink").prop('checked');
		sysFormTextbox.textboxLinkIsForm=$("#textboxLinkIsForm").prop('checked');
		if(sysFormTextbox.textboxLinkIsForm){
			sysFormTextbox.textboxLinkForm=$("#textlinkDetailFormId").val();
		}else{
			sysFormTextbox.textboxLinkUrl=$("#textboxLinkUrl1").val();
		}
		sysFormTextbox.textboxLinkWinOpenType=$("input[name='textboxLinkWinOpenType']:checked").val();
		if($("input[name='textboxLinkWinOpenType']:checked").val()=='1'){
			sysFormTextbox.textboxLinkSuccessDeal=$("#textboxLinkSuccessDeal").val();
			sysFormTextbox.textboxLinkWinTitle=$("#textboxLinkWinTitle").val();
			sysFormTextbox.textboxLinkWinWidth=$("#textboxLinkWinWidth").val();
			sysFormTextbox.textboxLinkWinHeight=$("#textboxLinkWinHeight").val();
		}
		saveInfo(url,sysFormTextbox);
	}else if(widgetType=='2'){
		var sysFormCombobox = {};
		url = "${pageContext.request.contextPath }/form/saveCombobox.action";
		sysFormCombobox.comboboxFormColumnId=formColumnId;
		sysFormCombobox.comboboxId=$("#comboboxId").val();
		sysFormCombobox.comboboxCheckFun=$("#comboboxcheckFun").val();
		sysFormCombobox.comboboxCheckType=$("#comboboxCheckType").val();
		sysFormCombobox.comboboxCheckSelf=$("#comboboxCheckSelf").val();
		sysFormCombobox.comboboxInitFun=$("#comboboxInitFun").val();
		sysFormCombobox.comboboxValuechangeFun=$("#comboboxValuechangeFun").val();
		sysFormCombobox.comboboxClickFun=$("#comboboxClickFun").val();
		sysFormCombobox.comboboxDataFromType=$("#comboboxDataFromType").val();
		sysFormCombobox.comboboxDataFromValue=$("#comboboxDataFromValue").val();
		sysFormCombobox.comboboxGuiInitType=$("#comboboxGuiInitType").val();
		sysFormCombobox.comboboxGuiInitValue=$("#comboboxGuiInitValue2").val();
		sysFormCombobox.comboboxTextfield=$("#comboboxTextfield").val();
		sysFormCombobox.comboboxValuefield=$("#comboboxValuefield").val();
		sysFormCombobox.comboboxDatafield=$("#comboboxDatafield").val();
		sysFormCombobox.comboboxEmptytext=$("#comboboxEmptytext").val();
		sysFormCombobox.comboboxAllowinput=$("#comboboxAllowinput").prop('checked');
		sysFormCombobox.comboboxMultiselect=$("#comboboxMultiselect").prop('checked');
		sysFormCombobox.comboboxNullitemtext=$("#comboboxNullitemtext").val();
		sysFormCombobox.comboboxGuiInitExcsqlId=$("#comboboxGuiInitExcsqlId").val();
		sysFormCombobox.comboboxIstext=$("#comboboxIstext").val();	
		sysFormCombobox.comboboxDictSql=$("#comboboxDictSql").val();	
		sysFormCombobox.comboboxIsSearch=$("#comboboxIsSearch").prop('checked');			
		saveInfo(url,sysFormCombobox);
	}else if(widgetType=='6'){
		var sysFormDatePicker = {};
		url = "${pageContext.request.contextPath }/form/saveDatePicker.action";
		sysFormDatePicker.datepickerFormColumnId=formColumnId;
		sysFormDatePicker.datepickerId=$("#datepickerId").val();
		sysFormDatePicker.datepickerCheckType=$("#datepickerCheckType").val();
		sysFormDatePicker.datepickerCheckSelf=$("#datepickerCheckSelf").val();
		sysFormDatePicker.datepickerInitFun=$("#datepickerInitFun").val();
		sysFormDatePicker.datepickerValuechangeFun=$("#datepickerValuechangeFun").val();
		sysFormDatePicker.datepickerClickFun=$("#datepickerClickFun").val();		
		sysFormDatePicker.datepickerDataFromType=$("#datepickerDataFromType").val();
		sysFormDatePicker.datepickerDataFromValue=$("#datepickerDataFromValue").val();		
		sysFormDatePicker.datepickerFormat=$("#datepickerFormat").val();		
		sysFormDatePicker.datepickerShowtime=$("#datepickerShowtime").prop('checked');
		sysFormDatePicker.datepickerShowokbutton=$("#datepickerShowokbutton").prop('checked');
		sysFormDatePicker.datepickerShowclearbutton=$("#datepickerShowclearbutton").prop('checked');
		sysFormDatePicker.datepickerAllowinput=$("#datepickerAllowinput").prop('checked');
		sysFormDatePicker.datepickerShowtodaybutton=$("#datepickerShowtodaybutton").prop('checked');
		sysFormDatePicker.datepickerDrawdateFun=$("#datepickerDrawdatefun").val();		
		saveInfo(url,sysFormDatePicker);
	}else if(widgetType=='8'){
			var sysFormTextarea = {};
			url = "${pageContext.request.contextPath }/form/saveTextarea.action";
			sysFormTextarea.textareaFormColumnId=formColumnId;
			sysFormTextarea.textareaId=$("#textareaId").val();
			sysFormTextarea.textareaCheckType=$("#textareaCheckType").val();
			if($("#textareaCheckType").val()=='fun'){
				sysFormTextarea.textareaCheckFun=$("#textareaCheckFun").val();
			}
			sysFormTextarea.textareaCheckSelf=$("#textareaCheckSelf").val();
			sysFormTextarea.textareaDataFromType=$("#textareaDataFromType").val();
			if($("#textareaDataFromType").val()=='initFun'){
				sysFormTextarea.textareaInitFun=$("#textareaInitFun").val();
			}
			sysFormTextarea.textareaDataFromValue=$("#textareaDataFromValue").val();
			sysFormTextarea.textareaValuechangeFun=$("#textareaValuechangeFun").val();
			sysFormTextarea.textareaClickFun=$("#textareaClickFun").val();
			sysFormTextarea.textareaEmptytext=$("#textareaEmptytext").val();
			saveInfo(url,sysFormTextarea);
		}else if(widgetType=='4'){
			var sysFormCheckboxlist = {};
			url = "${pageContext.request.contextPath }/form/saveCheckboxlist.action";
			sysFormCheckboxlist.checkboxlistFormColumnId=formColumnId;		
			sysFormCheckboxlist.checkboxlistId=$("#checkboxlistId").val();
			sysFormCheckboxlist.checkboxlistCheckFun=$("#checkboxlistcheckFun").val();
			sysFormCheckboxlist.checkboxlistCheckType=$("#checkboxlistCheckType").val();
			sysFormCheckboxlist.checkboxlistCheckSelf=$("#checkboxlistCheckSelf").val();
			sysFormCheckboxlist.checkboxlistInitFun=$("#checkboxlistInitFun").val();
			sysFormCheckboxlist.checkboxlistValuechangeFun=$("#checkboxlistValuechangeFun").val();
			sysFormCheckboxlist.checkboxlistClickFun=$("#checkboxlistClickFun").val();
			sysFormCheckboxlist.checkboxlistDataFromType=$("#checkboxlistDataFromType").val();
			sysFormCheckboxlist.checkboxlistDataFromValue=$("#checkboxlistDataFromValue").val();
			sysFormCheckboxlist.checkboxlistGuiInitType=$("#checkboxlistGuiInitType").val();
			sysFormCheckboxlist.checkboxlistGuiInitValue=$("#checkboxlistGuiInitValue2").val();
			sysFormCheckboxlist.checkboxlistTextfield=$("#checkboxlistTextfield").val();
			sysFormCheckboxlist.checkboxlistValuefield=$("#checkboxlistValuefield").val();
			sysFormCheckboxlist.checkboxlistDatafield=$("#checkboxlistDatafield").val();
			sysFormCheckboxlist.checkboxlistRepeatitems=$("#checkboxlistRepeatitems").val();
			sysFormCheckboxlist.checkboxlistRepeatlayout=$("#checkboxlistRepeatlayout").val();
			saveInfo(url,sysFormCheckboxlist);
		}else if(widgetType=='3'){
			var sysFormRadiolist = {};
			url = "${pageContext.request.contextPath }/form/saveRadiolist.action";
			sysFormRadiolist.radiolistFormColumnId=formColumnId;		
			sysFormRadiolist.radiolistId=$("#radiolistId").val();
			sysFormRadiolist.radiolistCheckFun=$("#radiolistcheckFun").val();
			sysFormRadiolist.radiolistCheckType=$("#radiolistCheckType").val();
			sysFormRadiolist.radiolistCheckSelf=$("#radiolistCheckSelf").val();
			sysFormRadiolist.radiolistInitFun=$("#radiolistInitFun").val();
			sysFormRadiolist.radiolistValuechangeFun=$("#radiolistValuechangeFun").val();
			sysFormRadiolist.radiolistClickFun=$("#radiolistClickFun").val();
			sysFormRadiolist.radiolistDataFromType=$("#radiolistDataFromType").val();
			sysFormRadiolist.radiolistDataFromValue=$("#radiolistDataFromValue").val();
			sysFormRadiolist.radiolistGuiInitType=$("#radiolistGuiInitType").val();
			sysFormRadiolist.radiolistGuiInitValue=$("#radiolistGuiInitValue2").val();
			sysFormRadiolist.radiolistTextfield=$("#radiolistTextfield").val();
			sysFormRadiolist.radiolistValuefield=$("#radiolistValuefield").val();
			sysFormRadiolist.radiolistDatafield=$("#radiolistDatafield").val();
			sysFormRadiolist.radiolistRepeatdirection=$("#radiolistRepeatdirection").val();
			sysFormRadiolist.radiolistRepeatitems=$("#radiolistRepeatitems").val();
			sysFormRadiolist.radiolistRepeatlayout=$("#radiolistRepeatlayout").val();
			saveInfo(url,sysFormRadiolist);
		}else if(widgetType=='5'){
			var sysFormCheckbox = {};
			url = "${pageContext.request.contextPath }/form/saveCheckbox.action";
			sysFormCheckbox.checkboxFormColumnId=formColumnId;
			sysFormCheckbox.checkboxId=$("#checkboxId").val();
			sysFormCheckbox.checkboxDataFromType=$("#checkboxDataFromType").val();
			if($("#checkboxDataFromType").val()=='initFun'){
				sysFormCheckbox.checkboxInitFun=$("#checkboxInitFun").val();
			}
			sysFormCheckbox.checkboxDataFromValue=$("#checkboxDataFromValue").val();
			sysFormCheckbox.checkboxValuechangeFun=$("#checkboxValuechangeFun").val();
			sysFormCheckbox.checkboxClickFun=$("#checkboxClickFun").val();
			sysFormCheckbox.checkboxShowName=$("#checkboxShowName").val();
			saveInfo(url,sysFormCheckbox);
		}else if(widgetType=='7'){
			var sysFormLookup = {};
			url = "${pageContext.request.contextPath }/form/saveLookup.action";
			sysFormLookup.lookupId=$("#lookupId").val();
			sysFormLookup.lookupFormColumnId=formColumnId;
			sysFormLookup.lookupCheckType=$("#lookupCheckType").val();
			sysFormLookup.lookupCheckSelf=$("#lookupCheckSelf").val();
			sysFormLookup.lookupCheckFun=$("#lookupCheckFun").val();
			
			sysFormLookup.lookupValuechangeFun=$("#lookupValuechangeFun").val();
			sysFormLookup.lookupClickFun=$("#lookupClickFun").val();
			sysFormLookup.lookupDataFromType=$("#lookupDataFromType").val();
			sysFormLookup.lookupDataFromValue=$("#lookupDataFromValue").val();
			if($("#lookupDataFromType").val()=='initFun'){
				sysFormLookup.lookupInitFun=$("#lookupInitFun").val();
			}
			sysFormLookup.lookupFormWinType=$("#lookupFormWinType").val();
			if($("#lookupFormWinType").val()=='1'){
				sysFormLookup.lookupWinTitle=$("#lookupWinTitle").val();
				sysFormLookup.lookupWinWidth=$("#lookupWinWidth").val();
				sysFormLookup.lookupWinHeight=$("#lookupWinHeight").val();
				sysFormLookup.lookupShowProperty=$("#lookupShowProperty").val();
				sysFormLookup.lookupValueProperty=$("#lookupValueProperty").val();
				sysFormLookup.lookupFormId=$("#lookupDetailFormId").val();
				sysFormLookup.lookupCustomDataback=$("#lookupCustomDataback").val();
				if($("#lookupCustomDataback").val()=='Y'){
					sysFormLookup.lookupCustomDatabackFun=$("#lookupCustomDatabackFun").val();
				}
				
			}else if($("#lookupFormWinType").val()=='2'){
				sysFormLookup.lookupWinTitle=$("#lookupWinTitle").val();
				sysFormLookup.lookupWinWidth=$("#lookupWinWidth").val();
				sysFormLookup.lookupWinHeight=$("#lookupWinHeight").val();
				sysFormLookup.lookupShowProperty=$("#lookupShowProperty").val();
				sysFormLookup.lookupValueProperty=$("#lookupValueProperty").val();
				sysFormLookup.lookupFormUrl=$("#lookupFormUrl").val();
			}else if($("#lookupFormWinType").val()=='3'){
				sysFormLookup.lookupButtonclickFun=$("#lookupButtonclickFun").val();
			}
			sysFormLookup.lookupDatabackValue=$("#lookupDatabackValue").val();
			sysFormLookup.lookupDatabackType=$("#lookupDatabackType").val();

			//sysFormLookup.lookupEmptytext=$("#lookupEmptytext").val();
			//sysFormLookup.lookupMuliselect=$("#lookupMuliselect").val();

			saveInfo(url,sysFormLookup);
		}else if(widgetType=='9'){
			var sysFormFileupload = {};
			url = "${pageContext.request.contextPath }/form/saveFileupload.action";
			sysFormFileupload.fileuploadFormColumnId = formColumnId;
			sysFormFileupload.fileuploadId = $("#fileuploadId").val();
			var arr = $("#fileuploadUploadFiletypes").val();
			var val = "";
			if(arr==null){
			}else{
				for(var i=0;i<arr.length;i++){
					val+=arr[i]+',';
				}
				val = val.substring(0,val.length-1);
			}
			sysFormFileupload.fileuploadUploadFiletypes=val;
			sysFormFileupload.fileuploadMaxFileCount=$("#fileuploadMaxFileCount").val();
			sysFormFileupload.fileuploadAutoUpload=$("#fileuploadAutoUpload").prop('checked');
			saveInfo(url,sysFormFileupload);
		}
	
	}
	

function checkInverse(){
	//$("#step2_part2 .bs-checkboxfind").find("input").prop()
}
function saveInfo(url,datas){
	$.ajax({
			url:url,
			type:'post',
			cache: false,
			contentType: 'application/json;charset=utf-8',
			data:JSON.stringify(datas),
			async: false,
			success: function (data) {
				if(data=="true"){  
                	layer.alert('保存成功', {
						  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})

                }else{  
                	layer.alert('保存失败', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})

                }  
				}
			});
}

//保存字段信息
function saveColumnInfo(){
	var column = {};
	var data = $("#columnList").bootstrapTable("getSelections")[0];
	url = "${pageContext.request.contextPath }/form/saveColumn.action";
	if(data.formColumnGuiType == $("#formColumnGuiType").val()&&data.formColumnWidth == $("#formColumnWidth").val()&&data.formColumnHeight == $("#formColumnHeight").val()&&data.formColumnShowType == $("#formColumnShowType").val()&&data.formColumnSort == $("#formColumnSort").val()&&data.formColumnAlign == $("#formColumnAlign").val()&&data.formColumnRequired == $("#formColumnRequired").val()&&data.formColumnColSpan == $("#formColumnColSpan").val()&&data.formColumnMaxLength == $("#formColumnMaxLength").val()&&data.formColumnMinLength == $("#formColumnMinLength").val()&&data.formColumnColorCondition == $("#formColumnColorCondition").val()&&data.formColumnColor == $("#formColumnColor").val()&&data.formColumnType == $("#formColumnType").val()&&data.formColumnRowSpan == $("#formColumnRowSpan").val()&&data.formColumnTotal == $("#formColumnTotal").val()&&data.formColumnIsDev == $("#formColumnIsDev").val()){
		alert("未修改");
	}else{
		column.formColumnId =  data.formColumnId;
		column.formColumnFormDefId = data.formColumnFormDefId;
		column.formColumnEntityId = data.formColumnEntityId;
		column.formColumnColumnId = data.formColumnColumnId;
		column.formFieldTablename = data.formFieldTablename;
		column.formEntityTablename = data.formEntityTablename;
		column.formColumnLable = $("#formColumnLable").val();
		column.formColumnGuiType = $("#formColumnGuiType").val();
		column.formColumnWidth = $("#formColumnWidth").val();
		column.formColumnHeight = $("#formColumnHeight").val();
		column.formColumnShowType = $("#formColumnShowType").val();
		column.formColumnSort = $("#formColumnSort").val();
		column.formColumnAlign = $("#formColumnAlign").val();
		column.formColumnRequired = $("#formColumnRequired").val();
		column.formColumnColSpan = $("#formColumnColSpan").val();
		column.formColumnMaxLength = $("#formColumnMaxLength").val();
		column.formColumnMinLength = $("#formColumnMinLength").val();
		column.formColumnColorCondition = $("#formColumnColorCondition").val();
		column.formColumnColor = $("#formColumnColor").val();
		column.formColumnGroupId = $("#formGroupId").val();
		column.formColumnType = $("#formColumnType").val();
		column.formColumnRowSpan = $("#formColumnRowSpan").val();
		column.formColumnTotal = $("#formColumnTotal").val();
		column.formColumnIsDev = $("#formColumnIsDev").val();
		saveInfo(url,column);		
		$("#columnList").bootstrapTable("refresh");
	}
	chooseMark = data.formColumnId;
}
//根据表单ID获取表单名
function getFormNameById(formDefId){
	var formDefName;
	$.ajax({
		url:'${pageContext.request.contextPath}/form/getFormNameById.action?formDefId='+formDefId,
		type:'post',
		cache: false,
		async: false,
		contentType: 'application/json;charset=utf-8',
		success: function (data) {
			formDefName = data.formDefName;
			}
		});
	return formDefName;
}

function getDictnameByCode(e){
	var DictName;
	$.ajax({
		url:'${pageContext.request.contextPath}/form/getDictnameByCode.action?dictTypeCode='+e,
		type:'post',
		cache: false,
		async: false,
		contentType: 'application/json;charset=utf-8',
		success: function (data) {
				DictName = data.dictTypeName;
			}
		});
	return DictName;
}

function query_tableStep4() {   
    $("#tableListStep4").bootstrapTable({  
        url : '${pageContext.request.contextPath }/form/queryFilterColumn.action?formId='+formId, 
		height:'800',
        queryParams :'',
        undefinedText : '-',  
        striped : true, // 是否显示行间隔色  
        cache : false, // 是否使用缓存  
        clickToSelect: true,
        pagination : false,        
        uniqueId : "templateColumnId", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ 
		{  
            title : '操作',  
            field : 'queryColumnId', // 字段  
            align: 'center',
            valign: 'middle',
            width:50,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input type=\"form-control\" name=\"queryColumnId\" style=\"width:0px;padding:0px;border:none\" readonly=true data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"操作\" value=\""+value+"\"/>"+
					   "<input type=\"hidden\" data-title=\"关联字段ID\" value=\""+row.gridColumnId+"\"/>"+
            		   "<i class=\"glyphicon glyphicon-remove\" style=\"position: absolute;opacity: 0.5;right: 5px;top: 5px;cursor: pointer;\" onclick=\"removeStep4(this)\"></i><div>" ;
        	}
		},{
			
            title : '实体表名',  
            field : 'formEntityTablename', // 字段  
            align: 'center',
            valign: 'middle',
            width:150,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" name=\"formEntityTablename\" style=\"padding:0 24px 0 12px;\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"实体表名\" value=\""+value+"\"/>" ;
        	}
        },{  
            title : '字段名称',  
            field : 'formFieldTablename', // 字段  
            align: 'center',
            valign: 'middle',
            width:175,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" name=\"formFieldTablename\" style=\"padding:0 24px 0 12px;\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"字段名称\" value=\""+value+"\"/>" ;
        	}
		 },{  
            title : '字段显示名称',  
            field : 'gridColumnLable', // 字段  
            align: 'center',
            valign: 'middle',
            width:175,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<div style=\"position:relative\"><input class=\"form-control\" name=\"gridColumnLable\" style=\"padding:0 24px 0 12px;\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"字段显示名称\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '控件类型',  
            field : 'formColumnGuiType',  
            align : 'center',  
            valign : 'middle', 
			width:200,
			formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"formColumnGuiType"+index+"\" class=\"form-control\" name=\"formColumnGuiType\" DictName=\"SYS_FORM_GUI_TYPE\" data-title=\"控件类型\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '查询规则',  
            field : 'gridColumnFilterRule',  
            align : 'center',  
            valign : 'middle',  
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"gridColumnFilterRule"+index+"\" class=\"form-control\"  name=\"gridColumnFilterRule\" DictName=\"SYS_FORM_WHERE_RULE\" dataField=\"\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"查询规则\" value=\""+value+"\"></selcct>" ;
        	}
        }, {  
            title : '显示宽度',  
            field : 'gridColumnWidth',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"gridColumnWidth\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"显示宽度\" value=\""+value+"\"/>";
        	}
		 }, {  
            title : '跨字段数',  
            field : 'gridColumnColSpan',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<input class=\"form-control\"  name=\"gridColumnColSpan\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"跨字段数\" value=\""+value+"\"/>";
        	}
        }, {  
            title : '是否区间查询',  
            field : 'gridColumnIsIntervais',  
            align : 'center',  
            valign : 'middle', 
            width:200,
            formatter: function (value, row, index) {
            	if(value==undefined){
	        		value = "";
	        	}
            	return "<select id=\"gridColumnIsIntervais"+index+"\" class=\"form-control\"  name=\"gridColumnIsIntervais\" DictName=\"SYS_COMMON_YES_NO\" dataField=\"\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"是否区间查询\" value=\""+value+"\"/>" ;
        	}
        }, {  
            title : '显示顺序',  
            field : 'formColumnSort',  
            align : 'center',  
            valign : 'middle',
            width:150,
            formatter: function (value, row, index) {
            	if(value==undefined){
		        		value = "";
		        	}
            	return "<input class=\"form-control\"  name=\"formColumnSort\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"显示顺序\" value=\""+value+"\"/>" ;
        	}
        }],
        onClickRow: function (row, $element) {
	                curRow = row;
	                
	            },
	            onLoadSuccess: function (aa, bb, cc) {
	                addListSelectStep4();
	            },
	            data:[],       
        responseHandler : function(res) { 
        	rowsNum = res.total;
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        }, 
    });
}

function model(){
	layer.open({
		content: '<div style="width:300px"></div>'
		,btn: ['联系人式', '报表式', '预警式','无限扩展式','联系人无线扩展字段']
		,yes: function(index, layero){
			editModel(1);
		},btn2: function(index, layero){
			editModel(2);
		},btn3: function(index, layero){
			editModel(3);
		},btn4: function(index, layero){
			editModel(4);
		},btn5: function(index, layero){
			editModel(5);
		}
		,cancel: function(){
		
		}
	});
}

function editModel(modelType){
	var url = "";
	if(modelType==1){
		url = '/myehr/jsp/form/formcreate/formtemplatebuild/gridtocardtemplate/model1.jsp?formDefId='+formId+'&templateId='+modelType;
	}else if(modelType==2){
		url = '/myehr/jsp/form/formcreate/formtemplatebuild/gridtocardtemplate/model2.jsp?formDefId='+formId+'&templateId='+modelType;
	}else if(modelType==3){
		url = '/myehr/jsp/form/formcreate/formtemplatebuild/gridtocardtemplate/model3.jsp?formDefId='+formId+'&templateId='+modelType;
	}else if(modelType==4){
		url = '/myehr/jsp/form/formcreate/formtemplatebuild/gridtocardtemplate/model4.jsp?formDefId='+formId+'&templateId='+modelType;
	}else if(modelType==5){
		url = '/myehr/jsp/form/formcreate/formtemplatebuild/gridtocardtemplate/model5.jsp?formDefId='+formId+'&templateId='+modelType;
	}
	layer.open({
		type:2,
		closeBtn:1,
		shadeClose:true,
		area:['950','700'],
		maxmin:true,
		title:'模板编辑',
		content:url,
		success:function(layero,index){			
		},
		end:function(){		
			//$('#columnList').bootstrapTable('refresh');
			//initSelect();
		}
	 }); 
}

</script>
</html>