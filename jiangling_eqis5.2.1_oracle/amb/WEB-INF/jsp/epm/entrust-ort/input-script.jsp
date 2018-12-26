<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function addExceptionSingle(obj) {
		var str = $(obj).parent().prev().find("textarea[stage='three']");
		var name = str.attr("name");
		var prev = name.split("_")[0];
		var id = $("#" + prev + "_ortId").val();
		var str = $("#currentActivityName").val();

		if (!id) {
			alert("请先保存！");
			return;
		} else if (str != null && str == "实验室测试") {
			window
					.open('${epmctx}/exception-single/input.htm?type=ort&&formId='
							+ id);
		} else if (str != null && str == "上传报告") {
			window
					.open('${epmctx}/exception-single/input.htm?type=ort&&formId='
							+ id);
		} else if (str != null && str == "报告审核") {
			window
					.open('${epmctx}/exception-single/input.htm?type=ort&&formId='
							+ id);
		} else if (str != null && str == "流程结束") {
			window
					.open('${epmctx}/exception-single/input.htm?type=ort&&formId='
							+ id);
		}
	}
	function showInput(obj) {
		var str = $("#currentActivityName").val();
		if (str == '' || str == '填写委托单') {
			var chk = "";
			chk = $("input[name='category']:checked").val();
			var i = document.getElementById("abnormalOdd");
			if (chk == '回归验证') {
				i.type = '';
			} else {
				i.type = 'hidden';
			}
		}
	}
	$(function() {
		var $categorys = $("input[name=category]");
		$("#p").click(function(e) {
			$categorys.attr("checked", false);
		});
	});
	function getScrollTop() {
		return $("#opt-content").height() + 23;
	}
	function checkScrollDiv() {
		var scrollTop = getScrollTop();
		var tableTop = $("#checkItemsParent").position().top
				+ $("#checkItemsParent").height() - 18;
		if (tableTop < scrollTop) {
			$("#scroll").hide();
		} else {
			$("#scroll").show();
		}
	}
	function initScrollDiv() {
		var width = $(".ui-layout-center").width() - 30;
		var offset = $("#checkItemsParent").find("div").width(width).offset();
		var contentWidth = $("#checkItemsParent").find("table").width();
		$("#scroll").width(width).css("top", getScrollTop() + "px").find("div")
				.width(contentWidth);
	}
	function contentResize() {
		initScrollDiv();
		checkScrollDiv();
	}
	$(document).ready(function() {
		contentResize();

	});
	function addRowHtml(aObj) {
		var $tr = $(aObj).closest("tr");
		var clonetr = $tr.clone(false);
		$tr.after(clonetr);
		var tableName = $tr.attr("tableName");
		var tableParams = _ambWorkflowFormObj.children[tableName];
		var maxIndex = tableParams['maxIndex'] + 1;
		tableParams['maxIndex'] = maxIndex;
		var flag = tableParams['inputNamePrefix'] + maxIndex;
		clonetr.attr("trPrefix", flag);
		//重置对象
		clonetr
				.find(":input[fieldName]")
				.unbind()
				.removeClass("hasDatepicker")
				.each(
						function(index, input) {
							$input = $(input);
							//清除值
							if (_ambWorkflowFormObj.childrenInitParams.addRowToClearValue) {
								$input.val("");
								if ($input.attr("title") != "") {
									$input.attr("title", "");
								}
							}
							var id = flag + "_" + $input.attr("fieldName");
							$input.attr("name", id).attr("id", id);
							var hiddenInputName = $input
									.attr("hiddenInputName");
							if (hiddenInputName) {
								$input.attr("hiddenInputId", flag + "_"
										+ hiddenInputName);
							}
							var showInputName = $input.attr("showInputName");
							if (showInputName) {
								$input.attr("showInputId", flag + "_"
										+ showInputName);
							}
						});
		//移除自动添加的对象
		clonetr.find("[autoAppend]").remove();
		clonetr.find(":input[isFile]").removeAttr("showInputId");
		//初始化新增行的输入元素
		_initInputForScope(clonetr);
		//更新序号
		_updateRowNum(tableName);
		clickTime();
		//检查是否有回调事件
		if ($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterAddRow)) {
			_ambWorkflowFormObj.childrenInitParams.afterAddRow(tableName,
					clonetr);
		}
		$("#" + flag + "_factoryClassify").val($("#factoryClassify").val());
	}
	var selectIndex = "";
	var selectIndex1 = "";
	var orderId = "";
	function selectProject(obj) {
		var aa = obj.previousElementSibling;
		orderId = aa.id.split("_")[0];
		selectIndex1 = obj.parentNode.parentNode;
		var customerNo = $("#customerNo").val();
		var productNo = $("#productNo").val();
		var sampleType = $("#sampleType").val();
		var modelName = $("#modelName").val();
		$.colorbox({
			href : "${epmctx}/base-info/ort-item/select-list.htm?customerNo="
					+ customerNo + "&productNo=" + productNo + "&sampleType="
					+ sampleType+"&modelName"+modelName,
			iframe : true,
			width : $(window).width() < 700 ? $(window).width() - 100 : 900,
			height : $(window).height() < 400 ? $(window).height() - 100 : 600,
			overlayClose : false,
			title : "选择科目"
		});

	}
	function setProjectValue(datas) {
		var idFirst = orderId.split("_")[0];
		var a = idFirst.slice(0, 2);
		var b = idFirst.slice(2);
		for (var i = 0; i < datas.length; i++) {
			$("#" + a + b + "_properties").val(datas[i].itemName);
			$("#" + a + b + "_testCondition").val(datas[i].condition);
			$("#" + a + b + "_testNumber").val(datas[i].count);
			$("#" + a + b + "_criterionG").val(datas[i].standardGreen);
			$("#" + a + b + "_criterionY").val(datas[i].standardYellow);
			$("#" + a + b + "_criterionR").val(datas[i].standardRed);

			$("#" + a + b + "_quickResponse").val(datas[i].quickResponse);
			$("#" + a + b + "_testStation").val(datas[i].testStation);
			$("#" + a + b + "_equipmentNumber").val(datas[i].equipmentNumber);
			$("#" + a + b + "_testItem").val(datas[i].testItem);
			$("#" + a + b + "_lower").val(datas[i].lower);
			$("#" + a + b + "_upper").val(datas[i].upper);
			$("#" + a + b + "_testValue").val(datas[i].testValue);
			$("#" + a + b + "_upload").val(datas[i].upload);

			b++;
			if (i != datas.length - 1) {
				addRowHtml(selectIndex1);
			}
		}
		setweigh();
	}
	function alterException(obj) {
		var str = $("#currentActivityName").val();
		str = "流程结束";
		if ("流程结束" != str) {
			alert("该流程没结束");
			return;
		}
		$.post('${epmctx}/entrust-ort/input.htm?id=' + obj + "&&type=ort&&str="
				+ str, function(data) {
			window.location.href = '${epmctx}/entrust-ort/input.htm?id=' + obj
					+ "&&str=" + str;
		});
	}
	function subControl(a) {
		if (a == "流程结束") {
			return;
		}
		var str = $("#currentActivityName").val();
		if (!str) {
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
		} else if (str != null && str == "实验室排程") {
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});

		} else if (str != null && str == "实验室测试") {
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});

			$("select:[fieldname='testAfter']").each(function(index, obj) {
				$(obj).addClass("{required:true, messages:{required:'必填'}}");
			});
		} else if (str != null && str == "流程结束") {
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("select:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});

		} else if (str != null && str == "填写委托单") {
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});

		} else {
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled", "disabled");
			});
		}
	}

	function Epm() {
		$.colorbox({
			href : "${epmctx}/base-info/ort-item/list-view.htm",
			iframe : true,
			width : $(window).width() < 1000 ? $(window).width() - 100 : 1000,
			height : $(window).height() < 600 ? $(window).height() - 100 : 600,
			overlayClose : false,
			title : "选择项目"
		});
	}
	function setEpmValue(objs) {
		var obj = objs[0];
		$("#customerNo").val(obj.customerNo);
		$("#productNo").val(obj.model);
		$("#sampleType").val(obj.samplType);
		$("#modelName").val(obj.modelName);
	}
	function printPage() {
		var url = '${epmctx}/entrust-ort/print-page.htm?&formNo=${formNo}';
		window.open(url);
	}
	var weights = 0;
	function setweigh(obj) {
		var m = 0;
		var n = 0;
		var controls = $("tr[zbtr1=true]").find('input[fieldname=testNumber]');
		for (var i = 0; i < controls.length; i++) {
			m = parseInt(controls[i].value);
			if (isNaN(m)) {
				m = 0;
			}
			n += parseFloat(m);
		}
		weights = parseFloat(n);
		$("#quantity").val(weights);
		return true;
	};
	function isTestResult() {
		var result = $("tr[zbtr1=true]").find('select[fieldname=testAfter]');
		for (var i = 0; i < result.length; i++) {
			if (result[i].value == '') {
				$("#textResult").val("");
			} else if (result[i].value == 'NG') {
				$("#textResult").val("不合格");
				return;
			} else {
				$("#textResult").val("合格");
			}
		}
	}
	function chekced() {
		$("input[type='radio']").removeAttr('checked');
	}
	function badRate(obj) {
		var idFirst = obj.id.split("_")[0];
		var teb = $("#" + idFirst + "_testNumber").val();
		if (!teb) {
			alert("数量不能为空");
			return;
		}
		var deb = $("#" + idFirst + "_defectNumber").val();
		var inb = $("#" + idFirst + "_invalidNumber").val();
		if (inb == "") {
			inb = 0;
		}
		var debs = parseInt(deb) + parseInt(inb);
		if (debs > parseInt(teb)) {
			alert("不良数量不能大于数量");
			$("#" + idFirst + "_defectNumber").val(0);
			$("#" + idFirst + "_defectRate").val('');
			return;
		}
		var sun = deb / (teb - inb) * 100;
		var sun1 = deb + "/" + (teb - inb);
		// 		var a=idFirst.slice(0,2);
		// 		var b=idFirst.slice(2);
		$("#" + idFirst + "_defectRate").val(sun1);
		console.log(sun);

	}
	function clickTime() {
		$("input:[isTime='true']").each(function(index, obj) {
			$(obj).datetimepicker({
				changeYear : 'true',
				changeMonth : 'true'
			});
		});
	}
	

	

	 
	function setProj(datas) {
			var idFirst = orderId.split("_")[0];
			var a = idFirst.slice(0, 2);
			var b = idFirst.slice(2);
			for (var i = 0; i < datas.length; i++) {
				var  lower=	$("#" + a + b + "_lower").val();
			    var  upper= $("#" + a + b + "_upper").val();
			    var  testValue=$("#" + a + b + "_testValue").val();
				 if(lower>testValue ){
				   alert("测试值不能小于规格下限值");
				}
			     if(uppertestValue>upper){
				   alert("测试值不能大于规格上线值");
			    }
				b++;
			/* 	if (i != datas.length - 1) {
					addRowHtml(selectIndex1);
				} */
			}
			//setweigh();
		}
	 
	 

	
</script>