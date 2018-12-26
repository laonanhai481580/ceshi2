<%@page import="com.ambition.gp.entity.GpSubstance"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
		isUsingComonLayout=false;
		var initiated = {
			'0':true,
			'1' : false,
			'2' : false
		};
		var id='${id}',idIndex = 111,updateParams = {},deleteParams={},lastSelect={},gridHeight = 0,gridWidth=0;
		$(document).ready(function(){
			$("#tabs").tabs({
				show: function(event, ui) {
					createCertificateGrid();
				}
			}).height($(document).height()-30);
			$(".ui-jqgrid-bdiv").height($(document).height()-105);
			gridHeight = $(document).height()-150;
			gridWidth = $(document).width() - 70;
			$("#form").validate();//使规则生效
		});
		function selectedNode(obj){
			window.location = $(obj).children('a').attr('href');
		}
		//保存信息
		function saveSupplier(){
			if($("#workflowForm").valid()){
				var params = getParams();
				params.id = id;
				$(".opt-btn .btn").attr("disabled",true);
				$("#message").html("正在保存,请稍候... ...");
				$.post("${gpctx}/averageMaterial/save.htm",params,function(result){
					if(!result.error){
						$("#message").html("保存成功!");
					}else{
						$("#message").html(result.message);
						alert(result.message);
					}
					setTimeout(function(){
						$("#message").html('');
					},1000);
					$(".opt-btn .btn").attr("disabled",false);
				},'json');
			}
		}
		//保存供应产品
		function saveCertificates(){
			var params = {
				id : '${id}'
			};
			var pro = 'certificate_table';
			function priSaveCertificates(){
				var idObj = deleteParams[pro];
				var ids = [];
				for(var id in idObj){
					if(id > 0){
						ids.push(id);
					}
				}
				params["params.deletes"] = ids.toString();
				idObj = updateParams[pro];
				var strs = [];
				for(var id in idObj){
					if(idObj[id]){
						var data = jQuery("#" + pro).jqGrid('getRowData',id);
						if(data.id){
							var str = '{';
							var flag = false;
							for(var per in data){
								var val = data[per];
								if(val.indexOf("<")>-1&&val.indexOf(">")>-1){
									continue;
								}
								if(flag){
									str += ',';
								}
								str += '"' + per + '":' + '"' + data[per] + '\"';
								flag = true;
							}
							if(flag){
								str += '}';
								strs.push(str);
							}
						}
					}
				}
				params["params.updates"] = '['+ strs.toString() + ']';
				$(".opt-btn .btn").attr("disabled",true);
				$("#certificate-message").html("正在保存,请稍候... ...");
				$.post("${gpctx}/averageMaterial/save-certificate.htm",params,function(result){
					$("#certificate-message").html(result.message);
					if(!result.error){
						//设置表格的更新后的ID
						var updateDetails = result.updateDetails;
						try{
							var tableId = pro;
							var updateIdObj = updateDetails[tableId];
							var grid = $("#" + tableId);
							var isUpdate = false;
							for(var hisId in updateIdObj){
								grid.jqGrid('setRowData',hisId,{id:updateIdObj[hisId]});
								var trs = $("#" + tableId + " #" + hisId);
								if(trs.length>0){
									trs[0].id = updateIdObj[hisId];
									isUpdate = true;
								}
							}
							if(isUpdate){
								addOperateMethod(tableId);
							}
						}catch(e){
							alert(e.message);
						}
						idObj = deleteParams[pro];
						for(var id in idObj){
							idObj[id] = false;
						}
						idObj = updateParams[pro];
						for(var id in idObj){
							idObj[id] = false;
						}
					}
					$(".opt-btn .btn").attr("disabled",false);
					setTimeout(function(){
						$("#certificate-message").html('');
					},1000);
				},'json');
			}
			//先保存最后一次编辑的记录
			if(lastSelect[pro]){
				var grid = $("#" + pro);
				var data = grid.jqGrid("getRowData",lastSelect[pro]);
				var success = grid.jqGrid("saveRow",lastSelect[pro],{
					url:'clientArray'
				});
				if(!success){
					return;
				}
				addUpateParams(pro,lastSelect[pro]);
				$("#" + lastSelect[pro] + " .upload").hide();
				lastSelect[pro] = null;
				makeEditable(pro,true);
			}
			priSaveCertificates();
		}
		//获取参数
		function getParams(){
			var params = {};
			$("#workflowForm :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type=='radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			var linkMans = "";
			$(".link-man").each(function(index,obj){
				var str = '';
				$(obj).find(":workflowForm[customName]").each(function(flag,item){
					var $item = $(item);
					if(str){
						str += ",";
					}
					str += "\""+$item.attr("customName")+"\":\"" + $item.val() + "\"";
				});
				if(linkMans){
					linkMans += ",";
				}
				linkMans += "{" + str + "}";
			});
			if(linkMans){
				params.linkMans = "[" + linkMans + "]";
			}
			return params;
		}
		//重置方法
		function setParams(params){
			$("#asendForm input").each(function(index,obj){
				if((obj.type=='text'||obj.type=='hidden')&&obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
			$("#asendForm select").each(function(index,obj){
				if(obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
		}
		function customDate(value,options){
			var el = document.createElement("input");
            el.type="text";
            el.value = value;
            setTimeout(function(){
            	$(el).datepicker();
            },100);
            return el;
		}
		function getCustomDateValue(elem) {
            return $(elem).val();
        }
	
		//创建管控物质
		function createCertificateGrid(){
			jQuery("#_childrenInfos").jqGrid({
				url : '${gpctx}/averageMaterial/gpSubstance-datas.htm',
				postData : {id:'${id}'},
				height : gridHeight,
				width : gridWidth,
				datatype: "json",
				rownumbers:true,//行序号
				gridEdit: true,
				colNames : ${gpSubstanceGridColumnInfo.colNames},
				colModel : ${gpSubstanceGridColumnInfo.colModel},
				forceFit : true,
			   	shrinkToFit : true,//自动拉伸
				multiselect : false,//全选
			   	autowidth: true,//显示
				viewrecords: true, 
				sortorder: "desc",//排序
				gridComplete : function(){
					addOperateMethod("_childrenInfos");
				},
				ondblClickRow: function(rowId){
					editRow("_childrenInfos",rowId);
				},
				loadComplete : function(){
					var grid = $("#_childrenInfos");
					if(grid.jqGrid('getDataIDs').length == 0){
						grid.jqGrid("addRowData",--idIndex,{id:idIndex});
					};
				}
			});
		}
		
		//添加参数的方法
		function addOperateMethod(tableId){
			var grid = jQuery("#" + tableId);
			var ids = grid.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){ 
				var cl = ids[i]; 
				var operations = "<a class=\"small-button-bg\" href=\"javascript:addRow('"+tableId+"',"+cl+");\" title=\"添加\"><span class=\"ui-icon ui-icon-plusthick\" style='cursor:pointer;'></span></a>"
					+"&nbsp;<a class=\"small-button-bg\" onclick=\"delRow('"+tableId+"',"+cl+",event);\" href=\"#\" title=\"删除\"><span class=\"ui-icon ui-icon-closethick\" style='cursor:pointer;'></span></a>";
					grid.jqGrid('setRowData',ids[i],{act:operations});
			}
		}
		function priAddRow(tableId,byEnter){
			var grid = $("#" + tableId);
			if(byEnter==undefined){
				byEnter=false;
			}
			var initdata = {};
			if(tableId == "product_table"){
				initdata.applyState = defaultState;
			}
			if ((!editing&&!byEnter)||byEnter) {
				var id = --idIndex;
				initdata.id = id;
				grid.jqGrid(
					'addRow',
					{
						rowID : id,
						position : "last",
						initdata : initdata,
						addRowParams : {
							keys:true,
							aftersavefunc : function(rowId, data) {
								afterSaveRow(tableId,rowId,data);
							},
							afterrestorefunc :function(rowId){
								makeEditable(tableId,true);
							},
							url : 'clientArray'
						}
					});
				lastSelect[tableId] = id;
				makeEditable(tableId,false);
				editFun(tableId,id);
			}
		}
		//添加方法
		function addRow(tableId,byEnter) {
			var grid = $("#" + tableId);
			console.log(tableId);
			console.log(byEnter);
			function priAddRow(){
				if(byEnter==undefined){
					byEnter=false;
				}
				var initdata = {};
				if(tableId == "product_table"){
					initdata.applyState = defaultState;
				}
				if ((!editing&&!byEnter)||byEnter) {
					var id = --idIndex;
					initdata.id = id;
					grid.jqGrid(
						'addRow',
						{
							rowID : id,
							position : "last",
							initdata : initdata,
							addRowParams : {
								keys:true,
								aftersavefunc : function(rowId, data) {
									afterSaveRow(tableId,rowId,data);
								},
								afterrestorefunc :function(rowId){
									makeEditable(tableId,true);
								},
								url : 'clientArray'
							}
						});
					lastSelect[tableId] = id;
					makeEditable(tableId,false);
					editFun(tableId,id);
					if(tableId="product_table"){
						$("#" + id + "_code").attr("readonly", "readonly").click(
								function() {
									supplyMaterialClick(id);
							});
					}
				}
			}
			
			if(editing&&lastSelect[tableId]){
				var obj = grid.jqGrid("getRowData",lastSelect[tableId]);
				if(obj.id){
					grid.jqGrid("saveRow",lastSelect[tableId],{
						url:'clientArray',
						aftersavefunc : function(rowId, data) {
							$("#" + rowId + " .upload").hide();
							addUpateParams(tableId,lastSelect[tableId]);
							priAddRow();
						}
					});
				}else{
					priAddRow();
				}
			}else{
				priAddRow();
			}
		}
		
		//添加修改的编号
		function addUpateParams(tableId,id){
			var obj = updateParams[tableId];
			if(!obj){
				obj = {};
				updateParams[tableId] = obj;
			}
			if(!obj[id]){
				obj[id] = true;
			}
		}
		
		//添加删除的编号
		function addDeleteParams(tableId,id){
			if(id > 0){
				var obj = deleteParams[tableId];
				if(!obj){
					deleteParams[tableId] = {};
					obj = deleteParams[tableId];
				}
				if(!obj[id]){
					obj[id] = true;
				}
			}
			var obj = updateParams[tableId];
			if(!obj){
				obj = {};
				updateParams[tableId] = obj;
			}
			if(obj[id]){
				obj[id] = false;
			}
		}
		var editing=false;
		function makeEditable(tableId,editable){
			if(editable){
				editing=false;
				jQuery("#"+tableId+" tbody").sortable('enable');
			}else{
				editing=true;
				jQuery("#"+tableId+" tbody").sortable('disable');
			}
		}
		function editNextRow(tableId,rowId){
			var grid = jQuery("#" + tableId);
			var ids=grid.jqGrid("getDataIDs");
			var index=grid.jqGrid("getInd",rowId);
			index++;
			if(index>ids.length){//当前编辑行是最后一行
				addRow(tableId,true);
			}else{
				editRow(tableId,ids[index-1]);
			}
		}
		function afterSaveRow(tableId,rowId,data){
			$("#" + rowId + " .upload").hide();
			addUpateParams(tableId,rowId);
			lastSelect[tableId] = null;
			editNextRow(tableId,rowId);
		}
		//编辑表格
		function editRow(tableId,rowId){
			var grid = $("#" + tableId);
			function priEditRow(){
				lastSelect[tableId]=rowId;
				grid.jqGrid("editRow",rowId,{
					keys:true,
					aftersavefunc : function(rowId, data) {
						afterSaveRow(tableId,rowId,data);
					},
					afterrestorefunc :function(rowId){
						makeEditable(tableId,true);
						$("#product-message").html("");
						if(tableId == 'certificate_table'){
							var obj = $("#" + tableId).jqGrid("getRowData",rowId);
							if(obj.id){
								$("#" + rowId + " .upload").hide();
								$("#" + rowId + "_showAttachmentFiles").html($.getDownloadHtml(obj.certificationFiles));
								$("#" + rowId + "_hiddenAttachmentFiles").val(params.hisAttachmentFiles?params.hisAttachmentFiles:'');
								$("#" + tableId).jqGrid("setRowData",rowId,{certificationFiles:params.hisAttachmentFiles});
							}
						}
					},
					beforeEditCell :function(rowId){
						alert("11");
					},
					url : 'clientArray'
				});
				if(tableId=="product_table"){
					$("#" + rowId + "_code").attr("readonly", "readonly").click(
							function() {
								supplyMaterialClick(rowId);
						});
				}
				makeEditable(tableId,false);
				editFun(tableId,rowId);
			}
			if(lastSelect[tableId]&&rowId!=lastSelect[tableId]){
				var obj = grid.jqGrid("getRowData",lastSelect[tableId]);
				if(obj.id){
					grid.jqGrid("saveRow",lastSelect[tableId],{
						url:'clientArray',
						aftersavefunc : function(rowId, data) {
							addUpateParams(tableId,lastSelect[tableId]);
							$("#" + rowId + " .upload").hide();
							priEditRow();
						}
					});
				}else{
					priEditRow();
				}
			}else{
				priEditRow();
			}
		}
		//删除对象
		function delRow(tableId,rowId,event) {
			var grid = $("#" + tableId); 
			if(grid.jqGrid('getDataIDs').length == 1){
				alert("");
				return;
			}
			grid.jqGrid("delRowData",rowId);
			addDeleteParams(tableId,rowId);
			if(grid.jqGrid('getDataIDs').length == 0){
				idIndex--;
				grid.jqGrid("addRowData",idIndex,{id:idIndex,applyState:defaultState});
			};
			if(rowId == lastSelect['product_table']){
				lastSelect['product_table'] = null;
				makeEditable(tableId,true);
			}
			try {
				var evt = window.event || event;
				evt.cancelBubble=true;
			} catch (e) {}
		}
		
		var params = {};
		var editFumObj = {
				'certificate_table':function(id){
					setTimeout(function(){
						jQuery('#'+id+'_certificationDate','#certificate_table').attr("readonly","readonly");
						jQuery('#'+id+'_certificationDate','#certificate_table').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
						jQuery('#'+id+'_invalidationDate','#certificate_table').attr("readonly","readonly");
						jQuery('#'+id+'_invalidationDate','#certificate_table').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
						params = {
							hisAttachmentFiles : $("#" + id + "_hiddenAttachmentFiles").val()
						};
						$("#" + id + " .upload").show();
					},100);
				}
			};
		function editFun(tableId,id){
			if(editFumObj[tableId]){
				editFumObj[tableId](id);
				console.log("eeeee");
			}
		}
		
		
		
		
		
		
		
		
		
		
		
			
	</script>
</head>