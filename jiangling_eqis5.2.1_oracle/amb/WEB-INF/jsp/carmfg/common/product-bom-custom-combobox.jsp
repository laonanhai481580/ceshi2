<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	String elementId = request.getParameter("elementId");
%>
<table id="<%=elementId%>_table" cellspacing=0 cellpadding=0 style="height:100%;width:100%;margin:0px;padding:0px;clear:both;">
	<tr>
		<td colspan="3" style="padding:0px;" valign="middle">
			<div style="height:33px;line-height:33px;">
				<div class="opt-btn" style="height:30px;line-height:30px;">
					物料名称/代号:<input></input>
					<button class="sexybutton" onclick="$('#<%=elementId%>').CustomCombobox('search','<%=elementId%>_BomList',$(this).parent().find('input').val())"><span class="search">查询 </span></button>	
					<button class="sexybutton" onclick="$(this).parent().find('input').val('').focus();"><span class="undo">重置</span></button>
				</div>
			</div>
		</td>
	</tr>
	<tr>
		<td class="left" style="width:140px;padding:0px;" valign="top">
			<div style="padding:4px;margin:0px;overflow:auto;" id="<%=elementId %>-product-structure-tree">
			</div>
		</td>
		<td class="split" style="width:6px;background:#AED5F9;padding:0px;" valign="middle">
			<div title="Close">&nbsp;</div>
		</td>
		<td class="right" style="margin:0px;padding:5px;" valign="top">
			<div id="<%=elementId%>_page"></div>
			<table id="<%=elementId%>_BomList"></table>
		</td>
	</tr>
</table>
<script type="text/javascript">
	var tableId = "<%=elementId%>_table";
	$("#tableId").ready(function(){
		var parentWidth = $("#"+tableId).parent().width();
		var parentHeight = $("#"+tableId).parent().height();
		parentHeight -= 33;
		var treeId = '<%=elementId %>-product-structure-tree';
		$("#" + treeId)
			.width(140)
			.height(parentHeight-10)
			.jstree({
				json_data : {
					data : [
						{ 
							"data" : "产品结构", 
							"state" : "closed",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "<%=request.getContextPath()%>/mfg/base-info/bom/list-structure.htm",
						data : function(n){
							var id = n.attr("id");
							return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
						}
					}
				},
				plugins : ["themes","json_data","ui","crrm","contextmenu"],
				core : { "initially_open" : ["root"] },
				"ui" : {
//	 				"initially_select" : [ "root"]
				}
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				id = id == 'root'?'':id;
				$('#<%=elementId%>').CustomCombobox('searchByParent','<%=elementId%>_BomList',id);
			});
		var colModel = [
	   		{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
	   		{label:'modelSpecification',name:'modelSpecification',index:'modelSpecification', width:1,hidden:true},
	   		{label:'选择',name:'s',index:'s', width:30,formatter:function(value,o,obj){
	   			return "<a class=\"small-button-bg\" href=\"#\" onclick=\"$('#<%=elementId%>').CustomCombobox('select','<%=elementId%>_BomList',"+obj.id+");\" style='float:left;margin-left:5px;' title='选择【"+obj.name+"】'><span class=\"ui-icon ui-icon-circle-check\"></span></a>";
	   		}},
	   		{label:'名称',name:'name',index:'name', width:130,formatter:function(value,o,obj){
	   			if(obj.hasChild==true||obj.hasChild=='true'){
					return "<a class=\"small-button-bg\" onclick=\"window.loadStructure='yes';$('#<%=elementId%>').CustomCombobox('searchByParent','<%=elementId%>_BomList',"+obj.id+");\" href=\"javascript:void(0);\" style='float:left;margin-right:2px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + value;
				}else if(value){
					return value;
				}else{
					return '';
				}
	   		}},
	   		{label:'代号',name:'code',index:'code', width:120},
	   		{label:'物料类别',name:'materialType',index:'materialType', width:70,hidden:true},	
	   		{label:'追溯类型',name:'ascendType',index:'ascendType', width:70,hidden:true},			
	   		{label:'重要程度',name:'importance',index:'importance', width:65,hidden:true},		
	   		{label:'hasChild',name:'hasChild',index:'hasChild', width:65,hidden:true},		
	   		{label:'备注',name:'remark',index:'remark', width:180,hidden:true}
// 	   		{label:'操作',name:'aa',index:'aa', width:40,formatter:function(value,o,obj){
// 	   			if(obj.hasChild==true||obj.hasChild=='true'){
<%-- 					return "<a class=\"small-button-bg\" onclick=\"window.loadStructure='yes';$('#<%=elementId%>').CustomCombobox('searchByParent','<%=elementId%>_BomList',"+obj.id+");\" href=\"#\" style='float:left;margin-left:10px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>"; --%>
// 				}else{
// 					return '';
// 				}
// 	   		}}		
	   	];
		$("#<%=elementId%>_BomList").jqGrid({
			rownumbers:true,
			gridComplete:function(){},
			loadComplete:function(){},
			postData : {},
			multiselect:false,
			rowNum:15,
			datatype: "json",
			url:'<%=request.getContextPath()%>/mfg/base-info/bom/list-datas.htm',
		   	colModel : colModel,
		   	onSelectRow : function(rowid){
		   		if(window.loadStructure != 'yes'){
		   			$("#<%=elementId%>").CustomCombobox("select","<%=elementId%>_BomList",rowid);
		   		}else{
		   			delete window.loadStructure;
		   		}
		   	},
		   	pager:'#<%=elementId%>_page'
		});
		$("#<%=elementId%>").CustomCombobox("layout","<%=elementId%>_BomList");
	});
</script>