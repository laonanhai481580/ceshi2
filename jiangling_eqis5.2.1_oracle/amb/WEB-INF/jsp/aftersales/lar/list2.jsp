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
			<security:authorize ifAnyGranted="AFS_LAR_DATA_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		var selRowId = null;
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_inspectionCount").keyup(caculateBadRate);
			$("#" + rowId + "_qualifiedCount").keyup(caculateBadRate);
			$("#" + rowId + "_qualifiedRate").attr("disabled","disabled");
		}
		function caculateBadRate(){
			var inspectionCount = $("#" + selRowId + "_inspectionCount").val();
			var qualifiedCount = $("#" + selRowId + "_qualifiedCount").val();
			if(isNaN(inspectionCount)){
				alert("检验批数必须为整数！");
				$("#" + selRowId + "_inspectionCount").focus();
				return;
			}
			if(isNaN(qualifiedCount)){
				alert("合格批数必须为整数！");
				$("#" + selRowId + "_qualifiedCount").focus();
				return;
			}

			if((inspectionCount-0)<(qualifiedCount-0)){
				alert("合格批数不能大于检验批数！");
				return;
			}
			var rate=qualifiedCount*100/inspectionCount;
			$("#" + selRowId + "_qualifiedRate").val(rate.toFixed(2)+"%");
		}
		function customerModelClick(obj){
			selRowId=obj.rowid;	
			modelClick();
		}
		function ofilmModelClick(obj){
			selRowId=obj.rowid;	
			modelClick();
		}
	 	function modelClick(){
			var customerName=$("#"+selRowId+"_customer").val();
	 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
	 		$.colorbox({href:encodeURI(url),iframe:true, 
	 			innerWidth:700, 
				innerHeight:500,
	 			overlayClose:false,
	 			title:"选择机型"
	 		});
	 	}
	 	
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setProblemValue(datas){
	 		$("#"+selRowId+"_customerModel").val(datas[0].value);
	 		$("#"+selRowId+"_ofilmModel").val(datas[0].key);
	 	}
	</script>
	
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="lar";
		var thirdMenu="lar_data";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-lar-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="AFS_LAR_DATA_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="AFS_LAR_DATA_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="AFS_LAR_DATA_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${aftersalesctx}/lar/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${aftersalesctx}/lar/list-datas.htm" submitForm="defaultForm" code="AFS_LAR_DATA" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>