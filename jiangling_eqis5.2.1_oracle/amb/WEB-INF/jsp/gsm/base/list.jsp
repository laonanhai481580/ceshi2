<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		//新建
		function createGsmEquipmentBase(url){
			ajaxSubmit("defaultForm",url,"main",createGsmEquipmentBaseCallback);
		}
		function createGsmEquipmentBaseCallback(){
			validateGsmEquipmentBase();
		}
		//验证
		function validateGsmEquipmentBase(){
			$("#inputForm").validate({
				submitHandler: function() {
					$("#inputForm").ajaxSubmit(function (id){
						$("#id").attr("value",id);
						$("#message").show("show");
						setTimeout('$("#message").hide("show");',3000);
					});
				},
				rules: {
					
				},
				messages: {
					
				}
			});
		}
		
		//修改
		function updateGsmEquipmentBase(url){
			var ids = jQuery("#gsmEquipmentBaseGridId").getGridParam('selarrrow');
			if(ids==""){
				alert("请选择一条数据");
			}else if(ids.length > 1){
				alert("只能选择一条数据");
			}else if(ids.length == 1){
				ajaxSubmit("defaultForm",url+"?id="+ids[0],"main",createGsmEquipmentBaseCallback);
			}
		}
		
		//删除
		function deleteGsmEquipmentBase(url){
			var ids = jQuery("#gsmEquipmentBaseGridId").getGridParam('selarrrow');
			if(ids.length<=0){
				alert("请选择数据！！");
			}else {
				aa.submit('defaultForm', url+'?ids='+ids.join(','), 'main');
			}
		}

		function saveGsmEquipmentBase(url){
			$("#inputForm").attr("action",url);
			$("#inputForm").submit();
		}
		//导出
		function list_export() {
			var exportIds = $("#complaints").jqGrid('getGridParam', 'selarrrow');
			iMatrix.export_Data("${gsmctx }/base/export-file.htm?exportIds="+ exportIds);
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="chartReport3";
		var thirdMenu="myInspectionPlanReport";
	</script>
	
	<%@ include file="/menus/header.jsp" %>

	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<aa:zone name="main">
				<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<security:authorize ifAnyGranted="GSM_BASE_DELETE ">
					<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除 </span></span></button> 
				</security:authorize>
					<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<security:authorize ifAnyGranted="GSM_BASE_EXPORT">
					<button class='btn' type="button" onclick="iMatrix.export_Data('${gsmctx }/base/export-file.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="gsmEquipmentBaseGridId" url="${gsmctx }/base/list-datas.htm" pageName="page" code="MEASUREMENT_BASE"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>