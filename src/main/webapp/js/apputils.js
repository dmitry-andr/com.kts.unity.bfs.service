var COUNTER = 100000;
var SUBJ_LENGTH = 30;

var deviceIphone = "iphone";
var deviceIpod = "ipod";
var deviceAndroid = "android";
var deviceWinMob = "windows ce";
var deviceBB = "blackberry";
var devicePalm = "palm";



function IsEmail(email) {
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
}

function getContOfTag(data, tagName){
    var elem1 = data.split("<" + tagName + ">")[1];
    var val = elem1.split("</" + tagName + ">")[0];
    return val;
}

function renderAndLocalizeAnnouncementDate(dateStr){
    var dateReg = /(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})Z/;
    var d = dateReg.exec(dateStr);
    var rfc1123 = d[2] + "/" + d[3] + "/" + d[1] + " " + d[4] + ":" + d[5] + ":" + d[6] + " GMT+0";
    var date = new Date(rfc1123);
    document.write(date.toLocaleString());
}


function trunkSubject(valToCut){
    //TODO: Implement recount length of the string dynamically depending on the screen resolution
    return valToCut.substring(0, SUBJ_LENGTH) + "...";
}


function detectDeviceType(){
    
    if(DetectIphoneOrIpod()){
        //alert("You are using an iPhone 4");
        //alert("WhiTe oNe :)))");
        return 3;

    }

    if(DetectBlackBerry()){
        //alert("You are using BlackBerry");
        return 4;
    }

    if(DetectAndroid()){
        //alert("You are using Android phone");
        //$("link#style1").attr("href", "jqtouch/jqtouch.min.css");
        //$("link#style2").attr("href", "themes/jqt/theme.min.css");
        return 5;

    }

    if(DetectAndroidWebKit()){
        //alert("You are using android - OS based device");
        return 6;

    }

    if(DetectWindowsMobile()){
        alert("You are using Windows Phone");
        return 7;
    }

    if(DetectPalmOS()){
        alert("You are using Palm device");
        return 8;
    }
    return 1;
}


//**************************
// Detects if the current device is an iPhone.
function DetectIphone()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(deviceIphone) > -1)
        return true;
    else
        return false;
}

//**************************
// Detects if the current device is an iPod Touch.
function DetectIpod()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(deviceIpod) > -1)
        return true;
    else
        return false;
}

//**************************
// Detects if the current device is an iPhone or iPod Touch.
function DetectIphoneOrIpod()
{
    if (DetectIphone())
        return true;
    else if (DetectIpod())
        return true;
    else
        return false;
}

//**************************
// Detects if the current device is an Android OS-based device.
function DetectAndroid()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(deviceAndroid) > -1){
        return true;
    }else{
        return false;
    }
}

//**************************
// Detects if the current device is an Android OS-based device and
//   the browser is based on WebKit.
function DetectAndroidWebKit()
{
    if (DetectAndroid())
    {
        if (DetectWebkit())
            return true;
        else
            return false;
    }
    else
        return false;
}

//**************************
// Detects if the current browser is a Windows Mobile device.
function DetectWindowsMobile()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(deviceWinMob) > -1)
        return true;
    else
        return false;
}

//**************************
// Detects if the current browser is a BlackBerry of some sort.
function DetectBlackBerry()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(deviceBB) > -1)
        return true;
    else
        return false;
}

//**************************
// Detects if the current browser is on a PalmOS device.
function DetectPalmOS()
{
    var uagent = navigator.userAgent.toLowerCase();
    if (uagent.search(devicePalm) > -1)
        return true;
    else
        return false;
}
