/*
 * $Header: /cvsroot/mvnforum/mvnforum/i18n/tool/LocalizingKeyFinder.java,v 1.13 2008/05/05 10:25:33 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2008/05/05 10:25:33 $
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
 * @author: Minh Nguyen  
 * @author: Phong Ta Quoc  
 */
/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
//package org.apache.tools.ant.taskdefs.optional.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;

/**
 * This a a simple finder which find the key are not in a properties file call
 * bundle file Just find the keys and show its position (file, line in file).
 */
public class LocalizingKeyFinder extends MatchingTask {
    /**
     * Family name of resource bundle
     */
    private String bundle;
    /**
     * Starting token to identify keys
     */
    private String startToken;
    /**
     * Ending token to identify keys
     */
    private String endToken;
    /**
     * Resource Bundle file encoding scheme, defaults to srcEncoding
     */
    private String bundleEncoding;
    /**
     * Vector to hold source file sets.
     */
    private Vector filesets = new Vector();
    /**
     * Holds key value pairs loaded from resource bundle file
     */
    private Hashtable resourceMap = new Hashtable();
    /**
     * Used to resolve file names.
     */
    private FileUtils fileUtils = FileUtils.newFileUtils();
    /**
     * Has at least one file from the bundle been loaded?
     */
    private boolean loaded = false;
    /**
     * Sets Family name of resource bundle; required.
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
    /**
     * Sets starting token to identify keys; required.
     */
    public void setStartToken(String startToken) {
        this.startToken = startToken;
    }
    /**
     * Sets ending token to identify keys; required.
     */
    public void setEndToken(String endToken) {
        this.endToken = endToken;
    }
    /**
     * Sets Resource Bundle file encoding scheme; optional.  Defaults to source file
     * encoding
     */
    public void setBundleEncoding(String bundleEncoding) {
        this.bundleEncoding = bundleEncoding;
    }
    /**
     * Adds a set of files to translate as a nested fileset element.
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }
    /**
     * Check attributes values, load resource map and translate
     */
    public void execute() throws BuildException {
        if (bundle == null) {
            throw new BuildException("The bundle attribute must be set.",
            getLocation());
        }
        if (startToken == null) {
            throw new BuildException("The starttoken attribute must be set.",
            getLocation());
        }
        if (endToken == null) {
            throw new BuildException("The endtoken attribute must be set.",
            getLocation());
        }
        if (bundleEncoding == null) {
            bundleEncoding = "Cp1252";
        }

        loadResourceMaps();
        translate();
    }
    /**
     * Load resource maps based on resource bundle encoding scheme.
     * The resource bundle lookup searches for resource files with various
     * suffixes on the basis of (1) the desired locale and (2) the default
     * locale (basebundlename), in the following order from lower-level
     * (more specific) to parent-level (less specific):
     *
     * basebundlename + "_" + language1 + "_" + country1 + "_" + variant1
     * basebundlename + "_" + language1 + "_" + country1
     * basebundlename + "_" + language1
     * basebundlename
     * basebundlename + "_" + language2 + "_" + country2 + "_" + variant2
     * basebundlename + "_" + language2 + "_" + country2
     * basebundlename + "_" + language2
     *
     * To the generated name, a ".properties" string is appeneded and
     * once this file is located, it is treated just like a properties file
     * but with bundle encoding also considered while loading.
     */
    private void loadResourceMaps() throws BuildException {
        processBundle(bundle, true);
    }
    /**
     * Process each file that makes up this bundle.
     */
    private void processBundle(final String bundleFile,
        final boolean checkLoaded) throws BuildException {
        final File propsFile = new File(bundleFile + ".properties");
        log(propsFile + "[ BUNDLE PROPERTIES FILE ]");
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(propsFile);
            loaded = true;
            log("Using " + propsFile, Project.MSG_DEBUG);
            loadResourceMap(ins);
        } catch (IOException ioe) {
            log(propsFile + " not found.", Project.MSG_DEBUG);
            if (!loaded && checkLoaded) {
                throw new BuildException(ioe.getMessage());
            }
        }
    }
    /**
     * Load resourceMap with key value pairs. Values of existing keys are not
     * overwritten. Bundle's encoding scheme is used.
     */
    private void loadResourceMap(FileInputStream ins) throws BuildException {
        try {
            BufferedReader in = null;
            InputStreamReader isr = new InputStreamReader(ins, bundleEncoding);
            in = new BufferedReader(isr);
            String line = null;
            while ((line = in.readLine()) != null) {
                //So long as the line isn't empty and isn't a comment...
                if (line.trim().length() > 1
                && ('#' != line.charAt(0) || '!' != line.charAt(0))) {
                    //Legal Key-Value separators are :, = and white space.
                    int sepIndex = line.indexOf('=');
                    if (-1 == sepIndex) {
                        sepIndex = line.indexOf(':');
                    }
                    if (-1 == sepIndex) {
                        for (int k = 0; k < line.length(); k++) {
                            if (Character.isSpaceChar(line.charAt(k))) {
                                sepIndex = k;
                                break;
                            }
                        }
                    }
                    //Only if we do have a key is there going to be a value
                    if (-1 != sepIndex) {
                        String key = line.substring(0, sepIndex).trim();
                        String value = line.substring(sepIndex + 1).trim();
                        //Handle line continuations, if any
                        while (value.endsWith("\\")) {
                            value = value.substring(0, value.length() - 1);
                            if ((line = in.readLine()) != null) {
                                value = value + line.trim();
                            } else {
                                break;
                            }
                        }
                        if (key.length() > 0) {
                            //Has key already been loaded into resourceMap?
                            if (resourceMap.get(key) == null) {
                                resourceMap.put(key, value);
                            }
                        }
                    }
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage());
        }
    }
    /**
     * Reads source file line by line using the source encoding and searches for
     * keys that are sandwiched between the startToken and endToken. The values
     * for these keys are looked up from the hashtable and substituted. If the
     * hashtable doesn't contain the key, they key itself is used as the value.
     * Detination files and directories are created as needed. The destination
     * file is overwritten only if the forceoverwritten attribute is set to true
     * if the source file or any associated bundle resource file is newer than
     * the destination file.
     */
    private void translate() throws BuildException {
        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] srcFiles = ds.getIncludedFiles();
            int srcFilesCount = srcFiles.length;
            if (srcFilesCount == 1) {
                log("Checking 1 file in " + fs.getDir(getProject()), Project.MSG_INFO );
            } else {
                log("Checking " + srcFilesCount + " files in " + fs.getDir(getProject()), Project.MSG_INFO);
            }
            int bugCount = 0;
            int warningCount = 0;
            for (int j = 0; j < srcFiles.length; j++) {
                try {
                    File src = fileUtils.resolveFile(ds.getBasedir(),
                    srcFiles[j]);
                    log("Processing " + srcFiles[j], Project.MSG_DEBUG);
                    FileInputStream fis = new FileInputStream(src);
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(fis));
                    String line;
                    int lineNumber = 0;
                    while ((line = in.readLine()) != null) {
                        lineNumber++;
                        int startIndex = -1;
                        int endIndex = -1;
                        // just one key in a line (maybe no loop is also OK !!)
                        outer : while (true) {
                            startIndex = line.indexOf(startToken, endIndex + 1);
                            if ((startIndex < 0) || (startIndex + 1 >= line.length()) ) {
                                break;
                            }
                            endIndex = line.indexOf(endToken, startIndex + 1);
                            if (endIndex < 0) {
                                log("\nWARNING: Line contains the start key " +
                                    "but doesn't have the end key. ", Project.MSG_WARN);
                                log("WARNING: At line " + lineNumber + " in file : " + src, Project.MSG_WARN);
                                warningCount++;
                                break;
                            }
                            startIndex = line.indexOf("\"", startIndex);
                            if (startIndex < 0) {
                                // such as "return MVNForumResourceBundle.getString(locale, this.desc);"
                                if (line.indexOf("this.desc") < 0) {
                                    log("WARNING: \"" + line + "\" does not have starting quote", Project.MSG_WARN);
                                    warningCount++;
                                }
                                break;
                            }
                            endIndex = line.indexOf("\"", startIndex + 1);
                            if (endIndex < 0) {
                                // such as "return MVNForumResourceBundle.getString(locale, this.desc);"
                                log("WARNING: \"" + line + "\" does not have ending quote", Project.MSG_WARN);
                                warningCount++;
                                break;
                            }
                            String matches = line.substring(startIndex + 1, endIndex);
                            //log("[ MATCH ]::match = " + matches + " resource
                            // = " + (String) resourceMap.get(matches),
                            // Project.MSG_WARN);
                            /**
                             * Note: minhnn I change the code to consider a
                             * valid matches must begin with mvnforum
                             */
                            String replace = null;
                            replace = (String) resourceMap.get(matches);
                            //log (replace + "[ WHAT IS REPLACE ??]");
                            if (replace == null) {
                                bugCount++;
                                log("\nERROR: The key \"" + matches + "\" hasn't been defined.", Project.MSG_WARN);
                                log("ERROR: At line " + lineNumber + " in file : " + src, Project.MSG_WARN);
                                replace = matches;
                            }
                            line = line.substring(0, startIndex) + replace
                            + line.substring(endIndex + 1);
                            // minhnn: I dont know if the original code has bug
                            // I changed from "+ 1" to "- 1" and it works well
                            endIndex = startIndex + replace.length() - 1;
                            if (endIndex + 1 >= line.length()) {
                                break;
                            }
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ioe) {
                    throw new BuildException(ioe.getMessage(), getLocation());
                }
            }
            log("\n###########################################");
            log("Total error   count: " + bugCount);
            log("Total warning count: " + warningCount);
        }
    }
}