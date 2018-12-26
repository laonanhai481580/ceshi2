<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
			$.validator.addMethod("cycle_startMonth",function(value,element,params){
			   if(value&&$("#" + params.target).val()){
				   return true;
			   }else if(!value&&!$("#" + params.target).val()){
				   return true;
			   }else{
				   return false;
			   }
			},"周期和起始月份必须同时不能为空!");

			function estimateModelFormValidate(){
				$("#estimateModelForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('estimateModelForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				if($("#estimateModelForm").valid()){
					var params = {};
					$("#estimateModelForm :input").each(function(index,obj){
						if(obj.name){
							params[obj.name] = $(obj).val(); 
						}
					});
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					$("#message").html("正在保存,请稍候......");
					$.post(url,params,function(result){
						$(".opt_btn").find("button.btn").attr("disabled","");
						if(result.error){
							$("#message").html(result.message);
							alert(result.message);
						}else{
							$("#message").html("保存成功!");
							$("#id").val(result.id);
						}
						showMsg();
					},'json');
				}
			}
			function callback(){
				addFormValidate('${fieldPermission}','estimateModelForm');
				estimateModelFormValidate();
				showMsg();
			}
			$(function(){
				//添加验证
				estimateModelFormValidate();
			});
		</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button style="float:left;" class='btn'
					onclick="submitForm('${supplierctx}/estimate/model/quality/save.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存</span>
					</span>
				</button>
				<div style="float:left;line-height:30px;margin-left:4px;font-size:14px;" id="message"></div>
			</div>
			<div style="display: none;" id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<div id="opt-content">
				<form id="estimateModelForm" name="estimateModelForm" method="post"
					action="" onsubmit="return false">
					<input type="hidden" name="id" id="id" value="${id }" />
					<input type="hidden" name="structureId" id="structureId"
						value="${structureId }" />
					<input type="hidden" name="parentId" id="parentId"
						value="${parent.id}" />
					<table style="width: 90%;">
						<tr>
							<td align=right style="width:90px;">上级分类:</td>
							<td>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>名称:</td>
							<td><input id="name" name="name" value="${name}"
								class="{required: true,messages:{required:'必填'}}" /></td>
						</tr>
						<s:if test="%{id==null||children.size == 0}">
							<tr>
								<td align=right>周期:</td>
								<td><s:select id="cycle" list="modelCycles" listKey="name"
										listValue="name" name="cycle" value="cycle"
										emptyOption="true" theme="simple" cssStyle="width:90px;"
										cssClass="{cycle_startMonth:{target:'startMonth'}}"></s:select>
								</td>
							</tr>
							<tr>
								<td align=right>起始月份:</td>
								<td><s:select id="startMonth" list="startMonths"
										listKey="name" listValue="value" name="startMonth"
										value="startMonth" theme="simple" emptyOption="true"
										cssStyle="width:90px;"
										cssClass="{cycle_startMonth:{target:'cycle'}}"></s:select>
								</td>
							</tr>
						</s:if>
						<tr>
							<td align=right valign="top">备注:</td>
							<td><textarea rows="5" id="remark" name="remark"
									style="width: 100%;">${remark}</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>