/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.javasystem;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link JavaSystemBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Wim Vissers - Initial contribution
 */
public class JavaSystemBindingConstants {

    public static final String BINDING_ID = "javasystem";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_RUNTIME = new ThingTypeUID(BINDING_ID, "runtime");

    // List of all Channel ids
    public final static String CHANNEL_FREE_MEMORY = "freeMemory";
    public final static String CHANNEL_FREE_MEMORY_TEXT = "freeMemoryText";
    public final static String CHANNEL_TOTAL_MEMORY = "totalMemory";
    public final static String CHANNEL_TOTAL_MEMORY_TEXT = "totalMemoryText";
    public final static String CHANNEL_MAX_MEMORY = "maxMemory";
    public final static String CHANNEL_MAX_MEMORY_TEXT = "maxMemoryText";

}
