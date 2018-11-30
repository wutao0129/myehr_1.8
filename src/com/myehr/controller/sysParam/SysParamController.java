package com.myehr.controller.sysParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myehr.common.mybatis.Pager;
import com.myehr.common.util.AuthorizationUtil;
import com.myehr.common.util.GetDBPropertiesValue;
import com.myehr.common.util.XmlUtil;
import com.myehr.controller.dict.param.ResultMapNew;
import com.myehr.controller.form.parambean.CardformInitDataParams;
import com.myehr.controller.form.parambean.SaveButtonParams;
import com.myehr.mapper.sysParam.SysSourceMapper;
import com.myehr.mapper.sysParam.SysSystemParamMapper;
import com.myehr.pojo.formmanage.form.SysFormconfigWithBLOBs;
import com.myehr.pojo.formmanage.template.gridbycard.SysGridbycardTemplateColumn;
import com.myehr.pojo.sysParam.SysSource;
import com.myehr.pojo.sysParam.SysSourceExample;
import com.myehr.pojo.sysParam.SysSystemParam;
import com.myehr.pojo.sysParam.SysSystemParamExample;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.widget.IComboBoxService;import com.myehr.test.dao.TMapperExt;

@Controller
@RequestMapping("/systemParam")
public class SysParamController {
	
//	@Autowired
	@Resource(name = "TMapperExt")
	private TMapperExt tMapperExt;
	
	@Autowired
	private SysSystemParamMapper sysSystemParamMapper;
	
	@Autowired
	private SysSourceMapper sysSourceMapper;
	
