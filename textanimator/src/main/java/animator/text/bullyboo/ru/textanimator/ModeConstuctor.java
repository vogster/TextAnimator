package animator.text.bullyboo.ru.textanimator;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BullyBoo
 */

class ModeConstuctor {

    private float[] alphaDynamic;

    public float[] getAlphaDynamic(){
        return alphaDynamic;
    }

    protected int[] getModeFpsDynamic(int mode, long duration, Integer fps){
        return getModeFpsDynamic(mode, duration, fps, null);
    }

    protected int[] getModeFpsDynamic(int mode, long duration, Integer fromFps, Integer toFps){

        AlphaBuilder fromAlpha = AlphaBuilder.newInstance().fromAlpha(0f).toAlpha(1f);
        AlphaBuilder toAlpha = AlphaBuilder.newInstance().fromAlpha(1f).toAlpha(0f);

        switch (mode){
            case TextAnimator.LINEAR_MODE :
                return getFpsDynamicLinear(duration, fromFps);
            case TextAnimator.LINEAR_ACCELERATION_MODE :
                return getFpsDynamicLinearAcceleration(duration, fromFps);
            case TextAnimator.LINEAR_DECELERATION_MODE :
                return getFpsDynamicLinearDeceleration(duration, fromFps);

            case TextAnimator.DECELERATION_MODE :
                if (toFps != null) {
                    return getFpsDynamicDeceleration(duration, fromFps, toFps);
                } else {
                    return getFpsDynamicDeceleration(duration, fromFps);
                }
            case TextAnimator.DECELERATION_LINEAR_MODE :
                return getFpsDynamicDecelerationLinear(duration, fromFps);
            case TextAnimator.DECELERATION_ACCELERATION_MODE :
                return getFpsDynamicDecelerationAcceleration(duration, fromFps);

            case TextAnimator.ACCELERATION_MODE :
                if (toFps != null) {
                    return getFpsDynamicAcceleration(duration, fromFps, toFps);
                } else {
                    return getFpsDynamicAcceleration(duration, fromFps);
                }
            case TextAnimator.ACCELERATION_LINEAR_MODE :
                return getFpsDynamicAccelerationLinear(duration, fromFps);
            case TextAnimator.ACCELERATION_DECELERATION_MODE :
                return getFpsDynamicAccelerationDeceleration(duration, fromFps);

            case TextAnimator.LINEAR_TO_ALPHA_MODE :
                int[] linearToAlpha = getFpsDynamicLinear(duration, fromFps);
                alphaDynamic = toAlpha.createAlphaDynamic(linearToAlpha.length);
                return linearToAlpha;
            case TextAnimator.LINEAR_FROM_ALPHA_MODE :
                int[] linearFromAlpha = getFpsDynamicLinear(duration, fromFps);
                alphaDynamic = fromAlpha.createAlphaDynamic(linearFromAlpha.length);
                return linearFromAlpha;

            case TextAnimator.DECELERATION_TO_ALPHA_MODE :
                int[] decToAlpha = getFpsDynamicDeceleration(duration, fromFps);
                alphaDynamic = toAlpha.createAlphaDynamic(decToAlpha.length);
                return decToAlpha;
            case TextAnimator.DECELERATION_FROM_ALPHA_MODE :
                int[] decFromAlpha = getFpsDynamicDeceleration(duration, fromFps);
                alphaDynamic = fromAlpha.createAlphaDynamic(decFromAlpha.length);
                return decFromAlpha;

            case TextAnimator.ACCELERATION_TO_ALPHA_MODE :
                int[] accToAlpha = getFpsDynamicAcceleration(duration, fromFps);
                alphaDynamic = toAlpha.createAlphaDynamic(accToAlpha.length);
                return accToAlpha;
            case TextAnimator.ACCELERATION_FROM_ALPHA_MODE :
                int[] accFromAlpha = getFpsDynamicAcceleration(duration, fromFps);
                alphaDynamic = fromAlpha.createAlphaDynamic(accFromAlpha.length);
                return accFromAlpha;

            case TextAnimator.CUSTOM_MODE :
                return getCustomDynamic(duration, fromFps, toFps);

            default:
//                если такого кейса нет, возвращаем стандартную анимацию
                return getFpsDynamicLinear(duration, fromFps);

        }
    }


    protected int[] buildFpsDynamic(@NonNull List<Part> list){
        return getFpsDynamic(list);
    }

    protected float[] buildAlphaDynamic(@NonNull List<Part> list){
        return getAlphaDynamic(list);
    }

