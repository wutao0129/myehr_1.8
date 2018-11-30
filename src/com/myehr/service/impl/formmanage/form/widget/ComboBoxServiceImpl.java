package com.myehr.service.impl.formmanage.form.widget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.ModelAndView;

import com.myehr.pojo.activiti.ActReModel;
import com.myehr.pojo.activiti.ActReModelExample;
import com.myehr.pojo.activiti.ActRuTask;
import com.myehr.pojo.activiti.ActRuTaskExample;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpand;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample;
import com.myehr.pojo.dict.SysDictType;
import com.myehr.pojo.dict.SysDictTypeExample;
import com.myehr.pojo.entity.SysEntity;
import com.myehr.pojo.entity.SysEntityExample;
import com.myehr.pojo.formmanage.button.SysFormButtonSave;
import com.myehr.pojo.formmanage.button.SysFormButtonSaveExample;
import com.myehr.pojo.formmanage.cache.SysFormconfigCache;
import com.myehr.pojo.formmanage.form.FormChartsBarColumn;
import com.myehr.pojo.formmanage.form.FormChartsBarColumnExample;
import com.myehr.pojo.formmanage.form.SysFormAction;
import com.myehr.pojo.formmanage.form.SysFormActionExample;
import com.myehr.pojo.formmanage.form.SysFormCharts;
import com.myehr.pojo.formmanage.form.SysFormChartsExample;
import com.myehr.pojo.formmanage.form.SysFormColumn;
import com.myehr.pojo.formmanage.form.SysFormColumnExample;
import com.myehr.pojo.formmanage.form.SysFormconfig;
import com.myehr.pojo.formmanage.form.SysFormconfigExample;
import com.myehr.pojo.formmanage.form.SysFormconfigGrid;
import com.myehr.pojo.formmanage.form.SysFormconfigGridExample;
import com.myehr.pojo.formmanage.form.SysFormconfigTree;
import com.myehr.pojo.formmanage.form.SysFormconfigTreeExample;
import com.myehr.pojo.formmanage.form.SysFormconfigWithBLOBs;
import com.myehr.pojo.formmanage.form.SysGridFilter;
import com.myehr.pojo.formmanage.form.SysGridFilterExample;
import com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumn;
import com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumnExample;
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
import com.myehr.pojo.formmanage.widget.SysFormRichtext;
import com.myehr.pojo.formmanage.widget.SysFormRichtextExample;
import com.myehr.pojo.formmanage.widget.SysFormTextbox;
import com.myehr.pojo.formmanage.widget.SysFormTextboxExample;
import com.myehr.pojo.sysParam.SysRequestParam;
import com.myehr.pojo.sysParam.SysSystemParam;
import com.myehr.pojo.sysParam.SysSystemParamExample;
import com.myehr.pojo.sysRole.SysRole;
import com.myehr.pojo.sysRole.SysRoleExample;
import com.myehr.pojo.sysRole.SysUserOrg;
import com.myehr.pojo.sysRole.SysUserOrgExample;
import com.myehr.pojo.sysuser.SysOnlineUserExample;
import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.util.SerializeUtil;
import com.myehr.common.util.SpringContextUtils;
import com.myehr.mapper.entity.SysEntityMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonAddMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonCalculateMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonImportMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonIntroduceMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonRemoveMapper;
import com.myehr.mapper.formmanage.button.SysFormButtonSaveMapper;
import com.myehr.mapper.formmanage.form.SysExecSqlMapper;
import com.myehr.mapper.formmanage.form.SysFormActionMapper;
import com.myehr.mapper.formmanage.form.SysFormColumnMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigGridMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigTreeMapper;
import com.myehr.mapper.formmanage.form.SysGridFilterMapper;
import com.myehr.mapper.formmanage.widget.SysFormComboboxMapper;
import com.myehr.mapper.formmanage.widget.SysFormDatepickerMapper;
import com.myehr.mapper.formmanage.widget.SysFormFileuploadMapper;
import com.myehr.mapper.formmanage.widget.SysFormLookupMapper;
import com.myehr.mapper.formmanage.widget.SysFormRadiobuttonlistMapper;
import com.myehr.mapper.formmanage.widget.SysFormRichtextMapper;
import com.myehr.mapper.formmanage.widget.SysFormTextboxMapper;
import com.myehr.mapper.sysParam.SysSystemParamMapper;
import com.myehr.mapper.sysRole.SysRoleMapper;
import com.myehr.mapper.sysRole.SysUserOrgMapper;
import com.myehr.mapper.sysdict.SysDictEntryMapper;
import com.myehr.mapper.sysdict.SysDictTypeMapper;
import com.myehr.mapper.sysuser.SysOnlineUserMapper;
import com.myehr.service.RedisService;
import com.myehr.service.formmanage.form.IFormService;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.IsysFormColumnService;
import com.myehr.service.formmanage.form.widget.IComboBoxService;
import com.myehr.service.menu.SysMenuService;
@EnableAsync
public class ComboBoxServiceImpl implements IComboBoxService {
	private static Logger logger = LoggerFactory.getLogger(ComboBoxServiceImpl.class);
	@Autowired
	SysFormComboboxMapper mapper ;
	@Autowired
	private SysFormconfigTreeMapper sysFormconfigTreeMapper;
	@Autowired
	SysDictTypeMapper sysDictTypeMapper;
	
