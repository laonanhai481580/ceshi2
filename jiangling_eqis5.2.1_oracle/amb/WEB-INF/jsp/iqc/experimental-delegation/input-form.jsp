<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<h3 style="text-align: center;font-size: 25px;font-weight: bold;">实验委托单<br/>Relaibility Evaluation Request Form</h3>
<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
	<caption style="text-align: right;font-weight: bold;">编码:${experimentalNo}</caption>
	
	<tr>
		<td class="tableTd" style="text-align: center;width: 15%;"><b>样品名称</b><br/>sample</td>
		<td style="width: 15%;">
			<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
			<input id="sampleName" name="sampleName" value="${sampleName}"/>
		</td>
		<td style="text-align: center;width: 15%;"><b>规格型号</b><br/>specification</td>
		<td style="width: 20%;">
			<input id="specificationModel" name="specificationModel" value="${specificationModel}"/>
		</td>
		<td style="text-align: center;width: 15%;"><b>供应商</b><br/>supplier</td>
		<td style="width: 20%;">
			<input type="hidden" id="supplierCode" name="supplierCode" value="${supplierCode}"/>
			<input id="supplierName" name="supplierName" value="${supplierName}"/>
		</td>
	</tr>
	<tr>
		<td class="tableTd"><b>机种编号</b><br/>Product NO.</td>
		<td>
			<input id="meachineNo" name="meachineNo" value="${meachineNo}"/>
		</td>
		<td class="tableTd"><b>客户编号</b><br/>sample amount</td>
		<td>
			<input id="coustomerCode" name="coustomerCode" value="${coustomerCode}"/>
			<input type="hidden" id="coustomerName" name="coustomerName" value="${coustomerName}"/>
		</td>
		<td class="tableTd"><b>批号</b><br/>Consignable date</td>
		<td>
			<input id="batchNo" name="batchNo" value="${batchNo}"/>
		</td>
	</tr>
	<tr>
		<td class="tableTd"><b>紧急度</b><br/>Emergency degree</td>
		<td>
			<s:iterator value="emergency_degree" var="option">
				<input type="radio" id="${option.id}" name="emergencyDegree" value="${option.name}" <s:if test="%{#option.value.equals(emergencyDegree)}">checked="checked"</s:if>/>
				<label for="${option.id}">${option.name}</label>
			</s:iterator>
		</td>
		<td class="tableTd"><b>样品数量</b><br/>sample amount</td>
		<td>
			<input id="sampleAmount" name="sampleAmount" value="${sampleAmount}" class="{required:true, messages:{required:'必填'}}"/>
		</td>
		<td class="tableTd"><b>申请日期</b><br/>Consignable date</td>
		<td>
			<input id="consignableDate" name="consignableDate" value='<s:date name="consignableDate" format="yyyy-MM-dd HH:mm"/>'/>
		</td>
	</tr>
	<tr>
		<td class="tableTd"><b>申请人</b><br/> Consignor</td>
		<td>
			<input id="consignor" name="consignor" value="${consignor}" readonly="readonly"/>
		</td>
		<td class="tableTd"><b>申请部门</b><br/> Department</td>
		<td >
			<input id="consignDev" name="consignDev" value="${consignDev}" readonly="readonly"/>
		</td>
		<td class="tableTd"></td>
		<td >

		</td> 
	</tr>
	<tr>
		<td class="tableTd"><b>样品描述</b><br/> Sample Discription</td>
		<td colspan="5">
			<textarea rows="3" cols="2" name="sampleDiscription">${sampleDiscription}</textarea>
		</td>
	</tr>
