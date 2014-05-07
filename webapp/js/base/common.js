$(function() {
    JBack.init();
});

/*
***JBack 系统 beta 0.0.2 ***
使用方法：
1. 通过 JBack.init() 来启动系统的一些通用模块，如：JBack.panel 的一些common方法。
2. 可以在制定页面插入方法单独启动一些控件如 日历插件: JBack.datePicker.init();  前提是需要调用相应样式和控件link

优点：
1. 只占用一个JBack全局变量。
2. 代码有层次，通过方法来调用。
3. 前后台脱离，各模块方法的后期维护，不影响前台的代码调用。
*******************************************************
*/


var JBack = {
    init: function() {
        JBack.panel.init();
    }
}

/* panel模块 */
JBack.panel = {
    //启动函数
    init:function(){
        this.fold_action();
    },
    
    //折叠函数
    fold_action:function(){  
        $("div.cdrPanel div.hd a.tip").click(function() {
            var panel = $(this).closest('.cdrPanel');
            panel.toggleClass('fold');
            return false;
        });
    }
};

/* 日历模块 */
JBack.datePicker = {
    //启动函数
    init: function() {
        $('input.ui-datepicker')
            .datePicker({createButton:false,startDate:'2010-06-01'})
            .dpSetOffset(22, 0)
            .bind('click',function(){
                $(this).dpDisplay();
                return false;
            });
    }
};