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
	         <td>供应商名称</td>
	         <td><input id="supplierName" name="supplierName" value="${supplierName}" readonly="readonly"/></td>
	         <td>供应商编号</td>
	         <td ><input id="supplierCode" name="supplierCode" value="${supplierCode }" readonly="readonly"/>
	         <input id="supplierId" name="supplierId" value="${supplierId }" type="hidden"/>
	         </td>
	         <td>供应物料名称</td>
	         <td ><input id="supplyMaterial" name="supplyMaterial" value="${supplyMaterial }" readonly="readonly"/></td>
	     </tr>
	    <tr>
	         <td>材料类型</td>
	         <td><input id="materialType" name="materialType" value="${materialType}"  readonly="readonly"/></td>
	         <td>准入时间</td>
	         <td ><input id="supplierEnterDate" name="supplierEnterDate" isDate="true" value="<s:date name='supplierEnterDate' format="yyyy-MM-dd"/>" /></td>
	         <td>取消申请时间</td>
	         <td ><input id="cancleDate" name="cancleDate" isDate="true" value="<s:date name='cancleDate' format="yyyy-MM-dd"/>" /></td>
	     </tr>
	      <tr>
	         <td>申请部门</td>
	         <td><input id="applyDept" name="applyDept" value="${applyDept}" readonly="readonly"/></td>
	         <td>申请人</td>
	         <td ><input id="applyMan" name="applyMan"  value="${applyMan }" readonly="readonly"/></td>
	         <td></td> <td></td>
	     </tr>
	      <tr>
	         <td>问题点描述及影响范围</td>
	         <td colspan='5'><textarea rows="5" id="problemDesc" name="problemDesc" value="">${problemDesc}</textarea></td>
	     </tr>
	    <tr>
	         <td >采购中心</td>
	         <td >经办人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="purchaseProcesserLog" isUser="true" id="purchaseProcesser" name="purchaseProcesser" value="${purchaseProcesser}"/>
	              <input type="hidden" name="purchaseProcesserLog" id="purchaseProcesserLog"  value="${purchaseProcesserLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseProcesser','purchaseProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>审核人</td>
	         <td>
	             <input style="float:left;" hiddenInputId="purchaseCheckerLog" isUser="true" id="purchaseChecker" name="purchaseChecker" value="${purchaseChecker}"/>
	              <input type="hidden" name="purchaseCheckerLog" id="purchaseCheckerLog"  value="${purchaseCheckerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseChecker','purchaseCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
             </td>
	         <td></td>
	     </tr>
	     <tr>
	        <td rowspan='2'>财务部</td>
	        <td>应付账款余额</td>
	        <td colspan="4"> <input id="payMoney" name="payMoney" value="${payMoney}"/></td>
	     </tr>
	     <tr>
	         <td >经办人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="financeProcesserLog" isUser="true" id="financeProcesser" name="financeProcesser" value="${financeProcesser}"/>
	              <input type="hidden" name="financeProcesserLog" id="financeProcesserLog"  value="${financeProcesserLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('financeProcesser','financeProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>审核人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="financeCheckerLog" isUser="true" id="financeChecker" name="financeChecker" value="${financeChecker}"/>
	              <input type="hidden" name="financeCheckerLog" id="financeCheckerLog"  value="${financeCheckerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('financeChecker','financeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
             </td>
	         <td></td>
	     </tr>
	      <tr>
	        <td rowspan='2'>物控</td>
	        <td>在库库存数</td>
	        <td > <input id="pmcStockAmount" name="pmcStockAmount" value="${pmcStockAmount}"/></td>
	        <td>在线库存数</td>
	        <td > <input id="pmcLineAmount" name="pmcLineAmount" value="${pmcLineAmount}"/></td>
	        <td></td>
	     </tr>
	     <tr>
	         <td >经办人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="pmcProcesserLog" isUser="true" id="pmcProcesser" name="pmcProcesser" value="${pmcProcesser}"/>
	              <input type="hidden" name="pmcProcesserLog" id="pmcProcesserLog"  value="${pmcProcesserLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmcProcesser','pmcProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>审核人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="pmcCheckerLog" isUser="true" id="pmcChecker" name="pmcChecker" value="${pmcChecker}"/>
	              <input type="hidden" name="pmcCheckerLog" id="pmcCheckerLog"  value="${pmcCheckerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmcChecker','pmcCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
             </td>
	         <td></td>
	     </tr>
	     <tr>
	        <td rowspan='4'>SQM</td>
	        <td>IQC库存品检验状况</td>
	        <td colspan="4"><input id="stockInsepctionState" name="stockInsepctionState" value="${stockInsepctionState}"/></td>
	     </tr>
	     <tr>
	         <td >经办人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="sqmProcesserLog" isUser="true" id="sqmProcesser" name="sqmProcesser" value="${sqmProcesser}"/>
	              <input type="hidden" name="sqmProcesserLog" id="sqmProcesserLog"  value="${sqmProcesserLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqmProcesser','sqmProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>审核人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="sqmCheckerLog" isUser="true" id="sqmChecker" name="sqmChecker" value="${sqmChecker}"/>
	              <input type="hidden" name="sqmCheckerLog" id="sqmCheckerLog"  value="${sqmCheckerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqmChecker','sqmCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
             </td>
	         <td></td>
	     </tr>
	     <tr>
	        <td>SQE处理结果</td>
	        <td colspan="4"><input id="sqeProcessResult" name="sqeProcessResult" value="${sqeProcessResult}"/></td>
	     </tr>
	     <tr>
	         <td >经办人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="sqeProcesserLog" isUser="true" id="sqeProcesser" name="sqeProcesser" value="${sqeProcesser}"/>
	              <input type="hidden" name="sqeProcesserLog" id="sqeProcesserLog"  value="${sqeProcesserLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeProcesser','sqeProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td>审核人</td>
	         <td>
	              <input style="float:left;" hiddenInputId="sqeCheckerLog" isUser="true" id="sqeChecker" name="sqeChecker" value="${sqeChecker}"/>
	              <input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
             </td>
	         <td></td>
	     </tr>
	     <tr>
	         <td>最终结果及意见</td>
	         <td>意见</td>
	         <td>
<%-- 	            <input  name="managerIdeal" id="managerIdeal"  value="${managerIdeal}" /> --%>
	              <s:select list="finalResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="managerIdeal"
				  id="managerIdeal"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	          </td>
	         <td>总经理</td>
	         <td>
	            <input style="float:left;" hiddenInputId="managerLog" isUser="true" id="managerName" name="managerName" value="${managerName}"/>
	              <input type="hidden" name="managerLog" id="managerLog"  value="${managerLog}" />
                  <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('managerName','managerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td></td>
	     </tr>
	</tbody> 	 
</table>
