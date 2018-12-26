<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<c:set var="actionBaseCtx" value="${mfgctx}/ort/ort-entrust" />
<script type="text/javascript">
		var buttonSign="";
		function addForm(){
			window.location="${actionBaseCtx}/input.htm";
		}
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		
		function viewProcessInfo(value,o,obj){
			var strs = "";
			strs = "<div style='width:100%;text-align:center;' title=\"查看流转历史\" ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a></div>";
			return strs;
		}
		function _viewProcessInfo(formId){
			$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
				innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"实验委托单详情"
			});
		}
		//流程状态显示
		function stageFormatter(value,options, rowObject){
			var launchState = rowObject.launchState;
			var overdueStages = rowObject.overdueStages;
			if(launchState){
				var colName = options.colModel.name;
				if(launchState.indexOf(colName)>-1){
					var reg = new RegExp(colName + "$");
					var overdueReg = new RegExp(',' + colName + ",");
					if(reg.test(launchState)){
						var color = 'green';
						if(overdueReg.test(overdueStages)){
							color = 'red';
						}
						return "<div style='text-align:center;margin-left:-10px;color:"+color+";'>办理中...</div>";
					}else{
						var color = 'green';
						if(overdueReg.test(overdueStages)){
							color = 'red';
						}
						return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/"+color+".gif'/></div>";
					}
				}else{
					return '';
				}
			}else{
				return "";
			}
		}
		function click(cellvalue, options, rowObject){	
			return "<a href='${actionBaseCtx}/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="ort_list";
		var thirdMenu="_ORT_TEST_LIST";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ort-menu.jsp" %>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<div class="opt-btn">
				<security:authorize ifAnyGranted="MFG_ORT_TEST_INPUT">
					<button class='btn' onclick="addForm();">
						<span><span><b class="btn-icons btn-icons-add"></b>新建</span></span>
					</button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_ORT_TEST_DELETE">
					<button class="btn" onclick="iMatrix.delRow();">
						<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
					</button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
				</button>
				<security:authorize ifAnyGranted="MFG_ORT_TEST_EXPORT">
					<button class='btn'
						onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');">
						<span><span><b class="btn-icons btn-icons-export"></b>导出</span></span>
					</button>
				</security:authorize>
			</div>
			<div id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${actionBaseCtx}/list-datas.htm"
						submitForm="defaultForm" code="MFG_ORT_TEST_ENTRUST"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>