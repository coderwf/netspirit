package com.github.netspirit.channel;

public class AbstractChannelHandlerContext implements ChannelInBoundInvoker, ChannelOutboundInvoker{
    
	//bound type
	final boolean inbound;
	final boolean outbound;
	
	//prev next
	AbstractChannelHandlerContext prev;
	AbstractChannelHandlerContext next;
	
	//handler
	private ChannelHandler handler;
	
	//channel
	private Channel channel;
	
	//pipeline
	private ChannelPipeline pipeline;
	
	//io executor
	private EventExecutor ioexecutor;
	
	//workers
	private Executor workers;
	
	
	public final ChannelHandler handler() {
		return this.handler;
	}
	
	public final Channel channel() {
		return this.channel;
	}
	
	public final ChannelPipeline pipeline() {
		return this.pipeline;
	}
	
	public final EventExecutor ioexecutor() {
		return this.ioexecutor;
	}
	
	AbstractChannelHandlerContext(EventExecutor ioexecutor, Executor workers, boolean inbound, boolean outbound) {
	    this.inbound = inbound;
	    this.outbound = outbound;
	    this.ioexecutor = ioexecutor;
	    this.workers = workers;
	}
	
	public void execute(Runnable runner) {
		this.workers.execute(runner);
	}
	
	private AbstractChannelHandlerContext findNextInbound() {
		AbstractChannelHandlerContext from = this;
		do {
			from = from.next;
		}while(! from.inbound);
		return from;
	}
	
	private AbstractChannelHandlerContext findNextOutbound() {
		AbstractChannelHandlerContext from = this;
		do {
			from = from.next;
		}while(! from.outbound);
		return from;
	}
	
	@Override
	public ChannelInBoundInvoker fireChannelRead(Object read) {
		//从当前ctx开始寻找
		AbstractChannelHandlerContext next = findNextInbound();
	    invokeChannelRead(next, read);
	    return this;
	}
    
	public static final void invokeChannelRead(AbstractChannelHandlerContext ctx, Object read) {
	    ((ChannelInBoundHandler)ctx.handler()).channelRead(ctx, read);
	}
	
	@Override
	public ChannelInBoundInvoker fireChannelRegistered() {
		AbstractChannelHandlerContext next = findNextInbound();
	    invokeChannelRegistered(next);
	    return this;
	}
    
	public static final void invokeChannelRegistered(AbstractChannelHandlerContext ctx) {
		((ChannelInBoundHandler)ctx.handler()).channelRegistered(ctx);
	}
	
	@Override
	public ChannelInBoundInvoker fireChannelUnregistered() {
		AbstractChannelHandlerContext next = findNextInbound();
	    invokeChannelRegistered(next);
	    return this;
	}

	public static final void invokeChannelUnregistered(AbstractChannelHandlerContext ctx) {
		((ChannelInBoundHandler)ctx.handler()).channelUnRegistered(ctx);
	}
	
	@Override
	public ChannelInBoundInvoker fireExceptionCaught(Throwable exc) {
		AbstractChannelHandlerContext next = findNextInbound();
		invokeExceptionCaught(next, exc);
		return this;
	}
	
	public static final void invokeExceptionCaught(AbstractChannelHandlerContext ctx, Throwable exc) {
		((ChannelInBoundHandler)ctx.handler()).expCaught(ctx,exc);
	}
}
