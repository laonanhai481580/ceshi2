<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String userName=ContextUtils.getUserName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$( "#tabs" ).tabs();
			inputDisabled($("#unqualifiedSource"));
			inputDisabled($("input[name='defectType']"));
		});
		isUsingComonLayout=false;
		function closeBtn(){
			window.parent.$.colorbox.close();
		}
		
		function inputDisabled(obj){
			$(obj).attr("disabled","disabled").css("color","#333");
		}

		//流转历史和表单信息切换
		function changeViewSet(opt) {
			if (opt == "basic") {
				aa.submit("defaultForm1", "${mfgctx}/defective-goods/ledger/monitor-detail.htm",'btnZone,viewZone');
			} else if (opt == "history") {
				aa.submit("defaultForm1", "${mfgctx}/defective-goods/ledger/history.htm",'btnZone,viewZone');
			}
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
	<form id="defaultForm1" action="">
		<div>
			<input type="hidden" name="id" id="id" value="${id}" />
			<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
		</div>
	</form>
	<aa:zone name="main">
		<div class="opt-btn">
			<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
		</div>
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="changeViewSet('basic');">表单信息</a>
				</li>
				<li><a href="#tabs-1" onclick="changeViewSet('history');">流转历史</a>
				</li>
			</ul>
			<div id="tabs-1">
				<aa:zone name="viewZone">
				<div style="height: 30px;text-align: center"><h2>不合格品处理单</h2></div>
					<div style="height: 25px;width:99%;text-align: right;">
						<span>No.<span style="padding: 1px 11px 1px 1px;">${defectiveGoodsProcessingForm.formNo}</span></span><input type="hidden" name="formNo" style="padding:0 4px;" value="${defectiveGoodsProcessingForm.formNo}" readonly="readonly"/>
					</div>

					<table class="form-table-border-left" id="rejectTable"	style="width:100%;margin: auto;">
						<tr>
							<td style="text-align:center;" colspan="6">
								<s:checkboxlist name="defectType" value="defectiveGoodsProcessingForm.defectType" theme="simple" list="defectTypes" listKey="value" listValue="name"/>
							</td>
						</tr>
						<tr>
							<td style="width:14%;"><font color="red">*</font>送检单位</td>
							<td style="width:20%">
								<input type="text" name="defectiveGoodsProcessingForm.inspectionDept" id='inspectionDept' value="${defectiveGoodsProcessingForm.inspectionDept}"  readonly="readonly"  style="width:90%;max-width:148px;"/>
							</td>
							<td colspan="2"></td>
							<td style=""><font color="red">*</font>统计归属</td>
	                       <td>
	                       		<input name="defectiveGoodsProcessingForm.industryType" value="${defectiveGoodsProcessingForm.industryType}"/>
	                       </td>
						</tr>
						<tr>
							<td style="width:13%;"><font color="red">*</font>不合格来源</td>
							<td style="width:20%">
								<s:select list="unqualifiedSourceOptions" 
										  listKey="value" 
										  listValue="name"
										  theme="simple"
										  name="unqualifiedSource" ></s:select>
							</td>
							<td style="width:13%;"><font color="red">*</font>检验单号</td>
							<td style="width:20%">
								<input style="width:90%;max-width:148px;" type="text" name="defectiveGoodsProcessingForm.qualityTestingReportNo" value="${defectiveGoodsProcessingForm.qualityTestingReportNo}" readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
							</td>
							<td style="width:13%;">缺陷分类</td>
							<td style="width:20%">
                       			<input name="defectiveGoodsProcessingForm.defectLevel" id="defectLevel" value="${defectiveGoodsProcessingForm.defectLevel}" readonly="readonly"></input>
							</td>
						</tr>
						<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格品明细</td></tr>
						<tr>
							<td colspan="6" style="padding:0;">
								<table class="form-table-border" style="table-layout:fixed;border:0;">
									<tr>
								      	<td style="width:6%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
								      	<td style="width:5%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
								      	<td style="width:10%;border-top:0px;border-bottom:0px;">明细序号</td>
								      	<td style="width:15%;border-top:0px;border-bottom:0px;">产品代码</td>
								      	<td style="width:15%;border-top:0px;border-bottom:0px;">产品型号</td>
								      	<td style="width:8%;border-top:0px;border-bottom:0px;">送检数</td>
										<td style="width:8%;border-top:0px;border-bottom:0px;">抽检数</td>
										<td style="width:8%;border-top:0px;border-bottom:0px;">不合格数</td>
								      	<td style="width:10%;border-top:0px;border-bottom:0px;border-right:0px;">送检日期</td>  
							        </tr>
							        <%int bcount=0; %>
							        <s:iterator value="detailItemList" var="detailItemList" id="detailItemList" status="st">
									    <tr class="detailItemTr">
									    	<td style="border-bottom:0px;border-left:0px;">
									    		<div style="margin:0 auto;width: 42px;">
									      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('detailItemTotal','detailItemTr',this)" title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
												<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('detailItemTotal','detailItemTr',this)" title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
												</div>
									 		</td>
									 		<td style="border-bottom:0px;text-align:center;"><%=bcount+1%></td>
									      	<td style="border-bottom:0px;"><input style="width:80%;" name="detailNo-<%=bcount%>" id="detailNo-<%=bcount%>" value="${detailNo}" readonly="readonly"/></td>
									      	<td style="border-bottom:0px;">
									      		<input style="width:90%;max-width:148px;" name="productCode-<%=bcount%>" id="productCode-<%=bcount%>" onclick="selectComponentCode(<%=bcount%>)" value="${productCode}"  readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
									      		<input name="productName-<%=bcount%>" id="productName-<%=bcount%>" value="${productName}" type="hidden" readonly="readonly"/>
									      		<input name="productModel-<%=bcount%>" id="productModel-<%=bcount%>" value="${productModel}" type="hidden" readonly="readonly"/>
									      	</td>
									      	<td style="border-bottom:0px;"><input style="width:90%;max-width:148px;" name="modelSpecification-<%=bcount%>" id="modelSpecification-<%=bcount%>" onclick="selectComponentCode(<%=bcount%>)" value="${modelSpecification}" class="{required:true,messages:{required:'必填'}}" readonly="readonly"/></td>
									      	<td style="border-bottom:0px;"><input style="width:70%" name="tatolNum-<%=bcount%>" id="tatolNum-<%=bcount %>" value="${tatolNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}" readonly="readonly"/></td>
									      	<td style="border-bottom:0px;"><input style="width:70%" name="inspectNum-<%=bcount%>" id="inspectNum-<%=bcount %>" value="${inspectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}" readonly="readonly"/></td>
											<td style="border-bottom:0px;"><input style="width:70%" name="defectNum-<%=bcount%>" id="defectNum-<%=bcount %>" value="${defectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}" readonly="readonly" /></td>
									      	<td style="border-bottom:0px;border-right:0px;"><input style="width:80%" readonly="readonly" name="inspectDate-<%=bcount%>" id="inspectDate-<%=bcount%>" value="<s:date name='inspectDate' format='yyyy-MM-dd'/>" class="{required:true,messages:{required:'必填'}}" readonly="readonly"/></td>
	                                	</tr>
	                                	<%bcount++; %>
							        </s:iterator>
						        </table>
						        <input type="hidden" name="detailItemTotal" id="detailItemTotal" value="<%=bcount%>" readonly="readonly"/>
						        <input type="hidden" name="detailItems" id="detailItems" readonly="readonly"/>
						    </td>
						</tr>
						<tr>
							<td colspan="6">
							<table style="width:100%;">
								<tr>
									<td style="border:0; font-weight: bold;">
										<span>不良描述:</span>
									</td>
								</tr>
								<tr>
									<td style="border:0;">
										<textarea id="unqualifiedDescription" rows="5" style="border: 1px solid #4C9DC5;" name="defectiveGoodsProcessingForm.unqualifiedDescription" readonly="readonly">${defectiveGoodsProcessingForm.unqualifiedDescription}</textarea>
									</td>
								</tr>
								<tr >
									<td style="border:0;">
										<div style="float:right;">
											<span>报告人：</span><input style="width:120px;" readonly="readonly" onclick="selectObj('请选择报告人', this, 'MAN_DEPARTMENT_TREE');" type="text" id="inspector" name="inspector" value="<s:if test="inspector">${inspector}</s:if><s:else><%=userName%></s:else>"></input>
											<span style="padding-left:20px;">日期：</span><input rea style="margin-right:20px;width:120px;" readonly="readonly" type="text" name="reportDate" id="reportDate" value='<s:date name='reportDate' format='yyyy-MM-dd'/>' class="{required:true,messages:{required:'必填'}}"></input>
										</div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="6" >
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">质量部处理建议:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<s:checkboxlist cssStyle="margin-left:8px;" name="defectiveGoodsProcessingForm.qualityOpinion" value="defectiveGoodsProcessingForm.qualityOpinion" theme="simple" list="qualityOpinions" listKey="value" listValue="name" disabled="true"/>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<div style="float:right;">
												<span style="width: 160px;padding-right:20px;display: inline-block;">处理人：${defectiveGoodsProcessingForm.qualityDirector}</span><input type="hidden" id="qualityDirector" name="defectiveGoodsProcessingForm.qualityDirector" value='${defectiveGoodsProcessingForm.qualityDirector}' readonly="readonly"></input>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">执行单位签署意见:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<input type="checkbox" name="defectiveGoodsProcessingForm.isAgreeQO" disabled="disabled" id="isAgreeQO-1" <s:if test="defectiveGoodsProcessingForm.isAgreeQO==0">checked="checked"</s:if> value="0" /><label for="isAgreeQO-1">同意质量部处理意见，</label>
											<span>执行单位填写完成日期</span><input readonly="readonly" id="completeDate" style="width:90px;padding-left:4px;margin-left:2px;" value="<s:date name='defectiveGoodsProcessingForm.completeDate' format='yyyy-MM-dd'/>" name="defectiveGoodsProcessingForm.completeDate" type="text"/>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<input type="checkbox" name="defectiveGoodsProcessingForm.isAgreeQO"  disabled="disabled" id="isAgreeQO-2" <s:if test="defectiveGoodsProcessingForm.isAgreeQO==1">checked="checked"</s:if> value="1" /><label for="isAgreeQO-2">申请让步接收？</label>
											<span>原因描述:</span>
										</td>
									</tr>
									<tr>
										<td style="border:0;">
											<textarea rows="5" style="border: 1px solid #4C9DC5;" readonly="readonly" name="defectiveGoodsProcessingForm.concessionReason" id="concessionReason">${defectiveGoodsProcessingForm.concessionReason}</textarea>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">让步接收评审(风险评估)(并行评审)申请部门填写:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<span>填写与会人员:</span>
											<input name="defectiveGoodsProcessingForm.participants" id="participants" value="${defectiveGoodsProcessingForm.participants}" readonly="readonly" style="width:60%;max-width: 363px;"/>
											<span style="color:#999;font-size:12px;">(以英文","隔开！)</span>
										</td>
									</tr>
									<tr>
										<td style="border:0;">
											<s:checkboxlist cssStyle="margin-left:8px;" disabled="true" name="defectiveGoodsProcessingForm.concessionResult" value="defectiveGoodsProcessingForm.concessionResult" theme="simple" list="agreeConcessionOptions" listKey="value" listValue="name"/><label>风险评估意见:</label>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<textarea id="concessionOpinion"   readonly="readonly" rows="5" style="border: 1px solid #4C9DC5;" name="defectiveGoodsProcessingForm.concessionOpinion">${defectiveGoodsProcessingForm.concessionOpinion}</textarea>
										</td>
									</tr>
									<tr>
										<td style="border:0px;">
											<span style="color:#777;font-size:13px;">(上传评审会议附件)</span>
											<button type="button" disabled="disabled" class='btn' id="uploadFile" onclick="uploadFiles('showAttachment','reviewAttachment');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
											<input type="hidden" name="hisAttachment" value='${defectiveGoodsProcessingForm.reviewAttachment}' readonly="readonly"></input>
											<input type="hidden" name="reviewAttachment" id="reviewAttachment" value='${defectiveGoodsProcessingForm.reviewAttachment}' readonly="readonly"></input>
											<span id="showAttachment"></span>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
							<table style="width:100%;">
								<tr>
									<td style="border:0; font-weight: bold;">
										<span>质量部最终处理方式(含后期整改意见):</span>
									</td>
								</tr>
								<tr>
									<td style="border:0;">
										<textarea id="finalApproach" rows="5" style="border: 1px solid #4C9DC5;" name="defectiveGoodsProcessingForm.finalApproach" readonly="readonly">${defectiveGoodsProcessingForm.finalApproach}</textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格损失鉴定</td></tr>
						<tr>
							<td colspan="6" style="padding:0;">
								<table class="form-table-border" style="table-layout:fixed;border:0;">
									<thead>
										<tr>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
									      	<td style="width:20%;border-top:0px;border-bottom:0px;">质量成本科目</td>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;">金额</td>
									      	<td style="width:20%;border-top:0px;border-bottom:0px;border-right:0px;">责任单位</td>  
								        </tr>
							        </thead>
							        <%int ccount=0; %>
							        <tbody>
							        <s:iterator value="composingItemList" var="composingItemList" id="composingItemList" status="st">
									    <tr class="composingItemTr">
									    	<td style="border-bottom:0px;border-left:0px;">
									    		<div style="margin:0 auto;width: 42px;">
									      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('composingItemTotal','composingItemTr',this)" title="添加损失鉴定"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
												<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('composingItemTotal','composingItemTr',this)" title="删除损失鉴定"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
												</div>
									 		</td>
									 		<td style="border-bottom:0px;text-align:center;"><%=ccount+1%></td>
									      	<td style="border-bottom:0px;">
									      		<input style="width:60%;"  name="name-<%=ccount%>" id="name-<%=ccount%>" value="${name}" readonly="readonly"/>
									      		<input type="hidden" name="code-<%=ccount%>" id="code-<%=ccount%>" value="${code}" readonly="readonly"/>
									      	</td>
									      	<td style="border-bottom:0px;"><input style="width:50%;" name="value-<%=ccount%>" id="value-<%=ccount%>" value="${value}" readonly="readonly"/></td>
									      	<td style="border-bottom:0px;border-right:0px;"><input style="width:50%;" readonly="readonly" name="departmentName-<%=ccount%>" id="departmentName-<%=ccount %>" value="${departmentName}"/></td>
	                                	</tr>
	                                	<%ccount++; %>
							        </s:iterator>
							        </tbody>
						        </table> 
						        <input type="hidden" name="composingItemTotal" id="composingItemTotal" value="<%=ccount%>" readonly="readonly"/>
						        <input type="hidden" name="composingItems" id="composingItems" readonly="readonly"></input>
						    </td>
						</tr>
						<tr>
							<td colspan="6" style="height: 24px;"><span style="padding:0 0 0 4px;">是否需要8D单:</span>
								<s:checkboxlist name="defectiveGoodsProcessingForm.need8d" disabled="true"  value="defectiveGoodsProcessingForm.need8d" theme="simple" list="need8dOptions" listKey="value" listValue="name"/>
								<span id="show8D" style="display:none;">8D单号<input type="text" name="defectiveGoodsProcessingForm.need8dNo" value="${defectiveGoodsProcessingForm.need8dNo}" readonly="readonly"/></span>
							</td>
						</tr>
					</table>
				</aa:zone>
			</div>
		</div>
		</aa:zone>
	</div>
</body>
</html>