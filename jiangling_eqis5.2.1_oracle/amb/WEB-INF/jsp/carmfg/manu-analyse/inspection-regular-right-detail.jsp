<%@page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function $getExtraParams(){
		return {searchStrs:'${params}'};
	}
	function contentResize(){
		$("#dynamicInspection").jqGrid("setGridHeight",$(window).height()-75);
	}
	function formatRate(cellvalue, options, obj){
		if(cellvalue){
			var rate = cellvalue*100;
			if(obj.inspectionAmount != undefined && obj.qualifiedAmount != undefined){
				rate = obj.qualifiedAmount*100.0/obj.inspectionAmount;
			}
			if(rate.toFixed){
				rate = rate.toFixed(2);
			}
			var operations = "<div style='text-align:left;' class='rate'>"+rate+"%</div>";
			return operations;
		}else{
			return "<div style='text-align:left;' class='rate'></div>";
		}
	}
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	
	function beginUpload(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFiles",
			hiddenInputId : rowId + "_hiddenAttachmentFiles",
			callback : function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenAttachmentFiles").val();
			}
		});
	}
	function inspectionNoFormatter(value,o,rowObj){
		return '<a href="javascript:void(0);openViewInfo(\''+value+'\')">'+value+'</a>';
	}
	//比率格式化
	function rateFormatter(value){
		if(isNaN(value)){
			return "";
		}else{
			return (parseFloat(value)*100).toFixed(2) + "%";
		}
	}
	function openViewInfo(inspectionNo){
		window.location.href = '${mfgctx}/inspection/first-inspection/view-info.htm?inspectionNo='+inspectionNo;
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='#' onclick='javascript:goTo(\""+rowObject.id+"\")'>"+cellvalue+"</a>";
	}
	function goTo(id){
		 if($.isFunction(window.parent.goToNewLocationById)){
			window.parent.goToNewLocationById(id);
		} 
	}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
				${title}
			</div>
			<div id="opt-content">
				<%
					String _source = Struts2Utils.getParameter("_source");
					String dataUrl = "/manu-analyse/insepction-regular-right-detail-datas.htm";
					if("general".equals(_source)){
						dataUrl = "/manu-analyse/inspection-general-plato-detail-datas.htm";
					}
					ActionContext.getContext().put("dataUrl",dataUrl);
				%>
				<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}${dataUrl}" code="MFG_FIRST_INSPECTION_REPORT" pageName="inspectionPage"></grid:jqGrid>
			</div>
		</div>
	</div>
</body>
</html>