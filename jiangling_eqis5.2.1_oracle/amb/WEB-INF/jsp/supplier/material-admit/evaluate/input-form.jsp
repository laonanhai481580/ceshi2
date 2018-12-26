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
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-tabl">
	<tbody>
		<security:authorize ifAnyGranted="supplier-improve-conceal">
	    <tr>
	         <td style="width:10%;">事业群</td>
	         <td style="width:17%;"><input id="businessUnitName" name="businessUnitName" value="${businessUnitName}"/></td>
	         <td style="width:15%;">紧急程度</td>
	         <td colspan="2">
	             <s:select list="urgencyLevels" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="degreeLevel"
				  id="degreeLevel"
				  emptyOption="false"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	           </td>
	         <td style="width:13%;">产品型号</td>
	         <td><input id="productType" name="productType" value="${productType }"/></td>
	     </tr>
	    <tr>
	          <td>物料编号</td>
	         <td><input id="materialCode" style="float:left;" isMaterial="true"  showInputId="materialCode" hiddenInputId="materialName" name="materialCode" value="${materialCode }"/></td>
	         <td>物料名称</td>
	         <td colspan="2"> <input id="materialName" style="width:98%;"   name="materialName" value="${materialName}"/></td>
	         <td>物料类别</td>
	         <td ><input id="materialType" name="materialType" value="${materialType}"/></td>
	     </tr>
	    <tr>
	         <td>供应商</td>
	         <td>
	           <input id="supplier" style="float:left;" name="supplier" value="${supplier}"/>
	            <input id="supplierCode"  type="hidden" style="float:left;" name="supplierCode" value="${supplierCode}"/>
	         <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
	         </td>
	         <td>是否合格供应商</td>
	         <td colspan="2">
	           <s:select list="labIfTests" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="qualifiedSupplier"
				  id="qualifiedSupplier"
				  emptyOption="false"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	          </td>
	         <td>送检日期</td>
	         <td><input id="sendInspectionDate" name="sendInspectionDate" isDate="true" value="<s:date name='sendInspectionDate' format="yyyy-MM-dd"/>"  /></td>
	     </tr>
	     <tr>
	         <td>送样次数</td>
	         <td><input id="sendInspectionTimes" class="{number:true,messages:{number:'请输入有效数字!'}}" name="sendInspectionTimes" value="${sendInspectionTimes}"/></td>
	         <td >样品数量</td>
	         <td colspan="2"><input id="materialAmount" class="{number:true,messages:{number:'请输入有效数字!'}}" name="materialAmount" value="${materialAmount}"/></td>
	         <td>供应商邮箱</td>
	         <td><input name="supplierEmail" id="supplierEmail" value="${supplierEmail }"/></td>
	     </tr>
	     <tr>
	         <td rowspan="2">送样原因</td>
	         <td colspan="6"><s:checkboxlist cssStyle="margin-left:8px;" name="sendReason" id="sendReason" value="#request.sendReasonList" theme="simple" list="sendReasons" listKey="value" listValue="name"/></td>
	     </tr>
	      <tr>
	         <td>其他原因</td>
	         <td colspan="5"><input name="otherReason" id="otherReason" value="${otherReason }"/></td>
	     </tr>
	      <tr>
	         <td rowspan="2">厂商样件评估</td>
	         <td colspan="6"><s:checkboxlist cssStyle="margin-left:8px;" name="materialEvaluate" id="materialEvaluate" value="#request.materialEvaluateList" theme="simple" list="evaluates" listKey="value" listValue="name"/></td>
	     </tr>
	      <tr>
	         <td>其他</td>
	         <td colspan="3"><input name="otherEvaluate" id="otherEvaluate" value="${otherEvaluate }"/></td>
	         <td>测试报告编号</td>
	         <td><input name="testReportNum" id="testReportNum" value="${testReportNum }"/></td>
	     </tr>
         <tr>
	         <td>附件</td>
	         <td colspan="6"><input type="hidden" isFile="true" name="fileName" id="fileName" value="${fileName }"></td>
	     </tr>
	      <tr>
	         <td>样品正确性</td>
	         <td>
	              <s:select list="labIfTests" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="materialValidity"
				  id="materialValidity"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	         <td>样品使用紧急情况</td>
	         <td colspan="2">
	         <s:select list="materialUseinfos" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="materialUseInfo"
				  id="materialUseInfo"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	         <td>填表人</td>
	         <td><input id="creatorName" name="creatorName" disabled="true " readOnly="true" value="${creatorName}"/></td>
	     </tr>
	     <tr>
	         <td>部门</td>
	         <td><input id="dept" name="dept" value="${dept}"/></td>
	         <td>审核</td>
	         <td colspan="2">
	         <input  style="float:left;" hiddenInputId="reportCheckerLog" isUser="true"  id="reportChecker" name="reportChecker" value="${reportChecker}"/>
	         <input type="hidden" name="reportCheckerLog" id="reportCheckerLog"  value="${reportCheckerLog}" />
	        	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('reportChecker','reportCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td></td>
	         <td></td>
	    <!--   </tr>
	       <tr>
	         <td>RD评估人</td>
	         <td>
	              <input  style="float:left;" hiddenInputId="rdProcesserLog" isUser="true"  id="rdProcesser" name="rdProcesser" value="${rdProcesser}"/>
	         <input type="hidden" name="rdProcesserLog" id="rdProcesserLog"  value="${rdProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('rdProcesser','rdProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>QS办理人</td>
	         <td>
	         <input  style="float:left;" hiddenInputId="hsfProcesserLog" isUser="true"  id="hsfProcesser" name="hsfProcesser" value="${hsfProcesser}"/>
	         <input type="hidden" name="hsfProcesserLog" id="hsfProcesserLog"  value="${hsfProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('hsfProcesser','hsfProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>EHS办理人</td>
	         <td>
	             <input  style="float:left;" hiddenInputId="msdsProcesserLog" isUser="true"  id="msdsProcesser" name="msdsProcesser" value="${msdsProcesser}"/>
	         <input type="hidden" name="msdsProcesserLog" id="msdsProcesserLog"  value="${msdsProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('msdsProcesser','msdsProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr> -->
	     <tr>
 	         <td colspan="5" style="text-align:center">样品综合评估项目，标准提出(由工程部，品保部，品质中心提出)</td> 
	         <td rowspan="2" colspan="2">评估部门</td>
	     </tr> 
	     <tr>
	         <td colspan="3" style="text-align:center">评估项目(勾选项)</td>
	         <td style="width:12%;">评估标准</td>
	         <td>OK或NG</td>
	     </tr>
	     <tr>
	         <td colspan="3" style="text-align:center"> 
	           <input type="checkbox" name="supplierEvaluateProject" id="supplierEvaluateProject" value="厂商样品规格书参数与所需规格要求的符合性"  <s:if test='%{supplierEvaluateProject=="厂商样品规格书参数与所需规格要求的符合性"}'>checked="true"</s:if> /><label for="supplierEvaluateProject1">厂商样品规格书参数与所需规格要求的符合性</label>
	         </td>
	         <td><input id="supplierEvaluateStandar" name="supplierEvaluateStandar" value="	"/></td>
	         <td>
	         <s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="supplierEvaluateResult"
				  id="supplierEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
<%-- 	         <td rowspan="2"><input style="float:left;" id="sampleEvaluateDept" name="sampleEvaluateDept"  value="${sampleEvaluateDept}"/></td> --%>
	        <td rowspan="2">RD评估人</td>
	         <td rowspan="2">
	              <input  style="float:left;" hiddenInputId="rdProcesserLog" isUser="true"  id="rdProcesser" name="rdProcesser" value="${rdProcesser}"/>
	         <input type="hidden" name="rdProcesserLog" id="rdProcesserLog"  value="${rdProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('rdProcesser','rdProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	      <tr>
	         <td>实物样品</td>
	         <td colspan="2"><s:checkboxlist cssStyle="margin-left:8px;" name="sampleEvaluateProject" id="sampleEvaluateProject" value="#request.sampleEvaluateProjectList" theme="simple" list="evaluateProjects" listKey="value" listValue="name"/></td>
	        <td><input id="sampleEvaluateStandar" name="sampleEvaluateStandar" value="${sampleEvaluateStandar}"/></td>
	         <td>
	         <s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="sampleEvaluateResult"
				  id="sampleEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	     </tr>
	     <tr>
	         <td>产品HSF符合性确认</td>
	         <td colspan="2">
<%-- 	             <input id="hsfEvaluateProject" name="hsfEvaluateProject" value="${hsfEvaluateProject}"/> --%>
	              <s:select list="hsfValues" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="hsfEvaluateProject"
				  id="hsfEvaluateProject"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	         <td><input style="float:left;" id="hsfEvaluateStandar" name="hsfEvaluateStandar"  value="${hsfEvaluateStandar}"/></td>
	         
	         <td>
	          <s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="hsfEvaluateResult"
				  id="hsfEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
<%-- 	          <td><input style="float:left;" id="hsfEvaluateDept" name="hsfEvaluateDept" isUser="true" value="${hsfEvaluateDept}"/></td> --%>
	          <td>QS办理人</td>
	          <td>
	         <input  style="float:left;" hiddenInputId="hsfProcesserLog" isUser="true"  id="hsfProcesser" name="hsfProcesser" value="${hsfProcesser}"/>
	         <input type="hidden" name="hsfProcesserLog" id="hsfProcesserLog"  value="${hsfProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('hsfProcesser','hsfProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	      <tr>
	         <td colspan="3" style="text-align:center"><input type="checkbox" name="msdsEvaluateProject" id="msdsEvaluateProject" value="化学品MSDS"  <s:if test='%{msdsEvaluateProject=="化学品MSDS"}'>checked="true"</s:if> /><label for="ifTrust">化学品MSDS</label></td>
	         <td><input id="msdsEvaluateStandar" name="msdsEvaluateStandar" value="${msdsEvaluateStandar}"/></td>
	         <td>
	         <s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="msdsEvaluateResult"
				  id="msdsEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
<%-- 	          <td><input style="float:left;" id="msdsEvaluateDept" name="msdsEvaluateDept" isUser="true" value="${msdsEvaluateDept}"/></td> --%>
	          <td>EHS办理人</td>
	          <td>
	             <input  style="float:left;" hiddenInputId="msdsProcesserLog" isUser="true"  id="msdsProcesser" name="msdsProcesser" value="${msdsProcesser}"/>
	         <input type="hidden" name="msdsProcesserLog" id="msdsProcesserLog"  value="${msdsProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('msdsProcesser','msdsProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	     <tr>
	        <td rowspan="4" style="text-align:center">入料检验</td>
	        <td colspan="6">检验结果(全尺寸，性能，外观，HRT，等): <s:radio cssStyle="margin-left:8px;" name="inspectionResult" id="inspectionResult" value="inspectionResult" theme="simple" list="inspectionResults" listKey="value" listValue="name"/>(工程试用为准)</td>
	     </tr>
	     <tr>
	        <td>检验报告</td>
	        <td colspan="5"><input type="hidden" isFile="true" name="inspectionReportFile" id="inspectionReportFile" value="${inspectionReportFile}"></td>
	     </tr>
	      <tr>
	        <td colspan="6">供应商产品出货检验报告(属性，正确性评估): <s:radio cssStyle="margin-left:8px;" name="outInspectionResult" id="outInspectionResult" value="outInspectionResult" theme="simple" list="outInspectionResults" listKey="value" listValue="name"/></td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="2">
	           <input style="float:left;" hiddenInputId="inspectionReporterLog" isUser="true" id="inspectionReporter" name="inspectionReporter" value="${inspectionReporter}"/>
	            <input type="hidden" name="inspectionReporterLog" id="inspectionReporterLog"  value="${inspectionReporterLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('inspectionReporter','inspectionReporterLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	           </td>
	        <td >SQE审核</td>
	        <td colspan="2">
	            <input id="inspectionChecker" style="float:left;" hiddenInputId="inspectionCheckerLog" isUser="true" name="inspectionChecker" value="${inspectionChecker}"/>
	             <input type="hidden" name="inspectionCheckerLog" id="inspectionCheckerLog"  value="${inspectionCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('inspectionChecker','inspectionCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	        </td>
	     </tr>
	     
	      <tr>
	        <td rowspan="4" style="text-align:center">RD</td>
	        <td colspan="6">是否试做: <s:radio cssStyle="margin-left:8px;" name="rdIfTest" id="rdIfTest" value="rdIfTest" theme="simple" list="ifTests" listKey="value" listValue="name"/></td>
	     </tr>
	     <tr>
	        <td>RD报告</td>
	        <td colspan="5"><input type="hidden" isFile="true" name="rdReportFile" id="rdReportFile" value="${rdReportFile}"></td>
	     </tr>
	     <tr>
	        <td>建议与评述</td>
	        <td colspan="5"><textarea rows="5" id="rdOpinion" name="rdOpinion" >${rdOpinion}</textarea></td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="5">
<%-- 	           <input id="rdReporter" style="float:left;" hiddenInputId="rdReporterLog" isUser="true" name="rdReporter" value="${rdReporter}"/> --%>
<%-- 	           <input type="hidden" name="rdReporterLog" id="rdReporterLog"  value="${rdReporterLog}" /> --%>
<%--               <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('rdReporter','rdReporterLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
              <input readonly="readonly"  name='rdReporter' value="${rdProcesser}" />
	         </td>
	     </tr>
	     <tr>
	     	<td rowspan="4" style="text-align:center">NPI</td>
	     	<td colspan="6">是否试做: <s:radio cssStyle="margin-left:8px;" name="npiIfTest" id="npiIfTest" value="npiIfTest" theme="simple" list="ifTests" listKey="value" listValue="name"/></td>
	     </tr>
	      <tr>
	        <td>检验报告</td>
	        <td colspan="5"><input type="hidden" isFile="true" name="npiFile" id="npiFile" value="${npiFile}"></td>
	     </tr>
	      <tr>
	        <td>建议与评述</td>
	        <td colspan="5"><textarea rows="5" id="npiOpinion" name="npiOpinion" >${npiOpinion}</textarea></td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="5">
	          <input style="float:left;" hiddenInputId="npiProcesserLog" isUser="true" id="npiProcesser" name="npiProcesser" value="${npiProcesser}"/>
	            <input type="hidden" name="npiProcesserLog" id="npiProcesserLog"  value="${npiProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('npiProcesser','npiProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	     </security:authorize>
	     
	     <tr>
	     	<td rowspan="4" style="text-align:center">QS</td>
	     	<td colspan="6">是否试做: <s:radio cssStyle="margin-left:8px;" name="qsIfTest" id="qsIfTest" value="qsIfTest" theme="simple" list="ifTests" listKey="value" listValue="name"/></td>
	     </tr>
	      <tr>
	        <td>检验报告</td>
	        <td colspan="5"><input type="hidden" isFile="true" name="qsReportFile" id="qsReportFile" value="${qsReportFile}"></td>
	     </tr>
	      <tr>
	        <td>建议与评述</td>
	        <td colspan="5"><textarea rows="5" id="qsOpinion" name="qsOpinion" >${qsOpinion}</textarea></td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="5">
	           <input readonly="readonly" disabled="true" value="${hsfProcesser}" />
	         </td>
	     </tr>
	     
	    <security:authorize ifAnyGranted="supplier-improve-conceal">
	     <tr>
	        <td rowspan="3" style="text-align:center">LAB</td>
	        <td>是否试验: <s:radio cssStyle="margin-left:8px;" name="labIfTest" id="labIfTest" value="labIfTest" theme="simple" list="labIfTests" listKey="value" listValue="name"/></td>
	        <td colspan="2">试验内容: <s:radio cssStyle="margin-left:8px;" name="labTestContent" id="labTestContent" value="labTestContent" theme="simple" list="testContents" listKey="value" listValue="name"/></td>
	        <td>结果: <s:radio cssStyle="margin-left:8px;" name="labTestResult" id="labTestResult" value="labTestResult" theme="simple" list="labTestResults" listKey="value" listValue="name"/></td>
	        <td>报告编号: </td>
	        <td><input name="labTestNum" id="labTestNum" value="${labTestNum}"/></td>
	     </tr>
	     <tr>
	        <td>测试报告</td>
	        <td colspan="5"><input type="hidden" isFile="true" name="labReportFile" id="labReportFile" value="${labReportFile}"></td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="5">
	            <input id="labReporter" style="float:left;" hiddenInputId="labReporterLog" isUser="true" name="labReporter" value="${labReporter}"/>
	            <input type="hidden" name="labReporterLog" id="labReporterLog"  value="${labReporterLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('labReporter','labReporterLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	        </td>
	     </tr>
	     <tr>
	        <td rowspan="2" style="text-align:center">综合评估</td>
	        <td colspan="6">
            <input type="radio" id="evaluateResult1" name="evaluateResult" value="合格" <s:if test="evaluateResult=='合格'">checked="true"</s:if> title="合格"/><label for="evaluateResult1">合格</label>
            (<s:radio cssStyle="margin-left:8px;" name="evaluateOkInfo" id="evaluateOkInfo" value="evaluateOkInfo" theme="simple" list="okInfos" listKey="value" listValue="name"/>)
			<input  type="radio" id="evaluateResult2" name="evaluateResult" value="不合格" <s:if test="evaluateResult=='不合格'">checked="true"</s:if> title="不合格"/><label for="evaluateResult2">不合格</label>
			(<s:radio cssStyle="margin-left:8px;" name="evaluateNgInfo" id="evaluateNgInfo" value="evaluateNgInfo" theme="simple" list="ngInfos" listKey="value" listValue="name"/>)
            </td>
	     </tr>
	     <tr>
	        <td>填表人</td>
	        <td colspan="2">
	            <input id="evaluateReport" style="float:left;" hiddenInputId="evaluateReporterLog" isUser="true" name="evaluateReporter" value="${evaluateReporter}"/>
	            <input type="hidden" name="evaluateReporterLog" id="evaluateReporterLog"  value="${evaluateReporterLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('evaluateReporter','evaluateReporterLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	        </td>
	        <td >审核</td>
	        <td colspan="2"><input id="evaluateChecker"  style="float:left;" isUser="true"  hiddenInputId="evaluateCheckerLog" name="evaluateChecker" value="${evaluateChecker}"/>
	            <input type="hidden" name="evaluateCheckerLog" id="evaluateCheckerLog"  value="${evaluateCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('evaluateChecker','evaluateCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	        </td>
	     </tr>
	      <tr>
	        <td rowspan="2" style="text-align:center">是否送事业部长审核</td>
	        <td colspan="6"><s:radio cssStyle="margin-left:8px;" name="ifToManager" id="ifToManager" value="ifToManager" theme="simple" list="labIfTests" listKey="value" listValue="name"/></td>
	     </tr>
	     <td colspan="6">
	     <input id="managerName"  style="float:left;" hiddenInputId="managerLoginName"  isUser="true" name="managerName" value="${managerName}"/>
	     <input type="hidden" name="managerLoginName" id="managerLoginName"  value="${managerLoginName}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('managerName','managerLoginName')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	     </td>
	     </security:authorize>
	</tbody> 	 
</table>
