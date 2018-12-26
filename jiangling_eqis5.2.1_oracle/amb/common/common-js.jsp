<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>

<script type="text/javascript">
$.ajaxSetup({cache:false });
$.extend($.fn,{
    mask: function(msg,maskDivClass){
    	if(!msg){
    		msg = '数据加载中,请稍候... ...';
    	}
        this.unmask();
        // 参数
        var op = {
            opacity: 0.9,
            z: 10000,
            bgcolor: '#ccc'
        };
        var original=$(document.body);
        var position={top:0,left:0};
                    if(this[0] && this[0]!==window.document){
                        original=this;
                        position=original.position();
                    }
        // 创建一个 Mask 层，追加到对象中
        var maskDiv=$('<div class="maskdivgen">&nbsp;</div>');
        maskDiv.appendTo(original);
        var maskWidth=original.outerWidth();
        if(!maskWidth){
            maskWidth=original.width();
        }
        var maskHeight=original.outerHeight();
        if(!maskHeight){
            maskHeight=original.height();
        }
        maskDiv.css({
            position: 'absolute',
            top: position.top,
            left: position.left,
            'z-index': op.z,
          width: maskWidth,
            height:maskHeight,
            'background-color': op.bgcolor,
            opacity : 0
        });
        if(maskDivClass){
            maskDiv.addClass(maskDivClass);
        }
        var msgDiv=$('<div style="position:absolute;border:#6593cf 1px solid; padding:2px;background:#ccca;"><div style="line-height:24px;border:#a3bad9 1px solid;background:white;padding:2px 10px 2px 10px;">'+msg+'</div></div>');
        msgDiv.appendTo(maskDiv);
        var widthspace=(maskDiv.width()-msgDiv.width());
        var heightspace=(maskDiv.height()-msgDiv.height());
        msgDiv.css({
                    cursor:'wait',
                    top:(heightspace/2-2),
                    left:(widthspace/2-2)
	    });
	    maskDiv.fadeIn('fast', function(){
	        // 淡入淡出效果
	        $(this).fadeTo('slow', op.opacity);
        });
        return maskDiv;
    },
	unmask: function(){
        var original=$(document.body);
            if(this[0] && this[0]!==window.document){
               original=$(this[0]);
        }
        original.find("> div.maskdivgen").fadeOut('slow',0,function(){
            $(this).remove();
        });
    }
});
//上传文件
jQuery.extend({
	noData: {  
        "embed": true,  
        "object": true,  
        "applet": true  
    }
});
$.extend({
	/**
	 *  添加天数
	 *  date : 原始日期,日期格式
	 *  addDays : 增加的天数,数字,可为负数
	 * */
	addDays : function(date,addDays){
		if(!date||!date.getTime){
			alert("调用添加天数函数时,date必须是日期格式!");
			return;
		}
		if(isNaN(addDays)){
			alert("调用添加天数函数时,addDays必须是数字格式!");
			return;
		}
		var times = date.getTime() + parseFloat(addDays)* 24 * 60 * 60 * 1000;
		return new Date(times);
	},
	/**
	 *  格式化时间格式字符串
	 *  dateStr : 要格式化的时间格式字符串
	 * */
	parseDateStr : function(dateStr){
	   var isoExp = /^s*(\d{4})-(\d\d)-(\d\d)s*$/,
       date = new Date(NaN), month,
       parts = isoExp.exec(dateStr);
	   if(parts) {
	     month = +parts[2];
	     date.setFullYear(parts[1], month - 1, parts[3]);
	     if(month != date.getMonth() + 1) {
	       date.setTime(NaN);
	     }
	   }
	   return date;
	},
	/**
	 *  格式化时间
	 *  dateTime : 要格式化的时间
	 * */
	formatDate : function(dateTime){
		var str = dateTime.getFullYear() + "-";
		var m = dateTime.getMonth()+1;
		if(m<10){
			str += "0";
		}
		str += m + "-";
		var day = dateTime.getDate();
		if(day < 10){
			str += "0";
		}
		str += day;
		return str;
	},
	showMessage:function(message,flag){
		if($._custom_timeout){
			clearTimeout($._custom_timeout);
		}
		$("#message").html(message).show();
		if(flag != 'custom'){
			$._custom_timeout = setTimeout($.clearMessage,flag?flag:3000);
		}
	},
	clearMessage:function(timeout){
		if(timeout&&!isNaN(timeout)){
			$._custom_timeout = setTimeout(function(){$.clearMessage();},timeout);
			return;
		}
		if($._custom_timeout){
			clearTimeout($._custom_timeout);
			$._custom_timeout = null;
		}
		$("#message").hide();
	},
	getFiles : function(val){
		var files = [];
		if(val){
			if(val.indexOf("[")==0){
				files = eval("(" + val + ")");
			}else{
				var ids = val.split("s~s");
				for(var i=0;i<ids.length;i++){
					var file = ids[i];
					if(file){
						var strs = file.split("|~|");
						if(strs.length>1){
							files.push({
								id : strs[0],
								fileName : strs[1],
								createdTime : strs[2],
								canDelete : strs[3],
							});
						}
					}
				}
			}
		}
		return files;
	},
	getDownloadHtml: function(val){
		var html = '';
		var files = $.getFiles(val);
		for(var i=0;i<files.length;i++){
			var file = files[i];
			if(html){
				html += "<br>";
			}
			html += file.createdTime+'&nbsp;上传&nbsp;'+'<a style="text-decoration:underline;" href="'+$.getDownloadPath(file.id)+'" title="下载'+file.fileName+'">'+file.fileName+'</a>';
		}
		return html;
	},
	parseDownloadPath : function(params){//格式化下载文件的显示方法
		params = params || {};
		var hiddenInputId = params.hiddenInputId,showInputId = params.showInputId;
		if(hiddenInputId&&showInputId){
			var $showInput = $("#" + showInputId).html('');
			if($showInput.length==1){
				var html = $.getDownloadHtml($("#" + hiddenInputId).val());
				$showInput.append(html);
			}
		}
	},
	getDownloadPath : function(fileId){ //获取下载的地址
		return webRoot + "/carmfg/common/download.htm?id=" + fileId;
	},
	upload:function(params){
		params = params || {};
		var height = params.height?params.height:200;
		var width = params.width?params.width:300;
		var windowId = 'fileManagerWindow';
		if($("#" + windowId).length==0){
			var html = '<div style="border:0px;padding:3px;overflow-y:auto;" id="'+windowId+'" title="'+(params.title?params.title:"上传文件")+'">'
				+ '<a style="position:absolute;display:none;" class="info small-button-bg" href="#" title="修改文件名称"><span class="ui-icon ui-icon-comment" style="cursor:pointer;"></span></a>'
				+ '<div class="edit" style="background:#E8F2FE;position:absolute;display:none;padding-left:2px;"><input style="float:left;"></inut><a style="float:left;margin-left:2px;" title="保存文件名称" class="small-button-bg" href="#"><span class="ui-icon ui-icon-disk" style="cursor:pointer;"></span></a></div>'
				+ '<div id="fileProgressbar" style="height:10px;position:absolute;left:0px;top:0px;width:120px;display:none;"></div>'
				+ '<table class="body" style="padding;0px;margin:0px;width:100%;" cellspacing=0 cellpadding=0></table>'
				+ '</div>';
			$(params.appendTo?params.appendTo:"#header").append(html);
		}
		$("#" + windowId).ready(function(){
			$("#" + windowId).dialog({
				width : width,
				height : height,
				minWidth : params.minWidth?params.minWidth:300,
				minHeight: params.minHeight?params.minHeight:height,
				modal : true,
				draggable : true,
				close : function(){
					if($.swfu){
						$.swfu.destroy();
					}
					$("#fileProgressbar").hide().progressbar('destroy');
					$("#" + windowId + " .info").hide();
					$("#" + windowId + " .edit").hide();
				},
				resize : function(event,ui){
					var display = $("#" + windowId + " .edit").css("display");
					if(display == 'block'){
						var trId = $("#" + windowId + " .edit").find("input").attr("targetTrId");
						var operateTd = $("#" + trId + " td:first");
						var p = operateTd.position();
						$("#"+windowId + " .edit")
					  	.css("left",p.left + "px")
					  	.css("top",p.top + "px")
					  	.height(operateTd.height()-3)
					  	.width(operateTd.width())
					  	.css("padding-top",((operateTd.height()-23)/2) + "px")
					  	.find("input")
					  	.width(operateTd.width()-30)
					  	.focus();
					}
				},
				buttons : [{
					text : '全部上传',
					click : function(){
						$("#" + windowId + " .edit").hide();
						isAllUpload = true;
						uploadFirstFile();
					}
				},{
					text : '确定',
					click : function(){
						$("#" + windowId + " .edit").hide();
						var files = [];
						var fileStrs = '';
						$("#" + windowId).find("table tr").each(function(index,obj){
							var id = $(obj).find("input[name=id]").val();
							var fileName = $(obj).find("input[name=fileName]").val();
							var createdTime = $(obj).find("input[name=createdTime]").val();
							var canDelete = $(obj).find("input[name=canDelete]").val();
							if(id){
								var file = {
									id : id,
									fileName : fileName,
									createdTime : createdTime,
									canDelete : canDelete,
								};
								files.push(file);
								if(fileStrs){
									fileStrs += "s~s";
								}
								fileStrs += file.id + '|~|' + file.fileName + '|~|' + file.createdTime + '|~|' + file.canDelete;
							}
						});
						if(params.hiddenInputId){
							$("#" + params.hiddenInputId).val(fileStrs);
							$.parseDownloadPath(params);
						}
						if($.isFunction(params.callback)){
							params.callback(files);
						}
						$("#" + windowId).dialog("close");
					}
				}]
			});
			//初始化
			init();
		});
		var isAllUpload = false;//是否全部上传
		//初始化方法
		function init(){
			$("#"+windowId).height(height-61);
			//处理样式出错
			var header$ = $("#" + windowId).parent().find(".ui-widget-header")
			.height(24)
			.css("padding-left","4px")
			.css("padding-top","7px")
			.css("border-top","0px")
			.css("border-left","0px")
			.css("border-right","0px");
			$("#" + windowId).parent().find(".ui-dialog-titlebar-close")
				.css("float","right")
				.css("margin-right","4px");
			var bottonPanel = $("#" + windowId).parent().find(".ui-dialog-buttonset")
				.css("height","35px")
				.css("padding-top","5px")
				.css("text-align","center");
			bottonPanel.parent().css("border-left","0px")
			.css("border-right","0px")
			.css("border-bottom","0px");
			bottonPanel.prepend("<span id=\"spanButtonPlaceHolder\"></span>");
			bottonPanel.find(".ui-button")
				.css("height","24px")
				.css("margin-top","2px")
				.css("margin-left","4px");
			bottonPanel.find(".ui-button-text").css({
				"padding-top":"0px",
				"padding-bottom":"0px"
			});
			var JSESSIONID= '';
			try {
				var strs = document.cookie?document.cookie.split(";"):[];
				for(var i=0;i<strs.length;i++){
					var str = strs[i];
					if(str&&str.indexOf('JSESSIONID=')==0){
						JSESSIONID = str.split("=")[1];
						break;
					}
				}
			} catch (e) {}
			var uploadUrl = webRoot + "/carmfg/common/upload.htm";
			if(JSESSIONID){
				uploadUrl += "?JSESSIONID=" + JSESSIONID;
			}
			var settings = {
				flash_url : webRoot + "/widgets/swfupload/swfupload.swf",
				upload_url: uploadUrl,
				file_post_name : 'uploadFile',
				post_params: {},
				file_size_limit : params.file_size_limie?params.file_size_limie:"100 MB",
				file_types : params.file_types?params.file_types:"*.*",
				file_types_description : params.file_types_description?params.file_types_description:"All Files",
				file_upload_limit : 100,
				file_queue_limit : 0,
				debug: false,
				// Button settings
				button_image_url: webRoot + "/images/selectFile.jpg",
				button_width: "83",
				button_height: "26",
				button_placeholder_id: "spanButtonPlaceHolder",
				
				file_queued_handler : function(fileObj){
					appendFiles([{uploadId:fileObj.id,id:'',fileName:fileObj.name,createdTime:formatDate(new Date()),canDelete:true}]);
				},
				upload_progress_handler : function(fileObj,completeBytes,totalBytes){
					var rate = completeBytes/totalBytes * 100.0;
					$("#fileProgressbar")
					.progressbar('value',parseInt(rate));
				},
				upload_error_handler : function(fileObj,code,message){
					$("#fileProgressbar").hide();
					alert(message);
				},
				upload_success_handler : function(fileObj,serverData){
					var result = eval('(' + serverData + ')');
					var trId = $("#fileProgressbar")
						.hide()
						.attr("targetTrId");
					if(result.error){
						alert(result.message);
						$("#"+trId).remove();
						addPromptInfo();
					}else{
						$("#"+trId).find("td:last")
						   .find("a:first").replaceWith('<a href="'+$.getDownloadPath(result.fileId)+'" title="下载'+result.fileName+'">下载</a>');
						$("#"+trId).find(":input[name=uploadId]").val('');
						$("#"+trId).find(":input[name=id]").val(result.fileId);
					}
					//可用按钮
					$("#" + windowId).find("table").attr("disabled","");
					$("#" + windowId).parent().find("button").attr("disabled","");
					if(isAllUpload){
						uploadFirstFile();
					}
				}
			};
			//初始化进度条
			$("#fileProgressbar").progressbar({});
			$("#fileProgressbar").find("div")
				.height($("#fileProgressbar").height())
				//.css("background","blue")
				.css("border-left","0px")
				.css("border-top","0px")
				.css("border-bottom","0px");
			//可用按钮
			$("#" + windowId).find("table").attr("disabled","");
			$("#" + windowId).parent().find("button").attr("disabled","");
			//初始化原来的文件
			$("#" + windowId).find(".body").html('');
			var files = [];
			if(params.hiddenInputId){
				var hisValue = $("#" + params.hiddenInputId).val();
				files = $.getFiles(hisValue);
			}
			appendFiles(files);
			//创建上传控件
			if(window.applicationCache){
				$.swfu = $('#spanButtonPlaceHolder').Huploadify({
					fileTypeExts:'*.*',//允许上传的文件类型，格式'*.jpg;*.doc'
					uploader:uploadUrl,//文件提交的地址
					auto:false,//是否开启自动上传
					method:'post',//发送请求的方式，get或post
					multiple:true,//是否允许选择多个文件
					formData:null,//发送给服务端的参数，格式：{key1:value1,key2:value2}
					fileObjName:'uploadFile',//在后端接受文件的参数名称，如PHP中的$_FILES['file']
					fileSizeLimit:1048576,//允许上传的文件大小，单位KB
					showUploadedPercent:true,//是否实时显示上传的百分比，如20%
					showUploadedSize:true,//是否实时显示已上传的文件大小，如1M/2M
					buttonText:'选择文件',//上传按钮上的文字
					removeTimeout: null,//上传完成后进度条的消失时间，单位毫秒
					//itemTemplate:'',//上传队列显示的模板
					onUploadStart:function(file,completeBytes,totalBytes){
						console.log(file.name+'开始上传');
						var rate = completeBytes/totalBytes * 100.0;
						$("#fileProgressbar").progressbar('value',parseInt(rate));
					},//上传开始时的动作
					onUploadSuccess:function(file,serverData){
						console.log(file.name+'上传成功');
						var result = eval('(' + serverData + ')');
						var trId = $("#fileProgressbar").hide().attr("targetTrId");
						if(result.error){
							alert(result.message);
							$("#"+trId).remove();
							addPromptInfo();
						}else{
							$("#"+trId).find("td:last").find("a:first").replaceWith('<a href="'+$.getDownloadPath(result.fileId)+'" title="下载'+result.fileName+'">下载</a>');
							$("#"+trId).find(":input[name=uploadId]").val('');
							$("#"+trId).find(":input[name=id]").val(result.fileId);
							if("iqcInspectionDatas"==params.showInputId){
								for (var key in result) { 
									if(key!='fileId'&&key!="fileName"){
										var inspectionDatas = result[key];
										var datasArr = inspectionDatas.split(',');
										for(var m=1;m<=datasArr.length;m++){
											if(datasArr.length<=2&&isNaN(datasArr[0])){
												if(datasArr.length==2){
													$("#checkItemsParent td[itemname="+key+"]").find(":input[fieldname=results]").val(datasArr[m-2]);
													$("#checkItemsParent td[itemname="+key+"]").find(":input[fieldname=equipmentNo]").val(datasArr[m-1]);
												}else{
													$("#checkItemsParent td[itemname="+key+"]").find(":input[fieldname=results]").val(datasArr[m-1]);
												}
											}else{
												if(m==datasArr.length&&isNaN(datasArr[m-1])){
													 //最后一个格子不包含合格就是机台
													 $("#checkItemsParent td[itemname="+key+"]").find(":input[fieldname=equipmentNo]").val(datasArr[m-1]); 
												 }else{
													 var dataStr = datasArr[m-1];
													 if(dataStr.indexOf("E")>0){
														 dataStr = new Number(dataStr);
													 }
													 $("#checkItemsParent td[itemname="+key+"]").find(":input[fieldname=result"+m+"]").val(dataStr); 
													 resultChangeSelf($("td[itemname="+key+"]").find(":input[fieldname=result"+m+"]"),dataStr);
												 }
											}
										}
									}
						        } 
							}
						}
						//可用按钮
						$("#" + windowId).find("table").attr("disabled","");
						$("#" + windowId).parent().find("button").attr("disabled","");
						if(isAllUpload){
							uploadFirstFile();
						}
					},//上传成功的动作
					onUploadComplete:function(file){
						console.log(file.name+'上传完成');
					},//上传完成的动作
					onUploadError:function(file,code,message){
						console.log(file.name+'上传失败');
						$("#fileProgressbar").hide();
						alert(message);
					}, //上传失败的动作
					onInit:function(obj){
						console.log('初始化');
						console.log(obj);
					},//初始化时的动作
					onCancel:function(file){
						console.log(file.name+'删除成功');
					},//删除掉某个文件后的回调函数，可传入参数file
					onClearQueue:function(queueItemCount){
						console.log('有'+queueItemCount+'个文件被删除了');
					},//清空上传队列后的回调函数，在调用cancel并传入参数*时触发
					onDestroy:function(){
						console.log('destroyed!');
					},//在调用destroy方法时触发
					onSelect:function(file){
						console.log(file.name+'加入上传队列');
						appendFiles([{uploadId:file.index,id:'',fileName:file.name}]);
					},//选择文件后的回调函数，可传入参数file
					onQueueComplete:function(queueData){
						console.log('队列中的文件全部上传完成',queueData);
					}//队列中的所有文件上传完成后触发
				});
			}else{
				if(typeof(SWFUpload) == 'undefined'){
					$.getScript(webRoot + '/widgets/swfupload/swfupload.js',function(){
						$.swfu = new SWFUpload(settings);
					});
				}else{
					$.swfu = new SWFUpload(settings);
				}
			}
			//编辑名称的事件
			$("#" + windowId + " .info").click(function(){
				editTr($("#" + $(this).attr("targetTrId")));
			});
			$("#" + windowId + " .edit").find("input")
			.keyup(function(event){
				if(event.keyCode==13){
					saveFileName(this);
				}
			});
			//保存的事件
			$("#" + windowId + " .edit").find("a:first")
			.click(function(){
				saveFileName($("#" + windowId + " .edit").find("input")[0]);
			});
		}
		//编辑
		function editTr(tr$){
			var fileName = tr$.find("input[name=fileName]").val();
			var operateTd = tr$.find("td:first");
			var p = operateTd.position();
			$("#"+windowId + " .edit")
		  	.css("left",p.left + "px")
		  	.css("top",p.top + "px")
		  	.height(operateTd.height()-3)
		  	.width(operateTd.width())
		  	.css("padding-top",((operateTd.height()-23)/2) + "px")
		  	.show()
		  	.find("input")
		  	.width(operateTd.width()-30)
		  	.val(fileName)
		  	.attr("hisValue",fileName)
		  	.attr("targetTrId",tr$.attr("id"))
		  	.focus();
		}
		//添加文件
		function appendFiles(files){
			$("#" + windowId).find(".body .prompt").remove();
			files = files || [];
			for(var i=0;i<files.length;i++){
				var file = files[i];
				var trId = "file_" + (file.uploadId?file.uploadId:file.id);
				$("#" + windowId).find(".body").append('<tr height=29 id="'+trId+'">'
						+ '<td style="padding:0px 0px 0px 4px;margin:0px;border-bottom:1px dotted gray;">'
					    + '<span>'+file.fileName+'</span><input type="hidden" name="fileName" value="'+file.fileName+'"></input>'
						+ '<input type="hidden" name="uploadId" value="'+(file.uploadId?file.uploadId:'')+'"></input>'
						+ '<input type="hidden" name="id" value="'+(file.id?file.id:'')+'"></input>'
						+ '<input type="hidden" name="createdTime" value="'+(file.createdTime?file.createdTime:formatDate(new Date()))+'"></input>'
						+ '<input type="hidden" name="canDelete" value="'+(file.canDelete?file.canDelete:false)+'"></input>'
						+ '</td>'
						+ '<td style="width:70px;border-bottom:1px dotted gray;">'
						+ (file.uploadId?'<a style="color:red;" href="#" uploadId="'+file.uploadId+'" class="upload" title="上传'+file.fileName+'">上传</a>':'<a href="'+$.getDownloadPath(file.id)+'" title="上传'+file.fileName+'">下载</a>')
					    <security:authorize ifAnyGranted="amb_common_common-js_clear">
					    + '<a href="#" id="'+file.id+'" class="anyClear" title="清除【'+file.fileName+'】">清除</a>'
					    </security:authorize>
					    <security:authorize ifNotGranted="amb_common_common-js_clear">
					    + (file.canDelete==true||file.canDelete=='true'?'<a href="#" id="'+file.id+'" class="notClear" title="清除【'+file.fileName+'】">清除</a>':'')
					    </security:authorize>
					    +'</td>'
						+ '</tr>');
				var tr$ = $("#" + windowId).find(".body").find("tr:last")
				.mouseover(function(){
					$(this).css("background","#E8F2FE");
				}).mouseout(function(){
					$(this).css("background","");	
				});
				//编辑提示图标
				tr$.find("td:first")
				   .mouseover(function(){
					  clearTimeout(window.infoTimeout);
					  var p = $(this).position();
					  $("#"+windowId + " .info")
					  	.css("left",(p.left + $(this).width() - 20) + "px")
					  	.css("top",(p.top + ($(this).height()-20)/2) + "px")
					  	.attr("targetTrId",$(this).parent().attr("id"))
					  	.show();
				   }).mouseout(function(){
					   window.infoTimeout = setTimeout(function(){
						   $("#"+windowId + " .info").hide();
					   }, 1000);
				   }).dblclick(function(){
					   editTr($(this).parent());
				   });
				//绑定操作
				var operateTd = tr$.find("td:last");
				//可删除硬盘或数据库中的文件
				operateTd.find("a.anyClear").click(function(){
					$("#" + windowId + " .edit").hide();
					$(this).parent().parent().remove();
					addPromptInfo();
					//删除上传文件
					var id = this.id;
					var title = this.title;
					var params ={id:id,};
					$.post(webRoot + "/carmfg/common/delete-file.htm",params,function(result){
						if(result.error){
							alert(title+"失败！文件不存在！");
						}
					},'json');
				});
				//不可删除硬盘或数据库中的文件
				operateTd.find("a.notClear").click(function(){
					$("#" + windowId + " .edit").hide();
					$(this).parent().parent().remove();
					addPromptInfo();
				});
				//上传
				operateTd.find("a.upload").click(function(){
					$("#" + windowId + " .edit").hide();
					isAllUpload = false;
					uploadFile($(this).parent().parent());
				});
			}
			addPromptInfo();
			if($("#" + windowId + " table").tableDnD){
				$("#" + windowId + " table").tableDnD();
			};
		}
		
		function addPromptInfo(){
			if($("#" + windowId).find(".body tr").length==0){
				$("#" + windowId).find(".body").append('<tr height=29 class="prompt"><td style="padding-left:4px;">请选择文件... ...</td></tr>');
			}
		}
		//上传一个文件
		function uploadFile(tr$){
			$("#fileProgressbar").show().progressbar('value',[0]);
			var p = tr$.position();
			$("#fileProgressbar").css("top",(p.top+14) + "px").css("left",p.left + "px").width(tr$.width());
			$("#fileProgressbar").attr("targetTrId",tr$.attr("id"));
			$.swfu.setPostParams({
				uploadFileName : tr$.find(":input[name=fileName]").val(),
			});
			$.swfu.startUpload(tr$.find("input[name=uploadId]").val());
			//禁用按钮
			$("#" + windowId).find("table").attr("disabled",true);
			$("#" + windowId).parent().find("button").attr("disabled",true);
		}
		//上传第一个文件
		function uploadFirstFile(){
			$("#" + windowId).find(".body tr").each(function(index,obj){
				var val = $(obj).find(":input[name=uploadId]").val();
				if(val){
					uploadFile($(obj));
					return false;
				};
			});
		}
		
		//保存文件名
		function saveFileName(obj){
			var value = obj.value;
			if(!value){
				alert("文件名不能为空!");
			}else{
				$(obj).parent().hide();
				var hisValue = $(obj).attr("hisValue");
				if(hisValue != value){
					var tr$ = $("#" + $(obj).attr("targetTrId"));
					var id = tr$.find("input[name=id]").val();
					if(id){//已上传的文件
						tr$.find("td:first span").css("font-size","12px").html("正在保存新文件名,请稍候...");
						$.post(webRoot + "/carmfg/common/update-file-name.htm",{id:id,uploadFileName:value,},function(result){
							if(result.error){
								alert("更新【"+value+"】失败！文件不存在！");
								tr$.find("td:first span").html(hisValue).css("font-size","14px");
							}else{
								tr$.find("input[name=fileName]").val(value);
								tr$.find("td:first span").html(value).css("font-size","14px");
							};
						},'json');
					}else{//未上传的文件
						tr$.find("input[name=fileName]").val(value);
						tr$.find("td:first span").html(value);
					};
				};
			};
		};
	}
});
function contentResize(){
	changePosition();
	var ids =[];
	$('table.ui-jqgrid-btable').each(function(){
		if(this.id != undefined){
			ids.push(this.id);
		}
	});
	for(var i=0;i<ids.length;i++){
		jQuery("#"+ids[i]).jqGrid('setGridHeight',_getTableHeight(ids));
		jQuery("#"+ids[i]).jqGrid('setGridWidth',_getTableWidth());
		if($("#parameter_Table").height()>0){//固定查询
			$('#gbox_'+ids[i]).css('top',$('#parameter_Table').height()+5);
		}else if($("#advanced_search_table_id").height()){//高级查询
			$('#gbox_'+ids[i]).css('top',205);
		}else{
			$('#gbox_'+ids[i]).css('top','');
		};
	};
}

