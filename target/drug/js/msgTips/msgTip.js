/***
 * ����ԭ������������ʾJquery���
 * ��дʱ�䣺2012��10��29��
 * ��עJquerySchool��֧��ԭ��
 * http://www.jq-school.com
 * version:manhua_msgTips.js
***/
(function($){
	 var defaults = {
		 timeOut : 1000,				//��ʾ����ʾ��ʱ��
		 msg : "thank you!",			//��ʾ����Ϣ
		 speed : 300				//�����ٶ�
		};
     function showTip(options){
	  	var bid = parseInt(Math.random()*100000);
		$("body").prepend('<div id="tip_container'+bid+'" class="container tip_container"><div id="tip'+bid+'" class="mtip"><i class="micon"></i><span id="tsc'+bid+'"></span><i id="mclose'+bid+'" class="mclose"></i></div></div>');
		var $this = $(this);
		var $tip_container = $("#tip_container"+bid)
		var $tip = $("#tip"+bid);
		var $tipSpan = $("#tsc"+bid);
		var $colse = $("#mclose"+bid);
		//�������ʱ��
		clearTimeout(window.timer);
		
	    //����Ԫ�ذ��¼�
	   	$tip.attr("class", options.type).addClass("mtip");	
		$tipSpan.html(options.msg);			
		$tip_container.slideDown(options.speed);
		//��ʾ�����ض�ʱ��
		window.timer = setTimeout(function (){
				$tip_container.slideUp(options.speed);
		}, options.timeOut);
		//����Ƶ���ʾ��ʱ���ʱ��
		$tip_container.live("mouseover",function() {
			clearTimeout(window.timer);
		});
		
		//����Ƴ���ʾ��ʱ������ʱ��
		$tip_container.live("mouseout",function() {
			window.timer = setTimeout(function (){
				$tip_container.slideUp(options.speed);
			}, options.timeOut);
		});
	
		//�رհ�ť���¼�
		$colse.live("click",function() {
			$tip_container.slideUp(options.speed);
		});
	 }
    $.tips={success:function(options){
	    var options = $.extend(defaults,options);
        options.type="success";
        showTip(options);
	 },
     error:function(options){
	    var options = $.extend(defaults,options);
        options.type="error";
		 showTip(options);
	 },
     warning:function(options){
	    var options = $.extend(defaults,options);
        options.type="warning";
		 showTip(options);
	 }
	};
})(jQuery);
