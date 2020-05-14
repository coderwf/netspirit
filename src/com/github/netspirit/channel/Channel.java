package com.github.netspirit.channel;

public class Channel {
    final EventExecutor ioexecutor;
    private ChannelPipeline pipeline;
    
    public Channel(EventExecutor ioexecutor, Executor workers) {
    	this.ioexecutor = ioexecutor;
    	this.pipeline = new ChannelPipelineImpl(this, workers);
    }
    
    public ChannelPipeline pipeline() {
    	return this.pipeline;
    }
    
}