function _getTableHeight(ids){
	if($("#parameter_Table").height()>0){//固定查询
		heightOfSearchDiv=$('#parameter_Table').height();
	}else if($("#advanced_search_table_id").height()){//高级查询
		heightOfSearchDiv=205;
	}else{//查询中点确定按钮(移除)
		heightOfSearchDiv=0;
	}
	var h=0;
	if(ids.length>1){
		h=$('.ui-layout-center').height()-150-heightOfSearchDiv;
	}else{
		h=$('.ui-layout-center').height()-120-heightOfSearchDiv;
	}
	var tableHeight=h/ids.length;
	return tableHeight;
}

function _getTableWidth(){
	var w = $('.ui-layout-center').width()-30;
	if($.browser.msie){
		var obj=document.getElementById("ui-layout-center"); 
		if(obj!=null&&typeof(obj)!='undefined'){
			if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
				w = $('.ui-layout-center').width()-45;
			};
		};
	}
	return w;
}

jQuery.extend($.jgrid.defaults,{
	datatype: "json",
	jsonReader:{
		repeatitems:false
	},
	rowNum: 20, 
	prmNames:{
		rows:'page.pageSize',
		page:'page.pageNo',
		sort:'page.orderBy',
		order:'page.order'
	},
//	autowidth: true,
	//height: 330,
	gridComplete:contentResize,
	pager: '#pager', 
	viewrecords: true, 
	sortorder: "desc",
	shrinkToFit:false,
	multiselect: true,
	mtype:"POST"
});

