<%@page import="com.ambition.chartdesign.entity.ChartDatasource"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
	}
	function submitForm(url){
		if($("#form").valid()){
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	/**
	* 测试数据库连接
	*/
	function testConnection(){
		if($("#form").valid()){
			var params = {};
			$("#form :input[name]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$.post('${chartdesignctx}/base-info/database-setting/test-connection.htm',params,function(result){
				$("button").removeAttr("disabled");
				$("#message").html("");
				if(result.error){
					alert(result.message);
				}else{
					alert("恭喜你,连接成功!");
				}
			},'json');
		}
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/input.htm?isBack=true')">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/step3.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					2.指定表或查询
				</div>
				<form id="form" name="form"
					method="post" action="">
					<%
						String targetTableType = ActionContext.getContext().getValueStack().findString("targetTableType");
					%>
					<table style="width: 100%;">
						<tr height=60>
							<td style="padding-left:40px;">
								<input type="radio" name="targetTableType" id="SELECT" 
									value="<%=ChartDatasource.TARGET_TABLE_TYPE_SELECT%>"
									<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(targetTableType)?"":"checked=checked"%>/>
								<label for="SELECT">从所选数据库中选择数据表</label>
							</td>
						</tr>
						<tr>
							<td style="padding-left:40px;">
								<input type="radio" name="targetTableType" id="custom" 
									value="<%=ChartDatasource.TARGET_TABLE_TYPE_CUSTOM%>"
									<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(targetTableType)?"checked=checked":""%>/>
								<label for="custom">编写查询语句以自定义显示列</label>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>