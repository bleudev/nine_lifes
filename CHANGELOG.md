## 3.4

## Breaking changes
- `ServerPlayer.setLifes(lifesCountChanger)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property instead. (bleudev [#69](https://github.com/bleudev/nine_lifes/pull/69))
- `ServerPlayer.addLifes(addedLifesCount)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property incrementing instead. (bleudev [#69](https://github.com/bleudev/nine_lifes/pull/69))

## Deprecations
- `FogPropertiesKt.Transformer, fogColor, skyColor, fogStart, fogEnd` was deprecated. Use events or type aliases specified in `Deprecated` annotation.

## New features
- Better health rendering. This improves compatibility with resource packs which changes heart textures. (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
- Insomnia Potions. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))
- Better API for fog, sky and shaders rendering (bleudev [#70](https://github.com/bleudev/nine_lifes/pull/70))

## Changes
- Amethysm potion brewing recipe was slightly changed. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Bug fixes
- Living entities can sleep with insomnia. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Optimisations
- Reduced jar file size by compressing png files. (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
