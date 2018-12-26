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
		<caption style="font-size: 24px;">实验委托单</caption>
		<input type="hidden" name= "zibiao" id="zibiao" value=""/>
		<input type="hidden" name= "planId" id="planId" value="${planId}"/>
		<input type="hidden" name= "formNo" id="formNo" value="${formNo}"/>
			<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">样品名称</td>
			<td style="width:200px;">
				<input type="text"  style="width:75%;float:left;"   
				name="simpleName" id="simpleName" value="${simpleName}" />
			</td>
			<td style="width:160px;text-align:center;">规格型号</td>
			<td style="width:200px;">
				<input name="specification" id="specification" value="${specification}"></input>
			</td>
			<td style="width:160px;text-align:center;">供应商</td>
			<td style="width:200px;">
				<input  name="supplier" id="supplier" value="${supplier}"></input>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align: center;">机种编号</td>
			<td style="width:200px;">
				<input  name="productNo" id="productNo" value="${productNo}" onclick="checkBomClick(this);" ></input>
			</td>
			<td style="width:160px;text-align:center;">客户</td>
			<td style="width:200px;">
				<input type="text" name="customer" id="customer" value="${customer}" readonly="readonly" onclick="customerClick(this);" ></input> 
				<input type="hidden" name="customerNo" id="customerNo" value="${customerNo}" ></input>
			</td>			
			<td style="width:160px;text-align:center; ">批号</td>
			<td style="width:200px;">
				<input  type="text"  id="lotNo" name="lotNo" value="${lotNo}" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">紧急程度</td>
			<td style="width:200px;">
				<input  type="radio" id="d1" name="emergencyDegree" value="急" checked="checked" <s:if test='%{emergencyDegree=="急"}'>checked="true"</s:if> title="急"/><label for="d1">急</label>
				<input  type="radio" id="d2" name="emergencyDegree" value="正常"  <s:if test='%{emergencyDegree=="正常"}'>checked="true"</s:if> title="正常"/><label for="d2">正常</label>
			</td>
			<td style="width:160px;text-align:center; ">样品数量</td>
			<td style="width:200px;">
				<input name="simpleAmount" id="simpleAmount" value="${simpleAmount}"></input>
			</td>
			<td style="width:160px;text-align: center;">申请日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="consignableDate" name="consignableDate" value="<s:date name='consignableDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center; ">申请人</td>
			<td style="width:200px;">
				<input type="text" id="consignor" isTemp="true" isUser="true" hiddenInputId="consignorLogin" style="float: left;"  name="consignor" value="${consignor}" />
				<input type="hidden" id="consignorLogin" name="consignorLogin" value="${consignorLogin}" />
			</td>
			<td style="width:160px;text-align:center;">申请部门</td>
			<td style="width:200px;">
				<input name="department" id="department" value="${department}" isTemp="true"></input>
			</td>			
			<td style="width:160px;text-align:center; "></td>
			<td style="width:200px;">
				<%-- <input name="enterpriseGroup" id="enterpriseGroup" value="${enterpriseGroup}"  onchange="judgeValue(this);"/> --%>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center">样品描述</td>
			<td colspan="5">
				<textarea rows="2"   id="simpleDiscription" name="simpleDiscription"  >${simpleDiscription}</textarea>
			</td>
		</tr>
		<tr>
			<td  rowspan="2" style="width:160px;text-align:center">测试项目</td>
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
					name="testHsf" value="testHsf">
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
<%-- 		<tr>
			<td colspan="6" style="padding:0px;" id="testItemsParent">
				<div 	style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px;">
						<jsp:include page="test-items.jsp" />
				</div>
			</td>
		</tr> --%>
		<tr>
			<td colspan="6" style="padding: 0px;" >
				<table class="form-table-border-left" style="border: 0px; width: 100%;">
				 <tr>
					 <td style="text-align:center;width:8%;">操作</td>
					 <td style="text-align:center;width:20%;">ORT测试项目</td>
					 <td style="text-align:center;width:20%;">测试条件</td>
					 <td style="text-align:center;width:20%;">判定标准</td>
					 <td style="text-align:center;width:10%;">数量</td>
					 <td style="text-align:center;width:12%;">结果</td>
				 </tr>
			 <s:iterator value="_testItems" var="items" id="items" status="ss">
				 <tr class="ortTestItems" >
					 <td style="text-align:center;">
						<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
						<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
						<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
						<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
					</td> 
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:98%;"  name="testItem"  value="${testItem}"  readonly="readonly" onclick="inspectionItemClick(this);"/>		
					</td>										
					<td style="background:#E8F2FE;border-top:0px;text-align: center;">
						<input style="width:98%;" type="text"  name="testCondition"  value="${testCondition}"   readonly="readonly" onclick="inspectionItemClick(this);"/>
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:98%;"  name="judgeStandard"  value="${judgeStandard}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:98%;"  name="value" class="{number:true,messages:{number:'必须为数字'}}" value="${value}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<%-- <input style="width:98%;"    name="testResult" value="${testResult}" /> --%>
						<s:select list="conclusions" 
						  listKey="value" 
						  listValue="value"
						  theme="simple"
						  cssStyle="{required:true}"
						  emptyOption="true"
						  name="testResult"
						 ></s:select>
					</td>
				</tr>
			</s:iterator>	
			</table>
			</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center">测试目的</td>
			<td colspan="5">
				<textarea rows="2"   id="purpose" name="purpose"  >${purpose}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment" name="attachment" value="${attachment}"/>
			</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center; ">实验员</td>
			<td style="width:200px;">
				<input type="text" id="operator" isTemp="true" isUser="true" hiddenInputId="operatorLogin" style="float: left;"  name="operator" value="${operator}" />
				<input type="hidden" id="operatorLogin" name="operatorLogin" value="${operatorLogin}" />
			</td>
			<td style="width:160px;text-align:center;">测试结果</td>
			<td style="width:200px;">
				<input  type="radio" id="d3" name="testResult" value="PASS" checked="checked" <s:if test='%{testResult=="PASS"}'>checked="true"</s:if> title="PASS"/><label for="d3">PASS</label>
				<input  type="radio" id="d4" name="testResult" value="NG"  <s:if test='%{testResult=="NG"}'>checked="true"</s:if> title="NG"/><label for="d4">NG</label>
			</td>			
			<td style="width:160px;text-align:center; ">报告编号</td>
			<td style="width:200px;">
				<input name="reportNo" id="reportNo" value="${reportNo}"></input>
			</td>
		</tr>
		<tr >
			<td style="width:160px;text-align:center" rowspan="2">主管审核</td>
			<td colspan="5">
				<textarea rows="2"   id="directorComment" name="directorComment"  >${directorComment}</textarea>
			</td>
		</tr>	
		<tr >
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center" rowspan="2">审核人</td>
			<td >
				<input type="text" id="testDirector" isTemp="true" isUser="true" hiddenInputId="testDirectorLogin" style="float: left;"  name="testDirector" value="${testDirector}" />
				<input type="hidden" id="testDirectorLogin" name="testDirectorLogin" value="${testDirectorLogin}" />
			</td>
			<td style="width:160px;text-align: center;">审核日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="shenheDate" name="shenheDate" value="<s:date name='shenheDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
	</table>