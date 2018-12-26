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
	<table style="width:100%;margin: auto;border:0px;" class="form-table-border-left" >
	<tr>
			<td>公司主体</td>
			<td style="width:15%;">
			<s:select list="companybys"
					listKey="value" 
					listValue="value" 
					name="companyby" 
					id="companyby" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td >所属事业部</td>
			<td style="width:15%;">
				<s:select list="businessDivisions"
					listKey="value" 
					listValue="value" 
					name="businessDivision" 
					id="businessDivision" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td colspan="6" style="padding:0px;" id="checkItemsParent" >
			<div style="overflow-x:auto;overflow-y:hidden;">
			<table  class="form-table-border-left" style="border:0px;table-layout:fixed;">
			<tbody>
					<tr >	
						<td  style="text-align: center;width:50px;">操作</td>
						<td  style="text-align: center;width:100px;">管理编号</td>
						<td  style="text-align: center;width:100px;">设备名称</td>
						<td  style="text-align: center;width:100px;">规格型号</td>
						<td  style="text-align: center;width:100px;">安装地点</td>
						<td  style="text-align: center;width:100px;">责任人</td>
						<td  style="text-align: center;width:100px;">测量范围</td>
						<td  style="text-align: center;width:100px;">制造商</td>
						<td  style="text-align: center;width:100px;">机身编号</td>
						<td  style="text-align: center;width:100px;">校准日期</td>
						<td  style="text-align: center;width:100px;">校准方式</td>
						<td  style="text-align: center;width:100px;">使用参数标准</td>
						<td  style="text-align: center;width:100px;">状态</td>
						<td  style="text-align: center;width:100px;">确认人</td>
					</tr>
			
					<s:iterator value="_entrustSublists" id="item" var="item">
						<tr class="entrustSublists">
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>					
							<td style="text-align: center;"><input style="width:100%;" name="managerAssets" onclick="Epm(this);" value="${managerAssets }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="equipmentName" value="${equipmentName }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="equipmentModel" value="${equipmentModel }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="address" value="${address }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="dutyMan" value="${dutyMan }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="measuringRange" value="${measuringRange }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="manufacturer" value="${manufacturer }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="factoryNumber" value="${factoryNumber }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="calibrationDate" isDate="true" value="<s:date name='calibrationDate' format="yyyy-MM-dd"/>" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="checkMethod" value="${checkMethod }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="parameterStandard" value="${parameterStandard }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="measurementState" value="${measurementState }" /></td>
							<td style="text-align: center;"><input style="width:100%;" name="confirmor" value="${confirmor }" /></td>
							
						</tr>
					</s:iterator>
			</tbody>
			</table>
			</div>
			</td>
		</tr>
		<tr>
			<td colspan="6" style="text-align: center;">申请原因(使用部门)</td>
		</tr>
		<tr>
			<td colspan="6"><textarea rows="6" id="applyReason" name="applyReason" >${applyReason }</textarea></td>
		</tr>
		<tr>
			<td style="width:10%">责任人</td>
			<td style="width:15%"><input id="useResponsible" name="useResponsible" hiddenInputId="transactor"  value="${useResponsible }"/>
			<input type="hidden" id="transactor" name="transactor" value="${transactor }">
			</td>
			<td style="width:10%">部门</td>
			<td style="width:15%"><input id="useDepartment" name="useDepartment"  value="${useDepartment }"/></td>
			<td style="width:10%">确认日期</td>
			<td style="width:15%"><input id="confirmorDate" name="confirmorDate" isDate="true" value="<s:date name='confirmorDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td colspan="6" style="text-align: center;">部门主管审核</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="auditOfHead" name="auditOfHead" >${auditOfHead }</textarea></td>
		</tr>
		<tr>
			<td>部门主管</td>
			<td><input id="deptHead" name="deptHead" hiddenInputId="deptHeadLogin" style="float: left;" isUser="true"value="${deptHead }"/>
			<input type="hidden" id="deptHeadLogin" name="deptHeadLogin" value="${deptHeadLogin }">
			</td>
			<td>审核日期</td>
			<td><input id="auditDate" name="auditDate" isDate="true" value="<s:date name='auditDate' format="yyyy-MM-dd"/>"/></td>
			<td colspan="2"></td>
		</tr>
		<tr >
			<td colspan="6" style="text-align: center;">使用部门最高领导校准:</td>
		</tr>
		<tr>
			<td colspan="6"><textarea id="useDeptLead" name="useDeptLead" >${useDeptLead }</textarea></td>
		</tr>
		<tr>
			<td>使用部门最高领导校准</td>
			<td><input id="useDeptCalibr" name="useDeptCalibr" hiddenInputId="useDeptLeadLogin" isUser="true" style="float: left;"value="${useDeptCalibr }"/>
			<input type="hidden" id="useDeptLeadLogin" name="useDeptLeadLogin" value="${useDeptLeadLogin }">
			</td>
			<td>校准日期</td>
			<td><input id="useDeptCalibrDate" name="useDeptCalibrDate" isDate="true" value="<s:date name='useDeptCalibrDate' format="yyyy-MM-dd"/>"/></td>
			<td colspan="2"></td>
		</tr>
		
	</table>