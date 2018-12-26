<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
		}
		
		var topMenu ='';
		$(document).ready(function(){
		});
		
		//获取不良项目和 成本
		function addRowHtml(totalObj,classObj,obj){
	 		var tr = $(obj).closest("tr");
	 		var clonetr = tr.clone(false);
	 		tr.after(clonetr);
	 		var total = $("#"+totalObj);
			var num = total.val();
			clonetr.find(":input[fieldName]").val("");
	 	  	total.val(parseInt(num)+1);
	 	  	total.siblings("span").text(parseInt(num)+1);
	  		$("."+classObj).each(function(index, obj){
	  			$($(obj).children("td")[1]).text(parseInt(index)+1);
	  		});
	 	}
		
	 	function removeRowHtml(totalObj,classObj,obj){
	 		var total = $("#"+totalObj);
			var tr=$(obj).closest("tr");
			var pre=tr.prev("tr").attr("class");
			var next=tr.next("tr").attr("class");
			if(next==classObj){
			 	tr.remove();
		 	  	total.siblings("span").text(parseInt(total.val())-1);
			 	total.val(parseInt(total.val())-1);
			}else if(pre==classObj){
				tr.remove();
		 	  	total.siblings("span").text(parseInt(total.val())-1);
				total.val(parseInt(total.val())-1);
			}else{
				alert('至少要保留一行');
				total.val(1);
		 	  	total.siblings("span").text(1);
			}
	  		$("."+classObj).each(function(index, obj){
	  			$($(obj).children("td")[1]).text(parseInt(index)+1);
	  		});
	 	}
	 	
		function getDetailItems(){
			var infovalue="";
			var hasError = false;
			$(".detailItemTr").each(function(index,obj){
				var code = $(obj).find(":input[name=code]").val();
				if(!code){
					$.showMessage("第"+(index+1)+"行简码为空!");
					hasError=true;
					return false;
				}
				var name = $(obj).find(":input[name=name]").val();
				if(!name){
					$.showMessage("第"+(index+1)+"行特性名称为空!");
					hasError=true;
					return false;
				}
				if(infovalue){
					infovalue += ",";
				}
				infovalue += "{\"code\":\""+code+"\",\"name\":\""+code+"\"}";
			});
			if(infovalue&&!hasError){
				return "[" + infovalue + "]";
			}else{
				return "";
			}
		}
		
		function save(){
			var itemValues = getDetailItems();
			if(!itemValues){
				return;
			}
			var params = {
				itemValues : itemValues,
				id : $(":input[name=featureId]").val()
			};
			$("#opt-content").attr("disabled","disabled");
			$("button").attr("disabled","disabled");
			$("a.small-button-bg").attr("disabled","disabled");
			$.showMessage("执行中,请稍候... ...","custom");
			$.post("${spcctx}/base-info/process-define/copy-quality-feature.htm",params,function(result){
				$("#opt-content").removeAttr("disabled");
				$("button").removeAttr("disabled");
				$("a.small-button-bg").removeAttr("disabled");
				$.showMessage(result.message,5000);
				if(!result.error){
					window.parent._isFresh=true;
				}
			},'json');
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button  class='btn' type="button" onclick="save();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button  class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color:red;"></span>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="inspectionForm" name="inspectionForm">
					<input type="hidden" name="featureId" value="<%=request.getParameter("featureId")%>"/>
					<table class="form-table-border" style="table-layout:fixed;">
						<tr style="background:#99CCCC;font-weight:bold;">
					      	<td style="width:6%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
					      	<td style="width:5%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
					      	<td style="width:16%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>简码</td>
					      	<td style="width:20%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>特性名称</td>
				        </tr>
				        <tr class="detailItemTr">
					    	<td style="border-bottom:0px;border-left:0px;">
					    		<div style="margin:0 auto;width: 42px;">
					      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('detailItemTotal','detailItemTr',this)" title="添加质量特性"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('detailItemTotal','detailItemTr',this)" title="删除质量特性"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
								</div>
					 		</td>
					 		 <td style="border-bottom:0px;text-align:center;">1</td>
					      	<td style="border-bottom:0px;text-align:center;">
					      		<input style="width:98%;" fieldName="code" name="code" type="text"/>
					      	</td>
					      	<td style="border-bottom:0px;text-align:center;">
					      		<input style="width:98%;" fieldName="name"  name="name" type="text"/>
					      	</td>
	                    </tr>
			        </table>
			        <input type="hidden" name="detailItemTotal" id="detailItemTotal" value="1"/>
				</form>
			</div>
		</div>
	</div>
</body>
</html>