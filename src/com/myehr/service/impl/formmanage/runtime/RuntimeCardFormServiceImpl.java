package com.myehr.service.impl.formmanage.runtime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myehr.common.exception.DcfException;
import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.mybatis.Pager;
import com.myehr.common.util.ChangeCode;
import com.myehr.common.util.FormConfigResultMap;
import com.myehr.common.util.SpringContextUtils;
import com.myehr.pojo.formmanage.cache.FormParamConstants;
import com.myehr.pojo.formmanage.cache.SysDatepickerCache;
import com.myehr.pojo.formmanage.cache.SysFormColumnCache;
import com.myehr.pojo.formmanage.cache.SysFormComboboxCache;
import com.myehr.pojo.formmanage.cache.SysFormGridCache;
import com.myehr.pojo.formmanage.cache.SysFormWhereCache;
import com.myehr.pojo.formmanage.cache.SysFormconfigCache;
import com.myehr.pojo.formmanage.cache.SysGridFilterCache;
import com.myehr.pojo.formmanage.cache.UserObject;
import com.myehr.pojo.formmanage.form.SysFormColumn;
import com.myehr.service.RedisService;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.IsysFormColumnService;
import com.myehr.service.formmanage.runtime.IRuntimeCardFormService;

@Service
public class RuntimeCardFormServiceImpl  implements IRuntimeCardFormService  {
	private static Logger logger = LoggerFactory.getLogger(RuntimeCardFormServiceImpl.class);
	//private static final Logger log = DcfUtil.getLogger(RuntimeCardFormServiceImpl.class);
	
	@Autowired
	private ISysformconfigService sysformconfigService;
	
	@Autowired
	@Resource(name = "RedisService")
	private RedisService  redisService; 
	
