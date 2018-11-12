<%@ page language="java" import="java.util.*" import="com.myehr.common.util.LangUtil" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<body>
<div class="container-fluid" style="height:100%;overflow-y:hidden">
	<div style="margin-top:10px">
		<div class="BTNGROUP" style="margin:0 auto; display:inline-block;height: 35px;">
		    <div style="margin-right:10px;display:inline-block">
			    <input type="button" id="addForm" class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"增加")%> onclick="addForm()"/>
			</div>
			
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="remove" class="btn btn-danger" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"删除")%> onclick="deleteHr()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="update" class="btn btn-info" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"修改")%> onclick="update()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="refer" class="btn btn-success" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"提交")%> onclick="refer()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="move" class="btn btn-warning" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"移动")%> onclick="move()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="removeRefer" class="btn btn-primary" style="background-color:#990099;border:none" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"取消提交")%> onclick="removeRefer()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="look" class="btn btn-primary" style="background-color:pink;border:none" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"预览")%> onclick="look()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="copy" class="btn btn-primary" style="background-color:purple;border:none" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"复制")%> onclick="copy()"/>
			</div>
			<!--<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="copy" class="btn btn-primary" style="background-color:purple;border:none" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"生成所有表单")%> onclick="buildAllForm()"/>
			</div>-->
			
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="flushMap" class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"刷新缓存")%> onclick="flushMap()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="add" class="btn btn-primary" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"新增加表单")%> onclick="add()"/>
			</div>
			<div style="margin-right:10px;display:inline-block">
			    <input type="button" id="createVue" class="btn btn-primary" style="background-color:pink;border:none" value=<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"生成移动端页面")%> onclick="createVue()"/>
			</div>
			
		</div>
		<div id="formList_filter" style="float:right;display:inline-block;position:relative;">
			<input id="FILTER_2771" name="FILTER_2771" placeholder="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单编码")%>,<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单名称")%>" style="padding: 6px 30px 6px 10px;" title="<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单编码")%>,<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单名称")%>" type="text" class="form-control" oninput="grid_emp_proce_fun11()" />
			<i class="icon-search" style="position: absolute;right: 12px;top: 7px;cursor: pointer;"></i>
		</div>
	</div>
	<table id="formList">
	</table>
</div>
    
<script>
	$(function() {
    	querys();
		$(".fixed-table-toolbar").css("margin-top","0px");
		$(".container-fluid").css("max-height","100%");
		$(".fixed-table-container").css("height",$('body').height() -45 +"px");
		$(".fixed-table-body").css("height","85%");
	    initDict();
	    initDate();
	    console.log($(".BTNGROUP>div").length);
	    var DIVAMOUNT = 11;
	    if($(".BTNGROUP>div").length > DIVAMOUNT){
	    	var len = $(".BTNGROUP>div").length;
	    	$(".BTNGROUP>div:gt(11)").hide();
	    	var selectObj = $('<div class="btn-group"><button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">更多 <span class="caret"></span></button><ul class="dropdown-menu" style="min-width:90px;left:-14px;"></ul></div>');
	    	$(".BTNGROUP").append(selectObj);
	    	for(var i=DIVAMOUNT;i<len;i++){
	    		var liObj = $('<li style="margin-left:14px;"></li>');
	    		liObj.append($(".BTNGROUP>div").eq(i).clone(true));
	    		selectObj.find('.dropdown-menu').append(liObj);
	    		selectObj.find('.dropdown-menu').append($('<li role="separator" class="divider"></li>'));
	    	}
	    	selectObj.find('.dropdown-menu div').show();
	    }
	});
  
