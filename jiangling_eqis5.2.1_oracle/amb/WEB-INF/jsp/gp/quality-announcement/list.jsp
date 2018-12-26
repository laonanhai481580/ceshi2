<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<style type="text/css">
	td {
		border-right-style:none;
		border-left-style:none;
		border-top-style:none;
		border-bottom-style:none;
	}
</style>
<head>
<title></title>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
	//格式化
	function operateFormater(cellValue,options,rowObj){
		return operations = "<div style='text-align:left;' title='查看'><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
	}
	function click(cellvalue, options, rowObject){	
		var str = cellvalue;
		if(rowObject.topFlag > 0){
			str += "[置顶]";
		}
		var html = "<a href=\"#\" onclick=\"editInfo("+rowObject.id+");\">"+str+"</a>";
		return html;
	}
	
	function formateAttachmentFiles(value,o,obj){
		return "<div>" +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	
	//查看公告
	function editInfo(id){
		$.colorbox({
			href:'${gpctx}/quality-announcement/view.htm?id='+id,
			iframe:true, 
			width:$(window).width()-300, 
			height:$(window).height(),
			overlayClose:false,
			title:"品质公告",
			onClosed:function(){
			}
		});
	}
	
	function AuditAnnouncement(){
		var ids = $("#projectAnnouncementGrid").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0){
			alert("请选择需要审核的公告!");
			return;
		}
// 		$.post('${ppmctx}/project-announcement/audit-announcement.htm?ids='+ids,null,function(result){
// 			if(!result.error){
// 				$("#message").html("审核成功!");
// 				$("#projectAnnouncementGrid").trigger("reloadGrid");
// 			}else{
// 				$("#message").html(result.message);
// 				alert(result.message);
// 			}
// 			setTimeout(function(){
// 				$("#message").html('');
// 			},1000);
// 		},'json');  
		
	}
	function createAnnouncement(){
		window.location.href = '${gpctx}/quality-announcement/input.htm';
	}
	function editor(){
		var ids = $("#projectAnnouncementGrid").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0||ids.length>1){
			alert("请选择一条公告进行编辑!");
			return;
		}
		window.location.href = '${gpctx}/quality-announcement/input.htm?id='+ids;
	}
	//置顶
	function goTop(){
		var ids = $("#projectAnnouncementGrid").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0){
			alert("请选择需要置顶的数据!");
			return;
		}
		if(!confirm("确定要置顶吗?")){
			return;
		}
		var url = '${gpctx}/quality-announcement/go-top.htm';
		var params = {
			ids : ids.join(",")
		};
		$.showMessage("正在执行操作,请稍候... ...","custom");
		$.post(url,params,function(result){
			$.clearMessage();
			if(result.error){
				alert(result.message);
			}else{
				$("#projectAnnouncementGrid").trigger("reloadGrid");
			}
		},'json');
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="announcement";
		var thirdMenu="quality-announcement-list";
	</script>

<!-- 	<div id="header" class="ui-north"> -->
		<%@ include file="/menus/header.jsp"%>
<!-- 	</div> -->
	<div id="secNav">
		<%@ include file="/menus/gp-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gp-quality-announcement-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="quality-announcement-input">
					<button class='btn' onclick="createAnnouncement();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="portal.add"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="quality-announcement-input">
					<button class='btn' onclick="editor();"><span><span><b class="btn-icons"></b>编辑</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="quality-announcement-delete">
					<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="portal.delete"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="quality-announcement-top">
					<button class='btn' type="button" onclick="goTop();"><span><span><b class="btn-icons btn-icons-move"></b>置顶</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="portal.query"/></span></span></button>
					<a href="${gpctx}/quality-announcement/list-view.htm">布告页</a>
				<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="projectAnnouncementGrid"  url="${gpctx}/quality-announcement/list-datas.htm" code="GP_QUALITY_ANNOUNCEMENT" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>

</html>