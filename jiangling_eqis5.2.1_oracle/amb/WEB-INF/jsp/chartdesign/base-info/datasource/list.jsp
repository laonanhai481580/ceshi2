<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		//添加/修改数据库配置
		function editInfo(isEdit,isCopy){
			var id = "";
			if(isEdit||isCopy){
				id = $("#list").jqGrid("getGridParam","selrow");
				if(!id){
					alert("请先选择数据!");
					return;
				}
			}
			var url = '${chartdesignctx}/base-info/datasource/input.htm?id='+id;
			if(isCopy){
				url += '&isCopy=true';
			}
			$.colorbox({
				href:url,
				iframe:true, 
				width:$(window).width()<800?$(window).width()-100:800, 
				height:$(window).height()<500?$(window).height()-100:500,
				overlayClose:false,
				title:"数据集配置向导",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
					makeEditable(true);
				}
			});
		}
		
		function viewGridFormatter(val,o,rowObj){
			return "<div style='text-align:center;'><a href='javascript:void(0);showViewGrid(\""+rowObj.id+"\")'>预览</a><div>";
		}
		
		function showViewGrid(id){
			var url = '${chartdesignctx}/base-info/datasource/view-grid.htm?id='+id;
			$.colorbox({
				href:url,
				iframe:true, 
				width:$(window).width()<800?$(window).width()-100:800, 
				height:$(window).height()<500?$(window).height()-100:500,
				overlayClose:false,
				title:"预览数据"
			});
		}
		function import_Data(){
	 		var url = '${chartdesignctx}/base-info/datasource/import-form.htm';
			$.colorbox({
				href:url,
				iframe:true, 
				innerWidth:$(window).width()<700?$(window).width()-50:700, 
				innerHeight:$(window).height()<400?$(window).height()-50:400,
				overlayClose:false,
				title:"导入配置",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
	 	}
		function exportGrid(){
			var ids = $("#list").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择数据!");
				return;
			}
			var url = '${chartdesignctx}/base-info/datasource/exports.htm';
			$("button").attr("disabled","disabled");
			$.showMessage("正在导出,请稍候... ...","custom");
			var postData = {
				exportIds : ids.join(",")
			};
			$.post(url,postData,function(result){
				$.clearMessage();
				$("button").removeAttr("disabled");
				if(result.error){
					alert(result.message);
				}else{
					var fileUrl = '${ctx}/portal/export-data.action?fileName='+ result.exportFileFlag;
					fileUrl = encodeURI(fileUrl);
					$("#exportForm").attr("action",fileUrl);
					$("#exportForm").submit();
				}
			},'json');
		};
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="_basic_datasource";
		var thirdMenu="_base_datasource";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/chartdesign-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/chartdesign-datasource-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" >
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATASOURCE-INPUT">
				<button class='btn' onclick="editInfo();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<button class='btn' onclick="editInfo(true);"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
				<button class='btn' onclick="editInfo(true,true);"><span><span><b class="btn-icons btn-icons-copy"></b>复制</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATASOURCE-DELETE">
				<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="CHARTDESIGN-DATASOURCE-IMPORT">
				<button class='btn' onclick="import_Data();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				</security:authorize> 
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="CHARTDESIGN-DATASOURCE-EXPORT">
				<button class='btn' type="button" onclick="exportGrid();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
			</div>
			<div id="opt-content">
				<form id="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${chartdesignctx}/base-info/datasource/list-datas.htm" code="CHARTDESIGN_DATA_SOURCE"></grid:jqGrid>
				</form>
				<form id="exportForm" method="post">
				</form>
			</div>
		</div>
	</div>
</body>
</html>