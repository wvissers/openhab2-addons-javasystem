/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.javasystem.handler;

import static org.openhab.binding.javasystem.JavaSystemBindingConstants.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.javasystem.internal.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AmbilightHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Wim Vissers - Initial contribution
 */
public class JavaSystemHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(JavaSystemHandler.class);

    private final ChannelUID channelFreeMemory;
    private final ChannelUID channelFreeMemoryText;
    private final ChannelUID channelTotalMemory;
    private final ChannelUID channelTotalMemoryText;
    private final ChannelUID channelMaxMemory;
    private final ChannelUID channelMaxMemoryText;
    private final ChannelUID channelUsedMemoryPercent;
    private final ChannelUID channelFreeMemoryPercent;

    // Scheduler
    ScheduledFuture<?> refreshJob;

    public JavaSystemHandler(Thing thing) {
        super(thing);
        channelFreeMemory = new ChannelUID(thing.getUID(), CHANNEL_FREE_MEMORY);
        channelFreeMemoryText = new ChannelUID(thing.getUID(), CHANNEL_FREE_MEMORY_TEXT);
        channelTotalMemory = new ChannelUID(thing.getUID(), CHANNEL_TOTAL_MEMORY);
        channelTotalMemoryText = new ChannelUID(thing.getUID(), CHANNEL_TOTAL_MEMORY_TEXT);
        channelMaxMemory = new ChannelUID(thing.getUID(), CHANNEL_MAX_MEMORY);
        channelMaxMemoryText = new ChannelUID(thing.getUID(), CHANNEL_MAX_MEMORY_TEXT);
        channelUsedMemoryPercent = new ChannelUID(thing.getUID(), CHANNEL_USED_MEMORY_PERCENT);
        channelFreeMemoryPercent = new ChannelUID(thing.getUID(), CHANNEL_FREE_MEMORY_PERCENT);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            updateStates();
        }
    }

    private void updateStates() {
        Runtime runtime = Runtime.getRuntime();
        updateState(channelFreeMemory, new DecimalType(runtime.freeMemory()));
        updateState(channelFreeMemoryText, new StringType(FormatUtil.formatBytes(runtime.freeMemory())));
        updateState(channelTotalMemory, new DecimalType(runtime.totalMemory()));
        updateState(channelTotalMemoryText, new StringType(FormatUtil.formatBytes(runtime.totalMemory())));
        updateState(channelMaxMemory, new DecimalType(runtime.maxMemory()));
        updateState(channelMaxMemoryText, new StringType(FormatUtil.formatBytes(runtime.maxMemory())));
        long usedMemPercent = (runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.maxMemory();
        updateState(channelUsedMemoryPercent, new DecimalType(usedMemPercent));
        updateState(channelFreeMemoryPercent, new DecimalType(100 - usedMemPercent));
    }

    /**
     * Check every 60 seconds if one of the alarm times is reached.
     */
    protected void startAutomaticRefresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    updateStates();
                } catch (Exception e) {
                    logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                }
            }
        };

        refreshJob = scheduler.scheduleAtFixedRate(runnable, 0, 60, TimeUnit.SECONDS);
    }

    protected void stopAutomaticRefresh() {
        if (refreshJob != null) {
            refreshJob.cancel(true);
            refreshJob = null;
        }
    }

    @Override
    public void initialize() {

        Configuration config = getThing().getConfiguration();

        updateStatus(ThingStatus.ONLINE);

        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
        startAutomaticRefresh();
    }
}
