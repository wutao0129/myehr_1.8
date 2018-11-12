package com.myehr.controller.actfree;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import com.myehr.controller.flow.ActFreeParam;
import com.myehr.controller.flow.ActSaveDataParams;
import com.myehr.mapper.formmanage.form.SysActfreeConductMapper;
import com.myehr.mapper.formmanage.form.SysActfreeHisMapper;
import com.myehr.mapper.formmanage.form.SysActfreeModelMapper;
import com.myehr.mapper.formmanage.form.SysActfreeTaskMapper;
import com.myehr.mapper.formmanage.form.SysActfreeWayMapper;
import com.myehr.pojo.formmanage.cache.SysFormButtonCache;
import com.myehr.pojo.formmanage.cache.SysFormGeneralExecSqlCache;
import com.myehr.pojo.formmanage.cache.SysFormconfigCache;
import com.myehr.pojo.formmanage.form.ActFreeComent;
import com.myehr.pojo.formmanage.form.Announcement;
import com.myehr.pojo.formmanage.form.SysActfreeConduct;
import com.myehr.pojo.formmanage.form.SysActfreeConductExample;
import com.myehr.pojo.formmanage.form.SysActfreeHis;
import com.myehr.pojo.formmanage.form.SysActfreeHisExample;
import com.myehr.pojo.formmanage.form.SysActfreeHisExpand;
import com.myehr.pojo.formmanage.form.SysActfreeModel;
import com.myehr.pojo.formmanage.form.SysActfreeModelExample;
import com.myehr.pojo.formmanage.form.SysActfreeTask;
import com.myehr.pojo.formmanage.form.SysActfreeWay;
import com.myehr.pojo.formmanage.form.SysActfreeWayExample;
import com.myehr.service.formmanage.runtime.IRuntmeButtonService;
import com.myehr.service.impl.formmanage.form.SysformconfigService;
import com.myehr.service.impl.formmanage.runtime.RuntimeButtonServiceImpl;
import com.sun.xml.internal.ws.api.pipe.NextAction;

@Controller
@RequestMapping("/actfree")
public class ActFreeController {
  @Autowired
  private ActFreeService actfreeservice;
  @Autowired
  private SysActfreeConductMapper sysActfreeConductMapper;
  @Autowired
  private SysActfreeModelMapper sysActfreeModelMapper;
  @Autowired
  private SysActfreeWayMapper sysActfreeWayMapper;
  @Autowired
  private SysformconfigService sysformconfigService;
  @Autowired
  private SysActfreeHisMapper hisMapper;
  @Autowired
  private SysActfreeTaskMapper taskMapper;
  @Resource(name = "IRuntmeButtonService")
  private IRuntmeButtonService runtimeButtonServiceImpl;
  /**
   * 保存流程路线
   * @param request
   * @param actfreeway
   * @return
   * @throws Exception
   */
  @RequestMapping("/addactfreeway")
	 public int addAnnouncement(HttpServletRequest request,@RequestBody SysActfreeWay actfreeway) throws Exception {
         actfreeservice.addActFreeway(actfreeway);
	  return 1;
	}
  /**
   * 保存至流程进行表
   */
  @RequestMapping("/saveconduct")
	public  int saveDaiPi(HttpServletRequest request,@RequestBody SysActfreeConduct ActfreeConduct) throws Exception{
	     sysActfreeConductMapper.insert(ActfreeConduct);
		return 1;
		
	}
  /**
   * 流程实例回显
   */
  @RequestMapping("/selectActfreeModel")
	public @ResponseBody Map queryComboboxInfoParamsById(HttpServletRequest request) {
      SysActfreeModelExample example =new SysActfreeModelExample();
	  example.or();
	  List<SysActfreeModel> result=sysActfreeModelMapper.selectByExample(example);
	  Map map=new HashMap();
	  map.put("rows", result);
	  map.put("total", result.size());
		return map;
 }
  
  
    /**
     * 保存至流程进行表
     */
	@RequestMapping("/saveFreeWays")
	public @ResponseBody String[] saveFreeWays(HttpServletRequest request,@RequestBody List<SysActfreeWay> freeWays) throws Exception{
		String[] reCode = {"success",""};
		String userId = request.getSession().getAttribute("userid")+"";
		String taskId = request.getParameter("taskId");
		if(!freeWays.get(0).getAssigreeid().equals(userId)){
			reCode[0] = "error";
			reCode[1] = "非发起人不可修改流程路线";
			return reCode;
		}
		SysActfreeTask task = sysformconfigService.getActfreeTask(taskId);
		if (task!=null&&!task.getStatusa().equals("发起中")) {
			reCode[0] = "error";
			reCode[1] = "流程已发起,不可修改流程路线";
			return reCode;
		}else if(task==null){
			reCode[0] = "error";
			reCode[1] = "非法流程,请检查流程ID";
			return reCode;
		}
		for (SysActfreeWay sysActfreeWay : freeWays) {
			if (sysActfreeWay.getId()!=null) {
				sysActfreeWayMapper.updateByPrimaryKey(sysActfreeWay);
			} else {
				sysActfreeWayMapper.insert(sysActfreeWay);
			}
		}
		if (freeWays.size()>0) {
			sysformconfigService.setFreeWaysByTaskId(freeWays.get(0).getTaskid());
		}
		return reCode;
	}
	
