/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb;

public enum Mode {

    RestAPI("restAPI"), ClientAPI("clientAPI");
    private String mode;

    Mode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static Mode get(String mode) {
        if (Mode.RestAPI.getMode().equals(mode)) {
            return RestAPI;
        } else if (Mode.ClientAPI.getMode().equals(mode)) {
            return ClientAPI;
        } else {
            throw new EnumConstantNotPresentException(Mode.class, mode);
        }
    }
}
