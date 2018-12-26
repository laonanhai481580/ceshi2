<%@page import="com.ambition.chartdesign.entity.ChartDefinition"%>
<%@page import="org.hibernate.Hibernate"%>
<%@page import="com.ambition.chartdesign.entity.ChartDatasource"%>
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
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化表格	
		initGrid();
	}
	function submitForm(url){
		var ids = $("#table").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("数据源不能为空!");
			return;
		}
		$('#form').attr('action',url);
		$("button").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$('#form').submit();
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	//初始化所有列
	function initGrid(){
		var colModels = [
		{label:'数据源',name:'name'},
		{label:'数据库',name:'databaseName'}
		];
		$("#table").jqGrid({
			datatype:'json',
			url:'${chartdesignctx}/custom-search/search-table-datas.htm',
			rownumbers : true,
			width : $("#tableBtn").width(),
			data : [],
			colModel: colModels,
			gridEdit: false,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			ondblClickRow: function(rowId){
			}
		});
		/**var groupHeaders = ${groupHeaders};
		if(groupHeaders.length>0){
			$("#table").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders: groupHeaders
			});
		}*/
		var height = $("#opt-content").height() - $("#table").position().top-130;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
	}
	function addRow(id){
		var url = '${chartdesignctx}/custom-search/search-table-input.htm';
		if(id){
			url += "?tableId=" + id;
		}
		$.colorbox({
			href:url,
			iframe:true, 
			width:$(window).width()-50, 
			height:$(window).height()-40,
			overlayClose:false,
			title:"定义数据源",
			onClosed:function(){
				//$("#table").trigger("reloadGrid");
			}
		});
	}
	function editRow(){
		var id = $("#table").jqGrid("getGridParam","selrow");
		if(!id){
			alert("请选择数据!");
			return;
		}
		addRow(id);
	}
	function delRow(){
		var ids = $("#table").jqGrid("getGridParam","selarrrow");
		if(ids.length>0){
			if(!confirm("确定要删除吗?")){
				return;
			}
		}
		ids = ids.join(",").split(",");
		for(var i=0;i<ids.length;i++){
			if(editId == ids[i]){
				editId = null;
			}
			$("#table").jqGrid("delRowData",ids[i]);
		}
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
					onclick="submitForm('${chartdesignctx}/custom-search/step2.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					1/6.创建数据源
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="groupSetStrs"/>
					<div class="opt-body">
						<div class="opt-btn" id="tableBtn" style="margin-bottom:2px;">
							<button class='btn' type="button" onclick="addRow()">
								<span><span><b class="btn-icons btn-icons-add"></b>添加</span></span>
							</button>
							<button class='btn' type="button" onclick="editRow()">
								<span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span>
							</button>
							<button class='btn' type="button" onclick="delRow()">
								<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
							</button>
						</div>
						<table id="table"></table>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>