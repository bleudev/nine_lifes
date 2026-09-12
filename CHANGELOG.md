# 7.0

## Targets

| Minecraft Version | Fabric                                           |
|-------------------|--------------------------------------------------|
| 26.3-snapshot-9   | ✅️                                               |
| 26.2              | ✅️                                               |

## Changes

### Dependencies

- Fabric Loader 0.19.5 (bleudev [#143](https://github.com/bleudev/nine_lifes/pull/143))
- [DEV] Gradle 9.6.1 (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))

### New features

#### Wandering armor stand

- You can now hang armor, elytras, heads etc. like with normal armor stands!!! (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- Fluids won't affect wandering armor stand anymore (I hope) (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))

#### Public API [DEV]

- `Inventory.anyWithContainers(predicate)`: returns if there is any item in inventory (including items in containers, bundles, etc.) matching given `predicate` (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- `ItemStack.isCharged(...)`: returns if this item stack is charged. See function definition for arguments info. (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))

### Fixes

- Amethyst stick use cause server freeze forever in some situations (bleudev [#143](https://github.com/bleudev/nine_lifes/pull/143))
- Containers, bundles etc. weren't considered in players with charged items finder (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
- Deleted arms from wandering armor stand by default (without code modification) (bleudev [#142](https://github.com/bleudev/nine_lifes/pull/142))
