package animator.text.bullyboo.ru.textanimator;

import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.text.DecimalFormat;

import animator.text.bullyboo.ru.textanimator.exeptions.BuilderDataException;

/**
 * Created by BullyBoo
 */
public class TextAnimator{

    /**
     * Моды ускорения
     * LINEAR_MODE - линейная анимация
     * DECELERATION_MODE - замедление
     * DECELERATION_TO_ALPHA - замедление с исчезновением
     * DECELERATION_FROM_ALPHA - замедление с появлением
     * ACCELERATION_MODE - ускорение
     * ACCELERATION_TO_ALPHA - ускорение с исчезновением
     * ACCELERATION_FROM_ALPHA - ускорение с появлением
     */
    public static final int LINEAR_MODE = 1;
    public static final int LINEAR_ACCELERATION_MODE = 2;
    public static final int LINEAR_DECELERATION_MODE = 3;

    public static final int DECELERATION_MODE = 4;
    public static final int DECELERATION_LINEAR_MODE = 5;
    public static final int DECELERATION_ACCELERATION_MODE = 6;

    public static final int ACCELERATION_MODE = 7;
    public static final int ACCELERATION_LINEAR_MODE = 8;
    public static final int ACCELERATION_DECELERATION_MODE = 9;


    public static final int LINEAR_TO_ALPHA_MODE = 10;
    public static final int LINEAR_FROM_ALPHA_MODE = 11;

    public static final int DECELERATION_TO_ALPHA_MODE  = 12;
    public static final int DECELERATION_FROM_ALPHA_MODE  = 13;

    public static final int ACCELERATION_TO_ALPHA_MODE  = 14;
    public static final int ACCELERATION_FROM_ALPHA_MODE  = 15;

    public static final int CUSTOM_MODE  = 16;

    /**
     * Поддерживаемые типы данных, изменяемые для реализации анимации
     */
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INT = 3;
    public static final int FLOAT = 4;
    public static final int LONG = 5;
    public static final int DOUBLE = 6;

    /**
     * TextView в котором будут отображаться изменения
     */
    private TextView textView;

    /**
     * from - от какого числа начинается изменение
     * to - к какому числу происходит изменение
     */
    private Double from, to;

    /**
     * Префикс и постфикс
     * Используются для добавления начала и конца и строки в анимируемом TextView
     */
    private String prefix = "", postfix = "";

    /**
     * Автоопределение типа, реализуемое при вводе значений from и to (низкий приоритет)
     * Пользовательский выбор типа, указание типа напрямую через метод setType (высокий приоритет)
     *
     * Итоговый тип, используемый для работы класса
     */
    private int autoType;
    private int userType;
    private int type;

    /**
     * Выбранный мод воспроизведения анимации
     */
    private int mode = 1;

    /**
     * Длинна анимации
     */
    private long duration;

    /**
     * Шаг. На сколько увеличивается/уменьшается числовое значение за один фрейм
     */
    private Double step;

    /**
     * Количество кадров в секунду
     * По умолчанию 60 кадров в секунду
     *
     * Динамика изменения FPS (для анимации)
     */
    private int fps = 60;
    private Integer fromFps, toFps;

    private int[] fpsDynamic;
    private int[] userFpsDynamic;

    /**
     * Динамика изменения альфаканала у TextView
     */
    private float alpha = 1;
    private Float fromAlpha, toAlpha;

    private float[] alphaDynamic;
    private float[] userAlphaDynamic;

    /**
     * Список кастомных анимаций, задаваемых программно через ModeBuilder
     */
    private AnimationBuilder partList;

    /**
     * Текущее число
     * Число к которому стремится
     */
    private Double currentNumber;
    private double targetNumber;


    /**
     * Объект форматера, меняющий формат числа
     */
    private DecimalFormat decimalFormat;

    /**
     * Флаг анимации.
     * true - анимация проигрывается
     * false - анимация не проигрывается
     */
    private boolean isAnimate = false;

    private TextAnimator(){

    }

    public static Builder newBuilder() {
        return new TextAnimator().new Builder();
    }

    public void start(){
//        заглушка, чтобы нельзя было запустить анимацию повторно, пока она не закончится
        if(!isAnimate){
            isAnimate = true;
            frame = 0;
            init();
            timer();
        }
    }