	@Autowired
	ISysformconfigService ISysformconfigService;
	
	@Autowired
	SysMenuService sysMenuService;
	
	@Autowired
	SysDictEntryMapper sysDictEntryMapper;
	
	@Autowired
	SysEntityMapper sysEntityMapper;
	
	@Autowired
	SysRoleMapper sysRoleMapper;
	
	@Autowired
	SysFormconfigMapper sysFormconfigMapper;
	
	@Autowired
	SysOnlineUserMapper sysOnlineUserMapper;
	
	@Autowired
	SysFormColumnMapper sysFormColumnMapper;
	
	@Autowired
	SysFormTextboxMapper sysFormTextboxMapper;
	
	@Autowired
	SysFormComboboxMapper sysFormComboboxMapper;
	
	@Autowired
	SysFormRadiobuttonlistMapper sysFormRadiobuttonlistMapper;
	
	@Autowired
	SysFormDatepickerMapper sysFormDatepickerMapper;
	
	@Autowired
	SysFormLookupMapper sysFormLookupMapper;
	
	@Autowired
	SysFormRichtextMapper sysFormRichtextMapper;
	
	@Autowired
	SysFormFileuploadMapper sysFormFileuploadMapper;
	
	@Autowired
	SysFormButtonSaveMapper sysFormButtonSaveMapper;
	
	@Autowired
	SysFormButtonAddMapper sysFormButtonAddMapper;
	
	@Autowired
	SysFormButtonRemoveMapper sysFormButtonRemoveMapper;
	
	@Autowired
	SysFormButtonImportMapper sysFormButtonImportMapper;
	
	@Autowired
	SysFormButtonCalculateMapper sysFormButtonCalculateMapper;
	
	@Autowired
	SysFormButtonIntroduceMapper sysFormButtonIntroduceMapper;
	
	@Autowired
	SysExecSqlMapper sysExecSqlMapper;
	@Autowired
	SysGridFilterMapper sysGridFilterMapper;
	@Autowired
	private SysUserOrgMapper sysUserOrgMapper;
	@Autowired
	private SysFormActionMapper sActionMapper;
	
	@Autowired
	SysFormconfigGridMapper  sysFormconfigGridMapper;
	@Autowired
	IFormService formService;

	@Autowired   
    HttpServletRequest request1;
	@Autowired  
	private HttpSession session;  
	@Autowired
	private SysSystemParamMapper paramMapper;
	
	@Autowired
	@Resource(name = "RedisService")
	private RedisService  redisService; 
	
	@Override
	public SysFormCombobox queryComboBoxInfoByColumnId(String columnId) {
		// TODO 自动生成方法存根
		SysFormComboboxExample example = new SysFormComboboxExample();
		SysFormComboboxExample.Criteria criteria = example.createCriteria();
		criteria.andComboboxFormColumnIdEqualTo(new BigDecimal(columnId));
		example.or(criteria);
		List<SysFormCombobox> list =   mapper.selectByExample(example);
		if(list.size()==0){
			logger.info("字段ID为："+columnId+",未查出对应的textboxd对象");
			return null;
		}else {
			SysFormCombobox l = list.get(0);
			return l;
		}
	}

	@Override
	public String saveComboBoxInfo(Map obj) throws Exception {
		// TODO 自动生成方法存根
		if(obj==null) {
			return null;
		}
		return "1";

	}
	
	public void setDictEntrys() throws Exception {
		SysDictTypeExample example = new SysDictTypeExample();
		List<SysDictType> dictTypes = sysDictTypeMapper.selectByExample(example);
		for (int i = 0; i < dictTypes.size(); i++) {
			ISysformconfigService.getDictEntrys(dictTypes.get(i).getDictTypeCode());
		}
	}
	
