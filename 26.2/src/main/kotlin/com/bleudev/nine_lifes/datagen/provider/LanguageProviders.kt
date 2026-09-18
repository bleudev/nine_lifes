package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.NineLifesStats
import com.bleudev.nine_lifes.client.config.HealthRendering
import com.bleudev.nine_lifes.client.config.HeartPosition
import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.util.*
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import java.util.concurrent.CompletableFuture
import kotlin.enums.enumEntries

private fun generateNineLifesDefaultTranslations(builder: FabricLanguageProvider.TranslationBuilder) {
    // Mod Menu
    FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent {
        builder.add("modmenu.nameTranslation.$MOD_ID", it.metadata.name)
        builder.add("modmenu.descriptionTranslation.$MOD_ID", it.metadata.description)
    }

    // Mob effects
    builder.add(NineLifesMobEffects.AMETHYSM.value(), "Amethysm")
    builder.add(NineLifesMobEffects.INSOMNIA.value(), "Insomnia")

    fun translatePotion(translationsName: String, vararg names: String) {
        builder.addPotions(names.toList(), "Potion of $translationsName", "Splash Potion of $translationsName", "Lingering Potion of $translationsName", "Arrow of $translationsName")
    }
    translatePotion("Amethysm", "amethysm")
    translatePotion("Insomnia", "insomnia", "longer_insomnia")
    // Items
    builder.add(NineLifesItems.AMETHYST_STICK, "Amethyst stick")
    // Death messages
    builder.add("death.attack.amethysm", $$"%1$s didn't expect amethysts to kill")
    builder.add("death.attack.amethysm.player", $$"%1$s didn't expect amethysts to kill")
    builder.add("death.attack.charged_amethyst", $$"%1$s learned the power of amethyst")
    builder.add("death.attack.charged_amethyst.player", $$"%1$s learned the power of amethyst")
    builder.add("death.attack.unknown", $$"%1$s died of unknown cause")
    builder.add("death.attack.unknown.player", $$"%1$s died of unknown cause")
    // Enchantments
    builder.add("enchantment.nine_lifes.charge", "Charge")
    // Advancements
    builder.addAdvancement("root", "Nine lifes!", "Start your journey")
    builder.addAdvancement("try_sleep_without_shard", "Huh?", "For some reason, you couldn't sleep. Is there any item that will fix this?")
    builder.addAdvancement("slept_with_shard", "Sweet Dreams.. Again", "Sleep after eating amethyst shard")
    builder.addAdvancement("got_charged_shard", "Power of the light", "Get an charged amethyst shard")
    builder.addAdvancement("got_life_with_shard", "+1", "Get an life with charged amethyst shard")
    builder.addAdvancement("ate_64_charged_shards", "Is it that delicious?", "Eat 64 charged amethyst shard")
    builder.addAdvancement("got_amethyst_stick", "NOW I'M A GOD!", "Get an amethyst stick")
    builder.addAdvancement("almost_dead", "Almost dead", "Survive with one half heart and life")
    builder.addAdvancement("hundred_days", "100 days", "Survive 100 days")
    builder.addAdvancement("true_hundred_days", "True 100 days", "Survive 100 days with one life")
    builder.addAdvancement("all_done", "All done", "Award all Nine Lifes advancements")
    // Tags
    builder.add(NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE, "Cause blast furnace explode")
    builder.add(NineLifesItemTags.CAUSE_FURNACE_EXPLODE, "Cause furnace explode")
    builder.add(NineLifesItemTags.CAUSE_SMOKER_EXPLODE, "Cause smoker explode")
    builder.add(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE, "Cause campfires explode")
    builder.add(NineLifesItemTags.LIGHTNING_CHARGEABLE, "Lightning chargeable")
    builder.add(NineLifesItemTags.Enchantable.CHARGE, "Enchantable with charge")
    builder.add(NineLifesItemTags.Enchantable.CHARGE_IN_TABLE, "Enchantable with charge in enchanting table")
    builder.add(NineLifesDamageTypeTags.GIVES_LIFE, "Gives life on death")
    builder.add(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE, "Lightning or fire")
    // Chat messages
    builder.add("chat.message.join.lives", $$"Your lifes: %1$s.")
    builder.add("chat.message.join.lives.careful", $$"Your lifes: %1$s. Be careful!")
    builder.add("chat.message.join.beta", "Warning! You're running a beta version of the mod. If you find a bug, please report it to this link:")
    // Commands
    builder.add("commands.nl.text.author", $$"Author: %1$s")
    builder.add("commands.nl.text.version", $$"Version: %1$s")
    builder.add("commands.nl.text.links", "Links:")
    builder.add("commands.nl.reset.success", "Lifes were successfully reset")
    builder.add("commands.nl.reset.player.success", $$"%1$s's lifes have been successfully reset")
    builder.add("commands.nl.set.success", $$"The number of lifes was successfully set to %1$s")
    builder.add("commands.nl.set.player.success", $$"%2$s's lifes count was successfully set to %1$s")
    builder.add("commands.nl.add.success", $$"The number of lifes was successfully increased by %1$s")
    builder.add("commands.nl.add.success.neg", $$"The number of lifes was successfully decreased by %1$s")
    builder.add("commands.nl.add.player.success", $$"%2$s's lifes count was successfully increased by %1$s")
    builder.add("commands.nl.add.player.success.neg", $$"%2$s's lifes count was successfully decreased by %1$s")
    builder.add("commands.nl.revive.success", "You were revived")
    builder.add("commands.nl.revive.player.success", $$"%1$s was revived")
    builder.add("commands.nl.get.player.success", $$"%2$s's lifes count: %1$s")

    builder.add("commands.not_a_player", "The command is available only to players")
    // Entities
    builder.add(NineLifesEntities.WANDERING_ARMOR_STAND, "Wandering armor stand")
    // Stats
    builder.addStat(NineLifesStats.USED_CHARGED, "Used charged items")
    // Gamerules
    builder.add("gamerule.category.nine_lifes.general", "Nine lifes")
    builder.addGameRule("take_lifes", "Take lifes",
        "Take lifes from dead players. This only affects taking; charged items continue to add lifes.")
    builder.addGameRule("take_lifes_in_overworld", "Take lifes in the Overworld",
        "Take lifes from dead players in the Overworld. Doesn't matter if taking is disabled.")
    builder.addGameRule("take_lifes_in_nether", "Take lifes in the Nether",
        "Take lifes from dead players in the Nether. Doesn't matter if taking is disabled.")
    builder.addGameRule("take_lifes_in_end", "Take lifes in the End",
        "Take lifes from dead players in the End. Doesn't matter if taking is disabled.")
    builder.addGameRule("max_charged_items_at_a_time", "Max charged items at a time",
        "The max number of charged items gained from a single lightning bolt. A value of -1 means infinity (no limit).")
    // Sounds
    builder.add(NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_HURT, "Wandering armor stand hurts")
    builder.add(NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_DEATH, "Wandering armor stand disappears")
    // Other
    builder.add("block.minecraft.bed.insomnia_effect", "You won't be able to sleep now")
    builder.add(deathScreenRemaining(1), "Last chance!")
    builder.add(deathScreenRemaining(2), "2 lifes left")
    builder.add(deathScreenRemaining(3), "3 lifes left")
    builder.add(deathScreenRemaining(4), "4 lifes left")
    builder.add(deathScreenRemaining(5), "5 lifes left")
    builder.add(deathScreenRemaining(6), "6 lifes left")
    builder.add(deathScreenRemaining(7), "7 lifes left")
    builder.add(deathScreenRemaining(8), "8 lifes left")
    // Config
    var tb = ConfigTranslationKeyBuilder.default()

    builder.add(tb.additional("title"), "Nine lifes config")
    /// General
    tb = tb.category()
    builder.add(tb, "General")
    tb = tb.root()
    builder.addConfigOption(tb.option("join_message"),
        "Enable join message", "Display message with lifes count on join server")
    builder.addConfigOption(tb.option("heartbeat"),
        "Enable heartbeat effect", "When true lifes count will beat")
    builder.addConfigOption(tb.option("heart_position"),
        "Heart position", "Location of lifes count on the screen")
    builder.addConfigOption(tb.option("low_lifes_red_sky"),
        "Red sky when there are few lifes", "When true sky will become red when lifes count is low")
    builder.addConfigOption(tb.option("health_rendering"),
        "Health rendering", "Controls player health rendering\nHardcore - Always render hardcore hearts\nTrue hardcore -  Only if you have one life\nVanilla - Vanilla behavior")
    builder.addConfigOption(tb.option("death_screen_remaining"),
        "Remaining lifes on the death screen", "The death screen will now display the number of lifes remaining instead of the \"You Died!\" message")
    /// Charged/Amethysm
    tb = ConfigTranslationKeyBuilder.default().category("charged_amethysm")
    builder.addConfigOption(tb, "Amethysm/Charged Items")
    tb = tb.root()
    builder.addConfigOption(tb.option("enabled"),
        "Enable", "Toggles the effects near players with charged items or Amethysm on or off. Useful for quickly disabling everything with a single button.")
    tb = tb.group("charged")
    builder.addConfigOption(tb, "Charged Items")
    builder.addConfigOption(tb.option("self"),
        "This Player", "Show the effect when holding charged items in the inventory.")
    builder.addConfigOption(tb.option("players"),
        "Other Players", "Show the effect near players with charged items.")
    tb = tb.group("amethysm")
    builder.addConfigOption(tb, "Amethysm")
    builder.addConfigOption(tb.option("players"),
        "Other Players", "Show the effect near players with Amethysm.")

    builder.addConfigEnum<HeartPosition>(
        "Bottom left", "Bottom center", "Bottom right",
        "Top left", "Top center", "Top right"
    )
    builder.addConfigEnum<HealthRendering>(
        "Hardcore", "True hardcore", "Vanilla"
    )
}

class NLDefaultLanguageProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, registriesFuture) {
    override fun generateTranslations(
        registryLookup: HolderLookup.Provider,
        builder: TranslationBuilder
    ) {
        generateNineLifesDefaultTranslations(builder)
    }
}

class NLUpsideDownLanguageProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, "en_ud", registriesFuture) {
    private val upsideDownMap = mapOf(
        "a" to "ɐ", "b" to "q", "c" to "ɔ", "d" to "p", "e" to "ǝ", "f" to "ɟ", "g" to "ᵷ", "h" to "ɥ",
        "i" to "ᴉ", "j" to "ɾ", "k" to "ʞ", "l" to "ꞁ", "m" to "ɯ", "n" to "u", "o" to "o", "p" to "d",
        "q" to "b", "r" to "ɹ", "s" to "s", "t" to "ʇ", "u" to "n", "v" to "ʌ", "w" to "ʍ", "x" to "x",
        "y" to "ʎ", "z" to "z",
        "A" to "Ɐ", "B" to "ᗺ", "C" to "Ɔ", "D" to "ᗡ", "E" to "Ǝ", "F" to "Ⅎ", "G" to "⅁", "H" to "H",
        "I" to "I", "J" to "Ր", "K" to "Ʞ", "L" to "Ꞁ", "M" to "W", "N" to "N", "O" to "O", "P" to "Ԁ",
        "Q" to "Ꝺ", "R" to "ᴚ", "S" to "S", "T" to "⟘", "U" to "∩", "V" to "Ʌ", "W" to "M", "X" to "X",
        "Y" to "⅄", "Z" to "Z",
        "0" to "0", "1" to "Ɩ", "2" to "ᘔ", "3" to "Ɛ", "4" to "߈", "5" to "ϛ", "6" to "9", "7" to "ㄥ",
        "8" to "8", "9" to "6",
        "." to "˙", "," to "‘", "?" to "¿", "!" to "¡", "\"" to "„", "'" to ",", ";" to "⸵",
        "(" to ")", ")" to "(", "[" to "]", "]" to "[", "{" to "}", "}" to "{", "<" to ">", ">" to "<",
        "&" to "⅋", "_" to "‾", " " to " "
    )

