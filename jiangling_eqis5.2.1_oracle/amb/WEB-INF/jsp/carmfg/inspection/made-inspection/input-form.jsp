<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;margin-right: 20px;">
<caption style="height: 25px"><h2>制程报告</h2></caption>
<caption style="text-align:right;padding-bottom:4px;margin-right: 40px;">编号:${inspectionNo}</caption>
<tr>
		   <td colspan='8'>
		     <div style="float:left;">
		   <span style="font-size:30px;color:red;">上传检验数据</span>
		   <button class='btn' type="button" onclick="uploadFiles('iqcInspectionDatas','fileAll');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
		            <input type="hidden" name="hisAttachmentFilesAll" value='${fileAll}'></input>
		            <input type="hidden" name="fileAll" id="fileAll" value='${fileAll}'></input>
		           <span id='iqcInspectionDatas'></span>
		</div>
		   </td>
		</tr>
<tr>
	<td>二维码</td>
	<td>
	    <textarea rows="4" cols="3" name="qrCode" id="qrCode">${qrCode}</textarea>
	</td>
	<td>流程卡</td>
	<td>
		<input name="processCard" id="processCard" value="${processCard}" />
		<button class='btn' type="button" onclick="submitProcessCard('${mfgctx}/inspection/made-inspection/get-process-card.htm');"><span><span><b class="btn-icons btn-icons-submit"></b>确定</span></span></button>
	</td>
	<td><span style="color:red">*</span>厂区</td>
	<td>
		<s:select list="mfg_business_unit_name" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="businessUnitName"
			  id="businessUnitName"
			  cssClass="{required:true,messages:{required:'必填'}}"
			  emptyOption="true"></s:select>
	</td>	
	<td style="width:115px;"><span style="color:red">*</span>制程区段</td>
	<td>
		 <s:select list="processSections" 
 			  theme="simple"
			  listKey="value"  
 			  listValue="name" 
			  labelSeparator=""
 			  name="processSection" 
			  emptyOption="true" 
 			  cssClass="{required:true,messages:{required:'必填'}}"></s:select> 
	</td>	
</tr>
<tr>
	<td style="width:140px;"><span style="color:red">*</span>检验</td>
	<td style="">
		<s:select list="inspection_models"
			  theme="simple"
			  listKey="name" 
			  listValue="value" 
			  labelSeparator=""
			  cssClass="{required:true,messages:{required:'必填'}}"
			  name="inspectionPointType"
			  emptyOption="true"></s:select>
	</td>
	<td>产品类别</td>
	<td>
		<s:select list="mfg_category" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productModel"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;"><span style="color:red">*</span>机种</td>
	<td style="">
		<input name="machineNo" value="${machineNo}" id="machineNo" onchange="loadCheckItems();" class="{required:true,messages:{required:'必填'}}"></input>
	</td>
	<td style="width:140px;"><span style="color:red">*</span>机种名称</td>
	<td style="">
		<input name="machineName" value="${machineName}" id="machineName"  class="{required:true,messages:{required:'必填'}}"></input>
	</td>	
</tr>
<tr>
	<td><span style="color:red">*</span>工序</td>
	<td>
		<s:select list="mfg_process_bus" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  onchange="loadCheckItems();"
			  name="workProcedure"
			  id="workProcedure"
			  cssClass="{required:true,messages:{required:'必填'}}"
			  emptyOption="true"></s:select>
	</td>		
	<td>工站</td>
	<td>
		<input id="section" name="section" value="${section}"></input>
	</td>
	<td><span style="color:red">*</span>检验日期</td>
	<td>
		<input id="inspectionDate" name="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>" onchange="loadCheckItems();" class="{required:true,messages:{required:'必填'}}"/>
	</td>
	<td style="width:140px;">检验员</td>
	<td style="">
		<input id="inspector" name="inspector" value="${inspector}" />
	</td>
