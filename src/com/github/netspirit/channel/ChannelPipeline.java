package com.github.netspirit.channel;

public interface ChannelPipeline extends ChannelInBoundInvoker, ChannelOutboundInvoker{
	
	//addLast
    public ChannelPipeline addLast(ChannelHandler handler);
    public ChannelPipeline addLast(String name, ChannelHandler handler);
    
    //addFirst
    public ChannelPipeline addFirst(ChannelHandler handler);
    public ChannelPipeline addFirst(String name, ChannelHandler handler);
    
    //addBefore
    public ChannelPipeline addBefore(String before, ChannelHandler handler);
    public ChannelPipeline addBefore(String before, String name, ChannelHandler handler);
    
    
    //addAfter
    public ChannelPipeline addAfter(String after, ChannelHandler handler);
    public ChannelPipeline addAfter(String after, String name, ChannelHandler handler);
    
}
