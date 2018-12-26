<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	
	<script type="text/javascript">
		function modelFormatter(cellvalue, options, rowObject){
			if(cellvalue=='0'){
				return '趋势型';
			}else if(cellvalue=='1'){
				return '运行型';
			}else if(cellvalue=='2'){
				return '交替型';
			}else if(cellvalue=='3'){
				return '其他';
			}
			
		}	
		
		function typeFormatter(cellvalue, options, rowObject){
			if(cellvalue=='0'){
				return '主控制图';
			}else if(cellvalue=='1'){
				return '副控制图';
			}
		}
		
		function add(){
			$.colorbox({
				href:'${spcctx}/base-info/bs-rules/input.htm',
				iframe:true,
				innerWidth:850,
				innerHeight:500,
				overlayColse:false,
				title:"新建判异准则",
				onClosed:function(){
					$("#bsRules").trigger("reloadGrid");
				}
			});
		}
		
		function edit(){
			var id = $("#bsRules").jqGrid("getGridParam","selarrrow");
			if(id.length<1){
				alert("请选中需要编辑的记录！");
				return;
			}
			if(id.length>1){
				alert("请选中一条记录！");
				return;
			}
			$.colorbox({
				href:'${spcctx}/base-info/bs-rules/input.htm?id='+id,
				iframe:true,
				innerWidth:1000,
				innerHeight:600,
				overlayColse:false,
				title:"修改判断准则",
				onClosed:function(){
					$("#bsRules").trigger("reloadGrid");
				}
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="_bsRulesList";
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
					<security:authorize ifAllGranted="spc_base-info_bs-rules_input,spc_base-info_bs-rules_save-detail">
						<button class='btn' onclick="add();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						<button class='btn' onclick="edit();" type="button"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="spc_base-info_bs-rules_delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="bsRules"  url="${spcctx}/base-info/bs-rules/list-datas.htm" code="SPC_BS_RULES" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>