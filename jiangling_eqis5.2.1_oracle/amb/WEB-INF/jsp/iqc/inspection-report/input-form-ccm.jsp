<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String changeView=(String)ActionContext.getContext().get("changeView");
%>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
		<caption style="height: 25px">
			<%
				String checkBomMaterialType = ActionContext.getContext().getValueStack().findString("checkBomMaterialType");
				String storageType = ActionContext.getContext().getValueStack().findString("storageType");
				String tableName = "";
				if(StringUtils.isNotEmpty(checkBomMaterialType)){
					tableName = checkBomMaterialType;
				}else if(StringUtils.isNotEmpty(storageType)){
					tableName = storageType;
				}
				if(tableName.endsWith("检验")){
					tableName = tableName.substring(0,tableName.length()-2);
				}
			%>
			<h2>
			<%=tableName%>检验报告
<%-- 			<%=ContextUtils.getSubCompanyName()==null?"":"("+ContextUtils.getSubCompanyName()+")"%> --%>
			</h2>
		</caption>
		<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}</caption>
		<tr>
		   <td colspan='6'>
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
			<td style="width:115px;"><span style="color:red">*</span>厂区</td>
			<td>
			    <select name="processSection" id="processSection" class="xla_k" onchange="setCustomerCode();">
                         <option value=""></option>
                         <option value="下罗IQC" <c:if test='${"下罗IQC" eq processSection}'>selected</c:if>>下罗IQC</option>
                         <option value="高新IQC" <c:if test='${"高新IQC" eq processSection}'>selected</c:if>>高新IQC</option>
                         <option value="培训中心IQC" <c:if test='${"培训中心IQC" eq processSection}'>selected</c:if>>培训中心IQC</option>
                </select>
				 <%-- <s:select list="processSections" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  name="processSection"
					  emptyOption="true"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select>  --%>
				  <input type="hidden" name="businessUnitCode" id="businessUnitCode" value="${businessUnitCode}" />
			</td>
			<td><span style="color:red">*</span>物料编码</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input style="float:left;" name="checkBomCode"  id="checkBomCode" value="${checkBomCode}" hisValue="${checkBomCode}" class="{required:true,messages:{required:'物料编码不能为空'}}" onblur="loadCheckItems()"/>
				<%}else{ %>
					<span>${checkBomCode}</span>
					<input type="hidden" name="checkBomCode" id="checkBomCode" value="${checkBomCode}" />
				<%}%>
			</td>
			<td><span style="color:red">*</span>物料名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input name="checkBomName" id="checkBomName" value="${checkBomName}" />
				<%}else{ %>
					<span>
						${checkBomName}
						<input type="hidden" name="checkBomName" id="checkBomName" value="${checkBomName}" />
					</span>
				<%}%>
			</td>
		</tr>
		<tr>
			<td style="width:115px;"><span style="color:red">*</span>供应商编号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input style="float:left;" name="supplierCode" id="supplierCode" hisValue="${supplierCode}" value="${supplierCode}" class="{required:true,messages:{required:'不能为空'}}"/>
					<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				<%}else{ %>
					<span>
						${supplierCode}
						<input style="float:left;" type="hidden" name="supplierCode" id="supplierCode" hisValue="${supplierCode}" value="${supplierCode}"/>
					</span>
				<%}%>
			</td>
			<td style="width:115px;">供应商名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input name="supplierName" value="${supplierName}" id="supplierName"/>
				<%}else{ %>
					<span>
						${supplierName}
						<input type="hidden" name="supplierName" value="${supplierName}" id="supplierName"/>	
					</span>
				<%}%>
			</td>
			<td>Lot No</td>
			<td>
				<input id="lotNo" name="lotNo" value="${lotNo}"/>
			</td>
		</tr>
		<tr>
			<td>采购单号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input id="orderNo" name="orderNo" value="${orderNo}"/>
				<%}else{ %>
					<span>${orderNo}</span>
				<%}%>
			</td>
			<td>行号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input id="entryId" name="entryId" value="${entryId}"/>
				<%}else{ %>
				<span>${entryId}</span>
				<%}%>				
			</td>
			<td>接受单号</td>
			<td>
				<input type="hidden" id="acceptNo" name="acceptNo" value="${acceptNo}"/>
				<% if("true".equals(changeView)){ %>				
				<input id="erpInspectionNo" name="erpInspectionNo" value="${erpInspectionNo}"/>
				<%}else{ %>
					<span>${erpInspectionNo}</span>
				<%}%>
			</td>
		</tr>
		<tr>
			<td><span style="color:red">*</span>进料日期</td>
			<td>
			<% if("true".equals(changeView)){ %>
				<input name="enterDate" id="enterDate" value="<s:date name="enterDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line" />
			<%}else{%>
				<span><s:date name="enterDate" format="yyyy-MM-dd HH:mm"/></span>
			<%}%>
			</td>
			<td><span style="color:red">*</span>进料数量</td>
			<td>
				<%
					Double val = (Double)ActionContext.getContext().getValueStack().findValue("stockAmount");
					if(val == null){
						val = 0.0d;
					}
					DecimalFormat df = new DecimalFormat("#.#####");
				%>
				<input  name="stockAmount" id="stockAmount" value="${stockAmount}" onblur="loadCheckItems()" class="{required:true,number:true,messages:{required:'来料数量不能为空'}}"/>
				
			</td>
			<td>单位</td>
			<td>
				<input name="units" id="units" value="${units}" />
			</td>
		</tr>
		<tr>
			<td>承认状态</td>
			<td>
				<input name="acknowledgeState" id="acknowledgeState" value="${acknowledgeState}"/>
			</td>
			<td>承认书版本</td>
			<td>
				<input name="acknowledgeVersion" id="acknowledgeVersion" value="${acknowledgeVersion}" />
			</td>
			<td><span style="color:red">*</span>物料类别</td>
			<td>
			  <%-- <s:if test="checkBomMaterialType!=null">
			  ${checkBomMaterialType}
			  </s:if>
			  <s:else>
			     <input name="checkBomMaterialType" id="checkBomMaterialType" value="${checkBomMaterialType}" class="{required:true,messages:{required:'必填'}}" />
			  </s:else> --%>
				  <input name="checkBomMaterialType" id="checkBomMaterialType" style="float:left;" value="${checkBomMaterialType}" readonly="readonly" class="{required:true,messages:{required:'必填'}}" />
				 <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="materialType()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>
		</tr>
		<tr>
			<td>特殊检验状态 </td>
			<td>
			<s:select list="sampleSchemeTypes" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  labelSeparator=""
					  name="sampleSchemeType"
					  emptyOption="false"></s:select>
				<%-- <s:iterator value="sampleSchemeTypes" id="option">
					<input id="sampleSchemeType${option.id}"  control="true" type="radio" <s:if test="%{#option.value.equals(sampleSchemeType)}"> checked="checked" </s:if> value="${option.value}" name="sampleSchemeType"/>
					<label for="sampleSchemeType${option.id}">${option.name}</label>
			    </s:iterator> --%>
			</td>
			<td><span style="color:red">*</span>产品阶段</td>
			<td>
				<s:select list="iqc_product_stage" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  labelSeparator=""
					  name="productStage"
					  emptyOption="true"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select>
			</td>
			<td></td>
			<td>
			</td>
			<%-- <td>免检单据</td>
			<td>
				<input id="exemptionNo" name="exemptionNo" value="${exemptionNo}"/>
			</td> --%>
		</tr>
		<tr>
			<td colspan="6">
				<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr>
						<td style="width:15%;">检验类别</td>
						<td style="width:20%;">检验内容</td>
						<td style="width:15%;">检验结果</td>
						<td style="width:30%;">描述</td>
						<td style="width:10%;">判定</td>
						<td style="width:10%;">录入员</td>
					</tr>
					<tr>
						<td rowspan="3">包装/资料核对</td>
						<td>