    /**
     * Анимация с неизменяемой скоростью
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicLinear(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fps, fps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Анимация замедления
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicDeceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fps, fps - fps*9/10)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    public int[] getFpsDynamicDeceleration(long duration, int fromFps, int toFps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fromFps, toFps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Анимация ускорения
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicAcceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fps - fps*9/10, fps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    public int[] getFpsDynamicAcceleration(long duration, int fromFps, int toFps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fromFps, toFps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * FPS не меняется в первой половине анимации
     * Ускорение во второй половне анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicLinearAcceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration/4, fps/4, fps/4)
                .addPart(duration*3/4, fps/4, fps + fps*19/20)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * FPS не меняется в первой половине анимации
     * Замедление во второй половне анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicLinearDeceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration/3, fps, fps)
                .addPart(duration*2/3, fps, fps - fps*9/10)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Ускорение в первой половне анимации
     * FPS не меняется во второй половине анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicAccelerationLinear(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration*2/3, fps - fps*9/10, fps)
                .addPart(duration/3, fps, fps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
}

    /**
     * Ускорение в первой половне анимации
     * Замедление во второй половине анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicAccelerationDeceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration/2, fps - fps*9/10, fps + fps*9/10)
                .addPart(duration/2, fps + fps*9/10, fps - fps*9/10)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Замедление в первой половне анимации
     * FPS не меняется во второй половине анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicDecelerationLinear(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration*2/3, fps + fps*9/10, fps/2)
                .addPart(duration/3, fps/2, fps/2)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Замедление в первой половне анимации
     * Ускорение во второй половине анимации
     * @param duration - длинна анимации
     * @param fps - FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getFpsDynamicDecelerationAcceleration(long duration, int fps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration/2, fps + fps*9/10, fps - fps*9/10)
                .addPart(duration/2, fps - fps*9/10, fps + fps*9/10)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    /**
     * Линейное изменение FPS с ручным указанием начального и конечного FPS
     * @param duration - длинна анимации
     * @param fromFps - начальный FPS
     * @param toFps - конечный FPS
     * @return - массив-динамика изменения FPS
     */
    public int[] getCustomDynamic(long duration, int fromFps, int toFps){
        List<Part> fpsList = AnimationBuilder.newBuilder()
                .addPart(duration, fromFps, toFps)
                .build().getFpsList();
        return getFpsDynamic(fpsList);
    }

    private int[] getFpsDynamic(List<Part> fpsList){
        List<int[]> arrays = new ArrayList<>();

        for(Part part : fpsList){
            int[] onePart = getFpsDynamicOfPart(part.getDuration(), part.getFromFps(), part.getToFps());
            part.setAmountFrames(onePart.length);
            arrays.add(onePart);
        }

        return convertFpsToTimePause(unitIntArrays(arrays));
    }

    private int[] getFpsDynamicOfPart(long duration, int fromFps, int toFps){
        int[] fpsDynamic = countTimePauses(duration, fromFps, toFps);
        fpsDynamic = convertTimePauseToFps(fpsDynamic);
        return fpsDynamic;
    }

    private float[] getAlphaDynamic(List<Part> fpsList){
        List<float[]> arrays = new ArrayList<>();

//        проверяем есть ли данные по изменению альфаканала
        boolean hasAlphaData = false;

        for(Part part : fpsList){
            if(part.getAlphaDynamic() != null){
                hasAlphaData = true;
                break;
            }
        }
        if(!hasAlphaData){
            return null;
        }

        for(Part part : fpsList){
            if(part.getAlphaDynamic() == null){
                AlphaBuilder alphaBuilder = AlphaBuilder.newInstance().fromAlpha(1).toAlpha(1);
                arrays.add(alphaBuilder.createAlphaDynamic(part.getAmountFrames()));
            } else {
                arrays.add(part.getAlphaDynamic().createAlphaDynamic(part.getAmountFrames()));
            }
        }

        return unitFloatArrays(arrays);
    }

    /**
     * Конверторы
     * @param fps
     * @return
     */
    public static int convertFpsToTimePause(int fps){
        return convert(fps);
    }

    public static int[] convertFpsToTimePause(int[] fpsArray){
        for (int i = 0; i < fpsArray.length; i++){
            fpsArray[i] = convertFpsToTimePause(fpsArray[i]);
        }
        return fpsArray;
    }

    private static int convertTimePauseToFps(int timePause){
        return convert(timePause);
    }

    private static int[] convertTimePauseToFps(int[] timePauses){
        for (int i = 0; i < timePauses.length; i++){
            timePauses[i] = convertTimePauseToFps(timePauses[i]);
        }
        return timePauses;
    }

    private static int convert(int value){
        return 1000/value;
    }

    /**
     * Метод, рассчитывающий паузы во времени для динамики изменения FPS
     * учитывая время анимации, а так же начальное и конечное значение FPS
     * @param duration - длинна анимации
     * @param fromFps - начальное значение FPS
     * @param toFps - конечно значение FPS
     * @return - массив промежутком между кадрами
     */
    public int[] countTimePauses(long duration, int fromFps, int toFps){
        int fromTimePause = convertFpsToTimePause(fromFps);
        int toTimePause = convertFpsToTimePause(toFps);

        float midleTimePause = (fromTimePause + toTimePause)/ 2;

        int amountFrames = (int)(duration/midleTimePause);
        Log.d("mode constructor", "amountFrames = " + amountFrames);
        float stepFps = toTimePause - fromTimePause;

        stepFps = stepFps/amountFrames;

        int[] timePause = new int[amountFrames];

        for(int i = 0; i < amountFrames; i++){
            timePause[i] = (int)(fromTimePause + (stepFps*i));
        }
        return timePause;
    }

    /**
     * Метод объединяющий массивы динамик FPS
     * @param list - список массивов
     * @return - объединенный массив
     */
    private int[] unitIntArrays(List<int[]> list){

        int[] unitArray;

        int arraylenght = 0;

        for(int[] array : list){
            arraylenght += array.length;
        }

        unitArray = new int[arraylenght];

        int index = 0;
        for(int[] array : list){
            for(int i = 0; i < array.length; i++){
                unitArray[index] = array[i];
                Log.d("mode", "fps[" + index + "] = " + unitArray[index]);
                index++;
            }
        }

        return unitArray;
    }

    private float[] unitFloatArrays(List<float[]> list){

        float[] unitArray;

        int arraylenght = 0;

        for(float[] array : list){
            arraylenght += array.length;
        }

        unitArray = new float[arraylenght];

        int index = 0;
        for(float[] array : list){
            for(int i = 0; i < array.length; i++){
                unitArray[index] = array[i];
                Log.d("mode", "alpha[" + index + "] = " + unitArray[index]);
                index++;
            }
        }

        return unitArray;
    }
}
