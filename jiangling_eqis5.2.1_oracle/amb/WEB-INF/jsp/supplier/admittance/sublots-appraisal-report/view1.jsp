<%@page import="com.ambition.supplier.entity.AppraisalReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var isUsingComonLayout=false;
		$(function(){
			$("#tabs").tabs({
			});
			//添加验证
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			$("#appraisalReportForm :input").attr("readonly","readonly");
			$("#appraisalReportForm :checkbox").attr("disabled",true);
			$("#appraisalReportForm :radio").attr("disabled",true);
			setTimeout(function(){
				$("#tabs-1 .opt-content").width($(window).width()-4);
				$("#tabs-1 .opt-content").height($(window).height()-55);
			}, 100);
		});
	 	function submitImprove(id){
	 		window.location = '${improvectx}/correction-precaution/called-input.htm?sublotsAppraisalId='+id;
// 	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
// 	 			overlayClose:false,
// 	 			title:"改进页面",
// 	 			onClosed:function(){
// 	 			}
// 	 		});
	 	}
	 	function changeViewSet(url,obj){
	 		var target = $(obj).attr("href");
	 		var loaded = $(target).attr("loaded");
	 		if(!loaded){
	 			$(obj).attr("loaded","loading");
	 			var id = $("#opt-content :input[name=id]").val();
	 			$(target).html("<div style='paddint-top:4px;'>数据加载中,请稍候... ...</div>")
	 				.height($(window).height()-40)
	 				.load(url,{id:id},function(result){
		 				$(target).attr("loaded",true);
		 			});
	 		}
		}
	 	function viewDetailInfo(value){
	 		$("body").attr("disabled",true);
			window.location='${supplierctx}/admittance/sublots-appraisal-report/view1.htm?id='+value;
		}
	 	function operateFormatter(value,o,rowObj){
			var operations = "<div style='text-align:center;'>";
			operations += "<a class=\"small-button-bg\" href=\"javascript:void(0);viewDetailInfo("+value+");\" title='查看详细信息'><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a>";
			operations += "</div>";
			return operations;
		}
		function appraisalFormatter(value){
			return "第" + value + "次小批鉴定";
		}
		function improveOperateFormatter(value){
			return "<div style='text-align:center;' title='发起改进'><a class=\"small-button-bg\" onclick='submitImprove("+value+");'><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		}
		function admittanceFormatter(value,o,rowObj){
			if(rowObj.admittance){
				if(rowObj.admittance=='ok'){//代表可以准入
					return "&nbsp;可准入";
				}else{
					return "&nbsp;已准入";					
				}
			}else{
				return "&nbsp;不可准入";
			}
		}
		function addFormatter(value,o,rowObj){
			if(rowObj.admittance){
				if(rowObj.admittance=='ok'){//代表可以准入
					return "&nbsp;";
				}else{
					return "&nbsp;已准入";					
				}
			}else{
				return "&nbsp;";
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body" style="overflow-y:auto;">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="changeViewSet('',this)">第${appraisalReport.timeOfPhase}次小批鉴定信息</a></li>
				<li><a href="#tabs-2" onclick="changeViewSet('${supplierctx}/admittance/sublots-appraisal-report/view-other.htm?id=${appraisalReport.id}&supplierId=${appraisalReport.supplier.id}&bomCode=${appraisalReport.bomCodes}',this)">其他相关小批鉴定</a></li>
			</ul>
			<div id="tabs-1" style="padding:0px;" loaded='true'>
				<%@ include file="view-form.jsp"%>
			</div>
			<div id="tabs-2" style="padding:0px;overflow-y:auto;text-align:center;">
			</div>
		</div>
	</div>
</body>
</html>