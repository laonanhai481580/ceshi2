<%@page import="com.norteksoft.product.util.ThreadParameters"%>
<%@page import="com.norteksoft.product.util.ParameterUtils"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="com.norteksoft.product.util.WebContextUtils,java.text.SimpleDateFormat,java.util.Calendar,com.norteksoft.product.util.SystemUrls,com.norteksoft.product.util.PropUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@ include file="portal-meta.jsp"%>
<script src="${resourcesCtx}/js/jquery.timers-1.2.js" type="text/javascript"></script>
<script src="${portalCtx}/js/index.js" type="text/javascript"></script>
<script src="${portalCtx}/js/layout.js" type="text/javascript"></script>
<script src="${resourcesCtx}/js/myMessage.js" type="text/javascript"></script>
<style type="text/css">
	#header-resizer img.addpage{ float: left;  margin: 4px 6px;cursor: pointer;}
	#header-resizer img.addpage:hover{ background-color: transparent; border: none;}
	#header-resizer img.editpage{ float: left; margin: 10px 2px 2px 6px; cursor: pointer; display: inline;}
	#header-resizer img.editpage:hover{ background-color: transparent; border: none;}
	#header-resizer ul li span span a{ float: left; }
	.palace-left-c2{ width: 40%; }
	.palace-right-c2{ width: 59%; }
	.palace-widget{ width: 100%; }
	.leadTable th,.leadTable td{white-space:nowrap;}
	.ui-layout-center{overflow:auto;*padding-right:16px;overflow-x:hidden;}
	.div-tb{float:left;width:120px;height:20px;margin:0 5px;text-align: center;}
</style>

<script type="text/javascript">
	function enterSystem(url){
		if(url==null||url=='undefined'){
			return false;
		}
		window.parent.location=url;
	}
</script>
</head>
<body onclick="bodyClick();$('#sysTableDiv').hide();$('#styleList').hide();">
	<%@ include file="/menus/header.jsp"%>
	<div id="tabsDivDefault" style="width: 100px; height: 20px; background:#FFFFFF; display: none; border: 1px solid #B7B7B7;">
		<div style="width: 100px; height: 20px; text-align: center; line-height: 20px;" onmouseover="tabsAOver(this)" onmouseout="tabsAOut(this)"><a href="#" onclick="alterWebpage(this);bodyClick();">修改页签</a></div>
	</div>
	<div id="secNav"></div>
	<div class="ui-layout-center">
		<aa:zone name="webpage_zone">
		<div id="widgetDiv" align="center">
			<form id="widgetForm" action="${portalCtx}/index/index!saveWidgetToPortal.htm" method="post">
				<input type="hidden" id="webpageId" name="webpageId" value="${webpage.id}"/>
				<input type="hidden" id="widgetPosition" name="positions" value="${webpage.widgetPosition}"/>
				<input type="hidden" id="_widgetCode" name="widgetCode" value=""/>
				<input type="hidden" id="_position" name="position" value=""/>
				<input type="hidden" id="message_visible" value="${baseSetting.messageVisible }"/>
				<input type="hidden" id="refresh_time" value="${baseSetting.refreshTime }"/>
				<input type="hidden" id="portalMessage" value="portal"/>
			</form>
			<img src="${ctx}/images/system-road-map.jpg" usemap="#rm">
				<map name="rm" id="rm">
					<area shape="rect" coords="208,131,399,202" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("goal")+PropUtils.getProp("redirectUrl.properties", "goal")%>')" alt="目标管理"/>
					<area shape="rect" coords="59,231,115,376" href="#" onclick="enterSystem()" alt="研发质量管理" />
					<area shape="rect" coords="119,228,175,373" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("supplier")+PropUtils.getProp("redirectUrl.properties", "supplier")%>')" alt="供应商质量管理"/>
					<area shape="rect" coords="179,228,234,374" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("iqc")+PropUtils.getProp("redirectUrl.properties", "iqc")%>')" alt="进货检验"/>
					<area shape="rect" coords="239,229,294,375" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("carmfg")+PropUtils.getProp("redirectUrl.properties", "carmfg")%>')" alt="过程品质"/>
					<area shape="rect" coords="299,229,354,375" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("aftersales")+PropUtils.getProp("redirectUrl.properties", "aftersales")%>')" alt="售后质量管理"/>
					<area shape="rect" coords="374,232,444,377" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("spc")+PropUtils.getProp("redirectUrl.properties", "spc")%>')" alt="统计过程控制"/>
					<area shape="rect" coords="473,226,577,384" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("improve")+PropUtils.getProp("redirectUrl.properties", "improve")%>')" alt="项目改进管理"/>
					<area shape="rect" coords="186,399,301,451" href="#" onclick="enterSystem()" alt="产品评价"/>
					<area shape="rect" coords="321,400,438,451" href="#" onclick="enterSystem('<%=SystemUrls.getBusinessPath("monitor")+PropUtils.getProp("redirectUrl.properties", "monitor")%>')" alt="过程质量监控预警"/>
					<area shape="rect" coords="140,466,453,506" href="#" onclick="enterSystem()" alt="质量追溯管理"/>
				</map>
			</img>					   
		</div>
		</aa:zone>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
</html>