<%@page import="com.ambition.chartdesign.entity.ChartListViewColumn"%>
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
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化数据字段
		initDataColumnSet();
	}
	
	//编辑参数
	var editId=null;
	var editParams={
		keys:true,
		url : 'clientArray',
		oneditfunc:function(rowId){
			/**$("#" + rowId + " select[name]")
				.css("width","95%")
				.each(function(index,obj){
					if(fieldTypes[obj.name]){
						$(obj).bind("change",function(){
							fieldSelectChange(obj);
						});
						fieldSelectChange(obj);
					};
			});*/
			oneditRow(rowId);
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			editId = null;
			if(rowId<0){
				var rowData = $("#table").jqGrid("getRowData",rowId);
				$("#table").jqGrid("delRowData",rowId);
				rowData.id = -rowId;
				$("#table").jqGrid("addRowData",-rowId,rowData);
				rowId = -rowId;
			}
			if(!window.notAdd){
				var nextId = $("#" + rowId).next().attr("id");
				if(nextId){
					$("#table").jqGrid("editRow",nextId,editParams);
				}else{
					addRow();
				}
			}
		},
		afterrestorefunc:function(rowId){
			editId = null;
			if(rowId<0){
				$("#table").jqGrid("delRowData",rowId);
			}
		}
	};
	//编辑时的事件
	function oneditRow(rowId){
		$("#" + rowId + "_formatter").attr("readonly",true)
		.click(function(){
			eidtFormatter(rowId);
		});
	}
	function eidtFormatter(rowId){
		var url = "${chartdesignctx}/custom-search/set-formatter.htm?rowId=" + rowId;
		$.colorbox({
			href:url,
			iframe:true, 
			width:$(window).width()-50, 
			height:$(window).height()-50,
			overlayClose:false,
			title:"格式化设置",
			onClosed:function(){
				setTimeout(function(){
					$("#" + rowId + "_formatter").focus();
				},200);
			}
		});
	}
	function getRowData(){
		if(editId){
			return $("#table").jqGrid("getRowData",editId);
		}else{
			return {};
		}
	}
	function setRowData(obj){
		if(editId){
			$("#table").jqGrid("setRowData",editId,obj);
			$("#" + editId + "_formatter").val(formatterFiedFormat(obj));
		}
	}
	//初始化数据字段
	function initDataColumnSet(){
		$("#table").jqGrid({
			datatype:'local',
			data : ${formatColumnDatas},
			rownumbers : true,
			rowNum : 1000,
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'columnName',index:'columnName',label:'字段编码',width:160,editable:true,editrules:{required:true}},
				{name:'showName',index:'showName',label:'显示名称',width:160,editable:true,editrules:{required:true}},
				{name:'columnWidth',index:'columnWidth',label:'列宽',width:80,editable:true,editrules:{number:true,minValue:1}},
				{name:'visible',index:'visible',label:'是否显示',width:80,editable:true,edittype:'select',editoptions:{value:"是:是;否:否"}},
				{name:'total',index:'total',label:'是否合计',width:80,editable:true,edittype:'select',editoptions:{value:"否:否;是:是"}},
				{name:'canOrder',index:'canOrder',label:'是否排序',width:80,editable:true,edittype:'select',editoptions:{value:"否:否;是:是"}},
				{name:'rowspan',index:'rowspan',label:'是否合并单元格',width:110,editable:true,edittype:'select',editoptions:{value:"否:否;是:是"}},
				{name:'formatterType',index:'formatterType',label:'格式化类型',hidden:true},
				{name:'formatterValue',index:'formatterValue',label:'格式化值',hidden:true},
				{name:'formatter',index:'formatter',label:'格式化',width:140,editable:true,
					formatter:function(value,o,rowObj){
						return formatterFiedFormat(rowObj);
					}
				}
			],
			gridEdit: true,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			ondblClickRow: function(rowId){
				var res = saveEditing(true);
				if(!res){
					return;
				}
				$("#table").jqGrid("editRow",rowId,editParams);
			},
			gridComplete: function(){
				$("#table").tableDnDUpdate({});
		   	}
		});
		var height = $("#opt-content").height() - $("#table").position().top-130;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
		$("#table").tableDnD({});
	}
	
	//表格拖动
	/**$("#table").tableDnD({
		onDrop: function(table, row) {
			alert(1)
		}
	});*/
	
	function formatterFiedFormat(rowData){
		if(editId&&rowData.formatterType==undefined){
			rowData = $("#table").jqGrid("getRowData",editId);
		}
		if(rowData.formatterType){
			if(rowData.formatterType=='<%=ChartListViewColumn.FORMATTER_TYPE_CUSTOM%>'){
				return "自定义";
			}else if(rowData.formatterType=='<%=ChartListViewColumn.FORMATTER_TYPE_GROUP%>'){
				return "选项组:" + rowData.formatterValue;
			}else if(rowData.formatterType=='<%=ChartListViewColumn.FORMATTER_TYPE_NUMBER%>'){
				return "数字:" + rowData.formatterValue + "位";
			}else if(rowData.formatterType=='<%=ChartListViewColumn.FORMATTER_TYPE_ATTACHMENT%>'){
				return "附件";
			}else{
				return rowData.formatterValue?rowData.formatterValue:rowData.formatterType;
			}
		}
		return "";
	}
	var id = (new Date()).getTime();
	function addRow(){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		//查询最后的Id
		var size = $("#table").find("tr").length;
		var idIndex = -id--;
		$("#table").jqGrid('addRowData',idIndex,{
			id:idIndex,
			columnName:'field' + size
		},'last');
		$("#table").jqGrid("editRow",idIndex,editParams);
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
	function saveEditing(notAdd){
		window.notAdd = notAdd;
		if(editId){
			var res = $("#table").jqGrid("saveRow",editId,editParams);
			delete window.notAdd;
			return res;
		}
		delete window.notAdd;
		return true;
	}
	
	function submitForm(url){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		var columnData = $("#table").jqGrid("getRowData");
		if(columnData.length==0){
			alert("列不能为空!");
			return;
		}
		//判断是否存在重复的字段名
		var existJson = {};
		for(var index in columnData){
			var data = columnData[index];
			if(existJson[data.columnName]){
				alert("字段名["+data.columnName+"]重复!");
				return;
			}
			existJson[data.columnName] = true;
		}
		var columnDataJsonStr = JSON.stringify(columnData);
		$("#columnDataJsonStr").val(columnDataJsonStr);
		if($("#form").valid()){
			if(url.indexOf("?")>1){
				url += "&";
			}else{
				url += "?";
			}
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
					1/6.定义台账字段
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="columnDataJsonStr" id="columnDataJsonStr"/>
					<div class="opt-body">
						<div class="opt-btn" id="tableBtn" style="margin-bottom:2px;">
							<button class='btn' type="button" onclick="addRow()">
								<span><span><b class="btn-icons btn-icons-add"></b>添加</span></span>
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