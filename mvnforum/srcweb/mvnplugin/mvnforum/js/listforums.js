/* $Id: listforums.js,v 1.12 2008/03/26 02:42:49 phuongpdd Exp $ */
function show(element) {
    var tag = document.getElementById(element);
    if (tag == null) return;
    tag.style.display = '';
}

function hide(element) {
    var tag = document.getElementById(element);
    if (tag == null) return;
    tag.style.display = 'none';
}

function init() { //current element
    var arrayElement = document.getElementsByTagName("tr");  //get all element
    for (var i=0; i< arrayElement.length; i++) {
        //fix error with IE: (arrayElement[i].getAttribute("id").length > 0)
        if ((arrayElement[i] != null) && (arrayElement[i].getAttribute("id") != null ) && (arrayElement[i].getAttribute("id").length > 0)) {
            if (arrayElement[i].getAttribute("id").indexOf("_",2) == -1) {
                show(arrayElement[i].getAttribute("id"));
            } else {
                hide(arrayElement[i].getAttribute("id"));
            }
        }
    }
}

function execute(element) { //current element
    var arrayElement = document.getElementsByTagName("tr");  //get all element
    var flag = 1;

    for (var i=0; i< arrayElement.length; i++) {
        if ((arrayElement[i] != null) && (arrayElement[i].getAttribute("id") != null)) {
            var str = arrayElement[i].getAttribute("id");
            //fix bug
            if (str.indexOf(element + "_") == 0) {
                if (str.length != element.length) {
                    //test if it has child
                    if (arrayElement[i].style.display == '') {
                        //alert(arrayElement[i].getAttribute("id").value);
                        flag = 0;
                        //break;
                    }
                }
            }
        }
    }
    var count = 0;
    for (var i=0; i< arrayElement.length; i++) {
        if ((arrayElement[i] != null) && (arrayElement[i].getAttribute("id") != null)) {
            var str = arrayElement[i].getAttribute("id");
            if (str.indexOf(element + "_") == 0) {
                if (str.length != element.length) {  
                    var pos1 = arrayElement[i].getAttribute("id").indexOf("_",element.length + 1);
                    if (flag == 1 && pos1 == -1) {
                        show( arrayElement[i].getAttribute("id") );        
                    } else if (flag == 0) {
                        hide( arrayElement[i].getAttribute("id") );
                    }
                }
            }
        }
    }
}

function execute1(element) { //current element
    var keys = new Array(element.length);
    var count = 0;
    var tmp;
    for (var i=1; i<element.length; i++) {
        if (element.charAt(i) == '_') {
            keys[count]=i;
            count++;
        }
    }
    var num=0;
    values = new Array(keys.length);
    if (count-1==0) {
        values[0] = element.substring(2);
    } else {
        for (var j=0; j<count; j++) {
           if (j<count-1) {
               values[j] = element.substring(keys[j]+1, keys[j+1]);
           } else {
               values[j] = element.substring(keys[j]+1);
           }
        }
    }	

    var arrayElement = document.getElementsByTagName("tr");  //get all element
    var flag = 1;
    for (var i=0; i< arrayElement.length; i++) {
        if ( (arrayElement[i].getAttribute("id") != null) && (arrayElement[i] != null) ) {
            var str = arrayElement[i].getAttribute("id");
            if (arrayElement[i].getAttribute("id").indexOf(element) == 0) {
                if (arrayElement[i].getAttribute("id").length != element.length) {
                    if (arrayElement[i].style.display == '') {
                        flag = 0;
                    }
                }
            }
        }
    }
    for (var i=0; i< arrayElement.length; i++) {
        var dem = 0;
        if ( (arrayElement[i].getAttribute("id") != null) && (arrayElement[i] != null) ) {
            //dem so / trong element dang xet
            for (var t=0; arrayElement[i].getAttribute("id")!= null && t<arrayElement[i].getAttribute("id").length; t++) {
                if (arrayElement[i].getAttribute("id").charAt(t) == '_') {
                    dem=dem+1;
                }
            }
            if (dem-2>0 && arrayElement[i].getAttribute("id").indexOf("__"+values[0])==-1) {
                // truong hop //1 hay //2
                hide(arrayElement[i].getAttribute("id"));
            } else if (dem-2>0) {
                var mark = "__";
                for (var j=0; j<dem-2; j++) {
                    if (j!= dem-3)
                        mark = mark+values[j]+"_";
                    else mark = mark + values[j]	
                }
                if (arrayElement[i].getAttribute("id").indexOf(mark)==-1) {
                    hide(arrayElement[i].getAttribute("id"));
                } else {
                    if (arrayElement[i].getAttribute("id").indexOf("f") != -1 && 
                       arrayElement[i].getAttribute("id").indexOf(element) == -1) {
                        hide(arrayElement[i].getAttribute("id"));
                    } else {
                        show(arrayElement[i].getAttribute("id"));
                    }	
                }
            }
        }
    }
}

function osexecute(element) { //current element
    var arrayElement = document.getElementsByTagName("tr");  //get all element
    for (var i=0; i< arrayElement.length; i++) {
        if ((arrayElement[i] != null) && (arrayElement[i].getAttribute("id") != null)) {
            var str = arrayElement[i].getAttribute("id");
            if (str.indexOf(element + "_f") != -1) {
                if (arrayElement[i].style.display == '') {
                    hide(arrayElement[i].getAttribute("id"));
                } else if (arrayElement[i].style.display == 'none') {
                    show( arrayElement[i].getAttribute("id") );        
                }    
            }
        }
    }
}