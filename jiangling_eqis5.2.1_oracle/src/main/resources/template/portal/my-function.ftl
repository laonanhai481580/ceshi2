<div id="my-function-div" style="margin:0px;padding:0px;width:100%;overflow-y:auto;">
	<div style="float:right;padding-left:6px;padding-right:6px;padding-top:2px;padding-bottom:2px;border:1px solid #99bbe8;">
		<a href="javascript:customMyFunction()" title="设置我的功能">设置</a>
	</div>
	<div id="my-function-div-tree"></div>
</div>
<script type="text/javascript">
	function addCssOrJsFile(files){
		for (var i = 0; i < files.length; i++) {
	        var name = files[i].replace(/^\s|\s$/g, "");
	        var att = name.split('.');
	        var ext = att[att.length - 1].toLowerCase();
	        var isCSS = ext == "css";
	        var tag = isCSS ? "link" : "script";
	        var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' ";
	        var link = (isCSS ? "href" : "src") + "='${ctx}" + name + "'";
	        if ($(tag + "[" + link + "]").length == 0) document.write("<" + tag + attr + link + "></" + tag + ">");
	    }
	}
	var files = ["/widgets/jstree/themes/classic/style.css","/widgets/jstree/_lib/jquery.hotkeys.js","/widgets/jstree/jquery.jstree.js"];
    addCssOrJsFile(files);
    $(document).ready(function(){
    	var addressMaps = ${addressMaps};
    	createMyFunctionTree(addressMaps);
    	setTimeout(function(){
    		var offset = $("#my-function-div").offset();
    		var height = $(window).height()-offset.top-20;
    		//$("#my-function-div").height(height);
    	},200);
    	$("#my-function-div").height($("#my-function-div").width()/2);
    });
	function createMyFunctionTree(addressMaps){
		$("#my-function-div-tree").jstree({ 
				"json_data" : {
					"data" : addressMaps
				},
				"plugins" : [ "themes", "json_data", "ui" ]
			}).bind("select_node.jstree",function(e,data){
				var address = data.rslt.obj.attr("address");
				if(address){
					window.location = address;
				}
			});
	}
	function customMyFunction(){
		var url = "${ctx}/mfg/base-info/my-function/set-function-address-user.htm?userId=${userId}&companyId=${companyId}";
		$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:290,
			overlayClose:false,
			onClosed : function(){
				var contentDivId = $("#my-function-div").parent().attr("id");
				if(contentDivId){
					var strs = contentDivId.split("-");
					var widgetId = strs[strs.length-1];
					if(!isNaN(widgetId)){
						if(typeof(loadWidgetContent) != 'undefined'){
							$("#" + contentDivId).html('<div style="text-align:center;"><img alt="" src="../../images/loading.gif"/>&emsp;正在加载...</div>');
							loadWidgetContent(widgetId,contentDivId);
						}
					}
				}
			},
			title:"设置我的功能"
		});
	}
</script>
