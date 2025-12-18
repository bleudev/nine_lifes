# 2.0.1
## Minecraft versions
[+] Backport to Minecraft 1.21.6-1.21.8

# 2.0
## Minecraft versions
[-] Minimal minecraft version bumped to 1.21.9

## Depends updates
[+] New dependency: `fabric-language-kotlin >= 1.13.7+kotlin.2.2.21`\
[/] \[DEV] Minimal Fabric Loom version was bumped to `1.14`\
[/] \[DEV] Minimal Gradle version was bumped to `9.2`\
[/] Minimal `fabricloader` version was bumped to `0.18.0`
### 1.21.11+ only
[/] Minimal `fabric` (Fabric API) version was bumped to `0.140.0+1.21.11`\
[/] Minimal `midnightlib` version was bumped to `1.9.2`

## Breaking changes
[/] All code was rewritten to Kotlin\
[/] All code was rewritten to official Mojang Mappings (instead of `jarn`s)\
[-] \[DEV] Almost every class in mod was rewritten and renamed. If your mod wants to migrate to `2.0` see [#26](https://github.com/bleudev/nine_lifes/pull/26)\
[-] \[DEV] Signatures of many functions, methods etc. was edited, please see [#26](https://github.com/bleudev/nine_lifes/pull/26)\
[-] If you have world with nine lifes 1.x be careful and backup it.

## Fixes
[/] Death screen after death from charged amethyst shard is white\
[-] With amethysm effect question marks now doesn't appear.\
[+] Optimised and fixes many things such as lifes system

# 1.10 (unreleased)
## Depends updates
[+] \[DEV] Loom version was updated up to `1.13.3`\
[+] Fabric loader version was updated up to `0.17.3`

## Breaking changes
[-] `com.bleudev.nine_lifes.Nine_lifes.MOD_ID` was deleted.
Use `com.bleudev.nine_lifes.NineLifesConst.MOD_ID` instead.\
[-] `com.bleudev.nine_lifes.custom.CustomEnchantments.getEntry()` was deleted.
Use `Entries` class's functions and methods instead.

# 1.9.1
## Deprecations
[-] `com.bleudev.nine_lifes.Nine_lifes.MOD_ID` now is deprecated and will be deleted in `1.10`.
Use `com.bleudev.nine_lifes.NineLifesConst.MOD_ID`

## New features
[+] Beta mode

## Optimisations
[+] Some platform helper functions which helps easily get mod version, name and authors\
[+] Merge two version branches into one (`master` branch)

# 1.9
## Deprecations
[-] `com.bleudev.nine_lifes.custom.CustomEnchantments.getEntry(DynamicRegistryManager, RegistryKey<Enchantment>)`
now is deprecated and will be deleted in `1.10`. Use `Entries` class's functions and methods instead.

## API changes
[+] Added `EntitySpawnEvents.ENTITY_SPAWN` event which provides easy way to get entities that was spawned (didn't load!)
[-] Some variables from `com.bleudev.nine_lifes.custom.CustomTags` were deleted:
- `CAUSE_BLAST_FURNACE_EXPLODE`
- `CAUSE_FURNACE_EXPLODE`
- `CAUSE_SMOKER_EXPLODE`
- `CAUSE_CAMPFIRE_EXPLODE`

Make sure that you don't use they in your mod depends on `nine lifes`

## New features
[+] Chance of spawning Wandering Armor Stand instead of common Armor Stand ([#8](https://github.com/bleudev/nine_lifes/issues/8))

# 1.8
## Breaking changes
[+] Items that can be enchanted with charge enchantment in enchantment table moved to `#nine_lifes:enchantable/charge_in_table` tag

## Deprecations
### `com.bleudev.nine_lifes.custom.CustomTags`
[-] Those variables are deprecated and will be deleted in `1.9`. Use `ItemTags` variables with same name
- `CAUSE_BLAST_FURNACE_EXPLODE`
- `CAUSE_FURNACE_EXPLODE`
- `CAUSE_SMOKER_EXPLODE`
- `CAUSE_CAMPFIRE_EXPLODE`
 
## New features
[+] Amethyst shard is invulnerable to `#nine_lifes:is_lightning_or_fire` damage types\
[+] Amethyst shard can get charged when lightning bolt struck it ([#17](https://github.com/bleudev/nine_lifes/issues/17))

## Deleted
[-] Now you can't get charge enchantment for amethyst shard in enchantment table

# 1.7.2
## New features
[+] Added ability to use `/nl revive` without `player` argument to revive player who used command\
[+] Send join message only when player in survival like game mode
## Bug fixes
[/] Disable heart rendering in non survival like game modes ([#15](https://github.com/bleudev/nine_lifes/issues/15))
### 1.21.9 only
[/] [HOTFIX] Fix Russian localisation aren't working

# 1.7
## Depends updates
### 1.21.9+
[/] `cloth-config` dependency was replaced with `midnightlib` version >= `1.8.3`
### 1.21.6-1.21.8
[/] `cloth-config` dependency was replaced with `midnightlib` version >= `1.7.5`

## New features
[+] Rewrite config to [`MidnightLib`](https://modrinth.com/mod/midnightlib) ([#12](https://github.com/bleudev/nine_lifes/issues/12))\
[+] Add ability turn off heartbeat in config ([#11](https://github.com/bleudev/nine_lifes/issues/11))\
[+] Add ability choose position of lives heart ([#14](https://github.com/bleudev/nine_lifes/issues/14))\
[+] New `/nl revive` command ([#13](https://github.com/bleudev/nine_lifes/issues/13))

## Bug fixes
[/] Possibility of error when using `/nl set` and `/nl reset` (without arguments) commands on the server

---
# 1.6
## Breaking changes
[-] Deleted `nine_lifes:blue_eyes` entity

## Depends updates
### 1.21.9
[/] Required `fabricloader` version >= `0.17.2`\
[/] Required `fabric` version >= `0.134.0`\
[/] Required `cloth-config` version >= `20.0.0`

## New features
[+] Update for Minecraft 1.21.9\
[+] New amethysm screen effect\
[+] Move rendering center heart from mixin to layer\
[+] Center heart now has heartbeat

## Fixes
[/] Delete debug printing