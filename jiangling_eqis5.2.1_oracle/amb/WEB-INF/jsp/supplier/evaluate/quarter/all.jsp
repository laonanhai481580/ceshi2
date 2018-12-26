<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	function contentResize(){
		jQuery("#suppliers").jqGrid('setGridHeight',$(".ui-layout-center").height()-100);
		jQuery("#suppliers").jqGrid('setGridWidth',$(".ui-layout-center").width()-20);
	}
	var myLayout;
	$(document).ready(function(){
		myLayout = $('body').layout({
			north__paneSelector : '#header',
			north__size : 66,
			west__size : 250,
			north__spacing_open : 31,
			north__spacing_closed : 31,
			west__spacing_open : 6,
			west__spacing_closed : 6,
			center__minSize : 400,
			resizable : false,
			paneClass : 'ui-layout-pane',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
	});
</script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<script type="text/javascript">
		var params = {
			supplierId : '${supplierId}',
			evaluateYear : '${evaluateYear}'
		};
		$(document).ready(function(){
			
		});
		//改变路径
		function changeLocation(p){
			p = p || {};
			for(var pro in p){
				params[pro] = p[pro];
			}
			var str = '';
			for(var pro in params){
				if(!str){
					str = "?";
				}else{
					str += "&";
				}
				str += pro + "=" + params[pro];
			}
			$(document).mask();
			window.location = "${supplierctx}/evaluate/quarter/all.htm" + str;
		}
		//评价年改变
		function yearChange(obj){
			var evaluateYear = document.getElementsByName("evaluateYear")[0].value;
			var materialType = document.getElementsByName("materialType")[0].value;
			changeLocation({evaluateYear:evaluateYear,materialType:materialType});
		}
		//自定义评分显示
		function pointsFormat(cellvalue,options,rowObject){
			if(!cellvalue){
				return '';
			}else if(cellvalue == 'no'){
				return '';
			}else if(cellvalue.indexOf&&cellvalue.indexOf("未评价_")==0){
				var url = "${supplierctx}/evaluate/quarter/add.htm?supplierId=${supplierId}&evaluateYear=${evaluateYear}&evaluateMonth=" + cellvalue.split("_")[1] + "&estimateModelId=" + rowObject.estimateModelId;
				return '<div style="text-align:center;"><a href="'+url+'" title="添加评价" style="text-decoration:none;"><h3>--</h3></a></div>';
			}else{
				if(cellvalue.split&&cellvalue.indexOf("_")>0){
					var url = "${supplierctx}/evaluate/quarter/add.htm?id=" + cellvalue.split("_")[1];
					return '<div style="text-align:center;"><a href="'+url+'" title="修改评价" style="text-decoration:none;"><h3>'+cellvalue.split("_")[0]+'</h3></a></div>';
				}else{
					return '<div style="text-align:center;" title="合计"><h3>'+cellvalue+'</h3></div>';
				}
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="company-info";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@include file="left.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<table cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td valign="middle"
							style="padding: 0px; margin: 0px; padding-left: 8px;">
							评价年份:&nbsp;<s:select list="evaluateYears" listKey="value"
								theme="simple" value="evaluateYear" name="evaluateYear" onchange="yearChange()"
								listValue="name"></s:select>
								物料类别:&nbsp;<s:select list="materialTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="materialType"
								  id="materialType"
								  emptyOption="false"
								  labelSeparator=""
								  onchange="yearChange()"
								  cssStyle="width:150px;"
							  ></s:select></td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<div style="width: 100%; text-align: center; padding-bottom: 6px;">
					<h2>${supplierName}&nbsp;${evaluateYear}&nbsp;年度总评价</h2>
				</div>
				<form id="contentForm" name="contentForm" method="post" action=""
					onsubmit="return false;">
					<table id="suppliers"></table>
					<script type="text/javascript">
							$(document).ready(function(){
								var datas = ${datas};
								var colModel = [ 
							       {label:'',name:'estimateModelId',index:'estimateModelId',hidden:true},
					               {label:"Category Name",name:'estimateModelName',index:'estimateModelName',width:160}, 
					               {label:"Total Points",name:'allTotalPoints',index:'allTotalPoints',width:90},
					               <%
					               	for(int j=1;j<=12;j++){
					               %>
<%-- 					               {label:'<%=j%>月',name:'month<%=j%>',index:'month<%=j%>',width:50,formatter:pointsFormat}<%=(j==12?"":",")%>  --%>
					                   {label:'<%=j%>',name:'month<%=j%>',index:'month<%=j%>',width:50,formatter:pointsFormat},
					               <%
				               		if(j==3){%>
				               	       {label:'<%=(j/3)%>st Quarter',name:'JD<%=j%>',index:'JD<%=j%>',width:90,formatter:pointsFormat},
				               		<%}else if(j==6){%>
				               			{label:'<%=(j/3)%>nd Quarter',name:'JD<%=j%>',index:'JD<%=j%>',width:90,formatter:pointsFormat},
				               		<%}else if(j==9){%>
				               			{label:'<%=(j/3)%>rd Quarter',name:'JD<%=j%>',index:'JD<%=j%>',width:90,formatter:pointsFormat},
				               		<%}else if(j==12){%>
				               			{label:'<%=(j/3)%>th Quarter',name:'JD<%=j%>',index:'JD<%=j%>',width:90,formatter:pointsFormat}
					              	<%}%> 
					              <%}%> 
					            ];
								$("#suppliers").jqGrid({
									datatype: "local",
									multiselect:false,
									rownumbers:true,
									colModel:colModel,
						            data : datas,
						            gridComplete : function(){
						            	contentResize();
						            }
								});
					       	});
						</script>
				</form>
			</div>
		</div>
	</div>
</body>
</html>