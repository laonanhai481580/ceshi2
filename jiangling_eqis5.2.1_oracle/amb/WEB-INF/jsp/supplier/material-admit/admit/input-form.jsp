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
	    <tr> 
	         <td rowspan='4' style="width:5%;"><h3>研发部</h3></td>
	         <td style="width:10%;">规格型号/版本</td>
	         <td ><input id="productVersion" name="productVersion" value="${productVersion}"/>  </td>
	         <td >物料名称</td>
	         <td style="width:20%;"><input id="materialName" name="materialName" value="${materialName }"/></td>
	    	 <td >物料编号</td>
	         <td><input id="materialCode" name="materialCode" value="${materialCode}" style="float:left;"/>
	         <a class="small-button-bg" onclick="testSelect(this)" style="float:left;"><span class="ui-icon ui-icon-search"></span></a>
	         </td>
	    </tr>
	    <tr>
	        <td>申请日期</td>
	        <td><input id="applyDate" name="applyDate" isDate="true" value="<s:date name='applyDate' format="yyyy-MM-dd"/>" /></td>
	     	<td>供应商</td>
	        <td>
             <input style="width:200px;float:left;" id="supplierName" readonly=readonly name="supplierName"  value="${supplierName}"/>
             <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
            </td>
            <td>提交人</td>
            <td>
             <input name="qualityChecker" id="qualityChecker" value="${qualityChecker}" />
	        </td>
	    </tr>
<!-- 	     <tr> -->
<!-- 	     	<td>报告测试时间</td> -->
<%-- 	     	<td><input id="testReportDate" name="testReportDate" isDate="true" value="<s:date name='testReportDate' format="yyyy-MM-dd"/>" /></td> --%>
<!-- 	     	<td>报告截至时间</td> -->
<%-- 	     	<td><input id="cutTime" name="cutTime" isDate="true" value="<s:date name='cutTime' format="yyyy-MM-dd"/>" /></td> --%>
<!-- 	     </tr> -->
	     <tr>
	     <td>项目</td>
	      <td colspan="5"><textarea id="item" name="item">${item }</textarea></td>
	     </tr>
	     <tr>
	     <td >承认资料</td>
	        <td colspan="5" >
	        <s:checkboxlist cssStyle="margin-left:8px;" name="admitProject" id="admitProject" value="#request.projects" theme="simple" list="admitProjects" listKey="value" listValue="name"/>
	       	<input type="checkbox"  id="p">其它
	        <input id="else" name="admitProject" class="checkboxLabel" value="" style="display:none;"/>
	        </td>
	     </tr>
	    <tr>
	         <td rowspan="2"><h3>采购部</h3></td>
	         <td>经办人:</td>
	         <td >
	            <span style="float:left;"> </span><input id="purchaseProcesser" name="purchaseProcesser" isUser="true"style="width:200px;float:left;" hiddenInputId="purchaseProcessLog"  value="${purchaseProcesser}"/>
	             <input type="hidden"  name="purchaseProcessLog" id="purchaseProcessLog"  value="${purchaseProcessLog}" />