    /**
     * 获取流程路线
     */
	@RequestMapping("/getFreeWays")
	public @ResponseBody List<SysActfreeWay> getFreeWays(HttpServletRequest request) throws Exception{
		String taskId = request.getParameter("taskId");
		String userId = request.getSession().getAttribute("userid")+"";
		List<SysActfreeWay> waysx = new ArrayList<SysActfreeWay>();
		SysActfreeWay way = sysformconfigService.getNowFreeWayByTOId(taskId, "0");
		List<SysActfreeWay> ways = sysformconfigService.getFreeWaysByTaskId(taskId);
		if (way==null) {
			way = new SysActfreeWay();
			way.setAssigreeid(userId);
			way.setTaskid(taskId);
			way.setOrderby("0");
			waysx.add(way);
			for (SysActfreeWay sysActfreeWay : ways) {
				waysx.add(sysActfreeWay);
			}
			return waysx;
		}
		return ways;
	}
	
	/*
	 * getFreeHisComs获取审批历史
	 */
	@RequestMapping("/getFreeHisComs")
	public @ResponseBody List<SysActfreeHisExpand> getFreeHisComs(HttpServletRequest request) throws Exception{
		String taskId = request.getParameter("taskId");
		SysActfreeHisExample example = new SysActfreeHisExample();
		example.or().andTaskidEqualTo(taskId);
		example.setOrderByClause("ENDTIME");
		List<SysActfreeHis> his = hisMapper.selectByExample(example);
		List<SysActfreeHisExpand> hisx = new ArrayList<SysActfreeHisExpand>();
		for (SysActfreeHis sysActfreeHis : his) {
			SysActfreeHisExpand expand = new SysActfreeHisExpand();
			String cname = "";
			try {
				cname = sysformconfigService.getUserByUserid(sysActfreeHis.getAssigree()).getUserName();
			} catch (Exception e) {
				// TODO: handle exception
			}
			expand.setFreeHis(sysActfreeHis);
			expand.setCname(cname);
			SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String time = format0.format(sysActfreeHis.getEndtime());
			expand.setEndTime(time);
			hisx.add(expand);
		}
		return hisx;
	}
	
	
	/*
	 * 发起流程
	 */
	@RequestMapping("/createFreeAct")
	public @ResponseBody SysActfreeTask createFreeAct(HttpServletRequest request) throws Exception{
		String taskId = UUID.randomUUID().toString().replaceAll("-", "");
		String modelkey = request.getParameter("modelKey");
		String bussinessId = request.getParameter("bussinessId");//bussinessId
		String userId = request.getSession().getAttribute("userid")+"";
		//创建一条流程数据
		SysActfreeTask task = new SysActfreeTask();
		task.setActcode(bussinessId);
		task.setTaskid(taskId);
		task.setCratename(userId);
		task.setCratetime(new Date());
		task.setModelkey(modelkey);
		task.setStatusa("发起中");
		taskMapper.insert(task);
		sysformconfigService.setActfreeTask(taskId);
		return task;
	}
	
