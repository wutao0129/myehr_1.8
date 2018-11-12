/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.myehr.service.flow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myehr.common.exception.DcfException;
import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.util.ChangeCode;
import com.myehr.common.util.CreateFileUtil;
import com.myehr.common.utils.ActUtils;
import com.myehr.common.utils.ProcessDefCache;
import com.myehr.common.utils.ProcessDefUtils;
import com.myehr.controller.flow.ActSaveDataParams;
import com.myehr.controller.flow.ResultActListForVueMap;
import com.myehr.controller.flow.ResultActListMap;
import com.myehr.controller.form.parambean.CardformInitDataParams;
import com.myehr.mapper.activiti.ActHiProcinstMapper;
import com.myehr.mapper.activiti.ActReModelMapper;
import com.myehr.mapper.activiti.ActRuTaskMapper;
import com.myehr.mapper.activiti.expand.ActNodePropertiesExpandMapper;
import com.myehr.mapper.formmanage.form.SysActfreeConductMapper;
import com.myehr.mapper.formmanage.form.SysActfreeHisMapper;
import com.myehr.mapper.sysuser.SysUserMapper;
import com.myehr.mapper.task.SysTaskMapper;
import com.myehr.pojo.act.Act;
import com.myehr.pojo.act.ActForMobile;
import com.myehr.pojo.activiti.ActHiProcinst;
import com.myehr.pojo.activiti.ActReModel;
import com.myehr.pojo.activiti.ActRuTask;
import com.myehr.pojo.activiti.ActRuTaskExample;
import com.myehr.pojo.activiti.ProcessDiagramGenerator;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpand;
import com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample;
import com.myehr.pojo.formmanage.cache.SysFormconfigCache;
import com.myehr.pojo.formmanage.form.SysActfreeConduct;
import com.myehr.pojo.formmanage.form.SysActfreeConductExample;
import com.myehr.pojo.formmanage.form.SysActfreeHis;
import com.myehr.pojo.formmanage.form.SysActfreeHisExample;
import com.myehr.pojo.formmanage.form.SysActfreeModel;
import com.myehr.pojo.formmanage.form.SysActfreeTask;
import com.myehr.pojo.sysuser.SysUser;
import com.myehr.pojo.sysuser.SysUserExample;
import com.myehr.pojo.task.SysTask;
import com.myehr.pojo.task.SysTaskExample;
import com.myehr.service.flow.cmd.CreateAndTakeTransitionCmd;
import com.myehr.service.flow.cmd.JumpTaskCmd;
import com.myehr.service.flow.creator.ChainedActivitiesCreator;
import com.myehr.service.flow.creator.MultiInstanceActivityCreator;
import com.myehr.service.flow.creator.RuntimeActivityDefinitionEntityIntepreter;
import com.myehr.service.flow.creator.SimpleRuntimeActivityDefinitionEntity;
import com.myehr.service.formmanage.form.ISysformconfigService;

/**
 * 流程定义相关Service
 * @author zf
 * @version 2017-2-18
 */
@Service
@Transactional(readOnly = true)
public class ActTaskService {

	/*@Autowired
	private ActDao actDao;*/

