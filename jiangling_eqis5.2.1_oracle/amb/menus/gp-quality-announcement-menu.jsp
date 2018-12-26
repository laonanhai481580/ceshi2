<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	$(function() {
		
	});
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="quality-announcement-list-view,quality-announcement-zsk-view,quality-announcement-zjk-view,supplier-announcement-list-view">
	<h3 onclick="changeMenu(this);" url="<grid:authorize code="quality-announcement-list-view,quality-announcement-zsk-view,quality-announcement-zjk-view,supplier-announcement-list-view" systemCode="gp"></grid:authorize>">
		<a id="REPORT_SET">公告新闻</a>
	</h3>
	<div class="menuParent">
			<security:authorize ifAnyGranted="quality-announcement-list-view">
			<div id="quality-announcement-list-view" class="west-notree"
				url="${gpctx}/quality-announcement/list-view.htm"
				onclick="changeMenu(this);">
				<span><s:text name='品质公告页'/></span>
			</div>
			</security:authorize>
			<security:authorize ifAnyGranted="quality-announcement-zsk-view">
			<div id="quality-announcement-list-zsk-view" class="west-notree"
				url="${gpctx}/quality-announcement/list-zsk-view.htm"
				onclick="changeMenu(this);">
				<span><s:text name='知识库'/></span>
			</div>
			</security:authorize>
			<security:authorize ifAnyGranted="quality-announcement-zjk-view">
			<div id="quality-announcement-list-zjk-view" class="west-notree"
				url="${gpctx}/quality-announcement/list-zjk-view.htm"
				onclick="changeMenu(this);">
				<span><s:text name='专家库'/></span>
			</div>
			</security:authorize>
			<security:authorize ifAnyGranted="supplier-announcement-list-view">
			<div id="supplier-announcement-list-view" class="west-notree"
				url="${gpctx}/supplier-announcement/list-view.htm"
				onclick="changeMenu(this);">
				<span><s:text name='供应商公告页'/></span>
			</div>
			</security:authorize>
			<%-- <div id="strategy-input" class="west-notree"
				url="${gpctx}/quality-policy-strategy/strategy-input.htm"
				onclick="changeMenu(this);">
				<span><s:text name='BOE品质战略'/></span>
			</div> --%>
	</div>
	</security:authorize>
	<security:authorize ifAnyGranted="quality-announcement-list,supplier-announcement-list,quality-policy-strategy-policy-input">
	<h3 onclick="changeMenu(this);" url="<grid:authorize code="quality-announcement-list,supplier-announcement-list,quality-policy-strategy-policy-input" systemCode="gp"></grid:authorize>">
		<a>公告发布</a>
	</h3>
	<div class="menuParent">
		<security:authorize ifAnyGranted="quality-announcement-list">
		<div id="quality-announcement-list" class="west-notree"
			url="${gpctx}/quality-announcement/list.htm"
			onclick="changeMenu(this);">
			<span><s:text name='品质公告'/></span>
		</div>
		</security:authorize>
		
	</div>
	</security:authorize>
</div>
<script type="text/javascript" class="source">
	$(function() {
		var h3Obj = null;
		if (thirdMenu == "REPORT_SET"){
			h3Obj = $("#REPORT_SET").parent();
		}else{
			h3Obj = $("#" + thirdMenu).closest("div.menuParent").prev();
		}
		var index = h3Obj.index();
		var active = index%2+index/2;
		$("#accordion1").accordion({
			animated : false,
			collapsible : false,
			event : 'click',
			fillSpace : true,
			active : active
		});
		/**$("#vlrr").jstree({
			"html_data" : {
				"data" : "<ul>"
					<security:authorize ifAnyGranted="supplier-vlrr-list">
						+ "<li onclick='selectedNode(this)' id='REPORT_SET_POINT'><a href='${supplierctx }/vlrr/report/list.htm'><s:text name='数据采集'/></a></li>"
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-vlrr-list-all">
						+ "<li onclick='selectedNode(this)' id='REPORT_ALL_SET_POINT'><a href='${supplierctx }/vlrr/report/list-all.htm'><s:text name='VLRR数据管理'/></a></li>"
					</security:authorize>
						+"</ul>"
			},
			"ui" : {
				//"initially_select" : [ treeMenu ]
			},
			"plugins" : [ "themes", "html_data","ui" ]
		});*/
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function selectedNode(obj) {
		window.location = $(obj).children('a').attr('href');
	}
	function _change_menu(url) {
		window.location = url;
	}
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>