	/*
	 * 提交流程
	 */
	@RequestMapping("/start")
	public @ResponseBody String[] start(HttpServletRequest request) throws Exception{
		
		
		SysActfreeHis his = new SysActfreeHis();
		his.setStarttime(new Date());
		his.setComent("");
		String[] reCode = {"error","0"};
		String taskId = request.getParameter("taskId");
		String title = request.getParameter("title");
		String orderBy = request.getParameter("orderBy");
		String userId = request.getSession().getAttribute("userid")+"";
		HttpSession session = request.getSession();
		//runtimeButtonServiceImpl.execHzsql(paramMap, res, formId, buttonId, buttonType, session, sourceType)
		//reCode = runtimeButtonServiceImpl.execQzsql(param.getParamsMap(), param.getFormId(), param.getButtonId(), "", session, "ORACLE");
		if (userId==null||userId.equals("null")) {
			reCode[1] = "当前用户不存在";
			return reCode;
		}
		//待发表创建一条数据
		SysActfreeConduct conduct = new SysActfreeConduct();
		conduct.setTaskid(taskId);
		SysActfreeWay nextWay = sysformconfigService.getNextFreeWayByTOId(taskId, orderBy);//发起后获取下个审批人
		if (nextWay==null) {
			reCode[1] = "下个用户不存在";
			return reCode;
		}
		conduct.setAssignee(nextWay.getAssigreeid());
		conduct.setOrderby(nextWay.getOrderby());
		conduct.setOvertime(new Date());
		SysActfreeConductExample example = new SysActfreeConductExample();
		example.or().andTaskidEqualTo(taskId);
		SysActfreeConduct conductx = sysformconfigService.getActFreeConductByTaskId(taskId);
		if (conductx==null) {
			sysActfreeConductMapper.insert(conduct);
			sysformconfigService.setActFreeConductByTaskId(taskId);
		} else {
			reCode[1] = "禁止重复提交";
			return reCode;
		}
		
		//存入流程历史
		SysActfreeHisExample hisExample = new SysActfreeHisExample();
		hisExample.or().andAssigreeEqualTo(userId).andTaskidEqualTo(taskId);
		his.setNextassigree(nextWay.getAssigreeid());
		his.setResult("发起");
		his.setTaskid(taskId);
		his.setAssigree(userId);
		his.setEndtime(new Date());
		hisMapper.insert(his);
		
		//修改流程状态
		SysActfreeTask task = taskMapper.selectByPrimaryKey(taskId);
		task.setStatusa("审批中");
		task.setTitle(title);
		taskMapper.updateByPrimaryKey(task);
		sysformconfigService.setActfreeTask(taskId);
		//reCode = runtimeButtonServiceImpl.execQzsql(param.getParamsMap(), param.getFormId(), param.getButtonId(), "", session, "ORACLE");
		return reCode;
	}
	