    private void init(){
        long amountFrames = fpsDynamic.length + 1;

        currentNumber = from;
        targetNumber = to;
        step = to - from;
        step = step / amountFrames;
    }

    /**
     * Счетчик кадров
     */
    private int frame;

    private void timer(){
        new Handler().postDelayed(changeText, fpsDynamic[frame]);
    }

    private Runnable changeText = new Runnable() {
        @Override
        public void run() {
            currentNumber += step;

//            настраиваем прозрачность
            if(alphaDynamic != null){
                textView.setAlpha(alphaDynamic[frame]);
            }
//            прибавляем кадр
            if(frame+1 < fpsDynamic.length) {
                frame++;
            }

//            округляем число
            String roundedDouble = decimalFormat.format(currentNumber);
            roundedDouble = roundedDouble.replace(",", ".");

            changeText(roundedDouble);

//            если увеличиываем число
            if(step > 0){
                if(currentNumber < targetNumber){
                    timer();
                } else {
                    lastTextUpdate();
                    isAnimate = false;
                }

//                если уменьшаем число
            } else if (step < 0){
                if(currentNumber > targetNumber){
                    timer();
                } else {
                    lastTextUpdate();
                    isAnimate = false;
                }
            }
        }
    };

    private void changeText(String current){
        switch (type){
            case BYTE:

                textView.setText(prefix + currentNumber.byteValue() + postfix);
                break;
            case SHORT:
                textView.setText(prefix + currentNumber.shortValue() + postfix);
                break;
            case INT:
                textView.setText(prefix + currentNumber.intValue() + postfix);
                break;
            case FLOAT:
                textView.setText(prefix + current + postfix);
                break;
            case LONG:
                textView.setText(prefix + currentNumber.longValue() + postfix);
                break;
            case DOUBLE:
                textView.setText(prefix + current + postfix);
                break;
            default:
                return;
        }
    }

    private void lastTextUpdate(){
        switch (type){
            case BYTE:
                textView.setText(prefix + to.byteValue() + postfix);
                break;
            case SHORT:
                textView.setText(prefix + to.shortValue() + postfix);
                break;
            case INT:
                textView.setText(prefix + to.intValue() + postfix);
                break;
            case FLOAT:
                textView.setText(prefix + decimalFormat.format(to.floatValue()).replace(",", ".") + postfix);
                break;
            case LONG:
                textView.setText(prefix + to.longValue() + postfix);
                break;
            case DOUBLE:
                textView.setText(prefix + decimalFormat.format(to).replace(",", ".") + postfix);
                break;
            default:
                return;
        }
    }

    public class Builder{

        private Builder(){

        }

        public Builder setTextView(@NonNull TextView textView){
            TextAnimator.this.textView = textView;
            return this;
        }

        public Builder from(byte fromByte){
            from = Double.valueOf(fromByte);
            autoType = BYTE;
            return this;
        }

        public Builder to(byte toByte){
            to = Double.valueOf(toByte);
            autoType = BYTE;
            return this;
        }

        public Builder from(short fromShort){
            from = Double.valueOf(fromShort);
            autoType = SHORT;
            return this;
        }

        public Builder to(short toShort){
            to = Double.valueOf(toShort);
            autoType = SHORT;
            return this;
        }

        public Builder from(int fromInt){
            from = Double.valueOf(fromInt);
            autoType = INT;
            return this;
        }

        public Builder to(int toInt){
            to = Double.valueOf(toInt);
            autoType = INT;
            return this;
        }

        public Builder from(float fromFloat){
            from = Double.valueOf(fromFloat);
            autoType = FLOAT;
            return this;
        }

        public Builder to(float toFloat){
            to = Double.valueOf(toFloat);
            autoType = FLOAT;
            return this;
        }

        public Builder from(long fromLong){
            from = Double.valueOf(fromLong);
            autoType = LONG;
            return this;
        }

        public Builder to(long toLong){
            to = Double.valueOf(toLong);
            autoType = LONG;
            return this;
        }

        public Builder from(double fromDouble){
            from = fromDouble;
            autoType = DOUBLE;
            return this;
        }

        public Builder to(double toDouble){
            to = toDouble;
            autoType = DOUBLE;
            return this;
        }

        public Builder from(Byte fromByte){
            from = Double.valueOf(fromByte);
            autoType = BYTE;
            return this;
        }

