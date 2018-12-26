<%@page import="com.ambition.chartdesign.entity.ChartSearch"%>
<%@page import="com.ambition.chartdesign.baseinfo.datasource.service.ChartDatasourceManager"%>
<%@page import="org.hibernate.Hibernate"%>
<%@page import="com.ambition.chartdesign.entity.ChartListViewColumn"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<style>
  .sortable { list-style-type: none; margin: 0; padding: 0; }
  .sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size:14px; height: 18px; }
  .sortable li span { position: absolute; margin-left: -1.3em; }
</style>
<script type="text/javascript">
	$(function(){
		initForm();
		var saveSuccess = '${saveSuccess}';
		if(saveSuccess=='true'){
			window.parent.$("#list").trigger("reloadGrid");
			$.showMessage("保存成功!");
			//cancel();
		}
	});
	function initForm(){
		$("div[series]").css("cursor","pointer").attr("title","系列")
			.click(function(){
				var val = $(":input[name=caculate]").val();
				$(":input[name=caculate]").val(val + "{" + $(this).attr("series") + "}").focus();
			});
		//格式化初始化
		$(":input[formatter]").click(function(){
			formatterClick(this);
		}).each(function(index,obj){
			formatterClick(obj);
		});
	}
	function formatterClick(obj){
		var targetName = $(obj).attr("formatter");
		if($(obj).is(":checked")){
			var targetNames = targetName.split(",");
			for(var i=0;i<targetNames.length;i++){
				if(targetNames[i]){
					$(":input[name="+targetNames[i]+"]").removeAttr("disabled");				
				}
			}
		}else{
			var targetNames = targetName.split(",");
			for(var i=0;i<targetNames.length;i++){
				if(targetNames[i]){
					$(":input[name="+targetNames[i]+"]").attr("disabled","disabled");
				}
			}
		}
	}
	function submitForm(url){
		if($("#form").valid()){
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="javascript:window.location.href='${chartdesignctx}/custom-search/step5.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/custom-search/step-end.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>完成配置</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					6/6.设置自定义查询信息,配置完成。
				</div>
				<form id="form" name="form"
					method="post" action="">
					<fieldset style="margin-top:4px;">
					<legend>基本信息</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:15%;">
									编码
								</td>
								<td style="width:35%;">
									<input type="text" style="width:90%;" name="code" value="${code}" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
								<td style="width:15%;">
									名称
								</td>
								<td style="width:35%;">
									<input type="text" style="width:90%;" name="name" value="${name}" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<label>启用分页,每页显示</label>
									<input type="text" name="rowList" value="${rowList}" id="rowList" style="width:60px;"/>
									<label>条数据.</label>
								</td>
								<td colspan="2" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<label>默认按</label>
									<s:select list="sortBys"
										listKey="value"
										listValue="name"
										emptyOption="true"
										name="sortBy"
										value="sortBy"
										theme="simple">
									</s:select>
									<select name="sortByType">
										<option value="asc" <s:if test="sortByType == 'asc'">selected</s:if>>升序</option>
										<option value="desc" <s:if test="sortByType == 'desc'">selected</s:if>>降序</option>
									</select>
									<label>排序.</label>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<input type="checkbox" 
										name="isDefaultSearch" <s:if test="isDefaultSearch">checked="checked"</s:if> 
										value="1" 
										id="isDefaultSearch"/><label for="isDefaultSearch">打开台账时立即查询</label>
									<input type="checkbox" 
										name="isShowSearchSet" <s:if test="isShowSearchSet">checked="checked"</s:if> 
										value="1" 
										id="isShowSearchSet"/><label for="isShowSearchSet">默认显示查询条件</label>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>页面初始化完成事件</legend>
						<div style="font-weight:bold;">function $initComplete(gridId){</div>
						<textarea rows="20" style="width:98%;font-size:18px;" name="initCompleteMethod">${initCompleteMethod}</textarea>
						<div>}</div>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>数据加载完成后的事件</legend>
						<div style="font-weight:bold;">function $loadComplete(gridId){</div>
						<textarea rows="20" style="width:98%;font-size:18px;" name="loadCompleteMethod">${loadCompleteMethod}</textarea>
						<div>}</div>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>合计列渲染前的格式化方法</legend>
						<div style="font-weight:bold;">function totalFormatterMethod(totalValueJson){</div>
						<textarea rows="20" style="width:98%;font-size:18px;" name="totalFormatterMethod">${totalFormatterMethod}</textarea>
						<div>}</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>