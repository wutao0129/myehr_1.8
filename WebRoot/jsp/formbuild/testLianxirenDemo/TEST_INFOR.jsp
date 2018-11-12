<%@ page language="java" import="java.util.*" import="com.myehr.common.util.LangUtil" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>联系人</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
	<script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
<style type="text/css">
    /*这行很重要!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
    html,body{min-height: 100%;}
</style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
<div class="row" style="margin-left:0" id="TEST_INFOR">
</div>
</div>
<script>
	var _formId_2554='2554';
var heightGadedata =[];
function changeHeightGadedata(data){
	heightGadedata = data;
}
var filterParam = {};
function grid_TEST_INFOR_fun(pageReqeust){
	var _filterdata={};
	if(document.getElementById('grid_TEST_INFOR_filter')){
	}
	var paramsMap = {};
	var requestParam={};
		requestParam = {};
	pageReqeust.containerParam=containerParam;
	pageReqeust.paramsMap=paramsMap;
	pageReqeust.requestParam=requestParam;
	pageReqeust.filter=_filterdata;
	pageReqeust.userIds='${param.empIds}';
	pageReqeust.formId=_formId_2554;
	pageReqeust.isView=null;
	pageReqeust.offset=Currentpage*pagesize;;
	pageReqeust.limit=pagesize;;
	if(heightGadedata!=null&&heightGadedata!=undefined){
		pageReqeust.heightGrade = heightGadedata;
	}
	return JSON.stringify(pageReqeust);
}
function grid_TEST_INFOR_fun11_2554(){
	$('#TEST_INFOR_List').bootstrapTable('refresh');
}
//当前页和一次显示多少条数据
var Currentpage = 0;
var pagesize = 10;
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
           element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
};
//作为一个对象的w和h属性返回视口的尺寸
function getViewportSize(w){
    w = w || window;
    if(w.innerWidth != null){
        return {w: w.innerWidth, h: w.innerHeight};}
    var d = w.document;
    if(document.compatMode == "CSS1Compat"){
        return {w: d.documentElement.clientWidth, h: d.documentElement.clientHeight};}
    return {w: d.body.clientWidth, h: d.body.clientHeight};
}
//检测滚动条是否滚动到页面底部
function isScrollToPageBottom(){
    //文档高度
    var documentHeight = document.documentElement.offsetHeight;
    var viewPortHeight = getViewportSize().h;
    var scrollHeight = window.pageYOffset ||
            document.documentElement.scrollTop ||
            document.body.scrollTop || 0;
    return documentHeight - viewPortHeight - scrollHeight < 50;
}
//如果滚动条滚动到页面底部，需要加载新的数据,并且显示加载提示
function watchScroll(){
    if(!isScrollToPageBottom()){
        setTimeout( arguments.callee, 900);
        return;            }
    loadDataDynamic();
}
//获取区间中的一个随机数
function rand(n, m){
    var random = Math.floor(Math.random()*(m-n+1)+n);
    return random;
};
var containerParam={};
var xx = {};
$(document).ready(function () {
$.ajax({
    url: "/myehr/form/cardformInitData.action",
    type: 'post',
    data:grid_TEST_INFOR_fun(xx),//HTTP请求类型
    contentType: 'application/json;charset=utf-8',
    cache: false,
    async: false,
    success: function (data) {
	 	 Currentpage++;
        console.log(data);
        var arr=data.rows;
        showDate(arr);
      }
});
watchScroll();
});
function loadDataDynamic(){
//先显示正在加载中
if( $("#loadingLi").length === 0){
     $("#lianxiren").append("<div id='loadingLi' class='loading'>正在加载...</div>");
}else{
    $("#loadingLi").text("正在加载...").removeClass("space");
}
var loadingLi = document.getElementById("loadingLi");
//loadingLi.scrollIntoView();
//加载数据,数据加载完成后需要移除加载提示
var hasData = false, msg = "";
$.ajax({
    url: "/myehr/form/cardformInitData.action",
    type: 'post',
    data:grid_TEST_INFOR_fun(xx),//HTTP请求类型
    contentType: 'application/json;charset=utf-8',
    cache: false,
    async: false
}).done(function(data){
        Currentpage++;
        console.log(data);
        var arr=data.rows;
        if(arr.length > 0){
            hasData = true;
            showDate(arr);
        }else{
            msg = "数据已加载到底了"
        }
        $("#lianxiren").append(loadingLi);
}).fail(function(){
    msg = "数据加载失败!";
 }).always(function(){
    !hasData && setTimeout(function(){
        $(document.body).scrollTop(document.body.scrollTop -40);
    }, 500);
       msg && $("#loadingLi").text(msg);
    //重新监听滚动
    setTimeout(watchScroll, 900);
 });
}
function showDate(arr) {
for (var i = arr.length - 1; i >= 0; i--) {
    var cellObj = $(
        '<div class="col-md-6 col-sm-12 col-lg-4">'+
        '    <div class="contact-box">'+
        '        <a href="profile.html">'+
        '            <div class="col-sm-4">'+
        '                <div class="text-center">'+
        '                    <img width="100" style="margin:0 auto" alt="image" title="'+arr[i].TESTINFOR_china_name+'丶'+arr[i].TESTINFOR_eng_name+'" class="img-circle m-t-xs img-responsive" src="http://192.168.1.109:9999/'+arr[i].TESTINFOR_zhaopian+'">'+
        '                    <div class="m-t-xs font-bold" title="'+arr[i].TESTINFOR_china_name+'丶'+arr[i].TESTINFOR_eng_name+'">'+arr[i].TESTINFOR_china_name+'</div>'+
        '                </div>'+
        '            </div>'+
        '            <div class="col-sm-8">'+
        '                <h3 style="margin-top:0;overflow:hidden;height:26px;color:#400040;" title="'+arr[i].TESTINFOR_china_name+'丶'+arr[i].TESTINFOR_eng_name+'"><strong>'+arr[i].TESTINFOR_china_name+'丶'+arr[i].TESTINFOR_eng_name+'</strong></h3>'+
        '                <p style="overflow:hidden;height:20px;font-size:10;color:#800080" title="'+arr[i].TESTINFOR_guoji+'&'+arr[i].TESTINFOR_jiguan+'&'+arr[i].TESTINFOR_zhuzhi+'"><i class="fa fa-map-marker"></i> '+arr[i].TESTINFOR_guoji+'&'+arr[i].TESTINFOR_jiguan+'&'+arr[i].TESTINFOR_zhuzhi+'</p>'+
        '                <address style="margin-top:7px">'+
        '            <p style="color:#000000;font-size:13">毕业院校:'+ arr[i].TESTINFOR_biyeyuanxiao +'</p>'+
        '            <p style="color:#000000;font-size:13">入学时间:'+ arr[i].TESTINFOR_ruxueshijian +'</p>'+
        '            <p style="color:#000000;font-size:13">毕业时间:'+ arr[i].TESTINFOR_biyeshijian +'</p>'+
        '            <p style="color:#000000;font-size:13">住址:'+ arr[i].TESTINFOR_zhuzhi +'</p>'+
        '                </address>'+
        '            </div>'+
        '            <div class="clearfix"></div>'+
        '        </a>'+
        '    </div>'+
        '</div>');
    animationHover(cellObj, 'pulse');
    $('#TEST_INFOR').append(cellObj);
}
};
</script>
</body>
</html>