## 3.4

## Breaking changes
- `ServerPlayer.setLifes(lifesCountChanger)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property instead.
- `ServerPlayer.addLifes(addedLifesCount)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property incrementing instead.

## New features
- Better health rendering. This improves compatibility with resource packs which changes heart textures. (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
- Insomnia Potions (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Changes
- Amethysm potion brewing recipe was slightly changed (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Bug fixes
- Living entities can sleep with insomnia (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Optimisations
- Reduced jar file size by compressing png files (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
