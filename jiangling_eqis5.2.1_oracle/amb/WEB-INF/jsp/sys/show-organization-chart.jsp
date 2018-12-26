<%@ page contentType="text/html;charset=UTF-8" import="com.norteksoft.product.util.WebContextUtils,java.text.SimpleDateFormat,java.util.Calendar,com.norteksoft.product.util.SystemUrls,com.norteksoft.product.util.PropUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@include file="/common/meta.jsp" %>	
<script type="text/javascript">
	var isUsingComonLayout=false;
	function _ok_btn() {
		window.parent.$.colorbox.close();
	}
	function enterSystem(url){
		if(url==null||url=='undefined'){
			return false;
		}
		window.parent.$.colorbox.close();
		window.parent.location=url;
	}	
</script>
</head>
	
<body>
	<div class="opt-body">
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
</body>
</html>