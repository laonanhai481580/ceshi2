<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>物料BOM</title>
		<%@include file="/common/meta.jsp" %>	
		<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/validation/cmxform.css"/>
	<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				$("#productBomForm").validate({
				});
			});
			function save(){
				if($("#productBomForm").valid()){
					var params = getParams();
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					$("#message").html("正在保存,请稍候... ...");
					$.post("${mfgctx}/base-info/bom/save.htm",params,function(result){
						if(result.error){
							$("#message").html("<font color=red>" + result.message + "</font>");
						}else{
							$("#message").html("保存成功!");
							$("#id").val(result.id);
						}
						showMsg();
						$(".opt_btn").find("button.btn").attr("disabled","false");
					},'json');
				}
			}
			//获取参数
			function getParams(){
				var params = {};
				$(":input","#productBomForm").each(function(index,obj){
					var jObj = $(obj);
					if(obj.name){
						params[obj.name] = jObj.val();
					}
				});
				return params;
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button" onclick="save()"><span><span>保存</span></span></button>
				<span style="padding-left:4px;padding-top:4px;" id="message"></span>
			</div>
			<div id="opt-content">
				<form  id="productBomForm" name="productBomForm" method="post" action="">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<input type="hidden" name="structureId" id="structureId" value="${structureId}"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id}"/>
					<table>
						<tr>
							<td align=right>上级BOM:</td>
							<td>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>代号:</td>
							<td><input id="code" name="code" value="${code }"  class="{required:true,messages:{required:'请输入编码!'}}"/></td>
						</tr>
						<tr>
							<td align=right>名称:</td>
							<td><input id="name" name="name" value="${name }" class="{required: true,messages:{required:'请输入名称'}}"/> </td>
						</tr>
						<tr>
							<td align=right>标准配件数:</td>
							<td>
							<s:if test="id==null">
								<input id="assemblyNum" name="assemblyNum" value="1" class="{digits:true, messages:{digits:'请输入数字'}}"/> 
							</s:if>
							<s:else>
								<input id="assemblyNum" name="assemblyNum" value="${assemblyNum }" class="{digits:true, messages:{digits:'请输入数字'}}"/> 
							</s:else>
							</td>
						</tr>
						<tr>
							<td align=right>物料类别:</td>
							<td>
								<s:if test="id==null||children.size==0">
									<s:select list="materialTypes" 
										name="materialType"
										listKey="name"
										listValue="name"
										theme="simple"
										value="materialType"
										cssStyle="width:150px;"></s:select>
								</s:if>
								<s:else>
									<input disabled="disabled" style="background:gray;"></input>
								</s:else>
							</td>
						</tr>
						<tr>
							<td align=right>追溯类型:</td>
							<td>
								<s:if test="id==null||children.size==0">
									<s:select list="ascendTypes" 
										name="ascendType"
										listKey="name"
										listValue="name"
										theme="simple"
										value="materialType"
										cssStyle="width:150px;"></s:select>
								</s:if>
								<s:else>
									<input disabled="disabled" style="background:gray;"></input>
								</s:else>
							</td>
						</tr>
						<tr>
							<td align=right>重要程度:</td>
							<td>
								<s:select list="importances" 
										name="importance"
										listKey="name"
										listValue="name"
										theme="simple"
										value="importance"
										cssStyle="width:150px;"></s:select>
							</td>
						</tr>
						<tr>
							<td align=right>备注:</td>
							<td><textarea rows="5" cols="18" id="remark" name="remark">${remark}</textarea> </td>
						</tr>
					</table>
				</form>
			</div>
			</div>
	</body>
</html>