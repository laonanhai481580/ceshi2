<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	
	<script type="text/javascript">
	function contentResize(){
		var height = $(window).height() - $('#searchTable').height() - 290;
		$("#gridList").jqGrid("setGridHeight",height);
		$("#gridList").jqGrid("setGridWidth",$("#searchTable").width());
	}
	
	var params = null;
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		createGrid();
		contentResize();
	});
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#opt-content").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()&&jObj.val()!=""){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	//创建表格
	function createGrid(){
		params = getParams();
		$("#gridList").jqGrid({
			rownumbers:true,
			gridComplete:function(){},
			loadComplete:function(){},
			postData : params,
			rowNum:15,
			datatype: "json",
			url:'${spcctx}/statistics-analysis/list-datas.htm',
			prmNames:{
				rows:'page.pageSize',
				page:'page.pageNo',
				sort:'page.orderBy',
				order:'page.order'
			},
			colNames:['id','质量特性','异常','子组号','检测时间','原因','原因时间','责任人','改善措施','改善时间','责任人','分析原因结束时间','采取措施时间'], 
			colModel:[
				{name:'id',index:'id',hidden:true},
				{name:'qualityFeature.name',index:'qualityFeature.name'},
				{name:'name',index:'name'},
				{name:'num',index:'num'},
				{name:'occurDate',index:'occurDate',formatter:'date',formatoptions:{newformat:'Y-m-d H:i:s'}},
				{name:'reason',index:'reason'},
				{name:'analyzeStartTime',index:'analyzeStartTime',formatter:'date',formatoptions:{newformat:'Y-m-d H:i:s'}},
				{name:'reasonPersonLiable',index:'reasonPersonLiable'},
				{name:'measure',index:'measure'},
				{name:'improveTime',index:'improveTime',formatter:'date',formatoptions:{newformat:'Y-m-d H:i:s'}},
				{name:'reasonPersonLiable',index:'reasonPersonLiable'},
				{name:'analyzeEndTime',index:'analyzeEndTime',formatter:'date',formatoptions:{newformat:'Y-m-d H:i:s'}},
				{name:'measureTime',index:'measureTime',formatter:'date',formatoptions:{newformat:'Y-m-d H:i:s'}}
			],
			autowidth: true,
// 		   	shrinkToFit: true,
			multiselect:false,
		   	pager:'#pager'
		});
		$("#gridList").jqGrid('setGroupHeaders', {
			useColSpanStyle: true, 
			groupHeaders:[
				{startColumnName: 'name', numberOfColumns: 3, titleText: '异常'},
				{startColumnName: 'reason', numberOfColumns: 3, titleText: '原因'},
				{startColumnName: 'measure', numberOfColumns: 3, titleText: '措施'},
				{startColumnName: 'analyzeEndTime', numberOfColumns: 2, titleText: '处理时间（单位：小时）'}
			]
		});
	}
	//确定的查询方法
	function search(){
		params = getParams();
// 		alert($.param(params));
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			$('table.ui-jqgrid-btable').each(function(index,obj){
				obj.p.postData = params;
				$(obj).trigger("reloadGrid");
			});
		}
	}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_cpk_reason";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<form id="contentForm" method="post" onsubmit="return false;">
					<div class="opt-btn">
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					</div>
					<div id="opt-content" style="display:block">					
						<table class="form-table-outside-border" width="100%" style="height: 30px;" id="searchTable">
							<tr>
								<td style="padding: 0px;margin: 0px;" >
									<span>
										日&nbsp;&nbsp;期
										<input id="datepicker1" type="text" readonly="readonly" style="width:72px;border: none;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;<input id="datepicker2" type="text" readonly="readonly" style="width:72px;border: none;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
									</span>
								</td>
								<!-- <td style="padding-right: 4px;text-align:right;" valign="middle">
									<span>
										<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
									</span>
								</td> -->
							</tr>
						</table></br>
						<table style="width:100%;">
							<caption><h2>异常原因及改善措施报表</h2></caption>
							<tr>
								<td colspan="2"><h4>报表结果：</h4></td>
							</tr>
						</table>
						<table style="width:100%;">
							<tr>
								<td valign="top" id="detail_table_parent">
									<table id="gridList"></table>
									<div id="pager"></div>
								</td>
							</tr>
						</table>
					</div>
				</form>
			</aa:zone>
		</div>
	</div>
</body>
</html>