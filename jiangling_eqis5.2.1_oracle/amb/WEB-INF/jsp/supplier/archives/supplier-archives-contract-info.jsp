<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<title>供应商</title>

	<script type="text/javascript" src="../../js/jquery-latest.js"></script>
	<script type="text/javascript" src="../../js/jquery.layout-latest.js"></script>	
	<script type="text/javascript" src="../../js/jquery-ui-latest.js"></script>
	<script type="text/javascript" src="../../js/jquery.layout.resizePaneAccordions-1.0.js"></script>
	<link href="../../widgets/colorbox/colorbox.css" rel="stylesheet" type="text/css"/>
	<script src="../../widgets/colorbox/jquery.colorbox.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="../../widgets/jqgrid/ui.jqgrid.css" />	
	<script src="../../widgets/jqgrid/grid.locale-en.js" type="text/javascript"></script>
	<script src="../../widgets/jqgrid/jquery.jqGrid.src.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="../../css/sky-blue/jquery-ui-1.8.16.custom.css" id="_style"/>
	<script type="text/javascript" src="../../widgets/jstree/jquery.jstree.js"></script>
	<style type="text/css">
.container {
	/* used for elements that should not have padding or scrollbars */
	padding: 0;
	overflow: hidden;
}

.hidden { /* used to HIDE layout-panes until Layout initializes */
	display: none;
}

/*
	 *	TAB CLASSES - STRUCTURAL, REQUIRED
	 */
DIV#tabs {
	/* page-wrapper - used ONLY to 'wrap' tabButtons and tabPanels */
	position: static !important;
	/* overide UI Theme - prevent from becoming a positional wrapper */
}

UL#tabButtons,DIV#tabPanels {
	margin: 0 !important; /* layout-panes should never have margins */
}

DIV#tabPanels {
	
}

DIV#tab1.tabPanel { /* container for tabLayout */
	height: 100%;
	/* need to 'fill' the tabPanels - layout container must have 'height' */
	/* padding:	0;    DO NOT set top- or bottom-padding because height=100% */
}

/*
	 *	DEBUG - add colored padding & borders to show what is possible
	 */
#tabs {
	
}

#tabButtons {
	
}

#tabPanels { /* Tab-Panels Container (pageLayout center-pane) */
	border: 10px solid #fff; /* DEBUG - Dark Blue */
	background: #fff; /* DEBUG - Lite Blue */
	padding: 0px; /* DEBUG - allows bgColor to show */
}

#tabPanels .tabPanel { /* #tab0 & #tab1 */
	background: #fff; /* DEBUG - Grey */
	min-height: 100px; /* DEBUG */
	padding: 0 10px;
	/* DEBUG - No vertical-padding because #tab1.height=100% */
}

/*
	 *	add some padding and set overflow
	 */
.ui-widget-header {
	padding: 2px 1em 3px;
	overflow: hidden;
}

.ui-widget-content {
	padding: 1ex 1em;
	overflow: auto;
}
</style>

	<script type="text/javascript">
	/*
	 *	Save settings for each layout to hash-vars for easier-to-read code
	 */

	var pageLayoutSettings = {
		name:						"pageLayout"
	,	triggerEventsOnLoad:		false
	,	north__paneSelector:		"#tabButtons"
	,	center__paneSelector:		"#tabPanels"
	,	center__onresize:			"resizeTabLayout"
	,	spacing_open:				0
	};

	var tabLayoutSettings = {
		name:						"tabLayout"
	,	initPanes:					false // delay layout init until tab.show calls tabLayout.resizeAll()
	,	resizeWithWindow:			false // needed because is 'nested' inside the tabLayout div
	,	center__paneSelector:		".middle-center"
	,	west__paneSelector:			".middle-west"
	,	contentSelector:			".ui-widget-content"
	,	spacing_open:				4
	,	spacing_closed:				4
	,	west__size:					150
	//,	west__initClosed:			true
	,	center__onresize:			"innerLayout.resizeAll"
	};

	/*
	 *	init Object vars
	 */
	var pageLayout, tabLayout, innerLayout, $Tabs;

	/*
	 *	init page onLoad
	 */
	$(document).ready(function () {

		// create the tabs before the page layout because tabs will change the height of the north-pane
		$Tabs = $("#tabs").tabs({
			// using callback addon
			//show: $.layout.callbacks.resizeTabLayout
			//* OR with a manual/custom callback
			show: function (evt, ui) {
				var tabLayout = $(ui.panel).data("layout");
				if ( tabLayout ) tabLayout.resizeAll();
			}
			
		});

		// add UI classes for cosmetics -- before creating layouts because may change element heights
		//addCosmeticStyles();

		// create the outer/page layout
		pageLayout = $('body').layout( pageLayoutSettings );

		// init #tab1 tabLayout - will only partially init if 'not visible'
		tabLayout = $('#tab1').layout( tabLayoutSettings );

	});

	</script>

