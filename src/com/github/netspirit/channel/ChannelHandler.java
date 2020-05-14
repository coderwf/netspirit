package com.github.netspirit.channel;

public interface ChannelHandler {
	public void expCaught(AbstractChannelHandlerContext ctx, Throwable exc);
}
