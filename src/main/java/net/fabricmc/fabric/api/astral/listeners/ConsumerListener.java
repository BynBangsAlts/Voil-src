package net.fabricmc.fabric.api.astral.listeners;

import java.util.function.Consumer;
import net.fabricmc.fabric.api.astral.listeners.IListener;

public class ConsumerListener<T>
implements IListener {
    private final Class<T> target;
    private final int priority;
    private final Consumer<T> executor;

    public ConsumerListener(Class<T> target, int priority, Consumer<T> executor) {
        this.target = target;
        this.priority = priority;
        this.executor = executor;
    }

    public ConsumerListener(Class<T> target, Consumer<T> executor) {
        this(target, 0, executor);
    }

    @Override
    public void call(Object event) {
        this.executor.accept(this.target.cast(event));
    }

    public Class<T> getTarget() {
        return this.target;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
