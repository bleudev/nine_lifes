# 8.0

## Targets

| Minecraft Version | Fabric |
|-------------------|--------|
| 26.4-snapshot-7   | ✅️     |
| 26.3              | ✅️     |
| 26.2              | ❌️     |

## Changes

## Breaking changes

- Rewrite `PostEffectRegistry` and delete `SpectatePostEffectRegistry`:
Use `NLPostEffect` and `SpectateEntityPostEffect` instead. (bleudev [#176](https://github.com/bleudev/nine_lifes/pull/176))
- Many classes, methods etc. were renamed!!!! If you are developer see [#178](https://github.com/bleudev/nine_lifes/pull/178) for details. (bleudev [#178](https://github.com/bleudev/nine_lifes/pull/178))

## Version changes

- End of support for 26.2 (bleudev [#176](https://github.com/bleudev/nine_lifes/pull/176))
- Fabric Loader `0.20.0` (bleudev [#179](https://github.com/bleudev/nine_lifes/pull/179))
- Dump dependencies (bleudev [#179](https://github.com/bleudev/nine_lifes/pull/179))

## Fixes

- Resource pack reload freezes the game (bleudev)
- Wandering armor stand loses their data after rerunning (bleudev [#178](https://github.com/bleudev/nine_lifes/pull/178))