<%@page import="com.ambition.carmfg.bom.service.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		createBusinessGrid();
		$("#business_table").jqGrid("setGridWidth",$(window).width()-20);
		$("#business_table").jqGrid("setGridHeight",$(window).height()-75);
	});
	//创建所属事业部列表
	function createBusinessGrid(){
		jQuery("#business_table").jqGrid({
				url:"${mfgctx}/base-info/business-unit/user-belong-business-listDatas.htm?loginName=<%=request.getParameter("loginName")%>",
				prmNames:{
				rows:'userPage.pageSize',
				page:'userPage.pageNo',
				sort:'userPage.orderBy',
				order:'userPage.order'
			},
			rownumbers:true,
			gridEdit: false,
			colNames:['id','事业部编码','事业部名称','操作'],
			colModel:[{name:'id', index:'id', width:180, editable:false,hidden:true},
			          {name:'businessUnitCode', index:'businessUnitCode', width:180, editable:false},
			          {name:'businessUnitName', index:'businessUnitName', width:180, editable:false},
			          {name:'operate', index:'operate', width:90,align:"center", editable:false,formatter:operateFormatter,sortable:false}
			          ],
			pager:"#business_pager",
			multiselect : true,
			multiboxonly:true,
		   	forceFit : true,
		   	shrinkToFit : true,
			sortorder: "desc",
			gridComplete:function(){}
		});
	}
	function operateFormatter(value,options,rowObj){
		var operations = "";
		<security:authorize ifAnyGranted="MFG_BASE-INFO_BUSINESS-UNIT_REMOVE">
		operations="<a style='margin-left:2px;' title='删除' class='small-button-bg' onclick='removeBusiness(\""+rowObj.businessUnitCode+"\")'><span class='ui-icon ui-icon-closethick'></span></a>";
		</security:authorize>
		return operations;
	}
	function removeBusiness(businessUnitCode,loginName){
		$.post("${mfgctx}/base-info/business-unit/delete-business.htm",{businessUnitCode:businessUnitCode,loginName:'<%=request.getParameter("loginName")%>'},
				function(result){
			if(result.error){
				alert(result.message);
			}else{
				$("#business_table").trigger("reloadGrid");
			}
		},'json');
		$("#business_table").trigger("reloadGrid");
	}
	</script>
</head>

<body>
	<div class="ui-layout-center">
		<div class="opt-body">
<!-- 			<div class="opt-btn" id="btnDiv"> -->
<!-- 				<button class='btn' type="button" onclick="addBusiness();"><span><span><b class="btn-icons btn-icons-ok"></b>添加事业部</span></span></button>	 -->
<!-- 				<button class='btn' type="button" onclick="removeBusiness();"><span><span><b class="btn-icons btn-icons-cancel"></b>移除事业部</span></span></button> -->
<!-- 				</div> -->
			<div id="opt-content">
				<table id="business_table"></table>
			</div>
				<div id="business_pager"></div>
		</div>
	</div>
</body>
</html>