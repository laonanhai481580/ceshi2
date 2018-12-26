<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
		var isUsingComonLayout=false;
			$(function(){
				$("#functionAddress").validate({
				});
			});
			function save(addNew){
				if($("#functionAddress").valid()){
					var params = getParams();
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					$("#message").html("正在保存,请稍候... ...");
					$.post("${mfgctx}/base-info/my-function/save.htm",params,function(result){
						if(result.error){
							$("#message").html("<font color=red>" + result.message + "</font>");
						}else{
							if(addNew){
								var names = ',id,name,';
								$("#functionAddress :input").each(function(index,obj){
									var $obj = $(obj);
									if(names.indexOf("," + obj.name + ",")>-1){
										$obj.val('');
									}else if(obj.name=='orderNum'){
										$obj.val(parseInt($obj.val())+1);
									}
								});
								$("#functionAddress :input[name=name]").focus();
							}else{
								$("#message").html("保存成功!");
								$("#id").val(result.id);
							}
						}
						showMsg();
						$(".opt_btn").find("button.btn").attr("disabled","false");
					},'json');
				}
			}
			//获取参数
			function getParams(){
				var params = {};
				$(":input","#functionAddress").each(function(index,obj){
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
			<security:authorize ifAnyGranted="base-info-my-function-save">
			<button  class='btn' type="button" onclick="save()"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			<button  class='btn' type="button" onclick="save(true)"><span><span><b class="btn-icons btn-icons-save"></b>保存并新建</span></span></button>
			</security:authorize>	
				<span style="padding-left:4px;padding-top:4px;" id="message"></span>
			</div>
			<div>
				<form  id="functionAddress" name="functionAddress" method="post" action="">
					<input type="hidden" name="id" id="id" value="${id}"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id}"/>
					<table style="width:100%;">
						<tr>
							<td align=right width=90>上级功能:</td>
							<td>${parent.name}</td>
						</tr>
						<tr>
							<td align=right><font color=red>*</font>名称:</td>
							<td><input name="name" value="${name }" class="{required:true,messages:{required:'请输入名称'}}" style="width:95%;"/> </td>
						</tr>
						<tr>
							<td align=right>链接地址:</td>
							<td><input name="address" value="${address}" style="width:95%;"/></td>
						</tr>
						<tr>
							<td align=right><font color=red>*</font>排序:</td>
							<td><input name="orderNum" value="${orderNum}" class="{required:true,digits:true,messages:{required:'请输入序号'}}" style="width:40px;"/></td>
						</tr>
					</table>
				</form>
			</div>
			</div>
	</body>
</html>