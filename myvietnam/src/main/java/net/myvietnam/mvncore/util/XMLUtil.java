/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/XMLUtil.java,v 1.4 2009/07/30 03:05:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2009/07/30 03:05:31 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Trung Tang
 */
package net.myvietnam.mvncore.util;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

public class XMLUtil {

    private static final Logger log = LoggerFactory.getLogger(XMLUtil.class);
    
    public static void updateNode(org.dom4j.Element root, String xpathExpression, String value) {
        org.dom4j.Node node = root.selectSingleNode(xpathExpression);
        if ( (node != null) && node.getNodeTypeName().equals("Element") ) {
            node.setText(value);
        }
    }

    // Borrow from http://viewvc.jboss.org/cgi-bin/viewvc.cgi/messaging/trunk/src/main/org/jboss/messaging/utils/XMLUtil.java
    public static String elementToString(Node n)
    {

       String name = n.getNodeName();

       short type = n.getNodeType();

       if (Node.CDATA_SECTION_NODE == type)
       {
          return "<![CDATA[" + n.getNodeValue() + "]]>";
       }

       if (name.startsWith("#"))
       {
          return "";
       }

       StringBuffer sb = new StringBuffer();
       sb.append('<').append(name);

       NamedNodeMap attrs = n.getAttributes();
       if (attrs != null)
       {
          for (int i = 0; i < attrs.getLength(); i++)
          {
             Node attr = attrs.item(i);
             sb.append(' ').append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
          }
       }

       String textContent = null;
       NodeList children = n.getChildNodes();

       if (children.getLength() == 0)
       {
          if ((textContent = XMLUtil.getTextContent(n)) != null && !"".equals(textContent))
          {
             sb.append(textContent).append("</").append(name).append('>');
          }
          else
          {
             sb.append("/>").append('\n');
          }
       }
       else
       {
          sb.append('>').append('\n');
          boolean hasValidChildren = false;
          for (int i = 0; i < children.getLength(); i++)
          {
             String childToString = elementToString(children.item(i));
             if (!"".equals(childToString))
             {
                sb.append(childToString);
                hasValidChildren = true;
             }
          }

          if (!hasValidChildren && ((textContent = XMLUtil.getTextContent(n)) != null))
          {
             sb.append(textContent);
          }

          sb.append("</").append(name).append('>');
       }

       return sb.toString();
    }

    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * This method is here because Node.getTextContent() is not available in JDK 1.4 and I would like
     * to have an uniform access to this functionality.
     *
     * Note: if the content is another element or set of elements, it returns a string representation
     *       of the hierarchy.
     *
     * TODO implementation of this method is a hack. Implement it properly.
     */
    // Borrow from http://viewvc.jboss.org/cgi-bin/viewvc.cgi/messaging/trunk/src/main/org/jboss/messaging/utils/XMLUtil.java
    public static String getTextContent(Node n)
    {
       if (n.hasChildNodes())
       {
          StringBuffer sb = new StringBuffer();
          NodeList nl = n.getChildNodes();
          for (int i = 0; i < nl.getLength(); i++)
          {
             sb.append(XMLUtil.elementToString(nl.item(i)));
             if (i < nl.getLength() - 1)
             {
                sb.append('\n');
             }
          }

          String s = sb.toString();
          if (s.length() != 0)
          {
             return s;
          }
       }

       Method[] methods = Node.class.getMethods();

       for (int i = 0; i < methods.length; i++)
       {
          if ("getTextContent".equals(methods[i].getName()))
          {
             Method getTextContext = methods[i];
             try
             {
                return (String)getTextContext.invoke(n, EMPTY_ARRAY);
             }
             catch (Exception e)
             {
                log.error("Failed to invoke getTextContent() on node " + n, e);
                return null;
             }
          }
       }

       String textContent = null;

       if (n.hasChildNodes())
       {
          NodeList nl = n.getChildNodes();
          for (int i = 0; i < nl.getLength(); i++)
          {
             Node c = nl.item(i);
             if (c.getNodeType() == Node.TEXT_NODE)
             {
                textContent = n.getNodeValue();
                if (textContent == null)
                {
                   // TODO This is a hack. Get rid of it and implement this properly
                   String s = c.toString();
                   int idx = s.indexOf("#text:");
                   if (idx != -1)
                   {
                      textContent = s.substring(idx + 6).trim();
                      if (textContent.endsWith("]"))
                      {
                         textContent = textContent.substring(0, textContent.length() - 1);
                      }
                   }
                }
                if (textContent == null)
                {
                   break;
                }
             }
          }

          // TODO This is a hack. Get rid of it and implement this properly
          String s = n.toString();
          int i = s.indexOf('>');
          int i2 = s.indexOf("</");
          if (i != -1 && i2 != -1)
          {
             textContent = s.substring(i + 1, i2);
          }
       }

       return textContent;
    }
    
}