function querys() {   
    $("#formList").bootstrapTable({  
        url : '${pageContext.request.contextPath }/form/findFormList.action',  
        height : '700',  
        undefinedText : '-',  
        pagination : true, // 分页  
        striped : true, // 是否显示行间隔色  
        queryParams : queryParams,  
        cache : false, // 是否使用缓存  
        pageList : [ 5, 10, 3000 ],  
        toolbar : "#toolbar",// 指定工具栏  
        showColumns : true, // 显示隐藏列  
        clickToSelect: true,
        showRefresh : true, // 显示刷新按钮  
        uniqueId : "userName", // 每一行的唯一标识  
        sidePagination : "server", // 服务端处理分页  
        columns : [ {  
            field : 'state',  
            checkbox : true,  
            align : 'center',  
            valign : 'middle'  
        }, {  
            title : '表单编码',  
            field : 'formDefCode', // 字段  
            align : 'center', // 对齐方式（左 中 右）  
            valign : 'middle', //  
            sortable : true ,
            formatter: function (value, row, index) {
                    	return "<a  class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formDefCode\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"表单编码\">"+value+"</a>";
                	} 
        }, {  
            title : '表单名称',  
            field : 'formDefName',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a  class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formDefName\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"表单名称\">"+value+"</a>";
                	}
        }, {  
            title : '表单布局类型',  
            field : 'formDefLayoutType',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formDefLayoutType\" data-type=\"select\" data-pk=\""+row.Id+"\" data-value=\""+value+"\" data-title=\"表单布局类型\" data-source=\"/myehr/dict/searchSysDictEntryTypeCode1.action?deleteMark=SYS_FORM_LAYOUT\">";
                	}
        }, {  
            title : '流程表单',  
            field : 'formDefIsProcess',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a class=\"eli w50px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formDefIsProcess\" data-type=\"select\" data-pk=\""+row.Id+"\" data-value=\""+value+"\" data-title=\"流程表单\" data-source=\"/myehr/dict/searchSysDictEntryTypeCode1.action?deleteMark=SYS_COMMON_YES_NO\">";
                	}
        }, {  
            title : '提交状态',  
            field : 'formSubmitStatus',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a class=\"eli w50px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formSubmitStatus\" data-type=\"select\" data-pk=\""+row.Id+"\" data-value=\""+value+"\" data-title=\"提交状态\" data-source=\"/myehr/dict/searchSysDictEntryTypeCode1.action?deleteMark=SYS_FORM_SUBMIT_STATUS\">";
                	}
            }, {  
            title : '提交路径',  
            field : 'formSubmitUrl',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a  class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formSubmitUrl\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"提交路径\">"+value+"</a>";
                	}
        }, {  
            title : '提交日期',  
            field : 'formSubmitTime',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
		            	var date = "-";
		                if(value!=null){
		                    date = value;
		                }
                    	return "<a class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:100px\" name=\"formSubmitTime\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"提交日期\">"+date+"</a>";
                	}
        },  {  
            title : '表单描述',  
            field : 'formDefDesc',  
            align : 'center',  
            valign : 'middle',  
            sortable : true  ,
            formatter: function (value, row, index) {
                    	return "<a  class=\"eli w100px\" style=\"font-size:12px;text-decoration:none;color:black;width:200px;\" name=\"formDefDesc\" data-type=\"text\" data-pk=\""+row.Id+"\" data-title=\"表单描述\">"+value+"</a>";
                	}
        }],
        onClickRow: function (row, $element) {
	                curRow = row;
	                $('#formList').bootstrapTable('uncheckAll');//checkBox单选
	            },
	            onLoadSuccess: function (aa, bb, cc) {
	                $("#formList a").editable({
	                    disabled: true,
	                    emptytext: "-",
	                    url: function (params) {
	                        var sName = $(this).attr("name");
	                        curRow[sName] = params.value;
	                    },
	                    type: 'text'
	                });
	            },
        responseHandler : function(res) {  
            return {  
                total : res.total,  
                rows : res.rows  
            };  
        }, 
    });
    
    window.operateEvents = {  
                
           };
	}    
	
	//查询
	function search(){
	var condition=new Object();
	condition.fieldCode = $("#search").val();
	condition.deleteMark = $("#deleteMark").val();
	condition.fieldEntityId = $("#EntityId").val();
		$.ajax({  
                  type: 'POST',  
                  data: JSON.stringify(condition),  
                  url: '${pageContext.request.contextPath }/formList/searchformList.action',  
                  success: function (data) {  
                      $("#formList").bootstrapTable('load',data); 
                  }  
              });
	}

