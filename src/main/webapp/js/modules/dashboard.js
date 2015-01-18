
$(document).ready(function(){$(".alert").addClass("in").fadeOut(4500);

/* swap open/close side menu icons */
$('[data-toggle=collapse]').click(function(){
  	// toggle icon
  	$(this).find("i").toggleClass("glyphicon-chevron-right glyphicon-chevron-down");
});
});
;(function ($) {
	$("#logout").click(function(){
		$.ajax({
			contentType: "application/json; charset=utf-8",
			url: "account/v0/register?token=" + IAS.config.getCookie("token"),
			type: "DELETE",
			complete : function(data){
				if(!data.responseText.password){
					var d = new Date();
					document.cookie = "token=;expires=" + d.toGMTString() + ";;";
					window.location.href = "login.html";
				}
			}
		});
	});
})(jQuery);