<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>

	<script type="text/javascript">
	
		$(document).ready(function(){
			setTimeout(function(){
				$("#message").html("");
			},3000);
		});
		
		
	 	function submitForm(url){
	 		var defectiveInfo = getDefectiveInfo();
			$("#defectiveInfo").val(defectiveInfo);
	 		var params = getParams();
	 		$("#DefectiveGoodsForm").attr("action",url);
	 		$.showMessage("正在提交,请稍候... ...","custom");
	 		$(".opt-btn").find("button.btn").attr("disabled",true);
	 		$("#DefectiveGoodsForm").removeAttr("disabled");
	 		$("#DefectiveGoodsForm").submit();
			/* if($('#DefectiveGoodsForm').valid()){
				$("#btnDiv").find("button.btn").attr("disabled",true);
				$("#message").html("正在提交,请稍候... ...");
				$.post(url,params,function(result){
					$("#btnDiv").find("button.btn").attr("disabled",false);
					if(result.error&&result.error!=""){
						alert(result.message);
					}else{
						$("#message").html(result.message);
						document.getElementById("btnsub").remove();
						document.getElementById("btnsave").remove();
					}
					setTimeout(function() {
						$("#message").html('');
					}, 1000);
				},"json");
			}  */
		} 
	 	function getDefectiveInfo(){
	 		var checkItemStrs = "";
	 		$("#defectiveInfoTable tr[class=oqcDefectiveItemsTr]").each(function(index,obj){
	 			if(checkItemStrs){
	 				checkItemStrs += ",";
	 			}
	 			var str = '';
	 			$(obj).find(":input").each(function(index,obj){
	 				if(obj.name){
	 					if(str){
	 						str += ","; 
	 					}
	 					str += "\"" + obj.name + "\":\"" + $(obj).val() + "\"";
	 					$(obj).attr("name","");
	 				}
	 			});
	 			if(str != ""){
	 				checkItemStrs += "{" + str + "}";
	 			}
	 		});
	 		return "[" + checkItemStrs + "]";
	 	}
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","form").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type == 'radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}else if(!params[obj.name]){
							params[obj.name] = '';
						}
					}else if(obj.type == 'checkbox'){
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
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			return params;
		}
		
		//增加维修项目
		function addRowHtml(totalObj,classObj,obj){
	 		var tr = $(obj).closest("tr");
	 		var clonetr = tr.clone(false);
	//  		clonetr.children().last().remove();
	 		tr.after(clonetr);
	 		//document.getElementById('totalPrice').setAttribute('rowspan', p++);
	 		var total = $("#"+totalObj);
			var num = total.val();
			clonetr.find(":input").each(function(index ,obj){
	 			obj=$(obj);
	 			var name=obj.attr("name").split("_")[0];
	 			obj.attr("id",name+"_"+num).val("");
	 			if(name=="startDate"||name=="endDate"){
	 				$("#"+name+"_"+num).removeClass();
	 				$("#"+name+"_"+num).unbind();
	 				$("input[name='"+name+"'").datetimepicker({
	 					changeMonth : true,
	 					changeYear : true
	 					 
	 				});
	 			}
	 			
	 			obj.attr("name",name);
	 		});
	 	  	total.val(parseInt(num)+1);
	 	  	total.siblings("span").text(parseInt(num)+1);
	  		$("."+classObj).each(function(index, obj){
	  			$($(obj).children("td")[1]).text(parseInt(index)+1);
	  		});
	  		
 	}
 	//减少
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn">
				<div id="btnDiv">
					<div style="display:inline" id="btn">
				    <button class='btn' id="btnsave" type="button" onclick="submitForm('${mfgctx}/oqc/edit-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						
						<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
					</div>
				</div>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;padding-bottom: 4px;">
				<form action="" method="post" id="DefectiveGoodsForm" name="DefectiveGoodsForm">
					<jsp:include page="input-form.jsp" />
				</form>
			</div>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>