</tr>
<tr>
	<td><span style="color:red;">*</span>批次数量</td>
	<td>
		<input id="stockAmount" name="stockAmount" value="${stockAmount}" onchange="loadCheckItems();"  class="{required:true,digits:true,messages:{required:'出货数量不能为空',digits:'必须是整数!'}}"></input>
		<s:select list="amountUnits" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  cssStyle="width:50px"
			  name="amountUnit"
			  id="amountUnit"></s:select>
	</td>
	<td style="width:140px;">班别</td>
	<td style="">
		<s:select list="mfg_work_group_type" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="workGroupType"
			  emptyOption="false"></s:select>
	</td>
	<td >客户
	</td>
	<td >
		<s:select list="customers" 
			theme="simple"
			listKey="value" 
			listValue="name" 
			id="customerName"
			name="customerName"
			emptyOption="true">
		</s:select>
	</td>
	<td >客户机种
	</td>
	<td ><input id="customerModel" name="customerModel" value="${customerModel}" onclick="modelClick(this);"/>
	</td>	
</tr>
<tr>
   	<td>批次号
	</td>
	<td ><input id="batchNo" name="batchNo" value="${batchNo}" />
	</td>
	<td >楼层
	</td>
	<td ><input id="floor" name="floor" value="${floor}" />
	</td>
	<td style="width:140px;"><span style="color:red">*</span>选择审核人</td>
	<td style="">
	    <%if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){%>
	    	<s:select list="mfg_audit_mans" 
	  			  theme="simple"
	  			  listKey="value" 
	  			  listValue="name" 
	  			  labelSeparator=""
	  			  name="auditman"
	  			  onchange="setAuditMan(this);"
	  			  emptyOption="true"></s:select><br/>
	    <%} %>
		<input type="hidden" id="choiceAuditLoinMan" name="choiceAuditLoinMan" value="${choiceAuditLoinMan}"/>
		<input  id="choiceAuditMan" name="choiceAuditMan" value="${choiceAuditMan}" onclick='selectObj("选择审核人","choiceAuditLoinMan","choiceAuditMan","loginName");' readonly="readonly"/>
	</td>	
	<td colspan="4">
		<button class='btn' type="button" onclick="choiceWaitCheckItem();">
			<span><span><b class="btn-icons btn-icons-search"></b>领取待检验项目</span></span>
		</button>
	</td>

</tr>	
<tr>
	<td colspan="8" style="padding:0px;" id="checkItemsParent">
		<div style="overflow-x:auto;overflow-y:hidden;">
			<%@ include file="check-items.jsp"%>
		</div>
	</td>
</tr>
 <tr>
	<td  colspan="8" style="text-align: center;">设备参数检验记录</td>
</tr>
<tr>
	<td colspan="8" style="padding:0px;" id="plantParameterItems">
		<div style="overflow-x:auto;overflow-y:hidden;">
			<%@ include file="plant-parameter-items.jsp"%>
		</div>
	</td>
</tr>
<tr>
	<td colspan="8" style="text-align: center;font-weight: bold;font-size: 14px;">物料信息</td>
