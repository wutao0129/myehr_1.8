package com.myehr.pojo.formmanage.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.sql.visitor.functions.If;
import com.myehr.common.exception.DcfException;
import com.myehr.common.util.SpringContextUtils;
import com.myehr.pojo.cache.SysCacheConfig;
import com.myehr.pojo.formmanage.button.SysFormButtonCalculate;
import com.myehr.pojo.formmanage.button.SysFormButtonEditorWithBLOBs;
import com.myehr.pojo.formmanage.button.SysFormButtonExport;
import com.myehr.pojo.formmanage.button.SysFormButtonImport;
import com.myehr.pojo.formmanage.button.SysFormButtonSave;
import com.myehr.pojo.formmanage.form.SysExecSql;
import com.myehr.pojo.formmanage.form.SysFormButton;
import com.myehr.pojo.formmanage.form.SysFormGeneralParam;
import com.myehr.pojo.sysParam.SysRequestParam;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.generalparam.ISysFormGeneralParamService;

public class SysFormButtonCache implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SysFormButton pojo;
	
	private Object buttonExt;
	
	private List<SysCacheConfig> cacheConfig;
	
//	按钮后置sql
	private SysFormGeneralExecSqlCache hzsql ;

	public SysFormGeneralExecSqlCache getHzSql() {
		return hzsql;
	}

	public void setHzql(SysFormGeneralExecSqlCache hzsql) {
		this.hzsql = hzsql;
	}

