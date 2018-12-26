<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<%@ include file="list-script.jsp" %>
	<script type="text/javascript">
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
// 			<security:authorize ifAnyGranted="EPM_ORT_ITEM_SAVE">
			  isRight =  true;
// 			</security:authorize>
			return isRight;
		}
		//确定
		function realSelect(){
			var ids = $("#List").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请选择项目!");
				return;
			}
			if(ids.length == 0){
				alert("请选择设备!");
				return;
			}
// 			if(ids.length > 1){
// 				alert("只能选择一条信息!");
// 				return;
// 			}
			if($.isFunction(window.parent.setProjectValue)){
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#List").jqGrid('getRowData',ids[i]);
					if(data){
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setProjectValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setProjectValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>
			        <button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			        <button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="List" url="${gpctx}/averageMaterial/list-datas.htm?type=2" code="select_gp_average"></grid:jqGrid>		
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>