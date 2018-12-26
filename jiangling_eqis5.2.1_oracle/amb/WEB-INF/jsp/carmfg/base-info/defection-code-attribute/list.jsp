<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">	
		//列表自动大小
		$(function(){
			setTimeout(function(){
				contentResize();
			},100);
		});
		//选择不良组件
		var partObj = null;
		function partClick(obj){
			partObj = obj.currentInputId;
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm", iframe:true, innerWidth:750, innerHeight:500, overlayClose:false, title:"选择不良组件"});
		}
		function setBomValue(datas){
			var keys = [];
			for(var i=0;i<datas.length;i++){
				keys.push(datas[i].value);
			}
			$("#" + partObj).val(keys);
	 	}
		//选择责任单位
		function liabilityDepartmentClick(obj){
			var acsSystemUrl = "${ctx}";
			popTree({ title :'选择责任单位',
				innerWidth:'400',
				treeType:'DEPARTMENT_TREE',
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.currentInputId,
				showInputId:obj.currentInputId,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}
			});
		}
		//返回异常信息
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		//导入不良代码
		function importDatas(){
			if(confirm("您要导入不良代码属性吗？")){
				$.post("${mfgctx}/base-info/defection-code-attribute/import-datas.htm", {}, function(result) {
					jQuery("#defectionCodeAttributeList").clearGridData();
					jQuery("#defectionCodeAttributeList").trigger("reloadGrid");
				});
			}
		}	
		//导入
		function imports(){
			var url = '${mfgctx}/base-info/defection-code-attribute/imports.htm';
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入",
				onClosed:function(){
				}
			});
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="defectionCode";
		var treeMenu="attribute";
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
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_Add">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_EXPORT">
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${mfgctx}/base-info/defection-code-attribute/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_IMPORT-DATAS">
					<button  class='btn' type="button" onclick="importDatas();"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_DEFECTION-CODE-ATTRIBUTE_IMPORTS">
					<button  class='btn' type="button" onclick="imports();"><span><span><b class="btn-icons btn-icons-export"></b>导入EXCEL</span></span></button>
					</security:authorize>
					</div>
					<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="defectionCodeAttributeList" url="${mfgctx}/base-info/defection-code-attribute/list-datas.htm" code="MFG_DEFECTION_CODE_ATTRIBUTE"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>