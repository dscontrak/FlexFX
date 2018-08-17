/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.process;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

/**
 *
 * @author daniel
 */
public class RssTask {

    public Worker<String> worker;
    public AtomicBoolean shouldThrow = new AtomicBoolean(false);

    public RssTask() {
        worker = new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateTitle("Example Task");
                updateMessage("Starting...");
                final int total = 250;
                updateProgress(0, total);
                for (int i = 1; i <= total; i++) {
                    if (isCancelled()) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                        return null; // ignored
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                        return null; // ignored 
                    }
                    if (shouldThrow.get()) {
                        throw new RuntimeException("Exception thrown at "
                                + System.currentTimeMillis());
                    }
                    updateTitle("Example Task (" + i + ")");
                    updateMessage("Processed " + i + " of " + total + " items.");
                    updateProgress(i, total);
                }
                return "Completed at " + System.currentTimeMillis();
            }

            @Override
            protected void scheduled() {
                System.out.println("The task is scheduled.");
            }

            @Override
            protected void running() {
                System.out.println("The task is running.");
            }
        };
        ((Task<String>) worker).setOnSucceeded(event -> {
            System.out.println("IN -> The task succeeded.");
        }
        );
        ((Task<String>) worker).setOnCancelled(event -> {
            System.out.println("IN -> The task is canceled.");
        }
        );
        ((Task<String>) worker).setOnFailed(event -> {
            System.out.println("IN -> The task failed.");
        }
        );
    }

}