    override fun generateTranslations(
        registryLookup: HolderLookup.Provider,
        builder: TranslationBuilder
    ) {
        generateNineLifesDefaultTranslations { key, tr ->
            var ans = upsided(tr)

            var i = 1
            var s = $$"%$$i$s"
            while (upsided(s) in ans) {
                ans = ans.replace(upsided(s), s)
                i++
                s = $$"%$$i$s"
            }
            builder.add(key, ans)
        }
    }

    private fun upsided(t: String): String = t
        .reversed()
        .mapString { upsideDownMap.getOrDefault(it.toString(), it.toString()) }
}

class NLRussianLanguageProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, "ru_ru", registriesFuture) {
    override fun generateTranslations(registryLookup: HolderLookup.Provider, builder: TranslationBuilder) {
        // Mod Menu
        builder.add("modmenu.descriptionTranslation.$MOD_ID", "Мод, который даёт в выживании только 9 жизней перед смертью навсегда.")
        // Mob effects
        builder.add(NineLifesMobEffects.AMETHYSM.value(), "Аметизм")
        builder.add(NineLifesMobEffects.INSOMNIA.value(), "Бессоница")

        fun translatePotion(translationsName: String, vararg names: String) {
            builder.addPotions(names.toList(), "Зелье $translationsName", "Взрывное зелье $translationsName", "Туманное зелье $translationsName", "Стрела $translationsName")
        }
        translatePotion("аметизма", "amethysm")
        translatePotion("бессоницы", "insomnia", "longer_insomnia")
        // Items
        builder.add(NineLifesItems.AMETHYST_STICK, "Аметистовая палочка")
        // Death messages
        builder.add("death.attack.amethysm", $$"%1$s не ожидал, что аметисты убивают")
        builder.add("death.attack.amethysm.player", $$"%1$s не ожидал, что аметисты убивают")
        builder.add("death.attack.charged_amethyst", $$"%1$s познал силу аметиста")
        builder.add("death.attack.charged_amethyst.player", $$"%1$s познал силу аметиста")
        builder.add("death.attack.unknown", $$"%1$s умер по неизвестной причине")
        builder.add("death.attack.unknown.player", $$"%1$s умер по неизвестной причине")
        // Enchantments
        builder.add("enchantment.nine_lifes.charge", "Заряд")
        // Advancements
        builder.addAdvancement("root", "Девять жизней!", "Начните своё путешествие")
        builder.addAdvancement("try_sleep_without_shard", "Ээм?", "По непонятной причине вы не могли уснуть. Есть ли средство от этого?")
        builder.addAdvancement("slept_with_shard", "Спи моя радость усни.. Опять", "Поспите съев аметистовый осколок")
        builder.addAdvancement("got_charged_shard", "Сила света", "Получите заряженный аметистовый осколок")
        builder.addAdvancement("got_life_with_shard", "+1", "Получите жизнь с помощью заряженного аметистового осколка")
        builder.addAdvancement("ate_64_charged_shards", "Это настолько вкусно?", "Съешьте 64 заряженных аметистовых осколков")
        builder.addAdvancement("got_amethyst_stick", "ТЕПЕРЬ Я БОГ!", "Получите аметистовую палочку")
        builder.addAdvancement("almost_dead", "На волоске", "Выживите с одним полсердечком и жизней")
        builder.addAdvancement("hundred_days", "100 дней", "Проживите 100 дней")
        builder.addAdvancement("true_hundred_days", "Истинные 100 дней", "Проживите 100 дней с одной жизнью")
        builder.addAdvancement("all_done", "Готов", "Выполните все достижения Nine Lifes")
        // Tags
        builder.add(NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE, "Взрывает плавильные печи")
        builder.add(NineLifesItemTags.CAUSE_FURNACE_EXPLODE, "Взрывает печки")
        builder.add(NineLifesItemTags.CAUSE_SMOKER_EXPLODE, "Взрывает коптильни")
        builder.add(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE, "Взрывает костры")
        builder.add(NineLifesItemTags.LIGHTNING_CHARGEABLE, "Заряжается электричеством")
        builder.add(NineLifesItemTags.Enchantable.CHARGE, "Способно быть заряженным")
        builder.add(NineLifesItemTags.Enchantable.CHARGE_IN_TABLE, "Способно заряжаться в столе зачарований")
        builder.add(NineLifesDamageTypeTags.GIVES_LIFE, "Даёт жизнь при смерти")
        builder.add(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE, "Молния или огонь")
        // Chat messages
        builder.add("chat.message.join.lives", $$"Ваши жизни: %1$s.")
        builder.add("chat.message.join.lives.careful", $$"Ваши жизни: %1$s. Будьте начеку!")
        builder.add("chat.message.join.beta", "Внимание! Вы запустили бета-версию мода. Если вы обнаружите баг, то отправьте его по этому адресу:")
        // Commands
        builder.add("commands.nl.text.author", $$"Автор: %1$s")
        builder.add("commands.nl.text.version", $$"Версия: %1$s")
        builder.add("commands.nl.text.links", "Ссылки:")
        builder.add("commands.nl.reset.success", "Жизни были успешно сброшены")
        builder.add("commands.nl.reset.player.success", $$"Жизни %1$s были успешно сброшены")
        builder.add("commands.nl.set.success", $$"Количество жизней было успешно установлено на %1$s")
        builder.add("commands.nl.set.player.success", $$"Количество жизней %2$s было успешно установлено на %1$s")
        builder.add("commands.nl.add.success", $$"Количество жизней успешно увеличилось на %1$s")
        builder.add("commands.nl.add.success.neg", $$"Количество жизней успешно уменьшилось на %1$s")
        builder.add("commands.nl.add.player.success", $$"Количество жизней %2$s успешно увеличилось на %1$s")
        builder.add("commands.nl.add.player.success.neg", $$"Количество жизней %2$s успешно уменьшилось на %1$s")
        builder.add("commands.nl.revive.success", "Вы были возрождены")
        builder.add("commands.nl.revive.player.success", $$"Игрок %1$s был возрождён")
        builder.add("commands.nl.get.player.success", $$"Количество жизней %2$s: %1$s")

        builder.add("commands.not_a_player", "Команда доступна только для игроков")
        // Entities
        builder.add(NineLifesEntities.WANDERING_ARMOR_STAND, "Бродячая стойка для брони")
        // Stats
        builder.addStat(NineLifesStats.USED_CHARGED, "Использовано заряженных предметов")
        // Gamerules
        builder.add("gamerule.category.nine_lifes.general", "Девять жизней")
        builder.addGameRule("take_lifes", "Отнимать жизни",
            "Отнимать жизни умерших игроков. Влияет только на отнятие, заряженные предметы продолжают добавлять жизни.")
        builder.addGameRule("take_lifes_in_overworld", "Отнимать жизни в Верхнем Мире",
            "Отнимать жизни умерших игроков в Верхнем Мире. Не имеет значения если отнятие жизней отключено.")
        builder.addGameRule("take_lifes_in_nether", "Отнимать жизни в Незере",
            "Отнимать жизни умерших игроков в Незере. Не имеет значения если отнятие жизней отключено.")
        builder.addGameRule("take_lifes_in_end", "Отнимать жизни в Энде",
            "Отнимать жизни умерших игроков в Энде. Не имеет значения если отнятие жизней отключено.")
        builder.addGameRule("max_charged_items_at_a_time", "Максимум заряженных предметов за раз",
            "Максимальное количество заряженных предметов, получаемых от одного удара молнии. Значение -1 означает бесконечность (отсутствие ограничения).")
        // Sounds
        builder.add(NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_HURT, "Бродячая стойка для брони ранена")
        builder.add(NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_DEATH, "Бродячая стойка для брони пропадает")
        // Other
        builder.add("block.minecraft.bed.insomnia_effect", "Сейчас не получится уснуть")
        builder.add(deathScreenRemaining(1), "Последний шанс!")
        builder.add(deathScreenRemaining(2), "2 жизни осталось")
        builder.add(deathScreenRemaining(3), "3 жизни осталось")
        builder.add(deathScreenRemaining(4), "4 жизни осталось")
        builder.add(deathScreenRemaining(5), "5 жизней осталось")
        builder.add(deathScreenRemaining(6), "6 жизней осталось")
        builder.add(deathScreenRemaining(7), "7 жизней осталось")
        builder.add(deathScreenRemaining(8), "8 жизней осталось")
        // Config
        var tb = ConfigTranslationKeyBuilder.default()
        builder.add(tb.additional("title"), "Конфиг Nine lifes")
        /// General
        tb = tb.category()
        builder.add(tb, "Главные")
        tb = tb.root()
        builder.addConfigOption(tb.option("join_message"),
            "Включить приветственное сообщение", "Показывать сообщение с количеством жизней при заходе на сервер")
        builder.addConfigOption(tb.option("heartbeat"),
            "Включить эффект сербцебиения", "Когда включено сердце будет пульсировать")
        builder.addConfigOption(tb.option("heart_position"),
            "Расположение сердца", "Расположение количества жизней на экране")
        builder.addConfigOption(tb.option("low_lifes_red_sky"),
            "Красное небо когда мало жизней", "Когда включено небо будет краснеть при низком количестве жизней")
        builder.addConfigOption(tb.option("health_rendering"),
            "Рендеринг здоровья", "Контролирует рендеринг здоровья игрока\nХардкор - всегда рендерить хардкорные сердца\nИстинный хардкор - Только если у вас одна жизнь\nВанила - Ванильное поведение")
        builder.addConfigOption(tb.option("death_screen_remaining"),
            "Оставшиеся жизни на экране смерти", "На экране смерти будет отображаться количество оставшихся жизней вместо надписи \"Вы умерли!\"")
        /// Charged/amethysm
        tb = ConfigTranslationKeyBuilder.default().category("charged_amethysm")
        builder.addConfigOption(tb, "Аметизм/Заряженные предметы")
        tb = tb.root()
        builder.addConfigOption(tb.option("enabled"),
            "Включить", "Включает/выключает полностью эффекты рядом с игроками с заряженными предметами или аметизмом. Удобно когда нужно быстро выключить одной кнопкой.")
        tb = tb.group("charged")
        builder.addConfigOption(tb, "Заряженные предметы")
        builder.addConfigOption(tb.option("self"),
            "Этот игрок", "Показывать эффект при наличии в инвентаре заряженных предметов.")
        builder.addConfigOption(tb.option("players"),
            "Остальные игроки", "Показывать эффект рядом с игроками с заряженными предметами.")
        tb = tb.group("amethysm")
        builder.addConfigOption(tb, "Аметизм")
        builder.addConfigOption(tb.option("players"),
            "Остальные игроки", "Показывать эффект рядом с игроками с аметизмом.")

        builder.addConfigEnum<HeartPosition>(
            "Снизу слева", "Снизу в центре", "Снизу справа",
            "Сверху слева", "Сверху в центре", "Сверху справа"
        )
        builder.addConfigEnum<HealthRendering>(
            "Хардкор", "Истинный хардкор", "Ванила"
        )
    }
}

class NLPreReformRussiandLanguageProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, "rpr", registriesFuture) {
    override fun generateTranslations(
        registryLookup: HolderLookup.Provider,
        builder: TranslationBuilder
    ) {
        // Mod Menu
        builder.add("modmenu.descriptionTranslation.$MOD_ID", "Модъ, дающій въ выживаніи лишь 9 жизней до смерти навѣки.\n")
        // Mob effects
        builder.add(NineLifesMobEffects.AMETHYSM.value(), "Аметизмъ")
        builder.add(NineLifesMobEffects.INSOMNIA.value(), "Безсоница")

        fun translatePotion(translationsName: String, vararg names: String) {
            builder.addPotions(
                names.toList(),
                "Снадобье $translationsName",
                "Взрывное снадобье $translationsName",
                "Туманное снадобье $translationsName",
                "Стрѣла $translationsName"
            )
        }
        translatePotion("аметизма", "amethysm")
        translatePotion("безсоницы", "insomnia", "longer_insomnia")
        // Items
        builder.add(NineLifesItems.AMETHYST_STICK, "Аметистовая палочка")
        // Death messages
        builder.add("death.attack.amethysm", $$"%1$s не ожидалъ, что аметисты убиваютъ")
        builder.add("death.attack.amethysm.player", $$"%1$s не ожидалъ, что аметисты убиваютъ")
        builder.add("death.attack.charged_amethyst", $$"%1$s позналъ силу аметиста")
        builder.add("death.attack.charged_amethyst.player", $$"%1$s позналъ силу аметиста")
        builder.add("death.attack.unknown", $$"%1$s умеръ по неизвѣстной причинѣ")
        builder.add("death.attack.unknown.player", $$"%1$s умеръ по неизвѣстной причинѣ")
        // Enchantments
        builder.add("enchantment.nine_lifes.charge", "Зарядъ")
        // Advancements
        builder.addAdvancement("root", "Девять жизней!", "Начните своё путешествіе")
        builder.addAdvancement("try_sleep_without_shard", "Бѣсы въ головѣ", "По непонятной причинѣ вы не могли уснуть. Есть ли средство отъ сего?")
        builder.addAdvancement("slept_with_shard", "Утро вечера мудренѣе.. Опять", "Поспите, съѣвъ осколокъ аметиста")
        builder.addAdvancement("got_charged_shard", "Освященіе", "Получите заряженный осколокъ аметиста")
        builder.addAdvancement("got_life_with_shard", "Чудо божіе", "Получите жизнь съ помощью заряженнаго осколка аметиста")
        builder.addAdvancement("ate_64_charged_shards", "Запретный плодъ", "Съѣшьте 64 заряженныхъ осколка аметиста")
        builder.addAdvancement("got_amethyst_stick", "Я ЕСМЬ БОГЪ!", "Получите аметистовую палочку")
        builder.addAdvancement("almost_dead", "На волоскѣ", "Выживите съ половиною сердца и одной жизнію")
        builder.addAdvancement("hundred_days", "100 дней и ночей", "Проживите 100 дней и ночей")
        builder.addAdvancement("true_hundred_days", "Истинные 100 дней", "Проживите 100 дней съ одною жизнью")
        builder.addAdvancement("all_done", "Богатырь", "Выполните всѣ достиженія Nine Lifes")
        // Tags
        builder.add(NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE, "Взрываетъ плавильную печь")
        builder.add(NineLifesItemTags.CAUSE_FURNACE_EXPLODE, "Взрываетъ печь")
        builder.add(NineLifesItemTags.CAUSE_SMOKER_EXPLODE, "Взрываетъ коптильню")
        builder.add(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE, "Взрываетъ костёръ")
        builder.add(NineLifesItemTags.LIGHTNING_CHARGEABLE, "Заряжается электричествомъ")
        builder.add(NineLifesItemTags.Enchantable.CHARGE, "Способно быть заряженнымъ")
        builder.add(NineLifesItemTags.Enchantable.CHARGE_IN_TABLE, "Способно заряжаться въ чародѣйскомъ алтарѣ")
        builder.add(NineLifesDamageTypeTags.GIVES_LIFE, "Даётъ жизнь при смерти")
        builder.add(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE, "Молнія или огонь")
        // Chat messages
        builder.add("chat.message.join.lives", $$"Ваши жизни: %1$s.")
        builder.add("chat.message.join.lives.careful", $$"Ваши жизни: %1$s. Будьте начеку!")
        builder.add("chat.message.join.beta", "Вниманіе! Вы запустили бета-версію мода. Если вы обнаружите ошибку, то отправьте её по сему адресу:")
        // Commands
        builder.add("commands.nl.text.author", $$"Авторъ: %1$s")
        builder.add("commands.nl.text.version", $$"Версія: %1$s")
        builder.add("commands.nl.text.links", "Ссылки:")
        builder.add("commands.nl.reset.success", "Жизни успѣшно сброшены")
        builder.add("commands.nl.reset.player.success", $$"Жизни %1$s успѣшно сброшены")
        builder.add(
            "commands.nl.set.success",
            $$"Количество жизней успѣшно установлено на %1$s"
        )
        builder.add(
            "commands.nl.set.player.success",
            $$"Количество жизней %2$s успѣшно установлено на %1$s"
        )
        builder.add(
            "commands.nl.add.success",
            $$"Количество жизней успѣшно увеличено на %1$s"
        )
        builder.add(
            "commands.nl.add.success.neg",
            $$"Количество жизней успѣшно уменьшено на %1$s"
        )
        builder.add(
            "commands.nl.add.player.success",
            $$"Количество жизней %2$s успѣшно увеличено на %1$s"
        )
        builder.add(
            "commands.nl.add.player.success.neg",
            $$"Количество жизней %2$s успѣшно уменьшено на %1$s"
        )
        builder.add("commands.nl.revive.success", "Вы были возрождены")
        builder.add(
            "commands.nl.revive.player.success",
            $$"Игрокъ %1$s былъ возрождёнъ"
        )
        builder.add(
            "commands.nl.get.player.success",
            $$"Количество жизней %2$s: %1$s"
        )
        builder.add("commands.not_a_player", "Приказъ доступенъ только игрокамъ")
        // Entities
        builder.add(NineLifesEntities.WANDERING_ARMOR_STAND, "Бродячая стойка для брони")
        // Stats
        builder.addStat(NineLifesStats.USED_CHARGED, "Использовано заряженныхъ вещей")
        // Game rules
        builder.add("gamerule.category.nine_lifes.general", "Девять жизней")
        builder.addGameRule(
            "take_lifes",
            "Отнимать жизни",
            "Отнимать жизни умершихъ игроковъ. Вліяетъ только на отнятіе; заряженныя вещи продолжаютъ добавлять жизни."
        )
        builder.addGameRule(
            "take_lifes_in_overworld",
            "Отнимать жизни въ обычномъ мірѣ",
            "Отнимать жизни умершихъ игроковъ въ обычномъ мірѣ. Не имѣетъ значенія, если отнятіе жизней отключено."
        )
        builder.addGameRule(
            "take_lifes_in_nether",
            "Отнимать жизни въ Нѣдрѣ",
            "Отнимать жизни умершихъ игроковъ въ Нѣдрѣ. Не имѣетъ значенія, если отнятіе жизней отключено."
        )
        builder.addGameRule(
            "take_lifes_in_end",
            "Отнимать жизни въ Эндѣ",
            "Отнимать жизни умершихъ игроковъ въ Эндѣ. Не имѣетъ значенія, если отнятіе жизней отключено."
        )
        builder.addGameRule(
            "max_charged_items_at_a_time",
            "Наибольшее число заряженныхъ вещей за разъ",
            "Наибольшее число заряженныхъ вещей, получаемыхъ отъ одного удара молніи. Значеніе -1 означаетъ безконечность (отсутствіе предѣла)."
        )
        // Sounds
        builder.add(
            NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_HURT,
            "Бродячая стойка для брони ранена"
        )
        builder.add(
            NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_DEATH,
            "Бродячая стойка для брони пропадаетъ"
        )
        // Other
        builder.add("block.minecraft.bed.insomnia_effect", "Сейчасъ не получитсяъ уснуть")
        builder.add(deathScreenRemaining(1), "Послѣдній шансъ!")
        builder.add(deathScreenRemaining(2), "2 жизни осталось")
        builder.add(deathScreenRemaining(3), "3 жизни осталось")
        builder.add(deathScreenRemaining(4), "4 жизни осталось")
        builder.add(deathScreenRemaining(5), "5 жизней осталось")
        builder.add(deathScreenRemaining(6), "6 жизней осталось")
        builder.add(deathScreenRemaining(7), "7 жизней осталось")
        builder.add(deathScreenRemaining(8), "8 жизней осталось")
        // Config
        var tb = ConfigTranslationKeyBuilder.default()
        builder.add(tb.additional("title"), "Конфигъ Nine Lifes")
        /// General
        tb = tb.category()
        builder.add(tb, "Основныя")
        tb = tb.root()
        builder.addConfigOption(
            tb.option("join_message"),
            "Включить привѣтственное сообщеніе",
            "Показывать сообщеніе съ количествомъ жизней при входѣ въ салонъ"
        )
        builder.addConfigOption(
            tb.option("heartbeat"),
            "Включить эффектъ сердцебіенія",
            "При включеніи сердце будетъ пульсироватьъ"
        )
        builder.addConfigOption(
            tb.option("heart_position"),
            "Положеніе сердецъ",
            "Положеніе количества жизней на экранѣ"
        )
        builder.addConfigOption(
            tb.option("low_lifes_red_sky"),
            "Червонное небо при маломъ числѣ жизней",
            "При включеніи небо будетъ краснѣть при маломъ числѣ жизней"
        )
        builder.addConfigOption(
            tb.option("health_rendering"),
            "Отображеніе здоровья",
            "Управляетъ отображеніемъ здоровья игрока\nХардкоръ — всегда отображать хардкорныя сердца\nИстинный хардкоръ — только при одной жизні\nВаниль — ванильное повѣденіе"
        )
        builder.addConfigOption(
            tb.option("death_screen_remaining"),
            "Оставшіяся жизни на экранѣ смерти",
            "На экранѣ смерти будетъ отображаться количество оставшихсяъ жизней вмѣсто надписи «Вы умерли!»"
        )

        /// Charged items / amethysm
        tb = ConfigTranslationKeyBuilder.default().category("charged_amethysm")
        builder.addConfigOption(tb, "Аметизмъ / Заряженныя вещи")
        tb = tb.root()
        builder.addConfigOption(
            tb.option("enabled"),
            "Включить",
            "Полностью включаетъ или отключаетъ эффекты рядомъ съ игроками, у коихъ есть заряженныя вещи или аметизмъ. Удобно, когда нужно быстро отключить ихъ одной кнопкою."
        )
        tb = tb.group("charged")
        builder.addConfigOption(tb, "Заряженныя вещи")
        builder.addConfigOption(
            tb.option("self"),
            "Этотъ игрокъ",
            "Показывать эффектъ при наличіи въ инвентарѣ заряженныхъ вещей."
        )
        builder.addConfigOption(
            tb.option("players"),
            "Остальныя игроки",
            "Показывать эффектъ рядомъ съ игроками, у коихъ есть заряженныя вещи."
        )
        tb = tb.group("amethysm")
        builder.addConfigOption(tb, "Аметизмъ")
        builder.addConfigOption(
            tb.option("players"),
            "Остальныя игроки",
            "Показывать эффектъ рядомъ съ игроками, у коихъ есть аметизмъ."
        )
        builder.addConfigEnum<HeartPosition>(
            "Внизу слѣва", "Внизу по центру", "Внизу справа",
            "Вверху слѣва", "Вверху по центру", "Вверху справа"
        )
        builder.addConfigEnum<HealthRendering>(
            "Хардкоръ", "Истинный хардкоръ", "Ваниль"
        )
    }

}