/** 替换数据为文字 */  
function genderFormatter(value) {  
    if (value == null || value == undefined) {  
        return "-";  
    } else if (value==1) {  
        return "已删除";  
    } else if(value==0){  
        return "正常";  
    }  
}  
/** 刷新页面 */  
function refresh() {  
    $('#formList').bootstrapTable('refresh');  
}  
/**查询条件与分页数据 */  
function queryParams(pageReqeust) {  
	pageReqeust.enabled = $("#enabled").val();  
	pageReqeust.querys = $("#querys").val();  
    pageReqeust.condition = $("#FILTER_2771").val();  
    pageReqeust.pageNo = this.offset;  
    pageReqeust.pageSize = this.pageNumber;
    pageReqeust.formDefFolderId = '${param.FOLDER_TREE_ID}';
    pageReqeust.sort = 'form_def_id'
    return pageReqeust;  
}   
/**下拉控件 */
var classes = $("select");
var parame={
    id:'',
    value:"N",
    width: "100px",
    height:"45px",
    url:'${pageContext.request.contextPath }/dict/searchSysDictEntryTypeCode.action?userId=${sessionScope.userid}',
	  jsonParam:{},
    chang:function (e){
    }
};
function initDict(){
    for(var i=0;i<classes.length;i++){
        parame.id=classes[i].id;
		  var type=$(classes[i]).attr('multiSelect');
        parame.placeholder="";
        parame.jsonParam.dictTypeCode=$(classes[i]).attr('DictName');
		  if(type=="true"){
		  	myehr_initSelectMore(parame);
		  }else{
	        myehr_initSelect(parame);
		  }
    }
}
/**初始化日期控件  */
function initDate(){
			$('.form_date1').datetimepicker({ 
			 language:  'zh-CN',  
			 format:"yyyy-mm-dd",  
			 weekStart: 1,  
			 todayBtn:  1,  
			 autoclose: 1,  
			 todayHighlight: 1,  
			 startView: 2,  
			 minView: 2,  
			 forceParse: 0,  
			 pickerPosition: "bottom-left"  
			});  
			$('.form_date2').datetimepicker({ 
			 language:  'zh-CN',  
			 format:"yyyy-mm-dd hh:ii",  
			 weekStart: 1,  
			 todayBtn:  1,  
			 autoclose: 1,  
			 todayHighlight: 1,  
			 startView: 2,  
			 minView: 0,  
			 forceParse: 0,  
			 pickerPosition: "bottom-left"  
			});  
			$('.form_date3').datetimepicker({ 
			 language:  'zh-CN',  
			 format:"hh:ii",  
			 weekStart: 1,  
			 todayBtn:  1,  
			 autoclose: 1,  
			 todayHighlight: 1,  
			 startView: 0,  
			 minView: 2,  
			 forceParse: 0,  
			 pickerPosition: "bottom-left"  
			});  
			$('.form_date4').datetimepicker({ 
			 language:  'zh-CN',  
			 format:"yyyy-mm-dd",  
			 weekStart: 1,  
			 todayBtn:  1,  
			 autoclose: 1,  
			 todayHighlight: 1,  
			 startView: 4,  
			 minView: 4,  
			 forceParse: 0,  
			 pickerPosition: "bottom-left"  
			});  
} 

/** 
 * 删除数据 
 */  
