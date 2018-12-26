<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
	body{
		overflow:auto;
	}
	body a{
		color:#b9a69f;
	}
	.nav_head{
		width:100%;
		height:2rem;
		background-color:blue;
		position:fixed;	
		z-index: 99999999;
		top:0;
		left:0;
		text-align:center;
	}
	.nav_head span{
		line-height:2rem;
		font-size:1rem;
		color:#ffffff;
		margin-left: -1.5rem;
	}
    .nav_head img{
		height: 1.5rem;
		width: 1.5rem;
		float: left;
		margin: 0.25rem 0 0.25rem 0.5rem;
	}
	.all_tab{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 2.5rem 3% 0;
		border-radius: 0.4rem;
		display: block;
	}
	.all_tab th{
		width:100%;
		height:2rem;
		text-align:left;
		display: block;
		text-indent: 1rem;
		line-height: 2rem;
		font-weight: 400;
	}
	.all_tab tr{
		width:100%;
		height:2rem;
		border-bottom:0.05rem solid #b9a69f;
		display: block;
	}
	.all_tab tr:last-child{
		border-bottom:0;
	}
	.all_tab td{
		width:50%;
		height:2rem;
		display: block;
		float:left;
	}
	.all_tab tbody{
		display: block;
	}
	.td_left{
		font-size: 0.4rem!important;
		color: #b9a69f;
		line-height: 1.9rem;
		text-align:right;
	}
	.td_right{
		text-align:left;
	}
	.text-inp{
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	.select-inp{
		height: 1.4rem;
		width: 50%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		font-size: 10px;
	}
	.method{
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	.last_td{
		width:100%!important;
		color:red;
		font-size:0.6rem;
		text-align:right;
		line-height:2rem;
		padding-right: 0.5rem;
	}
	.badproduct{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	#badform{
		width:100%;
		height:2rem;
		text-align:center;
	}
	.badselect{
		width:80%;
		height:1.4rem;
		margin-top:0.3rem;
		padding: 2px;
		border-radius: 0.3rem;
		font-size: 0.7rem;
		color: #b9a69f;
	}
	#processForm{
		background: #000000;
		opacity: 0.7;
		z-index: 1000;
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		
	}
	#processFormNk {
		background: #5ec0ec;
		width: 76%;
		height: 75%;
		z-index: 1111;
		position: fixed;
		margin: 30% 12%;
		top: 0;
	}
	/* .surface{
		display:none;
		width:90%;
		border: 0.05rem solid #b9a69f;
		margin:0 auto;
	} */
	.surface li{
		width:100%;
		height:2rem;
		border-bottom:0.01rem solid #9e9e9e;
	}
	/* .surface ul > li:last-child{
		border-bottom:0;
	} */
	.surface span{
		display:inline-block;
		width:30%;
		height:2rem;
		font-size:0.7rem;
		color: #b9a69f;
		line-height:2rem;
		text-align:center;
	}
	.delineate{
		height: 1.4rem;
		width: 100%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		padding: 1px;
		font-size: 10px;
		text-align: right;
	}
	.surface a{
		display: inline-block;
		width: 60%;
	}
	.surface a:after{
		content:"%";
		color:#000000;
		vertical-align: middle;
	}
	.surface form{
		display: inline-block;
		width: 60%;
	}
	.admit_infor{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
		height:2.2rem;
	}
	.admit_infor ul{
		width:100%;
		height:2.2rem;
	
	}
	.admit_infor li{
		float:left;
		height:2rem;
		width:30%;
		text-align:right;
	}
	.admit_infor li:first-child{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height:2rem;
		text-align:left;
		text-indent: 0.5rem;
		width:70%;
	}
	#down_select1,#down_select2,#down_select3,#down_select4,#down_select5,#down_select6,#down_select7{     /*下拉菜单input样式*/
		height:1rem;
		width:1rem;
		vertical-align: middle;
		margin-right: -1rem;
		position: relative;
		opacity: 0;
		z-index:99;
	}	
	.admit_infor img{
		height:1rem;
		width:1rem;
		vertical-align: middle;
		margin: 0.5rem 0.5rem 0.5rem 0;
	}
	#badDesc,#pmcOpinion,#qualityOpinion,#tempCountermeasures,#trueReasonCheck,#countermeasures,#preventHappen{
		display:none;
		width: 88%;
		height: 5rem;
		margin: 0 6%;
		position: relative;
		font-size: 0.5rem;
		padding: 0.2rem;
		border-radius: 0 0 0.5rem 0.5rem;
	}
	.admit_infor_li input[type=checkbox]:checked + img{
		transform: rotate(90deg);
	}
	.problem{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.problem li{
		height:2rem;
		width:100%;
		border-bottom: 0.05rem solid #b9a69f;
	}
	.other_li{
		color: #000000;
		font-size: 0.75rem;
		text-align: center;
		line-height: 2rem;
	}
	.problem li:last-child{
		border-bottom: 0;
		text-align: left;
	}
	#upload{
		height: 3rem;
	}	
	.upload_span{
	    float:left;
		display: inline-block;
		height: 1.2rem;
		width: 4rem;
		font-size: 0.5rem;
		color: #000000;
		background-image: url(${ctx}/mobile/img/sc1.png);
		background-size: 0.8rem 0.8rem;
		background-repeat: no-repeat;
		line-height: 1.2rem;
		border: 0.02rem solid #9e9e9e;
		text-indent: 1rem;
		background-position-x: 0.2rem;
		background-position-y: 0.15rem;
		border-radius: 0.2rem;
		float: left;
		margin: 0.4rem 0 0.4rem 0.5rem;
	}
	#descFile,#attachment2,#attachment3{
		float: left;
		margin: -1.6rem 0 0 0.6rem;
		position: relative;
		z-index: 9;
		opacity: 0;
	}	
	.upload_b a{
		color: black;
		margin-left: 0%;
		font-size: 0.6rem;
	}
	.upload_b p{
		color: black;
		margin-left: 0%;
		font-size: 0.6rem;
		height: 0.6rem;
		margin-top: -1rem;
		text-align: left;
	}
	.upload_b img{
		width: 0.7rem;
		height: 0.8rem;
		margin: 1% -2% 3% 8%;
		vertical-align: middle;
	}		
	#descFile,#supplierFile{
		float: left;
		margin: -1.6rem 0 0 0.6rem;
		position: relative;
		z-index: 9;
		opacity: 0;
	}
	.polling_sell_1 span{
		font-size: 0.5rem;
		color: #b9a69f;
		line-height: 2rem;
		margin-right: 0.5rem;
	}
	.polling_sell_2 input{   /*查询input的样式*/
	    height: 1.4rem;
		width: 50%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	.polling_sell_2 img{
		width: 1rem;
		vertical-align: middle;
		border: 0.01rem solid #9e9e9e;
	}
	.polling_sell_2 sub{
	    color: red;
		font-size: 0.7rem;
		top: .5rem;
		margin-left: -0.2rem;
	}
	#polling_fdj{
		margin-left: -0.6rem;
	}
	#polling_ljt{
		margin-left: -0.2rem;
	}
	.polling_sell_3 input{
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	.polling_sell_1 p{
		margin: 0;
		width: 100%;
		height: 2rem;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		text-align: right;
	}
	.syq{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.syq ul{
		width:100%;
		list-style-type:none;
	}
	.syq_1 li{
		width:100%;
		height:2rem;
		border-bottom:0.05rem solid #b9a69f;
	}
	.syq_1 div{
		width:50%;
		float: left;
		height:100%;
	}
	.syq_2 span{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height: 1.9rem;
		float: right;
		margin-right: 0.5rem;
	}	
	#inspector{ 
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	#zzc{
		background: #000000;
		opacity: 0.7;
		z-index: 1000;
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		
	}
	#ym{
		height:1.5rem;
	}
	#ym p{
		margin:0;
		background: #5ec0ec
		
	}
	#ym span{
		font-size: 0.8rem;
		display: inline-block;
		width: 90%;
		
	}
	#ym img{
		width:1rem;
		vertical-align: middle;
	}
	#nk {
		background: #5ec0ec;
		width: 76%;
		height: 75%;
		z-index: 1111;
		position: fixed;
		margin: 30% 12%;
		top: 0;
	}
	#nk div{
		background:#dad4d4;
		width:94%;
		margin:0% 3%;
		float:left;
	}
	#navigation{
		height:2.2rem;
		border-bottom:0.1rem solid #9e9e9e;
		background-color:#dad4d4;
	}
	#navigation button{
		width: 19%;
		height: 62%;
		font-size: 0.5rem;
		margin-left: 0.2rem;
		float: left;
		margin: 2.4% 2%;
	}
	#navigation input{
		width: 58%;
		margin: 2% 5%;
		float: left;
		padding:1% 1%;
	}
	#navigation img{
		width: 1rem;
		border: 0.1rem solid #9e9e9e;
		float: left;
		margin-top: 4%;
		margin-left: -4%;
	}
	.dsb{
		height:80%;

	}
	#root a{
		color:#000000;
		font-size:0.7rem;
	}
	
	#root span{
		background-image:url(${ctx}/mobile/img/fangz.png);
		background-size:100% 100%;
		float:left;	
	}
	/*
	 *
	 *确认提交的样式
	 *
	 *
	 */

	.endding {
    width: 100%;
    background: #e2d1d1;
    margin-top: 0.5rem;
    position: fixed;
    bottom: 0;
    left: 0;
    z-index: 999;
   }
   .opinion{
			text-indent: .5rem;
			font-size: 0.8rem;
			color: #000000;
			line-height: 1.5rem;
		}
	.endding div{
		width:100%;
		height:1.5rem;
		text-align:left;
	}
	.endding div:last-child{
		text-align:center;
	}
	.endding a{
		display:inline-block;
		height:1.2rem;
		width:3rem;
		background-color:#9e9e9e;
		font-size:0.7rem;
		line-height:1.2rem;
		text-align:center;
		color:#000000;
		vertical-align: middle;
		margin-left: 0.5rem;
		box-shadow: 1px 1px 2px;
	}
	.give input{
		height: 1.2rem;
		width: 9rem;
		vertical-align: middle;
		margin-left: 0.2rem;
		font-size: 0.7rem;
		padding: 0.1rem;
	}
	.give i{
		display: inline-block;
		background-image: url(${ctx}/mobile/img/ljt.png);
		background-size: 100% 100%;
		width: 1rem;
		height: 1rem;
		vertical-align: middle;
		border: 1px solid #9e9e9e;
		margin-left: 0.3rem;
		box-shadow: 1px 1px 3px;
	}
	#file {
		background: #5ec0ec;
		width: 70%;
		height: 60%;
		z-index: 1111;
		position: fixed;
		margin: 30% 12%;
		top: 0;
	}
	#select_man input{   /*查询input的样式*/
	    height: 1.4rem;
		width: 50%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	#select_man img{   /*查询按钮图片样式*/
		width: 1rem;
		vertical-align: middle;
		border: 0.01rem solid #9e9e9e;
	}
	#select_man sub{  /*下标样式*/
	    color: red;
		font-size: 0.7rem;
		top: .5rem;
		margin-left: -0.2rem;
	}		
 </style>
</head>
<body>

</body>
</html>