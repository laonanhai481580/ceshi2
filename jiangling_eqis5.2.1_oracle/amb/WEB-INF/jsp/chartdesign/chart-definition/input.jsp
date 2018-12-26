<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
		$("#tabs-can-sel-data-set").height(tabHeight).tabs();
		$("#tabs-sel-data-set").height(tabHeight).tabs();
		//初始化可选数据集
		initCanSelDataSet();
		//初始化已选数据集
		initSelDataSet();
	}
	//初始化可选数据集
	function initCanSelDataSet(){
		var systemWidth = $("#tabs-can-sel-data-set").width()+4;
		var systemHeight = $("#tabs-can-sel-data-set").height()-100-$("#searchDiv1").height();
		$("#canSelDataSet").jqGrid({
			url : '${chartdesignctx}/chart-definition/data-set-datas.htm?_list_code=CHARTDESIGN_DATA_SOURCE',
			rownumbers : true,
			width : systemWidth,
			pager : '#canSelDataSetPager',
			rowNum : 10,
			prmNames:{
				rows:'datasourcePage.pageSize',
				page:'datasourcePage.pageNo',
				sort:'datasourcePage.orderBy',
				order:'datasourcePage.order'
			},
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'name',index:'name',label:'名称',width:systemWidth-40}
			],
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
		});
		$("#canSelDataSet").jqGrid("setGridHeight",systemHeight);
		//选择框宽度
		$("#searchDiv1").width(systemWidth-30);
	}
	function search(){
		$("#canSelDataSet").jqGrid("setGridParam",{
			postData:{
				name : $(":input[name=searchName]").val()
			}
		}).trigger("reloadGrid");
	}
	//初始化数据集
	function initSelDataSet(){
		var systemWidth = $("#tabs-sel-data-set").width()+4;
		var systemHeight = $("#tabs-sel-data-set").height()-90;
		$("#selDataSet").jqGrid({
			rownumbers : true,
			datatype:'local',
			width : systemWidth,
			pager : '#selDataSetPager',
			rowNum : 100,
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'name',index:'name',label:'名称',width:160}
			],
			data:${selDatasets},
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
		});
		$("#selDataSet").jqGrid("setGridHeight",systemHeight);
	}
	function submitForm(url){
		var ids = $("#selDataSet").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("请先选择数据集!");
			return;
		}
		if($("#form").valid()){
			if(url.indexOf("?")>1){
				url += "&";
			}else{
				url += "?";
			}
			url += "datasourceIds=" + ids.join(",");
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//添加数据集
	function addDataSet(){
		var ids = $("#canSelDataSet").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先在左边表格中选择数据集!");
			return;
		}
		for(var i=0;i<ids.length;i++){
			var d = $("#canSelDataSet").jqGrid("getRowData",ids[i]);
			if($("#selDataSet tr[id="+ids[i]+"]").length==0){
				$("#selDataSet").jqGrid("addRowData",ids[i],d);			
			}
		}
	}
	//移除数据集
	function removeDataSet(){
		var ids = $("#selDataSet").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先在右边表格中选择数据集!");
			return;
		}
		ids = ids.join(",").split(",");
		for(var i=0;i<ids.length;i++){
			$("#selDataSet").jqGrid("delRowData",ids[i]);		
		}
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
					onclick="submitForm('${chartdesignctx}/chart-definition/step2.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					1/6.选择数据集
				</div>
				<form id="form" name="form"
					method="post" action="">
					<table style="width: 100%;height:100%;">
						<tr>
							<td style="width:40%;">
								<div id="tabs-can-sel-data-set">
									<ul>
										<li><a href="#tabs-can-sel-data-set-1">可选数据集</a>
										</li>
									</ul>
									<div id="tabs-can-sel-data-set-1" style="padding:4px 0px 0px 10px;">
										<div class="opt-btn" id="searchDiv1" style="margin-bottom:2px;width:10px;">
											名称:<input type="text" name="searchName"/>
											<button class='btn' type="button"
												onclick="search()">
												<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
											</button>
										</div>
										<table id="canSelDataSet"></table>
										<div id="canSelDataSetPager"></div>
									</div>
								</div>
							</td>
							<td style="width:10%;" valign="middle" align="center">
								<button style="cursor:pointer;" type="button"
									onclick="addDataSet()">添加 >>
								</button>
								<button style="margin-top:20px;cursor:pointer;"  type="button"
									onclick="removeDataSet()"><< 移除
								</button>
							</td>
							<td style="width:40%;">
								<div id="tabs-sel-data-set">
									<ul>
										<li><a href="#tabs-sel-data-set-1">已选数据集</a>
										</li>
									</ul>
									<div id="tabs-sel-data-set-1" style="padding:4px 0px 0px 10px;">
										<table id="selDataSet"></table>
										<div id="selDataSetPager"></div>
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