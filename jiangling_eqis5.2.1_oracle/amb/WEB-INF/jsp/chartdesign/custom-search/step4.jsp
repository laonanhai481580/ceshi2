<%@page import="com.ambition.chartdesign.entity.CustomSearchTable"%>
<%@page import="com.ambition.chartdesign.entity.ChartListViewColumn"%>
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
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<style>
  .sortable { list-style-type: none; margin: 0; padding: 0; }
  .sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size:14px; height: 18px; }
  .sortable li span { position: absolute; margin-left: -1.3em; }
</style>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化可选数据集
		createGrid();
	    //初始化连接列字段
	    searchTableChange();
	}
	function searchTableChange(callback){
		var searchTableId = $("#searchTable").val();
		$("#tableName").html($("#searchTable").find("option[value="+searchTableId+"]").html());
		var hisTableId = $("#hisTableId").val();
		if(!saveEditing(true)){
			if(hisTableId){
				$("#hisTableId").val(hisTableId);
			}
			return;
		}
		var columnDatas = $("#table").jqGrid("getRowData");
		var params = {
			hisTableId:hisTableId,
			newTableId : searchTableId,
			tableColumnStr : JSON.stringify(columnDatas)
		};
		$("#form :input[name]").each(function(index,obj){
			if(obj.type=='radio'){
				if($(obj).is(":checked")){
					params[obj.name] = obj.value;
				}
			}else{
				params[obj.name] = obj.value;
			}
		});
		var url = '${chartdesignctx}/custom-search/save-and-load-join-info.htm';
		$.showMessage("正在加载字段信息,请稍候...","custom");
		$(":input").attr("disabled","disabled");
		$.post(url,params,function(result){
			$(":input").removeAttr("disabled");
			$.clearMessage();
			if(result.error){
				alert("保存字段映射信息失败!");
				$("#searchTable").val(hisTableId);
				return;
			}else{
				if(callback&&$.isFunction(callback)){
					callback();
					return;
				}
				$("#hisTableId").val(searchTableId);
				
				$(":input[name=joinMode][value="+result.joinMode+"]").attr("checked","checked");
				$("#selfJoinFields").val(result.selfJoinFields);
				$("#mainJoinFields").val(result.mainJoinFields);
				
				//添加数据
				createGrid(result.rows);
			}
		},'json');
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
		$("tr[id="+rowId+"]").find(":input").css("width","99%");
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
	//初始化可选数据集
	var grid = null;
	function createGrid(data){
		if(grid){
			grid.GridDestroy();
		}
		$("#tableDiv").html('<table id="table"></table>');
		grid = $("#table").jqGrid({
			datatype:'local',
			data : data?data:[],
			rownumbers : true,
			rowNum : 1000,
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'columnName',index:'columnName',label:'字段名',width:160},
				{name:'alias',index:'alias',label:'字段别名',width:160},
				{name:'listColumnName',index:'listColumnName',label:'对应台账字段',width:190,editable:true,editrules:{},edittype:'select',editoptions:{value:'${fieldInfos}'},formatter:'select'},
				{name:'joinEmptyValue',index:'joinEmptyValue',label:'拼接为空时填充数据',width:190,editable:true},
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
			gridComplete : function(){
				//$("#table").tableDnDUpdate({});
			}
		});
		//$("#table").tableDnD({});
		
		var height = $("#opt-content").height() - $("#table").position().top-180;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
	}
	function search(){
		$("#canSelDataSet").jqGrid("setGridParam",{
			postData:{
				name : $(":input[name=searchName]").val()
			}
		}).trigger("reloadGrid");
	}
	function submitForm(url){
		searchTableChange(function(){
			if(url.indexOf("?")>1){
				url += "&";
			}else{
				url += "?";
			}
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		});
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
					onclick="javascript:window.location.href='${chartdesignctx}/custom-search/step3.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/custom-search/step5.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					4/6.定义数据填充方式
					<div style="float:right;margin-right:4px;">
						数据源:
						<s:select list="searchTables" id="searchTable"
							onchange="searchTableChange(this)"
							listKey="id" listValue="name" theme="simple"></s:select>
					</div>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="hisTableId" id="hisTableId"/>
					<fieldset>
						<legend><span id="tableName"></span>填充方式</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td>
									<input type="radio" name="joinMode" id="joinMode1" value="<%=CustomSearchTable.JOIN_MODE_附加 %>"/>
									<label for="joinMode1"><%=CustomSearchTable.JOIN_MODE_附加 %></label>
								</td>
							</tr>
							<tr>
								<td>
									<input type="radio" name="joinMode" id="joinMode2" value="<%=CustomSearchTable.JOIN_MODE_拼装 %>"/>
									<label for="joinMode2"><%=CustomSearchTable.JOIN_MODE_拼装 %></label>
									连接字段:
									<input type="text" name="selfJoinFields" id="selfJoinFields"/>
									台账连接字段:
									<input type="text" name="mainJoinFields" id="mainJoinFields"/>
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
					<fieldset style="margin-top:8px;">
						<legend>映射关系</legend>
						<div id="tableDiv"></div>
					</fieldset>
			</div>
		</div>
	</div>
</body>
</html>