	public void setSysFields() throws Exception {
		SysEntityExample example = new SysEntityExample();
		List<SysEntity> sysEntitys = sysEntityMapper.selectByExample(example);
		for (int i = 0; i < sysEntitys.size(); i++) {
			ISysformconfigService.getSysFields(sysEntitys.get(i).getEntityCode());
		}
	}
	
	
	public void setSysMenusWithRoleId() throws Exception {
		SysRoleExample example = new SysRoleExample();
		List<SysRole> sysRoles = sysRoleMapper.selectByExample(example);
		for (int i = 0; i < sysRoles.size(); i++) {
			sysMenuService.queryMainframeMenuWithRoleId(sysRoles.get(i).getRoleId()+"",null);
		}
	}
	
	public void setMenusWithUserId() throws Exception {
		sysMenuService.queryMainframeMenu("1",null);
	}
	
	@Async
	public void setForm() throws Exception {
		
		String sql = "SELECT DISTINCT MENU_FORM_ID as formId from SYS_MENU  WHERE MENU_IS_DYNAMIC_FORM = 'Y' and MENU_FORM_ID is not NULL and MENU_FORM_ID<>''";
		List<Map> selectObjs = MybatisUtil.queryList("runtime.selectSql", sql);
		for (Map map : selectObjs) {
			String formId = map.get("formId")+"";
			try {
				ISysformconfigService.getForm(formId);
			} catch (Exception e) {
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		}
		/*int[] formIds = {13,16,47,51,52,57,59,61,63,67,69,74,97,103,106,154,159,252,272,274,276,278,281,283,285,287,291,298,301,324,399,405,427,430,453,519,521,537,579,583,586,591,594,597,600,603,606,609,611,642,643,651,655,658,660,1802,1817,1822,1826,1830,1847,1850,1851,1859,1862,1865,1868,1871,1874,1877,1880,1884,1888,1891,1894,1899,1903,1909,1911,1914,1916,1919,1922,1928,1929,1930,1934,1937,1942,1946,1948,1952,1955,1958,1960,1963,1966,1969,1972,1976};
		for (int i : formIds) {
			String formId = i+"";
			try {
				ISysformconfigService.getForm(formId);
			} catch (Exception e) {
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		}*/
	}
	
	public void setFormCaches() throws Exception {
		SysFormconfigExample example = new SysFormconfigExample();
		List<SysFormconfigWithBLOBs> forms = sysFormconfigMapper.selectByExampleWithBLOBs(example);
		for (int i = 0; i < forms.size(); i++) {
			ISysformconfigService.getFormColumns(forms.get(i).getFormDefId()+"");
			ISysformconfigService.getFormButtons(forms.get(i).getFormDefId(),"1");
			ISysformconfigService.getFormWheres(forms.get(i).getFormDefId(),null);
			ISysformconfigService.getFormFilterColumns(forms.get(i).getFormDefId()+"");
		}
	}
	
	public void setFormInfos() throws Exception {
		SysFormconfigExample example = new SysFormconfigExample();
		List<SysFormconfigWithBLOBs> forms = sysFormconfigMapper.selectByExampleWithBLOBs(example);
		ISysformconfigService.setFormInfos(forms);
	}
	
	public void setFormColumns() throws Exception {
		SysFormColumnExample example = new SysFormColumnExample();
		List<SysFormColumn> columns = sysFormColumnMapper.selectByExample(example);
		ISysformconfigService.setFormColumns(columns);
	}
	
	public void setColumnInfosByColumnId() throws Exception {
		SysFormColumnExample example = new SysFormColumnExample();
		List<SysFormColumn> columns = sysFormColumnMapper.selectByExample(example);
		for (int i = 0; i < columns.size(); i++) {
			ISysformconfigService.getEntityColumnByColumnId(columns.get(i).getFormColumnColumnId()+"");
		}
	}
	
	
	public void setSysFormTextBoxs() throws Exception {
		SysFormTextboxExample example = new SysFormTextboxExample();
		List<SysFormTextbox> textboxs = sysFormTextboxMapper.selectByExample(example);
		ISysformconfigService.setTextboxs(textboxs);
	}
	
	public void setSysFormComboboxs() throws Exception {
		SysFormComboboxExample example = new SysFormComboboxExample();
		List<SysFormCombobox> comboboxs = sysFormComboboxMapper.selectByExample(example);
		ISysformconfigService.setComboboxs(comboboxs);
	}
	
	public void setSysFormRadiobuttonlists() throws Exception {
		SysFormRadiobuttonlistExample example = new SysFormRadiobuttonlistExample();
		List<SysFormRadiobuttonlist> radiobuttonlists = sysFormRadiobuttonlistMapper.selectByExample(example);
		ISysformconfigService.setRadiobuttonlists(radiobuttonlists);
	}
	
	public void setSysDatepickers() throws Exception {
		SysFormDatepickerExample example = new SysFormDatepickerExample();
		List<SysFormDatepicker> datepickers = sysFormDatepickerMapper.selectByExample(example);
		ISysformconfigService.setDatepickers(datepickers);
	}
	
	public void setSysLookups() throws Exception {
		SysFormLookupExample example = new SysFormLookupExample();
		List<SysFormLookup> lookups = sysFormLookupMapper.selectByExample(example);
		ISysformconfigService.setLookups(lookups);
	}
	
	public void setSysRichtexts() throws Exception {
		SysFormRichtextExample example = new SysFormRichtextExample();
		List<SysFormRichtext> richtexts = sysFormRichtextMapper.selectByExample(example);
		ISysformconfigService.setRichtexts(richtexts);
	}
	
	public void setSysFileuploads() throws Exception {
		SysFormFileuploadExample example = new SysFormFileuploadExample();
		List<SysFormFileupload> fileuploads = sysFormFileuploadMapper.selectByExample(example);
		ISysformconfigService.setFileuploads(fileuploads);
	}

	@Override
	public void setSysFormButtonSaves() throws Exception {
		// TODO Auto-generated method stub
		SysFormButtonSaveExample example = new SysFormButtonSaveExample();
		List<SysFormButtonSave> buttonSaves = sysFormButtonSaveMapper.selectByExample(example);
		ISysformconfigService.setButtonSaves(buttonSaves);
	}

	@Override
	public void setSysFormButtonAdds() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSysFormButtonRemoves() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSysFormButtonImports() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSysFormButtonCalculates() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSysExecSqls() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSysFormButtonIntroduces() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Async
	public void setCardDictDataByColumnId() throws Exception {
		// TODO Auto-generated method stub
		SysFormComboboxExample example = new SysFormComboboxExample();
		List<SysFormCombobox> comboboxs = sysFormComboboxMapper.selectByExample(example);
		for (SysFormCombobox sysFormCombobox : comboboxs) {
			if (sysFormCombobox.getComboboxFormColumnId()!=null) {
				ISysformconfigService.setCardDictDataByColumnId(sysFormCombobox.getComboboxFormColumnId()+"","");
			}
		}
	}
	
	public void setSysGridFilters() throws Exception {
		SysGridFilterExample example = new SysGridFilterExample();
		List<SysGridFilter> filters = sysGridFilterMapper.selectByExample(example);
		ISysformconfigService.setGridFilters(filters);
	}
	
	public void initOnlineUsers() throws Exception {
		SysOnlineUserExample example = new SysOnlineUserExample();
		example.or().andOnlineUserIdIsNotNull();
		sysOnlineUserMapper.deleteByExample(example);
	}
	
	public void initTimerTasks() throws Exception {
		String sql = "update sys_timer set sys_timer_status = 0";
		MybatisUtil.insert("runtime.insertSql", sql);
	}
	
	public void initUserOrgs() throws Exception {
		String sql = "select distinct user_id from SYS_USER_ORG";
		List<Map> rs = MybatisUtil.queryList("runtime.selectSql", sql);
		for (int i = 0; i < rs.size(); i++) {
			String userId = rs.get(i).get("user_id")+"";
			SysUserOrgExample example = new SysUserOrgExample();
			com.myehr.pojo.sysRole.SysUserOrgExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(Integer.parseInt(userId));
			List<SysUserOrg> userOrgs = sysUserOrgMapper.selectByExample(example);
			
			String orgIds = "";
			for (int j = 0; j < userOrgs.size(); j++) {
				SysUserOrg obj = userOrgs.get(i);
				orgIds += obj.getOrgId()+",";
			}
			orgIds = orgIds.substring(0, orgIds.length()-1);
		
	    	redisService.set("orgIds_userId_"+userId,orgIds);
		}
	}

	@Override
	public void initSystemParam() {
		// TODO Auto-generated method stub
		SysSystemParamExample example = new SysSystemParamExample();
		List<SysSystemParam> params = paramMapper.selectByExample(example);
		Map map = new HashMap();
		for (SysSystemParam sysSystemParam : params) {
			map.put(sysSystemParam.getSysParamCode(), sysSystemParam);
		}
	
		redisService.set(("sysSystemParam").getBytes(), SerializeUtil.serialize(map));
	}

	@Override
	public void setSystemParam() {
		// TODO Auto-generated method stub
	}
	
	public void createCommonJsp() throws IOException {
		File f = new File("ttt.jsp");
		if (f.exists()) {  
			System.out.print("文件存在");  
		} else {  
			System.out.print("文件不存在");  
			f.createNewFile();// 不存在则创建  
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"utf-8");
		BufferedWriter output=new BufferedWriter(write);
		output.close(); 
	}

	
	
}
