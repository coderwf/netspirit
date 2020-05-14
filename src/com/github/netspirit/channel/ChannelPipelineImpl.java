package com.github.netspirit.channel;

import java.util.HashMap;
import java.util.Map;

public class ChannelPipelineImpl implements ChannelPipeline{
    
	private AbstractChannelHandlerContext head;
	private AbstractChannelHandlerContext tail;
	
	private Channel channel;
	private Executor workers;
	private EventExecutor ioexecutor;
	
	//
	
	private Map<String, AbstractChannelHandlerContext> contextsMap = new HashMap<String, AbstractChannelHandlerContext>(10);
	
	public ChannelPipelineImpl(Channel channel, Executor workers) {
	    this.channel = channel;
	    this.ioexecutor = this.channel.ioexecutor;
	    this.workers = workers;
	}
	
	@Override
	public ChannelInBoundInvoker fireChannelRead(Object read) {
	    AbstractChannelHandlerContext.invokeChannelRead(head, read);
	    return this;
	}

	@Override
	public ChannelInBoundInvoker fireChannelRegistered() {
		AbstractChannelHandlerContext.invokeChannelRegistered(head);
		return this;
	}

	@Override
	public ChannelInBoundInvoker fireChannelUnregistered() {
		AbstractChannelHandlerContext.invokeChannelUnregistered(head);
		return this;
	}

	@Override
	public ChannelInBoundInvoker fireExceptionCaught(Throwable exc) {
		AbstractChannelHandlerContext.invokeExceptionCaught(head, exc);
		return this;
	}
    
	private AbstractChannelHandlerContext newHandlerContext(EventExecutor ioexecutor,Executor workers,  ChannelHandler handler) {
		return new DefaultChannelHandlerContext(ioexecutor,workers, isInbound(handler), isOutboound(handler));
	}
	
	private boolean isInbound(ChannelHandler handler) {
		return handler instanceof AbstractChannelInBoundHandler;
	}
	
	private boolean isOutboound(ChannelHandler handler) {
	    return handler instanceof AbstractChannelOutBoundHandler;	
	}
	
	private void checkDupName(String name) {
        if(contextsMap.containsKey(name))
        	throw new RuntimeException(String.format("dup handler %s", name));
	}
	
	private ChannelPipeline addBeforeCtx(AbstractChannelHandlerContext beforeWhat, String name, AbstractChannelHandlerContext ctx) {
		
		checkDupName(name);
	    this.contextsMap.put(name, ctx);
	    
		ctx.next = beforeWhat;
		ctx.prev = beforeWhat.prev;
		beforeWhat.prev.next = ctx;
		beforeWhat.prev = ctx;
		
		return this;
	}
	
	@Override
	public ChannelPipeline addLast(ChannelHandler handler) {
	    return addLast(handler.getClass().getSimpleName(), handler);
	}

	@Override
	public ChannelPipeline addLast(String name, ChannelHandler handler) {
		AbstractChannelHandlerContext newCtx = newHandlerContext(ioexecutor, workers, handler);
	    return addBeforeCtx(tail, name, newCtx);
	}

	@Override
	public ChannelPipeline addFirst(ChannelHandler handler) {
	    return addFirst(handler.getClass().getSimpleName(), handler);
	}

	@Override
	public ChannelPipeline addFirst(String name, ChannelHandler handler) {
		AbstractChannelHandlerContext newCtx = newHandlerContext(ioexecutor, workers, handler);
		return addBeforeCtx(head.next, name, newCtx);
	}

	@Override
	public ChannelPipeline addBefore(String before, ChannelHandler handler) {
	    return addBefore(before, handler.getClass().getSimpleName(), handler);
	}

	@Override
	public ChannelPipeline addBefore(String before, String name, ChannelHandler handler) {
		AbstractChannelHandlerContext beforeWhat = contextsMap.get(before);
		if(beforeWhat == null)
			throw new RuntimeException(String.format("no handler %s", name));
		AbstractChannelHandlerContext newCtx = newHandlerContext(ioexecutor, workers, handler);
		return addBeforeCtx(beforeWhat, name, newCtx);
	}

	@Override
	public ChannelPipeline addAfter(String after, ChannelHandler handler) {
		return addAfter(after, handler.getClass().getSimpleName(), handler);
	}

	@Override
	public ChannelPipeline addAfter(String after, String name, ChannelHandler handler) {
		AbstractChannelHandlerContext afterWhat = contextsMap.get(after);
		if(afterWhat == null)
			throw new RuntimeException(String.format("no handler %s", name));
		AbstractChannelHandlerContext newCtx = newHandlerContext(ioexecutor, workers, handler);
		return addBeforeCtx(afterWhat.next, name, newCtx);
	}
	
}