function toggleSearchDiv(){
	$("#searchDiv").toggle();
}

function progressbar(title,time){
	var windowId = "window" + (new Date()).getTime();
	var progressbarId = "progress" + (new Date()).getTime();
	var backId = "back" + (new Date()).getTime();
	var html = '<div style="border:0px;padding:3px;overflow-y:auto;" id="'+windowId+'" title="'+title+'">'
		+ '<div id="'+backId+'" style="position:absolute;left:10px;top:40px;width:380px;height:19px;border:1px solid #175787;"></div>'
		+ '<div id="'+progressbarId+'" style="position:absolute;left:10px;top:40px;width:380px;height:20px;display:none;background:#175787;"></div>'
		+ '</div>';
	$(".ui-layout-center").append(html);
	var t = null;
	$("#" + windowId).dialog({
		width : 400,
		height : 60,
		resizable:false,
		modal : false,
		draggable : true,
		close : function(){
			$("#" + windowId)
			.remove();
			clearInterval(t);
		}
	});
	var start = (new Date()).getTime();
	t = setInterval(function(){
		var sec = ((new Date()).getTime() - start)/1000;
		var width = parseInt(sec/time * 380)%380;
		$("#" + progressbarId).show().width(width);
	},1000);
	//处理样式出错
	$("#" + windowId).parent().find(".ui-widget-header")
	.height(24)
	.css("padding-left","4px")
	.css("padding-top","7px")
	.css("border-top","0px")
	.css("border-left","0px")
	.css("border-right","0px");
	$("#" + windowId).parent().find(".ui-dialog-titlebar-close")
		.css("float","right")
		.css("margin-right","4px");
	var bottonPanel = $("#" + windowId).parent().find(".ui-dialog-buttonset")
		.css("height","35px")
		.css("padding-top","5px")
		.css("text-align","center");
	bottonPanel.parent().css("border-left","0px")
	.css("border-right","0px")
	.css("border-bottom","0px");
	return $("#" + windowId);
}

