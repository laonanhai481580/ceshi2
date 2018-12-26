<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="myDefectiveInput" class="west-notree" url="${iqcctx }/defective-goods/input.htm" onclick="changeMenu(this);"><span>不合格品处理单</span></div>
<div id="myDefectiveReport" class="west-notree" url="${iqcctx }/defective-goods/list.htm" onclick="changeMenu(this);"><span>不合格品处理台帐</span></div>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>