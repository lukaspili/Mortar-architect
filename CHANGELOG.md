# Changelog

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