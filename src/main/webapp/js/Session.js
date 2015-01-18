(function(){
    var session = IAS.session = {};
    session.logout = function(){
        _removeCookies();
        window.location.href = "login.html";
    };
    session._removeCookies = function(){
        var d = new Date();
        document.cookie = "token=;expires=" + d.toGMTString() + ";;";
        console.debug("Cookies removed. Current cookies remaining: " + document.cookie);
    }
})();
