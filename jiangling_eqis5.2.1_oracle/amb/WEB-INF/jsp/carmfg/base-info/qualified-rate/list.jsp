<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.api.entity.Option"%>
<%@ page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){});
	function submitForm(url){
		if($('#rateSettingForm').valid()){
			var checkbox = document.getElementsByName("state");
		    for(var i=0;i<checkbox.length;i++){
				if(checkbox[i].checked != false){
					var radio = document.getElementsByName("rateSelect");
					var selectType = "noSerious";
					if(radio.length <= 0){
						alert("规则没有设置，不能启用！");
						return;
					}else{
						for(var j=0;j<radio.length;j++){
							if(selectType==radio[j].value){
								var checkBox1 = document.getElementsByName("numAndScoreType");
								if(checkBox1.length <= 0){
									alert("无'严重'不良规则没有设置，不能启用！");
									return;
								}else{
									var ns = "scoreValue",ns1 = "itemValue";
									for(var k=0;k<checkBox1.length;k++){
										if(ns==checkBox1[k].value&&checkBox1[k].checked!=false){
											if($(":input[name=score]").val()==""){
												alert("不良扣分值没有设置，不能启用！");
												return;
											}
										}
										if(ns1==checkBox1[k].value&&checkBox1[k].checked!=false){
											if($(":input[name=number]").val()==""){
												alert("不良项目个数没有设置，不能启用！");
												return;
											}
										}
									}
								}
							}
						}
					}
				}
		    } 
			$('#rateSettingForm').attr('action',url);
			$("#message").html("<b style=\"color: red\">数据保存中,请稍候... ...</b>");
			$('#rateSettingForm').submit();
		}
	}
	function checkme(e){ 
		var t = document.getElementsByName(e.name);//取得鼠标点中的控件数组。 
		var est = e.checked; 
		for(var i=0;i <t.length;i++){
			t[i].checked=false;//排除同组控件选中
		} 
		e.checked=est; 
	} 

	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="qualifiedRate";
		var treeMenu="rate";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
			<security:authorize ifAnyGranted="base-info-qualified-rate-save">
				<button class="btn" onclick="submitForm('${mfgctx}/base-info/qualified-rate/save.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="rateSettingForm" name="rateSettingForm">
					<table style="width:100%;">
						<tr>
							<td style="width:100%;" align="center">
								<table class="form-table-border-left" style="width:90%;">
									<caption style="height: 10px"><h2>一次合格率规则设置</h2></caption>
								</table>
							</td>
						</tr>
						<tr>
							<td style="width:100%;" align="center">
								<table class="form-table-border-left" style="width:90%;margin-top: 50px;">
									<tr>
										<td style="width:15%;" rowspan="2">一次合格率</td>
										<td colspan="2">
											<input type="radio" name="rateSelect" value="noChange" <s:if test="%{#rateRule.rateSelect=='noChange'}">checked="checked" </s:if>/>出现“不可调”不良，即为一次不合格；
										</td>
										<td style="width:10%;" rowspan="2">
											<input type="checkbox" name="state" value="启用" <s:if test="%{#rateRule.state=='启用'}">checked="checked" </s:if>/>启用
										</td>
									</tr>
									<tr>
										<td style="width:20%;">
											<input type="radio" name="rateSelect" value="noSerious" <s:if test="%{#rateRule.rateSelect=='noSerious'}">checked="checked" </s:if>/>无“严重”不良
										</td>
										<td>
											<table class="form-table-without-border">
												<tr>
													<td>
														<input type="checkbox" name="numAndScoreType" value="scoreValue" onclick="checkme(this);" <s:if test="%{#rateRule.numAndScoreType=='scoreValue'}">checked="checked" </s:if>/>不良扣分值总分低于<input name="score" value="${rateRule.score }" style="width:10%;"/>分,即为合格；
													</td>
												</tr>
												<tr>
													<td>
														<input type="checkbox" name="numAndScoreType" value="itemValue" onclick="checkme(this);" <s:if test="%{#rateRule.numAndScoreType=='itemValue'}">checked="checked" </s:if>/>不良项目<input name="number" value="${rateRule.number }" style="width:10%;"/>个内,即为合格；
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	
</body>
</html>