<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="myInspectionPlanInput" class="west-notree" url="${gsmctx }/inspectionplan/input.htm" onclick="changeMenu(this);"><span>检定记录</span></div>
<div id="myInspectionPlanReport" class="west-notree" url="${gsmctx }/inspectionplan/list.htm" onclick="changeMenu(this);"><span>检定计划台帐</span></div>
<div id="myInspectionSimpleReport" class="west-notree" url="${gsmctx }/inspection-chart/simplelist.htm" onclick="changeMenu(this);"><span>抽检计划台帐</span></div>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>