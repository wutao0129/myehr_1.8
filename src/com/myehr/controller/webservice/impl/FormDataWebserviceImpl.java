package com.myehr.controller.webservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.myehr.common.mybatis.MybatisUtil;
import com.myehr.common.util.datasource.CustomerContextHolder;
import com.myehr.controller.webservice.service.FormDataWebservice;
import com.myehr.mapper.formmanage.form.SysInterfaceSchemeMapper;
import com.myehr.pojo.act.Act;
import com.myehr.pojo.formmanage.form.SysInterfaceSchemeWithBLOBs;
import com.myehr.service.flow.ActTaskService;


@XmlSeeAlso({java.util.HashMap.class})
@WebService(endpointInterface="com.myehr.controller.webservice.service.FormDataWebservice")//指定webservice所实现的接口以及服务名称
public class FormDataWebserviceImpl implements FormDataWebservice{
	private static Logger logger = LoggerFactory.getLogger(FormDataWebserviceImpl.class);
	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private SysInterfaceSchemeMapper sysInterfaceSchemeMapper;

	@Override  
    public String getInterfaceSchemeDatas(String interfaceSchemeId,String contractid,String contractType) throws NumberFormatException, Exception {
		CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_oracle);
		String interfaceSchemeSql = "select * from okc_h_v "+
				" where okc_h_v.okc_header_id = '"+contractid+"'";
		List<Map> datas = null;
		try {
			datas = MybatisUtil.queryList("runtime.selectSql", interfaceSchemeSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			 return "{'msg':'error','sql':'"+interfaceSchemeSql+"'}";
		}
		CustomerContextHolder.setContextType(CustomerContextHolder.session_factory_sqlserver);
		String userId = "1";
		Map leave =datas.get(0);
		Act act = new Act(); 
		act.setProcDefKey("contractsigningcompany");
		act.setBusinessId(contractid);
		act.setBusinessTable(datas.get(0).get("CONTRACT_NUMBER")+"");
		Map<String, Object> vars = Maps.newHashMap();
		leave.put("userId", userId);
		for (Object key : leave.keySet()) {
			vars.put("OKCHV_"+key+"", leave.get(key));	
		}
		vars.put("title", "系统管理员"+":"+datas.get(0).get("CONTRACT_NUMBER")+"");
		act.setVars(vars);
		
		String id = actTaskService.startProcess(vars,leave,act.getProcDefKey(), act.getBusinessTable(), act.getBusinessId(), act.getTitle(),userId);
		
		actTaskService.initTask(userId,act,Integer.parseInt(contractid),id);
		
        return "{'msg':'success','contractid':'"+contractid+"','pronInst':'"+id+"'}";
    }
	
	@Override  
    public String getInterfaceDatasByScheme(String interfaceSchemeId,String pageNo,String pagesize) {
		SysInterfaceSchemeWithBLOBs interfaceScheme = sysInterfaceSchemeMapper.selectByPrimaryKey(Long.parseLong(interfaceSchemeId));
		
		String sql = interfaceScheme.getInterfaceSchemeDefSql();
		
		String isPager = interfaceScheme.getInterfaceSchemePager();
		List<Map> datas = null;
		try {
			datas = MybatisUtil.queryList("runtime.selectSql", sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error(e.getMessage(),e);
			 return "{'msg':'01','sql':'"+sql+"'}";
		}
		List<Map> datas1 = new ArrayList<Map>();
		if (isPager!=null && isPager.equals("Y")) {
			int start = 0;
			if (!pageNo.equals("0")) {
				start = (Integer.parseInt(pageNo)-1)*Integer.parseInt(pagesize);
			}
			
			for (int i = 0; i < datas.size(); i++) {
				datas1.add(datas.get(i));
			}
		}else {
			datas1 = datas;
		}
		String jsonDatas = JSON.toJSONString(datas1);
		
        return "{'msg':'00',count:'"+datas1.size()+"','datas':'"+jsonDatas+"'}";
    }
    
}