# 1.7.1 (1.21.6)
## Depends updates
[/] `[HOTFIX]` 1.21.6 port can be runned on Minecraft 1.21.9+

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