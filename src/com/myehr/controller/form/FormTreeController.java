package com.myehr.controller.form;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.mybatis.Pager;
import com.myehr.common.util.ChangeCode;
import com.myehr.common.util.SerializeUtil;
import com.myehr.controller.dict.param.ResultMapNew;
import com.myehr.controller.form.parambean.CardformInitDataParams;
import com.myehr.controller.form.parambean.SaveButtonParams;
import com.myehr.controller.form.parambean.SaveFilterColumnDataParams;
import com.myehr.mapper.entity.SysEntityMapper;
import com.myehr.mapper.field.SysFieldMapper;
import com.myehr.mapper.formmanage.form.SysFormconfigTreeMapper;
import com.myehr.mapper.formmanage.form.SysTreeFolderTreeMapper;
import com.myehr.mapper.formmanage.form.SysTreeNodeTypeMapper;
import com.myehr.mapper.formmanage.form.SysTreeSolutionColumnMapper;
import com.myehr.mapper.formmanage.form.SysTreeSolutionMapper;
import com.myehr.pojo.entity.SysEntity;
import com.myehr.pojo.entity.SysEntityExample;
import com.myehr.pojo.field.SysField;
import com.myehr.pojo.field.SysFieldExample;
import com.myehr.pojo.formmanage.form.SysFormColumn;
import com.myehr.pojo.formmanage.form.SysFormconfigTree;
import com.myehr.pojo.formmanage.form.SysFormconfigTreeExample;
import com.myehr.pojo.formmanage.form.SysTreeFolderTree;
import com.myehr.pojo.formmanage.form.SysTreeFolderTreeExample;
import com.myehr.pojo.formmanage.form.SysTreeNodeType;
import com.myehr.pojo.formmanage.form.SysTreeNodeTypeExample;
import com.myehr.pojo.formmanage.form.SysTreeSolution;
import com.myehr.pojo.formmanage.form.SysTreeSolutionColumn;
import com.myehr.pojo.formmanage.form.SysTreeSolutionColumnExample;
import com.myehr.pojo.formmanage.form.SysTreeSolutionExample;
import com.myehr.service.RedisService;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.test.dao.TMapperExt;

@Controller
@RequestMapping("/tree")
public class FormTreeController {
	private static Logger logger = LoggerFactory.getLogger(FormTreeController.class);
	@Autowired
	private SysTreeFolderTreeMapper sysTreeFolderTreeMapper;
	
	@Autowired
	private SysTreeSolutionMapper sysTreeSolutionMapper;
	
	@Autowired
	private SysTreeSolutionColumnMapper sysTreeSolutionColumnMapper;
	
	@Autowired
	private SysFormconfigTreeMapper sysFormconfigTreeMapper;
	
	@Autowired
	private SysTreeNodeTypeMapper sysTreeNodeTypeMapper;
	
	@Autowired
	private SysEntityMapper sEntityMapper;
	
	@Autowired
	private SysFieldMapper sFieldMapper;
//	@Autowired
	@Resource(name = "TMapperExt")
	private TMapperExt tMapperExt;
	
	@Autowired
	@Resource(name = "RedisService")
	private RedisService  redisService; 

	@Autowired ISysformconfigService sysformconfigService;
	
	/*@Autowired
	private PrimaryKeyServiceImpl keyserviceImpl;*/
	
