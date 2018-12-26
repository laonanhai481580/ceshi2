<%@page import="com.ambition.chartdesign.entity.ChartSearch"%>
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
		var searchSets = [];
		for(var i=0;i<ids.length;i++){
			var str = "";
			var rowData = $("#table").jqGrid("getRowData",ids[i]);
			for(var j=0;j<colModels.length;j++){
				var fieldName = colModels[j].name;
				if(fieldName=='displayOrder'||fieldName=='id'||fieldName=='format_'){
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
			searchSets.push("{" + str + "}");
		}
		var searchSetStrs = "[" + searchSets.join(",") + "]";
		$(":input[name=searchSetStrs]").val(searchSetStrs);
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
			oneditRow(rowId);
			$("#" + rowId + " a[setBtn]").show();
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
			if(rowId<0){
				/**$("#table").jqGrid("setRowData",rowId,{id:-rowId,displayOrder:''});
				$("#" + rowId).attr("id",-rowId);*/
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
	//编辑时的事件
	function oneditRow(rowId){
		$("#" + rowId + " select[name]").css("width","98%");
		var bindChangeFields = '${bindChangeFields}'.split(",");
		for(var i=0;i<bindChangeFields.length;i++){
			var bindChangeField = bindChangeFields[i];
			if(bindChangeField){
				var $field = $("#" + rowId + "_" + bindChangeField);
				$field.change(function(){
					fieldChange($(this));
				});
				fieldChange($field,true);
			}
		}
	}
	//equals:等于;include:包含;leftequals:左包含;rightequals:右包含;notinclude:不包含;gt:大于;ge:大于等于;lt:小于;le:小于等于
	var searchTypeMap = {
		'number':[{
				key:'gt',
				value:'大于'
			},{
				key:'ge',
				value:'大于等于'
			},{
				key:'equals',
				value:'等于'
			},{
				key:'le',
				value:'小于等于'
			},{
				key:'lt',
				value:'小于'
			},{
				key:'between',
				value:'范围内'
			}],
		'string':[{
				key:'include',
				value:'包含'
			},{
				key:'leftequals',
				value:'左包含'
			},{
				key:'rightequals',
				value:'右包含'
			},{
				key:'notinclude',
				value:'不包含'
			},{
				key:'equals',
				value:'等于'
			},{
				key:'in',
				value:'多包含'
			}]
	};
	var fieldTypeMap = ${fieldTypeMap};
	function fieldChange($obj,isInit){
		var value = $obj.val();
		if(!value){
			return;
		}
		if(!isInit){
			$obj.closest("tr")
			.find(":input[name=label]")
			.val($obj.find("option:selected").html());
		}
		var datasourceId = $obj.attr("name").split("_")[1];
		var typeMap = fieldTypeMap[datasourceId];
		if(typeMap){
			var fieldType = typeMap[value];
			var searchFlag = '';
			if(fieldType=='<%=Hibernate.STRING.getName()%>'){
				searchFlag = 'string';
			}else if(fieldType=='<%=Hibernate.SHORT.getName()%>'
				||fieldType=='<%=Hibernate.INTEGER.getName()%>'
				||fieldType=='<%=Hibernate.FLOAT.getName()%>'
				||fieldType=='<%=Hibernate.DOUBLE.getName()%>'
				||fieldType=='<%=Hibernate.BYTE.getName()%>'
				||fieldType=='<%=Hibernate.LONG.getName()%>'){
				searchFlag = 'number';
			}
			var typeObj = $obj.closest("tr").find(":input[name=searchType]");
			var hisType = typeObj.val();
			typeObj.find("option").remove();
			if(searchFlag){
				typeObj.show();
				var vals = searchTypeMap[searchFlag];
				for(var i=0;i<vals.length;i++){
					typeObj.append("<option role='option' value='"+vals[i].key+"'>"+vals[i].value+"</option>");
				}
				typeObj.val(hisType);
			}else{
				typeObj.hide();
			}
		}
	}
	//初始化所有列
	var colModels = ${colModels};
	var fieldAliasMap = ${fieldAliasMap};
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
		colModels.push({
			name:'id',
			hidden:true
		});
		for(var i=0;i<colModels.length;i++){
			if(colModels[i].name&&colModels[i].name.indexOf("format_")==0){
				colModels[i].formatter = function(val,options,rowObj){
					var html = "<label>";
					var valueSetType = rowObj.valueSetType;
					if(valueSetType=='<%=ChartSearch.VALUESETTYPE_OPTIONGROUP%>'){
						html += "选项组["+rowObj.valueSetCode+"]";
					}else if(valueSetType=='<%=ChartSearch.VALUESETTYPE_PLUGIN%>'){
						html += "插件["+rowObj.valueSetName+"]";
					}else{
						html += "";
					}
					html += "</label>";
					html += "<a class='small-button-bg' style='margin-right:4px;float:right;display:none;' setBtn=true onclick='setValueSetValue("+rowObj.id+")' href='javascript:void(0);'  title='输入框格式化'><span class='ui-icon ui-icon-settings' style='cursor:pointer;'></span></a>";
					return html;
				};
			}
		};
		$("#table").jqGrid({
			datatype:'local',
			rownumbers : true,
			width : $("#tableBtn").width(),
			data : ${searchSetDatas},
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
		var height = $("#opt-content").height() - $("#table").position().top-110;
		if(groupHeaders.length>0){
			$("#table").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders: groupHeaders
			});
			height -= 20;
		}
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
		
		$("#table").tableDnD({});
	};
	function setValueSetValue(rowId){
		var rowData = $("#table").jqGrid("getRowData",rowId);
		var valueSetType = rowData.valueSetType;
		var valueSetCode = rowData.valueSetCode;
		var isMulti = rowData.isMulti;
		var url = "${chartdesignctx}/chart-definition/set-valueset-param.htm?valueSetType=" + (valueSetType?valueSetType:"")
			 + "&valueSetCode=" + (valueSetCode?valueSetCode:"")
			  + "&isMulti=" + (isMulti?isMulti:"")
			  +"&editId=" + rowId;
		$.colorbox({
			href:url,
			iframe:true, 
			width:450, 
			height:240,
			overlayClose:false,
			title:"查询框格式化"
		});
	}
	function afterSetValueSetParam(rowId,valueSetType,valueSetCode,valueSetName,isMulti){
		var obj = {
			id:rowId,
			valueSetType:valueSetType,
			valueSetCode:valueSetCode,
			valueSetName:valueSetName,
			isMulti:isMulti,
			format_:''
		};
		$("#table").jqGrid("setRowData",rowId,obj);
		$("#" + rowId + " a[setBtn]").show();
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
					onclick="javascript:window.location.href='${chartdesignctx}/chart-definition/step4.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/step6.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					5/6.设置查询条件
					<label style="font-size:10px;margin-left:10px;font-weight:normal;">选择行后可按住鼠标调整顺序</label>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="searchSetStrs"/>
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