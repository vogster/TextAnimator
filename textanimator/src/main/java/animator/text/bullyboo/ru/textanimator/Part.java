package animator.text.bullyboo.ru.textanimator;

import android.support.annotation.NonNull;

/**
 * Created by BullyBoo
 */

class Part{

    private int fromFps;
    private int toFps;
    private long duration;

    private int amountFrames;

    private AlphaBuilder alphaAnimator;

    public Part(long duration, int fps) {
        this.duration = duration;
        this.fromFps = fps;
        this.toFps = fps;
    }

    public Part(long duration, int fps, @NonNull AlphaBuilder alphaAnimator) {
        this.duration = duration;
        this.fromFps = fps;
        this.toFps = fps;
        this.alphaAnimator = alphaAnimator;
    }

    public Part(long duration, int fromFps, int toFps) {
        this.duration = duration;
        this.fromFps = fromFps;
        this.toFps = toFps;
    }

    public Part(long duration, int fromFps, int toFps, @NonNull AlphaBuilder alphaAnimator) {
        this.duration = duration;
        this.fromFps = fromFps;
        this.toFps = toFps;
        this.alphaAnimator = alphaAnimator;
    }

    public int getFromFps() {
        return fromFps;
    }

    public int getToFps() {
        return toFps;
    }

    public long getDuration() {
        return duration;
    }

    protected void setAmountFrames(int amountFrames){
        this.amountFrames = amountFrames;
    }

    protected int getAmountFrames(){
        return amountFrames;
    }

    public AlphaBuilder getAlphaDynamic(){
        return alphaAnimator;
    }
}