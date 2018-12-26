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
	ul li:last-child{    /*设置所有ul最后一个li底边为none*/
		border-bottom:none;
	}
	/*
	 *
	 *头部样式
	 *
	 *
	 */

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
	/*
	 *
	 *
	 *研发部
	 *
	 *
	 */
	.syq{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 2.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.syq ul{
		width:100%;
		list-style-type:none;
	}
	.research,.purchase,.company{
		font-size: 0.7rem;
		color: #b9a69f;
		line-height: 1.9rem;
		text-align:center;
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
	}
	#productVersion,#materialCode,#applyDate,#productName,#businessUnitName{  /*研发部input*/
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}

	/*  select样式   */
	#gys_fl_1{
		height: 1.5rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
	}
	.gys_fl_2{
		height: 80%;
		width: 80%;
		border:2;
		border-radius: 0.2rem;
	}
	.gys_fl_2{
		font-size:0.8rem;
	}
	/*
	 *
	 *承认资料
	 *
	 *
	 */
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
	/*
	 *采购部
	 *
	 */ 

	.gys_mc{    
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.gys_mc ul{
		width:100%;
		list-style-type:none;
	}
	.gys_mc_a li{
		width:100%;
		height:2rem;
		border-bottom:0.05rem solid #b9a69f;
	}
	.gys_mc_a div{
		width:50%;
		float: left;
		height:100%;
	}
	#supplier,#purchaseProcesser{
		height: 1.4rem;
		width: 85%;
		border: 0.05rem solid #b9a69f;
		border-radius: 0.2rem;
		margin: 0.25rem 7% 0%;
		padding: 2% 1%;
		font-size: 10px;
	}
	/*
	 *
	 *供应商
	 *
	 *
	 */
	.upload{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
		padding:0.2rem 0;
	}
	.upload ul{
		width:100%;
		list-style-type:none;
	}
	.upload_a li{
		width:100%;
		height:2rem;
		border-bottom:0.1rem solid #b9a69f;
		clear:both;
	}	
	.upload_a div{
		width:50%;
		float: left;
		height:100%;
	}
	#supplierFile{
		height:1.5rem;
		width: 48%;
		margin: 0.24rem 7% 0%;
		opacity: 0;
		z-index: 1;
		position: relative;
	}
	.upload_a img{
		width: 0.7rem;
		height: 0.8rem;
		margin: 1% -2% 3% 8%;
		vertical-align: middle;
	}
	.upload_b a{
		color: black;
		margin-left: 0%;
		font-size: 0.6rem;
	}
	.upload_b span{
		margin-left: 5%;
		border: 0.01rem solid black;
		display: inline-block;
		width: 45%;
		border-radius: 0.2rem;
	}
	.upload_span{
		display: inline-block;
		height: 1.2rem;
		width: 5rem;
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
		margin: 0.2rem 0 0.2rem 1rem;
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
		text-overflow: ellipsis;
       display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin: 0;
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
	/*
	 *
	 *会签单位
	 *
	 *
	 */

	.polling{
		width: 94%;
		border: 0.1rem solid #b9a69f;
		margin: 0.5rem 3% 0;
		border-radius: 0.4rem;
	}
	.polling_sell{
		width:100%;
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

	/*
	 *
	 *遮罩层样式
	 *
	 *
	 */

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
	</style>
</head>
<body>

</body>
</html>