	@Autowired
	private ProcessEngineFactoryBean processEngineFactory;
	
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActHiProcinstMapper actHiProcinstMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private ActReModelMapper actReModelMapper;
	@Autowired 
	private ActRuTaskMapper actRuTaskMapper;
	@Autowired 
	private SysTaskMapper sysTaskMapper;
	@Autowired
	private ActNodePropertiesExpandMapper actNodePropertiesExpandMapper;
	@Autowired
	private ISysformconfigService sysformconfigService;
	@Autowired
	private SysActfreeConductMapper sysActfreeConductMapper;
	@Autowired 
	private SysActfreeHisMapper sysActfreeHisMapper;
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 * @throws Exception 
	 */
	public ResultActListMap todoList_New(Act act,String userId,String showType,List<ActReModel> models,ActSaveDataParams params) throws Exception{
		List<Act> result = new ArrayList<Act>();
		Long total = (long) 0;
		TaskQuery todoTaskQuery = null;
		if (showType!=null && showType.equals("all")) {
			// =============== 所有已经签收的任务  ===============
			todoTaskQuery = taskService.createTaskQuery().active()
					.includeProcessVariables().orderByTaskCreateTime().desc();
		}else {
			// =============== 当前用户已经签收的任务  ===============
			todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
					.includeProcessVariables().orderByTaskCreateTime().desc();
		}
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
		
		//自由流程
		//List<SysActfreeConduct> conducts2 = sysformconfigService.getActFreeConductByUserId(userId);
		SysActfreeConductExample example3 = new SysActfreeConductExample();
    	example3.or().andAssigneeEqualTo(userId);
    	example3.setOrderByClause("OVERTIME");
    	List<SysActfreeConduct> conducts2 = sysActfreeConductMapper.selectByExample(example3);
			
		// 查询所有待批流程列表
		List<Task> todoListForTotal = todoTaskQuery.list();
		total = Long.valueOf(todoListForTotal.size());
		List<SysActfreeConduct> conducts = sysformconfigService.getActFreeConductByUserId(userId);
		int conductSize = conducts2.size();
		int realSize = 0;
		if(todoListForTotal.size()>0){
			for (Task task : todoListForTotal) {
				if (realSize<conductSize) {
					for (int i = conductSize-1; i >= realSize; i--) {
						if (compare_date(conducts2.get(i).getOvertime(),task.getCreateTime())) {
							Act freeAct = new Act();
							
							freeAct.setTaskId(conducts2.get(i).getTaskid());
							freeAct.setIsFreeAction("Y");
							freeAct.setAssignee(conducts2.get(i).getAssignee());
							freeAct.setActCreateTime(conducts2.get(i).getOvertime());
							freeAct.setCreateAgo(ActUtils.TimeAgo(conducts2.get(i).getOvertime()));
							SysActfreeTask freetask = new SysActfreeTask();
							freetask = sysformconfigService.getActfreeTask(conducts2.get(i).getTaskid());
							freeAct.setBusinessId(freetask.getActcode());
							freeAct.setTaskDefKey(conducts2.get(i).getOrderby());
							SysActfreeModel freemodel = sysformconfigService.getSysActfreeModelBytaskId(conducts2.get(i).getTaskid());
							if (freemodel!=null) {
								freeAct.setFormId(freemodel.getFormid());
								freeAct.setTaskName(freemodel.getModelname()+"("+freemodel.getModelkey()+")");
							}
							SysUser user = new SysUser();
							try {
								user = sysformconfigService.getUserByUserid(freetask.getCratename());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							freeAct.setTitle(user.getUserName()+"的自由流程");
							if (freetask.getTitle()!=null&&!freetask.getTitle().equals("")) {
								freeAct.setTitle(freetask.getTitle());
							}
							result.add(freeAct);
							conductSize--;
						}else{
							break;
						}
					}
				}
				String taskDefinitionKey = task.getTaskDefinitionKey();
				String[] aa = taskDefinitionKey.split(":");
				Act e = new Act();
				String formkey = "";
					if (aa.length>1) {
						formkey = this.getFormKey(task.getProcessDefinitionId(),aa[1]);
						e.setTaskDefKey(aa[1]);
					}else {
						formkey = this.getFormKey(task.getProcessDefinitionId(),task.getTaskDefinitionKey());
					}
					e.setActCreateTime(task.getCreateTime());
					e.setCreateAgo(ActUtils.TimeAgo(task.getCreateTime()));
					e.setTask(task);
					e.setVars(task.getProcessVariables());
					e.setFormId(formkey);
					String modelKey =  task.getProcessDefinitionId().split(":")[0];
					
					ActReModel model = new ActReModel();
					try {
						model = sysformconfigService.getActModelByKey(modelKey);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (model!=null) {
						ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
						com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample.Criteria nodePropertiesExpandCriteria = nodePropertiesExpandExample.createCriteria();
						nodePropertiesExpandCriteria.andActModelIdEqualTo("\""+model.getId()+"\"");
						nodePropertiesExpandCriteria.andActNodeNameEqualTo("\""+task.getName()+"\"");
						List<ActNodePropertiesExpand> actNodePropertiesExpands = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample);
						if (actNodePropertiesExpands.size()>0) {
							e.setIsViewProcessPicture(actNodePropertiesExpands.get(0).getIsProhibitViewingFlowChart());
							e.setIsAllowBatchApprove(actNodePropertiesExpands.get(0).getIsAllowBatchApprove());
						}
					}
					ActHiProcinst actHiProcinst = new ActHiProcinst();
					try {
						actHiProcinst = sysformconfigService.getActHiProcinstByPid(task.getProcessInstanceId());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(actHiProcinst!=null){
						//组合自由流与普通流
						
						String startUserId = (String) actHiProcinst.getStartUserId();
						SysUser startuser = sysUserMapper.selectByPrimaryKey(Integer.valueOf(startUserId));//发起人
						if (startuser==null) {
							continue;
						}
						
						SysUser userx = sysUserMapper.selectByPrimaryKey(Integer.valueOf(userId));//当前审批人
						e.setAssigneeName(userx.getUserName());
						
						//审批历史路径
						List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
						int order = historicActivityInstanceList.size()-2;
						String prevuserId = historicActivityInstanceList.get(order).getAssignee();
						SysUser user = sysUserMapper.selectByPrimaryKey(Integer.valueOf(prevuserId));//上一审批人
						
						e.setPrevAssignee(prevuserId);
						e.setPrevAssigneeName(user.getUserName());
						
						e.setCreaterPhoto(ActUtils.getUserPhotoById(startuser.getEmpId()+"", sysformconfigService)[0]);
						
						Map map = task.getProcessVariables();
						String title = map.get("title")+"";
						
						if (!title.equals("")&&!title.equals("null")) {
							String[] bb =  title.split(":");
							title = bb[0]+"的"+model.getName()+"("+bb[1]+")";
						}else {
							title = startuser.getUserName()+"的"+model.getName();
						}
						try {
							String businessId = (actHiProcinst.getBusinessKey()+"").split(":")[1];
							String titlex = sysformconfigService.getActTitle(modelKey,businessId);
							if (titlex!=null&&!titlex.equals("")) {
								title = titlex;
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.setBusinessKey(actHiProcinst.getBusinessKey()+"");
						e.setTitle(title);
						e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
						e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
						e.setStatus("todo");
						
						result.add(e);
					}
			}
			if (realSize<conductSize) {
				for (int i = conductSize-1; i <= realSize; i--) {
					Act freeAct = new Act();
					freeAct.setTaskId(conducts2.get(i).getTaskid());
					freeAct.setIsFreeAction("Y");
					freeAct.setAssignee(conducts2.get(i).getAssignee());
					freeAct.setActCreateTime(conducts2.get(i).getOvertime());
					SysActfreeTask freetask = new SysActfreeTask();
					freetask = sysformconfigService.getActfreeTask(conducts2.get(i).getTaskid());
					freeAct.setBusinessId(freetask.getActcode());
					freeAct.setTaskDefKey(conducts2.get(i).getOrderby());
					SysActfreeModel freemodel = sysformconfigService.getSysActfreeModelBytaskId(conducts2.get(i).getTaskid());
					if (freemodel!=null) {
						freeAct.setFormId(freemodel.getFormid());
						freeAct.setTaskName(freemodel.getModelname()+"("+freemodel.getModelkey()+")");
					}
					SysUser user = new SysUser();
					try {
						user = sysformconfigService.getUserByUserid(freetask.getCratename());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					freeAct.setTitle(user.getUserName()+"的自由流程");
					if (freetask.getTitle()!=null&&!freetask.getTitle().equals("")) {
						freeAct.setTitle(freetask.getTitle());
					}
					result.add(freeAct);
				}
			}
		}else {
			for (int i = conductSize-1; i >= 0; i--) {
				Act freeAct = new Act();
				freeAct.setTaskId(conducts2.get(i).getTaskid());
				freeAct.setIsFreeAction("Y");
				freeAct.setAssignee(conducts2.get(i).getAssignee());
				freeAct.setActCreateTime(conducts2.get(i).getOvertime());
				SysActfreeTask freetask = new SysActfreeTask();
				freetask = sysformconfigService.getActfreeTask(conducts2.get(i).getTaskid());
				freeAct.setBusinessId(freetask.getActcode());
				freeAct.setTaskDefKey(conducts2.get(i).getOrderby());
				SysActfreeModel freemodel = sysformconfigService.getSysActfreeModelBytaskId(conducts2.get(i).getTaskid());
				if (freemodel!=null) {
					freeAct.setFormId(freemodel.getFormid());
					freeAct.setTaskName(freemodel.getModelname()+"("+freemodel.getModelkey()+")");
				}
				SysUser user = new SysUser();
				try {
					user = sysformconfigService.getUserByUserid(freetask.getCratename());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				freeAct.setTitle(user.getUserName()+"的自由流程");
				if (freetask.getTitle()!=null&&!freetask.getTitle().equals("")) {
					freeAct.setTitle(freetask.getTitle());
				}
				result.add(freeAct);
			}
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			toClaimQuery.taskCreatedBefore(act.getEndDate());
		}
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			
			e.setStatus("claim");
			for (SysActfreeConduct conduct : conducts) {
				if (compare_date(act.getActCreateTime(),conduct.getOvertime())) {
					Act freeAct = new Act();
					freeAct.setTaskId(conduct.getTaskid());
					freeAct.setIsFreeAction("Y");
					freeAct.setAssignee(conduct.getAssignee());
					freeAct.setBeginDate(conduct.getOvertime());
					result.add(freeAct);
				}
			}
			result.add(e);
		}
		if (conducts!=null) {
			total = total + conducts.size();
		}
		ResultActListMap map = new ResultActListMap();
		map.setRows(result);
		map.setTotal(total);
		return map;
	}

	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public ResultActListForVueMap todoListForMobile(Act act,String userId,String showType,List<ActReModel> models,ActSaveDataParams params){
		List<ActForMobile> result = new ArrayList<ActForMobile>();
		Long total = (long) 0;
		TaskQuery todoTaskQuery = null;
		if (showType!=null && showType.equals("all")) {
			// =============== 所有已经签收的任务  ===============
			todoTaskQuery = taskService.createTaskQuery().active()
					.includeProcessVariables().orderByTaskCreateTime().desc();
		}else {
			// =============== 当前用户已经签收的任务  ===============
			todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
					.includeProcessVariables().orderByTaskCreateTime().desc();
		}
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
		// 查询列表
		List<Task> todoListForTotal = todoTaskQuery.list();
		total = Long.valueOf(todoListForTotal.size());
		/*int offSet = params.getOffset();
		int limit = params.getLimit();
		List<Task> todoList = todoTaskQuery.listPage(offSet, limit);*/
		for (Task task : todoListForTotal) {
			String taskDefinitionKey = task.getTaskDefinitionKey();
			String[] aa = taskDefinitionKey.split(":");
			ActForMobile e = new ActForMobile();
			String formkey = "";
				if (aa.length>1) {
					formkey = this.getFormKey(task.getProcessDefinitionId(),aa[1]);
					e.setTaskDefKey(aa[1]);
				}else {
					formkey = this.getFormKey(task.getProcessDefinitionId(),task.getTaskDefinitionKey());
				}
				e.setActCreateTime(task.getCreateTime());
				e.setTask(task);
				e.setVars(task.getProcessVariables());
				e.setFormId(formkey);
				try {
					SysFormconfigCache form;
					try {
						form = sysformconfigService.getForm(formkey);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						continue;
					}
					String path11 = form.getFormTreePath();
					String path="/myehrpath/form/"+path11;
					e.setPath(path+form.getPojoform().getFormDefCode()+".vue");
				} catch (DcfException e2) {
					e2.printStackTrace();
				} 
				
				String modelKey =  task.getProcessDefinitionId().split(":")[0];
				ActReModel model = new ActReModel();
				try {
					model = sysformconfigService.getActModelByKey(modelKey);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (model!=null) {
					ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
					com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample.Criteria nodePropertiesExpandCriteria = nodePropertiesExpandExample.createCriteria();
					nodePropertiesExpandCriteria.andActModelIdEqualTo("\""+model.getId()+"\"");
					nodePropertiesExpandCriteria.andActNodeNameEqualTo("\""+task.getName()+"\"");
					List<ActNodePropertiesExpand> actNodePropertiesExpands = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample);
					if (actNodePropertiesExpands.size()>0) {
						e.setIsViewProcessPicture(actNodePropertiesExpands.get(0).getIsProhibitViewingFlowChart());
						e.setIsAllowBatchApprove(actNodePropertiesExpands.get(0).getIsAllowBatchApprove());
					}
				}
				ActHiProcinst actHiProcinst = new ActHiProcinst();
				try {
					actHiProcinst = sysformconfigService.getActHiProcinstByPid(task.getProcessInstanceId());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(actHiProcinst!=null){
					String startUserId = (String) actHiProcinst.getStartUserId();
					SysUserExample example1 = new SysUserExample();
					com.myehr.pojo.sysuser.SysUserExample.Criteria criteria1 = example1.createCriteria();
					criteria1.andUserIdEqualTo(Integer.valueOf(startUserId));
					List<SysUser> userlists = sysUserMapper.selectByExample(example1);
					if (userlists.size()==0) {
						continue;
					}
					Map map = task.getProcessVariables();
					String title = map.get("title")+"";
					
					if (!title.equals("")&&!title.equals("null")) {
						String[] bb =  title.split(":");
						title = bb[0]+"的"+model.getName()+"("+bb[1]+")";
					}else {
						title = userlists.get(0).getUserName()+"的"+model.getName();
					}
					try {
						String businessId = (actHiProcinst.getBusinessKey()+"").split(":")[1];
						String titlex = sysformconfigService.getActTitle(modelKey,businessId);
						if (titlex!=null&&!titlex.equals("")) {
							title = titlex;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.setBusinessKey(actHiProcinst.getBusinessKey()+"");
					e.setTitle(title);
					e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
					e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
					//e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
					e.setStatus("todo");
					result.add(e);
				}
			// =============== 等待签收的任务  ===============
			TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			
			// 设置查询条件
			if (StringUtils.isNotBlank(act.getProcDefKey())){
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (act.getBeginDate() != null){
				toClaimQuery.taskCreatedAfter(act.getBeginDate());
			}
			if (act.getEndDate() != null){
				toClaimQuery.taskCreatedBefore(act.getEndDate());
			}
		}
		ResultActListForVueMap map = new ResultActListForVueMap();
		map.setRows(result);
		map.setTotal(total);
		return map;
	}
	
/**
 * 获取待办列表
 * @param procDefKey 流程定义标识
 * @return
 */
public ResultActListMap todoList(Act act,String userId,String showType,List<ActReModel> models,ActSaveDataParams params){
	List<Act> result = new ArrayList<Act>();
	Long total = (long) 0;
	TaskQuery todoTaskQuery = null;
	if (showType!=null && showType.equals("all")) {
		// =============== 所有已经签收的任务  ===============
		todoTaskQuery = taskService.createTaskQuery().active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
	}else {
		// =============== 当前用户已经签收的任务  ===============
		todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
	}
	
	// 设置查询条件
	if (StringUtils.isNotBlank(act.getProcDefKey())){
		todoTaskQuery.processDefinitionKey(act.getProcDefKey());
	}
	if (act.getBeginDate() != null){
		todoTaskQuery.taskCreatedAfter(act.getBeginDate());
	}
	if (act.getEndDate() != null){
		todoTaskQuery.taskCreatedBefore(act.getEndDate());
	}
	
	
	if (models!=null&&models.size()>0) {
		for (int i = 0; i < models.size(); i++) {
			todoTaskQuery.processDefinitionKey(models.get(i).getKey()+"");
			// 查询列表
			List<Task> todoList = todoTaskQuery.list();
			for (Task task : todoList) {
				String taskDefinitionKey = task.getTaskDefinitionKey();
				String[] aa = taskDefinitionKey.split(":");
				Act e = new Act();
				String formkey = "";
					if (aa.length>1) {
						formkey = this.getFormKey(task.getProcessDefinitionId(),aa[1]);
						e.setTaskDefKey(aa[1]);
					}else {
						formkey = this.getFormKey(task.getProcessDefinitionId(),task.getTaskDefinitionKey());
					}
					e.setActCreateTime(task.getCreateTime());
					e.setTask(task);
					e.setVars(task.getProcessVariables());
					e.setFormId(formkey);
					String modelKey =  task.getProcessDefinitionId().split(":")[0];
					
					ActReModel model = new ActReModel();
					try {
						model = sysformconfigService.getActModelByKey(modelKey);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (model!=null) {
						ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
						com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample.Criteria nodePropertiesExpandCriteria = nodePropertiesExpandExample.createCriteria();
						nodePropertiesExpandCriteria.andActModelIdEqualTo("\""+model.getId()+"\"");
						nodePropertiesExpandCriteria.andActNodeNameEqualTo("\""+task.getName()+"\"");
						List<ActNodePropertiesExpand> actNodePropertiesExpands = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample);
						if (actNodePropertiesExpands.size()>0) {
							e.setIsViewProcessPicture(actNodePropertiesExpands.get(0).getIsProhibitViewingFlowChart());
							e.setIsAllowBatchApprove(actNodePropertiesExpands.get(0).getIsAllowBatchApprove());
						}
					}
					ActHiProcinst actHiProcinst = new ActHiProcinst();
					try {
						actHiProcinst = sysformconfigService.getActHiProcinstByPid(task.getProcessInstanceId());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(actHiProcinst!=null){
						String startUserId = (String) actHiProcinst.getStartUserId();
						
						SysUserExample example1 = new SysUserExample();
						com.myehr.pojo.sysuser.SysUserExample.Criteria criteria1 = example1.createCriteria();
						criteria1.andUserIdEqualTo(Integer.valueOf(startUserId));
						List<SysUser> userlists = sysUserMapper.selectByExample(example1);
						if (userlists.size()==0) {
							continue;
						}
						
						Map map = task.getProcessVariables();
						String title = map.get("title")+"";
						
						if (!title.equals("")&&!title.equals("null")) {
							String[] bb =  title.split(":");
							title = bb[0]+"的"+model.getName()+"("+bb[1]+")";
						}else {
							title = userlists.get(0).getUserName()+"的"+model.getName();
						}
						try {
							String businessId = (actHiProcinst.getBusinessKey()+"").split(":")[1];
							String titlex = sysformconfigService.getActTitle(modelKey,businessId);
							if (titlex!=null&&!titlex.equals("")) {
								title = titlex;
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.setBusinessKey(actHiProcinst.getBusinessKey()+"");
						e.setTitle(title);
						e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
						e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
						//e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
						e.setStatus("todo");
						result.add(e);
						total++;
					}
			}
			// =============== 等待签收的任务  ===============
			TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			
			// 设置查询条件
			if (StringUtils.isNotBlank(act.getProcDefKey())){
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (act.getBeginDate() != null){
				toClaimQuery.taskCreatedAfter(act.getBeginDate());
			}
			if (act.getEndDate() != null){
				toClaimQuery.taskCreatedBefore(act.getEndDate());
			}
			System.out.println(toClaimQuery.count());
			// 查询列表
			List<Task> toClaimList = toClaimQuery.list();
			for (Task task : toClaimList) {
				Act e = new Act();
				e.setTask(task);
				e.setVars(task.getProcessVariables());
				
				e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
				
				e.setStatus("claim");
				result.add(e);
				total++;
			}
		}
	}else{
		// 查询列表
		List<Task> todoListForTotal = todoTaskQuery.list();
		total = Long.valueOf(todoListForTotal.size());
		/*int offSet = params.getOffset();
		int limit = params.getLimit();
		List<Task> todoList = todoTaskQuery.listPage(offSet, limit);*/
		for (Task task : todoListForTotal) {
			String taskDefinitionKey = task.getTaskDefinitionKey();
			String[] aa = taskDefinitionKey.split(":");
			Act e = new Act();
			String formkey = "";
				if (aa.length>1) {
					formkey = this.getFormKey(task.getProcessDefinitionId(),aa[1]);
					e.setTaskDefKey(aa[1]);
				}else {
					formkey = this.getFormKey(task.getProcessDefinitionId(),task.getTaskDefinitionKey());
				}
				e.setActCreateTime(task.getCreateTime());
				e.setTask(task);
				e.setVars(task.getProcessVariables());
				e.setFormId(formkey);
				String modelKey =  task.getProcessDefinitionId().split(":")[0];
				
				ActReModel model = new ActReModel();
				try {
					model = sysformconfigService.getActModelByKey(modelKey);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (model!=null) {
					ActNodePropertiesExpandExample nodePropertiesExpandExample = new ActNodePropertiesExpandExample();
					com.myehr.pojo.activiti.expand.ActNodePropertiesExpandExample.Criteria nodePropertiesExpandCriteria = nodePropertiesExpandExample.createCriteria();
					nodePropertiesExpandCriteria.andActModelIdEqualTo("\""+model.getId()+"\"");
					nodePropertiesExpandCriteria.andActNodeNameEqualTo("\""+task.getName()+"\"");
					List<ActNodePropertiesExpand> actNodePropertiesExpands = actNodePropertiesExpandMapper.selectByExample(nodePropertiesExpandExample);
					if (actNodePropertiesExpands.size()>0) {
						e.setIsViewProcessPicture(actNodePropertiesExpands.get(0).getIsProhibitViewingFlowChart());
						e.setIsAllowBatchApprove(actNodePropertiesExpands.get(0).getIsAllowBatchApprove());
					}
				}
				ActHiProcinst actHiProcinst = new ActHiProcinst();
				try {
					actHiProcinst = sysformconfigService.getActHiProcinstByPid(task.getProcessInstanceId());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(actHiProcinst!=null){
					String startUserId = (String) actHiProcinst.getStartUserId();
					
					SysUserExample example1 = new SysUserExample();
					com.myehr.pojo.sysuser.SysUserExample.Criteria criteria1 = example1.createCriteria();
					criteria1.andUserIdEqualTo(Integer.valueOf(startUserId));
					List<SysUser> userlists = sysUserMapper.selectByExample(example1);
					if (userlists.size()==0) {
						continue;
					}
					
					Map map = task.getProcessVariables();
					String title = map.get("title")+"";
					
					if (!title.equals("")&&!title.equals("null")) {
						String[] bb =  title.split(":");
						title = bb[0]+"的"+model.getName()+"("+bb[1]+")";
					}else {
						title = userlists.get(0).getUserName()+"的"+model.getName();
					}
					try {
						String businessId = (actHiProcinst.getBusinessKey()+"").split(":")[1];
						String titlex = sysformconfigService.getActTitle(modelKey,businessId);
						if (titlex!=null&&!titlex.equals("")) {
							title = titlex;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.setBusinessKey(actHiProcinst.getBusinessKey()+"");
					e.setTitle(title);
					e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
					e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
					//e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
					e.setStatus("todo");
					result.add(e);
				}
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			toClaimQuery.taskCreatedBefore(act.getEndDate());
		}
		System.out.println(toClaimQuery.count());
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			
			e.setStatus("claim");
			result.add(e);
		}
	}
	ResultActListMap map = new ResultActListMap();
	map.setRows(result);
	map.setTotal(total);
	return map;
}
	
	public boolean compare_date(Date date, Date date2) {
        try {
            Date dt1 = date;
            Date dt2 = date2;
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
	
	public long getHistoricListTotal(String userId) {
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		return histTaskQuery.count();
	}
	public long getTodoListTotal(String userId,List<ActReModel> models) {
		TaskQuery todoTaskQuery = null;
		todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active().includeProcessVariables().orderByTaskCreateTime().desc();
		long num = 0;
		if (models.size()>0) {
			for (int i = 0; i < models.size(); i++) {
				todoTaskQuery.processDefinitionKey(models.get(i).getKey()+"");
				num += todoTaskQuery.count();
			}
		}
		return num;
	}
	
	/**
	 * 获取已办任务
	 * @param params 
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 * @throws Exception 
	 */
	public ResultActListMap historicList(Act act,String userId, CardformInitDataParams params) throws Exception{
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.taskCompletedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.taskCompletedBefore(act.getEndDate());
		}
		
		// 查询总数
//		page.setCount(histTaskQuery.count());
		
		// 查询列表
		int offSet = Integer.valueOf(params.getOffset());
		int limit = Integer.valueOf(params.getLimit());
//		histTaskQuery.orderByHistoricTaskInstanceEndTime();
		List<HistoricTaskInstance> histList = histTaskQuery.listPage(offSet,limit);
		List<HistoricTaskInstance> histListx = histTaskQuery.list();
		int totalx = histListx.size();
		Map mapy = new HashMap();
		for (HistoricTaskInstance historicTaskInstance : histListx) {
			String exId = historicTaskInstance.getExecutionId();
			String flag = mapy.get(exId)+"";
			if (!flag.equals("null")) {
				totalx--;
				continue;
			}
			mapy.put(exId, "true");
		}
		//处理分页问题
		List<Act> actList=Lists.newArrayList();
		Map mapx = new HashMap();
		for (HistoricTaskInstance histTask : histList) {
			String exId = histTask.getExecutionId();
			String flag = mapx.get(exId)+"";
			if (!flag.equals("null")) {
				continue;
			}
			mapx.put(exId, "true");
			Act e = new Act();
			ActHiProcinst actP = sysformconfigService.getActHiProcinstByPid(histTask.getProcessInstanceId());
			String modelKey =  histTask.getProcessDefinitionId().split(":")[0];
			ActReModel actReModel = sysformconfigService.getActModelByKey(modelKey);
			if (actP!=null) {
				String startUserId = (String) actP.getStartUserId();
				SysUserExample example1 = new SysUserExample();
				example1.or().andUserIdEqualTo(Integer.valueOf(startUserId));
				List<SysUser> userlists = sysUserMapper.selectByExample(example1);
				if (userlists.size()==0) {
					continue;
				}
				Map map = histTask.getProcessVariables();
				String title = map.get("title")+"";
				if (!title.equals("")&&!title.equals("null")) {
					String[] bb =  title.split(":");
					title = bb[0]+"的"+actReModel.getName()+"("+bb[1]+")";
				}else {
					title = userlists.get(0).getUserName()+"的"+actReModel.getName();
				}
				try {
					String businessId = (histTask.getProcessDefinitionId()+"").split(":")[1];
					String titlex = sysformconfigService.getActTitle(modelKey,businessId);
					if (titlex!=null&&!titlex.equals("")) {
						title = titlex;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.setTitle(title);

				String businesskey = (String)actP.getBusinessKey();
				String[] aa = businesskey.split(":");
				e.setBusinessId(aa[1]);
				e.setBusinessTable(aa[0]);
				e.setBusinessKey(businesskey);
				e.setActCreateTime(histTask.getCreateTime());
				e.setActEndTime(histTask.getEndTime());
				e.setHistTask(histTask);
				e.setTaskId(histTask.getId());
				e.setTaskDefKey(histTask.getTaskDefinitionKey());
				e.setTaskName(histTask.getName());
				e.setVars(histTask.getProcessVariables());
				e.setAssignee(histTask.getAssignee());
				e.setProcDefId(histTask.getProcessDefinitionId());
				e.setProcInsId(histTask.getProcessInstanceId());
				e.setProcDefKey(histTask.getTaskDefinitionKey());
	//			e.setTaskVars(histTask.getTaskLocalVariables());
	//			System.out.println(histTask.getId()+"  =  "+histTask.getProcessVariables() + "  ========== " + histTask.getTaskLocalVariables());
				e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
	//			e.setProcIns(runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
	//			e.setProcExecUrl(ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
				e.setStatus("finish");
				e.setFlag(modelKey);
				actList.add(e);
				//page.getList().add(e);
			}
		}
		
		List<Act> actListx = new ArrayList<Act>();
		for (Act act2 : actList) {
			if (act2!=null) {
				actListx.add(act2);
			}
		}
		ResultActListMap result = new ResultActListMap();
		result.setRows(actListx);
		result.setTotal(totalx);
		return result;
	}
	
	/**
	 * 获取已办任务
	 * @param params 
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 * @throws Exception 
	 */
	public ResultActListMap historicFreeList(Act act,String userId, CardformInitDataParams params) throws Exception{
		// 查询列表
		int offSet = Integer.valueOf(params.getOffset());
		int limit = Integer.valueOf(params.getLimit());
		int end = offSet*limit+limit;
		SysActfreeHisExample example = new SysActfreeHisExample();
		example.or().andAssigreeEqualTo(userId);
		List<SysActfreeHis> hiss = sysActfreeHisMapper.selectByExample(example);
		String sql = "SELECT * FROM SYS_ACTFREE_HIS h WHERE assigree IN ( SELECT TOP "+limit+" assigree FROM ( SELECT TOP "+end+" assigree, ENDTIME FROM SYS_ACTFREE_HIS where assigree = "+userId+" ORDER BY ENDTIME DESC) w ORDER BY w.ENDTIME DESC) ORDER BY h.ENDTIME DESC";
		List<Map> his = MybatisUtil.queryList("runtime.selectSql", sql);
		List<Act> actList = new ArrayList<Act>();
		int total = hiss.size();
		for (Map map : his) {
			Act e = new Act();
			String taskId = map.get("TASKID")+"";
			SysActfreeTask task = sysformconfigService.getActfreeTask(taskId);
			SysActfreeModel model = sysformconfigService.getSysActfreeModelBytaskId(taskId);
			if (model==null) {
				total--;
				continue;
			}
			e.setTitle(task.getTitle());
			e.setStatus("finish");
			e.setFlag(task.getModelkey());
			e.setTaskName(model.getModelname());
			String startTime = map.get("STARTTIME")+"";
			String endTime = map.get("ENDTIME")+"";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			e.setActCreateTime(sdf.parse(startTime));
			e.setActEndTime(sdf.parse(endTime));
			e.setBusinessId(task.getActcode());
			e.setProcInsId(taskId);
			actList.add(e);
		}
		ResultActListMap result = new ResultActListMap();
		result.setRows(actList);
		result.setTotal(total);
		return result;
	}
	
	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	public List<Act> histoicFlowList(String procInsId, String startAct, String endAct){
		List<Act> actList = Lists.newArrayList();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId)
				.orderByHistoricActivityInstanceStartTime().desc().orderByHistoricActivityInstanceEndTime().desc().list();
		
		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();
		
		for (int i=0; i<list.size(); i++){
			
			HistoricActivityInstance histIns = list.get(i);
			
			// 过滤开始节点前的节点
			if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())){
				start = true;
			}
			if (StringUtils.isNotBlank(startAct) && !start){
				//continue;
			}
			
			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtils.isNotBlank(histIns.getAssignee())
					 || "startEvent".equals(histIns.getActivityType())
					 || "endEvent".equals(histIns.getActivityType())){
				
				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null){
					actMap.put(histIns.getActivityId(), actMap.size());
				}
				
				Act e = new Act();
				e.setHistIns(histIns);
				// 获取流程发起人名称
				if ("startEvent".equals(histIns.getActivityType())){
					List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).orderByProcessInstanceStartTime().asc().list();
//					List<HistoricIdentityLink> il = historyService.getHistoricIdentityLinksForProcessInstance(procInsId);
					if (il.size() > 0){
						if (StringUtils.isNotBlank(il.get(0).getStartUserId())){
							/*User user = UserUtils.getByLoginName(il.get(0).getStartUserId());
							if (user != null){
								e.setAssignee(histIns.getAssignee());
								e.setAssigneeName(user.getName());
							}*/
						}
					}
				}
				// 获取任务执行人名称
				if (StringUtils.isNotEmpty(histIns.getAssignee())){
					/*User user = UserUtils.getByLoginName(histIns.getAssignee());
					if (user != null){
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
					}*/
				}
				// 获取意见评论内容
				if (StringUtils.isNotBlank(histIns.getTaskId())){
					List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size()>0){
						e.setComment(commentList.get(0).getFullMessage());
					}
				}
				actList.add(e);
			}
			
			// 过滤结束节点后的节点
			if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())){
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j=i+1; j<list.size(); j++){
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())){
						bl = true;
					}
				}
				if (!bl){
					break;
				}
			}
		}
		return actList;
	}

	
	
	
	/**
	 *当前节点信息
	 */
	public List<Act> activityIdFlowList(String activityId, String startAct, String endAct){
		List<Act> actList = Lists.newArrayList();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().activityId(activityId)
				.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();
		
		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();
		
		for (int i=0; i<list.size(); i++){
			
			HistoricActivityInstance histIns = list.get(i);
			
			// 过滤开始节点前的节点
			if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())){
				start = true;
			}
			if (StringUtils.isNotBlank(startAct) && !start){
				continue;
			}
			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtils.isNotBlank(histIns.getAssignee())
					 || "startEvent".equals(histIns.getActivityType())
					 || "endEvent".equals(histIns.getActivityType())){
				
				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null){
					actMap.put(histIns.getActivityId(), actMap.size());
				}
				
				Act e = new Act();
				e.setHistIns(histIns);
				// 获取任务执行人名称
				if (StringUtils.isNotEmpty(histIns.getAssignee())){
					/*User user = UserUtils.getByLoginName(histIns.getAssignee());
					if (user != null){
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
					}*/
				}
				// 获取意见评论内容
				if (StringUtils.isNotBlank(histIns.getTaskId())){
					List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size()>0){
						e.setComment(commentList.get(0).getFullMessage());
					}
				}
				actList.add(e);
			}
			
			// 过滤结束节点后的节点
			if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())){
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j=i+1; j<list.size(); j++){
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())){
						bl = true;
					}
				}
				if (!bl){
					break;
				}
			}
		}
		return actList;
	}
	
	
	
	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	public List<Object[]> processList(List<Object[]> page, String category) {
		/*
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().active().orderByProcessDefinitionKey().asc();
	    
	    if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    
	    //page.setCount(processDefinitionQuery.count());
	    
	    List<ProcessDefinition> processDefinitionList = null;//processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
	    for (ProcessDefinition processDefinition : processDefinitionList) {
	      String deploymentId = processDefinition.getDeploymentId();
	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	      //page.getList().add(new Object[]{processDefinition, deployment});
	    }
		return page;
	}
	
	/**
	 * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
	 * @return
	 */
	public String getFormKey(String procDefId, String taskDefKey){
		String formKey = "";
		if (StringUtils.isNotBlank(procDefId)){
			if (StringUtils.isNotBlank(taskDefKey)){
				try{
					formKey = formService.getTaskFormKey(procDefId, taskDefKey);
				}catch (Exception e) {
					formKey = "";
				}
			}
			if (StringUtils.isBlank(formKey)){
				formKey = formService.getStartFormKey(procDefId);
			}
			if (StringUtils.isBlank(formKey)){
				formKey = "/404";
			}
		}
		//logger.debug("getFormKey: {}", formKey);
		return formKey;
	}
	
	/**
	 * 获取流程实例对象
	 * @param procInsId
	 * @return
	 */
	@Transactional(readOnly = false)
	public ProcessInstance getProcIns(String procInsId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
	}

	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String userId) {
		Map map = new HashMap();
		return startProcess(null,map,procDefKey, businessTable, businessId, "" ,userId);
	}
	
	public String[] initTask(String userId,Act act,int businessId,String id) throws Exception {
		/*SysUserExample userExample = new SysUserExample();
		userExample.or().andUserIdEqualTo(Integer.parseInt(userId));
		SysUser sysUser = sysUserMapper.selectByExample(userExample).get(0);*/
		SysUser sysUser = new SysUser();
		try {
			sysUser = sysformconfigService.getUserByUserid(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String userName = sysUser.getUserName();
		
		/*ActReModelExample actReModelExample = new ActReModelExample();
		actReModelExample.or().andKeyEqualTo(act.getProcDefKey());
		ActReModel actReModel = actReModelMapper.selectByExample(actReModelExample).get(0);*/
		ActReModel actReModel = new ActReModel();
		actReModel = sysformconfigService.getActModelByKey(act.getProcDefKey());
		String modelName = (String) actReModel.getName();
		String[] reCode = passTask(userId,act,id,actReModel,businessId,userName+"的"+modelName,userId);
		return reCode;
	}
	
	public String[] passTask(String userId,Act act,String id,ActReModel actReModel,int businessId,String title,String nowUserId) throws Exception {

		ActRuTaskExample actRuTaskExample = new ActRuTaskExample();
		actRuTaskExample.or().andProcInstIdEqualTo(id);
		List<ActRuTask> actRuTasks = actRuTaskMapper.selectByExample(actRuTaskExample);
		String[] reCode = new String[2];
		reCode[0] = "000000";
		reCode[1] = id;
	
		/*if (actRuTasks.size()==0) {
			reCode[0] = "error";
			reCode[1] = "未获取节点信息";
			return reCode;
		}*/
		for (int i = 0; i < actRuTasks.size(); i++) {
			ActRuTask actRuTask = actRuTasks.get(i);
			/*ActNodePropertiesExpandExample actNodePropertiesExpandExample = new ActNodePropertiesExpandExample();
			actNodePropertiesExpandExample.or().andActModelIdEqualTo(actReModel.getId()+"").andActNodeNameEqualTo(actRuTask.getName()+"");
			ActNodePropertiesExpand ActNodePropertiesExpand = actNodePropertiesExpandMapper.selectByExample(actNodePropertiesExpandExample).get(0);*/
			Map map = new HashMap();
			try {
				map = sysformconfigService.getNodeByModelId(actReModel.getId()+"");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ActNodePropertiesExpand ActNodePropertiesExpand = new ActNodePropertiesExpand();
			try {
				ActNodePropertiesExpand = (ActNodePropertiesExpand)map.get(actRuTask.getName()+"");
			} catch (Exception e) {
				reCode[0] = "error";
				reCode[1] = "未获取节点信息";
			}
			String userIds="";
			try {
				userIds += sysformconfigService.getApproveId(actReModel.getId()+"_"+businessId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean flag = false;
			if (ActNodePropertiesExpand.getIsSkipRepeatedApprove().equals("true")) {//判断是否重复通过人员
				if (userIds.indexOf(","+userId+",")>-1||nowUserId.equals(actRuTask.getAssignee())) {
					flag = true;
				}
			}
			if (ActNodePropertiesExpand.getIsAutomaticallyPass().equals("true")||flag) {
				//将通过人员名单存入缓存
				String newUserIds = "";
				newUserIds = userIds+","+actRuTask.getAssignee()+",";
				sysformconfigService.setApproveId(actReModel.getId()+"_"+businessId,newUserIds);
				complete(actRuTask.getId()+"", actRuTask.getProcInstId()+"", "通过|通过", act.getVars().getVariableMap());

				SysTaskExample taskExample = new SysTaskExample();
				taskExample.or().andProcInsIdEqualTo(actRuTask.getProcInstId()+"").andProcTaskIdEqualTo(actRuTask.getId()+"");
				List<SysTask>  sysTaskList= sysTaskMapper.selectByExample(taskExample);
				
				ActRuTaskExample actRuTaskExampleAfter = new ActRuTaskExample();
				actRuTaskExampleAfter.or().andProcInstIdEqualTo(act.getProcInsId());
				List<ActRuTask> actRuTaskAfter = actRuTaskMapper.selectByExample(actRuTaskExampleAfter);
				
				SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Map<String,String> objMap = new HashMap<String,String>();
				objMap.put("contractid", act.getBusinessId());
				objMap.put("contractid", "3");
				objMap.put("contractid", userId);
				objMap.put("approvalstatus", "APPROVED");
				objMap.put("approvalcomments", "通过");
				objMap.put("approvedate", sdf.format(new Date()));
				if (actRuTaskAfter.size()>0) {
					objMap.put("wfstatus", "P");
				}else {
					objMap.put("wfstatus", "O");
				}
				
//						CUXOKCBPMSERAPKGPortType_CUXOKCBPMSERAPKGPort_Client.Test(objMap);
				
				
				if (sysTaskList.size()>0) {
					SysTask sysTask = new SysTask();
					sysTask = sysTaskList.get(0);
					sysTask.setSysTaskIdentifier("finish");
					sysTask.setSysTaskCompleteTime(new Date());
					sysTaskMapper.updateByPrimaryKey(sysTask);
				}else {
					SysTask sysTask = new SysTask();
					sysTask.setProcBusinessId(businessId+"");
					sysTask.setProcFormId(Long.parseLong(actRuTask.getFormKey()+""));
					sysTask.setProcInsId(actRuTask.getProcInstId()+"");
					sysTask.setProcTaskId(actRuTask.getId()+"");
					sysTask.setSysTaskContent(title);
					sysTask.setSysTaskStatus("0");
					sysTask.setSysTaskType("6");
					sysTask.setSysTaskIdentifier("finish");
					sysTask.setSysTaskCreateUserid(userId);
					sysTask.setSysTaskCreateTime(new Date());
					sysTask.setSysTaskCompleteUserid(Long.parseLong(actRuTask.getAssignee()+""));
					sysTask.setSysTaskCompleteTime(new Date());
					sysTaskMapper.insert(sysTask);
				}
				
				ActRuTaskExample actRuTaskExample1 = new ActRuTaskExample();
				actRuTaskExample1.or().andProcInstIdEqualTo(id);
				List<ActRuTask> taskList = actRuTaskMapper.selectByExample(actRuTaskExample1);
				for (int j = 0; j < taskList.size(); j++) {
					SysTaskExample taskExample1 = new SysTaskExample();
					taskExample1.or().andProcInsIdEqualTo(id).andProcTaskIdEqualTo(taskList.get(j).getId()+"");
					List<SysTask> sysTaskList1 = sysTaskMapper.selectByExample(taskExample1);
					if (sysTaskList1.size()==0) {
						SysTask sysTask = new SysTask();
						sysTask.setProcBusinessId(businessId+"");
						sysTask.setProcFormId(Long.parseLong(taskList.get(j).getFormKey()+""));
						sysTask.setProcInsId(id);
						sysTask.setProcTaskId(taskList.get(j).getId()+"");
						sysTask.setSysTaskContent(title);
						sysTask.setSysTaskStatus("0");
						sysTask.setSysTaskType("6");
						sysTask.setSysTaskIdentifier("todo");
						sysTask.setSysTaskCreateUserid(userId);
						sysTask.setSysTaskCreateTime(new Date());
						if (ChangeCode.isInteger(taskList.get(j).getAssignee()+"")) {
							sysTask.setSysTaskCompleteUserid(Long.parseLong(taskList.get(j).getAssignee()+""));
							sysTaskMapper.insert(sysTask);
						}else {
							sysTaskMapper.insert(sysTask);
							return reCode;
						}
					}
				}
				if (taskList!=null&&taskList.size()>0) {
					reCode = passTask(taskList.get(0).getAssignee()+"",act,id,actReModel,businessId,title,nowUserId);
				} else {
					reCode = passTask(userId,act,id,actReModel,businessId,title,nowUserId);
				}
			}else {
				if ((actRuTasks.get(0).getAssignee()+"").contains("SQL")) {
					reCode[0] = "error";
					reCode[1] = "下个节点审批人未找到,请相关人员维护;";		
					return reCode;
				}
				SysTaskExample taskExample1 = new SysTaskExample();
				taskExample1.or().andProcInsIdEqualTo(actRuTask.getProcInstId()+"").andProcTaskIdEqualTo(actRuTask.getId()+"");
				List<SysTask> sysTaskList1 = sysTaskMapper.selectByExample(taskExample1);
				if (sysTaskList1.size()==0) {
					SysTask sysTask = new SysTask();
					sysTask.setProcBusinessId(businessId+"");
					sysTask.setProcFormId(Long.parseLong(actRuTask.getFormKey()+""));
					sysTask.setProcInsId(id);
					sysTask.setProcTaskId(actRuTask.getId()+"");
					sysTask.setSysTaskContent(title);
					sysTask.setSysTaskStatus("0");
					sysTask.setSysTaskType("6");
					sysTask.setSysTaskIdentifier("todo");
					sysTask.setSysTaskCreateUserid(userId);
					sysTask.setSysTaskCreateTime(new Date());
					sysTask.setSysTaskCompleteUserid(Long.parseLong(actRuTask.getAssignee()+""));
					sysTaskMapper.insert(sysTask);
				}
			}
		}
		return reCode;
	}
			
	
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(Map<String, Object> vars,Map map,String procDefKey, String businessTable, String businessId, String title ,String userId) {
		/*Map<String, Object> vars = Maps.newHashMap();
		for (Object key : map.keySet()) {
			vars.put(key+"", map.get(key));	
		}*/
		return startProcessWithVars(procDefKey, businessTable, businessId, title, vars  ,userId);
	}
	
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars			流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcessWithVars(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars,String userId) {
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable+":"+businessId, vars);
		
		// 更新业务表流程实例ID
		/*Act act = new Act();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId);	// 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);*/
		return procIns.getId();
	}

	/**
	 * 获取任务
	 * @param taskId 任务ID
	 */
	public Task getTask(String taskId){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	/**
	 * 删除任务
	 * @param taskId 任务ID
	 * @param deleteReason 删除原因
	 */
	@Transactional(readOnly = false)
	public void deleteTask(String taskId, String deleteReason){
		taskService.deleteTask(taskId, deleteReason);
	}
	
	/**
	 * 签收任务
	 * @param taskId 任务ID
	 * @param userId 签收用户ID（用户登录名）
	 */
	@Transactional(readOnly = false)
	public void claim(String taskId, String userId){
		taskService.claim(taskId, userId);
	}
	
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars){
		complete(taskId, procInsId, comment, "", vars);
	}
	
	/**
	 * 驳回任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void completeReject(String procInsId,String taskId, String targetTaskDefinitionKey, Map<String, Object> vars,String comment){
		if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)){
			taskService.addComment(taskId, procInsId, comment);
		}
		jumpTask(procInsId, taskId, targetTaskDefinitionKey,  vars);
	}
	
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars 任务变量
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars){
		// 添加意见
		if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)){
			taskService.addComment(taskId, procInsId, comment);
		}
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		
		// 提交任务
		taskService.complete(taskId, vars);
	}

	/**
	 * 完成第一个任务
	 * @param procInsId
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId){
		completeFirstTask(procInsId, null, null, null);
	}
	
	/**
	 * 完成第一个任务
	 * @param procInsId
	 * @param comment
	 * @param title
	 * @param vars
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId, String comment, String title, Map<String, Object> vars){
		String userId = "";//UserUtils.getUser().getLoginName();
		Task task = taskService.createTaskQuery().taskAssignee(userId).processInstanceId(procInsId).active().singleResult();
		if (task != null){
			complete(task.getId(), procInsId, comment, title, vars);
		}
	}

//	/**
//	 * 委派任务
//	 * @param taskId 任务ID
//	 * @param userId 被委派人
//	 */
//	public void delegateTask(String taskId, String userId){
//		taskService.delegateTask(taskId, userId);
//	}
//	
//	/**
//	 * 被委派人完成任务
//	 * @param taskId 任务ID
//	 */
//	public void resolveTask(String taskId){
//		taskService.resolveTask(taskId);
//	}
//	
//	/**
//	 * 回退任务
//	 * @param taskId
//	 */
//	public void backTask(String taskId){
//		taskService.
//	}
	
	/**
	 * 添加任务意见
	 */
	public void addTaskComment(String taskId, String procInsId, String comment){
		taskService.addComment(taskId, procInsId, comment);
	}
	
	//////////////////  回退、前进、跳转、前加签、后加签、分裂 移植  https://github.com/bluejoe2008/openwebflow  ////////////////////////////////////////////////// 

	/**
	 * 任务后退一步
	 */
	public void taskBack(String procInsId, Map<String, Object> variables) {
		taskBack(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务后退至指定活动
	 */
	public void taskBack(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getIncomingTransitions().get(0).getSource();
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 任务前进一步
	 */
	public void taskForward(String procInsId, Map<String, Object> variables) {
		taskForward(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务前进至指定活动
	 */
	public void taskForward(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getOutgoingTransitions().get(0).getDestination();

		jumpTask(currentTaskEntity, activity, variables);
	}
	
	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getCurrentTask(procInsId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String currentTaskId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getTaskEntity(currentTaskId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity 当前任务节点
	 * @param targetTaskDefinitionKey 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void jumpTask(TaskEntity currentTaskEntity, String targetTaskDefinitionKey, Map<String, Object> variables) {
		String[] aa = targetTaskDefinitionKey.split(":");
		if (aa.length>1) {
			targetTaskDefinitionKey=aa[1];
		}
		ActivityImpl activity = ProcessDefUtils.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(),targetTaskDefinitionKey);
		jumpTask(currentTaskEntity, activity, variables); 
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity 当前任务节点
	 * @param targetActivity 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	private void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
	}
	
	/**
	 * 后加签
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl[] insertTasksAfter(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		List<String> assigneeList = new ArrayList<String>();
		//assigneeList.add(Authentication.getAuthenticatedUserId());
		assigneeList.addAll(CollectionUtils.arrayToList(assignees));
		String[] newAssignees = assigneeList.toArray(new String[0]);
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), targetTaskDefinitionKey);
		return cloneAndMakeChain(processDefinition, procInsId, targetTaskDefinitionKey, prototypeActivity.getOutgoingTransitions().get(0).getDestination().getId(), variables, newAssignees);
	}

	/**
	 * 前加签
	 */
	public ActivityImpl[] insertTasksBefore(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		ProcessDefinitionEntity procDef = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		return cloneAndMakeChain(procDef, procInsId, targetTaskDefinitionKey, targetTaskDefinitionKey, variables, assignees);
	}

	/**
	 * 分裂某节点为多实例节点
	 */
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignee) {
		return splitTask(procDefId, procInsId, targetTaskDefinitionKey, variables, true, assignee);
	}
	
	/**
	 * 分裂某节点为多实例节点
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, boolean isSequential, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDefId);
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

		radei.setPrototypeActivityId(targetTaskDefinitionKey);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setSequential(isSequential);
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		ActivityImpl clone = new MultiInstanceActivityCreator().createActivities(processEngine, processDefinition, info)[0];

		TaskEntity currentTaskEntity = this.getCurrentTask(procInsId);
		
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new CreateAndTakeTransitionCmd(currentTaskEntity, clone, variables));

//		recordActivitiesCreation(info);
		return clone;
	}

	private TaskEntity getCurrentTask(String procInsId) {
		return (TaskEntity) taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
	}

	private TaskEntity getTaskEntity(String taskId) {
		return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@SuppressWarnings("unchecked")
	private ActivityImpl[] cloneAndMakeChain(ProcessDefinitionEntity procDef, String procInsId, String prototypeActivityId, String nextActivityId, Map<String, Object> variables, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDef.getId());
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);
		radei.setPrototypeActivityId(prototypeActivityId);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setNextActivityId(nextActivityId);

		ActivityImpl[] activities = new ChainedActivitiesCreator().createActivities(processEngine, procDef, info);

		jumpTask(procInsId, activities[0].getId(), variables);
//		recordActivitiesCreation(info);

		return activities;
	}
	
//	private void recordActivitiesCreation(SimpleRuntimeActivityDefinitionEntity info) {
//		info.serializeProperties();
//		_activitiesCreationStore.save(info);
//	}
	
	//////////////////////////////////////////////////////////////////// 
	

//	private void recordActivitiesCreation(SimpleRuntimeActivityDefinitionEntity info) throws Exception {
//		info.serializeProperties();
//		_activitiesCreationStore.save(info);
//	}
//
//	/**
//	 * 分裂某节点为多实例节点
//	 * 
//	 * @param targetTaskDefinitionKey
//	 * @param assignee
//	 * @throws IOException
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 */
//	public ActivityImpl split(String targetTaskDefinitionKey, boolean isSequential, String... assignees) throws Exception {
//		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
//		info.setProcessDefinitionId(processDefinition.getId());
//		info.setProcessInstanceId(_processInstanceId);
//
//		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);
//
//		radei.setPrototypeActivityId(targetTaskDefinitionKey);
//		radei.setAssignees(CollectionUtils.arrayToList(assignees));
//		radei.setSequential(isSequential);
//
//		ActivityImpl clone = new MultiInstanceActivityCreator().createActivities(_processEngine, processDefinition, info)[0];
//
//		TaskEntity currentTaskEntity = getCurrentTask();
//		executeCommand(new CreateAndTakeTransitionCmd(currentTaskEntity.getExecutionId(), clone));
//		executeCommand(new DeleteRunningTaskCmd(currentTaskEntity));
//
//		recordActivitiesCreation(info);
//		return clone;
//	}
//
//	public ActivityImpl split(String targetTaskDefinitionKey, String... assignee) throws Exception {
//		return split(targetTaskDefinitionKey, true, assignee);
//	}

	////////////////////////////////////////////////////////////////////
	
	/**
	 * 读取带跟踪的图片
	 * @param executionId	环节ID
	 * @return	封装了各种节点信息
	 */
	public InputStream tracePhotoTask(String processDefinitionId, String executionId,String procInsId) {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		
		List<String> activeActivityIds = Lists.newArrayList();
		if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0){
			activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		}
		
		// 不使用spring请使用下面的两行代码
		// ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
//		return ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
		return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				.generateDiagram(bpmnModel, "png", activeActivityIds, Collections.<String> emptyList(), "宋体", "宋体",null,null, 1.0);
	}
	
	/**
	 * 读取带跟踪的图片
	 * @param executionId	环节ID
	 * @return	封装了各种节点信息
	 */
	public InputStream tracePhotoLink(String processDefinitionId, String executionId,String procInsId) {
		if(1==1){
			return getImage(procInsId);
		}
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		
		/*List<String> activeActivityIds = Lists.newArrayList();
		if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0){
			activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		}*/
		
		
		//  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInsId).singleResult();

        // 获取流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId).orderByHistoricActivityInstanceId().asc().list();
		
        // 已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        int index = 1;
        //获取已经执行的节点ID
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            executedActivityIdList.add(activityInstance.getActivityId());                
            index++;
        }
        
        // 已执行的线集合
        List<String> flowIds = new ArrayList<String>();
        // 获取流程走过的线 
        flowIds = getHighLightedFlows(processDefinition, historicActivityInstanceList);
		// 不使用spring请使用下面的两行代码
		// ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
//		return ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
		return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体", "宋体",
						null, null, 1.0);
	}
	
	public InputStream getImage(String processInstanceId) 
	{
	    try
	    {
	        //  获取历史流程实例
	        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
	                .processInstanceId(processInstanceId).singleResult();
	        if (historicProcessInstance != null)
	        {
	            // 获取流程定义
	            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
	                    .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
	            //审批历史路径
	            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();

	            // 已执行的节点ID集合
	            List<String> executedActivityIdList = new ArrayList<String>();
	            int index = 1;
	            for (HistoricActivityInstance activityInstance : historicActivityInstanceList)
	            {
	                executedActivityIdList.add(activityInstance.getActivityId());
	                index++;
	            }
	            // 获取流程图图像字符流
	            InputStream imageStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", executedActivityIdList,processInstanceId);

	          /*  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int len = -1;
	            while((len = imageStream.read(buffer)) != -1){
	                outStream.write(buffer, 0, len);
	            }
	            outStream.close();
	            imageStream.close();*/
	            return imageStream;

	        }
	        return null;
	    }
	    catch (Exception e)
	    {
	        throw new RuntimeException("获取流程图失败！" + e.getMessage());
	    }
	}
	
	/**
	 * 
	*获取流程走过的线 
	*/
	public List<String> getHighLightedFlows( ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) { 
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId 
		
		 // 已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        int index = 1;
        //获取已经执行的节点ID
        for (HistoricActivityInstance activityInstance : historicActivityInstances) {
        	//if (activityInstance.getEndTime()!=null) {
    		executedActivityIdList.add(activityInstance.getActivityId());                
            index++;
			//}
        }
		
		for (int i = 0; i < historicActivityInstances.size(); i++) {// 对历史流程节点进行遍历 
			ActivityImpl activityImpl = processDefinitionEntity .findActivity(historicActivityInstances.get(i) .getActivityId());// 得到节点定义的详细信息
			
			//TaskDefinition taskDefinition = ((UserTaskActivityBehavior)((ActivityImpl)activityImpl).getActivityBehavior()).getTaskDefinition();    
			
			List<PvmTransition> pvmTransitions = activityImpl .getOutgoingTransitions();// 取出节点的所有出去的线 
			for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历 
				if (executedActivityIdList.contains(pvmTransition.getDestination().getId())) { 
					highFlows.add(pvmTransition.getId()); 
				}
			} 
		} 
		return highFlows;
	}
	
	/**
	 * 流程跟踪图信息
	 * @param processInstanceId		流程实例ID
	 * @return	封装了各种节点信息
	 */
	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
		Object property = PropertyUtils.getProperty(execution, "activityId");
		String activityId = "";
		if (property != null) {
			activityId = property.toString();
		}
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			boolean currentActiviti = false;
			String id = activity.getId();

			// 当前节点
			if (id.equals(activityId)) {
				currentActiviti = true;
			}

			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

			activityInfos.add(activityImageInfo);
		}

		return activityInfos;
	}
	

	/**
	 * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 * @param activity
	 * @param processInstance
	 * @param currentActiviti
	 * @return
	 */
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
			boolean currentActiviti) throws Exception {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		setPosition(activity, activityInfo);
		setWidthAndHeight(activity, activityInfo);

		Map<String, Object> properties = activity.getProperties();
		vars.put("节点名称", properties.get("name"));
		vars.put("任务类型", ActUtils.parseToZhType(properties.get("type").toString()));

		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		//logger.debug("activityBehavior={}", activityBehavior);
		if (activityBehavior instanceof UserTaskActivityBehavior) {

			Task currentTask = null;

			// 当前节点的task
			if (currentActiviti) {
				currentTask = getCurrentTaskInfo(processInstance);
			}

			// 当前任务的分配角色
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if (!candidateGroupIdExpressions.isEmpty()) {

				// 任务的处理角色
				setTaskGroup(vars, candidateGroupIdExpressions);

				// 当前处理人
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}

		vars.put("节点说明", properties.get("documentation"));

		String description = activity.getProcessDefinition().getDescription();
		vars.put("描述", description);

		//logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}

	/**
	 * 设置任务组
	 * @param vars
	 * @param candidateGroupIdExpressions
	 */
	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = "";
		for (Expression expression : candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}

	/**
	 * 设置当前处理人信息
	 * @param vars
	 * @param currentTask
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
	}

	/**
	 * 获取当前节点信息
	 * @param processInstance
	 * @return
	 */
	private Task getCurrentTaskInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
			//logger.debug("current activity id: {}", activitiId);

			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
					.singleResult();
			//logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

		} catch (Exception e) {
			//logger.error("can not get property activityId from processInstance: {}", processInstance);
		}
		return currentTask;
	}

	/**
	 * 设置宽度、高度属性
	 * @param activity
	 * @param activityInfo
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}

	/**
	 * 设置坐标位置
	 * @param activity
	 * @param activityInfo
	 */
	private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	
	 /**  
     * 会签操作  
     *   
     * @param taskId  
     *            当前任务ID  
     * @param userCodes  
     *            会签人账号集合  
     * @throws Exception  
     */    
    public void jointProcess(String taskId, List<String> userCodes)    
            throws Exception {    
        for (String userCode : userCodes) {    
            TaskEntity task = (TaskEntity) taskService.newTask(UUID.randomUUID().toString().replaceAll("-", ""));    
            task.setAssignee(userCode);    
            task.setName(findTaskById(taskId).getName() + "-会签");    
            task.setProcessDefinitionId(findProcessDefinitionEntityByTaskId(    
                    taskId).getId());    
            task.setProcessInstanceId(findProcessInstanceByTaskId(taskId)    
                    .getId());    
            task.setParentTaskId(taskId);    
            task.setDescription("jointProcess");    
            taskService.saveTask(task);    
        }    
    }  
    
    /**  
     * 根据任务ID获得任务实例  
     *   
     * @param taskId  
     *            任务ID  
     * @return  
     * @throws Exception  
     */    
    private TaskEntity findTaskById(String taskId) throws Exception {    
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(    
                taskId).singleResult();    
        if (task == null) {    
            throw new Exception("任务实例未找到!");    
        }    
        return task;    
    }    
    
    
   
    
    
    
    
    
    /**
     * 获取当前流程的下一个节点
     * @param procInstanceId
     * @return
     */
    public List<Map> getNextNode(String modelId,String processDefinitionId){
    	List<Map> maps = new ArrayList<Map>();
        // 1、首先是根据流程ID获取当前任务：
        //List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(procInstanceId).list();
    	//String nextId = "";
    	//for (Task task : tasks) {
            RepositoryService rs = processEngine.getRepositoryService();
            // 2、然后根据当前任务获取当前流程的流程定义，然后根据流程定义获得所有的节点：
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rs)
                    .getDeployedProcessDefinition(processDefinitionId);
            List<ActivityImpl> activitiList = def.getActivities(); // rs是指RepositoryService的实例
           /* // 3、根据任务获取当前流程执行ID，执行实例以及当前流程节点的ID：
            String excId = task.getExecutionId();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId)
                    .singleResult();
            String activitiId = execution.getActivityId();*/
            // 4、然后循环activitiList
            // 并判断出当前流程所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
            for (ActivityImpl activityImpl : activitiList) {
                String id = activityImpl.getId();
                if ("start".equals(id)) {
                	//获取下一个节点信息 
                	Map nodes = new HashMap();
                	
                	Map map = new HashMap();
                	map.put("DefinitionId", modelId);
                	map.put("nowNode", "start");
                	map.put("nowName", "开始");
                	map.put("preNode", "");
                	map.put("preName", "");
                	maps.add(map);
                	
                	maps  = nextTaskDefinition(modelId,maps,nodes,processDefinitionId,activityImpl, activityImpl.getId(),"开始", null);  
                	
                	/*System.out.println(maps.size());
                	
                	System.out.println("当前任务：" + activityImpl.getProperty("name")); // 输出某个节点的某种属性
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();// 获取从某个节点出来的所有线路
                    for (PvmTransition tr : outTransitions) {
                        PvmActivity ac = tr.getDestination(); // 获取线路的终点节点
                        
                        System.out.println("下一步任务任务：" + ac.getProperty("name"));
                        nextId = ac.getId();
                    }
                    break;*/
                }
            }
            //}
        return maps;
    }
    
    
    /**  
     * 下一个任务节点信息,  
     *  
     * 如果下一个节点为用户任务则直接返回,  
     *  
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,  
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务信息  
     * @param ActivityImpl activityImpl     流程节点信息  
     * @param String activityId             当前流程节点Id信息  
     * @param String elString               排他网关顺序流线段判断条件, 例如排他网关顺序留线段判断条件为${money>1000}, 若满足流程启动时设置variables中的money>1000, 则流程流向该顺序流信息  
     * @param String processInstanceId      流程实例Id信息  
     * @return  
     */    
    private List<Map> nextTaskDefinition(String modelId,List<Map> maps,Map nodes, String processDefinitionId, ActivityImpl activityImpl, String activityId,String activityName, String elString){   
              
        PvmActivity ac = null;  
          
        Object s = null;  
          
        //如果遍历节点为用户任务并且节点不是当前节点信息   
        if("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())){    
            //获取该节点下一个节点信息   
            //TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition();    
            //return taskDefinition; 
            return maps;
        }else{    
            //获取节点所有流向线路信息   
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();    
            List<PvmTransition> outTransitionsTemp = null;    
            for(PvmTransition tr : outTransitions){      
                ac = tr.getDestination(); //获取线路的终点节点      
                //如果流向线路为排他网关   
                System.out.println(ac.getProperty("type"));
                if("exclusiveGateway".equals(ac.getProperty("type"))){  
                	if (nodes.get(activityId+"--->"+tr.getDestination().getId())==null) {
                		Map map = new HashMap();
                		map.put("DefinitionId", modelId);
                    	map.put("nowNode", tr.getDestination().getId());
                    	map.put("nowName", tr.getDestination().getProperty("name"));
                    	map.put("preNode", activityId);
                    	map.put("preName", activityName);
                    	maps.add(map);
                    	nodes.put(activityId+"--->"+tr.getDestination().getId(), tr.getDestination().getId());
                    	maps = nextTaskDefinition(modelId,maps,nodes,processDefinitionId,(ActivityImpl)tr.getDestination(), tr.getDestination().getId(),tr.getDestination().getProperty("name")+"", elString);   
					}
                	
                	
                    /*outTransitionsTemp = ac.getOutgoingTransitions();  
                    if (outTransitionsTemp.size()==0) {
                		return maps;
					}else {
		                for(PvmTransition tr1 : outTransitionsTemp){ 
		                	Map map = new HashMap();
		                	map.put("DefinitionId", processDefinitionId);
		                	map.put("nowNode", tr1.getId());
		                	map.put("preNode", activityId);
		                	maps.add(map); 
		                	
		                    maps = nextTaskDefinition(maps,processDefinitionId,(ActivityImpl)tr1.getDestination(), tr1.getDestination().getId(), elString, processInstanceId);   
		                }    
					}*/
                }else if("userTask".equals(ac.getProperty("type"))){
                	TaskDefinition taskDefinition = ((UserTaskActivityBehavior)((ActivityImpl)ac).getActivityBehavior()).getTaskDefinition();    
                	if (taskDefinition==null) {
                		return maps;
                	}else {
                		if (nodes.get(activityId+"--->"+tr.getDestination().getId())==null) {
                			Map map = new HashMap();
                    		map.put("DefinitionId", modelId);
                        	map.put("nowNode", tr.getDestination().getId());
                        	map.put("nowName", tr.getDestination().getProperty("name"));
                        	map.put("preNode", activityId);
                        	map.put("preName", activityName);
                        	maps.add(map);
                        	nodes.put(activityId+"--->"+tr.getDestination().getId(), tr.getDestination().getId());
                        	maps = nextTaskDefinition(modelId,maps,nodes,processDefinitionId,(ActivityImpl)tr.getDestination(), tr.getDestination().getId(),tr.getDestination().getProperty("name")+"", elString);   
    					}
					}
                }else if ("parallelGateway".equals(ac.getProperty("type"))) {
                	if (nodes.get(activityId+"--->"+tr.getDestination().getId())==null) {
                		Map map = new HashMap();
                		map.put("DefinitionId", modelId);
                    	map.put("nowNode", tr.getDestination().getId());
                    	map.put("nowName", tr.getDestination().getProperty("name"));
                    	map.put("preNode", activityId);
                    	map.put("preName", activityName);
                    	maps.add(map); 
                    	nodes.put(activityId+"--->"+tr.getDestination().getId(), tr.getDestination().getId());
                    	maps = nextTaskDefinition(modelId,maps,nodes,processDefinitionId,(ActivityImpl)tr.getDestination(), tr.getDestination().getId(),tr.getDestination().getProperty("name")+"", elString);   
					}
                	
                	/*outTransitionsTemp = ac.getOutgoingTransitions();
                	if (outTransitionsTemp.size()==0) {
                		return maps;
					}else {
						for(PvmTransition tr1 : outTransitionsTemp){
	                		Map map1 = new HashMap();
		                	map.put("DefinitionId", processDefinitionId);
		                	map.put("nowNode", tr1.getDestination().getId());
		                	map.put("preNode", tr.getDestination().getId());
		                	maps.add(map1); 
		                	maps = nextTaskDefinition(maps,processDefinitionId,(ActivityImpl)tr1.getDestination(), tr1.getDestination().getId(), elString, processInstanceId);   
	                    }    
					}*/
				}else if ("endEvent".equals(ac.getProperty("type"))) {
					Map map = new HashMap();
                	map.put("DefinitionId", modelId);
                	map.put("nowNode", tr.getDestination().getId());
                	map.put("nowName", tr.getDestination().getProperty("name"));
                	map.put("preNode", activityId);
                	map.put("preName", activityName);
                	maps.add(map); 
                	return maps;
				}
            }     
            return maps;    
        }    
    }  
      
    /** 
     * 查询流程启动时设置排他网关判断条件信息  
     * @param String gatewayId          排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息  
     * @param String processInstanceId  流程实例Id信息  
     * @return 
     */  
    public String getGatewayCondition(String gatewayId, String processInstanceId) {  
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();  
        return runtimeService.getVariable(execution.getId(), gatewayId).toString();  
    }  
      
    /** 
     * 根据key和value判断el表达式是否通过信息  
     * @param String key    el表达式key信息  
     * @param String el     el表达式信息  
     * @param String value  el表达式传入值信息  
     * @return 
     */  
    public boolean isCondition(String key, String el, String value) {  
        ExpressionFactory factory = new ExpressionFactoryImpl();    
        SimpleContext context = new SimpleContext();    
        context.setVariable(key, factory.createValueExpression(value, String.class));    
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);    
        return (Boolean) e.getValue(context);  
    }

	public String[] withdrawTaskTo(String taskId,String userId,String objId,String processInstanceId) {
		// TODO Auto-generated method stub
		String[] reCode = {"success","撤回成功"};
		try {
            // 取得当前任务
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
          //根据流程id查询代办任务中流程信息
            Task task = taskService.createTaskQuery().processInstanceId(currTask.getProcessInstanceId()).singleResult();
            if(task==null) {
    			reCode[0] = "false";
    			reCode[1] = "流程未启动或已执行完成，无法撤回";
    			return reCode;
            }
            if (objId!=null) {
            	/*List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
        				.processInstanceBusinessKey(objId)
        				.orderByTaskCreateTime()
        				.asc()
        				.list();
        		String myTaskId = null;
        		HistoricTaskInstance myTask = null;
    			if(userId.equals(htiList.get(0).getAssignee())) {
    				myTaskId = htiList.get(0).getId();
    				myTask = htiList.get(0);
    			}
        		if(null==myTaskId) {
        			reCode[0] = "false";
        			reCode[1] = "该任务非当前用户发起，无法撤回";
        			return reCode;
        		}*/
            	ActHiProcinst actHiProcinst = sysformconfigService.getActHiProcinstByPid(processInstanceId);
        		if(actHiProcinst!=null&&actHiProcinst.getStartUserId().equals(userId)) {
        			
        		}else{
        			reCode[0] = "false";
        			reCode[1] = "该任务非当前用户发起，无法撤回";
        			return reCode;
        		}
			} else {
				reCode[0] = "false";
    			reCode[1] = "业务标识为空";
    			return reCode;
			}
    		
            
            //取回流程接点 当前任务id 取回任务id
            callBackProcess(task.getId(),currTask.getId());
            //删除历史流程走向记录
            historyService.deleteHistoricTaskInstance(currTask.getId());
            historyService.deleteHistoricTaskInstance(task.getId());
            runtimeService.deleteProcessInstance(processInstanceId,"发起人撤回");
            return reCode;
        } catch (Exception e) {
        	reCode[0] = "false";
			reCode[1] = "未知错误,请联系开发人员";
    		return reCode;
        }
	}  

	/**  
     * 驳回流程  
     *   
     * @param taskId  
     *            当前任务ID  
     * @param activityId  
     *            驳回节点ID  
     * @param variables  
     *            流程存储参数  
     * @throws Exception  
     */    
    public void backProcess(String taskId, String activityId,    
            Map<String, Object> variables) throws Exception {    
        if (StringUtils.isEmpty(activityId)) {    
            throw new Exception("驳回目标节点ID为空！");    
        }    
    
        // 查找所有并行任务节点，同时驳回    
        List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(    
                taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());    
        for (Task task : taskList) {    
            commitProcess(task.getId(), variables, activityId);    
        }    
    }  
  
  
    /**  
     * 取回流程  
     *   
     * @param taskId  
     *            当前任务ID  
     * @param activityId  
     *            取回节点ID  
     * @throws Exception  
     */    
    public void callBackProcess(String taskId, String activityId)    
            throws Exception {    
        if (StringUtils.isEmpty(activityId)) {    
            throw new Exception("目标节点ID为空！");    
        }    
    
        // 查找所有并行任务节点，同时取回    
        List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(    
                taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());    
        for (Task task : taskList) {    
            commitProcess(task.getId(), null, activityId);    
        }    
    }  
  
  
    /**  
     * 清空指定活动节点流向  
     *   
     * @param activityImpl  
     *            活动节点  
     * @return 节点流向集合  
     */    
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {    
        // 存储当前节点所有流向临时变量    
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();    
        // 获取当前节点所有流向，存储到临时变量，然后清空    
        List<PvmTransition> pvmTransitionList = activityImpl    
                .getOutgoingTransitions();    
        for (PvmTransition pvmTransition : pvmTransitionList) {    
            oriPvmTransitionList.add(pvmTransition);    
        }    
        pvmTransitionList.clear();    
    
        return oriPvmTransitionList;    
    }  
  
  
    /**  
     * 提交流程/流程转向 
     * @param taskId  
     *            当前任务ID  
     * @param variables  
     *            流程变量  
     * @param activityId  
     *            流程转向执行任务节点ID<br>  
     *            此参数为空，默认为提交操作  
     * @throws Exception  
     */    
    private void commitProcess(String taskId, Map<String, Object> variables,    
            String activityId) throws Exception {    
        if (variables == null) {    
            variables = new HashMap<String, Object>();    
        }    
        // 跳转节点为空，默认提交操作    
        if (StringUtils.isEmpty(activityId)) {    
            taskService.complete(taskId, variables);    
        } else {// 流程转向操作    
            turnTransition(taskId, activityId, variables);    
        }    
    }  
  
  
    /**  
     * 中止流程(特权人直接审批通过等)  
     *   
     * @param taskId  
     */    
    public void endProcess(String taskId) throws Exception {    
        ActivityImpl endActivity = findActivitiImpl(taskId, "end");    
        commitProcess(taskId, null, endActivity.getId());    
    }  
  
  
    /**  
     * 根据流入任务集合，查询最近一次的流入任务节点  
     *   
     * @param processInstance  
     *            流程实例  
     * @param tempList  
     *            流入任务集合  
     * @return  
     */    
    private ActivityImpl filterNewestActivity(ProcessInstance processInstance,    
            List<ActivityImpl> tempList) {    
        while (tempList.size() > 0) {    
            ActivityImpl activity_1 = tempList.get(0);    
            HistoricActivityInstance activityInstance_1 = findHistoricUserTask(    
                    processInstance, activity_1.getId());    
            if (activityInstance_1 == null) {    
                tempList.remove(activity_1);    
                continue;    
            }    
    
            if (tempList.size() > 1) {    
                ActivityImpl activity_2 = tempList.get(1);    
                HistoricActivityInstance activityInstance_2 = findHistoricUserTask(    
                        processInstance, activity_2.getId());    
                if (activityInstance_2 == null) {    
                    tempList.remove(activity_2);    
                    continue;    
                }    
    
                if (activityInstance_1.getEndTime().before(    
                        activityInstance_2.getEndTime())) {    
                    tempList.remove(activity_1);    
                } else {    
                    tempList.remove(activity_2);    
                }    
            } else {    
                break;    
            }    
        }    
        if (tempList.size() > 0) {    
            return tempList.get(0);    
        }    
        return null;    
    }  
  
  
    /**  
     * 根据任务ID和节点ID获取活动节点 <br>  
     *   
     * @param taskId  
     *            任务ID  
     * @param activityId  
     *            活动节点ID <br>  
     *            如果为null或""，则默认查询当前活动节点 <br>  
     *            如果为"end"，则查询结束节点 <br>  
     *   
     * @return  
     * @throws Exception  
     */    
    private ActivityImpl findActivitiImpl(String taskId, String activityId)    
            throws Exception {    
        // 取得流程定义    
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);    
    
        // 获取当前活动节点ID    
        if (StringUtils.isEmpty(activityId)) {    
            activityId = findTaskById(taskId).getTaskDefinitionKey();    
        }else{
        HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(activityId)
                    .singleResult();
        activityId = currTask.getTaskDefinitionKey();
        }    
    
        // 根据流程定义，获取该流程实例的结束节点    
        if (activityId.toUpperCase().equals("END")) {    
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {    
                List<PvmTransition> pvmTransitionList = activityImpl    
                        .getOutgoingTransitions();    
                if (pvmTransitionList.isEmpty()) {    
                    return activityImpl;    
                }    
            }    
        }    
    
        // 根据节点ID，获取对应的活动节点    
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);    
    
        return activityImpl;    
    }  
  
  
    /**  
     * 根据当前任务ID，查询可以驳回的任务节点  
     *   
     * @param taskId  
     *            当前任务ID  
     */    
    public List<ActivityImpl> findBackAvtivity(String taskId) throws Exception {    
        List<ActivityImpl> rtnList =  iteratorBackActivity(taskId, findActivitiImpl(taskId,    
                    null), new ArrayList<ActivityImpl>(),    
                    new ArrayList<ActivityImpl>());    
        return reverList(rtnList);    
    }  
  
    /**  
     * 查询指定任务节点的最新记录  
     *   
     * @param processInstance  
     *            流程实例  
     * @param activityId  
     * @return  
     */    
    private HistoricActivityInstance findHistoricUserTask(    
            ProcessInstance processInstance, String activityId) {    
        HistoricActivityInstance rtnVal = null;    
        // 查询当前流程实例审批结束的历史节点    
        List<HistoricActivityInstance> historicActivityInstances = historyService    
                .createHistoricActivityInstanceQuery().activityType("userTask")    
                .processInstanceId(processInstance.getId()).activityId(    
                        activityId).finished()    
                .orderByHistoricActivityInstanceEndTime().desc().list();    
        if (historicActivityInstances.size() > 0) {    
            rtnVal = historicActivityInstances.get(0);    
        }    
    
        return rtnVal;    
    }    
    
    /**  
     * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID  
     *   
     * @param activityImpl  
     *            当前节点  
     * @return  
     */    
    private String findParallelGatewayId(ActivityImpl activityImpl) {    
        List<PvmTransition> incomingTransitions = activityImpl    
                .getOutgoingTransitions();    
        for (PvmTransition pvmTransition : incomingTransitions) {    
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;    
            activityImpl = transitionImpl.getDestination();    
            String type = (String) activityImpl.getProperty("type");    
            if ("parallelGateway".equals(type)) {// 并行路线    
                String gatewayId = activityImpl.getId();    
                String gatewayType = gatewayId.substring(gatewayId    
                        .lastIndexOf("_") + 1);    
                if ("END".equals(gatewayType.toUpperCase())) {    
                    return gatewayId.substring(0, gatewayId.lastIndexOf("_"))    
                            + "_start";    
                }    
            }    
        }    
        return null;    
    }    
    
    /**  
     * 根据任务ID获取流程定义  
     *   
     * @param taskId  
     *            任务ID  
     * @return  
     * @throws Exception  
     */    
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(    
            String taskId) throws Exception {    
        // 取得流程定义    
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)    
                .getDeployedProcessDefinition(findTaskById(taskId)    
                        .getProcessDefinitionId());    
    
        if (processDefinition == null) {    
            throw new Exception("流程定义未找到!");    
        }    
    
        return processDefinition;    
    }    
    
    /**  
     * 根据任务ID获取对应的流程实例  
     *   
     * @param taskId  
     *            任务ID  
     * @return  
     * @throws Exception  
     */    
    public ProcessInstance findProcessInstanceByTaskId(String taskId)    
            throws Exception {    
        // 找到流程实例    
        ProcessInstance processInstance = runtimeService    
                .createProcessInstanceQuery().processInstanceId(    
                        findTaskById(taskId).getProcessInstanceId())    
                .singleResult();    
        if (processInstance == null) {     
            throw new Exception("流程实例未找到!");    
        }    
        return processInstance;    
    }    
    
     
    
    
    /**  
     * 根据流程实例ID和任务key值查询所有同级任务集合  
     *   
     * @param processInstanceId  
     * @param key  
     * @return  
     */    
    private List<Task> findTaskListByKey(String processInstanceId, String key) {    
        return taskService.createTaskQuery().processInstanceId(    
                processInstanceId).taskDefinitionKey(key).list();    
    }    
    
    
    /**  
     * 迭代循环流程树结构，查询当前节点可驳回的任务节点  
     *   
     * @param taskId  
     *            当前任务ID  
     * @param currActivity  
     *            当前活动节点  
     * @param rtnList  
     *            存储回退节点集合  
     * @param tempList  
     *            临时存储节点集合（存储一次迭代过程中的同级userTask节点）  
     * @return 回退节点集合  
     */    
    private List<ActivityImpl> iteratorBackActivity(String taskId,    
            ActivityImpl currActivity, List<ActivityImpl> rtnList,    
            List<ActivityImpl> tempList) throws Exception {    
        // 查询流程定义，生成流程树结构    
        ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);    
    
        // 当前节点的流入来源    
        List<PvmTransition> incomingTransitions = currActivity    
                .getIncomingTransitions();    
        // 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点    
        List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();    
        // 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点    
        List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();    
        // 遍历当前节点所有流入路径    
        for (PvmTransition pvmTransition : incomingTransitions) {    
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;    
            ActivityImpl activityImpl = transitionImpl.getSource();    
            String type = (String) activityImpl.getProperty("type");    
            /**  
             * 并行节点配置要求：<br>  
             * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)  
             */    
            if ("parallelGateway".equals(type)) {// 并行路线    
                String gatewayId = activityImpl.getId();    
                String gatewayType = gatewayId.substring(gatewayId    
                        .lastIndexOf("_") + 1);    
                if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归    
                    return rtnList;    
                } else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点    
                    parallelGateways.add(activityImpl);    
                }    
            } else if ("startEvent".equals(type)) {// 开始节点，停止递归    
                return rtnList;    
            } else if ("userTask".equals(type)) {// 用户任务    
                tempList.add(activityImpl);    
            } else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点    
                currActivity = transitionImpl.getSource();    
                exclusiveGateways.add(currActivity);    
            }    
        }    
    
        /**  
         * 迭代条件分支集合，查询对应的userTask节点  
         */    
        for (ActivityImpl activityImpl : exclusiveGateways) {    
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);    
        }    
    
        /**  
         * 迭代并行集合，查询对应的userTask节点  
         */    
        for (ActivityImpl activityImpl : parallelGateways) {    
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);    
        }    
    
        /**  
         * 根据同级userTask集合，过滤最近发生的节点  
         */    
        currActivity = filterNewestActivity(processInstance, tempList);    
        if (currActivity != null) {    
            // 查询当前节点的流向是否为并行终点，并获取并行起点ID    
            String id = findParallelGatewayId(currActivity);    
            if (StringUtils.isEmpty(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点    
                rtnList.add(currActivity);    
            } else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点    
                currActivity = findActivitiImpl(taskId, id);    
            }    
    
            // 清空本次迭代临时集合    
            tempList.clear();    
            // 执行下次迭代    
            iteratorBackActivity(taskId, currActivity, rtnList, tempList);    
        }    
        return rtnList;    
    }    
    
    
    /**  
     * 还原指定活动节点流向  
     *   
     * @param activityImpl  
     *            活动节点  
     * @param oriPvmTransitionList  
     *            原有节点流向集合  
     */    
    private void restoreTransition(ActivityImpl activityImpl,    
            List<PvmTransition> oriPvmTransitionList) {    
        // 清空现有流向    
        List<PvmTransition> pvmTransitionList = activityImpl    
                .getOutgoingTransitions();    
        pvmTransitionList.clear();    
        // 还原以前流向    
        for (PvmTransition pvmTransition : oriPvmTransitionList) {    
            pvmTransitionList.add(pvmTransition);    
        }    
    }    
    
    /**  
     * 反向排序list集合，便于驳回节点按顺序显示  
     *   
     * @param list  
     * @return  
     */    
    private List<ActivityImpl> reverList(List<ActivityImpl> list) {    
        List<ActivityImpl> rtnList = new ArrayList<ActivityImpl>();    
        // 由于迭代出现重复数据，排除重复    
        for (int i = list.size(); i > 0; i--) {    
            if (!rtnList.contains(list.get(i - 1)))    
                rtnList.add(list.get(i - 1));    
        }    
        return rtnList;    
    }    
    
    /**  
     * 转办流程  
     *   
     * @param taskId  
     *            当前任务节点ID  
     * @param userCode  
     *            被转办人Code  
     */    
    public void transferAssignee(String taskId, String userCode) {    
        taskService.setAssignee(taskId, userCode);    
    }    
    
    /**  
     * 流程转向操作  
     *   
     * @param taskId  
     *            当前任务ID  
     * @param activityId  
     *            目标节点任务ID  
     * @param variables  
     *            流程变量  
     * @throws Exception  
     */    
    private void turnTransition(String taskId, String activityId,    
            Map<String, Object> variables) throws Exception {    
        // 当前节点    
        ActivityImpl currActivity = findActivitiImpl(taskId, null);    
        // 清空当前流向    
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);    
    
        // 创建新流向    
        TransitionImpl newTransition = currActivity.createOutgoingTransition();    
        // 目标节点    
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);    
        // 设置新流向的目标节点    
        newTransition.setDestination(pointActivity);    
    
        // 执行转向任务    
        taskService.complete(taskId, variables);    
        // 删除目标节点新流入    
        pointActivity.getIncomingTransitions().remove(newTransition);    
    
        // 还原以前流向    
        restoreTransition(currActivity, oriPvmTransitionList);    
    }

	public String[] stopActTask(String taskId, String userId, String objId,String processInstanceId) {
		String[] reCode = {"success","终止成功"};
		try {
            // 取得当前任务
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
          //根据流程id查询代办任务中流程信息
            Task task = taskService.createTaskQuery().processInstanceId(currTask.getProcessInstanceId()).singleResult();
            if(task==null) {
    			reCode[0] = "false";
    			reCode[1] = "流程未启动或已执行完成，无法终止";
    			return reCode;
            }
            if (objId!=null) {
            	/*List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
        				.processInstanceBusinessKey(objId)
        				.orderByTaskCreateTime()
        				.asc()
        				.list();
        		String myTaskId = null;
        		HistoricTaskInstance myTask = null;
    			if(userId.equals(htiList.get(0).getAssignee())) {
    				myTaskId = htiList.get(0).getId();
    				myTask = htiList.get(0);
    			}*/
            	ActHiProcinst actHiProcinst = sysformconfigService.getActHiProcinstByPid(processInstanceId);
        		if(actHiProcinst!=null&&actHiProcinst.getStartUserId().equals(userId)) {
        			
        		}else{
        			reCode[0] = "false";
        			reCode[1] = "该任务非当前用户发起，无法终止";
        			return reCode;
        		}
			} else {
				reCode[0] = "false";
    			reCode[1] = "业务标识为空";
    			return reCode;
			}
            //取回流程接点 当前任务id 取回任务id
            callBackProcess(task.getId(),currTask.getId());
            //删除历史流程走向记录
            historyService.deleteHistoricTaskInstance(currTask.getId());
            historyService.deleteHistoricTaskInstance(task.getId());
            runtimeService.deleteProcessInstance(processInstanceId,"发起人终止");
            return reCode;
        } catch (Exception e) {
        	reCode[0] = "false";
			reCode[1] = "未知错误,请联系开发人员";
    		return reCode;
        }
	}

}