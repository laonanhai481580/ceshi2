var myLayout;
$(document).ready(function() {
	myLayout = $('body').layout({
		north__paneSelector : '#header',
		north__size : 66,
		west__size : 180,
		north__spacing_open : 31,
		north__spacing_closed : 31,
		west__spacing_open : 6,
		west__spacing_closed : 6,
		center__minSize : 500,
		resizable : false,
		paneClass : 'ui-layout-pane',
		north__resizerClass : 'ui-layout-resizer',
		west__onresize : $.layout.callbacks.resizePaneAccordions,
		center__onresize : contentResize

	});
});


//变为固定查询调整列表
function contentResizeForFixed(){
	searchResize("fixed");
}
//变为高级查询调整列表
function contentResizeForAdvanced(){
	searchResize("advanced");
}
function searchResize(tableType){
	changePosition();
	if(tableType == "fixed"){
		var searchDivHeight=$('#parameter_Table').height();
	}
	var ids =[];
	$('table.ui-jqgrid-btable').each(function(){
		if(this.id != undefined){
			ids.push(this.id);
		}
	});
	for(var i=0;i<ids.length;i++){
		jQuery("#"+ids[i]).jqGrid('setGridHeight',_getTableHeight(ids));
		jQuery("#"+ids[i]).jqGrid('setGridWidth',_getTableWidth());
		if(tableType=="fixed"){
			$('#gbox_'+ids[i]).css('top',searchDivHeight+5);
		}else if(tableType=="advanced"){
			$('#gbox_'+ids[i]).css('top',205);
		}
	}
}


function headerChange(obj) {
	myLayout.toggle('north');
	if (myLayout.state['north'].isClosed) {
		$(obj).attr('class', 'show-header');
	} else {
		$(obj).attr('class', 'hid-header');
	}
}

$(document)
		.ready(
				function() {
					/* 去除链接，button，image button的点击时虚线框 */
					$(
							"a,input[type='button'],input[type='checkbox'],input[type='submit'],input[type='radio'],area,img,button")
							.bind("focus", function() {
								if (this.blur) {
									this.blur();
								}
							})
				});
//默认不显示的系统列表
var sysUrls=[];

function selectSystems(obj) {
	$('#styleList').hide();
	if ($('#sysTableDiv').attr('id') != 'sysTableDiv') {
		//如果更多菜单中的系统出现在了topMenu中则把最后一个系统隐藏掉
		if(sysUrls[topMenu]!=null){
			delete sysUrls[topMenu];
			sysUrls['lastSys']={name:'test',url:'test.html'};
		}
		var table = "<div id='sysTableDiv'><table id='systemTable'><tbody>"
		for(var i in sysUrls){
			table=table+"<tr><td><a href='"+sysUrls[i].url+"'>"+sysUrls[i].name+"</a></td></tr>";
		}		
		table=table	+ "<tr><td onclick='showRoadMap()'><a href='#'><strong>系统</strong></a></td></tr>"
				+ "</tbody></table></div>";
		$('body').append(table);
		addSysClickEvent();
	}
	$('#sysTableDiv').show();
	var position = $(obj).position();
	$('#sysTableDiv').css('top', (position.top + 36) + 'px');
	$('#sysTableDiv').css('right', '0px');
}
function addSysClickEvent() {
	// $('#systemTable tbody tr td').click(function(){
	// var title = $('#lastSys').children('span').text().replace('<span><span
	// class="flat">', '');
	// $('#lastSys').html('<span><span
	// class="flat"></span>'+$(this).html()+'</span>');
	// $(this).html(title);
	// });
}
var styles = [ 'black', '酷黑', 'sky-blue', '天蓝' ];
function changeStyle(event, obj) {
	$('#sysTableDiv').hide();
	event.cancelBubble = true;
	if ($('#styleList').attr('id') != 'styleList') {
		var table = "<div id='styleList'><table id='styleTable'><tbody><tr>";
		for ( var i = 0; i < styles.length; i = i + 2) {
			table += ("<td><img src='../../images/" + styles[i]
					+ ".gif'/><br/><span>" + styles[i + 1] + "</span></td>");
		}
		table += "</tr></tbody></table></div>";
		$('body').append(table);
		addStytleClickEvent();
	}
	$('#styleList').show();
	var position = $(obj).position();
	$('#styleList').css('top', '66px');
	$('#styleList').css('right', '76px');
}
function addStytleClickEvent() {
	$('#styleTable tbody tr td').click(function() {
		var array = $(this).children('img').attr('src').split('/');
		var style = array[array.length - 1].replace('.gif', '');
		$.cookie('demo-theme-cookie', style, {
			expires : 7,
			path : '/'
		});
		style = '../../css/' + style + '/jquery-ui-1.8.16.custom.css';
		$('#_style').attr('href', style);
	});
}

$(document).ready(function() {
	if(topMenu=="")return;
	//选中相应的系统菜单
	$('#' + topMenu).addClass('top-selected');
	//如果当前选中的系统是默认不显示的系统则需要作相应的替换
	if(sysUrls[topMenu]!=null){
		var lastSys=$('#lastSys');
		lastSys.attr('href',sysUrls[topMenu].url);
		lastSys.addClass('top-selected');
		//$('span',lastSys).text(sysUrls[topMenu].name);
		$('#lastSys').html('<span><span class="flat"></span>'+sysUrls[topMenu].name+'</span>');
	}
	
});

/**/
$(document).ready(function() {
	$("body").click( function () {
		$('#sysTableDiv').hide();
		$('#styleList').hide();
	}); 
	$("#opt-content").css("height",$(window).height()-155);
});