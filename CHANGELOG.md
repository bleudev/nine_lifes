# 6.0

# Target versions

| MC Version      | Fabric |
|-----------------|--------|
| 26.3-snapshot-5 | ✅️     |
| 26.2            | ✅️     |
| 26.1.2          | ✅️     |


# Changes

## Breaking changes

- Shader system was rewritten!! Old methods to render post effects are deleted.
Use `PostEffectRegistry` and `PostEffectRegistry.Builder` instead. See [#113](https://github.com/bleudev/nine_lifes/pull/113) for more
(bleudev [#113](https://github.com/bleudev/nine_lifes/pull/113))

## Version updates

- 26.3-snapshot-5 (bleudev [#115](https://github.com/bleudev/nine_lifes/pull/115))
- [DEV] More convenient dependencies system (bleudev [#115](https://github.com/bleudev/nine_lifes/pull/115)

## New features

- New builder based post effect rendering system (bleudev [#113](https://github.com/bleudev/nine_lifes/pull/113))

## Fixes

- Fog lifes count features are enabled in creative mode (bleudev [#111](https://github.com/bleudev/nine_lifes/pull/111))
- Amethysm's visual effect doesn't disappear when dying with it (bleudev [#117](https://github.com/bleudev/nine_lifes/pull/117))
