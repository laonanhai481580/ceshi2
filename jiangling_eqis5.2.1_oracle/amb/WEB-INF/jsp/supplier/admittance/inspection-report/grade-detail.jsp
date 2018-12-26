<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.supplier.entity.InspectionGradeType"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	InspectionGradeType inspectionGradeType = (InspectionGradeType)request.getAttribute("inspectionGradeType");
	if(inspectionGradeType == null){
		inspectionGradeType = new InspectionGradeType();
	}
	boolean canEdit = false;
	String editable = request.getParameter("editable");
	if(editable != null){
		canEdit = Boolean.valueOf(editable);
	}
%>
<div class="opt-content" id="opt-content" style="overflow-y:auto;">
	<table class="form-table-border-left" style="width:100%;border:0px;">
		<caption style="height: 35px;text-align: center"><h2>潜在供应商考察报告 -- <%=inspectionGradeType.getName() %></h2></caption>
		<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${inspectionReport.code}</div></caption>
		<tr>
			<td style="width:90px;">供应商<font color="red">*</font></td>
			<td>
				<input id="supplierName" value="${inspectionReport.supplier.name}" style="width:70%;" readonly="readonly"/>
				<input type="hidden" name="supplierId" id="supplierId" value="${inspectionReport.supplier.id}"></input>
			</td>
			<td style="width:90px;">生产的产品</td>
			<td colspan="3">
				<input name="matingProducts" value="${inspectionReport.matingProducts}" style="width:90%;"  readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td style="width:90px;">稽核日期<font color="red">*</font></td>
			<td>
				<input name="inspectionDate" id="inspectionDate"  readonly="readonly" value="${inspectionReport.inspectionDateStr}" style="width:120px;"/>
			</td>
			<td style="width:90px;">稽核人员<font color="red">*</font></td>
			<td>
				<input id="inspectionManLoginName" value="${inspectionReport.inspectionManLoginName}" type="hidden"/>
				<input id="inspectionMan" readonly="readonly" value="${inspectionReport.inspectionMan}" style="width:70%;"/>
			</td>
			<td style="width:90px;">版本<font color="red">*</font></td>
			<td>
				<input name="reportVersion" value="${inspectionReport.reportVersion}" readonly="readonly" style="width:90px;"/>
			</td>
		</tr>
		<tr>
			<td style="width:90px;">评审人员</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getReviewer() %>" style="width:70%;"/>
			</td>
			<td style="width:90px;">标准分</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getTotalFee() %>" style="width:90px;"/>
			</td>
			<td style="width:90px;">实际分</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getRealFee() %>" style="width:90px;"/>
			</td>
		</tr>
	</table>
	<%
		class Generated{
			List<String> createCheckTypeTr(InspectionGradeType inspectionGradeType,Map<String,Integer> numberMap,boolean canEdit){
				int index = numberMap.get("index");
				List<String> strs = new ArrayList<String>();
				if(!inspectionGradeType.getChildren().isEmpty()){
					for(InspectionGradeType child : inspectionGradeType.getChildren()){
						strs.addAll(createCheckTypeTr(child,numberMap,canEdit));
					}
				}else{
					DecimalFormat df = new DecimalFormat("0.#");
					strs.add("<tr class='inspectionGradeType' id='"+inspectionGradeType.getId()+"'><td style='text-align:center;'>"+(index++)+"</td>");
					strs.add("<td>"+inspectionGradeType.getName()+"</td>");
					strs.add("<td style='text-align:center;'>"+df.format(inspectionGradeType.getTotalFee())+"</td>");
					if(canEdit){
						strs.add("<td style='text-align:center;'><input type='text' style='width:95%;' class='{number:true}' name='realFee' value='"+df.format(inspectionGradeType.getRealFee())+"'></input></td>");
						strs.add("<td><textarea rows=2 class='{maxlen:255}' name='remark'>"+(inspectionGradeType.getRemark()==null?"":inspectionGradeType.getRemark())+"</textarea></td></tr>");
					}else{
						strs.add("<td style='text-align:center;'>"+df.format(inspectionGradeType.getRealFee())+"</td>");
						strs.add("<td>"+(inspectionGradeType.getRemark()==null?"":inspectionGradeType.getRemark())+"</td></tr>");
					}
					numberMap.put("index",index);
				}
				return strs;
			}
		}
// 		DecimalFormat df = new DecimalFormat("0.#");
// 		out.println("<tr>");
// 		out.println("<td colspan=2 style='padding-left:20px;'><span style='margin-right:4px;'>得分:</span>S=<input size=\"12\" name=\"realFee\" id='realFee' readonly='readonly' value='"+inspectionReport.getRealFee()+"'/>(分)</td>");
// 		out.println("</tr>");
	%>
	<form>
		<table class="form-table-border-left" style="width:100%;margin-top:4px;margin-bottom:4px;">
			<tr height=29>
				<td style="width:40px;background:#FFCC99;font-weight:bold;">
					序号
				</td>
				<td style="width:300px;background:#FFCC99;font-weight:bold;">
					项目
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					标准分
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					实际分
				</td>
				<td style="background:#FFCC99;font-weight:bold;">
					备注
				</td>
			</tr>
			<%
			Generated generated = new Generated();
			int rowNumbers = 1;
			Map<String,Integer> numberMap = new HashMap<String,Integer>();
			numberMap.put("index",1);
			canEdit = true;
			List<String> strs = generated.createCheckTypeTr(inspectionGradeType,numberMap,canEdit);
			for(String str : strs){
				out.println(str);
			}
			%>
			<tr height=29>
				<td colspan="2" style="width:40px;background:#CCFFCC;font-weight:bold;text-align:center;">
					合计
				</td>
				<td style="width:60px;background:#CCFFCC;font-weight:bold;text-align:center;">
					<%=inspectionGradeType.getTotalFee() %>
				</td>
				<td style="width:60px;background:#CCFFCC;font-weight:bold;text-align:center;">
					<%=inspectionGradeType.getRealFee() %>
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					
				</td>
			</tr>
		</table>
	</form>
</div>
				