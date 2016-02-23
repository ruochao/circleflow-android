package com.mocircle.flowsample.utils;

import java.util.Random;

public class Simulator {

    public static void simulateNetworkRequest(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void simulateNetworkRequest() {
        long time = (long) (new Random(System.currentTimeMillis()).nextDouble() * 3000);
        simulateNetworkRequest(time);
    }
}