//	按钮前置sql
	private SysFormGeneralExecSqlCache qzsql ;

	public SysFormGeneralExecSqlCache getQzSql() {
		return qzsql;
	}

	public void setQzql(SysFormGeneralExecSqlCache qzsql) {
		this.qzsql = qzsql;
	}	
	
	public SysFormButton getPojo() {
		return pojo;
	}

	public void setPojo(SysFormButton pojo) {
		this.pojo = pojo;
	}

	
	

	public List<SysCacheConfig> getCacheConfig() {
		return cacheConfig;
	}

	public void setCacheConfig(List<SysCacheConfig> cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	/**
	 * 该控件对应的通用参数
	 */
	private  Map<String,List<SysFormGeneralParamCache>> generalParams = new HashMap<String,List<SysFormGeneralParamCache>>();

	public Map<String,List<SysFormGeneralParamCache>> getGeneralParams() {
		return generalParams;
	}

	public void setGeneralParams(Map<String,List<SysFormGeneralParamCache>> generalParams) {
		this.generalParams = generalParams;
	}
	

	public Object getButtonExt() {
		return buttonExt;
	}

	public String getFormButtonCss() {
		return pojo.getFormButtonCss();
	}
	
	public String getFormButtonFormDefId() {
		return pojo.getFormButtonFormDefId()+"";
	}

	public String getFormButtonFormId() {
		return pojo.getFormButtonFormId();
	}

	public String getFormButtonHeight() {
		return pojo.getFormButtonHeight();
	}

	public String getFormButtonIcon() {
		return pojo.getFormButtonIcon();
	}

	public String getFormButtonId() {
		return pojo.getFormButtonId()+"";
	}

	public String getFormButtonName() {
		return pojo.getFormButtonName();
	}

	public String getFormButtonType() {
		return pojo.getFormButtonType();
	}

	public String getFormButtonWidth() {
		return pojo.getFormButtonWidth();
	}

	public String getFormButtonCode() {
		return pojo.getFormButtonCode();
	}

	public float getFormButtonSort() {
		return pojo.getFormButtonSort().floatValue();
	}

	public String getOperatorName() {
		return pojo.getOperatorName();
	}

	public Date getOperatorTime() {
		return pojo.getOperatorTime();
	}
	public String getFormButtonWarningInfo() {
		return pojo.getFormButtonWarningInfo();
	}
	
	public String getFormButtonCheckFun() {
		return pojo.getFormButtonCheckFun();
	}
	
	
	/**
	 * 构造函数 通过数据库加载
	 *
	 */
	public SysFormButtonCache(String buttonId){
		System.out.println(buttonId);
	}
	
	/**
	 * 构造函数 通过DataObject加载
	 *
	 */
	public SysFormButtonCache(SysFormButton obj,SysFormconfigCache currentform) throws NumberFormatException, DcfException, Exception{
		setThisByDataObject(obj);
		this.setButtonExt(currentform);
		//加载通用参数对象数据
		ISysFormGeneralParamService paramService = (ISysFormGeneralParamService)SpringContextUtils.getBeanById("ISysFormGeneralParamService");
		String paramType = FormParamConstants.FORM_BUTTON;
		if (this.getPojo().getFormButtonType().equals("calculate")) {
			paramType = FormParamConstants.BUTTON_CALCULATE;
		}
		List<SysFormGeneralParam> params = paramService.queryGeneralParamByTypeAndTypeValue(paramType,Long.parseLong(this.getFormButtonId()));
		for(int i=0; i<params.size(); i++){
			SysFormGeneralParamCache p =  new SysFormGeneralParamCache(params.get(i));
			String type = p.getParamTypeEnd();
			if(this.generalParams.containsKey(type)){
				List<SysFormGeneralParamCache> l = this.generalParams.get(type);
				l.add(p);
				this.generalParams.put(type, l);
			}else {
				List<SysFormGeneralParamCache> l = new ArrayList<SysFormGeneralParamCache>();
				l.add(p);
				this.generalParams.put(type, l);
			}
		}
		//加载按钮后置sql
		String type = FormParamConstants.EXECSQL_BUTTON_HZ_TYPE;
		this.hzsql = new SysFormGeneralExecSqlCache(type,this.getFormButtonId()+"");
		//加载按钮前置sql
		String type2 = FormParamConstants.EXECSQL_BUTTON_QZ_TYPE;
		this.qzsql = new SysFormGeneralExecSqlCache(type2,this.getFormButtonId()+"");
		this.cacheConfig = setCacheConfig(this.getFormButtonId());
	}
	
	public SysFormButtonCache() {
		// TODO Auto-generated constructor stub
	}

	private List<SysCacheConfig> setCacheConfig(String formButtonId) {
		ISysformconfigService service =  (ISysformconfigService)SpringContextUtils.getBeanById("ISysformconfigService");
		return service.getFreshCache("BUTTON",formButtonId,"DICT");
	}

	/**
	 * DataObject 填充对象
	 * @param obj
	 */
	private void setThisByDataObject(SysFormButton obj){
		System.out.println(obj.getFormButtonCheckFun());
		this.pojo = obj;
		//数据类型问题this.pojo.setOperatorTime((Date)obj.get("operatorName"));
		
		
		if((!this.getFormButtonIcon().isEmpty() && this.getFormButtonIcon()!="") && !this.getFormButtonIcon().startsWith("icon-")) {
			this.pojo.setFormButtonIcon("glyphicon-" + this.getFormButtonIcon());
		}
		String qzjs = (String) obj.getFormButtonCheckFun();
		qzjs = qzjs==null?"":qzjs;
		this.pojo.setFormButtonCheckFun(qzjs);
	}
	
	public void setButtonExt(Object buttonExt) {
		this.buttonExt = buttonExt;
	}

	/**
	 * 初始化按钮扩展属性
	 */
	public void setButtonExt(SysFormconfigCache currentform) throws NumberFormatException, DcfException, Exception {
		ISysformconfigService service = (ISysformconfigService)SpringContextUtils.getBeanById("ISysformconfigService");
		if("save_form".equals(this.getFormButtonType())||"free_submit".equals(this.getFormButtonType())||"save".equals(this.getFormButtonType())||"select".equals(this.getFormButtonType())||"other".equals(this.getFormButtonType())){
			SysFormButtonSave obj =service.getButtonSave(this.getFormButtonId());
			if(obj!=null){
				this.buttonExt = new SysFormButtonSaveCache(obj);
			}
		} else if("add".equals(this.getFormButtonType())||"edit".equals(this.getFormButtonType()) ||"batchEdit".equals(this.getFormButtonType())||"copyAndUpdate".equals(this.getFormButtonType())) {
			this.buttonExt = new SysFormButtonAddOrEditCache(this.getFormButtonId());
		} else if("edit1".equals(this.getFormButtonType())) {
			this.buttonExt = new SysFormButtonAddOrEditCache(this.getFormButtonId());
		} else if("remove".equals(this.getFormButtonType())) {
			this.buttonExt = new SysFormButtonRemoveCache(this.getFormButtonId());
		} else if ("export".equals(this.getFormButtonType())) {
			SysFormButtonExport obj = service.getButtonExport(this.getFormButtonId()+"");
			if (obj != null) {
				this.buttonExt = new SysFormButtonExportCache(obj,currentform);
			}
		} else if ("import".equals(this.getFormButtonType())) {
			//IButtonService service = (IButtonService)ApplicationContextFactory.getContext().getBean("IButtonService");
			SysFormButtonImport obj = service.getButtonImport(this.getFormButtonId()+"");
			if (obj != null) {
				this.buttonExt = new SysFormButtonImportCache(obj, currentform);
			}
		} else if("calculate".equals(this.getFormButtonType())){
			//IButtonService
			SysFormButtonCalculate obj = service.getButtonCalculate(this.getFormButtonId());
			if (obj != null) {
				this.buttonExt = new SysFormButtonCalculateCache(obj,this);
			}
		}else if("executeSQL".equals(this.getFormButtonType())) {
			//IButtonService service = (IButtonService)ApplicationContextFactory.getContext().getBean("IButtonService");
			SysExecSql obj = service.getSysExecSql(this.getFormButtonId());
			if (obj != null) {
				this.buttonExt = new SysFormGeneralExecSqlCache(obj);
			}
		}else if("introduce".equals(this.getFormButtonType())) {
			this.buttonExt = new SysFormButtonIntroduceCache(Long.parseLong(this.getFormButtonId()),currentform);
		}else if("editorModel".equals(this.getFormButtonType())||"email".equals(this.getFormButtonType())||"message".equals(this.getFormButtonType())) {
			SysFormButtonEditorWithBLOBs obj = service.getButtonEditor(FormParamConstants.EXECSQL_BUTTON, this.getFormButtonId());
			if (obj != null) {
				this.buttonExt = new SysFormButtonEditorCache(obj);
			}
		}
	}
	
	/**
	 * 获取当前按钮对象的html片段
	 */
	public String getHtml(String formCode,SysRequestParam request,String formid,String isApp){
		String type = this.pojo.getFormButtonType();
		if("save_form".equals(type)){
			type = "save";
		}
		String icon = "glyphicon "+this.pojo.getFormButtonIcon();
		if("introduce".equals(type)){
			icon = "icon-addnew";
		}
		String enabled="";
		if("true".equals(request.getIsView())) {
			enabled = " enabled=\"false\" ";
		}
		//处理js
		//初始化函数
		return SysCardFormBeansUtil.getButtonHtml(this.getFormButtonId(),this.getFormButtonCode(), this.getFormButtonName(), icon, this.getFormButtonIcon(),
				this.getFormButtonWidth(), this.getFormButtonCss(),formCode,type,enabled,formid,isApp);
	}
	
	/**
	 * 生成所有相关的js函数
	 * @param sbs
	 */
	public void getJs(StringBuffer[] sbs,String formCode,SysRequestParam request,SysFormconfigCache form,String isApp){
		if ("save_form".equals(this.getFormButtonType()) || "save".equals(this.getFormButtonType())||"free_submit".equals(this.getFormButtonType())) { //保存按钮
			SysFormButtonSaveCache buttonsave = (SysFormButtonSaveCache) this.buttonExt;
			if (buttonsave != null && buttonsave.getButtonSaveId() != null && !"0".equals(buttonsave.getButtonSaveId())) {
				if("1".equals(buttonsave.getButtonSaveType())){//卡片式表单
					//初始化函数
					String str1 = buttonsave.getButtonSaveInitFun();
					if(str1!=null&&!str1.equals("")){
						sbs[1].append(SysCardFormBeansUtil.NEW_LINE_ONLY);
						sbs[1].append(SysCardFormBeansUtil.getNbsp(2) + str1 + SysCardFormBeansUtil.NEW_LINE_ONLY);
					}
					//默认处理js
					SysFormColumnCache pkColumn = form.getPkColumn();
					String pkJsId = pkColumn.getJsId("id",form.getPojoform().getFormDefId().toString());
					String pkColumnName = pkColumn.getJsId("name",form.getPojoform().getFormDefId().toString());
					if("default".equals(buttonsave.getButtonSaveExcType())||buttonsave.getButtonSaveExcType()==null){
						String temp = SysCardFormBeansUtil.getCardFormSaveJs(this.getFormButtonCode()+formCode+"_click_"+form.getPojoform().getFormDefId(),buttonsave.getButtonSaveSuccessDeal(),sbs[10].toString(),Long.parseLong(this.getFormButtonId()), pkJsId,pkColumnName,
								this.hzsql,form,request);
						//, SysFormGeneralExecSql hzsql ,SysFormconfig form,HttpServletRequest request
						sbs[2].append(temp);
					}else if("url".equals(buttonsave.getButtonSaveExcType()))  {//自定义URL
						String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveUrl(),buttonsave.getButtonSaveSuccessDeal());
						sbs[2].append(temp);
					}else if("fun".equals(buttonsave.getButtonSaveExcType())) {
						String temp = SysCardFormBeansUtil.getCardFormFunSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveFun());
						sbs[2].append(temp);
					}else if("approve".equals(buttonsave.getButtonSaveExcType())) {
						String temp = SysCardFormBeansUtil.getCardFormSubmitJs(this.getFormButtonCode()+formCode+"_click_"+form.getPojoform().getFormDefId(),buttonsave.getButtonSaveSuccessDeal(),sbs[10].toString(),Long.parseLong(this.getFormButtonId()), pkJsId,pkColumnName,this.hzsql,form,request);
						sbs[2].append(temp);
					}else if("free".equals(buttonsave.getButtonSaveExcType())) {
						String temp = SysCardFormBeansUtil.getCardFormFreeSubmitJs(this.getFormButtonCode()+formCode+"_click_"+form.getPojoform().getFormDefId(),buttonsave.getButtonSaveSuccessDeal(),sbs[10].toString(),Long.parseLong(this.getFormButtonId()), pkJsId,pkColumnName,this.hzsql,form,request);
						sbs[2].append(temp);
					}
				}else{ //列表式表单保存按钮
					String str1 = buttonsave.getButtonSaveInitFun();
					if(str1!=null&&!str1.equals("")){
						sbs[8].append(SysCardFormBeansUtil.NEW_LINE_ONLY);
						sbs[8].append(SysCardFormBeansUtil.getNbsp(2)+str1+SysCardFormBeansUtil.NEW_LINE_ONLY);
					}
					String temp = SysGridFormBeansUtil.getGridFormSaveJs(buttonsave,this.getFormButtonCode()+formCode+"_click","grid_"+formCode,formCode,
							this.hzsql,form,request);
					sbs[7].append(temp);
					
				}
			}
		}else if("add".equals(this.getFormButtonType())||"edit".equals(this.getFormButtonType()) || "batchEdit".equals(this.getFormButtonType()) || "copyAndUpdate".equals(this.getFormButtonType())){ //新增按钮
			SysFormButtonAddOrEditCache button = (SysFormButtonAddOrEditCache)this.buttonExt;
			if(button!=null&&button.getButtonAddId()!=0){
				if(button.getButtonAddInitFun()!=null){
					sbs[8].append(button.getButtonAddInitFun());
				}
				/*if(button.getButtonAddInitFun()!=null){
					sbs[8].append(button.getButtonAddInitFun());
				}*/
				String str1 = button.getButtonAddInitFun();
				if(str1!=null&&!str1.equals("")){
					sbs[1].append(SysCardFormBeansUtil.NEW_LINE_ONLY);
					sbs[1].append(SysCardFormBeansUtil.getNbsp(2)+str1+SysCardFormBeansUtil.NEW_LINE_ONLY);
				
				}
				
				if("addUrl".equals(button.getButtonAddType())){ //弹窗式
					String url = "";
					String addfun ="";
					if("default".equals(button.getButtonAddExcType())||button.getButtonAddExcType()==null||"default".equals(button.getButtonAddExcType())){ //打开配置表单
						url = request.getContextPath()+SysGridFormBeansUtil.FORM_EXEC_URL+"?formId="+button.getButtonAddFormId();
						System.out.println(url);
					}else if("url".equals(button.getButtonAddExcType()))  {//自定义URL
						url = button.getButtonAddUrl() ;
						String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click_"+form.getFormDefId(),button.getButtonAddUrl(),button.getButtonAddSuccessDeal());
						sbs[7].append(temp);
					}else if("fun".equals(button.getButtonAddExcType()))  {//自定义方案
						addfun = button.getButtonAddFun() ;
						System.out.println(addfun);
						//String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveUrl(),buttonsave.getButtonSaveSuccessDeal());
						//sbs[2].append(temp);
					}
					//判断参数是否前面已增加参数
					String paramstr="";
					if ("edit".equals(this.getFormButtonType()) || "add".equals(this.getFormButtonType())|| "copyAndUpdate".equals(this.getFormButtonType())) {
						paramstr = button.getParamStr(request,("edit".equals(this.getFormButtonType())||"copyAndUpdate".equals(this.getFormButtonType())));
					}else {
						paramstr = button.getParamStrForBatch(request,"batchEdit".equals(this.getFormButtonType()));
					}
					String[] aa = paramstr.split("\\*");
					
					if(paramstr!=null&&!"".equals(paramstr)){
						if ("add".equals(this.getFormButtonType())) {
							if(url.indexOf("?")>=0){
								url = url+"&"+aa[0]+"*"+url+"&"+aa[1]+"*"+aa[2];
							}else {
								url = url+"?"+paramstr;
							}
						}
						if ("edit".equals(this.getFormButtonType())||"copyAndUpdate".equals(this.getFormButtonType())) {
							if("fun".equals(button.getButtonAddExcType())){
								
							}else {
								if(url.indexOf("?")>=0){
									url = url+"&"+aa[0]+"*"+url+"&"+aa[3]+"*"+aa[2];
								}else {
									url = url+"?"+aa[0]+"*"+url+"?"+aa[3]+"*"+aa[2];
								}
							}
							
						}
						if ("batchEdit".equals(this.getFormButtonType())) {
							if(url.indexOf("?")>=0){
								url = url+"&"+aa[0]+"*"+aa[1];
							}else {
								url = url+"?"+paramstr;
							}
						}
					}
					String temp = "";
					if("fun".equals(button.getButtonAddExcType())){
						String funName = this.getFormButtonCode()+formCode+"_click";
						temp = SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"_"+form.getFormDefId()+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
						temp += addfun;
						temp+= "\n}\n";
					} else {
						temp = SysGridFormBeansUtil.getGridFormSaveJsOpenWin(button,this,this.getFormButtonCode()+formCode+"_click",button.getButtonAddSuccessDeal(),url,"grid_"+formCode,("edit".equals(this.getFormButtonType())||"batchEdit".equals(this.getFormButtonType())||"copyAndUpdate".equals(this.getFormButtonType())),form.getPojoform().getFormDefId()+"",formCode);
					}
					
					sbs[7].append(temp);
				}else if("addRow".equals(button.getButtonAddType())) { //新增一行方式
//				判断参数是否前面已增加参数
					String paramstr = button.getParamStr(request,"edit".equals(this.getFormButtonType()));
					String url = request.getContextPath()+SysGridFormBeansUtil.FORM_EXEC_URL+"?formId="+button.getButtonAddFormId()+"&"+paramstr;
					if("default".equals(button.getButtonAddExcType())||button.getButtonAddExcType()==null){
						String temp = SysGridFormBeansUtil.getGridFormSaveJsAddRow(button,this,this.getFormButtonCode()+formCode+"_click",button.getButtonAddSuccessDeal(),url,"grid_"+formCode,form.getColumns(),request);
						sbs[7].append(temp);
					}else if("url".equals(button.getButtonAddExcType()))  {//自定义URL
						//String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveUrl(),buttonsave.getButtonSaveSuccessDeal());
						//sbs[2].append(temp);
					}
				}
			}
		}else if("edit1".equals(this.getFormButtonType())){ //编辑按钮 王利锋增加
			SysFormButtonAddOrEditCache button = (SysFormButtonAddOrEditCache)this.buttonExt;
			if(button!=null&&button.getButtonAddId()!=0){
				if(button.getButtonAddInitFun()!=null){
					sbs[8].append(button.getButtonAddInitFun());
				}
				if(button.getButtonAddInitFun()!=null){
					sbs[8].append(button.getButtonAddInitFun());
				}
				String str1 = button.getButtonAddInitFun();
				if(str1!=null&&!str1.equals("")){
					sbs[1].append(SysCardFormBeansUtil.NEW_LINE_ONLY);
					sbs[1].append(SysCardFormBeansUtil.getNbsp(2)+str1+SysCardFormBeansUtil.NEW_LINE_ONLY);
				
				}
				
				if("addUrl".equals(button.getButtonAddType())){ //弹窗式
					String url = "";
					String addfun ="";
					System.out.println(button.getButtonAddExcType());
					if("default".equals(button.getButtonAddExcType())||button.getButtonAddExcType()==null||"default".equals(button.getButtonAddExcType())){ //打开配置表单
						url = request.getContextPath()+SysGridFormBeansUtil.FORM_EXEC_URL+"?formId="+button.getButtonAddFormId();
						System.out.println(url);
					}else if("url".equals(button.getButtonAddExcType()))  {//自定义URL
						url = button.getButtonAddUrl() ;
						//String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveUrl(),buttonsave.getButtonSaveSuccessDeal());
						//sbs[2].append(temp);
					}else if("fun".equals(button.getButtonAddExcType()))  {//自定义方案
						addfun = button.getButtonAddFun() ;
						System.out.println(addfun);
						//String temp = SysCardFormBeansUtil.getCardFormUrlSaveJs(this.getFormButtonCode()+formCode+"_click",buttonsave.getButtonSaveUrl(),buttonsave.getButtonSaveSuccessDeal());
						//sbs[2].append(temp);
					}
					//判断参数是否前面已增加参数
					String paramstr = button.getParamStr(request,"edit1".equals(this.getFormButtonType()));
					if(paramstr!=null&&!"".equals(paramstr)){
						if(url.indexOf("?")>=0){
							url = url+"&"+paramstr;
						}else {
							url = url+"?"+paramstr;
						}
					}
					String temp = "";
					if("fun".equals(button.getButtonAddExcType())){
						String funName = this.getFormButtonCode()+formCode+"_click";
						temp = SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
						temp += addfun;
						temp+= "}";
						
					} else {
						temp = SysGridFormBeansUtil.getGridFormSaveJsOpenWin(button,this,this.getFormButtonCode()+formCode+"_click",button.getButtonAddSuccessDeal(),url,"grid_"+formCode,"edit1".equals(this.getFormButtonType()),form.getPojoform().getFormDefId()+"",formCode);
						
					}
					
					sbs[7].append(temp);
				}else if("addRow".equals(button.getButtonAddType())) { //新增一行方式
					//判断参数是否前面已增加参数
					String addfun ="";
					addfun = "var rows = grid_"+formCode+".getData();"
					+"if (rows.length > 0) {"
					+ "for (var i = 0, l = rows.length; i < l; i++) {"
					+   "var row = rows[i];"
					+ "grid_"+formCode+".beginEditRow(row);"
					+ "}"
					+"} ";
					String temp = "";
					String funName = this.getFormButtonCode()+formCode+"_click";
					temp = SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp += addfun;
					temp+= "}";
					sbs[7].append(temp);
				}
			}
		}else if("remove".equals(this.getFormButtonType())) { //删除按钮
			SysFormButtonRemoveCache button = (SysFormButtonRemoveCache)this.buttonExt;
			if(button!=null&&button.getButtonRemoveId()!=0){
				if(button.getButtonRemoveInitFun()!=null){
					sbs[8].append(button.getButtonRemoveInitFun()+SysCardFormBeansUtil.NEW_LINE_ONLY);
				}
				//1 判断删除方式  
				String removeType = button.getButtonRemoveType();
				if("1".equals(removeType)) { //js页面删除数据
					/*String temp = SysGridFormBeansUtil.getGridFormRemoveJs(button,this.getFormButtonCode()+formCode+"_click","grid_"+formCode);
					sbs[7].append(temp);*/
					String pkColumnName = form.getPkColumn().getFormFiledTablename();
					String temp = SysGridFormBeansUtil.getGridFormRemoveDefaultJs(button,pkColumnName,this.getFormButtonCode()+formCode+"_click","grid_"+formCode,formCode,form.getPojoform().getFormDefId()+"",this.getPojo().getFormButtonCheckFun());
					sbs[7].append(temp);
				}else { //后台数据删除方式
					//获取js方法
					String pkColumnName = form.getPkColumn().getFormFiledTablename();
					String temp = SysGridFormBeansUtil.getGridFormRemoveDefaultJs(button,pkColumnName,this.getFormButtonCode()+formCode+"_click","grid_"+formCode,formCode,form.getPojoform().getFormDefId()+"",this.getPojo().getFormButtonCheckFun());
					sbs[7].append(temp);
				}
			}
		} else if ("copy".equals(this.getFormButtonType())) { // 导出按钮
			String pkColumnName = form.getPkColumn().getFormFiledTablename();
			sbs[7].append("function "+this.getFormButtonCode()+formCode+"_click"+"_"+form.getPojoform().getFormDefId()+"(){\n");
			sbs[7].append("    var selects = $(\"#"+formCode+"_List\").bootstrapTable('getSelections');\n");
			sbs[7].append("    if(selects!=null && selects.length>0){\n");
			sbs[7].append("        select = selects[0];\n");
			sbs[7].append("    }else {\n");
			sbs[7].append("        alert('请选择数据');return ;\n");
			sbs[7].append("    }\n");
			
			sbs[7].append("    var param={}\n");
			sbs[7].append("    param={formSaveTable:'"+form.getPojoform().getFormDefSavetable()+"',pkColumn:'"+pkColumnName+"',params:selects};\n");
			sbs[7].append("    $.ajax({\n");
			sbs[7].append("        url:'/myehr/form/copyData.action',\n");
			sbs[7].append("        type:'post',\n");
			sbs[7].append("        data: JSON.stringify(param),\n");
			sbs[7].append("        cache: false,\n");
			sbs[7].append("        contentType: 'application/json;charset=utf-8',\n");
			sbs[7].append("        success: function (text) {\n");
			sbs[7].append("            if(text!='1'){\n");
			sbs[7].append("                alert('复制失败');\n");
			sbs[7].append("            } else {\n");
			sbs[7].append("                alert('复制成功');\n");
			sbs[7].append("            }\n");
			sbs[7].append("        }\n");
			sbs[7].append("    });\n");
			sbs[7].append("}\n");
		} else if ("export".equals(this.getFormButtonType())) { // 导出按钮
			SysFormButtonExportCache button = (SysFormButtonExportCache)this.buttonExt;
			if (button != null && (button.getButtonExportId()!=null && !button.getButtonExportId().equals(""))) {
				if (button.getButtonExportInitFun()!=null && !button.getButtonExportInitFun().equals("")) {
					sbs[8].append(button.getButtonExportInitFun() + SysCardFormBeansUtil.NEW_LINE_ONLY);
				}
				
				String excType = button.getButtonExportExcType();
				if (excType!=null && !excType.equals("")) {
					String funName = this.getFormButtonCode() + formCode + "_click";
					if ("default".equals(excType)) {// 默认处理
						sbs[7].append(SysGridFormBeansUtil.getGridFormExport(request, form, button, funName));
					} else if ("fun".equals(excType)) {// 自定义处理
						sbs[7].append(SysGridFormBeansUtil.getGridFormButtonFunJs(funName, button.getButtonExportFun()));
					}
				}
			}
		} else if ("import".equals(this.getFormButtonType())) { // 导入按钮
			SysFormButtonImportCache button = (SysFormButtonImportCache)this.buttonExt;
			if (button != null && (button.getButtonImportId()!=null && button.getButtonImportId()!="")) {
				if (button.getButtonImportInitFun()!=null && button.getButtonImportInitFun()!="") {
					sbs[8].append(button.getButtonImportInitFun() + SysCardFormBeansUtil.NEW_LINE_ONLY);
				}
				
				String excType = button.getButtonImportExcType();
				if (excType!=null && excType!="") {
					String funName = this.getFormButtonCode() + formCode + "_click";
					if ("default".equals(excType)) {// 默认处理
						sbs[7].append(SysGridFormBeansUtil.getGridFormImport(request, form, button, funName));
					} else if ("fun".equals(excType)) {// 自定义处理
						sbs[7].append(SysGridFormBeansUtil.getGridFormButtonFunJs(funName, button.getButtonImportFun()));
					}
				}
			}
		} else if("select".equals(this.getFormButtonType())||"other".equals(this.getFormButtonType())){//选择按钮
			SysFormButtonSaveCache buttonsave = (SysFormButtonSaveCache) this.buttonExt;
			if("other".equals(this.getFormButtonType())){
				if(buttonsave!=null){
					sbs[2].append(SysTreeFormBeansUtil.getWriteJs(buttonsave.getButtonSaveFun(),this.getFormButtonCode(),form.getFormDefCode(),form.getPojoform().getFormDefId()+""));
				}
			}else {
				if("3".equals(form.getFormDefLayoutType())){//树式表单
					if (buttonsave != null && buttonsave.getButtonSaveId() != null && !"0".equals(buttonsave.getButtonSaveId())&&buttonsave.getButtonSaveFun()!=null) {
						sbs[2].append(SysTreeFormBeansUtil.getWriteJs(buttonsave.getButtonSaveFun(),this.getFormButtonCode(),form.getFormDefCode(),form.getPojoform().getFormDefId()+""));
					}else { //生成默认js
						//生成GetData方法
				//		String getDataJs = SysTreeFormBeansUtil.getSelectGetDataButtonJs(form.getFormDefCode(),form.getTree().getFormTreeIdField(),form.getTree().getFormTreeTextField());
			//			sbs[2].append(getDataJs);
						//生成SetData方法
			//			String setDataJs  =SysTreeFormBeansUtil.getSelectSetDataButtonJs(form.getFormDefCode(),form.getTree().getFormTreeIdField(),form.getTree().getFormTreeTextField());
			//			sbs[2].append(setDataJs);
						//获取按钮点击函数
						String clickJs = SysTreeFormBeansUtil.getSelectClickButtonJs(form.getFormDefCode(),this.getFormButtonCode(),form.getTree().getFormTreeIdField(),form.getTree().getFormTreeTextField(),form.getTree().getFormAllowSelect(),String.valueOf(form.getFormDefId()));
						sbs[2].append(clickJs);
					}
				}else if("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())) { //列表
					if (buttonsave != null && buttonsave.getButtonSaveId() != null && !"0".equals(buttonsave.getButtonSaveId())&&buttonsave.getButtonSaveFun()!=null) {
						sbs[2].append(SysGridFormBeansUtil.getWriteJs(buttonsave.getButtonSaveFun(),this.getFormButtonCode(),form.getFormDefCode()));
					}else{ //生成默认js
//                         //	生成GetData方法
//						String getDataJs = SysGridFormBeansUtil.getSelectGetDataButtonJs(form.getFormDefCode(),form.getGrid().getFormGridIdField(),form.getGrid().getFormGridTextField());
//						sbs[2].append(getDataJs);
						
						String clickJs = SysGridFormBeansUtil.getSelectClickButtonJs(form.getFormDefCode(),this.getFormButtonCode(),form.getGrid().getFormGridIdField(),form.getGrid().getFormGridTextField(),pojo.getFormButtonFormDefId().toString());
						sbs[2].append(clickJs);
					}
				}
			}
		}else  if("calculate".equals(this.getFormButtonType())) { //计算按钮
			SysFormButtonCalculateCache buttoncalculate = (SysFormButtonCalculateCache) this.buttonExt;
			if (buttoncalculate != null && buttoncalculate.getCalculateId().intValue()!=0) {
				String funName = this.getFormButtonCode() + formCode + "_click";
				
				String str = SysGridFormBeansUtil.getGridFormCalculate(request, form, buttoncalculate, funName);
				
				sbs[7].append(str);
			}
		}else if("executeSQL".equals(this.getFormButtonType())) {
			SysFormGeneralExecSqlCache button = (SysFormGeneralExecSqlCache)this.buttonExt;
			if(button!=null && button.getExecSqlRelaid()!=0){
				String jsParams = new String("var paramsMap = {");
				List<String[]> params = button.getParams();
				String getListField = "";
				
				String afterExecType = button.getAfterExecType();
				String afterExecFun = button.getAfterExecFun();
				String codeContent = (afterExecFun!=null && afterExecFun.length()>0) ? afterExecFun : "";
				String afterExecFunCode = "function successExecFun(){" + codeContent + "}";
				String afterSuccess = "";
				String warningstr = "";
				String warningstrend = "";
				String warningInfo = this.getFormButtonWarningInfo();
				warningInfo= warningInfo==null?"":warningInfo;
				if(!"".equals(warningInfo)){
					warningstr = "nui.confirm(\""+warningInfo+"\",\"警告\",function(action){if(action!=\"ok\") return"+SysCardFormBeansUtil.NEW_LINE;
					warningstrend= " });";
				}
				
				if("self_define".equals(afterExecType)){
					afterSuccess = "successExecFun();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				} else if("reload".equals(afterExecType)){
					if("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())){//列表
						afterSuccess = "refresh_" + form.getFormDefCode() + "();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					} else if("1".equals(form.getFormDefLayoutType())){
						afterSuccess = "_initData_"+form.getFormDefId()+"(null);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					}
				} else {
					afterSuccess = "var index = parent.layer.getFrameIndex(window.name);"+SysCardFormBeansUtil.NEW_LINE_ONLY+SysCardFormBeansUtil.getNbsp(4)+"parent.layer.close(index);//执行关闭"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
//				考虑从表更新主表
				/*afterSuccess += SysCardFormBeansUtil.getNbsp(6)+"if(window.parent._loadmstmaindata){"+SysCardFormBeansUtil.NEW_LINE;
				afterSuccess += SysCardFormBeansUtil.getNbsp(7)+"window.parent._loadmstmaindata()"+SysCardFormBeansUtil.NEW_LINE;
				afterSuccess += SysCardFormBeansUtil.getNbsp(6)+"}"+SysCardFormBeansUtil.NEW_LINE;*/
				
				//String grid = "grid_" + form.getFormDefCode();
				getListField += ("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())) ? ("var columnDataRows" + " = $(\"#"+form.getFormDefCode()+"_List\").bootstrapTable('getSelections');\n") : "";
				
				boolean isCheckSelectData =  false;
				for(String[] param : params){
					if("c".equals(param[0])){
						String str = param[1].substring(param[1].indexOf("_")+1);
						SysFormColumnCache sfc = form.serchColumnByColumnId(Long.parseLong(str));
						if (sfc!=null) {
							String str1 = sfc.getJsId("id",form.getFormDefId()+"");
							String str11 = sfc.getJsId1("id");
							String str2 = str1.substring(str1.indexOf(".")+1);
							String str21 = str11.substring(str1.indexOf(".")+1);
							
							String str3 = null;
							if("1".equals(form.getFormDefLayoutType())){//卡片
								str3 = SysCardFormBeansUtil.getNbsp(2) + "'c_" + str + "' : " + "document.getElementById(\""+str1+"\").value,\n" ;
							} else if("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())) {//列表
								String str2replace = str2.replace(".", "_");
								//String str21replace = str21.replace(".", "_");
								//String[] str2split = str2.split("\\.");
								String[] str21split = str21.toUpperCase().split("\\.");
								String tempGetListField = "";
								if(sfc.getFormColumnGuiType().equals("6")){
									SysDatepickerCache datepicker = (SysDatepickerCache)sfc.getFormColumGui();
									tempGetListField = 
									SysCardFormBeansUtil.getNbsp(1)+"var " + str2replace.toLowerCase() + "Datas;\n"
									+SysCardFormBeansUtil.getNbsp(1)+"var temp"+str2replace.toLowerCase()+"Datas = [];\n"
									+SysCardFormBeansUtil.getNbsp(1)+"for(var i=0; i<columnDataRows" + ".length; i++){\n"
									//+ 	"	temp"+str2.toLowerCase()+"Datas.push(\"'\"+" + str2.toLowerCase() + "Rows" + "[i]."+ str2 +"+\"'\");\n"
									+SysCardFormBeansUtil.getNbsp(2)+"temp"+str2replace.toLowerCase()+"Datas.push(\"'\"+" + "new Date(columnDataRows" + "[i]."+ str21split[0]+").Format(\""+datepicker.getDatepickerFormat().replace("HH", "hh")+"\")+\"'\");\n"
									+SysCardFormBeansUtil.getNbsp(1)+"}\n"
									+SysCardFormBeansUtil.getNbsp(1)+str2replace.toLowerCase() + "Datas = temp"+str2replace.toLowerCase()+"Datas.join(',');\n"  ;
								}else{
									tempGetListField = 
									SysCardFormBeansUtil.getNbsp(1)+"var " + str2replace.toLowerCase() + "Datas;\n"
									+SysCardFormBeansUtil.getNbsp(1)+"var temp"+str2replace.toLowerCase()+"Datas = [];\n"
									+SysCardFormBeansUtil.getNbsp(1)+"for(var i=0; i<columnDataRows" + ".length; i++){\n"
									//+ 	"	temp"+str2.toLowerCase()+"Datas.push(\"'\"+" + str2.toLowerCase() + "Rows" + "[i]."+ str2 +"+\"'\");\n"
									+SysCardFormBeansUtil.getNbsp(2)+"temp"+str2replace.toLowerCase()+"Datas.push(\"'\"+" + "columnDataRows" + "[i]."+ str21split[0] +"+\"'\");\n"
									+SysCardFormBeansUtil.getNbsp(1)+"}\n"
									+SysCardFormBeansUtil.getNbsp(1)+str2replace.toLowerCase() + "Datas = temp"+str2replace.toLowerCase()+"Datas.join(',');\n"  ;
								}
								
								
								getListField += tempGetListField;
								str3 = SysCardFormBeansUtil.getNbsp(2) + "'c_" + str + "' : " + str2replace.toLowerCase() + "Datas,\n" ;
								isCheckSelectData = true;
							}
							jsParams += str3;
						}
						
						
					} else if("s".equals(param[0])){
						String str = param[1];
						String str1 = SysCardFormColumnUtil.getDefaultValue(request,"session",str);
						if(str1!=null){
							String str2 = SysCardFormBeansUtil.getNbsp(2) + "'s_" + param[1] + "' : '" + str1 + "',\n";
							jsParams += str2;
						}
					} else if("r".equals(param[0])){
						String str = param[1];
						String str1 = SysCardFormColumnUtil.getDefaultValue(request,"request",str);
						if(str1!=null){
							String str2 = SysCardFormBeansUtil.getNbsp(2) + "'r_" + param[1] + "' : '" + str1 + "',\n";
							jsParams += str2;
						}
					} 
				}
				if(jsParams.endsWith(",\n")){
					jsParams = jsParams.substring(0,jsParams.length()-2);
				}
//				校验
				String validationStr ="";
				if("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())){//列表
					/*String gridname = "grid_" + form.getFormDefCode() ;
					validationStr += SysCardFormBeansUtil.getNbsp(2) +gridname+ ".validate();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					validationStr += SysCardFormBeansUtil.getNbsp(2) +"if("+gridname+".isValid() == false) {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					validationStr += SysCardFormBeansUtil.getNbsp(3) +"return;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					validationStr += SysCardFormBeansUtil.getNbsp(2) +"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;*/
				} else if("1".equals(form.getFormDefLayoutType())){
					/*validationStr += SysCardFormBeansUtil.getNbsp(2)+" var _form = new nui.Form(\"#form1\");"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					validationStr += SysCardFormBeansUtil.getNbsp(2)+"_form.validate();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					validationStr += SysCardFormBeansUtil.getNbsp(2)+"if (_form.isValid() == false) return;"+SysCardFormBeansUtil.NEW_LINE_ONLY;*/
				}
				jsParams += "};\n";
				String scstr =  isCheckSelectData?"var checkselecdata = $(\"#"+form.getFormDefCode()+"_List\").bootstrapTable('getSelections');\n"+SysCardFormBeansUtil.getNbsp(1)+"if(checkselecdata==null||checkselecdata==''||checkselecdata.lenght<=0){\n"+SysCardFormBeansUtil.getNbsp(2)+"alert('请选择数据！');\n"+SysCardFormBeansUtil.getNbsp(2)+"return;\n"+SysCardFormBeansUtil.getNbsp(1)+"}\n":"";
				String code = validationStr +
				SysCardFormBeansUtil.getNbsp(1) + "var data = {" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+		"'formId' : _formId" +"_"+form.getFormDefId()+"," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+		"'buttonId' : buttonId," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+		"'params' : paramsMap," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+		"'containerParam' : containerParam" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				//
				//_form.validate();
				//if (_form.isValid() == false) return;
				//
				
				
				SysCardFormBeansUtil.getNbsp(1)+"};" +   SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(0)+ warningstr +
				//SysCardFormBeansUtil.getNbsp(0)+ "jQuery('#activity_pane').showLoading({'addClass': 'loading-indicator'});\n" +
				SysCardFormBeansUtil.getNbsp(0)+ "var indey=layer.load();\n" +
				//SysCardFormBeansUtil.getNbsp(0)+ "RENDERER.init(STRATEGY);\n" +
				SysCardFormBeansUtil.getNbsp(1)+"$.ajax({" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	url:'/myehr/form/execButtonExecSql.action'," + SysCardFormBeansUtil.NEW_LINE_ONLY +
				SysCardFormBeansUtil.getNbsp(2)+"	type : 'POST'," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	data: JSON.stringify(data)," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	cache : false," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	contentType: 'application/json;charset=utf-8'," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	success : function (text) {" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(3)+"	layer.close(indey);" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(3)+"		if (text[0] == '000000') {" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(4)+"			alert(text[1]);" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(4)+"			refreshDictCache(_formId"+"_"+form.getFormDefId()+",buttonId);\n" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				//SysCardFormBeansUtil.getNbsp(4)+afterSuccess + SysCardFormBeansUtil.NEW_LINE_ONLY +
				SysCardFormBeansUtil.getNbsp(4)+"		$(\"#"+form.getFormDefCode()+"_List\").bootstrapTable('refresh');\n"+
				SysCardFormBeansUtil.getNbsp(3)+"		} else {" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(4)+"		$(\"#"+form.getFormDefCode()+"_List\").bootstrapTable('refresh');\n"+
				SysCardFormBeansUtil.getNbsp(4)+"			refreshDictCache(_formId"+"_"+form.getFormDefId()+",buttonId);\n" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(4)+"			alert('执行结果：' + text[1]);" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(3)+"		}" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(4)+"       jQuery('#activity_pane').hideLoading();\n" +
				//SysCardFormBeansUtil.getNbsp(4)+"       RENDERER.hide();\n" +
				SysCardFormBeansUtil.getNbsp(3)+ afterSuccess + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	}," + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	error : function (jqXHR, textStatus, errorThrown) {" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(3)+"	layer.close(indey);" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(2)+"	}" + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				SysCardFormBeansUtil.getNbsp(1)+"	});"+ SysCardFormBeansUtil.NEW_LINE_ONLY +
				SysCardFormBeansUtil.getNbsp(0)+  warningstrend;
				String temps="";
				//调用sql前置触发,寅
				if(this.getPojo().getFormButtonCheckFun()!=null&&!this.getPojo().getFormButtonCheckFun().equals("")){
					String[] preFunction = this.getPojo().getFormButtonCheckFun().split(";");
					if (preFunction[0].toUpperCase().equals("SQLFORBID")) {
						
						temps+=SysCardFormBeansUtil.getNbsp(1)+"var sql = \""+preFunction[1]+"\";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(1)+"var data = myehr_SQLFORBID_sql(sql);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(1)+"if(data.length>0&&data[0]."+preFunction[3]+"=='"+preFunction[4]+"'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;

						temps+=SysCardFormBeansUtil.getNbsp(2)+"layer.alert('"+preFunction[2]+"', {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(3)+"icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(3)+" skin: 'layer-ext-moon' //"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(2)+"})"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						temps+=SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						String temp = SysCardFormBeansUtil.getNbsp(1)  + "\n" + 
								SysCardFormBeansUtil.getNbsp(0) +  SysCardFormBeansUtil.JAVASCRIPT_FUNCTION + this.getFormButtonCode() + formCode + "_click_"+form.getFormDefId() + SysCardFormBeansUtil.JAVASCRIPT_PARAM_START+ SysCardFormBeansUtil.JAVASCRIPT_PARAM_END+ SysCardFormBeansUtil.JAVASCRIPT_FUN_START + SysCardFormBeansUtil.NEW_LINE_ONLY +
								SysCardFormBeansUtil.getNbsp(1) +temps+ SysCardFormBeansUtil.NEW_LINE_ONLY+
								SysCardFormBeansUtil.getNbsp(1) + "var buttonId = " + button.getExecSqlRelaid() + SysCardFormBeansUtil.NEW_LINE;
								temp += SysCardFormBeansUtil.getNbsp(1) + scstr+ SysCardFormBeansUtil.getNbsp(1) + (("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())) ? getListField : "")
								+SysCardFormBeansUtil.getNbsp(1)+jsParams + code + SysCardFormBeansUtil.NEW_LINE_ONLY;
								temp += SysCardFormBeansUtil.getNbsp(1) + SysCardFormBeansUtil.JAVASCRIPT_FUN_END + SysCardFormBeansUtil.NEW_LINE_ONLY +
								SysCardFormBeansUtil.getNbsp(1) + SysCardFormBeansUtil.JAVASCRIPT_FUN_END + SysCardFormBeansUtil.NEW_LINE_ONLY +
												
								("self_define".equals(afterExecType) ? afterExecFunCode : "")+"\n";
								
								sbs[2].append(temp);
					}
				}else{
				String temp = SysCardFormBeansUtil.getNbsp(1)  + "\n" + 
				SysCardFormBeansUtil.getNbsp(0) +  SysCardFormBeansUtil.JAVASCRIPT_FUNCTION + this.getFormButtonCode() + formCode + "_click_"+form.getFormDefId() + SysCardFormBeansUtil.JAVASCRIPT_PARAM_START+ SysCardFormBeansUtil.JAVASCRIPT_PARAM_END+ SysCardFormBeansUtil.JAVASCRIPT_FUN_START + SysCardFormBeansUtil.NEW_LINE_ONLY +
				SysCardFormBeansUtil.getNbsp(1) +temps+ SysCardFormBeansUtil.NEW_LINE+
				SysCardFormBeansUtil.getNbsp(1) + "var buttonId = " + button.getExecSqlRelaid() + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(1) + scstr+ SysCardFormBeansUtil.getNbsp(1) + (("2".equals(form.getFormDefLayoutType())||"10".equals(form.getFormDefLayoutType())) ? getListField : "")
				+SysCardFormBeansUtil.getNbsp(1)+jsParams + code + SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp += SysCardFormBeansUtil.getNbsp(1) + SysCardFormBeansUtil.JAVASCRIPT_FUN_END + SysCardFormBeansUtil.NEW_LINE_ONLY + 
				("self_define".equals(afterExecType) ? afterExecFunCode : "")+"\n";
				
				sbs[2].append(temp);
				}
			}
		}else if("introduce".equals(this.getFormButtonType())){//引入按钮
			SysFormButtonIntroduceCache introduce = (SysFormButtonIntroduceCache) this.buttonExt;
			if(introduce!=null&&introduce.getIntroduceId()!=0){
				if("default".equals(introduce.getIntroduceType())){//默认处理
					String formId  = pojo.getFormButtonFormDefId().toString();
					String clickJs = SysGridFormBeansUtil.getIntroduceOpenJs(introduce.getGeneralParams(), introduce, request,this.getFormButtonCode(),formCode,formId);
					sbs[7].append(clickJs);
				}
			}
		}else if("editorModel".equals(this.getFormButtonType())||"message".equals(this.getFormButtonType())||"email".equals(this.getFormButtonType())){//模板按钮
			SysFormButtonEditorCache editorModel = (SysFormButtonEditorCache) this.buttonExt;
			if(editorModel!=null&&editorModel.getButtonEditorModelId()!=0){
				String formId  = pojo.getFormButtonFormDefId().toString();
				String clickJs = SysGridFormBeansUtil.getEditorModelOpenJs(form.getColumns(),editorModel,request,this.getFormButtonType(),this.getFormButtonCode(),formCode,formId);
				sbs[7].append(clickJs);
			}
		}else if("interface".equals(this.getFormButtonType())){//调用接口按钮
			/*SysFormButtonEditorCache editorModel = (SysFormButtonEditorCache) this.buttonExt;
			if(editorModel!=null&&editorModel.getButtonEditorModelId()!=0){
				String formId  = pojo.getFormButtonFormDefId().toString();
				String clickJs = SysGridFormBeansUtil.getEditorModelOpenJs(form.getColumns(),editorModel, request,this.getFormButtonType(),this.getFormButtonCode(),formCode,formId);
				sbs[7].append(clickJs);
			}*/
			sbs[7].append("function "+this.getFormButtonType()+form.getFormDefCode()+"_click_"+form.getFormDefId()+"(){\n"+
					"var obj = $(\"#"+form.getFormDefCode()+"_List\").bootstrapTable('getSelections');\n"+
					"var  _param ={formId:"+this.getFormButtonId()+",params:obj};\n"+
					"$.ajax({\n"+
					"	url:'/myehr/form/receiveInterfaceScheme.action',\n"+
					"	type:'post',\n"+
					"	data: JSON.stringify(_param),\n"+
					"	cache: false,\n"+
					"	contentType: 'application/json;charset=utf-8',\n"+
					"	success: function (text) {\n"+
					"		if(text!='1'){\n"+
					"			alert(text);\n"+
					"		} else {\n"+
					"			refresh_ceishi();\n"+
					"			if(\"undefined\" != typeof mstTab_activechanged ){\n"+
					"				mstTab_activechanged();\n"+
					"			}\n"+
					"		}\n"+
					"	},\n"+
					"	error: function (jqXHR, textStatus, errorThrown) {\n"+
					"		alert(jqXHR.responseText);\n"+
					"	}\n"+
					"});\n"+
					"}\n");
		}else if("submitButton".equals(this.getFormButtonType())){//提交按钮
			sbs[2].append(	"'<input class=\"btn btn-info submitButton\" id=\"submitButton_main1\" type=\"button\" value=\"提交\" style=\"width:50px\" onclick=\""+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()\"/>';\n");
			sbs[7].append(  "function "+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"(){\n"+
							"	var paramsMap = {};\n"+
							"	var _param = {};\n"+
							"	var hzsParam = [];\n"+
							"	_param.formId=formId_main;\n"+
							"	_param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	_param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	_param.paramsMap = paramsMap;\n"+
							//"	indexApprove = layer.load(1, {shade: [0.1,'#fff']});\n"+
							"	if(checkNull($(\"#MainElement\").find(\"form\"))){\n"+
							"		layer.close(indexApprove);\n"+
							"		return true;\n"+
							"	}else{\n"+
							"	var signstr = JSON.stringify(_param);\n"+
							
							"	var rule ='\"' ;\n"+
							"	var regS = new RegExp(rule,'g');\n"+
							"	var rule2 =':' ;\n"+
							"	var regS2 = new RegExp(rule2,'g');\n"+
							"	signstr = signstr.replace(regS, '');\n"+
							"	signstr = signstr.replace(regS2, '=');\n"+
							"	var sign = md5(signstr);\n"+

							//"		indexApprove = layer.load(4);\n"+
							"		$.ajax({\n"+
							"			url:'/myehr/form/saveButtonSave.action?sign='+sign,\n"+
							"			type:'post',\n"+
							"			data: JSON.stringify(_param),\n"+
							"			cache: false,\n"+
							"			async: false,\n"+
							"			contentType: 'application/json;charset=utf-8',\n"+
							"			success: function (text) {\n"+
							"				if(text[0]=='error'){\n"+
							"					alert(\"操作异常\");\n"+
							"					return ;\n"+
							"				}else {\n"+
							"					hzsParam = text;\n"+
							"				}\n"+
							"			}\n"+
							"		});\n"+
							"		if(hzsParam.length>0){\n"+
							"			_param.excresult = hzsParam;\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/form/hzAction.action?sign='+sign,\n"+
							"				type:'post',\n"+
							"				data: JSON.stringify(_param),\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				contentType: 'application/json;charset=utf-8',\n"+
							"				success: function (text) {\n"+
							"				}\n"+
							"			});\n"+
							"		}\n"+
							
							this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"();\n"+
							"}\n" +
							"}\n"+
							"function "+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(){\n");
			if (isApp!=null&&isApp.equals("ACTFREE")) {
				sbs[7].append(  "			saveFreeLines();\n"+
								"			var title = getActTitle_"+formCode+"();\n"+
								"			$.ajax({\n"+
								"				url:'/myehr/actfree/start.action?taskId='+actFree_taskId+'&orderBy='+actFree_orderBy+'&title='+title,\n");
			} else {
				sbs[7].append(  "			var paramsMap = {};\n"+
								"			var _param = {};\n"+
								"			paramsMap.bussinessId = BUSSINESSID_ACT;\n"+
								"			_param.formId=formId_main;\n"+
								"			_param = cardGetdata_main_act('#submitButton_main',formId_main);\n"+
								"			_param.buttonId='"+this.getFormButtonId()+"';\n"+
								"			_param.paramsMap = paramsMap;\n"+
								"			_param.actFlowParams= {key:param_key};\n"+
								"			$.ajax({\n"+
								"				url:'/myehr/act/task/start.action',\n"+
								"				data: JSON.stringify(_param),\n"+
								"				contentType: 'application/json;charset=utf-8',\n");
			}
			sbs[7].append(  "				type:'post',\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				success: function (text) {\n");
							//"					layer.close(indexApprove);\n"+
		if (isApp!=null&&isApp.equals("ACTFREE")) {
			sbs[7].append(  "					save_act_title();\n"+
							"					if(text[1]==0){\n"+
							"						layer.open({\n"+
							"	    					content: '保存成功',\n"+
							"	    					btn: '确定'\n"+
							"						});\n"+
							"					 	updateFiles();\n"+
							"					}else {\n"+
							"						layer.open({\n"+
							"	    					content: 'text[1]',\n"+
							"	    					btn: '确定'\n"+
							"						});\n"+
							"						return ;\n"+
							"					}\n");
		}else {
			sbs[7].append(  "					save_act_title();\n"+
							"					if(text[0]==0){\n"+
							//"						alert(\"保存成功\");\n"+
							"						layer.open({\n"+
							"	    					content: '保存成功',\n"+
							"	    					btn: '确定'\n"+
							"						});\n"+
							"						param_procInsId = text[1];\n"+
							"					 	updateFiles();\n"+
							//"						closex();\n"+
							"					}else if(text[0]=='error'){\n"+
							"						alert(\"操作异常:\"+text[1]);\n"+
							"						return ;\n"+
							"					}else {\n"+
							"						alert(\"操作失败\");\n"+
							"						return ;\n"+
							"					}\n");
		}

			sbs[7].append(  "				}\n"+
							"			});\n"+
							"		}\n");
		}else if("pass".equals(this.getFormButtonType())){//通过按钮
			String paramsMap = SysFormGeneralExecSqlCache.getCardParamsJsStr(this.getHzSql().getParams(),form, request, false);
			sbs[4].append(	"'	<input class=\"btn btn-info checkButton\" id=\""+this.getFormButtonCode()+"_main\" type=\"button\" value=\""+this.getFormButtonName()+"\" onclick=\""+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(\\'"+this.getFormButtonType()+"\\')\"/>'+\n");
			sbs[7].append(  "function "+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"(){\n"+
							"	var paramsMap = {};\n"+
							"	var _param = {};\n"+
							"	var hzsParam = [];\n"+
							"	_param.formId=formId_main;\n"+
							"	_param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	_param.buttonId='0000';\n"+
							"	_param.paramsMap = paramsMap;\n"+
							"	if(checkNull($(\"#MainElement\").find(\"form\"))){\n"+
							"		return true;\n"+
							"	}else{\n"+
							"	var signstr = JSON.stringify(_param);\n"+
							
							"	var rule ='\"' ;\n"+
							"	var regS = new RegExp(rule,'g');\n"+
							"	var rule2 =':' ;\n"+
							"	var regS2 = new RegExp(rule2,'g');\n"+
							"	signstr = signstr.replace(regS, '');\n"+
							"	signstr = signstr.replace(regS2, '=');\n"+
							"	var sign = md5(signstr);\n"+
							
							"		$.ajax({\n"+
							"			url:'/myehr/form/saveButtonSave.action?sign='+sign,\n"+
							"			type:'post',\n"+
							"			data: JSON.stringify(_param),\n"+
							"			cache: false,\n"+
							"			async: false,\n"+
							"			contentType: 'application/json;charset=utf-8',\n"+
							"			success: function (text) {\n"+
							"				if(text[0]=='error'){\n"+
							"					alert(\"操作异常\");\n"+
							"					return ;\n"+
							"				}else {\n"+
							"					hzsParam = text;\n"+
							"				}\n"+
							"			}\n"+
							"		});\n"+
							"		if(hzsParam.length>0){\n"+
							"			_param.excresult = hzsParam;\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/form/hzAction.action?sign='+sign,\n"+
							"				type:'post',\n"+
							"				data: JSON.stringify(_param),\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				contentType: 'application/json;charset=utf-8',\n"+
							"				success: function (text) {\n"+
							"				}\n"+
							"			});\n"+
							"		}\n"+
							"	}\n"+
							"}\n"+
							
							"function "+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(buttonType){\n"+
							"	if(checkedPointRequire()){\n"+
							"			if(!"+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()){\n");
			  sbs[7].append("				content = \"<textarea id='approveRemark' class='SHENPIYIJIANC form-control' rows='7' style='width:90%;' ></textarea>\";\n");
			  sbs[7].append("			}else{\n"+
							"				layer.alert('保存异常', {\n"+
							"		  		  	  offset:'50%',\n"+
							"				      shift: 6,\n"+
							"					  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
							"					  skin: 'layer-ext-moon' \n"+
							"					})\n"+
							"				return ;\n"+
							"			}\n");
			  sbs[7].append("		layer.open({\n"+
							"			title:'请填写审批意见',\n"+
							"			content: content,\n"+
							"			btn: ['确定', '取消'],\n"+
							"			success: function(layero, index){\n");
			  sbs[7].append("			},\n"+
							"			yes:function(index, layero){\n"+
							"				var approveRemark=document.getElementById(\"approveRemark\").value;\n"+
							"				var rejectType;\n"+
							"				layer.close(index);\n"+
							"				"+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType);\n"+
							"			},\n"+
							"		})\n");
			  sbs[7].append("	}else{\n"+
							"		layer.alert('有必选的指标未勾选', {\n"+
							"  		  	  offset:'50%',\n"+
							"		      shift: 6,\n"+
							"			  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
							"			  skin: 'layer-ext-moon' \n"+
							"			})\n"+
							"	}\n"+
							"}\n"+
							"function "+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType){\n");
			if (isApp!=null&&isApp.equals("ACTFREE")) {
				sbs[7].append(  "		 var _param = {};	\n"+
								"		 "+paramsMap+"\n"+
								"	     _param = cardGetdata_main('#submitButton_main',formId_main);\n"+
								"		 _param.taskId = actFree_taskId;\n"+
								"		 _param.orderBy = actFree_orderBy;\n"+
								"		 _param.content = $(\"#approveRemark\").val();\n"+
								"	     _param.formId=formId_main;\n"+
								"	     _param.paramsMap = paramsMap;\n"+
								"	     _param.buttonId='"+this.getFormButtonId()+"';\n"+
								"		 _param.content = $(\"#approveRemark\").val();\n"+
								"		 _param.action = 'approve';\n"+
								"		 \n"+
								"			$.ajax({\n"+
								"				url:'/myehr/actfree/complete.action',\n");
			} else {
				sbs[7].append(  "    	 var flowAction = param_flowAction;\n"+
								"	     var paramsMap = {};\n"+
								"	     var _param = {};\n"+
								"	     _param.formId=formId_main;\n"+
								"	     _param =  cardGetdata_main('#submitButton_main',formId_main);\n"+
								"	     _param.buttonId='"+this.getFormButtonId()+"';\n"+
								"	     _param.paramsMap = paramsMap;\n"+
								"	     _param.actFlowParams= {taskId:param_taskId,procInsId:param_procInsId,buttonType:buttonType,approveRemark:approveRemark,rejectType:rejectType};\n"+
								"			$.ajax({\n"+
								"				url:'/myehr/act/task/complete.action',\n");
			}
			sbs[7].append(  "			 type:'post',\n"+
							"		     data: JSON.stringify(_param),\n"+
							"		     cache: false,\n"+
							"			 async: false,\n"+
							"		     contentType: 'application/json;charset=utf-8',\n"+
							"		     success: function (text) {\n");
			if (isApp!=null&&isApp.equals("ACTFREE")) {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='success'){\n"+
								"				     alert(text[1]);\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}else {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='000000'){\n"+
								"				     alert(\"操作成功\");\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}
		}else if("reject".equals(this.getFormButtonType())){//驳回按钮
			sbs[4].append(	"'	<input class=\"btn btn-info checkButton\" id=\""+this.getFormButtonCode()+"_main\" type=\"button\" value=\""+this.getFormButtonName()+"\" onclick=\""+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(\\'"+this.getFormButtonType()+"\\')\"/>'+\n");
			sbs[7].append(  "function "+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"(){\n"+
							"	var paramsMap = {};\n"+
							"	var _param = {};\n"+
							"	var hzsParam = [];\n"+
							"	_param.formId=formId_main;\n"+
							"	_param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	_param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	_param.paramsMap = paramsMap;\n"+
							"	if(checkNull($(\"#MainElement\").find(\"form\"))){\n"+
							"		return true;\n"+
							"	}else{\n"+
							"	var signstr = JSON.stringify(_param);\n"+
							
							"	var rule ='\"' ;\n"+
							"	var regS = new RegExp(rule,'g');\n"+
							"	var rule2 =':' ;\n"+
							"	var regS2 = new RegExp(rule2,'g');\n"+
							"	signstr = signstr.replace(regS, '');\n"+
							"	signstr = signstr.replace(regS2, '=');\n"+
							"	var sign = md5(signstr);\n"+
							
							"		$.ajax({\n"+
							"			url:'/myehr/form/saveButtonSave.action?sign='+sign,\n"+
							"			type:'post',\n"+
							"			data: JSON.stringify(_param),\n"+
							"			cache: false,\n"+
							"			async: false,\n"+
							"			contentType: 'application/json;charset=utf-8',\n"+
							"			success: function (text) {\n"+
							"				if(text[0]=='error'){\n"+
							"					alert(\"操作异常\");\n"+
							"					return ;\n"+
							"				}else {\n"+
							"					hzsParam = text;\n"+
							"				}\n"+
							"			}\n"+
							"		});\n"+
							"		if(hzsParam.length>0){\n"+
							"			_param.excresult = hzsParam;\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/form/hzAction.action?sign='+sign,\n"+
							"				type:'post',\n"+
							"				data: JSON.stringify(_param),\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				contentType: 'application/json;charset=utf-8',\n"+
							"				success: function (text) {\n"+
							"				}\n"+
							"			});\n"+
							"		}\n"+
							"	}\n"+
							"}\n"+
							
							"function "+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(buttonType){\n"+
							"	if(checkedPointRequire()){\n"+
							"		if(!"+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()){\n");
				  if (isApp!=null&&isApp.equals("ACTFREE")) {
					  sbs[7].append("			getHisTaskForFree();\n"+
									"			var datas = hisTask;\n");
					  sbs[7].append("			content = \"<div style='margin-bottom:10px;width:90%'>驳回方式<select class='form-control' id='rejectType' style='height:40px'>\";\n"+
									"			content+= \"<option class='form-control' value ='previousNode'>驳回到上节点</option>\";\n"+
									"			content+= \"<option class='form-control' value ='startUser'>驳回到申请人</option>\";\n"+
									"			content+= \"</select></div>\";\n"+
									"			content += \"<div style='margin-bottom:10px;width:90%;'>驳回节点<select id='rejectNode' class='form-control' style='height:40px'>\";\n"+
									"			for(var i=0;i<datas.length-1;i++){\n"+
									"				content+=\"<option  class='form-control' value ='\"+datas[i].orderby+\"'>\"+datas[i].assigreeid+\"</option>\";\n"+
									"			}\n"+
									"			content+=\"</select></div>\";\n"+
									"			content += \"<textarea class='form-control SHENPIYIJIANC' id='approveRemark' rows='7' style='width:90%'></textarea>\";\n");
				  }else {
					  sbs[7].append("			getHisTask();\n"+
									"			var datas = hisTask;\n");
					  sbs[7].append("			content = \"<select id='shenpi2' title='通过意见' name='shenpi' style='width:90%;height:40px;margin-bottom:20px' class='form-control SHENPIYIJIANS' emptytext='' datafield1='dict' required='true' shownullitem='true' dictname='FinalRejection' datafield1='dict' onchange='writeYiJian(this)'></select><div style='margin-bottom:10px;width:90%'>驳回方式<select class='form-control' id='rejectType' style='height:40px'>\";\n"+
									"			content+= \"<option class='form-control' value ='previousNode'>驳回到上节点</option>\";\n"+
									"			content+= \"<option class='form-control' value ='startUser'>驳回到申请人</option>\";\n"+
									"			content+= \"</select></div>\";\n"+
									"			content += \"<div style='margin-bottom:10px;width:90%;'>驳回节点<select id='rejectNode' class='form-control' style='height:40px'>\";\n"+
									"			for(var i=0;i<datas.length;i++){\n"+
									"				content+=\"<option  class='form-control' value ='\"+datas[i].taskId+\"'>\"+datas[i].ACTNAME+\"</option>\";\n"+
									"			}\n"+
									"			content+=\"</select></div>\";\n"+
									"			content += \"<textarea class='form-control SHENPIYIJIANC' id='approveRemark' rows='7' style='width:90%'></textarea>\";\n");
				  }
				  sbs[7].append("		}else{\n"+
								"			layer.alert('保存数据异常', {\n"+
								"		  		  offset:'50%',\n"+
								"				  shift: 6,\n"+
								"				  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
								"				  skin: 'layer-ext-moon' \n"+
								"				})\n"+
								"			return ;\n"+
								"		}\n");
				  sbs[7].append("		layer.open({\n"+
								"			title:'请填写审批意见',\n"+
								"			content: content,\n"+
								"			btn: ['确定', '取消'],\n"+
								"			success: function(layero, index){\n"+
				  				"				$(\".layui-m-layercont\").css('padding','0 30px 20px 30px');\n" );
				  sbs[7].append("			},\n"+
								"			yes:function(index, layero){\n"+
								"				var approveRemark=document.getElementById(\"approveRemark\").value;\n"+
								"				var rejectType;\n"+
								"				rejectType=document.getElementById(\"rejectType\").value;\n"+
								"				layer.close(index);\n"+
								"				"+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType);\n"+
								"			},\n"+
								"		})\n");
			  sbs[7].append("	}else{\n"+
							"		layer.alert('有必选的指标未勾选', {\n"+
							"  		  	  offset:'50%',\n"+
							"		      shift: 6,\n"+
							"			  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
							"			  skin: 'layer-ext-moon' \n"+
							"			})\n"+
							"	}\n"+
							"}\n"+
							"function "+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType){\n");
							
		if (isApp!=null&&isApp.equals("ACTFREE")) {
			sbs[7].append(  "		 var _param = {};	\n"+
							"	     var paramsMap = {};\n"+
							"		 _param.taskId = actFree_taskId;\n"+
							"		 _param.orderBy = actFree_orderBy;\n"+
							"	     _param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"		 _param.content = $(\"#approveRemark\").val();\n"+
							"	     _param.formId=formId_main;\n"+
							"	     _param.paramsMap = paramsMap;\n"+
							"	     _param.buttonId='"+this.getFormButtonId()+"';\n"+
							"		 var detail = {};\n"+//shenpi2
							"		 if($(\"#rejectType\").val()=='startUser'){\n"+
							"		 	detail.isToCreator = 'Y';\n"+
							"		 	detail.nextOrder = '';\n"+//
							"		 }else{\n"+
							"		 	detail.isToCreator = 'N';\n"+
							"		 	detail.nextOrder = $(\"#rejectNode\").val();\n"+
							"		 }\n"+
							"		 _param.detail = detail;\n"+
							"		 \n"+
							"			$.ajax({\n"+
							"				url:'/myehr/actfree/complete.action',\n");
		} else {
			sbs[7].append(  "    	 var flowAction = param_flowAction;\n"+
							"	     var paramsMap = {};\n"+
							"	     var _param = {};\n"+
							"	     _param.formId=formId_main;\n"+
							"	     _param =  cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	     _param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	     _param.paramsMap = paramsMap;\n"+
							"	     _param.actFlowParams= {taskId:param_taskId,procInsId:param_procInsId,buttonType:buttonType,approveRemark:approveRemark,rejectType:rejectType};\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/act/task/complete.action',\n");
		}
			sbs[7].append(  "			 type:'post',\n"+
							"		     data: JSON.stringify(_param),\n"+
							"		     cache: false,\n"+
							"			 async: false,\n"+
							"		     contentType: 'application/json;charset=utf-8',\n"+
							"		     success: function (text) {\n");
			if (isApp!=null&&isApp.equals("ACTFREE")) {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='success'){\n"+
								"				     alert(text[1]);\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}else {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='000000'){\n"+
								"				     alert(\"操作成功\");\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}
		}else if("approved".equals(this.getFormButtonType())){//审批按钮
			String buttonName = this.getPojo().getFormButtonName();
			sbs[4].append(	"'	<input class=\"btn btn-info checkButton\" id=\""+this.getFormButtonCode()+"_main\" type=\"button\" value=\""+this.getFormButtonName()+"\" onclick=\""+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(\\'"+this.getFormButtonName()+"\\')\"/>'+\n");
			sbs[7].append(  "function "+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"(){\n"+
							"	var paramsMap = {};\n"+
							"	var _param = {};\n"+
							"	var hzsParam = [];\n"+
							"	_param.formId=formId_main;\n"+
							"	_param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	_param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	_param.paramsMap = paramsMap;\n"+
							"	if(checkNull($(\"#MainElement\").find(\"form\"))){\n"+
							"		return true;\n"+
							"	}else{\n"+
							"	var signstr = JSON.stringify(_param);\n"+
							
							"	var rule ='\"' ;\n"+
							"	var regS = new RegExp(rule,'g');\n"+
							"	var rule2 =':' ;\n"+
							"	var regS2 = new RegExp(rule2,'g');\n"+
							"	signstr = signstr.replace(regS, '');\n"+
							"	signstr = signstr.replace(regS2, '=');\n"+
							"	var sign = md5(signstr);\n"+
							
							"		$.ajax({\n"+
							"			url:'/myehr/form/saveButtonSave.action?sign='+sign,\n"+
							"			type:'post',\n"+
							"			data: JSON.stringify(_param),\n"+
							"			cache: false,\n"+
							"			async: false,\n"+
							"			contentType: 'application/json;charset=utf-8',\n"+
							"			success: function (text) {\n"+
							"				if(text[0]=='error'){\n"+
							"					alert(\"操作异常\");\n"+
							"					return ;\n"+
							"				}else {\n"+
							"					hzsParam = text;\n"+
							"				}\n"+
							"			}\n"+
							"		});\n"+
							"		if(hzsParam.length>0){\n"+
							"			_param.excresult = hzsParam;\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/form/hzAction.action?sign='+sign,\n"+
							"				type:'post',\n"+
							"				data: JSON.stringify(_param),\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				contentType: 'application/json;charset=utf-8',\n"+
							"				success: function (text) {\n"+
							"				}\n"+
							"			});\n"+
							"		}\n"+
							"	}\n"+
							"}\n"+
							"function "+this.getFormButtonType()+"_refer_"+this.getFormButtonId()+"(buttonType){\n"+
							"	if(checkedPointRequire()){\n");
			if(buttonName.equals("通过")){
			  sbs[7].append("			if(!"+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()){\n");
			  sbs[7].append("				var approveRemark=document.getElementById(\"approveRemark\").value;\n");
			  sbs[7].append("				var rejectType;\n");
			  sbs[7].append("				"+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType);\n");
			  sbs[7].append("			}else{\n"+
							"				layer.alert('有必选的指标未勾选', {\n"+
							"		  		  	  offset:'50%',\n"+
							"				      shift: 6,\n"+
							"					  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
							"					  skin: 'layer-ext-moon' \n"+
							"					})\n"+
							"				return ;\n"+
							"			}\n");
			}else if(buttonName.equals("驳回")){
				sbs[7].append("			if(!"+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()){\n");
				  if (isApp!=null&&isApp.equals("ACTFREE")) {
					  sbs[7].append("				getHisTaskForFree();\n"+
									"				var datas = hisTask;\n");
					  sbs[7].append("				content = \"<select id='shenpi2' title='通过意见' name='shenpi' style='width:90%;height:40px;margin-bottom:20px' class='form-control SHENPIYIJIANS' emptytext='' datafield1='dict' required='true' shownullitem='true' dictname='FinalRejection' datafield1='dict' onchange='writeYiJian(this)'></select><div style='margin-bottom:10px;width:90%'>驳回方式<select class='form-control' id='rejectType' style='height:40px'>\";\n"+
									"				content+= \"<option class='form-control' value ='previousNode'>驳回到上节点</option>\";\n"+
									"				content+= \"<option class='form-control' value ='startUser'>驳回到申请人</option>\";\n"+
									"				content+= \"</select></div>\";\n"+
									"				content += \"<div style='margin-bottom:10px;width:90%;'>驳回节点<select id='rejectNode' class='form-control' style='height:40px'>\";\n"+
									"				for(var i=0;i<datas.length-1;i++){\n"+
									"					content+=\"<option  class='form-control' value ='\"+datas[i].orderby+\"'>\"+datas[i].assigreeid+\"</option>\";\n"+
									"				}\n"+
									"				content+=\"</select></div>\";\n"+
									"				content += \"<textarea class='form-control SHENPIYIJIANC' id='approveRemark' rows='7' style='width:90%'></textarea>\";\n");
				  }else {
					  sbs[7].append("				getHisTask();\n"+
									"				var datas = hisTask;\n");
					  sbs[7].append("				content = \"<select id='shenpi2' title='通过意见' name='shenpi' style='width:90%;height:40px;margin-bottom:20px' class='form-control SHENPIYIJIANS' emptytext='' datafield1='dict' required='true' shownullitem='true' dictname='FinalRejection' datafield1='dict' onchange='writeYiJian(this)'></select><div style='margin-bottom:10px;width:90%'>驳回方式<select class='form-control' id='rejectType' style='height:40px'>\";\n"+
									"				content+= \"<option class='form-control' value ='previousNode'>驳回到上节点</option>\";\n"+
									"				content+= \"<option class='form-control' value ='startUser'>驳回到申请人</option>\";\n"+
									"				content+= \"</select></div>\";\n"+
									"				content += \"<div style='margin-bottom:10px;width:90%;'>驳回节点<select id='rejectNode' class='form-control' style='height:40px'>\";\n"+
									"				for(var i=0;i<datas.length;i++){\n"+
									"					content+=\"<option  class='form-control' value ='\"+datas[i].taskId+\"'>\"+datas[i].ACTNAME+\"</option>\";\n"+
									"				}\n"+
									"				content+=\"</select></div>\";\n");
				  }
				  	  sbs[7].append("			}else{\n"+
								"				layer.alert('有必选的指标未勾选', {\n"+
								"		  		  	  offset:'50%',\n"+
								"				      shift: 6,\n"+
								"					  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
								"					  skin: 'layer-ext-moon' \n"+
								"					})\n"+
								"				return ;\n"+
								"			}\n");
					  sbs[7].append("		layer.open({\n"+
									"			title:'请选择驳回对象',\n"+
									"			content: content,\n"+
									"			btn: ['确定', '取消'],\n"+
									"			success: function(layero, index){\n"+
									"				initSelectForShenPi(buttonType);\n");//layui-m-layercont
					  sbs[7].append("				$(\".layui-m-layercont\").css('padding','0 30px 20px 30px');\n");
					  sbs[7].append("			},\n"+
									"			yes:function(index, layero){\n"+
									"				var approveRemark=document.getElementById(\"approveRemark\").value;\n"+
									"				var rejectType;\n"+
									"				rejectType=document.getElementById(\"rejectType\").value;\n"+
									"				layer.close(index);\n"+
									"				"+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType);\n"+
									"			},\n"+
									"		})\n");
			}		
			  sbs[7].append("	}else{\n"+
							"		layer.alert('有必选的指标未勾选', {\n"+
							"  		  	  offset:'50%',\n"+
							"		      shift: 6,\n"+
							"			  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
							"			  skin: 'layer-ext-moon' \n"+
							"			})\n"+
							"	}\n"+
							"}\n"+
							"function "+this.getFormButtonType()+"_refer2_"+this.getFormButtonId()+"(buttonType,approveRemark,rejectType){\n");
							
		if (isApp!=null&&isApp.equals("ACTFREE")) {
			sbs[7].append(  "		 var _param = {};	\n"+
							"		 _param.taskId = actFree_taskId;\n"+
							"		 _param.orderBy = actFree_orderBy;\n"+
							"		 _param.content = $(\"#approveRemark\").val();\n"+
							"		 if(buttonType=='通过'){\n"+
							"		 	_param.action = 'approve';\n"+
							"		 }else{\n"+
							"		 	var detail = {};\n"+//shenpi2
							"		 	if($(\"#rejectType\").val()=='startUser'){\n"+
							"		 		detail.isToCreator = 'Y';\n"+
							"		 		detail.nextOrder = '';\n"+//
							"		 	}else{\n"+
							"		 		detail.isToCreator = 'N';\n"+
							"		 		detail.nextOrder = $(\"#rejectNode\").val();\n"+
							"		 	}\n"+
							"		 	_param.detail = detail;\n"+
							"		 }\n"+
							"		 \n"+
							"			$.ajax({\n"+
							"				url:'/myehr/actfree/complete.action',\n");
		} else {
			sbs[7].append(  "    	 var flowAction = param_flowAction;\n"+
							"	     var paramsMap = {};\n"+
							"	     var _param = {};\n"+
							"	     _param.formId=formId_main;\n"+
							"	     _param =  cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	     _param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	     _param.paramsMap = paramsMap;\n"+
							"	     _param.actFlowParams= {taskId:param_taskId,procInsId:param_procInsId,buttonType:buttonType,approveRemark:approveRemark,rejectType:rejectType};\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/act/task/complete.action',\n");
		}
			sbs[7].append(  "			 type:'post',\n"+
							"		     data: JSON.stringify(_param),\n"+
							"		     cache: false,\n"+
							"			 async: false,\n"+
							"		     contentType: 'application/json;charset=utf-8',\n"+
							"		     success: function (text) {\n");
			if (isApp!=null&&isApp.equals("ACTFREE")) {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='success'){\n"+
								"				     alert(text[1]);\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}else {
				sbs[7].append(  "			     if(text[0]=='error'){\n"+
								"				     alert(\"审批失败!\"+text[1]);\n"+
								"			     }else if(text[0]=='000000'){\n"+
								"				     alert(\"操作成功\");\n"+
								"					 updateFiles();\n"+
								"			     }\n"+
								"		     }\n"+
								"	     });\n"+
								"}\n");
			}
		}else if("temporaryStorage".equals(this.getFormButtonType())){//暂存按钮
			sbs[4].append(	"'		<input class=\"btn btn-info checkButton\" id=\""+this.getFormButtonCode()+"_main\" type=\"button\" value=\""+this.getFormButtonName()+"\" onclick=\""+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"()\"/>'+\n");
			sbs[7].append(  "function "+this.getFormButtonType()+"_save_"+this.getFormButtonId()+"(){\n"+
							"	var paramsMap = {};\n"+
							"	var _param = {};\n"+
							"	var hzsParam = [];\n"+
							"	_param.formId=formId_main;\n"+
							"	_param = cardGetdata_main('#submitButton_main',formId_main);\n"+
							"	_param.buttonId='"+this.getFormButtonId()+"';\n"+
							"	_param.paramsMap = paramsMap;\n"+
							"	if(checkNull($(\"#MainElement\").find(\"form\"))){\n"+
							"		return true;\n"+
							"	}else{\n"+
							"	var signstr = JSON.stringify(_param);\n"+
							
							"	var rule ='\"' ;\n"+
							"	var regS = new RegExp(rule,'g');\n"+
							"	var rule2 =':' ;\n"+
							"	var regS2 = new RegExp(rule2,'g');\n"+
							"	signstr = signstr.replace(regS, '');\n"+
							"	signstr = signstr.replace(regS2, '=');\n"+
							"	var sign = md5(signstr);\n"+

							"		$.ajax({\n"+
							"			url:'/myehr/form/saveButtonSave.action?sign='+sign,\n"+
							"			type:'post',\n"+
							"			data: JSON.stringify(_param),\n"+
							"			cache: false,\n"+
							"			async: false,\n"+
							"			contentType: 'application/json;charset=utf-8',\n"+
							"			success: function (text) {\n"+
							"				if(text[0]=='error'){\n"+
							"					alert(\"操作异常\");\n"+
							"					return ;\n"+
							"				}else if(text[0]='000000'){\n"+
							"					alert(\"操作成功\");\n"+
							"					closex();\n"+
							"				}else {\n"+
							"					hzsParam = text;\n"+
							"				}\n"+
							"			}\n"+
							"		});\n"+
							"		if(hzsParam.length>0){\n"+
							"			_param.excresult = hzsParam;\n"+
							"			$.ajax({\n"+
							"				url:'/myehr/form/hzAction.action',\n"+
							"				type:'post',\n"+
							"				data: JSON.stringify(_param),\n"+
							"				cache: false,\n"+
							"				async: false,\n"+
							"				contentType: 'application/json;charset=utf-8',\n"+
							"				success: function (text) {\n"+
							"				}\n"+
							"			});\n"+
							"		}\n"+
							"	}\n"+
							"}\n");
		}
	}

	private String getParamsMap(SysFormGeneralExecSqlCache hzSql2) {
		return null;
	}
}
