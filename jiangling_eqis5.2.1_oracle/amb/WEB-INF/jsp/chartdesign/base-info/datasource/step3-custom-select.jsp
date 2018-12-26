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
		$("#tabs-datatable").height(tabHeight).tabs();
		//初始化数据表
		initDatatableGrid();
	}
	
	//初始化数据表
	function initDatatableGrid(){
		var systemWidth = $("#tabs-datatable").width()+4;
		var systemHeight = $("#tabs-datatable").height()-121;
		$("#dataTable").jqGrid({
			rownumbers : true,
			url:'${chartdesignctx}/base-info/datasource/custom-datatable-datas.htm',
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
				{name:'name',index:'name',label:'数据表名称',width:300}
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
					window.isLoadedDataTable = true;
					var initDataTableObj = ${initDataTableObj};
					if(initDataTableObj){
						var ids = $("#dataTable").jqGrid('getDataIDs');
						var hasExist = false;
						for(var i=0;i<ids.length;i++){
							var data = $("#dataTable").jqGrid('getRowData',ids[i]);
							if(data.name == initDataTableObj.name){
								initDataTableObj.id = ids[i];
								hasExist = true;
								break;
							}
						}
						if(!hasExist){
							$("#dataTable").jqGrid("addRowData",
								initDataTableObj.id,
								initDataTableObj,
								'before',
								ids.length>0?ids[0]:0);
						}
					}
					if(initDataTableObj&&initDataTableObj.id){
						$("#dataTable").setSelection(initDataTableObj.id);
					}
				}
			}
		});
		$("#dataTable").jqGrid("setGridHeight",systemHeight);
	}
	function search(){
		$("#dataTable").jqGrid("setGridParam",{
			postData:{
				searchName : $(":input[name=searchName]").val()
			},
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
					<div id="tabs-datatable">
						<ul>
							<li><a href="#tabs-datatable-1">数据列表</a>
							</li>
						</ul>
						<div id="tabs-datatable-1" style="padding:4px 0px 0px 0px;">
							<div class="opt-body">
								<div class="opt-btn" style="margin-bottom:2px;">
									数据表名称:
									<input type="text" name="searchName"/>
									<button class='btn' type="button"
										onclick="search()">
										<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
									</button>
									<button class='btn' type="reset">
										<span><span><b class="btn-icons btn-icons-reset"></b>重置</span></span>
									</button>
								</div>
								<table id="dataTable"></table>
								<div id="dataTablePager"></div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>