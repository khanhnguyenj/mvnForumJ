/*
 * $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/js/mvncode.js,v 1.21 2009/06/03 11:12:48 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.21 $
 * $Date: 2009/06/03 11:12:48 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2006 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen  
 */

// *******************************************************

tags = new Array();

// *******************************************************
// replacements for unsupported array functions (because arrayname.push(var)
// and arrayname.pop() are not implemented in IE until version 5.5)

// Note: Reference Mozilla from http://www.massless.org/mozedit/

function getarraysize(thearray) {
// replacement for arrayname.length property
    for (i = 0; i < thearray.length; i++) {
        if ((thearray[i] == "undefined") || (thearray[i] == "") || (thearray[i] == null)) {
            return i;
        }
    }
    return thearray.length;
}

function arraypush(thearray, value) {
// replacement for arrayname.push(value)
    thearraysize = getarraysize(thearray);
    thearray[thearraysize] = value;
}

function arraypop(thearray) {
// replacement for arrayname.pop()
    thearraysize = getarraysize(thearray);
    retval = thearray[thearraysize - 1];
    delete thearray[thearraysize - 1];
    return retval;
}

// *******************************************************

function setmode(modevalue) {
// sets cookie for normal (0) and enhanced (1) modes
    document.cookie = "mvncodemode="+modevalue+"; path=/; expires=Wed, 1 Jan 2020 00:00:00 GMT;";
}

function normalmode(theform) {
// checks value of mode radio buttons. returns true if normal mode
    if (theform.mode[0].checked) {
        return true;
    } else {
        return false;
    }
}

function stat(thevalue) {
// places mini-help text into help/error textbox
// strings are stored in mvncode_language.js
    document.mvnform.status.value = eval(thevalue+"_text");
}

// *******************************************************

function closetag(theform) {
// closes last opened tag
    if (normalmode(theform)) {
        stat('enhanced_only');
    } else {
        if (tags[0]) {
            theform.message.value += "[/"+ arraypop(tags) +"]";
        } else {
            stat('no_tags');
        }
    }
    //theform.closecurrent.checked=false;
    theform.message.focus();
}

function closeall(theform) {
// closes all open tags
    if (normalmode(theform)) {
        stat('enhanced_only');
    } else {
        if (tags[0]) {
            while (tags[0]) {
                theform.message.value += "[/"+ arraypop(tags) +"]";
            }
            theform.message.value += " ";
        } else {
            stat('no_tags');
        }
    }
    //theform.closealltags.checked=false;
    theform.message.focus();
}

// *******************************************************

function mvncode(theform, mvncode, prompttext) {
// insert [x]yyy[/x] style markup
    var txtarea = theform.message;
    if ((normalmode(theform)) || (mvncode=="img")) {
        var selection = getSelection(txtarea);
        var inserttext = "";
        if (selection.length > 0) {
            inserttext = "["+mvncode+"]"+selection+"[/"+mvncode+"]";
        } else {
            inserttext = prompt(tag_prompt+"\n["+mvncode+"]xxx[/"+mvncode+"]",prompttext);
            if ((inserttext != null) && (inserttext != "")) {
                inserttext = "["+mvncode+"]"+inserttext+"[/"+mvncode+"] ";
            }
        }
        if ((inserttext != null) && (inserttext != "")) {
            insertString(txtarea, inserttext);
        }
    } else {
        donotinsert = false;
        for (i = 0; i < tags.length; i++) {
            if (tags[i] == mvncode) {
                donotinsert = true;
            }
        }
        if (donotinsert) {
            stat("already_open");
        } else {
            txtarea.value += "["+mvncode+"]";
            arraypush(tags, mvncode);
        }
    }
    txtarea.focus();
}

// *******************************************************

function fontformat(theform,thevalue,thetext,thetype) {
// insert two-parameter markup - [x=y]zzz[/x]
    var txtarea = theform.message;
    if (normalmode(theform)) {
        if (thevalue != 0) {
            var selection = getSelection(txtarea);
            var inserttext = selection;
            if (selection.length == 0) {
                inserttext = prompt(font_formatter_prompt+" '"+thetext+"'","");
            }
            if ((inserttext != null) && (inserttext != "")) {
                inserttext = "["+thetype+"="+thevalue+"]"+inserttext+"[/"+thetype+"] ";
                insertString(txtarea, inserttext);
            }
        }
    } else {
        theform.message.value += "["+thetype+"="+thevalue+"]";
        arraypush(tags,thetype);
    }
    theform.sizeselect.selectedIndex = 0;
    theform.fontselect.selectedIndex = 0;
    theform.colorselect.selectedIndex = 0;
    txtarea.focus();
}

// *******************************************************

