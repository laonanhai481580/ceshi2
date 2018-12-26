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
		$("#detail input[type=checkbox]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.checked){
				if(!params[obj.name]){
					params[obj.name] = jObj.val();
				}else{
					params[obj.name] = params[obj.name] + "," + jObj.val();
				}
			}else{
				if(!params[obj.name]){
					params[obj.name] = '';
				}
			}
		});
		return params;
	}
		function saveProductLine(){
			var params=getParams();
			$.post('${mfgctx}/efficient-management/save-productline.htm',params,function(){
				window.parent.$.colorbox.close();
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
	<div  class="opt-btn" style="line-height:33px;">
		<button class='btn' type="button" onclick="saveProductLine()"><span><span>保存</span></span></button>
		<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span>关闭</span></span></button>
		</div>
	<div id="detail">
	<input type="hidden" name="structureId" value="${structureId}" />
		<table>
		<tr height=29>
			<td>请选择生产线:</td>
			</tr>
			<tr>
			<td>
				<s:iterator value="productline" id="option">
					<input name="productline" type="checkbox" value="${option.name}" <s:if test="%{#option.checked==1}">checked="checked"</s:if>></input>${option.name}&nbsp;&nbsp;
				</s:iterator>
			</td>
		</tr>
		</table>
		</div>
	</div>
</body>
</html>