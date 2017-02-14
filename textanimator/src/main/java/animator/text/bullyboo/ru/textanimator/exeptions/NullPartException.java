package animator.text.bullyboo.ru.textanimator.exeptions;

/**
 * Created by BullyBoo
 */

public class NullPartException extends Exception {

    public static final String NULL_PART = "Didn`t find the parts on animation, try to use addPart() method";

    public NullPartException() {
    }

    public NullPartException(String message) {
        super(message);
    }

    public NullPartException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullPartException(Throwable cause) {
        super(cause);
    }
}
