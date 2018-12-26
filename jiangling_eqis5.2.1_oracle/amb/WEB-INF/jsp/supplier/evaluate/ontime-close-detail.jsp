<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='complain.title-1'/></title>
	<%@include file="/common/meta.jsp" %> 
	<c:set var="actionBaseCtx" value="${complainctx}/customer-complaint"/>
	<script type="text/javascript">
		function $getExtraParams(){
			var params = ${params};
			var postData = {};
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
			return postData;
		}
		
		function contentResize(){
			$("#list").jqGrid("setGridWidth",$(window).width()-25);
			$("#list").jqGrid("setGridHeight",$(window).height()-95);
		}

		function formNoFormate(cellvalue, options, rowObject){
			if(cellvalue){
				return "<a href='#' onclick='callList("+rowObject.id+")'> "+cellvalue+"</a>";
			}else{
				return "";
			}			
		}

		function callList(id){
			$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+id,iframe:true,
				innerWidth:$(window).width()<850?$(window).width()-150:850, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"<s:text name='complain.message-4'/>"
			});
		}
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title=\"<s:text name='complain.message-5'/>\"><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		function viewProcessInfo(value,o,obj){
			var strs = "";
			strs = "<div style='width:100%;text-align:center;' title=\"<s:text name='complain.message-6'/>\" ><a class=\"small-button-bg\"  onclick=\"callList("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
			return strs;
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			return false;
		}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${actionBaseCtx}/ontime-close-detail-datas.htm" submitForm="defaultForm" code="CUSTOMER_COMPLAINT_INFO" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script> --%>
</html>