function namedlink(theform, thetype) {
    // inserts named url or email link - [url=mylink]text[/url]
    var txtarea = theform.message;
    var selection = getSelection(txtarea);
    var linktext = selection;
    if (linktext.length == 0) {
        linktext = prompt(link_text_prompt, "");
    }
    var prompttext;
    if (thetype == "url") {
        prompt_text = link_url_prompt;
        prompt_contents = "http://";
    } else {
        prompt_text = link_email_prompt;
        prompt_contents = "";
    }
    linkurl = prompt(prompt_text, prompt_contents);
    if ((linkurl != null) && (linkurl != "")) {
        if ((linktext == null) || (linktext == "")) {
            linktext = linkurl;
        }
        var inserttext;
        if (thetype == "url") {
            inserttext = "["+thetype+"="+linkurl+"]"+linktext+"[/"+thetype+"] ";
        } else {//email
            inserttext = "[url=mailto:"+linkurl+"]"+linktext+"[/url] ";
        }
        insertString(txtarea, inserttext);
    }
    txtarea.focus();
}

// *******************************************************

function dolist(theform) {
// inserts list with option to have numbered or alphabetical type
    listtype = prompt(list_type_prompt, "");
    if ((listtype == "a") || (listtype == "1")) {
        thelist = "[list="+listtype+"]\n";
        listend = "[/list="+listtype+"] ";
    } else {
        thelist = "[list]\n";
        listend = "[/list] ";
    }
    listentry = "initial";
    while ((listentry != "") && (listentry != null)) {
        listentry = prompt(list_item_prompt, "");
        if ((listentry != "") && (listentry != null)) {
            thelist = thelist+"[*]"+listentry+"[/*]"+"\n";
        }
    }
    var inserttext = thelist+listend;
    var txtarea = theform.message;
    insertString(txtarea, inserttext);
    //txtarea.focus();
}

// *******************************************************

function smilie(thesmilie) {
// inserts smilie text
    var txtarea = document.mvnform.message;
    var newSmilie = ' ' + thesmilie + ' ';
    if (usingBB) {
        insertString(txtarea, newSmilie);
    } else {
        tinyMCE.execCommand('mceInsertContent',false,' ' + convertSmilie(thesmilie) + ' ');
    }
}

//*******************************************************

