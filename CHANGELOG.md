# 4.0

## Breaking changes
- Deleted `FogProperties.kt` as deprecation in 3.4 (bleudev [#74](https://github.com/bleudev/nine_lifes/pull/74))
- Deleted `NineLifesClientData`. Use `NineLifesClientStorage.kt` instead. (bleudev [#75](https://github.com/bleudev/nine_lifes/pull/75))

## Deletions
- Deleted low lifes red sky effect because it breaks red majority effect.
Newer config is compatible with 3.4's one, but if you have problems with it report
about that in GitHub repository (bleudev [#75](https://github.com/bleudev/nine_lifes/pull/75))

## Version updates
- 26.1
- Fabric Language Kotlin: `1.13.9+kotlin.2.3.10 -> 1.13.10+kotlin.2.3.20`
- [DEV] Fabric Loom: `1.15.4 -> 1.15.5`
- [DEV] Update to Kotlin 2.3.20
### 26.1 only
- Fabric version was bumped up to `0.144.3+26.1`
- YACL was bumped up to `3.9.1+26.1-fabric`