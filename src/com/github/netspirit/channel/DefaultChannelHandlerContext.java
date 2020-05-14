package com.github.netspirit.channel;

public class DefaultChannelHandlerContext extends AbstractChannelHandlerContext{

	DefaultChannelHandlerContext(EventExecutor ioexecutor, Executor workers, boolean inbound, boolean outbound) {
		super(ioexecutor, workers, inbound, outbound);
	}

}
