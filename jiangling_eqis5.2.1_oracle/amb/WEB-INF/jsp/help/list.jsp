<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/js/opinion.js" type="text/javascript"></script>
	<script type="text/javascript">
		
// 		function $gridComplete(){
// 			$("#list").jqGrid('setGroupHeaders',{
// 				useColSpanStyle: true, 
// 				groupHeaders:${groupNames}
// 			});
// 		}

  /*
    var params = {};
	function $oneditfunc(rowid){
		params = {
			hisAttachmentFilesG : $("#" + rowid + "_hiddenAttachmentFilesG").val()
		};
		$("#" + rowid + " .upload").show();
	};
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		if(data.id){
			$("#" + data.id).find(":input[name]").each(function(index,obj){
				data[obj.name] = $(obj).val();
			});
		}
		return data;
	}	
	function formateMessageFileG(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUploadG("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFilesG'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFilesG' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	function beginUploadG(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFilesG",
			hiddenInputId : rowId + "_hiddenAttachmentFilesG",
			callback : function(files){
				params.fileSystem = $("#" + rowId + "_hiddenAttachmentFilesG").val();
			}
		});
	}
  */
	
	var params = {};
	function $oneditfunc(rowid){
		params = {
			hisAttachmentFilesG : $("#" + rowid + "_hiddenAttachmentFilesG").val()
		};
		$("#" + rowid + " .upload").show();
	};
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		if(data.id){
			$("#" + data.id).find(":input[name]").each(function(index,obj){
				data[obj.name] = $(obj).val();
			});
		}
		return data;
	}
	//上传附件
	function formateMessageFileG(value,o,obj){
		var btn = "<a style='float:left;' class=\"small-button-bg upload\" onclick=\"beginUploadG("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFilesG'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFilesG' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	function beginUploadG(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFilesG",
			hiddenInputId : rowId + "_hiddenAttachmentFilesG",
			callback : function(files){
				params.fileSystem = $("#" + rowId + "_hiddenAttachmentFilesG").val();
			}
		});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="document";
	var thirdMenu="document-list";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/document-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
	   <%@ include file="/menus/document-third-menu.jsp" %>
	</div>
	   
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				        <security:authorize ifAnyGranted="document-add">	  	
					     	<button class='btn' id="btnsub" type="button" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-ok"></b>新建</span></span></button>
				  	    </security:authorize>
				  	    <security:authorize ifAnyGranted="document-delete">	  	
					     	<button class='btn' id="btnsub" type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				  	    </security:authorize>
						<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
						
                    <span style="color:red;" id="message"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicDefectiveGood" url="${helpctx}/list-datas.htm" code="DOCUMENT_DOCUMENT" pageName="page"></grid:jqGrid>
						
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>