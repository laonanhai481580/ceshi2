<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
		<%@include file="/common/meta.jsp" %>
		<script type="text/javascript">
		var isUsingComonLayout=false;	
			$(function (){
			    $("#my-function-tree").jstree({ 
			        "plugins" : [ "themes", "html_data", "checkbox", "sort", "ui" ]
			    });
			});
			
			function save(){
				var ids = [];
				//已选择
				$("#my-function-tree").find(".jstree-checked").each(function(index,obj){
					ids.push(obj.id);
				});
				//半选择
				$("#my-function-tree").find(".jstree-undetermined").each(function(index,obj){
					ids.push(obj.id);
				});
				$("body").attr("disabled",true);
				$("#message").html("数据保存中,请稍候... ...");
				var url = "${ctx}/mfg/base-info/my-function/save-my-function.htm";
				var params = {
					deleteIds:ids.join(","),	
					userId : <%=request.getParameter("userId") %>
				};
				$.post(url,params,function(result){
					$("body").attr("disabled","");
					if(result.error){
						$("#message").html("<font color=red>" + result.message + "</font>");
						alert(result.message);
					}else{
						window.saved = true;
						$("#message").html(result.message);
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		</script>
	</head>
	<body>
		<div class="opt-body" style="height:100%;">
			<div class="opt-btn">
				<button class='btn' type="button" onclick="save()"><span><span>保存</span></span></button>
				<span style="padding-left:4px;padding-top:4px;" id="message"></span>
			</div>
			<div id="my-function-tree" style="padding:4px;">
				${treeUl}
			</div>
		</div>
	</body>
</html>