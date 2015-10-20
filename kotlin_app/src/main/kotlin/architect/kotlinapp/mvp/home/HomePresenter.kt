package architect.kotlinapp.mvp.home

import architect.kotlinapp.MainActivity
import architect.kotlinapp.mvp.home.content.HomeContentPresenter
import architect.kotlinapp.mvp.home.drawer.HomeDrawerPresenter
import architect.robot.*
import autodagger.AutoComponent
import mortar.ViewPresenter
import org.parceler.Parcel

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = AutoComponent(dependencies = arrayOf(MainActivity::class)),
        pathView = HomeView::class,
        subScreens = arrayOf(
                ContainsSubscreen(type = HomeContentPresenter::class, name = "content"),
                ContainsSubscreen(type = HomeDrawerPresenter::class, name = "drawer")
        )
)
class HomePresenter(@field:NavigationParam val navParam1: String,
                    @field:NavigationParam val navParam2: String,
                    @field:NavigationResult val navResult: String?,
                    @field:ScreenData val state: HomePresenter.State) : ViewPresenter<HomeView>() {

//    @NavigationParam
    private lateinit var yo: String

//    @NavigationParam
    private var foo: String? = null

    @Parcel(parcelsIndex = false)
    class State {

    }
}