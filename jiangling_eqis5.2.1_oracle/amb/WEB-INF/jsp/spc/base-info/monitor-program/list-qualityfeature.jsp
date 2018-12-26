<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>选择检验员</title>
<%@include file="/common/meta.jsp" %>	
<script type="text/javascript">
	function typeFormatter(cellValue,options,rowObj){
		if(cellValue==1){
			return "计量型";
		}else if(cellValue==2){
			return "计数型";
		}else if(cellValue==3){
			return "其他";
		}else{
			return "";
		}
	}
	
	isUsingComonLayout=false;
	function delRow(rowId) {
		var ids = jQuery("#inspectorList").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要删除的记录！");
			return;
		}
		$.post("${mfgctx}/base-info/inspection-point/delete-inspector.htm", {
			deleteIds : ids.join(',')
		}, function(data) {
			//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
			while (ids.length>0) {
				jQuery("#inspectorList").jqGrid('delRowData', ids[0]);
			}
		});
		
	}
	
	function closeBtn(){
		window.parent.$.colorbox.close();
	}
	
	function selectQualityFeature(){
		$.colorbox({href:'${spcctx}/common/feature-bom-multi-select.htm',iframe:true, innerWidth:800, innerHeight:500,overlayClose:false,title:"选择质量特性"});
	}
	
	function setFeatureValue(data){
		 var monitPointId=$("#monitPointId").val();
		 var ids="";
		 for(var i=0;i<data.length;i++){
			 var id=data[i].id;
			 if(ids){
			 ids=ids+','+id;
			 }else{
				 ids=ids+id;
			 }
		 }
	 	$.post("${spcctx}/base-info/monitor-point/save-qualityfeature.htm",{monitPointId : monitPointId,ids:ids},function(data){
	 		var monitPointId = $("#monitPointId").val();
	 		$("#qualityFeatureList").setGridParam({postData:{"monitPointId":monitPointId}});
	 		$("#qualityFeatureList").trigger("reloadGrid");	
	 	},'json');
	}
	
	function delQualityFeature(){
		var ids = jQuery("#qualityFeatureList").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要删除的记录！");
			return;
		}
		$.post("${spcctx}/base-info/monitor-point/delete-monit-quality-feature.htm", {
			deleteIds : ids.join(',')
		}, function(data) {
			//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
			while (ids.length>0) {
				jQuery("#qualityFeatureList").jqGrid('delRowData', ids[0]);
			}
		});
	}
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc-common-feature-bom-multi-select">
					<button class='btn' onclick="selectQualityFeature();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>选择质量参数</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="base-info-monitor-point-delete-monit-quality-feature">
					<button class='btn' onclick="delQualityFeature();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
			</div>
			<div id="opt-content" class="form-bg">
				<div style="display: none;" id="message"><font class=onSuccess><nobr>保存成功！</nobr></font></div>
				<form id="contentForm" name="contentForm" method="post"  action="" onsubmit="return false;">
					<input type="hidden"  value="${monitPointId}" name="monitPointId" id="monitPointId" />
					<table id="qualityFeatureList"></table>
					<div id="pager"></div> 
					<script type="text/javascript">
						$(document).ready(function(){
							var monitPointId = $("#monitPointId").val();
							jQuery("#qualityFeatureList").jqGrid({
								url:'${spcctx}/base-info/monitor-point/list-monit-qualityfeature-datas.htm',
								postData:{monitPointId:monitPointId},
								pager:"#pager",
								rownumbers:true,
								height:480,
								colNames:['参数名称','参数编码','数据类型','数据单位'],
								colModel:[
								          {name:'name', index:'name',width:100},
								          {name:'code', index:'code',width:100},
								          {name:'paramType', index:'paramType',width:100,formatter:typeFormatter},
								          {name:'unit', index:'unit',width:100}
								]
							});
				       	});
					</script>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>