function attachmentFormatter(cellValue, options, rowObject){
	return (cellValue=='yes')?"<a href='#'><span class='ui-icon ui-icon-document'></span></a>":"";
}

function dateFormatter(cellValue,options,rowObject){
	
	return (cellValue==null)?"":formatDate(new Date(cellValue.time));
}

function dateFormatter(cellValue,options,rowObject){
	
	return (cellValue==null)?"":formatDate(new Date(cellValue.time),'date');
}

function timeFormatter(cellValue,options,rowObject){
	
	return (cellValue==null)?"":formatDate(new Date(cellValue.time),'date-time');
}
/*---------------------------------------------------------
函数名称:formatDate
参          数:无
功          能:格式化列表中的时间
------------------------------------------------------------*/
function formatDate(datetime,options){
	var year = datetime.getFullYear();
	var month = datetime.getMonth()+1;
	var date = datetime.getDate();
	var hours = datetime.getHours();
	var minutes= datetime.getMinutes();
	var seconds= datetime.getSeconds();
	
	if(month<10){
		month="0"+month;
	}
	if(date<10){
		date="0"+date;
	}
	if(hours<10){
		hours="0"+hours;
	}
	if(minutes<10){
		minutes="0"+minutes;
	}
	if(seconds<10){
		seconds="0"+seconds;
	}
	
	if(options=='date'){
		return year+"-"+month+"-"+date;
	}else if(options=='datetime'){
		return year+"-"+month+"-"+date+" "+hours+":"+minutes+":"+seconds;
	}else{
		return year+"-"+month+"-"+date+" "+hours+":"+minutes;
	};
}
/**
* jQuery Cookie plugin
*
* Copyright (c) 2010 Klaus Hartl (stilbuero.de)
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
*
*/
jQuery.cookie = function (key, value, options) {

    // key and at least value given, set cookie...
    if (arguments.length > 1 && String(value) !== "[object Object]") {
        options = jQuery.extend({}, options);

        if (value === null || value === undefined) {
            options.expires = -1;
        }

        if (typeof options.expires === 'number') {
            var days = options.expires, t = options.expires = new Date();
            t.setDate(t.getDate() + days);
        }

        value = String(value);

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? value : encodeURIComponent(value),
            options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''));
    }

    // key and possibly options given, get cookie...
    options = value || {};
    var result, decode = options.raw ? function (s) { return s; } : decodeURIComponent;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

