package architect.kotlinapp.mvp.home.content

import android.content.Context
import architect.MortarFactory
import architect.ArchitectedScope
import architect.SubScreenService
import architect.commons.view.PresentedFrameLayout
import architect.kotlinapp.mvp.home.content.screen.HomeContentScreenComponent
import architect.robot.dagger.DaggerService
import autodagger.AutoInjector
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(HomeContentPresenter::class)
class HomeContentView(context: Context) : PresentedFrameLayout<HomeContentPresenter>(context) {

    init {
        val screenContext = MortarFactory.createContext(context, SubScreenService.get<Screen>(context, "content"))

        DaggerService.get<HomeContentScreenComponent>(screenContext).inject(this)
        setupView(screenContext)
    }

    private fun setupView(context: Context) {
        frameLayout {
            backgroundColor = resources.getColor(android.R.color.holo_blue_bright)
        }
    }
}