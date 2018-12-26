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
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
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
			alert("统计分组不能为空!");
			return;
		}
		var totalSets = [];
		for(var i=0;i<ids.length;i++){
			var str = "";
			var rowData = $("#table").jqGrid("getRowData",ids[i]);
			for(var j=0;j<colModels.length;j++){
				var fieldName = colModels[j].name;
				if(fieldName=='displayOrder'||fieldName.indexOf("format_")==0){
					continue;
				}
				if(str){
					str += ',';
				}
				var val = rowData[fieldName];
				if(!val&&colModels[j].editrules&&colModels[j].editrules.required){
					alert("第"+(i+1)+"行 " + colModels[j].label + " 不能为空!");
					return;
				}
				str += fieldName + ":\"" + (val==undefined?"":val) + "\"";
			}
			totalSets.push("{" + str + "}");
		}
		var totalSetStrs = "[" + totalSets.join(",") + "]";
		$(":input[name=totalSetStrs]").val(totalSetStrs);
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
			$("#" + rowId + " a[setBtn]").show();
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
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
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
			if(rowId<0){
				$("#table").jqGrid("delRowData",rowId);
			}
		}
	};
	//初始化所有列
	var colModels = ${colModels};
	var fieldAliasMap = ${fieldAliasMap};
	function initGrid(){
		for(var i=0;i<colModels.length;i++){
			if(colModels[i].name&&colModels[i].name.indexOf("format_")==0){
				colModels[i].formatter = function(val,options,rowObj){
					var html = "<label>";
					var datasourceId = options.colModel.name.split("_")[1];
					var aliasMap = fieldAliasMap[datasourceId];
					var typeName = "type_" + datasourceId;
					var valueName = "value_" + datasourceId;
					if(rowObj[typeName]&&rowObj[valueName]){
						var alias = aliasMap[rowObj[valueName]];
						if(!alias){
							alias = rowObj[valueName];
						}
						if(rowObj[typeName]=='<%=ChartDefinition.TOTALTYPE_COUNT%>'){
							html += "计数["+alias+"]";
						}else if(rowObj[typeName]=='<%=ChartDefinition.TOTALTYPE_SUM%>'){
							html += "求和["+alias+"]";
						}else{
							html += "自定义["+rowObj[valueName]+"]";
						}
					}else{
						html += "未设置";
					}
					html += "</label>";
					html += "<a class='small-button-bg' style='margin-right:4px;float:right;display:none;' setBtn=true onclick='setCountValue("+datasourceId+")' href='javascript:void(0);'  title='设置'><span class='ui-icon ui-icon-set' style='cursor:pointer;'></span></a>";
					return html;
				};
			}
		}
		/**
		var displayOrderCol = 
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
			data : ${totalSetDatas},
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
		var height = $("#opt-content").height() - $("#table").position().top-110;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
		$("#table").tableDnD({});
	}
	function setCountValue(datasourceId){
		var rowData = $("#table").jqGrid("getRowData",editId);
		var typeName = "type_" + datasourceId;
		var valueName = "value_" + datasourceId;
		var type = rowData[typeName],value=rowData[valueName];
		var url = "${chartdesignctx}/chart-definition/set-count-param.htm?type=" + (type?type:"") + "&value=" + (value?value:"");
		url += "&datasourceId=" + datasourceId + "&editId=" + editId;
		$.colorbox({
			href:url,
			iframe:true, 
			width:400, 
			height:200,
			overlayClose:false,
			title:"设置统计参数"
		});
	}
	function afterSetCountParam(type,value,datasourceId){
		var obj = {};
		obj["type_" + datasourceId] = type;
		obj["value_" + datasourceId] = value;
		obj["format_" + datasourceId] = value;
		$("#table").jqGrid("setRowData",editId,obj);
		$("#" + editId + " a[setBtn]").show();
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
					onclick="javascript:window.location.href='${chartdesignctx}/chart-definition/step2.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/step4.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					3/6.选择统计对象
					<label style="font-size:10px;margin-left:10px;font-weight:normal;">选择行后可按住鼠标调整顺序</label>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="totalSetStrs"/>
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