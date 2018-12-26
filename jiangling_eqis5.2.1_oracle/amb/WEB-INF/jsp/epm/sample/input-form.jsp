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
			<td style="width:15%;text-align: center;">委托单号</td>
			<td style="width:18%;"><input id="reportNo" name="reportNo" value="${reportNo}"/></td>
			<td style="width:15%;text-align: center;">送样日期</td>
			<td style="width:18%;"><input id="sendDate" name="sendDate"  value="<s:date name='sendDate' format="yyyy-MM-dd HH:mm"/>" /></td>
			<td style="width:15%;text-align: center;">送检人</td>
			<td><input id="inspectionPerson" name="inspectionPerson" hiddenInputId="inspectionPersonLogin" style="float: left;" isUser="true" value="${inspectionPerson}"/>
			<input type="hidden" id="inspectionPersonLogin" name="inspectionPersonLogin" value="${inspectionPersonLogin}"></td>
		</tr>
		<tr>
			<td style="width:15%;text-align: center;">客户编号</td>
			<td ><input id="customerNo" name="customerNo" value="${customerNo}"/></td>
			<td style="width:15%;text-align: center;">机种编号</td>
			<td><input id="productNo" name="productNo" value="${productNo}"/></td>
			<td style="width:15%;text-align: center;">送测部门</td>
			<td><input id="inspectionDapt" name="inspectionDapt" isDept="true" style="float: left;" value="${inspectionDapt}"/></td>
		</tr>
		<tr>
			<td style="width:15%;text-align: center;">样品名称</td>
			<td><input id="sampleName" name="sampleName" value="${sampleName}"/></td>
			<td style="width:15%;text-align: center;">样品数量</td>
			<td><input id="quantity" name="quantity" value="${quantity}"/></td>
			<td style="width:15%;text-align: center;">是否留样:</td>
			<td>
				<input  type="radio" id="d1" name="sampleHandling" value="是" checked="checked" <s:if test='%{sampleHandling=="是"}'>checked="true"</s:if> title="是"/><label for="d1">是</label>
				<input  type="radio" id="d2" name="sampleHandling" value="否"  <s:if test='%{sampleHandling=="否"}'>checked="true"</s:if> title="否"/><label for="d2">否</label>
			</td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align: center;">样品管理</td>
			<td style="text-align: center;">收样日期</td>
			<td ><input id="receivedDate" name="receivedDate"  value="<s:date name='receivedDate' format="yyyy-MM-dd HH:mm"/>" /></td>
			<td colspan="3"></td>
		
		</tr>
		<tr>
			<td style="text-align: center;">样品管理员</td>
			<td colspan="4"><input id="specimenAdmin" name="specimenAdmin" hiddenInputId="specimenAdminLogin" style="float: left;" isUser="true" value="${specimenAdmin}"/>
			<input type="hidden" id="specimenAdminLogin" name="specimenAdminLogin" value="${specimenAdminLogin}"></td>
		</tr>
		<tr>
			<td style="text-align: center;" rowspan="2">样品颁发</td>
			<td style="text-align: center;">颁发日期</td>
			<td ><input id="grantDate" name="grantDate" isDate="true" value="<s:date name='grantDate' format="yyyy-MM-dd"/>" /></td>
			<td style="text-align: center;">颁发数量</td>
			<td colspan="2"><input id="grantNumber" name="grantNumber" value="${grantNumber}" class="{number:true,messages:{number:'请输入有效数字!'}}"/></td>
		</tr>
		<tr>
			<td style="text-align: center;">测试员</td>
			<td colspan="4"><input id="testEngineer" name="testEngineer" hiddenInputId="testEngineerLogin" style="float: left;" isUser="true" value="${testEngineer}"/>
			<input type="hidden" id="testEngineerLogin" name="testEngineerLogin" value="${testEngineerLogin}"></td>
		</tr>
		<tr>
			<td  style="text-align: center;">样品归还</td>
			<td colspan="2" style="padding:0px;" id="checkItemsParent">
				<div style="overflow-x:auto;overflow-y:hidden;">
					<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
						<tr >
							<td style="width:10px;text-align:center;border-top:0px;border-left:0px;">序号</td>	
							<td style="width:10px;text-align:center;border-top:0px;">归还日期</td>
							<td style="width:10px;text-align:center;border-top:0px;">归还数量</td>
						</tr>
						<s:iterator value="_sampleSublists" id="item" var="item">
							<tr class="sampleSublists" zbtr1=true> 
								<td style="text-align:center;">
									<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
									<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
<!-- 									<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除"> -->
<!-- 									<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a> -->
								</td>					
								<td style="text-align: center;"><input style="width:100%;" name="returnDate" isDate="true" value="<s:date name='returnDate' format="yyyy-MM-dd"/>" /></td>
								<td style="text-align: center;"><input style="width:90%;" name="returnQuantity" value="${returnQuantity}" onchange="setweigh(this)" class="{number:true,messages:{number:'请输入有效数字!'}}"/></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</td>
			<td style="text-align: center;">归还总数</td>
			<td><input id="sampleSum" name="sampleSum" style="float: left;"  value="${sampleSum}" readonly="turen"></td>
			<td><input type="hidden" id="sampletype" name="sampletype"  value="${sampletype}"></td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align: center;">样品管理人确认</td>
			<td colspan="5"><textarea name="remark2" rows="2" >${remark2}</textarea></td>
		</tr>
		<tr>
		<td>样品管理员:</td>
			<td><input name="specimenAdmin1" value="${specimenAdmin}" disabled="disabled"/>
			<td colspan="3"></td>
		</tr>	
		<tr>
			<td rowspan="2" style="text-align: center;">样品归还确认</td>
			<td colspan="5"><textarea name="remark" rows="2" >${remark}</textarea></td>
		</tr>
		<tr>
		<td>送检人:</td>
			<td colspan="4"><input id="scrapPerson" name="scrapPerson" style="float: left;" isUser="true" value="${inspectionPerson}"></td>
		</tr>	
				
	</table>