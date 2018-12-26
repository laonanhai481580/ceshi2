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
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		
		//重写(给单元格绑定事件)
		var params = {};
		function $oneditfunc(rowid){
			params = {
				hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()
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
		function formateMessageFile(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		function beginUpload(rowId){
			$.upload({
				showInputId : rowId + "_showAttachmentFiles",
				hiddenInputId : rowId + "_hiddenAttachmentFiles",
				callback : function(files){
					params.reportFile = $("#" + rowId + "_hiddenAttachmentFiles").val();
				}
			});
		}
	</script>
	</head>

	<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
		<script type="text/javascript">
			var secMenu="sentOutRecord";
			var thirdMenu="supplierMrbCodeList";
		</script>


		<div id="header" class="ui-north">
			<%@ include file="/menus/header.jsp" %>
		</div>
		
		<div id="secNav">
			<%@ include file="/menus/supplier-sec-menu.jsp" %>
		</div>
		
		<div class="ui-layout-west">
			<%@ include file="/menus/supplier-abnormal-menu.jsp" %>
		</div>
		<div class="ui-layout-center">
			<div class="opt-body">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplierMrbCode_save">
					    <button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplierMrbCode_delete">
					    <button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplierMrbCode_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/maintain/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>	
				</div>
				<div id="opt-content" style="clear:both;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="suppliers"
							url="${supplierctx}/supplierMrbCode/list-datas.htm" code="SUPPLIER_MRB_CODE"></grid:jqGrid>
					</form>
				</div>
			</div>
		</div>
	</body>
	</html>