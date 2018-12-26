<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function contentResize(){
		var tableHeight=$('.ui-layout-center').height()-210;
// 		var tableWidth=_getTableWidth();
		var tableWidth=$('.ui-layout-center').width()-10;
		jQuery("#inspectionRecordList").jqGrid('setGridHeight',tableHeight);
		jQuery("#inspectionRecordList").jqGrid('setGridWidth',tableWidth);
	
	}
	function closeBtn(){
		window.parent.$.colorbox.close();
	}
	function checkAmount(){
		
	}
	function initSearch(){
		
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<s:if test="error">
				<div id="opt-content">
					<h2>对不起，没有符合条件的检验/检查数据单！</h2>
				</div>
			</s:if>
			<s:else>
			<div style="height: 30px;text-align: center"><h2>检验/检查数据单</h2></div>
			<%-- <div style="width:95%;text-align: right">编号：${inspectionRecord.formNo}</div> --%>
			<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
			<table class="form-table-without-border" id="rejectTable" style="width:100%;margin: auto;">
				<tr>
					<td style="width:10%">&nbsp;</td>
					<td style="width:20%">&nbsp;</td>
					<td style="width:10%">&nbsp;</td>
					<td style="width:20%">&nbsp;</td>
					<td style="width:10%">编&nbsp;&nbsp;号：</td>
					<td style="width:20%;text-align: left">${batchNo}</td>
				</tr>
				<tr>
					<td style="width:10%">生产日期：</td>
					<td style="width:20%;text-align: left">${inspectionRecord.inspectionDate}</td>
					<td style="width:10%">采集点：</td>
					<td style="width:20%;text-align: left">${inspectionRecord.inspectionPoint}</td>
					<td style="width:10%">生产线：</td>
					<td style="width:20%;text-align: left">${inspectionRecord.productionLine}</td>
				</tr>
				<%-- <tr>
					<td style="width:13%;text-align:right;">日期:</td>
					<td style="width:20%">${inspectionRecord.inspectionDate}</td>
					<td style="width:13%;text-align:right;">采集点:</td>
					<td style="width:20%">${inspectionRecord.inspectionPoint}</td>
					<td style="width:13%;text-align:right;">制令号:</td>
					<td style="width:20%">${inspectionRecord.planNo}</td>
				</tr>
				<tr>
					<td style="width:13%;text-align:right;">产品类型:</td>
					<td style="width:20%">${inspectionRecord.productModel}</td>
					<td style="width:13%;text-align:right;">产品型号:</td>
					<td style="width:20%">${inspectionRecord.modelSpecification}</td>
					<td style="width:13%;text-align:right;">检查数:</td>
					<td style="width:20%">${inspectionRecord.inspectionAmount}</td>
				</tr>
				<tr>
					<td style="width:14%;text-align:right;">良品数:</td>
					<td style="width:20%">${inspectionRecord.qualifiedAmount}</td>
					<td style="text-align:right;">不良数:</td>
					<td >${inspectionRecord.unqualifiedAmount}</td>
					<td style="text-align:right;">不良率(%):</td>
					<td>${inspectionRecord.unqualifiedRate}</td>
				</tr>
				<tr>
					<td colspan="6" style="text-align:center;clear:both;padding:8px;" align="center">
						<table width=100%>
							<caption style="font-weight: bold; padding-bottom:8px;">不良项目</caption>
							<thead>
								<tr>
									<th width=190></th>
									<th width=200 style="text-align:center">项目名称</th>
									<th style="padding-left:35px;">数量</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="#inspectionRecord.defectiveItems" id="badItem">
									<tr>
										<td>&nbsp;</td>
										<td style="text-align:center">${badItem.name}</td>
										<td style="padding-left:42px;">${badItem.value}</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="2" style="text-align:right;padding-right:30px;">合计:</td>
									<td style="padding-left:42px;">${inspectionRecord.unqualifiedAmount}</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr> --%>
			</table>
			<grid:jqGrid gridId="inspectionRecordList" url="${mfgctx}/manu-analyse/info-datas.htm?id=${id}" code="MFG_INSPECTION_RECORDS2" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
			<script type="text/javascript">
				$(document).ready(function(){                
					var colCodes = $("#colCode").val().split(',');
					var firstCol = colCodes[0];
					var colNumbers = colCodes.length-1;
					 $("#inspectionRecordList").jqGrid('setGroupHeaders', {
						  useColSpanStyle: true, 
						  groupHeaders:[
							{startColumnName:firstCol , numberOfColumns: colNumbers, titleText: '不良明细'}
						  ]
					}); 
					$("#inspectionRecordList").jqGrid('setGridParam',{gridComplete:contentResize});
				});
			</script>
			</s:else>
		</div>
	</div>
</body>
</html>