	/**
	 * 初始化卡片表单数据
	 * @param requestParam
	 * @param pkId
	 * @param filter 查询字段参数
	 * @param heightGrade高级查询参数
	 * @return
	 * 实现思路
	 * 1 获取原始查询sql语句
	 * 
	 * 2 根据权限在 原来sql基础上包一个字查询
	 * 3 外出所查询的字段根据用户配置及权限过滤确定
	 * 4 根据用户设置的过滤条件增加增查询的where条件，其条件值及查询下规则根据配置来定
	 * 5 执行sql 返回结果集  
	 * 6 大字段怎么处理？？？
	 */
	public FormConfigResultMap cardFormInitData(Map<String, String> requestParam,
			Map<String, String> filter, Map<String,String>[] heightGrade,
			String pkId, String formId, String currentUserId, Pager page,
			UserObject user, String isView,Map<String, String> paramsMap,HttpServletRequest request,String userIds,Map<String, String> containerParam,String queryType,String sourceType) throws Throwable {
		// TODO 自动生成方法存根
		 //获取form对象
		long startTime = new Date().getTime();
		Map[] datas  = null;
		String userId = request.getSession().getAttribute("userid")+"";
		FormConfigResultMap result = new FormConfigResultMap();
		try {
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			String realSql = form.getFormDefSql();
			 
			String otherColumSql = form.getFormOtherColumnSql();
			String sql = this.getExcSql(form, otherColumSql, currentUserId, realSql, requestParam, filter, heightGrade, formId, form.getFilter(), user, isView,request,containerParam,sourceType);
			logger.info("**********************************拼接sql********"+(new Date().getTime()-startTime)+"mS******************************************");
			if (sql == null) {
				return result;
			}
			//判断是否需要分页
			SysFormGridCache grid = form.getGrid();
			boolean showPager  = false;
			if (grid != null) {
				String isPager = grid.getFormShowPager();
				if ("true".equals(isPager)) {
					showPager = true;
				}
				if (queryType!=null && "all".equals(queryType)) {
					showPager = false;
				}
			}
			//替换参数
			List<String[]> params = form.getParams();
			for(int i=0; i<params.size(); i++){
				String[] temp = params.get(i);
				String value = paramsMap.get(temp[0]+"_"+temp[1]);
				String pstr = "["+temp[0]+":"+temp[1]+"]";
				if(value==null){
					value="null";
				}
				if(sql.indexOf(pstr)>0){
					sql = sql.replace(pstr, value);
				}
			}
			logger.info("SQL:" + sql);
			
			if (form.getPojoform().getPowerSql()!=null) {
				String pSql = form.getPojoform().getPowerSql();
				if (form.getPojoform().getIsOrgPower()!=null&&form.getPojoform().getIsOrgPower().equals("Y")) {
			    	String orgIds = redisService.get("orgIds_userId_"+userId);
			    	if (orgIds==null) {
				    	pSql = pSql.replace("[re:orgIds]","");
					} else {
				    	pSql = pSql.replace("[re:orgIds]",orgIds);
					}
				}
				while(pSql.indexOf("[s:userId]")>=0){
					pSql = pSql.replace("[s:userId]",userId);
				}
				sql = "select p.* FROM (" +sql+ ") p " + pSql;
			}
			if (userIds!=null && !userIds.equals("")) {
				String [] aa = userIds.split(",");
				String bb ="";
				for (int i = 0; i < aa.length; i++) {
					if (aa[i]!=null && aa[i]!="") {
						bb+="'"+aa[i]+"',";
					} 
				}
				bb=bb.substring(0, bb.length()-1);
				String whereSql = "";
				if (sql.contains("A01_A0101")) {
					whereSql+=" z.A01_A0101 in ("+bb+") or";
				}
				if (sql.contains("A01_A0190")) {
					whereSql+=" z.A01_A0190 in ("+bb+") or";
				}
				if (sql.contains("A01_A019A")) {
					whereSql+=" z.A01_A019A in ("+bb+") or";
				}
				if (whereSql.length()>2) {
					whereSql = whereSql.substring(0, whereSql.length()-2);
					sql = "select z.* FROM (" +sql+ ") z " + "where "+ whereSql;
				}
			}
			if (form.getPojoform().getOrderSql()!=null) {
				sql = "select o.* FROM (" +sql+ ") o " + form.getPojoform().getOrderSql();
			}
			logger.info("**********************************拼接参数********"+(new Date().getTime()-startTime)+"mS******************************************");
			if (showPager == false) {
				List<Map> rs = MybatisUtil.queryList("runtime.selectSql", sql.toUpperCase()); // getDASTemplate().queryByNamedSql(DataObject.class, "com.dcf.form.runtime.cardform.excuteQuerySql", sql);
				logger.info("**********************************不分页查询********"+(new Date().getTime()-startTime)+"mS******************************************");
				if (userIds!=null && userIds!="") {
					List<Map> rs1 = new ArrayList<Map>();
					String [] aa = userIds.split(",");
					for (int i = 0; i < rs.size(); i++) {
						int flag=-1;
						for (int j = 0; j < aa.length; j++) {
							String A0190 = (String) rs.get(i).get("A01_A0190");
							String A0101 = (String) rs.get(i).get("A01_A0101");
							String A019A = (String) rs.get(i).get("A01_A019A");
							if (aa[i]!=null && aa[i]!="") {
								if (aa[i].equals(A0190) || aa[i].equals(A0101)) {
									flag=i;
								}else {
									continue;
								}
							}
						} 
						if (flag>0) {
							rs1.add(rs.get(flag));
						}
					}
					result.setRows(rs1);
				}else {
					result.setRows(rs);
				}
			} else {
				if (userIds!=null && userIds!="") {
					List<Map> rs = MybatisUtil.queryListWithPage("runtime.selectSql", sql.toUpperCase(), page);
					logger.info("**********************************分页查询********"+(new Date().getTime()-startTime)+"mS******************************************");
					List<Map> rs1 = new ArrayList<Map>();
					String [] aa = userIds.split(",");
					List<String> list =new ArrayList<String>();
					String oldStr = "";
					for (String currStr : aa) {
					    if (oldStr.contains(currStr + ",")) {//已有跳过
					        continue;
					    }
					    list.add(currStr);
					    oldStr += currStr + ",";
					    int count = 0;
					    for (String anArr : aa) {
					        if (anArr.equals(currStr)) {
					            count++;
					        }
					    }
					    logger.info(currStr + "|" + count);
					}
					aa = list.toArray(new String[list.size()]);

					for (int j = 0; j < aa.length; j++) {
						getList(aa[j],rs,rs1);
					}
					result.setRows(rs1);
				}else {
					logger.info(sql);
					List<Map> rs = MybatisUtil.queryListWithPage("runtime.selectSql", sql, page);
					logger.info("**********************************分页查询********"+(new Date().getTime()-startTime)+"mS******************************************");
					List<SysFormColumnCache> distColumns = form.getDictColumns();
					List<SysFormColumnCache> dateColumns = form.getDateColumns();
					for (Map map : rs) {
						for (SysFormColumnCache sysFormColumnCache : distColumns) {
							SysFormComboboxCache comboboxCache = (SysFormComboboxCache)sysFormColumnCache.getFormColumGui();
							if(comboboxCache.getPojo()!=null){
								if (comboboxCache.getComboboxMultiselect()!=null&&comboboxCache.getComboboxMultiselect().equals("Y")) {
									String dictCode = comboboxCache.getComboboxGuiInitValue();//字典编码
									String columnCode = ChangeCode.getUniqueCode(sysFormColumnCache.getFormEntityTablename().toUpperCase(), sysFormColumnCache.getFormFiledTablename().toUpperCase());
									if (map.get(columnCode)!=null&&!map.get(columnCode).equals("")) {
										String[] value = (map.get(columnCode)+"").split(",");
										String name = "";
										Map map2 = sysformconfigService.getDictValueMap(dictCode);
										if (map2!=null) {
											for (int i = 0; i < value.length; i++) {
												if (i==0) {
													name = map2.get(value[i])+"";
												}else{
													name += ","+map2.get(value[i]);
												}
											}
											map.put(columnCode+"_DICTNAME",name);
										}
									}else{
										map.put(columnCode+"_DICTNAME","-");
									}
								} else {
									String dictCode = comboboxCache.getComboboxGuiInitValue();//字典编码
									String columnCode = ChangeCode.getUniqueCode(sysFormColumnCache.getFormEntityTablename().toUpperCase(), sysFormColumnCache.getFormFiledTablename().toUpperCase());
									String value = map.get(columnCode)+"";
									Map map2 = sysformconfigService.getDictValueMap(dictCode);
									if (map2!=null) {
										String name = map2.get(value)+"";
										if(name.equals("null")){
											map.put(columnCode+"_DICTNAME","");
										}else{
											map.put(columnCode+"_DICTNAME",name);
										}
									}
								}
							}
						}
						for (SysFormColumnCache sysFormColumnCache : dateColumns) {
							SysDatepickerCache dCache = (SysDatepickerCache)sysFormColumnCache.getFormColumGui();
							String columnCode = ChangeCode.getUniqueCode(sysFormColumnCache.getFormEntityTablename().toUpperCase(), sysFormColumnCache.getFormFiledTablename().toUpperCase());
							String dateValue = map.get(columnCode)+"";
							if(dateValue.equals("null")){
								map.put(columnCode+"_NEWDATE","");
							}else {
								String format = dCache.getDatepickerFormat();
								Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateValue);
								String now = new SimpleDateFormat(format).format(date);
								map.put(columnCode+"_NEWDATE",now);
							}
						}
					}
					logger.info("**********************************特殊字段翻译********"+(new Date().getTime()-startTime)+"mS******************************************");
					result.setRows(rs);
				}
				result.setTotal(page.total);
			}
		} catch (Exception e) { 
			throw e;
		}
		return result;
	}
	/**
	 * 初始化卡片表单数据
	 * @param requestParam
	 * @param pkId
	 * @param filter 查询字段参数
	 * @param heightGrade高级查询参数
	 * @return
	 * 实现思路
	 * 1 获取原始查询sql语句
	 * 2 根据权限在 原来sql基础上包一个字查询
	 * 3 外出所查询的字段根据用户配置及权限过滤确定
	 * 4 根据用户设置的过滤条件增加增查询的where条件，其条件值及查询下规则根据配置来定
	 * 5 执行sql 返回结果集  
	 * 6 大字段怎么处理？？？
	 */
	public FormConfigResultMap exportCardFormInitData(Map<String, String> requestParam,
			Map<String, String> filter, Map<String,String>[] heightGrade,
			String pkId, String formId, String currentUserId, Pager page,
			UserObject user, String isView,Map<String, String> paramsMap,HttpServletRequest request,String userIds,Map<String, String> containerParam,String queryType,String sourceType) throws Throwable {
		// TODO 自动生成方法存根
		 //获取form对象
		Map[] datas  = null;
		String userId = request.getSession().getAttribute("userid")+"";
		FormConfigResultMap result = new FormConfigResultMap();
		try {
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			String realSql = form.getFormDefSql();
			 
			String otherColumSql = form.getFormOtherColumnSql();
			String sql = this.getExcSql(form, otherColumSql, currentUserId, realSql, requestParam, filter, heightGrade, formId, form.getFilter(), user, isView,request,containerParam,sourceType);
			if (sql == null) {
				return result;
			}
			//判断是否需要分页
			SysFormGridCache grid = form.getGrid();
			boolean showPager  = false;
			
			//替换参数
			List<String[]> params = form.getParams();
			for(int i=0; i<params.size(); i++){
				String[] temp = params.get(i);
				String value = paramsMap.get(temp[0]+"_"+temp[1]);
				String pstr = "["+temp[0]+":"+temp[1]+"]";
				if(value==null){
					value="null";
				}
				if(sql.indexOf(pstr)>0){
					sql = sql.replace(pstr, value);
				}
			}
			logger.info("SQL:" + sql);
			
			if (form.getPojoform().getPowerSql()!=null&&!form.getPojoform().getPowerSql().equals("")) {
				String pSql = form.getPojoform().getPowerSql();
				if (form.getPojoform().getIsOrgPower()!=null&&form.getPojoform().getIsOrgPower().equals("Y")) {
			    	String orgIds = redisService.get("orgIds_userId_"+userId);
			    	pSql = pSql.replace("[re:orgIds]",orgIds);
				}
				while(pSql.indexOf("[s:userId]")>=0){
					pSql = pSql.replace("[s:userId]",userId);
				}
				sql = "select p.* FROM (" +sql+ ") p " + pSql;
			}
			if (userIds!=null && userIds!="") {
				//Boolean isZh = StringUtils.checkStringContainChinese(userIds);
				String [] aa = userIds.split(",");
				String bb ="";
				for (int i = 0; i < aa.length; i++) {
					if (aa[i]!=null && aa[i]!="") {
						bb+="'"+aa[i]+"',";
					} 
				}
				bb=bb.substring(0, bb.length()-1);
				String whereSql = "";
				if (sql.contains("A01_A0101")) {
					whereSql+=" z.A01_A0101 in ("+bb+") or";
				}
				if (sql.contains("A01_A0190")) {
					whereSql+=" z.A01_A0190 in ("+bb+") or";
				}
				if (sql.contains("A01_A019A")) {
					whereSql+=" z.A01_A019A in ("+bb+") or";
				}
				whereSql = whereSql.substring(0, whereSql.length()-2);
				sql = "select z.* FROM (" +sql+ ") z " + "where "+ whereSql;
			}
			if (form.getPojoform().getOrderSql()!=null) {
				sql = "select o.* FROM (" +sql+ ") o " + form.getPojoform().getOrderSql();
			}
			 
			List<Map> rs = MybatisUtil.queryList("runtime.selectSql", sql); // getDASTemplate().queryByNamedSql(DataObject.class, "com.dcf.form.runtime.cardform.excuteQuerySql", sql);
			if (userIds!=null && userIds!="") {
				List<Map> rs1 = new ArrayList<Map>();
				String [] aa = userIds.split(",");
				List<String> list =new ArrayList<String>();
				String oldStr = "";
				for (String currStr : aa) {
				    if (oldStr.contains(currStr + ",")) {//已有跳过
				        continue;
				    }
				    list.add(currStr);
				    oldStr += currStr + ",";
				    int count = 0;
				    for (String anArr : aa) {
				        if (anArr.equals(currStr)) {
				            count++;
				        }
				    }
				    logger.info(currStr + "|" + count);
				}
				aa = list.toArray(new String[list.size()]);

				for (int j = 0; j < aa.length; j++) {
					getList(aa[j],rs,rs1);
				}
				result.setRows(rs1);
			}else {
				result.setRows(rs);
			}
			result.setTotal(page.total);
		} catch (Exception e) { 
			throw e;
		}
		return result;
	}
	
	
	public List<Map> getList(String a,List<Map> rs,List<Map> rs1){
		for (int i = 0; i < rs.size(); i++) {
			Map obj = rs.get(i);
			String A0190 = obj.get("A01_A0190")+"";
			String A0101 = obj.get("A01_A0101")+"";
			String A019A = obj.get("A01_A019A")+"";
			if (a.equals(A0190) || a.equals(A0101) || a.equals(A019A)) {
				rs1.add(obj);
			}
		}
		return rs1;
	}
	
	/**
	 * 导出数据
	 *//*
	public DcfReponse exportGridData(Map<String, String> requestParam,
			Map<String, String> filter, Map<String, String>[] heightGrade,
			String pkId, String formId, String currentUserId, PageCond page,
			UserObject user, String fileName, long buttonId,Map<String, String> paramsMap) {
		DcfReponse reponse = new DcfReponse();
		String userid = user.getUserId();
		try {
			SysFormconfig form = FormCacheManager.getForm(formId);
			SysFormButton button = form.serchButtonButtonId(buttonId+"");
			SysFormButtonExport export = (SysFormButtonExport)button.getButtonExt();
			
			
			String realSql = form.getFormDefSql();
			String otherColumSql = form.getFormOtherColumnSql();
			String sql = this.getExcSql(form, otherColumSql, currentUserId,
					realSql, requestParam, filter, heightGrade, formId, form.getFilter(), user, "false");
//			替换参数
			 List<String[]> params = form.getParams();
			 for(int i=0; i<params.size(); i++){
				 String[] temp = params.get(i);
				 String value = paramsMap.get(temp[0]+"_"+temp[1]);
				 String pstr = "["+temp[0]+":"+temp[1]+"]";
				 if(value==null){
					 value="null";
				 }
				 if(sql.indexOf(pstr)>0){
					 sql = sql.replace(pstr, value);
				 }
			 }
			 logger.info("SQL:" + sql);
			 
			if (StringUtils.isNotNullAndBlank(sql)) {
				DataObject[] datas = getDASTemplate().queryByNamedSql(DataObject.class, "com.dcf.form.runtime.cardform.excuteQuerySql", sql);
				if (datas != null) {
					Map<String, String> excelConfig = com.myehr.common.util.DcfUtil.getExportExcelConfig();
					if (excelConfig != null && excelConfig.size() > 0) {
						String exportExcelMaxRow = excelConfig.get("exportExcelMaxRow");
						if (StringUtil.isNotNullAndBlank(exportExcelMaxRow)) {
							if (datas.length > Long.parseLong(exportExcelMaxRow)) {
								throw new Exception("导出数据记录数超过最大数，数据记录数{" + datas.length + "} 最大允许数{" + exportExcelMaxRow + "}");
							}
						}
					}
				}

				IFormCacheBeanUtil service = (IFormCacheBeanUtil) ApplicationContextFactory.getContext().getBean("IFormCacheBeanUtil");
				DataObject[] fields = service.queryFieldByUserIdAndFormId( formId + "", currentUserId);
				List<SysFormButtonExportColumn> exportColumns = export.getExportColumnconfigs();

				List<ExcelHead> headExcelList = new ArrayList<ExcelHead>();
				for (SysFormButtonExportColumn ecolumn : exportColumns) {
					SysFormColumn column = ecolumn.getColumn();
					String formAuthorityIsControl = form.getFormAuthorityIsControl();
					// 权限过滤
					boolean hsFieldRigt = SysCardFormBeansUtil.hasFiled(fields, userid, column.getFormColumnId() + "", "false",formAuthorityIsControl);
					if ("1".equals(column.getFormColumnType())) {
						// formFiledTablename
						if (hsFieldRigt) {
							
							String dictTypeId = "";
							String ectype = ecolumn.getExportColumnType();
							SysFormGeneralExecSql backsql = null;
							if(ectype !=null ){
								if("dict".equals(ectype)){
									dictTypeId = ecolumn.getExportColumnDictType();
								}else if("sql".equals(ectype)) {
									backsql = ecolumn.getBacksql();
								}
							}else {
								if("2".equals(column.getFormColumnGuiType())) {// 下拉框
									SysCombobox combobox = (SysCombobox)column.getFormColumGui();
									if("dict".equals(combobox.getComboboxGuiInitType())) { 
										dictTypeId = combobox.getComboboxGuiInitValue();
									}
								}
								if("7".equals(column.getFormColumnGuiType())) {// 弹出框
									SysLookup lookup = (SysLookup)column.getFormColumGui();
									if("sql".equals(lookup.getLookupDatabackType())) { 
										backsql = lookup.getBackSql();
									}
								}
							}
								
							ExcelHead excelHead = new ExcelHead(column
									.getFormFiledTablename(), column
									.getFormColumnLable(), column
									.getFormColumnWidth(), column
									.getFormColumnAlign(), dictTypeId, column
									.getFormColumnGuiType(), column.getColumnDataType(),backsql);
							
							headExcelList.add(excelHead);
						}

					} else { // 虚拟列
						String[] fileds = column.getFiledName();
						String dictTypeId = "";
						String ectype = ecolumn.getExportColumnType();
						SysFormGeneralExecSql backsql = null;
						if(ectype !=null ){
							if("dict".equals(ectype)){
								dictTypeId = ecolumn.getExportColumnDictType();
							}else if("sql".equals(ectype)) {
								backsql = ecolumn.getBacksql();
							}
						}else {
							if("2".equals(column.getFormColumnGuiType())) {// 下拉框
								SysCombobox combobox = (SysCombobox)column.getFormColumGui();
								if("dict".equals(combobox.getComboboxGuiInitType())) { 
									dictTypeId = combobox.getComboboxGuiInitValue();
								}
							}
							if("7".equals(column.getFormColumnGuiType())) {// 弹出框
								SysLookup lookup = (SysLookup)column.getFormColumGui();
								if("sql".equals(lookup.getLookupDatabackType())) { 
									backsql = lookup.getBackSql();
								}
							}
						}
						ExcelHead excelHead = new ExcelHead(fileds[1], column
								.getFormColumnLable(), column
								.getFormColumnWidth(), column
								.getFormColumnAlign(), dictTypeId, column
								.getFormColumnGuiType(), "",backsql);
						
						headExcelList.add(excelHead);
					}
				}

				if (headExcelList != null && headExcelList.size() > 0) {
					ExcelHead[] headers = headExcelList.toArray(new ExcelHead[headExcelList.size()]);
					
					Map<String, String> exportMap = ExcelUtil.exportExcel(headers, datas, fileName,getDASTemplate());
					
					reponse.setSuccessResponse(exportMap);// 成功
				} else {
					throw new Exception("未获取表头数据信息，请联系系统管理员！");
				}
			} 
		} catch (Throwable t) {
			reponse.setFailureMsg(StrUtil.parseString(t.getMessage()), t);
			log.error(t.getMessage(), t);
		}
		
		return reponse;
	}*/
	
	/**
	 * 根据当前用户权限获取查询字段sql段
	 * @param columns
	 * @param otherColumSql
	 * @param currentUserId
	 * @param heightGrade 
	 * @param filter 
	 * @param user 当前session用户对象
	 * @return
	 * @throws Exception 
	 */
	public String getExcSql(SysFormconfigCache form, String otherColumSql,
			String currentUserId, String realSql, Map requestParam,
			Map<String, String> filterParam, Map<String,String>[] heightGrade,
			String formId, SysGridFilterCache filter, UserObject user, String isView,HttpServletRequest request,Map<String, String> containerParam,String sourceType)
			throws Exception {
		long startTime = new Date().getTime();
		String productName =sourceType.toUpperCase();
		IsysFormColumnService service = (IsysFormColumnService)SpringContextUtils.getBeanById("SysFormColumnServiceImpl");
		//获取当前表单和当前用户下所有有权限字段集合。
//		List<SysFormColumn> fields = service.queryColumnsByFormIdAndUserId(formId+"", currentUserId);
		List<SysFormColumn> fields = sysformconfigService.getColumnPowerById(formId+"", currentUserId);
		String userid = user.getUserId();
		String columnSql = "";
		String whereSql = "";
		List<SysFormColumnCache> columns = form.getColumns();
		List<SysFormWhereCache> wheres = form.getWheres();

		logger.info("**********************************sql0变********"+(new Date().getTime()-startTime)+"mS******************************************");
		for(SysFormColumnCache column:columns){
			Boolean isInRole = false;
			for (int i = 0; i < fields.size(); i++) {
				if (column.getFormFiledTablename().equals(fields.get(i).getFormFieldTablename())) {
					isInRole= true;
				}
			}
			
			if (currentUserId.equals("1")) {
				
			}else {
				if (isInRole==false) {
					//continue;
				}
			}
			
			// 权限过滤
			String formAuthorityIsControl = form.getFormAuthorityIsControl();
			// 不需要权限boolean hsFieldRigt = SysCardFormBeansUtil.hasFiled(fields , userid, column.getFormColumnId() + "", isView,formAuthorityIsControl);
			if("1".equals(column.getFormColumnType())){
				
				if("Oracle".equalsIgnoreCase(productName)){
					if(column.getEntityColumn()!=null){
						logger.info(column.getEntityColumn()+"");
					}
					columnSql += RuntimeUtil.getSelectFieldFormat(column.getFormFiledTablename(), column.getEntityColumn())	+ ",";
					logger.info("**************************:"+columnSql);
				}else if("SqlServer".equalsIgnoreCase(productName)){
					columnSql += RuntimeUtilSqlServer.getSelectFieldFormat(column.getFormFiledTablename(), column.getEntityColumn())+ ",";
				}

			} else { // 虚拟列
				String[] fileds = column.getFiledName();
				columnSql += "t." + fileds[1].toUpperCase() + ",";
			}
		}
		if("".equals(columnSql)){
			return null;
		}
		columnSql = columnSql.substring(0,columnSql.length()-1);
		logger.info("**********************************sql1变********"+(new Date().getTime()-startTime)+"mS******************************************");
		if(productName.equals("ORACLE")){
			whereSql = RuntimeUtil.getWhereSql( form,wheres, requestParam,filterParam,filter,heightGrade,null,request,containerParam,sourceType);
		}else if(productName.equals("SQLSERVER")){
			whereSql = RuntimeUtil.getWhereSql(form,wheres, requestParam,filterParam,filter,heightGrade,null,request,containerParam,sourceType);
		}
		String excSql = "select "+columnSql+" from ("+realSql+") t where 1=1 "+whereSql;
		
		//where 条件
		
		return excSql;
	}
	
	public String returnWhereSqlForColumn(SysFormWhereCache where) {
		//SysFormColumn  column = where.getColumn();
		//根据数据库类型凭借where条件语句
		return null;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkUniqueVerify(String formId, String field, String value,String pkValue,HttpSession session) throws DcfException, Exception {
		String userId = session.getAttribute("userid")+"";
		SysFormconfigCache form  = sysformconfigService.getForm(formId);
		String tablename= form.getFormDefSaveTable();
		List<SysFormColumnCache> list = form.getColumns();
		String columnType = null;
		for(SysFormColumnCache temp:list ){
			if(field.equals(temp.getFormFiledTablename())){
				Map o =  temp.getEntityColumn();
				columnType = (String)o.get("fieldType");
			}
		}
		if(columnType==null){
			return false;
		}
		String wheresql;
		String tempPkWhere = "";
		try {
			wheresql = RuntimeUtil.getFieldValue(columnType,value,"=",false,session,null,"sqlserver");
			if(pkValue!=null&&!"".equals(pkValue)){
				tempPkWhere = " and "+ form.getPkColumn().getJsId("name",formId)+" != "+pkValue;
			}
			String sql= "select * from "+tablename +" where " + field + "=" +wheresql +tempPkWhere ;
			List<Map> dataObject = MybatisUtil.queryList("runtime.selectSql", new HashMap().put("selectSql", sql));
			if(dataObject.size() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();logger.error(e.getMessage(),e);
			return false;
		}
		return false;
	}
	
	//通过自定sql查询combobox下拉数据等控件；
	public Map[] queryGuiInitDatas(String  paramsJson ,String execType,String _value) throws DcfException{
		Map<String,Object> params = RuntimeUtil.jsonToMap(paramsJson);
		String formId = (String)params.get("formId");
		
		long columnId = Long.parseLong((String)params.get("columnId"));
		if(FormParamConstants.EXECSQL_COMBOBOX.equals(execType)){
			/*后期实现
			 * 
			 * SysFormComboboxCache  combobox = null;
			if(columnId>=5000000){ //代表是从参数控件里来得combobox
				combobox = (SysFormComboboxCache)SqlParamsCacheManager.getSysSqlParams(columnId).getGui();
			}else {
				combobox = (SysCombobox)FormCacheManager.getForm(formId).serchColumnByColumnId(columnId).getFormColumGui();
			}
			if(combobox!=null&&combobox.getComboboxId()!=0){
				SysFormGeneralExecSql execsql = combobox.getInitSql();
				if(_value!=null){
					
					params.put("_combobox_value", _value);
					params.put("_combobox_combobox", combobox);
				}
				
				return RuntimeUtil.execsql(execsql, getDASTemplate(), params);
			}*/
		}
		return null;
	}

	@Override
	public FormConfigResultMap cardFormInitData_Question(Map requestParam,
			Map filter, Map[] heightGrade, String pkId, String formId,
			String currentUserId, Pager page, UserObject user, String isView,
			Map paramsMap, HttpServletRequest request, String userIds,
			Map containerParam, String queryType,String templateId) throws Throwable {
		Map[] datas  = null;
		String userId = request.getSession().getAttribute("userid")+"";
		FormConfigResultMap result = new FormConfigResultMap();
		try {
			SysFormconfigCache form = sysformconfigService.getForm(formId);
			String realSql = form.getFormDefSql() + " WHERE SYS_QUESTION.ID NOT IN (SELECT SYS_EXAMTEMPLATE_QUESTION.QUESTIONID AS ID from SYS_EXAMTEMPLATE_QUESTION WHERE SYS_EXAMTEMPLATE_QUESTION.TEMPLATEID = "+templateId+")";
			 
			String otherColumSql = form.getFormOtherColumnSql();
			String sql = this.getExcSql(form, otherColumSql, currentUserId, realSql, requestParam, filter, heightGrade, formId, form.getFilter(), user, isView,request,containerParam,"sqlserver");
			List<Map> rs = MybatisUtil.queryListWithPage("runtime.selectSql", sql, page);
			result.setRows(rs);
			result.setTotal(page.total);
		} catch (Exception e) { 
			throw e;
		}
		return result;
	}
}
