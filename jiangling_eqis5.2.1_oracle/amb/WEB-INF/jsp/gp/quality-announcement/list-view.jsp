<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title></title>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	function searchClick(){
		var search = $("#search").val();
		var pageNo = $("#pageNo").val();
 		$("#listView").attr("src","list-view-datas.htm?announcementType=质量公告&pageNo="+pageNo+"&search="+search);
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
	//----分页----//
	function up(obj){
		var search = $("#search").val();
		var pageNo = obj;
		if(pageNo>1){
			pageNo = parseInt(pageNo)-1;
		}
		$("#listView").attr("src","list-view-datas.htm?announcementType=质量公告&pageNo="+pageNo+"&search="+search);
	}
	function down(obj){
		var search = $("#search").val();
		var pageNo = obj;
		if(pageNo<=pages){
			pageNo = parseInt(pageNo)+1;
		}
		$("#listView").attr("src","list-view-datas.htm?announcementType=质量公告&pageNo="+pageNo+"&search="+search);
	}
	function setPageNo(obj){
		var search = $("#search").val();
		var pageNo = obj;
		$("#listView").attr("src","list-datas.htm?announcementType=质量公告&pageNo="+pageNo+"&search="+search);
	}
	//----分页----//
</script>
</head>

<body>
	<script type="text/javascript">
		var secMenu="announcement";
		var thirdMenu="quality-announcement-list-view";
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
			<div id="opt-content" style="clear:both;" align="center">
					<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
					<div style="width:100%;border-top:0px;border-right:0px;border-left:0px;border-bottom: 0px ;padpadding-bottom:20px;text-align:right;" >
						<input type="text"  style="width:150px" name="search" id="search" placeholder="搜索"/>
						<a class="small-button-bg" style="margin-left:2px;margin-top: 0px;float: right;" onclick="searchClick();" href="javascript:void(0);" title="搜索"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
					</div>
					<div style="width:100%;height: 80%">
						<iframe id="listView" src="list-view-datas.htm?pageNo=1&announcementType=质量公告"></iframe>
					</div>
					<%-- <div cstyle="text-align: center;border:0px;padding-top: 15px">
						<button type="button" onclick="up();" >上一页</button>&nbsp;&nbsp;
							<input type="text" style="width:20px" id="pageNo" class="pageNo" href="#" style="color: red" onchange="setPageNo(this);" value="${pages}"></input>
							<span>共 ${pages} 页</span>
						&nbsp;&nbsp;<button type="button" onclick="down();">下一页</button>
					</div> --%>
			</div>
		</div>
	</div>
</body>

</html>