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
		<caption style="font-size: 24px;">仪器设备內校报告</caption>
		<input type="hidden" name= "zibiao1" id="zibiao1" value=""/>
		<input type="hidden" name= "zibiao2" id="zibiao2" value=""/>
		<input type="hidden" name="id" id="id" value="${id}" />
			<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">校验设备相关信息</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">管理编号</td>
			<td style="width:200px;">
				<input type="text"    name="managementNo" id="managementNo" value="${managementNo}" />
			</td>
			<td style="width:160px;text-align:center;">仪器名称</td>
			<td style="width:200px;">
				<input name="measurementName" id="measurementName" value="${measurementName}" onchange="judgeValue(this);"></input>
				<input type="hidden" name="frequency" id="frequency" value="${frequency}" ></input>
			</td>
			<td style="width:160px;text-align:center;">规格型号</td>
			<td style="width:200px;">
				<input  name="measurementSpecification" id="measurementSpecification" value="${measurementSpecification}" onchange="judgeValue(this);"></input>
			</td>
		</tr>

		<tr>
			<td style="width:160px;text-align:center;">使用部门<span style="color:red">*</span></td>
			<td style="width:200px;">
				<input  name="departMent" id="departMent" value="${departMent}" class="{required:true,messages:{required:'必填'}}"></input>
			</td>
			<td style="width:160px;text-align:center; ">安装地点</td>
			<td style="width:200px;">
				<input name="installPlace" id="installPlace" value="${installPlace}"></input>
			</td>
			<td style="width:160px;text-align: center;">制造厂商</td>
			<td style="width:200px;">
				<input  type="text"  id="manufacturer" name="manufacturer" value="${manufacturer}" onchange="judgeValue(this);"/>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align: center;">责任人</td>
			<td>
