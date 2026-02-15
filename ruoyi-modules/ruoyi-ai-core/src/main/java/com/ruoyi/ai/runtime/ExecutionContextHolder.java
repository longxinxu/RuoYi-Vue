package com.ruoyi.ai.runtime;

/**
 * Thread local holder for execution context.
 */
public final class ExecutionContextHolder
{
    private static final ThreadLocal<ExecutionContext> HOLDER = new ThreadLocal<>();

    private ExecutionContextHolder()
    {
    }

    public static void setContext(ExecutionContext executionContext)
    {
        HOLDER.set(executionContext);
    }

    public static ExecutionContext getContext()
    {
        ExecutionContext context = HOLDER.get();
        return context != null ? context : ExecutionContext.empty();
    }

    public static void clear()
    {
        HOLDER.remove();
    }
}
