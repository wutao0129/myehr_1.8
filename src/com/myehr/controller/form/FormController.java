package com.myehr.controller.form;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.jsqlparser.expression.LongValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myehr.common.exception.DcfException;
import com.myehr.common.mybatis.DcfReponse;
import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.mybatis.Pager;
import com.myehr.common.util.AuthorizationUtil;
import com.myehr.common.util.ChangeCode;
import com.myehr.common.util.CreateFileUtil;
import com.myehr.common.util.DataSign;
import com.myehr.common.util.DealStrSub;
import com.myehr.common.util.ExcelUtils;
import com.myehr.common.util.FormConfigResultMap;
import com.myehr.common.util.GetDBPropertiesValue;
import com.myehr.common.util.GetRequestJsonUtils;
import com.myehr.common.util.MyEhrJDBCUtil;
import com.myehr.common.util.ResultMap;
import com.myehr.common.util.StringUtils;
import com.myehr.common.util.datasource.CustomerContextHolder;
import com.myehr.controller.dict.param.ResultMapNew;
import com.myehr.controller.form.parambean.CardformInitDataParams;
import com.myehr.controller.form.parambean.CopyDataformParams;
import com.myehr.controller.form.parambean.DictDataParams;
import com.myehr.controller.form.parambean.ExcRuleDataParams;
import com.myehr.controller.form.parambean.ExecButtonExecSqlDataParams;
import com.myehr.controller.form.parambean.ExportCardformInitDataParams;
import com.myehr.controller.form.parambean.ImportExcelData;
import com.myehr.controller.form.parambean.InitExecSqlParams;
import com.myehr.controller.form.parambean.IntroduceParams;
import com.myehr.controller.form.parambean.RemoveDataformParams;
import com.myehr.controller.form.parambean.ResultColumns;
import com.myehr.controller.form.parambean.ResultFilters;
import com.myehr.controller.form.parambean.ResultGroups;
import com.myehr.controller.form.parambean.ResultWheres;
import com.myehr.controller.form.parambean.SaveButtonParams;
import com.myehr.controller.form.parambean.SaveFilterColumnDataParams;
import com.myehr.controller.form.parambean.SaveSelectFilterColumnParams;
import com.myehr.controller.login.Login;
import com.myehr.controller.webservice.cxf.erp.CUXOKCBPMSERAPKGPortType_CUXOKCBPMSERAPKGPort_Client;
import com.myehr.controller.webservice.service.FormDataWebservice;
import com.myehr.mapper.activiti.ActReModelMapper;
import com.myehr.mapper.activiti.ActRuTaskMapper;
import com.myehr.mapper.activiti.expand.ActNodePropertiesExpandMapper;
import com.myehr.mapper.entity.SysEntityMapper;
import com.myehr.mapper.field.SysFieldMapper;
import com.myehr.mapper.fileinput.SysFileCheckedMapper;
import com.myehr.mapper.fileinput.SysFileCheckedPointMapper;
import com.myehr.mapper.formmanage.button.ImportField;
import com.myehr.mapper.formmanage.button.SysFormButtonExportColumnMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonExportMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonInterfaceMapper;
import com.myehr.mapper.formmanage.form.FormChartsBarColumnMapper;
import com.myehr.mapper.formmanage.form.SysExecSqlMapper;
import com.myehr.mapper.formmanage.form.SysFileuploadConfigMapper;
import com.myehr.mapper.formmanage.form.SysFormActionMapper;
import com.myehr.mapper.formmanage.form.SysFormButtonMapper;
import com.myehr.mapper.formmanage.form.SysFormCalculateSolutionMapper;
import com.myehr.mapper.formmanage.form.SysFormChartsMapper;
import com.myehr.mapper.formmanage.form.SysFormColumnExpandMapper;
import com.myehr.mapper.formmanage.form.SysFormColumnMapper;
import com.myehr.mapper.formmanage.form.SysFormFolderTreeMapper;
import com.myehr.mapper.formmanage.form.SysFormGeneralParamMapper;
import com.myehr.mapper.formmanage.form.SysFormGroupMapper;
import com.myehr.mapper.formmanage.form.SysFormShowInfoMapper;
import com.myehr.mapper.formmanage.form.SysFormWhereMapper;
import com.myehr.mapper.formmanage.form.SysFormYkReportMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigGridMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigMstTabDetailMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigMstTabMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigTreeMapper;
import com.myehr.mapper.formmanage.form.SysGridFilterColumnMapper;
import com.myehr.mapper.formmanage.form.SysGridFilterMapper;
import com.myehr.mapper.formmanage.form.SysInterfaceSchemeMapper;
import com.myehr.mapper.formmanage.form.SysRuleMapper;
import com.myehr.mapper.formmanage.form.SysSolutionMapper;
import com.myehr.mapper.formmanage.form.SysTreeNodeTypeMapper;
import com.myehr.mapper.formmanage.form.SysTreeSolutionMapper;
import com.myehr.mapper.formmanage.formexpand.SysFormColumnMapperExpand;
import com.myehr.mapper.formmanage.formexpand.SysFormconfigMapperExpand;
import com.myehr.mapper.formmanage.question.SysExamtemplateMapper;
import com.myehr.mapper.formmanage.question.SysExamtemplateQuestionMapper;
import com.myehr.mapper.formmanage.template.timeaxis.SysTimeaxisTemplateColumnMapper;
import com.myehr.mapper.formmanage.widget.SysFileuploadMapper;
import com.myehr.mapper.formmanage.widget.SysFormCheckboxMapper;
import com.myehr.mapper.formmanage.widget.SysFormCheckboxlistMapper;
import com.myehr.mapper.formmanage.widget.SysFormComboboxMapper;
import com.myehr.mapper.formmanage.widget.SysFormDatepickerMapper;
import com.myehr.mapper.formmanage.widget.SysFormFileuploadMapper;
import com.myehr.mapper.formmanage.widget.SysFormLookupMapper;
import com.myehr.mapper.formmanage.widget.SysFormRadiobuttonlistMapper;
import com.myehr.mapper.formmanage.widget.SysFormRichtextMapper;
import com.myehr.mapper.formmanage.widget.SysFormTextareaMapper;
import com.myehr.mapper.formmanage.widget.SysFormTextboxMapper;
import com.myehr.mapper.lang.SysAlarmMapper;
import com.myehr.mapper.process.FreeProcessMapper;
import com.myehr.mapper.sysParam.SysSystemParamMapper;
import com.myehr.mapper.sysUserExpand.SysUserMapperExpand;
import com.myehr.mapper.sysdict.SysDictEntryMapper;
import com.myehr.mapper.sysdict.SysDictTypeMapper;
import com.myehr.mapper.sysuser.SysUserExpandMapper;
import com.myehr.mapper.sysuser.SysUserMapper;
import com.myehr.pojo.TreeModel;
import com.myehr.pojo.cache.SysCacheConfig;
import com.myehr.pojo.dict.SysDictEntry;
import com.myehr.pojo.dict.SysDictEntryExample;
import com.myehr.pojo.activiti.ActReModel;
import com.myehr.pojo.activiti.ActReModelExample;
import com.myehr.pojo.activiti.ActRuTask;
import com.myehr.pojo.activiti.ActRuTaskExample;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpand;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample;
import com.myehr.pojo.dict.DictData;
import com.myehr.pojo.dict.SysDictEntryExpand;
import com.myehr.pojo.dict.SysDictType;
import com.myehr.pojo.dict.SysDictTypeExample;
import com.myehr.pojo.entity.SysEntity;
import com.myehr.pojo.entity.SysEntityExample;
import com.myehr.pojo.field.SysField;
import com.myehr.pojo.field.SysFieldExample;
import com.myehr.pojo.formmanage.button.SysFormButtonExport;
import com.myehr.pojo.formmanage.button.SysFormButtonExportExample;
import com.myehr.pojo.formmanage.button.SysFormButtonInterface;
import com.myehr.pojo.formmanage.button.SysFormButtonInterfaceExample;
import com.myehr.pojo.fileinput.SysFileChecked;
import com.myehr.pojo.fileinput.SysFileCheckedExample;
import com.myehr.pojo.fileinput.SysFileCheckedPoint;
import com.myehr.pojo.fileinput.SysFileCheckedPointExample;
import com.myehr.pojo.formmanage.cache.SysFormButtonCache;
import com.myehr.pojo.formmanage.cache.SysFormButtonCalculateCache;
import com.myehr.pojo.formmanage.cache.SysFormButtonIntrDetailCache;
import com.myehr.pojo.formmanage.cache.SysFormButtonIntroduceCache;
import com.myehr.pojo.formmanage.cache.SysFormColumnCache;
import com.myehr.pojo.formmanage.cache.SysFormComboboxCache;
import com.myehr.pojo.formmanage.cache.SysFormGeneralExecSqlCache;
import com.myehr.pojo.formmanage.cache.SysFormconfigCache;
import com.myehr.pojo.formmanage.cache.SysFormconfigMstTabDetailCache;
import com.myehr.pojo.formmanage.cache.SysRuleCache;
import com.myehr.pojo.formmanage.cache.SysSolutionCache;
import com.myehr.pojo.formmanage.cache.SysSqlParamsCache;
import com.myehr.pojo.formmanage.cache.UserObject;
import com.myehr.pojo.formmanage.form.FormChartsBarColumn;
import com.myehr.pojo.formmanage.form.FormChartsBarColumnExample;
import com.myehr.pojo.formmanage.form.SysExecSql;
import com.myehr.pojo.formmanage.form.SysExecSqlExample;
import com.myehr.pojo.formmanage.form.SysFileuploadConfig;
import com.myehr.pojo.formmanage.form.SysFormAction;
import com.myehr.pojo.formmanage.form.SysFormActionExample;
import com.myehr.pojo.formmanage.form.SysFormButton;
import com.myehr.pojo.formmanage.form.SysFormButtonExample;
import com.myehr.pojo.formmanage.form.SysFormCalculateSolution;
import com.myehr.pojo.formmanage.form.SysFormCalculateSolutionExample;
import com.myehr.pojo.formmanage.form.SysFormCharts;
import com.myehr.pojo.formmanage.form.SysFormChartsExample;
import com.myehr.pojo.formmanage.form.SysFormColumn;
import com.myehr.pojo.formmanage.form.SysFormColumnExample;
import com.myehr.pojo.formmanage.form.SysFormFolderTree;
import com.myehr.pojo.formmanage.form.SysFormFolderTreeExample;
import com.myehr.pojo.formmanage.form.SysFormGeneralParam;
import com.myehr.pojo.formmanage.form.SysFormGeneralParamExample;
import com.myehr.pojo.formmanage.form.SysFormGroup;
import com.myehr.pojo.formmanage.form.SysFormGroupExample;
import com.myehr.pojo.formmanage.form.SysFormShowInfo;
import com.myehr.pojo.formmanage.form.SysFormShowInfoExample;
import com.myehr.pojo.formmanage.form.SysFormShowInfoExpand;
import com.myehr.pojo.formmanage.form.SysFormWhere;
import com.myehr.pojo.formmanage.form.SysFormWhereExample;
import com.myehr.pojo.formmanage.form.SysFormYkReport;
import com.myehr.pojo.formmanage.form.SysFormYkReportExample;
import com.myehr.pojo.formmanage.form.SysFormconfig;
import com.myehr.pojo.formmanage.form.SysFormconfigAndGrid;
import com.myehr.pojo.formmanage.form.SysFormconfigAndGrid2;
import com.myehr.pojo.formmanage.form.SysFormconfigExample;
import com.myehr.pojo.formmanage.form.SysFormconfigGrid;
import com.myehr.pojo.formmanage.form.SysFormconfigGridExample;
import com.myehr.pojo.formmanage.form.SysFormconfigMstTab;
import com.myehr.pojo.formmanage.form.SysFormconfigMstTabDetail;
import com.myehr.pojo.formmanage.form.SysFormconfigMstTabDetailExample;
import com.myehr.pojo.formmanage.form.SysFormconfigMstTabExample;
import com.myehr.pojo.formmanage.form.SysFormconfigTree;
import com.myehr.pojo.formmanage.form.SysFormconfigTreeExample;
import com.myehr.pojo.formmanage.form.SysFormconfigWithBLOBs;
import com.myehr.pojo.formmanage.form.SysGridFilter;
import com.myehr.pojo.formmanage.form.SysGridFilterColumn;
import com.myehr.pojo.formmanage.form.SysGridFilterColumnExample;
import com.myehr.pojo.formmanage.form.SysGridFilterExample;
import com.myehr.pojo.formmanage.form.SysInterfaceScheme;
import com.myehr.pojo.formmanage.form.SysInterfaceSchemeExample;
import com.myehr.pojo.formmanage.form.SysInterfaceSchemeWithBLOBs;
import com.myehr.pojo.formmanage.form.SysRule;
import com.myehr.pojo.formmanage.form.SysRuleExample;
import com.myehr.pojo.formmanage.form.SysSolution;
import com.myehr.pojo.formmanage.form.SysSolutionExample;
import com.myehr.pojo.formmanage.form.SysSqlParams;
import com.myehr.pojo.formmanage.form.SysTreeNodeTypeExample;
import com.myehr.pojo.formmanage.question.SysExamtemplate;
import com.myehr.pojo.formmanage.question.SysExamtemplateExample;
import com.myehr.pojo.formmanage.question.SysExamtemplateQuestion;
import com.myehr.pojo.formmanage.question.SysExamtemplateQuestionExample;
import com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumn;
import com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumnExample;
import com.myehr.pojo.formmanage.widget.SysFileupload;
import com.myehr.pojo.formmanage.widget.SysFormCheckbox;
import com.myehr.pojo.formmanage.widget.SysFormCheckboxExample;
import com.myehr.pojo.formmanage.widget.SysFormCheckboxlist;
import com.myehr.pojo.formmanage.widget.SysFormCheckboxlistExample;
import com.myehr.pojo.formmanage.widget.SysFormCombobox;
import com.myehr.pojo.formmanage.widget.SysFormComboboxExample;
import com.myehr.pojo.formmanage.widget.SysFormDatepicker;
import com.myehr.pojo.formmanage.widget.SysFormDatepickerExample;
import com.myehr.pojo.formmanage.widget.SysFormFileupload;
import com.myehr.pojo.formmanage.widget.SysFormFileuploadExample;
import com.myehr.pojo.formmanage.widget.SysFormLookup;
import com.myehr.pojo.formmanage.widget.SysFormLookupExample;
import com.myehr.pojo.formmanage.widget.SysFormRadiobuttonlist;
import com.myehr.pojo.formmanage.widget.SysFormRadiobuttonlistExample;
import com.myehr.pojo.formmanage.widget.SysFormRichtextExample;
import com.myehr.pojo.formmanage.widget.SysFormTextarea;
import com.myehr.pojo.formmanage.widget.SysFormTextareaExample;
import com.myehr.pojo.formmanage.widget.SysFormTextbox;
import com.myehr.pojo.formmanage.widget.SysFormTextboxExample;
import com.myehr.pojo.lang.SysAlarm;
import com.myehr.pojo.lang.SysAlarmExample;
import com.myehr.pojo.process.FreeProcess;
import com.myehr.pojo.process.FreeProcessExample;
import com.myehr.pojo.sysParam.SysRequestParam;
import com.myehr.pojo.sysParam.SysSystemParam;
import com.myehr.pojo.sysParam.SysSystemParamExample;
import com.myehr.pojo.sysuser.SysUser;
import com.myehr.pojo.sysuser.SysUserExample;
import com.myehr.pojo.sysuser.SysUserLoginLog;
import com.myehr.service.formmanage.form.IFileUploadService;
import com.myehr.service.formmanage.form.IFormService;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.IsysFormColumnService;
import com.myehr.service.formmanage.form.IButtonService;
import com.myehr.service.formmanage.runtime.IRuntimeCardFormService;
import com.myehr.service.formmanage.runtime.IRuntimeGridFormService;
import com.myehr.service.formmanage.runtime.IRuntimeTreeFormService;
import com.myehr.service.formmanage.runtime.IRuntmeButtonService;
import com.myehr.service.impl.formmanage.runtime.RuntimeUtil;
import com.myehr.service.menu.SysMenuService;
import com.myehr.service.primaryKey.PrimaryKeyService;
import com.myehr.service.sysdict.SysDictService;
import com.myehr.service.sysdict.SysDictTypeService;
import com.myehr.test.dao.TMapperExt;

@Controller
@RequestMapping("/form")
public class FormController {
	
	private static Logger logger = LoggerFactory.getLogger(FormController.class);
	
	@Autowired
	private SysFormconfigMapper sysFormconfigMapper;
	
	@Autowired
	private SysInterfaceSchemeMapper sysInterfaceSchemeMapper;
	
	@Autowired
	private SysFormWhereMapper sysFormWhereMapper;
	
	@Autowired
	private SysFormButtonMapper sysFormButtonMapper;
	
	@Autowired
	private SysFormButtonExportMapper sysFormButtonExportMapper;
	
	@Autowired
	private SysFormColumnMapper sysFormColumnMapper;
	
	@Autowired
	private SysTreeSolutionMapper sTreeSolutionMapper;
	
	@Autowired
	private SysSystemParamMapper sysSystemParamMapper;
	
	@Autowired
	private SysGridFilterColumnMapper sysGridFilterColumnMapper;
	
	@Autowired
	private SysFormconfigGridMapper sysFormconfigGridMapper;
	
	@Autowired
	private SysFormChartsMapper sysFormChartsMapper;
	
	@Autowired
	private SysTimeaxisTemplateColumnMapper sysTimeaxisTemplateColumnMapper;
	
	@Autowired
	private SysFormconfigTreeMapper sysFormconfigTreeMapper;
	
	@Autowired
	private SysSolutionMapper sysSolutionMapper;
	
	@Autowired
	private SysRuleMapper sysRuleMapper;
	
	@Autowired
	private SysFormCalculateSolutionMapper sysFormCalculateSolutionMapper;
	
//	@Autowired
	@Resource(name = "IFormService")
	private IFormService formService;
	
	@Autowired
	private ISysformconfigService sysformconfigService;

	@Autowired
	private IsysFormColumnService columnService;
	
	@Autowired
	private SysEntityMapper sEntityMapper;
	
//	@Autowired
	@Resource(name = "TMapperExt")
	private TMapperExt tMapperExt;
	
//	@Autowired
	@Resource(name = "IRuntmeButtonService")
	private IRuntmeButtonService butService;
	
	@Autowired
	private IRuntimeCardFormService runtimeService;
	
//	@Autowired
	@Resource(name = "IRuntimeGridFormService")
	private IRuntimeGridFormService runtimeGridService;
	
//	@Autowired
	@Resource(name = "IRuntimeTreeFormService")
	private IRuntimeTreeFormService runtimeTreeService;
	
	@Autowired 
	private SysMenuService sMService;
	
//	@Autowired
	@Resource(name = "PrimaryKeyService")
	private PrimaryKeyService pkService;
	
	@Autowired 
	private IFileUploadService iFileUploadService;
	
	@Autowired 
	private SysFileuploadMapper sysFileuploadMapper;
	
	@Autowired
	private SysUserMapper sUserMapper;
	
	@Autowired
	private SysUserMapperExpand sUserMapperExpand;
	
	@Autowired
	private SysFormFolderTreeMapper treeMapper;
	
	@Autowired
	private SysFormconfigMstTabDetailMapper tabDetailMapper;

	@Autowired
	private SysFieldMapper sFieldMapper;
	@Autowired
	private SysFormComboboxMapper sysFormComboboxMapper;
//	@Autowired
	@Resource(name = "SysDictTypeService")
	private SysDictTypeService dictTypeServiceImpl;
	@Autowired
	private SysDictService serviceImpl;
	@Autowired
	private SysExecSqlMapper sExecSqlMapper;
	
	@Autowired
	private SysFormconfigMstTabMapper sMstTabMapper;
	@Autowired
	private SysFormconfigMstTabDetailMapper sMstTabDetailMapper;
	@Autowired
	private SysFormGeneralParamMapper sGeneralParamMapper;
	@Autowired
	private SysFormconfigMapperExpand sFormconfigMapperExpand;
	@Autowired
	private SysFormColumnMapper sColumnMapper;
	@Autowired
	private SysFormTextboxMapper sTextboxMapper;
	@Autowired
	private SysFormComboboxMapper sComboboxMapper;
	@Autowired
	private SysFormDatepickerMapper sDatepickerMapper;
	@Autowired
	private SysFormTextareaMapper sTextareaMapper;
	@Autowired
	private SysFormCheckboxMapper sCheckboxMapper;
	@Autowired
	private SysFormCheckboxlistMapper sCheckboxlistMapper;
	@Autowired
	private SysFormRadiobuttonlistMapper sRadiobuttonlistMapper;
	@Autowired
	private SysDictTypeMapper sDictTypeMapper;
	@Autowired
	private SysFormLookupMapper sLookupMapper;
	@Autowired 
	private SysFormFileuploadMapper sFileuploadMapper;
	@Autowired
	private SysFormGroupMapper sGroupMapper;
	@Autowired
	private SysGridFilterMapper sysGridFilterMapper;
	@Autowired
	private SysFormColumnMapperExpand sysFormColumnMapperExpand;
	@Autowired
	private SysAlarmMapper sysAlarmMapper;
	@Autowired
	private SysFormRichtextMapper sRichtextMapper;
	@Autowired SysTreeNodeTypeMapper sNodeTypeMapper;
	@Autowired SysFormYkReportMapper sYkReportMapper;
	
	@Autowired
	private ActRuTaskMapper actRuTaskMapper;
	
	@Autowired
	private ActReModelMapper actReModelMapper;
	
	@Autowired
	private ActNodePropertiesExpandMapper actNodePropertiesExpandMapper;
	
	@Autowired
	private SysDictTypeMapper sysDictTypeMapper;
	
	@Autowired
	private SysDictEntryMapper sysDictEntryMapper;
	
	@Autowired
	private FormChartsBarColumnMapper formChartsBarColumnMapper;
	@Autowired
	private SysExamtemplateQuestionMapper questionMapper;
	@Autowired
	private SysFormColumnExpandMapper sysFormColumnExpandMapper;
	@Autowired
	private IButtonService IButtonService;
	
	@Autowired
	SysFormButtonExportColumnMapper sysFormButtonExportColumnMapper;
	@Autowired
	SysExamtemplateMapper sMapper;
	@Autowired
	SysFileCheckedMapper sFileCheckedMapper;
	@Autowired
	SysFileCheckedPointMapper sfCheckedPointMapper;
	
	@Autowired
	FreeProcessMapper freeProcessMapper;
	@Autowired
	SysFileuploadConfigMapper fileuploadConfigMapper;
	
	@Autowired
	SysFormButtonInterfaceMapper sysFormButtonInterfaceMapper;
	@Autowired
	SysFormActionMapper sActionMapper;
	@Autowired
	SysFormShowInfoMapper showInfoMapper;
	@Autowired
	private SysUserExpandMapper sysUserExpandMapper;
	DataSign datasign = new DataSign();	
	