<%--               <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseProcesser','purchaseProcessLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>    --%>
	         </td>
	         <td colspan='3'>
	            <span style="float:left;">指定供应商: </span>
	            <input  id="supplier" name="supplier"  hiddenInputId="supplierLoginName" value="${supplierName}" />
	             <input type="hidden" name="supplierLoginName" id="supplierLoginName"  value="${supplierLoginName}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('supplier','supplierLoginName')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>   
	         </td>
	        <td ></td>
	     </tr>
	     <tr>
	     	<td >供应商邮箱</td>
	        <td colspan="2">
	        <input id="supplierEmail" name="supplierEmail" value="${supplierEmail}" style="width:60%;"/><span style="color:red;font-size:8px;" >*用于创建供应商QIS账号</span>
	        </td>
	     	<td colspan="3"></td>
	     </tr>
	      <tr >
	         <td rowspan="3"><h3>供应商</h3></td>
	         <td>其他</td>
	         <td><input id="supplierFile" name="supplierFile" type="hidden" isFile="true" value="${supplierFile}"/></td>
	     	<td>承认状态</td>
	     	<td >
	     		<s:select list="admitStatuss"
					listKey="value" 
					listValue="value" 
					name="admitStatus" 
					id="admitStatus" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
	     	</td>
	     	<td colspan="2"></td>
	     </tr>
	     <tr>
		      <td>工程画图</td>
		      <td><input type="hidden" id="engineeringDrawing" isFile="true" name="engineeringDrawing" value="${engineeringDrawing }" /></td>
		      <td>出货检验报告</td>
		      <td ><input type="hidden" id="checkoutReport" isFile="true" name="checkoutReport" value="${checkoutReport }"/></td>
	     	   <td>环保资料</td>
		      <td ><input type="hidden" id="environmentalMaterial" isFile="true" name="environmentalMaterial" value="${environmentalMaterial }"/></td>
	     </tr>
	     <tr>
		      <td>包装方式</td>
		      <td><input type="hidden" id="packing" isFile="true" name="packing" value="${packing }"/></td>
		      <td>代码说明</td>
		      <td><input type="hidden" id="codeExplain" isFile="true" name="codeExplain" value="${codeExplain }"/></td>
	     	<td colspan="2"></td>
	      </tr>
	      <tr>
		     
	      </tr>
	      <tr id="checkerLog">
	      <td rowspan="2"><h3>部门会签</h3></td>
	       <input  type="hidden" name="checkDeptMansLog" id="checkDeptMansLog" value="${checkDeptMansLog }"/>
	         <td>会签单位</td>
	         <td style="width:15%;"><span style="float:left;" >PM核准:</span><input style="float:left;" hiddenInputId="pmCheckerLog"  isUser="true" id="pmChecker" name="pmChecker" isUser="true" value="${pmChecker}"/>
	          <input type="hidden" class="isCheckerLog" name="pmCheckerLog" id="pmCheckerLog"  value="${pmCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmChecker','pmCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	          <td style="width:15%;"><span style="float:left;" >研发核准:</span><input style="float:left;" hiddenInputId="developCheckerLog" isUser="true" id="developChecker" name="developChecker" isUser="true" value="${developChecker}"/>
	          <input type="hidden" class="isCheckerLog" name="developCheckerLog" id="developCheckerLog"  value="${developCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('developChecker','developCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	          <td style="width:15%;"><span style="float:left;" >QS核准:</span><input style="float:left;" hiddenInputId="qsCheckerLog" isUser="true" id="qsChecker" name="qsChecker" isUser="true" value="${qsChecker}"/>
	          <input type="hidden" class="isCheckerLog" name="qsCheckerLog" id="qsCheckerLog"  value="${qsCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qsChecker','qsCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td style="width:15%;"><span style="float:left;" >SQE核准:</span><input style="float:left;" hiddenInputId="sqeCheckerLog" isUser="true" id="sqeChecker" name="sqeChecker" isUser="true" value="${sqeChecker}"/>
	          <input type="hidden" class="isCheckerLog" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td ></td>
	     </tr>
	     <tr>
	     	<td>签核意见</td>
	     	<td><s:select list="countersigns"
					listKey="value" 
					listValue="value" 
					name="countersignPM" 
					id="countersignPM" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select></td>
				
				<td><s:select list="countersigns"
					listKey="value" 
					listValue="value" 
					name="countersignRD" 
					id="countersignRD" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select></td>
				
				<td><s:select list="countersigns"
					listKey="value" 
					listValue="value" 
					name="countersignQS" 
					id="countersignQS" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select></td>
				<td><s:select list="countersigns"
					listKey="value" 
					listValue="value" 
					name="countersignSQE" 
					id="countersignSQE" 
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select></td>
				<td ></td>
	     </tr>
	    <tr>
	         <td><h3>文控</h3></td>
	         <td>文控意见</td>
	         <td colspan="3"><textarea id="docControl" name="docControl">${docControl }</textarea></td>
	         <td colspan="2">
	            <span style="float:left;">抄送人： </span><input id="copyMan" name="copyMan" multiple=true isUser="true"style="width:200px;float:left;" hiddenInputId="copyManLogin"  value="${copyMan}"/>
	             <input type="hidden"  name="copyManLogin" id="copyManLogin"  value="${copyManLogin}" />
	         </td>
	     </tr>	     
	</tbody> 	 
</table>
