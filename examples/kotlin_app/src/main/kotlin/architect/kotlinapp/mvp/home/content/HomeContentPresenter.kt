package architect.kotlinapp.mvp.home.content

import architect.kotlinapp.mvp.home.HomePresenter
import architect.robot.AutoScreen
import autodagger.AutoComponent
import mortar.ViewPresenter

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = AutoComponent(dependencies = arrayOf(HomePresenter::class)),
        pathView = HomeContentView::class
)
class HomeContentPresenter : ViewPresenter<HomeContentView>() {

}