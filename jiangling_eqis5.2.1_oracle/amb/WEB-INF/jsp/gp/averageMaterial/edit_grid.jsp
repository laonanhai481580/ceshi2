<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<div class="opt-btn" style="line-height: 40px;">
		<button class='btn' onclick="saveCertificates();">
			<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
		</button>
		<span id="certificate-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<div class="content" style="overflow:auto;">
		<table id="certificate_table"></table>
	</div>
