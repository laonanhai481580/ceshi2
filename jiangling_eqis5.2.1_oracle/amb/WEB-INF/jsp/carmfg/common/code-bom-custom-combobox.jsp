<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	String elementId = request.getParameter("elementId");
%>
<table id="<%=elementId%>_table" style="height:100%;width:100%;margin:0px;padding:0px;clear:both;" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="3" style="padding:0px;background:red;" valign="middle">
			<div style="height:33px;line-height:33px;background:blue;">
				<div class="opt-btn" style="height:30px;line-height:30px;">
					不良名称/代码<input></input>
					<button class="sexybutton" onclick="$('#<%=elementId%>').CodeCombobox('search','<%=elementId%>_BomList',$(this).parent().find('input').val())"><span class="search">查询 </span></button>	
					<button class="sexybutton" onclick="$(this).parent().find('input').val('').focus();"><span class="undo">重置</span></button>
<%-- 					<button class="sexybutton" onclick="$('#<%=elementId%>').CodeCombobox('expand');"><span class="save">保存</span></button> --%>
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
							"data" : "不良类别", 
							"state": "closed",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "<%=request.getContextPath()%>/mfg/common/list-structure.htm",
						data : function(n){
							var id = n.attr("id");
							return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
						}
					}
				},
				plugins : ["themes","json_data","ui","crrm","contextmenu"],
				core : { "initially_open" : ["root"] },
				"ui" : {
	// 				"initially_select" : [ "root"]
				}
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				id = id == 'root'?'':id;
				$('#<%=elementId%>').CodeCombobox('searchByParent','<%=elementId%>_BomList',id);
			});
		var colModel = [
	   		{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
	   		{label:'选择',name:'s',index:'s', width:35,formatter:function(value,o,obj){
	   			return "<a class=\"small-button-bg\" href=\"#\" onclick=\"$('#<%=elementId%>').CodeCombobox('select','<%=elementId%>_BomList',"+obj.id+")\" style='float:center;margin-left:8px;' title='选择【"+obj.name+"】'><span class=\"ui-icon ui-icon-circle-check\"></span></a>";
	   		}},
            {label:'不良名称',name:'defectionCodeName', index:'defectionCodeName', width: 100,align:'left',editable:false},
            {label:'不良代码',name:'defectionCodeNo', index:'defectionCodeNo', width: 100,align:'left',editable:false},
            {label:'不良类别',name:'defectionTypeName', index:'defectionTypeName', width: 120,align:'left',editable:false}
	   	];
		$("#<%=elementId%>_BomList").jqGrid({
			rownumbers:true,
			gridComplete:function(){},
			loadComplete:function(){},
			postData : {},
			multiselect:false,
			rowNum:15,
			datatype: "json",
			url:'<%=request.getContextPath()%>/mfg/common/structure-list-datas.htm',
		   	colModel : colModel,
		   	onSelectRow : function(rowid){
		   		$("#<%=elementId%>").CodeCombobox("select","<%=elementId%>_BomList",rowid);
		   	},
		   	pager:'#<%=elementId%>_page'
		});
		$("#<%=elementId%>").CodeCombobox("layout","<%=elementId%>_BomList");
	});		
</script>		
