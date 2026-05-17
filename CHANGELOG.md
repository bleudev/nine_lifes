# 4.1: Wandering armor stand update

## Deprecations
### 26.1.2 only
- `EntityLifecycleEvents.ENTITY_SPAWN` is deprecated and will be deleted in next major/minor version.
Use Fabric ServerEntityEvents.ENTITY_LOAD event instead (bleudev [#82](https://github.com/bleudev/nine_lifes/pull/82))

## New features
- Wandering armor stand is now breakable: if you hit it 3 times quickly, the stand will break (with visual effects!) (bleudev [#86](https://github.com/bleudev/nine_lifes/pull/86))
- Wandering armor stand will no longer last forever after feeding, but only for 5 minutes (bleudev [#86](https://github.com/bleudev/nine_lifes/pull/86))

## Bug fixes
- Health rendering config option was untranslated in Russian localization (bleudev [#85](https://github.com/bleudev/nine_lifes/pull/85))

## Dev changes
### 26.1.2 only
- Use new fabric allow load event (bleudev [#82](https://github.com/bleudev/nine_lifes/pull/82))

## Version updates
- End of support for 26.1-26.1.1 (bleudev [#81](https://github.com/bleudev/nine_lifes/pull/81))
- 26.1.2 (bleudev [#81](https://github.com/bleudev/nine_lifes/pull/81))
- Fabric language kotlin version was bumped to `1.13.11+kotlin.2.3.21` (bleudev [#84](https://github.com/bleudev/nine_lifes/pull/84))
- Fabric loader version was bumped to `0.19.2` (bleudev [#84](https://github.com/bleudev/nine_lifes/pull/84))
- [DEV] Kotlin version was bumped to `2.3.21` (bleudev [#84](https://github.com/bleudev/nine_lifes/pull/84))
- [DEV] Kotlin Serialization JSON version was bumped to `1.11.0` (bleudev [#84](https://github.com/bleudev/nine_lifes/pull/84))
### 26.1.2 only
- Fabric API version was bumped to `0.149.0` (bleudev [#81](https://github.com/bleudev/nine_lifes/pull/81))
- YACL version was bumped to `3.9.3` (bleudev [#81](https://github.com/bleudev/nine_lifes/pull/81))