	@RequestMapping("/menuJson")
	public @ResponseBody String getMenuJson(HttpServletRequest request) throws Exception{
		long start = new Date().getTime();
		String menuJson = "";
		String  userId = request.getParameter("userId");
		try {
			menuJson = sMService.queryMainframeMenu(userId,request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		logger.info((new Date().getTime()-start)+"*************");
		return menuJson;
	}

	@RequestMapping("/menuJsonWithScheme")
	public @ResponseBody String getMenuJsonWithScheme(HttpServletRequest request) throws Exception{
		String menuJson = "";
		HttpSession session = request.getSession();
		String userId = ((BigDecimal) session.getAttribute("userid"))+"";
		String schemeId =request.getParameter("schemeId");
		String isApp = "";
		isApp =request.getParameter("formType");
		try {
			menuJson = sMService.queryMainframeMenuWithScheme(schemeId,userId,request,isApp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return menuJson;
	}	
	
	@RequestMapping("/menuJsonWithSchemex")
	public @ResponseBody String getMenuJsonWithSchemex(HttpServletRequest request) throws Exception{
		String menuJson = "";
		HttpSession session = request.getSession();
		String userId = ((BigDecimal) session.getAttribute("userid"))+"";
		String schemeId =request.getParameter("schemeId");
		String isApp = "";
		isApp =request.getParameter("formType");
		try {
			menuJson = sMService.queryMainframeMenuWithSchemex(schemeId,userId,request,isApp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return menuJson;
	}	
	
		
	@RequestMapping("/getForm")
	public @ResponseBody String getFormconfigCache(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			String formType =request.getParameter("formType");
			if(formId==null){
				return "formId无效";
			}
			SysFormconfigCache aa = sysformconfigService.getForm(formId);
			formService.buildForm(aa, param,formType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/getExcSolutionByFormId")
	public @ResponseBody List<SysSolution> getExcSolutionByFormId(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			String formButtonId =request.getParameter("formButtonId");
			if(formButtonId==null){
				return null;
			}
			String formId =request.getParameter("formId");
			if(formId==null){
				return null;
			}
			
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			SysFormButtonCalculateCache calculate = (SysFormButtonCalculateCache)form.getButton(Long.parseLong(formButtonId)).getButtonExt();

			SysFormCalculateSolutionExample example = new SysFormCalculateSolutionExample();
			com.myehr.pojo.formmanage.form.SysFormCalculateSolutionExample.Criteria criteria = example.createCriteria();
			criteria.andCalculateIdEqualTo(calculate.getCalculateId());
			List<SysFormCalculateSolution> sysFormCalculateSolutions = sysFormCalculateSolutionMapper.selectByExample(example);

			
			List<SysSolution> sysSolutions=new ArrayList<SysSolution>(); 
			for (SysFormCalculateSolution sysFormCalculateSolution : sysFormCalculateSolutions) {
				SysSolutionExample example1 = new SysSolutionExample();
				com.myehr.pojo.formmanage.form.SysSolutionExample.Criteria criteria1 = example1.createCriteria();
				criteria1.andSolutionIdEqualTo(sysFormCalculateSolution.getSolutionId().longValue());
				List<SysSolution> sysSolutions1 = sysSolutionMapper.selectByExample(example1);
				SysSolution sysSolution = null;
				if (sysSolutions1.size()>0) {
					sysSolution = sysSolutions1.get(0);
					sysSolutions.add(sysSolution);
				}
			}
			
			
			return sysSolutions;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查找表单信息
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/findformListByformId")
	public @ResponseBody ResultMap findformListByformId(HttpServletRequest request) throws Exception {
        SysFormconfigMstTabDetailExample example = new SysFormconfigMstTabDetailExample();
        String mstId = request.getParameter("mstId");
        example.or().andMstTabIdEqualTo(new BigDecimal(mstId));
        example.setOrderByClause("MST_TAB_ID");
        List<SysFormconfigMstTabDetail> tabs = tabDetailMapper.selectByExample(example);
        ResultMap resultMap = new ResultMap();
        List<Object> list_obj=new ArrayList<Object>();
		for (SysFormconfigMstTabDetail u : tabs) {
			list_obj.add(u);
		}
		resultMap.setRows(list_obj); 
        return resultMap; 
	}
	
	@RequestMapping("/getSysRuleBySolutionId")
	public @ResponseBody List<SysRule> getSysRuleBySolutionId(HttpServletRequest request) throws Exception{
		try {
			String solutionId =request.getParameter("solutionId");
			if(solutionId==null){
				return null;
			}
			int a = Integer.parseInt(solutionId);
			BigDecimal solutionId1 = new BigDecimal(a);
			SysRuleExample example = new SysRuleExample();
			com.myehr.pojo.formmanage.form.SysRuleExample.Criteria criteria = example.createCriteria();
			criteria.andRuleSolutionIdEqualTo(solutionId1);
			example.setOrderByClause("RULE_SORT");
			List<SysRule> sysRules = sysRuleMapper.selectByExample(example);
			return sysRules;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	@RequestMapping("/checkAndExcSolutionRules")
	public @ResponseBody List<SysRule> checkAndExcSolutionRules(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			String formId =request.getParameter("formId");
			String buttonId =request.getParameter("buttonId");
			String ruleIds =request.getParameter("ruleIds");
			if ("".equals(formId)||formId == null||"".equals(buttonId)||buttonId == null || "".equals(ruleIds)||ruleIds == null ) {
				logger.info("参数传入出错formId:"+formId+" buttonId:"+buttonId+" ruleIds:"+ruleIds);
				return null;
			}
			String[] ruleidsarr = ruleIds.split(",");
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			SysFormButtonCalculateCache calculate = (SysFormButtonCalculateCache)form.getButtonByButtonId(buttonId+"").getButtonExt();
			List<SysRuleCache> rules = new ArrayList<SysRuleCache>();
			List<SysRule> rules1 = new ArrayList<SysRule>();
			List<SysSolutionCache> solutions =  calculate.getSolutions();
			for(int i=0; i<solutions.size(); i++){
				List<SysRuleCache> tl  =  solutions.get(i).getRules();
				for(int k=0; k<tl.size(); k++){
					long tempruleid = tl.get(k).getRuleId();
					for(int j=0; j<ruleidsarr.length; j++){
						if(ruleidsarr[j].equals(tempruleid+"")){
							rules.add(tl.get(k));
							rules1.add(tl.get(k).getPojo());
						}
					}
				}
			}
			
			return rules1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	//getParambyruleId
	@RequestMapping("/getParambyruleId")
	public @ResponseBody Object getParambyruleId(HttpServletRequest request) throws Exception{
		String[] ruleIds =request.getParameter("ruleIds").split(",");
		List<SysFormGeneralParam> params = new ArrayList<SysFormGeneralParam>();
		String strFlag = "";
		for (String string : ruleIds) {
			List<SysFormGeneralParam> params1 = sysformconfigService.getGenParamByRuleId(string);
			for (SysFormGeneralParam sysFormGeneralParam : params1) {
				strFlag += ",";
				if(strFlag.indexOf(","+sysFormGeneralParam.getParamName()+",")==-1){
					params.add(sysFormGeneralParam);
					strFlag += sysFormGeneralParam.getParamName();
				}
			}
		}
		return params;
	}
	
	@RequestMapping("/checkAndExcSolutionParams")
	public @ResponseBody Object checkAndExcSolutionParams(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			/*String formId =request.getParameter("formId");
			String buttonId =request.getParameter("buttonId");
			String ruleIds =request.getParameter("ruleIds");
			if ("".equals(formId)||formId == null||"".equals(buttonId)||buttonId == null || "".equals(ruleIds)||ruleIds == null ) {
				logger.info("参数传入出错formId:"+formId+" buttonId:"+buttonId+" ruleIds:"+ruleIds);
				return null;
			}
			String[] ruleidsarr = ruleIds.split(",");
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			SysFormButtonCalculateCache calculate = (SysFormButtonCalculateCache)form.getButtonByButtonId(buttonId+"").getButtonExt();
			List<SysRuleCache> rules = new ArrayList<SysRuleCache>();
			List<SysRule> rules1 = new ArrayList<SysRule>();
			List<SysSqlParamsCache> params = new ArrayList<SysSqlParamsCache>();
			List<SysSqlParams> params1 = new ArrayList<SysSqlParams>();
			List<SysSolutionCache> solutions =  calculate.getSolutions();

			for(int i=0; i<solutions.size(); i++){
				List<SysRuleCache> tl  =  solutions.get(i).getRules();
				for(int k=0; k<tl.size(); k++){
					long tempruleid = tl.get(k).getRuleId();
					
					for(int j=0; j<ruleidsarr.length; j++){
						if(ruleidsarr[j].equals(tempruleid+"")){
							List<SysSqlParamsCache> params2=tl.get(k).getSysSqlParams();
							for (int l = 0; l < params2.size(); l++) {
								params.add(params2.get(i));
							}
						}
					}
				}
			}
			if (params.size()>0) {
				if (params.size()>1) {
					params1.add(params.get(0).getPojo());
					for (int i = 1; i < params.size()-1; i++) {
						if (params.get(i-1).getPojo().getSqlParamsId().equals(params.get(i).getPojo().getSqlParamsId())) {
							continue;
						}else {
							params1.add(params.get(i+1).getPojo());
						}
					}
				}else {
					params1.add(params.get(0).getPojo());
				}
			}
			return params1;*/

			String[] ruleIds =request.getParameter("ruleIds").split(",");
			int num = 0;
			for (String string : ruleIds) {
				List<SysFormGeneralParam> params = sysformconfigService.getGenParamByRuleId(string);
				if (params!=null) {
					num += params.size();
				} 
			}
			if (num==0) {
				return null;
			} else {
				return ruleIds;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	@RequestMapping("/excRule")
	public @ResponseBody String excRule(HttpServletRequest request, @RequestBody ExcRuleDataParams params) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			String formId = params.getFormId();
			String buttonId = params.getButtonId();
			String ruleIds = params.getRuleIds();
			Map params1 = params.getParams();
			Map oparams = params.getOparams();
			String[] ruleidsarr = ruleIds.split(",");
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			SysFormButtonCalculateCache calculate = (SysFormButtonCalculateCache)form.getButtonByButtonId(buttonId+"").getButtonExt();
			List<SysSolutionCache> solutions =  calculate.getSolutions();
			String errorRuleName = "";
			if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {
				CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
				
			}
			for(int i=0; i<solutions.size(); i++){
				List<SysRuleCache> tl  =  solutions.get(i).getRules();
				for(int k=0; k<tl.size(); k++){
					long tempruleid = tl.get(k).getRuleId();
					for(int j=0; j<ruleidsarr.length; j++){
						if(ruleidsarr[j].equals(tempruleid+"")){ //被勾选中的公式
							String sql = formService.replaceSqlParam(tl.get(k), params1,oparams);
//							String sql = sysformconfigService.getGenParamByRuleId(tl.get(j).getRuleId()+"");
							String[] sqls = sql.split(";");
							try {
								for(String tempsql:sqls){
									logger.info("公式执行sql:"+tempsql);
									String reg = "\\[.+\\.*?\\]";
									Pattern p = Pattern.compile(reg);
							        Matcher m = p.matcher(tempsql);
							        while(m .find()){
							        	String ret = m.group();
							        	String str = ret.substring(ret.indexOf("[")+1,ret.indexOf("]"));
							        	logger.info(params1.get(str)+"");
							        	tempsql =  tempsql.replace(""+ret+"",params1.get(str)+"");
							        }
							        //开始执行
									if (tempsql.toUpperCase().contains("INSERT")) {
										MybatisUtil.insert("runtime.insertSql", tempsql);
									}else if (tempsql.toUpperCase().contains("DELETE")) {//delete
										Map map= new HashMap();
										map.put("deleteSql", tempsql);
										MybatisUtil.delete("runtime.deleteSql", map);
									}else {
										Map map= new HashMap();
										map.put("updateSql", tempsql);
										MybatisUtil.update("runtime.updateSql", map);
									}
									//Batch.goBatch(tempsql);
									//getDASTemplate().queryByNamedSql(DataObject.class,"com.dcf.form.runtime.cardform.excuteRule", tempsql);
								}
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();logger.error(e.getMessage(),e);
								errorRuleName+= "公式为:\""+tl.get(k).getRuleName()+"\" 执行失败!<br/>";
							}
						}
					}
				}
			}
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
			return "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			return "error";
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			return "error";
		}
	}
	
	@RequestMapping("/execButtonExecSql")
	public @ResponseBody String[] execButtonExecSql(HttpServletRequest request, @RequestBody ExecButtonExecSqlDataParams params) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			String formId = params.getFormId();
			String buttonId = params.getButtonId();
			HttpSession session = request.getSession();
			Map params1 = params.getParams();
			Map containerParam = params.getContainerParam();
			if(formId==null){
				return new String[]{"222222","formId无效"};
			}
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			int a = Integer.parseInt(buttonId);
			SysFormButtonCache button = form.getButton(a);
			SysFormGeneralExecSqlCache buttonExecSql = (SysFormGeneralExecSqlCache)button.getButtonExt();
			
			String execSql = buttonExecSql.getExecSql();
			execSql = execSql.trim();
			try {
				if(execSql.indexOf("CALL") == 0 || execSql.indexOf("EXEC") == 0 || execSql.indexOf("call") == 0 || execSql.indexOf("exec") == 0){//存储过程
					String[] recode =RuntimeUtil.execPrepare(buttonExecSql, pkService, params1,MybatisUtil.getConnection(),containerParam);
					if(recode[0].equals("error")){
						return recode;
					}else{
						SysAlarmExample example = new SysAlarmExample();
						com.myehr.pojo.lang.SysAlarmExample.Criteria criteria = example.createCriteria();
						criteria.andIdEqualTo(Integer.valueOf(recode[0]));
						List<SysAlarm> alarms = sysAlarmMapper.selectByExample(example);
						if (alarms.size()>0) {
							recode[1] = alarms.get(0).getCname();
						}else {
							recode[1] = "错误代码"+recode[0]+",未找到对应的错误信息";
						}
						return recode;
					}
				}
				else {//sql
					String sourceType = "sqlserver";
					if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {	
						sourceType = "oracle";
						CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
					}
					RuntimeUtil.execsql(buttonExecSql, pkService, params1,session,sourceType);
				}
			}catch (Exception e) {
				// TODO: handle exception
				throw new DcfException(e.getMessage());
			}
			return new String[]{"000000","操作成功"};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			return new String[]{"error",e.getMessage()};
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			return new String[]{"error",e.getMessage()};
		}
	}

	/*List<SysCacheConfig> configs = form.getButton(Long.valueOf(buttonId)).getCacheConfig();
	if (configs!=null) {
		for (SysCacheConfig sysCacheConfig : configs) {
			sysformconfigService.setCardDictDataByDictTypeCode(sysCacheConfig.getCacheId(), "sql");
		}
	}*/
	
	@RequestMapping("/refreshDictCache")
	public void refreshDictCache(HttpServletRequest request) throws Exception{
		String buttonId = request.getParameter("buttonId");
		String formId = request.getParameter("formId");
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		List<SysCacheConfig> configs = form.getButton(Long.valueOf(buttonId)).getCacheConfig();
		if (configs!=null) {
			for (SysCacheConfig sysCacheConfig : configs) {
				sysformconfigService.setCardDictDataByDictTypeCode(sysCacheConfig.getCacheId(), "sql");
			}
		}
	}
	
	@RequestMapping("/getfile")
	public @ResponseBody ModelAndView getfile(HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		String fileId = request.getParameter("fileId");
		SysFileupload file = iFileUploadService.selectByPrimaryKey(fileId);
		String baseExclePath = "";
		baseExclePath = file.getFilepath();
		String filename = file.getClientfilename();
		String contentType= file.getContenttype();
        mv = new ModelAndView("forward:/jsp/form/button/download.jsp?filename="+filename+"&contentType="+contentType+"&filePath="+baseExclePath);
        return mv;
	}
	
	
	/**
	 * 读取导入列表中文字段
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getImportFileHead")
	public @ResponseBody DcfReponse getImportFileHead(HttpServletRequest request) throws Exception{
		String fileid =request.getParameter("fileid");
		SysFileupload file = sysFileuploadMapper.selectByPrimaryKey(new BigDecimal(fileid));
		String filePath = file.getFilepath();
		//String filePath ="E:/myehr/Myeclipse/importExcel/日结果统计列表.xlsx";
		DcfReponse dcfReponse = butService.getImportFileHead(filePath);
		return dcfReponse;
	}
	/**
	 * 获取导入配置字段
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFormField")
	public @ResponseBody ImportField[] getFormField(HttpServletRequest request) throws Exception{
		ImportField[] importField = null;
		String formId =request.getParameter("formId");
		String userId = request.getSession().getAttribute("userid")+"";
		if (formId == null) {
			return null;
		} else {
			List<SysFormColumn> objs = columnService.queryimportColumns(formId,userId);
			if (objs != null && objs.size() > 0) {
				importField = new ImportField[objs.size()];
				for (int i = 0; i < objs.size(); i++) {
					importField[i] = new ImportField();
					importField[i].setFieldId(objs.get(i).getFormColumnId().longValue());
					importField[i].setFieldTalbename(objs.get(i).getFormFieldTablename());
					importField[i].setFieldLableName(objs.get(i).getFormColumnLable());
				}
			}
			return importField;
		}
	}
	
	/**
	 * 导入
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/importExcelData")
	public @ResponseBody String[] importExcelData(HttpServletRequest request,HttpServletResponse response,@RequestBody ImportExcelData params) throws Exception{
		Map[] fieldSet = params.getFieldSet();
		String formId = params.getFormId();
		String filePath = params.getFilePath();
		
		String clientFileName = params.getClientFileName();
		String repeOper = params.getRepeOper();
		String execptOper = params.getExecptOper();
		String buttonId = params.getButtonId();	
		SysFileupload file = sysFileuploadMapper.selectByPrimaryKey(new BigDecimal(filePath));
		String fileRealPath = file.getFilepath();
		
		SysFormconfig form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId));
		String sourceType = "sqlserver";
		if (form.getFormDefSource()!=null && form.getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		
//		DcfReponse result = butService.importExcelData(formId,fileRealPath,clientFileName,repeOper,execptOper,fieldSet,Long.parseLong(buttonId),request,sourceType);
		String[] result = butService.importExcelDatax(formId,fileRealPath,clientFileName,repeOper,execptOper,fieldSet,Long.parseLong(buttonId),request,sourceType,response);
		CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
		return result;
	}
	
	@RequestMapping("/uploadData")
	public @ResponseBody SysFileupload uploadData(HttpServletRequest request) throws Exception{
		return formService.uploadData(request);
	}
	
	@RequestMapping("/buildForm")
	public @ResponseBody String buildForm(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		System.out.println("jsp消耗时间*****:"+(new Date().getTime()-new Date().getTime())/1000);
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return "formId无效";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/createAppVue")
	public @ResponseBody String[]  createAppVue(HttpServletRequest request) throws Exception{
		String[] reCode = {"success","ok"};
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				reCode[0] = "fail";
				reCode[1] = "表单主键为空";
				return null;
			}
			SysFormconfigCache formCache  = sysformconfigService.getForm(formId);
			if(formService.isCreateForm(formCache)){
				if(formCache.getFormDefLayoutType().equals("1")){
					formService.createCardVue(formCache,request);
				}else if(formCache.getFormDefLayoutType().equals("2")) {
					formService.createGridVue(formCache,request);
				}else if(formCache.getFormDefLayoutType().equals("5")) {
					String isProcess = formCache.getPojoform().getFormDefIsProcess();
					String type = formCache.getPojoform().getExpandField();
					if(isProcess!=null&&isProcess.equals("Y")){//普通流程
						if (type!=null&&type.equals("CARDTOCARD")) {
							formService.createCCYFormVue(formCache,request);
						}
					}else if(isProcess!=null&&isProcess.equals("S")){//自由流程
						if (type!=null&&type.equals("CARDTOCARD")) {
							formService.createCCSFormVue(formCache,request);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e.getMessage());
		}
		return reCode;
	}
	
	@RequestMapping("/toForm")
	public @ResponseBody ModelAndView toForm(HttpServletRequest request) throws Exception{
		
		long start = new Date().getTime();
		
		ModelAndView mv = null;
		String userId = request.getSession().getAttribute("userid")+"";
		DataSign datasign = new DataSign();
		
		if(datasign.OutData(request)!="111"){
			return new ModelAndView("error1");
		}		
		String taskId = request.getParameter("taskId");//流程任务ID
		String flowAction = request.getParameter("flowAction");//流程任务ID
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return null;
			}
			String formType =request.getParameter("formType");
			if (formType!=null) {
			} else {
				formType = "PC";
			}
			Map<String, String> activitiparam = new HashMap<String, String>();
			String procInsId =request.getParameter("procInsId");
//			ActNodePropertiesExpand actNodePropertiesExpand = new ActNodePropertiesExpand();
			ActNodePropertiesExpand actNodePropertiesExpand = null;
			if (procInsId!=null) {
				ActRuTaskExample taskExample = new ActRuTaskExample();
				com.myehr.pojo.activiti.ActRuTaskExample.Criteria taskCriteria = taskExample.createCriteria();
				taskCriteria.andProcInstIdEqualTo(procInsId);
				if (actRuTaskMapper.selectByExample(taskExample).size()>0) {
					ActRuTask actRuTask = actRuTaskMapper.selectByExample(taskExample).get(0);
					String procDefId = (String)actRuTask.getProcDefId();
					String modelKey =  procDefId.split(":")[0];
					ActReModel actReModel = sysformconfigService.getActModelByKey(modelKey);
					ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
					nodePropertiesExpandExample.or().andActModelIdEqualTo(""+actReModel.getId()+"").andActNodeNameEqualTo(""+actRuTask.getName()+"");
					actNodePropertiesExpand = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample).get(0);
				}
			}
			SysFormconfigCache aa  = sysformconfigService.getForm(formId);
			String sb = "";
			String statue = "";
			if(aa!=null){
				sb = aa.getFormSubmitUrl();
				if(aa.getFormSubmitStatus()==null){
					statue = "";
				}else {
					statue = aa.getFormSubmitStatus();
				}
			}
			logger.info("**********************************标签111********"+(new Date().getTime()-start)/1000+"S******************************************");
			
			//sb =  null;
			
			String checkPath = param.getPath()+sb;
			
			logger.info("++++++sb:"+sb+";statue:"+statue+";checkPath:"+checkPath+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			if (sb==null||statue.equals("")||statue.equals("N") || !new File(checkPath).exists()) {
				if(aa.getFormDefLayoutType().equals("1")){
					formService.buildCardForm(aa,param,formType,actNodePropertiesExpand,userId);
					
				}else if (aa.getFormDefLayoutType().equals("2")||aa.getFormDefLayoutType().equals("10")) {
					SysFormconfigGrid formGrid =  aa.getGrid().getPojo();//
					if(aa.getExpandField()==null||aa.getExpandField().equals("")){
						if (formGrid.getFormTableIsHandsontable()!=null) {
							if (formGrid.getFormTableIsHandsontable().equals("true")) {
								formService.buildHandsontableForm(aa,param,formType);
							}else {
								formService.buildForm(aa,param,formType);
							}
						}else {
							if (formGrid.getFormTableIsHandsontable()!=null) {
								formService.buildHandsontableForm(aa,param,formType);
							}else{
								formService.buildForm(aa,param,formType);
							}
						}
					}else if(aa.getExpandField().equals("GRIDBYCARD")){
						formService.buildFormByTemplate(aa,param,formType);
					}
				}else if (aa.getFormDefLayoutType().equals("9")) {
					SysFormChartsExample example = new SysFormChartsExample();
					com.myehr.pojo.formmanage.form.SysFormChartsExample.Criteria criteria = example.createCriteria();
					criteria.andFormChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
					SysFormCharts charts = sysFormChartsMapper.selectByExample(example).get(0);
					String selectformId = charts.getFormChartsSelectFormId()+"";
					
					if (charts.getFormChartsType().equals("bar") || charts.getFormChartsType().equals("line")) {
						FormChartsBarColumnExample barExample2 = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria2 = barExample2.createCriteria();
						barCriteria2.andChartsBarColumnTypeEqualTo("4");
						barCriteria2.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns2 = formChartsBarColumnMapper.selectByExample(barExample2);
						
						FormChartsBarColumnExample barExample = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria = barExample.createCriteria();
						barCriteria.andChartsBarColumnTypeEqualTo("2");
						barCriteria.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(barExample);
						
						FormChartsBarColumnExample barExample1 = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria1 = barExample1.createCriteria();
						barCriteria1.andChartsBarColumnTypeEqualTo("1");
						barCriteria1.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns1 = formChartsBarColumnMapper.selectByExample(barExample1);
						
						SysFormconfigCache selectForm = sysformconfigService.getForm(selectformId);
						
						Map map = new HashMap();
						String columns = barColumns1.get(0).getSelectFormColumnTable().replace("_", "")+"_"+barColumns1.get(0).getSelectFormColumnName();
						for (int i = 0; i < barColumns.size(); i++) {
							String sql = selectForm.getFormDefSql();
							
							String groupColumn = barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
							String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
							
							String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
							
							String columns1 =columns+","+barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
							sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
							
							List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
							List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
							List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
							map.put("columnDatas"+i, groupDataList);
							map.put("groupColumn"+i, groupDataColumn);
							map.put("xColumn"+i, xDataColumn);
						}
						for (int i = 0; i < barColumns2.size(); i++) {
							String sql = selectForm.getFormDefSql();
							
							String groupColumn = barColumns2.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns2.get(i).getSelectFormColumnName();
							String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
							
							String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
							
							String columns1 =columns+","+barColumns2.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns2.get(i).getSelectFormColumnName();
							sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
							
							List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
							List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
							List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
							map.put("columnDatas"+i, groupDataList);
							map.put("groupColumn"+i, groupDataColumn);
							map.put("xColumn"+i, xDataColumn);
						}	
						formService.buildChartsForm(aa,sysformconfigService.getForm(selectformId),param,formType,charts,barColumns,barColumns1,barColumns2,map);
					}else if(charts.getFormChartsType().equals("pie")){
						
						FormChartsBarColumnExample barExample2 = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria2 = barExample2.createCriteria();
						barCriteria2.andChartsBarColumnTypeEqualTo("4");
						barCriteria2.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns2 = formChartsBarColumnMapper.selectByExample(barExample2);
						
						FormChartsBarColumnExample barExample = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria = barExample.createCriteria();
						barCriteria.andChartsBarColumnTypeEqualTo("3");
						barCriteria.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(barExample);
						
						FormChartsBarColumnExample barExample1 = new FormChartsBarColumnExample();
						com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria1 = barExample1.createCriteria();
						barCriteria1.andChartsBarColumnTypeEqualTo("1");
						barCriteria1.andChartsFormIdEqualTo(aa.getPojoform().getFormDefId());
						List<FormChartsBarColumn> barColumns1 = formChartsBarColumnMapper.selectByExample(barExample1);
						
						SysFormconfigCache selectForm = sysformconfigService.getForm(selectformId);
						
						Map map = new HashMap();
						String columns = barColumns1.get(0).getSelectFormColumnTable().replace("_", "")+"_"+barColumns1.get(0).getSelectFormColumnName();
						for (int i = 0; i < barColumns.size(); i++) {
							String sql = selectForm.getFormDefSql();
							
							String groupColumn = barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
							String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
							
							String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
							
							String columns1 =columns+","+barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
							sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
							
							List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
							List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
							List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
							map.put("columnDatas"+i, groupDataList);
							map.put("groupColumn"+i, groupDataColumn);
							map.put("xColumn"+i, xDataColumn);
						}
						
						formService.buildChartsForm(aa,sysformconfigService.getForm(selectformId),param,formType,charts,barColumns,barColumns1,barColumns2,map);
					}	
				}else if (aa.getFormDefLayoutType().equals("3")) {
					SysFormconfigTreeExample example = new SysFormconfigTreeExample();
					com.myehr.pojo.formmanage.form.SysFormconfigTreeExample.Criteria criteria = example.createCriteria();
					criteria.andFormTreeFormIdEqualTo(aa.getPojoform().getFormDefId());
					SysFormconfigTree formTree = sysFormconfigTreeMapper.selectByExample(example).get(0);
					if (formTree.getFormShowTreeOnly().equals("true")) {
						formService.buildTreeOrgChart(aa,param,"",formTree);
					}else {
						formService.buildTreeForm(aa,param);
					}
				}else if (aa.getFormDefLayoutType().equals("5")) {
					if(aa.getExpandField()==null||aa.getExpandField().equals("")||aa.getExpandField().equals("GRIDTOGRID")){
						formService.buildGridAndGridForm(aa,param,"");//列列主从
					}else if (aa.getExpandField()!=null&&aa.getExpandField().equals("TABS")) {
						formService.buildFullTabForm(aa,param,"");//多tab
					}else if(aa.getExpandField().equals("CARDTOGRID")){
						if (procInsId!=null) {
							formService.buildCardAndGridForm(aa,param,"",actNodePropertiesExpand);//卡列主从
						}else{
							formService.buildCardAndGridForm(aa,param,"",null);//卡列主从
						}
						SysFormconfigCache formCache = aa;
						if(formCache.getFormDefIsProcess()!=null&&formCache.getFormDefIsProcess().equals("S")){//自由流程关联表单
							if(flowAction!=null&&flowAction.equals("start")){
								formService.buildFreeActForm_Grid(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}else {
								formService.buildFreeActForm2_Grid(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}
						}else{
							if (formCache.getPojoform().getFormDefIsProcess()!=null&&formCache.getPojoform().getFormDefIsProcess().equals("Y")&&taskId!=null&&!taskId.equals("")) {
								//关联流程卡卡主从表单
								ActRuTask task = actRuTaskMapper.selectByPrimaryKey(taskId);
								ActReModelExample example = new ActReModelExample();
								example.or().andKeyEqualTo((task.getProcDefId()+"").split(":")[0]);
								ActReModel model = actReModelMapper.selectByExample(example).get(0);
								ActNodePropertiesExpandExample example2 = new ActNodePropertiesExpandExample();
								example2.or().andActNodeKeyEqualTo(task.getTaskDefKey()+"").andActModelIdEqualTo(model.getId()+"");
								ActNodePropertiesExpand node = actNodePropertiesExpandMapper.selectByExample(example2).get(0);
								//普通流程卡列审批表单
								formService.buildCardAndNewGridForm2_ACT(formCache,param,"CARDANDCARD",actNodePropertiesExpand,node.getActModelId()+"_"+node.getActNodeKey(),taskId,formType);//卡卡主从
							}else if(flowAction!=null&&flowAction.equals("start")){
								////普通流程卡列发起表单
								formService.buildCardAndGridForm_ACT(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}
						}
					}else if(aa.getExpandField().equals("CARDTOCARD")){
						SysFormconfigCache formCache = aa;
						if(formCache.getFormDefIsProcess()!=null&&formCache.getFormDefIsProcess().equals("S")){//自由流程关联表单
							if(flowAction!=null&&flowAction.equals("start")){
								formService.buildFreeActForm(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}else {
								formService.buildFreeActForm2(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}
						}else{
							if (formCache.getPojoform().getFormDefIsProcess()!=null&&formCache.getPojoform().getFormDefIsProcess().equals("Y")&&taskId!=null&&!taskId.equals("")) {
								//关联流程卡卡主从表单
								ActRuTask task = actRuTaskMapper.selectByPrimaryKey(taskId);
								ActReModelExample example = new ActReModelExample();
								example.or().andKeyEqualTo((task.getProcDefId()+"").split(":")[0]);
								ActReModel model = actReModelMapper.selectByExample(example).get(0);
								ActNodePropertiesExpandExample example2 = new ActNodePropertiesExpandExample();
								example2.or().andActNodeKeyEqualTo(task.getTaskDefKey()+"").andActModelIdEqualTo(model.getId()+"");
								ActNodePropertiesExpand node = actNodePropertiesExpandMapper.selectByExample(example2).get(0);
								formService.buildCardAndCardForm2_ACT(formCache,param,"CARDANDCARD",actNodePropertiesExpand,node.getActModelId()+"_"+node.getActNodeKey(),taskId,formType);//卡卡主从
							}else if(flowAction!=null&&flowAction.equals("start")){
								formService.buildCardAndCardForm2_ACT(formCache,param,"CARDANDCARD",null,null,null,formType);//卡卡主从
							}else {
								formService.buildCardAndCardForm2(formCache,param,"CARDANDCARD",actNodePropertiesExpand,formType);//卡卡主从
							}
						}
					}
				}else if (aa.getFormDefLayoutType().equals("7")) {
					formService.buildTabsForm(aa,param,"");//多tab
				}else if (aa.getFormDefLayoutType().equals("8") || aa.getFormDefLayoutType().equals("8")) {
					formService.buildReportForm(aa,param);
				}else if(aa.getFormDefLayoutType().equals("11")){
					SysTimeaxisTemplateColumnExample example = new SysTimeaxisTemplateColumnExample();
					com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumnExample.Criteria criteria = example.createCriteria();
					criteria.andTemplateColumnFormDefIdEqualTo(aa.getPojoform().getFormDefId().intValue());
					List<SysTimeaxisTemplateColumn> templateColumns = sysTimeaxisTemplateColumnMapper.selectByExample(example);
					
					formService.buildTimeaxisForm(aa,param,formType,templateColumns);
				}else if(aa.getFormDefLayoutType().equals("12")){
					formService.buildDragForm(sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId)),param);
				}else if(aa.getFormDefLayoutType().equals("13")){
					formService.buildQuestionForm(aa,param);
				}else if(aa.getFormDefLayoutType().equals("16")){
					formService.buildNewChartForm(aa,param);
				}
				sb = aa.getFormSubmitUrl();
			}
			logger.info("**********************************标签2222222222******"+(new Date().getTime()-start)+"mS******************************************");
			//表单加载行为记录 
			/*SysFormActionExample eActionExample = new SysFormActionExample();
			eActionExample.or().andObjTypeEqualTo("FORM").andObjIdIsNotNull().andObjIdEqualTo(new BigDecimal(formId));
			List<SysFormAction> formActions = sActionMapper.selectByExample(eActionExample);
			if (formActions.size()>0) {
				SysFormAction action = formActions.get(0);
				action.setAccessTimes(new BigDecimal(Integer.valueOf(action.getAccessTimes()+"")+1));
				sActionMapper.updateByPrimaryKey(action);
			}else {
				SysFormAction action = new SysFormAction();
				action.setAccessName(userId);
				action.setAccessTime(new Date());
				action.setAccessTimes(new BigDecimal("1"));
				action.setObjId(new BigDecimal(formId));
				action.setObjType("FORM");
			}*/
			logger.info("**********************************标签33333333333*******"+(new Date().getTime()-start)/1000+"S******************************************");
			logger.info(sb);
			
			mv = new ModelAndView("forward:/jsp/formbuild/"+sb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		logger.info("**********************************"+(new Date().getTime()-start)/1000+"S******************************************");
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		logger.info("********************************************************************************");
		return mv;
	}
	
	@RequestMapping("/toFormAll")
	public @ResponseBody String toFormAll(HttpServletRequest request) throws Exception{	
		SysFormconfigExample example1 = new SysFormconfigExample();
		List<SysFormconfig> forms =  sysFormconfigMapper.selectByExample(example1);
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		for (int j = 0; j < forms.size(); j++) {
			try {
				String formId =forms.get(j).getFormDefId()+"";
				
				String formType =request.getParameter("formType");
				if (formType!=null) {
					
				} else {
					formType = "PC";
				}
				
				//Map<String, String> activitiparam = new HashMap<String, String>();
				//String taskId =request.getParameter("taskId");
				
				String procInsId =request.getParameter("procInsId");
				ActNodePropertiesExpand actNodePropertiesExpand = null;
				if (procInsId!=null) {
					ActRuTaskExample taskExample = new ActRuTaskExample();
					com.myehr.pojo.activiti.ActRuTaskExample.Criteria taskCriteria = taskExample.createCriteria();
					taskCriteria.andProcInstIdEqualTo(procInsId);
					ActRuTask actRuTask = actRuTaskMapper.selectByExample(taskExample).get(0);
					String procDefId = (String)actRuTask.getProcDefId();
					String modelKey =  procDefId.split(":")[0];
					
					ActReModelExample modelExample = new ActReModelExample();
					com.myehr.pojo.activiti.ActReModelExample.Criteria modelCriteria = modelExample.createCriteria();
					modelCriteria.andKeyEqualTo(modelKey);
					ActReModel actReModel = actReModelMapper.selectByExample(modelExample).get(0);
					
					ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
					com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample.Criteria nodePropertiesExpandCriteria = nodePropertiesExpandExample.createCriteria();
					nodePropertiesExpandCriteria.andActModelIdEqualTo(""+actReModel.getId()+"");
					nodePropertiesExpandCriteria.andActNodeNameEqualTo("\""+actRuTask.getName()+"\"");
					actNodePropertiesExpand = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample).get(0);
				}
				
				SysFormconfig aa = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId));
				String sb = "";
				String statue = "";
				if(aa!=null){
					sb = aa.getFormSubmitUrl();
					if(aa.getFormSubmitStatus()==null){
						statue = "";
					}else {
						statue = aa.getFormSubmitStatus();
					}
				}
				if (sb==null||statue.equals("")||statue.equals("N")) {
					if(aa.getFormDefLayoutType().equals("1")){
						formService.buildCardForm(sysformconfigService.getForm(formId),param,formType,actNodePropertiesExpand,userId);
					}else if (aa.getFormDefLayoutType().equals("2")||aa.getFormDefLayoutType().equals("10")) {
						SysFormconfigGridExample example = new SysFormconfigGridExample();
						com.myehr.pojo.formmanage.form.SysFormconfigGridExample.Criteria criteria = example.createCriteria();
						criteria.andFormGridFromIdEqualTo(aa.getFormDefId());
						SysFormconfigGrid formGrid = sysFormconfigGridMapper.selectByExample(example).get(0);
						if(aa.getExpandField()==null||aa.getExpandField().equals("")){
							if (formGrid.getFormTableIsHandsontable()!=null) {
								if (formGrid.getFormTableIsHandsontable().equals("true")) {
									formService.buildHandsontableForm(sysformconfigService.getForm(formId),param,formType);
								}else {
									formService.buildForm(sysformconfigService.getForm(formId),param,formType);
								}
							}else {
								if (formGrid.getFormTableIsHandsontable()!=null) {
									formService.buildHandsontableForm(sysformconfigService.getForm(formId),param,formType);
								}else{
									formService.buildForm(sysformconfigService.getForm(formId),param,formType);
								}
							}
						}else if(aa.getExpandField().equals("GRIDBYCARD")){
							formService.buildFormByTemplate(sysformconfigService.getForm(formId),param,formType);
						}
					}else if (aa.getFormDefLayoutType().equals("9")) {
						SysFormChartsExample example = new SysFormChartsExample();
						com.myehr.pojo.formmanage.form.SysFormChartsExample.Criteria criteria = example.createCriteria();
						criteria.andFormChartsFormIdEqualTo(aa.getFormDefId());
						SysFormCharts charts = sysFormChartsMapper.selectByExample(example).get(0);
						String selectformId = charts.getFormChartsSelectFormId()+"";
						
						if (charts.getFormChartsType().equals("bar") || charts.getFormChartsType().equals("line")) {
							
							FormChartsBarColumnExample barExample2 = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria2 = barExample2.createCriteria();
							barCriteria2.andChartsBarColumnTypeEqualTo("4");
							barCriteria2.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns2 = formChartsBarColumnMapper.selectByExample(barExample2);
							
							FormChartsBarColumnExample barExample = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria = barExample.createCriteria();
							barCriteria.andChartsBarColumnTypeEqualTo("2");
							barCriteria.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(barExample);
							
							FormChartsBarColumnExample barExample1 = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria1 = barExample1.createCriteria();
							barCriteria1.andChartsBarColumnTypeEqualTo("1");
							barCriteria1.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns1 = formChartsBarColumnMapper.selectByExample(barExample1);
							
							SysFormconfigCache selectForm = sysformconfigService.getForm(selectformId);
							
							Map map = new HashMap();
							String columns = barColumns1.get(0).getSelectFormColumnTable().replace("_", "")+"_"+barColumns1.get(0).getSelectFormColumnName();
							for (int i = 0; i < barColumns.size(); i++) {
								String sql = selectForm.getFormDefSql();
								
								String groupColumn = barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
								String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
								
								String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
								
								String columns1 =columns+","+barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
								sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
								
								List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
								List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
								List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
								map.put("columnDatas"+i, groupDataList);
								map.put("groupColumn"+i, groupDataColumn);
								map.put("xColumn"+i, xDataColumn);
							}
							for (int i = 0; i < barColumns2.size(); i++) {
								String sql = selectForm.getFormDefSql();
								
								String groupColumn = barColumns2.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns2.get(i).getSelectFormColumnName();
								String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
								
								String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
								
								String columns1 =columns+","+barColumns2.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns2.get(i).getSelectFormColumnName();
								sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
								
								List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
								List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
								List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
								map.put("columnDatas"+i, groupDataList);
								map.put("groupColumn"+i, groupDataColumn);
								map.put("xColumn"+i, xDataColumn);
							}
								
							formService.buildChartsForm(sysformconfigService.getForm(formId),sysformconfigService.getForm(selectformId),param,formType,charts,barColumns,barColumns1,barColumns2,map);
						}else if(charts.getFormChartsType().equals("pie")){
							
							FormChartsBarColumnExample barExample2 = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria2 = barExample2.createCriteria();
							barCriteria2.andChartsBarColumnTypeEqualTo("4");
							barCriteria2.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns2 = formChartsBarColumnMapper.selectByExample(barExample2);
							
							FormChartsBarColumnExample barExample = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria = barExample.createCriteria();
							barCriteria.andChartsBarColumnTypeEqualTo("3");
							barCriteria.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(barExample);
							
							FormChartsBarColumnExample barExample1 = new FormChartsBarColumnExample();
							com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria barCriteria1 = barExample1.createCriteria();
							barCriteria1.andChartsBarColumnTypeEqualTo("1");
							barCriteria1.andChartsFormIdEqualTo(aa.getFormDefId());
							List<FormChartsBarColumn> barColumns1 = formChartsBarColumnMapper.selectByExample(barExample1);
							
							SysFormconfigCache selectForm = sysformconfigService.getForm(selectformId);
							
							Map map = new HashMap();
							String columns = barColumns1.get(0).getSelectFormColumnTable().replace("_", "")+"_"+barColumns1.get(0).getSelectFormColumnName();
							for (int i = 0; i < barColumns.size(); i++) {
								String sql = selectForm.getFormDefSql();
								
								String groupColumn = barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
								String sql1 = "select "+groupColumn+",count(*) as sum from ("+sql+")o group by "+groupColumn;
								
								String sql2 = "select "+columns+",count(*) as sum from ("+sql+")o group by "+columns;
								
								String columns1 =columns+","+barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
								sql = "select "+columns1+",count(*) as sum from ("+sql+")o group by "+columns1;
								
								List<Map> groupDataList = tMapperExt.queryByFormDefSql(sql);
								List<Map> groupDataColumn = tMapperExt.queryByFormDefSql(sql1);
								List<Map> xDataColumn = tMapperExt.queryByFormDefSql(sql2);
								map.put("columnDatas"+i, groupDataList);
								map.put("groupColumn"+i, groupDataColumn);
								map.put("xColumn"+i, xDataColumn);
							}
							
							formService.buildChartsForm(sysformconfigService.getForm(formId),sysformconfigService.getForm(selectformId),param,formType,charts,barColumns,barColumns1,barColumns2,map);
						}	
					}else if (aa.getFormDefLayoutType().equals("3")) {
						SysFormconfigTreeExample example = new SysFormconfigTreeExample();
						com.myehr.pojo.formmanage.form.SysFormconfigTreeExample.Criteria criteria = example.createCriteria();
						criteria.andFormTreeFormIdEqualTo(aa.getFormDefId());
						SysFormconfigTree formTree = sysFormconfigTreeMapper.selectByExample(example).get(0);
						if (formTree.getFormShowTreeOnly().equals("true")) {
							formService.buildTreeOrgChart(sysformconfigService.getForm(formId),param,"",formTree);
						}else {
							formService.buildTreeForm(sysformconfigService.getForm(formId),param);
						}
					}else if (aa.getFormDefLayoutType().equals("5")) {
						if(aa.getExpandField()==null){
							formService.buildGridAndGridForm(sysformconfigService.getForm(formId),param,"");//列列主从
						}else if (aa.getExpandField()!=null&&aa.getExpandField().equals("CARD_CARD_TAB")) {
							//formService.buildCardAndCardForm(sysformconfigService.getForm(formId),request,"");//卡列主从
						}else if (aa.getExpandField()!=null&&aa.getExpandField().equals("CARD_GRID_TAB")) {
							if (procInsId!=null) {
								formService.buildCardAndGridForm(sysformconfigService.getForm(formId),param,"",actNodePropertiesExpand);//卡列主从
							}else{
								formService.buildCardAndGridForm(sysformconfigService.getForm(formId),param,"",null);//卡列主从
							}
						}else if (aa.getExpandField()!=null&&aa.getExpandField().equals("TABS")) {
							formService.buildFullTabForm(sysformconfigService.getForm(formId),param,"");//多tab
						}
						formService.buildGridAndGridForm(sysformconfigService.getForm(formId),param,"");//列列主从
//						formService.buildCardAndGridForm(sysformconfigService.getForm(formId),request);//卡列主从
//						formService.buildFullTabForm(sysformconfigService.getForm(formId),request);//多tab
						if(aa.getExpandField()==null||aa.getExpandField().equals("")||aa.getExpandField().equals("GRIDTOGRID")){
							formService.buildGridAndGridForm(sysformconfigService.getForm(formId),param,"");//列列主从
						}else if(aa.getExpandField().equals("CARDTOGRID")){
							if (procInsId!=null) {
								formService.buildCardAndGridForm(sysformconfigService.getForm(formId),param,"",actNodePropertiesExpand);//卡列主从
							}else{
								formService.buildCardAndGridForm(sysformconfigService.getForm(formId),param,"",null);//卡列主从
							}
						}
					}else if (aa.getFormDefLayoutType().equals("7")) {
						formService.buildTabsForm(sysformconfigService.getForm(formId),param,"");//多tab
					}else if (aa.getFormDefLayoutType().equals("8") || aa.getFormDefLayoutType().equals("8")) {
						formService.buildReportForm(sysformconfigService.getForm(formId),param);
					}else if(aa.getFormDefLayoutType().equals("11")){
						SysTimeaxisTemplateColumnExample example = new SysTimeaxisTemplateColumnExample();
						com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumnExample.Criteria criteria = example.createCriteria();
						criteria.andTemplateColumnFormDefIdEqualTo(aa.getFormDefId().intValue());
						List<SysTimeaxisTemplateColumn> templateColumns = sysTimeaxisTemplateColumnMapper.selectByExample(example);
						
						formService.buildTimeaxisForm(sysformconfigService.getForm(formId),param,formType,templateColumns);
					}else if(aa.getFormDefLayoutType().equals("12")){
						formService.buildDragForm(sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId)),param);
					}
					sb = aa.getFormSubmitUrl();
				}
				logger.info(sb);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
				logger.info(e+"");
			}
		}
		
		return "true";
	}
	
	@RequestMapping("/buildCardForm")
	public @ResponseBody String buildCardForm(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return "formId无效";
			}
			String sb = formService.buildCardForm(sysformconfigService.getForm(formId),param,"",null,userId);
//			String sb = formService.buildCardForm(sysformconfigService.getForm(formId),request);
//			String sb = formService.buildTreeForm(sysformconfigService.getForm(formId),request);
			logger.info(sb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/buildTreeForm")
	public @ResponseBody String buildTreeForm(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return "formId无效";
			}
			String sb = formService.buildTreeForm(sysformconfigService.getForm(formId),param);
			logger.info(sb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/buildCardAndGridForm")
	public @ResponseBody String buildCardAndGridForm(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return "formId无效";
			}
			String sb = formService.buildCardAndGridForm(sysformconfigService.getForm(formId),param,"",null);
			logger.info(sb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/buildGridAndGridForm")
	public @ResponseBody String buildGridAndGridForm(HttpServletRequest request) throws Exception{
		String userId = request.getSession().getAttribute("userid")+"";
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			String formId =request.getParameter("formId");
			if(formId==null){
				return "formId无效";
			}

			String sb = formService.buildGridAndGridForm(sysformconfigService.getForm(formId),param,"");

			logger.info(sb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return "";
	}
	
	@RequestMapping("/findSysDictEntryList")
	public @ResponseBody List<SysFormconfigWithBLOBs> findSysDictEntryById(HttpServletRequest request) throws Exception{
		SysFormconfigWithBLOBs sExample=(SysFormconfigWithBLOBs) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SysFormconfigWithBLOBs.class);
		logger.info(sExample.getFormDefEntitySql());
		SysFormconfigExample example = new SysFormconfigExample();
		List<SysFormconfigWithBLOBs> list= sysFormconfigMapper.selectByExampleWithBLOBs(example);
		return list;
	}
	
	@RequestMapping("/findDataByformDefSql")
	public @ResponseBody Map findDataByformDefSql(HttpServletRequest request) throws Exception{
		String formId =request.getParameter("formId");
		int a = Integer.parseInt(formId);
		SysFormconfigWithBLOBs formConfigWithBLOBs =  formService.findSysFormconfigWithBLOBsByFormId(a);
		String formDefSql = formConfigWithBLOBs.getFormDefSql();
		List<Map> t = (List<Map>) tMapperExt.queryByFormDefSql(formDefSql);
		Map r = new HashMap();
		r.put("total", t.size());
		r.put("rows", t);
		return r;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveButtonSave")
	public @ResponseBody String[] saveButtonSave(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
		String[] recode = {"success","成功"};
		String formId = params.getFormId();
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		String sourceType = "sqlserver";
		if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		try {
			if(datasign.OutData2(request, params).equalsIgnoreCase("000")){
				recode[0] = "error";
				recode[1]="保存失败";
			}else{			
				if(params!=null){
	//				 return recode;	
					 recode  =  butService.cardFormSaveButtonSave(params.getParam(),form, params.getParamsMap(), params.getButtonId(), request,"save",sourceType);
					 CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			recode[0] = "error";
			recode[1] = "未知异常";
		}
		if("error".equals(recode[0])) {
			if(!StringUtils.isNotNullAndBlank(recode[1])) {
				recode[1] = "保存失败";
			}
		}
		return recode;	
	} 
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/batchSaveButtonSave")
	public @ResponseBody String[] batchSaveButtonSave(HttpServletRequest request, @RequestBody List<SaveButtonParams> paramsList) throws Exception{
		//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
		String[] recode = {"success","成功"};
		String formId = request.getParameter("formId");
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		String sourceType = "sqlserver";
		if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		for (SaveButtonParams params : paramsList) {
			try {
				if(params!=null){
//					 return recode;	
					 butService.cardFormSaveButtonSave(params.getParam(),form, params.getParamsMap(), params.getButtonId(), request,"save",sourceType);
					 CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
				recode[0] = "error";
				recode[1] = "未知异常";
			}
			if("error".equals(recode[0])) {
				if(!StringUtils.isNotNullAndBlank(recode[1])) {
					recode[1] = "保存失败";
				}
			}
		}
		return recode;	
	} 
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/hzAction")
	public @ResponseBody String[] hzAction(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		HttpSession session = request.getSession();
		String formId = params.getFormId();
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		String sourceType = "sqlserver";
		if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		String[] excresult2 = butService.execHzsql(params.getParamsMap(), params.getExcresult(), formId, params.getButtonId(), "save",session,sourceType);
		
		return excresult2;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveFreeSubmit")
	public @ResponseBody String[] saveFreeSubmit(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
		String[] recode = {"success","成功"};
		String userId = request.getSession().getAttribute("userid")+"";
		Map paramsMap = params.getParamsMap();
		String approveRemark = paramsMap.get("approveRemark")+"";
		String approveType = paramsMap.get("approveType")+"";
		String nextApproveUser = paramsMap.get("nextApproveUser")+"";
		
		String isInit = paramsMap.get("isInit")+"";
		String pkId = paramsMap.get("pkId")+"";
		
		String formId = params.getFormId();
		SysFormconfigCache form = formService.getForm(formId);

		String businessTable = form.getFormDefSaveTable();
		String pkColumn = form.getPkColumn().getFormFiledTablename();
		if (isInit.equals("")) {
			int businessId = pkService.getMaxId(businessTable, pkColumn)-1;
			FreeProcess obj = new FreeProcess();
			obj.setFreeProcessApprove("申请");
			obj.setFreeProcessCommit("申请人申请");
			obj.setFreeStartTime(new Date());
			obj.setFreeNowUser(userId);
			obj.setFreeFormId(formId);
			obj.setFreePkId(businessId+"");
			obj.setFreeNextUser(nextApproveUser);
			obj.setFreeNodeStartTime(new Date());
			obj.setFreeNodeEndTime(new Date());
			obj.setFreeNodeStatus("1");
			obj.setFreeAllStatus("0");
			freeProcessMapper.insert(obj);
			FreeProcess obj1 = new FreeProcess();
			obj1.setFreeNowUser(nextApproveUser);
			obj1.setFreeNodeStartTime(new Date());
			obj1.setFreeFormId(formId);
			obj1.setFreePkId(businessId+"");
			obj1.setFreeNodeStatus("0");
			obj1.setFreeAllStatus("0");
			freeProcessMapper.insert(obj1);
		}else {
			FreeProcessExample example = new FreeProcessExample();
			example.or().andFreeFormIdEqualTo(formId).andFreePkIdEqualTo(pkId).andFreeNodeStatusEqualTo("0");
			FreeProcess obj1 = freeProcessMapper.selectByExample(example).get(0);
			obj1.setFreeProcessApprove(approveType);
			obj1.setFreeProcessCommit(approveRemark);
			obj1.setFreeNowUser(userId);
			obj1.setFreeNextUser(nextApproveUser);
			obj1.setFreeNodeEndTime(new Date());
			obj1.setFreeNodeStatus("1");
			freeProcessMapper.updateByPrimaryKey(obj1);
			
			FreeProcess obj = new FreeProcess();
			obj.setFreeFormId(formId);
			obj.setFreePkId(pkId);
			obj.setFreeNowUser(nextApproveUser);
			obj.setFreeNodeStartTime(new Date());
			obj.setFreeNodeStatus("0");
			obj.setFreeAllStatus("0");
			
			freeProcessMapper.insert(obj);
		}
		
		
		return recode;	
	} 
	
	@RequestMapping("/quertFreeCommitByPkId")
	public @ResponseBody List<FreeProcess> quertFreeCommitByPkId(HttpServletRequest request) throws Exception{
		String pkId = request.getParameter("pkId");
		String formId = request.getParameter("formId");
		FreeProcessExample example = new FreeProcessExample();
		example.or().andFreePkIdEqualTo(pkId).andFreeFormIdEqualTo(formId).andFreeNodeStatusEqualTo("1");
		
		return freeProcessMapper.selectByExample(example);
	} 

	
	@SuppressWarnings("unchecked")
	@RequestMapping("/initExecSql")
	public @ResponseBody String[] initExecSql(HttpServletRequest request, @RequestBody InitExecSqlParams params) throws Exception{
		//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
		String userId = request.getSession().getAttribute("userid")+"";
		String[] recode = {"success","成功"};
		Map param =  params.getParam();
		Map paramMap =  params.getParamsMap();
		String formId = params.getFormId();
		
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		
		String formDefInitQzSql = form.getPojoform().getFormDefInitQzSql();
		
		List<String> paramsList=DealStrSub.getSubUtil(formDefInitQzSql,"\\[p:(.*?)\\]");
		for(int i=0;i<paramsList.size();i++){
			String value = (String)paramMap.get(paramsList.get(i));
			formDefInitQzSql= formDefInitQzSql.replace("[p:"+paramsList.get(i)+"]", value);
		}
		formDefInitQzSql=formDefInitQzSql.toUpperCase();
		try {
			Connection conn = MybatisUtil.getConnection();
			if(formDefInitQzSql.indexOf("CALL") == 0){//存储过程
				String[] result = MyEhrJDBCUtil.execPrepare(conn,formDefInitQzSql);
				return result;
			}
			if(conn!=null){  
			    try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();logger.error(e.getMessage(),e);
				} 
			    conn = null;
			}  
		}catch (Exception e) {
			// TODO: handle exception
			throw new DcfException(e.getMessage());
		}
		
		return recode;	
	} 
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/introduce")
	public @ResponseBody String[] introduceButtonSave(HttpServletRequest request, @RequestBody IntroduceParams params) throws Exception{
		//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
		String[] recode = {"success","成功"};
		logger.info("**************************************"+params);
		String userId = request.getSession().getAttribute("userid")+"";
		try {
			if(params!=null){
//				 recode  =  butService.cardFormSaveButtonSave(params.getParam(),params.getFormId(), params.getParamsMap(), params.getButtonId(), request);
				 SysFormconfigCache form = sysformconfigService.getForm(params.getFormId());
				 SysFormButtonCache buttonCache = form.getButton(Long.valueOf(params.getButtonId()));
				 SysFormButtonIntroduceCache introduce = new SysFormButtonIntroduceCache();
				 if (buttonCache!=null) {
					 introduce = (SysFormButtonIntroduceCache)buttonCache.getButtonExt();
				 }
				 List<SysFormButtonIntrDetailCache> details = introduce.getDetails();
				 recode[0] = "recode";
				 recode[1] = "保存成功";
				 /**
					 * 请求和会话参数处理
					 */
					Map<String, String> paramobj = new HashMap<String,String>();
					/*Iterator<String> paramkeys = params.keySet().iterator();
					while(paramkeys.hasNext()){
						String key = (String) paramkeys.next();
						String value = params.get(key);
						for(int k=0; k<details.size(); k++){
							if("r".equals(details.get(k).getTargetColumnType()) || "s".equals(details.get(k).getTargetColumnType())||"1".equals(details.get(k).getTargetColumnType())){
								if(key.equals(details.get(k).getTargetColumnId())){
									SysFormColumn column = details.get(k).getFormColumn();
									paramobj.put(column.getFormFiledTablename(), value);
									break;
								}
							}
						}
					}*/
					
				/**
				 * 开发参数处理
				 */
						
					for(int k=0; k<details.size(); k++){
						String type = details.get(k).getTargetColumnType();
						if("l".equals(type)){
							SysFormColumnCache column = details.get(k).getFormColumn();
							String target = details.get(k).getTargetColumnId();
							String value = (String)params.getParams()[0].get(target);
							paramobj.put(column.getFormFiledTablename(), value);
							break;
						}
					}

					for(int i=0; i<params.getParams().length; i++){
						Map obj = new HashMap();
						Map<String,String> temp = params.getParams()[i];
						Iterator<String> keys = temp.keySet().iterator();
						while(keys.hasNext()) {
							String key = (String) keys.next();
							String value=String.valueOf(temp.get(key));
							for(int k=0; k<details.size(); k++){
								if("r".equals(details.get(k).getTargetColumnType()) || "s".equals(details.get(k).getTargetColumnType())||"l".equals(details.get(k).getTargetColumnType()) ){
									continue;
								}
								SysFormColumnCache column = details.get(k).getFormColumn();
								SysFormColumnCache targetColumn = details.get(k).getTargetColumn();
								if(column!=null&&targetColumn!=null){
									if(targetColumn.getFormFiledTablename().equals(key)){
										obj.put(column.getFormFiledTablename(), value);
										break;
									}
								}
							}
						}
						obj.putAll(paramobj);
						
						//
						
						Map<String,Object> paramsMap = new HashMap<String,Object>();
						//处理字段参数
						SysFormButtonCache b = form.serchButtonButtonId(params.getButtonId()+"");
						SysFormGeneralExecSqlCache a = b.getHzSql();
						if(a.getPojo()!=null&&a.getExecSqlId()!=0){
							List<String[]> p = a.getParams();
							for(String[] ptemp:p){
								if(ptemp[0].equals("c")){
									String columnId = ptemp[1].substring(ptemp[1].lastIndexOf("_")+1);
									String fieldName = (form.serchColumnByColumnId(Long.parseLong(columnId)).getJsId("name",String.valueOf(form.getFormDefId())));
									paramsMap.put("c_"+columnId, paramobj.get(fieldName));
								}
							}
						}
						String[] r  = butService.cardFormSaveButtonSave(obj, params.getFormId()+"",paramsMap,params.getButtonId()+"",request,"introduce","sqlserver");
						if(!r[0].endsWith("error")){
							continue;
						}else {
							return r;
						}
					}
					return recode;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			recode[0] = "error";
			recode[1] = "未知异常";
		}
		if("error".equals(recode[0])) {
			if(!StringUtils.isNotNullAndBlank(recode[1])) {
				recode[1] = "保存失败";
			}
		}
		return recode;	
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cardformInitData")
	public @ResponseBody FormConfigResultMap  cardformInitData(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		long startTime = new Date().getTime();
		String key =CustomerContextHolder.getContextType();
		String formId = params.getFormId();
		SysFormconfigCache form =  sysformconfigService.getForm(formId);
		String sourceType = "sqlserver";
		if (form.getPojoform().getFormDefSource()!=null && form.getPojoform().getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		
		Map requestParam = params.getRequestParam();
		Map containerParam = params.getContainerParam();
		Map filter = params.getFilter();
		Map[] heightGrade = params.getHeightGrade();
		String pkId = params.getPkId();
		String userIds = params.getUserIds();
		//String order = params.getOrder();
		String offset = params.getOffset();
		String limit = params.getLimit();
		Map paramsMap = params.getParamsMap();
		HttpSession session = request.getSession();
//		List<Map> heightGrade = null;
		UserObject user = new UserObject();
		String isView = "1";
		Pager page = params.getPage();
		page = new Pager();
		if(offset==null&&limit==null){
			
		}else {
			int start=Integer.parseInt(offset);
			page.setStart(start/Integer.parseInt(limit)+1); //不能设置为0
			page.setLimt(Integer.parseInt(limit));
		}
		String queryType = request.getParameter("queryType");
		
		logger.info(session.getAttribute("userid")+"");
		String currentUserId = session.getAttribute("userid").toString();
		logger.info("**********************************标签111********"+(new Date().getTime()-startTime)+"S******************************************");
		FormConfigResultMap resultMap = runtimeService.cardFormInitData(requestParam, filter, heightGrade, pkId, formId, currentUserId, page, user, isView, paramsMap, request,userIds,containerParam,queryType,sourceType);
		logger.info("**********************************标签222********"+(new Date().getTime()-startTime)+"mS******************************************");
		CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
		return resultMap;
	}
	
	/**
	 * 导出查询数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportCardformInitData")
	public @ResponseBody Object  exportCardformInitData(HttpServletRequest request,HttpServletResponse response, @RequestBody ExportCardformInitDataParams params ) throws Throwable {
		String[] reCode = {"success"};
		Map requestParam = params.getRequestParam();
		Map containerParam = params.getContainerParam();
		Map filter = params.getFilter();
		Map[] heightGrade = params.getHeightGrade();
		Map fieldArray = params.getFieldArray();
		String pkId = "0";
		String formId = params.getFormId();
		String buttonId =params.getButtonId();
		String userIds = params.getUserIds();
		Map paramsMap = params.getParamsMap();
		SysFormButtonExportExample exportExample = new SysFormButtonExportExample();
		exportExample.or().andFormButtonIdEqualTo(new BigDecimal(buttonId));
		List<SysFormButtonExport> exports = sysFormButtonExportMapper.selectByExample(exportExample);
		if(exports.size()>0){
			SysFormButtonExport export = sysFormButtonExportMapper.selectByExample(exportExample).get(0);
			String fileName = export.getButtonExportFilename();
		
			HttpSession session = request.getSession();
			UserObject user = new UserObject();
			String isView = "1";
			Pager page = null;
			page = new Pager();
			String queryType = request.getParameter("queryType");
			
			String currentUserId = session.getAttribute("userid").toString();
			SysFormconfig formInfo = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId));
			String sourceType = "sqlserver";
			if (formInfo.getFormDefSource()!=null && formInfo.getFormDefSource().equals("oracle1")) {
				sourceType = "oracle";
				CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
				
			}
			//待优化
			FormConfigResultMap resultMap = runtimeService.exportCardFormInitData(requestParam, filter, heightGrade, pkId, formId, currentUserId, page, user, isView, paramsMap, request,userIds,containerParam,queryType,sourceType);
			
			
			List<Map> datas = resultMap.getRows();
			
			SysFormColumnExample example = new SysFormColumnExample();
			example.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
			List<SysFormColumn> exportColumns = sysFormColumnMapper.selectByExample(example);
			
			Map<String,SysFormCombobox> comBoboxMap = new HashMap<String, SysFormCombobox>();
			Map<String,SysFormDatepicker> datepickerMap = new HashMap<String, SysFormDatepicker>();
			Map<String,SysExecSql> execSqlMap = new HashMap<String, SysExecSql>();
			Map<String,SysFormColumn> columnMap = new HashMap<String, SysFormColumn>();
			Map<String,Map> sqlValueMap = new HashMap<String, Map>();
			Map<String,Map> dictValueMap = new HashMap<String, Map>();
			
			for (int i = 0; i < datas.size(); i++) {
				Map obj = datas.get(i);
				for (int j = 0; j < exportColumns.size(); j++) {
					BigDecimal sysFormColumnId = exportColumns.get(j).getFormColumnId();
					SysFormColumn column = exportColumns.get(j);
					//logger.info(column.getFormColumnLable());
					if (column.getFormColumnGuiType().equals("2")) {
						SysFormCombobox combobox = null;
						if (comBoboxMap.get(sysFormColumnId+"")==null || comBoboxMap.get(sysFormColumnId+"").equals("")) {
							SysFormComboboxExample comboboxExample = new SysFormComboboxExample();
							comboboxExample.or().andComboboxFormColumnIdEqualTo(sysFormColumnId);
							combobox = sysFormComboboxMapper.selectByExample(comboboxExample).get(0);
							comBoboxMap.put(sysFormColumnId+"",combobox);
						}else {
							combobox = comBoboxMap.get(sysFormColumnId+"");
						}
						if (combobox.getComboboxGuiInitType().equals("dict")) {
							String dataField = ChangeCode.getUniqueCode(column.getFormEntityTablename(), column.getFormFieldTablename());
							String value = obj.get(dataField.toUpperCase())+"";
							if (dictValueMap.get(combobox.getComboboxGuiInitValue())==null || dictValueMap.get(combobox.getComboboxGuiInitValue()).equals("")) {
								List<DictData> rs = sysformconfigService.getCardDictDataByDictTypeCode(combobox.getComboboxGuiInitValue(), "dict");
								Map dict = new HashMap();
								for (int k = 0; k < rs.size(); k++) {
									dict.put(rs.get(k).getCode(), rs.get(k).getName());
								}
								value = dict.get(value)+"";
								dictValueMap.put(combobox.getComboboxGuiInitValue(), dict);
							}else {
								value = dictValueMap.get(combobox.getComboboxGuiInitValue()).get(value)+"";
							}
							if (value.equals("null")) {
								value = "";
							}
							obj.put(dataField.toUpperCase(), value);
						}
						if (combobox.getComboboxGuiInitType().equals("sql")) {
							String dictName = combobox.getComboboxGuiInitValue();
							String dataField = ChangeCode.getUniqueCode(column.getFormEntityTablename(), column.getFormFieldTablename());
							String value = obj.get(dataField.toUpperCase())+"";
							if (dictValueMap.get(combobox.getComboboxGuiInitValue())==null || dictValueMap.get(combobox.getComboboxGuiInitValue()).equals("")) {
								List<DictData> rs = sysformconfigService.getCardDictDataByDictTypeCode(combobox.getComboboxGuiInitValue(), "sql");
								Map dict = new HashMap();
								if (rs!=null) {
									for (int k = 0; k < rs.size(); k++) {
										dict.put(rs.get(k).getCode(), rs.get(k).getName());
									}
								}
								value = dict.get(value)+"";
								dictValueMap.put(combobox.getComboboxGuiInitValue(), dict);
							}else {
								value = dictValueMap.get(combobox.getComboboxGuiInitValue()).get(value)+"";
							}
							if (value.equals("null")) {
								value = "";
							}
							obj.put(dataField.toUpperCase(), value);
						}
					}
					if (column.getFormColumnGuiType().equals("6")) {
						String dataField = ChangeCode.getUniqueCode(column.getFormEntityTablename(), column.getFormFieldTablename());
						String value = obj.get(dataField.toUpperCase())+"";
						String dateString = "";
						if (value.length()>=18) {
							dateString = value.substring(0, 18);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = sdf.parse(dateString);
							SysFormDatepicker datepicker = null;
							if (datepickerMap.get(sysFormColumnId+"")==null) {
								SysFormDatepickerExample datepickerExample = new SysFormDatepickerExample();
								datepickerExample.or().andDatepickerFormColumnIdEqualTo(sysFormColumnId);
								if(sDatepickerMapper.selectByExample(datepickerExample).size()>0){
									datepicker = sDatepickerMapper.selectByExample(datepickerExample).get(0);
									datepickerMap.put(sysFormColumnId+"",datepicker);
								}
							}else {
								datepicker = datepickerMap.get(sysFormColumnId+"");
							}
							String dateType = datepicker.getDatepickerFormat();
							SimpleDateFormat sdf1 = new SimpleDateFormat(dateType);
						    if(dateType.equals("HH:mm")||dateType.equals("hh:mm")){
						    	sdf1 = new SimpleDateFormat("HH:mm");
						    	if(dateType.equals("HH:mm")){
						    		sdf1 = new SimpleDateFormat("hh:mm");
						    	}
						    }
						    String date1 = sdf1.format(date);
						    obj.put(dataField.toUpperCase(), date1);
						}
					}
				}
				
			}
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			List<SysFormColumnCache> columns = form.getColumns();
			LinkedHashMap<String,String> fieldMap = new LinkedHashMap<String,String>();
			for (int i = 0; i < columns.size(); i++) {
				String columnLable = columns.get(i).getPojo().getFormColumnLable();
				String columnTable = columns.get(i).getPojo().getFormEntityTablename();
				String columnField = columns.get(i).getPojo().getFormFieldTablename();
				if (fieldArray.get(columnField)!=null && fieldArray.get(columnField).equals("Y")) {
					fieldMap.put(ChangeCode.getUniqueCode(columnTable.toUpperCase(), columnField.toUpperCase()), columnLable);
				}
			}
			
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
//			String path =  System.getProperty("user.dir").replace("\\", "/").replace("bin", "");
//			path = path+"webapps/myehr/WEB-INF/classes/sysparam.properties";
			String path =  Thread.currentThread().getContextClassLoader().getResource("/").getPath()+"sysparam.properties";
			String userId = request.getSession().getAttribute("userid")+"";
			String baseExclePath = GetDBPropertiesValue.readValue(path,"excel.exportTempPath");
			CreateFileUtil.createDir(baseExclePath);
			if(datas.size()>15000){
				ExcelUtils.listToExcelBigData(datas, fieldMap,baseExclePath, fileName+userId, response);
				reCode[0] = "BIG";
				try {
					return reCode;
				} catch (Exception e) {
					// TODO: handle exception
					logger.info(e.getMessage());
					
					JSONArray jsonObject = JSONArray.fromObject(reCode);
					String jsonStr = jsonObject.toString();
			        logger.info(jsonStr);
					return null;
				}
			}else {
				ExcelUtils.listToExcel(datas, fieldMap,baseExclePath, fileName+userId, response);
				reCode[0] = "SMALL";
				return reCode;
			}	
		}else {
			reCode[0] = "fail";
			return reCode;
		}
	}
	
	/**
	 * 导出查询数据xlsx
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/getExportBigFile")
	public @ResponseBody ModelAndView getExportBigFile(HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		String buttonId = request.getParameter("buttonId");
		SysFormButtonExportExample exportExample = new SysFormButtonExportExample();
		exportExample.or().andFormButtonIdEqualTo(new BigDecimal(buttonId));
		SysFormButtonExport export = sysFormButtonExportMapper.selectByExample(exportExample).get(0);
		String userId = request.getSession().getAttribute("userid")+"";
		String fileName =export.getButtonExportFilename()+userId+".xlsx";
		String path =  System.getProperty("user.dir").replace("\\", "/").replace("bin", "");
		path = path+"webapps/myehr/WEB-INF/classes/sysparam.properties";
//		String path = "E:/workspace/Myeclipse/.metadata/.me_tcat/webapps/myehr/WEB-INF/classes/sysparam.properties";

		String baseExclePath = GetDBPropertiesValue.readValue(path,"excel.exportTempPath");
		baseExclePath = baseExclePath+export.getButtonExportFilename()+userId+".xlsx";
		mv = new ModelAndView("forward:/jsp/form/button/download.jsp?filename="+fileName+"&contentType=application/octet-stream&filePath="+baseExclePath);
        return mv;
	}
	
	/**
	 * 导出查询数据xls
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/getExportFile")
	public @ResponseBody ModelAndView getExportFile(HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		String buttonId = request.getParameter("buttonId");
		SysFormButtonExportExample exportExample = new SysFormButtonExportExample();
		exportExample.or().andFormButtonIdEqualTo(new BigDecimal(buttonId));
		SysFormButtonExport export = sysFormButtonExportMapper.selectByExample(exportExample).get(0);
		String userId = request.getSession().getAttribute("userid")+"";
		String fileName =export.getButtonExportFilename()+userId+".xls";
		String path =  System.getProperty("user.dir").replace("\\", "/").replace("bin", "");
		path = path+"webapps/myehr/WEB-INF/classes/sysparam.properties";
//		String path = "E:/workspace/Myeclipse/.metadata/.me_tcat/webapps/myehr/WEB-INF/classes/sysparam.properties";
		String baseExclePath = GetDBPropertiesValue.readValue(path,"excel.exportTempPath");
		baseExclePath = baseExclePath+export.getButtonExportFilename()+userId+".xls";
		mv = new ModelAndView("forward:/jsp/form/button/download.jsp?filename="+fileName+"&contentType=application/octet-stream&filePath="+baseExclePath);
        return mv;
	}
	
	public String getDictValue(String dictCode,String value) throws Exception {
		String sql = "select SYS_DICT_ENTRY.DICT_ENTRY_NAME as dictName  from SYS_DICT_ENTRY,SYS_DICT_TYPE where SYS_DICT_ENTRY.DICT_TYPE_ID = SYS_DICT_TYPE.DICT_TYPE_ID and SYS_DICT_TYPE.DICT_TYPE_CODE='"+dictCode+"' and DICT_ENTRY_CODE='"+value+"'";
		List<Map> rs = MybatisUtil.queryList("runtime.selectSql", sql);
		if (rs.size()>0) {
			return rs.get(0).get("dictName")+"";
		}
		return "";
	}
	
	/**
	 * 查询卡片表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadFormInfo")
	public @ResponseBody SysFormconfigWithBLOBs  loadFormInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
		
		return fWithBLOBs;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadFormInfoTabs")
	public @ResponseBody Object loadFormInfoTabs(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigMstTabExample example = new SysFormconfigMstTabExample();
		example.or().andMstTabFormIdEqualTo(new BigDecimal(formDefId));
		Map map = new HashMap();
		String mstId = "0";
		if(sMstTabMapper.selectByExample(example).size()>0){
			mstId = sMstTabMapper.selectByExample(example).get(0).getMstTabId()+"";
		}
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));

		map.put("mstId", mstId);
		map.put("FormInfo", fWithBLOBs);
		return map;
	}
	
	/**
	 * 查询卡片表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/loadInterfaceScheme")
	public @ResponseBody SysInterfaceSchemeWithBLOBs  loadInterfaceScheme(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String interfaceSehemeId = params.getFormId();
		
		SysInterfaceSchemeWithBLOBs interfaceScheme = sysInterfaceSchemeMapper.selectByPrimaryKey(Long.parseLong(interfaceSehemeId));
		
		return interfaceScheme;
	}
	
	/**
	 * 查询列表表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/loadGridFormInfo")
	public @ResponseBody SysFormconfigAndGrid2 loadGridFormInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigAndGrid2 sysFormconfigAndGrid = new SysFormconfigAndGrid2();
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
		SysFormconfigGridExample example = new SysFormconfigGridExample();
		sysFormconfigAndGrid.setParam(fWithBLOBs);
		example.or().andFormGridFromIdEqualTo(new BigDecimal(formDefId));
		List<SysFormconfigGrid> sGrids = sysFormconfigGridMapper.selectByExample(example);
		
		if(fWithBLOBs.getFormDefLayoutType().equals("9")){
			SysFormChartsExample chartsExample = new SysFormChartsExample();
			chartsExample.or().andFormChartsFormIdEqualTo(new BigDecimal(formDefId));
			List<SysFormCharts> sCharts = sysFormChartsMapper.selectByExample(chartsExample);
			if (sCharts.size()>0) {
				SysFormCharts sgGrid = sCharts.get(0);
				sysFormconfigAndGrid.setSysFormCharts(sgGrid);
			}
		}
		
		if (sGrids.size()>0) {
			SysFormconfigGrid sgGrid = sGrids.get(0);
			sysFormconfigAndGrid.setFormGrid(sgGrid);
		}
		
		return sysFormconfigAndGrid;		
	}
	/**
	 * 查询树表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadTreeFormInfo")
	public @ResponseBody SysFormconfigAndGrid2 loadTreeFormInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigAndGrid2 sysFormconfigAndGrid = new SysFormconfigAndGrid2();
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
		SysFormconfigTreeExample example = new SysFormconfigTreeExample();
		sysFormconfigAndGrid.setParam(fWithBLOBs);
		example.or().andFormTreeFormIdEqualTo(new BigDecimal(formDefId));
		List<SysFormconfigTree> sTrees = sysFormconfigTreeMapper.selectByExample(example);
		if (sTrees.size()>0) {
			SysFormconfigTree sTree = sTrees.get(0);
			sysFormconfigAndGrid.setFormTree(sTree);
			return sysFormconfigAndGrid;
		}
		return sysFormconfigAndGrid;		
	}
	/**
	 * 查询主从表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadMstFormInfo")
	public @ResponseBody SysFormconfigAndGrid2 loadMstFormInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigAndGrid2 sysFormconfigAndGrid = new SysFormconfigAndGrid2();
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
		SysFormconfigMstTabExample example = new SysFormconfigMstTabExample();
		sysFormconfigAndGrid.setParam(fWithBLOBs);
		example.or().andMstTabFormIdEqualTo(new BigDecimal(formDefId));
		List<SysFormconfigMstTab> sMstTabs = sMstTabMapper.selectByExample(example);
		if (sMstTabs.size()>0) {
			sysFormconfigAndGrid.setFormMstTab(sMstTabs.get(0));
			return sysFormconfigAndGrid;
		}
		return sysFormconfigAndGrid;		
	}
	
	/**
	 * 查询易客报表表单基本数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadYKInfo")
	public @ResponseBody SysFormconfigAndGrid2 loadYKInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigAndGrid2 sysFormconfigAndGrid = new SysFormconfigAndGrid2();
		SysFormconfigWithBLOBs fWithBLOBs = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
		
		SysFormYkReportExample example = new SysFormYkReportExample();
		sysFormconfigAndGrid.setParam(fWithBLOBs);
		example.or().andReportFormIdEqualTo(Long.valueOf(formDefId));
		List<SysFormYkReport> sYkReports = sYkReportMapper.selectByExample(example);
		if (sYkReports.size()==1) {
			sysFormconfigAndGrid.setFormYkReport(sYkReports.get(0));
			return sysFormconfigAndGrid;
		}
		return sysFormconfigAndGrid;		
	}
	
	/**
	 * 查询多tab信息
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadTabsInfo")
	public @ResponseBody SysFormconfigMstTab  loadTabsInfo(HttpServletRequest request, @RequestBody CardformInitDataParams params ) throws Throwable {
		String formDefId = params.getFormId();
		SysFormconfigMstTabExample sExample = new SysFormconfigMstTabExample();
		sExample.or().andMstTabFormIdEqualTo(new BigDecimal(formDefId));
		List<SysFormconfigMstTab> smTab = sMstTabMapper.selectByExample(sExample);
		if (smTab.size()==1) {
			return smTab.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 删除数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/removeData")
	public @ResponseBody String  removeData(HttpServletRequest request, @RequestBody RemoveDataformParams params ) throws Throwable {
		UserObject user = (UserObject)request.getAttribute("userObject");
		SysFormconfig form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(params.getFormId()));
		String sourceType = "sqlserver";
		String falg = datasign.removedata1(request, params);
		if(falg=="000"){
			return "-1";
		}
		if (form.getFormDefSource()!=null && form.getFormDefSource().equals("oracle1")) {
			sourceType = "oracle";
			CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		}
		String recode = runtimeGridService.removeDatas(params.getParams(), params.getFormId(), params.getTabparam(), user, request,sourceType);
		return recode;
		
	}
	
	/**
	 * 删除数据
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/copyData")
	public @ResponseBody String  copyData(HttpServletRequest request, @RequestBody CopyDataformParams params ) throws Throwable {
		UserObject user = (UserObject)request.getAttribute("userObject");
		String formSaveTable = params.getFormSaveTable();
		SysEntityExample entityExample = new SysEntityExample();
		entityExample.or().andEntityCodeEqualTo(formSaveTable);
		List<SysEntity> sysEntitys = sEntityMapper.selectByExample(entityExample);

		SysFieldExample example = new SysFieldExample();
		example.or().andFieldEntityIdEqualTo(new BigDecimal(sysEntitys.get(0).getEntityId())).andDeleteMarkNotEqualTo("Y");
		List<SysField> sysFields = sFieldMapper.selectByExample(example);
		
		return runtimeGridService.copyDatas(sysFields,params.getParams(), params.getFormSaveTable(), params.getPkColumn(), user, request);
		
	}
	
	/**
	 * 加载树方案
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryTreeSolution")
	public @ResponseBody List<Map>  queryTreeSolution(HttpServletRequest request) throws Throwable {
		long start = new Date().getTime();
		UserObject user = (UserObject)request.getAttribute("userObject");
		String formId = request.getParameter("formId");
		String params = request.getParameter("params");
		List<Map> maps = runtimeTreeService.querySqlResult(formId, params,request);	
		long end = new Date().getTime();
		logger.info("**********************************树2非懒******"+(end-start)+"mS******************************************");
		return maps;
	}
	
	
	/**
	 * 验证密码
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkPassword")
	public @ResponseBody String[]  checkPassword(HttpServletRequest request) throws Throwable {
		String userCode = request.getParameter("userCode");
		String password = request.getParameter("password")+"==";
		String pwd = request.getParameter("pwd");
		String updateType = request.getParameter("updateType")+"";
		//SysUser suser = new SysUser();
		SysUser user2 =sysUserExpandMapper.selectByUsercode(userCode);
		password=password.toUpperCase();
		boolean falg = false;
		String[] recode1 = {"success","成功"};
		String[] recode2 = {"defeat","失败"};
		
		if(!updateType.equals("firstLoad")||updateType.equals("null")){
			SysUserExample example = new SysUserExample();
			example.or().andUserCodeEqualTo(userCode);
			SysUser user = sUserMapper.selectByExample(example).get(0);
			
			logger.info(user.getUserPassword());//ZWSUCORZKCRSUJLIL6T2VQ==
		if(user.getUserPassword().toUpperCase().equals(password)){
			return recode1;
		}
		return recode2;
	}
		SysSystemParam param = sysformconfigService.getSystemParam("InitPasswork");
		
		if(param.getSysParamEntry().equals(pwd)){
			//默認密碼 000000
			System.out.println("默认密码或自定义");
			
			
			return recode1;
			
		}else if(param.getSysParamEntry().equalsIgnoreCase("idcard")){
			//身份證後六位
			SysSystemParam paramEmp = sysformconfigService.getSystemParam("EMP_ENTITY");
			if(paramEmp!=null){
				if (user2.getEmpId()!=null) {
					String sql = "select emp_certno from "+paramEmp.getSysParamValue1()+" where "+paramEmp.getSysParamValue2()+" = '" +user2.getEmpId()+"'";
					System.out.println(sql);
					List<Map> rs = new ArrayList<Map>();
					rs = MybatisUtil.queryList("runtime.selectSql", sql);
					String code = rs.get(0).get("emp_certno")+"";
					System.out.println(code);
					if(code.equals("null")){
						//默认密码 000000
						//自定密码
						System.out.println("默认密码000000");
						if("000000".equals(pwd)){
							return recode1;
						}else{
							/*loginLog.setLoginMark("1");
							mv = new ModelAndView("../login");
							mv.addObject("reCode","6");//初始密码校验失败*/
							return recode2;
						}
					}else{
						//身份证后六位
						String pw = code.substring(code.length()-6);
						
						if(pw.equals(pwd)){
							return recode1;
						}else{
						/*	loginLog.setLoginMark("1");
							mv = new ModelAndView("../login");
							mv.addObject("reCode","5");//初始密码校验失败
*/						
							return recode2;	
						}
					}
				}else{
/*//					loginLog.setLoginMark("1");
//					mv = new ModelAndView("../login");
//					mv.addObject("reCode","5");//未查到此人用户信息
*/				
					return recode2;		
				}
			}else{
				/*loginLog.setLoginMark("1");
				mv = new ModelAndView("../login");
				mv.addObject("reCode","4");//系统参数配置有误,EMP_ENTITY参数数据为空
*/			
				return recode2;		
			}
		}else{
			/*loginLog.setLoginMark("1");
			mv = new ModelAndView("../login");
			mv.addObject("reCode","3");//初始密码校验失败
*/		
			return recode2;		
		}
	}
	/**
	 * 验证密码是否过期
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkPasswordTime")
	public @ResponseBody String[] checkPasswordTime(HttpServletRequest request) throws Throwable {
		String userCode = request.getParameter("userCode");
		//call EMP_SP_CADREADD('52',1,?)
		logger.info(MybatisUtil.getConnection()+"");
		String[] flag = MyEhrJDBCUtil.execPrepare(MybatisUtil.getConnection(),"call SYS_SP_UPDATEPASSWORD('"+userCode+"',?)");
		return flag;
	}
	
	/**
	 * 验证唯一性
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkUniqueVerify")
	public @ResponseBody String[]  checkUniqueVerify(HttpServletRequest request) throws Throwable {
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("fieldName");
		String value = request.getParameter("value");
		String pkFieldName = request.getParameter("pkFieldName");
		String pkId = request.getParameter("pkId");
		String[] recode = new String[2];
		String sql = "select count(*) as totle from "+tableName+ " where "+fieldName+ " = '"+value+"'";
		List<Map> t = (List<Map>) tMapperExt.queryByFormDefSql(sql);
		String sql1 = "select count(*) as totle from "+tableName+ " where "+fieldName+ " = '"+value+"' and "+pkFieldName+" = "+pkId;
		List<Map> t1 = (List<Map>) tMapperExt.queryByFormDefSql(sql1);
		int totle = (Integer)t.get(0).get("totle");
		int totle1 = (Integer)t1.get(0).get("totle");
		if (totle>0 && totle1==0) {
			recode[0] = "1";
			recode[1] = "不符合唯一校验！";
		}else {
			recode[0] = "0";
			recode[1] = "成功";
		}
		return recode;
	}
	
	
	/**
	 * 修改密码
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updatePassword")
	public @ResponseBody Object  updatePassword(HttpServletRequest request) throws Throwable {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password")+"==";
		String newPassword = request.getParameter("newPassword")+"==";
		String[] recode1 = {"success","成功"};
		String[] recode2 = {"defeat","失败"};
		if (password.equals(newPassword)) {
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("userId", userId);
//			map.put("newPassword", newPassword);
//			sUserMapperExpand.updatePassword(map);
			
			SysUser user = sUserMapper.selectByPrimaryKey(Integer.valueOf(userId));
			user.setPasswordWarningTime(new Date());
			user.setUserPassword(newPassword);
			user.setUserRemark("x");
			sUserMapper.updateByPrimaryKey(user);
			
			return recode1;
		}else {
			return recode2;
		}
	}
	
	/**
	 * 获取数据库密码正则
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getrule")
	public @ResponseBody String getrule(HttpServletRequest request) throws Exception{
		SysSystemParam param = sysformconfigService.getSystemParam("InitPasswork");
		
		
		
		String rule  = param.getSysParamValue1()+"&"+param.getSysParamValue2()+"pw"+param.getSysParamEntry();
		
		return rule;
		
	}
	
	/**
	 * 密码配置回显
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getInit")
	public @ResponseBody String getInit(HttpServletRequest request) throws Exception{
		SysSystemParam param = sysformconfigService.getSystemParam("InitPasswork");
		//密碼長度
		String cd = (String) param.getSysParamValue1().subSequence(param.getSysParamValue1().length()-3, param.getSysParamValue1().length()-2);
		
		String rule  = param.getSysParamEntry()+"&"+param.getSysParamValue2()+"&"+cd;
		
		return rule;
		
	}
	/**
	 * 修改密码
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updatePasswordAdmin")
	public @ResponseBody String[]  updatePasswordAdmin(HttpServletRequest request) throws Throwable {
		String userIds = request.getParameter("userIds");
		String newPassword = request.getParameter("newPassword")+"==";

		String[] recode1 = {"success","成功"};
		String[] recode2 = {"defeat","失败"};
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", userIds);
		map.put("newPassword", newPassword);
		sUserMapperExpand.updatePasswordAdmin(map);
		return recode1;
	}
	
	/**
	 * 加载表单树方案
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFormTree")
	public @ResponseBody List<Map>  queryFormTree(HttpServletRequest request) throws Throwable {
		SysFormFolderTreeExample example = new SysFormFolderTreeExample();
		List<SysFormFolderTree> trees =  treeMapper.selectByExample(example);
		List<Map> maps = new ArrayList();
		Map map1 = new HashMap();
		map1.put("FOLDER_TREE_ID", "0");
		map1.put("FOLDER_TREE_NAME", "表单目录树");
		maps.add(map1);
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getFolderTreeParentId()==null) {
				trees.get(i).setFolderTreeParentId(new BigDecimal("0"));
			}
			Map map = new HashMap();
			map.put("FOLDER_TREE_ID", trees.get(i).getFolderTreeId());
			map.put("FOLDER_TREE_NAME", trees.get(i).getFolderTreeName());
			map.put("FOLDER_TREE_PARENT_ID", trees.get(i).getFolderTreeParentId());
			map.put("FOLDER_TREE_SEQ", trees.get(i).getFolderTreeSeq());
			map.put("FOLDER_TREE_CODE", trees.get(i).getFolderTreeCode());
			map.put("FOLDER_TREE_DESC", trees.get(i).getFolderTreeDesc());
			map.put("OPERATOR_TIME", trees.get(i).getOperatorTime());
			map.put("OPERATOR_NAME", trees.get(i).getOperatorName());
			maps.add(map);
		}
		return maps;
	}
	
	/**
	 * 加载题库树方案
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/questionTree")
	public @ResponseBody
	List<Map> questionTree(HttpServletRequest request) throws Throwable {
		List<TreeModel> tList = new ArrayList<TreeModel>();
		TreeModel test1 = new TreeModel();
		test1.setId("root");
		test1.setIsParent(true);
		test1.setName("题库");
		test1.setOpen(false);
		test1.setPid("");
		tList.add(test1);
		SysDictTypeExample example1 = new SysDictTypeExample();
		example1.or().andDictTypeCodeEqualTo("SYS_QUESTION");
		SysDictType type = sDictTypeMapper.selectByExample(example1).get(0);
		SysDictEntryExample example = new SysDictEntryExample();
		example.or().andDictTypeIdEqualTo(type.getDictTypeId());
		List<SysDictEntry> entries = sysDictEntryMapper.selectByExample(example);
		for (SysDictEntry sysDictEntryExpand : entries) {
			if(!sysDictEntryExpand.getDictEntryName().equals("")){
				TreeModel test2 = new TreeModel();
				test2.setId(sysDictEntryExpand.getDictEntryCode());
				test2.setIsParent(true);
				test2.setName(sysDictEntryExpand.getDictEntryName());
				test2.setOpen(false);
				test2.setPid("root");
				tList.add(test2);
				
				SysDictTypeExample example2 = new SysDictTypeExample();
				example2.or().andDictTypeCodeEqualTo("QUESTIONTYPE");
				SysDictType type1 = sDictTypeMapper.selectByExample(example2).get(0);
				SysDictEntryExample example3 = new SysDictEntryExample();
				example3.or().andDictTypeIdEqualTo(type1.getDictTypeId());
				List<SysDictEntry> entries1 = sysDictEntryMapper.selectByExample(example3);
				for (SysDictEntry sysEntity : entries1) {
					if(!sysEntity.getDictEntryName().equals("")){
					TreeModel test3 = new TreeModel();
					test3.setId(sysEntity.getDictEntryCode());
					test3.setIsParent(false);
					test3.setName(sysEntity.getDictEntryName());
					test3.setOpen(true);
					test3.setPid(sysDictEntryExpand.getDictEntryCode());
					tList.add(test3);
					}
				}
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(tList);
		String str = jsonArray.toString();
		String newStr = str.replaceAll("pid", "pId");
		JSONArray json = JSONArray.fromObject(newStr);
		return json;
	}
	
	//查询所有表单信息
	@RequestMapping("/findFormList")
	 public @ResponseBody ResultMap findFormList(HttpServletRequest request) throws Exception {
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
	        try {  
	            return formService.getAllFormMsSql(param);  
	        } catch (Exception e) {  
	            e.printStackTrace();logger.error(e.getMessage(),e);  
	        }  
	        return null; 
	}
	
	@RequestMapping("/queryInterfaceList")
	public @ResponseBody ResultMapNew queryInterfaceList(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Exception{
		
		//Map requestParam = params.getRequestParam();
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
		
		String wheres="";
		String searchValue = (String) filter.get("searchValue");
		if (searchValue!=null && searchValue!="") {
			wheres += " and ( sys_interface_scheme.interface_scheme_code like "+" '%"+searchValue+"%' "+
					  "or sys_interface_scheme.interface_scheme_name like "+" '%"+searchValue+"%' )";
		}
		
		String sql = "select sys_interface_scheme.*, "+
					 " SYS_USER.USER_NAME  "+
                     " from sys_interface_scheme left join SYS_USER on SYS_USER.USER_ID = sys_interface_scheme.interface_scheme_create_user "+
					 " where 1=1 "+wheres;
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

	/**
	 * 查询过滤字段
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/queryWhereColumn")
	public @ResponseBody ResultWheres  queryWhereColumn(HttpServletRequest request) throws Throwable {
		String formId = request.getParameter("formId");
		
		ResultWheres resultMap = new ResultWheres();
		SysFormWhereExample example = new SysFormWhereExample();
		com.myehr.pojo.formmanage.form.SysFormWhereExample.Criteria criteria = example.createCriteria();
		criteria.andFormWhereFormDefIdEqualTo(new BigDecimal(formId));
		example.setOrderByClause("FORM_WHERE_SORT");
		List<SysFormWhere> wheres = sysFormWhereMapper.selectByExample(example);
		
		resultMap.setRows(wheres);
		resultMap.setTotal(wheres.size());
		
		return resultMap;
	}
	
	/**
	 * 查询分组信息
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/getGroupList")
	public @ResponseBody ResultGroups  getGroupList(HttpServletRequest request) throws Throwable {
		String formId = request.getParameter("formId");
		
		ResultGroups resultMap = new ResultGroups();
		SysFormGroupExample example = new SysFormGroupExample();
		example.or().andGroupFormDefIdEqualTo(new BigDecimal(formId));
		example.setOrderByClause("FORM_GROUP_SORT");
		List<SysFormGroup> wheres = sGroupMapper.selectByExample(example);
		
		resultMap.setRows(wheres);
		resultMap.setTotal(wheres.size());
		
		return resultMap;
	}
	
	@RequestMapping("/saveFormParam")
	public @ResponseBody Object saveFormParam(HttpServletRequest request,@RequestBody SysFormconfigWithBLOBs form) throws Exception{
		String userId = (String)request.getSession().getAttribute("userid").toString();
		
		if(checkFormCode(form.getFormDefCode(),form.getFormDefId())==0){//编码不重复	
			form.setOperatorName(userId);
			form.setOperatorTime(new Date());
			sysFormconfigMapper.insert(form);
			SysFormconfigExample example = new SysFormconfigExample();
			example.or().andFormDefCodeEqualTo(form.getFormDefCode());
			String formDefId = sysFormconfigMapper.selectByExample(example).get(0).getFormDefId()+"";
			SysFormGroup group = new SysFormGroup();
			String[] groupName = {"表头标题","表单内容","系统信息"};	
			if(form.getFormDefLayoutType().equals("1")){
				for (int i = 0; i < 3; i++) {
					group.setGroupFormDefId(new BigDecimal(formDefId));
					group.setFormGroupName(groupName[i]);
					group.setFormGroupRowCount(new BigDecimal("1"));
					group.setFormGroupSort(new BigDecimal(i));
					group.setFormGroupIsopen("Y");
					group.setOperatorName(userId);
					group.setOperatorTime(new Date());
					sGroupMapper.insert(group);
				}
			}
			return formDefId;
		}else if (checkFormCode(form.getFormDefCode(),form.getFormDefId())==1) {
			return 0;
		} else {
			return 2;
		}
	}
	
	@RequestMapping("/saveInterfaceScheme")
	public @ResponseBody Object saveInterfaceScheme(HttpServletRequest request,@RequestBody SysInterfaceSchemeWithBLOBs interfaceScheme) throws Exception{
		String userId = (String)request.getSession().getAttribute("userid").toString();
		
		if(checkInterfaceCode(interfaceScheme.getInterfaceSchemeCode(),interfaceScheme.getInterfaceSchemeId())==0){//编码不重复	
			interfaceScheme.setInterfaceSchemeCreateUser(Long.parseLong(userId));
			interfaceScheme.setInterfaceSchemeCreateTime(new Date());
			interfaceScheme.setInterfaceSchemeCategory(interfaceScheme.getInterfaceSchemeCategory());
			sysInterfaceSchemeMapper.insert(interfaceScheme);
			SysInterfaceSchemeExample example = new SysInterfaceSchemeExample();
			example.or().andInterfaceSchemeCodeEqualTo(interfaceScheme.getInterfaceSchemeCode());
			String formDefId = sysInterfaceSchemeMapper.selectByExample(example).get(0).getInterfaceSchemeId()+"";
			return formDefId;
		}else if (checkInterfaceCode(interfaceScheme.getInterfaceSchemeCode(),interfaceScheme.getInterfaceSchemeId())==1) {
			return 0;
		} else {
			return 2;
		}
	}
	
	@RequestMapping("/saveQuestionParam")
	public @ResponseBody Object saveQuestionParam(HttpServletRequest request,@RequestBody SysExamtemplate examtemplate) throws Exception{
		String userId = (String)request.getSession().getAttribute("userid").toString();
		String code = examtemplate.getCode();
		if(checkQuestionCode(code,null)==0){//编码不重复	
			examtemplate.setOperatorName(userId);
			examtemplate.setOperatorTime(new Date());
			sMapper.insert(examtemplate);
			return examtemplate.getId();
		}else if (checkQuestionCode(code,null)==1) {
			return 0;
		} else {
			return 2;
		}
	}
	
	
	
		@RequestMapping("/updataFormParam")
		public @ResponseBody Object updataFormParam(HttpServletRequest request,@RequestBody SysFormconfigWithBLOBs form) throws Exception{
			SysFormconfigWithBLOBs form2 = sysFormconfigMapper.selectByPrimaryKey(form.getFormDefId());		
			String userId = (String)request.getSession().getAttribute("userid").toString();
			JSONObject json = JSONObject.fromObject(form);//将java对象转换为json对象
            String str = json.toString();
			String replaceStr = "actModelId=,expand1=,expandField=,";
			DataSign datasign = new DataSign();
			String sGrid1 = str+"";
			sGrid1 = sGrid1.replaceAll("\"", "");
			sGrid1 = sGrid1.replaceAll(":", "=");
			sGrid1 = sGrid1.replaceAll(replaceStr, "");
			char a = sGrid1.charAt(sGrid1.indexOf("=")+1);
			
			if((sGrid1.charAt(sGrid1.indexOf("=")+1)+"").equals(",")){
				sGrid1 = sGrid1.replace("fileuploadConfig=,","");
			}			
			sGrid1 = sGrid1.replace("formDefDataType=,","");
			sGrid1 = sGrid1.replace("formDefProcessDefName=,","");	
			sGrid1 = sGrid1.replace("formOtherColumnSql=,","");
			sGrid1 = sGrid1.replace("formSubmitStatus=,","");
			sGrid1 = sGrid1.replace("formSubmitTime=,","");
			sGrid1 = sGrid1.replace("nullformSubmitUrl=,","");
			sGrid1 = sGrid1.replace("formSubmitUserId=0,","");
			sGrid1 = sGrid1.replace("formUrl=,","");
			sGrid1 = sGrid1.replace("isEmpPower=,","");
			sGrid1 = sGrid1.replace("isOrgPower=,","");
			sGrid1 = sGrid1.replace("isShowFlowTitle=,","");
			sGrid1 = sGrid1.replace("operatorName=,","");
			sGrid1 = sGrid1.replace("operatorTime=null,","");
			sGrid1 = sGrid1.replace("templateId=0,","");
			sGrid1 = sGrid1.replace("formDefProcessDefNameText=,","");
			sGrid1 = sGrid1.replace("formDefSavetable=,","");
			sGrid1 = sGrid1.replace("formSubmitTime=null,","");
			sGrid1 = sGrid1.replace("formSubmitUrl=,","");
			if(sGrid1.indexOf("isFilemanage=,")>0){
			sGrid1 = sGrid1.replace("isFilemanage=","isFilemanage=null");
			}
			sGrid1 = sGrid1.replace(",templateId=0","");
			if(datasign.UpdataFormparm(request, sGrid1)=="2"){
				return "2";
			}
			if(checkFormCode(form.getFormDefCode(),form.getFormDefId())==0){//编码不重复		
				form2.setFormDefId(form.getFormDefId());
				form2.setFormDefFolderId(form.getFormDefFolderId());
				form2.setFormDefLayoutType(form.getFormDefLayoutType());
				form2.setFormDefName(form.getFormDefName());
				form2.setFormDefCode(form.getFormDefCode());
				form2.setFormDefIsProcess(form.getFormDefIsProcess());
				form2.setIsApp(form.getIsApp());
				form2.setIsComment(form.getIsComment());
				form2.setFormDefSource(form.getFormDefSource());
				form2.setIsFilemanage(form.getIsFilemanage());
				form2.setFormAuthorityIsControl(form.getFormAuthorityIsControl());
				form2.setPowerSql(form.getPowerSql());
				form2.setOrderSql(form.getOrderSql());
				form2.setFormDefEntitySql(form.getFormDefEntitySql());
				form2.setFormDefSql(form.getFormDefSql());
				form2.setFormDefDesc(form.getFormDefDesc());
				form2.setFormDefInitQzJs(form.getFormDefInitQzJs());
				form2.setFormDefInitQzSql(form.getFormDefInitQzSql());
				form2.setFormDefRowCount(form.getFormDefRowCount());
				form2.setFormLableWidth(form.getFormLableWidth());	
				form2.setExpand1(form.getExpand1());
				form2.setExpandField(form.getExpandField());	
				form2.setOperatorName(userId);
				form2.setOperatorTime(new Date());
				return sysFormconfigMapper.updateByPrimaryKeyWithBLOBs(form2);
			}else if (checkFormCode(form.getFormDefCode(),form.getFormDefId())==1) {
				return 0;
			} else {
				return 2;
			}
		}
		/**
		 * 
		 * @param request
		 * @param form
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/updataInterfaceScheme")
		public @ResponseBody Object updataInterfaceScheme(HttpServletRequest request,@RequestBody SysInterfaceSchemeWithBLOBs interfaceScheme) throws Exception{
			SysInterfaceSchemeWithBLOBs interfaceScheme2 = sysInterfaceSchemeMapper.selectByPrimaryKey(interfaceScheme.getInterfaceSchemeId());		
			String userId = (String)request.getSession().getAttribute("userid").toString();
			if(checkInterfaceCode(interfaceScheme.getInterfaceSchemeCode(),interfaceScheme.getInterfaceSchemeId())==0){//编码不重复		
				interfaceScheme2.setInterfaceSchemeId(interfaceScheme.getInterfaceSchemeId());
				interfaceScheme2.setInterfaceSchemeCode(interfaceScheme.getInterfaceSchemeCode());
				interfaceScheme2.setInterfaceSchemeName(interfaceScheme.getInterfaceSchemeName());
				interfaceScheme2.setInterfaceSchemeType(interfaceScheme.getInterfaceSchemeType());
				interfaceScheme2.setInterfaceSchemeCategory(interfaceScheme.getInterfaceSchemeCategory());
				interfaceScheme2.setInterfaceSchemePager(interfaceScheme.getInterfaceSchemePager());
				interfaceScheme2.setInterfaceSchemeUrl(interfaceScheme.getInterfaceSchemeUrl());
				interfaceScheme2.setInterfaceSchemeUsercode(interfaceScheme.getInterfaceSchemeUsercode());
				interfaceScheme2.setInterfaceSchemePassword(interfaceScheme.getInterfaceSchemePassword());
				return sysInterfaceSchemeMapper.updateByPrimaryKeyWithBLOBs(interfaceScheme2);
			}else if (checkInterfaceCode(interfaceScheme.getInterfaceSchemeCode(),interfaceScheme.getInterfaceSchemeId())==1) {
				return 0;
			} else {
				return 2;
			}
		}
		
		/**
		 * 查询查询字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/queryFilterColumn")
		public @ResponseBody ResultFilters  queryFilterColumn(HttpServletRequest request) throws Throwable {
			String formId = request.getParameter("formId");
			
			ResultFilters resultMap = new ResultFilters();
			String sql = "select t.query_column_id as queryColumnId,"
					+" b.form_entity_tablename as formEntityTablename,"
					+" b.form_field_tablename as formFieldTablename,"
					+" t.grid_column_lable as gridColumnLable,"
					+" b.form_column_gui_type as formColumnGuiType,"
					+" t.grid_column_filter_rule  as gridColumnFilterRule,"
					+" t.grid_column_width  as gridColumnWidth,"
					+" t.grid_column_col_span  as gridColumnColSpan,"
					+" t.grid_column_is_intervais  as gridColumnIsIntervais,"
					+" t.form_column_sort  as formColumnSort,"
					+" t.isShowWeb  as isshowweb,"
					+" t.isShowMobile  as isshowmobile,"
					+" b.form_column_form_def_id  as formColumnFoemDefId,"
					+" t.grid_column_id  as gridColumnId,"
					+" b.form_column_width  as formColumnWidth,"
					+" b.form_column_height  as formColumnHeight"
					+" from sys_grid_filter_column t, sys_form_column b"
					+" where t.grid_column_id = b.form_column_id"
					+" and b.form_column_form_def_id = "+formId
					+" order by  t.form_column_sort";
			List<Map> filters = tMapperExt.queryByFormDefSql(sql);
			
			resultMap.setRows(filters);
			resultMap.setTotal(filters.size());
			
			return resultMap;
		}
		
		/**
		 * 删除过滤字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/deleteWhereColumn")
		public @ResponseBody String[]  deleteWhereColumn(HttpServletRequest request,@RequestBody SysFormWhere where) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			sysFormWhereMapper.deleteByPrimaryKey(where.getFormWhereId());
			
			return recode;
		}
		/**
		 * 选择查询字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/selectFilterColumn")
		public @ResponseBody ResultColumns  selectFilterColumn(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Throwable {
			Map filter = params.getFilter();
			String searchValue = (String) filter.get("searchValue");
			String formId = (String) filter.get("formId");
			
			
			ResultColumns resultMap = new ResultColumns();
			SysFormColumnExample example = new SysFormColumnExample();
			com.myehr.pojo.formmanage.form.SysFormColumnExample.Criteria criteria = example.createCriteria();
			com.myehr.pojo.formmanage.form.SysFormColumnExample.Criteria criteria1 = example.createCriteria();
			if (searchValue!=null && !searchValue.equals("")) {
				criteria.andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
				criteria.andFormFieldTablenameLike("%"+searchValue+"%");
				criteria1.andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
				criteria1.andFormColumnLableLike("%"+searchValue+"%");
				example.or(criteria1); 
			}else {
				criteria.andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
			}
			
			List<SysFormColumn> columns = sysFormColumnMapper.selectByExample(example);
			
			resultMap.setRows(columns);
			resultMap.setTotal(columns.size());
			
			return resultMap;
		}
		
		/**
		 * 保存过滤配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveWhereColumn")
		public @ResponseBody String[]  saveWhereColumn(HttpServletRequest request,@RequestBody List<SysFormWhere> wheres) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			String formId = request.getParameter("formId");
			for (int i = 0; i < wheres.size(); i++) {
				SysFormWhere where = wheres.get(i);
				if (where.getFormWhereId()!=null && !(where.getFormWhereId()+"").equals("")) {
					where.setFormWhereFormDefId(new BigDecimal(formId));
					sysFormWhereMapper.updateByPrimaryKey(where);
				}else{
//					int pkid = pkService.getMaxId("SYS_FORM_WHERE", "FORM_WHERE_ID");
//					where.setFormWhereId(new BigDecimal(pkid));
					where.setFormWhereFormDefId(new BigDecimal(formId));
					sysFormWhereMapper.insert(where);
				}
			}
			
			return recode;
		}
		@RequestMapping("/updataGridFormParam1")
		public @ResponseBody Object updataGridFormParam1(HttpServletRequest request,@RequestBody SysFormconfigWithBLOBs form) throws Exception{
			String userId = (String)request.getSession().getAttribute("userid").toString();
			SysFormconfigWithBLOBs form2 = sysFormconfigMapper.selectByPrimaryKey(form.getFormDefId());		
			JSONObject json = JSONObject.fromObject(form);//将java对象转换为json对象			
			String str = json.toString();			
			DataSign datasign = new DataSign();			
			String sGrid1 = str+"";			
			sGrid1 = sGrid1.replaceAll("\"", "");			
			sGrid1 = sGrid1.replaceAll(":", "=");			
			sGrid1 = sGrid1.replaceAll("actModelId=,", "");			
			sGrid1 = sGrid1.replaceAll("expand1=,expandField=,", "");			
			sGrid1 = sGrid1.replaceAll("fileuploadConfig=,", "");			
			sGrid1 = sGrid1.replaceAll("formDefDataType=,", "");									
			sGrid1 = sGrid1.replaceAll("formDefEntitySql=,", "formDefEntitySql=null,");						
			sGrid1 = sGrid1.replaceAll("formDefProcessDefName=,", "");
			sGrid1 = sGrid1.replaceAll("formDefProcessDefNameText=,", "");
			sGrid1 = sGrid1.replaceAll("formDefRowCount=,", "");			
			sGrid1 = sGrid1.replaceAll("formDefSavetable=,", "");						
			sGrid1 = sGrid1.replaceAll("formDefSource=,", "");									
			sGrid1 = sGrid1.replaceAll("formDefSql=,", "formDefSql=null,");						
			sGrid1 = sGrid1.replaceAll("formLableWidth=,", "");			
			sGrid1 = sGrid1.replaceAll("formOtherColumnSql=,", "");			
			sGrid1 = sGrid1.replaceAll("formSubmitStatus=,", "");			
			sGrid1 = sGrid1.replaceAll("formSubmitTime=null,", "");			
			sGrid1 = sGrid1.replaceAll("formSubmitUrl=,", "");			
			sGrid1 = sGrid1.replaceAll("formSubmitUserId=0,", "");			
			sGrid1 = sGrid1.replaceAll("formUrl=,", "");			
			sGrid1 = sGrid1.replaceAll("isComment=,", "");			
			sGrid1 = sGrid1.replaceAll("isEmpPower=,", "");			
			sGrid1 = sGrid1.replaceAll("isFilemanage=,", "");			
			sGrid1 = sGrid1.replaceAll("isOrgPower=,", "");			
			sGrid1 = sGrid1.replaceAll("isShowFlowTitle=,", "");			
			sGrid1 = sGrid1.replaceAll("operatorName=,", "");			
			sGrid1 = sGrid1.replaceAll("operatorTime=null,", "");			
			sGrid1 = sGrid1.replaceAll(",templateId=0", "");
			
			sGrid1 = sGrid1.replace("<\\/", "</");	
			String falg = datasign.UpdataFormparm(request, sGrid1);			
			if(falg=="2"){				
				return "2";			
			}
			if(checkFormCode(form.getFormDefCode(),form.getFormDefId())==0){//编码不重复	
				form2.setFormDefFolderId(form.getFormDefFolderId());
				form2.setFormDefLayoutType(form.getFormDefLayoutType());
				form2.setFormDefName(form.getFormDefName());
				form2.setFormDefCode(form.getFormDefCode());
				form2.setFormDefIsProcess(form.getFormDefIsProcess());
				form2.setIsApp(form.getIsApp());
				form2.setIsFilemanage(form.getIsFilemanage());
//				form2.setFileuploadConfig(form.getFileuploadConfig());
				form2.setIsOrgPower(form.getIsOrgPower());
				form2.setIsEmpPower(form.getIsEmpPower());
				form2.setIsComment(form.getIsComment());
				form2.setFormDefSource(form.getFormDefSource());
				form2.setFormAuthorityIsControl(form.getFormAuthorityIsControl());
				form2.setPowerSql(form.getPowerSql());
				form2.setOrderSql(form.getOrderSql());
				form2.setFormDefEntitySql(form.getFormDefEntitySql());
				form2.setFormDefSql(form.getFormDefSql());
				form2.setFormDefDesc(form.getFormDefDesc());
				if(form.getFormDefLayoutType().equals("10")){
					form2.setExpandField("GRIDBYCARD");
				}else {
					form2.setExpandField(form.getExpandField());
					form2.setExpand1(form.getExpand1());
				}
				
				form2.setOperatorName(userId);
				form2.setOperatorTime(new Date());
				form2.setFormDefInitQzJs(form.getFormDefInitQzJs());
				form2.setFormDefInitQzSql(form.getFormDefInitQzSql());			
				return sysFormconfigMapper.updateByPrimaryKeyWithBLOBs(form2);
			}else if (checkFormCode(form.getFormDefCode(),form.getFormDefId())==1) {
				return 0;
			} else {
				return 2;
			}	
		}
		
		
		//检查接口方案编码是否重复
		public int checkInterfaceCode(String formCode,Long formDefId) {
			SysInterfaceSchemeExample example = new SysInterfaceSchemeExample();
			example.or().andInterfaceSchemeCodeEqualTo(formCode);
			List<SysInterfaceScheme> interfaceSchemess = sysInterfaceSchemeMapper.selectByExample(example);
			if (interfaceSchemess.size()==1) {
				if (formDefId!=null&&interfaceSchemess.get(0).getInterfaceSchemeId().intValue()==formDefId.intValue()) {
					return 0;
				}else {
					return 1;
				}
			}else if (interfaceSchemess.size()>1) {
				return 2;
			}else {
				return 0;
			}
		}
		
		
		//检查表单编码是否重复
		public int checkFormCode(String formCode,BigDecimal formDefId) {
			SysFormconfigExample example = new SysFormconfigExample();
			example.or().andFormDefCodeEqualTo(formCode);
			List<SysFormconfig> forms = sysFormconfigMapper.selectByExample(example);
			if (forms.size()==1) {
				if (formDefId!=null&&forms.get(0).getFormDefId().intValue()==formDefId.intValue()) {
					return 0;
				}else {
					return 1;
				}
			}else if (forms.size()>1) {
				return 2;
			}else {
				return 0;
			}
		}
		//checkQuestionCode检查试卷编码是否重复
		public int checkQuestionCode(String code,BigDecimal id) {
			SysExamtemplateExample example = new SysExamtemplateExample();
			example.or().andCodeEqualTo(code);
			List<SysExamtemplate> exams = sMapper.selectByExample(example);
			if (exams.size()==1) {
				if (id!=null&&exams.get(0).getId().intValue()==id.intValue()) {
					return 0;
				}else {
					return 1;
				}
			}else if (exams.size()>1) {
				return 2;
			}else {
				return 0;
			}
		}
		
		@RequestMapping("/saveTabsForm")
		public @ResponseBody Object saveTabsForm(HttpServletRequest request,@RequestBody SysFormconfigWithBLOBs form) throws Exception{			
			String mstTabId = request.getParameter("mstTabId");
			SysFormconfigMstTab sMstTab = new SysFormconfigMstTab();
			String userId = (String)request.getSession().getAttribute("userid").toString();
			sMstTab.setOperatorName(userId);
			sMstTab.setOperatorTime(new Date());
			int reCode = 0;
			int reCode2 = 0;	
			String[] id = {form.getFormDefId()+"",mstTabId,"1"};
			if(checkFormCode(form.getFormDefCode(),form.getFormDefId())==0){//编码不重复	
				reCode = sysFormconfigMapper.updateByPrimaryKeyWithBLOBs(form);
				if(mstTabId.equals("0")){//逐渐为空
					sMstTab.setMstTabFormId(form.getFormDefId());
					reCode2 = sMstTabMapper.insert(sMstTab);
//					SysFormconfigMstTabExample example = new SysFormconfigMstTabExample();
//					example.or().andMstTabFormIdEqualTo(form.getFormDefId());
					id[1]=sMstTab.getMstTabId()+"";//sMstTabMapper.selectByExample(example).get(0).getMstTabId()+"";
				}else{
					reCode2 = 1;
				}
				if (reCode==1&&reCode2==1) {
					return id;
				} else {
					id[2]="3";
					return id;
				}
			}else if (checkFormCode(form.getFormDefCode(),form.getFormDefId())==1) {
				id[2]="0";
				return id;
			} else {
				id[2]="2";
				return id;
			}
		}
		
		@RequestMapping("/saveFileuploadConfig")
		public @ResponseBody Object saveFileuploadConfig(HttpServletRequest request,@RequestBody SysFileuploadConfig fileuploadConfig) throws Exception{			
			String formId = request.getParameter("formId");
			SysFormconfig form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId));
			String userId = (String)request.getSession().getAttribute("userid").toString();
			fileuploadConfig.setOperatorName(userId);
			fileuploadConfig.setOperatorTime(new Date());
			try {
				if (fileuploadConfig.getId()!=null&&!fileuploadConfig.getId().equals("")) {
					fileuploadConfigMapper.updateByPrimaryKey(fileuploadConfig);
					form.setFileuploadConfig(fileuploadConfig.getId()+"");
					sysFormconfigMapper.updateByPrimaryKey(form);
				} else {
					fileuploadConfigMapper.insert(fileuploadConfig);
					form.setFileuploadConfig(fileuploadConfig.getId()+"");
					sysFormconfigMapper.updateByPrimaryKey(form);
				}
				return "1";
			} catch (Exception e) {
				// TODO: handle exception
				return "0";
			}
		}
		
		
		@RequestMapping("/updataGridFormParamx")
		public @ResponseBody Object updataGridFormParamx(HttpServletRequest request,@RequestBody Map sGrid) throws Exception{
			int flag = 1;
			String userId = (String)request.getSession().getAttribute("userid").toString();
			
			String author1 = request.getParameter("sign");
			DataSign datasign = new DataSign();
			String sGrid1 = sGrid+"";
			sGrid1 = sGrid1.replaceAll("\\s+", "");
			
			if(datasign.UpdataFormparm(request, sGrid1)=="2"){
				return "2";
			}
			SysFormconfigGrid sGrid2 = new SysFormconfigGrid();
			sGrid2.setOperatorName(userId);
			sGrid2.setOperatorTime(new Date());
			if (sGrid.get("formGridId").equals("0")) {
				String formGridFormId = (String)sGrid.get("formGridFormId");
				sGrid2.setFormGridFromId(new BigDecimal(formGridFormId));
				sGrid2.setFormShowPager(sGrid.get("formShowPage")+"");
				String formPageSize = (String)sGrid.get("formPageSize");
				sGrid2.setFormPageSize(new BigDecimal(formPageSize));
				sGrid2.setFormPageSizeList(""+sGrid.get("formPageSizeList"));
				sGrid2.setFormIsIndex(""+sGrid.get("formIsIndex"));
				sGrid2.setFormShowColumns(""+sGrid.get("formShowColumns"));
				sGrid2.setFormEditNextOnEnter(""+sGrid.get("formEditNextOnEnter"));
				sGrid2.setFormEditNextRowCell(""+sGrid.get("formEditNextRowCell"));
				sGrid2.setFormAllowResize(""+sGrid.get("formAllowResize"));
				sGrid2.setFormAllowCellEdit(""+sGrid.get("formAllowCellEdit"));
				sGrid2.setFormAllowCellSelect(""+sGrid.get("formAllowCellSelect"));
				sGrid2.setFormMultiSelect(""+sGrid.get("formMultiSelect"));
				sGrid2.setFormShowTitle(""+sGrid.get("formShowTitle"));
				sGrid2.setFormIndexWidth(""+sGrid.get("formIndexWidth"));
				sGrid2.setFormMultiWidth(""+sGrid.get("formMultiWidth"));
				String formFrozen = (String)sGrid.get("formFrozen");
				sGrid2.setFormFrozen(new BigDecimal(formFrozen));
				sGrid2.setFormGridHeight(""+sGrid.get("formGridHeight"));
				sGrid2.setFormTableIsHandsontable(""+sGrid.get("formTableIsHandsontable"));
				sGrid2.setFormBorderShowType(""+sGrid.get("formBorderShowType"));
				return sysFormconfigGridMapper.insert(sGrid2);
			} else {
				String formGridId = (String)sGrid.get("formGridId")+"";
				sGrid2.setFormGridId(new BigDecimal(formGridId));
				String formGridFormId = (String)sGrid.get("formGridFormId");
				sGrid2.setFormGridFromId(new BigDecimal(formGridFormId));
				sGrid2.setFormShowPager(sGrid.get("formShowPage")+"");
				String formPageSize = (String)sGrid.get("formPageSize");
				sGrid2.setFormPageSize(new BigDecimal(formPageSize));
				sGrid2.setFormPageSizeList(""+sGrid.get("formPageSizeList"));
				sGrid2.setFormIsIndex(""+sGrid.get("formIsIndex"));
				sGrid2.setFormShowColumns(""+sGrid.get("formShowColumns"));
				sGrid2.setFormEditNextOnEnter(""+sGrid.get("formEditNextOnEnter"));
				sGrid2.setFormEditNextRowCell(""+sGrid.get("formEditNextRowCell"));
				sGrid2.setFormAllowResize(""+sGrid.get("formAllowResize"));
				sGrid2.setFormAllowCellEdit(""+sGrid.get("formAllowCellEdit"));
				sGrid2.setFormAllowCellSelect(""+sGrid.get("formAllowCellSelect"));
				sGrid2.setFormMultiSelect(""+sGrid.get("formMultiSelect"));
				sGrid2.setFormShowTitle(""+sGrid.get("formShowTitle"));
				sGrid2.setFormIndexWidth(""+sGrid.get("formIndexWidth"));
				sGrid2.setFormMultiWidth(""+sGrid.get("formMultiWidth"));
				String formFrozen = (String)sGrid.get("formFrozen");
				sGrid2.setFormFrozen(new BigDecimal(formFrozen));
				sGrid2.setFormGridHeight(""+sGrid.get("formGridHeight"));
				sGrid2.setFormTableIsHandsontable(""+sGrid.get("formTableIsHandsontable"));
				sGrid2.setFormBorderShowType(""+sGrid.get("formBorderShowType"));
				return sysFormconfigGridMapper.updateByPrimaryKey(sGrid2);
			}
		}
		
		@RequestMapping("/updataChartsFormParamx")
		public @ResponseBody Object updataChartsFormParamx(HttpServletRequest request,@RequestBody Map sCharts) throws Exception{
			int flag = 1;
			String userId = (String)request.getSession().getAttribute("userid").toString();
			SysFormCharts charts = new SysFormCharts();
			/*charts.setOperatorName(userId);
			charts.setOperatorTime(new Date());*/
			if (sCharts.get("formChartsId").equals("0")) {
				String formChartsFormId = (String)sCharts.get("formChartsFormId");
				charts.setFormChartsFormId(new BigDecimal(formChartsFormId));
				charts.setFormChartsType(sCharts.get("formChartsType")+"");
				charts.setFormChartsColor(sCharts.get("formChartsColor")+"");
				charts.setFormChartsTitle(""+sCharts.get("formChartsTitle"));
				charts.setFormChartsTooltip(""+sCharts.get("formChartsTooltip"));
				charts.setFormChartsToolbox(""+sCharts.get("formChartsToolbox"));
				charts.setFormChartsHideTable(""+sCharts.get("formChartsHideTable"));
				charts.setFormChartsShowLegend(""+sCharts.get("formChartsShowLegend"));
				charts.setFormChartsLegendName(""+sCharts.get("formChartsLegendName"));
				charts.setFormChartsXAxis(""+sCharts.get("formChartsXAxis"));
				charts.setFormChartsYAxis(""+sCharts.get("formChartsYAxis"));
				charts.setFormChartsSelectFormId(new BigDecimal(sCharts.get("formChartsSelectFormId")+""));
				charts.setFormChartsSelectFormName(""+sCharts.get("formChartsSelectFormName"));
				
				charts.setFormChartsSize(""+sCharts.get("formChartsSize"));
				charts.setFormChartsTitleOrient(""+sCharts.get("formChartsTitleOrient"));
				charts.setFormChartsTitlePosition(""+sCharts.get("formChartsTitlePosition"));
				charts.setFormChartsPositionX(""+sCharts.get("formChartsPositionX"));
				charts.setFormChartsPositionY(""+sCharts.get("formChartsPositionY"));
				return sysFormChartsMapper.insert(charts);
			} else {
				String formChartsId = (String)sCharts.get("formChartsId")+"";
				charts.setFormChartsId(new BigDecimal(formChartsId));
				String formChartsFormId = (String)sCharts.get("formChartsFormId");
				charts.setFormChartsFormId(new BigDecimal(formChartsFormId));
				charts.setFormChartsType(sCharts.get("formChartsType")+"");
				charts.setFormChartsColor(sCharts.get("formChartsColor")+"");
				charts.setFormChartsTitle(""+sCharts.get("formChartsTitle"));
				charts.setFormChartsTooltip(""+sCharts.get("formChartsTooltip"));
				charts.setFormChartsToolbox(""+sCharts.get("formChartsToolbox"));
				charts.setFormChartsHideTable(""+sCharts.get("formChartsHideTable"));
				charts.setFormChartsShowLegend(""+sCharts.get("formChartsShowLegend"));
				charts.setFormChartsLegendName(""+sCharts.get("formChartsLegendName"));
				charts.setFormChartsXAxis(""+sCharts.get("formChartsXAxis"));
				charts.setFormChartsYAxis(""+sCharts.get("formChartsYAxis"));
				charts.setFormChartsSelectFormId(new BigDecimal(sCharts.get("formChartsSelectFormId")+""));
				charts.setFormChartsSelectFormName(""+sCharts.get("formChartsSelectFormName"));
				
				charts.setFormChartsSize(""+sCharts.get("formChartsSize"));
				charts.setFormChartsTitleOrient(""+sCharts.get("formChartsTitleOrient"));
				charts.setFormChartsTitlePosition(""+sCharts.get("formChartsTitlePosition"));
				charts.setFormChartsPositionX(""+sCharts.get("formChartsPositionX"));
				charts.setFormChartsPositionY(""+sCharts.get("formChartsPositionY"));
				return sysFormChartsMapper.updateByPrimaryKey(charts);
			}
		}
		
		@RequestMapping("/updataTreeFormParamx")
		public @ResponseBody Object updataTreeFormParamx(HttpServletRequest request,@RequestBody SysFormconfigTree sTree) throws Exception{
			String formTreeId = sTree.getFormTreeId()+"";
			String userId = (String)request.getSession().getAttribute("userid").toString();
			JSONObject json = JSONObject.fromObject(sTree);//将java对象转换为json对象
			String str = json.toString();
			
			DataSign datasign = new DataSign();
			String sGrid1 = str+"";
			sGrid1 = sGrid1.replaceAll("\"", "");
			sGrid1 = sGrid1.replaceAll(":", "=");
			
			sGrid1 = sGrid1.replace("formTreeCustomFun=,formTreeDataField=,", "");
			int index = sGrid1.indexOf("formTreeIdField")-1;
			String sGrid11 =  sGrid1.substring(index);                         
			String author = sGrid1.replace(sGrid11, "}");
			
			if(datasign.UpdataFormparm(request, author)=="2"){
				return "2";
			}
			sTree.setOperatorName(userId);
			SysFormconfigTreeExample example = new SysFormconfigTreeExample();
			example.or().andFormTreeFormIdEqualTo(sTree.getFormTreeFormId());
			sTree.setOperatorTime(new Date());
			if (sysFormconfigTreeMapper.selectByExample(example).size()==0) {
				return sysFormconfigTreeMapper.insert(sTree);
			}else {
				SysFormconfigTree sTree2 = sysFormconfigTreeMapper.selectByExample(example).get(0);
				sTree2.setFormTreeFormId(sTree.getFormTreeFormId());
				sTree2.setFormIsLazy(sTree.getFormIsLazy());
				sTree2.setFormShowTreeIcon(sTree.getFormShowTreeIcon());
				sTree2.setFormResultAsTree(sTree.getFormResultAsTree());
				sTree2.setFormShowTreeLines(sTree.getFormShowTreeLines());
				sTree2.setFormExpandOnClick(sTree.getFormExpandOnClick());
				sTree2.setFormExpandOnDbclick(sTree.getFormExpandOnDbclick());
				sTree2.setFormAutoCheckParent(sTree.getFormAutoCheckParent());
				sTree2.setFormAllowSelect(sTree.getFormAllowSelect());
				sTree2.setFormShowCheckbox(sTree.getFormShowCheckbox());
				sTree2.setFormShowFolderCheckbox(sTree.getFormShowFolderCheckbox());
				sTree2.setFormCheckRecursive(sTree.getFormCheckRecursive());
				sTree2.setFormShowTreeOnly(sTree.getFormShowTreeOnly());
				sTree2.setFormShowTitle(sTree.getFormShowTitle());
				sTree2.setFormShowFilter(sTree.getFormShowFilter());
				sTree2.setFormShowEmpContainer(sTree.getFormShowEmpContainer());
				sTree2.setFormExpandOnLoad(sTree.getFormExpandOnLoad());
				sTree2.setFormTreeWidth("18%");
				return sysFormconfigTreeMapper.updateByPrimaryKey(sTree2);
			}
		}
		@RequestMapping("/updataMstFormParamx")
		public @ResponseBody Object updataMstFormParamx(HttpServletRequest request,@RequestBody SysFormconfigMstTab sMstTab) throws Exception{
			String mstTabId = sMstTab.getMstTabId()+"";
			String userId = (String)request.getSession().getAttribute("userid").toString();
			sMstTab.setOperatorName(userId);
			sMstTab.setOperatorTime(new Date());
			SysFormconfigMstTabExample example = new SysFormconfigMstTabExample();
			example.or().andMstTabFormIdEqualTo(sMstTab.getMstTabFormId());
			if (mstTabId.equals("0")) {
				sMstTabMapper.insert(sMstTab);
				return sMstTabMapper.selectByExample(example).get(0).getMstTabId()+"";
			}else {
			    sMstTabMapper.updateByPrimaryKey(sMstTab);
			    return sMstTabMapper.selectByExample(example).get(0).getMstTabId()+"";
			}
		}
		
		@RequestMapping("/updataFormYkReport")
		public @ResponseBody Object updataFormYkReport(HttpServletRequest request,@RequestBody SysFormYkReport sYkReport) throws Exception{
			String reportId = sYkReport.getReportId()+"";
			String userId = (String)request.getSession().getAttribute("userid").toString();
			sYkReport.setOperatorName(userId);
			sYkReport.setOperatorTime(new Date());
			SysFormYkReportExample example = new SysFormYkReportExample();
			example.or().andReportFormIdEqualTo(sYkReport.getReportFormId());
			if (reportId.equals("0")) {
				sYkReportMapper.insert(sYkReport);
				return sYkReportMapper.selectByExample(example).get(0).getReportId()+"";
			}else {
				sYkReportMapper.updateByPrimaryKey(sYkReport);
			    return sYkReportMapper.selectByExample(example).get(0).getReportId()+"";
			}
		}
		
		@RequestMapping("/saveMstTab")
		public @ResponseBody Object saveMstTab(HttpServletRequest request,@RequestBody SysFormconfigMstTabDetail sMstTabDetail) throws Exception{
			String userId = (String)request.getSession().getAttribute("userid").toString();
			sMstTabDetail.setOperatorName(userId);
			sMstTabDetail.setOperatorTime(new Date());
			sMstTabDetail.setTabDetailConfig("N,Y,Y,N,N");
			int reCode = sMstTabDetailMapper.insert(sMstTabDetail);
			if (reCode==1) {
				return reCode;
			} else {
				return 0;
			}
		}
		
		/**
		 * 保存选择查询字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveSelectFilterColumn")
		public @ResponseBody String[]  saveSelectFilterColumn(HttpServletRequest request,@RequestBody SaveSelectFilterColumnParams param) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			List<Map> columns = param.getColumns();
			
			String formId = param.getFormId();
			String gridFilterId = param.getGridFilterId();
			String userId = (String)request.getSession().getAttribute("userid").toString();
			for (int i = 0; i < columns.size(); i++) {
				Map obj = columns.get(i);
				String formColumnId = (Integer) obj.get("formColumnId")+"";
				
				SysGridFilterColumnExample example = new SysGridFilterColumnExample();
				com.myehr.pojo.formmanage.form.SysGridFilterColumnExample.Criteria criteria = example.createCriteria();
				criteria.andGridColumnIdEqualTo(new BigDecimal(formColumnId));
				List<SysGridFilterColumn> filterColumns = sysGridFilterColumnMapper.selectByExample(example);
				if (filterColumns.size()==0) {
					SysGridFilterColumn sysGridFilterColumn = new SysGridFilterColumn();
					sysGridFilterColumn.setGridColumnFilterId(new BigDecimal(gridFilterId));
					sysGridFilterColumn.setGridColumnLable((String) obj.get("formColumnLable"));
					sysGridFilterColumn.setGridColumnId(new BigDecimal(formColumnId));
					sysGridFilterColumn.setGridColumnFilterRule("=");
					sysGridFilterColumn.setFormColumnSort(new BigDecimal(i));
					sysGridFilterColumn.setGridColumnWidth("200px");
					sysGridFilterColumn.setGridColumnIsIntervais("N");
					sysGridFilterColumn.setOperatorName(userId);
					sysGridFilterColumn.setOperatorTime(new Date());
					sysGridFilterColumnMapper.insert(sysGridFilterColumn);
				}
			}
			
			return recode;
		}
		
		@RequestMapping("/delectMstTab")
		public @ResponseBody Object delectMstTab(HttpServletRequest request) throws Exception{
			String tabDetailId = request.getParameter("tabDetailId");
			int reCode = sMstTabDetailMapper.deleteByPrimaryKey(new BigDecimal(tabDetailId));
			if (reCode==1) {
				return reCode;
			} else {
				return 0;
			}
		}	
		/**
		 * 保存查询字段配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveFilterColumn")
		public @ResponseBody String[]  saveFilterColumn(HttpServletRequest request,@RequestBody List<SysFormWhere> wheres) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			String formId = request.getParameter("formId");
			for (int i = 0; i < wheres.size(); i++) {
				SysFormWhere where = wheres.get(i);
				if (where.getFormWhereId()!=null && !(where.getFormWhereId()+"").equals("")) {
					where.setFormWhereFormDefId(new BigDecimal(formId));
					sysFormWhereMapper.updateByPrimaryKey(where);
				}else{
					where.setFormWhereFormDefId(new BigDecimal(formId));
					sysFormWhereMapper.insert(where);
				}
			}
			
			return recode;
		}
		
		/**
		 * 删除查询字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/deleteFilterColumn")
		public @ResponseBody String[]  deleteFilterColumn(HttpServletRequest request,@RequestBody SysGridFilterColumn filter) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			sysGridFilterColumnMapper.deleteByPrimaryKey(filter.getQueryColumnId());
			
			return recode;
		}
		
		
		/**
		 * 查询按钮配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/queryFormButton")
		public @ResponseBody ResultFilters  queryFormButton(HttpServletRequest request) throws Throwable {
			String formId = request.getParameter("formId");
			
			ResultFilters resultMap = new ResultFilters();
			String sql = "select t.FORM_BUTTON_ID as formButtonId,"
						+"t.FORM_BUTTON_FORM_DEF_ID as formButtonFormDefId,"
						+"t.FORM_BUTTON_TYPE as formButtonType,"
						+"t.FORM_BUTTON_CODE as formButtonCode,"
						+"t.FORM_BUTTON_NAME as formButtonName,"
						+"t.FORM_BUTTON_SORT as formButtonSort,"
						+"t.FORM_BUTTON_FORM_ID as formButtonFormId,"
						+"t.FORM_BUTTON_WIDTH as formButtonWidth,"
						+"t.FORM_BUTTON_HEIGHT as formButtonHeight,"
						+"t.FORM_BUTTON_ICON as formButtonIcon,"
						+"t.FORM_BUTTON_CSS as formButtonCss,"
						+"t.OPERATOR_TIME as operatorTime,"
						+"t.OPERATOR_NAME as operatorName,"
						+"t.show_type as showType,"
						+"t.show_area as showArea,"
						+"t.isShowWeb as isshowweb,"
						+"t.isShowMobile as isshowmobile,"
						
						+"t.FORM_BUTTON_WARNING_INFO as formButtonWarningInfo,"
						+"t.FORM_BUTTON_CHECK_FUN as formButtonCheckFun,"
						+"       (case"
						+"         when ses2.exec_sql_id is not null then"
						+"          '已编写脚本'"
						+"         else"
						+"          '未编写脚本'"
						+"       end) QZESQL,"
						+"       (case"
						+"         when ses.exec_sql_id is not null then"
						+"          '已编写脚本'"
						+"         else"
						+"          '未编写脚本'"
						+"       end) HZESQL"
						+"  from SYS_FORM_BUTTON t"
						+"  left join (select * from sys_exec_sql"
						+"  where exec_sql_type = 'EXECSQL_BUTTON_HZ_TYPE') ses on ses.exec_sql_relaid =t.form_button_id"
						+"  left join (select * from sys_exec_sql"
						+"              where exec_sql_type = 'EXECSQL_BUTTON_QZ_TYPE') ses2 on ses2.exec_sql_relaid = t.form_button_id"
						+"  where t.FORM_BUTTON_FORM_DEF_ID = "+formId
						+"  order by t.FORM_BUTTON_SORT";
			List<Map> filters = tMapperExt.queryByFormDefSql(sql);
			
			resultMap.setRows(filters);
			resultMap.setTotal(filters.size());
			
			return resultMap;
		}
		
		/**
		 * 保存表单按钮配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveFormButton")
		public @ResponseBody String[]  saveFormButton(HttpServletRequest request,@RequestBody List<SysFormButton> buttons) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			String formId = request.getParameter("formId");
			for (int i = 0; i < buttons.size(); i++) {
				SysFormButton button = buttons.get(i);
				if (button.getFormButtonId()!=null && !(button.getFormButtonId()+"").equals("")) {
					button.setFormButtonFormDefId(new BigDecimal(formId));
					sysFormButtonMapper.updateByPrimaryKey(button);
				}else{
					int pkid = pkService.getMaxId("SYS_FORM_BUTTON", "FORM_BUTTON_ID");
					button.setFormButtonId(new BigDecimal(pkid));
					button.setFormButtonFormDefId(new BigDecimal(formId));
					sysFormButtonMapper.insert(button);
				}
			}
			return recode;
		}
		
		/**
		 * 删除表单按钮配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/deleteFormButton")
		public @ResponseBody String[]  deleteFormButton(HttpServletRequest request,@RequestBody SysFormButton button) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			sysFormButtonMapper.deleteByPrimaryKey(button.getFormButtonId());
			
			return recode;
		}
		
		/**
		 * 删除表单分组配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/deleteFormGroup")
		public @ResponseBody String[]  deleteFormGroup(HttpServletRequest request,@RequestBody SysFormGroup group) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			sGroupMapper.deleteByPrimaryKey(group.getFormGroupId());
			SysFormColumnExample example = new SysFormColumnExample();
			example.or().andFormColumnFormDefIdEqualTo(group.getGroupFormDefId()).andFormColumnGroupIdEqualTo(group.getFormGroupId()+"");
			List<SysFormColumn> sColumns = sColumnMapper.selectByExample(example);
			for (SysFormColumn sysFormColumn : sColumns) {
				sysFormColumn.setFormColumnGroupId("");
				sysFormColumnMapper.updateByPrimaryKey(sysFormColumn);
			}
			
			return recode;
		}	
		
		/**
		 * 保存表单分组配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveFormGroup")
		public @ResponseBody String[]  saveFormGroup(HttpServletRequest request,@RequestBody List<SysFormGroup> groups) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			for (int i = 0; i < groups.size(); i++) {
				SysFormGroup group = groups.get(i);
				if (group.getFormGroupId()!=null && !(group.getFormGroupId()+"").equals("")) {
					sGroupMapper.updateByPrimaryKey(group);
				}else{
					sGroupMapper.insert(group);
				}
			}
			return recode;
		}
		
	/*//查询下拉初始值
		@RequestMapping("/findSelectInitData")
		public @ResponseBody List<Map<String,String>> findSelectInitData(HttpServletRequest request) throws Exception{
			SysFormComboboxExample example = new SysFormComboboxExample();
			List<Map<String,String>> rs = new ArrayList<Map<String,String>>();
			String formColumnId = request.getParameter("formColumnId");
			String sqlExpand = "";
			if (formColumnId.split("\\|").length>1&&formColumnId.split("\\|")[0].equals("0000")) {
				sqlExpand = "select form_column_id as DICTVALUE , form_column_lable as DICTENTRY from sys_form_column where form_column_form_def_id = "+formColumnId.split("\\|")[1];
			} else {
				example.or().andComboboxFormColumnIdEqualTo(new BigDecimal(formColumnId));//form_column_show_type = 'show'
				List<SysFormCombobox> sList = sysFormComboboxMapper.selectByExample(example);
				if (sList.get(0).getComboboxGuiInitType().equals("dict")) {
					//查字典
					SysDictType dictType=dictTypeServiceImpl.findSysDictTypeByCode(sList.get(0).getComboboxGuiInitValue());
					List<SysDictEntryExpand> list= serviceImpl.searchDictEntryListByTypeId(dictType.getDictTypeId());
					Map<String,String> m0 = new HashMap<String,String>();
	//				m0.put("text", "<%=LangUtil.getLangByCode((String)session.getAttribute(\"langType\"),\"请选择\")%>");
					m0.put("text", "请选择");
					m0.put("id", "");
					rs.add(m0);
					for (int i = 0; i < list.size(); i++) {
						int flag=0;
						Map<String,String> m1 = new HashMap<String,String>();
						if (flag == 0) {
							m1.put("text", list.get(i).getDictEntryName());
							m1.put("id", list.get(i).getDictEntryCode());
							rs.add(m1);
						}	
					}
				}else if (sList.get(0).getComboboxGuiInitType().equals("1")){
					//默认值
					
				}else if (sList.get(0).getComboboxGuiInitType().equals("sql")) {
					//查sql
					String sql = sExecSqlMapper.selectByPrimaryKey(sList.get(0).getComboboxGuiInitExcsqlId()).getExecSql().toUpperCase();
					List<Map> ids = tMapperExt.queryByFormDefSql(sql);
					Map<String,String> m0 = new HashMap<String,String>();
					m0.put("text", "请选择");
					m0.put("id", "");
					rs.add(m0);
					for (int i = 0; i < ids.size(); i++) {
							Map m1 = new HashMap();
							m1.put("text", ids.get(i).get("DICTENTRY"));
							m1.put("id", ids.get(i).get("DICTVALUE"));
							rs.add(m1);
						}
				}
			}
			if (sqlExpand!=null&&!sqlExpand.equals("")&&!sqlExpand.equals("undefined")) {
				List<Map> ids = tMapperExt.queryByFormDefSql(sqlExpand);
				Map<String,String> m0 = new HashMap<String,String>();
				m0.put("text", "请选择");
				m0.put("id", "");
				rs.add(m0);
				for (int i = 0; i < ids.size(); i++) {
						Map m1 = new HashMap();
						m1.put("text", ids.get(i).get("DICTENTRY"));
						m1.put("id", ids.get(i).get("DICTVALUE"));
						rs.add(m1);
					}
			}
			if (sqlExpand!=null&&!sqlExpand.equals("")&&!sqlExpand.equals("undefined")) {
				List<Map> ids = tMapperExt.queryByFormDefSql(sqlExpand);
				Map<String,String> m0 = new HashMap<String,String>();
				m0.put("text", "请选择");
				m0.put("id", "");
				rs.add(m0);
				for (int i = 0; i < ids.size(); i++) {
						Map m1 = new HashMap();
						m1.put("text", ids.get(i).get("DICTENTRY"));
						m1.put("id", ids.get(i).get("DICTVALUE"));
						rs.add(m1);
					}
			}
			return rs;
		}*/
		
		@RequestMapping("/findJsParamList")
		public @ResponseBody ResultMap findJsParamList(HttpServletRequest request) throws Exception{
			String paramTypeValue =request.getParameter("paramTypeValue");
			SysFormGeneralParamExample example = new SysFormGeneralParamExample();
			example.or().andParamTypeValueEqualTo(new BigDecimal(paramTypeValue)).andParamFromTypeNotEqualTo("mstform");
			
			List<SysFormGeneralParam> sGeneralParams = sGeneralParamMapper.selectByExample(example);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysFormGeneralParam u : sGeneralParams) {
				list_obj.add(u);
			}
			SysFormGeneralParam newSql = new SysFormGeneralParam();
			SysFormGeneralParamExample example1 = new SysFormGeneralParamExample();
			example1.or().andParamTypeValueIsNull().andParamTypeIsNull();		
			if(sGeneralParamMapper.selectByExample(example1).size()>0){
				list_obj.add(sGeneralParamMapper.selectByExample(example1).get(0));
			}else {
				sGeneralParamMapper.insert(newSql);
				list_obj.add(sGeneralParamMapper.selectByExample(example1).get(0));
			}
			
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		@RequestMapping("/findJsParamList2")
		public @ResponseBody ResultMap findJsParamList2(HttpServletRequest request) throws Exception{
			String paramTypeValue =request.getParameter("paramTypeValue");
			SysFormGeneralParamExample example = new SysFormGeneralParamExample();
			example.or().andParamTypeValueEqualTo(new BigDecimal(paramTypeValue)).andParamFromTypeEqualTo("mstform");
			
			List<SysFormGeneralParam> sGeneralParams = sGeneralParamMapper.selectByExample(example);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysFormGeneralParam u : sGeneralParams) {
				list_obj.add(u);
			}
			SysFormGeneralParam newSql = new SysFormGeneralParam();
			SysFormGeneralParamExample example1 = new SysFormGeneralParamExample();
			example1.or().andParamTypeValueIsNull().andParamTypeIsNull();		
			if(sGeneralParamMapper.selectByExample(example1).size()>0){
				list_obj.add(sGeneralParamMapper.selectByExample(example1).get(0));
			}else {
				sGeneralParamMapper.insert(newSql);
				list_obj.add(sGeneralParamMapper.selectByExample(example1).get(0));
			}
			
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		@RequestMapping("/findCheckedByFormId")
		public @ResponseBody ResultMap findCheckedByFormId(HttpServletRequest request) throws Exception{
			String formId =request.getParameter("formId");
			SysFileCheckedExample example = new SysFileCheckedExample();
			example.or().andFormDefIdEqualTo(new BigDecimal(formId));
			List<SysFileChecked> checkeds = sFileCheckedMapper.selectByExample(example);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysFileChecked u : checkeds) {
				list_obj.add(u);
			}
			SysFileChecked newSql = new SysFileChecked();
			SysFileCheckedExample example1 = new SysFileCheckedExample();
			example1.or().andCheckedNameIsNull();		
			if(sFileCheckedMapper.selectByExample(example1).size()>0){
				list_obj.add(sFileCheckedMapper.selectByExample(example1).get(0));
			}else {
				sFileCheckedMapper.insert(newSql);
				list_obj.add(sFileCheckedMapper.selectByExample(example1).get(0));
			}
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		@RequestMapping("/findCheckedPointsBycheckedId")
		public @ResponseBody ResultMap findCheckedPointsBycheckedId(HttpServletRequest request,@RequestBody SysFileCheckedPoint checkedpoint) throws Exception{
			SysFileCheckedPointExample example = new SysFileCheckedPointExample();
			example.or().andCheckedIdEqualTo(checkedpoint.getCheckedId());
			List<SysFileCheckedPoint> checkedPoints = sfCheckedPointMapper.selectByExample(example);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysFileCheckedPoint u : checkedPoints) {
				list_obj.add(u);
			}
			SysFileCheckedPoint newSql = new SysFileCheckedPoint();
			SysFileCheckedPointExample example1 = new SysFileCheckedPointExample();
			example1.or().andCheckedRequireIsNull().andCheckedContentIsNull().andCheckedIdEqualTo(checkedpoint.getCheckedId());		
			List<SysFileCheckedPoint> points = sfCheckedPointMapper.selectByExample(example1);
			if(points.size()>0){
//				list_obj.add(sfCheckedPointMapper.selectByExample(example1).get(0));
			}else {
				newSql.setCheckedId(checkedpoint.getCheckedId());
				sfCheckedPointMapper.insert(newSql);
				list_obj.add(points.get(0));
			}
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		//保存数据
		@RequestMapping("/updateJsParam")
		public @ResponseBody Object updateJsParam(HttpServletRequest request) throws Exception{
			JSONArray jArray=GetRequestJsonUtils.getJsonArray(request);
			String userId = (String)request.getSession().getAttribute("userid").toString();
			@SuppressWarnings("unchecked")
			List<SysFormGeneralParam> sList = JSONArray.toList(jArray,new SysFormGeneralParam(), new JsonConfig()); 
			for (SysFormGeneralParam sGeneralParam : sList) {
				if(!sGeneralParam.getParamName().equals("")&&sGeneralParam.getParamName()!=null){
					sGeneralParam.setOperatorName(userId);
					sGeneralParam.setOperatorTime(new Date());
					sGeneralParamMapper.updateByPrimaryKey(sGeneralParam);
				}
			}
			return null;
	    }
		
		//保存数据
		@RequestMapping("/clearCheckedAndPoint")
		public void clearCheckedAndPoint(HttpServletRequest request) throws Exception{
			SysFileCheckedPointExample example = new SysFileCheckedPointExample();
			example.or().andCheckedRequireIsNull().andCheckedContentIsNull();
			sfCheckedPointMapper.deleteByExample(example);
			SysFileCheckedExample example2 = new SysFileCheckedExample();
			example2.or().andCheckedNameIsNull();
			sFileCheckedMapper.deleteByExample(example2);
	    }
		
		//保存数据
		@RequestMapping("/updateCheckeds")
		public @ResponseBody Object updateCheckeds(HttpServletRequest request,@RequestBody List<SysFileChecked> checkeds) throws Exception{
			String userId = (String)request.getSession().getAttribute("userid").toString();
			for (SysFileChecked checked : checkeds) {
				if(!checked.getCheckedName().equals("")&&checked.getCheckedName()!=null){
					checked.setOperatorName(userId);
					checked.setOperatorTime(new Date());
					sFileCheckedMapper.updateByPrimaryKey(checked);
				}
			}
			return null;
	    }
		
		//保存数据
		@RequestMapping("/updateCheckedPoints")
		public @ResponseBody Object updateCheckedPoints(HttpServletRequest request,@RequestBody List<SysFileCheckedPoint> checkedPoints) throws Exception{
			String userId = (String)request.getSession().getAttribute("userid").toString();
			for (SysFileCheckedPoint checkedPoint : checkedPoints) {
				if(!checkedPoint.getCheckedRequire().equals("")&&checkedPoint.getCheckedRequire()!=null){
					checkedPoint.setOperatorName(userId);
					if(checkedPoint.getIsAchieve()==null){
						checkedPoint.setIsAchieve("N");
					}
					checkedPoint.setOperatorTime(new Date());
					sfCheckedPointMapper.updateByPrimaryKey(checkedPoint);
				}
			}
			return null;
	    }
		
		//保存数据
		@RequestMapping("/updateForms")
		public @ResponseBody Object updateForms(HttpServletRequest request,@RequestBody List<SysFormColumn> columns) throws Exception{
			for (SysFormColumn column : columns) {
				SysFormColumn column1 = sysFormColumnMapper.selectByPrimaryKey(column.getFormColumnId());
				if (column.getFormColumnAlign()!=null&&!column.getFormColumnAlign().equals("")) {
					column1.setFormColumnAlign(column.getFormColumnAlign());
				}
				if (column.getFormColumnWidth()!=null&&!column.getFormColumnWidth().equals("")) {
					column1.setFormColumnWidth(column.getFormColumnWidth());	
				}
				if (column.getFormColumnRequired()!=null&&!column.getFormColumnRequired().equals("")) {
					column1.setFormColumnRequired(column.getFormColumnRequired());
				}
				if (column.getFormColumnGroupId()!=null&&!column.getFormColumnGroupId().equals("")) {
					column1.setFormColumnGroupId(column.getFormColumnGroupId());
				}
				if (column.getFormColumnShowType()!=null&&!column.getFormColumnShowType().equals("")) {
					column1.setFormColumnShowType(column.getFormColumnShowType());
				}
				sysFormColumnMapper.updateByPrimaryKey(column1);
			}
			return null;
	    }
		
		@RequestMapping("/deleteJsParam")
		public @ResponseBody Object deleteJsParam(HttpServletRequest request,@RequestBody SysFormGeneralParam sGeneralParam) throws Exception{
			SysFormGeneralParamExample example = new SysFormGeneralParamExample();
			example.or().andParamNameEqualTo(sGeneralParam.getParamName()).andParamFromTypeNotEqualTo("mstform");			
			return sGeneralParamMapper.deleteByExample(example);
	    }
		@RequestMapping("/deleteJsParam2")
		public @ResponseBody Object deleteJsParam2(HttpServletRequest request,@RequestBody SysFormGeneralParam sGeneralParam) throws Exception{
			SysFormGeneralParamExample example = new SysFormGeneralParamExample();
			example.or().andParamNameEqualTo(sGeneralParam.getParamName()).andParamFromTypeEqualTo("mstform");			
			return sGeneralParamMapper.deleteByExample(example);
	    }
		@RequestMapping("/deleteChecked")
		public @ResponseBody Object deleteChecked(HttpServletRequest request,@RequestBody SysFileChecked checked) throws Exception{
			SysFileCheckedPointExample example = new SysFileCheckedPointExample();
			example.or().andCheckedIdEqualTo(checked.getId());
			sfCheckedPointMapper.deleteByExample(example);
			return sFileCheckedMapper.deleteByPrimaryKey(checked.getId());
	    }
		@RequestMapping("/deleteCheckedPoint")
		public @ResponseBody Object deleteCheckedPoint(HttpServletRequest request,@RequestBody SysFileCheckedPoint checkedPoint) throws Exception{
			return sfCheckedPointMapper.deleteByPrimaryKey(checkedPoint.getId());
	    }
		
		/**
		 * 查找表单参数树
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/selectParamTree")
		public @ResponseBody Object selectParamTree(HttpServletRequest request) throws Exception{
			String paramTypeValue =request.getParameter("paramTypeValue");
			List<TreeModel> tList =new ArrayList<TreeModel>();
			TreeModel test1 = new TreeModel();
			test1.setId("SYS_FORM_INIT_DATA_TYPE");
			test1.setIsParent(true);
			test1.setName("参数类");
			test1.setOpen(false);
			test1.setPid("");
			tList.add(test1);
			//全加载
			List<SysDictEntryExpand> list= serviceImpl.searchDictEntryListByTypeId(61);
			for (SysDictEntryExpand sysDictEntryExpand : list) {
				TreeModel test2 = new TreeModel();
				test2.setId(sysDictEntryExpand.getDictEntryCode());
				test2.setIsParent(true);
				test2.setName(sysDictEntryExpand.getDictEntryName());
				test2.setOpen(false);
				test2.setPid("SYS_FORM_INIT_DATA_TYPE");
				SysFormGeneralParamExample example2 = new SysFormGeneralParamExample();
				example2.or().andParamFromTypeEqualTo(sysDictEntryExpand.getDictEntryCode()).andParamTypeValueEqualTo(new BigDecimal(paramTypeValue));
				List<SysFormGeneralParam> sList = sGeneralParamMapper.selectByExample(example2);
				if (sList.size()>0) {
					tList.add(test2);				
				}
				for (SysFormGeneralParam sGeneralParam : sList) {
					TreeModel test3 = new TreeModel();
					test3.setId(String.valueOf(sGeneralParam.getParamId()));
					test3.setIsParent(false);
					test3.setName(sGeneralParam.getParamName());
					test3.setOpen(true);
					test3.setPid(sysDictEntryExpand.getDictEntryCode());
					tList.add(test3);
				}
			}
			JSONArray jsonArray=JSONArray.fromObject(tList);
	    	String str=jsonArray.toString();
	    	String newStr = str.replaceAll("pid","pId");
	    	JSONArray json = JSONArray.fromObject(newStr);
			return json;
		}
		
		@RequestMapping("/getRealSql")
		public @ResponseBody Object getRealSql(HttpServletRequest request) throws Exception{
			String entitySql = request.getParameter("entitySql");
			return getRealSql(entitySql);
		}
		
		public String getRealSql(String entitySql) {
			if(entitySql==null){
				return null;
			}
			String result = new String(entitySql);
			
			//获取所有实体字段名并替换为表字段名
			String reg = "\\[[^c:^p:^s:^r:].*?\\]";
			Pattern p = Pattern.compile(reg);
	        Matcher m = p.matcher(entitySql);
	        try {
	        while(m .find()){
	        	   String ret = m.group();
	        	   if(ret.indexOf(".*")>-1){  //[xx.*]的处理
	        		   String tableChinesName = ret.substring(ret.indexOf("[")+1,ret.indexOf(".*]"));
	        		   SysEntityExample example = new SysEntityExample();
	        		   example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	        		   //String ss = sEntityMapper.selectByExample(example).get(0).getEntityTablename();
	        		   SysEntity entity = sEntityMapper.selectByExample(example).get(0);
	        		   SysFieldExample example2 = new SysFieldExample();
	        		   example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId()));
	        		   List<SysField> fields = sFieldMapper.selectByExample(example2);
	        		   String ss = "";
	        		   int i = 0;
	        		   for (SysField sysField : fields) {
	        			   if(i==0){
	        				   ss = entity.getEntityCode()+"."+sysField.getFieldCode()+" AS "+sysField.getFieldTablename();
	        			   }else {
	        				   ss = ss+","+ entity.getEntityCode()+"."+sysField.getFieldCode()+" AS "+sysField.getFieldTablename();
	        			   }
	        			   i++;
	        		   }
	        		   //大改
	        		   result = result.replace(""+ret+"",ss);
	        		   continue;
	        	   }
	        	   String reg1="\\[.+\\..*?\\]";
	        	   Pattern p1 = Pattern.compile(reg1);
	        	   Matcher m1 = p1.matcher(ret);
	               if(m1.find()){  //[xx.zz]的处理
	               		String temp = m1.group();
	               		String tableChinesName = temp.substring(ret.indexOf("[")+1,ret.indexOf("."));
	               		String columnChinaname = ret.substring(ret.indexOf(".")+1,ret.indexOf("]"));
	               		SysEntityExample example = new SysEntityExample();
	               		example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	               		SysEntity entity = sEntityMapper.selectByExample(example).get(0);
	               		SysFieldExample example2 = new SysFieldExample();
	               		example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldChinaNameEqualTo(columnChinaname);
	               		List<SysField> obj =  sFieldMapper.selectByExample(example2);
	               		if(obj.size()==0){
	               			return "通过实体中文名:\""+tableChinesName+"\"和字段中文名\""+columnChinaname+"\"无法获取对应的数据库字段名!";
	               		}
	               		//String fieldTablename = obj.get(0).getFieldTablename();
	               		String fieldTablename = obj.get(0).getFieldCode();
	               		String entityTableName =entity.getEntityTablename();
	               		//小改
	               		result = result.replace(""+temp+"",entityTableName+"."+fieldTablename+" AS "+ ChangeCode.getUniqueCode(entityTableName, fieldTablename));
	               		continue;
	               }
	               String reg2="\\[.+.*?\\]";
	               Pattern p2 = Pattern.compile(reg2);
	        	   Matcher m2 = p2.matcher(ret);
	               if(m2.find()){ //[xx]的处理
	               		String temp = m2.group();
	               		String tableChinesName = temp.substring(ret.indexOf("[")+1,ret.indexOf("]"));
	               		SysEntityExample example = new SysEntityExample();
	               		example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	               		List<SysEntity> entitys = sEntityMapper.selectByExample(example);
	               		if (entitys.size()==0) {
							continue;
						}
	               		SysEntity entity2 = entitys.get(0);
	               		if(entity2==null||entity2.getEntityTablename()==null){
	            			return "通过实体中文名:\""+tableChinesName+"\"无法获取对应数据库表名!";
	               		}
	               		String entityName = entity2.getEntityTablename();
	               		result = result.replace(""+temp+"",entityName);
	               		continue;
	               }
	        }
		} catch (Exception e) {
			e.getMessage();
		}
	        //获取非实体列字段
	        //获取第一个select 到 from 之间的字符串
			return result;
		}
		@RequestMapping("/getRealSqlCN")
		public @ResponseBody Object getRealSqlCN(HttpServletRequest request) throws Exception{
			String entitySql = request.getParameter("entitySql");
			Map map = new HashMap();
			map.put("sql", getRealSqlCN(entitySql));
			return map;
		}
		public String getRealSqlCN(String entitySql) {
			if(entitySql==null){
				return null;
			}
			String result = new String(entitySql);
			//获取所有实体字段名并替换为表字段名
			String reg = "\\[[^c:^p:^s:^r:].*?\\]";
			Pattern p = Pattern.compile(reg);
	        Matcher m = p.matcher(entitySql);
	        while(m .find()){
	        	   String ret = m.group();
	        	   if(ret.indexOf(".*")>-1){  //[xx.*]的处理
	        		   String tableChinesName = ret.substring(ret.indexOf("[")+1,ret.indexOf(".*]"));
	        		   SysEntityExample example = new SysEntityExample();
	        		   example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	        		   //String ss = sEntityMapper.selectByExample(example).get(0).getEntityTablename();
	        		   SysEntity entity = sEntityMapper.selectByExample(example).get(0);
	        		   SysFieldExample example2 = new SysFieldExample();
	        		   example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId()));
	        		   List<SysField> fields = sFieldMapper.selectByExample(example2);
	        		   String ss = "";
	        		   int i = 0;
	        		   for (SysField sysField : fields) {
	        			   if(i==0){
	        				   ss = entity.getEntityCode()+"."+sysField.getFieldCode()+" AS "+sysField.getFieldChinaName();
	        			   }else {
	        				   ss = ss+","+ entity.getEntityCode()+"."+sysField.getFieldCode()+" AS "+sysField.getFieldChinaName();
	        			   }
	        			   i++;
	        		   }
	        		   //大改
	        		   result = result.replace(""+ret+"",ss);
	        		   continue;
	        	   }
	        	   String reg1="\\[.+\\..*?\\]";
	        	   Pattern p1 = Pattern.compile(reg1);
	        	   Matcher m1 = p1.matcher(ret);
	               if(m1.find()){  //[xx.zz]的处理
	               		String temp = m1.group();
	               		String tableChinesName = temp.substring(ret.indexOf("[")+1,ret.indexOf("."));
	               		String columnChinaname = ret.substring(ret.indexOf(".")+1,ret.indexOf("]"));
	               		SysEntityExample example = new SysEntityExample();
	               		example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	               		SysEntity entity = sEntityMapper.selectByExample(example).get(0);
	               		SysFieldExample example2 = new SysFieldExample();
	               		example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldChinaNameEqualTo(columnChinaname);
	               		List<SysField> obj =  sFieldMapper.selectByExample(example2);
	               		if(obj.size()==0){
	               			return "通过实体中文名:\""+tableChinesName+"\"和字段中文名\""+columnChinaname+"\"无法获取对应的数据库字段名!";
	               		}
	               		//String fieldTablename = obj.get(0).getFieldTablename();
	               		String fieldTablename = obj.get(0).getFieldCode();
	               		String entityTableName =entity.getEntityTablename();
	               		String fieldChina = obj.get(0).getFieldChinaName();
	               		//小改
	               		result = result.replace(""+temp+"",entityTableName+"."+fieldTablename+" AS "+ fieldChina);
	               		continue;
	               }
	               String reg2="\\[.+.*?\\]";
	               Pattern p2 = Pattern.compile(reg2);
	        	   Matcher m2 = p2.matcher(ret);
	               if(m2.find()){ //[xx]的处理
	               		String temp = m2.group();
	               		String tableChinesName = temp.substring(ret.indexOf("[")+1,ret.indexOf("]"));
	               		SysEntityExample example = new SysEntityExample();
	               		example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	               		SysEntity entity2 = sEntityMapper.selectByExample(example).get(0);
	               		if(entity2==null||entity2.getEntityTablename()==null){
	            			return "通过实体中文名:\""+tableChinesName+"\"无法获取对应数据库表名!";
	               		}
	               		String entityName = entity2.getEntityTablename();
	               		result = result.replace(""+temp+"",entityName);
	               		continue;
	               }
	        }
	        //获取非实体列字段
	        //获取第一个select 到 from 之间的字符串
			return result;
		}
		
		@RequestMapping("/findColumnListSteps_3")
		public @ResponseBody Object findColumnListSteps_3(HttpServletRequest request) throws Exception{
			String formDefId =request.getParameter("formDefId");
			String entitySql =request.getParameter("entitySql");  
			SysFormconfigWithBLOBs form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
			logger.info(getRealSql(entitySql));
			form.setFormDefSql(getRealSql(entitySql));
			form.setFormDefEntitySql(entitySql);
			return sysFormconfigMapper.updateByPrimaryKeyWithBLOBs(form);
			
		}
		
		@RequestMapping("/saveInterfaceSchemeSql")
		public @ResponseBody Object saveInterfaceSchemeSql(HttpServletRequest request) throws Exception{
			String interfaceSchemeId =request.getParameter("interfaceSchemeId");
			String entitySql = new String(request.getParameter("entitySql").getBytes("iso-8859-1"), "utf-8");
			SysInterfaceSchemeWithBLOBs interfaceScheme = sysInterfaceSchemeMapper.selectByPrimaryKey(Long.parseLong(interfaceSchemeId));
			logger.info(getRealSql(entitySql));
			interfaceScheme.setInterfaceSchemeDefSql(getRealSql(entitySql));
			interfaceScheme.setInterfaceSchemeEntitySql(entitySql);
			return sysInterfaceSchemeMapper.updateByPrimaryKeyWithBLOBs(interfaceScheme);
			
		}
		
		@RequestMapping("/findColumnListSteps_2")
		public @ResponseBody Object findColumnListSteps_2(HttpServletRequest request) throws Exception{
			String formDefId =request.getParameter("formDefId");
			String entitySql = request.getParameter("entitySql");
			//删除表单对应字段表中字段
			/*SysFormColumnExample examplex = new SysFormColumnExample();
			examplex.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formDefId));
			sColumnMapper.deleteByExample(examplex);*/

			List<BigDecimal> columnIds = new ArrayList<BigDecimal>();
			columnIds.add(new BigDecimal(0));
			int columnNum = 0;
			Map  sparams = new HashMap();
			sparams.put("entitySql", entitySql);
			entitySql = entitySql.replaceAll("[\\t\\n\\r]", "");
			Object[] res =  new Object[2];
			List<SysFormColumn> list = new ArrayList<SysFormColumn>();
			String otherColumn = "";
			/*if (true) {*/
				//分析实体sql对应需要保存的表
				//String reg = "(from)|(FROM) {1,}[\\[][.]{1,}\\.[.]{1,}[\\]]";
				String sreg = "[fF][rR][oO][mM].*?\\[.*?\\]";
				Pattern sp = Pattern.compile(sreg);
				Matcher sm = sp.matcher(entitySql);
				String temp1 = null;
				String saveChinaEntityname = null;
				while(sm.find()){
					temp1 = sm.group();
					break;
				}
				if(temp1!=null){
					//获取实体名称
					sreg = "\\[.+\\]";
					sp = Pattern.compile(sreg);
					sm = sp.matcher(temp1);
					while(sm.find()){
						saveChinaEntityname = sm.group();
						saveChinaEntityname = saveChinaEntityname.replaceAll("\\[", "");
						saveChinaEntityname = saveChinaEntityname.replaceAll("\\]", "");
						break;
					}
				}
				String tablename = null;
				
				if(saveChinaEntityname!=null){
					//获取表对应实体ID
					 Map params2 = new HashMap();
					 params2.put("tableChinaName", saveChinaEntityname);
					 SysEntityExample example = new SysEntityExample();
					 example.or().andEntityChinanameEqualTo(saveChinaEntityname).andDeleteMarkEqualTo("N");
					 List<SysEntity> sEntitys = sEntityMapper.selectByExample(example);
					 if(sEntitys.size()>0){
						 tablename = sEntitys.get(0).getEntityTablename();
						 //在表单配置表中增加保存字段
					 }
				}
				
				// 获取可执行sql
				String realSql  = getRealSql(entitySql);
				
				
				sparams.put("excSql", realSql);
				sparams.put("tablename", tablename);
				sparams.put("formId", formDefId);
				if(!otherColumn.equals("")){
					otherColumn = otherColumn.substring(0,otherColumn.length()-1);
				}
				sparams.put("otherColumn", otherColumn);
				
				
				//获取第一个select 到from 之间的查询字段
				String reg = "[sS][eE][lL][eE][cC][tT].*?[fF][rR][oO][mM]";
				Pattern p = Pattern.compile(reg);
				Matcher m = p.matcher(entitySql);
				String columns = null;
				while(m.find()){
					columns = m.group();
					//logger.info(columns);
					break;
				}
				if(columns==null){
					throw new Exception("实体SQL解析错误");
				}
				columns = columns.substring(6,columns.length()-4).trim();
				
				String[] arrcolumn = columns.split(",");
				int indexSort = 1;
				SysFieldExample example2 = new SysFieldExample();
				example2.setOrderByClause("FIELD_SORT");
				for (int i = 0; i < arrcolumn.length; i++) {
					String temp = arrcolumn[i];
					if (temp.indexOf("[") >= 0 && temp.indexOf("]") >= 0 && temp.indexOf(".") >= 0) {
						 String tableChinesName = temp.substring(temp.indexOf("[")+1,temp.indexOf("."));
						 String columChinaName =  temp.substring(temp.indexOf(".")+1,temp.indexOf("]"));
						 if("*".equals(columChinaName)) {
							 Map params = new HashMap<String,String>();
							 
							 params.put("tableChinaName", tableChinesName);
							 SysEntityExample example = new SysEntityExample();
							 example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
			               	 SysEntity entity = sEntityMapper.selectByExample(example).get(0);
			               	 example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andDeleteMarkEqualTo("N");
			               	 example2.setOrderByClause("FIELD_SORT");
			               	 List<SysField> objs = sFieldMapper.selectByExample(example2);
							 for(int k=0; k<objs.size(); k++){
								 SysFormColumn obj = new SysFormColumn();
								 obj.setFormEntityTablename(entity.getEntityTablename());
								 obj.setFormColumnEntityId(objs.get(k).getFieldEntityId());
								 
								 obj.setFormColumnLable(objs.get(k).getFieldChinaName());
								 obj.setFormColumnColumnId(objs.get(k).getFieldId());
								 obj.setFormFieldTablename(objs.get(k).getFieldCode());
								 obj.setFormColumnUsName(objs.get(k).getFieldCode());
								 obj.setFormColumnHeight("");
								 obj.setFormColumnWidth("200px");
								 if("Y".equals(objs.get(k).getFieldIsKey())){
									 obj.setFormColumnShowType("hide");
								 }else {
									 obj.setFormColumnShowType("show");
									 if(entity.getEntityTablename()!=null){
										 
										 if(!entity.getEntityTablename().equals(tablename)) {
											 obj.setFormColumnShowType("readonly");
										 }
									 }
								 }
								 
								 obj.setFormColumnRowSpan(new BigDecimal("1"));
								 if (objs.get(k).getControlType()!=null&&objs.get(k).getControlType().equals("2")) {//实体字段为下拉控件的话
									 obj.setFormColumnGuiType("2");
									 sFieldMapper.selectByPrimaryKey(objs.get(k).getFieldId());
								 } else {
									 obj.setFormColumnGuiType("1");
								 }
								 
								 obj.setFormColumnType("1");
								 obj.setFormColumnAlign("left");
								 obj.setFormColumnRequired("N");
								 obj.setFormColumnSort(new BigDecimal(indexSort+""));
								 if("VARCHAR2".equals(objs.get(k).getFieldType())){
									 obj.setFormColumnMaxLength(new BigDecimal(Math.round(Math.floor(Integer.parseInt(objs.get(k).getFieldLen())/3))));
								 }else{
									 obj.setFormColumnMaxLength(new BigDecimal(objs.get(k).getFieldLen()));
								 }
								 indexSort++;  
								 //formColumnSort
								 //list.add(obj);
								 obj.setFormColumnFormDefId(new BigDecimal(formDefId));
								 //formEntityTablename,formFieldTablename,formColumnLable
								 SysFormColumnExample exampleColumn = new SysFormColumnExample();
								 exampleColumn.or().andFormEntityTablenameEqualTo(obj.getFormEntityTablename()).andFormFieldTablenameEqualTo(obj.getFormFieldTablename()).andFormColumnFormDefIdEqualTo(obj.getFormColumnFormDefId());
								 List<SysFormColumn> columnList = sColumnMapper.selectByExample(exampleColumn);
								 if (columnList.size()>0) {
									logger.info("已存在!");
									columnIds.add(columnList.get(0).getFormColumnId());
								 } else {
									 sColumnMapper.insert(obj);
									 //如果是关联下拉实体字段,默认增加控件信息
									 if (objs.get(k).getControlType()!=null&&objs.get(k).getControlType().equals("2")) {//实体字段为下拉控件的话
										 SysFormComboboxExample example3 = new SysFormComboboxExample();
										 example3.or().andComboboxIsfieldEqualTo("true").andComboboxFormColumnIdEqualTo(objs.get(k).getFieldId());
										 if (sComboboxMapper.selectByExample(example3).size()>0) {
											 SysFormCombobox combobox = sComboboxMapper.selectByExample(example3).get(0);
											 combobox.setComboboxFormColumnId(obj.getFormColumnId());
											 combobox.setComboboxIsfield("false");
											 sComboboxMapper.insert(combobox);
											 if (combobox.getComboboxGuiInitType()!=null&&combobox.getComboboxGuiInitType().equals("sql")) {//调用SQL下拉,关联下拉sql表
												SysExecSql execSql = sExecSqlMapper.selectByPrimaryKey(combobox.getComboboxGuiInitExcsqlId());
												execSql.setExecSqlRelaid(Long.valueOf(combobox.getComboboxId()+""));
												sExecSqlMapper.insert(execSql);
												combobox.setComboboxGuiInitExcsqlId(execSql.getExecSqlId());
												sComboboxMapper.updateByPrimaryKey(combobox);
											 }
										 } 
									 }
									 columnIds.add(sColumnMapper.selectByExample(exampleColumn).get(0).getFormColumnId());
								 }
								 
							 }
							 
						 }else {
							 SysFormColumn obj = new SysFormColumn();
							 //com.dcf.form.entitySql.queryEntityTableByChineseName
							 SysEntityExample example = new SysEntityExample();
							 example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
							 SysEntity entity = sEntityMapper.selectByExample(example).get(0);
							 if(entity!=null){
//								 obj.put("entityChinaname", tableChinesName);
								 obj.setFormEntityTablename(entity.getEntityTablename());
								 obj.setFormColumnEntityId(new BigDecimal(entity.getEntityId()));
								 
							 }
//							 /queryColumnByChineseName
							 //DataObject[] objs2  = queryColumnByColumnChinaname(columChinaName,tableChinesName);
							 SysFieldExample example3 = new SysFieldExample();
							 example3.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldChinaNameEqualTo(columChinaName);
							 List<SysField> objs2 = sFieldMapper.selectByExample(example3);
							 if(objs2!=null&&objs2.size()>0){
								 obj.setFormColumnLable(objs2.get(0).getFieldChinaName());
								 obj.setFormColumnColumnId(objs2.get(0).getFieldId());
								 obj.setFormColumnLable(objs2.get(0).getFieldChinaName()); 
								 obj.setFormFieldTablename(objs2.get(0).getFieldCode()); 
								 obj.setFormColumnUsName(objs2.get(0).getFieldCode());
								 if (objs2.get(0).getControlType()!=null&&objs2.get(0).getControlType().equals("2")) {//实体字段为下拉控件的话
									 obj.setFormColumnGuiType("2");
									 sFieldMapper.selectByPrimaryKey(objs2.get(0).getFieldId());
								 } else {
									 obj.setFormColumnGuiType("1");
								 }
							 }
							 obj.setFormColumnHeight("");
							 obj.setFormColumnWidth("200px");
							 if("Y".equals(objs2.get(0).getFieldIsKey())){
								 obj.setFormColumnShowType("hide");
							 }else {
								 obj.setFormColumnShowType("show");
								 if(entity.getEntityTablename()!=null){
									 
									 if(!entity.getEntityTablename().equals(tablename)) {
										 obj.setFormColumnShowType("readonly");
									 }
								 }
							 }
							 obj.setFormColumnRowSpan(new BigDecimal("1"));
							 obj.setFormColumnType("1");
							 obj.setFormColumnGuiType("1");
							 obj.setFormColumnAlign("left");
							 obj.setFormColumnRequired("N");
							 obj.setFormColumnSort(new BigDecimal(indexSort+""));
							 if("VARCHAR2".equals(objs2.get(0).getFieldType())){
								 obj.setFormColumnMaxLength(new BigDecimal(Math.round(Math.floor(Integer.parseInt(objs2.get(0).getFieldLen())/3))));
							 }else{
								 obj.setFormColumnMaxLength(new BigDecimal(objs2.get(0).getFieldLen()));
							 }
							 indexSort++;  
							 //list.add(obj);
							 obj.setFormColumnFormDefId(new BigDecimal(formDefId));
							 SysFormColumnExample exampleColumn = new SysFormColumnExample();
							 exampleColumn.or().andFormEntityTablenameEqualTo(obj.getFormEntityTablename()).andFormFieldTablenameEqualTo(obj.getFormFieldTablename()).andFormColumnFormDefIdEqualTo(obj.getFormColumnFormDefId());
							 List<SysFormColumn> columnList = sColumnMapper.selectByExample(exampleColumn);
							 if (columnList.size()>0) {
								logger.info("已存在!");
								columnIds.add(columnList.get(0).getFormColumnId());
							 } else {
								 sColumnMapper.insert(obj);
								 if (objs2.get(0).getControlType()!=null&&objs2.get(0).getControlType().equals("2")) {//实体字段为下拉控件的话
									 SysFormComboboxExample example4 = new SysFormComboboxExample();
									 example4.or().andComboboxIsfieldEqualTo("true").andComboboxFormColumnIdEqualTo(objs2.get(0).getFieldId());
									 if (sComboboxMapper.selectByExample(example4).size()>0) {
										 SysFormCombobox combobox = sComboboxMapper.selectByExample(example4).get(0);
										 combobox.setComboboxFormColumnId(obj.getFormColumnId());
										 combobox.setComboboxIsfield("false");
										 sComboboxMapper.insert(combobox);
										 if (combobox.getComboboxGuiInitType()!=null&&combobox.getComboboxGuiInitType().equals("sql")) {//调用SQL下拉,关联下拉sql表
											SysExecSql execSql = sExecSqlMapper.selectByPrimaryKey(combobox.getComboboxGuiInitExcsqlId());
											execSql.setExecSqlRelaid(Long.valueOf(combobox.getComboboxId()+""));
											sExecSqlMapper.insert(execSql);
											combobox.setComboboxGuiInitExcsqlId(execSql.getExecSqlId());
											sComboboxMapper.updateByPrimaryKey(combobox);
										 }
									 } 
								 }
								 columnIds.add(sColumnMapper.selectByExample(exampleColumn).get(0).getFormColumnId());
							 }
						 }
						 
						 logger.info("tablename:"+tableChinesName);
					 }else { //非实体字段
						 otherColumn += temp+",";
						 String[] array=temp.split("\\.");
						 if(array.length>1){
							 String[] array1 = array[0].split("'");
							 if(array1[1].toUpperCase().equals("VIR")){
								 String formFieldTablename = "VIR_"+array[1].split("'")[0].toUpperCase();
								 logger.info(formFieldTablename);
								 SysFormColumn obj = new SysFormColumn();
								 obj.setFormEntityTablename("");
								 obj.setFormColumnEntityId(new BigDecimal("0"));
								 //fieldChinaname
								 obj.setFormColumnLable("");
								 obj.setFormColumnLable("虚拟字段");
								 obj.setFormColumnColumnId(new BigDecimal("13557"));
								 obj.setFormFieldTablename(formFieldTablename);
								 obj.setFormColumnUsName(formFieldTablename);
								 obj.setFormColumnHeight("");
								 obj.setFormColumnWidth("200px");
								 obj.setFormColumnShowType("show");
								 obj.setFormColumnRowSpan(new BigDecimal("1"));
								 obj.setFormColumnType("2");
								 obj.setFormColumnGuiType("1");
								 obj.setFormColumnAlign("left");
								 obj.setFormColumnRequired("N");
								 obj.setFormColumnSort(new BigDecimal(indexSort+""));
								 indexSort++;  
								 //formColumnSort
								 //list.add(obj);
								 obj.setFormColumnFormDefId(new BigDecimal(formDefId));
								 SysFormColumnExample exampleColumn = new SysFormColumnExample();
								 exampleColumn.or().andFormEntityTablenameEqualTo(obj.getFormEntityTablename()).andFormFieldTablenameEqualTo(obj.getFormFieldTablename()).andFormColumnFormDefIdEqualTo(obj.getFormColumnFormDefId());
								 List<SysFormColumn> columnList = sColumnMapper.selectByExample(exampleColumn);
								 if (columnList.size()>0) {
									logger.info("已存在!");
									columnIds.add(columnList.get(0).getFormColumnId());
								 } else {
									 sColumnMapper.insert(obj);
									 columnIds.add(sColumnMapper.selectByExample(exampleColumn).get(0).getFormColumnId());
								 }
							 }else {
								 String tableCode = array[0];
								 String columCode = array[1];
								 SysFormColumn obj = new SysFormColumn();
								 //com.dcf.form.entitySql.queryEntityTableByChineseName
								 SysEntityExample example = new SysEntityExample();
								 example.or().andEntityCodeEqualTo(tableCode).andDeleteMarkEqualTo("N");
								 SysEntity entity = sEntityMapper.selectByExample(example).get(0);
								 if(entity!=null){
//									 obj.put("entityChinaname", tableChinesName);
									 obj.setFormEntityTablename(entity.getEntityTablename());
									 obj.setFormColumnEntityId(new BigDecimal(entity.getEntityId()));
									 
								 }
//								 /queryColumnByChineseName
								 //DataObject[] objs2  = queryColumnByColumnChinaname(columChinaName,tableChinesName);
								 SysFieldExample example3 = new SysFieldExample();
								 example3.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldCodeEqualTo(columCode);
								 List<SysField> objs2 = sFieldMapper.selectByExample(example3);
								 if(objs2!=null&&objs2.size()>0){
									 obj.setFormColumnLable(objs2.get(0).getFieldChinaName());
									 obj.setFormColumnColumnId(objs2.get(0).getFieldId());
									 obj.setFormColumnLable(objs2.get(0).getFieldChinaName()); 
									 obj.setFormFieldTablename(objs2.get(0).getFieldCode()); 
									 obj.setFormColumnUsName(objs2.get(0).getFieldCode());
								 }
								 obj.setFormColumnHeight("");
								 obj.setFormColumnWidth("200px");
								 if("Y".equals(objs2.get(0).getFieldIsKey())){
									 obj.setFormColumnShowType("hide");
								 }else {
									 obj.setFormColumnShowType("show");
									 if(entity.getEntityTablename()!=null){
										 
										 if(!entity.getEntityTablename().equals(tablename)) {
											 obj.setFormColumnShowType("readonly");
										 }
									 }
								 }
								 obj.setFormColumnRowSpan(new BigDecimal("1"));
								 obj.setFormColumnType("1");
								 obj.setFormColumnGuiType("1");
								 obj.setFormColumnAlign("left");
								 obj.setFormColumnRequired("N");
								 obj.setFormColumnSort(new BigDecimal(indexSort+""));
								 if("VARCHAR2".equals(objs2.get(0).getFieldType())){
									 obj.setFormColumnMaxLength(new BigDecimal(Math.round(Math.floor(Integer.parseInt(objs2.get(0).getFieldLen())/3))));
								 }else{
									 obj.setFormColumnMaxLength(new BigDecimal(objs2.get(0).getFieldLen()));
								 }
								 indexSort++;  
								 //list.add(obj);
								 obj.setFormColumnFormDefId(new BigDecimal(formDefId));
								 SysFormColumnExample exampleColumn = new SysFormColumnExample();
								 exampleColumn.or().andFormEntityTablenameEqualTo(obj.getFormEntityTablename()).andFormFieldTablenameEqualTo(obj.getFormFieldTablename()).andFormColumnFormDefIdEqualTo(obj.getFormColumnFormDefId());
								 List<SysFormColumn> columnList = sColumnMapper.selectByExample(exampleColumn);
								 if (columnList.size()>0) {
									logger.info("已存在!");
									columnIds.add(columnList.get(0).getFormColumnId());
								 } else {
									 sColumnMapper.insert(obj);
									 columnIds.add(sColumnMapper.selectByExample(exampleColumn).get(0).getFormColumnId());
								 }
								
							}
						 }else {
							 String[] array2 = temp.split("as ");
							 if(array2[1].split("_")[0].toUpperCase().equals("VIR")){
								 String formFieldTablename = array2[1].toUpperCase();
								 logger.info(formFieldTablename);
								 SysFormColumn obj = new SysFormColumn();
								 obj.setFormEntityTablename("");
								 obj.setFormColumnEntityId(new BigDecimal("0"));
								 //fieldChinaname
								 obj.setFormColumnLable("");
								 obj.setFormColumnLable("虚拟字段");
								 obj.setFormColumnColumnId(new BigDecimal("13557"));
								 obj.setFormFieldTablename(formFieldTablename);
								 obj.setFormColumnUsName(formFieldTablename);
								 obj.setFormColumnHeight("");
								 obj.setFormColumnWidth("200px");
								 obj.setFormColumnShowType("show");
								 obj.setFormColumnRowSpan(new BigDecimal("1"));
								 obj.setFormColumnType("2");
								 obj.setFormColumnGuiType("1");
								 obj.setFormColumnAlign("left");
								 obj.setFormColumnRequired("N");
								 obj.setFormColumnSort(new BigDecimal(indexSort+""));
								 indexSort++;  
								 //formColumnSort
								 //list.add(obj);
								 obj.setFormColumnFormDefId(new BigDecimal(formDefId));
								 SysFormColumnExample exampleColumn = new SysFormColumnExample();
								 exampleColumn.or().andFormEntityTablenameEqualTo(obj.getFormEntityTablename()).andFormFieldTablenameEqualTo(obj.getFormFieldTablename()).andFormColumnFormDefIdEqualTo(obj.getFormColumnFormDefId());
								 List<SysFormColumn> columnList = sColumnMapper.selectByExample(exampleColumn);
								 if (columnList.size()>0) {
									logger.info("已存在!");
									columnIds.add(columnList.get(0).getFormColumnId());
								 } else {
									 sColumnMapper.insert(obj);
									 columnIds.add(sColumnMapper.selectByExample(exampleColumn).get(0).getFormColumnId());
								 }
							 }
						}
					 }
				}
				SysFormColumnExample exColumnExample = new SysFormColumnExample();
				exColumnExample.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formDefId)).andFormColumnIdNotIn(columnIds);
				sColumnMapper.deleteByExample(exColumnExample);
				res[0] = entitySql;
				res[1] = realSql;
				//获取非实体列 //获取select 到 from 之间的字符串
				sFormconfigMapperExpand.updateFormEntitySql(sparams);
				return res;
				/*} else {
				res[0] = "-2";
				return res;
			}*/
		}
	
		@RequestMapping("/findColumnList")
		public @ResponseBody ResultMap findColumnList(HttpServletRequest request) throws Exception{
			String formDefId =request.getParameter("formDefId");
			SysFormColumnExample sColumnExample = new SysFormColumnExample();
			sColumnExample.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formDefId));
			sColumnExample.setOrderByClause("FORM_COLUMN_SORT");
			List<SysFormColumn> sColumns = sColumnMapper.selectByExample(sColumnExample);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysFormColumn u : sColumns) {
				list_obj.add(u);
			}
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		@RequestMapping("/findQuestionList")
		public @ResponseBody ResultMap findQuestionList(HttpServletRequest request) throws Exception{
			String templateId =request.getParameter("templateId");
			SysExamtemplateQuestionExample example = new SysExamtemplateQuestionExample();
			example.or().andTemplateidEqualTo(Integer.valueOf(templateId));
			example.setOrderByClause("XORDER");
			List<SysExamtemplateQuestion> questions = questionMapper.selectByExample(example);
			ResultMap map = new ResultMap();
			List<Object> list_obj=new ArrayList<Object>();
			for (SysExamtemplateQuestion u : questions) {
				list_obj.add(u);
			}
			map.setRows(list_obj);
			long size = list_obj.size();
			map.setTotal(size);
			return map;
		}
		
		//保存数据
		@RequestMapping("/saveColumn")
		public @ResponseBody String saveColumn(HttpServletRequest request,@RequestBody SysFormColumn column) throws Exception{
			String reCode = "false";
			try {
				sColumnMapper.updateByPrimaryKey(column);
				//加载字段缓存
				sysformconfigService.setColumn(column.getFormColumnId()+"");
				reCode = "true";
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		
		//保存图表字段配置数据
		@RequestMapping("/saveChartsColumn")
		public @ResponseBody String saveColumn(HttpServletRequest request,@RequestBody Map column) throws Exception{
			String reCode = "false";
			try {
				String formColumnId =column.get("formColumnId")+"";
				String chartsFormId =column.get("chartsFormId")+"";
				FormChartsBarColumnExample example = new FormChartsBarColumnExample();
				example.or().andSelectFormColumnIdEqualTo(new BigDecimal(formColumnId)).andChartsFormIdEqualTo(new BigDecimal(chartsFormId));
				List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(example);
				if(barColumns.size()>0){
					FormChartsBarColumn barColumn = barColumns.get(0);
					barColumn.setSelectFormColumnId(new BigDecimal(column.get("formColumnId")+""));
					barColumn.setSelectFormColumnTable(column.get("formEntityTablename")+"");
					barColumn.setSelectFormColumnName(column.get("formFieldTablename")+"");
					barColumn.setSelectFormColumnLable(column.get("formColumnLable")+"");
					barColumn.setChartsBarColumnType(column.get("barChartsColumnType")+"");
					barColumn.setChartsFormId(new BigDecimal(column.get("chartsFormId")+""));
					barColumn.setChartsBarColumnColor(column.get("barChartsColumnColor")+"");
					barColumn.setChartsPieType(column.get("barChartsPieType")+"");
					barColumn.setPieOuterRingDiameter(column.get("pieOuterRingDiameter")+"");
					barColumn.setPieInnerRingDiameter(column.get("pieInnerRingDiameter")+"");
					formChartsBarColumnMapper.updateByPrimaryKey(barColumn);
				}else{
					FormChartsBarColumn barColumn = new  FormChartsBarColumn();
					barColumn.setSelectFormColumnId(new BigDecimal(column.get("formColumnId")+""));
					barColumn.setChartsFormId(new BigDecimal(column.get("chartsFormId")+""));
					barColumn.setSelectFormColumnTable(column.get("formEntityTablename")+"");
					barColumn.setSelectFormColumnName(column.get("formFieldTablename")+"");
					barColumn.setSelectFormColumnLable(column.get("formColumnLable")+"");
					barColumn.setChartsBarColumnType(column.get("barChartsColumnType")+"");
					barColumn.setChartsBarColumnColor(column.get("barChartsColumnColor")+"");
					barColumn.setChartsPieType(column.get("barChartsPieType")+"");
					barColumn.setPieOuterRingDiameter(column.get("pieOuterRingDiameter")+"");
					barColumn.setPieInnerRingDiameter(column.get("pieInnerRingDiameter")+"");
					formChartsBarColumnMapper.insert(barColumn);
				}
				
				//sColumnMapper.updateByPrimaryKey(column);
				reCode = "true";
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		
		
		//保存图表字段配置数据
		@RequestMapping("/getChartsColumnInfo")
		public @ResponseBody FormChartsBarColumn getChartsColumnInfo(HttpServletRequest request) throws Exception{
			String reCode = "false";
			try {
				String columnId =request.getParameter("columnId");
				String formId =request.getParameter("formId");
				FormChartsBarColumnExample example = new FormChartsBarColumnExample();
				com.myehr.pojo.formmanage.form.FormChartsBarColumnExample.Criteria  criteria= example.createCriteria();
				criteria.andSelectFormColumnIdEqualTo(new BigDecimal(columnId));
				criteria.andChartsFormIdEqualTo(new BigDecimal(formId));
				List<FormChartsBarColumn> barColumns = formChartsBarColumnMapper.selectByExample(example);
				
				if(barColumns.size()>0){
					return barColumns.get(0);
				}

				return null;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
	    }
		
		@RequestMapping("/getTextInfoByColumnId")
		public @ResponseBody SysFormTextbox getTextInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormTextboxExample example = new SysFormTextboxExample();
			example.or().andTextboxFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormTextbox> sTextboxs = sTextboxMapper.selectByExample(example);
			if(sTextboxs.size()>0){
				return sTextboxs.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getDateInfoByColumnId")
		public @ResponseBody SysFormDatepicker getDateInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormDatepickerExample example = new SysFormDatepickerExample();
			example.or().andDatepickerFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormDatepicker> sDatepickers = sDatepickerMapper.selectByExample(example);
			if(sDatepickers.size()==1){
				return sDatepickers.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getComboboxInfoByColumnId")
		public @ResponseBody SysFormCombobox getComboboxInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormComboboxExample example = new SysFormComboboxExample();
			example.or().andComboboxFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormCombobox> sComboboxs = sComboboxMapper.selectByExample(example);
			if(sComboboxs.size()==1){
				return sComboboxs.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getComboboxInfoByFieldId")
		public @ResponseBody SysFormCombobox getComboboxInfoByFieldId(HttpServletRequest request) throws Exception{
			String fieldId =request.getParameter("fieldId");
			SysFormComboboxExample example = new SysFormComboboxExample();
			example.or().andComboboxFormColumnIdEqualTo(new BigDecimal(fieldId)).andComboboxIsfieldEqualTo("true");
			List<SysFormCombobox> sComboboxs = sComboboxMapper.selectByExample(example);
			SysField field = sFieldMapper.selectByPrimaryKey(new BigDecimal(fieldId));
			field.setControlType("2");
			sFieldMapper.updateByPrimaryKey(field);
			if(sComboboxs.size()==1){//查出来回显
				return sComboboxs.get(0);
			}else if(sComboboxs.size()==0){//查不到,新增一条回显
				SysFormCombobox combobox = new SysFormCombobox();
				combobox.setComboboxIsfield("true");
				combobox.setComboboxFormColumnId(new BigDecimal(fieldId));
				sComboboxMapper.insert(combobox);
				return combobox;
			}else {//查到多条,提示
				return null;
			}
		}
		
		@RequestMapping("/getTextareaInfoByColumnId")
		public @ResponseBody SysFormTextarea getTextareaInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormTextareaExample example = new SysFormTextareaExample();
			example.or().andTextareaFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormTextarea> sTextareas = sTextareaMapper.selectByExample(example);
			if(sTextareas.size()==1){
				return sTextareas.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getCheckboxlistInfoByColumnId")
		public @ResponseBody SysFormCheckboxlist getCheckboxlistInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormCheckboxlistExample example = new SysFormCheckboxlistExample();
			example.or().andCheckboxlistFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormCheckboxlist> checkboxlists = sCheckboxlistMapper.selectByExample(example);
			if (checkboxlists.size()==1) {
				return checkboxlists.get(0);
			} else {
				return null;
			}
		}
		
		@RequestMapping("/getRadiolistInfoByColumnId")
		public @ResponseBody SysFormRadiobuttonlist getRadiolistInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormRadiobuttonlistExample example = new SysFormRadiobuttonlistExample();
			example.or().andRadiolistFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormRadiobuttonlist> sRadiobuttonlists = sRadiobuttonlistMapper.selectByExample(example);
			if(sRadiobuttonlists.size()==1){
				return sRadiobuttonlists.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getCheckboxInfoByColumnId")
		public @ResponseBody SysFormCheckbox getCheckboxInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormCheckboxExample example = new SysFormCheckboxExample();
			example.or().andCheckboxFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormCheckbox> sCheckboxs = sCheckboxMapper.selectByExample(example);
			if(sCheckboxs.size()==1){
				return sCheckboxs.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getLookupInfoByColumnId")
		public @ResponseBody SysFormLookup getLookupInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormLookupExample example = new SysFormLookupExample();
			example.or().andLookupFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormLookup> sLookups = sLookupMapper.selectByExample(example);
			if(sLookups.size()==1){
				return sLookups.get(0);
			}else {
				return null;
			}
		}
		
		@RequestMapping("/getFileuploadInfoByColumnId")
		public @ResponseBody SysFormFileupload getFileuploadInfoByColumnId(HttpServletRequest request) throws Exception{
			String columnId =request.getParameter("columnId");
			SysFormFileuploadExample example = new SysFormFileuploadExample();
			example.or().andFileuploadFormColumnIdEqualTo(new BigDecimal(columnId));
			List<SysFormFileupload> sFileuploads = sFileuploadMapper.selectByExample(example);
			if(sFileuploads.size()==1){
				return sFileuploads.get(0);
			}else {
				return null;
			}
		}
		
		//保存数据
		@RequestMapping("/saveTextBox")
		public @ResponseBody String saveTextBox(HttpServletRequest request,@RequestBody SysFormTextbox sTextbox) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sTextbox.setOperatorName(userId);
			sTextbox.setOperatorTime(new Date());
			try {
				if(sTextbox.getTextboxId()!=null&&!sTextbox.getTextboxId().equals("")){
					sTextboxMapper.updateByPrimaryKey(sTextbox);
					reCode = "true";
				}else {
					sTextboxMapper.insert(sTextbox);
					reCode = "true";
				}
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveDatePicker")
		public @ResponseBody Object saveDatePicker(HttpServletRequest request,@RequestBody SysFormDatepicker sDatepicker) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sDatepicker.setOperatorName(userId);
			sDatepicker.setOperatorTime(new Date());
			try {
				if(sDatepicker.getDatepickerId()!=null&&!sDatepicker.getDatepickerId().equals("")){
					sDatepickerMapper.updateByPrimaryKey(sDatepicker);
					reCode = "true";
				}else {
					sDatepickerMapper.insert(sDatepicker);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveCombobox")
		public @ResponseBody String saveCombobox(HttpServletRequest request,@RequestBody SysFormCombobox sCombobox) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sCombobox.setOperatorName(userId);
			sCombobox.setOperatorTime(new Date());
			try {
				if(sCombobox.getComboboxId()!=null&&!sCombobox.getComboboxId().equals("")){
					sComboboxMapper.updateByPrimaryKey(sCombobox);
					reCode = "true";
				}else {
					sComboboxMapper.insert(sCombobox);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveTextarea")
		public @ResponseBody String saveTextarea(HttpServletRequest request,@RequestBody SysFormTextarea sTextarea) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sTextarea.setOperatorName(userId);
			sTextarea.setOperatorTime(new Date());
			try {
				if(sTextarea.getTextareaId()!=null&&!sTextarea.getTextareaId().equals("")){
					sTextareaMapper.updateByPrimaryKey(sTextarea);
					reCode = "true";
				}else {
					sTextareaMapper.insert(sTextarea);
					reCode = "true";
				}
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveCheckbox")
		public @ResponseBody String saveCheckbox(HttpServletRequest request,@RequestBody SysFormCheckbox sCheckbox) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sCheckbox.setOperatorName(userId);
			sCheckbox.setOperatorTime(new Date());
			try {
				if(sCheckbox.getCheckboxId()!=null&&!sCheckbox.getCheckboxId().equals("")){
					sCheckboxMapper.updateByPrimaryKey(sCheckbox);
					reCode = "true";
				}else {
					sCheckboxMapper.insert(sCheckbox);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveRadiolist")
		public @ResponseBody String saveRadiolist(HttpServletRequest request,@RequestBody SysFormRadiobuttonlist sRadiobuttonlist) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sRadiobuttonlist.setOperatorName(userId);
			sRadiobuttonlist.setOperatorTime(new Date());
			try {
				if(sRadiobuttonlist.getRadiolistId()!=null&&!sRadiobuttonlist.getRadiolistId().equals("")){
					sRadiobuttonlistMapper.updateByPrimaryKey(sRadiobuttonlist);
					reCode = "true";
				}else {
					sRadiobuttonlistMapper.insert(sRadiobuttonlist);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }		
		//保存数据
		@RequestMapping("/saveCheckboxlist")
		public @ResponseBody String saveCheckboxlist(HttpServletRequest request,@RequestBody SysFormCheckboxlist sCheckboxlist) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sCheckboxlist.setOperatorName(userId);
			sCheckboxlist.setOperatorTime(new Date());
			try {
				if(sCheckboxlist.getCheckboxlistId()!=null&&!sCheckboxlist.getCheckboxlistId().equals("")){
					sCheckboxlistMapper.updateByPrimaryKey(sCheckboxlist);
					reCode = "true";
				}else {
					sCheckboxlistMapper.insert(sCheckboxlist);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveLookup")
		public @ResponseBody String saveLookup(HttpServletRequest request,@RequestBody SysFormLookup sLookup) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sLookup.setOperatorName(userId);
			sLookup.setOperatorTime(new Date());
			try {
				if(sLookup.getLookupId()!=null&&!sLookup.getLookupId().equals("")){
					sLookupMapper.updateByPrimaryKey(sLookup);
					reCode = "true";
				}else {
					sLookupMapper.insert(sLookup);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }
		//保存数据
		@RequestMapping("/saveFileupload")
		public @ResponseBody String saveFileupload(HttpServletRequest request,@RequestBody SysFormFileupload sFileupload) throws Exception{
			String reCode = "false";
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			sFileupload.setOperatorName(userId);
			sFileupload.setOperatorTime(new Date());
			try {
				if(sFileupload.getFileuploadId()!=null&&!sFileupload.getFileuploadId().equals("")){
					sFileuploadMapper.updateByPrimaryKey(sFileupload);
					reCode = "true";
				}else {
					sFileuploadMapper.insert(sFileupload);
					reCode = "true";
				}
				
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
	    }	
		
		@RequestMapping("/getFormNameById")
		public @ResponseBody SysFormconfig getFormNameById(HttpServletRequest request) throws Exception{
			String formDefId =request.getParameter("formDefId");
			if (formDefId!=null&&!formDefId.equals("null")) {
				return sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formDefId));
			} else {
				return null;
			}
			
		}
		
		@RequestMapping("/getGroupNameById")
		public @ResponseBody SysFormGroup getGroupNameById(HttpServletRequest request) throws Exception{
			String formGroupId =request.getParameter("formGroupId");
			
			return sGroupMapper.selectByPrimaryKey(new BigDecimal(formGroupId));
		}
		
		@RequestMapping("/getDictnameByCode")
		public @ResponseBody SysDictType getDictnameByCode(HttpServletRequest request) throws Exception{
			String dictTypeCode =request.getParameter("dictTypeCode");
			SysDictTypeExample example = new SysDictTypeExample();
			example.or().andDictTypeCodeEqualTo(dictTypeCode);
			if (sDictTypeMapper.selectByExample(example).size()>0) {
				return sDictTypeMapper.selectByExample(example).get(0);
			} else {
				return null;
			}
			
		}
		
		//查询所有表单信息
		@RequestMapping("/findDictList")
		 public @ResponseBody ResultMap findDictList(HttpServletRequest request) throws Exception {
			SysRequestParam param = new SysRequestParam();
			param.setRequest(request);
			param.setSession(request.getSession());
			param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
			param.setPath(request.getRealPath("/jsp/formbuild/")+"");
			param.setIsInit(request.getParameter("isInit")+"");
			param.setIsView(request.getParameter("isView")+"");
			param.setContextPath(request.getContextPath()+"");
		        try {  
		            return formService.getAllDictMsSql(param);  
		        } catch (Exception e) {  
		            e.printStackTrace();logger.error(e.getMessage(),e);  
		        }  
		        return null; 
		}
		
		/**
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/findSelectInitData")
		public @ResponseBody List<Map<String,String>> findSelectInitData(HttpServletRequest request,@RequestBody DictDataParams params) throws Exception{
			
			Map containerParam = params.getContainerParam();
			
			SysFormComboboxExample example = new SysFormComboboxExample();
			List<Map<String,String>> rs = new ArrayList<Map<String,String>>();
			
			String formColumnId = request.getParameter("formColumnId");
			String sqlExpand = "";
			if (formColumnId.split("\\|").length>1&&formColumnId.split("\\|")[0].equals("0000")) {//当前表单字段
				sqlExpand = "select form_column_id as DICTVALUE , form_column_lable as DICTENTRY from sys_form_column where form_column_form_def_id = "+formColumnId.split("\\|")[1];
			}else if(formColumnId.split("\\|").length>1&&formColumnId.split("\\|")[0].equals("0001")){
				sqlExpand = "select form_group_id as DICTVALUE , form_group_name as DICTENTRY from sys_form_group where GROUP_FORM_DEF_ID = "+formColumnId.split("\\|")[1];
			}else if(formColumnId.split("\\|").length>1&&formColumnId.split("\\|")[0].equals("0002")){
				sqlExpand = "select form_group_id as DICTVALUE , form_group_name as DICTENTRY from sys_template_group where GROUP_TEMPLATE_ID = "+formColumnId.split("\\|")[1];
			}else {
				example.or().andComboboxFormColumnIdEqualTo(new BigDecimal(formColumnId));//form_column_show_type = 'show'
				List<SysFormCombobox> sList = sysFormComboboxMapper.selectByExample(example);
				if (sList.get(0).getComboboxGuiInitType().equals("dict")) {
					List<SysDictEntry> entryList = sysformconfigService.getDictEntrys(sList.get(0).getComboboxGuiInitValue());
					Map<String,String> m0 = new HashMap<String,String>();
//					m0.put("text", "<%=LangUtil.getLangByCode((String)session.getAttribute(\"langType\"),\"请选择\")%>");
					m0.put("text", "请选择");
					m0.put("id", "");
					rs.add(m0);
					for (int i = 0; i < entryList.size(); i++) {
						int flag=0;
						Map<String,String> m1 = new HashMap<String,String>();
						if (flag == 0) {
							m1.put("text", entryList.get(i).getDictEntryName());
							m1.put("id", entryList.get(i).getDictEntryCode());
							rs.add(m1);
						}	
					}
				}else if (sList.get(0).getComboboxGuiInitType().equals("1")){
				}else if (sList.get(0).getComboboxGuiInitType().equals("sql")) {
					String getFormSql = "select sys_formconfig.FORM_DEF_SOURCE from sys_formconfig left join  SYS_FORM_COLUMN on sys_formconfig.FORM_DEF_ID = SYS_FORM_COLUMN.FORM_COLUMN_FORM_DEF_ID where SYS_FORM_COLUMN.FORM_COLUMN_ID = "+sList.get(0).getComboboxFormColumnId();
					Map form = (Map) MybatisUtil.queryList("runtime.selectSql", getFormSql).get(0);
					logger.info(form.get("FORM_DEF_SOURCE")+"");
					if (form.get("FORM_DEF_SOURCE")!=null&&form.get("FORM_DEF_SOURCE").equals("oracle1")) {
						CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
					}
					//查sql
					String sql = sExecSqlMapper.selectByPrimaryKey(sList.get(0).getComboboxGuiInitExcsqlId()).getExecSql();
					sql = MyEhrJDBCUtil.repleaceContainerParam(containerParam, sql);
					List<Map> ids = MybatisUtil.queryList("runtime.selectSql", sql);
					CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
					Map<String,String> m0 = new HashMap<String,String>();
					m0.put("text", "请选择");
					m0.put("id", "");
					rs.add(m0);
					for (int i = 0; i < ids.size(); i++) {
						Map m1 = new HashMap();
						m1.put("text", ids.get(i).get("DICTENTRY"));
						m1.put("id", ids.get(i).get("DICTVALUE"));
						rs.add(m1);
					}
				}
			}
			if (sqlExpand!=null&&!sqlExpand.equals("")&&!sqlExpand.equals("undefined")) {
				List<Map> ids = tMapperExt.queryByFormDefSql(sqlExpand);
				Map<String,String> m0 = new HashMap<String,String>();
				m0.put("text", "请选择");
				m0.put("id", "");
				rs.add(m0);
				for (int i = 0; i < ids.size(); i++) {
						Map m1 = new HashMap();
						m1.put("text", ids.get(i).get("DICTENTRY"));
						m1.put("id", ids.get(i).get("DICTVALUE"));
						rs.add(m1);
					}
			}
			return rs;
		}
		
		@RequestMapping("/findSelectInitDataByFormId")
		public @ResponseBody List<Map<String,String>> findSelectInitDataByFormId(HttpServletRequest request) throws Exception{
			String formId = request.getParameter("formId");
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			long startTime = System.currentTimeMillis();//获取开始时间
			SysFormconfigCache formCache = sysformconfigService.getForm(formId);
			long endTime = System.currentTimeMillis();    //获取结束时间
			logger.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
			return null;
		}
		
		public List<SysDictEntry> getRealList(List<SysDictEntry> realList,List<SysDictEntry> list,SysDictEntry sysDictEntry){ 
			if (sysDictEntry.getDictParentCode()==null) {
				realList.add(sysDictEntry);
			}
			for (int i = 0; i < list.size(); i++) {
				SysDictEntry entryExpand = list.get(i);
				if (entryExpand.getDictParentCode()!=null && entryExpand.getDictParentCode().equals(sysDictEntry.getDictEntryId()+"")) {
					int level = list.get(i).getDictEntrySeq().split("\\.").length-2;
					entryExpand.setDictEntryName(getNbsp(level)+entryExpand.getDictEntryName());
					realList.add(entryExpand);
					getRealList(realList,list,entryExpand);
				}
			}
			
			return realList;
		}
		
		//判断字符串是否为数字
		public String getNbsp(int num){
			String str = "";
			for (int i = 0; i < num; i++) {
				str+="--";
			}
			return str;
		}
		
		/**
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/selectFormField")
		public @ResponseBody Object selectFormField(HttpServletRequest request) throws Exception{
			String formId = request.getParameter("formId");
			
			List<Map> tList =new ArrayList<Map>();
			
			SysFormColumnExample example = new SysFormColumnExample();
			com.myehr.pojo.formmanage.form.SysFormColumnExample.Criteria criteria = example.createCriteria();
			criteria.andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
			List<SysFormColumn> columns = sysFormColumnMapper.selectByExample(example);
			
			Map test1 = new HashMap();
			test1.put("id", formId);
			test1.put("isParent",true);
			test1.put("name","表单字段树");
			test1.put("open",true);
			test1.put("pId","root");
			tList.add(test1);
			//全加载
			for (SysFormColumn column : columns) {
				Map test2 = new HashMap();
				test2.put("id",column.getFormColumnId()+"");
				test2.put("isParent",false);
				test2.put("name",column.getFormColumnLable());
				test2.put("code",ChangeCode.getUniqueCode(column.getFormEntityTablename().toUpperCase(), column.getFormFieldTablename().toUpperCase()));
				test2.put("field",column.getFormFieldTablename());
				test2.put("open",false);
				test2.put("pId",formId);
				tList.add(test2);
			}
			JSONArray jsonArray=JSONArray.fromObject(tList);
	    	String str=jsonArray.toString();
	    	String newStr = str.replaceAll("pid","pId");
	    	JSONArray json = JSONArray.fromObject(newStr);
			return json;
		}
		
		/**
		 * 查询列表过滤
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/queryGridFilterByFormId")
		public @ResponseBody SysGridFilter queryGridFilterByFormId(HttpServletRequest request) throws Exception{
			String formId = request.getParameter("formId");
			
			SysGridFilterExample example = new SysGridFilterExample();
			com.myehr.pojo.formmanage.form.SysGridFilterExample.Criteria criteria = example.createCriteria();
			criteria.andGridFilterFormDefIdEqualTo(new BigDecimal(formId));
			List<SysGridFilter> gridFilters = sysGridFilterMapper.selectByExample(example);
			if (gridFilters.size()>0) {
				return gridFilters.get(0);
			}else{
				SysGridFilter gridFilter = new SysGridFilter();
				gridFilter.setGridFilterFormDefId(new BigDecimal(formId));
				gridFilter.setGridFilterRowCount(new BigDecimal(3));
				gridFilter.setGridFilterHeightGrade("false");
				gridFilter.setGridFilterLableWidth("100");
				sysGridFilterMapper.insert(gridFilter);
				return sysGridFilterMapper.selectByExample(example).get(0);
			}
		}
		
		/**
		 * 保存列表过滤
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/updateGridFilter")
		public @ResponseBody SysGridFilter updateGridFilter(HttpServletRequest request,@RequestBody Map gridFilter) throws Exception{
			String formId = request.getParameter("formId");
			
			SysGridFilterExample example = new SysGridFilterExample();
			com.myehr.pojo.formmanage.form.SysGridFilterExample.Criteria criteria = example.createCriteria();
			criteria.andGridFilterFormDefIdEqualTo(new BigDecimal(formId));
			List<SysGridFilter> gridFilters = sysGridFilterMapper.selectByExample(example);
			if (gridFilters.size()>0) {
				SysGridFilter filter = gridFilters.get(0);
				filter.setGridFilterFormDefId(new BigDecimal(formId));
				filter.setGridFilterRowCount(new BigDecimal(gridFilter.get("gridFilterRowCount")+""));
				filter.setGridFilterHeightGrade(gridFilter.get("gridFilterHeightGrade")+"");
				filter.setGridFilterLableWidth(gridFilter.get("gridFilterLableWidth")+"");
				sysGridFilterMapper.updateByPrimaryKey(filter);
				return filter;
			}
			return null;
		}
		
		/**
		 * 保存过滤配置
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/saveFilterColumnData")
		public @ResponseBody String[]  saveFilterColumnData(HttpServletRequest request,@RequestBody SaveFilterColumnDataParams param) throws Throwable {
			String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
			
			List<Map> filterColumns = param.getColumns();
			
			for (int i = 0; i < filterColumns.size(); i++) {
				SysGridFilterColumn filterColumn = new SysGridFilterColumn();
				Map obj = filterColumns.get(i);
				filterColumn.setQueryColumnId(new BigDecimal((String)obj.get("queryColumnId")));
				filterColumn.setGridColumnId(new BigDecimal((String)obj.get("gridColumnId")));
				filterColumn.setGridColumnLable((String)obj.get("gridColumnLable"));
				filterColumn.setGridColumnFilterRule((String)obj.get("gridColumnFilterRule"));
				filterColumn.setGridColumnWidth((String)obj.get("gridColumnWidth"));
				filterColumn.setGridColumnColSpan((String)obj.get("gridColumnColSpan"));
				filterColumn.setGridColumnIsIntervais((String)obj.get("gridColumnIsIntervais"));
				filterColumn.setFormColumnSort(new BigDecimal((String)obj.get("formColumnSort")));
				filterColumn.setGridColumnFilterId(new BigDecimal((String)obj.get("gridColumnFilterId")));
				
				if (filterColumn.getQueryColumnId()!=null && !(filterColumn.getQueryColumnId()+"").equals("")) {
					sysGridFilterColumnMapper.updateByPrimaryKey(filterColumn);
				}
			}
			
			return recode;
		}

		/**
		 * 新增虚拟字段
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/addColumn")
		public @ResponseBody Object  addColumn(HttpServletRequest request,@RequestBody SysFormColumn column) throws Throwable {			
			return sColumnMapper.insert(column);
		}
		
		//删除数据
		@RequestMapping("/deleteColumns")
		public @ResponseBody String deleteColumns(HttpServletRequest request,@RequestBody SysFormconfigAndGrid forms) throws Exception{
			String formColumnId ;
			String reCode = "false";
			try {
				for (int j = 0; j < forms.getFormsBloBs().size(); j++) {
					formColumnId = (String)(forms.getFormsBloBs().get(j).get("formColumnId")+"");
					sColumnMapper.deleteByPrimaryKey(new BigDecimal(formColumnId));
					reCode = "true";
				}
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
			
		}
		
		/**
		 * 更新接口方案状态
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/startInterfaceSchemes")
		public @ResponseBody String startInterfaceSchemes(HttpServletRequest request) throws Exception{
			String reCode = "false";
			String type = request.getParameter("type");
			String interfaceSchemeIds = request.getParameter("interfaceSchemeIds");
			String[] interfaceSchemeIdArr = interfaceSchemeIds.split(",");
			List<Long> values = new ArrayList<Long>();
			for (int i = 0; i < interfaceSchemeIdArr.length; i++) {
				values.add(Long.parseLong(interfaceSchemeIdArr[i]));
			}
			SysInterfaceSchemeExample example = new SysInterfaceSchemeExample();
			example.or().andInterfaceSchemeIdIn(values);
			List<SysInterfaceScheme> interfaceSchemes = sysInterfaceSchemeMapper.selectByExample(example);
			
			if (type.equals("start")) {
				for (int i = 0; i < interfaceSchemes.size(); i++) {
					SysInterfaceScheme interfaceScheme = interfaceSchemes.get(i);
					interfaceScheme.setInterfaceSchemeStatus("0");
					sysInterfaceSchemeMapper.updateByPrimaryKey(interfaceScheme);
				}
			}else {
				for (int i = 0; i < interfaceSchemes.size(); i++) {
					SysInterfaceScheme interfaceScheme = interfaceSchemes.get(i);
					interfaceScheme.setInterfaceSchemeStatus("1");
					sysInterfaceSchemeMapper.updateByPrimaryKey(interfaceScheme);
				}
			}
			reCode = "true";
			return reCode;
		}
		
		/**
		 * 预览接口方案
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/viewInterfaceScheme")
		public @ResponseBody String viewInterfaceScheme(HttpServletRequest request,HttpServletResponse response) throws Exception{
			String result = "";
			String interfaceSchemeId = request.getParameter("interfaceSchemeId");
			SysInterfaceScheme interfaceScheme = sysInterfaceSchemeMapper.selectByPrimaryKey(Long.parseLong(interfaceSchemeId));
			if (interfaceScheme.getInterfaceSchemeStatus().equals("0")) {
				if (interfaceScheme.getInterfaceSchemeType().equals("webservice")) {
					ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-client.xml");  
					FormDataWebservice formClient = (FormDataWebservice) ctx.getBean("formClient");  
			        result = formClient.getInterfaceSchemeDatas("2","2","1");  
				}else {
					ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-client.xml");  
					FormDataWebservice formClient = (FormDataWebservice) ctx.getBean("formClient");  
			        result = formClient.getInterfaceSchemeDatas(interfaceSchemeId,"wsuser","oXcaWA1pZiUIwLceUGbZ+Q==");  
				}
			}else {
				result = "接口未发布，请先发布";
			}
			response.setContentType("text/xml;charset=UTF-8");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setCharacterEncoding("UTF-8");
			
			return result;
		}
		
		/**
		 * 预览接口方案
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/receiveInterfaceScheme")
		public @ResponseBody String receiveInterfaceScheme(HttpServletRequest request,HttpServletResponse response, @RequestBody RemoveDataformParams params ) throws Exception{
			String result = "";
			SysFormButtonInterfaceExample example1 = new SysFormButtonInterfaceExample();
			com.myehr.pojo.formmanage.button.SysFormButtonInterfaceExample.Criteria criteria = example1.createCriteria();
			criteria.andButtonInterfaceButtonIdEqualTo(new BigDecimal(params.getFormId()));
			SysFormButtonInterface buttonInterface = sysFormButtonInterfaceMapper.selectByExample(example1).get(0);
			
			Map obj = params.getParams()[0];
			SysFormGeneralParamExample example = new SysFormGeneralParamExample();
			example.or().andParamTypeValueEqualTo(buttonInterface.getButtonInterfaceSchemeId()).andParamFromTypeNotEqualTo("mstform");
			
			List<SysFormGeneralParam> sGeneralParams = sGeneralParamMapper.selectByExample(example);
			Map<String,String> objMap = new HashMap();
			for (int i = 0; i < sGeneralParams.size(); i++) {
				objMap.put(sGeneralParams.get(i).getParamName()+"", obj.get(sGeneralParams.get(i).getParamFromValue())+"");
			}
	        
			result = CUXOKCBPMSERAPKGPortType_CUXOKCBPMSERAPKGPort_Client.Test(objMap);
			//result = "<?xml version=\"1.0\" encoding=\"utf-8\"?><datainfos syscodesyncode=\"CONTRACT\" uniqueid=\"1\" syscode=\"okccontapp\"><return_message><bpmformid>1</bpmformid><returncode>S</returncode><errortext></errortext></return_message></datainfos>";
			return result;
		}
		
		//批量删除数据updateFormFolderid
		@RequestMapping("/deleteForms")
		public @ResponseBody String deleteForms(HttpServletRequest request) throws Exception{
			String reCode = "false";
			JSONArray jArray=GetRequestJsonUtils.getJsonArray(request);
			logger.info(request.getParameter("sign")+"加密前端");
			logger.info(jArray+"");
			
			DataSign datasign = new DataSign();
			
			String author= datasign.getMD5Str(jArray+"");
			
			logger.info("后端加密后的结果"+author);
			
			if(!author.equalsIgnoreCase(request.getParameter("sign"))){
				logger.info("前后请求不一致");
				logger.info("跳到错误界面");
			
				return "false"; 
			     
			}

			@SuppressWarnings("unchecked")
			List<SysFormconfigWithBLOBs> forms = JSONArray.toList(jArray,new SysFormconfigWithBLOBs(), new JsonConfig()); 
			try {
				for (SysFormconfigWithBLOBs form : forms) {
					if (form.getFormDefLayoutType().equals("1")) {//卡片
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						//删除分组信息
						SysFormGroupExample exampleGroup = new SysFormGroupExample();
						exampleGroup.or().andGroupFormDefIdEqualTo(form.getFormDefId());
						sGroupMapper.deleteByExample(exampleGroup);
						//删除字段信息
						SysFormColumnExample exampleColumn = new SysFormColumnExample();
						exampleColumn.or().andFormColumnFormDefIdEqualTo(form.getFormDefId());
						List<SysFormColumn> columns = sColumnMapper.selectByExample(exampleColumn);
						for (SysFormColumn column : columns) {
							sColumnMapper.deleteByPrimaryKey(column.getFormColumnId());
							SysFormTextboxExample exampleText = new SysFormTextboxExample();
							SysFormComboboxExample exampleCombobox = new SysFormComboboxExample();
							SysFormRadiobuttonlistExample exampleRadio = new SysFormRadiobuttonlistExample();
							SysFormCheckboxlistExample exampleChecklist = new SysFormCheckboxlistExample();
							SysFormCheckboxExample exampleCheckbox = new SysFormCheckboxExample();
							SysFormDatepickerExample exampleDate = new SysFormDatepickerExample();
							SysFormLookupExample exampleLookup = new SysFormLookupExample();
							SysFormTextareaExample exampleTextarea = new SysFormTextareaExample();
							SysFormFileuploadExample exampleFileload = new SysFormFileuploadExample();
							SysFormRichtextExample exampleRich = new SysFormRichtextExample();
							exampleRich.or().andRichtextFormColumnIdEqualTo(column.getFormColumnId());
							exampleFileload.or().andFileuploadFormColumnIdEqualTo(column.getFormColumnId());
							exampleTextarea.or().andTextareaFormColumnIdEqualTo(column.getFormColumnId());
							exampleLookup.or().andLookupFormColumnIdEqualTo(column.getFormColumnId());
							exampleDate.or().andDatepickerFormColumnIdEqualTo(column.getFormColumnId());
							exampleCheckbox.or().andCheckboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleChecklist.or().andCheckboxlistFormColumnIdEqualTo(column.getFormColumnId());
							exampleRadio.or().andRadiolistFormColumnIdEqualTo(column.getFormColumnId());
							exampleCombobox.or().andComboboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleText.or().andTextboxFormColumnIdEqualTo(column.getFormColumnId());
							if(column.getFormColumnGuiType().equals("1")){//文本框
								sTextboxMapper.deleteByExample(exampleText); 
							}else if (column.getFormColumnGuiType().equals("2")) {//下拉列表选择
								sComboboxMapper.deleteByExample(exampleCombobox);
							}else if (column.getFormColumnGuiType().equals("3")) {//单选框组
								sRadiobuttonlistMapper.deleteByExample(exampleRadio);
							}else if (column.getFormColumnGuiType().equals("4")) {//复选框组
								sCheckboxlistMapper.deleteByExample(exampleChecklist);
							}else if (column.getFormColumnGuiType().equals("5")) {//复选框
								sCheckboxMapper.deleteByExample(exampleCheckbox);
							}else if (column.getFormColumnGuiType().equals("6")) {//日期选择框
								sDatepickerMapper.deleteByExample(exampleDate);
							}else if (column.getFormColumnGuiType().equals("7")) {//lookup弹出窗
								sLookupMapper.deleteByExample(exampleLookup);
							}else if (column.getFormColumnGuiType().equals("8")) {//多行文本域
								sTextareaMapper.deleteByExample(exampleTextarea);
							}else if (column.getFormColumnGuiType().equals("9")) {//附件
								sFileuploadMapper.deleteByExample(exampleFileload);
							}else if (column.getFormColumnGuiType().equals("10")) {//富文本
								sRichtextMapper.deleteByExample(exampleRich);
							}
						}
						//删除过滤信息
						SysFormWhereExample exampleWhere = new SysFormWhereExample();
						exampleWhere.or().andFormWhereFormDefIdEqualTo(form.getFormDefId());
						sysFormWhereMapper.deleteByExample(exampleWhere);
						//删除按钮信息
						SysFormButtonExample exampleButton = new SysFormButtonExample();
						exampleButton.or().andFormButtonFormDefIdEqualTo(form.getFormDefId());
						sysFormButtonMapper.deleteByExample(exampleButton);
						
					}else if (form.getFormDefLayoutType().equals("2")) {//栅格式列表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						//删除分组信息
						SysFormGroupExample exampleGroup = new SysFormGroupExample();
						exampleGroup.or().andGroupFormDefIdEqualTo(form.getFormDefId());
						sGroupMapper.deleteByExample(exampleGroup);
						//删除字段信息
						SysFormColumnExample exampleColumn = new SysFormColumnExample();
						exampleColumn.or().andFormColumnFormDefIdEqualTo(form.getFormDefId());
						List<SysFormColumn> columns = sColumnMapper.selectByExample(exampleColumn);
						for (SysFormColumn column : columns) {
							sColumnMapper.deleteByPrimaryKey(column.getFormColumnId());
							SysFormTextboxExample exampleText = new SysFormTextboxExample();
							SysFormComboboxExample exampleCombobox = new SysFormComboboxExample();
							SysFormRadiobuttonlistExample exampleRadio = new SysFormRadiobuttonlistExample();
							SysFormCheckboxlistExample exampleChecklist = new SysFormCheckboxlistExample();
							SysFormCheckboxExample exampleCheckbox = new SysFormCheckboxExample();
							SysFormDatepickerExample exampleDate = new SysFormDatepickerExample();
							SysFormLookupExample exampleLookup = new SysFormLookupExample();
							SysFormTextareaExample exampleTextarea = new SysFormTextareaExample();
							SysFormFileuploadExample exampleFileload = new SysFormFileuploadExample();
							SysFormRichtextExample exampleRich = new SysFormRichtextExample();
							exampleRich.or().andRichtextFormColumnIdEqualTo(column.getFormColumnId());
							exampleFileload.or().andFileuploadFormColumnIdEqualTo(column.getFormColumnId());
							exampleTextarea.or().andTextareaFormColumnIdEqualTo(column.getFormColumnId());
							exampleLookup.or().andLookupFormColumnIdEqualTo(column.getFormColumnId());
							exampleDate.or().andDatepickerFormColumnIdEqualTo(column.getFormColumnId());
							exampleCheckbox.or().andCheckboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleChecklist.or().andCheckboxlistFormColumnIdEqualTo(column.getFormColumnId());
							exampleRadio.or().andRadiolistFormColumnIdEqualTo(column.getFormColumnId());
							exampleCombobox.or().andComboboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleText.or().andTextboxFormColumnIdEqualTo(column.getFormColumnId());
							if(column.getFormColumnGuiType().equals("1")){//文本框
								sTextboxMapper.deleteByExample(exampleText); 
							}else if (column.getFormColumnGuiType().equals("2")) {//下拉列表选择
								sComboboxMapper.deleteByExample(exampleCombobox);
							}else if (column.getFormColumnGuiType().equals("3")) {//单选框组
								sRadiobuttonlistMapper.deleteByExample(exampleRadio);
							}else if (column.getFormColumnGuiType().equals("4")) {//复选框组
								sCheckboxlistMapper.deleteByExample(exampleChecklist);
							}else if (column.getFormColumnGuiType().equals("5")) {//复选框
								sCheckboxMapper.deleteByExample(exampleCheckbox);
							}else if (column.getFormColumnGuiType().equals("6")) {//日期选择框
								sDatepickerMapper.deleteByExample(exampleDate);
							}else if (column.getFormColumnGuiType().equals("7")) {//lookup弹出窗
								sLookupMapper.deleteByExample(exampleLookup);
							}else if (column.getFormColumnGuiType().equals("8")) {//多行文本域
								sTextareaMapper.deleteByExample(exampleTextarea);
							}else if (column.getFormColumnGuiType().equals("9")) {//附件
								sFileuploadMapper.deleteByExample(exampleFileload);
							}else if (column.getFormColumnGuiType().equals("10")) {//富文本
								sRichtextMapper.deleteByExample(exampleRich);
							}
						}
						//删除过滤信息
						SysFormWhereExample exampleWhere = new SysFormWhereExample();
						exampleWhere.or().andFormWhereFormDefIdEqualTo(form.getFormDefId());
						sysFormWhereMapper.deleteByExample(exampleWhere);
						//删除按钮信息
						SysFormButtonExample exampleButton = new SysFormButtonExample();
						exampleButton.or().andFormButtonFormDefIdEqualTo(form.getFormDefId());
						sysFormButtonMapper.deleteByExample(exampleButton);
					}else if (form.getFormDefLayoutType().equals("3")) {//普通树
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						SysFormconfigTreeExample exampleTree = new SysFormconfigTreeExample();
						exampleTree.or().andFormTreeFormIdEqualTo(form.getFormDefId());
						sysFormconfigTreeMapper.deleteByExample(exampleTree);
						SysFormButtonExample exampleButton = new SysFormButtonExample();
						exampleButton.or().andFormButtonFormDefIdEqualTo(form.getFormDefId());
						sysFormButtonMapper.deleteByExample(exampleButton);
						SysTreeNodeTypeExample exampleNode = new SysTreeNodeTypeExample();
						exampleNode.or().andFormTreeFormIdEqualTo(form.getFormDefId());
						sNodeTypeMapper.deleteByExample(exampleNode);
					}else if (form.getFormDefLayoutType().equals("4")) {//普通报表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
					}else if (form.getFormDefLayoutType().equals("5")) {//主从表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						SysFormconfigMstTabExample exampleMstTab = new SysFormconfigMstTabExample();
						exampleMstTab.or().andMstTabFormIdEqualTo(form.getFormDefId());
						String mstTabId = sMstTabMapper.selectByExample(exampleMstTab).get(0).getMstTabId()+"";
						sMstTabMapper.deleteByExample(exampleMstTab);
						SysFormconfigMstTabDetailExample exampleMstTabDetail = new SysFormconfigMstTabDetailExample();
						exampleMstTabDetail.or().andMstTabIdEqualTo(new BigDecimal(mstTabId));
						sMstTabDetailMapper.deleteByExample(exampleMstTabDetail);
						SysFormGeneralParamExample exampleGeneralParam = new SysFormGeneralParamExample();
						exampleGeneralParam.or().andParamTypeEqualTo("FORM_TAB|FORM_MST_TAB").andParamTypeValueEqualTo(form.getFormDefId());
						sGeneralParamMapper.deleteByExample(exampleGeneralParam);
						
					}else if (form.getFormDefLayoutType().equals("6")) {//方案树
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
					}else if (form.getFormDefLayoutType().equals("7")) {//多TAB
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						SysFormconfigMstTabExample exampleMstTab = new SysFormconfigMstTabExample();
						exampleMstTab.or().andMstTabFormIdEqualTo(form.getFormDefId());
						String mstTabId = sMstTabMapper.selectByExample(exampleMstTab).get(0).getMstTabId()+"";
						sMstTabMapper.deleteByExample(exampleMstTab);
						SysFormconfigMstTabDetailExample exampleMstTabDetail = new SysFormconfigMstTabDetailExample();
						exampleMstTabDetail.or().andMstTabIdEqualTo(new BigDecimal(mstTabId));
						sMstTabDetailMapper.deleteByExample(exampleMstTabDetail);
					}else if (form.getFormDefLayoutType().equals("8")) {//易客报表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
					}else if (form.getFormDefLayoutType().equals("9")) {//图表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
					}else if (form.getFormDefLayoutType().equals("10")) {//卡片式列表
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
						//删除分组信息
						SysFormGroupExample exampleGroup = new SysFormGroupExample();
						exampleGroup.or().andGroupFormDefIdEqualTo(form.getFormDefId());
						sGroupMapper.deleteByExample(exampleGroup);
						//删除字段信息
						SysFormColumnExample exampleColumn = new SysFormColumnExample();
						exampleColumn.or().andFormColumnFormDefIdEqualTo(form.getFormDefId());
						List<SysFormColumn> columns = sColumnMapper.selectByExample(exampleColumn);
						for (SysFormColumn column : columns) {
							sColumnMapper.deleteByPrimaryKey(column.getFormColumnId());
							SysFormTextboxExample exampleText = new SysFormTextboxExample();
							SysFormComboboxExample exampleCombobox = new SysFormComboboxExample();
							SysFormRadiobuttonlistExample exampleRadio = new SysFormRadiobuttonlistExample();
							SysFormCheckboxlistExample exampleChecklist = new SysFormCheckboxlistExample();
							SysFormCheckboxExample exampleCheckbox = new SysFormCheckboxExample();
							SysFormDatepickerExample exampleDate = new SysFormDatepickerExample();
							SysFormLookupExample exampleLookup = new SysFormLookupExample();
							SysFormTextareaExample exampleTextarea = new SysFormTextareaExample();
							SysFormFileuploadExample exampleFileload = new SysFormFileuploadExample();
							SysFormRichtextExample exampleRich = new SysFormRichtextExample();
							exampleRich.or().andRichtextFormColumnIdEqualTo(column.getFormColumnId());
							exampleFileload.or().andFileuploadFormColumnIdEqualTo(column.getFormColumnId());
							exampleTextarea.or().andTextareaFormColumnIdEqualTo(column.getFormColumnId());
							exampleLookup.or().andLookupFormColumnIdEqualTo(column.getFormColumnId());
							exampleDate.or().andDatepickerFormColumnIdEqualTo(column.getFormColumnId());
							exampleCheckbox.or().andCheckboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleChecklist.or().andCheckboxlistFormColumnIdEqualTo(column.getFormColumnId());
							exampleRadio.or().andRadiolistFormColumnIdEqualTo(column.getFormColumnId());
							exampleCombobox.or().andComboboxFormColumnIdEqualTo(column.getFormColumnId());
							exampleText.or().andTextboxFormColumnIdEqualTo(column.getFormColumnId());
							if(column.getFormColumnGuiType().equals("1")){//文本框
								sTextboxMapper.deleteByExample(exampleText); 
							}else if (column.getFormColumnGuiType().equals("2")) {//下拉列表选择
								sComboboxMapper.deleteByExample(exampleCombobox);
							}else if (column.getFormColumnGuiType().equals("3")) {//单选框组
								sRadiobuttonlistMapper.deleteByExample(exampleRadio);
							}else if (column.getFormColumnGuiType().equals("4")) {//复选框组
								sCheckboxlistMapper.deleteByExample(exampleChecklist);
							}else if (column.getFormColumnGuiType().equals("5")) {//复选框
								sCheckboxMapper.deleteByExample(exampleCheckbox);
							}else if (column.getFormColumnGuiType().equals("6")) {//日期选择框
								sDatepickerMapper.deleteByExample(exampleDate);
							}else if (column.getFormColumnGuiType().equals("7")) {//lookup弹出窗
								sLookupMapper.deleteByExample(exampleLookup);
							}else if (column.getFormColumnGuiType().equals("8")) {//多行文本域
								sTextareaMapper.deleteByExample(exampleTextarea);
							}else if (column.getFormColumnGuiType().equals("9")) {//附件
								sFileuploadMapper.deleteByExample(exampleFileload);
							}else if (column.getFormColumnGuiType().equals("10")) {//富文本
								sRichtextMapper.deleteByExample(exampleRich);
							}
						}
						//删除过滤信息
						SysFormWhereExample exampleWhere = new SysFormWhereExample();
						exampleWhere.or().andFormWhereFormDefIdEqualTo(form.getFormDefId());
						sysFormWhereMapper.deleteByExample(exampleWhere);
						//删除按钮信息
						SysFormButtonExample exampleButton = new SysFormButtonExample();
						exampleButton.or().andFormButtonFormDefIdEqualTo(form.getFormDefId());
						sysFormButtonMapper.deleteByExample(exampleButton);
					}else {
						sysFormconfigMapper.deleteByPrimaryKey(form.getFormDefId());
					}
					
					reCode = "true";
				}
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				sysFormconfigMapper.deleteByPrimaryKey(forms.get(0).getFormDefId());
				return reCode;
			}
		}
		
		//批量修改数据
		@RequestMapping("/updateFormFolderid")
		public @ResponseBody String updateFormFolderid(HttpServletRequest request) throws Exception{
			String reCode = "false";
			String formFolderId = request.getParameter("formFolderId");
			JSONArray jArray=GetRequestJsonUtils.getJsonArray(request);
			@SuppressWarnings("unchecked")
			List<SysFormconfigWithBLOBs> forms = JSONArray.toList(jArray,new SysFormconfigWithBLOBs(), new JsonConfig()); 
			Map map = new HashMap();
			map.put("formFolderId", new BigDecimal(formFolderId));
			try {
				for (SysFormconfigWithBLOBs form : forms) {
					map.put("formDefId", form.getFormDefId());
					sFormconfigMapperExpand.updataFormFolderId(map);
					reCode = "true";
				}
				return reCode;
			} catch (Exception e) {
				// TODO: handle exception
				return reCode;
			}
		}
		
		/**
		 * 树节点操作
		 * @param request
		 * @param params
		 * @throws Throwable 
		 */
		@RequestMapping("/treeNodeOperation")
		public @ResponseBody Object  treeNodeOperation(HttpServletRequest request,@RequestBody SysFormFolderTree folderTree) throws Throwable {			
			String reCode = "0";
			SysFormFolderTreeExample example = new SysFormFolderTreeExample();
			String userId = (String)(request.getSession().getAttribute("userid")+"");
			example.or().andFolderTreeCodeEqualTo(folderTree.getFolderTreeCode());
			if (folderTree.getFolderTreeId()!=null) {
				List<SysFormFolderTree> fList = treeMapper.selectByExample(example);
				if (fList.size()>0&&folderTree.getFolderTreeId().toString().equals(fList.get(0).getFolderTreeId().toString())) {
					fList.get(0).setFolderTreeCode(folderTree.getFolderTreeCode());
					fList.get(0).setFolderTreeName(folderTree.getFolderTreeName());
					fList.get(0).setFolderTreeDesc(folderTree.getFolderTreeDesc());
					fList.get(0).setOperatorName(userId);
					fList.get(0).setOperatorTime(new Date());
					treeMapper.updateByPrimaryKey(fList.get(0));
					return reCode;
				} else if (fList.size()==0) {
					SysFormFolderTree folderTree2 = treeMapper.selectByPrimaryKey(folderTree.getFolderTreeId());
					folderTree2.setFolderTreeCode(folderTree.getFolderTreeCode());
					folderTree2.setFolderTreeName(folderTree.getFolderTreeName());
					folderTree2.setFolderTreeDesc(folderTree.getFolderTreeDesc());
					folderTree2.setOperatorName(userId);
					folderTree2.setOperatorTime(new Date());
					treeMapper.updateByPrimaryKey(folderTree2);
					return reCode;
				} else {
					return "1";//树节点编码重复
				}
			} else {
				if(treeMapper.selectByExample(example).size()>0){
					return "1";//树节点编码重复
				}else {
					folderTree.setOperatorName(userId);
					folderTree.setOperatorTime(new Date());
					treeMapper.insert(folderTree);
					SysFormFolderTree folderTree2 = treeMapper.selectByExample(example).get(0);
					folderTree2.setFolderTreeSeq(folderTree2.getFolderTreeId()+".");
					treeMapper.updateByPrimaryKey(folderTree2);
					return reCode;
				}
			}
		}
		
		//删除树节点
		@RequestMapping("/deleteTreeNode")
		public @ResponseBody String deleteTreeNode(HttpServletRequest request) throws Exception{
			String reCode = "0";
			String folderTreeId = request.getParameter("folderTreeId");
			treeMapper.deleteByPrimaryKey(new BigDecimal(folderTreeId));
			return reCode;
		}
		/**
		 * 构建流程分类树
		 */
		@RequestMapping("/buildFlowTree")
		public @ResponseBody List<Map> buildFlowTree(HttpServletRequest request) throws Exception{
			List<Map> datas = new ArrayList<Map>();
			HttpSession session = request.getSession();
			String userId = session.getAttribute("userid")+"";
			String type = request.getParameter("type");
			Map mapRoot=new HashMap();
			mapRoot.put("expanded", false);
			mapRoot.put("isLeaf", false);
			mapRoot.put("isParent", false); 
			mapRoot.put("open", false); 
			mapRoot.put("flowName", "流程管理");
			mapRoot.put("flowCode", "FlowRoot");
			mapRoot.put("flowType", "Root");
			mapRoot.put("flowParentCode", "");
			mapRoot.put("ORG_DEL", "N");
			datas.add(mapRoot);
			
			List<DictData> datas2 = sysformconfigService.getCardDictDataByDictTypeCode("act_flow_type","dict");
			for (int i = 0; i < datas2.size(); i++) {
			   String flowType = datas2.get(i).getCode();
			   String sql = "";
			   if(userId.equals("1")||type.equals("daipi")){//userId.equals("1")
				   sql = "select distinct * from (select act_re_model.ID_ as id, " +
							" act_re_model.REV_ as rev, " +
							" act_re_model.NAME_ as name, " +
							" act_re_model.KEY_ as key_, " +
							" act_re_model.CATEGORY_ category, " +
							" act_re_model.CREATE_TIME_ as createTime, " +
							" act_re_model.LAST_UPDATE_TIME_ as lastUpdateTime, " +
							" act_re_model.VERSION_ as version, " +
							" act_re_model.META_INFO_ as metaInfo, " +
							" act_re_model.DEPLOYMENT_ID_ as deoloymentId, " +
							" act_re_model.EDITOR_SOURCE_VALUE_ID_ as editorSourceValueId, " +
							" act_re_model.EDITOR_SOURCE_EXTRA_VALUE_ID_ as editorSourceExtraValueId, " +
							" act_re_model.TENANT_ID_ as tenantId, " +
							" act_node_properties_expand.ACT_NODE_FORM_ID as actNodeFormId " +
							" from act_re_model left join act_node_properties_expand on act_re_model.id_ = act_node_properties_expand.ACT_MODEL_ID" +
							" where act_node_properties_expand.ACT_NODE_KEY = 'start' and act_re_model.CATEGORY_ ="+flowType + 
							" union select act_re_model.ID_ as id, " +
							" act_re_model.REV_ as rev, " +
							" act_re_model.NAME_ as name, " +
							" act_re_model.KEY_ as key_, " +
							" act_re_model.CATEGORY_ category, " +
							" act_re_model.CREATE_TIME_ as createTime, " +
							" act_re_model.LAST_UPDATE_TIME_ as lastUpdateTime, " +
							" act_re_model.VERSION_ as version, " +
							" act_re_model.META_INFO_ as metaInfo, " +
							" act_re_model.DEPLOYMENT_ID_ as deoloymentId, " +
							" act_re_model.EDITOR_SOURCE_VALUE_ID_ as editorSourceValueId, " +
							" act_re_model.EDITOR_SOURCE_EXTRA_VALUE_ID_ as editorSourceExtraValueId, " +
							" act_re_model.TENANT_ID_ as tenantId, " +
							" act_node_properties_expand.ACT_NODE_FORM_ID as actNodeFormId " +
							" from act_re_model left join act_node_properties_expand on act_re_model.id_ = act_node_properties_expand.ACT_MODEL_ID" +
							" where act_re_model.ID_ not in( select ACT_MODEL_ID from act_node_properties_expand ) and act_re_model.CATEGORY_ ="+flowType+"" +
							") t";
			   }else {
				   sql = "select distinct * from (select act_re_model.ID_ as id, " +
							" act_re_model.REV_ as rev, " +
							" act_re_model.NAME_ as name, " +
							" act_re_model.KEY_ as key_, " +
							" act_re_model.CATEGORY_ category, " +
							" act_re_model.CREATE_TIME_ as createTime, " +
							" act_re_model.LAST_UPDATE_TIME_ as lastUpdateTime, " +
							" act_re_model.VERSION_ as version, " +
							" act_re_model.META_INFO_ as metaInfo, " +
							" act_re_model.DEPLOYMENT_ID_ as deoloymentId, " +
							" act_re_model.EDITOR_SOURCE_VALUE_ID_ as editorSourceValueId, " +
							" act_re_model.EDITOR_SOURCE_EXTRA_VALUE_ID_ as editorSourceExtraValueId, " +
							" act_re_model.TENANT_ID_ as tenantId, " +
							" act_node_properties_expand.ACT_NODE_FORM_ID as actNodeFormId " +
							" from act_re_model left join act_node_properties_expand on act_re_model.id_ = act_node_properties_expand.ACT_MODEL_ID" +
							" where act_node_properties_expand.ACT_NODE_KEY = 'start' and act_re_model.CATEGORY_ ="+flowType + 
							" and act_re_model.id_ in (SELECT ACTID FROM SYS_ACT_ROLE WHERE SYS_ACT_ROLE.ROLEID in (SELECT role_id from SYS_USER_ROLE where USER_ID = "+userId+"))"+
							" union select act_re_model.ID_ as id, " +
							" act_re_model.REV_ as rev, " +
							" act_re_model.NAME_ as name, " +
							" act_re_model.KEY_ as key_, " +
							" act_re_model.CATEGORY_ category, " +
							" act_re_model.CREATE_TIME_ as createTime, " +
							" act_re_model.LAST_UPDATE_TIME_ as lastUpdateTime, " +
							" act_re_model.VERSION_ as version, " +
							" act_re_model.META_INFO_ as metaInfo, " +
							" act_re_model.DEPLOYMENT_ID_ as deoloymentId, " +
							" act_re_model.EDITOR_SOURCE_VALUE_ID_ as editorSourceValueId, " +
							" act_re_model.EDITOR_SOURCE_EXTRA_VALUE_ID_ as editorSourceExtraValueId, " +
							" act_re_model.TENANT_ID_ as tenantId, " +
							" act_node_properties_expand.ACT_NODE_FORM_ID as actNodeFormId " +
							" from act_re_model left join act_node_properties_expand on act_re_model.id_ = act_node_properties_expand.ACT_MODEL_ID" +
							" where act_re_model.ID_ not in( select ACT_MODEL_ID from act_node_properties_expand ) and act_re_model.CATEGORY_ ="+flowType+"" +
							" and act_re_model.id_ in (SELECT ACTID FROM SYS_ACT_ROLE WHERE SYS_ACT_ROLE.ROLEID in (SELECT role_id from SYS_USER_ROLE where USER_ID = "+userId+"))"+
							") t";
			   }
				List<Map> lists = tMapperExt.queryByFormDefSql(sql);
				if (lists!=null&&lists.size()>0) {
					Map map=new HashMap();
					map.put("expanded", false);
					map.put("isLeaf", false);
					map.put("isParent", false); 
					map.put("open", false); 
					map.put("flowName", datas2.get(i).getName());
					map.put("flowCode", datas2.get(i).getCode());
					map.put("flowType", datas2.get(i).getCode());
					map.put("flowDicCode", "");
					map.put("flowParentCode", "FlowRoot");
					map.put("ORG_DEL", "N");
					datas.add(map);
				}
			}
			
			Map map=new HashMap();
			map.put("expanded", false);
			map.put("isLeaf", false);
			map.put("isParent", false); 
			map.put("open", false); 
			map.put("flowName", "自由流程");
			map.put("flowCode", "FreeAct");
			map.put("flowType", "FreeAct");
			map.put("flowDicCode", "");
			map.put("flowParentCode", "FlowRoot");
			map.put("ORG_DEL", "F");
			datas.add(map);
			
			return  datas;
		}
		//加载表单缓存
		@RequestMapping("/setFormById")
		public void setFormById(HttpServletRequest request) throws Exception{
			HttpSession session = request.getSession();
			String formId = request.getParameter("formId");
			DataSign datasign = new DataSign();
			
			String author= datasign.getMD5Str(formId);
			
			if(!author.equalsIgnoreCase(request.getParameter("sign"))){
				logger.info("前后请求不一致");
				logger.info("跳到错误界面");
			
				logger.info("没有返回值，有缺陷");
			     
			}




			sysformconfigService.setFormColumns(formId);//重新加载表单关联字段列表缓存
			sysformconfigService.setFormInfo(formId);//刷新表单信息缓存
			sysformconfigService.setFormFilterColumns(formId);//刷新表单查询字段缓存
			sysformconfigService.setFormFilter(formId);//刷新表单查询配置缓存
			sysformconfigService.setFormWheres(formId);//刷新表单过滤字段缓存
			sysformconfigService.setCardandcardConfig(formId);
			SysFormColumnExample example = new SysFormColumnExample();
			example.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formId));
			List<SysFormColumn> formColumns = sysFormColumnMapper.selectByExample(example);
			for (int i = 0; i < formColumns.size(); i++) {
				SysFormColumn column = formColumns.get(i);
				String columnId = column.getFormColumnId()+"";
				sysformconfigService.setColumn(columnId);//刷新每一个字段信息缓存
				sysformconfigService.setEntityColumnByColumnId(columnId);//刷新每一个字段关联实体信息缓存
				logger.info(columnId);
				if (column.getFormColumnGuiType().equals("1")) {
					sysformconfigService.setTextbox(columnId);//刷新文本控件信息
				}else if (column.getFormColumnGuiType().equals("2")) {
					sysformconfigService.setCombobox(columnId);//刷新下拉控件信息
				}else if (column.getFormColumnGuiType().equals("3")||column.getFormColumnGuiType().equals("4")) {
					sysformconfigService.setRadiobuttonlist(columnId);//刷新单选多选控件信息
				}else if (column.getFormColumnGuiType().equals("6")) {
					sysformconfigService.setDatepicker(columnId);//刷新日期控件信息
				}else if (column.getFormColumnGuiType().equals("7")) {
					sysformconfigService.setLookup(columnId);//刷新LookUp弹出控件信息
				}else if (column.getFormColumnGuiType().equals("10")) {
					sysformconfigService.setRichtext(columnId);//刷新富文本控件信息
				}else if (column.getFormColumnGuiType().equals("9")) {
					sysformconfigService.setFileupload(columnId);//刷新附件控件信息
				}
			}
			
			sysformconfigService.setFormbuttons(formId);//刷新表单关联按钮缓存
			
			SysFormButtonExample example1 = new SysFormButtonExample();
			example1.or().andFormButtonFormDefIdEqualTo(new BigDecimal(formId));
			List<SysFormButton> formButtons = sysFormButtonMapper.selectByExample(example1);
			for (int i = 0; i < formButtons.size(); i++) {
				SysFormButton button = formButtons.get(i);
				String formButtonId = button.getFormButtonId()+"";
				sysformconfigService.setButton(formButtonId);//刷新每一个字段信息缓存
				if("save_form".equals(button.getFormButtonType())||"save".equals(button.getFormButtonType())||"select".equals(button.getFormButtonType())||"other".equals(button.getFormButtonType())){
					sysformconfigService.setButtonSave(formButtonId);//刷新保存按钮信息
				}else if ("add".equals(button.getFormButtonType())||"edit".equals(button.getFormButtonType()) ||"batchEdit".equals(button.getFormButtonType())||"copyAndUpdate".equals(button.getFormButtonType())) {
					sysformconfigService.setButtonAdd(formButtonId);//刷新新增按钮信息
				}else if ("remove".equals(button.getFormButtonType())) {
					sysformconfigService.setButtonRemove(formButtonId);//刷新删除按钮信息
				}else if ("import".equals(button.getFormButtonType())) {
					sysformconfigService.setButtonImport(formButtonId);//刷新导入按钮信息
				}else if ("calculate".equals(button.getFormButtonType())) {
					sysformconfigService.setButtonCalculate(formButtonId);//刷新计算按钮信息
				}else if ("introduce".equals(button.getFormButtonType())) {
					sysformconfigService.setButtonIntroduce(formButtonId);//刷新引入按钮信息
				}else if ("executeSQL".equals(button.getFormButtonType())) {
					sysformconfigService.setSysExecSql(formButtonId);//刷新调用SQL信息
				}
			}

			sysformconfigService.setForm(formId);//重新加载表单缓存
		}
		
		/**
		 * 构建流程分类树
		 */
		@RequestMapping("/buildFlowTypeTree")
		public @ResponseBody List<Map> buildFlowTypeTree(HttpServletRequest request) throws Exception{
			//SaveButtonParams params = (SaveButtonParams) JSONObject.toBean(GetRequestJsonUtils.getJsonObject(request), SaveButtonParams.class);
			List<Map> datas = new ArrayList<Map>();
			Map mapRoot=new HashMap();
			mapRoot.put("expanded", false);
			mapRoot.put("isLeaf", false);
			mapRoot.put("isParent", false); 
			mapRoot.put("open", false); 
			mapRoot.put("flowName", "流程管理");
			mapRoot.put("flowCode", "FlowRoot");
			mapRoot.put("flowType", "Root");
			mapRoot.put("flowParentCode", "");
			mapRoot.put("ORG_DEL", "N");
			datas.add(mapRoot);
			
			Map map=new HashMap();
			map.put("expanded", false);
			map.put("isLeaf", false);
			map.put("isParent", false); 
			map.put("open", false); 
			map.put("flowName", "待办任务");
			map.put("flowCode", "undo");
			map.put("flowType", "undo");
			map.put("flowDicCode", "");
			map.put("flowParentCode", "FlowRoot");
			map.put("ORG_DEL", "N");
			datas.add(map);
			
			Map map1=new HashMap();
			map1.put("expanded", false);
			map1.put("isLeaf", false);
			map1.put("isParent", false); 
			map1.put("open", false); 
			map1.put("flowName", "已办任务");
			map1.put("flowCode", "done");
			map1.put("flowType", "done");
			map1.put("flowDicCode", "");
			map1.put("flowParentCode", "FlowRoot");
			map1.put("ORG_DEL", "N");
			datas.add(map1);
			
			return  datas;
		}
		
		//问卷调查
		@RequestMapping("/questionFormList")
		 public @ResponseBody ResultMap questionFormList(HttpServletRequest request) throws Exception {
			SysRequestParam param = new SysRequestParam();
			param.setRequest(request);
			param.setSession(request.getSession());
			param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
			param.setPath(request.getRealPath("/jsp/formbuild/")+"");
			param.setIsInit(request.getParameter("isInit")+"");
			param.setIsView(request.getParameter("isView")+"");
			param.setContextPath(request.getContextPath()+"");
		        try {  
		            return formService.getExamtemplate(param);  
		        } catch (Exception e) {  
		            e.printStackTrace();logger.error(e.getMessage(),e);  
		        }  
		        return null; 
		}
		
	/**
	 * 根据用户查询表单字段
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryCardFormColumnsWithUserId")
	public @ResponseBody List<SysFormColumn> queryCardFormColumnsWithUserId(HttpServletRequest request) throws Exception{
		String formId = request.getParameter("formId");
		String userId = request.getSession().getAttribute("userid")+"";
		if (userId.equals("1")) {
			List<SysFormColumn> columns = sysFormColumnExpandMapper.queryColumnsByFormIdAndUserId1(formId,userId);
			return columns;
		}else {
//			List<SysFormColumn> columns = sysformconfigService.getFormColumnByUserid(formId, userId);
			List<SysFormColumn> columns = sysFormColumnExpandMapper.queryColumnsByFormIdAndUserIdAll(formId,userId);
	    	return columns;
		}
	}
	
	/**
	 * 根据用户查询表单按钮
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryCardFormButtonsWithUserId")
	public @ResponseBody List<Map> queryCardFormButtonsWithUserId(HttpServletRequest request) throws Exception{
		String formId = request.getParameter("formId");
		String userId = request.getSession().getAttribute("userid")+"";
		if (userId.equals("1")) {
			return null;
		}else {
			List<Map> buttons = IButtonService.queryFormButtonWithUserId(formId,userId);
			return buttons;
		}
	}
	/**
	*lookup弹出窗
	 */
	@RequestMapping("/getLookupReturnValue")
	public @ResponseBody Object getLookupReturnValue(HttpServletRequest request) throws Exception {
		String formId = request.getParameter("formId");
		String entityCode = request.getParameter("entityCode");
		String fieldCode = request.getParameter("fieldCode");
		String value = request.getParameter("value");
		try {
			SysFormColumnExample example = new SysFormColumnExample();
			example.or().andFormColumnFormDefIdEqualTo(new BigDecimal(formId)).andFormEntityTablenameEqualTo(entityCode).andFormFieldTablenameEqualTo(fieldCode);
			SysFormLookupExample lookupExample = new SysFormLookupExample();
			lookupExample.or().andLookupFormColumnIdEqualTo(sColumnMapper.selectByExample(example).get(0).getFormColumnId());
			SysFormLookup lookup= sLookupMapper.selectByExample(lookupExample).get(0);
			SysFormconfigWithBLOBs form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(lookup.getLookupFormId()));
			if (form.getFormDefLayoutType().equals("3")) {
				//弹出树
				SysFormconfigTreeExample treeExample = new SysFormconfigTreeExample();
				treeExample.or().andFormTreeFormIdEqualTo(form.getFormDefId());
				String a=sTreeSolutionMapper.selectByPrimaryKey(sysFormconfigTreeMapper.selectByExample(treeExample).get(0).getFormTreeSolutionId()).getTreeSolutionExcSql();
				String d="\\s+";
				String e=a.replaceAll(d, " ");
				int b=e.indexOf("order by");
				String sql = "";
				if (b!=-1) {
					e=e.substring(0, b);
					sql = "select lookup."+lookup.getLookupShowProperty()+" from ("+e;
					sql += ")lookup where "+lookup.getLookupValueProperty() +" = '"+value+"'";
				}else{
					sql = "select lookup."+lookup.getLookupShowProperty()+" from ("+e;
					sql += ")lookup where "+lookup.getLookupValueProperty() +" = '"+value+"'";
				}
			    
				List<Map> ids = tMapperExt.queryByFormDefSql(sql);
				String[] result = new String[1];
				result[0] = (String) ids.get(0).get(lookup.getLookupShowProperty());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();logger.error(e.getMessage(),e);
			return null;
		}
		return null;
	}
	
	//getLookupReturnValue
	@RequestMapping("/dragconfigFormList")
	public @ResponseBody
	ResultMap dragconfigFormList(HttpServletRequest request) throws Exception {
		SysRequestParam param = new SysRequestParam();
		param.setRequest(request);
		param.setSession(request.getSession());
		param.set_workflowBusPrimaryKeyParamName(request.getAttribute("_workflowBusPrimaryKeyParamName")+"");
		param.setPath(request.getRealPath("/jsp/formbuild/")+"");
		param.setIsInit(request.getParameter("isInit")+"");
		param.setIsView(request.getParameter("isView")+"");
		param.setContextPath(request.getContextPath()+"");
		try {
			return formService.getDragconfig(param);
		} catch (Exception e) {
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	//saveToImgByStr
	//查询所有数据
	@RequestMapping("/batchGetPassword")
	public @ResponseBody  Object batchGetPassword(HttpServletRequest request) throws Exception {
		String[] userid = request.getParameter("userid").split(",");
		String[] tempSqls = new String[1];
		String[] datas = new String[userid.length];
		for (int i = 0; i < userid.length; i++) {
			tempSqls[0] = "CALL UP_Sys_User_PassWord('"+userid[i]+"',?)";//CALL ORG_SP_COMPVALID([c:主键_20810],[s:userId],?)
			MyEhrJDBCUtil.execPrepare(MybatisUtil.getConnection(),tempSqls[0]);
			SysUser user = sUserMapper.selectByPrimaryKey(Integer.valueOf(userid[i]));
			datas[i] = user.getUserPassword();
		}
		return datas;
	}
	//saveToImgByStr
	//查询所有数据
	@RequestMapping("/batchUpdatePassword")
	public @ResponseBody  Object batchUpdatePassword(HttpServletRequest request) throws Exception {
		String[] userid = request.getParameter("userid").split(",");
		String[] passWord = request.getParameter("passwords").split(",");
		for (int i = 0; i < userid.length; i++) {
			SysUser user = sUserMapper.selectByPrimaryKey(Integer.valueOf(userid[i]));
			user.setUserPassword(passWord[i]+"==");
			sUserMapper.updateByPrimaryKey(user);
		}
		return null;
	}
	
	@RequestMapping("/getUsernameById")
	public @ResponseBody  Object getUsernameById(HttpServletRequest request) throws Exception {
		String userid = request.getParameter("userId");
		return sUserMapper.selectByPrimaryKey(Integer.valueOf(userid));
	}
	
	//查询附表信息
	@RequestMapping("/getMstTabDetailById")
	public @ResponseBody  Object getMstTabDetailById(HttpServletRequest request) throws Exception {
		String tabDetailId = request.getParameter("tabDetailId");
		SysFormconfigMstTabDetail detail = new SysFormconfigMstTabDetail();
		return sMstTabDetailMapper.selectByPrimaryKey(new BigDecimal(tabDetailId));
	}
	
	//updateTabDetail修啊该附表配置
	@RequestMapping("/updateTabDetail")
	public @ResponseBody Object  updateTabDetail(HttpServletRequest request,@RequestBody SysFormconfigMstTabDetail detail) throws Throwable {			
		SysFormconfigMstTabDetail detail2 = sMstTabDetailMapper.selectByPrimaryKey(detail.getTabDetailId());
		detail2.setTabDetailFormTitle(detail.getTabDetailFormTitle());
		detail2.setTabDetailSort(detail.getTabDetailSort());
		detail2.setTabDetailConfig(detail.getTabDetailConfig());
		return sMstTabDetailMapper.updateByPrimaryKey(detail2);
	}
	
	//flushDictCacheByFormIds刷新表单中所有下拉字段的字典缓存
	@RequestMapping("/flushDictCacheByFormIds")
	public @ResponseBody Object  flushDictCacheByFormIds(HttpServletRequest request,@RequestBody List<SysFormconfig> forms) throws Throwable {			
		try {
			for (SysFormconfig sysFormconfig : forms) {
				SysFormColumnExample example = new SysFormColumnExample();
				example.or().andFormColumnFormDefIdEqualTo(sysFormconfig.getFormDefId());
				List<SysFormColumn> columns = sColumnMapper.selectByExample(example);
				for (SysFormColumn sysFormColumn : columns) {
					if(sysFormColumn.getFormColumnGuiType().equals("2")){
						sysformconfigService.setCardDictDataByColumnId(sysFormColumn.getFormColumnId()+"","");
					}
				}
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
		
	}
	
	//根据表单ID查询字典信息
	@RequestMapping("/getDictDatasByFormId")
	public @ResponseBody Object  getDictDatasByFormId(HttpServletRequest request) throws Throwable {			
		String[] formIds = request.getParameter("formIds").split(",");
		String formId = request.getParameter("formId");
		String userId = request.getSession().getAttribute("userid")+"";
		Map map = new HashMap();
		String name = "";
		SysFormconfigCache formconfigx = sysformconfigService.getForm(formId);
		if (formconfigx.getFormDefLayoutType().equals("2")) {//列表表单
			SysFormconfigCache formconfig = sysformconfigService.getForm(formIds[0]);
			List<SysFormColumnCache> columns = formconfig.getColumns();
			for (SysFormColumnCache sysFormColumn : columns) {
				if (sysFormColumn.getFormColumnGuiType().equals("2")) {
					SysFormComboboxCache combobox =  (SysFormComboboxCache)sysFormColumn.getFormColumGui();
					name = "dictDatas_"+formconfig.getFormDefCode()+"_"+sysFormColumn.getFormColumnId();
					Map value = getDictDatas(combobox);
					map.put(name, value);
				}
			}
		}else if (formconfigx.getFormDefLayoutType().equals("5")) {
			for (String formid : formIds) {
				if (formid!=null&&!formid.equals("")) {
					SysFormconfigCache formconfig = sysformconfigService.getForm(formid);
					List<SysFormColumnCache> columns = formconfig.getColumns();
					for (SysFormColumnCache sysFormColumn : columns) {
						if (sysFormColumn.getFormColumnGuiType().equals("2")) {
							SysFormComboboxCache combobox =  (SysFormComboboxCache)sysFormColumn.getFormColumGui();
							name = "dictDatas_"+formconfig.getFormDefCode()+"_"+sysFormColumn.getFormColumnId();
							logger.info(sysFormColumn.getFormColumnLable());
							Map value = getDictDatas(combobox);
							map.put(name, value);
						}
					}
				}
			}
		}else if (formconfigx.getFormDefLayoutType().equals("7")) {
			for (String formid : formIds) {
				if (formid!=null&&!formid.equals("")) {
					SysFormconfigCache formconfig = sysformconfigService.getForm(formid);
					List<SysFormColumnCache> columns = formconfig.getColumns();
					for (SysFormColumnCache sysFormColumn : columns) {
						if (sysFormColumn.getFormColumnGuiType().equals("2")) {
							SysFormComboboxCache combobox =  (SysFormComboboxCache)sysFormColumn.getFormColumGui();
							name = "dictDatas_"+formconfig.getFormDefCode()+"_"+sysFormColumn.getFormColumnId();
							Map value = getDictDatas(combobox);
							map.put(name, value);
						}
					}
				}
			}
		}
		return map;
	}
	private Map getDictDatas(SysFormComboboxCache combobox) {
		try {
			if(combobox.getComboboxGuiInitType().equals("sql")){
				List<DictData> list = sysformconfigService.getCardDictDataByDictTypeCode(combobox.getComboboxGuiInitValue(),"sql");

				if (list.size()>0) {
					Map map = new HashMap();
					for (int i = 0; i < list.size(); i++) {
						map.put(list.get(i).getCode(),list.get(i).getName());
					}
					return map;
				}else { 
					return null;
				}
			}else if(combobox.getComboboxGuiInitType().equals("dict")){
				List<DictData> list = sysformconfigService.getCardDictDataByDictTypeCode(combobox.getComboboxGuiInitValue(),"dict");
				if (list!=null && list.size()>0) {
					Map map = new HashMap();
					for (DictData sysDictEntryExpand : list) {
						map.put(sysDictEntryExpand.getCode(), sysDictEntryExpand.getName());
					}
					return map;
				}else {
					return null;
				}
			}else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			return null;
		}
	}
	//selectBussinessIdByFormId
	@RequestMapping("/selectBussinessIdByFormId")
	public @ResponseBody Object selectBussinessIdByFormId(HttpServletRequest request) throws Throwable {			
		String formId = request.getParameter("formId");
		SysFormconfigCache form = sysformconfigService.getForm(formId);
		SysFormconfigCache mainForm = form.getMstTab().getMainFrom();
		String TableName = mainForm.getPojoform().getFormDefSavetable();
		String pkColumn = mainForm.getPkColumn().getFormFiledTablename();
		List<SysFormColumnCache> otherColumns = mainForm.getColumns();
		String otherColumn = "";
		String sql1 = "";
		for (SysFormColumnCache sysFormColumn : otherColumns) {
			if(!sysFormColumn.getFormFiledTablename().equals(pkColumn)){
				otherColumn = sysFormColumn.getFormFiledTablename();
				if (sysFormColumn.getField()!=null&&sysFormColumn.getField().getFieldType().equals("VARCHAR")||sysFormColumn.getField().getFieldType().equals("NVARCHAR")) {
					sql1 = "insert into "+TableName+" ("+otherColumn+") values ('')";
					break;
				}else if (sysFormColumn.getField()!=null&&sysFormColumn.getField().getFieldType().equals("DECIMAL")||sysFormColumn.getField().getFieldType().equals("INT")){
					sql1 = "insert into "+TableName+" ("+otherColumn+") values (0)";
					break;
				}
			}
		}
		String sql2 = "select MAX("+pkColumn+") as id from "+TableName;
		MybatisUtil.insert("runtime.insertSql", sql1);
		Map map = MybatisUtil.queryOne("runtime.selectSql",sql2);
		return map.get("id");
	}
	
	/**
	 * 导出查询数据xls
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/getImportErrorFile")
	public @ResponseBody ModelAndView getImportErrorFile(HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		String fileName = request.getParameter("path");
		String path = request.getParameter("filename");
		String baseExclePath = fileName+path;
		baseExclePath = baseExclePath+".xls";
		mv = new ModelAndView("forward:/jsp/form/button/download.jsp?filename="+path+".xls&contentType=application/octet-stream&filePath="+baseExclePath);
        return mv;
	}
	
	/**
	 * showFormBaseInfo
	 */
	@RequestMapping("/showFormBaseInfo")
	public @ResponseBody List<SysFormShowInfoExpand> showFormBaseInfo(HttpServletRequest request) throws Exception{
		String tags = request.getParameter("tags");
		SysFormShowInfoExample example = new SysFormShowInfoExample();
		if (tags!=null&&!tags.equals("")) {
			String[] tagx = tags.split(",");
			for (String string : tagx) {
				example.or().andFormTagLike("%,"+string+",%");
			}
		} 
		List<SysFormShowInfo> infos = showInfoMapper.selectByExample(example);
		List<SysFormShowInfoExpand> infoExpands = new ArrayList<SysFormShowInfoExpand>();
		for (SysFormShowInfo sysFormShowInfo : infos) {
			SysFormShowInfoExpand expand = new SysFormShowInfoExpand();
			expand.setShowInfo(sysFormShowInfo);
			SysFormconfigExample formExample = new SysFormconfigExample();
			formExample.or().andFormDefLayoutTypeEqualTo(sysFormShowInfo.getFormType());
			int num = sysFormconfigMapper.countByExample(formExample);
			expand.setNumber(num);
			infoExpands.add(expand);
		}
		return infoExpands;
	}
	
	//updateActFreeTitle
	@RequestMapping("/updateActFreeTitle")
	public void updateActFreeTitle(HttpServletRequest request) throws Exception{
		String title = request.getParameter("title");
		String formId = request.getParameter("formId");
		SysFormconfigWithBLOBs form = sysFormconfigMapper.selectByPrimaryKey(new BigDecimal(formId));
		form.setFormDefSavetable(title);
		sysFormconfigMapper.updateByPrimaryKeyWithBLOBs(form);
	}
	
	@RequestMapping("/saveColumnExpandConfig")
	public void saveColumnExpandConfig(HttpServletRequest request,@RequestBody List<SysFormColumn> columns) throws Throwable {	
		String formId = request.getParameter("formId");
		for (SysFormColumn sysFormColumn : columns) {
			SysFormColumn column = sysFormColumnMapper.selectByPrimaryKey(sysFormColumn.getFormColumnId());
			column.setIsshowweb(sysFormColumn.getIsshowweb());
			column.setIsshowmobile(sysFormColumn.getIsshowmobile());
			column.setIssort(sysFormColumn.getIssort());
			column.setDefaultsort(sysFormColumn.getDefaultsort());
			sysFormColumnMapper.updateByPrimaryKey(column);
		}
	}
	
	@RequestMapping("/saveFilterExpandConfig")
	public void saveFilterExpandConfig(HttpServletRequest request,@RequestBody List<SysGridFilterColumn> columns) throws Throwable {	
		String formId = request.getParameter("formId");
		for (SysGridFilterColumn filterColumn : columns) {
			SysGridFilterColumn column = sysGridFilterColumnMapper.selectByPrimaryKey(filterColumn.getQueryColumnId());
			column.setIsshowweb(filterColumn.getIsshowweb());
			column.setIsshowmobile(filterColumn.getIsshowmobile());
			sysGridFilterColumnMapper.updateByPrimaryKey(column);
		}
	}
	
	@RequestMapping("/saveButtonExpandConfig")
	public void saveButtonExpandConfig(HttpServletRequest request,@RequestBody List<SysFormButton> buttons) throws Throwable {	
		String formId = request.getParameter("formId");
		for (SysFormButton sysFormButton : buttons) {
			SysFormButton button = sysFormButtonMapper.selectByPrimaryKey(sysFormButton.getFormButtonId());
			button.setIsshowweb(sysFormButton.getIsshowweb());
			button.setIsshowmobile(sysFormButton.getIsshowmobile());
			button.setShowArea(sysFormButton.getShowArea());
			button.setShowType(sysFormButton.getShowType());
			sysFormButtonMapper.updateByPrimaryKey(button);
		}
	}
	
}
