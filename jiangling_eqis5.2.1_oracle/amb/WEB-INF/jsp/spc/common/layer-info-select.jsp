<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择层别信息</title>
	<%@include file="/common/meta.jsp" %>
	<%String myId = request.getParameter("layerId");%>
	<script type="text/javascript">
		function contentResize(){
			var searchDivHeight = 0;
			if($("#customerSearchDiv").css("display")=='block'){
				searchDivHeight = $("#customerSearchDiv").height();
			}
			$("#layer_table").jqGrid("setGridWidth",$("#btnDiv").width());
			$("#layer_table").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 70 - searchDivHeight);
		}
		
		$(document).ready(function(){
			contentResize();
		});
		
		function $addGridOption(jqGridOption){
			jqGridOption.postData.layerId=<%=myId%>;
		}
		
		function myFormatter(cellValue,options,rowObj){
			return "<a class=\"small-button-bg\" href=\"#\" onclick=\"realSelect("+rowObj.id+")\" style='float:center;margin-left:8px;' title='选择【"+rowObj.tetailName+"】'><span class=\"ui-icon ui-icon-circle-check\"></span></a>";
		}
		//确定
		function realSelect(id){
			var ids = [];
			if(id){
				ids.push(id);
			}else{
				var id = $("#layer_table").jqGrid("getGridParam","selrow");
				if(id){
					ids.push(id);
				}
			}
			if(ids.length == 0){
				alert("请选择层别信息!");
				return;
			}
			if($.isFunction(window.parent.setLayerValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#layer_table").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.detailCode,value:objs.detailName,id:objs.id});
					}
				}	
				if(data.length > 0){
					//选择层别信息后调用父窗口的setLayerValue（data）方法，将层别信息返回到打开窗口的父页面
					window.parent.setLayerValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setLayerValue()方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		//查询方法
		function search(){
			var paramsStr = "";
			$("#searchForm :input").each(function(index,obj){
				if(obj.name){
					var $obj = $(obj);
					if($obj.val()){
						if(paramsStr){
							paramsStr += ",";
						}
						paramsStr += "\"" + obj.name + "\":\"" + $obj.val() + "\""; 
					}
				}
			});
			var postData = {
				searchParams : paramsStr?("{" + paramsStr + "}"):""
			};
			scroolTop = 0;
			$("#layer_table").clearGridData();
			$("#layer_table").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#layer_table").jqGrid("setGridParam",{postData:{searchParams:''}});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="javascript:$('#customerSearchDiv').toggle();contentResize();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>显示查询</span></span></button>
			</div>
			<div id="customerSearchDiv" style="padding:6px 10px 0px 8px;display: none;'">
				<form id="searchForm" name="searchForm" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td style="padding-left:6px;padding-bottom:4px;">
					 			<span class="field-label">代码</span>
					 			<input type="text" name="defectionCodeNo" style="width:135px;"/>
					 			<span class="field-label">名称</span>
					 			<input type="text" name="defectionCodeName" style="width:135px;"/>
					 			<button class='btn' onclick="javascript:search();$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-find"></b>查询</span></span></button>
							 	<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="padding:6px;">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="layer_table" url="${spcctx}/common/layer-info-datas.htm" code="SPC_SELECT_LAYER_DETAIL" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>