function deleteHr() {  
	var hrs = [];
    hrs = $("#formList").bootstrapTable('getSelections');  
    if (hrs.length < 1) {  
        layer.alert('请选择一条或多条数据进行删除！', {icon: 2});  
    } else {  
    	layer.msg('确定要删除此表单', {
	  		  time: 0 //不自动关闭
	  		  ,btn: ['确定', '再想想']
	  		  ,success: function(layero){
	  				layero.find('.layui-layer-btn').css('text-align', 'center');
	  			}
	  		  ,yes: function(index){
		  			layer.close(index);              
					var signstr = JSON.stringify(hrs);
		  			var sign = md5(signstr); 		 
		            $.ajax({  
		                url:'${pageContext.request.contextPath }/form/deleteForms.action?sign='+sign,  		                type:'post',
		    			cache: false,
		    			async: false,
		                contentType:'application/json',  
		                data:JSON.stringify(hrs),
		                success:function(msg){  
		                    if(msg=="true"){  
		                    	layer.alert('删除成功', {
									  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
									  skin: 'layer-ext-moon' 
									})
		
		                    }else if(msg=="false"){
		                    layer.alert('删除失败', {
									  icon: 3,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
									  skin: 'layer-ext-moon' 
									})
		                    }else if(!msg=="false"&&msg=="true"){
		                    	layer.alert('删除失败', {
									  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
									  skin: 'layer-ext-moon' 
									})
		
		                    }  
		                    refresh();    
		                }  
		            });
		      	}
	        })
    }  
}  
function addForm(){
   if('${param.FOLDER_TREE_ID}'==''){
		alert("系统参数错误，未找到业务类型。");
		return;
	}
   	
	var url = '/myehr/jsp/form/formcreate/';
	var content =   "<div class=\"col-lg-4 col-md-4 col-sm-6 col-xs-12\">\n"+
					"	<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;\">\n"+
					"		<lable style=\"float:left;font-size:12px\">表单编码: </lable>\n"+
					"		<input id=\"formDefCode\" name=\"formDefCode\" type=\"text\"  required=\"true\" class=\"form-control\"  />\n"+
					"	</div>\n"+
					"</div>\n"+
					"<div class=\"col-lg-4 col-md-4 col-sm-6 col-xs-12\">\n"+
					"	<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;\">\n"+
					"		<lable style=\"float:left;font-size:12px\">表单名称: </lable>\n"+
					"		<input id=\"formDefName\" name=\"formDefName\" type=\"text\"  required=\"true\" class=\"form-control\"  />\n"+
					"	</div>\n"+
					"</div>\n"+
					"<div class=\"col-lg-4 col-md-4 col-sm-6 col-xs-12\">\n"+
					"	<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;\">\n"+
					"		<lable style=\"float:left;font-size:12px\">表单布局类型: </lable>\n"+
					"		<select id=\"formDefLayoutType\" title=\"表单布局类型\" styleType=\"select\" name=\"formDefLayoutType\" onchange=\"LayoutTypeChanged()\"  class=\"form-control\" style=\"width:200px;\">\n"+
					"			<option value>请选择</option>\n"+
					"			<option value=\"1\">普通卡片式</option>\n"+
					"			<option value=\"2\">栅格式列表</option>\n"+
					"			<option value=\"10\">卡片式列表</option>\n"+
					"			<option value=\"3\">普通树</option>\n"+
					"			<option value=\"4\">普通报表</option>\n"+
					"			<option value=\"6\">方案树</option>\n"+
					"			<option value=\"5\">主从表(tab)</option>\n"+	
					"			<option value=\"7\">多Tab</option>\n"+		
					"			<option value=\"8\">易客报表</option>\n"+		
					"			<option value=\"9\">图表</option>\n"+							
					"			<option value=\"11\">时间轴表单</option>\n"+
					"			<option value=\"12\">布局表单</option>\n"+
					"			<option value=\"13\">问卷表单</option>\n"+
					"			<option value=\"14\">新闻表单</option>\n"+
					"			<option value=\"15\">九宫格表单</option>\n"+
					"			<option value=\"16\">新图表表单</option>\n"+
					"		</select>\n"+
					"	</div>\n"+
					"</div>\n"+
					"<div id=\"gridType\" class=\"col-lg-4 col-md-4 col-sm-6 col-xs-12\" style=\"display:none\">\n"+
					"	<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;\">\n"+
					"		<lable style=\"float:left;font-size:12px\">列表类型: </lable>\n"+
					"		<select id=\"expandField\" title=\"列表类型\" styleType=\"select\" name=\"expandField\"  class=\"form-control\" style=\"width:200px;\">\n"+
					"			<option value=\"\">栅格式列表</option>\n"+
					"			<option value=\"GRIDBYCARD\">卡片式列表</option>\n"+						
					"		</select>\n"+
					"	</div>\n"+
					"</div>\n";
							
		layer.open({
			title:'输入内容',
			shadeClose: true,
			zIndex:9999,
			content: content,
			area: ['700px', '300px'],
			btn: ['确定', '取消'],
			success:function(layero,index){
			},
			yes: function(index, layero){
				var param = {};
				param.formDefName = $("#formDefName").val();
				param.formDefCode = $("#formDefCode").val();
				param.formDefFolderId = '${param.FOLDER_TREE_ID}';
				param.formDefLayoutType = $("#formDefLayoutType").val();
				param.formDefRowCount = '3,2,1';
				param.formLableWidth =	'200';
				param.expandField = $("#expandField").val();
				if(param.formDefName!=""&&param.formDefCode!=""&&param.formDefLayoutType!=""){
					$.ajax({
						url:'${pageContext.request.contextPath }/form/saveFormParam.action',
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
								} else{
									if(param.formDefLayoutType=="1"){
										url+= "formCardBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="2"){
										url+= "formGridBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="3"){
										url+= "formTreeBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="5"){
										url+= "formMstBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="7"){
										url+= "formTabsBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="10"){
										url+= "formGridByCardBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="8"){
										url+= "formYKreportBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="9"){
										url+= "formChartsBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="11"){
										url+= "formTimeAxisBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="12"){
										url+= "formDragFormStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="13"){
										url+= "formQuestionBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}else if(param.formDefLayoutType=="16"){
										url+= "formNewChartsBuildStep.jsp?formDefId="+data;
										layer.close(index);
										window.open(url);
									}
								}
							
							}
						});
				}else{
					layer.alert('信息输入不完整', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
				}
			  },
			btn2: function(index, layero){
				
			  }
		});
	
}
function add(){
	if('${param.FOLDER_TREE_ID}'==''){
		alert("系统参数错误，未找到业务类型。");
		return;
	}
	top.layer.open({
		type: 2,
		title: '选择表单类型',
		shadeClose: true,
		shade: 0.8,
		area: ['100%', '100%'],
		content: '/myehr/jsp/formbuild/test/formManage/Card1.jsp?FOLDER_TREE_ID=${param.FOLDER_TREE_ID}', //iframe的url
		success:function(layero,index){
	    },
		end:function(){
			refresh();
		}
	})
}

