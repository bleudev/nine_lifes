# 7.0

![Minecraft 26 3](https://github.com/bleudev/nine_lifes/raw/master/markdown_assets/26_3.png)

## Targets

| Minecraft Version | Fabric |
|-------------------|--------|
| 26.3              | ✅️     |
| 26.2              | ✅️     |

## Changes

### Breaking changes

#### 26.3

- [DEV] `ClientEnvironmentSetupEvents.SKY_COLOR` now use `Vector3fc` instead of `Int` (how Mojang) (bleudev [#143](https://github.com/bleudev/nine_lifes/pull/143))

### Dependencies

#### Minecraft
- 26.3 (bleudev [#152](https://github.com/bleudev/nine_lifes/pull/152))
- End of support for Minecraft `26.1.2` (bleudev [#144](https://github.com/bleudev/nine_lifes/pull/144))

#### Others
- Fabric Language Kotlin `1.14.1+kotlin.2.4.20` (bleudev [#147](https://github.com/bleudev/nine_lifes/pull/147))
- Fabric Loader `0.19.5` (bleudev [#143](https://github.com/bleudev/nine_lifes/pull/143))
- [DEV] Fabric Loom `1.18.1` (bleudev [#152](https://github.com/bleudev/nine_lifes/pull/152))
- [DEV] Gradle `9.7.1` (bleudev [#152](https://github.com/bleudev/nine_lifes/pull/152))

### New features

- Ability to disable the effects near players with charged items or amethysm. (bleudev [#148](https://github.com/bleudev/nine_lifes/pull/148))

#### Wandering armor stand

- You can now hang armor, elytras, heads etc. like with normal armor stands!!! (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- Fluids won't affect wandering armor stand anymore (I hope) (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))

#### Public API [DEV]

- `Inventory.anyWithContainers(predicate)`: returns if there is any item in inventory (including items in containers, bundles, etc.) matching given `predicate` (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- `ItemStack.isCharged(...)`: returns if this item stack is charged. See function definition for arguments info. (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))

### Fixes

- Amethyst stick use cause server freeze forever in some situations (bleudev [#143](https://github.com/bleudev/nine_lifes/pull/143))
- Sunset is visible when always day (bleudev [#154](https://github.com/bleudev/nine_lifes/pull/154))
- Containers, bundles etc. weren't considered in players with charged items finder (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- Deleted arms from wandering armor stand by default (without code modification) (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
