# 1.8
## Breaking changes
### `com.bleudev.nine_lifes.custom.CustomTags`
[-] Those variables are deprecated and will be deleted in `1.9`. Use `ItemTags` variables with same name
 - `CAUSE_BLAST_FURNACE_EXPLODE`
 - `CAUSE_FURNACE_EXPLODE`
 - `CAUSE_SMOKER_EXPLODE`
 - `CAUSE_CAMPFIRE_EXPLODE`
 
## New features
[+] Amethyst shard is invulnerable to `#nine_lifes:is_lightning_or_fire` damage types

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