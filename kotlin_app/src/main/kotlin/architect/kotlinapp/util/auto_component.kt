package architect.kotlinapp.util

import architect.kotlinapp.MainActivity
import autodagger.AutoComponent

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(dependencies = arrayOf(MainActivity::class), superinterfaces = arrayOf(GlobalDependencies::class))
annotation class ActivityChildComponent

interface GlobalDependencies {

}