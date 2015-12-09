package architect.examples.simple_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import architect.Architect;
import architect.Attachments;
import architect.Stack;
import architect.examples.simple_app.screen.home.HomeScreen;
import architect.examples.simple_app.screen.popup1.Popup1Screen;
import architect.examples.simple_app.transition.BottomSlideTransition;
import architect.examples.simple_app.transition.TopSlideTransition;
import architect.service.commons.FrameContainerView;
import architect.service.commons.Transitions;
import architect.service.navigation.NavigationService;
import architect.service.navigation.NavigationTransition;
import architect.service.navigation.commons.ActivityTransition;
import architect.service.navigation.commons.LateralTransition;
import architect.service.show.ShowTransition;
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

    @Bind(R.id.show_container_view)
    protected FrameContainerView showContainerView;

    @Bind(R.id.navigation_container_view)
    protected FrameContainerView navigationContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        architect = Architect.create(new Parceler());

        architect.register(Architecture.NAVIGATION_SERVICE, new NavigationService() {

            @Override
            public void registerTransitions(Transitions<NavigationTransition> transitions) {
                transitions.setDefault(new ActivityTransition());
                transitions.add(Architecture.NAVIGATION_SERVICE_LATERAL_TRANSITION, new LateralTransition());
            }
        });

        architect.register(Architecture.SHOW_SERVICE, new ShowService() {

            @Override
            public void registerTransitions(Transitions<ShowTransition> transitions) {
                transitions.setDefault(new BottomSlideTransition());
                transitions.add(Architecture.SHOW_SERVICE_TOP_TRANSITION, new TopSlideTransition());
            }
        });

        architect.delegate().onCreate(getIntent(), savedInstanceState,
                new Attachments()
                        .attach(Architecture.NAVIGATION_SERVICE, navigationContainerView)
                        .attach(Architecture.SHOW_SERVICE, showContainerView),
                new Stack()
                        .put(Architecture.NAVIGATION_SERVICE, new HomeScreen("Initial home"))
                        .put(Architecture.SHOW_SERVICE, new Popup1Screen("Initial popup")));
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