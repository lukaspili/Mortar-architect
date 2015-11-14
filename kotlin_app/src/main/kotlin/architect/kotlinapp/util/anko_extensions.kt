package architect.kotlinapp.util

import android.view.View
import android.view.ViewManager
import architect.NavigationView
import architect.kotlinapp.mvp.home.content.HomeContentView
import architect.kotlinapp.mvp.home.drawer.HomeDrawerView
import org.jetbrains.anko.custom.ankoView

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */

public inline fun ViewManager.navigatorView() = navigatorView {}

public inline fun ViewManager.navigatorView(init: NavigationView.() -> Unit): NavigationView {
    return ankoView({ NavigationView(it) }, init)
}


public inline fun ViewManager.homeDrawerView() = homeDrawerView {}

public inline fun ViewManager.homeDrawerView(init: HomeDrawerView.() -> Unit): HomeDrawerView {
    return ankoView({ HomeDrawerView(it) }, init)
}


public inline fun ViewManager.homeContentView() = homeContentView {}

public inline fun ViewManager.homeContentView(init: HomeContentView.() -> Unit): HomeContentView {
    return ankoView({ HomeContentView(it) }, init)
}

public inline fun View.dip(value: Int): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()
public inline fun View.dip(value: Float): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()
