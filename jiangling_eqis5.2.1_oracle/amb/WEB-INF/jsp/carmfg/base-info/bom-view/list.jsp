<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
		function changePosition(){}
		$(document).ready(function(){ 
			$("#treegrid").jqGrid({
				prmNames:{
					rows:'page.pageSize',
					page:'page.pageNo',
					sort:'page.orderBy',
					order:'page.order'
				},
                treeGrid: true,  
                treeGridModel: 'adjacency', //treeGrid模式，跟json元数据有关 ,adjacency/nested  
                ExpandColClick:true,
                expandOnload : true,
                ExpandColumn : 'code',  
                url: '${mfgctx}/base-info/bom-view/list-datas.htm',  
                datatype: 'json',  
                colNames:['编码','名称','型号','事业部','认证产品','标准件','关键件','检验文件','是否有变更',"id","parent"],  
                colModel:[  
                    {name:'code',index:'code',width:180},
                    {name:'name',index:'name', width:220},  
                    {name:'model',index:'model', width:140},
                    {name:'is3C',index:'is3C', width:65},
                    {name:'isStandard',index:'isStandard', width:60},
                    {name:'iskeyComponent',index:'iskeyComponent', width:60},
                    {name:'checkFiles',index:'checkFiles', width:190,formatter:checkFilesFormatter},  
                    {name:'hasUpdate',index:'hasUpdate', width:80,formatter:function(value){
                    	if(value=='1'){
                    		return "<div style='color:red;width:100%;text-align:center;'>是</div>";
                    	}else{
                    		return "<div style='width:100%;text-align:center;'>否</div>";
                    	}
                    }},
                	{name:'id',index:'id', width:90,hidden:true},
                    {name:'parent',index:'parent',hidden:true}
                ],  
                multiselect:true,
                pager : '#ptreegrid',
                height: 'auto',
                //gridview: true,
                sortname: 'code',  
                sortorder: "asc",  
                jsonReader: {  
                    root: "rows",  
                    total: "total",  
                    repeatitems: false  
                },
                treeReader : {  
                    level_field: "level",  
                    parent_id_field: "parent", 
                    leaf_field: "isLeaf",  
                    expanded_field: "expanded"  
                },  
                mtype: "POST",  
                rowNum : 50,
                rowList:[50,70,100],
                height: 400,    // 设为具体数值则会根据实际记录数出现垂直滚动条  
                shrinkToFit:false  // 控制水平滚动条  
            });  
        });  
		function checkFilesFormatter(value,o,obj){
			if(!value){
				return "";
			}
			var strs = value.split(",");
			var html = "";
			var flag = 0;
			for(var i=0;i<strs.length;i++){
				var str = strs[i];
				if(str&&str.indexOf("_")>-1){
					if(html.length>0){
						html += ",";
					}
					var ss = str.split("_");
					var method = 'openIqcIndicator("'+ss[2]+'")';
					if(ss[1]=='mfg'){
						method = 'openMfgIndicator("'+ss[2]+'")';
					}
					if(flag>1){
						html += "</br>";
						flag = 0;
					}
					html += "<a href='javascript:void(0);"+method+"' title='"+ss[0]+"'>" + ss[0] + "</a>";
					flag++;
				}
			}
			return html;
		}
		//打开检验项目
		function openIqcIndicator(id){
			var url='${iqcctx}/inspection-base/indicator/edit-indicator.htm?indicatorId=' + id;
			$.colorbox({href:encodeURI(url),iframe:true,
				width:$(window).width()-100,
				height:$(window).height()-20,
				overlayClose:false,
				title:"查看检验项目"
			});
		}
		//
		function openMfgIndicator(id){
			var url='${mfgctx}/inspection-base/indicator/edit-indicator.htm?indicatorId=' + id;
			$.colorbox({href:encodeURI(url),iframe:true,
				width:$(window).width()-100,
				height:$(window).height()-20,
				overlayClose:false,
				title:"查看检验项目"
			});
		}
		function search(){
			var postData = $("#treegrid").jqGrid("getGridParam","postData");
			$("#form :input[name]").each(function(index,obj){
				if($(obj).val()){
					postData[obj.name] = $(obj).val();
				}else{
					delete postData[obj.name];
				}
			});
			$("#treegrid").jqGrid("setGridParam",{
				//postData : postData,
				page : 1
			}).trigger("reloadGrid");
		}
		
		function confirmChange(){
			var id = $("#treegrid").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要确认变更的产品!");
				return;
			}
			var strs = [];
			var obj = $("#treegrid").jqGrid("getRowData",id);
			strs.push('{"code":"'+id.split("_")[1]+'","parentCode":"'+(obj.parent?obj.parent.split("_")[1]:"")+'"}');
			$.post('${mfgctx}/base-info/bom-view/confirm-change.htm',{updateStrs:"[" + strs.join(",") + "]"},function(result){
				if(result.error){
					alert(result.message);
				}else{
					$("#treegrid").trigger("reloadGrid");
				}
			},'json');
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="bomView";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
				<div class="opt-btn">
				<form id="form">
				事业部:
				<s:select list="businessUnits" 
				  theme="simple"
				  listKey="businessUnitCode" 
				  listValue="businessUnitName"
				  name="params.businessUnitCode"
				  labelSeparator=""
				  emptyOption="true"></s:select>
				<span style="margin-left:4px;">产品代码:</span>
				<input name="params.code" value=""></input>
				<span style="margin-left:4px;">产品名称:</span>
				<input name="params.name" value=""></input>
				<button class='btn' onclick="search();" type="button" style="margin-left:4px;"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<button class='btn' type="reset" style="margin-left:4px;"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM-VIEW_CONFIRM-CHANGE">
				<button class='btn' onclick="confirmChange();" type="button" style="float:right;margin-right:4px;"><span><span><b class="btn-icons btn-icons-ok"></b>确认变更</span></span></button>
				</security:authorize>
				</form>
				</div>
				<div id="opt-content">
					<table id="treegrid"></table>
					<div id="ptreegrid"></div> 
				</div>
		</div>
	</div>
</body>
</html>