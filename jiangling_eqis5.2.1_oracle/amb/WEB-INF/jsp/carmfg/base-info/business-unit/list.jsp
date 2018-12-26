<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	function contentResize(){
		var leftWidth = $("#leftTd").width();
		var panelHeight = $(".ui-layout-center").height()-100;
		$("#business_table").jqGrid("setGridWidth",leftWidth-10);
		$("#business_table").jqGrid("setGridHeight",panelHeight);
		$("#user_item_table").jqGrid("setGridWidth",$(".ui-layout-center").width()-leftWidth - 20);
		$("#user_item_table").jqGrid("setGridHeight",panelHeight);
	}
	$(document).ready(function(){
		createUserItemGrid();
		createBusinessGrid();
		contentResize();
	});
	//创建事业部列表
	function createBusinessGrid(){
			jQuery("#business_table").jqGrid({
				url:"${mfgctx}/base-info/business-unit/business-listDatas.htm",
				rownumbers:true,
				gridEdit: false,
				colNames:['id','事业部编码','事业部名称'],
				colModel:[{name:'id', index:'id', width:180, editable:false,hidden:true},
				          {name:'businessUnitCode', index:'businessUnitCode', width:180, editable:false,hidden:true},
				          {name:'businessUnitName', index:'businessUnitName', width:180, editable:false}],
				pager:"#business_pager",
				multiselect : true,
				multiboxonly:true,
			   	forceFit : true,
			   	shrinkToFit : true,
				sortorder: "desc",
 				gridComplete : function(){},
 				ondblClickRow : function(rowId){
 					var rowData = $("#business_table").jqGrid("getRowData",rowId);
 					$("#user_item_table").jqGrid("setGridParam",{
 						postData:{
 							businessUnitCode : rowData.businessUnitCode
 						},
 						page:1
 					}).trigger("reloadGrid");
 				}
			});
		}
	
	//创建事业部人员列表
	function createUserItemGrid(){
		var tableId = 'user_item_table';
		$("#" + tableId).jqGrid({
			url:"${mfgctx}/base-info/business-unit/user-listDatas.htm",
			prmNames:{
				rows:'userPage.pageSize',
				page:'userPage.pageNo',
				sort:'userPage.orderBy',
				order:'userPage.order'
			},
			rownumbers:true,
			gridEdit: true,
			colNames:['id','事业部编码','事业部名称','姓名','工号','邮箱','授权事业部'],
			colModel:[
				{name:'id',index:'id',editable:true,hidden:true},	
		        {name:'businessUnitCode',index:'businessUnitCode',editable:true,hidden:true},
		        {name:'businessUnitName', index:'businessUnitName',editable:true,align:'center',width:120},
		        {name:'name', index:'name', width:80, editable:true,align:'center'},
		        {name:'loginName', index:'loginName', width:80, editable:true,align:'center'},
		        //{name:'sex', index:'sex', width:50, editable:true,align:'center'},
		        {name:'email', index:'email', width:190, editable:true},
		        {name:'belongBusinessUint', index:'belongBusinessUint', width:100, editable:true,align:'center',formatter:operateFormatter}
			],
			multiselect : true,
			multiboxonly:true,
			pager : "#user_item_pager",
		   	autowidth: true,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete: function(){
			}
		});
	}
	function addUserItem(){
		var ids=$("#business_table").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择至少一个事业部！");
			return;
		}
		var loginNames = $("#loginName").val();
		if(!loginNames){
			alert("请选择用户！");
			return;
		}
		$(".opt-btn").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$.post("${mfgctx}/base-info/business-unit/save-user.htm",{businessUnitIds:ids.join(","),loginNames:loginNames},
				function(result){
			$(".opt-btn").removeAttr("disabled");
			$("#message").html("");
			if(result.error){
				alert(result.message);
			}else{
				$("#user_item_table").trigger("reloadGrid");
			}
		},'json');
	}
	function addPerson(){
		var ids=$("#business_table").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择至少一个事业部！");
			return;
		}
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			hiddenInputId:'loginName',
			showInputId:'name',
			multiple:'true',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				addUserItem();
			}});
	}
	function operateFormatter(value,options,rowObj){
		var operations = "<div style='text-align:center;' title='查看绑定的事业部'><a class=\"small-button-bg\" onclick='viewAllBusinessUnit(\""+rowObj.loginName+"\")'><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		return operations;
	}
	function viewAllBusinessUnit(loginName){
		var url = "${mfgctx}/base-info/business-unit/user-belong-business-unit.htm?loginName=" + loginName;
		$.colorbox({href:url,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"所属事业部列表",
 			onClosed:function(){
 				$("#user_item_table").trigger("reloadGrid");
 			}
 		});
		}
	function removePerson(){
		var ids=$("#user_item_table").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择一条记录！");
			return;
		}
		$(".opt-btn").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$.post("${mfgctx}/base-info/business-unit/delete-user.htm",{userLinkBusinessUnitIds:ids.join(",")},
				function(result){
			$(".opt-btn").removeAttr("disabled");
			$("#message").html("");
			if(result.error){
				alert(result.message);
			}else{
				$("#user_item_table").trigger("reloadGrid");
			}
		},'json');
	}
	</script>
</head>


<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="_businessUnit";
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
	<div class="ui-layout-center" >
		<input name="id" id="id" type="hidden"></input>
		<table class="tableclass" style="border: 0px;margin: 0px;padding: 0px;background-color: rgb(187,232,255);" cellpadding="0px" border="0px" >
   			<tr>
   				<td style="width:240px;"  id="leftTd">
   					<div class="opt-body">
		 			<div class="opt-btn" style="background-color: #ddf0fa;">
						<b>事业部列表</b>
					</div>
					<div style="padding:6px;">
						 <table id="business_table"></table>
						 <div id="business_pager"></div>
					</div>
					</div>
		 		</td>
		 		<td id="rightTd">
		 			<div class="opt-body">
			 			<div class="opt-btn">
<%-- 			 				<security:authorize ifAnyGranted="MFG_BASE-INFO_BUSINESS-UNIT_ADD"> --%>
							 <button class="btn" onclick="addPerson();"><span><span><b class="btn-icons btn-icons-add"></b>添加用户</span></span></button>
<%-- 							 </security:authorize> --%>
							 <input id="loginName" name="loginName" type="hidden" ></input>
							 <input id="name" name="name" type="hidden" ></input>
<%-- 							 <security:authorize ifAnyGranted="MFG_BASE-INFO_BUSINESS-UNIT_REMOVE"> --%>
							 <button class="btn" onclick="removePerson();"><span><span><b class="btn-icons btn-icons-delete"></b>移除用户</span></span></button>
<%-- 							 </security:authorize> --%>
							 <span id="message" style="color:red;"></span>
						</div>
						<div style="padding:6px;">
							<table id="user_item_table"></table>
					 		<div id="user_item_pager"></div>
						</div>
					</div>
		 		</td>
		 	</tr>
		 </table>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>