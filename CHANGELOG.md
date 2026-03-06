![Advancements](https://github.com/bleudev/nine_lifes/raw/master/markdown_assets/advancements.png)

## 3.2: Advancements update

## New features
- Added 9 new advancements (bleudev [#50](https://github.com/bleudev/nine_lifes/pull/50))
- [DEV] Use Fabric Data Generator (bleudev [#50](https://github.com/bleudev/nine_lifes/pull/50))

## API changes
### New features
- Use `ServerPlayer.lifes` property setter instead of `ServerPlayer.setLifes()` (bleudev [#58](https://github.com/bleudev/nine_lifes/pull/58))
### Breaking changes
- `ServerPlayer.setLifes(newLifesCount)` was deleted. Use `ServerPlayer.lifes` property setter instead (bleudev [#58](https://github.com/bleudev/nine_lifes/pull/58))
### Deprecations
- `ServerPlayer.setLifes(lifesCountChanger)` is deprecated. Use `ServerPlayer.lifes` property setter instead. Will be deleted in 3.3 (bleudev [#58](https://github.com/bleudev/nine_lifes/pull/58))
- `ServerPlayer.addLifes()` is deprecated. Use `ServerPlayer.lifes` property incrementing instead. Will be deleted in 3.3 (bleudev [#58](https://github.com/bleudev/nine_lifes/pull/58))

## Bug fixes
- Crash when reloading assets related to shaders (bleudev [#50](https://github.com/bleudev/nine_lifes/pull/50))
- Fix screen effects don't pause when game is paused (bleudev [#54](https://github.com/bleudev/nine_lifes/pull/54))