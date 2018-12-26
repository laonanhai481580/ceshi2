<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.ambition.carmfg.entity.InspectionType"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	var inspectionTypeObj = null;
	function $oneditfunc(rowId){
		if(inspectionTypeObj==null){
			inspectionTypeObj = {};
			var inspectionName = '<%=InspectionType.INSPECTION.name()%>';
			var checkName = '<%=InspectionType.CHECK.name()%>';
			inspectionTypeObj[inspectionName] = [];
			inspectionTypeObj[checkName] = [];
			$("#" + rowId + "_listType").find("option").each(function(index,obj){
				if(obj.value&&obj.value != '<%=InspectionPointTypeEnum.COMPLETEINSPECTION.name()%>'){
					if(obj.value == '<%=InspectionPointTypeEnum.PRODUCTCHECK.name()%>'
					|| obj.value == '<%=InspectionPointTypeEnum.PRODUCTDALIYREPORT.name()%>'){
						inspectionTypeObj[checkName].push({name:$(obj).html(),value:obj.value});
					}else{
						inspectionTypeObj[inspectionName].push({name:$(obj).html(),value:obj.value});
					}
				}
			});
		}
		var $type =$("#" + rowId + "_inspectionType");
		if($type.length>0){
			$("#" + rowId + "_inspectionType").bind("change",function(){
				inspectionTypeChange(this,rowId);
			});
			inspectionTypeChange($type[0],rowId);
		}
	}
	function inspectionTypeChange(obj,rowId){
		var hisVal = $("#" + rowId + "_listType").val();
		//清除采集模式的选项
		$("#" + rowId + "_listType").html('<option role="option" value="">请选择</option>');
		var pointTypes = inspectionTypeObj[obj.value];
		if(pointTypes&&pointTypes.length){
			for(var i=0;i<pointTypes.length;i++){
				var json = pointTypes[i];
				$("#" + rowId + "_listType").append('<option role="option" value="'+json.value+'">'+json.name+'</option>');
			}
		}
		$("#" + rowId + "_listType").css("width","95%").val(hisVal);
	}
	
	function selectInspector(rowId){
		<security:authorize ifAnyGranted="mfg-base-info-inspection-point-list-inspector">
			if(rowId==undefined){
				alert("请先按回车保存检查点！");
				return;
			}
			$.colorbox({href:'${mfgctx}/base-info/inspection-point/list-inspector.htm?inspectorPointId='+rowId,iframe:true, innerWidth:560, innerHeight:500,overlayClose:false,title:"选择检验员"});
			return;
		</security:authorize>
		alert("没有选择检验员的权限，请联系管理员！");
	}   
	function addAllInspector(cellValue,options,rowObj){
		return "<a  style='width:50px;float:right'  title='选择检验员' href='#' onclick='selectInspector("+cellValue+")'><span><span class='ui-icon ui-icon-person'></span></span></a>";
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
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point-add">
		  isRight =  true;
		</security:authorize>
		return isRight;
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="inspectionPoint";
		var treeMenu="point";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>	
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" >
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point-add">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point-del">
				<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point-export">
				<button  class='btn' onclick="export_Data('${mfgctx}/base-info/inspection-point/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
				</security:authorize>
				</div>
				<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="inspectionPointList" url="${mfgctx}/base-info/inspection-point/list-datas.htm" code="MFG_INSPECTION_POINT"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>