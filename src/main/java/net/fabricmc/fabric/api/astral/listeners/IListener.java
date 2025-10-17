package net.fabricmc.fabric.api.astral.listeners;

public interface IListener {
    public void call(Object var1);

    public Class<?> getTarget();

    public int getPriority();

    @Deprecated
    public boolean isStatic();
}
