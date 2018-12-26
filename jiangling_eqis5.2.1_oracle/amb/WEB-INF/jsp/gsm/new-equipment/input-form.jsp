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
			<td style="width:160px;text-align:center;">事业部</td>
			<td style="">
				<s:select list="businessDivisions" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  labelSeparator=""
					  name="businessDivision"
					  id="businessDivision"
					  emptyOption="false"></s:select>
			</td>
			
			
			<td style="width:160px;text-align:center;">申请部门</td>
			<td>
				<input name="applyDepartment" id="applyDepartment" value="${applyDepartment}"/>
			</td>
			<td style="width:160px;text-align:center;">申请人</td>
			<td style="width:200px;">
				<input  id="proposer" name="proposer"  isUser="true" hiddenInputId="proposerLogin" style="float: left;"   value="${proposer}" />
				<input type="hidden" id="proposerLogin" name="proposerLogin" value="${proposerLogin}" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">工号</td>
			<td>
				<input name="jobNumber" id="jobNumber" value="${jobNumber}"/>
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="applyDate" name="applyDate" value="<s:date name='applyDate' format="yyyy-MM-dd"/>" />
			</td>

			<td></td>
			<td></td>
		</tr>
		
		<tr>
			<td colspan="6" style="padding: 0px; border-bottom: 0px;">
			<table class="form-table-border-left" style="border: 0px; width: 100%;">
				<thead>
					<tr>
						<td  style="text-align: center; border-top: 0px; border-left: 0px;">操作</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">设备名称</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">型号规格</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">厂商</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">出厂编号</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">保管人</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">仪器管理编号</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">备注</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="_newEquipmentSublists" id="item" var="item">
						<tr class="newEquipmentSublists">
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: center;"><input type="text" stage="one" name="deviceName" value="${deviceName}" /></td>
							<td style="text-align: center;"><input type="text" stage="one" name="modelSpecification" value="${modelSpecification}" /></td>
							<td style="text-align: center;"><input type="text" stage="one" name="manufacturer" value="${manufacturer}" /></td>
							<td style="text-align: center;"><input type="text" stage="one" name="factoryNumber" value="${factoryNumber}" /></td>
							<td style="text-align: center;"><input type="text" stage="one" name="preserver" value="${preserver}" /></td>
							<td style="text-align: center;"><input type="text" stage="two" name="nstrumentNumber" value="${nstrumentNumber}" /></td>
							<td style="text-align: center;"><input type="text" stage="two" name="remark" value="${remark}" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;" rowspan='2'>实验室确认</td>
			<td style="width:160px;text-align:center;" >意见</td>
			<td colspan='2'><textarea rows="5" style="width:90%" id="opinion" name="opinion" >${opinion }</textarea></td>
			<td>是否生成MSA</td>
			<td>
				<input  type="radio" id="d1" name="isMSA" value="是"  <s:if test='%{isMSA=="是"}'>checked="true"</s:if> title="是"/><label for="d1">是</label>
				<input  type="radio" id="d2" name="isMSA" value="否"  checked="checked"<s:if test='%{isMSA=="否"}'>checked="true"</s:if> title="否"/><label for="d2">否</label>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">确认人</td>
			<td style="width:200px;">
				<input type="text" id="confirmor" isTemp="true" isUser="true" hiddenInputId="confirmorLogin" style="float: left;"  name="confirmor" value="${confirmor}" />
				<input type="hidden" id="confirmorLogin" name="confirmorLogin" value="${confirmorLogin}" />
			</td>		
			<td style="width:160px;text-align:center;">确认时间</td>
			<td colspan='2' style="width:200px;">
				<input  type="text" isDate="true" id="affirmDate" name="affirmDate" value="<s:date name='affirmDate' format="yyyy-MM-dd"/>" />
			</td>
			
		</tr>
		
		
		
	</table>