<%-- 							<s:iterator value="packing1" id="option">
								<input id="packingFir${option.name}" name="packingFir" fileName="packingFir" control="true" type="checkbox" onclick="getCheckedBoxVal();" <s:if test="%{packingFir.indexOf(#option.value)}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingFir${option.name}">${option.name}</label>
						    </s:iterator> --%>
							<s:checkboxlist
								theme="simple" list="packing1" listKey="value" listValue="name"
								name="packingFir" value="packingFir">
							</s:checkboxlist>
						</td>
						<td>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingFirResult"
							  emptyOption="false"></s:select>
						</td>
						<td rowspan="3">
							<textarea name="packingText" id="packingText" rows="" cols="">${packingText}</textarea>
						</td>
						<td rowspan="3">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  labelSeparator=""
							  id="packingResult"
							  name="packingResult"
							  emptyOption="false"></s:select>
						</td>
						<td rowspan="3">
							<input style="width:100px;" id="packingInspcetionMan"  loginname="true" name="packingInspcetionMan" value="${packingInspcetionMan}" onclick='selectObj("选择检验员","packingInspcetionMan","packingInspcetionMan");'/>
						</td>
					</tr>
					<tr>
						<td>
							<%-- <s:iterator value="packing2" id="option">
								<input id="packingSec${option.name}" fileName="packingSec" onclick="getCheckedBoxVal();"  control="true" type="checkbox" <s:if test="%{packingSec.indexOf(#option.value)}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingSec${option.name}">${option.name}</label>
						    </s:iterator> --%>
							<s:checkboxlist
								theme="simple" list="packing2" listKey="value" listValue="name"
								name="packingSec" value="packingSec">
							</s:checkboxlist>
						</td>
						<td>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingSecResult"
							  emptyOption="false"></s:select>
						</td>
					</tr>
					<tr>
						<td>
