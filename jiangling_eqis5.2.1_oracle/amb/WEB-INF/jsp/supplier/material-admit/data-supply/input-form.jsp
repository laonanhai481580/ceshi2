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
	         <td rowspan='5' >需求部门</td>
	         <td >供应商名称</td>
	         <td ><input id="supplierName" style="float:left;" name="supplierName" value="${supplierName}"/>
	         <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
	         </td>
	         <td >产品名称/料号</td>
	         <td  colspan='2'><input id="productName" name="productName" value="${productName }"/></td>
	     </tr>
	    <tr>
	         <td >授权宣告人</td>
	         <td ><input id="declarer" name="declarer" value="${declarer}"/></td>
	         <td>欧菲光料号</td>
	         <td colspan='2'><input id="materialCode" name="materialCode" value="${materialCode }"/></td>
	     </tr>
	      <tr>
<!-- 	         <td>指定供应商</td> -->
<!-- 	         <td> -->
<%-- 	           <input  style="float:left;" hiddenInputId="supplierLoginName" isUser="true"  id="supplierUserName" name="supplierUserName" value="${supplierUserName}"/> --%>
	        
<%--               <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('supplierUserName','supplierLoginName')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
<!-- 	         </td> -->
	         <td>供应商邮箱</td>
	         <td colspan='4'>
	          <input type="hidden" name="supplierLoginName" id="supplierLoginName"  value="${supplierLoginName}" />
	         <textarea style="width:80%" rows="3" id="supplierMails" name="supplierMails" >${supplierMails}</textarea>
	         <span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">如需发多个邮箱请用"/"分开.</span></span>	</td>
	     </tr>
	      <tr>
	         <td>提出部门</td>
	         <td><input id="raiseDept" name="raiseDept" value="${raiseDept}"/></td>
	         <td>提出日期</td>
	         <td colspan='2'><input id="raiseDate" name="raiseDate" isDate="true" value="<s:date name='raiseDate' format="yyyy-MM-dd"/>" /></td>
	     </tr>
	      <tr>
	         <td>说明</td>
	         <td colspan='4'><textarea rows="5" id="instruction" name="instruction" >${instruction}</textarea></td>
	     </tr>
	     <tr><td style="text-align:center;" colspan='5'>供应商-产品成分宣告表</td><td style="font-family:SimHei;font-weight:bold;">重量:<a id="weights" style="font-family:STHeiti"></a></td>
							</tr>
	    <tr>
	         <input id="rateAll" name="rateAll" type="hidden"/>
	         <td colspan='6' style="padding: 0px; " id="checkItemsParent">
	         <div style="overflow-x:scroll;overflow-y:hidden;">
	              <table class="form-table-border-left" style="border: 0px;table-layout:fixed;">
				<thead>
					<tr>
						<td style="text-align: center; border-top: 0px; border-left: 0px;width:50px;">操作</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">报告编号</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">拆解部位名称</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">均质材料名称</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">均质材料属性</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">均质材料型号</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:70px;">重量</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">制造商</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">化学物质英文全名</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">化学文摘社号</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:50px;">均质材料中化学物质重量</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">测试机构</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">确认</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">豁免条款说明</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">检测日期</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">报告测试时间</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">报告截至时间</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:90px;">比例</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">备注</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">RoHS附件</td>
						<td style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:130px;">SDS附件</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="_productPartReports" id="productPartReport" var="productPartReport" status="st">
						<tr class="productPartReports" zbtr1=true>
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this);rateChange();" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: center;"><input type="text" style="width:95%;"    name="reportName"   value="${reportName}" /></td>
							<td style="text-align: center;">
							  <input type="text" style="width:95%;" name="partName"  value="${partName}" />
							</td>
							<td style="text-align: center;"><input  style="width:95%;"  type="text"  name="stuffName" value="${stuffName}" /></td>
							<td style="text-align: center;"><input style="width:95%;"  type="text"  name="materialType" value="${materialType}" /></td>
							<td style="text-align: center;"><input style="width:95%;"  type="text"  name="materialTypeName" value="${materialTypeName}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text" style="width:50px;" onchange="setweigh(this)"name="materialWeight" value="${materialWeight}" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
							<td style="text-align: center;"><input  style="width:95%;" type="text"  name="vender" value="${vender}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="substanceName" value="${substanceName}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="casNo" value="${casNo}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text" style="width:50px;" name="substanceWeight" value="${substanceWeight}" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="testLab" value="${testLab}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="casNoReachSvhc" value="${casNoReachSvhc}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="exemption" value="${exemption}" /></td>
							<td style="text-align: center;"><input style="width:95%;" isDate="true"   value="<s:date name='completeDate' format="yyyy-MM-dd"/>" name="completeDate"  /></td>
							<td style="text-align: center;"><input style="width:95%;" isDate="true"   value="<s:date name='testReportDate' format="yyyy-MM-dd"/>" name="testReportDate"  /></td>
							<td style="text-align: center;"><input style="width:95%;" isDate="true"   value="<s:date name='cutTime' format="yyyy-MM-dd"/>" name="cutTime"  /></td>
							<td style="text-align: center;"><input style="width:70%;"  type="text"   name="partRate" class="{number:true,messages:{number:'请输入有效数字!'}}" onchange="rateChange();"  value="${partRate}" />%</td>
							<td style="text-align: center;"><input style="width:95%;" type="text"  name="remark" value="${remark}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="hidden"  isFile=true  id="reportFile" name="reportFile" value="${reportFile}" /></td>
							<td style="text-align: center;"><input style="width:95%;" type="hidden"  isFile=true  id="sdsFile" name="sdsFile" value="${sdsFile}" /></td>
							
						</tr>
					</s:iterator>
				</tbody>
			</table>
			</div>
	         </td>
	         
	     </tr>
	    <%--  <tr>
	         <td>RoHS报告</td>
	         <td><input id="partNameRohs" name="partNameRohs" value="${partNameRohs}"/></td>
	         <td><input id="stuffNameRohs" name="stuffNameRohs" value="${stuffNameRohs}"/></td>
	         <td><input id="completeDateRohs" name="completeDateRohs" isDate="true" value="<s:date name='completeDateRohs' format="yyyy-MM-dd"/>" /></td>
	     </tr>
	      <tr>
	         <td>卤素报告</td>
	         <td><input id="partNameHalogen" name="partNameHalogen" value="${partNameHalogen}"/></td>
	         <td><input id="stuffNameHalogen" name="stuffNameHalogen" value="${stuffNameHalogen}"/></td>
	         <td><input id="completeDateHalogen" name="completeDateHalogen" isDate="true" value="<s:date name='completeDateHalogen' format="yyyy-MM-dd"/>" /></td>
	     </tr> --%>
