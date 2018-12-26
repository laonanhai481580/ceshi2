<%@page import="com.ambition.chartdesign.entity.ChartSearch"%>
<%@page import="com.ambition.chartdesign.baseinfo.datasource.service.ChartDatasourceManager"%>
<%@page import="org.hibernate.Hibernate"%>
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
		//初始化数据字段
		initDataColumnSet();
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
		var $field = $("#" + rowId + "_fieldType");
		$field.change(function(){
			fieldChange($(this));
		});
		fieldChange($field,true);
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
			}],
		'date':[{
				key:'gt',
				value:'大于'
			},{
				key:'ge',
				value:'大于等于'
			},{
				key:'le',
				value:'小于等于'
			},{
				key:'lt',
				value:'小于'
			},{
				key:'between',
				value:'范围内'
			}]
	};
	function fieldChange($obj,isInit){
		var fieldType = $obj.val();
		if(!fieldType){
			return;
		}
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
		}else if(fieldType=='<%=Hibernate.DATE.getName()%>'
			||fieldType=='<%=Hibernate.TIMESTAMP.getName()%>'){
				searchFlag = 'date';
			}
		var typeObj = $obj.closest("tr").find(":input[name=searchType]");
		typeObj.find("option").remove();
		if(searchFlag){
			typeObj.show();
			var vals = searchTypeMap[searchFlag];
			for(var i=0;i<vals.length;i++){
				typeObj.append("<option role='option' value='"+vals[i].key+"'>"+vals[i].value+"</option>");
			}
		}else{
			typeObj.hide();
		}
	}
	var dataTypeMap = <%=ChartDatasourceManager.getHibernateDataTypeFormatter()%>;
	var defualtValues = ":;<%=ChartSearch.DEFAULTVALUE_WEEK%>:本周;<%=ChartSearch.DEFAULTVALUE_MONTH%>:本月;<%=ChartSearch.DEFAULTVALUE_YEAR%>:本年";
	function initDataColumnSet(){
		var dataTypeStr = "";
		for(var pro in dataTypeMap){
			if(dataTypeStr){
				dataTypeStr += ";";
			}
			dataTypeStr += pro + ":" + dataTypeMap[pro];
		}
		$("#table").jqGrid({
			datatype:'local',
			data : ${searchSetDatas},
			rownumbers : true,
			rowNum : 1000,
			colModel: [
				{name:'id',index:'id',label:'ID',hidden:true},
				{name:'showLabel',index:'showLabel',width:120,label:'标签名称',editable:true,editrules:{required:true}},
				{name:'fieldType',index:'fieldType',width:100,label:'字段类型',editable:true,edittype:'select',editrules:{required:true},editoptions:{value:dataTypeStr},formatter:'select'},
				${customModelStr}
				{name:'inputAmount',index:'inputAmount',width:100,label:'输入框数量',editable:true,edittype:'select',editrules:{required:true},editoptions:{value:'1:1;2:2'},formatter:'select'},
				{name:'defaultValue',index:'defaultValue',width:100,label:'默认值',editable:true,edittype:'select',editoptions:{value:defualtValues},formatter:'select'},
				{name:'valueSetType',index:'valueSetType',width:100,label:'格式类型',hidden:true},
				{name:'valueSetCode',index:'valueSetCode',width:100,label:'格式编码',hidden:true},
				{name:'valueSetName',index:'valueSetName',width:100,label:'编码名称',hidden:true},
				{name:'isMulti',index:'isMulti',width:100,label:'是否多选',hidden:true},
				{name:'formatter',index:'formatter',width:190,label:'格式化',formatter:function(val,options,rowObj){
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
				}},
				{name:'requestParam',index:'requestParam',width:120,label:'请求参数名称',editable:true}
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
				$("#table").tableDnDUpdate({});
			}
		});
		$("#table").tableDnD({});
		
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
	}
	function setValueSetValue(rowId){
		var rowData = $("#table").jqGrid("getRowData",rowId);
		var valueSetType = rowData.valueSetType;
		var valueSetCode = rowData.valueSetCode;
		var isMulti = rowData.isMulti;
		var url = "${chartdesignctx}/custom-search/set-valueset-param.htm?valueSetType=" + (valueSetType?valueSetType:"")
			 + "&valueSetCode=" + (valueSetCode?valueSetCode:"")
			  + "&isMulti=" + (isMulti?isMulti:"")
			  +"&editId=" + rowId;
		$.colorbox({
			href:url,
			iframe:true, 
			width:400, 
			height:200,
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
			formatter:''
		};
		$("#table").jqGrid("setRowData",rowId,obj);
		$("#" + rowId + " a[setBtn]").show();
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
	
	function submitForm(url){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		var columnData = $("#table").jqGrid("getRowData");
		var columnDataJsonStr = JSON.stringify(columnData);
		$("#searchParamJsonStr").val(columnDataJsonStr);
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
					onclick="javascript:window.location.href='${chartdesignctx}/custom-search/step4.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/custom-search/step6.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					5/6.查询条件
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="searchParamJsonStr" id="searchParamJsonStr"/>
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