<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
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
		var inspectionPointType = rowObj.inspectionPointType;
		if('<%=InspectionPointTypeEnum.FIRSTINSPECTION.name()%>' == inspectionPointType){
			return '<a href="${mfgctx}/inspection/first-inspection/input.htm?id='+rowObj.id+'">'+value+'</a>';
		}else if('<%=InspectionPointTypeEnum.STORAGEINSPECTION.name()%>' == inspectionPointType){
			return '<a href="${mfgctx}/inspection/storage-inspection/input.htm?id='+rowObj.id+'">'+value+'</a>';
		}else if('<%=InspectionPointTypeEnum.DELIVERINSPECTION.name()%>' == inspectionPointType){
			return '<a href="${mfgctx}/inspection/deliver-inspection/input.htm?id='+rowObj.id+'">'+value+'</a>';
		}else{
			return value;
		}
	}
	//比率格式化
	function rateFormatter(value){
		if(isNaN(value)){
			return "";
		}else{
			return (parseFloat(value)*100).toFixed(2) + "%";
		}
	}
	function defectiveFormFormatter(cellvalue, options, rowObject){
		if(cellvalue&&cellvalue!='&nbsp;'){
			return "<a href='#' onclick='showDefectiveForm(\""+cellvalue+"\")'>"+cellvalue+"</a>";
		}else{
			if(rowObject.auditRemark){
				return rowObject.auditRemark;
			}else{
				return "";			
			}
		}
	}
	function showDefectiveForm(defectiveFormFormNo){
		$.colorbox({href:'${mfgctx}/defective-goods/ledger/view-info.htm?formNo='+defectiveFormFormNo,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"不合格品处理单详情"
		});
	}
	<jsp:include page="../first-inspection/audit-method.jsp" />
	<jsp:include page="../first-inspection/re-check.jsp" />
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="unquafiedInspectionRecord";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inspection-list-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="">
					<!--<button class='btn' onclick="window.location='${mfgctx}/inspection/first-inspection/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button> -->
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_INSPECTION_AUDIT">
					<button class='btn' type="button" onclick="auditPqc();"><span><span><b class="btn-icons btn-icons-audit"></b>审核</span></span></button>			
					<button class='btn' type="button" onclick="reCheckList();"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_UNQALIFIED_EXPORTS">
					<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/inspection/unqualified/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span id="message"></span>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/inspection/unqualified/list-datas.htm" code="MFG_INSPECTION_UNQUALIFIED" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>