<%-- 							<s:iterator value="packing3" id="option">
								<input id="packingTre${option.id}"  control="true" type="checkbox" <s:if test="%{#option.value.equals(packingTre)}"> checked="checked" </s:if> value="${option.value}" name="packingTre"/>
								<label for="packingTre${option.id}">${option.name}</label>
						    </s:iterator> --%>
							<s:checkboxlist
								theme="simple" list="packing3" listKey="value" listValue="name"
								name="packingTre" value="packingTre">
							</s:checkboxlist>				  
						</td>
						<td>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingTreResult"
							  emptyOption="false"></s:select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">检验类别</td>
						<td style="text-align: center;">检验内容</td>
						<td style="text-align: center;">检验结果</td>
						<td style="text-align: center;">报告编号</td>
						<td colspan="2"  style="text-align: center;">描述</td>
					</tr>
					<tr>
						<td>HSF符合性检查</td>
						<td>
							<textarea rows="2" cols="2" name="hisInspectionItem">${hisInspectionItem}</textarea>
<%-- 							${hisInspectionItem} --%>
						</td>
						<td>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="hisPackingResult"
							  emptyOption="false"></s:select>
<%-- 							${hisPackingResult} --%>
<%-- 							<input id="hisPackingResult" name="hisPackingResult" value="${hisPackingResult}"/> --%>
<%-- 							${hisPackingResult} --%>
						</td>
						<td>
							<input name="hisReportNo" id="hisReportNo" value="${hisReportNo}" type="hidden" />
							 <input name="hisReportId" id="hisReportId" value="${hisReportId}" type="hidden" />
							 <span style="text-decoration:underline;" id="hsfSpan1" onclick="hisReportInput(this);">${hisReportNo}</span>
							 <span id="hsfSpan2" ></span>
						</td>
						<td colspan="2">
							<textarea rows="2" cols="2" name="hisText">${hisText}</textarea>
							<%-- ${hisText} --%>
						</td>
					</tr>
					<tr>
						<td>ORT测试</td>
						<td>
							<textarea rows="2" cols="2" name="ordInspectionItem">${ordInspectionItem}</textarea>
<%-- 							${ordInspectionItem} --%>
						</td>
						<td>
