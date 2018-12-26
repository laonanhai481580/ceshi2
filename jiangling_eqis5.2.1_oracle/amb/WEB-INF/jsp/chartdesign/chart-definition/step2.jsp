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
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化表格	
		initGrid();
	}
	function submitForm(url){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		var ids = $("#table").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("正在分组不能为空!");
			return;
		}
		var groupSets = [];
		for(var i=0;i<ids.length;i++){
			var str = "";
			var rowData = $("#table").jqGrid("getRowData",ids[i]);
			for(var j=0;j<colModels.length;j++){
				var fieldName = colModels[j].name;
				if(fieldName=='displayOrder'){
					continue;
				}
				if(str){
					str += ',';
				}
				var val = rowData[fieldName];
				str += "\"" + fieldName + "\":\"" + (val==undefined?"":val) + "\"";
			}
			groupSets.push("{" + str + "}");
		}
		var groupSetStrs = "[" + groupSets.join(",") + "]";
		$(":input[name=groupSetStrs]").val(groupSetStrs);
		$('#form').attr('action',url);
		$("button").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$('#form').submit();
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	//编辑参数
	var editId=null;
	var editParams={
		keys:true,
		url : 'clientArray',
		oneditfunc:function(rowId){
			$("#" + rowId + " select[name]")
				.css("width","95%")
				.each(function(index,obj){
					if(fieldTypes[obj.name]){
						$(obj).bind("change",function(){
							fieldSelectChange(obj);
						});
						fieldSelectChange(obj);
					};
			});
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
	//初始化所有列
	var colModels = ${colModels};
	function initGrid(){
		/**var displayOrderCol = 
			{name:'displayOrder',index:'displayOrder',label:'排序',width:70,sortable:false,
				formatter:function(val,o,rowObj){
					var top = "<a id='top_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:10px;' href=\"javascript:void(0);toTop('top_"+rowObj.id+"');\" title='向上'><span class=\"ui-icon ui-icon-arrowthick-1-n\" style='cursor:pointer;'></span></a>";
					var bottom = "<a id='bottom_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:6px;' href=\"javascript:void(0);toBottom('bottom_"+rowObj.id+"');\" title='向下'><span class=\"ui-icon ui-icon-arrowthick-1-s\" style='cursor:pointer;'></span></a>";
					return top+ bottom;
				}
			};
		colModels.push(displayOrderCol);*/
		$("#table").jqGrid({
			datatype:'local',
			rownumbers : true,
			width : $("#tableBtn").width(),
			data : ${groupSetDatas},
			colModel: colModels,
			gridEdit: true,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){
				$("#table").tableDnDUpdate({});
			},
			ondblClickRow: function(rowId){
				var res = saveEditing(true);
				if(!res){
					return;
				}
				$("#table").jqGrid("editRow",rowId,editParams);
			}
		});
		var groupHeaders = ${groupHeaders};
		if(groupHeaders.length>0){
			$("#table").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders: groupHeaders
			});
		}
		var height = $("#opt-content").height() - $("#table").position().top-130;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
		$("#table").tableDnD({});
	}
	var fieldTypes = ${fieldTypes};
	function fieldSelectChange(obj){
		var fieldType = fieldTypes[obj.name];
		if(fieldType){
			var type = fieldType[obj.value];
			var dataSourceId = obj.name.split("_")[1];
			var formatterSelectName = "format_" + dataSourceId;
			var formatterObj = $(obj).closest("tr").find("select[name="+formatterSelectName+"]");
			if(type == '<%=Hibernate.TIMESTAMP.getName()%>' 
				|| type == '<%=Hibernate.DATE.getName()%>'){
				if(formatterObj.find("option").length<2){
					formatterObj
					.show()
					.find("option").remove();
					formatterObj.append("<option value='<%=ChartDefinition.DATEFORMATTER_YEAR%>' role='option'><%=ChartDefinition.DATEFORMATTER_YEAR_SHOW%></option>");
					formatterObj.append("<option value='<%=ChartDefinition.DATEFORMATTER_QUARTER%>' role='option'><%=ChartDefinition.DATEFORMATTER_QUARTER_SHOW%></option>");
					formatterObj.append("<option value='<%=ChartDefinition.DATEFORMATTER_MONTH%>' role='option'><%=ChartDefinition.DATEFORMATTER_MONTH_SHOW%></option>");
					formatterObj.append("<option value='<%=ChartDefinition.DATEFORMATTER_WEEK%>' role='option'><%=ChartDefinition.DATEFORMATTER_WEEK_SHOW%></option>");
					formatterObj.append("<option value='<%=ChartDefinition.DATEFORMATTER_DATE_SHOW%>' role='option'><%=ChartDefinition.DATEFORMATTER_DATE_SHOW%></option>");
				}
			}else{
				formatterObj.find("option").remove();
				formatterObj.hide().append("<option value='' role='option'></option>");
			}
		}
	}
	var id = (new Date()).getTime();
	function addRow(){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		var idIndex = -id--;
		$("#table").jqGrid('addRowData',idIndex,{
			id:idIndex
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
	function toTop(clickId){
		var rowTr = $("#" + clickId).closest("tr");
		var rowId = rowTr.attr("id");
		var prevTr = $("tr[id="+rowId+"]").prev();
		var prevId = prevTr.attr("id");
		if(prevId){
			prevTr.before(rowTr);
		}
		clearSelectionAndSel(rowId);
	}
	
	function toBottom(clickId){
		var rowTr = $("#" + clickId).closest("tr");
		var rowId = rowTr.attr("id");
		var nextTr = $("tr[id="+rowId+"]").next();
		var nextId = nextTr.attr("id");
		if(nextId){
			nextTr.after(rowTr);
		}
		clearSelectionAndSel(rowId);
	}
	function clearSelectionAndSel(clickId){
		var selIds = $("#table").jqGrid("getGridParam","selarrrow");
		var $t = $("#table")[0];
		for(var index in selIds){
			$("#table").jqGrid("setSelection",selIds[index],false);		
		}
		$t.p.selarrrow=[];
		$("#table").jqGrid("setSelection",clickId,false);
		$("#table")[0].updatepager(true,false);
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
					onclick="javascript:window.location.href='${chartdesignctx}/chart-definition/input.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/step3.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					2/6.指定统计维度
					<label style="font-size:10px;margin-left:10px;font-weight:normal;">选择行后可按住鼠标调整顺序</label>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="groupSetStrs"/>
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