	/*
	 * 审批流程
	 */
	@RequestMapping("/complete")
	public @ResponseBody String[] complete(HttpServletRequest request,@RequestBody ActFreeParam param) throws Exception{
		String[] reCode = {"error","0"};
		String[] reCode2 = {"000000","执行成功"};
		Map map = param.getParamsMap();
		map.put("a_taskId", param.getTaskId());
		HttpSession session = request.getSession();
		reCode = runtimeButtonServiceImpl.execQzsql(param.getParamsMap(), param.getFormId(), param.getButtonId(), "", session, "ORACLE");
		SysActfreeHis his = new SysActfreeHis();
		String taskId = param.getTaskId();
		String orderBy = param.getOrderBy();
		String userId = request.getSession().getAttribute("userid")+"";
		if (userId==null||userId.equals("null")) {
			reCode[1] = "当前用户不存在";
			return reCode;
		}
		//待发表更新
		SysActfreeConductExample conductExample = new SysActfreeConductExample();
		conductExample.or().andTaskidEqualTo(taskId);
		SysActfreeConduct conduct = new SysActfreeConduct();
		if (sysActfreeConductMapper.selectByExample(conductExample).size()>0) {
			conduct = sysActfreeConductMapper.selectByExample(conductExample).get(0);//获取待批流程状态
		} else {
			reCode[1] = "当前流程不再进行状态";
			return reCode;
		}
		his.setStarttime(conduct.getOvertime());//更新历史任务时间

		if(param.getAction()!=null&&param.getAction().equals("approve")){//通过
			SysActfreeWay nextWay = sysformconfigService.getNextFreeWayByTOId(taskId, orderBy);//发起后获取下个审批人
			if (nextWay==null) {
				List<SysActfreeWay> ways = sysformconfigService.getFreeWaysByTaskId(taskId);
				SysActfreeWay nextWayx = ways.get(ways.size()-1);
				if (nextWayx.getOrderby().equals(orderBy)) {
					reCode[0] = "success";
					reCode[1] = "流程结束";
					his.setResult("通过");
//					sysActfreeConductMapper.deleteByExample(conductExample);
					sysformconfigService.setActFreeConductByTaskId(taskId);
					//存入流程历史
					his.setTaskid(taskId);
					his.setAssigree(userId);
					his.setEndtime(new Date());
					his.setComent(param.getContent());
					hisMapper.insert(his);
					//修改流程状态
					SysActfreeTask task = taskMapper.selectByPrimaryKey(taskId);
					task.setStatusa("审批结束,流程完成");
					taskMapper.updateByPrimaryKey(task);
					sysformconfigService.setActfreeTask(taskId);
					sysActfreeConductMapper.deleteByExample(conductExample);
					reCode2 = runtimeButtonServiceImpl.execHzsql(param.getParamsMap(),reCode, param.getFormId(), param.getButtonId(), "", session, "ORACLE");
					if (reCode2[0].equals("000000")||reCode2[0].equals("0")) {
						return reCode;
					} else {
						reCode2[0] = "error";
						return reCode2;
					}
				} else {
					reCode[1] = "下个用户不存在";
					return reCode;
				}
			}
			conduct.setAssignee(nextWay.getAssigreeid());
			conduct.setOrderby(nextWay.getOrderby());
			his.setResult("通过");
			his.setNextassigree(nextWay.getAssigreeid());
		}else{//驳回
			String toP = param.getDetail().getIsToCreator();
			if (toP.equals("true")) {//驳回发起人
				SysActfreeHisExample hisExamplex = new SysActfreeHisExample();
				hisExamplex.or().andTaskidEqualTo(taskId).andResultEqualTo("发起");
				String nextUser = hisMapper.selectByExample(hisExamplex).get(0).getAssigree();
				conduct.setAssignee(nextUser);
				conduct.setOrderby("0");
			}else {
				String nextOrder = param.getDetail().getNextOrder();
				SysActfreeWay nextWay = sysformconfigService.getNowFreeWayByTOId(taskId, nextOrder);//发起后获取下个审批人
				conduct.setAssignee(nextWay.getAssigreeid());
				conduct.setOrderby(nextOrder);
				his.setResult("驳回");
				his.setNextassigree(nextWay.getAssigreeid());
			}
		}
		conduct.setOvertime(new Date());
		sysActfreeConductMapper.updateByPrimaryKey(conduct);
		sysformconfigService.setActFreeConductByTaskId(taskId);//刷新当前流程状态
		sysformconfigService.setActfreeTask(taskId);//刷新此流程状态
		//存入流程历史
		his.setTaskid(taskId);
		his.setAssigree(userId);
		his.setEndtime(new Date());
		his.setComent(param.getContent());
		hisMapper.insert(his);
		reCode[0] = "success";
		reCode[1] = "操作成功";
		reCode2 = runtimeButtonServiceImpl.execQzsql(param.getParamsMap(), param.getFormId(), param.getButtonId(), "", session, "ORACLE");
		if (reCode2[0].equals("000000")||reCode2[0].equals("0")) {
			return reCode;
		} else {
			reCode2[0] = "error";
			return reCode2;
		}
	}
	/**
	   * 增加流程实例
	   */
	  @RequestMapping("/saveActfreeModel")
		public @ResponseBody String[] saveComboboxInfoParamsById(HttpServletRequest request,@RequestBody SysActfreeModel sysActfreeModel) {
		  String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
		  sysActfreeModelMapper.insert(sysActfreeModel);
		  return recode;
	  }
	  /**
	   * 流程实例修改卡片回显
	   */
	  @RequestMapping("/seeActfreeModelEdit")
		public @ResponseBody  SysActfreeModel seeActfreeModelById(HttpServletRequest request) throws Exception {
				String modelid=request.getParameter("modelid");
				//int id1=Integer.parseInt(id);
				SysActfreeModel actfree = sysActfreeModelMapper.selectByPrimaryKey(modelid);
				/*String sql="SELECT  FORM_DEF_NAME FROM  SYS_FORMCONFIG WHERE FORM_DEF_ID="+actfree.getFormid();
				Map formName=MybatisUtil.queryOne("runtime.selectSql", sql);
				actfree.setFormid(formName.get("FORM_DEF_NAME")+"");*/
			   return actfree;
		}
	  /**
	   * 流程实例修改卡片修改数据
	   */
	  @RequestMapping("/updateActfreeModel")
	  public @ResponseBody String[] updateActfreeModel(HttpServletRequest request,@RequestBody SysActfreeModel sysActfreeModel) throws Exception {
		  String[] recode = new String[2];
			recode[0]="0";
			recode[1]="操作成功";
		  String modelid=request.getParameter("modelid");
			//int id1=Integer.parseInt(id);
		      sysActfreeModelMapper.updateByPrimaryKeySelective(sysActfreeModel);
		return recode;
	}
  /**
   * 流程实例删除
   */
	@RequestMapping("/deletActfreeModelById")
    public@ResponseBody String[]  deleteActfreeModelById(HttpServletRequest request) throws Exception {
		String[] recode = new String[2];
		recode[0]="0";
		recode[1]="操作成功";  
		String id=request.getParameter("modelid");
		sysActfreeModelMapper.deleteByPrimaryKey(id);
		return recode;
	}
		
	//getActByTaskId	
	/**
	   * 流程实例删除
	   */
	@RequestMapping("/getActByTaskId")
    public @ResponseBody ActFreeComent getActByTaskId(HttpServletRequest request) throws Exception {
		String taskId=request.getParameter("taskId");
		ActFreeComent coment = actfreeservice.getComentsByTaskId(taskId);
		return coment;
	}
}
