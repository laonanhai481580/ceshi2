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
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化表格	
		initGrid();
	}
	var dataTypeJson =  ${dataTypeJsonStr};
	function submitForm(url){
		if($("#form").valid()){
			saveEditing();
			var ids = $("#table").jqGrid('getDataIDs');
			var strs = [];
			for(var i=0;i<ids.length;i++){
				var obj = $("#table").jqGrid("getRowData",ids[i]);
				var fieldType = dataTypeJson[obj.fieldName];
				var str = '{leftBracket:"'+obj.leftBracket+'",fieldName:"'+obj.fieldName+'",fieldType:"'+fieldType+'",operate:"'+obj.operate+'",value:"'+obj.value+'",rightBracket:"'+obj.rightBracket+'",joinStr:"'+obj.joinStr+'"}';
				strs.push(str);
			}
			$(":input[name=goalDataConditionStrs]").val("[" + strs.join(",") + "]");
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	//初始化所有列
	function initGrid(){
		$("#table").jqGrid({
			datatype:'local',
			rownumbers : true,
			width : $("#tableBtn").width(),
			data : ${goalDataConditionStrs},
			colModel: [
				{	name:'leftBracket',
					index:'leftBracket',
					label:'(',
					width:60,
					sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'','(':'(','((':'((','(((':'(((','((((':'(((('}}
				},
				{name:'fieldName',index:'fieldName',label:'字段',width:170,editable:true,sorable:false,editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:${dataColumnJsonStr}}
				},
				{name:'operate',index:'operate',label:'操作符',width:85,editable:true,sorable:false,editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'=':'等于','>':'大于','>=':'大于等于','<':'小于','<=':'小于等于','<>':'不等于','include':'包含','notinclude':'不包含'}}
				},
				{name:'value',index:'value',label:'值',width:140,editable:true,sorable:false,editable:true},
				{name:'rightBracket',index:'rightBracket',label:')',width:60,sorable:false,
					sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'',')':')','))':'))',')))':')))','))))':'))))'}}
				},
				{name:'joinStr',index:'joinStr',label:'并且/或者',width:90,sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'','and':'并且','or':'或者'}}
				}
			],
			gridEdit: true,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			ondblClickRow: function(rowId){
				$("#table").jqGrid("editRow",rowId,{
					keys:true,
					url : 'clientArray'
				});
			}
		});
		var height = $("#opt-content").height() - $("#table").position().top-130;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
	}
	function addRow(){
		var id = (new Date()).getTime();
		$("#table").jqGrid(
			'addRow',
			{
				rowID : id,
				position : "last",
				addRowParams : {
					keys:true,
					url :'clientArray'
				}
		});
	}
	function delRow(){
		var ids = $("#table").jqGrid("getGridParam","selarrrow");
		for(var i=0;i<ids.length;i++){
			$("#table").jqGrid("delRowData",ids[i]);
		}
	}
	function viewCondition(){
		saveEditing();
		var ids = $("#table").jqGrid('getDataIDs');
		var str = '';
		for(var i=0;i<ids.length;i++){
			var obj = $("#table").jqGrid("getRowData",ids[i]);
			str += " " + obj.leftBracket + ' ' + obj.fieldName + ' ' + obj.operate + ' ' + obj.value + ' ' + obj.rightBracket + ' ' + obj.joinStr;
		}
		alert(str);
	}
	function saveEditing(){
		var idObj = {};
		$("#table :input[name]").each(function(index,obj){
			var trId = $(obj).closest("tr").attr("id");
			idObj[trId] = true;
		});
		for(var pro in idObj){
			$("#table").jqGrid("saveRow",pro,{url:'clientArray'});
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
					onclick="javascript:window.location='${chartdesignctx}/base-info/datasource/step4.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/step-end.htm')">
					<span><span><b class="btn-icons btn-icons-ok"></b>完成</span></span>
				</button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					5.添加过滤条件,完成向导.
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="goalDataConditionStrs"/>
					<fieldset style="margin-top:4px;">
						<legend>过滤条件</legend>
						<div class="opt-body">
							<div class="opt-btn" id="tableBtn" style="margin-bottom:2px;">
								<button class='btn' type="button" onclick="addRow()">
									<span><span><b class="btn-icons btn-icons-add"></b>添加</span></span>
								</button>
								<button class='btn' type="button" onclick="delRow()">
									<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
								</button>
								<button class='btn' type="button" onclick="viewCondition()">
									<span><span><b class="btn-icons btn-icons-search"></b>查看条件</span></span>
								</button>
							</div>
							<table id="table"></table>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>