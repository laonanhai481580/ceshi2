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
 		$("#listView").attr("src","list-view-datas.htm?announcementType=专家库&pageNo="+this.pageNo+"&search="+search);
	}
	//查看公告
	function editInfo(id){
		$.colorbox({
			href:'${gpctx}/quality-announcement/view.htm?id='+id,
			iframe:true, 
			width:$(window).width()-300, 
			height:$(window).height(),
			overlayClose:false,
			title:"专家库",
			onClosed:function(){
			}
		});
	}
	//----分页----//
	var pageNo=1;
	function up(){
		var search = $("#search").val();
		if(this.pageNo>1){
			pageNo = parseInt(pageNo)-1;
		}
		$("#listView").attr("src","list-view-datas.htm?announcementType=专家库&pageNo="+this.pageNo+"&search="+search);
		var min = $("#a1").html();
		for(var i=1;i<6;i++){
			$("#a"+i).attr("style","");
			if(i+parseInt(min)-1==this.pageNo){
				$("#a"+i).attr("style","color: red");
			}
		}
		if(pageNo<min){
			for(var i=5,t=0;i>0;i--,t++){
				$("#a"+i).html(pageNo-t);
			}
			$("#a5").attr("style","color: red");
		}
	}
	function down(){
		var search = $("#search").val();
		pageNo = parseInt(pageNo)+1;
		$("#listView").attr("src","list-view-datas.htm?announcementType=专家库&pageNo="+this.pageNo+"&search="+search);
		var max = $("#a5").html();
		for(var i=1;i<6;i++){
			$("#a"+i).attr("style","");
			if((i+parseInt(max)-5)==this.pageNo){
				$("#a"+i).attr("style","color: red");
			}
			if(this.pageNo>max){
				$("#a"+i).html(pageNo+i-1);
				$("#a1").attr("style","color: red");
			}
		}
	}
	function setPageNo(obj){
		var search = $("#search").val();
		pageNo = parseInt($(obj).html());
		$(".pageNo").each(function(){
			if($(this).html()==pageNo){
				$(this).attr("style","color: red");
			}else{
				$(this).attr("style","");
			}
		});
		$("#listView").attr("src","list-datas.htm?announcementType=专家库&pageNo="+this.pageNo+"&search="+search);
	}
	//----分页----//
</script>
</head>

<body>
	<script type="text/javascript">
		var secMenu="announcement";
		var thirdMenu="quality-announcement-list-zjk-view";
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
						<iframe id="listView" src="list-view-datas.htm?pageNo=1&announcementType=专家库"></iframe>
					</div>
					<!-- <div cstyle="text-align: center;border:0px;padding-top: 15px">
						<button type="button" onclick="up();" >上一页</button>&nbsp;&nbsp;
							<a id="a1" class="pageNo" href="#" style="color: red" onclick="setPageNo(this);">1</a>
							<a id="a2" class="pageNo" href="#" onclick="setPageNo(this);">2</a>
							<a id="a3" class="pageNo" href="#" onclick="setPageNo(this);">3</a>
							<a id="a4" class="pageNo" href="#" onclick="setPageNo(this);">4</a>
							<a id="a5" class="pageNo" href="#" onclick="setPageNo(this);">5</a>
						&nbsp;&nbsp;<button type="button" onclick="down();">下一页</button>
					</div> -->
			</div>
		</div>
	</div>
</body>

</html>