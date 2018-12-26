<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	isUsingComonLayout=false;
	function reasonFormatter(value,options,rowData){
		var strs = '';
		<security:authorize ifAnyGranted="spc_process-monitor_reason-measure-input">
		strs = "<div style='text-align:center;'><a title='添加原因措施' class='small-button-bg' onclick='addReason("+rowData.id+")'><span class='ui-icon ui-icon-note' style='cursor:pointer;'></span></a></div>";
		</security:authorize>
		return strs;
	}
	
	function addReason(id){
		$.colorbox({href:"${spcctx}/process-monitor/reason-measure-input.htm?messageId="+id,
			iframe:true, 
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"添加原因措施",
			onClosed:function(){
				$("#exMessages").trigger("reloadGrid");
			}
		});
	}
	
	function numFormat(cellvalue, options, rowObject){
		var operations = '';
		if(cellvalue){
			<security:authorize ifAnyGranted="spc_process-monitor_subgroup-detail">
			operations = "<a onclick='callList("+rowObject.id+");' href='#'>"+cellvalue+"</a>";
			</security:authorize>
		}
		return operations;
	}
	
	function callList(id){
		$.colorbox({
			href:'${spcctx}/process-monitor/subgroup-detail.htm?messageId='+id,
			iframe:true, 
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"子组信息",
			onClosed:function(){
				$("#exMessages").trigger("reloadGrid");
			}
		});
	}
	
	function exportMessage(){
		//$("#contentForm").attr("action","${spcctx}/process-monitor/exports.htm");
		//$("#contentForm").submit();
		iMatrix.export_Data("${spcctx}/process-monitor/exports.htm");
	}
	
	function contentResize(){
		$("#exMessages").jqGrid("setGridWidth",$(window).width()-20);
		$("#exMessages").jqGrid("setGridHeight",$(window).height()-110);
	}
	
	function closeBtn(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="opt-body">
		<aa:zone name = "main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_process-monitor_info-delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="spc_process-monitor_exports">
					<button class='btn' onclick="exportMessage();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<div style="margin-top:15px; margin-left: 10px;" id="content">
				<form id="contentForm" method="post" action="">
					<%
						ActionContext.getContext().put("featureId",request.getParameter("featureId"));
						ActionContext.getContext().put("startDateStr",request.getParameter("startDateStr"));
						ActionContext.getContext().put("endDateStr",request.getParameter("endDateStr"));
						ActionContext.getContext().put("lastAmout",request.getParameter("lastAmout"));
					%>
					<grid:jqGrid gridId="exMessages" url='${spcctx}/process-monitor/feature-list-datas.htm?featureId=${featureId}&startDateStr=${startDateStr}&endDateStr=${endDateStr}&lastAmout=${lastAmout}' code="SPC_ABNORMAL_INFO" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>