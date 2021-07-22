/**
 * $Id: editor_plugin.js,v 1.9 2009/06/15 11:19:11 lexuanttkhtn Exp $
 *
 * @author Moxiecode
 * @copyright Copyright © 2004-2008, Moxiecode Systems AB, All rights reserved.
 */

(function() {
    tinymce.create('tinymce.plugins.BBCodePlugin', {
        init : function(ed, url) {
            var t = this, dialect = ed.getParam('bbcode_dialect', 'punbb').toLowerCase();
            var tempContent;
            
            ed.onBeforeSetContent.add(function(ed, o) {
            	tempContent = o.content;
            	//alert('first o.content: ' + tempContent);
                o.content = t['_' + dialect + '_bbcode2html'](o.content);
            });

            ed.onPostProcess.add(function(ed, o) {
                if (o.set) {
                	//alert('to html o.content: ' + tempContent);
                	o.content = t['_' + dialect + '_bbcode2html'](tempContent);
                }

                if (o.get) {
                	o.content = t['_' + dialect + '_html2bbcode'](o.content);
                }
            });
        },

        getInfo : function() {
            return {
                longname : 'BBCode Plugin',
                author : 'Moxiecode Systems AB',
                authorurl : 'http://tinymce.moxiecode.com',
                infourl : 'http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/bbcode',
                version : tinymce.majorVersion + "." + tinymce.minorVersion
            };
        },

        // Private methods

        // HTML -> BBCode in PunBB dialect
        _punbb_html2bbcode : function(s) {
            //s = tinymce.trim(s);
            
        	//alert('start html2bbcode , s: \n' + s);
        	
            function rep(re, str) {
                s = s.replace(re, str);
            };
            // example: <strong> to [b]
            
            function getCodeContent(match) {
                match = match.replace(/<br \/>/gi,"\n");
                match = match.replace(/<br\/>/gi,"\n");
                match = match.replace(/<br>/gi,"\n");
                match = match.replace(/<p>/gi,"\n");
                match = match.replace(/<\/p>/gi,"\n");
                return match.stripTags();
            };
			
            rep(/\[code\](.*?)\[\/code\]/gi, getCodeContent);
            rep(/\[quote\](.*?)\[\/quote\]/gi, getCodeContent);

            var words = s.split("<font");
            var t = words.length;
            for (i=0; i<t ; i++ )
            {
                rep(/<font (.*?)>/,"<font"+i+" $1>");
            }
            for (i=t; i>-1 ; i-- )
            {
                rep("<font"+i,"<font");
                rep(/<font (color|face|size)=\"(.*?)\" (color|face|size)=\"(.*?)\" (color|face|size)=\"(.*?)\">(.*?)<\/font>/g,"[$1=$2][$3=$4][$5=$6]$7[/$5][/$3][/$1]"); 
                rep(/<font (color|face|size)=\"(.*?)\" (color|face|size)=\"(.*?)\">(.*?)<\/font>/g,"[$1=$2][$3=$4]$5[/$3][/$1]"); 
                rep(/<font .*?(face|color|size)=\"(.*?)\">(.*?)<\/font>/g,"[$1=$2]$3[/$1]");
            }
            
            rep(/<ul>/gi,"[list]\n");
            rep(/<\/ul>/gi,"[/list]\n");
            rep(/<ol>/gi,"[list=1]");
            rep(/<\/ol>/gi,"[/list=1]");
            rep(/<li>/gi,"[\*]");
            rep(/<\/li>/gi,"[/\*]\n");
            
            //rep(/<div style=\"border:1px solid #888;padding:2px;\"><code class=\"prettyprint\">(.*?)<\/code><\/div>/gi,"[code]$1[/code]");
            //rep(/<div style=\"font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;color: #444444;line-height: 125%;background-color: #EEE;border: 2px dotted #999;padding:5px;margin:10px;\">(.*?)<\/div>/gi,"[quote]$1[/quote]");
            rep(/<a.*?href=\"(.*?)\".*?>(.*?)<\/a>/gi,"[url=$1]$2[/url]"); 
            rep(/<font>(.*?)<\/font>/gi,"$1");
            rep(/<img src=\"(.*?)\" .*? \/>/gi,"[img]$1[/img]");
            rep(/<\/(strong|b)>/gi,"[/b]");
            rep(/<(strong|b)>/gi,"[b]");
            rep(/<\/(strike|s)>/gi,"[/s]");
            rep(/<(strike|s)>/gi,"[s]");
            rep(/<\/(em|i)>/gi,"[/i]");
            rep(/<(em|i)>/gi,"[i]");
            rep(/<\/u>/gi,"[/u]");
            rep(/<span style=\"text-decoration: ?underline;\">(.*?)<\/span>/gi,"[u]$1[/u]");
            rep(/<u>/gi,"[u]");
            rep(/<br \/>/gi,"\n");
            rep(/<br\/>/gi,"\n");
            rep(/<br>/gi,"\n");
            rep(/<p>/gi,"\n");
            rep(/<\/p>/gi,"\n");
            rep(/&nbsp;/gi," ");
            rep(/&quot;/gi,"\"");
            rep(/&lt;/gi,"<");
            rep(/&gt;/gi,">");
            rep(/&amp;/gi,"&");
            
            rep(/\[face=/gi,"[font=");
            rep(/\[\/face\]/gi,"[/font]");
            // reconvert emoticons
            
            function convertSmilies(smilie) {
            	var exp = new RegExp("\\[img\\]" + contextPath+ "\/mvnplugin\/mvnforum\/images\/emotion\/" + smilie + "\\[\/img\\]","gi"); 
            	return exp;
            }
            
            rep(convertSmilies("laughing\.gif"), "[:))]");
            rep(convertSmilies("smile\.gif"), "[:)]"); 
            rep(convertSmilies("crying\.gif"), "[:((]"); 
            rep(convertSmilies("sad\.gif"), "[:(]"); 
            rep(convertSmilies("sad\.gif"), "[:-(]"); 
            rep(convertSmilies("wink\.gif"), "[;)]"); 
            rep(convertSmilies("biggrin\.gif"), "[:D]"); 
            rep(convertSmilies("batting_eyelashes\.gif"), "[;;)]"); 
            rep(convertSmilies("confused\.gif"), "[:-/]"); 
            rep(convertSmilies("love\.gif"), "[:x]"); 

            rep(convertSmilies("blushing\.gif"), "[:\">]"); 

            rep(convertSmilies("tongue\.gif"), "[:p]"); 
            rep(convertSmilies("kiss\.gif"), "[:*]"); 
            rep(convertSmilies("shock\.gif"), "[:O]"); 
            rep(convertSmilies("angry\.gif"), "[X-\(]"); 

            rep(convertSmilies("smug\.gif"), "[:>]"); 

            rep(convertSmilies("cool\.gif"), "[B-)]"); 
            rep(convertSmilies("worried\.gif"), "[:-s]"); 

            rep(convertSmilies("devilish\.gif"), "[>:)]"); 

            rep(convertSmilies("straight_face\.gif"), "[:|]"); 
            rep(convertSmilies("raised_eyebrow\.gif"), "[/:)]"); 
            rep(convertSmilies("angel\.gif"), "[O:)]"); 
            rep(convertSmilies("nerd\.gif"), "[:-B]"); 
            rep(convertSmilies("talk_to_the_hand\.gif"), "[=;]"); 
            rep(convertSmilies("sleep\.gif"), "[I-)]"); 
            rep(convertSmilies("rolling_eyes\.gif"), "[8-|]"); 
            
            rep(convertSmilies("sick\.gif"), "[:-&]"); 

            rep(convertSmilies("shhh\.gif"), "[:-\$]"); 
            rep(convertSmilies("not_talking\.gif"), "[[-(]"); 
            rep(convertSmilies("clown\.gif"), "[:o)]"); 
            rep(convertSmilies("silly\.gif"), "[8-}]"); 
            rep(convertSmilies("tired\.gif"), "[(:|]"); 
            rep(convertSmilies("drooling\.gif"), "[=P~]"); 
            rep(convertSmilies("thinking\.gif"), "[:-?]"); 
            rep(convertSmilies("d_oh\.gif"), "[#-o]"); 

            rep(convertSmilies("applause\.gif"), "[=D>]"); 
            rep(convertSmilies("pig\.gif"), "[:@)]"); 
            rep(convertSmilies("cow\.gif"), "[3:-O]"); 
            rep(convertSmilies("monkey\.gif"), "[:(|)]"); 

            rep(convertSmilies("chicken\.gif"), "[~:>]"); 

            rep(convertSmilies("rose\.gif"), "[@};-]"); 
            rep(convertSmilies("good_luck\.gif"), "[%%-]"); 
            rep(convertSmilies("flag\.gif"), "[**==]"); 
            rep(convertSmilies("pumpkin\.gif"), "[(~~)]"); 
            rep(convertSmilies("coffee\.gif"), "[~o)]"); 
            rep(convertSmilies("idea\.gif"), "[*-:)]"); 
            rep(convertSmilies("skull\.gif"), "[8-X]"); 
            rep(convertSmilies("alien_1\.gif"), "[=:)]"); 

            rep(convertSmilies("alien_2\.gif"), "[>-)]"); 

            rep(convertSmilies("frustrated\.gif"), "[:-L]"); 

            rep(convertSmilies("cowboy\.gif"), "[<):)]"); 

            rep(convertSmilies("praying\.gif"), "[[-o<]"); 

            rep(convertSmilies("hypnotized\.gif"), "[@-)]"); 
            rep(convertSmilies("money_eyes\.gif"), "[\$-)]"); 
            //
            rep(convertSmilies("whistling\.gif"), "[:-\"]"); 

            rep(convertSmilies("liar\.gif"), "[:^o]"); 
            rep(convertSmilies("beat_up\.gif"), "[b-(]"); 

            rep(convertSmilies("peace\.gif"), "[:)>-]"); 

            rep(convertSmilies("shame_on_you\.gif"), "[[-X]"); 
            rep(convertSmilies("dancing\.gif"), "[\\:D/]"); 

            rep(convertSmilies("hugs\.gif"), "[>:D<]"); 

            return s; 
        },

        // BBCode -> HTML from PunBB dialect
        _punbb_bbcode2html : function(s) {
            s = tinymce.trim(s);
            
            if (htmlLoadTimes == 1) {
            	htmlLoadTimes = 0;
            	return s;
            }
            if (htmlLoadTimes == 0) {
            	htmlLoadTimes = 1;
            } 

            //alert('start bbcode2html, s: \n' + s);
            function rep(re, str) {
                s = s.replace(re, str);
            };
            
            function escapeCode(match) {
                match = match.replace(/<br \/>/gi,"\n");
                match = match.replace(/<br\/>/gi,"\n");
                match = match.replace(/<br>/gi,"\n");
                match = match.replace(/<p>/gi,"\n");
                match = match.replace(/<\/p>/gi,"\n");
                //match = match.unescapeHTML();
                match = match.escapeHTML();
                match = match.substring(match.indexOf("[code]")+6, match.indexOf("[/code]"));
                match = match.replace(/\[/gi,"[-");
                match = match.replace(/\]/gi,"-]");
                return "[code]" + match + "[/code]";
            };
            
            rep(/\[\/\*\]/gi,"");
            rep(/\n/gi,"<br />");
            s = s.replace(/\[code\](.*?)\[\/code\]/g,escapeCode);
            rep(/\[font=/gi,"[face=");
            rep(/\[\/font/gi,"[/face");
            rep(/\n/gi,"<br />");
            //rep(/\[(color|face|size)=(.*?)\]\[(color|face|size)=(.*?)\]\[(color|face|size)=(.*?)\](.*?)\[\/(color|face|size)\].*?\[\/(color|face|size)\].*?\[\/(color|face|size)\]/gi,"<font $1=\"$2\" $3=\"$4\" $5=\"$6\">$7</font>");
            //rep(/\[(color|face|size)=(.*?)\]\[(color|face|size)=(.*?)\](.*?)\[\/(color|face|size)\].*?\[\/(color|face|size)\]/gi,"<font $1=\"$2\" $3=\"$4\">$5</font>");
            //rep(/\[(color|face|size)=(.*?)\](.*?)\[\/(color|face|size)\]/gi,"<font $1=\"$2\">$3</font>");

            rep(/\[(color|size|face)=(.*?)\]/gi,"<font $1=\"$2\">");
            rep(/\[\/(color|size|face)\]/gi,"</font>");
            //rep(/\[color=(.*?)\](.*?)\[\/color\]/gi,"<font color=\"$1\">$2</font>");
            //rep(/\[face=(.*?)\](.*?)\[\/face\]/gi,"<font face='$1'>$2</font>");
            //rep(/\[size=(.*?)\](.*?)\[\/size\]/gi,"<font size=\"$1\">$2</font>");
            // example: [b] to <strong>
            
            rep(/\[list\]/gi,"<ul>");
            rep(/\[\/list\]/gi,"</ul>");
            rep(/\[list=1\]/gi,"<ol>");
            rep(/\[\/list=1\]/gi,"</ol>");
            rep(/\[\*\]/gi,"<li>");
            rep(/\[\/\*\]/gi,"</li>");
            
            rep(/\[b\]/gi,"<strong>");
            rep(/\[\/b\]/gi,"</strong>");
            rep(/\[s\]/gi,"<strike>");
            rep(/\[\/s\]/gi,"</strike>");
            rep(/\[i\]/gi,"<em>");
            rep(/\[\/i\]/gi,"</em>");
            rep(/\[u\]/gi,"<u>");
            rep(/\[\/u\]/gi,"</u>");
            rep(/\[url=([^\]]+)\](.*?)\[\/url\]/gi,"<a href=\"$1\">$2</a>");
            rep(/\[url\](.*?)\[\/url\]/gi,"<a href=\"$1\">$1</a>");
            rep(/\[img\](.*?)\[\/img\]/gi,"<img src=\"$1\" />");
            
            function restoreCode(match) {
                match = match.substring(match.indexOf("[code]")+6, match.indexOf("[/code]"));
                match = match.replace(/\[-/gi,"[");
                match = match.replace(/\-\]/gi,"]");
                return "[code]" + match + "[/code]";
            };
            
            rep(/\[code\](.*?)\[\/code\]/gi, restoreCode);
            
            //rep(/\[code\](.*?)\[\/code\]/gi,"<div style=\"border:1px solid #888;padding:2px;\"><code class=\"prettyprint\">$1</code></div><br>");
            //rep(/\[quote\](.*?)\[\/quote\]/gi,"<div style=\"font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;color: #444444;line-height: 125%;background-color: #EEE;border: 2px dotted #999;padding:5px;margin:10px;\">$1</div><br>");
            
            // Emoticon
            rep(/\[:\)\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/laughing.gif\" />");
            rep(/\[:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smile.gif\" />");
            rep(/\[:-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smile.gif\" />");
            rep(/\[:\(\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/crying.gif\" />");
            rep(/\[:\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sad.gif\" />");
            rep(/\[:-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sad.gif\" />");
            rep(/\[;\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/wink.gif\" />");
            rep(/\[:D\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/biggrin.gif\" />");
            rep(/\[;;\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/batting_eyelashes.gif\" />");
            rep(/\[:-\/\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/confused.gif\" />");
            rep(/\[:x\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/love.gif\" />");

            rep(/\[:\"\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/blushing.gif\" />");
            rep(/\[:&quot;&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/blushing.gif\" />");

            rep(/\[:p\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/tongue.gif\" />");
            rep(/\[:\*\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/kiss.gif\" />");
            rep(/\[:O\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shock.gif\" />");
            rep(/\[X-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/angry.gif\" />");

            rep(/\[:\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smug.gif\" />");
            rep(/\[:&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/smug.gif\" />");

            rep(/\[B-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cool.gif\" />");
            rep(/\[:-s\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/worried.gif\" />");

            rep(/\[\>:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/devilish.gif\" />");
            rep(/\[&gt;:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/devilish.gif\" />");

            rep(/\[:\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/straight_face.gif\" />");
            rep(/\[\/:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/raised_eyebrow.gif\" />");
            rep(/\[O:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/angel.gif\" />");
            rep(/\[:-B\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/nerd.gif\" />");
            rep(/\[=;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/talk_to_the_hand.gif\" />");
            rep(/\[I-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sleep.gif\" />");
            rep(/\[8-\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/rolling_eyes.gif\" />");

            rep(/\[:-&\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sick.gif\" />");
            rep(/\[:-&amp;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/sick.gif\" />");

            rep(/\[:-\$\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shhh.gif\" />");
            rep(/\[\[-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/not_talking.gif\" />");
            rep(/\[:o\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/clown.gif\" />");
            rep(/\[8-}\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/silly.gif\" />");
            rep(/\[\(:\|\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/tired.gif\" />");
            rep(/\[=P~\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/drooling.gif\" />");
            rep(/\[:-\?\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/thinking.gif\" />");
            rep(/\[#-o\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/d_oh.gif\" />");

            rep(/\[=D\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/applause.gif\" />");
            rep(/\[=D&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/applause.gif\" />");
            rep(/\[:@\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/pig.gif\" />");
            rep(/\[3:-O\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cow.gif\" />");
            rep(/\[:\(\|\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/monkey.gif\" />");

            rep(/\[~:\>\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/chicken.gif\" />");
            rep(/\[~:&gt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/chicken.gif\" />");

            rep(/\[@\};-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/rose.gif\" />");
            rep(/\[%%-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/good_luck.gif\" />");
            rep(/\[\*\*==\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/flag.gif\" />");
            rep(/\[\(~~\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/pumpkin.gif\" />");
            rep(/\[~o\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/coffee.gif\" />");
            rep(/\[\*-:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/idea.gif\" />");
            rep(/\[8-X\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/skull.gif\" />");
            rep(/\[=:\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_1.gif\" />");

            rep(/\[\>-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_2.gif\" />");
            rep(/\[&gt;-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/alien_2.gif\" />");

            rep(/\[:-L\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/frustrated.gif\" />");

            rep(/\[<\):\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cowboy.gif\" />");
            rep(/\[&lt;\):\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/cowboy.gif\" />");

            rep(/\[\[-o<\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/praying.gif\" />");
            rep(/\[\[-o&lt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/praying.gif\" />");

            rep(/\[@-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hypnotized.gif\" />");
            rep(/\[\$-\)\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/money_eyes.gif\" />");

            rep(/\[:-\"\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/whistling.gif\" />");
            rep(/\[:-&quot;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/whistling.gif\" />");

            rep(/\[:\^o\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/liar.gif\" />");
            rep(/\[b-\(\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/beat_up.gif\" />");

            rep(/\[:\)\>-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/peace.gif\" />");
            rep(/\[:\)&gt;-\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/peace.gif\" />");

            rep(/\[\[-X\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/shame_on_you.gif\" />");
            rep(/\[\\:D\/\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/dancing.gif\" />");

            rep(/\[\>:D<\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hugs.gif\" />");
            rep(/\[&gt;:D&lt;\]/gi,"<img src=\""+contextPath+"/mvnplugin/mvnforum/images/emotion/hugs.gif\" />");  
            //alert('returning ' + s);
            return s; 
        }
    });

    // Register plugin
    tinymce.PluginManager.add('bbcode', tinymce.plugins.BBCodePlugin);
})();