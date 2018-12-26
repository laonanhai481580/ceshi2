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
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:10%;">管理编号</td>
			<td style="width:12%;"><input id="managerAssets" name="managerAssets" onclick="Epm();" value="${managerAssets }"/></td>
			<td style="width:12%;">设备名称</td>
			<td style="width:12%;"><input id="equipmentName" name="equipmentName" readonly="ture" value="${equipmentName }"/></td>
			<td style="width:12%;">规格型号</td>
			<td style="width:12%;"><input id="equipmentModel" name="equipmentModel" readonly="ture" value="${equipmentModel }"/></td>
		</tr>
		<tr>
			<td>机身编号</td>
			<td><input type="text" id="factoryNumber" name="factoryNumber" readonly="ture" value="${factoryNumber }"/></td>
			<td>使用部门</td>
			<td><input id="devName" name="devName" readonly="ture" value="${devName }"/></td>
			<td>存放位置</td>
			<td><input id="placeStorage" name="placeStorage" value="${placeStorage }"/></td>
		</tr>
		<tr>
			<td>事业部</td>
			<td>
				<s:select list="businessUnitNames"
					listKey="value" 
					listValue="value" 
					name="businessUnitName" 
					id="businessUnitName"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td>日期</td>
			<td><input id="taskCreatedTime" name="taskCreatedTime" isDate="true" value="<s:date name='taskCreatedTime' format="yyyy-MM-dd"/>"/></td>
			
			
		</tr>
		<tr>
			<td rowspan="2">检测设备偏离校验状态描述</td>
			<td colspan="5"><textarea rows="5" id="deviateState" name="deviateState">${deviateState }</textarea></td>
		</tr>
		
		
		<tr>
			<td>实验室</td>
			<td>
			<input id="laboratory" name="laboratory"  style="float: left;" isUser="true" value="${laboratory }"/>
<%-- 			<input type="hidden" id="" name="" value="${ }"> --%>
			</td>
			<td colspan="3"></td>
		</tr>
		<tr>
			<td rowspan="4" >是否需要重新检测已检产品</td>
			<td >使用部门确认:</td>
			<td colspan="4"><textarea id="EPCdeptOpinion" name="EPCdeptOpinion" >${EPCdeptOpinion }</textarea></td>
		</tr>
		<tr id="jointlySignStr">
			<td>使用人</td>
			<td>
				<input style="float:left;" hiddenInputId="userPeopleLogin"  isUser="true" id="userPeople" name="userPeople" isUser="true" value="${userPeople}"/>
	        	<input type="hidden" login="true" name="userPeopleLogin" id="userPeopleLogin"  value="${userPeopleLogin}" />
             	<a class="small-button-bg" style="margin-left:2px;" onclick="setAllLogs();" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>工程部:</td>
			<td>
				<input style="float:left;" hiddenInputId="EPCdeptLogin"  isUser="true" id="EPCdept" name="EPCdept" isUser="true" value="${EPCdept}"/>
	        	<input type="hidden" login="true" name="EPCdeptLogin" id="EPCdeptLogin"  value="${EPCdeptLogin}" />
             	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('EPCdept','EPCdeptLogin')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td><input type="hidden" id="jointlySignStrs" name="jointlySignStrs" value="${jointlySignStrs}"/></td>
		</tr>
		<tr>
			<td >品保部意见:</td>
			<td colspan="4"><textarea id="QAdeptOpinion" name="QAdeptOpinion" >${QAdeptOpinion }</textarea></td>
		</tr>
		<tr>
			<td >品保部:</td>
			<td><input id="QAdept" name="QAdept" hiddenInputId="QAdeptLogin" style="float: left;" isUser="true"value="${QAdept }"/>
			<input type="hidden" id="QAdeptLogin" name="QAdeptLogin" value="${QAdeptLogin }">
			</td>
			<td colspan="4"></td>
		</tr>
		<tr>
			<td rowspan="5">原因分析及改善对策</td>
			<td colspan="5">需要重新检测的已检产品的范围(包括时间段和数量)和检验结果:</td>
		</tr>
		<tr>
			<td colspan="5"><textarea rows="5" id="inspectionResult" name="inspectionResult"  >${inspectionResult }</textarea></td>
		</tr>
		<tr>
			<td>原因分析:</td>
			<td colspan="4"><textarea rows="5" id="rootCauses" name="rootCauses" >${rootCauses }</textarea></td>
		</tr>
		<tr>
			<td>改善对策:</td>
			<td colspan="4"><textarea rows="5" id="correctiveActions" name="correctiveActions" >${correctiveActions }</textarea></td>
		</tr>
		<tr>
			<td>责任人</td>
			<td>
			<input id="responsible" name="responsible" hiddenInputId="responsibleLogin" style="float: left;" isUser="true" value="${responsible }"/>
			<input type="hidden"id="responsibleLogin" name="responsibleLogin" value="${responsibleLogin }">
			</td>
			<td>责任单位主管</td>
			<td colspan="2"><input id="responsibleLead" name="responsibleLead" hiddenInputId="responsibleLeadLogin" style="float: left;" isUser="true" value="${responsibleLead }"/>
			<input type="hidden"id="responsibleLeadLogin" name="responsibleLeadLogin" value="${responsibleLeadLogin }">
			</td>
		</tr>
		<tr>
			<td rowspan="2">效果确认</td>
			<td colspan="5"><textarea rows="5" id="checkResults" name="checkResults" >${checkResults }</textarea></td>
		</tr>
		<tr>
			<td>实验室发起人:</td>
			<td>
			<input id="initiator" name="initiator" hiddenInputId="initiatorLogin" style="float: left;" isUser="true" value="${initiator }"/>
			<input type="hidden" name="initiatorLogin" id="initiatorLogin"  value="${initiatorLogin }" />
			</td>
			<td>实验室主管:</td>
			<td colspan="2"><input id="laboratoryLead" hiddenInputId="laboratoryLeadLogin" style="float: left;" isUser="true" name="laboratoryLead" value="${laboratoryLead }"/>
			<input type="hidden" name="laboratoryLeadLogin" id="laboratoryLeadLogin"  value="${laboratoryLeadLogin }" />
			</td>
		</tr>
<!-- 		<tr><textarea  type="text"  style="width:260;overflow-x:visible;overflow-y:visible;"></textarea></tr> -->
		
	</table>