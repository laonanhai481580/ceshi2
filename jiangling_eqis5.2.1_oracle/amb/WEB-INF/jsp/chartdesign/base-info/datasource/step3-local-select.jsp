<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		var tabHeight = $("#opt-content").height()-54;
		$("#tabs-business-system").height(tabHeight).tabs();
		$("#tabs-datatable").height(tabHeight).tabs();
		//初始化业务系统表
		initBusinessSystemGrid();
		//初始化数据表
		initDatatableGrid();
	}
	//初始化业务系统表格
	function initBusinessSystemGrid(){
		var systemWidth = $("#tabs-business-system").width()+4;
		var systemHeight = $("#tabs-business-system").height()-90;
		$("#businessSystemTable").jqGrid({
			url : '${chartdesignctx}/base-info/datasource/business-system-datas.htm?initBusinessSystemCode=${businessSystemCode}',
			rownumbers : true,
			width : systemWidth,
			pager : '#businessSystemPager',
			rowNum : 10,
			prmNames:{
				rows:'businessSystemPage.pageSize',
				page:'businessSystemPage.pageNo',
				sort:'businessSystemPage.orderBy',
				order:'businessSystemPage.order'
			},
			colModel: [
				{name:'code',index:'code',label:'编码',hidden:true},
				{name:'name',index:'name',label:'名称',width:systemWidth-40}
			],
		    multiselect: false,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			ondblClickRow: function(rowId){
				loadDataTables(rowId);
			},
			loadComplete : function(){
				if(!window.isLoadedBusinessSystem){
					window.isLoadedBusinessSystem = true;
					var initBusinessSystemObj = ${initBusinessSystemObj};
					var ids = $("#businessSystemTable").jqGrid('getDataIDs');
					if(initBusinessSystemObj
						&&initBusinessSystemObj.id
						&&$("#businessSystemTable tr[id="+initBusinessSystemObj.id+"]").length==0){
						$("#businessSystemTable").jqGrid("addRowData",
							initBusinessSystemObj.id,
							initBusinessSystemObj,
							'before',
							ids.length>0?ids[0]:0);
					}
					if(ids.length>0||initBusinessSystemObj.id){
						var initId = initBusinessSystemObj.id?initBusinessSystemObj.id:ids[0];
						$("#businessSystemTable").setSelection(initId);
						loadDataTables(initId);
					}
				}
			}
		});
		$("#businessSystemTable").jqGrid("setGridHeight",systemHeight);
	}
	//初始化数据表
	function initDatatableGrid(){
		var systemWidth = $("#tabs-datatable").width()+4;
		var systemHeight = $("#tabs-datatable").height()-90;
		$("#dataTable").jqGrid({
			rownumbers : true,
			datatype:'local',
			width : systemWidth,
			pager : '#dataTablePager',
			rowNum : 10,
			prmNames:{
				rows:'dataTablePage.pageSize',
				page:'dataTablePage.pageNo',
				sort:'dataTablePage.orderBy',
				order:'dataTablePage.order'
			},
			colModel: [
				{name:'name',index:'name',label:'数据表',width:160},
				{name:'alias',index:'alias',label:'别名',width:220}
			],
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			loadComplete : function(){
				if(!window.isLoadedDataTable){
					window.isLoadedDataTable = 1;
				}else{
					window.isLoadedDataTable++;
				}
				if(window.isLoadedDataTable == 2){
					var initDataTableObj = ${initDataTableObj};
					var ids = $("#dataTable").jqGrid('getDataIDs');
					if(initDataTableObj&&initDataTableObj.id
						&&$("#dataTable tr[id="+initDataTableObj.id+"]").length==0){
						$("#dataTable").jqGrid("addRowData",
							initDataTableObj.id,
							initDataTableObj,
							'before',
							ids.length>0?ids[0]:0);
					}
					if(initDataTableObj&&initDataTableObj.id){
						$("#dataTable").setSelection(initDataTableObj.id);
					}
				}
			}
		});
		$("#dataTable").jqGrid("setGridHeight",systemHeight);
	}
	function loadDataTables(businessSystemId){
		var rowData = $("#businessSystemTable").jqGrid("getRowData",businessSystemId);
		$("#tabs-datatable").find(".ui-tabs-selected").find("a").html(rowData.name + "-数据表");
		$("#dataTable").jqGrid("setGridParam",{
			datatype:'json',
			url:"${chartdesignctx}/base-info/datasource/local-datatable-datas.htm?_businessSystemId="+businessSystemId,
			page:1
		}).trigger("reloadGrid");
	}
	function submitForm(url){
		var ids = $("#dataTable").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先选择数据表!");
			return;
		}
		if(ids.length>1){
			alert("只能选择一条数据!");
			return;
		}
		var rowData = $("#dataTable").jqGrid("getRowData",ids[0]);
		if($("#form").valid()){
			if(url.indexOf("?")>1){
				url += "&dataTableName=" + rowData.name;
			}else{
				url += "?dataTableName=" + rowData.name;
			}
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	function backTo(url){
		$('#form').attr('action',url);
		$("button").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$('#form').submit();
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="backTo('${chartdesignctx}/base-info/datasource/step2.htm?isBack=true')">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/step4.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					3.选择数据表
				</div>
				<form id="form" name="form"
					method="post" action="">
					<table style="width: 100%;height:100%;">
						<tr>
							<td style="width:40%;">
								<div id="tabs-business-system">
									<ul>
										<li><a href="#tabs-business-system-1">业务系统</a>
										</li>
									</ul>
									<div id="tabs-business-system-1" style="padding:4px 0px 0px 0px;">
										<table id="businessSystemTable"></table>
										<div id="businessSystemPager"></div>
									</div>
								</div>
							</td>
							<td style="width:60%;padding-left:4px;">
								<div id="tabs-datatable">
									<ul>
										<li><a href="#tabs-datatable-1">数据表</a>
										</li>
									</ul>
									<div id="tabs-datatable-1" style="padding:4px 0px 0px 0px;">
										<table id="dataTable"></table>
										<div id="dataTablePager"></div>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>