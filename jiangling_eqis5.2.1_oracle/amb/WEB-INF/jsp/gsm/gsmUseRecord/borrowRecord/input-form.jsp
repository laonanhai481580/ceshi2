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
			<td style="width:11%;text-align: center;">所属事业群</td>
			<td style="width:16%;">
			<s:select list="businessDivisions"
					listKey="value" 
					listValue="value" 
					name="businessDivision" 
					id="businessDivision" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:15%;text-align: center;">借用日期</td>
			<td style="width:16%;"><input id="borrowDate" name="borrowDate" isDate="true" value="<s:date name='borrowDate' format="yyyy-MM-dd"/>"/></td>
			<td style="width:15%;text-align: center;">计划归还日期</td>
			<td colspan="2" style="width:27%;"><input id="returnDate" name="returnDate" isDate="true" value="<s:date name='returnDate' format="yyyy-MM-dd"/>"/></td>
<!-- 			<td style="width:15%;" type="hidden" ></td>	 -->
		</tr>
		<tr>
			<td colspan="7" style="padding: 0px; border-bottom: 0px;text-align: center;">
<!-- 			<div> -->
			<table class="form-table-border-left" style="border: 0px; ">
				<tr>
						<td  style="text-align: center; border-top: 0px; border-left: 0px;width: 41px;">操作</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:155px;" >仪器管理编号</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:80px;">仪器名称</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:80px;">规格型号</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:80px;">品牌</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:100px;">备注</td>
					
					</tr>
					<s:iterator value="_borrowRecordSublists" id="item" var="item">
						<tr class="borrowRecordSublists">
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: center;"><input  name="managerAssets" onclick="Epm(this);" value="${managerAssets}" /></td>
							<td style="text-align: center;"><input  name="equipmentName" value="${equipmentName}" /></td>
							<td style="text-align: center;"><input  name="equipmentModel" value="${equipmentModel}" /></td>
							<td style="text-align: center;"><input  name="brand" value="${brand}" /></td>
							<td style="text-align: center;"><input  name="remark" value="${remark}" /></td>
						</tr>
					</s:iterator>
			</table>
<!-- 			</div> -->
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">借用人</td>
			<td colspan="2"><input id="borrower" name="borrower"  value="${borrower }"/>
			</td>
			<td style="text-align: center;">借用部门</td>
			<td colspan="3"><input id="borrowerDept" name="borrowerDept" value="${borrowerDept }"/></td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align: center;">原保管人确认</td>
			<td colspan="6"><textarea rows="5" id="depository" name="depository" value="${depository }"></textarea></td>
		</tr>
		<tr>
			<td style="text-align: center;">原保管人</td>
			<td><input id="preserver" name="preserver" hiddenInputId="preserverLogin" style="float: left;" isUser="true" value="${preserver }"/>
				<input type="hidden" id="preserverLogin" name="preserverLogin" value="${preserverLogin }">
			</td>
			<td style="text-align: center;">部门</td>
			<td><input id="preserverDept" name="preserverDept" style="float: left;" isDept="true" value="${preserverDept }"/></td>
			<td style="text-align: center;">确认日期</td>
			<td style="width:20%;"><input id="preserverDate" name="preserverDate" isDate="true" value="<s:date name='preserverDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align: center;">实验室确认</td>
			<td colspan="6"><textarea rows="5" id="laboratory" name="laboratory" value="${laboratory }"></textarea></td>
		</tr>
		<tr>
			<td style="text-align: center;">确认人</td>
			<td><input id="confirmor" name="confirmor" hiddenInputId="confirmorLogin" style="float: left;" isUser="true"  value="${confirmor }"/>
			<input type="hidden" id="confirmorLogin" name="confirmorLogin" value="${confirmorLogin }">
			</td>
			<td style="text-align: center;">部门</td>
			<td><input id="confirmorDept" name="confirmorDept" style="float: left;" isDept="true" value="${confirmorDept }"/></td>
			<td style="text-align: center;">确认日期</td>
			<td style="width:15%;"><input id="confirmorDate" name="confirmorDate" isDate="true" value="<s:date name='confirmorDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
<!-- 		<tr> -->
<%-- 		<td onclick="loadXMLDoc()"><input id="confirmor1" name="confirmor1" style="float: left;" isUser="true"  value="${confirmor1 }" /></td> --%>
<!-- 		<td colspan="6"> -->
			
<!-- 			<button type="button" onclick="loadXMLDoc()">请求数据</button> -->
<!-- 			<div id="myDiv"></div> -->
<!-- 			<input id="myDiv" name="" value=""> -->
<!-- 		</td> -->
<!-- 		</tr> -->
	</table>