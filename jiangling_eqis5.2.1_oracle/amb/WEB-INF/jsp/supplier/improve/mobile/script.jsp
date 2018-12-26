<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var userNameId="",userLoginNameId="";
var selectValue="",selectHiddenValue="";
    $(function(){
    	//添加表单验证
    	_validateForm();
    	var attachments = [
   	   		{showInputId:'showdescFile',hiddenInputId:'descFile'},
   	   	<security:authorize ifAnyGranted="supplier-improve-conceal">
   	   		{showInputId:'showsupplierFile',hiddenInputId:'supplierFile'} 
   	   	</security:authorize>
   	   	 ];
       	 parseDownloadFiles(attachments);   	
		//初始化用户框
		$(':input[type=checkbox]').bind('change',function(){
			selectValue = this.name;
			selectHiddenValue = this.value;
	     }); 
    	$(":input[isUser]").click(function(){
    		if($(this).is(":disabled")){
    			return;
    		}
    		selLoginNameId = $(this).attr("hiddenInputId");
    		selUserNameId = $(this).attr("id");
    		$("#userTree").show();
    	});
    	addFormValidate('${fieldPermission}', 'DefectiveGoodsForm');
    	//图片
    	/* id 绑定 也可以传class*/
    	$img($('#updataBtn'),$('#dataImgBox'));
    	$("#closeId").click(function(){
   		 $("#nk").hide();
   		 $("#zzc").hide();
   		});
    	$("#filecloseId").click(function(){
	   		$("#file").hide();
	   	 	$("#uploadFile").val("");
	   		document.getElementById('uploadFileName').value =  '';
            document.getElementById('fileSize').innerHTML = '';
            document.getElementById('fileType').innerHTML = '';
   		});
    });
    function del(id){
		document.getElementById(id).value="";
	} 
    function showProcessForm(){
   	 $("#processForm").show();
   	 $("#processFormNk").show();
   }
   function setOpinionValue(obj){
   	$("#opinion").val(obj.value);
   }
	//图片上传
	function $img(inputId,picBoxId){
		inputId.change(function(){
			var file = this.files[0];
			var r = new FileReader();
			r.readAsDataURL(file);
			$(r).load(function(){
				picBoxId.html('<img src="'+ this.result +'" alt="" />');
			});
		});
	};
 	//保存
 	function saveForm() {
 		$("input:[name='admit_select']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
 		$("#applicationForm :input[name]").removeAttr("disabled");
 		$("#applicationForm").attr("action","${supplierctx}/improve/save.htm");
 		var validator = $("#applicationForm").data('validator');
 		if(validator){
 			validator.cancelSubmit = true;
 		}
 		$("#applicationForm").submit();
 	}
 	
 	//第一次提交
 	function submitForm(){
 		var res = _beforeSaveFormInfo("SUBMIT");
 		if(res==false){
 			return;
 		}
 		//检查元素是否存在
 		if($('#applicationForm #taskTransact').length==0){
 			$("#applicationForm").append('<input type="hidden" name="taskTransact" id="taskTransact" />');
 		}
 		$('#applicationForm #taskTransact').val("SUBMIT");
 		$("#applicationForm").attr("action",'${supplierctx}/improve/submit-process.htm');
 		$("#applicationForm :input[name]").removeAttr("disabled");
 		$("#applicationForm").submit();
 	}
 	
 	//完成任务
 	function _completeTask(taskTransact) {
 		var res = _beforeSaveFormInfo(taskTransact);
 		if(res==false){
 			return;
 		}
 		/**$.showMessage("正在执行操作,请稍候... ...","custom");
 		$("#opt-content").scrollTop(0);*/
 		//检查元素是否存在
 		if($("#applicationForm #taskTransact").length==0){
 			$("#applicationForm").append('<input type="hidden" name="taskTransact" id="taskTransact" />');
 		}
 		$("#applicationForm #taskTransact").val(taskTransact);
 		$("#applicationForm :input[name]").removeAttr("disabled");
 		$("#applicationForm").attr("action","${supplierctx}/improve/complete-task.htm");
 		var validator = $("#applicationForm").data('validator');
 		if(validator){
 			validator.cancelSubmit = true;
 		}
 		$('#applicationForm').submit();
 	}
 	
 	//保存form
 	function _beforeSaveFormInfo(taskTransact){
 		//检查是否需要验证,默认不同意不验证
 		var needValid = true;
 		if(taskTransact=='REFUSE'){
 			needValid = false;
 		}
		if(taskTransact=='SUBMIT'||taskTransact=='APPROVE'){
			needValid = true;
		} 		
		 if(needValid){
			if(!$("#applicationForm").valid()){
				var error = $("#applicationForm").validate().errorList[0];
				$(error.element).focus();
				return false;
			}
		} 
 	}
	//添加验证
 	function _validateForm(){
 		var fieldPermissionStr = '${fieldPermission}';
 		if(fieldPermissionStr){
 			var fieldPermission =eval(fieldPermissionStr);
 			for(var i=0;i<fieldPermission.length;i++){
 				var obj = fieldPermission[i];
 				if(obj.request=='true'){
 					var inputObj = $(":input[name=" + obj.name + "]");
 					inputObj.addClass("{required:true,messages:{required:'必填!'}}");
 				}else if(obj.readonly=='true'){
 					$(":input[name="+obj.name+"]").attr("disabled","disabled");
					$("#"+obj.name+"a").removeAttr("onclick");
 				};
 				if(obj.controlType=="allReadolny"){
 					$(":input[name]","#applicationForm").attr("disabled","disabled");
 					$(".small-button-bg","#applicationForm").removeAttr("onclick");
 					$("button","#applicationForm").removeAttr("onclick");
 				};
 			};
 		}
 	}
 	function copyPersonCallBack(selectHiddenValue) {
 		$('#copyPerson').attr("value", selectHiddenValue);
 		$("#applicationForm").attr("action", "${supplierctx}/improve/copytask.htm?copyPerson=" + selectHiddenValue);
 		$("#applicationForm").ajaxSubmit(function(id) {
 			alert(id);
 		});
 	}
 	function assignUser(selectHiddenValue){
 		var url ="${supplierctx}/improve/assign.htm?assignee="+selectHiddenValue;
 			$("#applicationForm").attr("action",url); 
 				//name=name.split('|')[0].split('-')[1]; 
 				$("#assignee").attr("value",selectHiddenValue); 
 				$("#applicationForm").ajaxSubmit(function (str){
 					if(str){
 						alert(str);
 					}
 				}); 
 	   } 
	function supplierClick(){
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#supplierName").val(obj.name);
		//联系人
	} 
 	function showdiv(id){
 		var sbtitle=document.getElementById(id);
 		if(sbtitle){
 		   if(sbtitle.style.display=='block'){
 		       sbtitle.style.display='none';
 		   }else{
 		       sbtitle.style.display='block';
 		   }
 	     }
 	}
 	function searchPerson(show,hidden){
 		userNameId=show;userLoginNameId=hidden;
 		 $("#nk").show();
 		 $("#zzc").show();
 	};

 	function del(show,hidden){
 		$("#"+show).val("");
 		$("#"+hidden).val("");
 	} 
 	function searchElement(){
 		var searchTag = $("#searchTag").val();
 		$("#root").find(":input[name='"+searchTag+"']").each(function(index, obj){
 			var parentId = obj.getAttribute("deptname");
 			var sbtitle=document.getElementById(parentId);
 			if(sbtitle){
 			   if(sbtitle.style.display=='block'){
 			   sbtitle.style.display='none';
 			   }else{
 			   sbtitle.style.display='block';
 			   }
 		     }
 			obj.checked=true;
 			obj.focus();
 			selectValue = obj.name;
 			selectHiddenValue = obj.value;
 		});
 	}
 	function setInputValue(){
 		$("#"+userNameId).val(selectValue);
 		$("#"+userLoginNameId).val(selectHiddenValue);
 		 $("#nk").hide();
 		 $("#zzc").hide();
 		 $("#searchTag").val("");
 		 $(':input[type=checkbox]').each(function(index,obj){
 			 obj.checked=false;
 			 var parentId = obj.getAttribute("deptname");
 				var sbtitle=document.getElementById(parentId);
 				if(sbtitle){
 				   if(sbtitle.style.display=='block'){
 				     sbtitle.style.display='none';
 				   }
 			     }
 		 });
 		 if(userNameId=="copyPersonUser"){
 			 copyPersonCallBack(selectHiddenValue);
 		 }
 		 if(userNameId=="assigneeUser"){
 			 assignUser(selectHiddenValue);
 		 }
 	}
		var showFileId = "",hiddenFileId="";
		function _uploadFiles(showInputId,hiddenInputId){
			$("#file").show();
			showFileId=showInputId;hiddenFileId=hiddenInputId;
		}
		function setFileinfo(obj){
			$("#"+showFileId).html('<a style="text-decoration:underline;margin-left:1rem;margin-top:-10rem;" href="${mfgctx}/common/download.htm?id='+obj.fileId+'" title="下载'+obj.fileName+'">'+obj.fileName+'</a>');
			$("#"+hiddenFileId).val(obj.fileId+ "|~|"+obj.fileName);
		}
		function parseDownloadFiles(arrs){
			for(var i=0;i<arrs.length;i++){
				var hiddenInputId = arrs[i].hiddenInputId;
				var showInputId = arrs[i].showInputId;
				var files = $("#"+hiddenInputId).val().length;
				if(files>0){
			 		var fileId =  $("#"+hiddenInputId).val().split("|~|")[0];
			 		var fileName =  $("#"+hiddenInputId).val().split("|~|")[1];
			 		$("#"+showInputId).html('<a style="text-decoration:underline;" href="${mfgctx}/common/download.htm?id='+fileId+'" title="下载'+fileName+'">'+fileName+'</a>');
				}
			}
		}
	   function fileSelected() {
	    var file = document.getElementById('uploadFile').files[0];
	    if (file) {
	      var fileSize = 0;
	      if (file.size > 1024 * 1024)
	        fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
	      else
	        fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
	
	      document.getElementById('uploadFileName').value =  file.name;
	      document.getElementById('fileSize').innerHTML = 'Size: ' + fileSize;
	      document.getElementById('fileType').innerHTML = 'Type: ' + file.type;
	    }
	  }
		
	  function uploadFiles() {
	    var fd = new FormData();
	    fd.append("uploadFile", document.getElementById('uploadFile').files[0]);
	    fd.append("uploadFileName", document.getElementById('uploadFileName').value);
	    var xhr = new XMLHttpRequest();
	    xhr.upload.addEventListener("progress", uploadProgress, false);
	    xhr.addEventListener("load", uploadComplete, false);
	    xhr.addEventListener("error", uploadFailed, false);
	    xhr.addEventListener("abort", uploadCanceled, false);
	    xhr.open("POST", "${mfgctx}/common/upload.htm");
	    xhr.send(fd);
	  }
	
	  function uploadProgress(evt) {
	    if (evt.lengthComputable) {
	      var percentComplete = Math.round(evt.loaded * 100 / evt.total);
	      document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
	    }
	    else {
	      document.getElementById('progressNumber').innerHTML = 'unable to compute';
	    }
	  }
	
	  function uploadComplete(evt) {
	    window.parent.setFileinfo(JSON.parse(evt.target.responseText));
	  }
	
	  function uploadFailed(evt) {
	    alert("There was an error attempting to upload the file.");
	  }
	
	  function uploadCanceled(evt) {
	    alert("The upload has been canceled by the user or the browser dropped the connection.");
	  }
	  function delFile(){
		  $("#form1").reset();
		  }
</script>