	@RequestMapping("/buildTreeSchemeFolder")
	public @ResponseBody List<Map> buildTreeSchemeFolder(HttpServletRequest request) throws Exception{
		List<Map> results = new ArrayList<Map>();
		
		SysTreeFolderTreeExample example = new SysTreeFolderTreeExample();
		com.myehr.pojo.formmanage.form.SysTreeFolderTreeExample.Criteria criteria = example.createCriteria();
		List<SysTreeFolderTree> sysTreeFolderTrees = sysTreeFolderTreeMapper.selectByExample(example);
		
		Map mapRoot=new HashMap();
		mapRoot.put("expanded", false);
		mapRoot.put("isLeaf", false);
		mapRoot.put("isParent", false); 
		mapRoot.put("open", false); 
		mapRoot.put("nodeId", "0");
		mapRoot.put("nodeName", "树方案目录");
		mapRoot.put("nodeCode", "root");
		mapRoot.put("nodeDesc", "树方案目录");
		mapRoot.put("nodeType", "Default");
		mapRoot.put("parentCode", "");
		mapRoot.put("ORG_DEL", "N");
		results.add(mapRoot);
		
		for (int i = 0; i < sysTreeFolderTrees.size(); i++) {
			String parentCode = "";
			if (sysTreeFolderTrees.get(i).getFolderTreeParentId()==null) {
				parentCode = "0";
			}else {
				parentCode = sysTreeFolderTrees.get(i).getFolderTreeParentId()+"";
			}
			Map map=new HashMap();
			map.put("expanded", false);
			map.put("isLeaf", false);
			map.put("isParent", false); 
			map.put("open", false);
			map.put("nodeId", sysTreeFolderTrees.get(i).getFolderTreeId());
			map.put("nodeName", sysTreeFolderTrees.get(i).getFolderTreeName());
			map.put("nodeCode", sysTreeFolderTrees.get(i).getFolderTreeCode());
			map.put("nodeDesc", sysTreeFolderTrees.get(i).getFolderTreeDesc());
			map.put("nodeType", "N");
			map.put("parentCode", parentCode);
			results.add(map); 
		}
		
		return results;
	}
	
	@RequestMapping("/addTreeSchemeFolder")
	public @ResponseBody String addTreeSchemeFolder(HttpServletRequest request,@RequestBody SysTreeFolderTree folderTree) throws Exception{
		String reCode = "0";
		SysTreeFolderTreeExample example = new SysTreeFolderTreeExample();
		String userId = (String)(request.getSession().getAttribute("userid")+"");
		example.or().andFolderTreeCodeEqualTo(folderTree.getFolderTreeCode());
		if (folderTree.getFolderTreeId()!=null) {
			List<SysTreeFolderTree> fList = sysTreeFolderTreeMapper.selectByExample(example);
			if (fList.size()>0&&folderTree.getFolderTreeId().toString().equals(fList.get(0).getFolderTreeId().toString())) {
				fList.get(0).setFolderTreeCode(folderTree.getFolderTreeCode());
				fList.get(0).setFolderTreeName(folderTree.getFolderTreeName());
				fList.get(0).setFolderTreeDesc(folderTree.getFolderTreeDesc());
				fList.get(0).setOperatorName(userId);
				fList.get(0).setOperatorTime(new Date());
				sysTreeFolderTreeMapper.updateByPrimaryKey(fList.get(0));
				return reCode;
			} else if (fList.size()==0) {
				SysTreeFolderTree folderTree2 = sysTreeFolderTreeMapper.selectByPrimaryKey(folderTree.getFolderTreeId());
				folderTree2.setFolderTreeCode(folderTree.getFolderTreeCode());
				folderTree2.setFolderTreeName(folderTree.getFolderTreeName());
				folderTree2.setFolderTreeDesc(folderTree.getFolderTreeDesc());
				folderTree2.setOperatorName(userId);
				folderTree2.setOperatorTime(new Date());
				sysTreeFolderTreeMapper.updateByPrimaryKey(folderTree2);
				return reCode;
			} else {
				return "1";//树节点编码重复
			}
		} else {
			if(sysTreeFolderTreeMapper.selectByExample(example).size()>0){
				return "1";//树节点编码重复
			}else {
				folderTree.setOperatorName(userId);
				folderTree.setOperatorTime(new Date());
				if (folderTree.getFolderTreeParentId().intValue()==0) {
					folderTree.setFolderTreeParentId(null);
				}
				sysTreeFolderTreeMapper.insert(folderTree);
				SysTreeFolderTree folderTree2 = sysTreeFolderTreeMapper.selectByExample(example).get(0);
				folderTree2.setFolderTreeSeq(folderTree2.getFolderTreeId()+".");
				sysTreeFolderTreeMapper.updateByPrimaryKey(folderTree2);
				return reCode;
			}
		}
	}
	
	//删除树节点
	@RequestMapping("/deleteTreeSchemeFolder")
	public @ResponseBody String deleteTreeSchemeFolder(HttpServletRequest request) throws Exception{
		String reCode = "0";
		String folderTreeId = request.getParameter("folderTreeId");
		sysTreeFolderTreeMapper.deleteByPrimaryKey(new BigDecimal(folderTreeId));
		return reCode;
	}
	
