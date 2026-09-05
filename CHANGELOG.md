# 6.4: Wandering armor stand update!!

## Targets

| Minecraft Version | Fabric |
|-------------------|--------|
| 26.3-snapshot-9   | ✅️     |
| 26.2              | ✅️     |
| 26.1.2            | ✅️     |

## Changes

### New features

#### Wandering armor stand

- You can now hang armor, elytras, heads etc. like with normal armor stands!!! (bleudev)
- Added wiggle effect (bleudev)
- Fluids won't affect wandering armor stand anymore (I hope) (bleudev)

#### Public API [DEV]

- `Inventory.anyWithContainers(predicate)`: returns if there is any item in inventory (including items in containers, bundles, etc.) matching given `predicate`
- `ItemStack.isCharged(...)`: returns if this item stack is charged. See function definition for arguments info. (bleudev)

### Fixes

- Containers, bundles etc. weren't considered in players with charged items finder (bleudev)
- Deleted arms from wandering armor stand by default (without code modification) (bleudev)
