package com.github.netspirit.channel;

public interface ChannelInBoundHandler extends ChannelHandler{

	public void channelRead(AbstractChannelHandlerContext ctx, Object read);
    
	public void channelRegistered(AbstractChannelHandlerContext ctx);
	
	public void channelUnRegistered(AbstractChannelHandlerContext ctx);
	
}
