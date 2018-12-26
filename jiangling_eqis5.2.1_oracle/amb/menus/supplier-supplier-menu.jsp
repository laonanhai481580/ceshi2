<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="archives-list">
	<div id="_archives_list" class="west-notree" url="${supplierctx }/archives/list.htm"
		onclick="changeMenu(this);">
		<span>零部件供应商</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-list-all">
	<div id="_archives_list_all" class="west-notree" url="${supplierctx }/archives/list-all.htm"
		onclick="changeMenu(this);">
		<span>供应商台账</span>
	</div>
</security:authorize>

<%-- <div id="_potential" class="west-notree" url="${supplierctx }/archives/potential.htm" --%>
<!-- 	onclick="changeMenu(this);"> -->
<!-- 	<span>潜在供应商名录</span> -->
<!-- </div> -->
<%-- <div id="_allow" class="west-notree" url="${supplierctx }/archives/allow.htm" --%>
<!-- 	onclick="changeMenu(this);"> -->
<!-- 	<span>准供应商名录</span> -->
<!-- </div> -->
<security:authorize ifAnyGranted="archives-qualified">
<div id="_qualified" class="west-notree" url="${supplierctx }/archives/qualified.htm"
	onclick="changeMenu(this);">
	<span>合格供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-qualified-cancle-list">
<div id="_cancleList" class="west-notree" url="${supplierctx }/archives/supplier-cancle/list.htm"
	onclick="changeMenu(this);">
	<span>合格供应商取消申请台账</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-qualified-cancle-input">
<div id="_cancleInput" class="west-notree" url="${supplierctx }/archives/supplier-cancle/input.htm"
	onclick="changeMenu(this);">
	<span>合格供应商取消申请单</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-eliminated">
<div id="_eliminated" class="west-notree" url="${supplierctx }/archives/eliminated.htm"
	onclick="changeMenu(this);">
	<span>已淘汰供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-message">
<div id="_messageList" class="west-notree" url="${supplierctx }/archives/supplier-messages/list.htm"
	onclick="changeMenu(this);">
	<span>通告/下载栏</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-cancle">
<div id="_cancle_list" class="west-notree" url="${supplierctx }/archives/supplier-cancle-datas.htm"
	onclick="changeMenu(this);">
	<span>可取消D级供应商</span>
</div>
</security:authorize>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>