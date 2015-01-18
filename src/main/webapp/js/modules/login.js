;(function(){
    $(document).ready(function () {
        $('.forgot-pass').click(function(event) {
            $(".pr-wrap").toggleClass("show-pass-reset");
        });

        $('.pass-reset-submit').click(function(event) {
            $(".pr-wrap").removeClass("show-pass-reset");
        });

        $('.btn-submit').click(function(event){
            event.preventDefault();
            var jsonArray = {};
            jsonArray.username = $("#username").val();
            jsonArray.password = $("#password").val();
            $.ajax({
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonArray),
                url: "account/v0/register",
                type: "POST",
                complete : function(data){
                    if(data.responseJSON.password){
                        var res = data.responseJSON;
                        document.cookie = "token=" + (res.username + "|" + res.password) +";path=/";
                        window.location.href = "dashboard.html";
                    }
                }
            });
        });
    });
})();
