<%@page import="com.ambition.supplier.entity.AppraisalReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var isUsingComonLayout=false;
		$(function(){
			//添加验证
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			$("#appraisalReportForm :input").attr("readonly","readonly");
			$("#appraisalReportForm :checkbox").attr("disabled",true);
			$("#appraisalReportForm :radio").attr("disabled",true);
		});
	 	function submitImprove(id){
	 		window.location = '${improvectx}/correction-precaution/called-input.htm?sampleAppraisalId='+id;
// 	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
// 	 			overlayClose:false,
// 	 			title:"改进页面",
// 	 			onClosed:function(){
// 	 			}
// 	 		});
	 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body" style="overflow-y:auto;">
		<div id="opt-content">
			<form action="" method="post" id="appraisalReportForm" name="appraisalReportForm" >
				<input type="hidden" id="id" name="id" value="${id}"></input>
				<table class="form-table-border-left" id="appraisal-table"	style="width:100%">
						<caption><h2>样件鉴定报告</h2></caption>
						<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${appraisalReport.code}</div></caption>
						<tr>
							<td width="10%"><span style="color:red;">*</span>供应商</td>
							<td colspan="3">
								<input id="supplierName" value="${appraisalReport.supplier.name}" style="width:90%;" readonly="readonly" onclick="selectSupplier()" class="{required:true}"/>
								<input type="hidden" name="supplierId" id="supplierId" value="${appraisalReport.supplier.id}"></input>
							</td>
							<td width="10%"><span style="color:red;">*</span>零件代号</td>
							<td width="15%">
								<input name="bomCodes" id="bomCodes" value="${appraisalReport.bomCodes}" style="width:90%;" readonly="readonly" onclick="selectSupplyProducts()" class="{required:true}"/>
							</td>
							<td width="10%"><span style="color:red;">*</span>零件名称</td>
							<td width="15%">
								<input name="bomNames" id="bomNames" value="${appraisalReport.bomNames}" style="width:90%;" readonly="readonly" onclick="selectSupplyProducts()" class="{required:true}"/>
							</td>
						</tr>
						<tr>
							<td  width="10%">零件等级</td>
							<td ><input type="text" name="bomLevel" id="bomLevel" value="${appraisalReport.bomLevel}"/></td>
							<td  width="10%">供货情况</td>
							<td ><input type="text" name="supplyCircumstances" value="${appraisalReport.supplyCircumstances}"/></td>
							<td  width="10%">供货数量</td>
							<td ><input type="text" name="supplyNumber" value="${appraisalReport.supplyNumber}"/></td>
							<td  width="10%">批次号</td>
							<td ><input type="text" name="batchNo" value="${appraisalReport.batchNo}"/></td>
						</tr>
						<tr>
							<td>鉴定原因</td>
							<td  colspan="7">
								${appraisalReasons}
							</td>
						</tr>
						<tr>
							<td  colspan="2">鉴定条件</td>
							<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="appraisalConditions">${appraisalReport.appraisalConditions}</textarea></td>
						</tr>
						<tr>
							<td  colspan="2">鉴定标准</td>
							<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="appraisalStandard">${appraisalReport.appraisalStandard}</textarea></td>
						</tr>
						<tr>
							<td  colspan="2">外观及尺寸检验报告</td>
							<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="inspectionReport">${appraisalReport.inspectionReport }</textarea></td>
						</tr>
						<tr>
							<td  colspan="2">材料报告</td>
							<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="materialReport">${appraisalReport.materialReport }</textarea></td>
						</tr>
						<tr>
							<td  colspan="2">
								供应商提供的文件（记录）
								<input type="hidden" name="hisAttachmentFiles" value='${appraisalReport.attachmentFiles}'></input>
								<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${appraisalReport.attachmentFiles}'></input>
							</td>
							<td  colspan="6" style="text-align:left"  id="showAttachmentFiles">
							</td>
						</tr>
						<tr>
							<td rowspan="9">性能报告</td>
							<td>启动项目</td>
							<td colspan="5">技术要求</td>
							<td>实际情况</td>
						</tr>
						<tr>
							<td>1:基本性能</td>
							<td colspan="5">PVC面料表面不应有玷污、色泽不均、裂缝、破洞、经纬条痕明显等缺陷，表面平整、清洁。</td>
							<td>
								<textarea rows="2" style="width:100%;" name="basicInfo">${appraisalReport.basicInfo}</textarea>
							</td>
						</tr>
						<tr>
							<td>2:耐热性</td>
							<td colspan="5">短期耐热性:100±2℃/5h～室温/2h为一个循环, 共进行2个循环，零部件在高低温时及试验后不应有变形、变色等异常现象发生。长期耐热性:将样品置于90℃的恒温箱中72h后不发生异常现象，零部件在高低温时及试验后不应有变形、变色等异常现象发生</td>
							<td>
								<textarea rows="2" style="width:100%;" name="resistHot">${appraisalReport.resistHot}</textarea>
							</td>
						</tr>
						<tr>
							<td>3:耐液性</td>
							<td colspan="5">试验溶剂为汽油、人造汗液、皮革清洗剂，试验后不得出现异常现象。</td>
							<td>
								<textarea rows="2" style="width:100%;" name="resistFluid">${appraisalReport.resistFluid}</textarea>
							</td>
						</tr>
						<tr>
							<td>4:剥离强度</td>
							<td colspan="5">至少2.94N/25mm</td>
							<td>
								<textarea rows="2" style="width:100%;" name="peelOffIntensity">${appraisalReport.peelOffIntensity}</textarea>
							</td>
						</tr>
						<tr>
							<td>5:耐寒性</td>
							<td colspan="5">耐寒性:试验温度-40℃，试验后零件不应有变形、变色等异常现象发生</td>
							<td>
								<textarea rows="2" style="width:100%;" name="resistCold">${appraisalReport.resistCold}</textarea>
							</td>
						</tr>
						<tr>
							<td>6:耐冷热交变性</td>
							<td colspan="5">'90℃/4h至室温/0.5h至-40℃/1.5h至室温/0.5h至50±2℃、 95％RH/2h至室温/0.5h为一个循环,共进行3个循环后不发生变形、变色等异常现象</td>
							<td>
								<textarea rows="2" style="width:100%;" name="resistColdAndHot">${appraisalReport.resistColdAndHot}</textarea>
							</td>
						</tr>
						<tr>
							<td>7:阻燃性</td>
							<td colspan="5">GB8410-2006《汽车内饰材料的燃烧特性》燃烧速度≤100mm/min</td>
							<td>
								<textarea rows="2" style="width:100%;" name="resistBurn">${appraisalReport.resistBurn}</textarea>
							</td>
						</tr>
						<tr>
							<td>性能判定结论</td>
							<td colspan="6">
								<textarea rows="2" style="width:100%;" name="capabilityConclusion">${appraisalReport.capabilityConclusion}</textarea>
							</td>
						</tr>
						<tr>
							<td rowspan="10">鉴定结果</td>
							<td colspan="2">1、鉴定件是否为工装生产？</td>
							<td colspan="5"><input type="radio" <s:if test="appraisalReport.isIndustry"> checked="checked" </s:if> value="true" name="isIndustry"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  <s:if test="!appraisalReport.isIndustry"> checked="checked" </s:if> type="radio" value="false" name="isIndustry"/>否</td>
						</tr>
						<tr>
							<td colspan="2">2、鉴定件是否在批量生产等同条件下进行？</td>
							<td colspan="5">
								<input type="radio" name="isSameBatchCondition" <s:if test="appraisalReport.isSameBatchCondition"> checked="checked" </s:if>value="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isSameBatchCondition" <s:if test="!appraisalReport.isSameBatchCondition"> checked="checked" </s:if> value="false"/>否</td>
						</tr>
							<tr>
							<td colspan="2">3、鉴定件是否合格？</td>
							<td colspan="5"><input type="radio" name="isStandard"  <s:if test="appraisalReport.isStandard=='合格'"> checked="checked" </s:if>value="合格"/>合格
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input  type="radio" name="isStandard"  <s:if test="appraisalReport.isStandard=='不合格'"> checked="checked" </s:if> value="不合格"/>不合格
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input  type="radio" name="isStandard"  <s:if test="appraisalReport.isStandard=='部分合格'"> checked="checked" </s:if> value="部分合格"/>部分合格</td>
						</tr>
						<tr>
							<td colspan="2">4、是否需要改进？</td>
							<td colspan="5"><input type="radio" name="isImprovement" <s:if test="appraisalReport.isImprovement"> checked="checked" </s:if>value="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isImprovement" <s:if test="!appraisalReport.isImprovement"> checked="checked" </s:if> value="false"/>否</td>
						</tr>
							<tr>
							<td colspan="2">合格项目为：</td>
							<td colspan="5">
								<textarea rows="2" style="width:100%;" name="standardItems">${appraisalReport.standardItems}</textarea>
							</td>
						</tr>
						<tr>
							<td colspan="7">但对以下项目需要继续进行试验或验证：</td>
						</tr>
						<tr>
							<td colspan="7"><textarea width="100%" name="needTestItems">${appraisalReport.needTestItems}</textarea></td>
						</tr>
						<tr>
							<td colspan="7">综合结论：</td>
						</tr>
						<tr>
							<td colspan="7"><textarea width="100%" name="appraisalConclusion">${appraisalReport.appraisalConclusion}</textarea></td>
						</tr>
						<tr>
							<td colspan="2">鉴定结果</td>
							<td colspan="5">
								<input type="radio" name="appraisalResult" <s:if test="appraisalReport.appraisalResult=='合格'"> checked="checked" </s:if>value="<%=AppraisalReport.RESULT_PASS%>"/>合格
								<input type="radio" name="appraisalResult" <s:if test="appraisalReport.appraisalResult=='不合格'"> checked="checked" </s:if> value="<%=AppraisalReport.RESULT_FAIL%>"/>不合格
							</td>
						</tr>
						<tr>
							<td colspan="6">不合格鉴定件需要进行整改的项目及要求</td>
							<td >整改期限要求：</td>
							<td ><input name="rectificationDate"  style="width: 70%" value="${appraisalReport.rectificationDateStr}" id="rectificationDate"/>
							<%-- <a class="small-button-bg" onclick='submitImprove(${id});'><span class="ui-icon ui-icon-info" style='cursor:pointer;'></span></a> --%>
							</td>
						</tr>
						<tr>
							<td colspan="8"><textarea width="100%" name="rectificationRequest">${appraisalReport.rectificationRequest}</textarea></td>
						</tr>
						<tr>
							<td><span style="color:red;">*</span>会签人员</td>
							<td>
								<input type="hidden" name="evaluationMemberLoginNames" id="evaluationMemberLoginNames" value="${appraisalReport.evaluationMemberLoginNames}" style="width:120px;"/>
								<input name="evaluationMembers" id="evaluationMembers" class="{required:true}" value="${appraisalReport.evaluationMembers}" style="width:95%;" onclick="selectObj('选择会签人员','evaluationMembers','evaluationMemberLoginNames',true,'loginName')"/>
							</td>
							<td><span style="color:red;">*</span>审核人员</td>
							<td>
							<input name="auditManLoginName" id="auditManLoginName" value="${appraisalReport.auditManLoginName}" type="hidden"/>
							<input name="auditMan" id="auditMan" value="${appraisalReport.auditMan}" readonly="readonly" class="{required:true}" id="auditMan" onclick="selectObj('选择审核人员','auditMan','auditManLoginName',true,'loginName')"/></td>
							<td><span style="color:red;">*</span>报告人</td>
							<td><input name="reportMan" value="${appraisalReport.reportMan}" readonly="readonly" onclick="selectObj('reportMan')" id="reportMan" class="{required:true}"/></td>
							<td><span style="color:red;">*</span>报告日期</td>
							<td><input  name="reportDate" value="${appraisalReport.reportDateStr}" id="reportDate" class="{required:true}"/></td>
						</tr>
						<tr>
							<td style="text-align:right;" colspan="8">
								<button class="btn" onclick="submitImprove(${appraisalReport.id});" type="button"><span><span><b class="btn-icons btn-icons-alarm"></b>发起改进</span></span></button>
							</td>
						</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>