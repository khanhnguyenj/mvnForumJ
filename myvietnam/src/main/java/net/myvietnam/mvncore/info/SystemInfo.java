/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/info/SystemInfo.java,v 1.10 2007/01/15 10:31:14 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.10 $
 * $Date: 2007/01/15 10:31:14 $
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
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.info;

public class SystemInfo {

    private String vmName;
    private String vmVendor;
    private String vmVersion;
    private String runtimeName;
    private String runtimeVersion;
    private String osName;
    private String osVersion;
    private String cpu;

    long totalMemory    = 0;
    long freeMemory     = 0;
    long totalMemoryKB  = 0;
    long freeMemoryKB   = 0;

    public SystemInfo() {
        vmName          = getProperty("java.vm.name");
        vmVendor        = getProperty("java.vm.vendor");
        vmVersion       = getProperty("java.vm.version");
        runtimeName     = getProperty("java.runtime.name");
        runtimeVersion  = getProperty("java.runtime.version");
        osName          = getProperty("os.name");
        osVersion       = getProperty("os.version");
        cpu             = getProperty("sun.cpu.isalist");

        Runtime runtime = Runtime.getRuntime();
        totalMemory     = runtime.totalMemory();
        freeMemory      = runtime.freeMemory();
        totalMemoryKB   = totalMemory/1024;
        freeMemoryKB    = freeMemory/1024;
    }

    public static String getProperty(String key) {
        String retValue = null;
        try {
            retValue = System.getProperty(key, "");
        } catch (Exception ex) {
            retValue = "no access";
        }
        return retValue;
    }

    public String getCpu() {
        return cpu;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getRuntimeName() {
        return runtimeName;
    }

    public String getRuntimeVersion() {
        return runtimeVersion;
    }

    public String getVmName() {
        return vmName;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public long getFreeMemoryKB() {
        return freeMemoryKB;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getTotalMemoryKB() {
        return totalMemoryKB;
    }
}
