# Changelog

## Mortar Architect 0.17 *10/01/15*

 * History bug fix


## Mortar Architect 0.15 *07/17/15*

 * Add `HandlesViewTransition` to hook up on the view transition
 * Add examples of working with `Toolbar
 * Rename `TransitionDirection` to `ViewTransitionDirection`
 * Move all View transitions to project commons
 

## Mortar Architect 0.14 *07/17/15*

 * Do not make view transition for intermediate paths during navigation events
 * Do not require a `StackableParceler` when don't restore navigation stack option is enabled
 * Add `Navigator.backToRoot()`
 * Modify `Navigator.push()` to be able to take a varargs of `StackablePath`
 * Modify `Navigator.delegate().onCreate()` to be able to take a varargs of `StackablePath`
 * Modifiy `StackablePath` to take the parent ViewGroup as paramter. It allows to inflate a view from xml.
 * Update `@AutoStackable` by adding new member `pathWithLayout` to reflect the change above
 * Add `Navigator.set()`
 * Add transition direction to `NavigatorChain`
 * Modify `Navigator.show()` to be able to show several modals at the same time
 * Add `TransitionDirection` to specify the direction of the transition. It has only impact is on the transition animation.

## Mortar Architect 0.13 *07/10/15*

 * Refactoring of core with many breaking changes, but it's mostly simplification
 * Remove `StackScope` and `StackPath`
 * Introduce `Stackable` and `StackablePath` interfaces
 * Unique class for Navigator and view stacking
 * Unique annotation processor: Robot
 * Remove `ArchitectApp` and `ArchitectActivity`
 * Introduce `ArcitivtyArchitector`


## Mortar Architect 0.12 *07/08/15*

 * Improvements in `StackPagerAdapter`


## Mortar Architect 0.11 *07/08/15*

 * Improve `NavigationChain` with `Navigator.chain()`
 * Add `ReceivesResult` and `HasPresenter` interfaces, that let you pass back result to previous ViewPresenter in the history stack
 * All the commons views implements now `HasPresenter`


## Mortar Architect 0.1 

- Initial release