<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
    <div style="display: block; height: 10px;"></div>
	<div id="inputRecord" class="menu" url="${mfgctx}/inspection/inprocess-check/list.htm"  onclick="changeMenu(this);"><span>检查记录</span></div>
	<div id="allRecord"   class="menu" url="${mfgctx}/inspection/inprocess-check/list-all.htm" onclick="changeMenu(this);"><span>检查记录台帐</span></div>

	<script type="text/javascript" class="source">
		$().ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>