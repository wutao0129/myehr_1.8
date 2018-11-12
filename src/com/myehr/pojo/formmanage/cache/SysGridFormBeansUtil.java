package com.myehr.pojo.formmanage.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.visitor.functions.If;
import com.myehr.common.exception.DcfException;
import com.myehr.common.util.ChangeCode;
import com.myehr.common.util.DealStrSub;
import com.myehr.common.util.SpringContextUtils;
import com.myehr.pojo.dict.DictData;
import com.myehr.pojo.formmanage.form.FormChartsBarColumn;
import com.myehr.pojo.formmanage.form.SysFormCharts;
import com.myehr.pojo.formmanage.form.SysFormGeneralParam;
import com.myehr.pojo.formmanage.form.SysFormconfigWithBLOBs;
import com.myehr.pojo.formmanage.template.gridbycard.SysGridbycardTemplateColumn;
import com.myehr.pojo.formmanage.template.timeaxis.SysTimeaxisTemplateColumn;
import com.myehr.pojo.sysParam.SysRequestParam;
import com.myehr.service.formmanage.form.ISysformconfigService;
import com.myehr.service.formmanage.form.generalparam.ISysFormGeneralParamService;

public class SysGridFormBeansUtil implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(SysGridFormBeansUtil.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int HTML_SILZE = 12;
	
	public static final String RENDERER_NAME = "ON_RENDERER";
		
	public static final String FORM_EXEC_URL = "/form/toForm.action";
	
	public static final String LOOK_BIZ_BACK_TYPE_FUNNAME_START = "getLookupBackVal";
	
	public static final String LOOK_SQL_BACK_TYPE_FUNNAME_START = "getLookupSQLBackVal";
	
	public static final String TEXT_BOX_LINK_CLICK_FUNNAME = "goTo";
	
	public static final String CALCULATE_EXC_URL = "com.dcf.system.scheme.service.impl.excSolution.flow";
	
	public static final String INTRODUCE_URL = "com.dcf.form.runtime.gridformButton.introduceDatas.biz.ext";
	
	
	
	/**
	 * 卡片式表单 初始化数据url
	 */
	public static final String GRID_FORM_INIT_DATA_URL = "com.dcf.form.runtime.cardFormButton.cardformInitData.biz.ext";
	
	//列表表单默认删除数据逻辑
	public static final String GRID_REMOVE_BUTTON_DEFAULT_URL = "/myehr/form/removeData.action";
	
//	列表表单默认保存数据逻辑
	public static final String GRID_SAVE_BUTTON_DEFAULT_URL = "com.dcf.form.runtime.gridformButton.saveDatas.biz.ext";

	/**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }
    public static String isDateTime(List<SysFormColumnCache> columns,String columnId){
    	
        for (SysFormColumnCache sysformcolumncache : columns) {
			if (sysformcolumncache.getPojo().getFormColumnId().toString().equals(columnId)&&sysformcolumncache.getPojo().getFormColumnGuiType().equals("6")) {
				SysDatepickerCache datepicker = (SysDatepickerCache)sysformcolumncache.getFormColumGui();
				return datepicker.getDatepickerFormat();
			}else if (sysformcolumncache.getPojo().getFormColumnId().toString().equals(columnId)&&sysformcolumncache.getPojo().getFormColumnGuiType().equals("2")) {
				SysFormComboboxCache comboboxCache = (SysFormComboboxCache)sysformcolumncache.getFormColumGui();
				return comboboxCache.getComboboxGuiInitType()+"|"+comboboxCache.getComboboxGuiInitValue();
			}
		}
        return "false";
    }
    public static String checkDateType(List<SysFormColumnCache> columns,String columnId){
        for (SysFormColumnCache sysformcolumncache : columns) {
			if (sysformcolumncache.getPojo().getFormColumnId().toString().equals(columnId)&&sysformcolumncache.getPojo().getFormColumnGuiType().equals("6")) {
				SysDatepickerCache datepicker = (SysDatepickerCache)sysformcolumncache.getFormColumGui();
				return datepicker.getDatepickerFormat();
			}else if (sysformcolumncache.getPojo().getFormColumnId().toString().equals(columnId)&&sysformcolumncache.getPojo().getFormColumnGuiType().equals("2")) {
				SysFormComboboxCache comboboxCache = (SysFormComboboxCache)sysformcolumncache.getFormColumGui();
				String xxx = getDictValues(comboboxCache)+"|"+comboboxCache.getComboboxGuiInitType()+"_"+comboboxCache.getComboboxGuiInitValue();
				return xxx;
			}
		}
        return "false";
    }
    
    public static SysFormColumnCache getColumnById(List<SysFormColumnCache> columns,String columnId){
        for (SysFormColumnCache sysformcolumncache : columns) {
			if (sysformcolumncache.getFormColumnId()==Long.valueOf(columnId)) {
				return sysformcolumncache;
			}
		}
        return null;
    }
	
	public static String getTextBoxHtml(String classType, String jsId, String fieldName, String vtype,String othervtype, String emptyText, boolean isRequired,
			boolean isValidation, boolean isDisable, String entityName, String width,String lableName, boolean isRender,String formDefCode, boolean readonly, String align,String checkSelf,String height,String showtypeString,String colorCondition,String color,SysTextBoxCache textBox,String isApp,String columnPosition) {
		// TODO 自动生成方法存根
		String requiredVtypeHtml = isRequired?";required":"";
		String vtypeHtml =  "";
		if("10".equals(vtype)){
			vtype = checkSelf==null?"":checkSelf;
		}
		if(vtype!=null&&!"".equals(vtype)){
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = vtype+";"+othervtype;
			}
		}else {
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = othervtype;
			}
		}
		vtypeHtml = (isValidation)? " vtype=\""+vtype+requiredVtypeHtml+"\" ":(!"".equals(requiredVtypeHtml)?"vtype=\""+requiredVtypeHtml+"\")":"");
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String widthHtml =  width!=null? " width=\""+width+"\" ":"";
		String heightHtml =  height!=null? " height=\""+height+"\" ":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = align==null?"":"align=\""+align+"\"";
		String temp = "";
		if (isApp!=null&&isApp.equals("APP")) {
			//textbox.getGeneralParams().get("COLUMN_LINK")
			if (textBox.getPojo().getTextboxIsLink()!=null&&textBox.getPojo().getTextboxIsLink().equals("Y")) {
				temp = "<a  href=\"toForm.action?isInit=true&formType=APP&formId="+textBox.getPojo().getTextboxLinkForm()+"\">\n" +
					   "	<div class=\""+columnPosition+"\" v-if=\"item."+fieldName.toUpperCase()+"\">{{item."+fieldName.toUpperCase()+"}}</div>\n" +
					   "</a>\n";
			} else {
				temp = "	<div class=\""+columnPosition+"\" v-if=\"item."+fieldName.toUpperCase()+"\">{{item."+fieldName.toUpperCase()+"}}</div>\n";
			}
			
		}/*else if (isApp!=null&&isApp.equals("CARDANDCARD")) {
			temp = getCardAndCardColumnProperty(fieldName,lableName,vtypeHtml,width,rendereHtml,align,"",showtypeString,colorCondition,color,textBox,"TEXTBOX");
		} */else {
			if (classType.equals("richText")) {
				temp = getGridColumnProperty(fieldName,lableName,vtypeHtml,width,rendereHtml,align,"",showtypeString,colorCondition,color,textBox,classType);
			} else {
				temp = getGridColumnProperty(fieldName,lableName,vtypeHtml,width,rendereHtml,align,"",showtypeString,colorCondition,color,textBox,"");
			}
			
		}
		/*if(!readonly){
			temp+= SysCardFormBeansUtil.getNbsp(4)+"<input property=\"editor\" class=\""+classType+"\""+emptyTextHtml+widthHtml+heightHtml+" />"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		temp+= SysCardFormBeansUtil.getNbsp(3)+"</div>"+SysCardFormBeansUtil.NEW_LINE_ONLY;*/
		return temp;
	} 
	
	public static String getTextBoxHtmlForGrid(String classType, String jsId, String fieldName, String vtype,String othervtype, String emptyText, boolean isRequired,
			boolean isValidation, boolean isDisable, String entityName, String width,String lableName, boolean isRender,String formDefCode, boolean readonly, String align,String checkSelf,String height,String showtypeString,String isEdit,String color,SysTextBoxCache textBox,String isApp,String columnPosition) {
		// TODO 自动生成方法存根
		String requiredVtypeHtml = isRequired?";required":"";
		String vtypeHtml =  "";
		if("10".equals(vtype)){
			vtype = checkSelf==null?"":checkSelf;
		}
		if(vtype!=null&&!"".equals(vtype)){
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = vtype+";"+othervtype;
			}
		}else {
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = othervtype;
			}
		}
		vtypeHtml = (isValidation)? " vtype=\""+vtype+requiredVtypeHtml+"\" ":(!"".equals(requiredVtypeHtml)?"vtype=\""+requiredVtypeHtml+"\")":"");
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String widthHtml =  width!=null? " width=\""+width+"\" ":"";
		String heightHtml =  height!=null? " height=\""+height+"\" ":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = align==null?"":"align=\""+align+"\"";
		String temp = "";
		temp = getGridColumnPropertyForGrid(fieldName,lableName,vtypeHtml,width,rendereHtml,align,"",showtypeString,isEdit,color,textBox,"");
		return temp;
	} 
	
	//getTextLinkAlertJs悬浮弹出JS
	public static StringBuffer getTextLinkAlertJs(SysTextBoxCache textbox){
		String params = "";
		String url = "";
		StringBuffer temp = new StringBuffer();
		if(textbox.getPojo()!=null&&textbox.getPojo().getTextboxId()!=null&&!"0".equals(textbox.getPojo().getTextboxId())){
			if(textbox.getPojo().getTextboxIsLink()!=null){
				if(textbox.getPojo().getTextboxIsLink().equals("Y")){
					if(textbox.getPojo().getTextboxLinkIsForm().equals("Y")){
						List<SysFormGeneralParamCache> param = textbox.getGeneralParams().get("COLUMN_LINK");
						String paramStr = "";
						if (param!=null) {
							for (int i = 0; i < param.size(); i++) {
								paramStr += param.get(i).getPojo().getParamName().toUpperCase()+"='+"+param.get(i).getPojo().getParamFromValue()+"+'&";
								params += param.get(i).getPojo().getParamFromValue()+",";
							}
							params = params.substring(0,params.length()-1);
						}
						if (textbox.getPojo().getTextboxLinkWinOpenType().equals("4")) {
							url = "/myehr/form/toForm.action?formId="+textbox.getPojo().getTextboxLinkForm()+"&isInit=true&"+paramStr;
						} else if(textbox.getPojo().getTextboxLinkWinOpenType().equals("3")){
							url = "/myehr/form/toForm.action?formId="+textbox.getPojo().getTextboxLinkForm()+"&"+paramStr;
						}
						temp.append("/**mouseover触发*/\n");
						temp.append("var xuannum = 0;\n");
						temp.append("var xuanflag = false;\n");
						temp.append("function openNew("+params+"){\n"	);
						temp.append("trigger = setTimeout(function(){\n");
						temp.append("	if(xuanflag){\n");						temp.append("	closeNew();\n");
						//temp.append("	this.$loading = $('<div id=\"open\"  onmouseleave=\"changNum()\" style=\"position:absolute;top:5%;right:0;width:"+textbox.getPojo().getTextboxLinkWinWidth()+"%;height:"+textbox.getPojo().getTextboxLinkWinHeight()+"%;z-index:1000\"><iframe style=\"width:100%;height:100%;border:1px solid #ffcc00;\" src=\""+url+"\"></iframe></div>');\n");
						temp.append("	this.$loading = $('<div id=\"open\"  onmouseleave=\"changNum()\" style=\"position:absolute;top:5%;right:0;width:"+textbox.getPojo().getTextboxLinkWinWidth()+"%;height:"+textbox.getPojo().getTextboxLinkWinHeight()+"%;z-index:1000\">'+\n");
						temp.append("				  	  '<div class=\"layui-layer-title\" style=\"cursor: move;border-radius:6px 6px 0 0\">"+textbox.getPojo().getTextboxLinkWinTitle()+"</div>'+\n");
						temp.append("				      '<span class=\"layui-layer-setwin\" onclick=\"closeOpenNew()\">'+\n");
						temp.append("				      '<a class=\"layui-layer-ico layui-layer-close layui-layer-close1\" href=\"javascript:;\"></a>'+\n");
						temp.append("				      '</span>'+\n");
						temp.append("				      '<iframe style=\"width:100%;height:100%;border:1px solid #ffcc00;\" src=\""+url+"\"></iframe></div>');\n");
						temp.append("    if(xuannum==0){\n");
						temp.append("    	$('body').append(this.$loading);\n");
						temp.append("    	xuannum++;\n");
						temp.append("    }	\n");
						temp.append("    }	\n");
						temp.append("},1000); \n");
						temp.append("}\n");
						temp.append("function closeNew(){\n");
						temp.append("	if(xuannum==1){\n");
						temp.append("		$(\"#open\").remove();\n");
						temp.append("		xuannum--;\n");
						temp.append("	}\n");
						temp.append("}\n");
						temp.append("function closeOpenNew(){\n");
						temp.append("	$(\"#open\").remove();\n");
						temp.append("}\n");
						temp.append("function changNum(){\n");
						temp.append("	$(\"#open\").remove();\n");
						temp.append("	xuannum--;	\n");
						temp.append("}\n");					
					}else {
						
					}
				}
			}
		}
		return temp;
	}
	/*public static String getComboboxHtml(String classType, String jsId, String fieldName, String vtype, String emptyText, boolean isRequired,
			boolean isValidation, boolean isDisable, String entityName, String formColumnWidth, String lableName, boolean isRender, String formDefCode,
			boolean readonly, String formColumnAlign, String comboboxCheckSelf,boolean showNullItem,String textField,String valueField,boolean allowInput,
			String dictTypeId,String nullItemText,String url,String data,String dataField,boolean multiselect,String showtypeString,String isApp,String columnPosition,String formColumnId) {
		// TODO 自动生成方法存根
		fieldName = fieldName.toUpperCase();
		String requiredVtypeHtml = isRequired?";required":"";
		String vtypeHtml =  "";
		if("10".equals(vtype)){
			vtype = comboboxCheckSelf==null?"":comboboxCheckSelf;
		}
		String widthHtml =  formColumnWidth!=null? " width=\""+formColumnWidth+"\" ":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = formColumnAlign==null?"":"align=\""+formColumnAlign+"\"";
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String showNullItemHtml = showNullItem==true?" showNullItem=\"true\"":"";
		
		String textFieldHtml = textField!=null?" textField=\""+textField+"\"":"";
		String valueFieldHtml = valueField!=null?" valueField=\""+valueField+"\"":"";
		String allowInputHtml = allowInput==true?" allowInput=\"true\"":"";
		String dictTypeIdHtml = dictTypeId!=null?" dictTypeId=\""+dictTypeId+"\"":"";
		String nullItemTextHtml = nullItemText!=null?" nullItemText=\""+nullItemText+"\"":"";
		String urlHtml = url!=null?" url=\""+url+"\"":"";
		String dataHtml = data!=null?" data=\""+data+"\"":"";
		//String dataFieldHtml = dataField!=null?" dataField=\""+dataField+"\"":"";
		String dataFieldHtml = dataField!=null?" "+dataField+"_form_"+formColumnId+"":"";
		String multiselectHtml = multiselect==true?" multiSelect=\"true\"":"";
		
		vtypeHtml = (isValidation)? " vtype=\""+vtype+requiredVtypeHtml+"\" ":(!"".equals(requiredVtypeHtml)?"vtype=\""+requiredVtypeHtml+"\")":"");
		String temp = "";
		if (isApp!=null&&isApp.equals("APP")) {
			temp = "<div class=\""+columnPosition+"\" v-if=\"item."+fieldName+"\">{{item."+fieldName+"}}</div>\n";
		}else {
			StringBuffer js = new StringBuffer();
			String width = "100";
			if(!formColumnWidth.equals("")&&formColumnWidth!=null){
				width = formColumnWidth.substring(formColumnWidth.length()-2, formColumnWidth.length());
			}
			js.append("            {"+"\n"
			+"                title : '"+lableName+"',"+"\n"
			+"                field : '"+fieldName+"',"+"\n"
			+"                align : 'center',"+"\n"
			+"                "+showtypeString+","+"\n"
			+"                valign : '"+formColumnAlign+"',"+"\n"
			+"                formatter: function (value, row, index) {"+"\n");
			if(multiselect){
						js.append("           var value1 = '';"+"\n"
						+"					  if((value!=undefined&&value!=\"\"&&value!=null&&"+dictTypeId+"!=undefined&&"+dictTypeId+"!=null)||value==0){\n"
						+"					  value = value+'';\n" +
						"					  var val = value.split(\",\");\n" +
						"					  for(var i=0;i<val.length;i++){\n" +
						"					  	  if(i==0){\n" +
						"						  	  value1 = "+dictTypeId+"[val[i]];\n" +
						"						  }else{\n" +
						"							  value1 += ','+"+dictTypeId+"[val[i]];\n" +
						"						  }\n" +
						"					  }\n"
						+"					  }\n"
						+"                    if((value1==undefined || value1==\"\")&&value1!=0){"+"\n"
						+"                        value1='-';"+"\n"
						+"                    }"+"\n");
			}else {
						js.append("           var value1 = '';"+"\n"
						+"					  if((value!=undefined&&value!=\"\"&&value!=null&&"+dictTypeId+"!=undefined&&"+dictTypeId+"!=null)||value==0){\n"
						+"							value1 = "+dictTypeId+"[value];\n"	
						+"					  }\n"
						+"                    if((value1==undefined || value1==\"\")&&value1!=0){"+"\n"
						+"                        value1='-';"+"\n"
						+"                    }"+"\n"
						+"                    if((value==undefined || value==\"\")&&value!=0){"+"\n"
						+"                        value='-';"+"\n"
						+"                    }"+"\n"
						+"                    value = (value+'').replace(/ /g,'');"+"\n");
			}
			js.append("                    return "+"\""+"<a class=\\\"eli w"+width+"\\\" style=\\\"font-size:12px;text-decoration:none;color:black;width:"+formColumnWidth+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" data-value="+"\\"+"\""+"\""+"+value1+"+"\""+"\\"+"\" data-title="+"\\"+"\""+"\"+value1+\""+"\\"+"\" >\"+value1+\"</a>"+"\""+";"+"\n");
			js.append( "                }"+"\n"
					  +"            },"+"\n");
			temp=js.toString();
		}
		return temp;
	}*/
	
	public static String getComboboxHtml(String classType, String jsId, String fieldName, String vtype, String emptyText, boolean isRequired,
			boolean isValidation, boolean isDisable, String entityName, String formColumnWidth, String lableName, boolean isRender, String formDefCode,
			boolean readonly, String formColumnAlign, String comboboxCheckSelf,boolean showNullItem,String textField,String valueField,boolean allowInput,
			String dictTypeId,String nullItemText,String url,String data,String dataField,boolean multiselect,String showtypeString,String isApp,String columnPosition,String formColumnId,String colorCondition ) {
		// TODO 自动生成方法存根
		fieldName = fieldName.toUpperCase();
		String requiredVtypeHtml = isRequired?";required":"";
		String vtypeHtml =  "";
		if("10".equals(vtype)){
			vtype = comboboxCheckSelf==null?"":comboboxCheckSelf;
		}
		String widthHtml =  formColumnWidth!=null? " width=\""+formColumnWidth+"\" ":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = formColumnAlign==null?"":"align=\""+formColumnAlign+"\"";
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String showNullItemHtml = showNullItem==true?" showNullItem=\"true\"":"";
		
		String textFieldHtml = textField!=null?" textField=\""+textField+"\"":"";
		String valueFieldHtml = valueField!=null?" valueField=\""+valueField+"\"":"";
		String allowInputHtml = allowInput==true?" allowInput=\"true\"":"";
		String dictTypeIdHtml = dictTypeId!=null?" dictTypeId=\""+dictTypeId+"\"":"";
		String nullItemTextHtml = nullItemText!=null?" nullItemText=\""+nullItemText+"\"":"";
		String urlHtml = url!=null?" url=\""+url+"\"":"";
		String dataHtml = data!=null?" data=\""+data+"\"":"";
		//String dataFieldHtml = dataField!=null?" dataField=\""+dataField+"\"":"";
		String dataFieldHtml = dataField!=null?" "+dataField+"_form_"+formColumnId+"":"";
		String multiselectHtml = multiselect==true?" multiSelect=\"true\"":"";
		vtypeHtml = (isValidation)? " vtype=\""+vtype+requiredVtypeHtml+"\" ":(!"".equals(requiredVtypeHtml)?"vtype=\""+requiredVtypeHtml+"\")":"");
		String temp = "";
		if (isApp!=null&&isApp.equals("APP")) {
			temp = "<div class=\""+columnPosition+" hidden\" v-if=\"item."+fieldName.toUpperCase()+"\">{{item."+fieldName.toUpperCase()+"}}</div>\n";
			temp += "<div class=\""+columnPosition+"\" v-if=\"item."+fieldName.toUpperCase()+"_DICTNAME\">{{item."+fieldName.toUpperCase()+"_DICTNAME}}</div>\n";
		}else {
			StringBuffer js = new StringBuffer();
			String width = "100";
			if(!formColumnWidth.equals("")&&formColumnWidth!=null){
				width = formColumnWidth.substring(formColumnWidth.length()-2, formColumnWidth.length());
			}
			js.append(	 "            {"+"\n"
						//+"                title : '"+lableName+"',"+"\n"
						+"                title : '"+lableName+"',"+"\n"
						+"                field : '"+fieldName+"',"+"\n"
						+"                visible :false,"+"\n"
						+"            },{"+"\n"
//						+"                title : '"+lableName+"',"+"\n"
						+"                title : '"+lableName+"',"+"\n"
						+"                field : '"+fieldName+"_DICTNAME',"+"\n"
						+"                align : 'center',"+"\n"
						+"                "+showtypeString+","+"\n"
						+"                valign : '"+formColumnAlign+"',"+"\n"
						+"				  sortable: true,\n"			
						+"                formatter: function (value, row, index) {"+"\n"
						+"						if(value==undefined||value=='null'||value==''){"+"\n"
						+"        					value='-';"+"\n"
						+"    					}"+"\n");
						if(colorCondition!=null && !colorCondition.equals("")&& !colorCondition.equals("null")){
							//0<value<10|red|ge;value==10|yellow|hang;value>10|gold|lie
							String[] conditions = colorCondition.split(";");//条件
							
							for (int i = 0; i < conditions.length; i++) {
								String[] params = conditions[i].split("\\|");
								if (i==0) {
									js.append(	 "	if("+params[0]+"){\n");
								} else {
									js.append(	 "	}else if("+params[0]+"){\n");
								}
								if (params[2].equals("hang")) {
									js.append("                    	return "+"\""+"<a class=\\\"eli w"+width+" changeHangColorForGrid\\\" changeColor=\\\""+params[1]+"\\\" style=\\\"font-size:12px;text-decoration:none;color:"+params[1]+";width:"+formColumnWidth+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" title="+"\\"+"\"\"+value+\"\\"+"\">\"+value+\"</a>"+"\""+";"+"\n");
								}else{
									js.append("                    	return "+"\""+"<a class=\\\"eli w"+width+"\\\" style=\\\"font-size:12px;text-decoration:none;color:"+params[1]+";width:"+formColumnWidth+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" title="+"\\"+"\"\"+value+\"\\"+"\">\"+value+\"</a>"+"\""+";"+"\n");
								}
							}
							js.append( "		}else{\n" +
									   "                    	return "+"\""+"<a class=\\\"eli w"+width+"\\\" style=\\\"font-size:12px;text-decoration:none;color:black;width:"+formColumnWidth+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" title="+"\\"+"\"\"+value+\"\\"+"\">\"+value+\"</a>"+"\""+";"+"\n"+
									   "		}\n");
						}else{
							js.append( "                    	return "+"\""+"<a class=\\\"eli w"+width+"\\\" style=\\\"font-size:12px;text-decoration:none;color:black;width:"+formColumnWidth+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" title="+"\\"+"\"\"+value+\"\\"+"\">\"+value+\"</a>"+"\""+";"+"\n");
						}	
						
						
						js.append(	"                }"+"\n"
									+"            },"+"\n"
									);
			temp=js.toString();
		}
		return temp;
	}
	
	public static String getPicHtml(String classType, String jsId, String fieldName, String vtype,String othervtype, String emptyText, boolean isRequired,
			boolean isValidation, boolean isDisable, String entityName, String width,String lableName, boolean isRender,String formDefCode, boolean readonly, String align,String checkSelf,String height,String showtypeString,String colorCondition,String color,SysTextBoxCache textBox,String isApp,String columnPosition) {
		// TODO 自动生成方法存根
		String requiredVtypeHtml = isRequired?";required":"";
		String vtypeHtml =  "";
		if("10".equals(vtype)){
			vtype = checkSelf==null?"":checkSelf;
		}
		if(vtype!=null&&!"".equals(vtype)){
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = vtype+";"+othervtype;
			}
		}else {
			if(othervtype!=null&&!"".equals(othervtype)) {
				vtype = othervtype;
			}
		}
	    
		vtypeHtml = (isValidation)? " vtype=\""+vtype+requiredVtypeHtml+"\" ":(!"".equals(requiredVtypeHtml)?"vtype=\""+requiredVtypeHtml+"\")":"");
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String widthHtml =  width!=null? " width=\""+width+"\" ":"";
		String heightHtml =  height!=null? " height=\""+height+"\" ":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = align==null?"":"align=\""+align+"\"";
		String temp = "";
		if (isApp!=null&&isApp.equals("APP")) {
			if (textBox.getPojo().getTextboxIsLink()!=null&&textBox.getPojo().getTextboxIsLink().equals("Y")) {
				temp =  "<a  href=\"toForm.action?isInit=true&formType=APP&formId="+textBox.getPojo().getTextboxLinkForm()+"\">\n" +
						"	<img class=\"itemPic\" :src = \"'http://116.62.243.28:9875/' + item."+fieldName.toUpperCase()+" \" alt=\"\" />\n"+
						"	<span class=\"mui-icon\"></span>\n"+
						"</a>\n";
			} else {
				temp = "	<img class=\"itemPic\" :src = \"'http://116.62.243.28:9875/' + item."+fieldName.toUpperCase()+" \" alt=\"\" />\n"+
					   "	<span class=\"mui-icon\"></span>\n";
				}
		} else {
			temp = getGridColumnProperty(fieldName,lableName,vtypeHtml,width,rendereHtml,align,"",showtypeString,colorCondition,color,textBox,"");
		}
		
		/*if(!readonly){
			temp+= SysCardFormBeansUtil.getNbsp(4)+"<input property=\"editor\" class=\""+classType+"\""+emptyTextHtml+widthHtml+heightHtml+" />"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		temp+= SysCardFormBeansUtil.getNbsp(3)+"</div>"+SysCardFormBeansUtil.NEW_LINE_ONLY;*/
		return temp;
	}
	
	/**
	 * datepicker控件 html
	 * @param classType
	 * @param jsId
	 * @param fieldName
	 * @param vtype
	 * @param emptyText
	 * @param isRequired
	 * @param isValueChange
	 * @param isOnclick
	 * @param isValidation
	 * @param isDisable
	 * @param entityName
	 * @param formColumnWidth
	 * @param format
	 * @param timeformat
	 * @param showtime
	 * @param showokbutton
	 * @param showclearbutton
	 * @param allowinput
	 * @param showtodaybutton
	 * @param isDrawdate
	 * @param readonly 
	 * @return
	 */
	public static String getDatepickerHtml(String classType, String jsId, String fieldName, String vtype, String emptyText, boolean isRequired, boolean isCellEdite, 
			boolean isRender, boolean isValidation, boolean isDisable, String entityName, String formColumnWidth, String format, String timeformat, boolean showtime, 
			boolean showokbutton, boolean showclearbutton, boolean allowinput, boolean showtodaybutton, boolean isDrawdate,String formDefCode,boolean readonly, String formColumnAlign,String lableName,String isDrawdateFunName,String colorCondition,String color,SysTextBoxCache textbox) {
		// TODO 自动生成方法存根
		String requiredVtypeHtml = isRequired?"vtype=\"required\"":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String vtypeHtml = requiredVtypeHtml;
		
		String alignHtml = formColumnAlign==null?"":"align=\""+formColumnAlign+"\"";
		String widthHtml =  formColumnWidth!=null? " width=\""+formColumnWidth+"\" ":"";
		isDrawdateFunName = isDrawdate?" ondrawdate=\""+isDrawdateFunName+"\"":"";
		String formatHtml =  format!=null? " format=\""+format+"\" valueformat=\""+format+"\" ":"valueformat=\"yyyy-MM-dd HH:mm:ss\"";
		String timeFormatHtml =  timeformat!=null? " timeFormat=\""+timeformat+"\" ":"";
		String emptyTextHtml =  emptyText!=null? " emptyText=\""+emptyText+"\" ":"";
		String showtimeHtml = showtime?" showTime=\"true\"":"";
		String showokbuttonHtml = showokbutton?" showOkButton=\"true\"":"";
		String showclearbuttonHtml = showclearbutton?" showClearButton=\"true\"":"";
		String showtodaybuttonHtml = showtodaybutton?" showTodayButton=\"true\"":"";
		String allowinputHtml = allowinput?" allowInput=\"true\"":"";
		String dateFormat = format!=null? " dateFormat=\""+format+"\" ":"dateFormat=\"yyyy-MM-dd HH:mm:ss\"";
		dateFormat += " dataType=\"date\"";
		
		/*if(SysCardFormBeansUtil.CARD_FORM_MONTHPICKER.equals(classType)){
			emptyTextHtml =  "";
			showtimeHtml = "";
			showokbuttonHtml = "";
			showclearbuttonHtml = "";
			showtodaybuttonHtml = "";
			allowinputHtml = "";
		}*/
		
		String temp = getGridColumnProperty(fieldName,lableName,vtypeHtml,widthHtml,rendereHtml,formColumnAlign,dateFormat,"",colorCondition,color,textbox,"");
		if(!readonly){
			temp += "<input  property=\"editor\" class=\""+classType+"\"" + 
						emptyTextHtml+widthHtml+isDrawdateFunName+formatHtml+timeFormatHtml+showtimeHtml+showokbuttonHtml+showclearbuttonHtml+showtodaybuttonHtml+allowinputHtml+widthHtml+"/>";
		}
		temp+= SysCardFormBeansUtil.getNbsp(3)+"</div>"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	/**
	 * checkbox html代码
	 * @param classType
	 * @param jsId
	 * @param fieldName
	 * @param vtype
	 * @param emptyText
	 * @param isRequired
	 * @param isValueChange
	 * @param isOnclick
	 * @param isValidation
	 * @param isDisable
	 * @param entityName
	 * @param formColumnWidth
	 * @param showName
	 * @return
	 */
	public static String getCheckboxHtml(String classType, String jsId, String fieldName, String vtype, String emptyText, boolean isRequired, boolean isCellEdite, boolean isRender,
			boolean isValidation, boolean isDisable, String entityName, String formColumnWidth, String showName,String lableName,String formDefCode,String formColumnAlign) {
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String alignHtml = formColumnAlign==null?"":"align=\""+formColumnAlign+"\"";
		String widthHtml =  formColumnWidth!=null? " width=\""+formColumnWidth+"\" ":"";
		
		String checkBoxHtml = SysCardFormBeansUtil.getNbsp(3)+"<div type=\"checkboxcolumn\" field=\""+fieldName+"\" trueValue=\"1\" falseValue=\"0\" "+rendereHtml+widthHtml+alignHtml+">"+lableName+"</div> ";
		
		return checkBoxHtml;
	}
	
	/**
	 * lookup html
	 * @param classType
	 * @param jsId
	 * @param fieldName
	 * @param vtype
	 * @param emptyText
	 * @param isRequired
	 * @param isValueChange
	 * @param isOnclick
	 * @param isValidation
	 * @param isDisable
	 * @param entityName
	 * @param formColumnWidth
	 * @param formDefCode
	 * @param formColumnAlign
	 * @return
	 */
	public static String getLookupHtml(String classType, String jsId, String fieldName, String vtype, String emptyText, boolean isRequired, boolean isCellEdit, boolean isRender,
			boolean isValidation, boolean isDisable, String entityName, String formColumnWidth,String lableName, String formDefCode, String formColumnAlign,String displayField,String onButtonEditName,String colorCondition,String color,SysTextBoxCache textbox) {
		// TODO 自动生成方法存根
		String requiredVtypeHtml = isRequired?"vtype=\"required\"":"";
		String rendereHtml = isRender?" renderer=\""+RENDERER_NAME+formDefCode+jsId+"\"":"";
		String vtypeHtml = requiredVtypeHtml;
		
		String alignHtml = formColumnAlign==null?"":"align=\""+formColumnAlign+"\"";
		String widthHtml =  formColumnWidth!=null? " width=\""+formColumnWidth+"\" ":"";
		String displayFieldHtml = displayField==null?"":"displayField=\""+displayField+"\"";
		
		
		String temp = getGridColumnProperty(fieldName,lableName,vtypeHtml,widthHtml,rendereHtml,alignHtml,displayFieldHtml,"",colorCondition,color,textbox,"");
		if(isDisable==false){
			temp+=SysCardFormBeansUtil.getNbsp(4)+"<input property=\"editor\" class=\"nui-buttonedit\" onbuttonclick=\""+onButtonEditName+"\" style=\"width:100%;\"/>"+SysCardFormBeansUtil.NEW_LINE_ONLY; 
		}
		
		temp+= SysCardFormBeansUtil.getNbsp(3)+"</div>"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	


	
	public static String getGridColumnProperty(String fieldName,String lableName,String vtype,String width,String rendereHtml,String alignHtml,String otherHtml,String showtypeString,String colorCondition,String color,SysTextBoxCache textbox,String isRichText){
		//temp+=SysCardFormBeansUtil.getNbsp(3)+"<div field=\""+fieldName+"\" name=\""+fieldName+"\" allowSort=\"true\" headerAlign=\"center\" "+alignHtml+width+rendereHtml+vtype+otherHtml+">"+lableName+SysCardFormBeansUtil.NEW_LINE_ONLY;
		fieldName = fieldName.toUpperCase();
		String width1 = "";
		if (width!=null&&!width.equals("")) {
			width.substring(width.length()-2, width.length());
		}
		String colortemp = "";
		String hrefhtml = "";
		String textDecoration = "";
		if(textbox!=null&&textbox.getPojo()!=null&&textbox.getPojo().getTextboxId()!=null&&!"0".equals(textbox.getPojo().getTextboxId())){
			if(textbox.getPojo().getTextboxIsLink()!=null){
				if(textbox.getPojo().getTextboxIsLink().equals("Y")){
					textDecoration = "text-decoration:underline;";
					if(textbox.getPojo().getTextboxLinkIsForm().equals("Y")){
						List<SysFormGeneralParamCache> param = textbox.getGeneralParams().get("COLUMN_LINK");
						String paramStr = "";
						if (textbox.getPojo().getTextboxLinkWinOpenType().equals("3")||textbox.getPojo().getTextboxLinkWinOpenType().equals("4")) {
							hrefhtml += "onmouseout=\\\"xuanflag = false;\\\" onmouseover=\\\"xuanflag=true;openNew(";
							if (param!=null) {
								for (int i = 0; i < param.size(); i++) {
									paramStr += param.get(i).getPojo().getParamName()+"=\"+row."+param.get(i).getPojo().getParamFromValue()+"+\"&";
									hrefhtml += "'\"+row."+param.get(i).getPojo().getParamFromValue().toUpperCase()+"+\"',";
								}
								hrefhtml = hrefhtml.substring(0, hrefhtml.length()-1);
							} 
							hrefhtml += ")\\\"";
						} else {
							if (param!=null) {
								for (int i = 0; i < param.size(); i++) {
									paramStr += param.get(i).getPojo().getParamName()+"=\"+row."+param.get(i).getPojo().getParamFromValue()+"+\"&";
									hrefhtml += "'\"+row."+param.get(i).getPojo().getParamFromValue()+"+\"',";
								}
							} 
							String url = "toForm.action?formId="+textbox.getPojo().getTextboxLinkForm()+"&"+paramStr;
							hrefhtml = "href =\\\""+url+"\\\"";
						}
					}else{
						List<SysFormGeneralParamCache> param = textbox.getGeneralParams().get("COLUMN_LINK");
						String paramStr = "";
						if (param!=null) {
							
							for (int i = 0; i < param.size(); i++) {
								paramStr += param.get(i).getPojo().getParamName()+"=\"+row."+param.get(i).getPojo().getParamFromValue()+"+\"&";
							}
						} 
						String url = textbox.getPojo().getTextboxLinkUrl()+"&"+paramStr;
						hrefhtml = "href =\\\""+url+"\\\"";
					}
				}
			}else{
				textDecoration = "text-decoration:none;";
			}
		}else {
			textDecoration = "text-decoration:none;";
		}
		String colorstyle = "";
		if(color !=null && !color.equals("#000000")){
			colorstyle="color:"+color+";";
		}
		
		if(colorCondition!=null && !colorCondition.equals("")){
			//0<value<10|red|ge;value==10|yellow|hang;value>10|gold|lie
			String[] conditions = colorCondition.split(";");//条件
			
			for (int i = 0; i < conditions.length; i++) {
				String[] params = conditions[i].split("\\|");
				if (i==0) {
					colortemp = "		if("+params[0]+"){\n";
				} else {
					colortemp += "		}else if("+params[0]+"){\n";
				}
				if (params[2].equals("hang")) {
					colortemp += "                        	return "+"\""+"<a "+hrefhtml+" class=\\\"eli w"+width1+" changeHangColorForGrid\\\" changeColor=\\\""+params[1]+"\\\" style=\\\"font-size:12px;"+textDecoration+"width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" data-title="+"\\"+"\""+lableName+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n";
				}else{
					colortemp += "                        	return "+"\""+"<a "+hrefhtml+" class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:"+params[1]+";width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" data-title="+"\\"+"\""+lableName+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n";
				}
			}
			colortemp += "		}else{\n" +
						 "      	return "+"\""+"<a "+hrefhtml+" class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:black;width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" data-title="+"\\"+"\""+lableName+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n"+
						 "		}\n";
		}else if(isRichText.equals("richText")){
			colortemp = "                   return "+"\""+"<a "+hrefhtml+" class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:black;width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" data-title="+"\\"+"\""+lableName+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n";
		}else{
			colortemp = "                   return "+"\""+"<a "+hrefhtml+" class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:black;width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" data-title="+"\\"+"\""+lableName+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n";
		}
		
		String temp="  {"+"\n"
//		+"                title : '"+lableName+"',"+"\n"
		+"                title : '"+lableName+"',"+"\n"
		+"                field : '"+fieldName+"',"+"\n"
		+"                align : 'center',"+"\n"
		+"                "+showtypeString+","+"\n"
		+"                valign : '"+alignHtml+"',"+"\n"
//		+"                sortable : true,"+"\n"
		+"                formatter: function (value, row, index) {"+"\n"
		+"                    if(value==undefined){"+"\n"
		+"                        value='-';"+"\n"
		+"                    }"+"\n"
		+colortemp
		+"                }"+"\n"
		+"            },"+"\n";
		return temp;
	}
	
	public static String getGridColumnPropertyForGrid(String fieldName,String lableName,String vtype,String width,String rendereHtml,String alignHtml,String otherHtml,String showtypeString,String isEdit,String color,SysTextBoxCache textbox,String isRichText){
		//temp+=SysCardFormBeansUtil.getNbsp(3)+"<div field=\""+fieldName+"\" name=\""+fieldName+"\" allowSort=\"true\" headerAlign=\"center\" "+alignHtml+width+rendereHtml+vtype+otherHtml+">"+lableName+SysCardFormBeansUtil.NEW_LINE_ONLY;
		fieldName = fieldName.toUpperCase();
		String width1 = "";
		if (width!=null&&!width.equals("")) {
			width.substring(width.length()-2, width.length());
		}
		String colortemp = "";
		String hrefhtml = "";
		String textDecoration = "";
		if(textbox!=null&&textbox.getPojo()!=null&&textbox.getPojo().getTextboxId()!=null&&!"0".equals(textbox.getPojo().getTextboxId())){
			if(textbox.getPojo().getTextboxIsLink()!=null){
				if(textbox.getPojo().getTextboxIsLink().equals("Y")){
					textDecoration = "text-decoration:underline;";
				}
			}else{
				textDecoration = "text-decoration:none;";
			}
		}else {
			textDecoration = "text-decoration:none;";
		}
		String colorstyle = "";
		if (isEdit.equals("edit")) {
			colortemp = "                   return "+"\""+"<input  class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:black;width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" value=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\" data-title="+"\\"+"\""+lableName+"\\"+"\">\"\n";
		} else {
			colortemp = "                   return "+"\""+"<a  class=\\\"eli w"+width1+"\\\" style=\\\"font-size:12px;"+textDecoration+"color:black;width:"+width+";\\\" name="+"\\"+"\""+fieldName+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" title=\\\""+"\""+"+"+ "value" +"+"+"\""+"\\\"  data-title="+"\\"+"\""+lableName+"\\"+"\">\"+value+\"</a>\"\n";
		}
		String temp="  {"+"\n"
//		+"                title : '"+lableName+"',"+"\n"
		+"                title : '"+lableName+"',"+"\n"
		+"                field : '"+fieldName+"',"+"\n"
		+"                align : 'center',"+"\n"
		+"                "+showtypeString+","+"\n"
		+"                valign : '"+alignHtml+"',"+"\n"
		+"                formatter: function (value, row, index) {"+"\n"
		+"                    if(value==undefined){"+"\n"
		+"                        value='-';"+"\n"
		+"                    }"+"\n"
		+colortemp
		+"                }"+"\n"
		+"            },"+"\n";
		return temp;
	}
	
	/**
	 * 获取自定义检查方案
	 * @param fieldName
	 * @return
	 */
	public static String getCheckFun(String fieldName,String checkFun) {
		// TODO 自动生成方法存根
		String temp = "";
		/*checkFun =checkFun == null?"":checkFun;
		temp += "var e=nui.encode();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += "if (e.field == '"+fieldName+"') {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp +=checkFun+SysCardFormBeansUtil.NEW_LINE_ONLY;
		 temp +="}";*/
		return temp;
	}

	/**
	 * 获取字段对应的cellbeign js
	 * @param fieldName
	 * @param cellbegin
	 * @return
	 */
	public static String getCellbeginFun(String fieldName, String cellbegin) {
		// TODO 自动生成方法存根
		String temp = "";
		temp += "function  "+fieldName+"_valueChange() {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp +=cellbegin+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp +="}";
		return temp;
	}

	public static String getRendererFun(String jsId, String formDefCode, String renderer) {
		// TODO 自动生成方法存根
		String temp = "function "+RENDERER_NAME+formDefCode+jsId+"(e){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+= renderer+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+="}";
		return temp;
	}
	
	/**
	 * 生成grid组开始代码
	 * @param groupName
	 * @return
	 */
	public static String getCardFormGridGroupStart(String groupName,String isApp) {
		// TODO 自动生成方法存根
		String temp  = "";
		if (isApp!=null&&isApp.equals("APP")) {
			if (groupName.equals("left")) {
				temp = "<div class=\""+groupName+"Content\" style=\"width:35%;float:left;position:relative\">\n" +
					   "	<div class=\"clearfix\"  style=\"display:block;\">\n";
			} else {
				temp = "<div class=\""+groupName+"Content\" style=\"width:65%;float:right;position:relative\">\n" +
					   "	<div class=\"clearfix\"  style=\"display:block;\">\n";
			}
		}
		//temp+=SysCardFormBeansUtil.getNbsp(2)+"<div header=\""+groupName+"\" headerAlign=\"center\" >"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp+=SysCardFormBeansUtil.getNbsp(2)+"<div property=\"columns\">"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	public static void getGridColumns(List<SysFormColumnCache> columns,List<Map> fields,SysRequestParam request,StringBuffer[] sbs,String isApp) throws Exception{
		String userid = "";
		try {
			HttpSession s = request.getSession();
			userid = s.getAttribute(Constants.USER_ID)+"";
		} catch (Exception e) {
			e.printStackTrace();logger.error(e.getMessage(),e);
			throw e;
		}
		for (int i = 0; i < columns.size(); i++) {
			//生成字段div
			if (isApp!=null&&isApp.equals("APP")) {
				if (i==0) {
					
				} else if (columns.get(i).getPojo().getFormColumnPositionSort()!=null&&columns.get(i-1).getPojo().getFormColumnPositionSort()!=null) {
					String sort1 = columns.get(i).getPojo().getFormColumnPositionSort()+"";
					String sort2 = columns.get(i-1).getPojo().getFormColumnPositionSort()+"";
					logger.info(sort1.split("\\.")[0].length()+"");
					if (Integer.parseInt(sort1.split("\\.")[0])!=Integer.parseInt(sort2.split("\\.")[0])&&sort1.split("\\.").length>1) {
						sbs[0].append("</div>\n" +
									  "<div class=\"clearfix\" style=\"display:block;\">\n");
					} 
				}
			} 
			
			SysFormColumnCache c = columns.get(i);
			//logger.info(c.getFormColumnLable());
			//获取字段ID
			String columnId_ =  c.getFormColumnId()+"";
			String formAuthorityIsControl = c.getForm().getFormAuthorityIsControl();
			boolean hsFieldRigt = SysCardFormBeansUtil.hasFiled(fields, columnId_, request.getIsView(),formAuthorityIsControl);
			if("Y".equals(c.getFormColumnIsDev())){ //开发字段跳过
				continue;
			}
			String[] htmls = null;
			htmls = c.getHtmlsFrom(request,c.getFormColumnShowType(),isApp);
			
			sbs[0].append(htmls[0]);
			if(htmls!=null&&htmls[18]!=null&&!"".equals(htmls[18]))
				sbs[60].append(htmls[18]);//展开主表参数值定义
			if(htmls!=null&&htmls[24]!=null&&!"".equals(htmls[24]))
				sbs[69].append(htmls[24]);//展开附表参数值定义
			if(htmls!=null&&htmls[25]!=null&&!"".equals(htmls[25]))
				sbs[70].append(htmls[25]);//展开附表参数值定义
			if(htmls!=null&&htmls[16]!=null&&!"".equals(htmls[16]))
				sbs[61].append(htmls[16]);//展开只读样式
			if(htmls!=null&&htmls[17]!=null&&!"".equals(htmls[17]))
				sbs[62].append(htmls[17]);//展开隐藏样式
			if(htmls!=null&&htmls[19]!=null&&!"".equals(htmls[19]))
				sbs[63].append(htmls[19]);//合计参数定义
			if(htmls!=null&&htmls[20]!=null&&!"".equals(htmls[20]))
				sbs[64].append(htmls[20]);//合计只读样式
			
			if(htmls!=null&&htmls[26]!=null&&!"".equals(htmls[26]))
				sbs[57].append(htmls[26]);//批量修改
			
			if(htmls!=null&&htmls[21]!=null&&!"".equals(htmls[21]))
				sbs[65].append(htmls[21]);//修改操作参数取值
			if(htmls!=null&&htmls[22]!=null&&!"".equals(htmls[22]))
				sbs[66].append(htmls[22]);//修改操作控件样式改变
			if(htmls!=null&&htmls[23]!=null&&!"".equals(htmls[23]))
				sbs[67].append(htmls[23]);//修改操作控件样式改变后赋值
			
			if(htmls!=null&&htmls[13]!=null&&!"".equals(htmls[13]))
				sbs[22].append(htmls[13]);
			if(htmls!=null&&htmls[1]!=null&&!"".equals(htmls[1]))
				sbs[1].append(htmls[1]);
			if(htmls!=null&&htmls[2]!=null&&!"".equals(htmls[2]))
				sbs[2].append(htmls[2]);
			if(htmls!=null&&htmls[3]!=null&&!"".equals(htmls[3]))
				sbs[3].append(htmls[3]);
			if(htmls!=null&&htmls[4]!=null&&!"".equals(htmls[4]))
				sbs[4].append(htmls[4]);
			if(htmls!=null&&htmls[5]!=null&&!"".equals(htmls[5]))
				sbs[5].append(htmls[5]);
			if(htmls!=null&&htmls[8]!=null&&!"".equals(htmls[8]))
				sbs[8].append(htmls[8]);
			if(htmls!=null&&htmls[9]!=null&&!"".equals(htmls[9])) 
				sbs[9].append(htmls[9]);
			if(htmls!=null&&htmls[10]!=null&&!"".equals(htmls[10])) //js开头代码
				sbs[10].append(htmls[10]);
			if(htmls!=null&&htmls[11]!=null&&!"".equals(htmls[11])) //js开头代码
				sbs[11].append(htmls[11]);
			
		}
	}

	public static String getGridFormInitFunction(SysRequestParam request, SysFormconfigCache formconfig, StringBuffer[] sbs,String isApp) {
		// TODO 自动生成方法存根
				String isView = request.getIsView();
				String temp = "";
				String filterdata = "";
				List<SysFormWhereCache> wheres = formconfig.getWheres();
				//获取过滤区参数数据 参数formid生成规则为gridid+filter
				for(SysFormWhereCache where:wheres){
					sbs[48].append(SysCardFormBeansUtil.getNbsp(0)+"var "+where.getFormWhereValue()+"='';"+SysCardFormBeansUtil.NEW_LINE_ONLY);
				}
				temp +=SysCardFormBeansUtil.getNbsp(0)+"var filterParam = {};\n";
				temp +=SysCardFormBeansUtil.getNbsp(0)+"var queryParams;\n";
				
				
				temp+=SysCardFormBeansUtil.getNbsp(0)+"function grid_"+formconfig.getFormDefCode()+"_fun(pageReqeust){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(1)+"var _filterdata={}"+SysCardFormBeansUtil.NEW_LINE;
				if (isApp!=null&&isApp.equals("APP")) {
					//temp+=SysCardFormBeansUtil.getNbsp(1)+"var containerParam_"+formconfig.getFormDefId()+"={}"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"var pageReqeust={}"+SysCardFormBeansUtil.NEW_LINE;
				}
				for(int i=0;i<formconfig.getFilter().getFilterColumns().size();i++){
					if (formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnColSpan()!=null&&formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnColSpan().equals("1")) {
						temp +=SysCardFormBeansUtil.getNbsp(2)+"var "+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+" = \"\";\n";
						temp +=SysCardFormBeansUtil.getNbsp(2)+"if(filterParam."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"!=undefined){\n";
						temp +=SysCardFormBeansUtil.getNbsp(3)+""+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+" = filterParam."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+";\n";
						temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";
					}
				}
				//canvasContent
				if (isApp!=null&&isApp.equals("APP")) {
					temp+=SysCardFormBeansUtil.getNbsp(1)+"if($('.canvasContent')){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				} else {
					temp+=SysCardFormBeansUtil.getNbsp(1)+"if(document.getElementById('grid_"+formconfig.getFormDefCode()+"_filter')){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
				
				if (formconfig.getFilter().getFilterColumns().size()>0) {
					for(int i=0;i<formconfig.getFilter().getFilterColumns().size();i++){
						if (formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnColSpan()!=null&&formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnColSpan().equals("1")) {
						} else {
							SysFormColumnCache sysFormColumn = formconfig.getFilter().getFilterColumns().get(i).getColumn();
							if (sysFormColumn==null) {
								formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().setFormEntityTablename("VIR");
							}
							if (formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnIsIntervais().equals("Y")) {
								String filterEntityName = formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename().toUpperCase();
								String filterFieldName = formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename().toUpperCase();
								temp +=SysCardFormBeansUtil.getNbsp(2)+"var _INTERVAIL_MIN_"+filterFieldName+"=$(\"[id='_INTERVAIL_MIN_FILTER_"+filterEntityName+"."+ChangeCode.getUniqueCode(filterEntityName,filterFieldName)+"_"+formconfig.getFormDefId()+"']\").val();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
								filterdata +="_INTERVAIL_MIN_"+ChangeCode.getUniqueCode(filterEntityName,filterFieldName)+":_INTERVAIL_MIN_"+filterFieldName+",";
								temp +=SysCardFormBeansUtil.getNbsp(2)+"var _INTERVAIL_MAX_"+filterFieldName+"=$(\"[id='_INTERVAIL_MAX_FILTER_"+filterEntityName+"."+ChangeCode.getUniqueCode(filterEntityName, filterFieldName)+"_"+formconfig.getFormDefId()+"']\").val();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
								filterdata +="_INTERVAIL_MAX_"+ChangeCode.getUniqueCode(filterEntityName,filterFieldName)+":_INTERVAIL_MAX_"+filterFieldName+",";
							}else{
								String entityName0 = formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename();
								String fieldName0 = formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename();
								String columnName0 = ChangeCode.getUniqueCode(entityName0,fieldName0);
								
								if (isApp!=null&&isApp.equals("APP")) {							
									temp +=SysCardFormBeansUtil.getNbsp(1)+"if(($(\"[id='FILTER_"+entityName0+"."+columnName0+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"select\")||($(\"[id='FILTER_"+entityName0+"."+columnName0+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"dateTime\")){\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"if($(\"[id='FILTER_"+entityName0+"."+columnName0+"_"+formconfig.getFormDefId()+"']\").attr('multiSelect')){\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"if(app&&app.mulChooseArr&&app.chooseArr."+columnName0+"_"+formconfig.getFormDefId()+"){\n";
									temp +=SysCardFormBeansUtil.getNbsp(4)+"var "+columnName0+"=app.mulChooseArr."+columnName0+"_"+formconfig.getFormDefId()+".id;\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(4)+"var "+columnName0+"=\"\";\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"}\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"if(app&&app.chooseArr&&app.chooseArr."+columnName0+"_"+formconfig.getFormDefId()+"){\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"var "+columnName0+"=app.chooseArr."+columnName0+"_"+formconfig.getFormDefId()+".id;\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"var "+columnName0+"=\"\";\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"}\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";
									temp +=SysCardFormBeansUtil.getNbsp(1)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"var "+columnName0+"=$(\"[id='FILTER_"+formconfig.getFormDefId()+"']\").val();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
									temp +=SysCardFormBeansUtil.getNbsp(1)+"}\n";					
									filterdata += columnName0.toUpperCase()+":"+columnName0+",";											
								} else {
									String entityName = entityName0.toUpperCase();
									String fieldName = fieldName0.toUpperCase();
									temp +=SysCardFormBeansUtil.getNbsp(1)+"if($(\"[id='FILTER_"+entityName+"."+ChangeCode.getUniqueCode(entityName,fieldName)+"_"+formconfig.getFormDefId()+"']\").attr('multiSelect')){\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"var val = \"\";\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"var arr = $(\"[id='FILTER_"+entityName+"."+ChangeCode.getUniqueCode(entityName, fieldName)+"_"+formconfig.getFormDefId()+"']\").val();\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"if(arr==null){\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"for(var i=0;i<arr.length;i++){\n";
									temp +=SysCardFormBeansUtil.getNbsp(4)+"val+=arr[i]+',';\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"}\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"val = val.substring(0,val.length-1);\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"var "+ChangeCode.getUniqueCode(entityName,fieldName)+"=val;\n";
									temp +=SysCardFormBeansUtil.getNbsp(1)+"}else if(($(\"[id='FILTER_"+entityName+"."+ChangeCode.getUniqueCode(entityName, fieldName)+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"select\")||($(\"[id='FILTER_"+entityName+"."+ChangeCode.getUniqueCode(entityName, fieldName)+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"dateTime\")){\n";
									temp +=SysCardFormBeansUtil.getNbsp(3)+"var "+ChangeCode.getUniqueCode(entityName,fieldName)+"=$(\"[id='FILTER_"+entityName+"."+ChangeCode.getUniqueCode(entityName, fieldName)+"_"+formconfig.getFormDefId()+"']\").val();\n";
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}else{\n";
									temp +=SysCardFormBeansUtil.getNbsp(4)+"var "+ChangeCode.getUniqueCode(entityName,fieldName)+"=$(\"[id='FILTER_"+formconfig.getFormDefId()+"']\").val();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
									temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";					
									filterdata +=ChangeCode.getUniqueCode(entityName,fieldName)+":"+ChangeCode.getUniqueCode(entityName,fieldName)+",";											
								}
								
							}
						}
					}
				}
				if (filterdata.length()>0) {
					temp+=SysCardFormBeansUtil.getNbsp(2)+"_filterdata={"+filterdata.substring(0, filterdata.length()-1)+"};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
				
				temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
				
				String whereObj = SysCardFormBeansUtil.getParamStringJson(wheres,request,null);
				String whereObjMst = SysCardFormBeansUtil.getParamStringJsonMst(wheres,request);
				logger.info(formconfig.getParams()+"");
				temp  += SysCardFormBeansUtil.getNbsp(1)+SysFormGeneralExecSqlCache.getDataGridParamsJsStr(formconfig.getParams(), null, request);
				
				
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"var requestParam={};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				if (wheres.size()>0) {
					String paramName = wheres.get(0).getFormWhereValue().toUpperCase();
					sbs[40].append("var "+paramName+";") ;
					temp  +=SysCardFormBeansUtil.getNbsp(1)+"if('${param."+wheres.get(0).getFormWhereValue().toUpperCase()+"}'==''&&'"+wheres.get(0).getFormWhereValueType()+"'=='parameter'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(2)+"if("+wheres.get(0).getFormWhereValue().toUpperCase()+"==undefined){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(3)+"requestParam = {"+wheres.get(wheres.size()-1).getFormWhereValue().toUpperCase()+":'${param."+wheres.get(wheres.size()-1).getFormWhereValue()+"}'};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(2)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(3)+"requestParam = "+whereObjMst+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(2)+"requestParam = "+whereObj+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp  +=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}else {
					temp  +=SysCardFormBeansUtil.getNbsp(2)+"requestParam = "+whereObj+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}

				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.containerParam=containerParam_"+formconfig.getFormDefId()+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.paramsMap=paramsMap;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.requestParam=requestParam;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.filter=_filterdata;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.userIds=getQueryString('empIds');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.formId=_formId_"+formconfig.getFormDefId()+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.isView=null;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				if ((isApp!=null&&isApp.equals("APP"))||(formconfig.getExpandField()!=null&&formconfig.getExpandField().equals("GRIDBYCARD"))) {
					temp+=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.offset=Currentpage*pagesize;"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.limit=pagesize;"+SysCardFormBeansUtil.NEW_LINE;
				} 
				//if(formconfig.getWheres())
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"if(heightGadedata_"+formconfig.getFormDefId()+"!=null&&heightGadedata_"+formconfig.getFormDefId()+"!=undefined){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(2)+"pageReqeust.heightGrade = heightGadedata_"+formconfig.getFormDefId()+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"queryParams = JSON.stringify(pageReqeust);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(1)+"return queryParams;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp  +=SysCardFormBeansUtil.getNbsp(0)+"function grid_"+formconfig.getFormDefCode()+"_fun11_"+formconfig.getFormDefId()+"(){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				if (formconfig.getPojoform().getExpandField()!=null&&formconfig.getPojoform().getExpandField().equals("GRIDBYCARD")) {
					temp  +=SysCardFormBeansUtil.getNbsp(1)+"loadDataStart();\n"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				} else {
					temp  +=SysCardFormBeansUtil.getNbsp(1)+"$('#"+formconfig.getFormDefCode()+"_List').bootstrapTable('refresh');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
				temp  +=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				return temp;
		
		
		
		/*// TODO 自动生成方法存根
		String isView = request.getParameter("isView");
		String temp = "";
		String filterdata = "";
		List<SysFormWhereCache> wheres = formconfig.getWheres();
		//获取过滤区参数数据 参数formid生成规则为gridid+filter
		for(SysFormWhereCache where:wheres){
			sbs[48].append(SysCardFormBeansUtil.getNbsp(0)+"var "+where.getFormWhereValue()+"='';"+SysCardFormBeansUtil.NEW_LINE_ONLY);
		}
		
		temp+=SysCardFormBeansUtil.getNbsp(0)+"function grid_"+formconfig.getFormDefCode()+"_fun(pageReqeust){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"var _filterdata={}"+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"if(document.getElementById('grid_"+formconfig.getFormDefCode()+"_filter')){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		if (formconfig.getFilter().getFilterColumns().size()>0) {
			for(int i=0;i<formconfig.getFilter().getFilterColumns().size();i++){
				logger.info(formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnIsIntervais());
				if (formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()==null) {
					formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().setFormEntityTablename("VIR");
				}
				if (formconfig.getFilter().getFilterColumns().get(i).getPojo().getGridColumnIsIntervais().equals("Y")) {
					temp +=SysCardFormBeansUtil.getNbsp(2)+"var _INTERVAIL_MIN_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=document.getElementById('_INTERVAIL_MIN_FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"').value;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					filterdata +="_INTERVAIL_MIN_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+":_INTERVAIL_MIN_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+",";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"var _INTERVAIL_MAX_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=document.getElementById('_INTERVAIL_MAX_FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"').value;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					filterdata +="_INTERVAIL_MAX_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+":_INTERVAIL_MAX_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+",";
				}else{
//					if(i==0){
					//temp +=SysCardFormBeansUtil.getNbsp(2)+"var "+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=document.getElementById('FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"."+formconfig.getFormDefId()+"').value;"+SysCardFormBeansUtil.NEW_LINE_ONLY;					
					temp +=SysCardFormBeansUtil.getNbsp(1)+"if($(\"[id='FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"']\").attr('multiSelect')){\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"var val = \"\";\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"var arr = $(\"[id='FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"']\").val();\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"if(arr==null){\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"}else{\n";
					temp +=SysCardFormBeansUtil.getNbsp(3)+"for(var i=0;i<arr.length;i++){\n";
					temp +=SysCardFormBeansUtil.getNbsp(4)+"val+=arr[i]+',';\n";
					temp +=SysCardFormBeansUtil.getNbsp(3)+"}\n";
					temp +=SysCardFormBeansUtil.getNbsp(3)+"val = val.substring(0,val.length-1);\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"var "+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=val;\n";
					temp +=SysCardFormBeansUtil.getNbsp(1)+"}else if(($(\"[id='FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"select\")||($(\"[id='FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"']\").attr('styleType')==\"dateTime\")){\n";
					temp +=SysCardFormBeansUtil.getNbsp(3)+"var "+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=$(\"[id='FILTER_"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormEntityTablename()+"."+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"_"+formconfig.getFormDefId()+"']\").val();\n";
					temp +=SysCardFormBeansUtil.getNbsp(2)+"}else{\n";
					temp +=SysCardFormBeansUtil.getNbsp(4)+"var "+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+"=$(\"[id='FILTER_"+formconfig.getFormDefId()+"']\").val();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp +=SysCardFormBeansUtil.getNbsp(2)+"}\n";
					filterdata +=formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+":"+formconfig.getFilter().getFilterColumns().get(i).getColumn().getPojo().getFormFieldTablename()+",";					
//					}
				}
			}
		}
		if (filterdata.length()>0) {
			temp+=SysCardFormBeansUtil.getNbsp(2)+"_filterdata={"+filterdata.substring(0, filterdata.length()-1)+"};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		
		
		temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
		
		String whereObj = SysCardFormBeansUtil.getParamStringJson(wheres,request);
		String whereObjMst = SysCardFormBeansUtil.getParamStringJsonMst(wheres,request);
		temp  += SysCardFormBeansUtil.getNbsp(1)+SysFormGeneralExecSqlCache.getDataGridParamsJsStr(formconfig.getParams(), null, request);
		
		
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"var requestParam={};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		if (wheres.size()>0) {
			sbs[40].append("var "+wheres.get(0).getFormWhereValue()+";") ;
			temp  +=SysCardFormBeansUtil.getNbsp(1)+"if('${param."+wheres.get(0).getFormWhereValue()+"}'==''){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(2)+"if("+wheres.get(0).getFormWhereValue()+"==undefined){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(3)+"requestParam = {"+wheres.get(wheres.size()-1).getFormWhereValue()+":'${param."+wheres.get(wheres.size()-1).getFormWhereValue()+"}'};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(2)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(3)+"requestParam = "+whereObjMst+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(2)+"requestParam = "+whereObj+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp  +=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}else {
			temp  +=SysCardFormBeansUtil.getNbsp(2)+"requestParam = "+whereObj+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"var userIds='${param.empIds}';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.userIds=userIds;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.paramsMap=paramsMap;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.requestParam=requestParam;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.filter=_filterdata;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.formId=_formId_"+formconfig.getFormDefId()+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"pageReqeust.isView=null;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"if(heightGadedata!=null&&heightGadedata!=undefined){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(2)+"pageReqeust.heightGrade = heightGadedata;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"return JSON.stringify(pageReqeust);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(0)+"function grid_"+formconfig.getFormDefCode()+"_fun11_"+formconfig.getFormDefId()+"(){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"$('#"+formconfig.getFormDefCode()+"_List').bootstrapTable('refresh');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp  +=SysCardFormBeansUtil.getNbsp(0)+"function init_requestParam_fun(){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+"var requestParam={};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(1)+whereObj+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp  +=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;*/
	}
	
	public static String getGridDrawSummaryCellFunction(SysFormconfigCache formconfig){
		List<String> strList = new ArrayList<String>();
		List<SysFormColumnCache> list = formconfig.getColumns();
		for(SysFormColumnCache col : list){
			String formColumnTotal = col.getFormColumnTotal();
			if("Y".equals(formColumnTotal)){
				strList.add(col.getFormFiledTablename());
			}
		}
		String temp = "";
		if(strList.size() > 0){
			formconfig.getGrid().setFormShowSummaryRow("true");
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var grid = e.sender"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var length = grid.getData().length"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var columnIndex = e.column._index"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"if(length == 0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"if(columnIndex == 0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"grid.setShowSummaryRow(false)"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"if(columnIndex == 0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(3)+"grid.setShowSummaryRow(true)"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var _filterform=new nui.Form('grid_"+formconfig.getFormDefCode()+"_filter')"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"_filterdata=_filterform.getData()"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var dataJson=nui.encode({filter:_filterdata,formId:_formId_"+formconfig.getFormDefCode()+"})"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"nui.ajax({"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"url: \"com.dcf.form.runtime.gridFormSummary.queryFormGridSummaryData.biz.ext\","+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"type: \"post\","+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"data: dataJson,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"async: false,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"contentType: 'text/json',"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"success: function (ret) {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(5)+"if(ret.retData)retData=ret.retData;else retData={}"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"})"+SysCardFormBeansUtil.NEW_LINE;
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"e.cellCls='mini-grid-pager'"+SysCardFormBeansUtil.NEW_LINE;
			for(int i=0;i<strList.size();i++){
				temp+=SysCardFormBeansUtil.getNbsp(2)+"if(e.field == '"+strList.get(i)+"')e.cellHtml=summaryCell(retData."+strList.get(i)+"_SUM)"+SysCardFormBeansUtil.NEW_LINE;
			}
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}";					
		}

		return temp;
	}	

	/**
	 * 获取新增/修改按钮对应弹窗式js
	 * @param gridname 
	 * @param string
	 * @return
	 */
	public static String getGridFormSaveJsOpenWin(SysFormButtonAddOrEditCache button,SysFormButtonCache b, String funName,String successType,String url, String gridname,boolean isEdit,String formId,String formCode) {
		// TODO 自动生成方法存根
		String st = getReloadJsStr(gridname,successType,"2");
		String buttonType = b.getFormButtonType();
		
		String[] aa = url.split("\\*");
		String getRowsJs  = "";
		String a3 = "";
		if(isEdit) {	
			getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"var selects = $(\"#"+formCode+"_List\").bootstrapTable('getSelections');"+SysCardFormBeansUtil.NEW_LINE;
			String preJs = b.getFormButtonCheckFun();
			if (preJs!=null&&preJs.indexOf(";")!=-1) {
				String[] str= preJs.split(";");
				if (str[0]!=null&&str[0].toUpperCase().equals("FORBID")) {
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"if(selects[0]."+str[1]+"){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"alert(\""+str[2]+"\")"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}else if (str[0]!=null&&str[0].toUpperCase().equals("SQLFORBID")) {
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"var sql = \""+str[1]+"\";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"var data = myehr_SQLFORBID_sql(sql);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"if(data.length>0&&data[0]."+str[3]+"=='"+str[4]+"'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"layer.alert('"+str[2]+"', {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(3)+"icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(3)+"skin: 'layer-ext-moon' //"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"})"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
			}
			if (buttonType.equals("batchEdit")) {
				String[] bb = aa[1].split(",");
				String a1 = "";
				String a2 = "";
				for (int i = 0; i < bb.length; i++) {
					a1 += "var param"+i+" = '';\n";
					a2 += "param"+i+"+= selects[i]."+bb[i]+"+',';\n";
					a3 += "+'&"+bb[i]+"='+"+"param"+i+".substring(0,param"+i+".length-1)";
				}
				a3.substring(0, a3.length()-3);
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"if(selects!=null && selects.length>0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+a1+SysCardFormBeansUtil.NEW_LINE;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"for(var i=0;i<selects.length;i++){"+SysCardFormBeansUtil.NEW_LINE;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(3)+a2+SysCardFormBeansUtil.NEW_LINE;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"title='"+button.getButtonAddTitle()+"';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}else if(data!=null&&isView!=null){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"select = data;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"title='预览';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}else {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"alert('请选择一行数据');return ;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}else{
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"if(selects!=null && selects.length>0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"select = selects[0];"+SysCardFormBeansUtil.NEW_LINE;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"title='"+button.getButtonAddTitle()+"';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}else if(data!=null&&isView!=null){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"select = data;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"title='预览';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}else {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"alert('请选择一行数据');return ;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}
		}else{
			String preJs = b.getFormButtonCheckFun();
			if (preJs!=null&&preJs.indexOf(";")!=-1) {
				String[] str= preJs.split(";");
				if (str[0]!=null&&str[0].toUpperCase().equals("SQLFORBID")) {
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"var sql = \""+str[1]+"\";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"var data = myehr_SQLFORBID_sql(sql);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"if(data.length>0&&data[0]."+str[3]+"=='"+str[4]+"'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"layer.alert('"+str[2]+"', {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(3)+"icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(3)+"skin: 'layer-ext-moon' //"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"})"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
					getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
			}
			getRowsJs+=SysCardFormBeansUtil.getNbsp(1)+"title='"+button.getButtonAddTitle()+"';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		
		String temp = SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"_"+formId+"(isView,data){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += getRowsJs;
		
		if (aa.length>2) {
			if("url".equals(button.getButtonAddExcType())){
				temp+= SysCardFormBeansUtil.getNbsp(1)+"if("+aa[2]+"){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[1]+"&isView='+isView+'';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[0]+"&isView='+isView+'';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}else{
				temp+= SysCardFormBeansUtil.getNbsp(1)+"if("+aa[2]+"){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[1]+"&isView='+isView+'';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[0]+"&isView='+isView+'';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
				//加密
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		var author='';var author1='';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var rule = '\\&+';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var regS = new RegExp(rule,'g');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				temp+= SysCardFormBeansUtil.getNbsp(2)+"url=url.replace(regS,'\\&');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"var rule2 =':' ;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"var regS2 = new RegExp(rule2,'g');"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"url = url.replace(regS2, '=');			"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"			"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		if(url.indexOf('Https')||url.indexOf('Http')||url.indexOf('www.')||url.indexOf('http')||url.indexOf('https')){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var author2='';"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				temp+=SysCardFormBeansUtil.getNbsp(2)+"			}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		author =((url.replace((url.substring(0, url.indexOf('?')+1)),'')));"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		author1 = md5(author);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var author2=author.split('=');\nvar order = '';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				
				//temp+=SysCardFormBeansUtil.getNbsp(2)+"		author =((url.replace((url.substring(0, url.indexOf('?')+1)),'')));"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				//temp+=SysCardFormBeansUtil.getNbsp(2)+"		var author1 = md5(author);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				
				
				
				temp+= SysCardFormBeansUtil.getNbsp(2)+" for(var i = 0;i<author2.length;i++){;"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				temp+= SysCardFormBeansUtil.getNbsp(2)+" if(i==0){ "+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"  order +=author2[i]+',';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+"  }else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				
				temp+= SysCardFormBeansUtil.getNbsp(2)+" 	order += author2[i].split('&')[1];"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+" 	if(!(i==author2.length-1)){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+" 	order +=',';}"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				//temp+= SysCardFormBeansUtil.getNbsp(2)+" author2[i]=author2[2].split('&')[1];"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				//temp+= SysCardFormBeansUtil.getNbsp(2)+" author2[i]=author2[3].split('&')[1];"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				//temp+= SysCardFormBeansUtil.getNbsp(2)+" var order +=author2[1]; "+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+= SysCardFormBeansUtil.getNbsp(2)+" }"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				temp+= SysCardFormBeansUtil.getNbsp(2)+" }"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		url=url+'&'+'author1='+author1+'&order='+order;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			

				
						}
		}else {
			if (buttonType.equals("batchEdit")) {
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[0]+"'"+a3+";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}else{
				temp+= SysCardFormBeansUtil.getNbsp(2)+"var url='"+aa[0]+"';"+SysCardFormBeansUtil.NEW_LINE_ONLY;
						//加密
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		var author=null;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		if(url.indexOf('Https')||url.indexOf('Http')||url.indexOf('www.')||url.indexOf('http')||url.indexOf('https')){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"			}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		author =((url.replace((url.substring(0, url.indexOf('?')+1)),'')));"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		var author1 = md5(author);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				
				
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		url=url+'&'+'author1='+author1;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"		}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}
		}
		if (buttonType.equals("copyAndUpdate")) {
			temp+= SysCardFormBeansUtil.getNbsp(1)+"url=url+'&buttonType=copyAndUpdate'"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		
		temp+= SysCardFormBeansUtil.getNbsp(1)+"layer.open({"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"type: 2,"+SysCardFormBeansUtil.NEW_LINE_ONLY; 
		temp+=SysCardFormBeansUtil.getNbsp(2)+"title:title,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"shadeClose: true,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"shade: 0.8,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp+=SysCardFormBeansUtil.getNbsp(2)+"offset: ['0px', '0px'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp+=SysCardFormBeansUtil.getNbsp(2)+"maxmin:true,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"zIndex:9999,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp+=SysCardFormBeansUtil.getNbsp(2)+"offset: '0px',"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"area: ['950px', '500px'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		String layerWidth="1000";
//		String layerHeight="500";
//		String heightString = button.getButtonAddHeight().substring(button.getButtonAddHeight().length()-1,button.getButtonAddHeight().length());
//		String widthString = button.getButtonAddWidth().substring(button.getButtonAddWidth().length()-1,button.getButtonAddWidth().length());
//		if (heightString.equals("%")==true && widthString.equals("%")==true) {
//			temp+=SysCardFormBeansUtil.getNbsp(2)+"area: ['"+button.getButtonAddWidth()+"', '"+button.getButtonAddHeight()+"'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		}else{
//			if (Integer.parseInt(button.getButtonAddWidth())<1001) {
//				layerWidth=button.getButtonAddWidth();
//			}
//			if (Integer.parseInt(button.getButtonAddHeight())<500) {
//				layerHeight=button.getButtonAddHeight();
//			}
//			temp+=SysCardFormBeansUtil.getNbsp(2)+"area: ['"+layerWidth+"', '"+layerHeight+"'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		}	
		
		temp+=SysCardFormBeansUtil.getNbsp(2)+"content: url,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"success:function(layero,index){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"$('.layui-layer-max').click();\n";
//		temp+=SysCardFormBeansUtil.getNbsp(2)+"layer.setTop(layero);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp+=SysCardFormBeansUtil.getNbsp(2)+"$('.layui-layer-max').click();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"},"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"end:function(){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"    refresh_"+formCode+"();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"});"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
		
	}
	
	public static String getReloadJsStr(String gridname,String successType,String type){
		
		String st = "";
		if(type.equals("2")){
			if("2".equals(successType)){ //reload
				st = gridname+".reload()"+SysCardFormBeansUtil.NEW_LINE;
			}
			if("1".equals(successType)){ //load
				st = gridname+"_fun()"+SysCardFormBeansUtil.NEW_LINE;
				
			}
			if("3".equals(successType)){ //自定义
				st = "_reloadData()"+SysCardFormBeansUtil.NEW_LINE;
			}
			//考虑从表更新主表
			st += SysCardFormBeansUtil.getNbsp(4)+"if(window.parent._loadmstmaindata){"+SysCardFormBeansUtil.NEW_LINE;
			st += SysCardFormBeansUtil.getNbsp(5)+"window.parent._loadmstmaindata()"+SysCardFormBeansUtil.NEW_LINE;
			st += SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE;
		}
		
		return st;
	}

	/**
	 * 新增一行方式
	 * @param button
	 * @param string
	 * @param buttonAddSuccessDeal
	 * @param url
	 * @param string2
	 * @param b
	 * @return
	 */
	public static String getGridFormSaveJsAddRow(SysFormButtonAddOrEditCache button,SysFormButtonCache b, String funName,String successType,String url, String gridname,List<SysFormColumnCache> columns,SysRequestParam request) {
		// TODO 自动生成方法存根
		String temp = "";
		//根据初始化值初始化新增行的数据
		
		temp += SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+b.getFormButtonCheckFun()+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"var newRow={"+getDefaultValue(columns,request)+"}"+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+gridname+".addRow(newRow)"+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	public static String getDefaultValue(List<SysFormColumnCache> columns,SysRequestParam request){
		String temp = "";
		for(SysFormColumnCache column:columns){
			String _value = null;
			//权限判断暂时略过
			
			//根据不同控件设置不同值
			if("1".equals(column.getFormColumnGuiType())||"8".equals(column.getFormColumnGuiType())){
				SysTextBoxCache textbox  = (SysTextBoxCache)column.getFormColumGui();
				if(textbox!=null&&!"0".equals(textbox.getTextboxid())){
					if(!"initFun".equals(textbox.getTextboxDataFromType()))
						_value =  SysCardFormColumnUtil.getDefaultValue(request, textbox.getTextboxDataFromType(), textbox.getTextboxDataFromValue());
				}
			}else if("2".equals(column.getFormColumnGuiType())){
				SysFormComboboxCache combobox = (SysFormComboboxCache)column.getFormColumGui();
				if(combobox!=null&&combobox.getComboboxId()!=0){
					if(!"initFun".equals(combobox.getComboboxDataFromType()))
						_value =  SysCardFormColumnUtil.getDefaultValue(request, combobox.getComboboxDataFromType(),combobox.getComboboxDataFromValue());
				}
			}else if("5".equals(column.getFormColumnGuiType())){
				SysCheckboxCache checkbox = (SysCheckboxCache)column.getFormColumGui();
				if(checkbox!=null&&checkbox.getCheckboxId()!=0){ 
					if(!"initFun".equals(checkbox.getCheckboxDataFromType())){
						_value =  SysCardFormColumnUtil.getDefaultValue(request, checkbox.getCheckboxDataFromType(),checkbox.getCheckboxDataFromValue());
					}
				}
				
			}else if("6".equals(column.getFormColumnGuiType())){
				SysDatepickerCache datepicker = (SysDatepickerCache)column.getFormColumGui();
				if(datepicker!=null&&datepicker.getDatepickerId()!=0){ 
					if(!"initFun".equals(datepicker.getDatepickerDataFromType())){
						_value =  SysCardFormColumnUtil.getDefaultValue(request, datepicker.getDatepickerDataFromType(),datepicker.getDatepickerDataFromValue());
					}
				}
			}
			String[] ef = column.getFiledName();
			temp+= ef[1]+":"+(_value==null?"''":"'"+_value+"'")+",";
		}
		if(temp.length()>0){
			temp = temp.substring(0,temp.length()-1);
		}
		return temp;
	}

	/**
	 * 获取grid删除的js逻辑  页面删除
	 * @param button
	 * @param string
	 * @param string2
	 * @return
	 */
	public static String getGridFormRemoveJs(SysFormButtonRemoveCache button, String funName, String gridname) {
		// TODO 自动生成方法存根
		String temp = "";
		temp+= SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"var _rows = "+gridname+".getSelecteds()"+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"if(_rows==null||_rows.length<=0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"nui.alert('请选择一行数据');return ;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"}else {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		//grid.removeRows(rows);
		temp+=SysCardFormBeansUtil.getNbsp(1)+gridname+".removeRows(_rows)"+SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		return temp;
	}
	
	/**
	 * 导出js
	 * @param request
	 * @param formconfig
	 * @param button
	 * @param funName
	 * @return
	 */
	public static String getGridFormExport(SysRequestParam request, SysFormconfigCache formconfig, SysFormButtonExportCache button, String funName) {
		String temp = "";
		String filename = "";
		if (button.getButtonExportFilename()!=null && !button.getButtonExportFilename().equals("")) {
			filename = button.getButtonExportFilename();
		}else {
			filename = formconfig.getFormDefName();
		}
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "function " + funName +"_"+formconfig.getFormDefId()+"(){"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "var url = '/myehr/jsp/form/button/exportButton.jsp?formId="+formconfig.getFormDefId()+"&buttonId="+button.getFormButtonId()+"&filename="+filename+"';"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.open({"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "	   type: 2,"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    title:'导出字段',"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    shadeClose: true,"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    shade: 0.8,"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    maxmin:true,"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    zIndex:9999,"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    area: ['950px', '500px'],"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    content: url,"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    success:function(layero,index){"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"$('.layui-layer-max').click();\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	   },"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "	   end:function(){"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "	   }"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    });"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(1) + "}"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "function exportData_"+formconfig.getFormDefId()+"(fieldArray){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "var _filterdata={};" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "_filterdata.buttonId="+button.getFormButtonId()+";" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "_filterdata.fieldArray=fieldArray;" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "_filterdata.filename='"+button.getButtonExportFilename()+"';" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "_filterdata=grid_"+formconfig.getFormDefCode()+"_fun(_filterdata);" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp += SysCardFormBeansUtil.getNbsp(2) + "jQuery('#activity_pane').showLoading({'addClass': 'loading-indicator'});" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "var index = layer.load(1);\n" ;
		temp += SysCardFormBeansUtil.getNbsp(2) + "var url='/myehr/form/exportCardformInitData.action';" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "$.ajax({" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    url:url," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    type:'post'," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    data: _filterdata," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    cache: false," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    contentType: 'application/json;charset=utf-8'," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    success: function (text) {" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        layer.close(index);" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        if(text[0]==0){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "            alert(\"导出失败！\");" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp += SysCardFormBeansUtil.getNbsp(2) + "            jQuery('#activity_pane').hideLoading();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        }else if(text[0]=='error'){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "            alert(\"操作异常\");" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp += SysCardFormBeansUtil.getNbsp(2) + "            jQuery('#activity_pane').hideLoading();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        }else if(text[0]=='BIG'){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp += SysCardFormBeansUtil.getNbsp(2) + "            jQuery('#activity_pane').hideLoading();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "            document.getElementById(\"exportBig_"+button.getFormButtonId()+"\").click();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        }else if(text[0]=='SMALL'){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		//temp += SysCardFormBeansUtil.getNbsp(2) + "            jQuery('#activity_pane').hideLoading();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "            document.getElementById(\"export_"+button.getFormButtonId()+"\").click();" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        }" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    }," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    error: function (XMLHttpRequest, textStatus, errorThrown) {" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        layer.close(index);" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        alert(\"导出异常\");" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "        alert(textStatus);\nalert(XMLHttpRequest.status);\n" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "    }," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "});" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		/*List<SysFormWhereCache> wheres = formconfig.getWheres();// 获取条件
		String whereObj = SysCardFormBeansUtil.getParamStringJson(wheres, request,"");
		
		String fileName = button.getButtonExportFilename();
		if (fileName!=null && !fileName.equals("")) {
			fileName = formconfig.getFormDefName();
		}
		temp += SysFormGeneralExecSql.getDataGridParamsJsStr(formconfig.getParams(), null, request);
		String json = "{paramsMap:paramsMap,requestParam:" + whereObj + ", filter:_filterdata, formId:_formId_"	+ formconfig.getFormDefCode() + ", heightGrade:_heightGrade, fileName:\"" + fileName + "\",buttonId:"+button.getFormButtonId()+"}";
		
		temp += SysCardFormBeansUtil.getNbsp(2) + "var json = nui.encode(" + json + ");" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(2) + "dcf.mask();" + Constants.NEWLINE;
		// ajax组装
		temp += SysCardFormBeansUtil.getNbsp(2) + "nui.ajax({" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "url: \"com.dcf.form.runtime.cardFormButton.exportGridData.biz.ext\"," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "data: json," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "type: 'POST', cache: false, contentType: 'text/json'," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "success: function (text) {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "dcf.unmask();" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "if (text.reCode == \"1\") {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "var downlogFile = \"com.dcf.util.fileupload.swffileupload.downFile.flow?contentType=\" + text.result.contentType + \"&filePath=\" + text.result.filePath + \"&clientFileName=\" + text.result.clientFileName;" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "self.location = encodeURI(downlogFile);" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "} else {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "if(text.message!=null && text.message!=\"\" && text.message!=\"null\") {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(6) + "nui.alert(text.message, \"提示\", function() {});" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "} else {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(6) + "nui.alert(\"导出数据失败，请联系系统管理员！\", \"提示\", function() {});" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "}" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "}" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "}," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "error: function (jqXHR, textStatus, errorThrown) {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "dcf.unmask();" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "nui.alert(jqXHR.responseText);" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "}" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(2) + "});" + Constants.NEWLINE;*/
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		return temp;
	}
	
	/**
	 * 导出js
	 * @param request
	 * @param formconfig
	 * @param button
	 * @param funName
	 * @return
	 */
	public static String getGridFormImport(SysRequestParam request, SysFormconfigCache formconfig, SysFormButtonImportCache button, String funName) {
		String temp = "";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "function " + funName + "_"+formconfig.getFormDefId()+"() {"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.getNbsp(2) + "var url = \"" + request.getContextPath() + "/jsp/form/button/importButton.jsp?formId=" + formconfig.getFormDefId() + "&templateFileid=" + button.getButtonImportTemplate() + "&templateFilename=" + button.getButtonImportSql() + "&buttonId="+button.getFormButtonId()+"\";" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.open({" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "type: 2," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "title: \"选择导入的Excel\"," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "area: ['600', '500']," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "shadeClose: true," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "shade: 0.8," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "offset: ['0px', '0px']," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "content: url," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "onload: function () {}," + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "ondestroy: function (action) {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "if(action==\"ok\") {" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(5) + "grid_" + formconfig.getFormDefCode() + ".reload();" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(4) + "}" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + "}" + Constants.NEWLINE;
		temp += SysCardFormBeansUtil.getNbsp(2) + "});" + Constants.NEWLINE;
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		
		return temp;
	}
	
	/**
	 * 计算按钮js
	 * @param request
	 * @param formconfig
	 * @param button
	 * @param funName
	 * @return
	 */
	public static String getGridFormCalculate(SysRequestParam request, SysFormconfigCache formconfig, SysFormButtonCalculateCache button, String funName) {
		String temp = "";
		List<String[]> params1 = button.getParams();
		String tempParam = "";
		String tempCode = "";
		for(String[] param:params1){
			if("c".equals(param[0])){
				String str = param[1].substring(param[1].indexOf("_")+1);
				SysFormColumnCache sfc = formconfig.serchColumnByColumnId(Long.parseLong(str));
				String str1 = sfc.getJsId("id",formconfig.getFormDefId()+"");
				String str2 = str1.substring(str1.indexOf(".")+1);
				String tempGetListField = "";
				if("1".equals(formconfig.getFormDefLayoutType())){
					//str3 = SysCardFormBeansUtil.getNbsp(2) + "'c_" + str + "' : " + "nui.get('" + str1 +"').getValue(),\n" ;
				} else if("2".equals(formconfig.getFormDefLayoutType())||"10".equals(formconfig.getFormDefLayoutType())) {//列表
					
					String grid = "grid_" + formconfig.getFormDefCode();
					
					tempGetListField = "var " + str2.toLowerCase() + "Rows" + " = "+ grid + ".getSelecteds();\n" 
					+ "var " + str2.toLowerCase() + "Datas;\n"
					+ "if(" + str2.toLowerCase() + "Rows.length" + " > 0){\n"
					+ "var temp"+str2.toLowerCase()+"Datas = [];\n"
					+	"for(var i=0; i<" + str2.toLowerCase() + "Rows" + ".length; i++){\n"
					+ 	"	temp"+str2.toLowerCase()+"Datas.push(\"'\"+" + str2.toLowerCase() + "Rows" + "[i]."+ str2 +"+\"'\");\n"
					+	"}\n"
					+	str2.toLowerCase() + "Datas = temp"+str2.toLowerCase()+"Datas.join(',');\n"
					+ "}\n";
				}
				tempCode +=  tempGetListField +
					SysCardFormBeansUtil.getNbsp(1) + "var c_" + param[1].substring(param[1].indexOf("_")+1) + " = " + str2.toLowerCase() + "Datas;" + SysCardFormBeansUtil.NEW_LINE_ONLY;
				tempParam += "+'&c_" + param[1].substring(param[1].indexOf("_")+1) + "='" + "+c_" + param[1].substring(param[1].indexOf("_")+1);
			} else if("s".equals(param[0])){
				String str = param[1];
				String str1 = SysCardFormColumnUtil.getDefaultValue(request,"session",str);
				if(str1!=null){
					String str2 = "+'&s_" + param[1] + "=" + str1+"'";
					tempParam += str2;
				}
			} else if("r".equals(param[0])){
				String str = param[1];
				String str1 = SysCardFormColumnUtil.getDefaultValue(request,"request",str);
				if(str1!=null){
					String str2 = "+\'&r_" + param[1] + "=" + str1+"'";
					tempParam +=str2;
				}
			}
		}
		
		String url="'" + "/myehr/jsp/form/button/excSolution.jsp"+"?formId="+formconfig.getFormDefId()+"&formButtonId="+button.getButton().getFormButtonId() + "'"  + tempParam;
		Map<String, List<SysFormGeneralParamCache>> params = button.getButton().getGeneralParams();
		String endtype = FormParamConstants.BUTTON_CALCULATE;
		List<SysFormGeneralParamCache>  l = params.get(endtype);
		String paramStr = SysCardFormColumnUtil.getGeneralParamStr(l,request);
		if(paramStr!=null&&!"".equals(paramStr)){
			url = url +"&" + paramStr;
		}
		temp += SysCardFormBeansUtil.getNbsp(0) + "function " + funName +"_"+formconfig.getFormDefId()+ "(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY
				+ tempCode;
		
		if("2".equals(formconfig.getFormDefLayoutType())||"10".equals(formconfig.getFormDefLayoutType())){ //列表式布局
			String gridname = "grid_"+formconfig.getFormDefCode();
			if(url.indexOf("_srow")>0){
				temp += SysCardFormBeansUtil.getNbsp(1) + "var _srows="+gridname+".getSelecteds()" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(1) + "if(!_srows) {" + SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp += SysCardFormBeansUtil.getNbsp(2) + "nui.alert('请选中一行数据!')" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(2) + "return" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp += SysCardFormBeansUtil.getNbsp(1) + "var _srow=deal_srows(_srows)" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(1) + "if(!_srow){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp += SysCardFormBeansUtil.getNbsp(2) + "nui.alert('请选中一行数据!')" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(2) + "return" + SysCardFormBeansUtil.NEW_LINE;
				temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
			}
		}
		
		if(button.getButton().getPojo().getFormButtonCheckFun()!=null&&!button.getButton().getPojo().getFormButtonCheckFun().equals("")){
			String[] preFunction = button.getButton().getPojo().getFormButtonCheckFun().split(";");
			if (preFunction[0].toUpperCase().equals("SQLFORBID")) {
				temp+=SysCardFormBeansUtil.getNbsp(1)+"var sql = \""+preFunction[1]+"\";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(1)+"var data = myehr_SQLFORBID_sql(sql);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(1)+"if(data.length>0&&data[0]."+preFunction[3]+"=='"+preFunction[4]+"'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;

				temp+=SysCardFormBeansUtil.getNbsp(2)+"layer.alert('"+preFunction[2]+"', {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(3)+"icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(3)+" skin: 'layer-ext-moon' //"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(2)+"})"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				temp+=SysCardFormBeansUtil.getNbsp(1)+"}else{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}
			temp+= SysCardFormBeansUtil.getNbsp(1)+"layer.open({"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			/*if (url.indexOf("encodeURI") < 0) {
				url = "encodeURI(" + url + ")";
			}*/
			String gridname1 = "grid_"+formconfig.getFormDefCode();
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"type: 2,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"showMaxButton: true,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"title:\"选择公式\" ,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"area: ['600', '500'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"offset: ['0px', '0px'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"content:" + url + ","+SysCardFormBeansUtil.NEW_LINE_ONLY; //showMaxButton: false,
			temp+=SysCardFormBeansUtil.getNbsp(2)+"onload:function(){ "+SysCardFormBeansUtil.NEW_LINE_ONLY;

			temp+=SysCardFormBeansUtil.getNbsp(2)+"},"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"end: function (){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+" refresh_"+formconfig.getFormDefCode()+"();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil. getNbsp(1)+" }); "+SysCardFormBeansUtil.NEW_LINE_ONLY;
			if (preFunction[0].toUpperCase().equals("SQLFORBID")) {
				
				temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			}

			temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
				  
				 
				
		}else {
			
			temp+= SysCardFormBeansUtil.getNbsp(1)+"layer.open({"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			/*if (url.indexOf("encodeURI") < 0) {
				url = "encodeURI(" + url + ")";
			}*/
			String gridname1 = "grid_"+formconfig.getFormDefCode();
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"type: 2,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"showMaxButton: true,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"title:\"选择公式\" ,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"area: ['600', '500'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"offset: ['0px', '0px'],"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"content:" + url + ","+SysCardFormBeansUtil.NEW_LINE_ONLY; //showMaxButton: false,
			temp+=SysCardFormBeansUtil.getNbsp(2)+"onload:function(){ "+SysCardFormBeansUtil.NEW_LINE_ONLY;

			temp+=SysCardFormBeansUtil.getNbsp(2)+"},"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"end: function (){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(4)+" refresh_"+formconfig.getFormDefCode()+"();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil. getNbsp(1)+" }); "+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		}
		
		
		return temp;
	}
	

	
	/**
	 * 生成删除按钮对应默认方式js
	 * @param button
	 * @param pkColumnName 主键字段名称
	 * @param funName 函数名称
	 * @param gridname grid js变量名称
	 * @return js文本
	 */
	public static String getGridFormRemoveDefaultJs(SysFormButtonRemoveCache button,String pkColumnName, String funName,String gridname,String formCode,String formId,String preJs) {
		// TODO 自动生成方法存根 
		String temp = "";
		
		String st = getReloadJsStr(gridname,"2","2");
		temp+= SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"_"+formId+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		String url = "";
		if("fun".equals(button.getButtonRemoveExcType())){
			temp+= button.getButtonRemoveFun()+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}else {
			if("default".equals(button.getButtonRemoveExcType())){
				url = GRID_REMOVE_BUTTON_DEFAULT_URL;
			}else if("url".equals(button.getButtonRemoveExcType())){
				url = button.getButtonRemoveUrl();
			}
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var selects = $(\"#"+formCode+"_List\").bootstrapTable('getSelections');"+SysCardFormBeansUtil.NEW_LINE;
			if (preJs!=null&&preJs.indexOf(";")!=-1) {
				String[] str= preJs.split(";");
				if (str[0]!=null&&str[0].equals("forbid")) {
					temp+=SysCardFormBeansUtil.getNbsp(1)+"if(selects[0]."+str[1]+"){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(2)+"alert(\""+str[2]+"\")"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}else if (str[0]!=null&&str[0].toUpperCase().equals("SQLFORBID")) {
					temp+=SysCardFormBeansUtil.getNbsp(1)+"var sql = \""+str[1]+"\";"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"var data = myehr_SQLFORBID_sql(sql);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"if(data.length>0&&data[0]."+str[3]+"=='"+str[4]+"'){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(2)+"layer.alert('"+str[2]+"', {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(3)+"icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(3)+"skin: 'layer-ext-moon' //"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(2)+"})"+SysCardFormBeansUtil.NEW_LINE_ONLY;
					temp+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
					temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
				}
			}
			temp+=SysCardFormBeansUtil.getNbsp(1)+"if(selects!=null && selects.length>0){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"select = selects[0]"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}else {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"alert('请选择数据');return ;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var r=confirm(\"确定删除选中记录？\");"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"if (r==true){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"var _rowvalids = []"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"for(var i=0;i<selects.length;i++) {"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"if (selects[i]." + pkColumnName + "!=\"\") {"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"_rowvalids.push(selects[i])"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			//获取主键字段
			temp+=SysCardFormBeansUtil.getNbsp(2)+"var  _param ={formId:_formId_"+formId+",params:_rowvalids}"+SysCardFormBeansUtil.NEW_LINE;
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"var  buttonId = '"+button.getButtonRemoveButtonId()+"';"+SysCardFormBeansUtil.NEW_LINE;
			
			temp+=SysCardFormBeansUtil.getNbsp(2)+"if(\"undefined\" != typeof setCacadeParam ){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"setCacadeParam(_param)"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}"+SysCardFormBeansUtil.NEW_LINE;
			
		
			
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var signstr = JSON.stringify(_param);" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var rule ='\"' ;" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var regS = new RegExp(rule,'g');" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var rule2 =':' ;" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var regS2 = new RegExp(rule2,'g');" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"signstr = signstr.replace(regS, '');" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"signstr = signstr.replace(regS2, '=');" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			
			temp+=SysCardFormBeansUtil.getNbsp(3)+"var sign = md5(signstr);" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
			
												//temp+=SysCardFormBeansUtil.getNbsp(1)+"alert(nui.encode(_param))"+SysCardFormBeansUtil.NEW_LINE;
			
			//处理级联删除问题 
			//temp+=SysCardFormBeansUtil.getNbsp(2)+"dcf.mask()"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"$.ajax({" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"url:'"+url+"?sign='+sign," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"type:'post'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"data: JSON.stringify(_param)," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"cache: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"contentType: 'application/json;charset=utf-8'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"success: function (text) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			//temp+=SysCardFormBeansUtil.getNbsp(5)+"dcf.unmask()"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"if(text!='1'&&text!='-1'){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(5)+"alert('删除失败')" +SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"} else if(text=='-1'){" +SysCardFormBeansUtil.NEW_LINE_ONLY;	
			//	temp+=SysCardFormBeansUtil.getNbsp(5)+gridname+".removeRows(_rows)"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(5)+" alert('请求参数错误')" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			//temp+=SysCardFormBeansUtil.getNbsp(5)+st +SysCardFormBeansUtil.NEW_LINE;//成功后处理方式
			temp+=SysCardFormBeansUtil.getNbsp(4)+"} else {" +SysCardFormBeansUtil.NEW_LINE_ONLY;			
			temp+=SysCardFormBeansUtil.getNbsp(5)+"refresh_"+formCode+"()" +SysCardFormBeansUtil.NEW_LINE;//成功后处理方式
			temp+=SysCardFormBeansUtil.getNbsp(5)+"refreshDictCache(_formId"+"_"+formId+",buttonId)" +SysCardFormBeansUtil.NEW_LINE;//成功后处理方式
			temp+=SysCardFormBeansUtil.getNbsp(5)+"if(\"undefined\" != typeof mstTab_activechanged ){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(6)+"mstTab_activechanged();" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(5)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"}," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"error: function (jqXHR, textStatus, errorThrown) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
//			temp+=SysCardFormBeansUtil.getNbsp(4)+"alert(<%=LangUtil.getLangByCode((String)session.getAttribute(\"langType\"),jqXHR.responseText)%>);" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"alert(jqXHR.responseText);" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"});" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			// 
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}else{" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"return;" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
		}
		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	/**
	 * 生成保存按钮对应方式js
	 * @param button
	 * @param pkColumnName 主键字段名称
	 * @param funName 函数名称
	 * @param gridname grid js变量名称
	 * @param request 
	 * @param form 
	 * @param hzsql 
	 * @return js文本
	 */
	public static String getGridFormSaveJs(SysFormButtonSaveCache buttonsave, String funName, String gridname,String formCode, SysFormGeneralExecSqlCache hzsql,SysFormconfigCache form, SysRequestParam request) {
		// TODO 自动生成方法存根
//		/buttonsave.getButtonSaveExcType()
		List<String[]> params = hzsql.getParams();
		String[] res  = SysFormGeneralExecSqlCache.getDataGridButtonParamsJsStr(params, form, request);
		//String paramjs = SysFormGeneralExecSql.getDataGridButtonParamsJsStr(params, form, request);
		
		
		
		String excType = buttonsave.getButtonSaveExcType();
		String temp = "";
		temp+= SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(1)+res[1]+SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(1)+res[0]+SysCardFormBeansUtil.NEW_LINE;
		String succjs = "";
		String stype = buttonsave.getButtonSaveSuccessDeal();
		if("2".equals(stype)){
			succjs = gridname+".reload()"+SysCardFormBeansUtil.NEW_LINE;;
		}else if("1".equals(stype)){
			succjs = gridname+"_fun()"+SysCardFormBeansUtil.NEW_LINE;;
		}else if("3".equals(stype)){
			succjs = buttonsave.getButtonSaveSuccessFun()+SysCardFormBeansUtil.NEW_LINE;;
		}
		//考虑字表更新主表
		succjs += SysCardFormBeansUtil.getNbsp(4)+"if(window.parent._loadmstmaindata){"+SysCardFormBeansUtil.NEW_LINE;
		succjs += SysCardFormBeansUtil.getNbsp(5)+"window.parent._loadmstmaindata()"+SysCardFormBeansUtil.NEW_LINE;
		succjs += SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE;
		
		String url =  "";
		if("fun".equals(excType)){
			temp+=buttonsave.getButtonSaveFun()+SysCardFormBeansUtil.NEW_LINE_ONLY;
		}else {
			if("default".equals(excType)) {
				url = GRID_SAVE_BUTTON_DEFAULT_URL;
			}else if("url".equals(excType)) {
				url = buttonsave.getButtonSaveUrl();
			}
			temp+=SysCardFormBeansUtil.getNbsp(1)+gridname+".validate()"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"if("+gridname+".isValid() == false) {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"var error = "+gridname+".getCellErrors()[0]"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+gridname+".beginEditCell(error.record, error.column)"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"return"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+" var changeData = "+gridname+".getChanges()"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"if(changeData==null||changeData.length==0) return"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"var  _param ={formId:_formId_"+formCode+",datas:changeData,paramsMap:paramsMap}"+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"_param.buttonId="+buttonsave.getButtonSaveButtonId()+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"dcf.mask();" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"$.ajax({" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"url:'"+url+"'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"type:'post'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"data: nui.encode(_param)," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"cache: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"contentType: 'text/json'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"success: function (text) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"dcf.unmask();" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(3)+"if(!text.recode||text.recode[0]=='error'){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"nui.alert(text.recode[1])" +SysCardFormBeansUtil.NEW_LINE; 
			temp+=SysCardFormBeansUtil.getNbsp(3)+"} else {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(4)+succjs+SysCardFormBeansUtil.NEW_LINE;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"if(\"undefined\" != typeof mstTab_activechanged ){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(5)+"mstTab_activechanged();" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(4)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			
			temp+=SysCardFormBeansUtil.getNbsp(4)+"" +SysCardFormBeansUtil.NEW_LINE;//成功后处理方式
			temp+=SysCardFormBeansUtil.getNbsp(3)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}," +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"error: function (jqXHR, textStatus, errorThrown) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(2)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			temp+=SysCardFormBeansUtil.getNbsp(1)+"});" +SysCardFormBeansUtil.NEW_LINE_ONLY;
			//grid.removeRows(rows);
			
		}
		
		temp+=SysCardFormBeansUtil.getNbsp(0)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	public static String getGridFormButtonFunJs(String funName, String funJs) {
		String temp = "";
		
		temp += SysCardFormBeansUtil.getNbsp(0) + "function " + funName + "(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(1) + funJs + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		return temp;
	}
	
	

	
	
	/**
	 * 生成lookup 打开窗口js函数
	 * @param requst
	 * @param title
	 * @param width
	 * @param height
	 * @param winType
	 * @param formurl
	 * @param fromId
	 * @param jsId
	 * @param backValue 
	 * @param backType 
	 * @param fieldName 
	 * @param request 
	 * @param params 
	 * @param otherColumnJsId 
	 * @return
	 */
	public static String getLookupOpenJs(SysRequestParam request ,String title,String width,String height,String winType,String formurl,String fromId,
			String jsId, String fieldName, String backType, String backValue,String otherField,String formCode,String showProperty,String valueProperty, Map<String, List<SysFormGeneralParamCache>> params,String bizFunName,String lookupCustomDataback,String lookupCustomDatabackFun){
		String realUrl = "";
		if("1".equals(winType)){
			realUrl = request.getContextPath()+"/"+SysCardFormBeansUtil.LOOKUP_FORM_URL+"?formId="+fromId;
		}else {
			realUrl = request.getContextPath()+"/"+formurl;
		}
		
		if(params!=null){
			String endtype = FormParamConstants.COLUMN_LOOKUP_OPENPAGE;
			List<SysFormGeneralParamCache>  l = params.get(endtype);
			String paramStr = SysCardFormColumnUtil.getGeneralParamStr(l,request);
			if(paramStr!=null&&!"".equals(paramStr)){
				if(realUrl!=null&&realUrl.indexOf("?")>0){
					realUrl = realUrl +"&" +paramStr;
				}else {
					realUrl = realUrl+"?"+paramStr;
				}
			}
		}
		
		String gridName = "grid_"+formCode;
		String backValueJs = "";
		String onloadJs = "";
		onloadJs+=SysCardFormBeansUtil.getNbsp(5)+"var lookupid =row."+fieldName+SysCardFormBeansUtil.NEW_LINE;
		if("self".equals(backType)||"other".equals(backType)){
			boolean flag = false;
			if(otherField==null){
				otherField = fieldName;
				flag = true;
			}
			backValueJs += SysCardFormBeansUtil.getNbsp(1)+gridName+".cancelEdit();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"var _row = "+gridName+".getSelected();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+gridName+".updateRow(_row,{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(7)+fieldName+":id"+","+SysCardFormBeansUtil.NEW_LINE_ONLY;
		//	backValueJs += SysCardFormBeansUtil.getNbsp(7)+fieldName+":text"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs +=!flag?SysCardFormBeansUtil.getNbsp(7)+otherField+":text"+SysCardFormBeansUtil.NEW_LINE_ONLY:"";
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"});"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			onloadJs+=SysCardFormBeansUtil.getNbsp(5)+"var lookuptext =row."+otherField+SysCardFormBeansUtil.NEW_LINE;
		}else if("biz".equals(backType)){
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+gridName+".cancelEdit();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"var _row = "+gridName+".getSelected();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+gridName+".updateRow(_row,{"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(7)+fieldName+":data."+valueProperty+","+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"});"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			onloadJs+=SysCardFormBeansUtil.getNbsp(5)+"var lookuptext ="+bizFunName+"(lookupid)"+SysCardFormBeansUtil.NEW_LINE;
		}else if("sql".equals(backType)){
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+gridName+".cancelEdit();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"var _row = "+gridName+".getSelected();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"_row."+fieldName+"=id;"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+gridName+".updateRow(_row);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			backValueJs += SysCardFormBeansUtil.getNbsp(6)+"alert(grid_myorglist.getSelected().ORGSEQ);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			//backValueJs += SysCardFormBeansUtil.getNbsp(6)+"alert(e);e.row."+fieldName+"=id"+SysCardFormBeansUtil.NEW_LINE_ONLY;
			//onloadJs+=SysCardFormBeansUtil.getNbsp(5)+"var lookuptext ="+bizFunName+"(lookupid)"+SysCardFormBeansUtil.NEW_LINE;
		}
		
		String temp = "";
		temp+=SysCardFormBeansUtil.getNbsp(2)+"nui.open({"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		if (realUrl.indexOf("encodeURI") < 0) {
			realUrl = "encodeURI(\"" + realUrl + "\")";
		}
		
		temp+=SysCardFormBeansUtil.getNbsp(3)+"url:" + realUrl + ","+SysCardFormBeansUtil.NEW_LINE_ONLY; //showMaxButton: false,
		temp+=SysCardFormBeansUtil.getNbsp(3)+"showMaxButton: true,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"title:\""+title+"\" ,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"width:\""+width+"\" ,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"height:\""+height+"\" ,"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"onload:function(){ "+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(4)+"var row = "+gridName+".getSelected();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(4)+"if(row){"+SysCardFormBeansUtil.NEW_LINE_ONLY
			+onloadJs;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"var _data = {"+valueProperty+":lookupid,"+showProperty+":lookuptext};"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"var iframe = this.getIFrameEl();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp+=SysCardFormBeansUtil.getNbsp(5)+"iframe.contentWindow.SetData(_data);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"},"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"ondestroy: function (action,e){"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"if (action == 'ok') {"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"iframe = this.getIFrameEl();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"var data = iframe.contentWindow.GetData();"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"data = nui.clone(data);"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(5)+"if (data) {	"+SysCardFormBeansUtil.NEW_LINE_ONLY;//+backValueJs;
		
		
        temp+=SysCardFormBeansUtil.getNbsp(6)+"var arr=nui.decode(data);\n";
		
		temp+=SysCardFormBeansUtil.getNbsp(6)+"var id='';\n";
		temp+=SysCardFormBeansUtil.getNbsp(6)+"var text='';\n";

		temp+=SysCardFormBeansUtil.getNbsp(6)+"for(var i=0;i<arr.length;i++){ \n";
		
		temp+=SysCardFormBeansUtil.getNbsp(7)+"if(i==arr.length-1) { \n";
		temp+=SysCardFormBeansUtil.getNbsp(8)+" id += arr[i]."+valueProperty+";\n";
		temp+=SysCardFormBeansUtil.getNbsp(8)+" text += arr[i]."+showProperty+";\n";

		temp+=SysCardFormBeansUtil.getNbsp(7)+"} else { \n";
		temp+=SysCardFormBeansUtil.getNbsp(8)+" id += arr[i]."+valueProperty+"+',';\n";
		temp+=SysCardFormBeansUtil.getNbsp(8)+" text += arr[i]."+showProperty+"+',';\n";
	
		temp+=SysCardFormBeansUtil.getNbsp(7)+"}\n";
		temp+=SysCardFormBeansUtil.getNbsp(6)+"}\n";
		temp+=SysCardFormBeansUtil.getNbsp(6)+backValueJs+"\n";
	//	temp+=SysCardFormBeansUtil.getNbsp(7)+"nui.get('"+jsId+"').setValue(id);\n";
		//temp+=SysCardFormBeansUtil.getNbsp(7)+"nui.get('"+jsId+"').setText(text);\n";
		
		//temp+=SysCardFormBeansUtil.getNbsp(6)+"alert(id);"+"\n";
		if("Y".equals(lookupCustomDataback)){
			temp+=SysCardFormBeansUtil.getNbsp(6)+lookupCustomDatabackFun+"\n";
		}
        
		temp+=SysCardFormBeansUtil.getNbsp(5)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+" }); "+SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultLookupRenderer(String funName){
		String temp = "";
		temp+=SysCardFormBeansUtil.getNbsp(1)+" return "+funName+"(e.value)" +SysCardFormBeansUtil.NEW_LINE;
		return temp;
	}
	
	public static String getDefaultLookupRendererSql(String funName){
		String temp = "";
		temp+=SysCardFormBeansUtil.getNbsp(1)+"return "+funName+"(e)" +SysCardFormBeansUtil.NEW_LINE;
		return temp;
	}
	
	
	/**
	 *  look回填 根据回填id获取回填text
	 * @return
	 */
	public static String getDefaultLookupBiz(String showProperty,String valueProperty,String url,String funName){
		String temp = "";
		temp+= SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(_value){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"var returndata = null" +SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"$.ajax({" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"url:'"+url+"'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"type:'post'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"data: nui.encode({"+valueProperty+":_value})," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"async: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"cache: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"contentType: 'text/json'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"success: function (text) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"if(text.data){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp+=SysCardFormBeansUtil.getNbsp(4)+"returndata = text.data."+ showProperty+SysCardFormBeansUtil.NEW_LINE; 
		temp+=SysCardFormBeansUtil.getNbsp(3)+"}else {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp+=SysCardFormBeansUtil.getNbsp(4)+"alert('读取字段lookup Text失败!')" +SysCardFormBeansUtil.NEW_LINE; 
		
		temp+=SysCardFormBeansUtil.getNbsp(3)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"}," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"error: function (jqXHR, textStatus, errorThrown) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"})" +SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(1)+"return returndata" +SysCardFormBeansUtil.NEW_LINE;
		temp+= SysCardFormBeansUtil.getNbsp(1)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	
	/**
	 *  look回填 根据sql获取回填text
	 * @return
	 */
	public static String getDefaultLookupSql(String url,String funName, SysFormColumnCache column, SysRequestParam request, SysFormGeneralExecSqlCache backsql){
		
		
		String jsParams = backsql.getDataGridRendererParamsJsStr(column.getForm(), request);
		
		
		String temp = "";
		temp += SysCardFormBeansUtil.getNbsp(0)+"function "+funName+"(e){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2)+"var returndata = null" +SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(3) + jsParams;
		temp += SysCardFormBeansUtil.getNbsp(4) +   "var data = {" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) +		"'formId' : _formId_"+column.getForm().getFormDefCode()+"," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) +		"'columnId' : "+column.getFormColumnId()+"," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) +		"'execSqlRelaid' : "+backsql.getExecSqlRelaid()+"," + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) +		"'params' : paramsMap" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) +     "};" +   SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2)+"$.ajax({" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"url:'"+url+"'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"type:'post'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"data: nui.encode(data)," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"async: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"cache: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"contentType: 'text/json'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"success: function (text) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4)+"if(text.datas[0]='000000'){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.getNbsp(5)+"returndata = text.datas[1];"+SysCardFormBeansUtil.NEW_LINE; 
		temp += SysCardFormBeansUtil.getNbsp(4)+"}else {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.getNbsp(5)+"alert('读取字段lookup Text失败!')" +SysCardFormBeansUtil.NEW_LINE;
		
		temp += SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"}," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"error: function (jqXHR, textStatus, errorThrown) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2)+"})" +SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(2)+"return returndata" +SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(1)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	
	/**
	 *  combobox回填 combobox下拉为sql查询结果，其回填将自动生成回填的renderer方法
	 * @return
	 */
	public static String getComboboxSqlRenderer( SysFormColumnCache column, SysRequestParam request,SysFormComboboxCache combobox, SysFormconfigCache form){
		String temp = "";
		String[] results = SysFormGeneralExecSqlCache.getSqlJsStr(combobox,request, column.getFormFiledTablename(),combobox.getComboboxFormColumnId(),form,column.getJsId("js",form.getPojoform().getFormDefId().toString()),false);
		if (results==null) {
			return "";
		}
		String paramJson = results[1];
		String qjs = results[0];
		temp = qjs;
		temp+=SysCardFormBeansUtil.getNbsp(3)+paramJson+SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(2)+"var returndata = null" +SysCardFormBeansUtil.NEW_LINE;
		temp+=SysCardFormBeansUtil.getNbsp(3)+"var url = \""+SysCardFormBeansUtil.QUERY_SQL_URL+"\""+SysCardFormBeansUtil.NEW_LINE	;
		temp+= SysCardFormBeansUtil.getNbsp(3)+"nui.ajax({"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
		temp+= SysCardFormBeansUtil.getNbsp(4)+"url: url,"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
		temp+= SysCardFormBeansUtil.getNbsp(4)+"type: 'POST',"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
		temp+= SysCardFormBeansUtil.getNbsp(4)+"data: nui.encode({params:params,_value:e.value}),"+SysCardFormBeansUtil.NEW_LINE_ONLY;;
		temp += SysCardFormBeansUtil.getNbsp(3)+"async: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"cache: false," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"contentType: 'text/json'," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"success: function (text) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4)+"if(text.data[0]){" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.getNbsp(5)+"returndata = text.data[0]."+combobox.getComboboxTextfield()+";"+SysCardFormBeansUtil.NEW_LINE; 
		temp += SysCardFormBeansUtil.getNbsp(4)+"}else {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.getNbsp(5)+"alert('读取字段Text失败!')" +SysCardFormBeansUtil.NEW_LINE;
		
		temp += SysCardFormBeansUtil.getNbsp(4)+"}"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"}," +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"error: function (jqXHR, textStatus, errorThrown) {" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(3)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2)+"})" +SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(2)+"return returndata" +SysCardFormBeansUtil.NEW_LINE;
		return temp;
	}
	
	/**
	 * 获取链接字段的renderer函数
	 * @param url
	 * @return
	 */
	public static String getLinkRenderFunStr(String formCode,String cloumnName){
		String clickFunName = TEXT_BOX_LINK_CLICK_FUNNAME+"_"+formCode+"_"+cloumnName+"()";
		String jsstr = "return '<a  href=\"javascript:void(0);\" onclick=\""+clickFunName+"\"  >'+e.value+'</a>';";
		return jsstr;
	}
	
	/**
	 * 获取字段链接的点击事件函数
	 * @param textbox
	 * @param request
	 * @param lableName
	 * @return
	 */
	public static String getTextBoxLinkRenderFun(SysTextBoxCache textbox,SysRequestParam  request,String formCode,String cloumnName){
		String temp = "";
		String clickFunName = TEXT_BOX_LINK_CLICK_FUNNAME+"_"+formCode+"_"+cloumnName+"()";
	
		temp+= SysCardFormBeansUtil.getNbsp(0)+"function "+clickFunName+"{" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp+=SysCardFormBeansUtil.getNbsp(2)+"var _srow = grid_"+formCode+".getSelected();"+SysCardFormBeansUtil.NEW_LINE;
		if("2".equals(textbox.getTextboxLinkWinOpenType())){ //新窗口方式打开
			temp+=SysCardFormBeansUtil.getNbsp(2)+"window.open('"+getTextBoxLinkUrl(textbox,request)+"')" +SysCardFormBeansUtil.NEW_LINE;
		}else { //nui.open方式打开
			String successDealStr = "";
			if("reload".equals(textbox.getTextboxLinkSuccessDeal())){
				successDealStr = "grid_"+formCode+".reload();";
			}else if("load".equals(textbox.getTextboxLinkSuccessDeal())){
				successDealStr = "grid_"+formCode+".load();";
			}
			
			String url = getTextBoxLinkUrl(textbox,request);
			if (url.indexOf("encodeURI") < 0) {
				url = "encodeURI('" + url + "')";
			}
			
			temp += SysCardFormBeansUtil.getNbsp(2)+"nui.open({\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"url:" + url + ",\n"; //showMaxButton: false,
			temp += SysCardFormBeansUtil.getNbsp(3)+"showMaxButton: true,\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"title:\""+textbox.getTextboxLinkWinTitle()+"\" ,\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"width:\""+textbox.getTextboxLinkWinWidth()+"\" ,\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"height:\""+textbox.getTextboxLinkWinHeight()+"\" ,\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"onload:function(){ \n";
			temp += SysCardFormBeansUtil.getNbsp(4)+successDealStr+"\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"},\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"ondestroy: function (action){\n";
			temp += SysCardFormBeansUtil.getNbsp(4)+successDealStr+"\n";
			temp += SysCardFormBeansUtil.getNbsp(3)+"}\n";
			temp += SysCardFormBeansUtil.getNbsp(2)+" }); \n";
		}
		temp += SysCardFormBeansUtil.getNbsp(1)+"}" +SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	
	/** 
	 * 选择按钮生成GetData js代码（列表）
	 * @return
	 */

	public static String getSelectGetDataButtonJs(String formCode,String idField,String textField){
		String temp = "//选中按钮使用的GetData方法 （自定生成）"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) + "function GetData(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;		
		temp += SysCardFormBeansUtil.getNbsp(1) + "var selectGrid = nui.get('grid_"+formCode+"')" + SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(1) + "var selectNodes = selectGrid.getSelecteds()" + SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(1) + "if(selectNodes!=null&&selectNodes.length>0){" + SysCardFormBeansUtil.NEW_LINE_ONLY;

		temp += SysCardFormBeansUtil.getNbsp(2) + "return selectNodes"+ SysCardFormBeansUtil.NEW_LINE;

		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		
		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}


	
	/**
	 * 按钮点击函数js
	 * @param formCode
	 * @param idField
	 * @param textField
	 * @return
	 */
	
	public static String getSelectClickButtonJs(String formCode,String btnCode,String idField,String textField,String formId){
		
		
		
		String temp = "//选中按钮使用的click方法 （自定生成）"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) + "function "+btnCode+formCode+"_click_"+formId+"(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(1) + "var selectGrid = $(\"#"+formCode+"_List\").bootstrapTable('getSelections');" + SysCardFormBeansUtil.NEW_LINE;
//		temp += SysCardFormBeansUtil.getNbsp(1) + "var selectNodes = selectGrid.getSelecteds()" + SysCardFormBeansUtil.NEW_LINE;
		temp += SysCardFormBeansUtil.getNbsp(1) + "var parentId = '${param.fatherId}';\n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "var intrToId = '${param.intrToId}';\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "var intrButtonId = '${param.intrButtonId}';\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "var idField = '${param.idField}';\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "var textField = '${param.textField}';\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "if(intrToId==''&&intrButtonId==''){\n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "if(selectGrid!=null&&selectGrid.length>0){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(2) + "parent.document.getElementById(parentId).value=selectGrid[0][idField];\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "parent.document.getElementById(parentId+'_name').value=selectGrid[0][textField];\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "var index = parent.layer.getFrameIndex(window.name ); //获取当前窗体索引\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "parent.layer.close(index); //执行关闭\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}else {\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "alert('请选择数据');\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "if(selectGrid!=null&&selectGrid.length>0){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(4) + "gridIntroduce(selectGrid);\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "alert('请选择数据');\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "}\n";
		
		temp += SysCardFormBeansUtil.getNbsp(0) + "function getUrlParam(key){\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "	// 获取参数\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "	var url = window.location.search;\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    // 正则筛选地址栏\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    var reg = new RegExp(\"(^|&)\"+ key +\"=([^&]*)(&|$)\");\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    // 匹配目标参数\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    var result = url.substr(1).match(reg);\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    //返回参数值\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "    return result ? decodeURIComponent(result[2]) : null;\n";
		temp += SysCardFormBeansUtil.getNbsp(0) + "}\n";
		
		temp += SysCardFormBeansUtil.getNbsp(0) + "function gridIntroduce(selectGrid){\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "var introduceParam = {};  \n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "var creatParam = {};  \n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "introduceParam.columns = selectGrid;  \n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "	var creatParams ;\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "	var creatParam={} ;\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "	var requestParams ;\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "	var requestParam={} ;\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "	$.ajax({\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		url:'/myehr/button/getCreateParamById.action?intrToId=${param.intrToId}&intrButtonId=${param.intrButtonId}',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		type:'post',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		cache: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		async: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		contentType: 'application/json;charset=utf-8',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "success: function (data) {\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "if(data!=null){\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "creatParams = data;\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "creatParams = null;\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "});\n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "	$.ajax({\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		url:'/myehr/button/getRequestParamById.action?intrToId=${param.intrToId}&intrButtonId=${param.intrButtonId}',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		type:'post',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		cache: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		async: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "		contentType: 'application/json;charset=utf-8',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "success: function (data) {\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "if(data!=null){\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "requestParams = data;\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "requestParams = null;\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "});\n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "if(requestParams!=null){\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "for(var i = 0 ;i<requestParams.length;i++){\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "requestParam[requestParams[i]] = getUrlParam(requestParams[i]);\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "introduceParam.requestParam = requestParam;\n";	
		temp += SysCardFormBeansUtil.getNbsp(1) + "}\n";
		
		temp += SysCardFormBeansUtil.getNbsp(1) + "if(creatParams==null){\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "return null;\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "var content = \"\";\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "content +=  \"<div id='createParam' class='row cell' style='border:none;line-height:25px;height:95%;overflow:auto;' >\\n\";\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "//循环\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "for(var i = 0 ;i<creatParams.length;i++){\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "content +=	\"	<div id='param1' class='col-lg-12 col-md-12 col-sm-12 col-xs-12'>\\n\"+\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "\"		<div class='ui search selection dropdown entitybox field-control' style='display: inline-block;width:100%;'>\\n\"+\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "\"			<label style='float:left;font-size:12px'>\"+creatParams[i]+\"</label>\\n\"+\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "\"			<input id='\"+creatParams[i]+\"' type=\\\"text\\\" class='form-control ' style='float:left;width:60%;'/>\\n\"+\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "\"		</div>\\n\"+\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "\"	</div>\\n\";\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "content +=	\"</div>\\n\";\n";
		
		temp += SysCardFormBeansUtil.getNbsp(4) + "if(creatParams.length>0){\n";
		
		temp += SysCardFormBeansUtil.getNbsp(3) + "layer.open({\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "title:'自定义编辑',\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "shadeClose: true,\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "zIndex:9999,\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "content: content,\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "area: ['600px', '670px'],\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "btn: ['保存', '取消'],\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "success: function(layero, index){\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "},\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "yes: function(index, layero){\n";
		temp += SysCardFormBeansUtil.getNbsp(5) + "//循环\n";
		temp += SysCardFormBeansUtil.getNbsp(5) + "for(var i = 0 ;i<creatParams.length;i++){\n";
		temp += SysCardFormBeansUtil.getNbsp(6) + "creatParam[creatParams[i]] = $(\"#\"+creatParams[i]).val();\n";
		temp += SysCardFormBeansUtil.getNbsp(5) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "introduceParam.creatParam = creatParam;	  \n";	
		temp += SysCardFormBeansUtil.getNbsp(1) + "$.ajax({  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "type: 'POST', \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "cache: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "async: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "contentType: 'application/json;charset=utf-8',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "data: JSON.stringify(introduceParam),  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "url: '/myehr/button/saveIntroduceGrid.action?formId=${param.formId}&intrToId=${param.intrToId}&intrButtonId=${param.intrButtonId}',\n";  
		temp += SysCardFormBeansUtil.getNbsp(2) + "success: function (data) {  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "if(data[0]==0){\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.alert('引入成功', {\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  skin: 'layer-ext-moon' \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	})\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.alert('引入失败', {\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  skin: 'layer-ext-moon' \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	})\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	}\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "});\n";
		temp += SysCardFormBeansUtil.getNbsp(5) + "layer.close(index);\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "},\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "btn2: function(index, layero){\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(3) + "});\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "$.ajax({  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "type: 'POST', \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "cache: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "async: false,\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "contentType: 'application/json;charset=utf-8',\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "data: JSON.stringify(introduceParam),  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "url: '/myehr/button/saveIntroduceGrid.action?formId=${param.formId}&intrToId=${param.intrToId}&intrButtonId=${param.intrButtonId}',\n";  
		temp += SysCardFormBeansUtil.getNbsp(2) + "success: function (data) {  \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "if(data[0]==0){\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.alert('引入成功', {\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  skin: 'layer-ext-moon' \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	})\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	}else{\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "layer.alert('引入失败', {\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	  skin: 'layer-ext-moon' \n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	})\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "	}\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "});\n";
		temp += SysCardFormBeansUtil.getNbsp(4) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(2) + "}\n";
		temp += SysCardFormBeansUtil.getNbsp(1) + "}\n";
		
		
		
	
	//	temp += SysCardFormBeansUtil.getNbsp(1) + "if(typeof(window.parent._treegridselectedrows) == 'undefined'){" + SysCardFormBeansUtil.NEW_LINE_ONLY;
	//	temp += SysCardFormBeansUtil.getNbsp(2) + "WindowClose('ok')" + SysCardFormBeansUtil.NEW_LINE;
	
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "}else{"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "var treeFathergrid=window.parent.IsHaveFather();"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "if(treeFathergrid==1){"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "WindowClose('ok');"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "}else{"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
//		temp += SysCardFormBeansUtil.getNbsp(2) + "window.parent._treegridselectedrows=selectGrid;"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
//		temp += SysCardFormBeansUtil.getNbsp(2) + "parent.closex();"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
//		temp += SysCardFormBeansUtil.getNbsp(0) + "}"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	

	//	temp += SysCardFormBeansUtil.getNbsp(0) + "WindowClose('ok');"+ SysCardFormBeansUtil.NEW_LINE_ONLY;	
	//	temp += SysCardFormBeansUtil.getNbsp(0) + "window.parent._treegridselectedrows=selectNodes;"+ SysCardFormBeansUtil.NEW_LINE_ONLY;
	//	temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;			

//		temp += SysCardFormBeansUtil.getNbsp(1) + "}else {" + SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp += SysCardFormBeansUtil.getNbsp(2) + "alert('请选中数据!');" + SysCardFormBeansUtil.NEW_LINE_ONLY;
//		temp += SysCardFormBeansUtil.getNbsp(1) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
//		
//		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
//		
//		temp += SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
		
	}
	
	public static String getWriteJs(String buttonSaveFun,String btnCode,String formCode) {
		// TODO 自动生成方法存根
		String temp = "//选中按钮使用的click方法 （自定生成）"+SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) + "function "+btnCode+formCode+"_click(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) +buttonSaveFun+ SysCardFormBeansUtil.NEW_LINE_ONLY;
		temp += SysCardFormBeansUtil.getNbsp(0) + "}" + SysCardFormBeansUtil.NEW_LINE_ONLY;
		
		temp += SysCardFormBeansUtil.NEW_LINE_ONLY;
		return temp;
	}
	
	/**
	 * 获取链接字段点击的跳转url
	 * @return
	 */
	public static String getTextBoxLinkUrl(SysTextBoxCache textbox,SysRequestParam request){
		String realUrl = "";
		String isForm = textbox.getTextBoxLinkIsForm();
		Map<String, List<SysFormGeneralParamCache>> params = textbox.getGeneralParams();
		if("Y".equals(isForm)){ //如果是动态表单
			realUrl = request.getContextPath()+SysGridFormBeansUtil.FORM_EXEC_URL+"?formId="+textbox.getTextBoxLinkForm()+"&isInit=true";
			
		}else {//自定义url
			realUrl = textbox.getTextBoxLinkUrl();
		}
		String paramStr = "";
		if(params!=null){
			String endtype = FormParamConstants.COLUMN_LINK;
			List<SysFormGeneralParamCache>  l = params.get(endtype);
			paramStr = SysCardFormColumnUtil.getGeneralParamStr(l,request);
			if(paramStr!=null&&!"".equals(paramStr)){
				if(realUrl!=null&&realUrl.indexOf("?")>0){
					realUrl = realUrl +"&" +paramStr;
				}else {
					realUrl = realUrl+"?"+paramStr;
				}
			}
		}
		
		return realUrl;
	}


	
	/**
	 * 获取引入按钮js
	 * @param requst
	 * @param title
	 * @param width
	 * @param height
	 * @param winType
	 * @param formurl
	 * @param formId
	 * @param jsId
	 * @param otherColumnJsId
	 * @param backType
	 * @param backValue
	 * @param fieldName
	 * @param otherField
	 * @param showProperty
	 * @param valueProperty
	 * @param isGridQuery
	 * @param params
	 * @param request
	 * @return
	 */
	public static String getIntroduceOpenJs(Map<String, List<SysFormGeneralParamCache>> params,SysFormButtonIntroduceCache introduce, SysRequestParam request,
			String btnCode,String formCode,String FormId){
	String realUrl = request.getContextPath()+"/"+SysCardFormBeansUtil.LOOKUP_FORM_URL+"?formId="+introduce.getIntroduceFormId();
	ISysformconfigService sysformconfigService = (ISysformconfigService)SpringContextUtils.getBeanById("ISysformconfigService");
	if(params!=null){
		String endtype = FormParamConstants.INTRODUCE_TARGET_FORM;
		List<SysFormGeneralParamCache>  l = params.get(endtype);
		String paramStr = SysCardFormColumnUtil.getGeneralParamStr(l,request);
		if(paramStr!=null&&!"".equals(paramStr)){
			if(realUrl!=null&&realUrl.indexOf("?")>0){
				realUrl = realUrl +"&" +paramStr;
			}else {
				realUrl = realUrl+"?"+paramStr;
			}
		}
	}
	
	String paramCode = "";
	String requestParam = "";
	String gridVarParam = "";
	List<SysFormButtonIntrDetailCache> details = introduce.getDetails();
	for(SysFormButtonIntrDetailCache detail:details){
		if("r".equals(detail.getTargetColumnType())){
			String str = detail.getTargetColumnId();
			String str1 = SysCardFormColumnUtil.getDefaultValue(request,"request",str);
			if(str1!=null){
				String str2 = SysCardFormBeansUtil.getNbsp(7) + str + ":'" + str1 + "',\n";
				paramCode += str2;
			}
			requestParam += "&"+str+"=\"+"+str+"+\"";
			gridVarParam += "var "+str+" = '${param."+str+"}';\n";
		} else if("s".equals(detail.getTargetColumnType())){
			String str = detail.getTargetColumnId();
			String str1 = SysCardFormColumnUtil.getDefaultValue(request,"session",str);
			if(str1!=null){
				String str2 = SysCardFormBeansUtil.getNbsp(7) + str + ":'" + str1 + "',\n";
				paramCode += str2;
			}
		}
	}
	if(paramCode!=null && paramCode.length()>0){
		paramCode = paramCode.substring(0,paramCode.length()-2);
	}
	paramCode = "var params={\n" + paramCode + SysCardFormBeansUtil.getNbsp(7) + "\n};";
	
	/*if (realUrl.indexOf("encodeURI") < 0) {
		realUrl = "encodeURI(\"" + realUrl + "\")";
	}*/
	String width;
	String height;
	if (Long.valueOf(introduce.getIntroduceWidth())>1000) {
		width=introduce.getIntroduceWidth();
	}else {
		width="1000";
	}
	if (Long.valueOf(introduce.getIntroduceHeight())<600) {
		height=introduce.getIntroduceHeight();
	}else {
		height="600";
	}
	String temp = "//引入按钮处理函数\n";
	SysFormconfigCache formCache = new SysFormconfigCache();
	try {
		formCache = sysformconfigService.getForm(FormId);
		if (formCache.getPojoform().getFormDefLayoutType().equals("2")) {
			temp+=gridVarParam;
		}
	} catch (DcfException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();logger.error(e.getMessage(),e);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();logger.error(e.getMessage(),e);
	}
	
	temp+=SysCardFormBeansUtil.getNbsp(1) + "function "+btnCode+formCode+"_click_"+FormId+"(){"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
	temp+=SysCardFormBeansUtil.getNbsp(2)+"layer.open({\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"type:2,\n"; //showMaxButton: false,
	temp+=SysCardFormBeansUtil.getNbsp(3)+"content:\"" + realUrl + "&intrToId="+FormId+"&intrButtonId="+introduce.getIntroduceButtonId()+requestParam+"\",\n"; //showMaxButton: false,
	temp+=SysCardFormBeansUtil.getNbsp(3)+"shadeClose: true,\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"maxmin:true,\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"shade: 0.8,\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"zIndex:9999,\n";
	//temp+=SysCardFormBeansUtil.getNbsp(3)+"offset: ['0px', '0px'],\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"title:\""+introduce.getIntroduceTitle()+"\" ,\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"area: ['"+width+"', '"+height+"'],\n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"success:function(layero,index){ \n";
	temp+=SysCardFormBeansUtil.getNbsp(3)+"$('.layui-layer-max').click();\n";
	
	temp+=SysCardFormBeansUtil.getNbsp(3)+"},\n";
	
	temp+=SysCardFormBeansUtil.getNbsp(3)+"end:function(){\n";
	temp+=SysCardFormBeansUtil.getNbsp(4)+"refresh_"+formCode+"();\n";
	/*temp+=SysCardFormBeansUtil.getNbsp(3)+"if (action == 'ok') {\n";

	temp+=SysCardFormBeansUtil.getNbsp(5)+"iframe = this.getIFrameEl();\n";
	temp+=SysCardFormBeansUtil.getNbsp(5)+"var data = iframe.contentWindow.GetData();\n";
	temp+=SysCardFormBeansUtil.getNbsp(5)+"data = nui.clone(data);\n";
	temp+=SysCardFormBeansUtil.getNbsp(5)+"if (data) {	\n";
    // var id = '';
	//var name = '';//循环 ，取值
	temp+=SysCardFormBeansUtil.getNbsp(6)+"var arr=nui.decode(data);\n";
	//temp+=SysCardFormBeansUtil.getNbsp(6)+"alert(nui.encode(arr));\n";
	temp+=SysCardFormBeansUtil.getNbsp(6)+"dcf.mask();\n";
	temp+=SysCardFormBeansUtil.getNbsp(6)+paramCode;
	temp+=SysCardFormBeansUtil.getNbsp(6)+"$.ajax({\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"url: \""+INTRODUCE_URL+"\",\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"type: 'POST',\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"data: nui.encode({arr:arr,formId:_formId_"+formCode+",buttonId:"+introduce.getIntroduceButtonId()+",params:params"+"}),\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+" cache: false,\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"contentType: 'text/json',\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+" success: function (text) {\n";
	temp+=SysCardFormBeansUtil.getNbsp(8)+"dcf.unmask();\n";
	temp+=SysCardFormBeansUtil.getNbsp(8)+"if(text.result[0]=='success'){\n";
	temp+=SysCardFormBeansUtil.getNbsp(9)+"grid_"+formCode+".reload();\n";
	temp+=SysCardFormBeansUtil.getNbsp(9)+"alert('引入成功');\n";
	temp+=SysCardFormBeansUtil.getNbsp(8)+"}else {\n";
	temp+=SysCardFormBeansUtil.getNbsp(9)+"alert(引入失败);\n";
	temp+=SysCardFormBeansUtil.getNbsp(8)+"}\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"},\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"error: function (jqXHR, textStatus, errorThrown) {\n";
	temp+=SysCardFormBeansUtil.getNbsp(8)+"dcf.unmask();\n";
	temp+=SysCardFormBeansUtil.getNbsp(7)+"},\n";
	
	//


	temp+=SysCardFormBeansUtil.getNbsp(6)+"});\n";
	
	temp+=SysCardFormBeansUtil.getNbsp(5)+"}\n";
	temp+=SysCardFormBeansUtil.getNbsp(4)+"}\n";*/
	temp+=SysCardFormBeansUtil.getNbsp(3)+"}\n";
	temp+= SysCardFormBeansUtil.getNbsp(2)+" }); \n";
	temp += SysCardFormBeansUtil.getNbsp(1) + "}"	+ SysCardFormBeansUtil.NEW_LINE_ONLY;
	return temp;
}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp1(){
		String jspBuildString=	 "<%@ page language=\"java\" import=\"java.util.*\" import=\"com.myehr.common.util.LangUtil\" pageEncoding=\"UTF-8\"%>\n"
								+"<%@ include file=\"/common/commontest.jsp\" %>\n"
								+"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"
								+"<html>\n"
								+"<body>\n"
								+"<div class=\"container-fluid\" style=\"height:100%;overflow-y:auto;padding-top:5px;\">\n";
								//+"	<div>\n";
		
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp2(){
		String jspBuildString="<div class=\"input-group\">\n"
				+"    <input type=\"text\" id=\"search\" class=\"form-control\" placeholder=\"Search\" oninput=\"search()\">\n"
				+"</div>\n";
		
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp3(String formDefCode){
		String jspBuildString ="<table id=\""+formDefCode+"_List\">\n" 
				+"</table>\n" +
				"<div id=\"activity_pane\" style=\"position:absolute;left:500px;top:70px\"></div>\n";
		
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp4(){
		String jspBuildString =	 "<script>\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp4_1(SysFormconfigCache form){
		ISysFormGeneralParamService paramService = (ISysFormGeneralParamService)SpringContextUtils.getBeanById("ISysFormGeneralParamService");
		String paramType = FormParamConstants.FORMCONFIG;
//		List<SysFormGeneralParam> params = paramService.queryGeneralParamByTypeAndTypeValue(paramType,form.getFormDefId());
		List<SysFormGeneralParamCache> params = form.getGeneralParams().get(paramType);
		String containerParam="{";
		if (params!=null) {
			for (int i = 0; i < params.size(); i++) {
				if (params.get(i).getParamFromType().equals("containerParam")) {
					containerParam+=params.get(i).getParamName()+":'${sessionScope."+params.get(i).getPojo().getParamFromValue()+"_paramSession}',";
				}
				if (params.get(i).getParamFromType().equals("session")) {
					containerParam+=params.get(i).getParamName()+":'${sessionScope."+params.get(i).getPojo().getParamFromValue()+"}',";
				}
				if (params.get(i).getParamFromType().equals("parameter")) {
					containerParam+=params.get(i).getParamName()+":'${param."+params.get(i).getPojo().getParamFromValue()+"}',";
				}
			}
		}
		
		if (containerParam.length()>1) {
			containerParam = containerParam.substring(0, containerParam.length()-1)+"}";
		}else {
			containerParam = containerParam+"}";
		}
		String formId = form.getFormDefId()+"";
		String jspBuildString =	 "var heightGadedata_"+formId+" =[];\n"
								+"var containerParam_"+formId+" ={};\n"
								+"containerParam_"+formId+" ="+containerParam+";\n"
								+"function changeHeightGadedata_"+formId+"(data){\n" 
								+"	heightGadedata_"+formId+" = data;\n" 
								+"}\n";
		return jspBuildString;
	}
	public static String getBuildJsp4_2(){
		return "$(function() {\n";
	}
	
	public static String getBuildJsp20(){
		String jspBuildString =	 "var heightGadedata =[];\n"
								+"function changeHeightGadedata(data){\n" 
								+"	heightGadedata = data;\n" 
								+"}\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp13(String formDefId){
		String jspBuildString ="    querys_"+formDefId+"();\n";
		
		return jspBuildString;
	}
	
	public static String getBuildJsp14(){
		String jspBuildString =	 "\n" 
								+"    cardSelectInitFunctionx_grid('${param.formType}');\n"
								+"    cardDateWidInitFunction();\n";
		return jspBuildString;
	}
	public static String getBuildJsp14_1(SysFormconfigWithBLOBs form){
		String formId = form.getFormDefId()+"";
		String jspBuildString =	 "    //showMoreBtn($(\".BTNGROUP_"+formId+"\"));\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp15(){
		String jspBuildString =  "\n" 
								+"    function showMoreBtn(ele){\n"
								+"    	var DIVAMOUNT = 5;\n"
								+"    	if(ele.children('div').length > DIVAMOUNT){\n"
								+"    		var len = ele.children('div').length;\n"
								+"    		$(\"#ele>div:gt(4)\").hide();\n"
								+"    		var selectObj = $('<div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">更多 <span class=\"caret\"></span></button><ul class=\"dropdown-menu\" style=\"min-width:90px;left:-14px;\"></ul></div>');\n"
								+"    		ele.append(selectObj);\n"
								+"    		for(var i=DIVAMOUNT;i<len;i++){\n"
								+"    			ele.children('div').eq(i).hide();\n"
								+"    			var liObj = $('<li style=\"margin-left:14px;\"></li>');\n"
								+"    			liObj.append(ele.children('div').eq(i).clone(true));\n"
								+"    			selectObj.find('.dropdown-menu').append(liObj);\n"
								+"    			selectObj.find('.dropdown-menu').append($('<li role=\"separator\" class=\"divider\"></li>'));\n"
								+"    		}\n"
								+"    		selectObj.find('.dropdown-menu div').show();\n"
								+"    	}\n"
								+"    }\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp16(){
		String jspBuildString ="});\n";
		return jspBuildString;
	}

	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp5(){
		String jspBuildString ="</script>\n</body>\n</html>\n";
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp6(SysFormconfigCache form){
		String formDefCode =form.getPojoform().getFormDefCode();
		int formDefId = form.getPojoform().getFormDefId().intValue();

		StringBuffer js = new StringBuffer();

		String[] showButton = null;
		if (form.getGrid().getFormEditNextOnEnter()!=null) {
			showButton = form.getGrid().getFormEditNextOnEnter().split(",");
		}
		String showFreshButton = "true";
		String showColumnButton = "true";
		String showExportButton = "true";
		if (showButton!=null&&showButton.length>2) {
			if (showButton[0].toLowerCase().equals("f")) {
				showFreshButton = "false";
			}
			if (showButton[1].toLowerCase().equals("f")) {
				showColumnButton = "false";
			}
			if (showButton[2].toLowerCase().equals("f")) {
				showExportButton = "false";
			}
		}
		
		String jspBuildString = "var showButtonNum = 0;"+"\n"
				+"function initRoleButtons(buttonFormId,buttonFormCode){"+"\n"
				+"    var date = new Date();\n"
				+"    var url = '/myehr/form/queryCardFormButtonsWithUserId.action?formId='+buttonFormId+'&'+date;"+"\n"
				+"    $.ajax({"+"\n"
				+"        url:url,"+"\n"
				+"        type:'post',"+"\n"
				+"        cache: false,"+"\n"
				+"        async: false,"+"\n"
				+"        contentType:'application/json',"+"\n" 
				+"        success:function(datas){"+"\n"
				+"            if(datas.length==0){"+"\n"
				+"                showMoreBtn($(\".BTNGROUP_\"+buttonFormId+\"\"));\n"
				+"            }else{"+"\n"
				+"                for(var i=0;i<datas.length;i++){"+"\n"
				+"                    if(datas[i].powerType==4){"+"\n"
				+"                        var obj = $(\"#\"+datas[i].formButtonCode+buttonFormCode+\"\");"+"\n"
				+"                        obj.parent().remove();"+"\n"
				//+"                        obj[0].parentNode.style.display=\"none\";"+"\n"
				+"                    }else if(datas[i].powerType==3){"+"\n"
				+"                        var obj = $(\"#\"+datas[i].formButtonCode+buttonFormCode+\"\");"+"\n"
				//+"                        obj[0].parentNode.style.display=\"none\";"+"\n"
				+"                        obj.parent().remove();"+"\n"
				+"                    }else if(datas[i].powerType==2){"+"\n"
				+"                        var obj = $(\"#\"+datas[i].formButtonCode+buttonFormCode+\"\");"+"\n"
				+"                        obj[0].disabled=true;"+"\n"
				+"                        showButtonNum++;"+"\n"
				+"                    }else if(datas[i].powerType==0){"+"\n"
				+"                        var obj = $(\"#\"+datas[i].formButtonCode+buttonFormCode+\"\");"+"\n"
				+"                      obj.parent().remove();"+"\n"
				//+"                        obj[0].parentNode.style.display=\"none\";"+"\n"
				+"                    }else{"+"\n"
				+"                        showButtonNum++;"+"\n"
				+"                    }\n"
				+"                }"+"\n"
				+"                showMoreBtn1($(\".BTNGROUP_\"+buttonFormId+\"\"),showButtonNum);\n"
				+"            }"+"\n"
				+"        }"+"\n"
				+"    });"+"\n"
				+"}"+"\n";
		String pageSize = "10";
		if (form.getGrid().getPojo().getFormPageSize()!=null) {
			pageSize = form.getGrid().getPojo().getFormPageSize()+"";
		}
		String fixedColumns = "false";
		int fixedNumber = 0;
		if (form.getGrid().getPojo().getFormFrozen()!=null&&form.getGrid().getPojo().getFormFrozen().intValue()>-1) {
			fixedColumns = "true";
			fixedNumber = form.getGrid().getPojo().getFormFrozen().intValue();
		}
		jspBuildString +="    function querys_"+formDefId+"() {"+"\n"
		+"        initRoleColumn('"+formDefId+"');"+"\n"
		+"        initRoleButtons('"+formDefId+"','"+formDefCode+"');"+"\n"
		+"        $("+'"'+"#"+formDefCode+"_List"+'"'+").bootstrapTable({"+"\n"
		+"            url :'/myehr/form/cardformInitData.action',"+"\n"
		+"            contentType: 'application/json;charset=utf-8',"+"\n"
		+"            dataType:'json',"+"\n"
		+"            method:'post',"+"\n"
		+"            height : 500,"+"\n"
		+"            undefinedText : '-',"+"\n"
		+"            pagination : true,"+"\n"
		+"            striped : false,"+"\n"
		+"            fixedColumns: "+fixedColumns+","+"\n"
		+"            fixedNumber:"+fixedNumber+","+"\n"
		+"            showHeader : "+form.getGrid().getFormShowColumns()+","+"\n"
		+"            queryParams : grid_"+formDefCode+"_fun,"+"\n"
		+"            cache : false,"+"\n"
		+"            pageSize : "+pageSize+", "+"\n"
		+"            pageList : "+form.getGrid().getPojo().getFormPageSizeList()+", "+"\n";
//		+"            pageList : [10,20,50,10000], "+"\n";
		/*if(form.getPkColumn()!=null){
			jspBuildString +=	"            uniqueId:'"+ChangeCode.getUniqueCode(form.getPkColumn().getFormEntityTablename(), form.getPkColumn().getFormFiledTablename())+"', "+"\n";
		}*/
		jspBuildString +="            toolbar : "+'"'+"#toolbar"+'"'+","+"\n"
		+"            showColumns : "+showColumnButton+","+"\n"
		+"            showExport : "+showExportButton+","+"\n"
		+"            clickToSelect: true,"+"\n"
		+"            showRefresh : "+showFreshButton+","+"\n"
		+"            sidePagination : "+'"'+"server"+'"'+","+"\n"
		+"            columns : ["+"\n";
		return jspBuildString;
	}
	
	/**
	 * 获取生成批量修改列表头
	 * @return
	 */
	public static String getBuildJsp6ForBatchEdit(SysFormconfigCache form){
		String formDefCode =form.getPojoform().getFormDefCode();
		int formDefId = form.getPojoform().getFormDefId().intValue();

		StringBuffer js = new StringBuffer();

		String[] showButton = null;
		
		String jspBuildString = "";
		String fixedColumns = "false";
		int fixedNumber = 0;
		if (form.getGrid().getPojo().getFormFrozen()!=null&&form.getGrid().getPojo().getFormFrozen().intValue()>-1) {
			fixedColumns = "true";
			fixedNumber = form.getGrid().getPojo().getFormFrozen().intValue();
		}
		jspBuildString +="    function queryBatchEdit_"+formDefId+"(e) {"+"\n"
		+"        $(\"#batchEdit\"+e).bootstrapTable({"+"\n"
		+"            contentType: 'application/json;charset=utf-8',"+"\n"
		+"            dataType:'json',"+"\n"
		+"            method:'post',"+"\n"
		+"            height : 500,"+"\n"
		+"            undefinedText : '-',"+"\n"
		+"            pagination : false,"+"\n"
		+"            striped : false,"+"\n"
		+"            fixedColumns: "+fixedColumns+","+"\n"
		+"            fixedNumber:"+fixedNumber+","+"\n"
		+"            showHeader : true,"+"\n"
		+"            cache : false,"+"\n"
		+"            columns : ["+"\n";
		return jspBuildString;
	}
	
	//getBuildJspForBatchEditJs
	public static String getBuildJspForBatchEditJs(SysFormconfigCache form){
		String formDefCode =form.getPojoform().getFormDefCode();
		int formDefId = form.getPojoform().getFormDefId().intValue();
		String jspBuildString = "";
		jspBuildString ="function batchEdit_"+formDefId+"(e,formId){\n"+
						"	var selects = $(\"#\"+e).bootstrapTable('getSelections');\n"+
						"	var content = '<table id =\"batchEdit'+e+'\"></table>';\n"+
						"	layer.open({\n"+
						"		title:'"+form.getFormDefName()+"',\n"+
						"		shadeClose: true,\n"+
						"		zIndex:9999,\n"+
						"		area : ['100%', '100%'],\n"+
						"		maxmin: true,            //最大化按钮\n"+
						"		content: content,\n"+
						"		btn: ['保存','关闭'],\n"+
						"		yes: function(index, layero){\n"+
						"		//按钮【按钮一】的回调\n"+
						"			var datas = selects;\n"+
//						"			var batchEditLoad = layer.load(1);\n"+
						"			layer.msg('加载中', {\n"+
						"				  icon: 16\n"+
						"				  ,shade: 1\n"+
						"				});\n"+
						"			var trs = $(\"#batchEdit\"+e).find(\"tbody tr\");\n"+
						"			for(var i=0;i<trs.length;i++){\n"+
						"				var input = trs.eq(i).find(\"input\");\n"+
						"				var data = datas[i];\n"+
						"				for(j=0;j<input.length;j++){\n"+
						"					var columnName = input.eq(j).attr(\"name\");\n"+
						"					data[columnName] = input.eq(j).val();\n"+
						"				}\n"+
						"			}\n"+
						"			var reCode = saveFunction(selects,formId);\n"+
//						"			layer.close(batchEditLoad);\n"+
						"			if(reCode == '0'){\n"+
						"				layer.alert('保存成功', {\n"+
						"					  icon: 6,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
						"					  skin: 'layer-ext-moon' //\n"+
						"					})\n"+
						"			}else{\n"+
						"				layer.alert(reCode, {\n"+
						"					  icon: 5,//1:√;2:×;3:问号;4:锁;5:哭脸;6.笑脸;7:！;\n"+
						"					  skin: 'layer-ext-moon' //\n"+
						"					})\n"+
						"			}\n"+
						"		},\n"+
						"		success:function(layero,index){\n"+
						"			queryBatchEdit_"+formDefId+"(e);\n"+
						"			$(\"#batchEdit\"+e).bootstrapTable('load',selects); \n"+
						"		},\n"+
						"		end:function(){\n"+
						"			$(\"#\"+e).bootstrapTable('refresh');\n"+
						"		}\n"+
						"	}); \n"+
						"}\n";
						return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp6_selectForm(SysFormconfigCache form){
		//String jspBuildString ="</script>\n</body>\n</html>\n";
		String formDefCode =form.getPojoform().getFormDefCode();
		int formDefId = form.getPojoform().getFormDefId().intValue();

		StringBuffer js = new StringBuffer();
//		String pageList = form.getGrid().getPojo().getFormPageSizeList()

		String[] showButton = null;
		if (form.getGrid().getFormEditNextOnEnter()!=null) {
			showButton = form.getGrid().getFormEditNextOnEnter().split(",");
		}
		String showFreshButton = "true";
		String showColumnButton = "true";
		String showExportButton = "true";
		if (showButton!=null&&showButton.length>2) {
			if (showButton[0].toLowerCase().equals("f")) {
				showFreshButton = "false";
			}
			if (showButton[1].toLowerCase().equals("f")) {
				showColumnButton = "false";
			}
			if (showButton[2].toLowerCase().equals("f")) {
				showExportButton = "false";
			}
		}
		
		String pageSize = "10";
		if (form.getGrid().getPojo().getFormPageSize()!=null) {
			pageSize = form.getGrid().getPojo().getFormPageSize()+"";
		}
		String fixedColumns = "false";
		int fixedNumber = 0;
		if (form.getGrid().getPojo().getFormFrozen()!=null&&form.getGrid().getPojo().getFormFrozen().intValue()>-1) {
			fixedColumns = "true";
			fixedNumber = form.getGrid().getPojo().getFormFrozen().intValue();
		}
		String jspBuildString = "";
		jspBuildString +="    function querys_"+formDefId+"() {"+"\n"
		+"        $("+'"'+"#"+formDefCode+"_List"+'"'+").bootstrapTable({"+"\n"
		+"            url :'/myehr/form/cardformInitData.action',"+"\n"
		+"            contentType: 'application/json;charset=utf-8',"+"\n"
		+"            dataType:'json',"+"\n"
		+"            method:'post',"+"\n"
		+"            height : 500,"+"\n"
		+"            undefinedText : '-',"+"\n"
		+"            pagination : true,"+"\n"
		+"            striped : false,"+"\n"
		+"            fixedColumns: "+fixedColumns+","+"\n"
		+"            fixedNumber:"+fixedNumber+","+"\n"
		+"            showHeader : "+form.getGrid().getFormShowColumns()+","+"\n"
		+"            queryParams : grid_"+formDefCode+"_fun,"+"\n"
		+"            cache : false,"+"\n"
		+"            pageSize : "+pageSize+", "+"\n"
		+"            pageList : "+form.getGrid().getPojo().getFormPageSizeList()+", "+"\n";
		jspBuildString +="            toolbar : "+'"'+"#toolbar"+'"'+","+"\n"
		+"            showColumns : "+showColumnButton+","+"\n"
		+"            showExport : "+showExportButton+","+"\n"
		+"            clickToSelect: true,"+"\n"
		+"            showRefresh : "+showFreshButton+","+"\n"
		+"            sidePagination : "+'"'+"server"+'"'+","+"\n"
		+"            columns : ["+"\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp6_2(){
		String jspBuildString ="            {"+"\n"
		+"                field : 'state',"+"\n"
		+"                checkbox : true,"+"\n"
		+"                align : 'center',"+"\n"
		+"                visible : true,"+"\n"
		+"                valign : 'middle'"+"\n"
		+"            },"+"\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp6_4(){
		String jspBuildString ="            {"+"\n"
		+"                field : 'state',"+"\n"
		+"                checkbox : true,"+"\n"
		+"                align : 'center',"+"\n"
		+"                visible : false,"+"\n"
		+"                valign : 'middle'"+"\n"
		+"            },"+"\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp6_5(String formCode,String formId){
		String jspBuildString =  "            {"+"\n"
								+"                title : '预览',"+"\n"
								+"                field : 'yulan',"+"\n"
								+"                align : 'center',"+"\n"
								+"                valign : 'middle',"+"\n"
//								+"                sortable : true,"+"\n"
								+"                formatter: function (value, row, index) {"+"\n"
						    	+"                   return \"<span class='glyphicon glyphicon-eye-open' onclick='edit"+formCode+"_click_"+formId+"(\\\"isView\\\")' style=''></span>\";\n" 
								+"					 }"+"\n"
								+"            },"+"\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp6_3(){
		String jspBuildString ="            {"+"\n"
        +"                title : '序号',"+"\n"
        +"                field : 'defaultXH',"+"\n"
        +"                align : 'center',"+"\n"
        +"                visible : true,"+"\n"
    	+"                valign : 'left',"+"\n"
    	+"                formatter: function (value, row, index) {"+"\n"
    	+"                    if(value==undefined){"+"\n"
    	+"                        value=index+1;"+"\n"
    	+"                    }"+"\n"
    	+"                    return "+"\""+"<a style=\\\"font-size:12px;text-decoration:none;color:black;width:50px;\\\" name="+"\\"+"\""+"defaultXH"+"\\"+"\" data-type="+"\\"+"\"text"+"\\"+"\" data-pk="+"\\"+"\""+"\""+"+row.Id+"+"\""+"\\"+"\" data-title="+"\\"+"\""+"序号"+"\\"+"\">"+"\""+"+"+ "value" +"+"+"\""+"</a>"+"\""+";"+"\n"
		+"                }"+"\n"
		+"            },"+"\n";
		return jspBuildString;
	}
	
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp7_1(SysFormconfigCache form){
		String jspBuildString ="            ],"+"\n"
		+"            onClickRow: function (row, $element) {"+"\n"
		+"                curRow = row;"+"\n";
		return jspBuildString;
	}
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp7(SysFormconfigCache form){
		String initTimeaxis = "";
		if (form.getFormDefLayoutType().equals("11")) {
			//initTimeaxis="showDate(res.rows);\n";
		}
		String jspBuildString ="            },"+"\n"
		+"		onPostBody: function (res) {"+"\n"
		+"		    hiddenBorder("+form.getGrid().getPojo().getFormBorderShowType()+");"+"\n"
		+"		},"+"\n"
		+"		onAll: function (res) {"+"\n"
		+"				  changeColor('"+form.getFormDefCode()+"_List');\n"		
		+"		},"+"\n"
		+"		onLoadSuccess: function (aa, bb, cc) {"+"\n"
		+"				  changeColor('"+form.getFormDefCode()+"_List');\n"			
		+"                $(\"#"+form.getFormDefCode()+"_List"+" a\").editable({"+"\n"
    	+"                    disabled: true,"+"\n"
    	+"                    emptytext: \"-\","+"\n"
		+"                    url: function (params) {"+"\n"
		+"                        var sName = $(this).attr(\"name\");"+"\n"
		+"                        curRow[sName] = params.value;"+"\n"
		+"                    },"+"\n"
		+"                    type: 'text'"+"\n"
		+"                });"+"\n"
		+"      },"+"\n"
		+"		onDblClickCell: function (field, value,row,td) {\n"
		+"        	edit"+form.getFormDefCode()+"_click_"+form.getFormDefId()+"('isView',row);  \n" 
		+"         } ,\n"
		+"            responseHandler : function(res) {  "+"\n"
		+"                "+initTimeaxis
		+"                return {  "+"\n"
		+"                    total : res.total,  "+"\n"
		+"                    rows : res.rows  "+"\n"
		+"                };"+"\n"
		+"            }"+"\n"
		+"        });"+"\n"
		+"        window.operateEvents = {  "+"\n"
		+"    	  };"+"\n"
		+"}"+"\n";
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp8(String formDefCode){
		String jspBuildString ="/** 刷新页面 */ \n"
				+"function refresh_"+formDefCode+"() { \n" 
				+"    $('#"+formDefCode+"_List').bootstrapTable('refresh'); \n" 
				+"}\n"
				+"\n"
				+"/** 隐藏边框 */ \n"
				+"function hiddenBorder(type) { \n"
				+"    if(type == 0){\n"
				+"        $(\"#"+formDefCode+"_List td\").css(\"border\",\"none\");\n"
				+"    }else if(type == 1){\n"
				+"        $(\"#"+formDefCode+"_List td\").css(\"border-left\",\"none\");\n"
				+"    }else if(type == 2){\n"
				+"        $(\"#"+formDefCode+"_List td\").css(\"border-top\",\"none\");\n"
				+"    }\n"
				+"}\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp15(String formDefCode){
		String jspBuildString ="$('#"+formDefCode+"_List').bootstrapTable('uncheckAll'); \n"
				+"$element[0].click(); \n" ;
		return jspBuildString;
	}
	
	public static String seniorSearch(){
		String jspBuildString =	 "var _heightGadedatatemp =[];\n"
								+"function changeHeightGadedata(data){\n" 
								+"	heightGadedata = data;\n" 
								+"}\n" ;
		return jspBuildString;
	}
	
	/**
	 * 获取生成jsp的头
	 * @return
	 */
	public static String getBuildJsp9(){
		String jspBuildString ="/**查询条件与分页数据 */\n"
				+"function queryParams(pageReqeust) {\n"
				+"    pageReqeust.search = $(\"#search\").val();\n"
				+"    pageReqeust.enabled = $(\"#enabled\").val();\n" 
				+"    pageReqeust.querys = $(\"#querys\").val();\n" 
				+"    pageReqeust.pageNo = this.offset;\n"
				+"    pageReqeust.pageSize = this.pageNumber;\n" 
				+"    return pageReqeust;\n"
				+"}\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp10(){
		String jspBuildString ="/**下拉控件 */\n"
				+"var classes = $(\"select\");\n"
				+"var parame={\n"
				+"    id:'',\n" 
				+"    value:\"N\",\n" 
				+"    width: \"100px\",\n"
				+"    height:\"45px\",\n" 
				+"    url:'/myehr/dict/searchSysDictEntryTypeCode.action?userId=${sessionScope.userid}',\n"
				+"	  jsonParam:{},\n"
				+"    chang:function (e){\n"
				+"    }\n"
				+"};\n"
   			    +"function initDict(){\n"
   		    	+"	for(var i=0;i<classes.length;i++){\n"+
   		    	"		parame.id=classes[i].id;\n"+
   		    	"		var type=$(classes[i]).attr('multiSelect');\n" +
   		    	"		var dataField=$(classes[i]).attr('dataField');\n" +
   		    	"		parame.placeholder=\"\";\n"+
   		    	"		parame.jsonParam.nullItemText=$(classes[i]).attr('nullItemText');\n"+
   		    	"		parame.jsonParam.dictTypeCode=$(classes[i]).attr('DictName');\n"+
   		    	"		if(type==\"true\"){\n" +
   		    	"			myehr_initSelectMore(parame,dataField);\n" +
   		    	"		}else{\n" +
   		    	"			myehr_initSelect(parame,dataField);\n" +
   		    	"		}\n" +
   		    	"		}\n"
   			    +"}\n";
		return jspBuildString;
	}
	
	public static String getBuildJsp11(){
		String jspBuildString ="/**取url参数值  */\n"
				+"function getQueryString(name) {\n"
				+"    var reg = new RegExp(\"(^|&)\" + name + \"=([^&]*)(&|$)\", \"i\");\n"
				+"    var r = window.location.search.substr(1).match(reg);\n" 
				+"    if (r != null) return unescape(r[2]); return null;\n" 
				+"}\n";
		return jspBuildString;
	}
	
	public static String getHeightSearch(String formCode,String formId,String formDefLayout){
	  String jspBuild = SysCardFormBeansUtil.getNbsp(1)+"function grid_"+formCode+"_HeighQuery_"+formId+"() {\n"+
					 	SysCardFormBeansUtil.getNbsp(2)+"layer.open({\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"type: 2,\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"showMaxButton: true,\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"title:\"高级查询设置\" ,\n"+
					  	SysCardFormBeansUtil.getNbsp(3)+"area: ['855', '500'],\n"+
					  	SysCardFormBeansUtil.getNbsp(3)+"maxmin: true,\n"+
					  	SysCardFormBeansUtil.getNbsp(3)+"content:'/myehr/jsp/form/button/heightGrade.jsp?formFunction=grid_"+formCode+"_fun11_"+formId+"&formDefId="+formId+"&formDefLayout="+formDefLayout+"',\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"success:function(layero,index){\n"+
					    SysCardFormBeansUtil.getNbsp(4)+"},\n"+
					    SysCardFormBeansUtil.getNbsp(4)+"end:function(){\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"}\n"+
					    SysCardFormBeansUtil.getNbsp(3)+"});\n"+
					 	SysCardFormBeansUtil.getNbsp(1)+"}\n";
	   return jspBuild;
	}
	
	public static String getBuildJsp12(){
		String jspBuildString ="/**初始化日期控件  */\n"
				+"function initDate(){\n"
				+    "\t\t\t$('.form_date1').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyy-mm-dd\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 2,  \n"+
					        "\t\t\t minView: 2,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date2').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyy-mm-dd hh:ii\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 2,  \n"+
					        "\t\t\t minView: 0,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date3').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"hh:ii\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 0,  \n"+
					        "\t\t\t minView: 2,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date5').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyymm\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 3,  \n"+
					        "\t\t\t minView: 3,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"
				+"} \n";
		return jspBuildString;
	}
	
	public static String getBuildJsp13(){
		String jspBuildString ="/**初始化日期控件  */\n"
				+"function initDate(){\n"
				+    "\t\t\t$('.form_date1').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyy-mm-dd\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 2,  \n"+
					        "\t\t\t minView: 2,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date2').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyy-mm-dd hh:ii\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 2,  \n"+
					        "\t\t\t minView: 0,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date3').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"hh:ii\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 0,  \n"+
					        "\t\t\t minView: 2,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"+
					    "\t\t\t$('.form_date5').datetimepicker({ \n" +
					        "\t\t\t language:  'zh-CN',  \n"+
					        "\t\t\t format:\"yyyymm\",  \n"+
					        "\t\t\t weekStart: 1,  \n"+
					        "\t\t\t todayBtn:  1,  \n"+
					        "\t\t\t autoclose: 1,  \n"+
					        "\t\t\t todayHighlight: 1,  \n"+
					        "\t\t\t startView: 3,  \n"+
					        "\t\t\t minView: 3,  \n"+
					        "\t\t\t forceParse: 0,  \n"+
					        "\t\t\t pickerPosition: \"bottom-left\"  \n"+
					    "\t\t\t});  \n"
				+"} \n"
				+"/**mouseover触发*/\n"
				+"var num = 0;\n"
				+"function openNew(url){\n"	
				+"	closeNew();\n"
				+"	this.$loading = $('<div id=\"open\"  onmouseleave=\"changNum()\" style=\"position:absolute;top:5%;right:0;width:85%;height:90%;z-index:1000\"><iframe style=\"width:100%;height:100%;border:1px solid #ffcc00;\" src=\"'+url+'\"></iframe></div>');\n"
				+"    if(num==0){\n"
				+"    	$('body').append(this.$loading);\n"
				+"    	num++;\n"
				+"    }	\n"
				+"}\n"
				+"function closeNew(){\n"
				+"	if(num==1){\n"
				+"		$(\"#open\").remove();\n"
				+"		num--;\n"
				+"	}\n"
				+"}\n"
				+"function changNum(){\n"
				+"	$(\"#open\").remove();\n"
				+"	num--;	\n"
				+"}\n";
		return jspBuildString;
	}
	
	public static String frontParam1(){
		String jspBuildString ="\nlayer.open({\n"+
									"type: 2,\n"+
									"title:'参数输入',\n"+
									"shadeClose: true,\n"+
									"shade: 0.8,\n"+
									"maxmin:true,\n"+
									"area: ['400px', '500px'],\n"+
									"content: url,\n"+
									"success:function(layero,index){\n"+
									"},\n"+
									"end:function(){\n";
		return jspBuildString;
	}
	
	public static String frontParam2(){
		String jspBuildString = "}\n"+
								"});\n";
		return jspBuildString;
	}
	
	public static String formGridByCardHead(){
		String jspBuildString = "<%@ page language=\"java\" import=\"java.util.*\" import=\"com.myehr.common.util.LangUtil\" pageEncoding=\"UTF-8\"%>\n"+
								"<%@ include file=\"/common/gridbycard.jsp\" %>\n"+
								"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"+
								"<html>\n"+
								"<head>\n"+
								"<meta charset=\"utf-8\">\n"+
							    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"+
							    "<style type=\"text/css\">\n"+
							    "    /*这行很重要!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/\n"+
							    "    html,body{min-height: 100%;}\n"+
							    "</style>\n"+
								"</head>\n"+
								"<body class=\"gray-bg\">\n";
		return jspBuildString;
	}
	
	public static String formGridByCardJsHead(){
		String jspBuildString = "</div>\n"+
								"<script>\n";
		return jspBuildString;
	}
	
	public static String formGridByCardFunction1(){
		String jspBuildString = "//当前页和一次显示多少条数据\n"+
						        "var Currentpage = 0;\n"+
						        "var pagesize = 10;\n"+
								"function animationHover(element, animation) {\n"+
								"    element = $(element);\n"+
								"    element.hover(\n"+
								"        function () {\n"+
								"           element.addClass('animated ' + animation);\n"+
								"        },\n"+
								"        function () {\n"+
								"            //动画完成之前移除class\n"+
								"            window.setTimeout(function () {\n"+
								"                element.removeClass('animated ' + animation);\n"+
								"            }, 2000);\n"+
								"        });\n"+
								"};\n";
		return jspBuildString;
	}
	
	public static String getEmpPhotoSYS(){
		String jspBuildString = "function getEmpPhotoSYS(){\n"+
								"	var objs = $(\"#Employee_Photo_WT\");\n"+
								"	if(objs.length>0){\n"+
								"		$.ajax({ \n"+
								"			   url:'/myehr/FileList/getEmpPhotoSYS.action?EMPID=${param.EMPVEMPLOYEE_EMPID}',\n"+
								"			   type:'post',\n"+
								"			   cache: false,\n"+
								"			   async: false, \n"+
								"			   success: function (data){ \n"+
								"				   $(\"#Employee_Photo_WT\").attr(\"src\",data[0]);\n"+
								"			   }\n"+
								"		   });\n"+
								"	}\n"+
								"}\n";
		return jspBuildString;
	}
	
	public static String formGridByCardFunction5(){
		String jspBuildString = "//作为一个对象的w和h属性返回视口的尺寸\n"+
						        "function getViewportSize(w){\n"+
						        "    w = w || window;\n"+
						        "    if(w.innerWidth != null){\n"+
						        "        return {w: w.innerWidth, h: w.innerHeight};}\n"+
						        "    var d = w.document;\n"+
						        "    if(document.compatMode == \"CSS1Compat\"){\n"+
						        "        return {w: d.documentElement.clientWidth, h: d.documentElement.clientHeight};}\n"+
						
						        "    return {w: d.body.clientWidth, h: d.body.clientHeight};\n"+
						        "}\n"+
						        "//检测滚动条是否滚动到页面底部\n"+
						        "function isScrollToPageBottom(){\n"+
						        "    //文档高度\n"+
						        "    var documentHeight = document.documentElement.offsetHeight;\n"+
						        "    var viewPortHeight = getViewportSize().h;\n"+
						        "    var scrollHeight = window.pageYOffset ||\n"+
						        "            document.documentElement.scrollTop ||\n"+
						        "            document.body.scrollTop || 0;\n"+
						
						        "    return documentHeight - viewPortHeight - scrollHeight < 50;\n"+
						        "}\n"+
						        "//如果滚动条滚动到页面底部，需要加载新的数据,并且显示加载提示\n"+
						        "function watchScroll(){\n"+
						        "    if(!isScrollToPageBottom()){\n"+
						        "        setTimeout( arguments.callee, 900);\n"+
						        "        return;            \n"+
						        "    }//loadDataDynamic();\n"+
						        "}\n"+

						        "//获取区间中的一个随机数\n"+
						        "function rand(n, m){\n"+
						        "    var random = Math.floor(Math.random()*(m-n+1)+n);\n"+
						        "    return random;\n"+
						        "};\n";
		return jspBuildString;
	}
	
	public static String formGridByCardFunction4(String formCode,String formId){
		String jspBuildString = "var Currentpage = 0;\n"+
								"var pagesize = 12;\n"+
								"function grid_"+formCode+"_fun(pageReqeust){\n"+
								"	var pageReqeust={};\n"+
								"	pageReqeust.formId="+formId+";\n"+
								"	pageReqeust.offset=Currentpage*pagesize;\n"+
								"	pageReqeust.limit=pagesize;\n"+
								"	return JSON.stringify(pageReqeust);\n"+
								"};\n";
		return jspBuildString;
	}
	
	public static String formGridByCardFunction2(String formId,String formCode,String paramJs){
		String jspBuildString = "var containerParam_"+formId+"={};\n" +
								"var xx = {};\n" +
								"var main_Datas = [];\n" +
							    "function loadDataStart(){\n"+
							    "$.ajax({\n"+
							    "    url: \"/myehr/form/cardformInitData.action\",\n"+
							    "    type: 'post',\n"+
							    "    data:grid_"+formCode+"_fun(xx),//HTTP请求类型\n"+
							    "    contentType: 'application/json;charset=utf-8',\n"+
							    "    cache: false,\n"+
							    "    async: false,\n"+
							    "    success: function (data) {\n"+
							    "	 	 Currentpage++;\n"+
							    "        console.log(data);\n"+
							    "        var arr=data.rows;\n"+
							    "        main_Datas=data.rows;\n"+
							    "        showDate_"+formId+"(arr);\n"+
							    paramJs+
							    "      }\n"+
							    "});\n"+
								"}"+
								"function loadDataDynamic(){\n"+
								
								"//先显示正在加载中\n"+
								"if( $(\"#loadingLi\").length === 0){\n"+
								"     $(\"#lianxiren\").append(\"<div id='loadingLi' class='loading'>正在加载...</div>\");\n"+
								"}else{\n"+
								"    $(\"#loadingLi\").text(\"正在加载...\").removeClass(\"space\");\n"+
								"}\n"+
								"var loadingLi = document.getElementById(\"loadingLi\");\n"+
								"//loadingLi.scrollIntoView();\n"+
					            "//加载数据,数据加载完成后需要移除加载提示\n"+
					            "var hasData = false, msg = \"\";\n"+
							    "$.ajax({\n"+
							    "    url: \"/myehr/form/cardformInitData.action\",\n"+
							    "    type: 'post',\n"+
							    "    data:grid_"+formCode+"_fun(xx),//HTTP请求类型\n"+
							    "    contentType: 'application/json;charset=utf-8',\n"+
							    "    cache: false,\n"+
							    "    async: false\n"+
							    "}).done(function(data){\n"+
					            "        Currentpage++;\n"+
					            "        console.log(data);\n"+
					            "        var arr=data.rows;\n"+
					            "        if(arr.length > 0){\n"+
					            "            hasData = true;\n"+
					            "            showDate_"+formId+"(arr);\n"+
					            "        }else{\n"+
					            "            msg = \"数据已加载到底了\"\n"+
					            "        }\n"+
					            "        $(\"#lianxiren\").append(loadingLi);\n"+
					            "}).fail(function(){\n"+
					            "    msg = \"数据加载失败!\";\n"+
					            " }).always(function(){\n"+
					            "    !hasData && setTimeout(function(){\n"+
					            "        $(document.body).scrollTop(document.body.scrollTop -40);\n"+
					            "    }, 500);\n"+
					            "       msg && $(\"#loadingLi\").text(msg);\n"+
					            "    //重新监听滚动\n"+
					            "    setTimeout(watchScroll, 900);\n"+
					            " });\n"+
					            "}\n";
		return jspBuildString;
	}
	public static String formGridByCardFunction2_x(String formId,String formCode,String paramJs){
		String jspBuildString = "var containerParam_"+formId+"={};\n" +
								"var xx = {};\n" +
								"var main_Datas = [];\n" +
							    "function loadDataStart(){\n"+
							    "$.ajax({\n"+
							    "    url: \"/myehr/form/cardformInitData.action\",\n"+
							    "    type: 'post',\n"+
							    "    data:grid_"+formCode+"_fun(xx),//HTTP请求类型\n"+
							    "    contentType: 'application/json;charset=utf-8',\n"+
							    "    cache: false,\n"+
							    "    async: false,\n"+
							    "    success: function (data) {\n"+
							    "	 	 Currentpage++;\n"+
							    "        console.log(data);\n"+
							    "        var arr=data.rows;\n"+
							    "        main_Datas=data.rows;\n";
				/*if (!isUpdate.equals("true")) {
					jspBuildString+=" showDate_"+formId+"(arr);\n";
				}*/			    
				jspBuildString +=paramJs+
							    "      }\n"+
							    "});\n"+
								"}\n";
		return jspBuildString;
	}
	public static String formGridByCardFunction2_1(String formId){
		String jspBuildString = "loadDataStart_"+formId+"();\n";
		return jspBuildString;
	}
	public static String formGridByCardFunction2_2(String formId,String formCode){
		String jspBuildString = "function loadDataStart_"+formId+"(){\n"+
							    "$.ajax({\n"+
							    "    url: \"/myehr/form/cardformInitData.action\",\n"+
							    "    type: 'post',\n"+
							    "    data:grid_"+formCode+"_fun(xx),//HTTP请求类型\n"+
							    "    contentType: 'application/json;charset=utf-8',\n"+
							    "    cache: false,\n"+
							    "    async: false,\n"+
							    "    success: function (data) {\n"+
							    "        var arr=data.rows;\n"+
							    "        showDate_"+formId+"(arr);\n"+
							    "      }\n"+
							    "});\n"+
								"}";
		return jspBuildString;
	}
	
	/**
	 * 1：联系人模板
	 * 2：报表式模板
	 * 3：预警式模板
	 * 4：无线扩展式模板
	 * 5：联系人无线扩展字段
	 * @param sList
	 * @param form
	 * @return
	 */
	public static String formGridByCardFunction3_NACT(List<SysGridbycardTemplateColumn> sList,SysFormconfigCache form){
		String formCode = form.getFormDefCode();
		String formId = form.getFormDefId()+"";
		if(sList.get(0).getTemplateColumnTemplateId()==5){
			String[] title = sList.get(1).getTemplateColumnValue().split("&");
			String[] position = sList.get(2).getTemplateColumnValue().split("&");
			String photo = sList.get(3).getTemplateColumnValue().split("\\|")[0];
			String name = sList.get(4).getTemplateColumnValue().split("\\|")[0];
			String[] contentLabel = new String[50];
			String[] content = new String[50];
			int largeNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[0]));
			int middleNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[1]));
			int smallNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[2]));
			String titles = "";//
			String initTitles = "";
			String paramStrings = "";
			for (int i = 0; i < title.length; i++) {
				if (i==0) {
					titles = title[i].split("\\|")[0];
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
				} else {
					titles  = titles +"+'-'+" +title[i].split("\\|")[0];
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
				}
				String dateType = checkDateType(form.getColumns(),title[i].split("\\|")[2]);
				if (dateType.equals("false")) {
					initTitles+="if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"==null){\n"+
							"	 "+title[i].split("\\|")[0]+" = '';\n"+
							"}\n";
				}else if (dateType.indexOf("|")!=-1) {
					initTitles+= "if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"!=null){\n" +
								dateType.split("\\|")[0]+
								title[i].split("\\|")[0]+" = "+dateType.split("\\|")[1]+"[arr[i]."+title[i].split("\\|")[0].toUpperCase()+"];\n"+
						"}else{\n" +
						title[i].split("\\|")[0]+" = '';\n" +
						"}\n";
				}else {
					initTitles+="if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
							"	 "+title[i].split("\\|")[0]+" = new Date("+title[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
							"}else{\n" +
							title[i].split("\\|")[0]+" = '';\n" +
							"}\n";
				}
				paramStrings += "&"+title[i].split("\\|")[0]+"='+"+title[i].split("\\|")[0]+"+'";
			}
			String positions = "";
			String initPositions = "";
			if(!position[0].equals("")){
				if (sList.get(2).getTemplateIsTag()!=null && sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
					
				} else {
					for (int i = 0; i < position.length; i++) {
						if (i==0) {
							positions = "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						} else {
							positions  = positions +"+'" + sList.get(2).getTemplateColumnLinkMark()+"'+" + "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						}
						if (i==0) {
							positions = position[i].split("\\|")[0];//code_id
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
						} else {
							positions  = positions +"+'-'+" +position[i].split("\\|")[0];
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
						}
						String dateType = checkDateType(form.getColumns(),position[i].split("\\|")[2]);
						if (dateType.equals("false")) {
							initPositions+="if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"==null){\n"+
									"	 "+position[i].split("\\|")[0]+" = '';\n"+
									"}\n";
						}else if (dateType.indexOf("|")!=-1) {
							initPositions+= "if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"!=null){\n" +
											dateType.split("\\|")[0]+
											position[i].split("\\|")[0]+" = "+dateType.split("\\|")[1]+"[arr[i]."+position[i].split("\\|")[0].toUpperCase()+"];\n"+
									"}else{\n" +
									position[i].split("\\|")[0]+" = ''\n" +
									"}\n";
						}else {
							initPositions+="if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
									"	 "+position[i].split("\\|")[0]+" = new Date("+position[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
									"}else{\n" +
									position[i].split("\\|")[0]+" = ''\n" +
									"}\n";
						}
						paramStrings += "&"+position[i].split("\\|")[0]+"='+"+position[i].split("\\|")[0]+"+'";
					}
				}
			}
			
			int contentNum = 0;
			List<SysFormColumnCache> caches = new ArrayList<SysFormColumnCache>();
			for(int x=5,y=0;x<sList.size();x++,y++){
				logger.info(x+"");
				logger.info(sList.get(x).getTemplateColumnValue().split("\\|")[0]);
				logger.info(sList.get(x).getTemplateColumnValue().split("\\|")[1]);
				contentLabel[y] = sList.get(x).getTemplateColumnValue().split("\\|")[1];
				content[y] = sList.get(x).getTemplateColumnValue().split("\\|")[0];
				contentNum = y;
				initPositions += "var "+content[y] +" = arr[i]."+content[y].toUpperCase()+";\n";
				String dateType = checkDateType(form.getColumns(),sList.get(x).getTemplateColumnValue().split("\\|")[2]);
				caches.add(getColumnById(form.getColumns(),sList.get(x).getTemplateColumnValue().split("\\|")[2]));
				if (dateType.equals("false")) {
					initPositions+="if(arr[i]."+content[y].toUpperCase()+"==null){\n"+
							"	 "+content[y]+" = '';\n"+
							"}\n";
				}else if (dateType.indexOf("|")!=-1) {
					initPositions+= "if(arr[i]."+content[y].toUpperCase()+"!=null){\n" +
									dateType.split("\\|")[0]+
									"	 "+content[y]+" = "+dateType.split("\\|")[1]+"[arr[i]."+content[y].toUpperCase()+"];\n"+
									"}else{\n" +
									content[y]+" = '';"+
									"}\n";
				}else {
					initPositions+= "if(arr[i]."+content[y].toUpperCase()+"!=null){\n"+
							"	 "+content[y]+" = new Date("+content[y]+").Format(\""+dateType+"\");\n"+
							"}else{\n" +
							content[y]+" = '';"+
							"}\n";
				}
				
				paramStrings += "&"+sList.get(x).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(x).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			String[] photos = photo.split("_");
			String jspBuildString = "function showDate_"+formId+"(arr) {\n"+
							        "for (var i = arr.length - 1; i >= 0; i--) {\n"+
									initTitles+
									initPositions+
							        "    var cellObj = $(\n"+
							        "        '<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
							        "        '    <div class=\"contact-box\">'+\n";
			if (sList.get(0).getTemplateColumnLinkMark()!=null&&!sList.get(0).getTemplateColumnLinkMark().equals("")) {
				if(isNumeric(sList.get(0).getTemplateColumnLinkMark())){
					//纯数字是动态表单
					jspBuildString +="        '        <a href=\"/myehr/form/toForm.action?formId="+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}else{
					jspBuildString +="        '        <a href=\""+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}
			}		        
									if(photo==null||photo.equals("")){
										
									}else if (photos[photos.length-1].toUpperCase().equals("EMPID")) {
													jspBuildString +=  	"        '            <div class=\"col-sm-4\">'+\n"+
													        "        '                <div class=\"text-center\">'+\n"+
													        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" id=\"Employee_Photo_WT\" class=\"img-circle m-t-xs img-responsive\" >'+\n"+
													        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
													        "        '                </div>'+\n"+
													        "        '            </div>'+\n";	
									}else {
										jspBuildString +=  			"        '            <div class=\"col-sm-4\">'+\n"+
															        "        '                <div class=\"text-center\">'+\n"+
															        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" title=\"'+"+titles+"+'\" class=\"img-circle m-t-xs img-responsive\" src=\"http://116.62.243.28:9875/'+arr[i]."+photo.toUpperCase()+"+'\">'+\n"+
															        "        '                    <div class=\"m-t-xs font-bold\" title=\"'+"+titles+"+'\">'+arr[i]."+name.toUpperCase()+"+'</div>'+\n"+
															        "        '                </div>'+\n"+
															        "        '            </div>'+\n";							
									}
							       
							        
			jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
							        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
			if(position[0]==null||position[0].equals("")){
												
											}else {
												if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
													jspBuildString += "'<div>";
													for (int i = 0; i < position.length; i++) {
														int num=i%5+1;
														jspBuildString += "  			  <p style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;\" title=\"'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'\" class=\"tag"+num+" left\" >'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'</p>";
													}
													jspBuildString += "</div>'+\n'<br>'+\n";
												} else {
													jspBuildString +=  		"        '                <p style=\"overflow:hidden;height:20px;font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;color:"+sList.get(2).getTemplateColumnFontColor()+"\" title=\"'+"+positions+"+'\"><i class=\"fa fa-map-marker\"></i> '+"+positions+"+'</p>'+\n";							
												}
											}				       
							        
							        
			jspBuildString +=				        "        '                <div class=\"row\" id=\"row\" style=\"width:500px\">'+\n";
			for(int i = 0 ;i<=contentNum;i++){
				SysFormColumnCache columnCache = caches.get(i);
				String id = ChangeCode.getUniqueCode(columnCache.getFormEntityTablename(), columnCache.getFormFiledTablename())+formId;
				  jspBuildString += //"        '            <p style=\"color:"+sList.get(i+5).getTemplateColumnFontColor()+";font-size:"+sList.get(i+5).getTemplateColumnFontSize()+"px\">"+contentLabel[i]+":'+ "+content[i]+" +'</p>'+\n";
					        		"		 '				<div class=\"col-lg-"+smallNum+" col-md-"+smallNum+" col-sm-"+smallNum+" col-xs-"+smallNum+"\">'+\n"+
									"		 '					<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;width:200px\">'+\n"+
									"		 '						<label style=\"float:left;font-size:12px\">"+contentLabel[i]+":</label><input class=\"xxxx\" readonly style=\"overflow:hidden;border:none;max-width:100px;font-size:12px;line-height:15px\" title=\"'+ "+content[i]+" +'\" id="+id+" value=\"'+ "+content[i]+" +'\" />'+\n"+
									"		 '					</div>'+\n"+
									"		 '				</div>'+\n";
									}
			      jspBuildString += "        '                </div>'+\n"+
							        "        '            </div>'+\n"+
							        "        '            <div class=\"clearfix\"></div>'+\n"+
							        "        '        </a>'+\n"+
							        "        '    </div>'+\n"+
							        "        '</div>');\n"+
							        //"    animationHover(cellObj, 'pulse');\n"+
							        "    $('#"+formCode+"').append(cellObj);\n"+
							        "}\n";
			return jspBuildString;
		}else {
			return null;
		}
	}
	
	/**
	 * 1：联系人模板
	 * 2：报表式模板
	 * 3：预警式模板
	 * 4：无线扩展式模板
	 * 5：联系人无线扩展字段
	 * @param sList
	 * @param form
	 * @return
	 */
	public static String formGridByCardFunction3(List<SysGridbycardTemplateColumn> sList,SysFormconfigCache form){
		String formCode = form.getFormDefCode();
		String formId = form.getFormDefId()+"";
		if(sList.get(0).getTemplateColumnTemplateId()==2){
			String[] right1 = new String[1];
			String[] right2 = new String[1];
			String[] right3 = new String[1];
			String[] right4 = new String[1];
			String right1s = "";
			String right2s = "";
			String right3s = "";
			String right4s = "";
			String left = "";
			if(sList.get(1).getTemplateColumnValue()!=null){
				right1 = sList.get(1).getTemplateColumnValue().split("&");
				for (int i = 0; i < right1.length; i++) {
					if (i==0) { 
						right1s = "arr[i]."+right1[i].split("\\|")[0].toUpperCase();
					} else {
						right1s  = right1s +"+'"+ sList.get(1).getTemplateColumnLinkMark()+"'+" + "arr[i]."+right1[i].split("\\|")[0].toUpperCase();
					}
				}
			}
			if(sList.get(2).getTemplateColumnValue()!=null){
				right2 = sList.get(2).getTemplateColumnValue().split("&");
				for (int i = 0; i < right2.length; i++) {
					if (i==0) {
						right2s = "arr[i]."+right2[i].split("\\|")[0].toUpperCase();
					} else {
						right2s  = right2s +"+'"+ sList.get(2).getTemplateColumnLinkMark()+"'+" + "arr[i]."+right2[i].split("\\|")[0].toUpperCase();
					}
				}
			}
			if(sList.get(3).getTemplateColumnValue()!=null){
				right3 = sList.get(3).getTemplateColumnValue().split("&");
				for (int i = 0; i < right3.length; i++) {
					if (i==0) {
						right3s = "arr[i]."+right3[i].split("\\|")[0].toUpperCase();
					} else {
						right3s  = right3s +"+'"+ sList.get(3).getTemplateColumnLinkMark()+"'+" + "arr[i]."+right3[i].split("\\|")[0].toUpperCase();
					}
				}
			}
			if(sList.get(4).getTemplateColumnValue()!=null){
				right4 = sList.get(4).getTemplateColumnValue().split("&");
				for (int i = 0; i < right4.length; i++) {
					if (i==0) {
						right4s = "arr[i]."+right4[i].split("\\|")[0].toUpperCase();
					} else {
						right4s  = right4s +"+'"+ sList.get(4).getTemplateColumnLinkMark()+"'+" + "arr[i]."+right4[i].split("\\|")[0].toUpperCase();
					}
				}
			}
			if(sList.get(5).getTemplateColumnValue()!=null){
				left = sList.get(5).getTemplateColumnValue();
			}
			
			String[] contentLabel = new String[10];
			String[] content = new String[10];
			int largeNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[0]));
			int middleNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[1]));
			int smallNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[2]));
			String jspBuildString = "function showDate_"+formId+"(arr) {\n"+
									"var colors = new Array(\"#578ebe\",\"#e35b5a\",\"#44b6ae\",\"#8775a7\");\n";
			if(left!=null&&!left.equals("")){
				   jspBuildString += "for (var i = arr.length - 1; i >= 0; i--) {\n"+
									//"var colorNum = i%4;\n"+
									"var colorNum = Math.ceil(Math.random()*4);"+
							        "    var cellObj = $(\n" +
							        "			'<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
									"		'    <div class=\"widget style1 navy-bg\" style=\"background-color:'+colors[colorNum]+'\">'+\n"+
									"		'        <div class=\"row\" id=\"row\" >'+\n"+
									"		'            <div class=\"col-xs-4\">'+\n"+
									"		'                <span style=\"font-size:"+sList.get(5).getTemplateColumnFontSize()+";color:"+sList.get(5).getTemplateColumnFontColor()+";\" class=\""+left+"\">'+\n"+
									"		'            </div>'+\n"+
									"		'            <div class=\"col-xs-8 text-right\">'+\n";
				if(right1[0]!=null&&!right1.equals("")){
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(1).getTemplateColumnFontSize()+";color:"+sList.get(1).getTemplateColumnFontColor()+";font-weight:"+sList.get(1).getTemplateColumnFontWeight()+";\">'+"+right1s+"+'</span></div>'+\n";
									}
				if(right2[0]!=null&&!right2.equals("")){
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+";color:"+sList.get(2).getTemplateColumnFontColor()+";font-weight:"+sList.get(2).getTemplateColumnFontWeight()+";\">'+"+right2s+"+'</span></div>'+\n";
									}
				if(right3[0]!=null&&!right3.equals("")){
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(3).getTemplateColumnFontSize()+";color:"+sList.get(3).getTemplateColumnFontColor()+";font-weight:"+sList.get(3).getTemplateColumnFontWeight()+";\">'+"+right3s+"+'</span></div>'+\n";
									}
				if(right4[0]!=null&&!right4.equals("")){
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(4).getTemplateColumnFontSize()+";color:"+sList.get(4).getTemplateColumnFontColor()+";font-weight:"+sList.get(4).getTemplateColumnFontWeight()+";\">'+"+right4s+"+'</span></div>'+\n";
									}
					
				   jspBuildString += "		'            </div>'+\n"+
									"		'        </div>'+\n"+
									"		'    </div>'+\n" +
									"		'</div>');\n" +
									//"animationHover(cellObj, 'pulse');\n"+
									"$('#"+formCode+"').append(cellObj);\n" +
									"}\n" +
									"}\n";
			}else{
				   jspBuildString += "for (var i = arr.length - 1; i >= 0; i--) {\n"+
									//"var colorNum = i%4;\n"+
									"var colorNum = Math.ceil(Math.random()*4);"+
							        "    var cellObj = $(\n"+
							        "			'<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
									"		'    <div class=\"widget style1 navy-bg\" style=\"background-color:'+colors[colorNum]+'\">'+\n"+
									"		'        <div class=\"row\" id=\"row\" >'+\n"+
									"		'            <div class=\"col-xs-8 text-right\">'+\n";
				   if(right1[0]!=null&&!right1.equals("")){
					   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(1).getTemplateColumnFontSize()+";color:"+sList.get(1).getTemplateColumnFontColor()+";font-weight:"+sList.get(1).getTemplateColumnFontWeight()+";\">'+"+right1s+"+'</span></div>'+\n";
										}
					if(right2[0]!=null&&!right2.equals("")){
					   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+";color:"+sList.get(2).getTemplateColumnFontColor()+";font-weight:"+sList.get(2).getTemplateColumnFontWeight()+";\">'+"+right2s+"+'</span></div>'+\n";
										}
					if(right3[0]!=null&&!right3.equals("")){
					   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(3).getTemplateColumnFontSize()+";color:"+sList.get(3).getTemplateColumnFontColor()+";font-weight:"+sList.get(3).getTemplateColumnFontWeight()+";\">'+"+right3s+"+'</span></div>'+\n";
										}
					if(right4[0]!=null&&!right4.equals("")){
					   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"font-size:"+sList.get(4).getTemplateColumnFontSize()+";color:"+sList.get(4).getTemplateColumnFontColor()+";font-weight:"+sList.get(4).getTemplateColumnFontWeight()+";\">'+"+right4s+"+'</span></div>'+\n";
										}
					
				   jspBuildString += "		'            </div>'+\n"+
									"		'        </div>'+\n"+
									"		'    </div>'+\n"+
									"		'</div>');\n" +
									//"animationHover(cellObj, 'pulse');\n"+
									"$('#"+formCode+"').append(cellObj);\n" +
									"}\n";				
			}
			return jspBuildString;
		}else if(sList.get(0).getTemplateColumnTemplateId()==1){
			String[] title = sList.get(1).getTemplateColumnValue().split("&");
			String[] position = sList.get(2).getTemplateColumnValue().split("&");
			String photo = sList.get(3).getTemplateColumnValue().split("\\|")[0];
			String name = sList.get(4).getTemplateColumnValue().split("\\|")[0];
			String[] contentLabel = new String[10];
			String[] content = new String[10];
			int largeNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[0]));
			int middleNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[1]));
			int smallNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[2]));
			String titles = "";//
			String initTitles = "";
			String paramStrings = "";
			for (int i = 0; i < title.length; i++) {
				if (i==0) {
					titles = title[i].split("\\|")[0];//code_id
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
					//titles = "arr[i]."+title[i].split("\\|")[0];
				} else {
					titles  = titles +"+'-'+" +title[i].split("\\|")[0];
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
					//titles  = titles +"+'"+ sList.get(1).getTemplateColumnLinkMark()+"'+" + "arr[i]."+title[i].split("\\|")[0];
				}
				String dateType = isDateTime(form.getColumns(),title[i].split("\\|")[2]);
				if (!dateType.equals("false")) {
					initTitles+="if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
								"	 "+title[i].split("\\|")[0]+" = new Date("+title[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
								"}\n";
				}
				paramStrings += "&"+title[i].split("\\|")[0]+"='+"+title[i].split("\\|")[0]+"+'";
			}
			String positions = "";
			String initPositions = "";
			if(!position[0].equals("")){
				if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
					
				} else {
					for (int i = 0; i < position.length; i++) {
						if (i==0) {
							positions = "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						} else {
							positions  = positions +"+'" + sList.get(2).getTemplateColumnLinkMark()+"'+" + "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						}
						if (i==0) {
							positions = position[i].split("\\|")[0];//code_id
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
							//titles = "arr[i]."+title[i].split("\\|")[0];
						} else {
							positions  = positions +"+'-'+" +position[i].split("\\|")[0];
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
							//titles  = titles +"+'"+ sList.get(1).getTemplateColumnLinkMark()+"'+" + "arr[i]."+title[i].split("\\|")[0];
						}
						String dateType = isDateTime(form.getColumns(),position[i].split("\\|")[2]);
						if (!dateType.equals("false")) {
							initPositions+="if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
									"	 "+position[i].split("\\|")[0]+" = new Date("+position[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
									"}\n";
						}
						paramStrings += "&"+position[i].split("\\|")[0]+"='+"+position[i].split("\\|")[0]+"+'";
					}
				}
			}
			
			int contentNum = 0;
			if (sList.get(5).getTemplateColumnValue()!=null&&!sList.get(5).getTemplateColumnValue().equals("")) {
				contentLabel[0] = sList.get(5).getTemplateColumnValue().split("\\|")[1];
				content[0] = sList.get(5).getTemplateColumnValue().split("\\|")[0];
				contentNum = 1;
				initPositions += "var "+content[0] +" = arr[i]."+content[0].toUpperCase()+";\n";
				String dateType = isDateTime(form.getColumns(),sList.get(5).getTemplateColumnValue().split("\\|")[2]);
				if (!dateType.equals("false")) {
					initPositions+= "if(arr[i]."+content[0].toUpperCase()+"!=null){\n"+
							"	 "+content[0]+" = new Date("+content[0]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}
				paramStrings += "&"+sList.get(5).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(5).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			if (sList.get(6).getTemplateColumnValue()!=null&&!sList.get(6).getTemplateColumnValue().equals("")) {
				contentLabel[1] = sList.get(6).getTemplateColumnValue().split("\\|")[1];
				content[1] = sList.get(6).getTemplateColumnValue().split("\\|")[0];
				contentNum = 2;
				initPositions += "var "+content[1] +" = arr[i]."+content[1].toUpperCase()+";\n";
				String dateType = isDateTime(form.getColumns(),sList.get(6).getTemplateColumnValue().split("\\|")[2]);
				if (!dateType.equals("false")) {
					initPositions+= "if(arr[i]."+content[1].toUpperCase()+"!=null){\n"+
							"	 "+content[1]+" = new Date("+content[1]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}
				paramStrings += "&"+sList.get(6).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(6).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			if (sList.get(7).getTemplateColumnValue()!=null&&!sList.get(7).getTemplateColumnValue().equals("")) {
				contentLabel[2] = sList.get(7).getTemplateColumnValue().split("\\|")[1];
				content[2] = sList.get(7).getTemplateColumnValue().split("\\|")[0];
				contentNum = 3;
				initPositions += "var "+content[2] +" = arr[i]."+content[2].toUpperCase()+";\n";
				String dateType = isDateTime(form.getColumns(),sList.get(7).getTemplateColumnValue().split("\\|")[2]);
				if (!dateType.equals("false")) {
					initPositions+= "if(arr[i]."+content[2].toUpperCase()+"!=null){\n"+
							"	 "+content[2]+" = new Date("+content[2]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}
				paramStrings += "&"+sList.get(7).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(7).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			if (sList.get(8).getTemplateColumnValue()!=null&&!sList.get(8).getTemplateColumnValue().equals("")) {
				contentLabel[3] = sList.get(8).getTemplateColumnValue().split("\\|")[1];
				content[3] = sList.get(8).getTemplateColumnValue().split("\\|")[0];
				contentNum = 4;
				initPositions += "var "+content[3] +" = arr[i]."+content[3].toUpperCase()+";\n";
				String dateType = isDateTime(form.getColumns(),sList.get(8).getTemplateColumnValue().split("\\|")[2]);
				if (!dateType.equals("false")) {
					initPositions+= "if(arr[i]."+content[3].toUpperCase()+"!=null){\n"+
							"	 "+content[3]+" = new Date("+content[3]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}
				paramStrings += "&"+sList.get(8).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(8).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			
			String jspBuildString = "function showDate_"+formId+"(arr) {\n"+
									"if('${param.flowAction}'=='start'){\n"+
									"    var cellObj = $(\n"+
							        "        '<div class=\"col-md-"+middleNum+" col-sm-"+smallNum+" col-lg-"+largeNum+"\">'+\n"+
							        "        '    <div class=\"contact-box\">'+\n";
			if (sList.get(0).getTemplateColumnLinkMark()!=null&&!sList.get(0).getTemplateColumnLinkMark().equals("")) {
				if(isNumeric(sList.get(0).getTemplateColumnLinkMark())){
					//纯数字是动态表单
					jspBuildString +="        '        <a href=\"/myehr/form/toForm.action?formId="+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}else{
					jspBuildString +="        '        <a href=\""+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}
			}		        
			String[] photos = photo.split("_");
									if(photo==null||photo.equals("")){
										
									}else if (photos[photos.length-1].toUpperCase().equals("EMPID")) {
										jspBuildString +=  	"        '            <div class=\"col-sm-4\">'+\n"+
										        "        '                <div class=\"text-center\">'+\n"+
										        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" id=\"Employee_Photo_WT\" class=\"img-circle m-t-xs img-responsive\" >'+\n"+
										        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
										        "        '                </div>'+\n"+
										        "        '            </div>'+\n";	
						}else {
										jspBuildString +=  			"        '            <div class=\"col-sm-4\">'+\n"+
															        "        '                <div class=\"text-center\">'+\n"+
															        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" title=\"'+"+titles+"+'\" class=\"img-circle m-t-xs img-responsive\" src=\"http://116.62.243.28:9875/'+arr[i]."+photo.toUpperCase()+"+'\">'+\n"+
															        "        '                    <div class=\"m-t-xs font-bold\" title=\"'+"+titles+"+'\">'+arr[i]."+name.toUpperCase()+"+'</div>'+\n"+
															        "        '                </div>'+\n"+
															        "        '            </div>'+\n";							
									}
							       
			if(sList.get(1).getTemplateColumnFontSize()!=null&&!sList.get(1).getTemplateColumnFontSize().equals("")){				        
				jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
				        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;font-size:"+sList.get(1).getTemplateColumnFontSize()+"px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
				
			}else {
				jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
				        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
			}
			
			if(position[0]==null||position[0].equals("")){
												
											}else {
												if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
													jspBuildString += "'<div>";
													for (int i = 0; i < position.length; i++) {
														int num=i%5+1;
														jspBuildString += "  			  <p style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;\" title=\"'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'\" class=\"tag"+num+" left\" >'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'</p>";
													}
													jspBuildString += "</div>'+\n'<br>'+\n";
												} else {
													jspBuildString +=  		"        '                <p style=\"overflow:hidden;height:20px;font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;color:"+sList.get(2).getTemplateColumnFontColor()+"\" title=\"'+"+positions+"+'\"><i class=\"fa fa-map-marker\"></i> '+"+positions+"+'</p>'+\n";							
												}
											}				       
							        
							        
			jspBuildString +=				        "        '                <address style=\"margin-top:7px\">'+\n";
			for(int i = 0 ;i<contentNum;i++){
				  jspBuildString += "        '            <p style=\"color:"+sList.get(i+5).getTemplateColumnFontColor()+";font-size:"+sList.get(i+5).getTemplateColumnFontSize()+"px\">"+contentLabel[i]+":'+ "+content[i]+" +'</p>'+\n";
			}		        
			      jspBuildString += "        '                </address>'+\n"+
							        "        '            </div>'+\n"+
							        "        '            <div class=\"clearfix\"></div>'+\n"+
							        "        '        </a>'+\n"+
							        "        '    </div>'+\n"+
							        "        '</div>');\n"+
							        //"    animationHover(cellObj, 'pulse');\n"+
							        "    $('#"+formCode+"').append(cellObj);\n"+
							        "	}\n"+
									"}else{\n"+
							        "for (var i = arr.length - 1; i >= 0; i--) {\n"+
									initTitles+
									initPositions+
							        "    var cellObj = $(\n"+
							        "        '<div class=\"col-md-"+middleNum+" col-sm-"+smallNum+" col-lg-"+largeNum+"\">'+\n"+
							        "        '    <div class=\"contact-box\">'+\n";
			if (sList.get(0).getTemplateColumnLinkMark()!=null&&!sList.get(0).getTemplateColumnLinkMark().equals("")) {
				if(isNumeric(sList.get(0).getTemplateColumnLinkMark())){
					//纯数字是动态表单
					jspBuildString +="        '        <a href=\"/myehr/form/toForm.action?formId="+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}else{
					jspBuildString +="        '        <a href=\""+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}
			}		        
									if(photo==null||photo.equals("")){
										
									}else if (photos[photos.length-1].toUpperCase().equals("EMPID")) {
										jspBuildString +=  	"        '            <div class=\"col-sm-4\">'+\n"+
										        "        '                <div class=\"text-center\">'+\n"+
										        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" id=\"Employee_Photo_WT\" class=\"img-circle m-t-xs img-responsive\" >'+\n"+
										        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
										        "        '                </div>'+\n"+
										        "        '            </div>'+\n";	
						}else {
										jspBuildString +=  			"        '            <div class=\"col-sm-4\">'+\n"+
															        "        '                <div class=\"text-center\">'+\n"+
															        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" title=\"'+"+titles+"+'\" class=\"img-circle m-t-xs img-responsive\" src=\"http://116.62.243.28:9875/'+arr[i]."+photo.toUpperCase()+"+'\">'+\n"+
															        "        '                    <div class=\"m-t-xs font-bold\" title=\"'+"+titles+"+'\">'+arr[i]."+name.toUpperCase()+"+'</div>'+\n"+
															        "        '                </div>'+\n"+
															        "        '            </div>'+\n";							
									}
							       
			if(sList.get(1).getTemplateColumnFontSize()!=null&&!sList.get(1).getTemplateColumnFontSize().equals("")){				        
				jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
				        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;font-size:"+sList.get(1).getTemplateColumnFontSize()+"px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
				
			}else {
				jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
				        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
			}
			
			if(position[0]==null||position[0].equals("")){
												
											}else {
												if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
													jspBuildString += "'<div>";
													for (int i = 0; i < position.length; i++) {
														int num=i%5+1;
														jspBuildString += "  			  <p style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;\" title=\"'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'\" class=\"tag"+num+" left\" >'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'</p>";
													}
													jspBuildString += "</div>'+\n'<br>'+\n";
												} else {
													jspBuildString +=  		"        '                <p style=\"overflow:hidden;height:20px;font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;color:"+sList.get(2).getTemplateColumnFontColor()+"\" title=\"'+"+positions+"+'\"><i class=\"fa fa-map-marker\"></i> '+"+positions+"+'</p>'+\n";							
												}
											}				       
							        
							        
			jspBuildString +=				        "        '                <address style=\"margin-top:7px\">'+\n";
			for(int i = 0 ;i<contentNum;i++){
				  jspBuildString += "        '            <p style=\"color:"+sList.get(i+5).getTemplateColumnFontColor()+";font-size:"+sList.get(i+5).getTemplateColumnFontSize()+"px\">"+contentLabel[i]+":'+ "+content[i]+" +'</p>'+\n";
			}		        
			      jspBuildString += "        '                </address>'+\n"+
							        "        '            </div>'+\n"+
							        "        '            <div class=\"clearfix\"></div>'+\n"+
							        "        '        </a>'+\n"+
							        "        '    </div>'+\n"+
							        "        '</div>');\n"+
							        //"    animationHover(cellObj, 'pulse');\n"+
							        "    $('#"+formCode+"').append(cellObj);\n"+
							        "	}\n"+
							        "}\n";
			      
			return jspBuildString;
		}else if(sList.get(0).getTemplateColumnTemplateId()==5){
			String[] title = sList.get(1).getTemplateColumnValue().split("&");
			String[] position = sList.get(2).getTemplateColumnValue().split("&");
			String photo = sList.get(3).getTemplateColumnValue().split("\\|")[0];
			String name = sList.get(4).getTemplateColumnValue().split("\\|")[0];
			String[] contentLabel = new String[50];
			String[] content = new String[50];
			int largeNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[0]));
			int middleNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[1]));
			int smallNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[2]));
			String titles = "";//
			String initTitles = "";
			String paramStrings = "";
			for (int i = 0; i < title.length; i++) {
				if (i==0) {
					titles = title[i].split("\\|")[0];//code_id
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
					//titles = "arr[i]."+title[i].split("\\|")[0];
				} else {
					titles  = titles +"+'-'+" +title[i].split("\\|")[0];
					initTitles += "var "+title[i].split("\\|")[0]+" = arr[i]."+title[i].split("\\|")[0].toUpperCase()+";\n";
					//titles  = titles +"+'"+ sList.get(1).getTemplateColumnLinkMark()+"'+" + "arr[i]."+title[i].split("\\|")[0];
				}
				String dateType = isDateTime(form.getColumns(),title[i].split("\\|")[2]);
				if (!dateType.equals("false")) {
					initTitles+="if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
							"	 "+title[i].split("\\|")[0]+" = new Date("+title[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}else {
					initTitles+="if(arr[i]."+title[i].split("\\|")[0].toUpperCase()+"==null){\n"+
								"	 "+title[i].split("\\|")[0]+" = '';\n"+
								"}\n";
				}
				paramStrings += "&"+title[i].split("\\|")[0]+"='+"+title[i].split("\\|")[0]+"+'";
			}
			String positions = "";
			String initPositions = "";
			if(!position[0].equals("")){
				if (sList.get(2).getTemplateIsTag()!=null && sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
					
				} else {
					for (int i = 0; i < position.length; i++) {
						if (i==0) {
							positions = "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						} else {
							positions  = positions +"+'" + sList.get(2).getTemplateColumnLinkMark()+"'+" + "arr[i]."+position[i].split("\\|")[0].toUpperCase();
						}
						if (i==0) {
							positions = position[i].split("\\|")[0];//code_id
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
							//titles = "arr[i]."+title[i].split("\\|")[0];
						} else {
							positions  = positions +"+'-'+" +position[i].split("\\|")[0];
							initPositions += "var "+position[i].split("\\|")[0]+" = arr[i]."+position[i].split("\\|")[0].toUpperCase()+";\n";
							//titles  = titles +"+'"+ sList.get(1).getTemplateColumnLinkMark()+"'+" + "arr[i]."+title[i].split("\\|")[0];
						}
						String dateType = isDateTime(form.getColumns(),position[i].split("\\|")[2]);
						if (!dateType.equals("false")) {
							initPositions+="if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"!=null){\n"+
									"	 "+position[i].split("\\|")[0]+" = new Date("+position[i].split("\\|")[0]+").Format(\""+dateType+"\");\n"+
									"}\n";
						}else {
							initPositions+="if(arr[i]."+position[i].split("\\|")[0].toUpperCase()+"==null){\n"+
									"	 "+position[i].split("\\|")[0]+" = '';\n"+
									"}\n";
						}
						paramStrings += "&"+position[i].split("\\|")[0]+"='+"+position[i].split("\\|")[0]+"+'";
					}
				}
			}
			
			int contentNum = 0;
			
			for(int x=5,y=0;x<sList.size();x++,y++){
				logger.info(x+"");
				logger.info(sList.get(x).getTemplateColumnValue().split("\\|")[0]);
				logger.info(sList.get(x).getTemplateColumnValue().split("\\|")[1]);
				contentLabel[y] = sList.get(x).getTemplateColumnValue().split("\\|")[1];
				content[y] = sList.get(x).getTemplateColumnValue().split("\\|")[0];
				contentNum = y;
				initPositions += "var "+content[y] +" = arr[i]."+content[y].toUpperCase()+";\n";
				String dateType = isDateTime(form.getColumns(),sList.get(x).getTemplateColumnValue().split("\\|")[2]);
				if (!dateType.equals("false")) {
					initPositions+= "if(arr[i]."+content[y].toUpperCase()+"!=null){\n"+
							"	 "+content[y]+" = new Date("+content[y]+").Format(\""+dateType+"\");\n"+
							"}\n";
				}else {
					initPositions+="if(arr[i]."+content[y].toUpperCase()+"==null){\n"+
							"	 "+content[y]+" = '';\n"+
							"}\n";
				}
				paramStrings += "&"+sList.get(x).getTemplateColumnValue().split("\\|")[0]+"='+arr[i]."+sList.get(x).getTemplateColumnValue().split("\\|")[0].toUpperCase()+"+'";
			}
			
			
			
			String jspBuildString = "function showDate_"+formId+"(arr) {\n"+
									"if('${param.flowAction}'=='start'){\n"+
							        "    var cellObj = $(\n"+
							        "        '<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
							        "        '    <div class=\"contact-box\">'+\n";
			if (sList.get(0).getTemplateColumnLinkMark()!=null&&!sList.get(0).getTemplateColumnLinkMark().equals("")) {
				if(isNumeric(sList.get(0).getTemplateColumnLinkMark())){
					//纯数字是动态表单
					jspBuildString +="        '        <a href=\"/myehr/form/toForm.action?formId="+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}else{
					jspBuildString +="        '        <a href=\""+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}
			}		        
			String[] photos = photo.split("_");
									if(photo==null||photo.equals("")){
										
									}else if (photos[photos.length-1].toUpperCase().equals("EMPID")) {
										jspBuildString +=  	"        '            <div class=\"col-sm-4\">'+\n"+
													        "        '                <div class=\"text-center\">'+\n"+
													        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" id=\"Employee_Photo_WT\" class=\"img-circle m-t-xs img-responsive\" >'+\n"+
													        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
													        "        '                </div>'+\n"+
													        "        '            </div>'+\n";	
									}else {
										jspBuildString +=  			"        '            <div class=\"col-sm-4\">'+\n"+
															        "        '                <div class=\"text-center\">'+\n"+
															        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\"  class=\"img-circle m-t-xs img-responsive\" src=\"http://116.62.243.28:9875/'+arr[i]."+photo.toUpperCase()+"+'\">'+\n"+
															        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
															        "        '                </div>'+\n"+
															        "        '            </div>'+\n";							
									}
							       
							        
			jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
							        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" ></h3>'+\n";
			if(position[0]==null||position[0].equals("")){
												
											}else {
												if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
													jspBuildString += "'<div>";
													for (int i = 0; i < position.length; i++) {
														int num=i%5+1;
														jspBuildString += "  			  <p style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;\"  class=\"tag"+num+" left\" ></p>";
													}
													jspBuildString += "</div>'+\n'<br>'+\n";
												} else {
													jspBuildString +=  		"        '                <p style=\"overflow:hidden;height:20px;font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;color:"+sList.get(2).getTemplateColumnFontColor()+"\" ><i class=\"fa fa-map-marker\"></i></p>'+\n";							
												}
											}				       
							        
							        
			jspBuildString +=				        "        '                <div class=\"row\" id=\"row\" style=\"width:500px\">'+\n";
			for(int i = 0 ;i<contentNum;i++){
				  jspBuildString += //"        '            <p style=\"color:"+sList.get(i+5).getTemplateColumnFontColor()+";font-size:"+sList.get(i+5).getTemplateColumnFontSize()+"px\">"+contentLabel[i]+":'+ "+content[i]+" +'</p>'+\n";
					        		"		 '				<div class=\"col-lg-"+smallNum+" col-md-"+smallNum+" col-sm-"+smallNum+" col-xs-"+smallNum+"\">'+\n"+
									"		 '					<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;width:200px\">'+\n"+
									"		 '						<label style=\"float:left;font-size:12px\">"+contentLabel[i]+": </label>'+\n"+
									"		 '					</div>'+\n"+
									"		 '				</div>'+\n";
									}
			      jspBuildString += "        '                </div>'+\n"+
							        "        '            </div>'+\n"+
							        "        '            <div class=\"clearfix\"></div>'+\n"+
							        "        '        </a>'+\n"+
							        "        '    </div>'+\n"+
							        "        '</div>');\n"+
							       // "    animationHover(cellObj, 'pulse');\n"+
							        "    $('#"+formCode+"').append(cellObj);\n"+
									"}else{\n"+
							        "for (var i = arr.length - 1; i >= 0; i--) {\n"+
									initTitles+
									initPositions+
							        "    var cellObj = $(\n"+
							        "        '<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
							        "        '    <div class=\"contact-box\">'+\n";
			if (sList.get(0).getTemplateColumnLinkMark()!=null&&!sList.get(0).getTemplateColumnLinkMark().equals("")) {
				if(isNumeric(sList.get(0).getTemplateColumnLinkMark())){
					//纯数字是动态表单
					jspBuildString +="        '        <a href=\"/myehr/form/toForm.action?formId="+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}else{
					jspBuildString +="        '        <a href=\""+sList.get(0).getTemplateColumnLinkMark()+paramStrings+"\">'+\n";
				}
			}		        
									if(photo==null||photo.equals("")){
										
									}else if (photos[photos.length-1].toUpperCase().equals("EMPID")) {
													jspBuildString +=  	"        '            <div class=\"col-sm-4\">'+\n"+
													        "        '                <div class=\"text-center\">'+\n"+
													        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" id=\"Employee_Photo_WT\" class=\"img-circle m-t-xs img-responsive\" >'+\n"+
													        "        '                    <div class=\"m-t-xs font-bold\" ></div>'+\n"+
													        "        '                </div>'+\n"+
													        "        '            </div>'+\n";	
									}else {
										jspBuildString +=  			"        '            <div class=\"col-sm-4\">'+\n"+
															        "        '                <div class=\"text-center\">'+\n"+
															        "        '                    <img width=\"100\" style=\"margin:0 auto\" alt=\"image\" title=\"'+"+titles+"+'\" class=\"img-circle m-t-xs img-responsive\" src=\"http://116.62.243.28:9875/'+arr[i]."+photo.toUpperCase()+"+'\">'+\n"+
															        "        '                    <div class=\"m-t-xs font-bold\" title=\"'+"+titles+"+'\">'+arr[i]."+name.toUpperCase()+"+'</div>'+\n"+
															        "        '                </div>'+\n"+
															        "        '            </div>'+\n";							
									}
							       
							        
			jspBuildString +=       "        '            <div class=\"col-sm-8\">'+\n"+
							        "        '                <h3 style=\"margin-top:0;overflow:hidden;height:35px;color:"+sList.get(1).getTemplateColumnFontColor()+";\" title=\"'+"+titles+"+'\"><strong>'+"+titles+"+'</strong></h3>'+\n";
			if(position[0]==null||position[0].equals("")){
												
											}else {
												if (sList.get(2).getTemplateIsTag().toUpperCase().equals("Y")) {
													jspBuildString += "'<div>";
													for (int i = 0; i < position.length; i++) {
														int num=i%5+1;
														jspBuildString += "  			  <p style=\"font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;\" title=\"'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'\" class=\"tag"+num+" left\" >'+arr[i]."+position[i].split("\\|")[0].toUpperCase()+"+'</p>";
													}
													jspBuildString += "</div>'+\n'<br>'+\n";
												} else {
													jspBuildString +=  		"        '                <p style=\"overflow:hidden;height:20px;font-size:"+sList.get(2).getTemplateColumnFontSize()+"px;color:"+sList.get(2).getTemplateColumnFontColor()+"\" title=\"'+"+positions+"+'\"><i class=\"fa fa-map-marker\"></i> '+"+positions+"+'</p>'+\n";							
												}
											}				       
							        
							        
			jspBuildString +=				        "        '                <div class=\"row\" id=\"row\" style=\"width:500px\">'+\n";
			for(int i = 0 ;i<=contentNum;i++){
				  jspBuildString += //"        '            <p style=\"color:"+sList.get(i+5).getTemplateColumnFontColor()+";font-size:"+sList.get(i+5).getTemplateColumnFontSize()+"px\">"+contentLabel[i]+":'+ "+content[i]+" +'</p>'+\n";
					        		"		 '				<div class=\"col-lg-"+smallNum+" col-md-"+smallNum+" col-sm-"+smallNum+" col-xs-"+smallNum+"\">'+\n"+
									"		 '					<div class=\"ui search selection dropdown entitybox field-control\" style=\"display: inline-block;width:200px\">'+\n"+
									"		 '						<label style=\"float:left;font-size:12px\">"+contentLabel[i]+":'+ "+content[i]+" +' </label>'+\n"+
									"		 '					</div>'+\n"+
									"		 '				</div>'+\n";
									}
			      jspBuildString += "        '                </div>'+\n"+
							        "        '            </div>'+\n"+
							        "        '            <div class=\"clearfix\"></div>'+\n"+
							        "        '        </a>'+\n"+
							        "        '    </div>'+\n"+
							        "        '</div>');\n"+
							        //"    animationHover(cellObj, 'pulse');\n"+
							        "    $('#"+formCode+"').append(cellObj);\n"+
							        "}\n"+
							        "}\n";
			return jspBuildString;
		}else if(sList.get(0).getTemplateColumnTemplateId()==3){
			String right1 = "";
			String[] right2 = new String[1];
			String right1s = "";
			String right2s = "";
			String right3s = "";
			if(sList.get(1).getTemplateColumnValue()!=null){
				right1 = sList.get(1).getTemplateColumnValue();
				right1s = "arr[i]."+right1.split("\\|")[0].toUpperCase();
				right3s = right1.split("\\|")[0];
				right2s = right1.split("\\|")[1];
			}
			String[] contentLabel = new String[10];
			String[] content = new String[10];
			int largeNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[0]));
			int middleNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[1]));
			int smallNum = 12/(Integer.valueOf(sList.get(0).getTemplateColumnValue().split(",")[2]));
			String jspBuildString = "function showDate_"+formId+"(arr) {\n";
				   jspBuildString += "for (var i = arr.length - 1; i >= 0; i--) {\n"+
							        "    var cellObj = $(\n"+
							        "			'<div class=\"col-md-"+middleNum+" col-sm-12 col-lg-"+largeNum+"\">'+\n"+
									"		'    <div class=\"widget style1 navy-bg\" style=\"background-color:"+sList.get(0).getTemplateColumnFontColor()+"\">'+\n"+
									"		'        <div class=\"row\" id=\"row\" >'+\n"+
									"		'            <div class=\"col-xs-12 text-right\">'+\n";
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><a href=\"/myehr/form/toForm.action?formId="+sList.get(1).getTemplateColumnLinkMark()+"&"+right3s+"='+"+right1s+"+'\"><span style=\"height:10%;text-align:center;display:block;font-size:"+sList.get(1).getTemplateColumnFontSize()+";color:"+sList.get(1).getTemplateColumnFontColor()+";font-weight:"+sList.get(1).getTemplateColumnFontWeight()+";\">'+"+right1s+"+'</span></a></div>'+\n";
				   jspBuildString += "		'				<div style=\"margin-top:10px\"><span style=\"text-align:center;display:block;font-size:"+sList.get(2).getTemplateColumnFontSize()+";color:"+sList.get(2).getTemplateColumnFontColor()+";font-weight:"+sList.get(2).getTemplateColumnFontWeight()+";\">"+right2s+"</span></div>'+\n";
					
				   jspBuildString += "		'            </div>'+\n"+
									"		'        </div>'+\n"+
									"		'    </div>'+\n"+
									"		'</div>');\n" +
									//"animationHover(cellObj, 'pulse');\n"+
									"$('#"+formCode+"').append(cellObj);\n" +
									"}\n";				
			
			return jspBuildString;
		}else {
			return null;
		}
	}
	
	public static String formGridChartsFunction4(SysFormconfigCache form,SysFormconfigCache selectform,SysFormCharts charts,List<FormChartsBarColumn> barColumns,List<FormChartsBarColumn> barColumns1,List<FormChartsBarColumn> barColumns2,Map columnDatasmap){
		String jspBuildString="";
		String chartsType = charts.getFormChartsType();
		String isHideTable = charts.getFormChartsHideTable();
		String selectFormCode = selectform.getFormDefCode();
		String hideTableString = "";
		if (isHideTable.equals("true")) {
			//hideTableString = "document.getElementById(\""+selectFormCode+"_List\").parentNode.parentNode.parentNode.style.display=\"none\";\n";
		}
		
		String tooltip="";
		
		String legendColumn = "";
		String colorColumn = "";
		String seriesColumn = "";
		
		if (chartsType.equals("bar") || chartsType.equals("line")) {
			String xcolumn = barColumns1.get(0).getSelectFormColumnTable().replace("_", "")+"_"+barColumns1.get(0).getSelectFormColumnName();
			if (barColumns.size()>0) {
				for (int i = 0; i < barColumns.size(); i++) {
					String columnName = barColumns.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns.get(i).getSelectFormColumnName();
					List<Map> columnData = (List<Map>)columnDatasmap.get("columnDatas"+i);
					List<Map> groupColumn = (List<Map>)columnDatasmap.get("groupColumn"+i);
					List<Map> xColumn = (List<Map>)columnDatasmap.get("xColumn"+i);
					for (int j = 0; j < groupColumn.size(); j++) {
						String n="";
						for (int j2 = 0; j2 < xColumn.size(); j2++) {
							int flag = 0;
							String sum = "";
							for (int k = 0; k < columnData.size(); k++) {
								String x=columnData.get(k).get(xcolumn)+"";
								String y=columnData.get(k).get(columnName)+"";
								String x1=xColumn.get(j2).get(xcolumn)+"";
								String y1=groupColumn.get(j).get(columnName)+"";
								if (x.equals(x1) && y.equals(y1)) {
									flag=1;
									sum=columnData.get(k).get("sum")+"";
								}
							}
							if (flag==1) {
								n+=sum+",";
							}else {
								n+=0+",";
							}
						}
						n=n.substring(0, n.length()-1);
						seriesColumn+="{\n"
								+"    name: '"+barColumns.get(i).getSelectFormColumnLable()+"',\n"
								+"    type: 'bar',\n"
								+"    data:["+n+"]\n"
								+"},";
					}
					legendColumn+="'"+barColumns.get(i).getSelectFormColumnLable()+"',";
					
					colorColumn+="'"+barColumns.get(i).getChartsBarColumnColor()+"',";
					
				}
				legendColumn = legendColumn.substring(0, legendColumn.length()-1);
				colorColumn = colorColumn.substring(0, colorColumn.length()-1);
				seriesColumn = seriesColumn.substring(0, seriesColumn.length()-1);
			}else if (barColumns2.size()>0) {
				for (int i = 0; i < barColumns2.size(); i++) {
					String columnName = barColumns2.get(i).getSelectFormColumnTable().replace("_", "")+"_"+barColumns2.get(i).getSelectFormColumnName();
					List<Map> columnData = (List<Map>)columnDatasmap.get("columnDatas"+i);
					List<Map> groupColumn = (List<Map>)columnDatasmap.get("groupColumn"+i);
					List<Map> xColumn = (List<Map>)columnDatasmap.get("xColumn"+i);
					String n="";
					for (int j = 0; j < columnData.size(); j++) {
						n+=columnData.get(j).get(columnName)+",";
					}
					n=n.substring(0, n.length()-1);
					seriesColumn+="{\n"
							+"    name: '"+barColumns2.get(i).getSelectFormColumnLable()+"',\n"
							+"    type: 'bar',\n"
							+"	  barMaxWidth:50,"
							+"    data:["+n+"]\n"
							+"},";
					legendColumn+="'"+barColumns2.get(i).getSelectFormColumnLable()+"',";
					
					colorColumn+="'"+barColumns2.get(i).getChartsBarColumnColor()+"',";
					
				}
				legendColumn = legendColumn.substring(0, legendColumn.length()-1);
				colorColumn = colorColumn.substring(0, colorColumn.length()-1);
				seriesColumn = seriesColumn.substring(0, seriesColumn.length()-1);
			}else{
				legendColumn+="'"+barColumns1.get(0).getSelectFormColumnLable()+"',";
				
				colorColumn+="'"+barColumns1.get(0).getChartsBarColumnColor()+"',";
				seriesColumn+="{\n"
						+"    name: '"+barColumns1.get(0).getSelectFormColumnLable()+"',\n"
						+"    type: 'bar',\n"
						+"    data: seriesData\n"
						+"},";
			}
		}else if (chartsType.equals("pie")) {
			seriesColumn+="{\n"+
		            	  "    name:'访问来源',\n"+
		            	  "    type:'pie',\n"+
		            	  "    radius: ['50%', '70%'],\n"+
		            	  "    avoidLabelOverlap: false,\n"+
		            	  "    label: {\n"+
		            	  "        normal: {\n"+
		            	  "            show: false,\n"+
		            	  "            position: 'center'\n"+
		            	  "        },\n"+
		            	  "        emphasis: {\n"+
		            	  "            show: true,\n"+
		            	  "            textStyle: {\n"+
		            	  "                fontSize: '30',\n"+
		            	  "                fontWeight: 'bold'\n"+
		            	  "            }\n"+
		            	  "        }\n"+
		            	  "    },\n"+
		            	  "    labelLine: {\n"+
		            	  "        normal: {\n"+
		            	  "            show: false\n"+
		            	  "        }\n"+
		            	  "    },\n"+
		            	  "    data:[ {value:335, name:'男女'},{value:310, name:'男女'},]\n"+
		            	  "}\n";
		}
		
		
		if (chartsType.equals("bar") || chartsType.equals("line")) {
			if (charts.getFormChartsTooltip()!=null && charts.getFormChartsTooltip().equals("true")) {
				tooltip="		tooltip : {\n"+
						"			trigger: 'axis',\n"+
						"			axisPointer : {            // 坐标轴指示器，坐标轴触发有效\n"+
						"				type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'\n"+
						"			}\n"+
						"		},\n";
			}
		}else if (chartsType.equals("pie")) {
			if (charts.getFormChartsTooltip()!=null && charts.getFormChartsTooltip().equals("true")) {
				tooltip="		tooltip : {\n"+
						"			trigger: 'item',\n"+
						"			formatter : \"{a} <br/>{b}: {c} ({d}%)\"\n"+
						"		},\n";
			}
		}
		
		if (chartsType.equals("bar") || chartsType.equals("line")) {
			jspBuildString="function initCharts_"+form.getFormDefId()+"(xAxisData,seriesData){\n"+
			"   document.getElementById('"+form.getFormDefCode()+"_chart').style.width='"+charts.getFormChartsXAxis()+"px';\n"+
			"   document.getElementById('"+form.getFormDefCode()+"_chart').style.height='"+charts.getFormChartsYAxis()+"px';\n"+
			"	var myChart = echarts.init(document.getElementById('"+form.getFormDefCode()+"_chart'));\n"+
			"	var options={\n"+
			"		//定义柱状图颜色\n"+
			"		color: ["+colorColumn+"],\n"+
			tooltip+
			      
			"		toolbox: {\n"+
			"			show: true,\n"+
			"			feature: {\n"+
			"				dataView: {readOnly: false},\n"+
			"				magicType: {type: ['line', 'bar']},\n"+
			"				restore: {},\n"+
			"				saveAsImage: {}\n"+
			"			}\n"+
			"		},\n"+
					
			"		//定义一个标题\n"+
			"       title:{\n"+
			"           text:'"+charts.getFormChartsTitle()+"'\n"+
			"       },\n"+
			"        legend:{\n"+
			"            data:["+legendColumn+"]\n"+
			"        },\n"+
					
			"        //X轴设置\n"+
			"        xAxis:{\n"+
			"            data:xAxisData\n"+
			"        },\n"+
			"        yAxis: [\n"+
			"			{\n"+
			"				type : 'value'\n"+
			"			}\n"+
			"		],\n"+
			"        series:["+seriesColumn+"]\n"+
	
			"    };\n"+
			"    myChart.setOption(options);\n"+
			"}\n";
		}else if (chartsType.equals("pie")) {
			String radiusString="";
			String PieOuterRingDiameter = barColumns1.get(0).getPieOuterRingDiameter();
			String PieInnerRingDiameter = barColumns1.get(0).getPieInnerRingDiameter();
			if (barColumns1.get(0).getChartsPieType().equals("1")) {
				if (PieOuterRingDiameter!=null && PieOuterRingDiameter!="") {
					radiusString = "['0%', '"+PieOuterRingDiameter+"%']";
				}else {
					radiusString = "['0%', '50%']";
				}
			}else {
				radiusString = "['"+PieInnerRingDiameter+"%', '"+PieOuterRingDiameter+"%']";
			}
			
			jspBuildString="function initCharts_"+form.getFormDefId()+"(xAxisData,seriesData){\n"+
					"	var x = '"+charts.getFormChartsPositionX()+"';//页面左右\n"+
					"	var y = '"+charts.getFormChartsPositionY()+"';//页面上下\n"+
					"	var size = '"+charts.getFormChartsSize()+"';//图形大小\n"+
					"	var titleType = '"+charts.getFormChartsTitleOrient()+"';\n"+
					"	var titlePosition = '"+charts.getFormChartsTitlePosition()+"';\n"+
					
					"	var myChart = echarts.init(document.getElementById('"+form.getFormDefCode()+"_chart'));\n"+
					"	var options={\n"+
					tooltip+
					      
					"		toolbox: {\n"+
					"			show: true,\n"+
					"			feature: {\n"+
					"				dataView: {readOnly: false},\n"+
					"				restore: {},\n"+
					"				saveAsImage: {}\n"+
					"			}\n"+
					"		},\n"+
							
					"		//定义一个标题\n"+
					"       title:{\n"+
					"           text:'"+charts.getFormChartsTitle()+"',\n"+
					"			x: 'center'\n"+
					"       },\n"+
					"       legend:{\n"+
					"           orient: titleType,\n"+
					"           left: titlePosition,\n"+
					"           data:xAxisData\n"+
					"       },\n"+
					"       series:[\n"+
					"           {\n"+
	            	"               name:'"+barColumns1.get(0).getSelectFormColumnLable()+"',\n"+
	            	"               type:'pie',\n"+
	            	"               radius : size,\n"+
	            	"               center: [x,y],\n"+
	            	"               avoidLabelOverlap: false,\n"+
	            	"               label: {\n"+
	            	"                   normal: {\n"+
	            	"                       show: false,\n"+
	            	"                       position: 'center'\n"+
	            	"                   },\n"+
	            	"                   emphasis: {\n"+
	            	"                       show: true,\n"+
	            	"                       textStyle: {\n"+
	            	"                           fontSize: '30',\n"+
	            	"                           fontWeight: 'bold'\n"+
	            	"                       }\n"+
	                "                   }\n"+
	            	"               },\n"+
	            	"               labelLine: {\n"+
	            	"                   normal: {\n"+
	            	"                       show: false\n"+
	            	"                   }\n"+
	            	"               },\n"+
	            	"               data:seriesData\n"+
	            	"           }\n"+
	            	"       ]\n"+
					"    };\n"+
					"    myChart.setOption(options);\n"+
					"}\n";
		}
		
		
		
		jspBuildString+="function queryAllData_"+form.getFormDefId()+"(){\n"+
		hideTableString+
		"	var url='/myehr/form/cardformInitData.action?queryType=all';\n"+
		"	var pageReqeust = {};\n"+
		"	var paramData = grid_"+selectform.getFormDefCode()+"_fun(pageReqeust);\n"+
		"	$.ajax({\n"+
		"		url:url,\n"+
		"		type:'post',\n"+
		"		data: paramData,\n"+
		"		cache: false,\n"+
		"		contentType: 'application/json;charset=utf-8',\n"+
		"		success: function (text) {\n"+
		"			initArrData_"+form.getFormDefId()+"(text.rows);\n"+
		"		},\n"+
		"	});\n"+
		"}\n";
		
		String xColumn = (barColumns1.get(0).getSelectFormColumnTable().replaceAll("_", "")).toUpperCase()+"_"+barColumns1.get(0).getSelectFormColumnName();
		String xColumnId = barColumns1.get(0).getSelectFormColumnId()+"";
		
		if (chartsType.equals("bar") || chartsType.equals("line")) {
			jspBuildString+="function initArrData_"+form.getFormDefId()+"(datas){\n"+
			"	var xAxisData=[];\n"+
			"	var seriesData=[];\n"+
			"	var xAxisColumn = '"+xColumn+"';\n"+
			"	var formDefSql = '"+selectform.getFormDefSql()+"';\n"+
				
			"	var url='/myehr/charts/searchChartsType.action?xColumnId="+xColumnId+"&xAxisColumn='+xAxisColumn+'&formDefSql='+formDefSql;\n"+
			"	$.ajax({\n"+
			"		url:url,\n"+
			"		type:'post',\n"+
			"		cache: false,\n"+
			"		contentType: 'application/json;charset=utf-8',\n"+
			"		success: function (text) {\n"+
			"			for(var i=0;i<text.length;i++){\n"+
			"				xAxisData.push(text[i].dictName);\n"+
			"				var flag=0;\n"+
			"				for(var j=0;j<datas.length;j++){\n"+
			"					if(text[i]."+xColumn+"==datas[j]."+xColumn+"){\n"+
			"						flag++;\n"+
			"					}\n"+
			"				}\n"+
			"				seriesData.push(flag);\n"+
			"			}\n"+
			"			initCharts_"+form.getFormDefId()+"(xAxisData,seriesData)\n"+
			"		},\n"+
			"	});\n"+
			"}\n";
		}else if (chartsType.equals("pie")) {
			jspBuildString+="function initArrData_"+form.getFormDefId()+"(datas){\n"+
					"	var xAxisData=[];\n"+
					"	var seriesData=[];\n"+
					"	var xAxisColumn = '"+xColumn+"';\n"+
					"	var formDefSql = '"+selectform.getFormDefSql()+"';\n"+
						
					"	var url='/myehr/charts/searchChartsType.action?xColumnId="+xColumnId+"&xAxisColumn='+xAxisColumn+'&formDefSql='+formDefSql;\n"+
					"	$.ajax({\n"+
					"		url:url,\n"+
					"		type:'post',\n"+
					"		cache: false,\n"+
					"		contentType: 'application/json;charset=utf-8',\n"+
					"		success: function (text) {\n"+
					"			for(var i=0;i<text.length;i++){\n"+
					"				xAxisData.push(text[i].dictName);\n"+
					"				var flag=0;\n"+
					"				for(var j=0;j<datas.length;j++){\n"+
					"					if(text[i]."+xColumn+"==datas[j]."+xColumn+"){\n"+
					"						flag++;\n"+
					"					}\n"+
					"				}\n"+
					"				var obj={};\n"+
					"				obj.value = flag ;\n"+
					"				obj.name=text[i].dictName;\n"+
					"				seriesData.push(obj);\n"+
					"			}\n"+
					"			initCharts_"+form.getFormDefId()+"(xAxisData,seriesData)\n"+
					"		},\n"+
					"	});\n"+
					"}\n";
		}
		return jspBuildString;
	}
	
	
	public static String formTimeaxisHtml1(SysFormconfigCache form,List<SysTimeaxisTemplateColumn> templateColumns){
		String jspBuildString="";
		
		jspBuildString+="<div class=\"container-fluid\">\n"+
						"    <div style=\"margin-top:20px;\">\n"+
						"        <div class=\"wrapper wrapper-content animated fadeInRight ibox-content2\">\n"+
						"	         <div class=\"row dark-timeline\" id=\""+form.getFormDefCode()+"_timeaxis\">\n"+
						"	         </div>\n"+
						"        </div>\n"+
						"    </div>\n"+
						"</div>\n";
		
		return jspBuildString;
	}

	public static String formTimeaxisFunction1(SysFormconfigCache form,List<SysTimeaxisTemplateColumn> templateColumns){
		String jspBuildString="";
		
		String hideTableString = "";
		
		String iconColumn = "";
		String column1="";
		String column2="";
		String column2Style="";
		String column3="";
		String column3Style="";
		String column4="";
		String column4Style="";
		String bgColor = "";
		for (int i = 0; i < templateColumns.size(); i++) {
			if (templateColumns.get(i).getTemplateColumnCode().equals("right1")) {
				iconColumn=templateColumns.get(i).getTemplateColumnLinkMark();
				String a = templateColumns.get(i).getTemplateColumnValue();
				String[] aa = a.split("\\|");
				column1=aa[0];
			}else if (templateColumns.get(i).getTemplateColumnCode().equals("right3")) {
				String a = templateColumns.get(i).getTemplateColumnValue();
				String[] aa = a.split("\\|");
				column2=aa[0];
				column2Style = "font-size:"+templateColumns.get(i).getTemplateColumnFontSize()+";"
						+"color:"+templateColumns.get(i).getTemplateColumnFontColor()+";";
			}else if (templateColumns.get(i).getTemplateColumnCode().equals("right4")) {
				String a = templateColumns.get(i).getTemplateColumnValue();
				String[] aa = a.split("\\|");
				column3=aa[0];
				column3Style = "font-size:"+templateColumns.get(i).getTemplateColumnFontSize()+";"
						+"color:"+templateColumns.get(i).getTemplateColumnFontColor()+";";
			}else if (templateColumns.get(i).getTemplateColumnCode().equals("left")) {
				String a = templateColumns.get(i).getTemplateColumnValue();
				String[] aa = a.split("\\|");
				column4=aa[0];
				bgColor = templateColumns.get(i).getTemplateColumnBgColor();
				bgColor = "background-color:"+bgColor+";";
				column3Style = "font-size:"+templateColumns.get(i).getTemplateColumnFontSize()+";"
						+"color:"+templateColumns.get(i).getTemplateColumnFontColor()+";";
			}
		}
		
		//hideTableString = "document.getElementById(\""+form.getFormDefCode()+"_List\").parentNode.parentNode.parentNode.style.display=\"none\";\n";
		
		jspBuildString+="function queryAllData_"+form.getFormDefId()+"(){\n"+
				hideTableString+
				"	var url='/myehr/form/cardformInitData.action?queryType=all';\n"+
				"	$.ajax({\n"+
				"		url:url,\n"+
				"		type:'post',\n"+
				"		data: queryParams,\n"+
				"		cache: false,\n"+
				"		contentType: 'application/json;charset=utf-8',\n"+
				"		success: function (text) {\n"+
				"			showDate(text.rows);\n"+
				"		},\n"+
				"	});\n"+
				"}\n";
		
		jspBuildString+="function showDate(arr) {\n"+
		"    var colorArr = new Array();\n"+
		"    colorArr[0] = '#E35B5A';\n"+
		"    colorArr[1] = '#578EBE';\n"+
		"    colorArr[2] = '#8775A7';\n"+
		"    colorArr[3] = '#44B6AE';\n"+
		"    for (var i = 0; i < arr.length; i++) {\n"+
		"        var flag = i%4;\n"+
		"	     var message = '"+iconColumn+"';\n"+
		"	     var colorStyle = 'background-color:'+colorArr[flag]+';';\n"+
		"	     var messages = new Array();\n"+
		"	     messages = message.split(\":\");\n"+
		"	     var iconClass;\n"+
		"	     var timeaxis = arr[i]."+column4.toUpperCase()+";\n"+
		"	     if(timeaxis!=null){\n"+
		"	     	 time = new Date(timeaxis).Format(\"yyyy-MM-dd hh:mm:ss\");\n"+
		"	     }else{\n"+
		"	     	 time='';\n"+
		"	     }\n"+
		"	     if(arr[i]."+column1.toUpperCase()+"==messages[0]){\n"+
		"	         iconClass=messages[1];\n"+
		"	     }else{\n"+
		"	         iconClass='vertical-timeline-icon1';\n"+
		"	     }\n"+
		"		 var cellObj = $('<div class=\"vertical-timeline-block\">'+\n"+
		"						'    <div class=\"'+iconClass+'\"></div>'+\n"+
		"						'    <div class=\"vertical-timeline-content clearfix\" style=\"'+colorStyle+'\">'+\n"+
		"						'        <div class=\"pic fl\" style=\"float:left\">'+\n"+
		"						'            <img src=\"http://p.3761.com/pic/43701399945993.png\" alt=\"\">'+\n"+
		"						'            <div id=\"shenpiren\" class=\"shenpirens\" style=\"text-align:center;"+column2Style+"\">'+arr[i]."+column2.toUpperCase()+"+'</div>'+\n"+
		"						'        </div>'+\n"+
		"						'        <div class=\"content fl\">'+\n"+
		"						'            <div class=\"clearfix\"></div>'+\n"+
		"						'		     <div class=\"shenpizhuangtai\" style=\"text-align:center;"+column3Style+"\">'+arr[i]."+column3.toUpperCase()+"+'</div>'+\n"+
		"						'	     </div>'+\n"+
		"						'	     <div class=\"clearfix\"></div>'+\n"+
		"						'    </div>'+\n"+
		"						'    <div id=\"shenpijieguo\" class=\"shenpijieguo\" style=\""+bgColor+column4Style+"\">'+\n"+
		"						'        <div id=\"shenpishijian\" style=\"font-weight:bold;float:left\" class=\"shenpishijian\">'+time+'</div>'+\n"+
		"						'    </div>'+\n"+
		"						'</div>'\n"+
		"		);\n"+
		"		$(\"#"+form.getFormDefCode()+"_timeaxis\").append(cellObj);\n"+
		"	}\n"+
		"}\n";
		
		return jspBuildString;
	}
	
	
	public static String getEditorModelOpenJs(List<SysFormColumnCache> columns,SysFormButtonEditorCache editorModel, SysRequestParam request,String buttonType,String btnCode,String formCode,String FormId){
		String buttonId = editorModel.getButtonEditorModelButtonid()+"";
		String jsString="";
		/*
		String editorModelValue = editorModel.getButtonEditorModelValue();
		String rgex = "\\[(.*?)\\]";  
		List<String> aa = DealStrSub.getSubUtil(editorModelHtml, rgex);
		String editorModelHtmlJs = "";
		for (int i = 0; i < aa.size(); i++) {
			String a = aa.get(i);
			String[] bb = a.split(":");
			if (bb[0].equals("s")) {
				String oldString = "["+a+"]";
				editorModelHtmlJs += "            var "+bb[1]+" = '${sessionScope."+bb[1]+"}';\n";
				editorModelHtmlJs+="            modelHtml = modelHtml.replace('"+oldString+"',"+bb[1]+");\n";
				editorModelHtmlJs+="            modelValue = modelValue.replace('"+oldString+"',"+bb[1]+");\n";
			}else if (bb[0].equals("c")) {
				String c = bb[1];
				String[] cc = c.split("\\|");
				String oldString = "["+a+"]";
				String selectColumn = "";
				for (int j = 0; j < columns.size(); j++) {
					if ((columns.get(j).getFormColumnId()+"").equals(cc[cc.length-1])) {
						String tableName = columns.get(j).getFormEntityTablename();
						tableName = tableName.replaceAll("_", "");
						tableName = tableName.toUpperCase();
						selectColumn = tableName+"_"+cc[1].toUpperCase();
					}
				}
				editorModelHtmlJs += "            var "+cc[1]+" = select."+selectColumn+";\n";
				editorModelHtmlJs+="            modelHtml = modelHtml.replace('"+oldString+"',"+cc[1]+");\n";
				editorModelHtmlJs+="            modelValue = modelValue.replace('"+oldString+"',"+cc[1]+");\n";
			}
		}*/
		
		String modelJs = "";
		if (buttonType.equals("message")) {
			modelJs+="    var url='/myehr/jsp/form/formcreate/button/showEditorModel.jsp?formButtonId='+"+buttonId+"+'&formId='+"+FormId+"+'&btnCode='+'"+btnCode+"';\n"
					+"    var _param = {};\n"
					+"    _param.formId="+FormId+";\n"
					+"    _param.buttonId="+buttonId+";\n"
					+"    _param.messageType="+editorModel.getEditorMessageType()+";\n"
					+"    _param.messages = modelHtmls;\n"
					+"    $.ajax({\n"
					+"	      url:'/myehr/button/saveMessages.action',\n"
					+"	      type:'post',\n"
					+"	      data: JSON.stringify(_param),\n"
					+"	      cache: false,\n"
					+"	      contentType: 'application/json;charset=utf-8',\n"
					+"	      success: function (text) {\n"
					+"	          if(text[0]==0){\n"
					+"	              alert(\"保存失败！\");\n"
					+"	          }else if(text[0]=='error'){\n"
					+"	              alert(\"操作异常\");\n"
					+"	          }else {\n"
					+"	              alert(\"操作成功\");\n"
					+"	          }\n"
					+"        }\n"
					+"});\n";
		}else if (buttonType.equals("email")) {
			modelJs+="    var url='/myehr/jsp/form/formcreate/button/showEditorModel.jsp?formButtonId='+"+buttonId+"+'&formId='+"+FormId+"+'&btnCode='+'"+btnCode+"';\n"
					+"    var _param = {};\n"
					+"    _param.formId="+FormId+";\n"
					+"    _param.buttonId="+buttonId+";\n"
					+"    _param.messageType="+editorModel.getEditorMessageType()+";\n"
					+"    _param.messages = modelHtmls;\n"
					+"    $.ajax({\n"
					+"	      url:'/myehr/button/saveEmails.action?email='+email,\n"
					+"	      type:'post',\n"
					+"	      data: JSON.stringify(_param),\n"
					+"	      cache: false,\n"
					+"	      contentType: 'application/json;charset=utf-8',\n"
					+"	      success: function (text) {\n"
					+"	          if(text[0]==0){\n"
					+"	              alert(\"保存失败！\");\n"
					+"	          }else if(text[0]=='error'){\n"
					+"	              alert(\"操作异常\");\n"
					+"	          }else {\n"
					+"	              alert(\"操作成功\");\n"
					+"	          }\n"
					+"        }\n"
					+"});\n";
		}else if (buttonType.equals("editorModel")) {
			modelJs+="    document.getElementById(\"editorModel"+btnCode+"\").value= modelHtml;\n"
					+"    var url='/myehr/jsp/form/formcreate/button/showEditorModel.jsp?formButtonId='+"+buttonId+"+'&formId='+"+FormId+"+'&btnCode='+'"+btnCode+"';\n"
					+"    var title='富文本模板';\n"
					+"    layer.open({\n"
					+"        type: 2,\n"
					+"        title:title,\n"
					+"        shadeClose: true,\n"
					+"        shade: 0.8,\n"
					+"        maxmin:true,\n"
					+"        zIndex:9999,\n"
					+"        area: ['950px', '500px'],\n"
					+"        content: url,\n"
					//+"        content: modelHtml,\n"
					+"        success:function(layero,index){\n"
					+"        },\n"
					+"        end:function(){\n"
					+"        }\n"
					+"    });\n";		
		}
		
		jsString+="var modelHtmls=[];\n"
		+"var modelValues=[];\n"
		+"function "+btnCode+formCode+"_click_"+FormId+"(){\n"
		+"    var title='富文本模板';\n"
		+"    modelHtmls=[];\n"
		+"    modelValues=[];\n"
		+" var SYSUSER_USER_EMAIL='';\n"
		+"    var selects = $(\"#"+formCode+"_List\").bootstrapTable('getSelections');\n"
		+"    if(selects!=null && selects.length>0){\n"
		+"        for(var i=0;i<selects.length;i++){\n"
		+"            select = selects[i];\n"
		+"				var email = select.SYSUSER_USER_EMAIL;\n"
		+"            var modelHtmls = getEditorModelHtmlByButtonId('"+buttonId+"','"+FormId+"','"+buttonType+"',selects);\n"
//		+"            var modelHtml = modelHtmls[0];\n"
//		+"            var modelValue = modelHtmls[1];\n"
//		+"            var modelHtml = '"+editorModelHtml+"';\n"
//		+"            var modelValue = '"+editorModelValue+"';\n"
//		+editorModelHtmlJs
//		+"            modelHtmls.push(modelHtml);\n"
//		+"            modelValues.push(modelValue);\n"
		+"        }\n"
		+"    }else {\n"
		+"    	  alert('请选择一行数据');return ;\n"
		+"    }\n"
		
		+modelJs
		
		+"}\n";
		jsString+="function getParam(){\n"
				//+"    param = document.getElementById(\"editorModel"+btnCode+"\").value;\n"
				+"    param = modelHtmls;\n"
				+"    return param;\n"
				+"}\n";
		return jsString;
	}
	public static String getDictDatas(StringBuffer stringBuffer,String formIds) {
		String jspBuildString="";
		
		jspBuildString+="function getDictDatasByFormId(){\n"+
						"$.ajax({\n"+
						"	url:'/myehr/form/getDictDatasByFormId.action?formId=${param.formId}&formIds="+formIds+"',\n"+
						"	type:'post',\n"+
						"	cache: false,\n"+
						"	async:false,\n"+
						"	success: function (obj) {\n"+
						stringBuffer+		
						"	}\n"+
						"});\n"+
						"}\n";
		
		return jspBuildString;
	}
	
	public static String getDictNames(SysFormComboboxCache combobox) {
		ISysformconfigService sysformconfigService = (ISysformconfigService)SpringContextUtils.getBeanById("ISysformconfigService");
		// TODO Auto-generated method stub
		String guiInitType = combobox.getPojo().getComboboxGuiInitType();
		String result = "var "+guiInitType+"_"+combobox.getComboboxGuiInitValue()+" = {";
		String sql = "";
		List<DictData> rs2 = null;
		if (guiInitType.equals("sql")) {
			String guiInitValue = combobox.getPojo().getComboboxGuiInitValue();
			try {
				rs2 = sysformconfigService.getCardDictDataByDictTypeCode(guiInitValue, "sql");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		} else {
			String guiInitValue = combobox.getPojo().getComboboxGuiInitValue();
			try {
				rs2 = sysformconfigService.getCardDictDataByDictTypeCode(guiInitValue, "dict");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		}
		for (int i = 0; i < rs2.size(); i++) {
			//var kvArray = [{key: 1, value: 10},
			//				 {key: 2, value: 20},
			//				 {key: 3, value: 30}];
			if (i==0) {
				result += "'"+rs2.get(i).getName()+"':'"+rs2.get(i).getCode()+"'";
			} else {
				result += ",'"+rs2.get(i).getName()+"':'"+rs2.get(i).getCode()+"'";
			}
		}
		result += "};\n";
		return result;
	}
	public static String getDictValues(SysFormComboboxCache combobox) {
		ISysformconfigService sysformconfigService = (ISysformconfigService)SpringContextUtils.getBeanById("ISysformconfigService");
		// TODO Auto-generated method stub
		String guiInitType = combobox.getPojo().getComboboxGuiInitType();
		String result = "var "+guiInitType+"_"+combobox.getComboboxGuiInitValue()+" = {";
		String sql = "";
		List<DictData> rs2 = null;
		if (guiInitType.equals("sql")) {
			String guiInitValue = combobox.getPojo().getComboboxGuiInitValue();
			try {
				rs2 = sysformconfigService.getCardDictDataByDictTypeCode(guiInitValue, "sql");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		} else {
			String guiInitValue = combobox.getPojo().getComboboxGuiInitValue();
			try {
				rs2 = sysformconfigService.getCardDictDataByDictTypeCode(guiInitValue, "dict");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error(e.getMessage(),e);
			}
		}
		for (int i = 0; i < rs2.size(); i++) {
			if (i==0) {
				result += "'"+rs2.get(i).getCode()+"':'"+rs2.get(i).getName()+"'";
			} else {
				result += ",'"+rs2.get(i).getCode()+"':'"+rs2.get(i).getName()+"'";
			}
		}
		result += "};\n";
		return result;
	}
	
}
