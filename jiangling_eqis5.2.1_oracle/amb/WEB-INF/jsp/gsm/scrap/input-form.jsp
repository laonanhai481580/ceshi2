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
			<td style="width:12%;text-align:center;">管理编号</td>
			<td style="width:12%;"><input id="managerAssets" name="managerAssets" onclick="Epm();" value="${managerAssets }"/></td>
			<td style="width:12%;text-align:center;">设备名称</td>
			<td style="width:12%;"><input id="equipmentName" name="equipmentName" readonly="ture" value="${equipmentName }"/></td>
			<td style="width:12%;text-align:center;">型号规格</td>
			<td style="width:12%;"><input id="equipmentModel" name="equipmentModel" readonly="ture" value="${equipmentModel }"/></td>
		</tr>
		<tr>
			<td style="text-align:center;">生产产商</td>
			<td><input id="manufacturer" name="manufacturer" readonly="ture" value="${manufacturer }"/></td>
			<td style="text-align:center;">出厂编号</td>
			<td><input id="factoryNumber" name="factoryNumber" readonly="ture" value="${factoryNumber }"/></td>
			<td style="text-align:center;">使用部门</td>
			<td><input id="devName" name="devName" readonly="ture" value="${devName }"/></td>
		</tr>
		<tr>
			<td style="text-align:center;">责任人</td>
			<td><input style="float: left;" id="responsible" name="responsible" hiddenInputId="responsibleLogin" isUser="ture" value="${responsible }"/>
			<input type="hidden" id="responsibleLogin" name="responsibleLogin" value="${responsibleLogin }">
			</td>
			<td style="text-align:center;">设备担当</td>
			<td><input style="float: left;" id="equipmentGuarantee" name="equipmentGuarantee" hiddenInputId="equipmentGuaranteeLogin" isUser="ture" value="${equipmentGuarantee }"/>
				<input type="hidden" id="equipmentGuaranteeLogin" name="equipmentGuaranteeLogin" value="${equipmentGuaranteeLogin }">
			</td>
			<td style="text-align:center;">购置日期</td>
			<td><input id="acquisitionDate" name="acquisitionDate" isDate="true" value="<s:date name='acquisitionDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td style="text-align:center;">启用日期</td>
			<td><input id="usingDate" name="usingDate" isDate="true" value="<s:date name='usingDate' format="yyyy-MM-dd"/>"/></td>
			<td style="text-align:center;">停用日期</td>
			<td><input id="disableDate" name="disableDate" isDate="true" value="<s:date name='disableDate' format="yyyy-MM-dd"/>"/></td>
			<td style="text-align:center;">资产编号</td>
			<td><input id="assetNo" name="assetNo" value="${assetNo }"/></td>
		</tr>
		<tr>
			<td style="text-align:center;">放置地点</td>
			<td colspan="5"><textarea rows="3" id="placeStorage" name="placeStorage" value="${placeStorage }"></textarea></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">报废原因(使用部门):</td>
		</tr>
		<tr>
			<td colspan="6" ><textarea rows="5" id="scrapReason" name="scrapReason" >${scrapReason }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">申请人/日期</td>
			<td><input id="proposer" name="proposer" value="${proposer }"/></td>
			<td><input id="proposerDate" name="proposerDate" isDate="true" value="<s:date name='proposerDate' format="yyyy-MM-dd"/>"/></td>
			<td style="text-align:center;">审核人/日期</td>
			<td><input style="float: left;" id="scrapAudit" name="scrapAudit" hiddenInputId="scrapAuditLogin" isUser="ture" value="${scrapAudit }"/>
			<input type="hidden" id="scrapAuditLogin" name="scrapAuditLogin" value="${scrapAuditLogin }">
			</td>
			<td><input id="scrapAuditDate" name="scrapAuditDate" isDate="true" value="<s:date name='scrapAuditDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">实验室验证:</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="laboratorycheck" name="laboratorycheck" >${laboratorycheck }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">确认人/日期</td>
			<td><input style="float: left;" id="confirmor" name="confirmor" hiddenInputId="confirmorLogin" isUser="ture" value="${confirmor }"/>
			<input type="hidden" id="confirmorLogin" name="confirmorLogin" value="${confirmorLogin }">
			</td>
			<td><input id="confirmorDate" name="confirmorDate" isDate="true" value="<s:date name='confirmorDate' format="yyyy-MM-dd"/>"/></td>
			<td style="text-align:center;">审核人/日期</td>
			<td><input style="float: left;" id="labAudit" name="labAudit" hiddenInputId="labAuditLogin" isUser="ture" value="${labAudit }"/>
			<input type="hidden" id="labAuditLogin" name="labAuditLogin" value="${labAuditLogin }">
			</td>
			<td><input id="labAuditDate" name="labAuditDate" isDate="true" value="<s:date name='labAuditDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">使用部门最高主管核准:</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="userDept" name="userDept" >${userDept }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">核准人</td>
			<td colspan="2"><input style="float: left;" id="deptAudit" name="deptAudit" hiddenInputId="deptAuditLogin" isUser="ture" value="${deptAudit }"/>
			<input type="hidden" id="deptAuditLogin" name="deptAuditLogin" value="${deptAuditLogin }">
			</td>
			<td style="text-align:center;">核准日期</td>
			<td colspan="2"><input id="deptAuditDate" name="deptAuditDate" isDate="true" value="<s:date name='deptAuditDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">行政部确认</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="AdminDept" name="AdminDept" >${AdminDept }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">确认人</td>
			<td colspan="2"><input style="float: left;" id="adminAudit" name="adminAudit" hiddenInputId="adminAuditLogin" isUser="ture" value="${adminAudit }"/>
			<input type="hidden" id="adminAuditLogin" name="adminAuditLogin" value="${adminAuditLogin }">
			</td>
			<td style="text-align:center;">确认日期</td>
			<td colspan="2"><input id="adminAuditDate" name="adminAuditDate" isDate="true" value="<s:date name='adminAuditDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">财务部确认</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="financeDept" name="financeDept" >${financeDept }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">确认人</td>
			<td colspan="2"><input style="float: left;" id="financeAudit" name="financeAudit" hiddenInputId="financeAuditLogin" isUser="ture" value="${financeAudit }"/>
			<input type="hidden" id="financeAuditLogin" name="financeAuditLogin" value="${financeAuditLogin }">
			</td>
			<td style="text-align:center;">确认日期</td>
			<td colspan="2"><input id="financeAuditDate" name="financeAuditDate" isDate="true" value="<s:date name='financeAuditDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align:center;">实验室确认:</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="laboratoryConfirmor" name="laboratoryConfirmor" >${laboratoryConfirmor }</textarea></td>
		</tr>
		<tr>
			<td style="text-align:center;">确认人</td>
			<td colspan="2"><input style="float: left;" id="labconfirmor" name="labconfirmor" hiddenInputId="labconfirmorLogin" isUser="ture" value="${labconfirmor }"/>
			<input type="hidden" id="labconfirmorLogin" name="labconfirmorLogin" value="${labconfirmorLogin }">
			</td>
			<td style="text-align:center;">确认日期</td>
			<td colspan="2"><input id="labconfirmorDate" name="labconfirmorDate" isDate="true" value="<s:date name='labconfirmorDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		
	</table>