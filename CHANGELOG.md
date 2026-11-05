# 8.0

## Targets

| Minecraft Version | Fabric |
|-------------------|--------|
| 26.4-snapshot-4   | ✅️     |
| 26.3              | ✅️     |
| 26.2              | ❌️     |

## Changes

## Breaking changes

- Rewrite `PostEffectRegistry` and delete `SpectatePostEffectRegistry`:
Use `NLPostEffect` and `SpectateEntityPostEffect` instead. (bleudev [#176](https://github.com/bleudev/nine_lifes/pull/176))

## Version changes

- End of support for 26.2 (bleudev [#176](https://github.com/bleudev/nine_lifes/pull/176))

## Fixes

- Resource pack reload freezes the game (bleudev)