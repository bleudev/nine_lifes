# 6.0

# Target versions

| MC Version      | Fabric |
|-----------------|--------|
| 26.3-snapshot-3 | ✅️     |
| 26.2            | ✅️     |
| 26.1.2          | ✅️     |


# Changes

## Breaking changes

- Shader system was rewritten!! Old methods to render post effects are deleted.
Use `PostEffectRegistry` and `PostEffectRegistry.Builder` instead. See [#113](https://github.com/bleudev/nine_lifes/pull/113) for more
(bleudev [#113](https://github.com/bleudev/nine_lifes/pull/113))

## New features

- New builder based post effect rendering system (bleudev [#113](https://github.com/bleudev/nine_lifes/pull/113))

## Fixes

- Fog lifes count features are enabled in creative mode (bleudev [#111](https://github.com/bleudev/nine_lifes/pull/111))
