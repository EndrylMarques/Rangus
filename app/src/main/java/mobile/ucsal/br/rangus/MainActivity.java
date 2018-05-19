package mobile.ucsal.br.rangus;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayout;
    LottieAnimationView animationView;
    Button btnLogar;
    Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();
        startAnimations();

        btnLogar.setOnClickListener(this);
        btnCadastrar.setOnClickListener(this);

    }

    private void startAnimations() {

        animationView.setAnimation(R.raw.noodles);
        animationView.setSpeed(0.5f);
        animationView.playAnimation();

        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(800);

        animationView.addAnimatorListener(new Animator.AnimatorListener() {

            int cont = 0;

            @Override
            public void onAnimationStart(Animator animation) {

                //Fade in do layout

                linearLayout.startAnimation(fadeIn);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                //Fade out do layout
                linearLayout.startAnimation(fadeOut);

                if(cont == 0){

                    animationView.setAnimation(R.raw.location);
                    animationView.setSpeed(0.5f);
                    animationView.playAnimation();

                    cont++;

                }else if (cont == 1){

                    animationView.setAnimation(R.raw.stopwatch);
                    animationView.setSpeed(0.8f);
                    animationView.playAnimation();

                    cont++;

                }else{

                    animationView.setAnimation(R.raw.noodles);
                    animationView.setSpeed(0.5f);
                    animationView.playAnimation();

                    cont = 0;

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    private void findViewsById() {

        animationView = findViewById(R.id.main_animation_view);
        btnLogar = findViewById(R.id.main_btnLogar);
        btnCadastrar = findViewById(R.id.main_btnCadastrar);
        linearLayout = findViewById(R.id.main_layout_animation);

    }

    @Override
    public void onClick(View v) {

        if(v == btnCadastrar){

            //Startar Cadastro

        }

        if(v == btnLogar){

            Intent intent = new Intent(this, LoginActivity.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, btnLogar,"logar");

                startActivity(intent,options.toBundle());

            }else{
                startActivity(intent);
            }


        }

    }
}
