<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-abnormal-list">
	<div id="abnormalList" class="west-notree" url="${supplierctx }/abnormal/list.htm"
		onclick="changeMenu(this);">
		<span>上线异常数据录入</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="sentOutRecord-List">
	<div id="sentOutRecordList" class="west-notree" url="${supplierctx }/sent-out-record/list.htm"
		onclick="changeMenu(this);">
		<span>发料记录</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplierMrbCode-List">
	<div id="supplierMrbCodeList" class="west-notree" url="${supplierctx }/supplierMrbCode/list.htm"
		onclick="changeMenu(this);">
		<span>MRB单号</span>
	</div>
</security:authorize>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){;
			window.location = $(obj).attr('url');
		}
	</script>