<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
	body{
		overflow:auto;
	}
	.nav_head{
		width:100%;
		height:2rem;
		background-color:blue;
		position:fixed;	
		z-index: 999999;
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
		font-size: 0.5rem;
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
	.method{
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	.opinion_div_b{
		float: left;
		height: 2rem;
		width: 30%;
		text-align: right;
	}
	.opinion_div_a{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height: 2rem;
		text-align: left;
		text-indent: 0.5rem;
		width: 70%;
		height: 2rem;
		float: left;
	}
	.opinion img{
		height: 1rem;
		width: 1rem;
		vertical-align: middle;
		margin: 0.5rem 0.5rem 0.5rem 0;
	}
	.opinion_div_b input[type=checkbox]:checked + img{
		transform: rotate(90deg);
	}
	.teatareadown{
		height: 1rem;
		width: 1rem;
		vertical-align: middle;
		margin-right: -1rem;
		position: relative;
		opacity: 0;
		z-index: 99;
	}
	.purchase{
		color: #000000;
		text-align:center;
		background-color: #00ffe5;
		border-radius: 0.4rem 0.4rem 0 0;
	}
	.writetextarea{
		display:none;
		width: 88%;
		height: 5rem;
		margin: 0 6%;
		position: relative;
		font-size: 0.5rem;
		padding: 0.2rem;
		border-radius: 0 0 0.5rem 0.5rem;
	}
	.topdescribe{
		font-size:0.5rem;
		margin:0;
		padding: 0.15rem 0.5rem;

	}
	.testoperation{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
		height: 8rem;
	}
	.number{
		height:100%;
		width:15%;
		float:left;
		border-right: 0.03rem solid #9e9e9e;
	}
	.number p{
		font-size:0.75rem;
		color:#000;
		padding:0;
		text-align:center;
		margin: 2rem 0;
	}
	.number i{
		display:inline-block;
		width:0.6rem;
		height:0.6rem;
		background-size:70% 70%;
		background-repeat: no-repeat;
		background-position: center;
		border: 0.05rem solid;
	}
	.textcontent{
		height:100%;
		width:85%;
		float:left;
		overflow:auto;
	}
	.textcontent li{
		width:100%;
		height:25%;
		border-bottom: 0.03rem solid #b9a69f;
	}
	.textcontent span{
		font-size: 0.6rem;
		line-height: 2rem;
		color: #000000;
		display: inline-block;
		width: 5rem;
		height: 2rem;
		text-indent: 0.2rem;
	}
	.ulcontent{
		overflow:hidden;
		height:100%;
		width:100%;
	}
	.textwidth{
		height: 1.4rem;
		width: 7rem;
		padding: 2px;
		font-size: 0.65rem;
	}
	.opinion_div_2{
		float: left;
		height: 2rem;
		width: 50%;
	}
	.opinion_div_1{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height: 2rem;
		text-indent: 0.5rem;
		width: 50%;
		height: 2rem;
		float: left;
		text-align:right;
	}
	.other_li{
		color: #000000;
		font-size: 0.75rem;
		text-align: center;
		line-height: 2rem;
	}	
	#upload{
		height: 3rem;
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
	.upload_span{
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
		text-align: left;
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
	.receive{
		float: right;
		margin-top: -1.6rem;
		margin-right: 5%;
		position: relative;
		z-index: 10;
		width: 64%;
		height: 1.2rem;
		font-size: 0.7rem;
		text-indent: 0.3rem;
	}
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
	#polling_fdj{
		margin-left: -0.6rem;
	}
	#polling_ljt{
		margin-left: -0.2rem;
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
	.problem li:first-child{		
		border-radius: 0.4rem 0.4rem 0 0;
	}
	.problem li:last-child{
		border-bottom: 0;
		text-align: left;
	}
	.problem div{
		width:50%;
		height:2rem;
		float:left;
		text-align:left;
		font-size:0.7rem;
		color:#b9a69f;
	}
	.problem li > div:first-child{
		text-align:right;
		line-height:2rem;
	}	
</style>
</head>
</html>