<%-- 				<input type="hidden" id="dutyLoginMan" name="dutyLoginMan" value="${dutyLoginMan}" /> --%>
<%-- 				<input id="dutyMan" hiddenInputId="dutyLoginMan" name="dutyMan" isUser="true"  style="float: left;" value="${dutyMan}" /> --%>

				<input type="hidden" id="dutyLoginMan" name="dutyLoginMan" value="${dutyLoginMan}" /> 
				<input  name="dutyMan" id="dutyMan" value="${dutyMan}"  
					onclick="checkManClick('责任人','dutyMan','dutyLoginMan',false,'loginName')"  ></input>
			</td>
			<td style="width:160px;text-align: center;">抄送</td>
			<td>
				<input style="float:left;width:200px" name="copyMan" id="copyMan" value="${copyMan}" />
				<input style="float:left;" type='hidden' name="copyLoginMan" id="copyLoginMan" value="${copyLoginMan}" />
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('copyMan','copyLoginMan');"><span class="ui-icon ui-icon-search"  ></span></a>
				<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('copyMan','copyLoginMan')" href="javascript:void(0);" title="<s:text name='清空'/>">
				<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
			</td>
			</td>
		</tr>
		<%-- 
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">2.校验内容</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center; ">2.1仪器设备外观</td>
			<td colspan="5">
				<input  type="radio" id="d1" name="measurementAppearance" value="合格" checked="checked" <s:if test='%{measurementAppearance=="合格"}'>checked="true"</s:if> title="合格"/><label for="d1">合格</label>
				<input  type="radio" id="d2" name="measurementAppearance" value="不合格"  <s:if test='%{measurementAppearance=="不合格"}'>checked="true"</s:if> title="不合格"/><label for="d2">不合格</label>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center">具体情况说明</td>
			<td colspan="5">
				<textarea rows="2"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center; ">2.2示值误差</td>
			<td colspan="5">
				<input  type="radio" id="d3" name="indicationError" value="合格" checked="checked" <s:if test='%{indicationError=="合格"}'>checked="true"</s:if> title="合格"/><label for="d3">合格</label>
				<input  type="radio" id="d4" name="indicationError" value="不合格"  <s:if test='%{indicationError=="不合格"}'>checked="true"</s:if> title="不合格"/><label for="d4">不合格</label>
			</td>
		</tr>
 		<tr>
			<td colspan="6" style="padding:0px;" id=checkReportDetails>
				<div 	style="overflow-x:auto;overflow-y:hidden;">
						<jsp:include page="test-items.jsp" />
				</div>
			</td>
		</tr> 
		<%-- 
		<tr>
			<td colspan="6" style="padding: 0px;" >
			<div style="overflow-x: scroll; overflow-y: hidden; overflow: auto; ">
			<table class="form-table-border-left" style="border: 0px; table-layout: fixed;">
			<tr>
				<td style="text-align:center;width:8%;">操作</td>
				<td	style="text-align: center;">校准项目
		 		<td	style="text-align: center;">标准值</td> 
				<td	style="text-align: center;">指示值</td>
				<td	style="text-align: center;">误差值</td>
				<td	style="text-align: center;">允许误差</td>
				<td	style="text-align: center;">Pass Or Fail</td>
				<td	style="text-align: center;">备注</td>
			</tr>
			 <s:iterator value="_checkReportDetails" var="item" id="item" status="ss">
				 <tr class="checkReportDetails" zbtr1=true>
					 <td style="text-align:center;">
						<a id="aBtn" class="small-button-bg" addBtn="true" zba=true style="margin-left:2px;" onclick="addRowHtml(this)" href="#"  title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
						<a id="aBtn" class="small-button-bg" delBtn="true" zba=true style="margin-left:2px;" onclick="removeRowHtml(this)" href="#" title="删除"><span  class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					</td> 
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="itemName"  value="${itemName}" onclick="checkDetailClick(this);"/>				
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="standardValue"  value="${standardValue}" onclick="checkDetailClick(this);"/>
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="indicatedValue" value="${indicatedValue}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="errorValue"  value="${errorValue}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="allowableError"  value="${allowableError}" onclick="checkDetailClick(this);"/>				
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
				    	<s:select list="passOrFails"
						listKey="value" 
						listValue="value" 
						name="passOrFail" 
						id="passOrFail" 
						cssStyle="width:180px;"
						onchange=""
						theme="simple">
					</s:select>				
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="remark"  value="${remark}" />				
					</td>															
				</tr>
			</s:iterator>			
			</table></div>
			</td>
		</tr>
		--%>
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">校验结果</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align:center;">
				<input  type="radio" id="r1" name="checkResult" value="合格" checked="checked" <s:if test='%{checkResult=="合格"}'>checked="true"</s:if> title="合格"/><label for="r1">合格</label>
				<input  type="radio" id="r2" name="checkResult" value="限制使用"  <s:if test='%{checkResult=="限制使用"}'>checked="true"</s:if> title="限制使用"/><label for="r2">限制使用</label>
				<input  type="radio" id="r3" name="checkResult" value="不合格"  <s:if test='%{checkResult=="不合格"}'>checked="true"</s:if> title="不合格"/><label for="r3">不合格</label>
			</td>
			<td style="width:160px;text-align:center; ">校验附件</td>
			<td><input style="width:100%;" type="hidden" isFile="true" name="remark1" value="${remark1 }" />
			<td colspan="2"></td>
		</tr>
		<%-- 
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">4.校验依据</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center; ">校验依据文件</td>
			<td colspan="5">
				<textarea rows="2"   id="checkBasis" name="checkBasis"  >${checkBasis}</textarea>
			</td>
		</tr>
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">5.校验所使用的标准件</td>
		</tr>	
		<tr>
			<td colspan="6" style="padding: 0px;" >
			<div style="overflow-x: scroll; overflow-y: hidden; overflow: auto; ">
				<table class="form-table-border-left" style="border: 0px; table-layout: fixed;">
 				 <tr>
					 <td style="text-align:center;width:8%;">操作</td>
					 <td style="text-align:center;width:20%;">标准件名称</td>
					 <td style="text-align:center;width:20%;">标准件编号</td>
					 <td style="text-align:center;width:20%;">有效期至</td>
					 <td style="text-align:center;">证书编号</td>
				 </tr>
			 <s:iterator value="_checkReportItems" var="item" id="item" status="ss">
				 <tr class="checkReportItems" zbtr1=true>
					 <td style="text-align:center;">
						<a id="aBtn" class="small-button-bg" addBtn="true" zba=true style="margin-left:2px;" onclick="addRowHtml(this)" href="#"  title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
						<a id="aBtn" class="small-button-bg" delBtn="true" zba=true style="margin-left:2px;" onclick="removeRowHtml(this)" href="#" title="删除"><span  class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					</td> 
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="standardName"  value="${standardName}" onclick="standardItemClick(this);"/>				
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="standardNo"  value="${standardNo}" onclick="standardItemClick(this);"/>
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  isDate=true type="text"   name="validityDate" value="<s:date name='validityDate' format="yyyy-MM-dd"/>" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="certificateNo"  value="${certificateNo}" />
					</td>
				</tr>
			</s:iterator>	
			</table></div>
			</td>
		</tr>
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">6.校验条件</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">温度</td>
			<td style="width:200px;">
				<input  name="temperature" id="temperature" value="${temperature}"></input>℃
			</td>
			<td style="width:160px;text-align:center; ">相对湿度</td>
			<td style="width:200px;">
				<input name="humidity" id="humidity" value="${humidity}"></input>%RH
			</td>
			<td style="width:160px;text-align: center;">地点</td>
			<td style="width:200px;">
				<input  type="text"  id="checkPlace" name="checkPlace" value="${checkPlace}" />
			</td>
		</tr>
		--%>
	
		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">校验信息</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">校验人</td>
			<td style="width:200px;">
				<input type="hidden" id="checkManLogin" name="checkManLogin" value="${checkManLogin}" />			
				<input  name="checkMan" id="checkMan" value="${checkMan}"></input>	
			</td>
			<td style="width:160px;text-align:center; ">校验日期</td>
			<td style="width:200px;">
				<input style="width:90%;"  isDate=true type="text" id="checkDate"   name="checkDate" value="<s:date name='checkDate' format="yyyy-MM-dd"/>" onchange="checkDateChange(this);"/>
			</td>
			<td style="width:160px;text-align: center;">建议下次校验日期</td>
			<td style="width:200px;">
				<input style="width:90%;"  isDate=true type="text" id="nextCheckDate"   name="nextCheckDate" value="<s:date name='nextCheckDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center; ">备注</td>
			<td colspan="5">
				<textarea rows="2"   id="remark2" name="remark2"  >${remark2}</textarea>
			</td>
		</tr>

		<tr style="background-color: #6495ED;color: white;font-weight: bold;">
			<td style="width:200px;text-align: left;font-size: 18px;" colspan="6">8.审核</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">审核人</td>
			<td style="width:200px;">
				<%-- <input type="text" id="auditMan" isTemp="true" isUser="true" hiddenInputId="auditManLogin" style="float: left;"  name="auditMan" value="${auditMan}" />--%>
				<input type="hidden" id="auditManLogin" name="auditManLogin" value="${auditManLogin}" /> 
				<input  name="auditMan" id="auditMan" value="${auditMan}"  
					onclick="checkManClick('选择审核人','auditMan','auditManLogin',false,'loginName')"  ></input>
			</td>
			<td style="width:160px;text-align:center; ">审核日期</td>
			<td style="width:200px;">
				<input style="width:90%;"  isDate=true type="text" id="auditDate"   name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align: center;"></td>
			<td style="width:200px;">
			</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center; ">审核意见</td>
			<td colspan="5">
				<textarea rows="2"   id="auditText" name="auditText"  >${auditText}</textarea>
			</td>
		</tr>				
	</table>