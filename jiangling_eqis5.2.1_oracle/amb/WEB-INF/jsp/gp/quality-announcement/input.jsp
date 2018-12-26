<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.product.api.ApiFactory"%>

<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title></title>
<%@ include file="input-script.jsp" %>
<%@include file="/common/meta.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Calendar calendar = Calendar.getInstance();
	String nowDateStr = sdf.format(calendar.getTime());
	String currentUser=ContextUtils.getUserName();
	ValueStack valueStack=ActionContext.getContext().getValueStack();
	Long deptId = ContextUtils.getDepartmentId();
	String currentDept= ApiFactory.getAcsService().getDepartmentById(deptId).getName();
	
%>
	<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<link rel="stylesheet" href="${ctx}/widgets/kindeditor-4.1.7/themes/default/default.css" />
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/lang/zh_CN.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#releaseTime").datetimepicker({changeMonth:true,changeYear:true});
		if($("#releaseTime").val()==""){
			$("#releaseTime").val("<%=nowDateStr%>");
		}
		if($("#releaseTime").val().length>20){
			$("#releaseTime").val($("#releaseTime").val().substring(0,10));
		}
		if($("#publisher").val()==""){
			$("#publisher").val("<%=currentUser%>");
		}
		if($("#publishOrganization").val()==""){
			$("#publishOrganization").val("<%=currentDept%>");
		}
		$.parseDownloadPath({
			showInputId : 'showAttachFile',
			hiddenInputId : 'attachFile',
		});
		//格式化封面图片
		afterUpload();
	});
		
	
	var editor;
	var JSESSIONID= '';
	var strs = document.cookie?document.cookie.split(";"):[];
	for(var i=0;i<strs.length;i++){
		var str = strs[i];
		if(str&&str.indexOf('JSESSIONID=')==0){
			JSESSIONID = str.split("=")[1];
			break;
		}
	}
	var uploadUrl = webRoot + "/carmfg/common/upload.htm;jsessionid=" + JSESSIONID;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			allowFileManager : true,
			//uploadJson : uploadUrl,
			uploadJson : '${gpctx}/quality-announcement/upload-image.htm',
			//fileManagerJson : '${gpctx}/quality-announcement/file_manager_json.htm'
		});
		
	});
	
	 function selectProjectCode(){
			$.colorbox({
				href:'${ppmctx}/project-code/select-project-code.htm',
				iframe:true, 
				width:800, 
				height:$(window).height(),
				overlayClose:false,
				title:"项目任务",
				onClosed:function(){
				}
			});
		}
	 
	 
	
	
	
		function submitForm(){
			if($("#basicForm").valid()){
				var params = getParams();
				if(params.content){
					delete params.content;
				}
				//alert($.param(params));
				//内容超长时,分批保存,15000个字保存一次
				var contentHtml = params.showContentHtml;
				var contentArrs = [];
				var maxLen = contentHtml.length;
				var batchLen = 15000;//每一批传入的数据长度,15000个字保存一次
				var size = parseInt(maxLen/batchLen,10);
				if(maxLen%batchLen>0){
					size++;
				}
				for(var i=0;i<size;i++){
					var start = i * batchLen;
					var end = (i+1)*batchLen;
					if(end > maxLen){
						end = maxLen;
					}
					contentArrs.push(contentHtml.substring(start,end));
				}
				var batchSave = function(params,contentArrs,index){
					var tempParams = null;
					if(index+1==contentArrs.length){
						tempParams = params;
						tempParams.isEnd = true;
					}else{
						tempParams = {};
					}
					if(index==0){
						tempParams.isStart = true;
					}
					tempParams.showContentHtml = contentArrs[index];
					$.post('${gpctx}/quality-announcement/save.htm',tempParams,function(result){
						if(result.error){
							$.showMessage(result.message,"custom");
							window.location.replace();
						}else if(result.success){
							$.showMessage("保存成功!","custom");
							if(tempParams.id){
								$(".opt-btn .btn").removeAttr("disabled");
							}else{
								setTimeout(function(){
									window.location.href = '${gpctx}/quality-announcement/input.htm?id='+result.id;
								},1000);
							}
						}else{
							batchSave(params,contentArrs,index+1);
						}
					},'json');  
				};
				$.showMessage("正在保存,请稍候... ...","custom");
				$(".opt-btn .btn").attr("disabled",true);
				batchSave(params,contentArrs,0);
			}else{
				var error = $("#basicForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
	 
	//获取参数
		function getParams(){
			var params = {};
			$("#basicForm :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					params[obj.name] = jObj.val();
				}
			});	
// 			params['showContent']=editor.text();
			params['showContentHtml']=editor.html();
			return params;
		}
		function createAnnouncement(){
			window.location.href = '${gpctx}/quality-announcement/input.htm';
		}
		//上传附件
		function uploadFiles(showId,hideId,callBack){
			var params = {
				appendTo : '#opt-content',
				showInputId : showId,
				hiddenInputId : hideId
			};
			if(callBack){
				params.callback = callBack;
			}
			$.upload(params);
		}
		
		function preview(){
// 			var id = '${id}';
// 			if(!id){
// 				alert("请先保存!");
// 				return false;
// 			}
// 			var url = '${gpctx}/quality-announcement/preview-view.htm';
// 			 $.colorbox({
// 				href:url,
// 				iframe:true, 
// 				width:$(window).width()-100, 
// 				height:$(window).height()-100,
// 				overlayClose:false,
// 				title:"预览品质公告",
// 				onClosed:function(){}
// 			}); 
// 			return;
			var params = getParams();
			var url = '${gpctx}/quality-announcement/preview-view.htm?1=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			//alert(url);
			 $.colorbox({
				href:url,
				iframe:true, 
				width:900, 
				height:$(window).height(),
				overlayClose:false,
				title:"预览品质公告",
				onClosed:function(){
				}
			}); 
		}
		
		//选择人员
	 	function selectObj(title,hiddenInputId,showInputId,multiple){
			var acsSystemUrl = "${ctx}";
			popTree({ title : title,
				innerWidth:'400',
				treeType:"MAN_DEPARTMENT_TREE",
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:multiple,
				hiddenInputId :hiddenInputId,
				showInputId : showInputId,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
		function afterUpload(){
			var coverImg = $("#coverImg").val();
			var hasSet = false;
			if(coverImg&&coverImg.length>0){
				var files = $.getFiles(coverImg);
				if(files.length>0){
					$("#coverImgShow").attr("src",$.getDownloadPath(files[0].id));
					hasSet = true;
				}
			}
			if(!hasSet){
				$("#coverImgShow").attr("src","");
			}
		}
		//发布公告
		function release(topFlag){
			var id = $("#id").val();
			if(id==null||id==""){
				alert("请先保存!");
				return;
			}
			var params = {id:id};
			if(topFlag){
				params.topFlag = 1;
			}
			if(!confirm("确定要发布吗?")){
				return;
			}
			$("#message").html("正在发布请稍后......");
			$.post('${gpctx}/quality-announcement/release.htm',{id:id},function(result){
				if(!result.error){
					alert("发布成功!");
					$("#message").html("");
// 					$("#message").html("发布成功!");
					setTimeout(function(){
						window.location.href = '${gpctx}/quality-announcement/input.htm?id='+result.id;
					},1000);
				}else{
					$("#message").html(result.message);
					alert(result.message);
				}
			},'json');  
		}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="announcement";
		var thirdMenu="quality-announcement-list";
	</script>

<!-- 	<div id="header" class="ui-north"> -->
		<%@ include file="/menus/header.jsp"%>
<!-- 	</div> -->
	<div id="secNav">
		<%@ include file="/menus/gp-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gp-quality-announcement-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
			<security:authorize ifAnyGranted="quality-announcement-input">
			<button class='btn' onclick="createAnnouncement();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
			</security:authorize>
<%-- 			<s:if test="id!=null&&publisher == #currentUserName">
 --%>		<security:authorize ifAnyGranted="quality-announcement-save">
 			<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="quality-announcement-preview-view">
			<button  class='btn' type="button" onclick="preview()"><span><span><b class="btn-icons btn-icons-play"></b>预览</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="quality-announcement-release">
 			<button class='btn' type="button" onclick="release('1');"><span><span><b class="btn-icons btn-icons-ok"></b>发布并置顶</span></span></button>
 			<button class='btn' type="button" onclick="release();"><span><span><b class="btn-icons btn-icons-ok"></b>普通发布</span></span></button>
			</security:authorize>
	<%-- 			</s:if>
 --%>		<%-- <s:if test="id==null">
			<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</s:if> --%>
			
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="basicForm" name="basicForm" method="post" action="">
				<input type="hidden" name="id" id="id" value="${id}"></input>
						<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
								<table class="form-table-border-left">
									<tr><td colspan="6" style="text-align: center;">发布品质公告</td></tr>
									<tr>
										<td class="content-title" style="width:200px">标题：</td>
										<td colspan="6"><input style="width:75%" name="title" id="title" value="${title}" class="{required:true,messages:{required:'标题必填!'}}" style="float: left;"></input>
										</td>
							  		</tr>
									<tr>
										<td class="content-title" style="width:14%;">公告类型：</td>
							  			<td style="width:19%;">
							  			<s:select list="announcementTypes"
											  listKey="value"
											  listValue="name"
											  id="announcementType"
											  name="announcementType"
											  value="%{announcementType}"
											  theme="simple"
											  emptyOption="true"
											></s:select>
<%-- 							  			<input name="announcementType" id="announcementType" value="${announcementType}" class="{required:true,messages:{required:'公告类型必填!'}}" style="float: left;"></input>
 --%>							  		</td>
										<td class="content-title" style="width:14%;">属地：</td>
							  			<td style="width:19%;">
									  			<s:select list="territorials"
													  listKey="value"
													  listValue="name"
													  id="territorial"
													  name="territorial"
													  value="%{territorial}"
													  theme="simple"
													  emptyOption="true"
											></s:select>
										</td>
										<td class="content-title" style="width:14%">内容分类：</td>
							  			<td style="width:19%;">
							  			<s:select list="contentTypes"
													  listKey="value"
													  listValue="name"
													  id="contentClassification"
													  name="contentClassification"
													  value="%{contentClassification}"
													  theme="simple"
													  emptyOption="true"
											></s:select>
<%-- 							  			<input name="contentClassification" id="contentClassification" value="${contentClassification}" class="{required:true,messages:{required:'内容分类必填!'}}"></input>
 --%>							  		
 										</td></tr>
 
							  		<tr>
							  			<td class="content-title" style="width:200px">发布组织：</td>
							  			<td><input name="publishOrganization" id="publishOrganization" value="${publishOrganization}"  class="{required:true,messages:{required:'发布组织必填!'}}"></input></td>
							  			<td class="content-title" style="width:200px">发布人：</td>
							  			<td><input name="publisher" id="publisher" value="${publisher}" onclick="selectObj('选择发布人','publisher','publisher');" class="{required:true,messages:{required:'发布人必填!'}}"></input></td>
							  			<td class="content-title" style="width:200px">发布日期：</td>
							  			<td>
							  			<input id="releaseTime" name="releaseTime"  value="<s:date name='releaseTime' format="yyyy-MM-dd HH:mm"/>" class="{required:true,messages:{required:'发布日期必填!'}}"/>
							  			</td>
									</tr>
									<tr>
										<td class="content-title" style="width:200px">
											<button type="button" class='btn' onclick="uploadFiles('showAttachFile','attachFile');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
										</td>
										<td class="content-title" style="width:200px" colspan="5">
											<input type="hidden" name="hisAttachFile" value='${attachFile}'></input>
											<input type="hidden" name="attachFile" id="attachFile" value='${attachFile}'></input>
											<span id="showAttachFile"></span>
										</td>
									</tr>
									<tr>
										<td class="content-title" style="width:200px;text-align: left;">公告内容：</td>
										<td colspan="5"><textarea style="width:98%;border: 0;" rows="15" name="content" id="content">${content}</textarea></td>
									</tr>
<!-- 									<tr> -->
<!-- 										<td style="width:200px;text-align: left;">封面图片 -->
<!-- 											<button type="button" class='btn' onclick="uploadFiles('showAttachFile1','coverImg',afterUpload);"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button><br/>建议尺寸214*193 -->
<!-- 										</td> -->
<!-- 										<td colspan="5"> -->
<%-- 											<input type="hidden" name="hiscoverImg" value='${coverImg}'></input> --%>
<%-- 											<input type="hidden" name="coverImg" id="coverImg" value='${coverImg}'></input> --%>
<!-- 											<img id="coverImgShow" style="width:214px;height:193px;"></img> -->
<!-- 											<span id="showAttachFile1"></span> -->
<!-- 										</td> -->
<!-- 									</tr> -->
									<tr>
										<td class="content-title" style="width:200px">发送邮件：</td>
										<td colspan="5">
											<input style="float:left;width:40%" name="sendMail" id="sendMail" value="${sendMail}" />
	          	 							<input style="float:left;" type='hidden' name="sendMailLogin" id="sendMailLogin" value="${sendMailLogin}" />
										<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('sendMail','sendMailLogin');"><span class="ui-icon ui-icon-search"  ></span></a>
										</td>
							  		</tr>
								</table>
					</form>
				</div>
			</div>
			</div>
</body>

</html>