        public Builder to(Byte toByte){
            to = Double.valueOf(toByte);
            autoType = BYTE;
            return this;
        }

        public Builder from(Short fromShort){
            from = Double.valueOf(fromShort);
            autoType = SHORT;
            return this;
        }

        public Builder to(Short toShort){
            to = Double.valueOf(toShort);
            autoType = SHORT;
            return this;
        }

        public Builder from(Integer fromInt){
            from = Double.valueOf(fromInt);
            autoType = INT;
            return this;
        }

        public Builder to(Integer toInt){
            to = Double.valueOf(toInt);
            autoType = INT;
            return this;
        }

        public Builder from(Float fromFloat){
            from = Double.valueOf(fromFloat);
            autoType = FLOAT;
            return this;
        }

        public Builder to(Float toFloat){
            to = Double.valueOf(toFloat);
            autoType = FLOAT;
            return this;
        }

        public Builder from(Long fromLong){
            from = Double.valueOf(fromLong);
            autoType = LONG;
            return this;
        }

        public Builder to(Long toLong){
            to = Double.valueOf(toLong);
            autoType = LONG;
            return this;
        }

        public Builder from(Double fromDouble){
            from = fromDouble;
            autoType = DOUBLE;
            return this;
        }

        public Builder to(Double toDouble){
            to = toDouble;
            autoType = DOUBLE;
            return this;
        }

        public Builder from(String fromDouble){
            from = parseStringToDigitFrom(fromDouble);
            autoType = DOUBLE;
            return this;
        }

        public Builder to(String toDouble){
            to = parseStringToDigitTo(toDouble);
            autoType = DOUBLE;
            return this;
        }

        public Builder setPrefix(String prefix){
            TextAnimator.this.prefix = prefix;
            return this;
        }

        public Builder setType(@Type int type){
            userType = type;
            return this;
        }

        public Builder setPostfix(String postfix){
            TextAnimator.this.postfix = postfix;
            return this;
        }

        public Builder setDuration(long millis){
            duration = millis;
            return this;
        }

        public Builder setFPS(int fps){
            TextAnimator.this.fps = fps;
            return this;
        }

        public Builder setMode(@Mode int mode){
            TextAnimator.this.mode = mode;
            return this;
        }

        public Builder setFpsDynamic(int[] dynamic){
            userFpsDynamic = dynamic;
            return this;
        }

        public Builder setRound(int round){
            decimalFormat = new DecimalFormat(getRoundPattern(round));
            return this;
        }

        public Builder setFromFps(int fromFps){
            TextAnimator.this.fromFps = fromFps;
            return this;
        }

        public Builder setToFps(int toFps){
            TextAnimator.this.toFps = toFps;
            return this;
        }

        public Builder setFromAlpha(float fromAlpha){
            TextAnimator.this.fromAlpha = fromAlpha;
            return this;
        }

        public Builder setToAlpha(float toAlpha){
            TextAnimator.this.toAlpha = toAlpha;
            return this;
        }

        public Builder setAlphaDynamic(float[] alphaDynamic){
            TextAnimator.this.userAlphaDynamic = userAlphaDynamic;
            return this;
        }

        public Builder setCustomAnimation(@NonNull AnimationBuilder partList){
            TextAnimator.this.partList = partList;
            return this;
        }

        public TextAnimator build(){

            ModeConstuctor constuctor = buildFpsDynamic(new ModeConstuctor());
            buildAlphaDynamic(constuctor);

            buildType();

            if(decimalFormat == null){
                decimalFormat = new DecimalFormat(getRoundPattern(0));
            }

            try {
                checkData();
            } catch (BuilderDataException e) {
                e.printStackTrace();
                throw new NullPointerException(BuilderDataException.DIDNT_SET_DATA);
            }

            return TextAnimator.this;
        }

