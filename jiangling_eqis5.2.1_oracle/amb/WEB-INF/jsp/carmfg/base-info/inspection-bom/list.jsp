<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		function addBoms(){
			var url='${mfgctx}/base-info/inspection-bom/inspection-bom-select.htm';
			$.colorbox({href:url,
				iframe:true,
				width:850,
				height:$(window).height()-100,
				overlayClose:false,
				onClosed:function(){
				},
				title:"添加报检物料"
			});
		}	
		
		function setInspectingBomValue(datas){
			var ids = [];
			for(var i=0;i<datas.length;i++){
				ids.push(datas[i].id);
			}
			if(ids.length>0){
				$("button").attr("disabled","disabled");
				$("#opt-content").attr("disabled","disabled");
				$("#message").html("正在添加报检物料,请稍候... ...");
				$.post("${mfgctx}/base-info/inspection-bom/add-bom.htm",{deleteIds:ids.join(",")},function(result){
					$("button").attr("disabled","");
					$("#opt-content").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						$("#list").jqGrid("setGridParam").trigger("reloadGrid");
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
				
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="baseInfo";
	var thirdMenu="bom";
	var treeMenu="inspection-bom";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/base-info/inspection-bom/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_SAVE">
						<button class="btn" onclick="addBoms();" title="增加报检物料"><span><span><b class="btn-icons btn-icons-add"></b>增加</span></span></button>
					</security:authorize>					
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div style="float:right;position:absolute; right:50px;top:6px;" >
					<span style="color:red;" id="message"></span>
				</div>				
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${mfgctx}/base-info/inspection-bom/list-datas.htm" submitForm="defaultForm" code="MFG_PRODUCT_BOM_INSPECTION" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>