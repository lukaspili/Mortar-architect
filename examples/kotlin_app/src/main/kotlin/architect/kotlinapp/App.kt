package architect.kotlinapp

import android.app.Application
import architect.kotlinapp.util.GlobalDependencies
import architect.robot.dagger.DaggerScope
import architect.robot.dagger.DaggerService
import autodagger.AutoComponent
import mortar.MortarScope

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(superinterfaces = arrayOf(GlobalDependencies::class))
@DaggerScope(App::class)
class App : Application() {

    private var scope: MortarScope? = null

    override fun getSystemService(name: String?): Any? {
        return if (scope?.hasService(name) ?: false) scope?.getService(name) else super.getSystemService(name);
    }

    override fun onCreate() {
        super.onCreate()

        scope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, DaggerAppComponent.create())
                .build("Root")
    }
}