</tr>
<tr>
	<td colspan="8">
		<table  class="form-table-border-left" style="border:0px;table-layout:fixed;">
			<tr>
				<td style="width: 5%;text-align: center;">操作</td>
				<td style="text-align: center;">供应商代码</td>
				<td style="text-align: center;">供应商批次号</td>
				<td style="text-align: center;">物料号</td>
				<td style="text-align: center;">入料批次号</td>
				<td style="text-align: center;">供应商批次库存数</td>
				<td style="text-align: center;">备注</td>
			</tr>
				<%int bcount=0; %>
		        <s:iterator value="mfgSupplierMessages" var="mfgSupplierMessages" id="mfgSupplierMessages" status="st">
				    <tr class="mfgSupplierMessagesTr">
				    	<td>
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick='addRowHtml(this,"objId")' title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick='removeRowHtml("mfgSupplierMessagesTr",this,"IDX")' title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
						<td style="text-align: center;">
							<input id="supplierCode-<%=bcount %>" name="supplierCode-<%=bcount %>" filedName="supplierCode" value="${supplierCode}" style="width:80%;"/>
							<input id="supplierName-<%=bcount %>" name="supplierName-<%=bcount %>" filedName="supplierName" value="${supplierName}" type="hidden"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBatchNo-<%=bcount %>" name="supplierBatchNo-<%=bcount %>" filedName="supplierBatchNo" value="${supplierBatchNo}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBomCode-<%=bcount %>" name="supplierBomCode-<%=bcount %>" filedName="supplierBomCode" value="${supplierBomCode}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBomBatchNo-<%=bcount %>" name="supplierBomBatchNo-<%=bcount %>" filedName="supplierBomBatchNo" value="${supplierBomBatchNo}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBatchNum-<%=bcount %>" name="supplierBatchNum-<%=bcount %>" filedName="supplierBatchNum" value="${supplierBatchNum}" style="width:80%;" class="{digits:true,messages:{digits:'必须是整数!'}}"/>
						</td>
						<td style="text-align: center;">
							<textarea rows="" cols="" id="supplierRemark-<%=bcount %>" name="supplierRemark-<%=bcount %>" filedName="supplierRemark"  style="width:80%;">${supplierRemark}</textarea>
						</td>
                     </tr>
                     <%bcount++; %>
                     <input type="hidden" id="IDX" value="<%=bcount %>"/>
                     
		        </s:iterator>
		</table>
	</td>
</tr>
<tr>
	<td colspan="8" style="text-align: center;font-weight: bold;font-size: 14px;">生产信息</td>
</tr>
<tr>
	<td colspan="8">
		<table  class="form-table-border-left" style="border:0px;table-layout:fixed;">
			<tr>
				<td style="width: 5%;text-align: center;">操作</td>
				<td style="text-align: center;">二维码</td>
				<td style="text-align: center;">机台</td>
				<td style="text-align: center;">作业员</td>
				<td style="text-align: center;">生产时间</td>
				<td style="text-align: center;">工单</td>
				<td style="text-align: center;">工站</td>
			</tr>
			<tr>
				<%int ccount=0; %>
		        <s:iterator value="manufactureMessages" var="manufactureMessages" id="manufactureMessages" status="st">
				    <tr class="manufactureMessagesTr">
				    	<td>
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick='addRowHtml(this,"IDXA")' title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick='removeRowHtml("manufactureMessagesTr",this,"IDXA")' title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
						<td style="text-align: center;">
							<input type="hidden" id="qcCode-<%=ccount %>" name="qcCode-<%=ccount %>" filedName="qcCode" value="${qcCode}"  style="width:80%;"/>
							${qcCode}
						</td>
						<td style="text-align: center;">
							<input id="marchineNo-<%=ccount %>" name="marchineNo-<%=ccount %>"  filedName="marchineNo" value="${marchineNo}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="operator-<%=ccount %>" name="operator-<%=ccount %>"  filedName="operator" value="${operator}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="manufactureTime-<%=ccount %>" name="manufactureTime-<%=ccount %>"  filedName="manufactureTime" value="${manufactureTime}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="workOrderNo-<%=ccount %>" name="workOrderNo-<%=ccount %>"  filedName="workOrderNo" value="${workOrderNo}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="section-<%=ccount %>" name="section-<%=ccount %>"  filedName="section" value="${section}"  style="width:80%;"/>
						</td>
                     </tr>
                     <%ccount++; %>
                     <input type="hidden" id="IDXA" value="<%=ccount %>"/>
                     
		        </s:iterator>
				
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td>检验判定</td>
	<td style="border-top:0px;">
		<span><s:if test="inspectionConclusion=='OK'">合格</s:if><s:if test="inspectionConclusion=='NG'">不合格</s:if></span>
		<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"></input>
	</td>
	<td>处理方式</td>
	<td>
		<s:select list="mfg_processing_result" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="processingResult"
			  emptyOption="true"></s:select>
	</td>
	<td colspan="4">
	</td>
</tr>
<tr>
	<td>
		审核意见<span id="auditTextSpan"  style="color:red;"></span>
	</td>
	<td colspan="7">
		<textarea lanuch="stage2" rows="2" cols="3" id="auditText" name="auditText">${auditText}</textarea>
	</td>
</table>