package net.fabricmc.fabric.utils.render;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AnimationEngine {
    private final long duration;
    private long startTime;
    private boolean reversed;
    private String easingType;
    private static final Map<String, Function<Float, Float>> easings = new HashMap<String, Function<Float, Float>>();

    private static float bounceOut(float t) {
        if (t < 0.36363637f) {
            return 7.5625f * t * t;
        }
        if (t < 0.72727275f) {
            return 7.5625f * (t -= 0.54545456f) * t + 0.75f;
        }
        if (t < 0.90909094f) {
            return 7.5625f * (t -= 0.8181818f) * t + 0.9375f;
        }
        return 7.5625f * (t -= 0.95454544f) * t + 0.984375f;
    }

    public AnimationEngine(long time, String easingType) {
        this.duration = time;
        this.easingType = easingType;
        this.startTime = System.currentTimeMillis();
        this.reversed = false;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    public float getProgress() {
        float t = Math.min(1.0f, (float)(System.currentTimeMillis() - this.startTime) / (float)this.duration);
        t = this.reversed ? 1.0f - t : t;
        return this.ease(t);
    }

    private float ease(float t) {
        Function<Float, Float> easing = easings.getOrDefault(this.easingType, easings.get("linear"));
        return easing.apply(Float.valueOf(t)).floatValue();
    }

    public boolean isDone() {
        return System.currentTimeMillis() - this.startTime >= this.duration;
    }

    static {
        easings.put("linear", t -> t);
        easings.put("easeInSine", t -> Float.valueOf(1.0f - (float)Math.cos((double)t.floatValue() * Math.PI / 2.0)));
        easings.put("easeOutSine", t -> Float.valueOf((float)Math.sin((double)t.floatValue() * Math.PI / 2.0)));
        easings.put("easeInOutSine", t -> Float.valueOf(-((float)Math.cos(Math.PI * (double)t.floatValue())) / 2.0f + 0.5f));
        easings.put("easeInQuad", t -> Float.valueOf(t.floatValue() * t.floatValue()));
        easings.put("easeOutQuad", t -> Float.valueOf(1.0f - (1.0f - t.floatValue()) * (1.0f - t.floatValue())));
        easings.put("easeInOutQuad", t -> Float.valueOf((double)t.floatValue() < 0.5 ? 2.0f * t.floatValue() * t.floatValue() : 1.0f - (float)Math.pow(-2.0f * t.floatValue() + 2.0f, 2.0) / 2.0f));
        easings.put("easeInCubic", t -> Float.valueOf(t.floatValue() * t.floatValue() * t.floatValue()));
        easings.put("easeOutCubic", t -> Float.valueOf(1.0f - (float)Math.pow(1.0f - t.floatValue(), 3.0)));
        easings.put("easeInOutCubic", t -> Float.valueOf((double)t.floatValue() < 0.5 ? 4.0f * t.floatValue() * t.floatValue() * t.floatValue() : 1.0f - (float)Math.pow(-2.0f * t.floatValue() + 2.0f, 3.0) / 2.0f));
        easings.put("easeInQuart", t -> Float.valueOf(t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue()));
        easings.put("easeOutQuart", t -> Float.valueOf(1.0f - (float)Math.pow(1.0f - t.floatValue(), 4.0)));
        easings.put("easeInOutQuart", t -> Float.valueOf((double)t.floatValue() < 0.5 ? 8.0f * t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue() : 1.0f - (float)Math.pow(-2.0f * t.floatValue() + 2.0f, 4.0) / 2.0f));
        easings.put("easeInQuint", t -> Float.valueOf(t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue()));
        easings.put("easeOutQuint", t -> Float.valueOf(1.0f - (float)Math.pow(1.0f - t.floatValue(), 5.0)));
        easings.put("easeInOutQuint", t -> Float.valueOf((double)t.floatValue() < 0.5 ? 16.0f * t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue() * t.floatValue() : 1.0f - (float)Math.pow(-2.0f * t.floatValue() + 2.0f, 5.0) / 2.0f));
        easings.put("easeInExpo", t -> Float.valueOf(t.floatValue() == 0.0f ? 0.0f : (float)Math.pow(2.0, 10.0f * t.floatValue() - 10.0f)));
        easings.put("easeOutExpo", t -> Float.valueOf(t.floatValue() == 1.0f ? 1.0f : 1.0f - (float)Math.pow(2.0, -10.0f * t.floatValue())));
        easings.put("easeInOutExpo", t -> {
            if (t.floatValue() == 0.0f) {
                return Float.valueOf(0.0f);
            }
            if (t.floatValue() == 1.0f) {
                return Float.valueOf(1.0f);
            }
            return Float.valueOf((double)t.floatValue() < 0.5 ? (float)Math.pow(2.0, 20.0f * t.floatValue() - 10.0f) / 2.0f : (2.0f - (float)Math.pow(2.0, -20.0f * t.floatValue() + 10.0f)) / 2.0f);
        });
        easings.put("easeInCirc", t -> Float.valueOf(1.0f - (float)Math.sqrt(1.0f - t.floatValue() * t.floatValue())));
        easings.put("easeOutCirc", t -> Float.valueOf((float)Math.sqrt(1.0f - (t.floatValue() - 1.0f) * (t.floatValue() - 1.0f))));
        easings.put("easeInOutCirc", t -> Float.valueOf((double)t.floatValue() < 0.5 ? (1.0f - (float)Math.sqrt(1.0f - 4.0f * t.floatValue() * t.floatValue())) / 2.0f : ((float)Math.sqrt(1.0f - (2.0f * t.floatValue() - 2.0f) * (2.0f * t.floatValue() - 2.0f)) + 1.0f) / 2.0f));
        easings.put("easeInBack", t -> {
            float c1 = 1.70158f;
            float c3 = c1 + 1.0f;
            return Float.valueOf(c3 * t.floatValue() * t.floatValue() * t.floatValue() - c1 * t.floatValue() * t.floatValue());
        });
        easings.put("easeOutBack", t -> {
            float c1 = 1.70158f;
            float c3 = c1 + 1.0f;
            return Float.valueOf(1.0f + c3 * (float)Math.pow(t.floatValue() - 1.0f, 3.0) + c1 * (float)Math.pow(t.floatValue() - 1.0f, 2.0));
        });
        easings.put("easeInOutBack", t -> {
            float c1 = 1.70158f;
            float c2 = c1 * 1.525f;
            return Float.valueOf((double)t.floatValue() < 0.5 ? (float)(Math.pow(2.0f * t.floatValue(), 2.0) * (double)((c2 + 1.0f) * 2.0f * t.floatValue() - c2)) / 2.0f : (float)(Math.pow(2.0f * t.floatValue() - 2.0f, 2.0) * (double)((c2 + 1.0f) * (t.floatValue() * 2.0f - 2.0f) + c2) + 2.0) / 2.0f);
        });
        easings.put("easeOutBounce", AnimationEngine::bounceOut);
        easings.put("easeInBounce", t -> Float.valueOf(1.0f - AnimationEngine.bounceOut(1.0f - t.floatValue())));
        easings.put("easeInOutBounce", t -> {
            if (t.floatValue() < 0.5f) {
                return Float.valueOf((1.0f - AnimationEngine.bounceOut(1.0f - 2.0f * t.floatValue())) / 2.0f);
            }
            return Float.valueOf((1.0f + AnimationEngine.bounceOut(2.0f * t.floatValue() - 1.0f)) / 2.0f);
        });
        easings.put("easeInElastic", t -> {
            if (t.floatValue() == 0.0f || t.floatValue() == 1.0f) {
                return t;
            }
            float c4 = 2.0943952f;
            return Float.valueOf(-((float)Math.pow(2.0, 10.0f * t.floatValue() - 10.0f)) * (float)Math.sin((t.floatValue() * 10.0f - 10.75f) * c4));
        });
        easings.put("easeOutElastic", t -> {
            if (t.floatValue() == 0.0f || t.floatValue() == 1.0f) {
                return t;
            }
            float c4 = 2.0943952f;
            return Float.valueOf((float)Math.pow(2.0, -10.0f * t.floatValue()) * (float)Math.sin((t.floatValue() * 10.0f - 0.75f) * c4) + 1.0f);
        });
        easings.put("easeInOutElastic", t -> {
            if (t.floatValue() == 0.0f || t.floatValue() == 1.0f) {
                return t;
            }
            float c5 = 1.3962635f;
            if (t.floatValue() < 0.5f) {
                return Float.valueOf(-((float)Math.pow(2.0, 20.0f * t.floatValue() - 10.0f)) * (float)Math.sin((20.0f * t.floatValue() - 11.125f) * c5) / 2.0f);
            }
            return Float.valueOf((float)Math.pow(2.0, -20.0f * t.floatValue() + 10.0f) * (float)Math.sin((20.0f * t.floatValue() - 11.125f) * c5) / 2.0f + 1.0f);
        });
    }
}