function LayoutTypeChanged(){
	if($("#formDefLayoutType").val()==10){
		$("#expandField").val("GRIDBYCARD");
	}else{
		$("#expandField").val("");
	}
}

function flushMap(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length>0){
		var content =	"<div class=\"col-lg-4 col-md-4 col-sm-6 col-xs-12\">\n"+
						"	<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;\">\n"+
						"		<lable style=\"float:left;font-size:12px\">缓存类型: </lable>\n"+
						"		<select id=\"cacheType\" title=\"缓存类型\" styleType=\"select\" name=\"cacheType\" class=\"form-control\" style=\"width:200px;\">\n"+
						"			<option value=\"\">请选择</option>\n"+
						//"			<option value=\"1\">字段字典缓存</option>\n"+
						"			<option value=\"2\">重载表单redis缓存</option>\n"+
						"		</select>\n"+
						"	</div>\n"+
						"</div>\n";
		layer.open({
			title:'输入内容',
			shadeClose: true,
			zIndex:9999,
			content: content,
			area: ['700px', '300px'],
			btn: ['确定', '取消'],
			success:function(layero,index){
			},
			yes: function(index, layero){
				var cacheType = $("#cacheType").val();
				if(cacheType!=""){
					var url = '';
					if(cacheType=='1'){
						url = '${pageContext.request.contextPath }/form/flushDictCacheByFormIds.action?';
					}else if(cacheType=='2'){
						url = '${pageContext.request.contextPath }/cache/refreshCache.action?';
					}
					$.ajax({
						url:url,
						type:'POST',
						data: JSON.stringify(hrs),
						cache: false,
						contentType: 'application/json;charset=utf-8',
						async: false,
						success: function (data) {
							layer.close(index);
						}
					});
					layer.close(index);
				}else{
					layer.alert('信息输入不完整', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
				}
			  },
			btn2: function(index, layero){
				
			}
		});
	}
}

function update(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length!=1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	if(hrs[0].formSubmitStatus=='Y'){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"表单已提交,不可修改")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	var url;
	if(hrs[0].formDefLayoutType == "1"){
		url = '/myehr/jsp/form/formcreate/formCardBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "2"){
		url = '/myehr/jsp/form/formcreate/formGridBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "3"){
		url = '/myehr/jsp/form/formcreate/formTreeBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "5"){
		url = '/myehr/jsp/form/formcreate/formMstBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "7"){
		url = '/myehr/jsp/form/formcreate/formTabsBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "10"){
		url = '/myehr/jsp/form/formcreate/formGridByCardBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "8"){
		url = '/myehr/jsp/form/formcreate/formYKreportBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "9"){
		url = '/myehr/jsp/form/formcreate/formChartsBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "11"){
		url = '/myehr/jsp/form/formcreate/formTimeAxisBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "12"){
		url = '/myehr/jsp/form/formcreate/formDragFormStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "13"){
		url = '/myehr/jsp/form/formcreate/formQuestionBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}else if(hrs[0].formDefLayoutType == "16"){
		url = '/myehr/jsp/form/formcreate/formNewChartsBuildStep.jsp?update=true&formDefId='+hrs[0].formDefId;
	}
	var author =((url.replace((url.substring(0, url.indexOf('?')+1)),'')));
		var author1 = md5(author);

		url = url+'&'+'author1='+author1;
	window.open(url);
	
}

