<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="_admittance_inspection" class="west-notree" url="${supplierctx }/admittance/inspection-report/list.htm"
	onclick="changeMenu(this);">
	<span>供应商考察</span>
</div>
<div id="_sample_appraisal" class="west-notree" url="${supplierctx }/admittance/sample-appraisal-report/list.htm"
	onclick="changeMenu(this);">
	<span>样件鉴定</span>
</div>
<div id="_sublots_appraisal" class="west-notree" url="${supplierctx }/admittance/sublots-appraisal-report/list.htm"
	onclick="changeMenu(this);">
	<span>小批鉴定</span>
</div>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>