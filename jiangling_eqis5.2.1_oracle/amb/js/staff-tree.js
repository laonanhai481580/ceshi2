var okEnsure;
var singleId;
var singleName;
var singleType;
var singleLoginName;
var sInfor;

var mId;
var mName;
var mType;
var mLoginName;
var infor;
var userInfo;
function popTree(paramater){
	var title = paramater.title;
	var innerWidth = paramater.innerWidth;
	var treeType = paramater.treeType;
	var defaultTreeValue = paramater.defaultTreeValue;
	var leafPage = paramater.leafPage;
	var treeTypeJson = paramater.treeTypeJson;
	var multiple = paramater.multiple;
	var hiddenInputId = paramater.hiddenInputId;
	var showInputId = paramater.showInputId;
	var acsSystemUrl = paramater.acsSystemUrl;
	var callBack = paramater.callBack;
	
	if(hiddenInputId==""||showInputId=="") {alert("请设定隐藏域和显示域id!");return;}
	if(treeType==''||treeType==null){treeType="COMPANY" ;}
	if(leafPage=="true"&&treeTypeJson==''){alert("请设定页签参数!");return;}
	
	if(typeof(title)=='undefined'||typeof(innerWidth)=='undefined'){title='选择';innerWidth='300';}
	
	if(typeof(title)=='defaultTreeValue'||defaultTreeValue==''){defaultTreeValue="id";}
	//var url=webRoot+'/popTree.action?treeType='+treeType
	//var url='http://192.168.1.99:8000/acs/tags/tree!popTree.action?treeType='+treeType
	var url = acsSystemUrl+"/popTree.action?treeType="+treeType
			+'&multiple='+multiple+'&hiddenInputId='
			+hiddenInputId+'&showInputId='+showInputId
			+'&callBack='+callBack+'&treeTypeJson='+treeTypeJson
			+'&leafPage='+leafPage+'&defaultTreeValue='+defaultTreeValue;
	$.colorbox({href:encodeURI(url),iframe:true, innerWidth:innerWidth, innerHeight:400,overlayClose:false,title:title,onClosed:function(){if(okEnsure=='OK'){callBack.call();okEnsure='';}}});
}

    function getId(){
		return singleId;
	}	
	function getName(){
		return singleName;
	}	
		
	function getType(){
		return singleType;
	}
	function getLoginName(){
		return singleLoginName;
	}
	function getSingleInfor(){
		return sInfor;
	}
	
	function getIds(){return mId;};
	function getNames(){return mName;};
	function getTypes(){return mType;};
	function getLoginNames(){return mLoginName;};
	function getInfos(){return infor;}
	function getUserInfos(){return userInfo;}