<!-- 	     <tr> -->
<%-- 	         <td colspan="4"><input type="hidden" isFile="true" name="declareFile" id="declareFile" value="${declareFile }"></td> --%>
<!-- 	     </tr> -->
	     <tr>
	         <td >产品结构图/爆炸图</td>
	         <td ><input type="hidden" isFile="true" name="productFile" id="productFile" value="${productFile }"></td>
	     	 <td >BOM附件</td>
	         <td ><input type="hidden" isFile="true" name="bomFile" id="bomFile" value="${bomFile }"></td>
	    	 <td >调查表附件</td>
	         <td ><input type="hidden" isFile="true" name="surveyFile" id="surveyFile" value="${surveyFile }"></td>
	     </tr>
	      <tr>
	         <td>QS</td>
	         <td>审核人</td>
	         <td>
	            <input  style="float:left;" hiddenInputId="qsProcesserLog" isUser="true"  id="qsProcesser" name="qsProcesser" value="${qsProcesser}"/>
	         <input type="hidden" name="qsProcesserLog" id="qsProcesserLog"  value="${qsProcesserLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qsProcesser','qsProcesserLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
	         </td>
	         <td colspan="3"><input name="qsOpinion" style="width:98%" id="qsOpinion" value="${qsOpinion }"/></td>
	     </tr>
	</tbody> 	 
</table>
