<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.supplier.entity.InspectionGrade"%>
<%@page import="com.ambition.supplier.entity.InspectionGradeType"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<table class="form-table-border-left" id="item-table" style="width:100%;">
<%
	class Generated{
		String getIndexStr(int level,String parentIndex,int index){
			StringBuffer sb = new StringBuffer("");
			for(int i=1;i<level+1;i++){
				sb.append("&nbsp;");
			}
			sb.append(parentIndex + index + ".");
			return sb.toString();
		}
		List<String> createCheckTypeTr(InspectionGradeType inspectionGradeType,String parentIndex,int index,Map<String,Integer> lightMap){
			List<String> strs = new ArrayList<String>();
			String indexStr = getIndexStr(inspectionGradeType.getLevel(),parentIndex,index);
			strs.add("<tr>");
			strs.add("<td colspan=2><b>" + indexStr + "</b>" +inspectionGradeType.getName()+"</td>");
			strs.add("</tr>");
				if(!inspectionGradeType.getChildren().isEmpty()){
					int i=1;
					for(InspectionGradeType child : inspectionGradeType.getChildren()){
						strs.addAll(createCheckTypeTr(child,indexStr,i,lightMap));
						i++;
					}
				}else{
					DecimalFormat df = new DecimalFormat("0.#");
					strs.add("<tr id='"+inspectionGradeType.getId()+"'><td><ul style='list-style:none;margin:0px;'>");
					Double max = null,min=null,fee=null;
					for(InspectionGrade inspectionGrade : inspectionGradeType.getInspectionGrades()){
						if(inspectionGrade.getWeight() != null){
							if(max==null){
								max = inspectionGrade.getWeight();
								min = inspectionGrade.getWeight();
							}else{
								if(inspectionGrade.getWeight()>max){
									max = inspectionGrade.getWeight();
								}
								if(inspectionGrade.getWeight()<min){
									min = inspectionGrade.getWeight();
								}
							}
							if(inspectionGrade.getIsSelect()){
								fee = inspectionGrade.getWeight();
							}
						}
						strs.add("<li style='float:left;width:260px;line-height:24px;'><input type='radio' name='a"+inspectionGradeType.getId()+"' value='"+inspectionGrade.getId()+"' "+(inspectionGrade.getIsSelect()?"checked='checked'":"")+" title='"+inspectionGrade.getWeight()+"'>"+inspectionGrade.getName()+"（"+df.format(inspectionGrade.getWeight())+"）</input></li>");
					}
					if(fee != null){
						if(fee.equals(max)){
							lightMap.put("green",lightMap.get("green")+1);
						}else if(fee.equals(min)){
							lightMap.put("red",lightMap.get("red")+1);
						}else{
							lightMap.put("yellow",lightMap.get("yellow")+1);
						}
					}
					strs.add("</ul></td><td style='width:30%;'><textarea style='width:100%;' rows=2 name='remark'>"+(inspectionGradeType.getRemark()==null?"":inspectionGradeType.getRemark())+"</textarea>");
					strs.add("<input type='hidden' maxWeight='true' value='"+(max==null?"":df.format(max))+"'/>");
					strs.add("<input type='hidden' minWeight='true' value='"+(min==null?"":df.format(min))+"'/>");
					strs.add("</td></tr>");
				}
			return strs;
		}
	}
	Generated generated = new Generated();
	List<InspectionGradeType> inspectionGradeTypes = (List<InspectionGradeType>)request.getAttribute("inspectionGradeTypes");
	if(inspectionGradeTypes==null){
		inspectionGradeTypes = new ArrayList<InspectionGradeType>();
	}
	InspectionReport inspectionReport = (InspectionReport)ActionContext.getContext().getValueStack().findValue("inspectionReport");
	String idTarget = "inspection";
	int rowNumbers = 0;
	Map<String,Integer> lightMap = new HashMap<String,Integer>();
	lightMap.put("red",0);
	lightMap.put("yellow",0);
	lightMap.put("green",0);
	for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
		rowNumbers++;
		List<String> strs = generated.createCheckTypeTr(inspectionGradeType, "",rowNumbers,lightMap);
		for(String str : strs){
			out.println(str);
		}
	}
	int compare = 70;
	double totalFee=0.0;
	if(inspectionReport.getTotalFee()!=null){
		totalFee=inspectionReport.getTotalFee();
	}
	DecimalFormat df = new DecimalFormat("0.#");
	out.println("<tr>");
	out.println("<td colspan=2 style='padding-left:20px;'>");
	out.println("<span style='margin-right:4px;'>绿灯</span><input size='12' name='greenTotal' id='greenTotal' readonly='readonly' style='width:30px;margin-right:2px;background:green;color:white;' value='"+lightMap.get("green")+"'/>项");
	out.println("<span style='margin-right:4px;'>黄灯</span><input size='12' name='yellowTotal' id='yellowTotal' readonly='readonly' style='width:30px;margin-right:2px;background:yellow;' value='"+lightMap.get("yellow")+"'/>项");
	out.println("<span style='margin-right:4px;'>红灯</span><input size='12' name='redTotal' id='redTotal' readonly='readonly' style='width:30px;margin-right:2px;background:red;color:white;' value='"+lightMap.get("red")+"'/>项");
	out.println("<span style='margin-right:4px;'>得分:</span>S=<input size='12' style='width:40px;' name='realFee' id='realFee' readonly='readonly' value='"+inspectionReport.getRealFee()+"'/>(分)</td>");
	out.println("</tr>");
%>
</table>
					