private fun FabricLanguageProvider.TranslationBuilder.addPotion(name: String, potionTranslation: String,
                                                                splashPotionTranslation: String,
                                                                lingeringPotionTranslation: String,
                                                                tippedArrowTranslation: String) {
    this.add("item.minecraft.potion.effect.$name", potionTranslation)
    this.add("item.minecraft.splash_potion.effect.$name", splashPotionTranslation)
    this.add("item.minecraft.lingering_potion.effect.$name", lingeringPotionTranslation)
    this.add("item.minecraft.tipped_arrow.effect.$name", tippedArrowTranslation)
}
private fun FabricLanguageProvider.TranslationBuilder.addPotions(names: List<String>, potionTranslation: String,
                                                                 splashPotionTranslation: String,
                                                                 lingeringPotionTranslation: String,
                                                                 tippedArrowTranslation: String) {
    for (name in names) {
        addPotion(name, potionTranslation, splashPotionTranslation, lingeringPotionTranslation, tippedArrowTranslation)
    }
}
private fun FabricLanguageProvider.TranslationBuilder.addAdvancement(name: String, nameTranslation: String, descriptionTranslation: String) {
    this.add(advancement(name), nameTranslation)
    this.add(advancementDescription(name), descriptionTranslation)
}
private fun FabricLanguageProvider.TranslationBuilder.addStat(statId: Identifier, translation: String) {
    this.add(statId.toLanguageKey("stat"), translation)
}
private fun FabricLanguageProvider.TranslationBuilder.add(translationKeyBuilder: ConfigTranslationKeyBuilder, translation: String) {
    this.add(translationKeyBuilder.build(), translation)
}
private fun FabricLanguageProvider.TranslationBuilder.addConfigOption(translation: ConfigTranslationKeyBuilder, nameTranslation: String, descriptionTranslation: String? = null) {
    this.add(ConfigTranslationKeyBuilder.default().update(translation).build(), nameTranslation)
    descriptionTranslation?.let { this.add(ConfigTranslationKeyBuilder.default().update(translation).description().build(), descriptionTranslation) }
}
private inline fun <reified T : Enum<T>> FabricLanguageProvider.TranslationBuilder.addConfigEnum(vararg translations: String) {
    enumEntries<T>().zip(translations).forEach { (entry, translation) ->
        val tb = ConfigTranslationKeyBuilder.default().additional("enum.${T::class.java.name.split(".").last()}.${entry.name}")
        this.add(tb.build(), translation)
    }
}

private fun FabricLanguageProvider.TranslationBuilder.addGameRule(id: String, name: String, description: String) {
    this.add("gamerule.$MOD_ID.$id", name)
    this.add("gamerule.$MOD_ID.$id.description", description)
}
