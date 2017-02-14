package animator.text.bullyboo.ru.textanimator;

/**
 * Created by BullyBoo
 */

public class AlphaBuilder {

    public static AlphaBuilder newInstance(){
        return new AlphaBuilder();
    }

    private AlphaBuilder(){

    }

    private float fromAlpha;

    private float toAlpha;

    /**
     * @param fromAlpha - значение от 0.0 до 1.0
     * @return
     */
    public AlphaBuilder fromAlpha(float fromAlpha) {
        this.fromAlpha = fromAlpha;
        return this;
    }

    /**
     * @param toAlpha - значение от 0.0 до 1.0
     * @return
     */
    public AlphaBuilder toAlpha(float toAlpha) {
        this.toAlpha = toAlpha;
        return this;
    }

    protected float[] createAlphaDynamic(int arraySize){

        float[] alphaDynamic = new float[arraySize];

        float step = (toAlpha - fromAlpha)/arraySize;
        for(int i = 0; i < arraySize; i++){
            alphaDynamic[i] = fromAlpha + step*i;
        }

        alphaDynamic[0] = fromAlpha;
        alphaDynamic[arraySize-1] = toAlpha;

        return alphaDynamic;
    }
}
