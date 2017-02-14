package animator.text.bullyboo.ru.textanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import animator.text.bullyboo.ru.textanimator.AlphaBuilder;
import animator.text.bullyboo.ru.textanimator.AnimationBuilder;
import animator.text.bullyboo.ru.textanimator.TextAnimator;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AnimationBuilder builder = AnimationBuilder.newBuilder()
//                .addPart(1000, 60)
//                .addPart(1000, 60, 5, AlphaBuilder.newInstance().fromAlpha(1).toAlpha(0.2f))
//                .addPart(1000, 5, AlphaBuilder.newInstance().fromAlpha(0.2f).toAlpha(1))
//                .addPart(1000, 5, 60)
//                .addPart(1000, 60, 5).build();

        textView = (TextView) findViewById(R.id.textView);

        final TextAnimator textAnimator = TextAnimator.newBuilder()
                .setTextView(textView)
                .from(0d)
                .to(1000d)
                .setMode(TextAnimator.DECELERATION_TO_ALPHA_MODE)
                .setRound(0)
                .setDuration(5000)
//                .setCustomAnimation(builder)
                .build();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textAnimator.start();
            }
        });
    }
}
