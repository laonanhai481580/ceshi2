<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.AbnormalReason"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	<%String isView = request.getParameter("isView");%>
	$(document).ready(function(){
		//$("#contentForm").validate();
		$("#tabs").tabs({});
	<%if(StringUtils.isEmpty(isView)){%>
		$("input","#contentForm").each(function(index,obj){
			if(obj.name&&obj.name.indexOf("_value")>-1){	
				$(obj).bind("keyup",function(){
					caculateSigma();
					caculateDiff();
				}).bind("change",function(){
					caculateSigma();
					caculateDiff();
				});
			}
		});
	<%}else{%>
		$(":input","#contentForm").attr("disabled","disabled");
	<%}%>
	});
	
	function caculateSigma(){
		var val = 0;
		var data = new Array();
		$("input","#contentForm").each(function(index,obj){
			if(obj.name&&obj.name.indexOf("_value")>-1){
				if(!isNaN(obj.value)&&!isNaN(parseFloat(obj.value))){
					val += parseFloat(obj.value);
					data.push(parseFloat(obj.value));
				}
			}
		});
		if(data.length != 0 && data){
			val = val/data.length;
		}
		$("#sigma").val(val);
	}
	
	function caculateDiff(){
		var val = 0;
		var data = new Array();
		$("input","#contentForm").each(function(index,obj){
			if(obj.name&&obj.name.indexOf("_value")>-1){
				if(!isNaN(obj.value)&&!isNaN(parseFloat(obj.value))){
					val += parseFloat(obj.value);
					data.push(parseFloat(obj.value));
				}
			}
		});
		var max = Math.max.apply(Math,data);
		var min = Math.min.apply(Math,data);
		var diff = parseFloat(max) - parseFloat(min);
		$("#maxValue").val(max.toFixed(2));
		$("#minValue").val(min.toFixed(2));
		$("#rangeDiff").val(diff.toFixed(2));
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#contentForm").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		//alert($.param(params));
		return params;
	}
	
	function submitForm(url){
		var params = getParams();
		$.showMessage("正在保存,请稍候... ...","custom");
		$.post(url,params,function(result){
			if(result.error){
				$.showMessage(result.message);
			}else{
				$("#id").val(result);
				$.showMessage("保存成功!");
			}
		},'json');
	}	
	function openReport(spcSampleId){
		if(spcSampleId!=null&&spcSampleId.length!=0&&spcSampleId!="undefined"){
			var url = "${mfgctx}/inspection/made-inspection/input-spc.htm?spcSampleId="+spcSampleId;
			$.post(url,{},function(result){
				if(result.error){
					alert(result.message);
				}else{
					window.open("${mfgctx}/inspection/made-inspection/input.htm?id="+result.reportId);
				}
			},'json');
			
		}
	}
	function openReportIQC(spcSampleId){
		if(spcSampleId!=null&&spcSampleId.length!=0&&spcSampleId!="undefined"){
			var url = "${iqcctx}/inspection-report/input-spc.htm?spcSampleId="+spcSampleId;
			$.post(url,{},function(result){
				if(result.error){
					alert(result.message);
				}else{
					window.open("${iqcctx}/inspection-report/input.htm?id="+result.reportId);
				}
			},'json');
			
		}
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn" id="btnDiv">
					<%if(StringUtils.isEmpty(isView)){%>
					<button class='btn' type="button" onclick="submitForm('${spcctx}/data-acquisition/maintenance-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					<%}%>
					<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
				<div id="opt-content">
				<form id="contentForm" method="post" action="" onsubmit="return false;">
					<input type="hidden" name="id" value="${spcSubGroup.id}"></input>
					<table style="width:100%;">
						<tr>
							<td style="width:20%;" align="left"><img src="${ctx}/images/stat/feature.png"/></td>
							<td style="width:80%;">
								<table style="width:100%;">
									<tr>
										<td align="right">质量特性:</td>
										<td colspan="3">
											<input name="featureName" style="width:93%;" value="${qualityFeature.name }" disabled="disabled"/>
											<input type="hidden" name="featureId" id="featureId" value="${qualityFeature.id }"/>
										</td>
									</tr>
									<tr>
										<td align="right">子组编号:</td>
										<td><input name="subGroupOrderNum" value="${spcSubGroup.subGroupOrderNum }" disabled="disabled"/></td>
										<td align="right">采集人员:</td>
										<td><input name="creator" value="${spcSubGroup.creator }" disabled="disabled"/></td>
									</tr>
									<tr>
										<td align="right">采集站点:</td>
										<td><input name="workStation" value="${spcSubGroup.workStation }" disabled="disabled"/></td>
										<td align="right">采集时间:</td>
										<td><input name="createdTime" value="${spcSubGroup.createdTime }" disabled="disabled"/></td>
									</tr>
									<tr>
										<td align="right">备注:</td>
										<td colspan="3"><textarea name="remark" style="width:93%;">${spcSubGroup.remark }</textarea></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<div id="tabs">
						<ul>
							<li><span><a href="#tabs1">子组信息</a></span></li>
						</ul>
						<div id="tabs1">
						<table class="form-table-border-left" style="width:100%;height: 250px;overflow-y:auto;">
							<tr>
								<td style="width: 50%;vertical-align: top;">
									<table class="form-table-outside-border">
										<tr>
											<td>均值:</td>
											<td><input name="sigma" id="sigma" value="${sigma }" disabled="disabled"/></td>
										</tr>
										<tr>
											<td>极差:</td>
											<td><input name="rangeDiff" id="rangeDiff" value="${spcSubGroup.rangeDiff }" disabled="disabled"/></td>
										</tr>
										<tr style="display: none;">
											<td>最大值:</td>
											<td><input name="maxValue" id="maxValue" value="${spcSubGroup.maxValue }" readonly="readonly"/></td>
										</tr>
										<tr style="display: none;">
											<td>最小值:</td>
											<td><input name="minValue" id="minValue" value="${spcSubGroup.minValue }" readonly="readonly"/></td>
										</tr>
									</table>
									<table class="form-table-border-left">
										<thead>
											<tr style="height:25px;background:#99CCFF;font-weight:bold;">
												<th style="width:50%;text-align: center;">序号</th>
												<th style="width:50%;text-align: center;">采样值</th>
											</tr>
										</thead>
										<tbody>${sampleItems}</tbody>
									</table>
								</td>
								<td style="width: 50%;vertical-align: top;">
									<table class="form-table-border-left">
										<thead>
											<tr style="height:25px;background:#99CCFF;font-weight:bold;">
												<th style="width:50%;text-align: center;">层别信息</th>
												<th style="width:50%;text-align: center;">取值</th>
												<th style="display: none;">代码</th>
												<th style="display: none;">取值方式</th>
											</tr>
										</thead>
										<tbody id="defective-goods-composing-table-body">${layerItems}</tbody>
									</table>
								</td>
							</tr>
						</table>
						</div>
					</div>
				</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>