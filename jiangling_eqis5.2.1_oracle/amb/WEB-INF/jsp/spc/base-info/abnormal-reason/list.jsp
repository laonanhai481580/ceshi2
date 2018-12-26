<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
		
	<script type="text/javascript">
		$(function(){
			setTimeout(function(){},100);
		});
		
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		
		function $processRowData(data){
			data.isLedger = true;
			return data;
		}
		
		function add(){
			var typeId = $("#typeId").val();
			$.colorbox({
				href:'${spcctx}/base-info/abnormal-reason/input.htm?typeId='+typeId,
				iframe:true,
				innerWidth:900,
				innerHeight:600,
				overlayColse:false,
				title:"添加异常原因",
				onClosed:function(){
					$("#reasonList").trigger("reloadGrid");
				}
			});
		}
		
		function edit(){
			var id = $("#reasonList").jqGrid("getGridParam","selarrrow");
			var typeId = $("#typeId").val();
			if(id.length<1){
				alert("请选中需要编辑的记录！");
				return;
			}
			if(id.length>1){
				alert("修改时不能选中多条记录！");
				return;
			}
			$.colorbox({
				href:'${spcctx}/base-info/abnormal-reason/input.htm?id='+id+'&typeId='+typeId,
				iframe:true,
				innerWidth:900,
				innerHeight:600,
				overlayColse:false,
				title:"修改异常原因",
				onClosed:function(){
					$("#reasonList").trigger("reloadGrid");
				}
			});
		}
		
		jQuery.extend($.jgrid.defaults,{
			prmNames:{
				rows:'reasonPage.pageSize',
				page:'reasonPage.pageNo',
				sort:'reasonPage.orderBy',
				order:'reasonPage.order'
			}
		});
		
		function exportReasons(){
			var typeId = $("#typeId").val();
			//$("#contentForm").attr("action","${spcctx}/base-info/abnormal-reason/exports.htm?typeId="+typeId);
			//$("#contentForm").submit();
			iMatrix.export_Data("${spcctx}/base-info/abnormal-reason/exports.htm?typeId="+typeId);
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="reasonType";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-base-info-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
				    <security:authorize ifAllGranted="spc_base-info_abnormal-reason_input,spc_base-info_abnormal-reason_save">
						<button class='btn' onclick="add();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						<button class='btn' onclick="edit();" type="button"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="spc_base-info_abnormal-reason_delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="spc_base-info_abnormal-reason_exports">
						<button class='btn' onclick="exportReasons();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<input type="hidden" name="typeId" id="typeId"/>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="reasonList" url="${spcctx}/base-info/abnormal-reason/reason-list-datas.htm" code="SPC_ABNORMAL_REASON" pageName="reasonPage"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>