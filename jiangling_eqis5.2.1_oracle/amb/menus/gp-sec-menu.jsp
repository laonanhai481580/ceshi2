<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="gp-average-list_pending">
		<li id="pending">
		     <span>
		          <span><a href="<grid:authorize code="gp-average-list,gp-average-list_pending" systemCode="gp"></grid:authorize>">待处理事项</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="gp-gpmaterial-LIST">
		<li id="environmental">
		     <span>
		          <span><a href="<grid:authorize code="gp-average-list_ok" systemCode="gp"></grid:authorize>">绿色环保管理</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="gp-baseinfo-list">
		<li id="exemption">
		     <span>
		          <span><a href="<grid:authorize code="gp-exemption-list" systemCode="gp"></grid:authorize>">基础维护</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="quality-announcement-list-view">
		<li id=announcement><span><span>
			<a href="<grid:authorize code="quality-announcement-list-view" systemCode="gp"></grid:authorize>">公告新闻</a>
			</span>
		</span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='gp';
	$('#'+secMenu).addClass('sec-selected');
</script>


