<%@page import="java.util.Map"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
</object> 
<script type="text/javascript">
	var LODOP; //声明打印的全局变量 
	function getScrollTop(){
		return $("#opt-content").height()+23;
	}
	
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkItemsParent").position().top + $("#checkItemsParent").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#checkItemsParent").find("div").width(width).offset();
		var contentWidth =  $("#checkItemsParent").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	
	var topMenu ='';
	var processingResult="";
	var $inputs;
	function enterkey(){
		$inputs=$("input:text,textarea","#inspectionForm");   
		$inputs.bind("keypress",keypress);
	}
	
	function keypress(e){
		var key = e.which;
		if(key==13){
			e.preventDefault();
			var index=$inputs.index(this); 
			if(index==$inputs.length-1){
				submitForm('${iqcctx}/inspection-report/save.htm');
				return false;
			}
			var $input=$inputs.eq(index++);  
			$input.change();
			$input=$inputs.eq(index); 
			if($input.attr("readonly")==true&&$input.attr("name").indexOf("Date")<0){
				$input=$inputs.eq(++index);
			}
			$input.focus();
			$input.select(); 
			return false;
		} 
	}
	
	$(document).bind("keydown",function(e){
		var key=e.which;
		if(e.ctrlKey==true&&key==83){//ctrl+s
			e.preventDefault();
			submitForm('${iqcctx}/inspection-report/save.htm');
		}else if(key==9){//tab键
			e.preventDefault(); 
			$obj=$("td[id=check-items-td]"); 
			if($obj.length>0){
				var active=this.activeElement;
				if(active){ 
					var input;
				   	$obj.find(":input").each(function(i,obj){  
						if(obj.name==active.name){
						  	input=obj;
						  	return ;
						} 
				   	});  
				   	if(input){
						input=$(input).change().parent(); 
						var index=$obj.index($(input))+1; 
						if(index<$obj.length){ 
							input=$obj.eq(index).find(":input").first();  
						}else{
							input=$obj.first().find(":input").first();
						}
					}else{
					   input=$obj.first().find(":input").first();
					} 
					input.focus();
					input.select();
				}
				//$obj.find(":input:first").focus();
			} 
		}else if(e.ctrlKey==true&&key==61){//ctrl+=
			e.preventDefault();
		}else if(e.ctrlKey==true&&key==173){//ctrl+-
			e.preventDefault();
		}     
	}).ready(function(){
		enterkey();
		$("#scroll").bind("scroll",function(){
			$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#checkItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
 		caculateTotalAmountInit();
		contentResize();
		
// 		if($("#unqualifiedAmountFlag")[0]==undefined){
// 			if(parseFloat($("#unqualifiedAmount").val())>0){
// 				$($("#processingResult").parent().prev()).prepend("<span id='unqualifiedAmountFlag' >不合格<br></span>");
// 			}else{
// 				$($("#processingResult").parent().prev()).prepend("<span id='unqualifiedAmountFlag' >合格<br></span>");
// 			}
// 		}else{
// 			if(parseFloat($("#unqualifiedAmount").val())>0){
// 				$("#unqualifiedAmountFlag").html("<span id='unqualifiedAmountFlag' >不合格<br></span>");
// 			}else{
// 				$("#unqualifiedAmountFlag").html("<span id='unqualifiedAmountFlag' >合格<br></span>");
// 			}
// 		}
	
		if($("input[name=isPPMCount][checked=checked]").val()==0){
			isPPMCountNoFunc();
		}else{
			isPPMCountYesFunc();
		}
		
		if("${inspectionState}" != "<%=IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT%>"&&"${inspectionState}" != "<%=IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK%>"){
			$("button").attr("disabled","disabled");
			$(":input").attr("disabled","disabled");
			$("#requestCheckNo").attr("readonly","").focus();
			$("a").removeAttr("onClick");
			if("${failflag}"!="failed"){
				$("#message").html("<font color=red>检验状态【${inspectionState}】,不能修改!</font>");
			}else{
				$("#message").html("<font color=red>${failmessage}</font>");
				alert('${failmessage}');
			}
			
			$(".opt-btn").find("button").attr("disabled","");
			return;
		}
		if("${inspectionState}" == "<%=IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK%>"){
			if("${failflag}"=="failed"){
				$("#message").html("<font color=red>${failmessage}</font>");
				alert('${failmessage}');
				return;
			}
		}
		var inspector = $("#inspector").val();
		if(!inspector){
			$("#inspector").val("<%=ContextUtils.getUserName()%>");
			$("#inspectorLoginName").val("<%=ContextUtils.getLoginName()%>");
		}
// 		$('#enterDate').datepicker({changeYear:'true',changeMonth:'true'});
		$('#schemeStartDate').datepicker({changeYear:'true',changeMonth:'true'});
// 		$('#cargoesStartDate').datepicker({changeYear:'true',changeMonth:'true'});
// 		$('#inspectionDate').datetimepicker({changeYear:'true',changeMonth:'true'});
		//$("#stockAmount").bind("change",function(){
			//if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				//loadCheckItems();
			//}
		//});
		//$("#inspectionForm").validate({});
		//getSupplierProduct();//供方准入产品
		bindCustomEvent();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		setTimeout(function(){
			$("#message").html("");
		},3000);
		//自动填写，供应商编号
		$("#supplierCode")
		.bind("click",function(){ 
			this.select();
		})
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisValue = $(this).attr("hisValue");
				if(this.value != hisValue){ 
					$(this).attr("hisValue",this.value); 
					$("#checkBomCode").val("");
					$("#checkBomCode").attr("hisValue","");
					$("#checkBomName").val("");
					$("#checkBomMaterialType").val("");
					$("#bomProperty").val("");
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200,
			source: function( request, response ) {
				response([{label:'数据加载中...',value:''}]);
				$.post("${supplierctx}/archives/read-supplier.htm",{"params.code":request.term,"params.state":"合格"},function(result){
					response(result); 
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.value){
					var supplierName=$("#supplierName");
					var his=obj.attr("hisValue");
					supplierName.val(ui.item.value);
					this.value = ui.item.label;
					if(ui.item.value != his){
						$("#checkBomCode").val("").attr("hisValue","");
						setBomValue("","","","");
					}
					$("#supplierCode").attr("hisValue",this.value);
				}else{
					$("#supplierName").val("");
					this.value = "";
					$("#supplierCode").attr("hisValue",""); 
					setBomValue("","","","");
				}
				return true;
			},
			close : function(event,ui){
				var supplierCode = $("#supplierCode");
				if(supplierCode.val()){
					var hisValue =supplierCode.attr("hisValue");
					if(supplierCode.val() != hisValue){
						supplierCode.val(hisValue);
					}
				}else{
					supplierCode.val('').attr("hisValue",'');
					$("#supplierName").val('');
				}
			}
		});
		
		//自动填写,物料编码
		$("#checkBomCode")
		.bind("click",function(){ 
			this.select();
		}) 
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisValue = $(this).attr("hisValue");
				if(this.value != hisValue){
					$(this).attr("hisValue",this.value); 
					//loadCheckItems();
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200, 
			source: function( request, response) {
				var supplierId=$("#supplierId").val();  
				if(supplierId==''){
					alert("请先选择供应商");
					$("#checkBomCode").val("");
					return true;
				} 
				response([{label:'数据加载中...',value:''}]);
				$.post("${carmfgctx}/common/search-materials.htm",{"searchParams":"{\"code\":\""+request.term+"\"}","label":"code"},function(result){
					response(result);
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				var checkBomCode=$("#checkBomCode");
				if(ui.item.value){
					checkBomCode.attr("hisValue",ui.item.code);
					setBomValue(ui.item.code,ui.item.name,ui.item.materialType,item.productType);
					loadCheckItems();
				}else{
					checkBomCode.attr("hisValue",""); 
					this.value = "";
					setBomValue("","","","");
				}
				return true;
			},
			close : function(event,ui){
				var checkBomCode = $("#checkBomCode");
				if(checkBomCode.val()){
					var hisValue = checkBomCode.attr("hisValue");
					if(checkBomCode.val() != hisValue){
						checkBomCode.val(hisValue);
					}
				}else{
					checkBomCode.val('').attr("hisValue",'');
					setBomValue("","","","");
				}
			}
		});
		//设置是否纳入PPM及纳入数
		changeKS();
		if('${speicalCheckFlag}'=='1'){
			$("#totalAualifiedAmount").attr("readonly","");
			$("#stockAmount").removeAttr("onblur");
		}
		

	});
	
	function changeKS(obj){
// 		var inspectionType = '';
// 		if(obj){
// 			inspectionType = obj.value;
// 		}else{
// 			inspectionType = $("#inspectionType").val();
// 		}
// 		if(inspectionType=='KS'||inspectionType=='K'){
// 			$("input[name='isPPMCount']").removeAttr("disabled");
// 			$("input[name='bringAmount']").removeAttr("disabled");
// 			if($("#stockAmount").val()){
// 				$("input[name='bringAmount']").val($("#stockAmount").val());
// 			}
// 		}else{
// 			$("input[name='isPPMCount']").attr("disabled", "disabled");
// 			$("input[name='bringAmount']").attr("disabled", "disabled");
// 			$("input[name='isPPMCount']").attr("checked",false);
// 		}
	}
	
	function changeProcessingResult(obj){
		var processingResult = obj.value;
		if(processingResult=="退货"){
			$("#unqualifiedAmount").val(($("#stockAmount").val()));
			validateUnqAmount();
		}
	}
	
	function submitForm(url){
		if($("#checkItemsParent").find("input[name='flagIds']").val()==''){
			alert("没有检验项目，请重新选择物料！");
			return ;
		}
		if($("#processingResult").val()==""){
			alert("处理结果未填写，请选择！");
			$("#processingResult").focus();
			return ;
		}
		if($("#remark1").val()==""){
			alert("描述不能为空！");
			$("#remark1").focus();
			return;
		}
		
		if($("#isPPMCountNo").attr("checked") && $("#inspectionType").val()=="KS"){
			alert("重新送检，必须纳入PPM统计！");
			$("#isPPMCountYes").attr("checked","checked");
			$("#bringAmount").removeAttr("disabled");
			return;
		}
		
		if($("#inspectionConclusion").val()=='NG' && $("#responsibleDept").val()==''){
			alert("检验结果为不合格，责任部门不能为空！");
			$("#responsibleDept").focus();
			return;
		}
		
		
		if('${speicalCheckFlag}'=='1'){
			if(parseFloat($("#stockAmount").val())!=(parseFloat($("#unqualifiedAmount").val())+parseFloat($("#totalAualifiedAmount").val()))){
				alert("特殊检验，不良数+总合格数不等于来料数量！");
				return ;
			}
		}
		
		if($("#inspectionForm").valid()){
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	//取消特殊检验
	function cancelSpecialInspecte(url){
		$("#speicalCheckFlag").val("0");
		if($("#inspectionForm").valid()){
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	//置为特殊检验
	function setSpecialInspecte(url){
		$("#speicalCheckFlag").val("1");
		if($("#inspectionForm").valid()){
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}

	function setBomValue(code,name,materialType,productType){
		var checkBomName=$("#checkBomName");
		var checkBomCode=$("#checkBomCode");
		var checkBomMaterialType=$("#checkBomMaterialType"); 
		var bomProperty=$("#bomProperty"); 
		checkBomName.val(name);
		checkBomCode.val(code);
		checkBomMaterialType.val(materialType);
		bomProperty.val(productType);
	}
	
	function checkBomClick(obj){
// 		var supplierCode=$("#supplierCode").val(); 
// 		if(supplierCode==''){
// 			alert('请选择供应商');
// 			return ;
// 		}
// 		var url = "${carmfgctx}/common/product-bom-select.htm?supplierCode="+supplierCode;
		var url = "${carmfgctx}/common/product-bom-select.htm";
		$.colorbox({href:url,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	
	function setFullBomValue(datas){
		var his = $("#checkBomCode").val();
		setBomValue(datas[0].code, datas[0].name, datas[0].materialType, datas[0].productType);
		if(datas[0].code != his){
			loadCheckItems();
		}
 	}
	
	function supplierClick(){
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm?state=有效",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	
	function setSupplierValue(objs){
		var obj = objs[0];
		var his = $("#supplierCode").val();
		$("#supplierName").val(obj.name);
		$("#supplierName").closest("td").find("span").html(obj.name);
		$("#supplierCode").val(obj.code);
		$("#supplierId").val(obj.id);
		if(obj.code != his){
// 			setBomValue("","","","");
		}
	} 
	
	function setSupplierProduct(result){
		var checkBomCode = $("#checkBomCode").val();
		if(result.length<=0){
			alert("对不起，该供方没有准入物料！");
			return false;
		}else{
			var isSelected = false;
			for(var i=0;i<result.length;i++){
				if(checkBomCode!=""&&checkBomCode!=null&&checkBomCode==result[i].code){
					$("#selectBomCode").append("<option value="+result[i].code+" selected='selected'>"+result[i].code+"</option>");
					isSelected = true;
				}else{
					$("#selectBomCode").append("<option value="+result[i].code+">"+result[i].code+"</option>");
				}
			}
			if(!isSelected){
				$("#checkBomName").val(result[0].name);
				$("#checkBomCode").val(result[0].code);
				$("#checkBomMaterialType").val(result[0].materialType);
				$("#bomProperty").val(result[0].productType);
			}
		}
	}
	
	function getBomNameByCode(){
		var code = $("#selectBomCode").val();
		$("#checkBomCode").val(code);
		$.post("${carmfgctx}/common/search-product-boms.htm",{searchParams:'{"code":"'+code+'"}',label:'code'},function(result){
			autoSetBomNam(result);
		},'json');
	}
	
	function autoSetBomNam(result){
// 		$("#checkBomName").val(result[0].value);
// 		$("#checkBomCode").val(result[0].key);
// 		$("#checkBomMaterialType").val(result[0].materialType);
// 		$("#bomProperty").val(result[0].productType);
	}
	
	function callback(){
		showMsg();
	}
	
	//选择部门
	function selectDept(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择部门',
			innerWidth:'400',
			treeType:'DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.id,
			showInputId:obj.id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	
	//选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId :hiddenInputId,
			showInputId : showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	
	/* var isLoadItems = false; */
	function loadCheckItems(){
		var stockAmountobj = $("#stockAmount");
		var stockAmount1=stockAmountobj.val();
		var hisNum=stockAmountobj.attr("hisNum");
		var stockAmount=0;
		try{
			stockAmount=parseInt(stockAmount1);
			if(isNaN(stockAmount)){
				stockAmount="";
	        }
			if(stockAmount!=stockAmount1){
				stockAmountobj.val(stockAmount);
			}
		}catch(err){ 
			stockAmountobj.val("");
		} 
		if(stockAmount<=0){
		 	stockAmountobj.val(hisNum);
		 	return ; 
		} 
		if(stockAmount>50000000){
		 	alert("来料数不能大于50000000");
		 	return ;
		}
		var inspectionDate = $("#inspectionDate").val();
		var checkBomCode = $("#checkBomCode").val();
		var supplierCode = $("#supplierCode").val();
		var inspectionAmount=$("#inspectionAmount").val();
		var checkBomMaterialType=$("#checkBomMaterialType").val();
		if(false){ 
			return ;
		}else{
			var params = {
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				inspectionDate : inspectionDate,
				inspectionAmount:inspectionAmount,
				checkBomMaterialType : checkBomMaterialType,
				flagIds : $(":input[name=flagIds]").val()
			};
			$("#checkItemsParent").find(":input[fieldName=countType]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=checkItemName]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=results]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[results=true]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#scroll").hide();
			$("#checkItemsParent").find("div").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${iqcctx}/inspection-report/check-items.htm";
			/* isLoadItems = true; */
			$("#checkItemsParent").find("div"). load(url,params,function(){
				/* isLoadItems = false; */
				$("#inspectionForm").validate({});
				bindCustomEvent();
				//更新不合格数和合格数
				$(".checkItemsClass").each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
				var contentWidth =  $("#checkItemsParent").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();  
				enterkey();
			});
			stockAmountobj.attr("hisNum",stockAmount);
		}
	}
	 
	function addResultInput(obj,checkItemName){
		var tdLen = $(obj).parent().find("input").length;
		if(tdLen==32){
			alert("最多能添加32项！");
		}else{
			var countTypeName = $(obj).closest("tr").find(":input[fieldName=countType]").attr("name");
			var flagIndex = countTypeName.split("_")[0];
			var html = "<input results=\"true\" color=\"black\" style=\"width:33px;float:left;margin-left:2px;\" title=\""+checkItemName+"样品"+(tdLen+1)+"\" fieldName=\"result"+(tdLen+1)+"\" name=\""+flagIndex+"_result"+(tdLen+1)+"\"  class=\"{number:true,min:0}\"></input>";
			$(obj).before(html); 
			//更新顶部的宽度
			updateCheckItemsHeaderWidth();
			$lastinput=$(obj).parent().find("input").last();
			$lastinput.bind("keypress",keypress)
			.bind("change",resultChange);
			$inputs=$("input:text,textarea","#inspectionForm");
		}
	}
	
	function removeResultInput(obj){ 
		$children=$(obj).parent().find("input"); 
		if($children.length==1){
			return ;
		}
		$children.last().remove(); 
		$inputs=$("input:text,textarea","#inspectionForm");
		//更新顶部的宽度
		updateCheckItemsHeaderWidth();
		//初始化滚动条
		initScrollDiv();
	}
	
	//更新顶部的宽度
	function updateCheckItemsHeaderWidth(){
		var maxLen = 10;
		$(".checkItemsClass").each(function(index,obj){
			var len = $(obj).find("input").length;
			if(len>maxLen){
				maxLen = len;
			}
		});
		$("#checkItemsHeader").width(maxLen*38+70);
		//初始化滚动条
		initScrollDiv();
	}

	function addBadNum(obj){
		var qualifiedAmount=$(obj).closest("tr").find(":input[fieldName=qualifiedAmount]");
		var value=qualifiedAmount.val();
		if(isNaN(value)){
			value = 0;
		}else{
			value = parseFloat(value);
		}
		value--;
		if(value<0){
			return ;
		}  
		qualifiedAmount.val(value); 
		qualifiedAmount.change();
	}
	
	function removeBadNum(obj){
		var unqualifiedAmount=$(obj).closest("tr").find(":input[fieldName=unqualifiedAmount]");
		var value=unqualifiedAmount.val();
		if(isNaN(value)){
			value = 0;
		}else{
			value = parseFloat(value);
		}
		if(value<1){
			return ;
		}  
		var qualifiedAmount=$(obj).closest("tr").find(":input[fieldName=qualifiedAmount]");
		var hisVal = qualifiedAmount.val();
		qualifiedAmount.val(parseFloat(hisVal)+1); 
		qualifiedAmount.change();
	}
	
	//添加各种事件
	function bindCustomEvent(){
		//不合格数
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").bind("change",function(){
			unqualifiedAmountChange(this);
		});
		//合格数
		$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").bind("change",function(){
			var value=parseInt(this.value); 
			var hisamount=$(this).attr("hisamount");
			if(value>hisamount){
				value=hisamount;
			}
			this.value=value;
			var unqualifiedAmount=$(this).parent().next().find(":input[fieldName=unqualifiedAmount]");
			var value=hisamount-value; 
			unqualifiedAmount.val(value);
			unqualifiedAmount.change();
			//this.parent().next();
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				caculateTotalAmount();
			}
		});
		//计量的值不符合范围时的事件
		$(".checkItemsClass input").bind("change",resultChange);
	}
	
	function unqualifiedAmountChange(obj){
		if(!isNaN(obj.value)&&obj.value.indexOf(".")==-1){
			var parentTr = $(obj).parent().parent();
			var re = parentTr.find(":input[fieldName=aqlRe]").val();
			if(isNaN(re)){
				parentTr.find("span[name=conclusionSpan]").html("合格");
				parentTr.find(":input[fieldName=conclusion]").val("OK");
				parentTr.find(":input[fieldName=conclusion]").attr('color','');
				
			}else{
				if(parseInt(obj.value)<parseInt(re)){
					parentTr.find("span[name=conclusionSpan]").html("合格");
					parentTr.find(":input[fieldName=conclusion]").val("OK");
					parentTr.find(":input[fieldName=conclusion]").attr('color','');
				}else{
					parentTr.find("span[name=conclusionSpan]").html("<font color='red'>不合格</font>");
					parentTr.find(":input[fieldName=conclusion]").val("NG");
					parentTr.find(":input[fieldName=conclusion]").attr('color','red');
				}
			}
			caculateTotalAmount();
		}
	}
	
	function resultChange(){
		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find(":input[fieldName=maxlimit]").val(),minlimit = parentTr.find(":input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!this.value){
					$(this).css("color","black").attr("color","black");
				}else{
					var val = parseFloat(this.value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(val>=parseFloat(minlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else{//有上限和下限
						if(val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}
				}
				$(this).parent().each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
			}
		}
	}

	//计算总的检验数量
	function caculateTotalAmount(){
		if($("#checkItemsParent").find(":input[fieldName=conclusion]").length==0){
			var qualifiedRate = $("#qualifiedRate").val();
			try {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
			} catch (e) {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
			}
			return;
		}
		//总的判定
		var conclusionshow = '合格';
		var conclusion='OK';
		$("#checkItemsParent").find(":input[fieldName=conclusion]").each(function(index,obj){
			//if(obj.value!='合格'){alert(":"+obj.value);}
			if(obj.value=='NG'){
				conclusion ='NG';
				conclusionshow='<font color="red">不合格</font>';
				return false;
			}
		});
		$("input[name=inspectionConclusion]").val(conclusion);
		$("input[name=inspectionConclusion]").parent().find("span").html(conclusionshow);
		
		//自动带出处理方式
		<%-- if(conclusion=='OK'){
			$("#totalAualifiedAmount").val($("#stockAmount").val());
			$("#processingResult").empty();
			$("#processingResult").append("<option value=''></option>");
			<%
			Map<String,String> backResultMap3 =  IncomingInspectionService.getBackResultContrastOKMap();
			for(String result : backResultMap3.keySet()){
			%>
			$("#processingResult").append("<option value='"+<%=backResultMap3.get(result)%>+"'>"+"<%=result %>"+"</option>");
			<%} %>
		}else{
			$("#processingResult").empty();
			$("#processingResult").append("<option value=''></option>");
			<%
				Map<String,String> backResultMap2 =  IncomingInspectionService.getBackResultContrastNGMap();
				for(String result : backResultMap2.keySet()){
			%>
			$("#processingResult").append("<option value='"+<%=backResultMap2.get(result)%>+"'>"+"<%=result %>"+"</option>");
			<%} %>
			$("#totalAualifiedAmount").val(0);
		} --%>
		//检验数量
		var inspectionAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=inspectionAmount]").each(function(index,obj){
			if(obj.value){
				if(parseInt(obj.value)>inspectionAmount){
					inspectionAmount = parseInt(obj.value);
				}
			}
		});
		$("#inspectionAmount").val(inspectionAmount);
		$("#inspectionAmount").parent().find("span").html(inspectionAmount);
		//不合格数量 
		var unqualifiedAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
			if(obj.value&&!isNaN(obj.value)){
				unqualifiedAmount += parseInt(obj.value);
			}
		});
		if(unqualifiedAmount>inspectionAmount){
			unqualifiedAmount = inspectionAmount;
		}
		$("#unqualifiedAmount").val(unqualifiedAmount);
		$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
		//合格数量 
		var qualifiedAmount = inspectionAmount - unqualifiedAmount;
		if(qualifiedAmount<0){
			qualifiedAmount = 0;
		}
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);

		var qualifiedRate = 1;
		if(inspectionAmount>0){
			qualifiedRate = qualifiedAmount/(inspectionAmount*1.0);
		}
		$("#qualifiedRate").val(qualifiedRate); 
		if(unqualifiedAmount>0){
			//processingResult[1].checked=true;
			$("#processingResult").val("待审核");
		}
		try {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
		} catch (e) {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
		}
	}
	//计算总的检验数量----页面加载时使用
	function caculateTotalAmountInit(){
		if($("#checkItemsParent").find(":input[fieldName=conclusion]").length==0){
			var qualifiedRate = $("#qualifiedRate").val();
			try {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
			} catch (e) {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
			}
			return;
		}
		//总的判定
		var conclusionshow = '合格';
		var conclusion='OK';
		$("#checkItemsParent").find(":input[fieldName=conclusion]").each(function(index,obj){
			//if(obj.value!='合格'){alert(":"+obj.value);}
			if(obj.value=='NG'){
				conclusion ='NG';
				conclusionshow='<font color="red">不合格</font>';
				return false;
			}
		});
		$("input[name=inspectionConclusion]").val(conclusion);
		$("input[name=inspectionConclusion]").parent().find("span").html(conclusionshow);
		//检验数量
		var inspectionAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=inspectionAmount]").each(function(index,obj){
			if(obj.value){
				if(parseInt(obj.value)>inspectionAmount){
					inspectionAmount = parseInt(obj.value);
				}
			}
		});
		$("#inspectionAmount").val(inspectionAmount);
		$("#inspectionAmount").parent().find("span").html(inspectionAmount);
		//不合格数量 
		var unqualifiedAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
			if(obj.value&&!isNaN(obj.value)){
				unqualifiedAmount += parseInt(obj.value);
			}
		});
		if(unqualifiedAmount>inspectionAmount){
			unqualifiedAmount = inspectionAmount;
		}
		$("#unqualifiedAmount").val(parseInt('${unqualifiedAmount}').toFixed(0));
		$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
		//合格数量 
		var qualifiedAmount = inspectionAmount - unqualifiedAmount;
		if(qualifiedAmount<0){
			qualifiedAmount = 0;
		}
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);

		var qualifiedRate = $("#qualifiedRate").val();
		$("#qualifiedRate").val(qualifiedRate); 
		try {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
		} catch (e) {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
		}
	}
	//计算不合格数量和合格数 
	function caculateUqualifiedAmount(obj){
		var count = 0;
		$(obj).find("input").each(function(index,obj){
			if($(obj).attr("color")=='red'){
				count++;
			}
		});
		
		var $unqualifiedAmount = $(obj).parent().find(":input[fieldName=unqualifiedAmount]");
		$unqualifiedAmount.val(count);
		$unqualifiedAmount.parent().find("span").html(count);
		
		var qualifiedAmount = 0;
		var inspectionAmount = $(obj).parent().find(":input[fieldName=inspectionAmount]").val();
		if(inspectionAmount&&!isNaN(inspectionAmount)){
			qualifiedAmount = parseInt(inspectionAmount)-count;
			if(qualifiedAmount<1){
				qualifiedAmount=0;
			}
		}
		var $qualifiedAmount = $(obj).parent().find(":input[fieldName=qualifiedAmount]");
		$qualifiedAmount.val(qualifiedAmount);
		$qualifiedAmount.parent().find("span").html(qualifiedAmount);
		
		if($unqualifiedAmount.length>0){
			unqualifiedAmountChange($unqualifiedAmount[0]);
		}
	}
	
	//上传附件
	function uploadFiles(){
		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
	}
	
	//判断是否发起过改进
	function isHasSubmitImprove(){
		var id = '${id}';
		var sourceNo = '${inspectionNo}';
		var processingResult = $("#processingResult").val();;
		if(id=="undefined"||id==""){
			alert("请先执行单据保存操作！");
			return false;
		}else if(processingResult=="接收"){
			alert("检验合格，无需发起异常处理！");
			return false;
		}else{
			$.post("${improvectx}/correction-precaution/is-submit-improve.htm",{sourceNo:sourceNo},function(result){
				submitImprove(result);
			},'json');
		}
	}
	
	function submitImprove(result){
		var id = '${id}';
 		var url='${improvectx}/correction-precaution/called-input.htm?iqcInspectionReportId='+id;
 		if(result.error){
 			if(confirm(result.message)){
 				$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
 		 			overlayClose:false,
 		 			title:"异常处理",
 		 			onClosed:function(){}
 		 		});
 			}
 		}else{
 			$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"异常处理",
	 			onClosed:function(){}
	 		});
 		}
 	}
	
	//导出
	function input_export(){
		var id = '${id}';
		var current = 0;
		var dd = setInterval(function(){
			current++;
			var str = '';
			for(var i=0;i<(current%3);i++){
				str += "...";
			}
		}, 500);
		$("#iframe").bind("readystatechange",function(){
			clearInterval(dd);
			$("#message").html("");
			$("#iframe").unbind("readystatechange");
		}).attr("src","${iqcctx}/inspection-report/download-report.htm?id="+id);
	}
	
 	//发起不合格品处理流程
	function beginDefectiveGoodsProcessForm(){
		$.post("${iqcctx}/inspection-report/re-check-unqualified.htm",{id:'${id}'},function(result){
			if(result.error){
				$("#message").html("<b style='color:red;'>"+result.message+"</b>");
				alert(result.message);				
			}else{
				if(confirm("您确定要发起不合格品处理吗？")){
		 			window.location.href = '${iqcctx}/defective-goods/input.htm?unqualifiedSourceNo=${inspectionNo}&unqualifiedSource=iqc';
		 		}else{
		 			return;
		 		}
			}
		},'json');
	}
	
	function prn1_preview() {	
		CreateOneFormPage();	
		LODOP.PREVIEW();	
	}
	
	function CreateOneFormPage(){
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml = strBodyStyle+"<body>"+document.getElementById("inspectionForm").innerHTML+"</body>";

		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
	}
	
	function SaveAsFile(){
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("inspectionForm").innerHTML+"</body>";

		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_TABLE(0,0,"100%","100%",strFormHtml);

		LODOP.SET_SAVE_MODE("QUICK_SAVE",true);//快速生成（无表格样式,数据量较大时或许用到）
		LODOP.SAVE_TO_FILE("进货检验报告.xlsx");
	}
	
	function goback(){
		window.location="${iqcctx}/inspection-report/un-check.htm";
	}
	
	function showImprove(id){
		$.colorbox({href:'${improvectx}/correction-precaution/view-info.htm?id='+id,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"页面详情"
		});
	}
	
	function showPicture(name){
		var src="${iqcctx}/inspection-report/show-indicator-picture.htm?mid="+name;
		window.open(src,'','toolbar=no,resizable=yes,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,top=50,left=50,width=950,height=550') ;  
	}
	
	<jsp:include page="audit-method.jsp" />
	
	function quickSearch(){
		$.colorbox({href:"${iqcctx}/inspection-report/select-report.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择待检验的报告"
		});
	}
	
	function setReportValue(objs){
		window.location.href = '${iqcctx}/inspection-report/input.htm?id=' + objs[0].id;
	} 
	//验证不良数不能超过检验数
	function validateUnqAmount(){
		if(parseInt($("#unqualifiedAmount").val())>parseInt($("#inspectionAmount").val())){
			alert("不良数不能大于检验数！");
			$("#unqualifiedAmount").val($("#inspectionAmount").val());
			$("#qualifiedAmount").val(0);
			$("#qualifiedAmount").prev().html(0);
		}
		var inspectionAmount = $("#inspectionAmount").val();
		//不合格数量 
		var unqualifiedAmount = $("#unqualifiedAmount").val();
		if(unqualifiedAmount>inspectionAmount){
			unqualifiedAmount = inspectionAmount;
		}
		//合格数量 
		var qualifiedAmount = inspectionAmount - unqualifiedAmount;
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#qualifiedAmount").prev().html(qualifiedAmount);
		if(qualifiedAmount<0){
			qualifiedAmount = 0;
		}

		var qualifiedRate = 1;
		if(inspectionAmount>0){
			qualifiedRate = qualifiedAmount/(inspectionAmount*1.0);
		}
		$("#qualifiedRate").val(qualifiedRate); 
		if(unqualifiedAmount>0){
			$("#processingResult").val("待审核");
		}
		try {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
		} catch (e) {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
		}
		
		<%-- if((parseFloat($("#unqualifiedAmount").val())>0)&&('${speicalCheckFlag}'=='1')){
			//不合格
			$("#processingResult").empty();
			$("#processingResult").append("<option value=''></option>");
			<%
				Map<String,String> backResultMap5 =  IncomingInspectionService.getBackResultContrastNGMap();
				for(String result : backResultMap5.keySet()){
			%>
			$("#processingResult").append("<option value='"+<%=backResultMap5.get(result)%>+"'>"+"<%=result %>"+"</option>");
			<%} %>
		}else if((parseFloat($("#unqualifiedAmount").val())==0||isNaN(parseFloat($("#unqualifiedAmount").val())))&&('${speicalCheckFlag}'=='1')){
			//合格
			$("#processingResult").empty();
			$("#processingResult").append("<option value=''></option>");
			<%
			Map<String,String> backResultMap6 =  IncomingInspectionService.getBackResultContrastOKMap();
			for(String result : backResultMap6.keySet()){
			%>
			$("#processingResult").append("<option value='"+<%=backResultMap6.get(result)%>+"'>"+"<%=result %>"+"</option>");
			<%} %>
		} --%>
	}
	
	function isPPMCountNoFunc(){
		$("#bringAmount").val(0);
		$("#bringAmount").attr("disabled","disabled");
	}
	function isPPMCountYesFunc(){
		$("#bringAmount").removeAttr("disabled");
	}
	
	function totalAualifiedAmountChange(){
		if(parseFloat($("#totalAualifiedAmount").val())>0){
			//合格
			$("input[name=inspectionConclusion]").val("OK");
			$("input[name=inspectionConclusion]").parent().find("span").html("<font>合格</font>");
		}else{
			//不合格
			$("input[name=inspectionConclusion]").val("NG");
			$("input[name=inspectionConclusion]").parent().find("span").html("<font color='red'>不合格</font>");
		}
	}
	
	function totalAualifiedAmountKeyup(obj){
		if(('${speicalCheckFlag}'=='1')){
			if($("#stockAmount").val()-obj.value>=0){
				$("#unqualifiedAmount").val($("#stockAmount").val()-obj.value);
				
				<%-- if((parseFloat($("#unqualifiedAmount").val())>0)&&('${speicalCheckFlag}'=='1')){
					//不合格
					$("#processingResult").empty();
					$("#processingResult").append("<option value=''></option>");
					<%
						Map<String,String> backResultMap9 =  IncomingInspectionService.getBackResultContrastNGMap();
						for(String result : backResultMap9.keySet()){
					%>
					$("#processingResult").append("<option value='"+<%=backResultMap9.get(result)%>+"'>"+"<%=result %>"+"</option>");
					<%} %>
				}else if((parseFloat($("#unqualifiedAmount").val())==0||isNaN(parseFloat($("#unqualifiedAmount").val())))&&('${speicalCheckFlag}'=='1')){
					//合格
					$("#processingResult").empty();
					$("#processingResult").append("<option value=''></option>");
					<%
					Map<String,String> backResultMap11 =  IncomingInspectionService.getBackResultContrastOKMap();
					for(String result : backResultMap11.keySet()){
					%>
					$("#processingResult").append("<option value='"+<%=backResultMap11.get(result)%>+"'>"+"<%=result %>"+"</option>");
					<%} %>
				} --%>
				
			}else{
				alert("总合格数不能大于来料数！");
				return ;
			}
		}
	}
	//张顺志
	function update(id){
		$.colorbox({
			href:'${iqcctx}/inspection-report/audit-input.htm?id=' +id,
			iframe:true, 
			innerWidth:400, 
			innerHeight:300,
			overlayClose:false,
			title:"修改产品车型和纳入统计数"
		});
	}
</script>
