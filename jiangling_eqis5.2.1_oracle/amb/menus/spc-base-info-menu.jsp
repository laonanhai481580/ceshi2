<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%Map<String,Integer> pointKeyMap = ProcessDefineManager.getPointKeyMap();%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true }
		);
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	//刷新结构树
	function refreshProductStrure(){
		$("#product-structure-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "产品列表", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/process-define/point-list.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"],},
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${spcctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : [ "${processPoint.id}" ]
			},
			contextmenu : {
				items : {
					<security:authorize ifAnyGranted="spc_base-info_process-define_input">
					create:{
						label : '添加',
						action : function(obj){
							var id = obj.attr("id");
							editProductStructure('',id=='root'?'':id);
						}
					},
					rename : {
						label : '编辑',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能编辑根目录!");
								return false;
							}else{
								editProductStructure(obj.attr("id"));
								return true;
							}
						}
					},
					</security:authorize>
					<security:authorize ifAnyGranted="spc_base-info_process-define_delete">
					remove : {
						label : '删除',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能删除根目录!");
							}else{
								if(confirm("确定要删除吗?")){
									var _self = this;
									$.post('${spcctx}/base-info/process-define/delete.htm',{id:obj.attr("id")},function(data){
										if(data.error){
											alert(data.message);
										}else{
											_self.remove(obj);
										}
									},'json');
								}
							}
						}
					},
					</security:authorize>
					setPerson : {
						label : '设置人员',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("请选择段别!");
							}else{
								var _self = this;
								selectPersonAll(obj.attr("id"));
							}
						}
					},
					setRule : {
						label : '设置规则',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("请选择段别!");
							}else{
								var _self = this;
								selectRules(obj.attr("id"));
							}
						}
					},
					setLayerType : {
						label : '设置层别',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("请选择段别!");
							}else{
								var _self = this;
								selectBathLayerType(obj.attr("id"));
							}
						}
					},
					copyProduct : {
						label : '复制',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("请选择机种!");
							}else{
								var _self = this;
								copyProduct(obj.attr("id"));
							}
						}
					},
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var bomLevel = <%=pointKeyMap.keySet().size()%>;
			var level = data.rslt.obj.attr("level");
			if(level > 0){
				setFeatureTree(data.rslt.obj.attr("id"),data.rslt.obj.attr("name"));
			}
		});
	}
	//编辑结构
	var editingId = null;
	function editProductStructure(id,parentId){
		var url='${spcctx}/base-info/process-define/input.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId = id;
		}
		if(parentId){
			url += "&parentId=" + parentId;
			newId += "_" + parentId;
		}
		if(newId == editingId){
			return;
		}else{
			editingId = newId;
			setTimeout(function(){
				editingId = null;
			}, 500);
		}
		$.colorbox({href:url,iframe:true, innerWidth:620, innerHeight:450,
			overlayClose:false,
			onClosed:function(){
				refreshProductStrure();
			},
			title:(id?"编辑":"添加") + "产品列表"
		});
	}
	//加载 
	function setFeatureTree(structureId,name){
		if(window.selStructureId == structureId){
			return;
		}
		window.selStructureId = structureId || '';
		var selName = name || '';
		if(selStructureId){
// 			var url = '${spcctx}/process-define/quality-feature-datas.htm?structureId=' + selStructureId;
// 			window.location = url;
			createFeatureTree(selStructureId,selName);
		}
	}
	
	
	//刷新层级类别树
	function refreshLayerTypeTree(){
		$("#layer-type-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "层别类别", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/layer-type/list-datas.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${mfgctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : ["${layerType.id}"]
			},
			contextmenu : {
				items : {
					create:{
						label : '添加',
						action : function(obj){
							var id = obj.attr("id");
							editLayerType('',id=='root'?'':id);
						}
					},
					rename : {
						label : '编辑',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能编辑根目录!");
								return false;
							}else{
								editLayerType(obj.attr("id"));
								return true;
							}
						}
					},
					 remove : {
						label : '删除',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能删除根目录!");
							}else{
								if(confirm("确定要删除吗?")){
									var _self = this;
									$.post('${spcctx}/base-info/layer-type/delete.htm',{id:obj.attr("id")},function(data){
										if(data.error){
											alert(data.message);
										}else{
											_self.remove(obj);
										}
									},'json');
								}
							}
						}
					}, 
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var bomLevel = <%=pointKeyMap.keySet().size()%>;
			var level = data.rslt.obj.attr("level");
			if(data.rslt.obj.attr("id")=='root'){
				alert("请选择层级类别");
			}else{
			$("#detailParentId").val(data.rslt.obj.attr("id"));
			if(level > 0){
				$("#layerDetail").setGridParam({postData:{"parentId":data.rslt.obj.attr("id")}});
				$("#layerDetail").trigger("reloadGrid");
			}
		}
		});
	}
	
	//编辑结构
	var editingId = null;
	function editLayerType(id,parentId){
		var url='${spcctx}/base-info/layer-type/input.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId = id;
		}
		if(parentId){
			url += "&parentId=" + parentId;
			newId += "_" + parentId;
		}
		if(newId == editingId){
			return;
		}else{
			editingId = newId;
			setTimeout(function(){
				editingId = null;
			}, 500);
		}
		$.colorbox({href:url,iframe:true, innerWidth:620, innerHeight:200,
			overlayClose:false,
			onClosed:function(){
				refreshLayerTypeTree();
			},
			title:(id?"编辑":"添加") + "层级类别"
		});
	}
	
 	//监控方案树
	function refreshMointorTree(){
		$("#monitor-program-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "监控方案", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/monitor-program/monit-program-list.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"],},
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${mfgctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : ["${monitProgram.id}"]
			},
			contextmenu : {
				items : {
					create:{
						label : '添加',
						action : function(obj){
							var id = obj.attr("id");
							editMointorTree('',id=='root'?'':id);
						}
					},
					rename : {
						label : '编辑',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能编辑根目录!");
								return false;
							}else{
								editMointorTree(obj.attr("id"));
								return true;
							}
						}
					},
					remove : {
						label : '删除',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能删除根目录!");
							}else{
								if(confirm("确定要删除吗?")){
									var _self = this;
									$.post('${spcctx}/base-info/monitor-program/delete.htm',{id:obj.attr("id")},function(data){
										if(data.error){
											alert(data.message);
										}else{
											_self.remove(obj);
										}
									},'json');
								}
							}
						}
					},
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var level = data.rslt.obj.attr("level");
			var fileId = '';
			if(data.rslt.obj.attr("id")=='root'){
				alert("请选择监控方案");
			}else{
			$("#monitorProgramId").val(data.rslt.obj.attr("id"));
			var picurl='${spcctx }/base-info/monitor-program/monitor-program-pic.htm?monitorProgramId='+data.rslt.obj.attr("id")+"&nowtime=" + (new Date()).getTime();
			$("#pic").load(picurl,function(){
				refsh();
			});
			}
		});
	}
	
	//编辑结构
	var editingId = null;
	function editMointorTree(id,parentId){
		var url='${spcctx}/base-info/monitor-program/input.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId = id;
		}
		if(parentId){
			url += "&parentId=" + parentId;
			newId += "_" + parentId;
		}
		if(newId == editingId){
			return;
		}else{
			editingId = newId;
			setTimeout(function(){
				editingId = null;
			}, 500);
		}
		$.colorbox({href:url,iframe:true, innerWidth:620, innerHeight:500,
			overlayClose:false,
			onClosed:function(){
				refreshMointorTree();
			},
			title:(id?"编辑":"添加") + "监控方案"
		});
	}
	
	//异常原因类别树
	function refreshReasonTypeTree(){
		$("#reason-type-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "原因类别", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/abnormal-reason/reason-type-datas.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${spcctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : ["1"]
			},
			contextmenu : {
				items : {
					create:{
						label : '添加',
						action : function(obj){
							var id = obj.attr("id");
							editReasonTypeTree('',id=='root'?'':id);
						}
					},
					rename : {
						label : '编辑',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能编辑根目录!");
								return false;
							}else{
								editReasonTypeTree(obj.attr("id"));
								return true;
							}
						}
					},
					remove : {
						label : '删除',
						action : function(obj){
							if(obj.attr("id")=='root'){
								alert("不能删除根目录!");
							}else{
								if(confirm("确定要删除吗?")){
									var _self = this;
									$.post('${spcctx}/base-info/abnormal-reason/type-delete.htm',{id:obj.attr("id")},function(result){
										_self.remove(obj);
									},'json');
								}
							}
						}
					},
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var level = data.rslt.obj.attr("level");
			if(data.rslt.obj.attr("id")=='root'){
				alert("请选择某个原因类别！");
			}else{
				if(level > 0){
					$("#reasonList").setGridParam({postData:{"parentId":data.rslt.obj.attr("id")}});
					$("#reasonList").trigger("reloadGrid");
					$("#typeId").val(data.rslt.obj.attr("id"));
				}
			}
		});
	}
	
	//编辑原因类别结构
	var editingId = null;
	function editReasonTypeTree(id,parentId){
		var url='${spcctx}/base-info/abnormal-reason/type-input.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId = id;
		}
		if(parentId){
			url += "&parentId=" + parentId;
			newId += "_" + parentId;
		}
		if(newId == editingId){
			return;
		}else{
			editingId = newId;
			setTimeout(function(){
				editingId = null;
			}, 500);
		}
		$.colorbox({href:url,iframe:true, innerWidth:600, innerHeight:450,
			overlayClose:false,
			onClosed:function(){
				refreshReasonTypeTree();
			},
			title:(id?"编辑":"添加") + "原因类别"
		});
	}
