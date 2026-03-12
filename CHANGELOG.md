## 3.4

## Breaking changes
- `ServerPlayer.setLifes(lifesCountChanger)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property instead. (bleudev [#69](https://github.com/bleudev/nine_lifes/pull/69))
- `ServerPlayer.addLifes(addedLifesCount)` was deleted as deprecated in 3.2. Use `ServerPlayer.lifes` property incrementing instead. (bleudev [#69](https://github.com/bleudev/nine_lifes/pull/69))
- `com.bleudev.nine_lifes.client.api.render` package was moved to `com.bleudev.nine_lifes.api.render.client`

## Deprecations
- `FogPropertiesKt.Transformer, fogColor, skyColor, fogStart, fogEnd` was deprecated. Use events or type aliases specified in `Deprecated` annotation.

## New features
- Better health rendering. This improves compatibility with resource packs which changes heart textures. (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
- Insomnia Potions. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))
- Better API for fog and sky rendering (with `ClientEnvironmentSetupEvents`) (bleudev [#70](https://github.com/bleudev/nine_lifes/pull/70))
- Better API for post effect rendering (with `PostEffectRegistry`) (bleudev [#70](https://github.com/bleudev/nine_lifes/pull/70))

## Changes
- Amethysm potion brewing recipe was slightly changed. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Bug fixes
- Living entities can sleep with insomnia. (bleudev [#68](https://github.com/bleudev/nine_lifes/pull/68))

## Optimisations
- Reduced jar file size by compressing png files. (bleudev [#67](https://github.com/bleudev/nine_lifes/pull/67))
