<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@page import="com.ambition.util.erp.schedule.ScheduleJob"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
	var topMenu ='';
	function updates(url,title,max){
		if(confirm("确定要执行操作吗?")){
			$("#message").html("正在执行数据同步,请稍候... ...");
			$(".opt-btn button").attr("disabled",true);
			var progressWin = progressbar(title,max);
			$.post(url,{},function(result){
				$("#message").html("");
				$("button").attr("disabled","");
				progressWin.dialog("close");
				alert(result.message);
				if(!result.error){
					window.location.reload();
				}
			},'json');
		}
	}
	function updatesByTime(url,title,max){
			var startStr = prompt("请输入开始月份,格式如 2014-01:");
			if(!startStr){
				alert("开始月份不能为空!");
				return;
			}
			if(!/^\d{4}-\d{2}$/g.test(startStr)){
				alert("开始月份格式不正确,示例如:2014-01!");
				return;
			}
			var endStr = prompt("请输入结束月份,格式如 2014-01:");
			if(!endStr){
				alert("结束月份不能为空!");
				return;
			}
			if(!/^\d{4}-\d{2}$/g.test(endStr)){
				alert("结束月份格式不正确,格式如 2014-01!");
				return;
			}
			if(confirm("确定要执行操作吗?")){
				$("#message").html("正在执行数据同步,请稍候... ...");
				$(".opt-btn button").attr("disabled",true);
				var progressWin = progressbar(title,max);
				url += "?startMonth=" + startStr + "&endMonth=" + endStr;
				$.post(url,{},function(result){
					$("#message").html("");
					$("button").attr("disabled","");
					progressWin.dialog("close");
					alert(result.message);
					if(!result.error){
						window.location.reload();
					}
				},'json');
			}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="integration_sec";
	var thirdMenu="integration_third";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/integration-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/integration-input-third-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="line-height:30px;">
				<span style="font-weight:bold;font-size:16px;float:left;">手动同步集成数据</span><span style="margin-left:10px;color:red;float:left;" id="message"></span>
			</div>
			<div id="opt-content" style="text-align:center;">
				<table class="form-table-border-left" style="width:100%;margin:2px auto;">
					<thead style="background:#CCCC99;font-weight:bold;">
						<tr>
							<td style="padding-left:4px;width:180px;">名称</td>
							<td style="width:20%;">&nbsp;最近执行时间</td>
							<td style="width:30%;">&nbsp;执行策略</td>
							<td>操作</td>
						</tr>
					</thead>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">产品及物料清单</span>
						 </td>
						 <td>&nbsp;${bom}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getBomScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-material-from-erp.htm','从OR导入产品及物料清单',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">来料检验单</span>
						 </td>
						 <td>&nbsp;${iqc}</td>
						 <td>&nbsp;每隔&nbsp;<b><%=PropUtils.getProp("schedule.properties","iqc.interval")%></b>&nbsp;分钟自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-iqc-from-erp.htm','从OR导入来料检验单',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">供应商信息</span>
						 </td>
						 <td>&nbsp;${supplier}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getSupplierScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-supplier-from-erp.htm','从OR导入供应商清单',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">发料记录</span>
						 </td>
						 <td>&nbsp;${sentOutRecord}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getSentOutRecordScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-sent-out-record-from-erp.htm','从OR导入发料记录',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">赠品仓数</span>
						 </td>
						 <td>&nbsp;${supplierMaterialEvaluate}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getSupplierMaterialEvaluateScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/integration-supplier-evaluate-from-erp.htm','从OR导入赠品数据',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">供应商邮箱</span>
						 </td>
						 <td>&nbsp;${supplier_email}</td>
						 <td>&nbsp;手动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-supplier-email-erp.htm','从OR导入供应商邮箱数据',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					<security:authorize ifAnyGranted="import-dcrn-from-qis">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">Dcr/N</span>
						 </td>
						 <td>&nbsp;${dcrnEmail }</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getDcrnReportScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-dcrn-from-qis.htm','DCR/N提醒',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>
					<security:authorize ifAnyGranted="import-ecn-from-qis">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">ECN</span>
						 </td>
						 <td>&nbsp;${ecnEmail }</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getEcnReportScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-ecn-from-qis.htm','ECN提醒',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>
					<security:authorize ifAnyGranted="import-iqc-notice">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">检验任务超期通知</span>
						 </td>
						 <td>&nbsp;${iqcNotice }</td>
						  <td>&nbsp;每隔&nbsp;<b><%=PropUtils.getProp("schedule.properties","iqcNotice.interval")%></b>&nbsp;小时自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-iqc-notice.htm','检验任务超期提醒',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>
					<security:authorize ifAnyGranted="import-gsm-warming-qis">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">计量器具校验提前预警</span>
						 </td>
						 <td>&nbsp;${gsmWarming}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getGsmWarmingScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-gsm-warming.htm','计量器具校验提前预警',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>
					<security:authorize ifAnyGranted="import-ipqc-auto-qis">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">IPQC自动检测提交</span>
						 </td>
						 <td>&nbsp;${ipqcAutoSubmit}</td>
						<td>&nbsp;每隔&nbsp;<b><%=PropUtils.getProp("schedule.properties","ipqc.interval")%></b>&nbsp;分钟自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-ipqc-auto.htm','IPQC',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>	
					<security:authorize ifAnyGranted="spc-synchro-Datas">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">检验数据同步到spc</span>
						 </td>
						 <td>&nbsp;${spc}</td>
						 <td>&nbsp;每隔&nbsp;<b><%=PropUtils.getProp("schedule.properties","spc.time")%></b>&nbsp;分钟自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/synchro-spc.htm','检验数据同步',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>
					<security:authorize ifAnyGranted="import-gp-average-material">
					<tr style="height:29px;">
						 <td style="padding-left:4px;">
						 	<span style="font-size:16px;font-weight:bold;">环保均质材料过期提醒</span>
						 </td>
						 <td>&nbsp;${gpAverageMaterial}</td>
						 <td>&nbsp;每天&nbsp;<b><%=DateUtil.formateDateStr(ScheduleJob.getGpAverageMaterialScheduleDate(),"HH:mm")%></b>&nbsp;自动执行</td>
						 <td style="padding-left:6px;">
						 	<button  class='btn' type="button" onclick="updates('${integrationctx}/import-gp-average-material.htm','均质材料过期提醒',150)"><span><span><b class="btn-icons btn-icons-play"></b>立即同步</span></span></button>
						 </td>
					</tr>
					</security:authorize>						
				</table>
			</div>
		</div>
	</div>
</body>
</html>