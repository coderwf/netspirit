package com.github.netspirit.channel;

public interface ChannelInBoundInvoker {
	
    public ChannelInBoundInvoker fireChannelRead(Object read);
    
    public ChannelInBoundInvoker fireChannelRegistered();
    
    public ChannelInBoundInvoker fireChannelUnregistered();
    
    public ChannelInBoundInvoker fireExceptionCaught(Throwable exc);
    
}
