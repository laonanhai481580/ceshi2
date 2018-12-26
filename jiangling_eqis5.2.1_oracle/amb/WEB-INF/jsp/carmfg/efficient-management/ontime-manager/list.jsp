<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript" src="${ctx}/js/search.js"></script>
	<script type="text/javascript">
	
	function selectprocess(rowId){
		if(rowId==undefined){
			alert("请先按回车保存出勤时间！");
			return;
		}
			$.colorbox({href:'${mfgctx}/efficient-management/ontime-manager/list-process-hour.htm?ontimeManagerId='+rowId,iframe:true, innerWidth:560, innerHeight:500,overlayClose:false,title:"选择工序"});
		}   
	function addAllProcess(cellValue,options,rowObj){
		return "<a  style='width:50px;float:right'  title='选择工序' href='#' onclick='selectprocess("+cellValue+")'><span><span class='ui-icon ui-icon-info'></span></span></a>";
	}
	//后台返回错误信息
	function $successfunc(response){
		var jsonData = eval("(" + response.responseText+ ")");
		if(jsonData.error){
			alert(jsonData.message);
		}else{
			return true;
		}
	}
	function exportExcel(){
		$("#contentForm").attr("action","${mfgctx}/base-info/inspection-point/export.htm");
		$("#contentForm").submit();
	}
	
	//重写(给单元格绑定事件)
	function $oneditfunc(rowid){
		jQuery('#'+rowid+'_beginTime','#ontimeManagerList').change(function(){
			caclute(rowid);
		});
		jQuery('#'+rowid+'_endTime','#ontimeManagerList').change(function(){
			caclute(rowid);
		});
	}
	//系统计算合格率和不良值
	function caclute(rowid){
		var beginTime = jQuery('#'+rowid+'_beginTime','#ontimeManagerList').val();
		var endTime = jQuery('#'+rowid+'_endTime','#ontimeManagerList').val();
		alert(beginTime);
		alert(endTime);
		//var qualifiedAmount = inspectionAmount-unqualifiedAmount;
		//var qualifiedRate = (qualifiedAmount/inspectionAmount)*100;
		//jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val(qualifiedAmount);
		//jQuery('#'+rowid+'_qualifiedRate','#dynamicInspection').val(qualifiedRate.toFixed(2));
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="efficientManagement";
		var thirdMenu="ontimeManager";
		var treeMenu="ontimeManagerThreeMenu";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
		<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-efficient-management-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body" >
				<aa:zone name="main">
					<div class="opt-btn">
						<button  class="btn" onclick="addRow();"><span><span>新建</span></span></button>
						<button  class='btn' onclick="delRow();"><span><span>删除</span></span></button>
						<button  class='btn' onclick="showSearchDIV(this);"><span><span>查询</span></span></button>
						<button  class='btn' onclick="exportExcel()"><span><span>导出</span></span></button>
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="ontimeManagerList" url="${mfgctx}/efficient-management/ontime-manager/list-datas.htm" code="MFG_ON_TIME_MANAGER"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>