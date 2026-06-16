package com.bleudev.nine_lifes.util.helper

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata

object PlatformHelper {
    private fun <T> getModInfo(modId: String, getter: (ModMetadata) -> T): T? {
        try {
            return FabricLoader.getInstance().getModContainer(modId)
                .map { c: ModContainer -> getter(c.metadata) }
                .orElse(null)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Get version string of mod which has specified [modId]
     *
     * @param modId ID of mod where to get version
     * @return Mod version
     */
    fun getModVersion(modId: String): String = getModInfo(modId) { m -> m.version.friendlyString } ?: "0.0.0"

    /**
     * Get version substring before first occurrence of `before` of mod which has specified [modId]
     *
     * @param modId ID of mod where to get version
     * @param before String that is the end of a substring (exclusive).
     * If it isn't in the string, the function will return the result of `getModVersion(modId)`
     * @return Mod version substring
     *
     * ```
     * v1 = getModVersion(MOD_ID); // 1.0.0+1.21.9
     * v2 = getModVersion(MOD_ID, "+"); // 1.0.0
     *
     * // But
     * v3 = getModVersion(MOD_ID, "-"); // 1.0.0+1.21.9
     * ```
     */
    fun getModVersion(modId: String, before: String): String {
        val v = getModVersion(modId)
        val i = v.indexOf(before)
        return v.take(if (i == -1) v.length else i)
    }

    /**
     * Get name (not ID!) of mod which has specified [modId]
     *
     * @param modId ID of mod where to get name
     * @return Mod name
     */
    fun getModName(modId: String): String = getModInfo(modId) { m -> m.name } ?: "Unknown"

    /**
     * Get authors names of mod which has specified [modId]
     *
     * @param modId ID of mod where to get authors
     * @return Mod authors names
     */
    fun getModAuthorsNames(modId: String): List<String> = getModInfo(modId)
        { m -> m.authors.stream().map { p -> p.name }.toList() } ?: listOf()
}