</head>
<body>

<div id="tabs" class="container">

	<ul id="tabButtons" class="container">
		<li><a href="#tab1">åºæ¬ä¿¡æ¯</a></li>
		<li><a href="#tab0">ä¾åºäº§å</a></li>
		<li><a href="#tab2">ä½ç³»è¯ä¹¦</a></li>
	</ul>
	
	<!-- wrap tab-panels in ui-layout-content div -->
	<div id="tabPanels" class="container">
		<!-- TAB #1 -->
		<div id="tab1" class="tabPanel ui-widget-content ui-tabs-hide">
			<div class="middle-center container hidden">
				<div class="ui-widget-content">
					<table class="form-table-border-left">
								<tr>
									<td width="20%">ä¾åºååç§°</td>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr>
									<td >ä¾åºåç¼å·</td>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr>
									<td >ä¼ä¸å°å</td>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr>
									<td width="20%">å°åº</td>
									<td width="30%">&nbsp;</td>
									<td width="20%">ç®ç§°</td>
									<td width="30%">&nbsp;</td>
								</tr>
								<tr>
									<td >é®æ¿ç¼ç </td>
									<td >&nbsp;</td>
									<td >èç³»çµè¯</td>
									<td >&nbsp;</td>
								</tr>
								<tr>
									<td >ä¼ ç</td>
									<td >&nbsp;</td>
									<td >ææº</td>
									<td >&nbsp;</td>
								</tr>
								<tr>
									<td >Emailå°å</td>
									<td >&nbsp;</td>
									<td >èç³»äºº</td>
									<td >&nbsp;</td>
								</tr>
								<tr>
									<td >èç³»äººé¨é¨</td>
									<td >&nbsp;</td>
									<td >èå¡</td>
									<td >&nbsp;</td>
								</tr>
							</table>
						</div>

			</div>

			<div class="middle-west container container hidden">
				<div class="ui-widget-content">
					<div id="demo4" class="demo"></div>
					<script type="text/javascript">
						var treeMenu='s1';
						$(function () {
								$("#demo4").jstree({ 
									"core" : { "initially_open" : [ "root" ] },
									"html_data" : {
										"data" :"<ul><li onclick='selectedNode(this)' id='s1'><a href='supplier-archives-contract-info.html'>èç³»ä¿¡æ¯</a></li>"+
									      "<li onclick='selectedNode(this)' id='s2'><a href='supplier-archives-base-info.html'>åºæ¬ä¿¡æ¯</a></li>"+
									      "<li onclick='selectedNode(this)' id='s3'><a href='supplier-archives-enterprise-overview.html'>ä¼ä¸æ¦åµ</a></li>"+
									      "<li onclick='selectedNode(this)' id='s4'><a href=''>ä¸»è¦äº§åä»ç»</a></li>"+
									      "<li onclick='selectedNode(this)' id='s5'><a href=''>äº§åä¿¡æ¯</a></li>"+
									      "<li onclick='selectedNode(this)' id='s6'><a href=''>åæææåµ 	</a></li>"+
									      "<li onclick='selectedNode(this)' id='s7'><a href=''>ææ¡£ä¿¡æ¯</a></li>"+
									      "<li onclick='selectedNode(this)' id='s8'><a href=''>ä¿®æ¹è®°å½</a></li></ul>"
									},
									"ui" : {
										"initially_select" : [ treeMenu ]
									},
									"plugins" : [ "themes", "html_data","ui" ]
								});
						});
						function selectedNode(obj){
							window.location = $(obj).children('a').attr('href');
						}
						
						</script>
				</div>

			</div>
		</div><!-- TAB #1 / #tabLayout -->

		<!-- TAB #0 -->
		<div id="tab0" class="tabPanel ui-tabs-hide">
			<div class="opt-btn">
					<button class='btn' onclick=""><span><span>æ°å»º</span></span></button>
					<button class='btn' onclick=""><span><span>ä¿®æ¹</span></span></button>
					<button class='btn' onclick=""><span><span>å é¤</span></span></button>
			</div>
			<div class="ui-widget-content">
				<table id="product_table"></table>
				<div id="product_pager"></div>
							<script type="text/javascript">
								var tabInitiated=[1,0];
								var myData=[
						            {serialNumber:'',materialCategory:'', materialNO:'', materialName:'',admittanceDate:'',remark:''},
						            {serialNumber:'',materialCategory:'', materialNO:'', materialName:'',admittanceDate:'',remark:''},
						            {serialNumber:'',materialCategory:'', materialNO:'', materialName:'',admittanceDate:'',remark:''},
						            ];
								$(document).ready(function(){
									$( "#tabs" ).tabs({
										show: function(event, ui) { 
											if(ui.index==2&&tabInitiated[1]==0){
												tabInitiated[1]=1;
												var myData1=[
												             {serialNumber:'1', systemName:'ISO9001', awardingInstitution:'SGS', certificateDate:'2011-6-6', validityPeriod:'2014-6-5', certificateFile:''},
												             {serialNumber:'2', systemName:'ISO14001', awardingInstitution:'SGS', certificateDate:'2011-6-6', validityPeriod:'2014-6-5', certificateFile:''},
												            ];;
												jQuery("#file_table").jqGrid({
													height:320,
													datatype: "local",
													data: myData1,
													pager:"file_pager",
													colNames:['åºå·', 'ä½ç³»åç§°', 'é¢åæºæ', 'åè¯æ¥æ', 'æææ', 'è¯ä¹¦'],
													colModel:[{name:'serialNumber', index:'serialNumber', width:50},
													          {name:'systemName', index:'systemName'},
													          {name:'awardingInstitution', index:'awardingInstitution'},
													          {name:'certificateDate', index:'certificateDate'},
													          {name:'validityPeriod ', index:'validityPeriod'},
													          {name:'certificateFile', index:'certificateFile', width:60,align:'center'},
													          ], 
													rowNum: 20, 
												   	autowidth: true,
													viewrecords: true, 
													sortorder: "desc",
													gridComplete:function(){ 
														var ids = jQuery("#file_table").jqGrid('getDataIDs');
														for(var i=0;i < ids.length;i++){ 
															var cl = ids[i]; 
															var operations="<button title='ä½ç³»è¯ä¹¦' class='small-button-bg' onclick='' ><span class='ui-icon ui-icon-image'></span></button>";
															jQuery("#file_table").jqGrid('setRowData',ids[i],{certificateFile:operations}); 
														}
													},
												});
												
											}
										}
									});
									jQuery("#product_table").jqGrid({
										height: 320,
										datatype: "local",
										data: myData,
										pager:"product_pager",
										colNames:['åºå·','ç©æç±»å«','ç©æç¼å·','ç©æåç§°','åå¥æ¥æ','è¯´æ',], 
										colModel:[ {name:'serialNumber',index:'serialNumber',width:50}, 
										           {name:'materialCategory',index:'materialCategory'},
										           {name:'materialNO',index:'materialNO'}, 
												   {name:'materialName',index:'materialName'}, 
											       {name:'admittanceDate',index:'admittanceDate'}, 
											       {name:'remark',index:'remark'}, 
													], 
										rowNum: 20, 
									   	autowidth: true,
										viewrecords: true, 
										sortorder: "desc",
									});
									
					       		});
							</script>
			</div>
		</div>
		<!-- TAB #2 -->
		<div id="tab2" class="tabPanel ui-tabs-hide">
			<div class="opt-btn">
					<button class='btn' onclick=""><span><span>æ°å»º</span></span></button>
					<button class='btn' onclick=""><span><span>ä¿®æ¹</span></span></button>
					<button class='btn' onclick=""><span><span>å é¤</span></span></button>
			</div>
			<div class="ui-widget-content">
				<table id="file_table"></table>
				<div id="file_pager"></div>
			</div>
		</div>
	</div><!-- END tabPanels -->

</div><!-- /#tabs -->
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>