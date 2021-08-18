/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/EnvironmentService.java,v
 * 1.11 2009/08/31 03:48:16 minhnn Exp $ $Author: minhnn $ $Revision: 1.11 $ $Date: 2009/08/31
 * 03:48:16 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib MUST remain intact in the scripts
 * and source code.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Dung Bui
 */
package net.myvietnam.mvncore.service;

public interface EnvironmentService {

  public static final int DEFAULT_RUN_RETRY = 5;

  public static final int PRODUCT_DISABLED = 0;
  public static final int PRODUCT_OPENSOURCE = 1;
  public static final int PRODUCT_ENTERPRISE = 2;

  /**
   * Return the run mode of forum module, could be PRODUCT_DISABLED, PRODUCT_OPENSOURCE or
   * PRODUCT_ENTERPRISE
   * 
   * @return the run mode
   */
  public int getForumRunMode();

  /**
   * Return the run mode of CMS module, could be PRODUCT_DISABLED, PRODUCT_OPENSOURCE or
   * PRODUCT_ENTERPRISE
   * 
   * @return the run mode
   */
  public int getCmsRunMode();

  /**
   * Return the run mode of Ad module, could be PRODUCT_DISABLED, PRODUCT_OPENSOURCE or
   * PRODUCT_ENTERPRISE
   * 
   * @return the run mode
   */
  public int getAdRunMode();

  /**
   * Return if running in Portlet environment or non Portlet (Servlet)
   * 
   * @return true if running in portlet environment
   */
  public boolean isPortlet();

  /**
   * Return a string that this environment is for a specific customer name
   * 
   * @return the customer name in case customized for a customer
   */
  public String customizeFor();

  /**
   * This method could be use to start running again after being called setShouldStop.
   */
  public void setShouldRun();

  /**
   * This method could be use to stop run the forum in some condition. Some use could be a page that
   * immediately stop the forum for security. Other usage is to check to run on some environment
   * such as Servlet 2.3 or later.
   * 
   * This method will not try to retry running
   *
   * @param reason String the reason of the action, this reason will be shown in the error page
   */
  public void setShouldStop(String reason);

  /**
   * This method could be use to stop run the forum in some condition. Some use could be a page that
   * immediately stop the forum for security. Other usage is to check to run on some environment
   * such as Servlet 2.3 or later.
   * 
   * When retry, it will start again, and if it is called setShouldStop a number of time, it will
   * stop permanently.
   * 
   * Note that the retryCount only set 1 time, the second call, the retryCount is ignored
   *
   * @param reason String the reason of the action, this reason will be shown in the error page
   * @param retryCount the number of retries, each retry period is 5 minutes.
   */
  public void setShouldStop(String reason, int retryCount);

  /**
   * Return the current running status, which could be changed by setShouldRun and setShouldStop
   * 
   * @return the current running status
   */
  public boolean isShouldRun();

  /**
   * Return the reason if the system should not run
   * 
   * @return the reason
   */
  public String getReason();

  /**
   * This method could be used to overloaded the default parameters.
   */
  public void overloadEnvironment();

}
