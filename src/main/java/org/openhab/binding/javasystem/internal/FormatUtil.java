/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.javasystem.internal;

/**
 * The {@link FormatUtil} is responsible for formatting.
 *
 * @author Wim Vissers - Initial contribution
 */
public class FormatUtil {

    public static final String formatBytes(long bytes) {
        if (bytes > 1024 * 1024 * 2) {
            // > 1MByte
            long mb = bytes / 1024 / 1024;
            return "" + mb + " MB";
        } else if (bytes > 1024 * 2) {
            // > 2 kbyte
            long kb = bytes / 1024;
            return "" + kb + " kB";
        } else {
            return "" + bytes + " B";
        }
    }

}