function loadTheme(){
	// $.cookie('demo-theme-cookie',null, { expires: 7, path: '/' });
	var theme = $.cookie('demo-theme-cookie');
	if (theme != null) {
		var style = '../../css/' + theme + '/jquery-ui-1.8.16.custom.css';
		$('#_style').attr('href', style);
	};
}
loadTheme();
/*---------------------------------------------------------
函数名称:ajaxAnyWhere
参          数:
		*@param form 表单id （必须给的参数）
		* @param url 请求url （如果给空串，将使用Form的action属性上的url）
		* @param zoons 替换的区域名，多个区域之间以逗号隔开 （必须参数）
		* @param ajaxCallback  回调方法名 （可选）
		* @param arg1 回调函数的参数1 （可选）
		* @param arg2 回调函数的参数2 （可选）
		* @param arg3 回调函数的参数3 （可选）
功          能:提交表单的公共方法
------------------------------------------------------------*/
function ajaxAnyWhereSubmit(form, url, zoons, ajaxCallback,arg1,arg2,arg3){
	var formId = "#"+form;
	if(url != ""){
		$(formId).attr("action", url);
	}
	ajaxAnywhere.formName = form;
	ajaxAnywhere.getZonesToReload = function() {
		return zoons;
	};
	ajaxAnywhere.handleException = function(type, details) {
		var div = "<TABLE height=\"100%\" cellSpacing=0 cellPadding=0 width=\"100%\" align=\"center\" border=\"0\">" +
					"<TR>" +
						"<TD vAlign=\"center\" align=\"middle\">" +
						"错误类型：" + type +
						"<br/>错误信息：" + details +
						"</td>" +
					"</tr>" +
				"</table>";
		init_tb('TB_inline?height=300&width=600&modal=false',' 错误提示');
		$("#TB_ajaxContent").html(div);
		
	};
	ajaxAnywhere.handleHttpErrorCode = function(code){
		if(code==403){
			init_tb(webRoot+'/common/thickbox_403.jsp?TB_iframe=true&width=600&height=300','没有权限');
		}else{
			init_tb('TB_inline?height=300&width=600&modal=false',' 错误提示');
			$("#TB_ajaxContent").html("http Error code:"+code);
		}
	};
	ajaxAnywhere.onBeforeResponseProcessing = function(){
		//response被处理前
	};
	ajaxAnywhere.onAfterResponseProcessing = function () {
		if(typeof(ajaxCallback) == "function"){
			if(arg3!=""||typeof(arg3)!="undefined"){
				ajaxCallback(arg1,arg2,arg3);
			}else if(arg2!=""||typeof(arg2)!="undefined"){
				ajaxCallback(arg1,arg2);
			}else if(arg1!=""||typeof(arg1)!="undefined"){
				ajaxCallback(arg1);
			}else{
				ajaxCallback();
			}
		}
	};
	ajaxAnywhere.submitAJAX();
}
var aa={
	submit:	ajaxAnyWhereSubmit,
};

