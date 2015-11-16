package architect.kotlinapp.mvp.home

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import architect.commons.view.PresentedFrameLayout
import architect.kotlinapp.mvp.home.screen.HomeScreenComponent
import architect.kotlinapp.util.homeContentView
import architect.kotlinapp.util.homeDrawerView
import architect.robot.dagger.DaggerService
import autodagger.AutoInjector
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.drawerLayout

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(HomePresenter::class)
class HomeView(context: Context) : PresentedFrameLayout<HomePresenter>(context) {

    init {
        DaggerService.get<HomeScreenComponent>(context).inject(this)
        setupView()
    }

    private fun setupView() {
        frameLayout {
            drawerLayout {
                fitsSystemWindows = true
                setScrimColor(Color.TRANSPARENT)

                homeContentView().lparams(matchParent, matchParent)
                homeDrawerView().lparams(context.dip(240), matchParent, Gravity.START)
            }.lparams(matchParent, matchParent)
        }
    }
}