/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/gui/MessageBox.java,v 1.9 2007/01/15 10:31:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.9 $
 * $Date: 2007/01/15 10:31:12 $
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
package net.myvietnam.mvncore.gui;

import java.awt.Component;
import javax.swing.JOptionPane;

public final class MessageBox {

    private MessageBox() {
    }

    public static void showErrorBox(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoBox(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningBox(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static void showQuestionBox(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Question", JOptionPane.QUESTION_MESSAGE);
    }
}
