package org.thingsboard.server.service.script;

public class TestNashornJsInvokeService extends AbstractNashornJsInvokeService {

    private boolean useJsSandbox;
    private final int monitorThreadPoolSize;
    private final long maxCpuTime;
    private final int maxErrors;

    public TestNashornJsInvokeService(boolean useJsSandbox, int monitorThreadPoolSize, long maxCpuTime, int maxErrors) {
        this.useJsSandbox = useJsSandbox;
        this.monitorThreadPoolSize = monitorThreadPoolSize;
        this.maxCpuTime = maxCpuTime;
        this.maxErrors = maxErrors;
        init();
    }

    @Override
    protected boolean useJsSandbox() {
        return useJsSandbox;
    }

    @Override
    protected int getMonitorThreadPoolSize() {
        return monitorThreadPoolSize;
    }

    @Override
    protected long getMaxCpuTime() {
        return maxCpuTime;
    }

    @Override
    protected int getMaxErrors() {
        return maxErrors;
    }
}