<%-- 							<input id="ordPackingResult" name="ordPackingResult" value="${ordPackingResult}"/> --%>
<%-- 							${ordPackingResult} --%>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="ordPackingResult"
							  emptyOption="false"></s:select>
						</td>
						<td>
							<input name="ordReportNo" id="ordReportNo" value="${ordReportNo}" type="hidden" />
							<input name="ordReportId" id="ordReportId" value="${ordReportId}" type="hidden" />
							<span style="text-decoration:underline;" id="ortSpan1" onclick="ordReportInput(this);">${ordReportNo}</span>
							<span id="ortSpan2" ></span>
						</td>
						<td colspan="2">
							<textarea rows="2" cols="2" name="ordText">${ordText}</textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><span style="color:red">*</span>检验日期</td>
			<td>
				<input name="inspectionDate" id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line"/>
				<input type="hidden" name="schemeStartDate" value="${schemeStartDateStr}"/>
			</td>
			<td><span style="color:red">*</span>审核人</td>
			<td>
				<input type="hidden" id="auditLoginMan" name="auditLoginMan" value="${auditLoginMan}"/>
				<input id="auditMan" name="auditMan" value="${auditMan}" onclick='selectObj("选择审核人","auditLoginMan","auditMan","loginName");' readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td><span style="color:red">*</span>上级审核人</td>
			<td>
				<input type="hidden" id="lastStateLoginMan" name="lastStateLoginMan" value="${lastStateLoginMan}"/>
				<input id="lastStageMan" name="lastStageMan" value="${lastStageMan}" onclick='selectObj("选择上级审核人","lastStateLoginMan","lastStageMan","loginName");' readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td colspan="4">
				<textarea name="remark" rows="2" cols="2">${remark}</textarea>
			</td>
			<td style="text-align: center;">
				<button class='btn' type="button" onclick="choiceWaitCheckItem();">
					<span><span><b class="btn-icons btn-icons-search"></b>领取待检验项目</span></span>
				</button>
			</td>
		</tr>
		<tr>
			<td colspan="6" style="padding:0px;" id="checkItemsParent">
				<div style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px;">
					<s:if test="%{sampleSchemeType == '免检'}">
						<span style="padding:10px 0px 10px 6px;font-weight:bold;">
							免检物料,没有检验项目
						</span>
					</s:if>
					<s:else>
						<%@ include file="check-items.jsp"%>
					</s:else>
				</div>
			</td>
		</tr>
		<tr>
			<td>外观合格数</td>
			<td>
				<input id="appearanceAmount" name="appearanceAmount" onchange="countRate('外观');" value="${appearanceAmount}" class="{number:true,messages:{number:'必填数字'}}"/>
			</td>
			<td>外观不良数</td>
			<td>
				<input id="appearanceUnAmount" name="appearanceUnAmount" onchange="countRate('外观');" value="${appearanceUnAmount}"  class="{number:true,messages:{number:'必填数字'}}"/>
			</td>
			<td>外观合格率</td>
			<td>
				<input id="appearanceAmountRate" name="appearanceAmountRate" value="${appearanceAmountRate}"   class="{number:true,messages:{number:'必填数字'}}"/>%
			</td>
		</tr>
		<tr>
			<td>特性合格数</td>
			<td>
				<input id="qualifiedAmount" name="qualifiedAmount" onchange="countRate('特性');" value="${qualifiedAmount}"  class="{number:true,messages:{number:'必填数字'}}"/>
			</td>
			<td>特性不良数</td>
			<td>
				<input id="unqualifiedAmount" name="unqualifiedAmount" onchange="countRate('特性');" value="${unqualifiedAmount}"  class="{number:true,messages:{number:'必填数字'}}"/>
			</td>
			<td>特性合格率</td>
			<td>
				<input id="qualifiedRate" name="qualifiedRate" value="${qualifiedRate}"  class="{number:true,messages:{number:'必填数字'}}"/>%
			</td>
		</tr>
		<tr>
			<td><span style="color:red">*</span>综合判定</td>
			<td style="border-top:0px;">
				<%-- <span>${inspectionConclusion}</span> --%>
				<%-- <input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"/> --%> 
				 <select name="inspectionConclusion" id="inspectionConclusion" class="{required:true,messages:{required:'必填'}}">
                         <option value=""></option>
                         <option value="OK" <c:if test='${"OK" eq inspectionConclusion}'>selected</c:if>>OK</option>
                         <option value="NG" <c:if test='${"NG" eq inspectionConclusion}'>selected</c:if>>NG</option>
                </select> 
               <%--  <s:select list="iqc_product_stage" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  labelSeparator=""
					  name="inspectionConclusion"
					  id="inspectionConclusion"
					  emptyOption="true"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select> --%>
			</td>
			<td>处理方式</td>
			<td>
				<input name="processingResult" id="processingResult" value="${processingResult}" />
			</td>
			<td >异常单号</td>
			<td style="text-align: ;"><input name="exceptionNo" id="exceptionNo" value="${exceptionNo}" type="hidden" />
				 <input name="exceptionId" id="exceptionId" value="${exceptionId}" type="hidden" />
				 <span style="text-decoration:underline;" onclick="exceptionInput(this);">${exceptionNo}</span>
			</td>	
		</tr>
		<tr>
			<td>上传附件</td>		
		   <td colspan='5'>
	   			<button class='btn' type="button" onclick="uploadFiles('showAttachmentFiles','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
	            <input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
	            <input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
	           <span id='showAttachmentFiles'></span>
		   </td>		
		</tr>
		<tr>
			<td>审核时间</td>
			<td><s:date name="auditTime" format="yyyy-MM-dd HH:mm"/></td>
			<td>审核意见</td>
			<td colspan="3">${auditText}</td>
		</tr>
		<tr>
			<td>上级审核时间</td>
			<td><s:date name="lastStateTime" format="yyyy-MM-dd HH:mm"/></td>
			<td>上级审核意见</td>
			<td colspan="3">${lastStateText}</td>
		</tr>
		<tr>
			<td>重检时间</td>
			<td><s:date name="recheckTime" format="yyyy-MM-dd HH:mm"/></td>
			<td>重检意见</td>
			<td colspan="3">${recheckText}</td>
		</tr>		
	</table>
