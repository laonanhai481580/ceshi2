<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	//获取表单的值
	function getParams(){
		var params = {};
		$("#detail input[type=hidden]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#detail textarea").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
		function saveDetail(){
			var params=getParams();
			$.post('${mfgctx}/data-acquisition/save-detail.htm',params,function(){
				window.parent.$.colorbox.close();
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
	<div id="detail">
	<input type="hidden" name="id" value="${defectiveItem.id}" />
		<table>	
		<tr>
		<td>返修意见</td>
		</tr>
		<tr>
		<td><textarea rows="10" cols="10" style="width:100%" name="reDetail" id="reDetail">${defectiveItem.reDetail}</textarea></td>
		</tr>
		<tr>
		<td>终检意见</td>
		</tr>
		<tr>
		<td style="width:100%"><textarea rows="10" cols="10" style="width:100%" name="finalDetail" id="finalDetail">${defectiveItem.finalDetail}</textarea></td>
		</tr>
		<tr>
		<td align="right" ><button  class='btn' onclick="saveDetail();"><span><span>保存</span></span></button></td>
		<td align="left"><button  class='btn' onclick="reset();"><span><span>取消</span></span></button></td>
		</tr>
		</table>
		</div>
	</div>
</body>
</html>