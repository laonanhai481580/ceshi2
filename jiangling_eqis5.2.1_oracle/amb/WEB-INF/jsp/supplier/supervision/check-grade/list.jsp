<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/widgets/tablednd/jquery.tablednd.js"></script>
<script type="text/javascript">
	function contentResize(){
		var tableHeight = $(".ui-layout-center").height();
		jQuery("#list1").jqGrid('setGridHeight',tableHeight-75);
		jQuery("#list1").jqGrid('setGridWidth',_getTableWidth() - 204);
		$("#checkGradeTypeTree").height(tableHeight - 104);
	}
	$(document).ready(function(){
		$("#checkGradeTypeTree").jstree({ 
			"json_data" : {
				"data" : [
					{ 
						"attr" : { "id" : "-1"},
						"state" : "closed",
						"data" : { 
							"title" : "所有项目"
						}
					}
				],
				"ajax" : {
					"url" : "${supplierctx}/supervision/check-grade/check-grade-type-datas.htm",
					data : function(node){
						return {id:node.attr("id")};
					}
				}
			},
			"contextmenu":{
				items : {
					create:{
						label : '添加'
					},
					rename : {
						label : '编辑'
					},
					remove : {
						label : '删除'
					},
					ccp : null
				}
			},
			"dnd" : {
				"drop_finish" : function () { 
					alert("DROP"); 
				},
				"drag_check" : function (data) {
					alert("check");
					if(data.r.attr("id") == "phtml_1") {
						return false;
					}
					return { 
						after : false, 
						before : false, 
						inside : true 
					};
				},
				"drag_finish" : function (data) { 
					alert("DRAG OK"); 
				}
			},
			core : { "initially_open" : ["-1"] },
			"plugins" : [ "themes", "json_data","ui","contextmenu","dnd","crrm"]
		}).bind("move_node.jstree",function(event,data){
			var newParentId = data.rslt.np.attr("id");
			if(isNaN(newParentId)){
				$.jstree.rollback(data.rlbk);
			}else{
				var params = {
					parentId : newParentId,
					id : data.rslt.o.attr("id"),
					position : data.rslt.p,
					orderNum : data.rslt.r.attr("orderNum")
				};
				$("#checkGradeTypeTree").attr("disabled","disabled");
				<security:authorize ifAnyGranted="supervision-grade-type-move">
				$.post("${supplierctx}/supervision/check-grade/move-check-grade-type.htm",params,function(result){
					$("#checkGradeTypeTree").attr("disabled","");
					if(result.error){
						alert(result.message);
						$.jstree.rollback(data.rlbk);
					}else{
						data.rslt.o.attr("orderNum",result.obj.orderNum);
					}
				},'json');
				</security:authorize>
			}
		}).bind("create.jstree",function(event,data){
			$("#checkGradeTypeTree").attr("disabled","disabled");
			var parentId = data.rslt.parent.attr("id");
			var params = {
				parentId : parentId<0?'':parentId,
				name : data.rslt.name
			};
			<security:authorize ifAnyGranted="supervision-grade-type-save">
			$.post("${supplierctx}/supervision/check-grade/store-check-grade-type.htm",params,function(result){
				$("#checkGradeTypeTree").attr("disabled","");
				if(result.error){
					alert(result.message);
					$.jstree.rollback(data.rlbk);
				}else{
					data.rslt.obj.attr("id",result.obj.id);
					data.rslt.obj.attr("orderNum",result.obj.orderNum);
				}
			},'json');
			</security:authorize>
		}).bind("rename.jstree",function(event,data){
			if(data.rslt.old_name == data.rslt.new_name){
				return;
			}
			$("#checkGradeTypeTree").attr("disabled","disabled");
			var params = {
				id : data.rslt.obj.attr("id"),
				name : data.rslt.new_name
			};
			<security:authorize ifAnyGranted="supervision-grade-type-save">
			$.post("${supplierctx}/supervision/check-grade/store-check-grade-type.htm",params,function(result){
				$("#checkGradeTypeTree").attr("disabled","");
				if(result.error){
					alert(result.message);
					$.jstree.rollback(data.rlbk);
				}else{
					$("#list1").trigger("reloadGrid");
				}
			},'json');
			</security:authorize>
		}).bind("remove.jstree",function(event,data){
			var id = data.rslt.obj.attr("id");
			if(id<0){
				$.jstree.rollback(data.rlbk);
				return;
			}
			$.vakata.context.hide();
			if(confirm("确定要删除吗?")){
				$("#checkGradeTypeTree").attr("disabled","disabled");
				var params = {
					deleteIds : id
				};
				<security:authorize ifAnyGranted="supervision-grade-type-delete">
				$.post("${supplierctx}/supervision/check-grade/delete-check-grade-type.htm",params,function(result){
					$("#checkGradeTypeTree").attr("disabled","");
					if(result.error){
						alert(result.message);
						$.jstree.rollback(data.rlbk);
					}else{
						if(id == selCheckGradeTypeId){
							selCheckGradeTypeId = null;
							$("#list1").jqGrid("clearGridData");
							$("#checkGradeToolbar button").attr("disabled","disabled");
						}
					}
				},'json');
				</security:authorize>
			}else{
				$.jstree.rollback(data.rlbk);
			}
		}).bind("select_node.jstree",function(e,data){
			if($(data.rslt.obj).hasClass("jstree-closed")){
				$.jstree._reference("#checkGradeTypeTree").open_node($(data.rslt.obj),null,false);
			}else if($(data.rslt.obj).hasClass("jstree-open")){
				$.jstree._reference("#checkGradeTypeTree").close_node($(data.rslt.obj),null,false);
			}else{
				var childCount = $.jstree._reference("#checkGradeTypeTree")._get_children($(data.rslt.obj)).length;
				if(childCount == 0){
					loadCheckGradeByCheckGradeTypeId(data.rslt.obj.attr("id"),$.jstree._reference("#checkGradeTypeTree").get_text($(data.rslt.obj)));
				}
			}
		});
		//表格拖动
		$("#list1").tableDnD({
			onDrop: function(table, row) {
				var position = 'before';
				var orderNum = 0;
				var id = row.id;
		        var newIndex = row.rowIndex;
		        var rows = $("#list1")[0].rows;
		        if(rows.length<3){
		        	position = 'last';
		        }else if(newIndex == rows.length-1){
		        	position = 'last';
		        }else{
		        	position = 'before';
		        	var newId = rows[newIndex+1].id;
		        	var rowData = $("#list1").jqGrid("getRowData",newId);
		        	if(rowData){
		        		orderNum = rowData.orderNum;
		        	}else{
		        		alert("数据不存在!");
		        		return;
		        	}
		        }
		        $("#list1").attr("disabled","disabled");
		        var params = {
	        		position : position,
	        		orderNum : orderNum,
	        		id : id
		        };
		        $.post("${supplierctx}/supervision/check-grade/move-check-grade.htm",params,function(result){
		        	$("#list1").attr("disabled","");
		        	if(result.error){
		        		alert(result.message);
		        		$("#list1").trigger("reloadGrid");
		        	}else{
		        		$("#list1").jqGrid("setRowData",id,{orderNum:result.obj.orderNum});
		        		$("#list1")[0].refreshIndex();
		        		var addRowNum = function (pos,irow,pG,rN) {
			    			var v =  (parseInt(pG,10)-1)*parseInt(rN,10)+1+irow,
			    			prp = $("#list1")[0].formatCol( pos,irow,v, null, irow, true);
			    			return "<td role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" "+prp+">"+v+"</td>";
			    		}
			        	for(var i=1;i<rows.length;i++){
			        		var $row = $(rows[i]);
			        		var numStr = addRowNum(0,i-1,1,20);
			        		$row.find(".jqgrid-rownum").replaceWith(numStr);
			        	}
		        	}
		        },'json');
			}
		});
		//jqGrid
		$("#list1").jqGrid({ 
			rownumbers:true,
			shrinkToFit:true,
			sortorder: "asc",
			sortname: "orderNum",
			colModel:[ 
	           	{name:'id',index:'id',width:50,align:"center",hidden:true}, 
	            {label:'项目编号',name:'checkGradeTypeId',index:'checkGradeTypeId',width:10,hidden:true}, 
	            {label:'orderNum',name:'orderNum',index:'orderNum',width:10,hidden:true,sortable:false}, 
	            {label:'项目名称',name:'checkGradeTypeName',index:'checkGradeTypeName',width:120,sortable:false}, 
	            {label:'确认事项',name:'name',index:'name',width:300,editable:true,editrules:{required:true},sortable:false}, 
	            {label:'权重',name:'weight',index:'weight',width:70,editable:true,editrules:{required:true,number:true,min:0},sortable:false} 
	        ], 
           	multiselect: true,
		   	gridComplete: function(){
		   		$("#list1").tableDnDUpdate();
		   	},
		   	ondblClickRow: editRow,
			editurl: "${supplierctx}/supervision/check-grade/save.htm",
			serializeRowData:function(data){
				if(data.id==0){
					data.id="";
					data.checkGradeTypeId = selCheckGradeTypeId;
				}
				return data;
			}
		}); 
		$("button","#checkGradeToolbar").attr("disabled","disabled");
		contentResize();
	});
	
	var selCheckGradeTypeId = null,selCheckGradeTypeName=null;
	function loadCheckGradeByCheckGradeTypeId(checkGradeTypeId,checkGradeTypeName){
		if(checkGradeTypeId == selCheckGradeTypeId){
			return;
		}
		selCheckGradeTypeId = checkGradeTypeId;
		selCheckGradeTypeName = checkGradeTypeName;
		var params = {
			url : '${supplierctx}/supervision/check-grade/check-grade-datas.htm',
			postData : {id:selCheckGradeTypeId}
		};
		if(lastsel){
			$("#list1").jqGrid("restoreRow",lastsel);
		}
		$("#list1").jqGrid("setGridParam",params).trigger("reloadGrid");
		$("#checkGradeToolbar button").attr("disabled","");
	}
	var editParams={
		keys : true,
		oneditfunc : function(rowId){
		},
		successfunc: function( response ) {
			var jsonData = eval("(" + response.responseText + ")");
			if(jsonData.error){
				alert(jsonData.message);
				return false;
			}else{
				return true;
			}
	    },
		aftersavefunc : function(rowId, data) {
			var json = eval("(" + data.responseText + ")");
			var obj = json.obj;
			if(rowId == 0){
				$("#list1").jqGrid("setRowData",rowId,{id:obj.id,orderNum:obj.orderNum});
				$("#" + rowId,"#list1").attr("id",obj.id);//修改这一行的编号
				$("#"+$.jgrid.jqID(obj.id), "#list1").removeClass("jqgrid-new-row");//修改新建的标志
				$("#jqg_list1_" + rowId).attr("id","jqg_list1_" + obj.id);//修改Checkbox的编号
				var selarrrow = $("#list1")[0].p.selarrrow;//更新多选的
				for(var i=0;i<selarrrow.length;i++){
					if(selarrrow[i]==0){
						selarrrow.splice(i,1,obj.id);
						break;
					}
				}
			}
			makeEditable(true);
			editNextRow(obj.id);
		},
		afterrestorefunc : function(rowId) {
			if(rowId==0){
				$("#list1").jqGrid("delRowData",0);
				var selarrrow = $("#list1")[0].p.selarrrow;//更新多选的
				for(var i=0;i<selarrrow.length;i++){
					if(selarrrow[i]==0){
						selarrrow.splice(i,1);
						break;
					}
				}
			}
			makeEditable(true);
		},
		restoreAfterError:false
	};

	var lastsel = null,editing=false;
	function makeEditable(editable) {
		if (editable) {
			editing = false;
			lastsel=null;
		} else {
			editing = true;
		}

	}
	/**
	 * 新建一行
	 * @param byEnter
	 * @return
	 */
	function addRowByKey(byEnter) {
		if(!selCheckGradeTypeId){
			alert("请先选择项目!");
			return;
		}
		if (byEnter == undefined) {
			byEnter = false;
		}
		var editableGrid = $("#list1");
		if(editing){
			if(lastsel != null){
				editableGrid.jqGrid("restoreRow", lastsel,editParams.afterrestorefunc);
			}else{
				alert("请先完成编辑！");
				return;
			}
		}
		editableGrid.jqGrid('addRowData',0,{
			checkGradeTypeId:selCheckGradeTypeId,
			checkGradeTypeName : selCheckGradeTypeName
		});
		editRow(0);
	}

	/**
	 * 编辑行
	 * @param rowId
	 * @return
	 */
	function editRow(rowId,iRow,iCol,e) {
		var editableGrid = $("#list1");
		if(rowId==lastsel){
//			双击当前正在编辑的行，不用做任何特殊处理
			return;
		}else{
			editableGrid.jqGrid("restoreRow", lastsel,editParams.afterrestorefunc);
		}
		editableGrid.resetSelection();
		editableGrid.setSelection(rowId);
		lastsel = rowId;
		editableGrid.jqGrid("editRow", rowId, editParams);
		makeEditable(false);
	}
	/**
	 * 编辑下一行
	 * @param rowId
	 * @return
	 */
	function editNextRow(rowId) {
		var editableGrid = $("#list1");
		var ids = editableGrid.jqGrid("getDataIDs");
		var index = editableGrid.jqGrid("getInd", rowId);
		index++;
		if (index > ids.length) {// 当前编辑行是最后一行
			addRowByKey(true);
		} else {
			editRow(ids[index - 1]);
		}
		
	}
	
	/**
	 * 删除
	 * @param rowId
	 * @return
	 */
	function delRow() {
		var editableGrid = $("#list1");
		if (editing) {
			if(lastsel != null){
				editableGrid.jqGrid("restoreRow",lastsel,editParams.afterrestorefunc);
			}else{
				alert("请先完成编辑！");
				return;
			}
		}
		var ids = editableGrid.getGridParam('selarrrow');
		if (ids.length < 1) {
			alert("请选中需要删除的记录！");
			return;
		}
		if(confirm("确定要删除吗?")){
			var params={
				deleteIds : ids.join(',')
			};
			var url = "${supplierctx}/supervision/check-grade/delete.htm";
			$.post(url, params, function(data) {
				if(data.error){
					alert(data.message);
				}else{
					if (ids.length>0) {
						editableGrid.jqGrid().trigger("reloadGrid"); 
					}
				}
			},'json');
		}
	}
	//预览稽查评分表
	function previewCheckGrade(){
		var html = '<div id="preViewheckGradeTable" style="padding:6px;"><div style="text-align:center;">评分表加载中,请稍候... ...</div></div>';
		$.colorbox({
			title : '稽查评分表预览',
			html:html,
			height:550,
			width:900
		});
		$("#preViewheckGradeTable").load("${supplierctx}/supervision/check-grade/preview-check-grade-table.htm");
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="estimate";
		var thirdMenu="_check_grade";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-estimate-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
			<div class="opt-body">
				<table style="width:100%;height:100%;" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top" style="width:100px;">
							<div class="opt-btn" style="line-height:33px;">
								<table style="margin:0px;padding:0px;" >
									<tr>
										<td>监察评分项目</td>
										<td style="padding-bottom:2px;">
											<security:authorize ifAnyGranted="supervision-grade-preview">
											<a class="small-button-bg" style="margin-left:2px;float:left;"   onclick="previewCheckGrade();"><span class="ui-icon ui-icon-zoomin" style='cursor:pointer;' title="评分表预览"></span></a>
											</security:authorize>
										</td>
									</tr>									
								</table>
							</div>
							<div class="opt-body" id="checkGradeTypeTree" style="padding:6px;width:270px;overflow:auto;">
							</div>
						</td>
						<td style="width:6px;border-left:1px solid #99bbe8;border-right:1px solid #99bbe8;">
							&nbsp;
						</td>
						<td valign="top">
							<div id="checkGradeToolbar" class="opt-btn" style="line-height:33px;">
								<security:authorize ifAnyGranted="supervision-grade-save">
								<button class="btn" onclick="addRowByKey();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
								</security:authorize>
								<security:authorize ifAnyGranted="supervision-grade-delete">
								<button class="btn" onclick="delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除 </span></span></button> 
								</security:authorize>
							</div>
							<div class="opt-body" style="padding:6px;border:0px;">
								<table id="list1"></table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>