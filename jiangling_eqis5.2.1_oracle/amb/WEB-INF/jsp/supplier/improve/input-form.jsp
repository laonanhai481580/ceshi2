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
	    <tr><td style="text-align:center;background:#B452CD" colspan='6'>开单填写</td></tr>
	    <tr> 
	         <td>发生地点</td>
	         <td >
	            <input type="radio" id="happenSpace1" name="happenSpace" value="进料检验" <s:if test="happenSpace=='进料检验'">checked="true"</s:if> title="进料检验"/><label for="happenSpace1">进料检验</label>
	            <input type="radio" id="happenSpace2" name="happenSpace" value="产线" <s:if test="happenSpace=='产线'">checked="true"</s:if> title="产线"/><label for="happenSpace2">产线</label>
	            <input type="radio" id="happenSpace3" name="happenSpace" value="客户" <s:if test="happenSpace=='客户'">checked="true"</s:if> title="客户"/><label for="happenSpace3">客户</label>
	            <input type="radio" id="happenSpace4" name="happenSpace" value="其他" <s:if test="happenSpace=='其他'">checked="true"</s:if> title="其他"/><label for="happenSpace4">其他</label>
	         </td>
	         <td>产品阶段</td>
	         <td >
	         <s:select list="productStages" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="productStage"
				  id="productStage"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	        <td>事业部</td>
	         <td><input id="businessUnitName" name="businessUnitName" value="${businessUnitName}"/>
	         <input id="isSupplier" type="hidden" name="isSupplier" value="${isSupplier}"/>
	         <input id="isClosedAlaysis" type="hidden" name="isClosedAlaysis" value="${isClosedAlaysis}"/>
	         <input id="sqeCompleteTime" type="hidden" name="sqeCompleteTime" value="${sqeCompleteTime}"/>
	           <input id="sqeReplyTime" type="hidden" name="sqeReplyTime" value="${sqeReplyTime}"/>
	           <input id="currentMan" type="hidden" name="currentMan" value="${currentMan}"/>
	           <input id="currentManLog" type="hidden" name="currentManLog" value="${currentManLog}"/></td>	         
	     </tr>
	    <tr>
	        <td>开单区域</td>
	         <td><input id="billingArea" name="billingArea" value="${billingArea}"/></td>
	          <td>供应商</td>
	         <td><input style="float:left;" id="supplierName" name="supplierName" readonly=readonly value="${supplierName}"/><input type='hidden' id="supplierCode"  name="supplierCode" value="${supplierCode}"/>
	         <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
	         </td>
	          <td>检验日期</td>
	         <td><input id="inspectionDate" name="inspectionDate" isDate="true" value="<s:date name='inspectionDate' format="yyyy-MM-dd"/>" /></td>	          
	     </tr>
	     <tr>
	         <td>品名</td>
	         <td><input id="bomName" name="bomName" value="${bomName}"/></td>
	         <td>料号</td>
	         <td><input id="bomCode" name="bomCode" value="${bomCode}"/></td>
	         <td>物料类别</td>
	         <td><%-- <s:select list="materialTypes" 
									  theme="simple"
									  listKey="name" 
									  listValue="name" 
									  name="materialType"
									  id="materialType"
									  emptyOption="true"
									  labelSeparator=""
									  cssStyle="width:150px;"
									  ></s:select> --%>
				<input id="materialType" name="materialType" value="${materialType}"/>					  
			</td>
	     </tr>
	      <tr>
	         <td>进料数</td>
	         <td><input id="incomingAmount" name="incomingAmount" value="${incomingAmount}"/></td>
	         <td>抽检数</td>
	         <td><input id="checkAmount" name="checkAmount" value="${checkAmount}"/></td>
	          <td>单位</td>
	         <td><input id="units" name="units" value="${units}"/></td>
	     </tr>
	     <tr>
	     	<td>供应商邮箱地址</td>
	     	<td colspan='2'><textarea id="supplierEmail" name="supplierEmail" onchange="checkEmail(this);">${supplierEmail}</textarea></td>
	     	<td colspan='2'>
	     	<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">如需发多个邮箱请用"/"分开.</span></span>	
	     	</td>
	     	<td>检验编号:
	     	<input name="inspectionId" id="inspectionId" value="${inspectionId }" type="hidden">
	     	<input name="inspectionFormNo" id="inspectionFormNo" value="${inspectionFormNo }" type="hidden">
	     	<a title="查看检验单详情" href="javascript:void(0);openInspectionForm('${inspectionId}')">${inspectionFormNo}</a></td>
	     	
	     </tr>
	     <tr>
	        <!--  <td  style="text-align:center;"> <input type="radio" id="surfaceBad" name="surfaceBad" value="是" <s:if test="surfaceBad=='是'">checked="true"</s:if> title="外观不良"/><label for="surfaceBad">外观不良</label></td> -->
	         <td  style="text-align:center;">外观不良</td>
	         <td colspan='2' style="text-align:center;">外观不良率</td>
	         <td  style="text-align:center;"><input name="surfaceBadRate" id="surfaceBadRate" value="${surfaceBadRate }"/>%</td>
	     	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="surfaceBadState"
				  id="surfaceBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	      <tr>
	         <!-- <td  style="text-align:center;"> <input type="radio" id="functionBad" name="functionBad" value="是" <s:if test="functionBad=='是'">checked="true"</s:if> title="功能不良"/><label for="functionBad">功能不良</label></td> -->
	        <%--  <td  style="text-align:center;"> 功能不良</td>
	         <td colspan='2' style="text-align:center;">功能不良率</td>
	         <td style="text-align:center;"><input name="functionBadRate" id="functionBadRate" value="${functionBadRate }"/>%</td>
	      	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="functionBadState"
				  id="functionBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td> --%>
	     </tr>
	      <tr>
	         <!-- <td style="text-align:center;"> <input type="radio" id="sizeBad" name="sizeBad" value="是" <s:if test="sizeBad=='是'">checked="true"</s:if> title="尺寸不良"/><label for="sizeBad">尺寸不良</label></td> -->
	         <td style="text-align:center;">尺寸不良</td>
	         <td colspan='2' style="text-align:center;">尺寸不良率</td>
	         <td  style="text-align:center;"><input name="sizeBadRate" id="sizeBadRate" value="${sizeBadRate }"/>%</td>
	    	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="sizeBadState"
				  id="sizeBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	      <tr>
	        <!--  <td style="text-align:center;"> <input type="radio" id="featuresBad" name="featuresBad" value="是" <s:if test="featuresBad=='是'">checked="true"</s:if> title="特性不良"/><label for="surfaceBad">特性不良</label></td> -->
	        <td style="text-align:center;">特性不良</td>
	         <td colspan='2' style="text-align:center;">特性不良率</td>
	         <td  style="text-align:center;"><input name="featuresBadRate" id="featuresBadRate" value="${featuresBadRate }"/>%</td>
	    	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="featuresBadState"
				  id="featuresBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	     <tr>
	        <td >异常描述</td>
	        <td colspan='3'><textarea rows="5" name="badDesc" style="width:98%;" id="badDesc" >${badDesc}</textarea></td>
	        <td>附件</td>
	        <td><input name="descFile" isFile="true"  type="hidden" id="descFile" value="${descFile }"/></td>
	     </tr>
	     <tr>
	         <td>检验员</td>
	         <td><input id="inspector" name="inspector" value="${inspector}" readonly="readonly" /></td>
	         <td>审核</td>
	         <td>
	            <input id="reportChecker" name="reportChecker" value="${reportChecker}" isUser="true" style="float:left;" hiddenInputId="reportCheckerLog"/>
	            <input type="hidden" name="reportCheckerLog" id="reportCheckerLog"  value="${reportCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('reportChecker','reportCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	          </td>
	          <td>核准</td>
	         <td>
	             <input id="approvaler" name="approvaler" value="${approvaler}" isUser="true" style="float:left;" hiddenInputId="approvalerLog"/>
	            <input type="hidden" name="approvalerLog" id="approvalerLog"  value="${approvalerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('approvaler','approvalerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	    <tr>
	         <td>SQE确认</td>
	         <td colspan='5'>
	            <input id="sqeChecker" name="sqeChecker" style="float:left;" hiddenInputId="sqeCheckerLog" isUser="true" value="${sqeChecker}"/>
	             <input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>   
	         </td>
	     </tr>
	     <security:authorize ifAnyGranted="supplier-improve-conceal">
	     <tr><td style="text-align:center;background:#B452CD" colspan='6'>PMC经办</td></tr>
	      <tr>
	         <td>PMC意见</td>
	         <td colspan='5'><input style="width:98%" id="pmcOpinion" name="pmcOpinion" value="${pmcOpinion}"/></td>
	     </tr>
	     <tr>
	         <td>需求交期</td>
	         <td><%-- <input id="demandDeliveryPeriod" name="demandDeliveryPeriod" value="${demandDeliveryPeriod}"/> --%>
	         	<input id="demandDeliveryPeriod" name="demandDeliveryPeriod" isDate="true" value="<s:date name='demandDeliveryPeriod' format="yyyy-MM-dd"/>" />
	         </td>
	         <td>MRB申请</td>
	         <td>
	            <input type="radio" id="mrbApply1" name="mrbApply" value="需要" <s:if test="mrbApply=='需要'">checked="true"</s:if> checked="checked" title="需要" onchange="mrbApplyChange(this);"/><label for="mrbApply1">需要</label>
	            <input type="radio" id="mrbApply2" name="mrbApply" value="不需要" <s:if test="mrbApply=='不需要'">checked="true"</s:if>  title="不需要" onchange="mrbApplyChange(this);"/><label for="mrbApply2">不需要</label>
	         </td>
	          <td><span id="mrbReportNo_span" style="color:red;"></span>MRB单号</td>
	         <td><input id="mrbReportNo" name="mrbReportNo" value="${mrbReportNo }" /></td>
	     </tr>
	      <tr >
	         <td>审核</td>
	         <td>
	            <input id="pmcChecker" name="pmcChecker" value="${pmcChecker}" isUser="true" style="float:left;" hiddenInputId="pmcCheckerLog"/>
	            <input type="hidden" name="pmcCheckerLog" id="pmcCheckerLog"  value="${pmcCheckerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmcChecker','pmcCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	          </td>
	          <td>核准</td>
	         <td>
	             <input id="pmcApprovaler" name="pmcApprovaler" value="${pmcApprovaler}" isUser="true" style="float:left;" hiddenInputId="pmcApprovalerLog"/>
	            <input type="hidden" name="pmcApprovalerLog" id="pmcApprovalerLog"  value="${pmcApprovalerLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmcApprovaler','pmcApprovalerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td></td><td></td>
	     </tr>
	     <tr><td style="text-align:center;background:#B452CD" colspan='6'>SQE1</td></tr>
	     <tr>
	         <td>品质中心意见</td>
	         <td colspan='5'><input style="width:98%" id="qualityOpinion" name="qualityOpinion" value="${qualityOpinion}"/></td>
	     </tr>
	     <tr>
	         <td>处理意见</td>
	         <td>
                <input type="radio" id="sqeProcessOpinion1"  name="sqeProcessOpinion" value="特采" <s:if test="sqeProcessOpinion=='特采'">checked="checked"</s:if> title="特采" checked="checked" onchange="sqeProcessOpinionChange(this);"/><label for="sqeProcessOpinion1">特采</label>
	            <input type="radio" id="sqeProcessOpinion2"  name="sqeProcessOpinion" value="退货" <s:if test="sqeProcessOpinion=='退货'">checked="checked"</s:if> title="退货" onchange="sqeProcessOpinionChange(this);"/><label for="sqeProcessOpinion2">退货</label>
             </td>
	         <td><span id="returnReportNo_span" style="color:red;"></span>退货通知单单号</td>
	         <td>
	          <input id="returnReportNo" name="returnReportNo" value="${returnReportNo }" />
	         </td>
	          <td><span id="sqeMrbReportNo_span" style="color:red;"></span>MRB单号</td>
	         <td><input id="sqeMrbReportNo" name="sqeMrbReportNo" value="${sqeMrbReportNo }" /></td>
	     </tr>
	      <tr >
	          <td>经办人</td>
	          <td> <input readonly="readonly" disabled="true" value="${sqeChecker}" /></td> 
	         <td>审核</td>
	         <td>
 				<input id="sqeAuditer1" name="sqeAuditer1" value="${sqeAuditer1}" isUser="true" style="float:left;" hiddenInputId="sqeAuditerLog1"/>
	            <input type="hidden" name="sqeAuditerLog1" id="sqeAuditerLog1"  value="${sqeAuditerLog1}" />
             	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeAuditer','sqeAuditerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>	          </td>
	          <td>核准</td>
	         <td>
	             <input id="sqeApprovaler1" name="sqeApprovaler1" value="${sqeApprovaler1}" isUser="true" style="float:left;" hiddenInputId="sqeApprovalerLog1"/>
	            <input type="hidden" name="sqeApprovalerLog1" id="sqeApprovalerLog1"  value="${sqeApprovalerLog1}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeApprovaler1','sqeApprovalerLog1')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	     </tr>
	     </security:authorize>
	      <tr><td style="text-align:center;background:#B452CD" colspan='6'>供应商</td></tr>
	      <tr>
	         <td>暂定对策实施</td>
	         <td colspan='5'><textarea rows="5" cols="3" id="tempCountermeasures" name="tempCountermeasures">${tempCountermeasures }</textarea></td>
	      </tr>
	      <tr>
	          <td>真因定义及验证</td>
	          <td colspan='5'><textarea rows="5" cols="3" id="trueReasonCheck" name="trueReasonCheck">${trueReasonCheck }</textarea></td>
	      </tr>
	     <tr>
	         <td>永久对策实施</td>
	          <td colspan='5'><textarea rows="5" cols="3" id="countermeasures" name="countermeasures">${countermeasures }</textarea></td>
	     </tr>
	     <tr>
	         <td>预防再发生</td>
	         <td colspan='5'><textarea rows="5" cols="3" id="preventHappen" name="preventHappen">${preventHappen }</textarea></td>
	     </tr>
	     <tr>
	        <td>佐证资料</td>
	        <td colspan='3'>
	           <input type="hidden" name="supplierFile" id="supplierFile" isFile="true"  value="${supplierFile}" />
	        </td>
	        <%--  <td>经办</td>
	         <td colspan='5'>
	            <input id="supplierProcesser" name="supplierProcesser" value="${supplierName}"  />
	            <input type="hidden" name="supplierProcesserLog" id="supplierProcesserLog"  value="${supplierCode}" />
	         </td> --%>
	         <td>供应商回复日期</td>
	         <td >
	          <input type="hidden" id="supplierProcesser" name="supplierProcesser" value="${supplierName}"  />
	            <input type="hidden" name="supplierProcesserLog" id="supplierProcesserLog"  value="${supplierCode}" />
	            <input id="requestDate" name="requestDate" isDate="true" readonly="readonly" value="<s:date name='requestDate' format="yyyy-MM-dd"/>" />
	         </td>
	     </tr>
	     <tr><td style="text-align:center;background:#B452CD" colspan='6'>SQE</td></tr>
	     <tr>
	         <td>供应商改善效果确认</td>
	         <td colspan='3'><textarea rows="5" cols="3" id="checkResult" name="checkResult">${checkResult }</textarea></td>
	         <td>sqe追踪完成时间</td>
	         <td>
	           <input id="sqeFinishDate" name="sqeFinishDate" isDate="true" value="<s:date name='sqeFinishDate' format="yyyy-MM-dd"/>" />
	         </td>
	     </tr>
	      <tr >
	          <td>追踪人</td>
	          <td> <input readonly="readonly" disabled="true" value="${sqeChecker}" /></td> 
	         <td>审核</td>
	         <td>
	             <input id="sqeAuditer" name="sqeAuditer" value="${sqeAuditer1}"  style="float:left;" hiddenInputId="sqeAuditerLog"/>
<%-- 	            <input type="hidden" name="sqeAuditerLog" id="sqeAuditerLog"  value="${sqeAuditerLog1}" /> --%>
<%--              	 <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeAuditer','sqeAuditerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
	          </td>
	          <td>核准</td>
	         <td>
	              <input id="sqeApprovaler" name="sqeApprovaler" value="${sqeApprovaler1}"  style="float:left;" hiddenInputId="sqeApprovalerLog"/>
<%-- 	            <input type="hidden" name="sqeApprovalerLog" id="sqeApprovalerLog"  value="${sqeApprovalerLog1}" /> --%>
<%--              	 <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeAuditer','sqeAuditerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
	         </td>
	     </tr>
	</tbody> 	 
</table>
