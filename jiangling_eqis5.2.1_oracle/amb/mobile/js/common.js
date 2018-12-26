$(function(){
	$("#sub_menu_btn").click(function(){
		if ($(this).hasClass("on")) {
			$(this).removeClass("on");
			$('.sub_nav,.nav_hov').fadeOut(200);
		}else{
			$(".sub_menu_btn").removeClass("on"),
			$('.sub_nav,.nav_hov').fadeIn(200);
			$(this).addClass("on");
			$('.sub_nav,.nav_hov').fadeIn(200);
		}
	})
	$(".nav_hov").click(function(){
		$('.sub_nav,.nav_hov').fadeOut(200);
	})
})
$(function(){
	$("#city_btn").click(function(){
		$('.sel_hov,.nav_hov').fadeIn(200);
	})
	$(".close_btn,.nav_hov").click(function(){
		$('.sel_hov,.nav_hov').fadeOut(200);
	})
})

$(function(){
	$(".sel_block a").click(function(){
		$(".sel_block a").removeClass('on');
		$(this).addClass('on');
	});
})

/*标题分级*/
$(".calssMainTitle").on('click',function(){
	$(this).siblings('.subTitle').slideDown(200);
});