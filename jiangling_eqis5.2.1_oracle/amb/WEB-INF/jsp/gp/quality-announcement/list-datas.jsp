<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title></title>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
//查看公告
function editInfo1(id){
	window.parent.window.editInfo(id);
}
function moveTitle(obj){
	$(obj).attr("style","color: red");
}
function outTitle(obj){
	$(obj).attr("style","");
}
</script>
</head>
<body>
	<div class="ui-layout-center" id="opt-content" style="border:0px;width:100%;">
		<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
		<table  class="form-table-border-left" style="border:0px;width:100%" id="">
		<tbody>
<!-- 			<tr> -->
<!-- 				<td colspan="3" style="border-top:0px;border-right:0px;border-left:0px;border-bottom: 0px ;padpadding-bottom:20px;text-align:right;" > -->
<!-- 					<input type="text"  style="width:150px" name="" id="" placeholder="搜索"/> -->
<!-- 					<a class="small-button-bg" style="margin-left:2px;margin-top: 0px;float: right;" onclick="searchClick()" href="javascript:void(0);" title="搜索"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a> -->
<!-- 				</td> -->
<!-- 			</tr> -->
			<s:iterator value="list" var="list" id="list" status="st">
				<tr name="list">
					<td width="15%" style="text-align:center;border-top:0px;border-left:0px;border-right:0px;">【${territorial}】</td>
					<td width="60%" style="border-top:0px;border-left:0px;border-right:0px;">
						<a href="#" onclick="editInfo1(${id});" onmousemove="moveTitle(this)" onmouseout="outTitle(this)">${title}</a>
					</td>
			       	<td width="25%" style="border-top:0px;border-right:0px;border-left:0px;">
			       		<s:date name='releaseTime' format='yyyy-MM-dd HH:mm:ss'/>
			       	</td>
				</tr>
			</s:iterator>
<!-- 			<tr> -->
<!-- 				<td colspan="3" style="text-align: center;border:0px;padding-top: 15px"> -->
<!-- 					<button type="button">上一页</button>&nbsp;&nbsp; -->
<!-- 					<a>1</a> -->
<!-- 					<a>2</a> -->
<!-- 					<a>3</a> -->
<!-- 					<a>4</a> -->
<!-- 					<a>5</a> -->
<!-- 					&nbsp;&nbsp;<button type="button">下一页</button> -->
<!-- 				</td> -->
<!-- 			</tr> -->
		</tbody>
		</table>
	</div>
</body>

</html>