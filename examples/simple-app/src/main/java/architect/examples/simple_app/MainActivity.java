package architect.examples.simple_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import architect.Architect;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Root activity
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class MainActivity extends AppCompatActivity {

    private Architect architect;

    @Bind(R.id.container_view)
    protected ViewGroup containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        architect = new Architect(new Parceler());
//        architect.register("presentation", containerView, new Presentation() {
//            @Override
//            public void configurePresenter(PresentationPresenter presenter) {
//                presenter.transitions().setDefault(new BottomSlideTransition());
//                presenter.transitions().add("top", new TopSlideTransition());
//            }
//        });

        architect.delegate().onCreate(getIntent(), savedInstanceState);
//        architect.<PresentationController>getService("presentation").show(new HomeScreen("Initial"), Transitions.NO_TRANSITION);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        architect.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        architect.delegate().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        architect.delegate().onStart();
    }

    @Override
    protected void onStop() {
        architect.delegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        architect.delegate().onDestroy();
        architect = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (architect.delegate().onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    public Architect getArchitect() {
        return architect;
    }
}