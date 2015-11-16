package architect.kotlinapp.mvp.home.drawer

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import architect.MortarFactory
import architect.ArchitectedScope
import architect.SubScreenService
import architect.commons.view.PresentedLinearLayout
import architect.kotlinapp.mvp.home.drawer.screen.HomeDrawerScreenComponent
import architect.robot.dagger.DaggerService
import autodagger.AutoInjector
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(HomeDrawerPresenter::class)
class HomeDrawerView(context: Context) : PresentedLinearLayout<HomeDrawerPresenter>(context) {

    init {
        val screenContext = MortarFactory.createContext(context, SubScreenService.get<Screen>(context, "drawer"))

        DaggerService.get<HomeDrawerScreenComponent>(screenContext).inject(this)
        setupView(screenContext)
    }

    private fun setupView(context: Context) {
        verticalLayout {
            lparams(matchParent, matchParent) {
                gravity = Gravity.CENTER
            }
            backgroundColor = resources.getColor(android.R.color.holo_orange_light)

            textView("YO 1") {
                backgroundColor = Color.RED
                textSize = 22f
            }
            textView("YO 2")
            textView("YO 3")
        }
    }
}