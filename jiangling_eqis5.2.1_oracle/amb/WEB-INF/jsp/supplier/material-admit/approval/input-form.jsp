<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.acs.service.AcsUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>材料承认申请</h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
		<input type="hidden" id="str" name="str" value="${str}" />
		<input  type="hidden" id="id" name="id" value="${id}" />
		<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
</table>
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
	<tbody>
		<tr>
			<td rowspan="3" style="text-align:center;width:5%"><h3>研发部发起承认</h3></td>
			<td style="width:16%">规格型号/版本</td>
			<td><input id="productVersion"   name="productVersion" value="${productVersion }"/></td>
			<td style="width:13%">物料名称</td>
			<td><input style="width:80%" id="materialName" name="materialName" value="${materialName }"/></td>
			<td style="width:13%">物料编号</td>
			<td><input style="width:80%" id="materialCode" name="materialCode"  style="float:left;" value="${materialCode }"/>
				<a class="small-button-bg" onclick="testSelect(this)" ><span class="ui-icon ui-icon-search"></span></a>
			</td>
		</tr>
		<tr>
			<td>申请日期</td>
			<td><input id="applyDate" name="applyDate" isDate="true" value="<s:date name='applyDate' format="yyyy-MM-dd"/>" /></td>
			<td>申请人</td>
			<td><input id="applicat" name="applicat" value="${applicat }"/></td>
			<td>申请编号</td>
			<td>${formNo }</td>
		</tr>
		<tr>
			<td>承认供应商</td>
			<td>
			<input style="width:200px;float:left;" id="supplierName" readonly=readonly name="supplierName"  value="${supplierName}"/>
			<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
			</td>
			<td>应用项目</td>
			<td colspan="3">
				<textarea id="item" name="item">${item}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align:center;"><h3>采购部指定供应商</h3></td>
			<td>指定供应商</td>
			<td >
				 <input style="width:200px;float:left;" id="supplier" name="supplier"  hiddenInputId="supplierLoginName" value="${supplierName}" />
	             <input type="hidden" name="supplierLoginName" id="supplierLoginName"  value="${supplierLoginName}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('supplier','supplierLoginName')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>   
			</td>
			<td>供应商邮箱</td>
			<td colspan="3">
				<input id="supplierEmail" name="supplierEmail" value="${supplierEmail}" style="width:60%;"/><span style="color:red;font-size:8px;" >*用于创建供应商QIS账号</span>
			</td>
		</tr>
		<tr>
			<td>采购经办人</td>
			<td>
				<span style="float:left;"> </span><input id="purchaseProcesser" name="purchaseProcesser" isUser="true"style="width:200px;float:left;" hiddenInputId="purchaseProcessLog"  value="${purchaseProcesser}"/>
	        	<input type="hidden"  name="purchaseProcessLog" id="purchaseProcessLog"  value="${purchaseProcessLog}" />
	        </td>
			<td>意见</td>
			<td colspan="3">
			<textarea id="opinionPurchase" name="opinionPurchase">${opinionPurchase}</textarea>
			</td>
		</tr>
		<% if(!"欧菲科技-FPM".equals(ContextUtils.getCompanyName())){%>
		<tr>
			<td rowspan="7" style="text-align:center;"><h3>供应商上传承认资料</h3></td>
			<td>供应商料号</td>
			<td ><input id="vendorNo" name="vendorNo" value="${vendorNo }"/></td>
			<td>生产地址</td>
			<td colspan="3"><input style="width:300px;" id="address" name="address" value="${address }"/></td>
		</tr>
		<tr>
			<td>规格书</td>
			<td >
				<input type="hidden" stage="file" id="specification" isFile="true"  name="specification" value="${specification }"/>
			</td>
			<td>图纸</td>
			<td><input type="hidden" stage="file" id="drawing" isFile="true" name="drawing" value="${drawing }"/></td>
			<td>CPK报告</td>
			<td><input type="hidden" stage="file" id="cpkReport" isFile="true" name="cpkReport" value="${cpkReport }"/></td>
		</tr>
		<tr>
			<td>FAI报告</td>
			<td><input type="hidden" stage="file" id="faiReport" isFile="true" name="faiReport" value="${faiReport }"/></td>
			<td>可靠性报告</td>
			<td><input type="hidden" stage="file" id="raReport" isFile="true" name="raReport" value="${raReport }"/></td>
			<td>QC工程图</td>
			<td><input type="hidden" stage="file" id="qcPlan" isFile="true" name="qcPlan" value="${qcPlan }"/></td>
		</tr>
		<tr>
			<td>性能评估报告</td>
			<td><input type="hidden" stage="file" id="evaluateReport" isFile="true" name="evaluateReport" value="${evaluateReport }"/></td>
			<td>FMEA</td>
			<td><input type="hidden" stage="file" id="fmea" isFile="true" name="fmea" value="${fmea }"/></td>
			<td>产品实物&拆解图</td>
			<td><input type="hidden" stage="file" id="entityPhoto" isFile="true" name="entityPhoto" value="${entityPhoto }"/></td>
		</tr>
		<tr>
			<td>产品成分宣告</td>
			<td>
			<input type="hidden" class="fileCheck" hisId="productDeclare" id="productFileName" value="${productFileName }">
			<input type="hidden" stage="file" id="productDeclare" isFile="true" class="field" name="productDeclare" value="${productDeclare }"/>
			</td>
			<td>测试报告</td>
			<td><input type="hidden" stage="file" id="testReport" isFile="true" name="testReport" value="${testReport }"/></td>
			<td>MSDS</td>
			<td><input type="hidden" stage="file" id="msds" isFile="true" name="msds" value="${msds }"/></td>
		</tr>
		<tr>
			<td>REACH SVHC(调查表)</td>
			<td>
			<input type="hidden" class="fileCheck" hisId="surveyFile" id="surveyFileNmae" value="${surveyFileNmae }">
			<input type="hidden" stage="file" id="surveyFile" isFile="true" class="field" name="surveyFile" value="${surveyFile }"/></td>
			<td>BOM</td>
			<td><input type="hidden" stage="file" id="bomFile" isFile="true" name="bomFile" value="${bomFile }"/></td>
			<td>代码说明</td>
			<td><input type="hidden" stage="file" id="codeExplain" isFile="true" name="codeExplain" value="${codeExplain }"/></td>
		</tr>
		<tr>
			
			<td>包装规格</td>
			<td><input type="hidden" stage="file" id="packing" isFile="true" name="packing" value="${packing }"/></td>
			<td>出货报告</td>
			<td><input type="hidden" stage="file" id="checkoutReport" isFile="true" name="checkoutReport" value="${checkoutReport }"/></td>
			<td>其他</td>
			<td>
			<input type="hidden" id="others" stage="file" isFile="true" name="others" value="${others }"/></td>
		</tr>
		<tr>
			
			<td colspan="4"></td>
		</tr>
		<% }else{%>
		<tr>
			<td style="text-align:center;"><h3>供应商上传材料承认</h3></td>
			<td>承认书</td>
			<td colspan="5"><input type="hidden" id="materialBook" isFile="true" name="materialBook" value="${materialBook }"/></td>
		</tr>
		<% } %>
		<tr><td colspan="7" style="text-align:center;"><h3>会签<h3></td></tr>
		<tr>
			<td></td>	
			<td>部品</td>
			<td>结论</td>
			<td colspan="2">签核意见</td>
			<td colspan="2">上传附件</td>
		</tr>
		<tr >
		 	<input  type="hidden" name="checkDeptMansLog" id="checkDeptMansLog" value="${checkDeptMansLog }"/>
			<td>RD核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="rdCheckerLog"  isUser="true" id="rdChecker" name="rdChecker"  value="${rdChecker}"/>
	        	<input type="hidden"  name="rdCheckerLog" id="rdCheckerLog"  value="${rdCheckerLog}" />
              </td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="rdStatus" 
					id="rdStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignRD"  name="countersignRD" value="${countersignRD }"/>
			</td>
			<td colspan="2">
				<s:if test="consignorDept!='供应商'">
					<input type="hidden" id="fileRD" isFile="true" name="fileRD" value="${fileRD }"/>
				</s:if>
			</td>
		</tr>
		<tr id="checkerLog">
			<td>PM核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="pmCheckerLog"  isUser="true" id="pmChecker" name="pmChecker" value="${pmChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="pmCheckerLog" id="pmCheckerLog"  value="${pmCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmChecker','pmCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="pmStatus" 
					id="pmStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignPM"  name="countersignPM" value="${countersignPM }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
			<td>SQE核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="sqeCheckerLog"  isUser="true" id="sqeChecker" name="sqeChecker" value="${sqeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="sqeStatus" 
					id="sqeStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignSQE"  name="countersignSQE" value="${countersignSQE }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>QS核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="qsCheckerLog"  isUser="true" id="qsChecker" name="qsChecker" value="${qsChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="qsCheckerLog" id="qsCheckerLog"  value="${qsCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qsChecker','qsCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="qsStatus" 
					id="qsStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignQS"  name="countersignQS" value="${countersignQS }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<% if("欧菲科技-FPM".equals(ContextUtils.getCompanyName())){ %>
		<tr id="checkerLog">
		<td >NPI核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="npiCheckerLog"  isUser="true" id="npiChecker" name="npiChecker" value="${npiChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="npiCheckerLog" id="npiCheckerLog"  value="${npiCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('npiChecker','npiCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="npiStatus" 
					id="npiStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignNpi"  name="countersignNpi" value="${countersignNpi }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>DQE核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="dqeCheckerLog"  isUser="true" id="dqeChecker" name="dqeChecker" value="${dqeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="dqeCheckerLog" id="dqeCheckerLog"  value="${dqeCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('dqeChecker','dqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="dqeStatus" 
					id="dqeStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignDqe"  name="countersignDqe" value="${countersignDqe }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>工程核准:</td>
			<td>
			<input style="float:left;" hiddenInputId="projectCheckerLog"  isUser="true" id="projectChecker" name="projectChecker" value="${projectChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="projectCheckerLog" id="projectCheckerLog"  value="${projectCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('projectChecker','projectCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="projectStatus" 
					id="projectStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<input  id="countersignProject"  name="countersignProject" value="${countersignProject }"/>
			</td>
			<td colspan="2"></td>
		</tr>
		<% } %>
		<tr>
			<td style="text-align:center;"><h3>文控归档</h3></td>
			<td>
				<span style="float:left;"> </span><input id="docControl" name="docControl" isUser="true"style="width:200px;float:left;" hiddenInputId="docControlLoging"  value="${docControl}"/>
	        	<input type="hidden"  name="docControlLoging" id="docControlLoging"  value="${docControlLoging}" />
			</td>
			<td>文控意见</td>
	         <td colspan="2"><textarea id="opiniondoc" name="opiniondoc">${opiniondoc }</textarea></td>
	         <td colspan="2">
	            <span style="float:left;">抄送人： </span><input id="copyMan" name="copyMan" multiple=true isUser="true"style="float:left;" hiddenInputId="copyManLogin"  value="${copyMan}"/>
	             <input type="hidden"  name="copyManLogin" id="copyManLogin"  value="${copyManLogin}" />
	         </td>
		</tr>
	</tbody>
</table>
