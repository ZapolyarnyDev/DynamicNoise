package io.github.zapolyarnydev.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class VirtualThreadExecutor implements Executor {
    private final ThreadFactory threadFactory;

    public VirtualThreadExecutor() {
        this.threadFactory = Thread.ofVirtual().factory();
    }

    @Override
    public void execute(Runnable command) {
        threadFactory.newThread(command).start();
    }
}
