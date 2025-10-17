package net.fabricmc.fabric.api.astral;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.ICancellable;
import net.fabricmc.fabric.api.astral.IEventBus;
import net.fabricmc.fabric.api.astral.NoLambdaFactoryException;
import net.fabricmc.fabric.api.astral.listeners.IListener;
import net.fabricmc.fabric.api.astral.listeners.LambdaListener;

public class EventBus
implements IEventBus {
    private final Map<Object, List<IListener>> listenerCache = new ConcurrentHashMap<Object, List<IListener>>();
    private final Map<Class<?>, List<IListener>> staticListenerCache = new ConcurrentHashMap();
    private final Map<Class<?>, List<IListener>> listenerMap = new ConcurrentHashMap();
    private final List<LambdaFactoryInfo> lambdaFactoryInfos = new ArrayList<LambdaFactoryInfo>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void registerLambdaFactory(String packagePrefix, LambdaListener.Factory factory) {
        List<LambdaFactoryInfo> list = this.lambdaFactoryInfos;
        synchronized (list) {
            this.lambdaFactoryInfos.add(new LambdaFactoryInfo(packagePrefix, factory));
        }
    }

    @Override
    public boolean isListening(Class<?> eventKlass) {
        List<IListener> listeners = this.listenerMap.get(eventKlass);
        return listeners != null && !listeners.isEmpty();
    }

    @Override
    public <T> T post(T event) {
        List<IListener> listeners = this.listenerMap.get(event.getClass());
        if (listeners != null) {
            for (IListener listener : listeners) {
                listener.call(event);
            }
        }
        return event;
    }

    @Override
    public <T extends ICancellable> T post(T event) {
        List<IListener> listeners = this.listenerMap.get(event.getClass());
        if (listeners != null) {
            event.setCancelled(false);
            for (IListener listener : listeners) {
                listener.call(event);
                if (!event.isCancelled()) continue;
                break;
            }
        }
        return event;
    }

    @Override
    public void subscribe(Object object) {
        this.subscribe(this.getListeners(object.getClass(), object), false);
    }

    @Override
    public void subscribe(Class<?> klass) {
        this.subscribe(this.getListeners(klass, null), true);
    }

    @Override
    public void subscribe(IListener listener) {
        this.subscribe(listener, false);
    }

    private void subscribe(List<IListener> listeners, boolean onlyStatic) {
        for (IListener listener : listeners) {
            this.subscribe(listener, onlyStatic);
        }
    }

    private void subscribe(IListener listener, boolean onlyStatic) {
        if (onlyStatic) {
            if (listener.isStatic()) {
                this.insert(this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
            }
        } else {
            this.insert(this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
        }
    }

    private void insert(List<IListener> listeners, IListener listener) {
        int i;
        for (i = 0; i < listeners.size() && listener.getPriority() <= listeners.get(i).getPriority(); ++i) {
        }
        listeners.add(i, listener);
    }

    @Override
    public void unsubscribe(Object object) {
        this.unsubscribe(this.getListeners(object.getClass(), object), false);
    }

    @Override
    public void unsubscribe(Class<?> klass) {
        this.unsubscribe(this.getListeners(klass, null), true);
    }

    @Override
    public void unsubscribe(IListener listener) {
        this.unsubscribe(listener, false);
    }

    private void unsubscribe(List<IListener> listeners, boolean staticOnly) {
        for (IListener listener : listeners) {
            this.unsubscribe(listener, staticOnly);
        }
    }

    private void unsubscribe(IListener listener, boolean staticOnly) {
        List<IListener> l = this.listenerMap.get(listener.getTarget());
        if (l != null) {
            if (staticOnly) {
                if (listener.isStatic()) {
                    l.remove(listener);
                }
            } else {
                l.remove(listener);
            }
        }
    }

    private List<IListener> getListeners(Class<?> klass, Object object) {
        Function<Object, List<IListener>> func = o -> {
            CopyOnWriteArrayList<IListener> listeners = new CopyOnWriteArrayList<IListener>();
            this.getListeners(listeners, klass, object);
            return listeners;
        };
        if (object == null) {
            return this.staticListenerCache.computeIfAbsent(klass, func);
        }
        for (Object key : this.listenerCache.keySet()) {
            if (key != object) continue;
            return this.listenerCache.get(object);
        }
        List<IListener> listeners = func.apply(object);
        this.listenerCache.put(object, listeners);
        return listeners;
    }

    private void getListeners(List<IListener> listeners, Class<?> klass, Object object) {
        for (Method method : klass.getDeclaredMethods()) {
            if (!this.isValid(method)) continue;
            listeners.add(new LambdaListener(this.getLambdaFactory(klass), klass, object, method));
        }
        if (klass.getSuperclass() != null) {
            this.getListeners(listeners, klass.getSuperclass(), object);
        }
    }

    private boolean isValid(Method method) {
        if (!method.isAnnotationPresent(EventHandler.class)) {
            return false;
        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }
        if (method.getParameterCount() != 1) {
            return false;
        }
        return !method.getParameters()[0].getType().isPrimitive();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private LambdaListener.Factory getLambdaFactory(Class<?> klass) {
        List<LambdaFactoryInfo> list = this.lambdaFactoryInfos;
        synchronized (list) {
            for (LambdaFactoryInfo info : this.lambdaFactoryInfos) {
                if (!klass.getName().startsWith(info.packagePrefix)) continue;
                return info.factory;
            }
        }
        throw new NoLambdaFactoryException(klass);
    }

    private static class LambdaFactoryInfo {
        public final String packagePrefix;
        public final LambdaListener.Factory factory;

        public LambdaFactoryInfo(String packagePrefix, LambdaListener.Factory factory) {
            this.packagePrefix = packagePrefix;
            this.factory = factory;
        }
    }
}
