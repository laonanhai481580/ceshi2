<%@page import="com.ambition.chartdesign.baseinfo.datasource.service.ChartDatasourceManager"%>
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
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		var tabHeight = $("#opt-content").height()-54;
		$("#tabs").height(tabHeight).tabs();
		//初始化所有列
		initDatatableGrid();
	}
	//初始化所有列
	function initDatatableGrid(){
		var systemWidth = $("#tabs").width()+4;
		var systemHeight = $("#tabs").height()-62;
		var dataTypeFormatterObj = <%=ChartDatasourceManager.getHibernateDataTypeFormatter()%>;
		$("#table").jqGrid({
			datatype:'local',
			rownumbers : true,
			width : systemWidth,
			data : ${dataColumns},
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'name',index:'name',label:'字段名',width:180},
				{name:'alias',index:'alias',label:'字段别名',width:240,editable:true},
				{name:'dataType',index:'dataType',label:'字段类型',width:150,formatter:function(val,o,rowObj){
					var form = dataTypeFormatterObj[val];
					return form?form:val;
				}}/**,
				{name:'displayOrder',index:'displayOrder',label:'排序',width:70,formatter:function(val,o,rowObj){
					var top = "<a id='top_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:10px;' href=\"javascript:void(0);toTop('top_"+rowObj.id+"');\" title='向上'><span class=\"ui-icon ui-icon-arrowthick-1-n\" style='cursor:pointer;'></span></a>";
					var bottom = "<a id='bottom_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:6px;' href=\"javascript:void(0);toBottom('bottom_"+rowObj.id+"');\" title='向下'><span class=\"ui-icon ui-icon-arrowthick-1-s\" style='cursor:pointer;'></span></a>";
					return top+ bottom;
				}}*/
			],
			rowNum:500,
		    multiselect: false,
		    cellEdit:true,
		    cellsubmit:'clientArray',
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){
				$("#table").tableDnDUpdate({});
			}
		});
		$("#table").jqGrid("setGridHeight",systemHeight);
		$("#table").tableDnD({});
	}
	function saveEditing(){
		$("#table :input[name]").each(function(index,obj){
			var id = $(obj).closest("tr").attr("id");
			$("#table").jqGrid("saveCell",id,3);
		});
	}
	function submitForm(url){
		saveEditing();
		var $trs = $("#table tr[role=row][id]"); 
		if($trs.length==0){
			alert("字段信息不能为空!");
			return;
		}
		var strs = [];
		var fieldObj = {};
		$trs.each(function(index,obj){
			var rowData = $("#table").jqGrid("getRowData",obj.id);
			if(!rowData.name){
				alert("第" + (index+1) + "行字段名不能为空!");
				strs = [];
				return false;
			}
			if(fieldObj[rowData.name]){
				alert("第" + (index+1) + "行字段名【"+rowData.name+"】不能重复!");
				strs = [];
				return false;
			}
			fieldObj[rowData.name] = true;
			var str = '{name:"'+rowData.name+'",alias:"'+rowData.alias+'",dataType:"'+rowData.dataType+'"}';
			strs.push(str);
		});
		if(strs.length==0){
			return;
		}
		if($("#form").valid()){
			$(":input[name=fieldStrs]").val("[" + strs.join(",") + "]");
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
	
	function toTop(clickId){
		saveEditing();
		var rowId = $("#" + clickId).closest("tr").attr("id");
		var prevId = $("tr[id="+rowId+"]").prev().attr("id");
		if(prevId){
			var firstData = $("#table").jqGrid("getRowData",rowId);
			var secData = $("#table").jqGrid("getRowData",prevId);
			$("#table").jqGrid("setRowData",prevId,firstData);
			$("#table").jqGrid("setRowData",rowId,secData);
			$("#table").jqGrid("editCell",prevId,5);
		}
	}
	
	function toBottom(clickId){
		saveEditing();
		var rowId = $("#" + clickId).closest("tr").attr("id");
		var nextId = $("tr[id="+rowId+"]").next().attr("id");
		if(nextId){
			var firstData = $("#table").jqGrid("getRowData",rowId);
			var secData = $("#table").jqGrid("getRowData",nextId);
			$("#table").jqGrid("setRowData",nextId,firstData);
			$("#table").jqGrid("setRowData",rowId,secData);
			$("#table").jqGrid("editCell",nextId,5);
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
					onclick="backTo('${chartdesignctx}/base-info/datasource/step3.htm?isBack=true')">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/step5.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content" style="overflow:hidden;">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-size:14px;line-height:30px;">
					<b>4.验证和自定义显示的列</b>
					<label style="font-size:10px;margin-left:10px;">可修改双击字段别名,选择行后可按住鼠标调整顺序</label>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="fieldStrs"/>
					<table style="width: 100%;height:100%;">
						<tr>
							<td style="width:40%;">
								<div id="tabs">
									<ul>
										<li><a href="#tabs-1">字段信息</a>
										</li>
									</ul>
									<div id="tabs-1" style="padding:4px 0px 0px 0px;">
										<table id="table"></table>
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