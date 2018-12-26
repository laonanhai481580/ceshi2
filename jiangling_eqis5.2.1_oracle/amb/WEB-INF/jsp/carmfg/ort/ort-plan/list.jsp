<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
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
	function addRows(){
		iMatrix.addRow();	
		var userName=$("#userName").val();
		$("#0_producer").val(userName);
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="MFG_ORT_PLAN_SAVE">
		  isRight =  true;
		</security:authorize>
		return isRight;
	}
	function launch(){
		var rowIds = $("#list").jqGrid("getGridParam","selarrrow");
		var auditId=rowIds[0];
		if(rowIds.length==0){
			alert("请选择需要发起委托的数据!");
			return false;
		}
		var rowData = $("#list").jqGrid("getRowData",auditId);		
		var rowName=rowData.isTest;
		if(rowName==1){
			alert("该计划已经发起过委托!");
			return false;
		}
		if(rowIds.length>1){
			alert("只能选择一条数据发起委托!");
			return false;
		}		
		window.location="${mfgctx}/ort/ort-entrust/input.htm?auditId="+auditId;
	}
	
	function click(cellvalue, options, rowObject){	
		if(cellvalue!=null&&cellvalue!="&nbsp;"){
			return "<a href='${mfgctx}/ort/ort-entrust/input.htm?id="+rowObject.testEntrustId+"'>"+cellvalue+"</a>";
		}else{
			return "";
		}		
	}
	var selRowId = null;
	function modelClick(obj){
		selRowId=obj.rowid;	
		$.colorbox({href:"${mfgctx}/common/product-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
	}
	function setProductValue(datas){
		$("#"+selRowId+"_model").val(datas[0].materielCode);
 	}
	function customerNoClick(obj){
		selRowId=obj.rowid;
		var url = '${mfgctx}/ort/ort-base/customer-select.htm';
			$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择客户"
			});
	}
	function customerClick(obj){
		selRowId=obj.rowid;	
		var url = '${mfgctx}/ort/ort-base/customer-select.htm';
		$.colorbox({href:url,iframe:true, 
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择客户"
		});
	}
	function setCustomerValue(datas){
		$("#"+selRowId+"_customer").val(datas[0].value);;
		$("#"+selRowId+"_customerNo").val(datas[0].key);
 	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="ort_list";
		var thirdMenu="_ORT_PLAN_LIST";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ort-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="MFG_ORT_PLAN_SAVE">
				<button class='btn' onclick="addRows();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_ORT_PLAN_DELETE">
				<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_ORT_PLAN_EXPORT">
				<button  class="btn" onclick="iMatrix.export_Data('${mfgctx}/ort/ort-plan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<button class="btn" onclick="launch();"><span><span><b class="btn-icons btn-icons-ok"></b>发起委托</span></span></button>
				<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<input type="hidden" name="userName" id="userName" value="${userName }"/>
				<input type="hidden" name="loginName" id="loginName" value="${loginName }"/>
				<div id="opt-content">
					 <form id="contentForm" method="post" action="" >						
						<grid:jqGrid gridId="list" url="${mfgctx}/ort/ort-plan/list-datas.htm" code="MFG_ORT_PLAN"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>	
</body>
</html>