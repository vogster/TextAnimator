package animator.text.bullyboo.ru.textanimator;

import java.util.ArrayList;
import java.util.List;

import animator.text.bullyboo.ru.textanimator.exeptions.NullPartException;

/**
 * Created by BullyBoo
 */

public class AnimationBuilder {

    private List<Part> fpsList;

    public static Builder newBuilder(){
        return new AnimationBuilder().new Builder();
    }

    private AnimationBuilder(){
        fpsList = new ArrayList<>();
    }

    protected List<Part> getFpsList(){
        return this.fpsList;
    }

    public class Builder{

        public Builder addPart(long duration, int fps){
            fpsList.add(new Part(duration, fps, fps));
            return this;
        }

        public Builder addPart(long duration, int fps, AlphaBuilder animator){
            fpsList.add(new Part(duration, fps, animator));
            return this;
        }

        public Builder addPart(long duration, int fromFps, int toFps){
            fpsList.add(new Part(duration, fromFps, toFps));
            return this;
        }

        public Builder addPart(long duration, int fromFps, int toFps, AlphaBuilder animator){
            fpsList.add(new Part(duration, fromFps, toFps, animator));
            return this;
        }

        public AnimationBuilder build(){
            if(fpsList.size() > 0){
                return AnimationBuilder.this;
            } else {
                try {
                    throw new NullPartException(NullPartException.NULL_PART);
                } catch (NullPartException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}
