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
<input type="hidden" name="supplierId" id="supplierId" value="${supplierId}" />
<input type="hidden" name="year" id="year" value="${year}" />
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					    <tr>
					      <td>供应商名称</td>
					      <td>
					          <input  name="supplierName" id="supplierName" value="${supplierName}" readonly="readonly"/>
					      </td>
					      <td>供货事业部</td>
					        <td>
					          <input  name="supplyFactory" id="supplyFactory" value="${supplyFactory}" />
					      </td>
					      <td>供应物料</td>
					        <td>
					          <input  name="supplyMaterial" id="supplyMaterial" value="${supplyMaterial}" readonly="readonly"/>
					      </td>
					    </tr>
					    <tr>
					        <td colspan='6' style="text-align:center;">首次</td>
					    </tr>
					    <tr>
					        <td>首次计划日期</td><td><input isDate="true" name="firstCheckPlanDate" id="firstCheckPlanDate" value="<s:date name='firstCheckPlanDate' format="yyyy-MM-dd"/>" /></td>
					        <td>首次实际日期</td><td><input isDate="true" name="firstCheckDesignDate" id="firstCheckDesignDate" value="<s:date name='firstCheckDesignDate' format="yyyy-MM-dd"/>" /></td>
					        <td>首次稽核结果</td>
					       <td>
					       	<s:select list="finalCheckResults"
								listKey="value" 
								listValue="value" 
								name="firstCheckResult" 
								id="firstCheckResult" 
								onchange=""
								emptyOption="true"
								theme="simple">
							</s:select>
					       </td>
					    </tr>
					    <tr>
					       <td>首次稽核报告</td>
					       <td colspan='3'><input type="hidden" isFile=true name="checkFile" id="checkFile" value='${checkFile}'/></td>
					       <td>审核方式</td>
					       <td>
					           <select name="checkType" id="checkType" class="xla_k">
			                         <option value=""></option>
			                         <option value="实地" <c:if test='${"实地" eq checkType}'>selected</c:if>>实地</option>
			                         <option value="自评" <c:if test='${"自评" eq checkType}'>selected</c:if>>自评</option>
                			  </select>
					       </td>
					    </tr>
					     <tr>
					        <td colspan='6' style="text-align:center;">二次</td>
					    </tr>
					     <tr>
					        <td>二次计划日期</td><td><input isDate="true" name="secondCheckPlanDate" id="secondCheckPlanDate" value="<s:date name='secondCheckPlanDate' format="yyyy-MM-dd"/>" /></td>
					        <td>二次实际日期</td><td><input isDate="true" name="secondCheckDesignDate" id="secondCheckDesignDate" value="<s:date name='secondCheckDesignDate' format="yyyy-MM-dd"/>" /></td>
					        <td>二次稽核结果</td>
					       <td>
					       	<s:select list="finalCheckResults"
								listKey="value" 
								listValue="value" 
								name="secondCheckResult" 
								id="secondCheckResult" 
								onchange=""
								emptyOption="true"
								theme="simple">
							</s:select>
					       </td>
					    </tr>
					     <tr>
					       <td>二次稽核报告</td>
					       <td colspan='5'> <input type="hidden" isFile=true name="secondCheckFile" id="secondCheckFile" value='${secondCheckFile}'></input></td>
					    </tr>
					  	<tr>
					  		<td colspan='6' style="text-align:center;">改善报告</td>
					  	</tr>
					    <tr>
					       <td>首次稽核日期</td>
					       <td><input  name="firstCheckDate" isDate="true" id="firstCheckDate" value="<s:date name='firstCheckDate' format="yyyy-MM-dd"/>" /></td>
					       <td>二次稽核日期</td>
					       <td><input  name="secondCheckDate" isDate="true" id="secondCheckDate" value="<s:date name='secondCheckDate' format="yyyy-MM-dd"/>" /></td>
					       <td>最终稽核结果</td>
					       <td>
					       	<s:select list="finalCheckResults"
								listKey="value" 
								listValue="value" 
								name="finalCheckResult" 
								id="finalCheckResult" 
								onchange=""
								emptyOption="true"
								theme="simple">
							</s:select>
					       </td>
                        </tr>
                        <tr>
                           <td>问题状态</td>
					       <td><input  name="problemState" id="problemState" value="${problemState }" /></td>
					       <td>关闭日期</td>
					       <td><input  name="problemCloseDate" isDate="true" id="problemCloseDate" value="<s:date format='yyyy-mm-dd' name='problemCloseDate'/>" /></td>
                           <td>改善报告</td>
					       <td > <input type="hidden" isFile=true name="improveFile" id="improveFile" value='${improveFile}'></input></td>
                          
                        </tr>
                        <tr>
                           <td>备注</td>
					       <td colspan='3'><input  name="remark" id="remark" value="${remark}" /></td>
                       		 <td>确认人</td>
					       <td><input  name="checker" id="checker" value="${checker }" style="float:left;" hiddenInputId="checkerLog" isUser="true"/>
					          <input  name="checkerLog" id="checkerLog" value="${checkerLog }"  type="hidden"/>
					       </td>
                        </tr>
                       
					</table>