<%-- 	<tr>
		<td class="tableTd" rowspan="2"><b>测试项目</b><br/>Test Items</td>
		<td class="tableTd" name="hsf"><b><input type="radio" id="hsfOrt" name="hsfOrt" onclick="updateDisabledStauts(this);" value="hsf" <s:if test="%{hsfOrt.equals(hsf)}">checked="checked"</s:if>/>HSF</b></td>
		<td colspan="4">
			<ul>
			<%int a=1; %>
			<s:iterator value="hsfDetailItem" var="option">
				<li>
					<c:set var="setA" value="<%=a %>" scope="page"></c:set>
					<c:set var="S" value="<%="String"+a%>"></c:set>
					<input type="checkbox" id="${option.id}"  name="hsf" value="${option.name}" <s:if test="%{hsf.indexOf(#option.value)>=0}">checked="checked"</s:if>/>
					<label for="${option.id}" style="font-size: 14px;">${option.name}</label>&nbsp;&nbsp;
					<input type="hidden" name="file<%=a %>" 
					uploadBtnText="${option.name}上传附件" id="file<%=a %>" isFile="true" value="%{#experimentalDelegation.file[setA]}"/>
					
					<hr style="width:100%;" color="#8dc0e7"/>
				</li>
				<%a++; %>
			</s:iterator>
			</ul>
		</td>
	</tr> --%>
			<tr>
			<td  rowspan="2" style="width:160px;text-align:center"><b>测试项目</b><br/>Test Items</td>
			<td colspan="3" style="text-align:center">
				<input  type="radio" id="a1" name="testProject" value="HSF"  <s:if test='%{testProject=="HSF"}' >checked="true"</s:if> title="HSF"   onchange="radioChange(this);"/><label for="a1">HSF</label>
			</td>
			<td style="text-align:center">
				<input  type="radio" id="a2" name="testProject" value="ORT"  <s:if test='%{testProject=="ORT"}'  >checked="true"</s:if> title="ORT"   onchange="radioChange(this);"/><label for="a2">ORT</label>
			</td>
			<td style="text-align:center">	
				<input  type="radio" id="a3" name="testProject" value="其他"  <s:if test='%{testProject=="其他"}'  >checked="true"</s:if> title="其他"  onchange="radioChange(this);"/><label for="a3">其他</label>
			</td>
		</tr>
		<tr>
			<td colspan="3" style="text-align:center;" >
				<%-- <s:checkboxlist
					theme="simple" list="testHsfs" listKey="value" listValue="name" 
					name="testHsf" value="testHsf"  >
				</s:checkboxlist> --%>
				<s:checkboxlist cssStyle="margin-left:8px;" name="testHsf" id="testHsf" value="#request.testHsfList" theme="simple" list="testHsfs" listKey="value" listValue="name"/>
			</td>
			<td style="text-align:center">
				<input name="testOrt" id="testOrt" value="${testOrt}"></input>
			</td>
			<td style="text-align:center">
				<input name="testOther" id="testOther" value="${testOther}"></input>
			</td>
		</tr>
	<tr>
		<!-- <td class="tableTd" name="ort"><b><input type="radio" id="ort" name="hsfOrt" onclick="updateDisabledStauts(this);" value="ort" <s:if test="%{hsfOrt.equals(ort)}">checked="checked"</s:if>/>ORT</b></td> -->
		<td colspan="6">
			<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td class="tableTd"><b>操作</b></td>
					<td class="tableTd"><b>ORT测试项目</b></td>
					<td class="tableTd"><b>测试结果</b></td>
				</tr>
				<s:iterator value="_ortItems">
				<tr tableName="ortItems" class="ortItems" zbtr=true>
					<td class="tableTd">
						<div style="margin:0 auto;width: 42px;">
			      			<a class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加项目"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除项目"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						</div>
					</td>
					<td class="tableTd">
						<input id="ortItemName" fieldName="ortItemName" name="ortItemName" value="${ortItemName}"/>
					</td>
					<td class="tableTd">
						<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="ortResult"
							  emptyOption="true"></s:select>
					</td>
				</tr>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tableTd"><b>实验目的</b>Purpose</td>
		<td class="tableTd" colspan="5">
			<textarea rows="3" cols="3" name="purpose">${purpose}</textarea>
		</td>
	</tr>
	<tr>
		<td class="tableTd"><b>实验结果</b>Test Result</td>
		<td>
			<s:select list="iqc_okorng" 
				  theme="simple"
			 	 listKey="name" 
				  listValue="name" 
				  labelSeparator=""
				  name="experimentalResult"
				  emptyOption="false"></s:select>
		</td>
		<td class="tableTd"><b>实验员</b>Operator</td>
		<td>
			<input style="float:left;" id="experimentalMan" name="experimentalMan" value="${experimentalMan}" isUser="true" hiddenInputId="experimentalManLogin" readonly="readonly"/>
			<input type="hidden" id="experimentalManLogin" name="experimentalManLogin" value="${experimentalManLogin}"/>
		</td>
		<td class="tableTd"><b>测试报告编号</b>Report NO.:</td>
		<td class="tableTd">
			<input id="reportFormNo" name="reportFormNo" value="${reportFormNo}"/>
		</td>
	</tr>
	<tr>
		<td class="tableTd" rowspan="2"><b>实验室主管审核</b></td>
		<td colspan="5">
			<textarea rows="4" name="auditText" >${auditText}</textarea>
		</td>
	</tr>
	<tr>
		<td ></td>
		<td class="tableTd" ><b>审核人</b></td>
		<td>
			<input style="float:left;" id="auditMan" name="auditMan" value="${auditMan}" isUser="true" hiddenInputId="auditManLogin" readonly="readonly"/>
			<input type="hidden" id="auditManLogin" name="auditManLogin" value="${auditManLogin}"/>
		</td>
		<td class="tableTd"><b>审核日期</b></td>
		<td>
			<input id="auditTime" name="auditTime" value='<s:date name="auditTime" format="yyyy-MM-dd"/>'/>
		</td>
	</tr>
</table>
