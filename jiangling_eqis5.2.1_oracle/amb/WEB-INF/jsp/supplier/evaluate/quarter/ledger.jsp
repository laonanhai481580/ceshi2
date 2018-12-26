<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
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
	function deleteEvaluates(){
		var ids = $("#suppliers").jqGrid("getGridParam","selarrrow");
		if(ids.length<1){
			alert("请选择评价!");
		}else{
			if(confirm("确定要删除吗?")){
				$("#message").html("正在删除,请稍候...");
				$.post("${supplierctx}/evaluate/quarter/delete.htm?deleteIds=" + ids.join(","),{},function(result){
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						$("#suppliers").trigger("reloadGrid");
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
	}
	//改变路径
	var params = {
		supplierId : '${supplierId}',
		evaluateYear : '${evaluateYear}'
	};
	
	function $getExtraParams(){	
		return params;
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${supplierctx}/evaluate/quarter/add.htm?supplierId="+rowObject.supplierId+"&evaluateYear="+rowObject.evaluateYear+"&evaluateMonth="+rowObject.evaluateMonth+"&materialType="+rowObject.materialType+"&estimateModelId="+rowObject.estimateModelId+"'>"+cellvalue+"</a>";
	}
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
		window.location = "${supplierctx}/evaluate/quarter/ledger.htm" + str;
	}
	//评价年改变
	function yearChange(obj){
		changeLocation({evaluateYear:obj.value});
	}
	
	function operateFormater(cellValue,options,rowObj){
		var operations = "<div style='text-align:center;' title=\"请选择评价!\"><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		return operations;
	}
	
	function editInfo(id){
		$(document).mask();
		window.location = '${supplierctx}/evaluate/quarter/add.htm?id=' + id;
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="evaluate-ledger";
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
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="middle" style="padding: 0px; margin: 0px;">
							<security:authorize ifAnyGranted="supplier_evaluate_ledger_delete">
							<button class="btn" onclick="deleteEvaluates();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
							</security:authorize>
							<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							<security:authorize ifAnyGranted="supplier_evaluate_ledger_exports">
							<button class="btn" onclick="iMatrix.export_Data('${supplierctx}/evaluate/quarter/exports.htm?supplierId=${supplierId}');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
							<span id="message" style="line-height:30px;margin-left:4px;color:red;">
							</span>
						</td>
						<td align="right" style="padding-right: 6px;" valign="middle">
							<!-- 评价年份:&nbsp;<s:select list="evaluateYears" listKey="value"
								theme="simple" value="evaluateYear"
								onchange="yearChange(this)" listValue="name"></s:select> -->
							</td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/evaluate/quarter/ledger-datas.htm" code="SUPPLIER_EVALUATE"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>