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
		z-index: 6;
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
		font-size: 0.7rem;
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
	.divspan{
		text-indent:0.5rem;
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
	.endding{
		width:100%;
		height:1.5rem;
		background:#e2d1d1;
		margin-top:0.5rem;
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
	#root a{
		color:#000000;
		font-size:0.7rem;
	}	
	#root span{
		background-image:url(${ctx}/mobile/img/fangz.png);
		background-size:100% 100%;
		float:left;	
	}

</style>
</head>
<body>

</body>
</html>