package architect.examples.simple_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import architect.Architect;
import architect.Attachments;
import architect.Stack;
import architect.examples.simple_app.screen.home.HomeScreen;
import architect.examples.simple_app.transition.BottomSlideTransition;
import architect.examples.simple_app.transition.TopSlideTransition;
import architect.service.commons.FrameContainerView;
import architect.service.commons.Transitions;
import architect.service.show.Transition;
import architect.service.show.ShowService;
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
    protected FrameContainerView containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        architect = Architect.create(new Parceler());
        architect.register(Architecture.SHOW_SERVICE, new ShowService() {

            @Override
            public void withTransitions(Transitions<Transition> transitions) {
                transitions.setDefault(new BottomSlideTransition());
                transitions.add(Architecture.SHOW_SERVICE_TOP_TRANSITION, new TopSlideTransition());
            }
        });

        architect.delegate().onCreate(getIntent(), savedInstanceState,
                new Attachments()
                        .attach(Architecture.SHOW_SERVICE, containerView),
                new Stack()
                        .put(Architecture.SHOW_SERVICE, new HomeScreen("Initial")));
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