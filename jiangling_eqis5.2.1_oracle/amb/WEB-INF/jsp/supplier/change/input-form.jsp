<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-tabl">
	<tbody>
	     <tr>
	         <td >申请公司</td>
	         <td ><input id="applicant" name="applicant" value="${applicant }"/></td>
	         <td >申请日期</td>
	         <td  ><input id="applyingDate" name="applyingDate" isDate="true" value="<s:date name='applyingDate' format="yyyy-MM-dd"/>" /></td>
	     	<td >事业部</td>
	        <td >
					<s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnit" 
					id="businessUnit" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
					</td>
	     </tr>
	     <tr>
	         <td>申请事由</td>
	         <td colspan="5" ><textarea rows="5" id="reason" style="width:98%;" name="reason">${reason }</textarea></td>
	     </tr>
	    <tr>
	         <td>材料名</td>
	         <td><input id="materialName" name="materialName" value="${materialName}"/></td>
	         <td>物料编码</td>
	         <td><input id="materialCode" name="materialCode" value="${materialCode}"/></td>
	         <td>适用机型</td>
	         <td><input id="applyingProject" name="applyingProject" value="${applyingProject }"/></td>
	     </tr>
	     <tr>
	         <td >变更项目</td>
	         <td colspan="2">
	              <s:select list="levels" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="projectChange"
				  id="projectChange"
				  emptyOption="true"
				  labelSeparator=""
				  onChange="findKey(this);"
				  cssStyle="width:250px;"
				  ></s:select>
<%-- 	             <input id="projectChange" name="projectChange" value="${projectChange }"/> --%>
	           </td>
	         <td>变更等级</td>
	         <td colspan="2">
	             <input id="changeLevel" readonly="readonly" name="changeLevel" value="${changeLevel }" />
	         </td>
	     </tr>
	     <tr>
	         <td colspan="2" rowspan="3" style="text-align:center;" >变更内容</td>
	         <td colspan="2">变更前</td>
	         <td colspan="2" >变更后</td>
	     </tr>
	     <tr>
	         <td colspan="2">
	         <textarea rows="5" id="contentBefore" style="width:98%;" name="contentBefore">${contentBefore }</textarea>
<%-- 	         <input id="contentBefore" name="contentBefore" value="${contentBefore }"/></td> --%>
	         <td colspan="2">
	         <textarea rows="5" id="contentAfter" style="width:98%;" name="contentAfter">${contentAfter }</textarea>
<%-- 	         <input id="contentAfter" name="contentAfter" value="${contentAfter }"/></td> --%>
	     </tr>
	      <tr>
	         <td colspan="2"><input id="beforeFile" type="hidden" isFile="true" name="beforeFile" value="${beforeFile }"/></td>
	         <td colspan="2"><input id="afterFile" type="hidden" isFile="true" name="afterFile" value="${afterFile }"/></td>
	     </tr>
	     <tr>
	     	<td colspan="2">评估报告</td>
	     	<td colspan="4"><input id="reportFile" type="hidden" isFile="true" name="reportFile" value="${reportFile }"/></td>
	     </tr>
	      <tr>
	         <td  colspan="2" rowspan="2" >品质部意见</td>
	         <td colspan="2"><input id="qualityOpinion" name="qualityOpinion" value="${qualityOpinion }"/></td>
	         <td colspan="2"><input id="qualityFile" type="hidden" isFile="true" name="qualityFile" value="${qualityFile }"/></td>
	     </tr>
	     <tr>
	         <td colspan="">经办人</td>
	         <td ><input id="qualityProcesser" style="float:left;" hiddenInputId="qualityProcesserLog" isUser="true" name="qualityProcesser" value="${qualityProcesser }"/>
	               <input type="hidden" name="qualityProcesserLog" id="qualityProcesserLog"  value="${qualityProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qualityProcesser','qualityProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td colspan="">审核人</td>
	         <td ><input id="qualityChecker" style="float:left;" hiddenInputId="qualityCheckerLog" isUser="true" name="qualityChecker" value="${qualityChecker }"/>
	                <input type="hidden" name="qualityCheckerLog" id="qualityCheckerLog"  value="${qualityCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qualityChecker','qualityCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	      <tr>
	         <td colspan="2" rowspan="2" >研发部意见</td>
	         <td colspan="2"><input id="developOpinion" name="developOpinion" value="${developOpinion }"/></td>
	         <td colspan="2"><input id="developFile" type="hidden" isFile="true" name="developFile" value="${developFile }"/></td>
	     </tr>
	     <tr>
	         <td colspan="">经办人</td>
	         <td ><input id="developProcesser" style="float:left;" hiddenInputId="developProcessLog" isUser="true" name="developProcesser" value="${developProcesser }"/>
	               <input type="hidden" name="developProcessLog" id="developProcessLog"  value="${developProcessLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('developProcesser','developProcessLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td colspan="">审核人</td>
	         <td ><input id="developChecker" style="float:left;" hiddenInputId="developCheckLog" isUser="true" name="developChecker" value="${developChecker }"/>
	               <input type="hidden" name="developCheckLog" id="developCheckLog"  value="${developCheckLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('developChecker','developCheckLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	     <tr>
	         <td>采购部处理人</td>
	         <td >
	            <input id="procurementProcesser" style="float:left;" hiddenInputId="procurementProcesserLog" isUser="true" name="procurementProcesser" value="${procurementProcesser }"/>
	            <input type="hidden" name="procurementProcesserLog" id="procurementProcesserLog"  value="${procurementProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('procurementProcesser','procurementProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>采购部意见</td>
	         <td colspan="3" ><input id="procurementOpinion" name="procurementOpinion" value="${procurementOpinion }"/></td>
	     </tr>
	</tbody> 	 
</table>