function move(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length<1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中至少一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	layer.open({
		type: 2,
		title: '移动表单',
		shadeClose: true,
		shade: 0.8,
		area: ['25%', '85%'],
		content: 'formPosition.jsp', //iframe的url
		success:function(layero,index){
	    },
		end:function(){
			refresh();
		}
	})
}

function updateFormFolderid(formFolderId){
	var hrs = $("#formList").bootstrapTable('getSelections');
	layer.msg('确定要移动此表单', {
		  time: 0 //不自动关闭
		  ,btn: ['确定', '再想想']
		  ,success: function(layero){
				layero.find('.layui-layer-btn').css('text-align', 'center');
			}
		  ,yes: function(index){
	  			layer.close(index);              
	            $.ajax({  
	                url:'${pageContext.request.contextPath }/form/updateFormFolderid.action?formFolderId='+formFolderId,  
	                type:'post',
	    			cache: false,
	    			async: false,
	                contentType:'application/json',  
	                data:JSON.stringify(hrs),
	                success:function(msg){  
	                    if(msg=="true"){  
	                    	layer.alert('移动成功', {
								  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
								  skin: 'layer-ext-moon' 
								})
								layer.closeAll();
		                    refresh(); 
	                    }else{  
	                    	layer.alert('移动失败', {
								  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
								  skin: 'layer-ext-moon' 
								})
	
	                    }     
	                }  
	            });
	            
	      	}
      })
}

function look(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length!=1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	var url;
	url = '/myehr/form/toForm.action?formId='+hrs[0].formDefId;	
		
		var author =((url.replace((url.substring(0, url.indexOf('?')+1)),'')));
		var author1 = md5(author);
		var author2=author.split('=');
		var order = '';
		 for(var i = 0;i<author2.length;i++){;
		 if(i==0){ 
		  order +=author2[i]+',';
		  }else{
		 	order += author2[i].split('&')[1];
		 	if(!(i==author2.length-1)){
		 	order +=',';}
		 }
		 }
		
		url = url+'&'+'author1='+author1+'&order='+order;	
		window.open(url);
}