</script>
	<div id="accordion1" class="basic">
		<h3><a id="_bom" onclick="_change_menu('${spcctx}/base-info/process-define/list.htm');">过程定义</a></h3>
		<div>
			<div id="product-structure-tree" class="demo">菜单加载中,请稍候...</div>
			<script type="text/javascript" class="source">
			$(function () {
				if(thirdMenu=="bom"){
					refreshProductStrure();
					$("#accordion1").accordion({active:0});
				}
				if(thirdMenu=="layerType"){
					refreshLayerTypeTree();
					$("#accordion1").accordion({active:1});
				}
				if(thirdMenu=="_bsRulesList"||thirdMenu=="_bsRulesInput"){
					$("#accordion1").accordion({active:2});
				}
				if(thirdMenu=="monitorTree"){
					refreshMointorTree();
					$("#accordion1").accordion({active:3});
				}
				if(thirdMenu=="reasonType"){
					refreshReasonTypeTree();
					$("#accordion1").accordion({active:4});
				}
				if(thirdMenu=="_measuresList"){
					$("#accordion1").accordion({active:5});
				}
				if(thirdMenu=="_cpkRuleList"){
					$("#accordion1").accordion({active:6});
				}
			});
			function selectedNode(obj) {
				window.location = $(obj).children('a').attr('href');
			}
		    function _change_menu(url){
				window.location=url;
			}
			</script>
		</div>
		<h3><a id="_layerType" onclick="_change_menu('${spcctx}/base-info/layer-type/list.htm');">层别信息</a></h3>
		<div id="layer-type-tree" class="demo">菜单加载中,请稍候...</div>
		<h3><a id="_bsRules" onclick="javascript:window.location='${spcctx}/base-info/bs-rules/list.htm';">判异准则</a></h3>
		<div style="padding:0px;">
			<div id="_bsRulesList" class="west-notree" onclick="javascript:window.location='${spcctx}/base-info/bs-rules/list.htm';">
				<span>判异准则</span>
			</div>
		</div>
		<h3><a id="_monitorTree" onclick="_change_menu('${spcctx}/base-info/monitor-program/list.htm');">监控方案</a></h3>
		<div>
			<div id="monitor-program-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
		<h3><a id="_reasonType" onclick="_change_menu('${spcctx}/base-info/abnormal-reason/list.htm');">异常原因</a></h3>
		<div>
			<div id="reason-type-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
		<h3><a id="_measures" onclick="javascript:window.location='${spcctx}/base-info/improve-measure/list.htm';">改善措施</a></h3>
		<div style="padding:0px;">
			<div id="_measuresList" class="west-notree" onclick="javascript:window.location='${spcctx}/base-info/improve-measure/list.htm';">
				<span>改善措施</span>
			</div>
		</div>
		<h3><a id="_cpkRule" onclick="_change_menu('${spcctx}/base-info/cpk-rule/list.htm');">CPK规则</a></h3>
		<div style="padding:0px;">
			<div id="_cpkRuleList" class="west-notree" onclick="javascript:window.location='${spcctx}/base-info/cpk-rule/list.htm';">
				<span>CPK分组规则</span>
			</div>
		</div>
	</div>