	/**
	 * 查询菜单列表
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryTreeSchemeList")
	public @ResponseBody ResultMapNew queryTreeSchemeList(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Exception{
		Map requestParam = params.getRequestParam();
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
		
		String deleteMark = (String) filter.get("deleteMark");
		String treeSolutionFolderId = (String) requestParam.get("nodeId");		
		
		String wheres="";
		if (treeSolutionFolderId!=null && treeSolutionFolderId!="") {
			wheres += " and SYS_TREE_SOLUTION.TREE_SOLUTION_FOLDER_ID = '"+treeSolutionFolderId+"'";
		}
		if (deleteMark!=null && deleteMark!="") {
			wheres += " and SYS_TREE_SOLUTION.DELETE_MARK = '"+deleteMark+"'";
		}
		String searchValue = (String) filter.get("searchValue");
		if (searchValue!=null && searchValue!="") {
			wheres += " and ( SYS_TREE_SOLUTION.TREE_SOLUTION_CODE like "+" '%"+searchValue+"%' "+
					  "or SYS_TREE_SOLUTION.TREE_SOLUTION_NAME like "+" '%"+searchValue+"%' )";
		}
		
		String sql = "select SYS_TREE_SOLUTION.TREE_SOLUTION_ID as treeSolutionId, "+
					 " SYS_TREE_SOLUTION.TREE_SOLUTION_CODE as treeSolutionCode, "+
					 " SYS_TREE_SOLUTION.TREE_SOLUTION_NAME as treeSolutionName, "+
					 " SYS_TREE_SOLUTION.OPERATOR_NAME as operatorName, "+
					 " SYS_TREE_SOLUTION.OPERATOR_TIME as operatorTime, "+
					 " SYS_TREE_SOLUTION.DELETE_MARK as deleteMark, "+
					 " SYS_TREE_SOLUTION.TREE_SOLUTION_FOLDER_ID as treeSolutionFolderId, "+
					 " sys_user.user_name as userName "+
                     " from SYS_TREE_SOLUTION left join sys_user on SYS_TREE_SOLUTION.operator_name = sys_user.user_id "+
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
	 * 查询树方案字段列表
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryTreeSolutionColumnBySolutionId")
	public @ResponseBody ResultMapNew queryTreeSolutionColumnBySolutionId(HttpServletRequest request) throws Exception{
		
		String treeSolutionId = request.getParameter("treeSolutionId");
		
		String sql = " select c1.TREE_SLU_ID as treeSluId, "+
				" c1.TREE_SOLUTION_ID as treeSolutionId, "+
				" c1.TREE_ENTITY_ID as treeEntityId, "+
				" c1.TREE_COLUMN_ID as treeColumnId, "+
				" c1.TREE_ENTITY_TABLENAME as treeEntityTablename, "+
				" c1.TREE_FIELD_TABLENAME as treeFieldTablename, "+
				" c1.TREE_COLUMN_TYPE as treeColumnType, "+
				" t2.entity_chinaname as entityChinaname, "+
				" t3.field_china_name as fieldChinaName "+
				"   from sys_tree_solution_column c1 "+
				"   left join sys_entity t2 on c1.tree_entity_id = t2.entity_id "+
				"   left join sys_field t3 on t3.field_id = c1.tree_column_id "+
					 " where c1.TREE_SOLUTION_ID = "+treeSolutionId;
		List<Map> schemes = tMapperExt.queryByFormDefSql(sql);
		
		ResultMapNew resultMap=new ResultMapNew();

		resultMap.setTotal(schemes.size());
		resultMap.setRows(schemes);
		return resultMap;
	}
	
	

	@RequestMapping("/queryTreeSolutionById")
	public @ResponseBody SysTreeSolution queryTreeSolutionById(HttpServletRequest request) throws Exception{
		String treeSolutionId = request.getParameter("treeSolutionId");
		
		SysTreeSolutionExample example = new SysTreeSolutionExample();
		com.myehr.pojo.formmanage.form.SysTreeSolutionExample.Criteria criteria = example.createCriteria();
		criteria.andTreeSolutionIdEqualTo(new BigDecimal(treeSolutionId));
		List<SysTreeSolution> sysTreeSolutions =sysTreeSolutionMapper.selectByExample(example);
		if (sysTreeSolutions.size()>0) {
			return sysTreeSolutions.get(0);
		}
		return null;
	}
	
	
	@RequestMapping("/saveTreeSolution")
	public @ResponseBody String saveTreeSolution(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		String menuJson = "";
		
		Map param = params.getParam();
		Map paramsMap = params.getParamsMap();
		int reCode = 0;
		
		String treeSolutionId = (String) param.get("treeSolutionId");
		SysTreeSolution treeSolution = new SysTreeSolution();
		if (treeSolutionId==null || treeSolutionId=="") {
			treeSolution.setTreeSolutionFolderId(new BigDecimal((String) param.get("treeSolutionFolderId")));
			treeSolution.setTreeSolutionCode((String) param.get("treeSolutionCode"));
			treeSolution.setTreeSolutionName((String) param.get("treeSolutionName"));
			treeSolution.setTreeSolutionEntitySql((String) param.get("treeSolutionEntitySql"));
			treeSolution.setTreeSolutionExcSql(getRealSql((String) param.get("treeSolutionEntitySql")));
			treeSolution.setDeleteMark("N");
			treeSolution.setOperatorName((String) paramsMap.get("userId"));
			treeSolution.setOperatorTime(new Date());
			reCode = sysTreeSolutionMapper.insert(treeSolution);
		}else {
			treeSolution.setTreeSolutionId(new BigDecimal(treeSolutionId));
			treeSolution.setTreeSolutionFolderId(new BigDecimal((String) param.get("treeSolutionFolderId")));
			treeSolution.setTreeSolutionCode((String) param.get("treeSolutionCode"));
			treeSolution.setTreeSolutionName((String) param.get("treeSolutionName"));
			treeSolution.setTreeSolutionEntitySql((String) param.get("treeSolutionEntitySql"));
			treeSolution.setTreeSolutionExcSql(getRealSql((String) param.get("treeSolutionEntitySql")));
			treeSolution.setDeleteMark("N");
			treeSolution.setOperatorName((String) paramsMap.get("userId"));
			treeSolution.setOperatorTime(new Date());
			reCode = sysTreeSolutionMapper.updateByPrimaryKey(treeSolution);
			sysformconfigService.setTreeSolutionById(new BigDecimal(treeSolutionId));
		}
		
		return menuJson;
	}
	
	@RequestMapping("/deleteTreeSolution")
	public @ResponseBody String deleteTreeSolution(HttpServletRequest request) throws Exception{
		String reCode = "";
		
		String treeSolutionIds = request.getParameter("treeSolutionIds");
		String[] treeSolutionId = treeSolutionIds.split(",");
		for (int i = 0; i < treeSolutionId.length; i++) {
			reCode = sysTreeSolutionMapper.deleteByPrimaryKey(new BigDecimal(treeSolutionId[i]))+"";
		}	
		return reCode;
	}
	
	@RequestMapping("/cacheTreeSolution")
	public @ResponseBody String cacheTreeSolution(HttpServletRequest request) throws Exception{
		String reCode = "";
		String treeSolutionId = request.getParameter("treeSolutionId");	
		
		SysTreeSolution treeSolution = sysTreeSolutionMapper.selectByPrimaryKey(new BigDecimal(treeSolutionId));
		String sql = treeSolution.getTreeSolutionExcSql();
		List<Map> datas = null;
		try {
			datas = MybatisUtil.queryList("runtime.selectSql", sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
		}
		
	
		if(datas!=null&&datas.size()>0){
			redisService.set(("SysTreeSolution_"+treeSolutionId).getBytes(), SerializeUtil.serializeList(datas));
		}
			
		return reCode;
	}
	
	@RequestMapping("/getRealSql")
	public @ResponseBody String getRealSql(HttpServletRequest request) throws Exception{
		String treeSolutionEntitySql = request.getParameter("treeSolutionEntitySql");
		return getRealSql(treeSolutionEntitySql);
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
               		if(!temp.equals("[lv]")&&!temp.equals("[id]")){
	               		String tableChinesName = temp.substring(ret.indexOf("[")+1,ret.indexOf("]"));
	               		SysEntityExample example = new SysEntityExample();
	               		example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
	               		List<SysEntity> entitys = sEntityMapper.selectByExample(example);
	               		SysEntity entity2 = entitys.get(0);
	               		if(entity2==null||entity2.getEntityTablename()==null){
	            			return "通过实体中文名:\""+tableChinesName+"\"无法获取对应数据库表名!";
	               		}
	               		String entityName = entity2.getEntityTablename();
	               		result = result.replace(""+temp+"",entityName);
               		}
               		continue;
               }
        }
        //获取非实体列字段
        //获取第一个select 到 from 之间的字符串
		return result;

	}
	
	
	@RequestMapping("/saveTreeSolutionColumn")
	public @ResponseBody Object saveTreeSolutionColumn(HttpServletRequest request) throws Exception{
		String treeSolutionId =request.getParameter("treeSolutionId");
		String entitySql =request.getParameter("treeSolutionEntitySql");
		//删除表单对应字段表中字段
		SysTreeSolutionColumnExample examplex = new SysTreeSolutionColumnExample();
		if (!treeSolutionId.equals("")) {
			examplex.or().andTreeSolutionIdEqualTo(new BigDecimal(treeSolutionId));
			sysTreeSolutionColumnMapper.deleteByExample(examplex);
		}
		
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
			SysEntityExample example = new SysEntityExample();
			if(saveChinaEntityname!=null){
				//获取表对应实体ID
				 Map params2 = new HashMap();
				 params2.put("tableChinaName", saveChinaEntityname);
				 
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
						 example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
		               	 SysEntity entity = sEntityMapper.selectByExample(example).get(0);
		               	 example2.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andDeleteMarkEqualTo("N");
		               	 List<SysField> objs = sFieldMapper.selectByExample(example2);
						 for(int k=0; k<objs.size(); k++){
							 SysTreeSolutionColumn obj = new SysTreeSolutionColumn();
							 obj.setTreeEntityTablename(entity.getEntityTablename());
							 obj.setTreeEntityId(objs.get(k).getFieldEntityId());
							 obj.setTreeColumnId(objs.get(k).getFieldId());
							 obj.setTreeFieldTablename(objs.get(k).getFieldCode());
							 obj.setTreeSolutionId(new BigDecimal(treeSolutionId));
							 obj.setOperatorName("1");
							 obj.setOperatorTime(new Date());
							 sysTreeSolutionColumnMapper.insert(obj);
						 }
						 
					 }else {
						 SysTreeSolutionColumn obj = new SysTreeSolutionColumn();
						 //com.dcf.form.entitySql.queryEntityTableByChineseName
						 example.or().andEntityChinanameEqualTo(tableChinesName).andDeleteMarkEqualTo("N");
						 SysEntity entity = sEntityMapper.selectByExample(example).get(0);
						 if(entity!=null){
							 obj.setTreeEntityTablename(entity.getEntityTablename());
							 obj.setTreeEntityId(new BigDecimal(entity.getEntityId()));
							 
						 }
						 SysFieldExample example3 = new SysFieldExample();
						 example3.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldChinaNameEqualTo(columChinaName);
						 List<SysField> objs2 = sFieldMapper.selectByExample(example3);
						 if(objs2!=null&&objs2.size()>0){
							 obj.setTreeColumnId(objs2.get(0).getFieldId());
							 obj.setTreeFieldTablename(objs2.get(0).getFieldCode()); 
						 }
						 obj.setTreeSolutionId(new BigDecimal(treeSolutionId));
						 obj.setOperatorName("1");
						 obj.setOperatorTime(new Date());
						 sysTreeSolutionColumnMapper.insert(obj);
					 }
					 logger.info("tablename:"+tableChinesName);
				 }else { //非实体字段
					 otherColumn += temp+",";
					 String[] array=temp.split("\\.");
					 if(array.length>1){
						 if(array[0].toUpperCase().equals("VIR")){
						 String formFieldTablename = array[1].toUpperCase();
						 logger.info(formFieldTablename);
						 SysFormColumn obj = new SysFormColumn();
						 obj.setFormEntityTablename("");
						 obj.setFormColumnEntityId(new BigDecimal("0"));
						 //fieldChinaname
						 obj.setFormColumnLable("");
						 obj.setFormColumnLable("虚拟字段");
						 obj.setFormColumnColumnId(new BigDecimal("13557"));
						 obj.setFormFieldTablename(formFieldTablename);
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
						 //sColumnMapper.insert(obj);
					 }else {
						 String tableCode = array[0];
						 String columCode = array[1];
						 SysTreeSolutionColumn obj = new SysTreeSolutionColumn();
						 //com.dcf.form.entitySql.queryEntityTableByChineseName
						 SysEntityExample example1 = new SysEntityExample();
						 example1.or().andEntityCodeEqualTo(tableCode).andDeleteMarkEqualTo("N");
						 List<SysEntity> entitys = sEntityMapper.selectByExample(example1);
						 if(entitys.size()>0){
							 SysEntity entity = entitys.get(0);
//							 obj.put("entityChinaname", tableChinesName);
							 obj.setTreeEntityTablename(entity.getEntityTablename());
							 obj.setTreeEntityId(new BigDecimal(entity.getEntityId()));
							 //queryColumnByChineseName
							 //DataObject[] objs2  = queryColumnByColumnChinaname(columChinaName,tableChinesName);
							 SysFieldExample example3 = new SysFieldExample();
							 example3.or().andFieldEntityIdEqualTo(new BigDecimal(entity.getEntityId())).andFieldCodeEqualTo(columCode);
							 List<SysField> objs2 = sFieldMapper.selectByExample(example3);
							 if(objs2!=null&&objs2.size()>0){
								 obj.setTreeColumnId(objs2.get(0).getFieldId());
								 obj.setTreeFieldTablename(objs2.get(0).getFieldCode()); 
							 }
							 obj.setTreeSolutionId(new BigDecimal(treeSolutionId));
							 obj.setOperatorName("1");
							 obj.setOperatorTime(new Date());
							 sysTreeSolutionColumnMapper.insert(obj);
							 indexSort++;
						 }						
					 	}
					 }
				 }
				
			}
			res[0] = entitySql;
			res[1] = realSql;
			
			return res;
	}
	
	/**
	 * 保存过滤配置
	 * @param request
	 * @param params
	 * @throws Throwable 
	 */
	@RequestMapping("/saveYsColumnData")
	public @ResponseBody String[]  saveYsColumnData(HttpServletRequest request,@RequestBody SaveFilterColumnDataParams param) throws Throwable {
		String[] recode = new String[2];
		recode[0]="0";
		recode[1]="操作成功";
		
		List<Map> filterColumns = param.getColumns();
		
		for (int i = 0; i < filterColumns.size(); i++) {
			SysTreeSolutionColumn filterColumn = new SysTreeSolutionColumn();
			Map obj = filterColumns.get(i);
			filterColumn.setTreeEntityTablename((String)obj.get("treeEntityTablename"));
			filterColumn.setTreeEntityId(new BigDecimal((String)obj.get("treeEntityId")));
			filterColumn.setTreeColumnId(new BigDecimal((String)obj.get("treeColumnId")));
			filterColumn.setTreeFieldTablename((String)obj.get("treeFieldTablename"));
			filterColumn.setTreeSolutionId(new BigDecimal((String)obj.get("treeSolutionId")));
			filterColumn.setTreeColumnType((String)obj.get("treeColumnType"));
			filterColumn.setOperatorName("1");
			filterColumn.setOperatorTime(new Date());
			
			String treeSluId = (String)obj.get("treeSluId");
			if (treeSluId!=null && !(treeSluId+"").equals("")) {
				filterColumn.setTreeSluId(new BigDecimal(treeSluId));
				sysTreeSolutionColumnMapper.updateByPrimaryKey(filterColumn);
			}else {
				sysTreeSolutionColumnMapper.insert(filterColumn);
			}
		}
		
		return recode;
	}
	
	
	@RequestMapping("/getTreeFormStepTwo")
	public @ResponseBody Map getTreeFormStepTwo(HttpServletRequest request) throws Exception{
		String formId = request.getParameter("formId");
		
		String sql = " select t1.FORM_TREE_ID as formTreeId, "+
					" t1.FORM_TREE_FORM_ID as formTreeFormId, "+
					" t1.FORM_TREE_TYPE as formTreeType, "+
					" t1.FORM_TREE_VALUE as formTreeValue, "+
					" t1.FORM_TREE_SOLUTION_ID as formTreeSolutionId, "+
					" t1.FORM_TREE_INIT_FUN as formTreeInitFun, "+
					" t1.FORM_TREE_LOAD_FUN as formTreeLoadFun, "+
					" t1.FORM_TREE_NODE_CLICK_FUN as formTreeNodeClickFun, "+
					" t1.FORM_TREE_CUSTOM_FUN as formTreeCustomFun, "+
					" t2.tree_solution_name as formTreeSolutionName "+
					" from SYS_FORMCONFIG_TREE t1 "+
					" left join sys_tree_solution t2 on t1.form_tree_solution_id = t2.tree_solution_id "+
					 " where t1.FORM_TREE_FORM_ID = "+formId;
		List<Map> schemes = tMapperExt.queryByFormDefSql(sql);
		
		return schemes.get(0);
	}
	
	
	/**
	 * 保存树表单配置
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveTreeFormStepTwo")
	public @ResponseBody String saveTreeFormStepTwo(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		String menuJson = "";
		
		Map param = params.getParam();
		Map paramsMap = params.getParamsMap();
		int reCode = 0;
		
		String formId = (String) param.get("formTreeFormId");
		SysFormconfigTreeExample example = new SysFormconfigTreeExample();
		com.myehr.pojo.formmanage.form.SysFormconfigTreeExample.Criteria criteria = example.createCriteria();
		criteria.andFormTreeFormIdEqualTo(new BigDecimal(formId));
		SysFormconfigTree tree = sysFormconfigTreeMapper.selectByExample(example).get(0);
			
		tree.setFormTreeType((String) param.get("formTreeType"));
		tree.setFormTreeValue((String) param.get("formTreeValue"));
		tree.setFormTreeSolutionId(new BigDecimal((String) param.get("formTreeSolutionId")));
		tree.setFormTreeInitFun((String) param.get("formTreeInitFun"));
		tree.setFormTreeLoadFun((String) param.get("formTreeLoadFun"));
		tree.setFormTreeNodeClickFun((String) param.get("formTreeNodeClickFun"));
		tree.setFormTreeCustomFun((String) param.get("formTreeCustomFun"));
		reCode = sysFormconfigTreeMapper.updateByPrimaryKey(tree);
		
		return menuJson;
	}
	
	
	/**
	 * 查询菜单列表
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryTreeNodeTypeList")
	public @ResponseBody ResultMapNew queryTreeNodeTypeList(HttpServletRequest request, @RequestBody CardformInitDataParams params) throws Exception{
		Map requestParam = params.getRequestParam();
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
		
		String formId = (String) requestParam.get("formId");		
		
		String wheres="";
		if (formId!=null && formId!="") {
			wheres += " and SYS_TREE_NODE_TYPE.FORM_TREE_FORM_ID = '"+formId+"'";
		}
		String searchValue = (String) filter.get("searchValue");
		if (searchValue!=null && searchValue!="") {
			wheres += " and  SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_CODE like "+" '%"+searchValue+"%' ";
		}
		
		String sql =" select SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_ID as treeNodeTypeId, "+
                    " SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_CODE as treeNodeTypeCode, "+
                	" SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_ICON as treeNodeTypeIcon, "+
					" SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_TITTLE as treeNodeTypeTittle, "+
					" SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_EXC_TYPE as treeNodeTypeExcType, "+
				    " SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_FORM_NAME as treeNodeTypeFormName, "+
    			    " SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_URL as treeNodeTypeUrl, "+
    			    " SYS_TREE_NODE_TYPE.TREE_NODE_TYPE_ICON as treeNodeTypeIcon "+
    			    " from  SYS_TREE_NODE_TYPE "+
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
	 * 根据Id查询树节点信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getTreeNodeTypeById")
	public @ResponseBody SysTreeNodeType getTreeNodeTypeById(HttpServletRequest request) throws Exception{
		String treeNodeTypeId = request.getParameter("treeNodeTypeId");
		
		SysTreeNodeTypeExample example = new SysTreeNodeTypeExample();
		com.myehr.pojo.formmanage.form.SysTreeNodeTypeExample.Criteria criteria = example.createCriteria();
		criteria.andTreeNodeTypeIdEqualTo(new BigDecimal(treeNodeTypeId));
		SysTreeNodeType treeNode = sysTreeNodeTypeMapper.selectByExample(example).get(0);
		
		return treeNode;
	}
	
	/**
	 * 保存树节点信息
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveTreeNodeType")
	public @ResponseBody String saveTreeNodeType(HttpServletRequest request, @RequestBody SaveButtonParams params) throws Exception{
		String menuJson = "";
		
		Map param = params.getParam();
		Map paramsMap = params.getParamsMap();
		int reCode = 0;
		
		String treeNodeTypeId = (String) param.get("treeNodeTypeId");
		SysTreeNodeType treeNode = new SysTreeNodeType();
		if (treeNodeTypeId==null || treeNodeTypeId=="") {
			String treeNodeTypeExcType = (String) param.get("treeNodeTypeExcType");
			if (treeNodeTypeExcType.equals("url")) {
				treeNode.setTreeNodeTypeUrl((String) param.get("treeNodeTypeUrl"));
			}else{
				treeNode.setTreeNodeTypeFormId(new BigDecimal((String) param.get("treeNodeTypeFormId")));
				treeNode.setTreeNodeTypeFormName((String) param.get("treeNodeTypeFormName"));
			}
			treeNode.setFormTreeFormId(new BigDecimal((String) param.get("formTreeFormId")));
			treeNode.setTreeNodeTypeCode((String) param.get("treeNodeTypeCode"));
			treeNode.setTreeNodeTypeIcon((String) param.get("treeNodeTypeIcon"));
			treeNode.setTreeNodeTypeTittle((String) param.get("treeNodeTypeTittle"));
			treeNode.setTreeNodeTypeExcType((String) param.get("treeNodeTypeExcType"));
			treeNode.setOperatorName((String) paramsMap.get("userId"));
			treeNode.setOperatorTime(new Date());
			reCode = sysTreeNodeTypeMapper.insert(treeNode);
		}else {
			SysTreeNodeTypeExample example = new SysTreeNodeTypeExample();
			com.myehr.pojo.formmanage.form.SysTreeNodeTypeExample.Criteria criteria = example.createCriteria();
			criteria.andTreeNodeTypeIdEqualTo(new BigDecimal(treeNodeTypeId));
			List<SysTreeNodeType> sysTreeNodeTypes = sysTreeNodeTypeMapper.selectByExample(example);
			if (sysTreeNodeTypes.size()>0) {
				treeNode = sysTreeNodeTypes.get(0);
			}
			
			treeNode.setTreeNodeTypeId(new BigDecimal((treeNodeTypeId)));
			treeNode.setFormTreeFormId(new BigDecimal((String) param.get("formTreeFormId")));
			treeNode.setTreeNodeTypeCode((String) param.get("treeNodeTypeCode"));
			treeNode.setTreeNodeTypeIcon((String) param.get("treeNodeTypeIcon"));
			treeNode.setTreeNodeTypeTittle((String) param.get("treeNodeTypeTittle"));
			treeNode.setTreeNodeTypeExcType((String) param.get("treeNodeTypeExcType"));
			treeNode.setTreeNodeTypeUrl((String) param.get("treeNodeTypeUrl"));
			treeNode.setTreeNodeTypeFormId(new BigDecimal((String) param.get("treeNodeTypeFormId")));
			treeNode.setTreeNodeTypeFormName((String) param.get("treeNodeTypeFormName"));
			treeNode.setOperatorName((String) paramsMap.get("userId"));
			treeNode.setOperatorTime(new Date());
			reCode = sysTreeNodeTypeMapper.updateByPrimaryKey(treeNode);
		}
		
		return menuJson;
	}
	
	/**
	 * 根据Id删除树节点信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteTreeNodeTypeById")
	public @ResponseBody String[] deleteTreeNodeTypeById(HttpServletRequest request) throws Exception{
		String[] reCode  = new String[2];
		String treeNodeTypeId = request.getParameter("treeNodeTypeId");
		String[] treeNodeTypeIds = treeNodeTypeId.split(",");
		for (int i = 0; i < treeNodeTypeIds.length; i++) {
			sysTreeNodeTypeMapper.deleteByPrimaryKey(new BigDecimal(treeNodeTypeIds[i]));
		}
		reCode[0]="0";
		return reCode;
	}
}
