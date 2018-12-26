<%@page import="com.ambition.chartdesign.entity.ChartListViewColumn"%>
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
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
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
	//编辑参数
	var editId=null;
	var editParams={
		keys:true,
		url : 'clientArray',
		oneditfunc:function(rowId){
			oneditRow(rowId);
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			editId = null;
			if(!window.notAdd){
				var nextId = $("#" + rowId).next().attr("id");
				if(nextId){
					$("#table").jqGrid("editRow",nextId,editParams);
				}
			}
		},
		afterrestorefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
		}
	};
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
				{name:'dataType',index:'dataType',label:'字段类型',width:100,formatter:function(val,o,rowObj){
					var form = dataTypeFormatterObj[val];
					return form?form:val;
				}},
				{name:'alias',index:'alias',label:'字段别名',width:240,editable:true,editrules:{required:true}},
				{name:'width',index:'width',label:'宽度',width:50,editable:true,editrules:{digits:true}},
				{name:'isHidden',index:'isHidden',label:'是否隐藏',
					width:70,editable:true,editrules:{required:true},
					formatter:'select',
					edittype:'select',
					editoptions:{value:'false:否;true:是'}
				},
				{name:'formatterType',index:'formatterType',label:'格式化类型',hidden:true},
				{name:'formatterValue',index:'formatterValue',label:'格式化值',hidden:true},
				{name:'formatter',index:'formatter',label:'格式化',width:140,editable:true,
					formatter:function(value,o,rowObj){
						return formatterFiedFormat(rowObj);
					}
				}/**,
				{name:'displayOrder',index:'displayOrder',label:'排序',width:70,sortable:false,
					formatter:function(val,o,rowObj){
						var top = "<a id='top_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:10px;' href=\"javascript:void(0);toTop('top_"+rowObj.id+"');\" title='向上'><span class=\"ui-icon ui-icon-arrowthick-1-n\" style='cursor:pointer;'></span></a>";
						var bottom = "<a id='bottom_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:6px;' href=\"javascript:void(0);toBottom('bottom_"+rowObj.id+"');\" title='向下'><span class=\"ui-icon ui-icon-arrowthick-1-s\" style='cursor:pointer;'></span></a>";
						return top+ bottom;
					}
				}*/
			],
			rowNum:500,
		    multiselect: false,
		    cellsubmit:'clientArray',
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			gridEdit: true,
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
		$("#table").jqGrid("setGridHeight",systemHeight);
		$("#table").tableDnD({});
	}
	//编辑时的事件
	function oneditRow(rowId){
		$("#" + rowId + "_formatter").attr("readonly",true)
		.click(function(){
			eidtFormatter(rowId);
		});
	}
	function eidtFormatter(rowId){
		var url = "${chartdesignctx}/list-definition/set-formatter.htm?rowId=" + rowId;
		$.colorbox({
			href:url,
			iframe:true, 
			width:$(window).width()-50, 
			height:$(window).height()-50,
			overlayClose:false,
			title:"格式化设置"
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
		var ids = $("#table").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("字段信息不能为空!");
			return;
		}
		var strs = [];
		for(var i=0;i<ids.length;i++){
			var rowData = $("#table").jqGrid("getRowData",ids[i]);
			var str = '{name:"'+rowData.name+'",alias:"'+rowData.alias+'",isHidden:"'+rowData.isHidden+'",formatterType:"'+rowData.formatterType+'",formatterValue:"'+encodeURI(rowData.formatterValue)+'",width:"'+rowData.width+'"}';
			strs.push(str);
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
					onclick="backTo('${chartdesignctx}/list-definition/input.htm?isBack=true')">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/list-definition/step-end.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>完成配置</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<div id="opt-content" style="overflow:hidden;">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-size:14px;line-height:30px;">
					<b>2/2.设置显示格式</b>
					<label style="font-size:10px;margin-left:10px;">选择行后可按住鼠标调整顺序</label>
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