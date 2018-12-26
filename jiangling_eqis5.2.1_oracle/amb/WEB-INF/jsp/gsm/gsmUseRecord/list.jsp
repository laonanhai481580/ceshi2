<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	function borrowGsm() {
		$.colorbox({
			href : '${gsmctx}/gsmUseRecord/borrow-input.htm',
			iframe : true,
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose : false,
			title : "<s:text name='领用登记'/>",
			onClosed : function() {
				$("#dynamicGsmUseRecord").trigger("reloadGrid");
			}
		});
	}
	//红警灯
	function createColorLight(cellvalue, options, rowObject) {
		var returnDate = rowObject.returnDate;//预计归还时间
		var time = (new Date).getTime();//当前时间内
		var newDate = time - new Date(returnDate).getTime();
		var tempTime = 7 * 24 * 60 * 60 * 1000;
		if (newDate < 0) {//(24 * 60 * 60 * 1000)  一天时间
			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
		} else {
			if (newDate > 0 && newDate < tempTime) {
				return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
			} else {
				return '';
			}
		}
	}
	function returnGsm() {
		$.colorbox({
			href : '${gsmctx}/gsmUseRecord/return-input.htm?',
			iframe : true,
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose : false,
			title : "<s:text name='归还登记'/>",
			onClosed : function() {
				$("#dynamicGsmUseRecord").trigger("reloadGrid");
			}
		});
	}
	/**设置邮件提醒*/
	function mailSettings(flag) {
		var url = webRoot + '/gsm/gsmUseRecord/mail-settings.htm?businessCode='+ flag;
		$.colorbox({
			href : url,
			iframe : true,
			width : $(window).width() < 567 ? $(window).width() - 50 : 567,
			height : $(window).height() < 540 ? $(window).height() - 50 : 540,
			overlayClose : false,
			title : "<s:text name='设置提前提醒'/>",
			onClosed : function() {
			}
		});
	}
	function mailSettingsOver(flag, title) {
		var url = webRoot+ '/gsm/gsmUseRecord/mail-settings-over.htm?businessCode='+ flag;
		$.colorbox({
			href : url,
			iframe : true,
			width : $(window).width() < 567 ? $(window).width() - 50 : 567,
			height : $(window).height() < 540 ? $(window).height() - 50 : 540,
			overlayClose : false,
			title : "<s:text name='设置超期提醒'/>",
			onClosed : function() {
			}
		});
	}
	//保存邮件设置时获取的参数
	function getIndicators(businessCode, params) {
		if (businessCode == 'select') {
			params.ids = $("#dynamicGsmUseRecord").jqGrid("getGridParam","selarrrow").join(",");
		} else {
			var postData = $("#dynamicGsmUseRecord").jqGrid("getGridParam","postData");
			for ( var pro in postData) {
				params[pro] = postData[pro];
			}
		}
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "myUseRecord";
		var thirdMenu = "_gsmUseRecord";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp"%>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-use-record-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<%-- <security:authorize ifAnyGranted="gsm_gsmUseRecord_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="common.delete" /></span></span></button>
					</security:authorize> --%>
					<!-- 领用登记  -->
					<security:authorize ifAnyGranted="gsm_gsmUseRecord_register">
						<button class="btn" onclick="borrowGsm();"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name='领用登记' /></span></span></button>
					</security:authorize>
					<!-- 归还登记 -->
					<security:authorize ifAnyGranted="gsm_gsmUseRecord_return">
						<button class="btn" onclick="returnGsm();"><span><span><b class="btn-icons btn-icons-erase"></b><s:text name='归还登记' /></span></span></button>
					</security:authorize>
					<!-- 邮件提醒 -->
					<security:authorize ifAnyGranted="gsm_gsmUseRecord-mail">
						<button class="btn" onclick="mailSettings('gsmUseRecord-mail');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name="提前邮件提醒设置" /></span></span></button>
						<button class="btn" onclick="mailSettingsOver('gsmUseRecord-mail-over');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name="超期邮件提醒设置" /></span></span></button>
					</security:authorize>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="gsm_gsmUseRecord_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/gsmUseRecord/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicGsmUseRecord" url="${gsmctx}/gsmUseRecord/list-datas.htm" code="MEASUREMENT_USE_RECORD" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>