function convertSmilie(s) {
    s = s.replace(/\[:\)\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/laughing.gif\" />");
    s = s.replace(/\[:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smile.gif\" />");
    s = s.replace(/\[:-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smile.gif\" />");
    s = s.replace(/\[:\(\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/crying.gif\" />");
    s = s.replace(/\[:\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sad.gif\" />");
    s = s.replace(/\[:-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sad.gif\" />");
    s = s.replace(/\[;\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/wink.gif\" />");
    s = s.replace(/\[:D\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/biggrin.gif\" />");
    s = s.replace(/\[;;\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/batting_eyelashes.gif\" />");
    s = s.replace(/\[:-\/\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/confused.gif\" />");
    s = s.replace(/\[:x\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/love.gif\" />");

    s = s.replace(/\[:\"\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/blushing.gif\" />");
    s = s.replace(/\[:&quot;&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/blushing.gif\" />");

    s = s.replace(/\[:p\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/tongue.gif\" />");
    s = s.replace(/\[:\*\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/kiss.gif\" />");
    s = s.replace(/\[:O\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shock.gif\" />");
    s = s.replace(/\[X-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/angry.gif\" />");

    s = s.replace(/\[:\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smug.gif\" />");
    s = s.replace(/\[:&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smug.gif\" />");

    s = s.replace(/\[B-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cool.gif\" />");
    s = s.replace(/\[:-s\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/worried.gif\" />");

    s = s.replace(/\[\>:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/devilish.gif\" />");
    s = s.replace(/\[&gt;:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/devilish.gif\" />");

    s = s.replace(/\[:\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/straight_face.gif\" />");
    s = s.replace(/\[\/:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/raised_eyebrow.gif\" />");
    s = s.replace(/\[O:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/angel.gif\" />");
    s = s.replace(/\[:-B\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/nerd.gif\" />");
    s = s.replace(/\[=;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/talk_to_the_hand.gif\" />");
    s = s.replace(/\[I-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sleep.gif\" />");
    s = s.replace(/\[8-\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/rolling_eyes.gif\" />");

    s = s.replace(/\[:-&\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sick.gif\" />");
    s = s.replace(/\[:-&amp;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sick.gif\" />");

    s = s.replace(/\[:-\$\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shhh.gif\" />");
    s = s.replace(/\[\[-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/not_talking.gif\" />");
    s = s.replace(/\[:o\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/clown.gif\" />");
    s = s.replace(/\[8-}\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/silly.gif\" />");
    s = s.replace(/\[\(:\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/tired.gif\" />");
    s = s.replace(/\[=P~\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/drooling.gif\" />");
    s = s.replace(/\[:-\?\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/thinking.gif\" />");
    s = s.replace(/\[#-o\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/d_oh.gif\" />");

    s = s.replace(/\[=D\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/applause.gif\" />");
    s = s.replace(/\[=D&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/applause.gif\" />");
    s = s.replace(/\[:@\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/pig.gif\" />");
    s = s.replace(/\[3:-O\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cow.gif\" />");
    s = s.replace(/\[:\(\|\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/monkey.gif\" />");

    s = s.replace(/\[~:\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/chicken.gif\" />");
    s = s.replace(/\[~:&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/chicken.gif\" />");

    s = s.replace(/\[@\};-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/rose.gif\" />");
    s = s.replace(/\[%%-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/good_luck.gif\" />");
    s = s.replace(/\[\*\*==\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/flag.gif\" />");
    s = s.replace(/\[\(~~\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/pumpkin.gif\" />");
    s = s.replace(/\[~o\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/coffee.gif\" />");
    s = s.replace(/\[\*-:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/idea.gif\" />");
    s = s.replace(/\[8-X\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/skull.gif\" />");
    s = s.replace(/\[=:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_1.gif\" />");

    s = s.replace(/\[\>-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_2.gif\" />");
    s = s.replace(/\[&gt;-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_2.gif\" />");

    s = s.replace(/\[:-L\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/frustrated.gif\" />");

    s = s.replace(/\[<\):\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cowboy.gif\" />");
    s = s.replace(/\[&lt;\):\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cowboy.gif\" />");

    s = s.replace(/\[\[-o<\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/praying.gif\" />");
    s = s.replace(/\[\[-o&lt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/praying.gif\" />");

    s = s.replace(/\[@-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hypnotized.gif\" />");
    s = s.replace(/\[\$-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/money_eyes.gif\" />");

    s = s.replace(/\[:-\"\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/whistling.gif\" />");
    s = s.replace(/\[:-&quot;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/whistling.gif\" />");

    s = s.replace(/\[:\^o\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/liar.gif\" />");
    s = s.replace(/\[b-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/beat_up.gif\" />");

    s = s.replace(/\[:\)\>-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/peace.gif\" />");
    s = s.replace(/\[:\)&gt;-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/peace.gif\" />");

    s = s.replace(/\[\[-X\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shame_on_you.gif\" />");
    s = s.replace(/\[\\:D\/\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/dancing.gif\" />");

    s = s.replace(/\[\>:D<\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hugs.gif\" />");
    s = s.replace(/\[&gt;:D&lt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hugs.gif\" />");     
    return s;
}

// *******************************************************

function insertString(txtarea, thetext) {
    if (txtarea.createTextRange && txtarea.caretPos) { //for IE only
        var caretPos = txtarea.caretPos;
        var newText = thetext;
        if (caretPos.text.charAt(caretPos.text.length - 1) == ' ') newText = newText + ' ';
        if (caretPos.text.charAt(0) == ' ') newText = ' ' + newText;
        caretPos.text = newText;
    } else if ( (document.getElementById != undefined) && (txtarea.textLength != undefined) && (txtarea.selectionStart != undefined) && (txtarea.selectionEnd != undefined) ) {
    	// for FireFox
        var selLength = txtarea.textLength;
        var selStart = txtarea.selectionStart;
        var selEnd = txtarea.selectionEnd;
        if (selEnd==1 || selEnd==2) selEnd = selLength;
        var s1 = (txtarea.value).substring(0, selStart);
        var s2 = (txtarea.value).substring(selStart, selEnd)
        var s3 = (txtarea.value).substring(selEnd, selLength);
        var newText = thetext;
        if (s2.charAt(s2.length - 1) == ' ') newText = newText + ' ';
        if (s2.charAt(0) == ' ') newText = ' ' + newText;
        txtarea.value = s1 + thetext + s3;
    } else {
        txtarea.value += thetext;
    }
    txtarea.focus();
}

// *******************************************************

function getSelection(txtarea) {
    var retValue = "";
    if (txtarea) {
        if (txtarea.createTextRange && txtarea.caretPos) {
            if (txtarea.caretPos.text.length > 0) {
                retValue = txtarea.caretPos.text;        
            }
        } else if (document.getElementById) {
            var selLength = txtarea.textLength;
            var selStart = txtarea.selectionStart;
            var selEnd = txtarea.selectionEnd;
            if (selEnd==1 || selEnd==2) selEnd = selLength;
            var s2 = (txtarea.value).substring(selStart, selEnd)
            if (s2.length > 0) {
                retValue = s2;
            }
        }
    }   
    return retValue;
}

// *******************************************************

// http://www.faqts.com/knowledge_base/view.phtml/aid/1052/fid/130
function storeCaret(textEl) {
    if (textEl.createTextRange) {
        textEl.caretPos = document.selection.createRange().duplicate();
    }
}
 
// *******************************************************