        private double parseStringToDigitFrom(String string){

            try {
                return Double.valueOf(string);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private double parseStringToDigitTo(String string){

            try {
                return Double.valueOf(string);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private String getRoundPattern(int round){

            StringBuilder roundPattern = new StringBuilder();
            roundPattern.append("#0");

            if (round < 0) {
                return null;
            } else if (round > 0){
                roundPattern.append(".");
                for (int i = 0; i < round; i++){
                    roundPattern.append("0");
                }
            }
            return roundPattern.toString();
        }


        private ModeConstuctor buildFpsDynamic(ModeConstuctor constuctor){

            if(partList != null){
                fpsDynamic = constuctor.buildFpsDynamic(partList.getFpsList());
                return constuctor;
            }

            if(userFpsDynamic != null){
                fpsDynamic = constuctor.convertFpsToTimePause(userFpsDynamic);
                return constuctor;
            }

            if (fromFps != null && toFps != null){
                fpsDynamic = constuctor.getModeFpsDynamic(mode, duration, fromFps, toFps);
                return constuctor;
            }
            if(fromFps == null && toFps == null){
                fpsDynamic = constuctor.getModeFpsDynamic(mode, duration, fps);
                return constuctor;
            }

            try {
                if(fromFps == null && toFps != null){
                    throw new BuilderDataException(BuilderDataException.FROM_FPS_EQUELS_NULL);
                } else if(fromFps != null && toFps == null){
                    throw new BuilderDataException(BuilderDataException.TO_FPS_EQUELS_NULL);
                } else {
                    throw new BuilderDataException(BuilderDataException.DIDNT_SET_FPS_DATA);
                }
            } catch (BuilderDataException e) {
                e.printStackTrace();
                return constuctor;
            }
        }

        private void buildAlphaDynamic(ModeConstuctor constuctor){
            if(partList != null){
                alphaDynamic = constuctor.buildAlphaDynamic(partList.getFpsList());

                if(alphaDynamic != null){
                    return;
                }
            }

            if(userAlphaDynamic != null){
                alphaDynamic = userAlphaDynamic;
                return;
            }

            if (fromAlpha != null && toAlpha != null){
                AlphaBuilder alphaBuilder = AlphaBuilder.newInstance().fromAlpha(fromAlpha).toAlpha(toAlpha);
                alphaDynamic = alphaBuilder.createAlphaDynamic(fpsDynamic.length);
                return;
            }

            if(mode >=10 && mode <= 15){
                alphaDynamic = constuctor.getAlphaDynamic();
                return;
            }

            if (fromAlpha == null && toAlpha == null){
                AlphaBuilder alphaBuilder = AlphaBuilder.newInstance().fromAlpha(alpha).toAlpha(alpha);
                alphaDynamic = alphaBuilder.createAlphaDynamic(fpsDynamic.length);
                return;
            }

            try {
                if(fromAlpha == null && toAlpha != null){
                    throw new BuilderDataException(BuilderDataException.FROM_ALPHA_EQUELS_NULL);
                } else if(fromAlpha != null && toAlpha == null){
                    throw new BuilderDataException(BuilderDataException.TO_ALPHA_EQUELS_NULL);
                } else {
                    throw new BuilderDataException(BuilderDataException.DIDNT_SET_ALPHA_DATA);
                }
            } catch (BuilderDataException e) {
                e.printStackTrace();
                return;
            }
        }

        private void buildType(){
            if (userType == 0){
                type = autoType;
            } else {
                type = userType;
            }
        }

        private void checkData()throws BuilderDataException{
            if(duration == 0){
                throw new BuilderDataException(BuilderDataException.DIDNT_SET_DURATION);
            }
            if(textView == null){
                throw new BuilderDataException(BuilderDataException.DIDNT_SET_TEXT_VIEW);
            }
            if(from == null && to == null){
                throw new BuilderDataException(BuilderDataException.DIDNT_SET_FROM_TO);
            }
        }
    }

    @IntDef({BYTE, SHORT, INT, FLOAT, LONG, DOUBLE})
    public @interface Type {}

    @IntDef({LINEAR_MODE,
            LINEAR_ACCELERATION_MODE,
            LINEAR_DECELERATION_MODE,
            DECELERATION_MODE,
            DECELERATION_LINEAR_MODE,
            DECELERATION_ACCELERATION_MODE,
            ACCELERATION_MODE,
            ACCELERATION_LINEAR_MODE,
            ACCELERATION_DECELERATION_MODE,
            LINEAR_TO_ALPHA_MODE,
            LINEAR_FROM_ALPHA_MODE,
            DECELERATION_TO_ALPHA_MODE,
            DECELERATION_FROM_ALPHA_MODE,
            ACCELERATION_TO_ALPHA_MODE,
            ACCELERATION_FROM_ALPHA_MODE,
            CUSTOM_MODE})
    public @interface Mode {}
}
