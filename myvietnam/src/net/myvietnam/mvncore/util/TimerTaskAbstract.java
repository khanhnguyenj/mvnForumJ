/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/TimerTaskAbstract.java,v 1.2 2007/05/23 12:27:56 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2007/05/23 12:27:56 $
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
 * @author: Phuong, Pham Dinh Duy 
 */
package net.myvietnam.mvncore.util;

import java.util.Date;
import java.util.TimerTask;

public abstract class TimerTaskAbstract extends TimerTask {
    
    protected boolean scheduled = false;
    
    public synchronized void schedule(Date firstTime, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, firstTime, period);
        }
    }

    public synchronized void schedule(Date time) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, time);
        }
    }

    public synchronized void schedule(long delay) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay);
        }
    }

    public synchronized void schedule(long delay, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay, period);
        }
    }

    public synchronized void scheduleAtFixedRate(Date firstTime, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, firstTime, period);
        }
    }

    public synchronized void scheduleAtFixedRate(long delay, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay, period);
        }
    }

}
