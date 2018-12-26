<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
	body{
		margin:0;
		padding:0;
		font-family:"微软雅黑";
		overflow:auto;
	}
	.cause{
		width:100%;
		height:2rem;
		background-color:blue;
		position:absolute;	
		z-index: 6;
	}
	.cause span{
		display:inline-block;
		height:2rem;
		font-size:1rem;
		line-height:2rem;
		color:#ffffff;
		float:left;
	}
	.cause_0{
		width:32%;
	}
	.cause_0 img{
		height: 75%;
		margin: 4% 5% 0;
	}
	.syq{
		width: 94%;
		height: 14rem;
		border: 0.1rem solid #b9a69f;
		margin: 2.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.syq ul{
		width:100%;
		height:18rem;
		list-style-type:none;
	}
	.syq_1 li{
		width:100%;
		height:11.11%;
		border-bottom:0.1rem solid #b9a69f;
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
	.partOne{
		height: 60%;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 3% 7% 0%;
		padding: 7% 1%;
		font-size: 15px;
	}
	#gys_fl_1{
		height: 60%;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 3% 7% 0%;
	}
	.gys_fl_2{
		height: 100%;
		width: 100%;
		border:0;
		border-radius: 0.2rem;
	}
	.gys_fl_2{
		font-size:0.8rem;
	}
	ul li:last-child{
		border-bottom:none;
	}
	.gys_mc{
		width: 94%;
		height: 20rem;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.gys_mc ul{
		width:100%;
		height:20rem;
		list-style-type:none;
	}
	.gys_mc_a li{
		width:100%;
		height:10%;
		border-bottom:0.1rem solid #b9a69f;
	}
	.gys_mc_a div{
		width:50%;
		float: left;
		height:100%;
	}
	.partTwo{
		height: 60%;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 3% 7% 0%;
		padding: 7% 1%;
		font-size: 10px;
	}
	.upload{
		width: 94%;
		height: 24rem;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.upload ul{
		width:100%;
		height:24rem;
		list-style-type:none;
	}
	.upload_a li{
		width:100%;
		height:14.2%;
		border-bottom:0.1rem solid #b9a69f;
	}
	.upload_a div{
		width:50%;
		float: left;
		height:100%;
	}
	#upload_1,#upload_2,#upload_3,#upload_4,#upload_5,#upload_6,#upload_7{
		height: 90%;
		width: 48%;
		margin: 1.8% 7% 0%;
		opacity: 0;
		z-index: 1;
		position: relative;
	}
	.upload_a img{
		width: 0.6rem;
		height: 0.8rem;
		margin: 1% -2% 3% 8%;
		vertical-align: middle;
	}
	.upload_b span{
		margin-left: 5%;
		border: 0.01rem solid black;
		display: inline-block;
		width: 35%;
		border-radius: 0.2rem;
	}
	.upload_b a{
		color: black;
		margin-left: 8%;
		font-size: 0.6rem;
	}
	.upload_b p{
		color: black;
		margin-left: 0%;
		font-size: 0.6rem;
	}
	.polling{
		width: 94%;
		height: 58rem;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.polling_sell{
		width:100%;
		height:58rem;
		list-style-type:none;
	}
	.polling_sell li{
		    border-bottom: 0.05rem solid #b9a69f;
			height:2rem;
	}
	.polling_sell div{
		width:50%;
		height:2rem;
		float:left;
	}
	.polling_sell_1 span{
		font-size: 0.5rem;
		color: #b9a69f;
		line-height: 2rem;
		margin-right: 0.5rem;
	}
	.polling_sell_2 input{
	    height: 60%;
		width: 50%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 2.8% 7% 0%;
		padding: 7% 1%;
		font-size: 10px;
	}
	.method input{
		height: 1.4rem;
		width: 70%;
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
	#polling_fdj,#polling_fdj_1,#polling_fdj_2,#polling_fdj_3,#polling_fdj_4,#polling_fdj_5,#polling_fdj_6,#polling_fdj_7,#polling_fdj_8,#polling_fdj_9,#polling_fdj_10,#polling_fdj_11,#polling_fdj_12,#polling_fdj_13,#polling_fdj_14{
		margin-left: -0.6rem;
	}
	#polling_ljt,#polling_ljt_a,#polling_ljt_b,#polling_ljt_c,#polling_ljt_d,#polling_ljt_e,#polling_ljt_f,#polling_ljt_g,#polling_ljt_h,#polling_ljt_i,#polling_ljt_j,#polling_ljt_k,#polling_ljt_l,#polling_ljt_m,#polling_ljt_n{
		margin-left: -0.2rem;
	}
	.polling_sell_3 input{
		height: 60%;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 2.8% 7% 0%;
		padding: 7% 1%;
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
	.gg{
		list-style-type:none;
		width:100%;
		height:3rem;
	}
	.gg li{
		float:left;
		height:3rem;
		width:13%;
		text-align: center;
	}
	.gg span{
		font-size: 0.6rem;
		color: #000000;
	}
	.endding_1{
		border:0.1rem solid #000000;
	}
	.endding_a{
		width:3rem;
		height:2.5rem;
		text-align:center;
		
	}
	.endding_a span{
		color:#000000;
		font-size:0.5rem;
	}
	
	
	#endding_1 input{
		width: 1.5rem;
		height: 1.3rem;
		margin-top: 0.3rem;
	}

	#endding_2 img{
		width: 1.5rem;
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
		width: 91%;
		
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
	#file {
		background: #5ec0ec;
		width: 70%;
		height: 60%;
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
	}
	#root img{
		width: 22px;
		vertical-align: middle;
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
	.endding{
		width:100%;
		height:4.5rem;
		background:#e2d1d1;
		margin-top:0.5rem;
		position: fixed;
  		bottom: 0;
   		left: 0;
		z-index: 999;
	}
	.endding div{
		width:100%;
		height:1.5rem;
		text-align:left;
	}
	/* .endding div:last-child{
		text-align:center;
	} */ 
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
	/*问题描述*/
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
		font-size: 1rem;
		text-align: center;
		line-height: 2rem;
	}
	.problem li:first-child{
		border-radius: 0.5rem 0.5rem 0 0;
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
	/*下拉框*/
	.method{
		height: 1.4rem;
		width: 70%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
 		padding: 2% 1%; 
		font-size: 10px;
	}
	/*子表*/
	.testoperation{
		width: 95%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
		height: 4rem;
	}
	.number{
		height:100%;
		width:12%;
		float:left;
		border-right: 0.03rem solid #9e9e9e;
	}
	.number p{
		font-size:0.75rem;
		color:#000;
 		padding:0; 
 		text-align:center; 
		margin: 2rem 5;
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
		width:86%;
		float:left;
		overflow:auto;
	}
	.textcontent li{
		width:100%;
		height:25%;
 		border-bottom: 0.03rem solid #b9a69f; 
	}
	.textcontent ul{
 		border-top-style:dotted; 
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
		width: 16rem;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	/*文本框*/
	.check,.testItem{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height: 1.9rem;
		text-align:center;
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
		width:50%;
		text-align:right;
	}
	.admit_infor li:first-child{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height:2rem;
		text-align:left;
		text-indent: 0.5rem;
	}
	#select_down,#down_select{
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
	.admit_infor_div {
		height: 20rem;
		width: 94%;
		margin: -0.2rem auto;
		background-color: #eeeeee;
		position: relative;
		z-index: 99;
		border: 0.1rem solid #9e9e9e;
		border-radius: 0.3rem;
		padding: 0.2rem;
	}
	.admit_infor_div input{
		vertical-align: middle;
		margin-right:0.2rem;
	}
	.admit_infor_div span{
		vertical-align: middle;
		font-size: 0.5rem;
	}
	#assess{
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
	.fill_content{
		display:none;
		width: 88%;
		height: 5rem;
		margin: 0 6%;
		position: relative;
		font-size: 0.5rem;
		padding: 0.2rem;
		border-radius: 0 0 0.5rem 0.5rem;
	}
	.textbox{
		height:1rem;
		width:1rem;
		vertical-align: middle;
		margin-right: -1rem;
		position: relative;
		opacity: 0;
		z-index:99;
	}
	.upload_c{
		width: 1rem;
		height: 3rem;
 		line-height:1.5rem;
	}
	.upload_c img{
		width: 1rem;
		height: 1rem;
		margin: 0% 1% -5% 0%;
	}
	.upload_c span{
		margin: 0% 0% 0% 0%;
		border: 0.01rem solid black;
		display: inline-block;
		width: 50%;
		border-radius: 0.2rem;
	}
	.upload_c a{
		color: black;
		margin: 0% 1% 3% 0%;
		font-size: 0.7rem;
		vertical-align: middle;
	}
	.upload_c div{
		width: 8rem;
		height: 2rem;
		margin: 0% -20% -50% -50%;
	}
	
	.operation{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.operation li{
		width:100%;
		height:2rem;
		border-bottom:0.05rem solid #b9a69f;
	}
	.operation li:last-child{
		border-bottom:0;
	}
	.operation div{
		width:50%;
		height:2rem;
		float:left;
		text-align:left;
		font-size:0.7rem;
		color:#b9a69f;
	}
	.operation_lifir div:last-child{
		text-align:right;
	}
	.operation_lifir i{
		display:inline-block;
		height:1rem;
		width:1rem;
		background-size:70% 70%;
		border:0.02rem solid #9e9e9e;
		background-repeat: no-repeat;
		background-position: center;
		margin: 0.5rem 0.2rem 0.35rem 0;
		box-shadow: 0.5px 0.5px 2px black;
	}
	.operation_lifir div > i:last-child{
		margin-right:0.5rem;
	}
	.operation span{
		line-height:2rem;
	}
	</style>
</head>
<body>

</body>
</html>