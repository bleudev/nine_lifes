# 5.2

# Target versions

| MC Version      | Fabric |
|-----------------|--------|
| 26.3-snapshot-2 | ✅️     |
| 26.2            | ✅️     |
| 26.1.2          | ✅️     |


# Changes

## Version changes
- End of support for 1.21.11 (bleudev [#102](https://github.com/bleudev/nine_lifes/pull/102))
- 26.3 snapshot 2 (bleudev [#105](https://github.com/bleudev/nine_lifes/pull/105))
- [DEV] Gradle 9.5.1 (bleudev [#105](https://github.com/bleudev/nine_lifes/pull/105))
- [DEV] Fabric Loom 1.17.9 (bleudev [#105](https://github.com/bleudev/nine_lifes/pull/105))

## New features
- Charge `max_charged_items_at_a_time` amethyst shards with lightning at a time (bleudev [#99](https://github.com/bleudev/nine_lifes/pull/99))
- Add 5 new gamerules (bleudev [#102](https://github.com/bleudev/nine_lifes/pull/102)):
  - `take_lifes`
  - `take_lifes_in_overworld`
  - `take_lifes_in_nether`
  - `take_lifes_in_end`
  - `max_charged_items_at_a_time`

## Fixes
- Fix wrong cloud rendering in non overworld dimensions (bleudev [#104](https://github.com/bleudev/nine_lifes/pull/104))
- Delete some debug prints (bleudev [#99](https://github.com/bleudev/nine_lifes/pull/99))