	@Autowired
	IComboBoxService IComboBoxService ;
	@Autowired
	private ISysformconfigService sysformconfigService;
		@RequestMapping("/querySystemParam")
	public @ResponseBody ResultMapNew querySystemParam(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Exception{
		Map filter = params.getFilter();
		String offset = params.getOffset();
		String limit = params.getLimit();
		//Map paramsMap = params.getParamsMap();
		Pager page = params.getPage();
		page = new Pager();
		if(offset==null&&limit==null){
			
		}else {
			int start=Integer.parseInt(offset);
			page.setStart(start); //不能设置为0
			page.setLimt(Integer.parseInt(limit));
		}
		
		String sysParamType = (String) filter.get("sysParamType");
		String deleteMark = (String) filter.get("deleteMark");
		String wheres="";
		if (sysParamType!=null && sysParamType!="") {
			wheres += " and o.SYS_PARAM_TYPE = '"+sysParamType+"' ";
		}
		String searchValue = (String) filter.get("searchValue");
		if (searchValue!=null && searchValue!="") {
			wheres += " and ( o.SYS_PARAM_ENTRY like "+" '%"+searchValue+"%' "+
					  "or o.SYS_PARAM_CODE like "+" '%"+searchValue+"%' )";
		}
		if (deleteMark!=null && deleteMark!="") {
			wheres += " and SYS_DICT_TYPE.DELETE_MARK = "+" '"+deleteMark+"' ";
		}
		
		String sql = "select o.SYS_PARAM_ID as sysParamId, " +
				" o.SYS_PARAM_TYPE as sysParamType, "+
				" o.SYS_PARAM_CODE as sysParamCode, "+
				" o.SYS_PARAM_ENTRY as sysParamEntry, "+
				" o.SYS_PARAM_VALUE1 as sysParamValue1, "+
				" o.SYS_PARAM_VALUE2 as sysParamValue2, "+
				" o.SYS_PARAM_REMARK as sysParamRemark "+
				" from sys_system_param o where 1=1 "+wheres;
		List<Map> schemes = tMapperExt.queryByFormDefSql(sql);
		
		ResultMapNew resultMap=new ResultMapNew();
		
		List<Map> schemeList = new ArrayList<Map>();
		int end = 0;
		if (page.getStart()+page.getLimt()>schemes.size()) {
			end = schemes.size();
		}else {
			end = page.getStart()+page.getLimt();
		}
		
		for (int i = page.getStart(); i < end; i++) {
			schemeList.add(schemes.get(i));
		}

		resultMap.setTotal(schemes.size());
		resultMap.setRows(schemeList);
		return resultMap;
	}
	
	@RequestMapping("/querySystemSource")
	public @ResponseBody ResultMapNew querySystemSource(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Exception{
		Map filter = params.getFilter();
		String offset = params.getOffset();
		String limit = params.getLimit();
		//Map paramsMap = params.getParamsMap();
		Pager page = params.getPage();
		page = new Pager();
		if(offset==null&&limit==null){
			
		}else {
			int start=Integer.parseInt(offset);
			page.setStart(start); //不能设置为0
			page.setLimt(Integer.parseInt(limit));
		}
		
		String sysParamType = (String) filter.get("sysParamType");
		String wheres="";
		if (sysParamType!=null && sysParamType!="") {
			wheres += " and o.SYS_PARAM_TYPE = '"+sysParamType+"' ";
		}
		String searchValue = (String) filter.get("searchValue");
		if (searchValue!=null && searchValue!="") {
			wheres += " and ( o.SYS_PARAM_ENTRY like "+" '%"+searchValue+"%' "+
					  "or o.SYS_PARAM_CODE like "+" '%"+searchValue+"%' )";
		}
		
		String sql = "select * from sys_source where 1=1 ";
		List<Map> schemes = tMapperExt.queryByFormDefSql(sql);
		
		ResultMapNew resultMap=new ResultMapNew();
		
		List<Map> schemeList = new ArrayList<Map>();
		int end = 0;
		if (page.getStart()+page.getLimt()>schemes.size()) {
			end = schemes.size();
		}else {
			end = page.getStart()+page.getLimt();
		}
		
		for (int i = page.getStart(); i < end; i++) {
			schemeList.add(schemes.get(i));
		}

		resultMap.setTotal(schemes.size());
		resultMap.setRows(schemeList);
		return resultMap;
	}
	
	@RequestMapping("/querySystemParamById")
	public @ResponseBody SysSystemParam querySystemParamById(HttpServletRequest request) throws Exception{
		
		String sysParamId = request.getParameter("sysParamId");
		SysSystemParamExample example = new SysSystemParamExample();
		com.myehr.pojo.sysParam.SysSystemParamExample.Criteria criteria = example.createCriteria();
		criteria.andSysParamIdEqualTo(Long.valueOf(sysParamId));
		
		SysSystemParam systemParam = sysSystemParamMapper.selectByExample(example).get(0);
		return systemParam;
	}
	
	@RequestMapping("/querySystemSourceById")
	public @ResponseBody SysSource querySystemSourceById(HttpServletRequest request) throws Exception{
		
		String sysSourceId = request.getParameter("sysSourceId");
		SysSourceExample example = new SysSourceExample();
		example.or().andSysSourceIdEqualTo(Long.valueOf(sysSourceId));
		SysSource sysSource = sysSourceMapper.selectByExample(example).get(0);
		return sysSource;
	}
	
	@RequestMapping("/startSystemSourceById")
	public @ResponseBody SysSource startSystemSourceById(HttpServletRequest request) throws Exception{
		
		String sysSourceId = request.getParameter("sysSourceId");
		SysSourceExample example = new SysSourceExample();
		example.or().andSysSourceIdEqualTo(Long.valueOf(sysSourceId));
		SysSource sysSource = sysSourceMapper.selectByExample(example).get(0);
		
		String path =  System.getProperty("user.dir").replace("\\", "/").replace("bin", "");
		path = Thread.currentThread().getContextClassLoader().getResource("/").getPath()+"db.properties";
		String path1 =  Thread.currentThread().getContextClassLoader().getResource("/").getPath()+File.separator+"db.properties";
		GetDBPropertiesValue.writeProperties(path, sysSource.getSysSoruceDriver(),sysSource.getSysSoruceDriverValue());
		GetDBPropertiesValue.writeProperties(path, sysSource.getSysSoruceUrl(),sysSource.getSysSoruceUrlValue());
		GetDBPropertiesValue.writeProperties(path, sysSource.getSysSoruceUsername(),sysSource.getSysSoruceUsernameValue());
		GetDBPropertiesValue.writeProperties(path, sysSource.getSysSorucePassword(),sysSource.getSysSorucePasswordValue());
		GetDBPropertiesValue.writeProperties(path1, sysSource.getSysSoruceDriver(),sysSource.getSysSoruceDriverValue());
		GetDBPropertiesValue.writeProperties(path1, sysSource.getSysSoruceUrl(),sysSource.getSysSoruceUrlValue());
		GetDBPropertiesValue.writeProperties(path1, sysSource.getSysSoruceUsername(),sysSource.getSysSoruceUsernameValue());
		GetDBPropertiesValue.writeProperties(path1, sysSource.getSysSorucePassword(),sysSource.getSysSorucePasswordValue());
		
		XmlUtil.addSysSource(sysSource);
		
		return sysSource;
	}
	
	@RequestMapping("/saveSystemParam")
	public @ResponseBody String saveSystemParam(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		String menuJson = "";
		
		Map param = params.getParam();
		Map paramsMap = params.getParamsMap();
		int reCode = 0;
		
		String sysParamId = (String) param.get("sysParamId");
		SysSystemParam systemParam = new SysSystemParam();
		if (sysParamId==null || sysParamId=="") {
			systemParam.setSysParamType((String) param.get("sysParamType"));
			systemParam.setSysParamCode((String) param.get("sysParamCode"));
			systemParam.setSysParamEntry((String) param.get("sysParamEntry"));
			systemParam.setSysParamValue1((String) param.get("sysParamValue1"));
			systemParam.setSysParamValue2((String) param.get("sysParamValue2"));
			systemParam.setSysParamRemark((String) param.get("sysParamRemark"));
			reCode = sysSystemParamMapper.insert(systemParam);
		}else {
			systemParam.setSysParamId(Long.valueOf(sysParamId));
			systemParam.setSysParamType((String) param.get("sysParamType"));
			systemParam.setSysParamCode((String) param.get("sysParamCode"));
			systemParam.setSysParamEntry((String) param.get("sysParamEntry"));
			systemParam.setSysParamValue1((String) param.get("sysParamValue1"));
			systemParam.setSysParamValue2((String) param.get("sysParamValue2"));
			systemParam.setSysParamRemark((String) param.get("sysParamRemark"));
			reCode = sysSystemParamMapper.updateByPrimaryKey(systemParam);
		}
		
		return menuJson;
	}
	
	@RequestMapping("/saveSystemParamPW")
	public @ResponseBody String saveSystemParamPW(HttpServletRequest request) throws Exception{
		String menuJson = "";
		String SysParamCode = "InitPasswork";
		String regex="";
		String regex1="";
		String regex2="";
		String regex3="";
		int reCode = 0;
		String password = "";
		String rule = "";
		
		StringBuilder builder = new StringBuilder();
		String cbstr = request.getParameter("cbstr");
		String cbstrArr[] = cbstr.split(",");
		if(cbstrArr[0].equals("true")){
			builder.append(".*[a-zA-Z]+.*_");
		}else{
			builder.append("null_");
		}
		if(cbstrArr[1].equals("false")){
			builder.append("null_");
		}else{
			builder.append("(?=.*[A-Z].*)(?=.*[a-z].*)._");
		}
		if(cbstrArr[2].equals("true")){
			builder.append(".*[0-9]+.*_");
		}else{
			builder.append("null_");
		}
		builder.append("^[a-zA-Z0-9]{"+request.getParameter("cd")+"}$");
		rule = builder+"";
		SysSystemParam systemParam = new SysSystemParam();
		SysSystemParam param = sysformconfigService.getSystemParam(SysParamCode);
		if (param==null) {
			systemParam.setSysParamType((String) request.getParameter("SysParamType"));
			systemParam.setSysParamCode((String) SysParamCode);
			systemParam.setSysParamEntry(request.getParameter("password"));
			systemParam.setSysParamValue1(rule);
			systemParam.setSysParamValue2((String) request.getParameter("cbstr"));
			reCode = sysSystemParamMapper.insert(systemParam);
			sysformconfigService.initSystemParam();
			menuJson = "1";
		}else {
			param.setSysParamType((String) request.getParameter("SysParamType"));
			param.setSysParamEntry(request.getParameter("password"));
			param.setSysParamValue1(rule);
			param.setSysParamValue2((String) request.getParameter("cbstr"));
			reCode = sysSystemParamMapper.updateByPrimaryKey(param);
			sysformconfigService.initSystemParam();
			menuJson = "2";
		}
		
		return menuJson;
	}
	
	
	
	@RequestMapping("/saveSystemParamZM")
	public @ResponseBody String saveSystemParamZM(HttpServletRequest request,@RequestBody List<SysSystemParam> slist) throws Exception{
		SysSystemParam param = new SysSystemParam();
		for (SysSystemParam syssystemparam : slist) {
			String code = syssystemparam.getSysParamCode();
			SysSystemParamExample example = new SysSystemParamExample();
			example.or().andSysParamCodeEqualTo(code);
			System.out.println(sysSystemParamMapper.selectByPrimaryKey(syssystemparam.getSysParamId()));
			if(syssystemparam.getSysParamId()==null||syssystemparam.getSysParamId()+""==""){
				sysSystemParamMapper.insert(syssystemparam);
				sysformconfigService.setSystemParam();
				System.out.println("保存");
			}else{
				
				syssystemparam.setSysParamId(Long.valueOf((syssystemparam.getSysParamId())));
				sysSystemParamMapper.updateByPrimaryKey(syssystemparam);
				sysformconfigService.setSystemParam();
				System.out.println("更新");
			}
		}
		
		System.out.println("进来了");
		
		return "00";
		
	}
	

	@RequestMapping("/InitZM")
	public @ResponseBody List<SysSystemParam> InitZM(HttpServletRequest request) throws Exception{
		//背景图
		SysSystemParam param1 = sysformconfigService.getSystemParam("background-imagepath");
		//logo图地址
		SysSystemParam param2 = sysformconfigService.getSystemParam("logopath");
		//logo图位置
		SysSystemParam param3 = sysformconfigService.getSystemParam("logopathsp");
		//公司名称
		SysSystemParam param4 = sysformconfigService.getSystemParam("name");
		//公司名称位置
		SysSystemParam param5 = sysformconfigService.getSystemParam("namesp");
		//软件厂商
		SysSystemParam param6 = sysformconfigService.getSystemParam("Software");
		//软件厂商位置
		SysSystemParam param7 = sysformconfigService.getSystemParam("Softwaresp");
				
		//版权信息
		SysSystemParam param8 = sysformconfigService.getSystemParam("copyright");
		//版权信息位置
		SysSystemParam param9 = sysformconfigService.getSystemParam("copyrightsp");
		
		List<SysSystemParam> ss = new ArrayList<SysSystemParam>();
		ss.add(param1);
		ss.add(param2);
		ss.add(param3);
		ss.add(param4);
		ss.add(param5);
		ss.add(param6);
		ss.add(param7);
		ss.add(param8);
		ss.add(param9);
		
		//String rule  = param1.getSysParamId().toString()+"&"+param1.getSysParamEntry().toString()+"&"+param2.getSysParamId().toString()+"&"+param2.getSysParamEntry().toString()+"&"+param3.getSysParamId().toString()+"&"+param3.getSysParamEntry().toString()+"&"+"name"+"&"+"name"+"&"+"namesp"+"&"+"namesp"+"&"+param6.getSysParamId().toString()+"&"+param6.getSysParamEntry().toString()+"&"+param7.getSysParamId().toString()+"&"+param7.getSysParamEntry().toString()+"&"+param8.getSysParamId().toString()+"&"+param8.getSysParamEntry().toString()+"&"+param9.getSysParamId().toString()+"&"+param9.getSysParamEntry().toString();
		
		return ss;
		
	}
	

		@RequestMapping("/saveSystemSource")
	public @ResponseBody String saveSystemSource(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		
		Map param = params.getParam();
		int reCode = 0;
		
		String sysSourceId = (String) param.get("sysSourceId");
		SysSource sysSource = new SysSource();
		if (sysSourceId==null || sysSourceId=="") {
			sysSource.setSysSourceType((String) param.get("sysSourceType"));
			sysSource.setSysSourceCode((String) param.get("sysSourceCode"));
			sysSource.setSysSourceName((String) param.get("sysSourceName"));
			sysSource.setSysSoruceDriver((String) param.get("sysSoruceDriver"));
			sysSource.setSysSoruceDriverValue((String) param.get("sysSoruceDriverValue"));
			sysSource.setSysSoruceUrl((String) param.get("sysSoruceUrl"));
			sysSource.setSysSoruceUrlValue((String) param.get("sysSoruceUrlValue"));
			sysSource.setSysSoruceUsername((String) param.get("sysSoruceUsername"));
			sysSource.setSysSoruceUsernameValue((String) param.get("sysSoruceUsernameValue"));
			sysSource.setSysSorucePassword((String) param.get("sysSorucePassword"));
			sysSource.setSysSorucePasswordValue((String) param.get("sysSorucePasswordValue"));
			reCode = sysSourceMapper.insert(sysSource);
		}else {
			sysSource.setSysSourceId(Long.valueOf(sysSourceId));
			sysSource.setSysSourceType((String) param.get("sysSourceType"));
			sysSource.setSysSourceCode((String) param.get("sysSourceCode"));
			sysSource.setSysSourceName((String) param.get("sysSourceName"));
			sysSource.setSysSoruceDriver((String) param.get("sysSoruceDriver"));
			sysSource.setSysSoruceDriverValue((String) param.get("sysSoruceDriverValue"));
			sysSource.setSysSoruceUrl((String) param.get("sysSoruceUrl"));
			sysSource.setSysSoruceUrlValue((String) param.get("sysSoruceUrlValue"));
			sysSource.setSysSoruceUsername((String) param.get("sysSoruceUsername"));
			sysSource.setSysSoruceUsernameValue((String) param.get("sysSoruceUsernameValue"));
			sysSource.setSysSorucePassword((String) param.get("sysSorucePassword"));
			sysSource.setSysSorucePasswordValue((String) param.get("sysSorucePasswordValue"));
			reCode = sysSourceMapper.updateByPrimaryKey(sysSource);
		}
		
		return reCode+"";
	}
	
}
