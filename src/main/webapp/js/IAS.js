if(typeof IAS === "undefined"){
    IAS = {};
}
IAS.config = IAS.config || {};
IAS.env = {};

IAS.namespace = function (ns) {
    var o = this,
        tokens = [],
        i = 0;

    if (ns.indexOf(".") > -1) {
        tokens = ns.split(".");
        for (i = (tokens[0] === "IAS") ? 1 : 0; i < tokens.length; i++) {
            o[tokens[i]] = o[tokens[i]] || {};
            o = o[tokens[i]];
        }
    } else {
        o[ns] = o[ns] || {};
        o = o[ns];
    }

    return o;
};

IAS.config.getCookie = function(c_name){
    if (document.cookie.length>0) {
        c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1) {
            c_start=c_start + c_name.length+1;
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1) {
                c_end=document.cookie.length;
            }
            return unescape(document.cookie.substring(c_start,c_end));
        }
    }
    return "";
}