function copy(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length!=1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	layer.msg('确定要复制此表单', {
		  time: 0 //不自动关闭
		  ,btn: ['确定', '再想想']
		  ,success: function(layero){
				layero.find('.layui-layer-btn').css('text-align', 'center');
			}
		  ,yes: function(index){
	  			layer.close(index);  
	  			var form={};
	  			form.formDefId = hrs[0].formDefId;
	  			var url = "";
	  			if(hrs[0].formDefLayoutType=='1'){//卡片
	  				url = '${pageContext.request.contextPath }/FormCopy/cardFormCopy.action';
	  			}else if(hrs[0].formDefLayoutType=='2'){//栅格式列表
	  				url = '${pageContext.request.contextPath }/FormCopy/gridFormCopy.action';
	  			}else if(hrs[0].formDefLayoutType=='3'){//树
	  				url = '${pageContext.request.contextPath }/FormCopy/treeFormCopy.action';
	  			}else if(hrs[0].formDefLayoutType=='7'){//多Tab
	  				url = '${pageContext.request.contextPath }/FormCopy/tabsFormCopy.action';
	  			}else if(hrs[0].formDefLayoutType=='5'){//主从
	  				url = '${pageContext.request.contextPath }/FormCopy/mstFormCopy.action';
	  			}
	  			$.ajax({  
	  		        url:url,  
	  		        type:'post',
	  				cache: false,
	  				async: false,
	  		        contentType:'application/json',  
	  		        data:JSON.stringify(form),
	  		        success:function(msg){  
	  		            if(msg=="true"){  
	  		            	layer.alert('复制成功', {
	  							  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})
	  							layer.closeAll();
	  		                refresh(); 
	  		            }else if(msg=="1"){  
	  		            	layer.alert('复制失败,表单已复制,请修改复制表单编码', {
	  							  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})
	  		          	}else{  
	  		            	layer.alert('复制失败', {
	  							  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})

	  		            }     
	  		        }  
	  		    });
	  			
	      	}
    })
}
/*
function buildAllForm(){
	var url = '${pageContext.request.contextPath }/form/toFormAll.action';
		$.ajax({  
			url:url,  
			type:'post',
			cache: false,
			async: false,
			contentType:'application/json',  
			//data:JSON.stringify(hrs),
			success:function(msg){  
				if(msg=="true"){  
					layer.alert('生成成功', {
						  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})
						layer.closeAll();
					refresh(); 
				}else{  
					layer.alert('生成失败', {
						  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
						  skin: 'layer-ext-moon' 
						})

				}     
			}  
		});
}
*/
function refer(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length<1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	layer.msg('确定要提交此表单', {
		  time: 0 //不自动关闭
		  ,btn: ['确定', '再想想']
		  ,success: function(layero){
				layero.find('.layui-layer-btn').css('text-align', 'center');
			}
		  ,yes: function(index){
	  			layer.close(index);  
	  			//var form={};
	  			//form.formDefId = hrs[0].formDefId;
	  			var url = '${pageContext.request.contextPath }/FormCopy/referUrl.action';
	  			$.ajax({  
	  		        url:url,  
	  		        type:'post',
	  				cache: false,
	  				async: false,
	  		        contentType:'application/json',  
	  		        data:JSON.stringify(hrs),
	  		        success:function(msg){  
	  		            if(msg=="true"){  
	  		            	layer.alert('提交成功', {
	  							  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})
	  							layer.closeAll();
	  		                refresh(); 
	  		            }else{  
	  		            	layer.alert('提交失败', {
	  							  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})

	  		            }     
	  		        }  
	  		    });
	  			
	      	}
    })
}

function removeRefer(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length<1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	layer.msg('确定要取消此表单的提交状态', {
		  time: 0 //不自动关闭
		  ,btn: ['确定', '再想想']
		  ,success: function(layero){
				layero.find('.layui-layer-btn').css('text-align', 'center');
			}
		  ,yes: function(index){
	  			layer.close(index);  
	  			//var form={};
	  			//form.formDefId = hrs[0].formDefId;
	  			var url = '${pageContext.request.contextPath }/FormCopy/removeRefer.action';
	  			$.ajax({  
	  		        url:url,  
	  		        type:'post',
	  				cache: false,
	  				async: false,
	  		        contentType:'application/json',  
	  		        data:JSON.stringify(hrs),
	  		        success:function(msg){  
	  		            if(msg=="true"){  
	  		            	layer.alert('取消成功', {
	  							  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})
	  							layer.closeAll();
	  		                refresh(); 
	  		            }else{  
	  		            	layer.alert('取消失败', {
	  							  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
	  							  skin: 'layer-ext-moon' 
	  							})

	  		            }     
	  		        }  
	  		    });
	  			
	      	}
    })
}

function importFormDataToOther(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length<1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	var url = '${pageContext.request.contextPath}/FormCopy/cardFormCopyToOther.action?formId='+hrs[0].formDefId;
	  			$.ajax({  
	  		        url:url,  
	  		        type:'post',
	  				cache: false,
	  		        success:function(msg){     
	  		        }  
	  		    });
}

function createVue(){
	var hrs = $("#formList").bootstrapTable('getSelections');
	if(hrs.length<1){
		layer.alert('<%=LangUtil.getLangByCode((String)session.getAttribute("langType"),"请选中一条数据")%>', {
			  icon: 2,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;
			  skin: 'layer-ext-moon' //
			})
			return;
	}
	var url = '${pageContext.request.contextPath}/form/createAppVue.action?formId='+hrs[0].formDefId;
	  			$.ajax({  
	  		        url:url,  
	  		        type:'post',
	  				cache: false,
	  		        success:function(msg){     
	  		        }  
	  		    });
}

</script>  
</body>
</html>
