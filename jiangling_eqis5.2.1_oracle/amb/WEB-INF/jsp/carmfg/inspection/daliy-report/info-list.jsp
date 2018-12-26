<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		
		
		//重写调整高度
		function contentResize(){
			var tableHeight=$('.ui-layout-center').height()-250;
			var tableWidth=_getTableWidth();
			
			jQuery("#inprocessInspectionList").jqGrid('setGridHeight',tableHeight);
			jQuery("#inprocessInspectionList").jqGrid('setGridWidth',tableWidth);
		
		}
		
		function $addGridOption(jqGridOption){
			jqGridOption.postData.inspectionPoint=$("#inspectionPoint").val();
			jqGridOption.postData.reportId=$("#reportId").val();
			
		}
		
		function closeBtn(){
			window.parent.$.colorbox.close();
		}
		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="workshop"  value="${workshop}"/>
	<input type="hidden" id="inspectionPoint"  value="${inspectionPoint}"/>
	<input type="hidden" id="reportId" value="${reportId}"/>
	
	<div class="ui-layout-center">
			<div class="opt-body">
			<aa:zone name="main">
			
				<div class="opt-btn">
				<button  class='btn' type="button" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				</div>
				<s:if test="error">
					<div id="opt-content">
						<h2>对不起，没有符合条件的生产日报表！</h2>
					</div>
				</s:if>
				<s:else>
				<div id="opt-content">
					
					<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
					<form id="contentForm" name="contentForm" method="post"  action="">
							<table class="form-table-without-border" style="width: 100%;">
							<caption style="text-align:center;height: 35px"><h2>${inspectionPoint}生产日报表</h2></caption>
								<tr>
									<td style="width:8%">&nbsp;</td>
									<td style="width:22%">&nbsp;</td>
									<td style="width:6%">&nbsp;</td>
									<td style="width:25%">&nbsp;</td>
									<td style="width:6%">编号：</td>
									<td style="width:25%;text-align: left">${reportNO}</td>
									
								</tr>
								<tr>
									<td style="width:8%">生产日期：</td>
									<td style="width:15%;text-align: left" >${produceDate}</td>
									<td style="width:8%">班组：</td>
									<td style="width:17%;text-align: left">${workGroup}</td>
									<td style="width:8%">生产线：</td>
									<td style="width:17%;text-align: left">${productionLine}</td>
								</tr>
								
							</table>
							
							<grid:jqGrid gridId="inprocessInspectionList" url="${mfgctx}/inspection/daliy-report/info-datas.htm" code="MFG_DALIY_REPORT_INFO" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
							<script type="text/javascript">
								$(document).ready(function(){
									var colCodes = $("#colCode").val().split(',');
									var firstCol = colCodes[0];
									var colNumbers = colCodes.length-1;
									$("#inprocessInspectionList").jqGrid('setGroupHeaders', {
										  useColSpanStyle: true, 
										  groupHeaders:[
											{startColumnName:firstCol , numberOfColumns: colNumbers, titleText: '不良明细'}
										  ]
										});
									$("#inprocessInspectionList").jqGrid('setGridParam',{gridComplete:contentResize});
									
								});
							</script>
							
					 </form>
					</div>
				</s:else>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>