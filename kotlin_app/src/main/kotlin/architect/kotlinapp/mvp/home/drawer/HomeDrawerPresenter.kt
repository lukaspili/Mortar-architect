package architect.kotlinapp.mvp.home.drawer

import architect.kotlinapp.mvp.home.HomePresenter
import architect.robot.AutoScreen
import autodagger.AutoComponent
import mortar.ViewPresenter

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = AutoComponent(dependencies = arrayOf(HomePresenter::class)),
        pathView = HomeDrawerView::class
)
class HomeDrawerPresenter : ViewPresenter<HomeDrawerView>() {


}