/*---------------------------------------------------------
函数名称:showMsg
参          数:id 显示信息的元素的id
功          能:显示提示信息，3秒后隐藏
------------------------------------------------------------*/
function showMsg(id){
	if(id==undefined)id="message";
	$("#"+id).show();
	setTimeout('$("#'+id+'").hide();',3000);
}
/*---------------------------------------------------------
函数名称:_formate_date
参          数:无
功          能:格式化列表中的时间
------------------------------------------------------------*/
function _formate_date(date){
	var minute=date.getMinutes();
	var hour=date.getHours();
	if(minute<10){
		minute="0"+minute;
	}
	if(hour<10){
		hour="0"+hour;
	}
	return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+hour+":"+minute ; 
}
//流程状态显示
function stageFormatter(value,options, rowObject){
	var launchState = rowObject.launchState;
	if(launchState){
		var colName = options.colModel.name;
		if(launchState.indexOf(colName)>-1){
			var reg = new RegExp(colName + "$");
			if(reg.test(launchState)){
				return "<div style='text-align:center;margin-left:-10px;color:green;'>办理中...</div>";
			}else{
				return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/green.gif'/></div>";					
			};
		}else{
			return '';
		};
	}else{
		return "";
	};
}
/*---------------------------------------------------------
函数名称:customSave
参          数:
	gridId	表格的ID
功          能:自定义保存方法
------------------------------------------------------------*/
function customSave(gridId){
	if(lastsel==undefined||lastsel==null){
		alert("当前没有可编辑的行!");
		return;
	};
	var $grid = $("#" + gridId);
	var o = getGridSaveParams(gridId);
	if ($.isFunction(gridBeforeSaveFunc)) {
		gridBeforeSaveFunc.call($grid);
	}
	$grid.jqGrid("saveRow",lastsel,o);
}
/*---------------------------------------------------------
函数名称:enterKeyToNext
参          数:
	gridId	表格的ID
	rowId	行的ID
功          能:更改回车事件为切换到下一个单元格
------------------------------------------------------------*/
var gridBeforeSaveFunc = null;
function enterKeyToNext(gridId,rowId,beforeSaveFunc,notSetLeft){
	var $grid = $("#" + gridId);
	//进入编辑状态时，控件的padding设为1px，让控件靠左顶格
	setGridCellPadding(rowId,notSetLeft,"1px");
	gridBeforeSaveFunc = beforeSaveFunc;
	var o = getGridSaveParams(gridId);
	$("#" + rowId).unbind("keydown").bind("keyup",function(e){
		if (e.keyCode === 27) {
			$grid.jqGrid("restoreRow",rowId,o.afterrestorefunc);
			//取消编辑状态时，文字的padding设为15px，不会顶格
			setGridCellPadding(rowId,notSetLeft,"15px");
		//回车键和 → 下一个
		}else if(e.keyCode == 13 || e.keyCode == 39 ){
			if($(e.target).is(":input")){
				//判断是否下拉框
				var $input = $(e.target);
				var autocomplete = $input.attr("autocomplete");
				if(autocomplete){
					var self = $input.data("self");
					if(self&&self.menu){
						var isHidden = $(self.menu.element).is(":hidden");
						//如果下拉菜单显示时,默认显示第一条
						if(!isHidden){
							self._move("next",e);
							$(self.menu.element).menu("select",e);
						};
					};
				}
				var $nextInput = _getNextEditInput($(e.target).closest("td[role=gridcell]"));
				if($nextInput==null){
					if ($.isFunction(beforeSaveFunc)) {
						beforeSaveFunc.call($grid);
					}
					$grid.jqGrid("saveRow",rowId,o);
					if(rowId!=0){//rowId为零的行是新增行，保存后id就变了
						//取消编辑状态时，文字的padding设为15px，不会顶格
						setGridCellPadding(rowId,notSetLeft,"15px");
					};
				}else{
					if($nextInput.attr("type")=='text'){
						$nextInput[0].select();
					}else{
						$nextInput.focus();
					}
					var inputLeft = $nextInput.offset().left;
					var divLeft = $(".ui-jqgrid-bdiv").offset().left + $(".ui-jqgrid-bdiv").width();
					if(inputLeft+$nextInput.width()>divLeft){
						var hisLeft = $(".ui-jqgrid-bdiv").scrollLeft(); 
						$(".ui-jqgrid-bdiv").scrollLeft(hisLeft + ($(".ui-jqgrid-bdiv").width()/2));
					};
				};
			};
		//←前一格
		}else if(e.keyCode == 37){
			if($(e.target).is(":input")){
				//判断是否下拉框
				var $input = $(e.target);
				var autocomplete = $input.attr("autocomplete");
				if(autocomplete){
					var self = $input.data("self");
					var isHidden = $(self.menu.element).is(":hidden");
					//如果下拉菜单显示时,默认显示第一条
					if(!isHidden){
						self._move("next",e);
						$(self.menu.element).menu("select",e);
					};
				}
				var $preInput = _getPrevEditInput($(e.target).closest("td[role=gridcell]"));
				if($preInput==null){
					if ($.isFunction(beforeSaveFunc)) {
						beforeSaveFunc.call($grid);
					}
					$grid.jqGrid("saveRow",rowId,o);
					if(rowId!=0){//rowId为零的行是新增行，保存后id就变了
						//取消编辑状态时，文字的padding设为15px，不会顶格
						setGridCellPadding(rowId,notSetLeft,"15px");
					};
				}else{
					if($preInput.attr("type")=='text'){
						$preInput[0].select();
					}else{
						$preInput.focus();
					}
					var inputRight = $preInput.offset().left;
					var divLeft = $(".ui-jqgrid-bdiv").offset().left + $(".ui-jqgrid-bdiv").width();
					if(inputRight+$preInput.width()>divLeft){
						var hisLeft = $(".ui-jqgrid-bdiv").scrollLeft(); 
						$(".ui-jqgrid-bdiv").scrollLeft(hisLeft + ($(".ui-jqgrid-bdiv").width()/2));
					};
				};
			};
		//向下键为保存	
		}else if(e.keyCode == 40){
			//检查是否是自动填写下拉框
			var isSave = true;
			var $input = $(e.target);
			var autocomplete = $input.attr("autocomplete");
			if(autocomplete){
				isSave = false;
			}
			if($input.is("select")){
				isSave = false;
			}
			if(isSave){
				if ($.isFunction(beforeSaveFunc)) {
					beforeSaveFunc.call($grid);
				}
				$grid.jqGrid("saveRow",rowId,o);
				if(rowId!=0){//rowId为零的行是新增行，保存后id就变了
					//取消编辑状态时，文字的padding设为15px，不会顶格
					setGridCellPadding(rowId,notSetLeft,"15px");
				};
			};
		}
		return true;
	});
	//检查当前激活的控件是否行编辑里的控件,如果不是,默认为第一个
	var trId = $(document.activeElement).closest("tr").attr("id");
	if(trId != rowId){
		var $nextInput = _getNextEditInput($("#" + rowId).find("td[role=gridcell]").first());
		if($nextInput != null){
			if($nextInput.attr("type")=='text'){
				$nextInput[0].select();
			}else{
				var version = $.browser.version;
				if($.browser.msie&&(version==8.0||version==7.0)){
					$textInput = _getNextEditText($nextInput.closest("td[role=gridcell]"));
					if($textInput != null){
						$textInput[0].setActive();
					}
					setTimeout(function(){
						$nextInput.focus();
					}, 10);
				}else{
					$nextInput.focus();
				};
			};
		};
	};
}
function _getNextEditText($td){
	$input = _getNextEditInput($td);
	if($input == null){
		return null;
	}else{
		if($input.attr("type") != 'text'){
			return _getNextEditText($input.closest("td[role=gridcell]"));
		}else{
			return $input;
		}
	}
}
//获取下一个可编辑的输入框
function _getNextEditInput($td){
	var $next = $td.next();
	if($next.length==0){
		return null;
	}
	if($next.is(":hidden")){
		return _getNextEditInput($next);
	}
	var $input = $next.find(":input[name]");
	var readonly = $input.attr("readonly");
	if(readonly){
		readonly = readonly + "";
	}
	if($input.length==0||$input.is(":disabled")||readonly=='true'){
		return _getNextEditInput($next);
	}else{
		return $input;
	};
}
//获取前一个可编辑的输入框
function _getPrevEditInput($td){
	var $prev = $td.prev();
	if($prev.length==0){
		return null;
	}
	if($prev.is(":hidden")){
		return _getPrevEditInput($prev);
	}
	var $input = $prev.find(":input[name]");
	var readonly = $input.attr("readonly");
	if(readonly){
		readonly = readonly + "";
	}
	if($input.length==0||$input.is(":disabled")||readonly=='true'){
		return _getPrevEditInput($prev);
	}else{
		return $input;
	};
}
function setGridCellPadding(rowId,notSetLeft,padding){
	 if(!notSetLeft){
		$.each($("#" + rowId).find("td[role=gridcell]"),function(key,item){
			if($(item).find("input[role=checkbox]").length==0){//checkbox这个单元格不设置缩进,否则会被遮住
				$(item).css({
					'padding-left':padding
				});
			}
		});
	};
}
function getGridSaveParams(gridId){
	var o=editParams;
	var $grid = $("#" + gridId);
	o.url = $grid.jqGrid("getGridParam","editurl");
	return $.extend(true, {
		keys : false,
		oneditfunc: null,
		successfunc: null,
		url: null,
		extraparam: {},
		aftersavefunc: null,
		errorfunc: null,
		afterrestorefunc: null,
		restoreAfterError: true,
		mtype: "POST",
	}, $.jgrid.inlineEdit,o);
};
</script>