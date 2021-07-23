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
	
function execute(element, contextPath) { //current element
    var arrayElement = document.getElementsByTagName("tr");  //get all element
    var flag = 1;

    for (var i=0; i< arrayElement.length; i++) {
        if ( (arrayElement[i].getAttribute("id") != null ) && (arrayElement[i] != null) ) {
            var str = arrayElement[i].getAttribute("id");
            if (arrayElement[i].getAttribute("id").indexOf(element) == 0) {
                if (arrayElement[i].getAttribute("id").length != element.length) {
                    //test if it has child
                    if (arrayElement[i].style.display == '') {
                        //alert(arrayElement[i].getAttribute("id").value);
                        flag = 0;
                        //break;
                    }
                    if (str.indexOf("f") < 0) {
                        document.getElementById("img"+str).setAttribute("src", contextPath + "/mvnplugin/mvnforum/images/icon/sub.gif");
                    }
                }
            }
        }
    }
    var count = 0;
    for (var i=0; i< arrayElement.length; i++) {
        if ( (arrayElement[i].getAttribute("id") != null) && (arrayElement[i] != null) ) {
            //var str = arrayElement[i].getAttribute("id");
            if (arrayElement[i].getAttribute("id").indexOf(element) == 0) {
                if (arrayElement[i].getAttribute("id").length != element.length) {    
                    if (flag == 1) {
                        show( arrayElement[i].getAttribute("id") );        
                	    var tag = document.getElementById("img"+element);
                        tag.setAttribute("src", contextPath + "/mvnplugin/mvnforum/images/icon/sub.gif");                    
                    } else if (flag == 0) {
                        hide( arrayElement[i].getAttribute("id") );
        	            var tag = document.getElementById("img"+element);
            	        tag.setAttribute("src", contextPath + "/mvnplugin/mvnforum/images/icon/add.gif");
                    }
                }
            }
        }
    }
}