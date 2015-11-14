package architect.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout
import architect.Architect
import architect.NavigationView
import architect.commons.ActivityArchitector
import architect.commons.Architected
import architect.commons.transition.StandardTransition
import architect.kotlinapp.mvp.home.screen.HomeScreen
import architect.kotlinapp.util.GlobalDependencies
import architect.kotlinapp.util.navigatorView
import architect.robot.dagger.DaggerScope
import architect.robot.dagger.DaggerService
import autodagger.AutoComponent
import autodagger.AutoInjector
import mortar.MortarScope
import mortar.bundler.BundleServiceRunner
import org.jetbrains.anko.UI

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(dependencies = arrayOf(App::class), superinterfaces = arrayOf(GlobalDependencies::class))
@AutoInjector
@DaggerScope(MainActivity::class)
public class MainActivity : AppCompatActivity() {

    private var scope: MortarScope? = null
    private var architect: Architect? = null
    private lateinit var containerView: NavigationView

    override fun getSystemService(name: String?): Any? {
        return if (scope?.hasService(name) ?: false) scope?.getService(name) else super.getSystemService(name);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope = ActivityArchitector.onCreateScope(this, savedInstanceState, object : Architected {
            override fun createNavigator(): Architect {
                val navigator = Architect(Parceler())
                navigator.transitions().setPushDefault(StandardTransition())
                return navigator
            }

            override fun configureScope(builder: MortarScope.Builder, parentScope: MortarScope) {
                DaggerService.configureScope(builder, MainActivity::class.java, DaggerMainActivityComponent.builder()
                        .appComponent(parentScope.getService<AppComponent>(DaggerService.SERVICE_NAME))
                        .build())
            }
        })

        setupView()

        DaggerService.get<MainActivityComponent>(this).inject(this)
        architect = ActivityArchitector.onCreateNavigator(this, scope, savedInstanceState, containerView, HomeScreen("test1", "test2"))
    }

    private fun setupView() {
        UI {
            containerView = navigatorView {
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        architect?.delegate()?.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        BundleServiceRunner.getBundleServiceRunner(scope).onSaveInstanceState(outState)
        architect?.delegate()?.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        architect?.delegate()?.onStart()
    }

    override fun onStop() {
        architect?.delegate()?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        architect?.delegate()?.onDestroy()
        architect = null

        if (isFinishing) {
            scope?.destroy()
            scope = null
        }

        super.onDestroy()
    }

    override fun onBackPressed() {
        if (architect?.delegate()?.onBackPressed() ?: false) {
            return
        }

        super.onBackPressed()
    }
}
