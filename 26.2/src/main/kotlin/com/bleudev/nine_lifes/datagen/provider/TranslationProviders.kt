package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.NineLifesStats
import com.bleudev.nine_lifes.client.config.HealthRendering
import com.bleudev.nine_lifes.client.config.HeartPosition
import com.bleudev.nine_lifes.client.config.TranslatableConfigEnumProvider
import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.util.*
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import java.util.concurrent.CompletableFuture

class NineLifesDefaultTranslationProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, registriesFuture) {
    override fun generateTranslations(
        registryLookup: HolderLookup.Provider,
        builder: TranslationBuilder
    ) {
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
        builder.add("commands.nl.reset.success", "Lives were successfully reset")
        builder.add("commands.nl.reset.not_a_player", "The command is available only to players")
        builder.add("commands.nl.reset.player.success", $$"%1$s's lives have been successfully reset")
        builder.add("commands.nl.set.success", $$"The number of lives was successfully set to %1$s")
        builder.add("commands.nl.set.not_a_player", "The command is available only to players")
        builder.add("commands.nl.set.player.success", $$"%2$s's life count was successfully set to %1$s")
        builder.add("commands.nl.revive.success", "You were revived")
        builder.add("commands.nl.revive.not_a_player", "The command is available only to players")
        builder.add("commands.nl.revive.player.success", $$"%1$s was revived")
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
        builder.add(config("title"), "Nine lifes config")
        builder.add(config("category.general"), "General")
        builder.addConfigOption(rootOption("join_message"),
            "Enable join message", "Display message with lifes count on join server")
        builder.addConfigOption(rootOption("heartbeat"),
            "Enable heartbeat effect", "When true lifes count will beat")
        builder.addConfigOption(rootOption("heart_position"),
            "Heart position", "Location of lifes count on the screen")
        builder.addConfigOption(rootOption("low_lifes_red_sky"),
            "Red sky when there are few lifes", "When true sky will become red when lifes count is low")
        builder.addConfigOption(rootOption("health_rendering"),
            "Health rendering", "Controls player health rendering\nHardcore - Always render hardcore hearts\nTrue hardcore -  Only if you have one life\nVanilla - Vanilla behavior")
        builder.addConfigOption(rootOption("death_screen_remaining"),
            "Remaining lifes on the death screen", "The death screen will now display the number of lifes remaining instead of the \"You Died!\" message")
        builder.addConfigEnum(HeartPosition,
            "Bottom left", "Bottom center", "Bottom right",
            "Top left", "Top center", "Top right"
        )
        builder.addConfigEnum(HealthRendering,
            "Hardcore", "True hardcore", "Vanilla"
        )
    }
}

class NineLifesRussianTranslationProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output, "ru_ru", registriesFuture) {
    override fun generateTranslations(registryLookup: HolderLookup.Provider, builder: TranslationBuilder) {
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
        builder.add("commands.nl.reset.not_a_player", "Команда доступна только для игроков")
        builder.add("commands.nl.reset.player.success", $$"Жизни %1$s были успешно сброшены")
        builder.add("commands.nl.set.success", $$"Количество жизней было успешно установлено на %1$s")
        builder.add("commands.nl.set.not_a_player", "Команда доступна только для игроков")
        builder.add("commands.nl.set.player.success", $$"Количество жизней %2$s было успешно установлено на %1$s")
        builder.add("commands.nl.revive.success", "Вы были возрождены")
        builder.add("commands.nl.revive.not_a_player", "Команда доступна только для игроков")
        builder.add("commands.nl.revive.player.success", $$"%1$s был возрождён")
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
        builder.add(config("title"), "Конфиг Nine lifes")
        builder.add(config("category.general"), "Главные")
        builder.addConfigOption(rootOption("join_message"),
            "Включить приветственное сообщение", "Показывать сообщение с количеством жизней при заходе на сервер")
        builder.addConfigOption(rootOption("heartbeat"),
            "Включить эффект сербцебиения", "Когда включено сердце будет пульсировать")
        builder.addConfigOption(rootOption("heart_position"),
            "Расположение сердца", "Расположение количества жизней на экране")
        builder.addConfigOption(rootOption("low_lifes_red_sky"),
            "Красное небо когда мало жизней", "Когда включено небо будет краснеть при низком количестве жизней")
        builder.addConfigOption(rootOption("health_rendering"),
            "Рендеринг здоровья", "Контролирует рендеринг здоровья игрока\nХардкор - всегда рендерить хардкорные сердца\nИстинный хардкор - Только если у вас одна жизнь\nВанила - Ванильное поведение")
        builder.addConfigOption(rootOption("death_screen_remaining"),
            "Оставшиеся жизни на экране смерти", "На экране смерти будет отображаться количество оставшихся жизней вместо надписи \"Вы умерли!\"")
        builder.addConfigEnum(HeartPosition,
            "Снизу слева", "Снизу в центре", "Снизу справа",
            "Сверху слева", "Сверху в центре", "Сверху справа"
        )
        builder.addConfigEnum(HealthRendering,
            "Хардкор", "Истинный хардкор", "Ванила"
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
private fun FabricLanguageProvider.TranslationBuilder.addConfigOption(name: String, nameTranslation: String, descriptionTranslation: String? = null) {
    this.add(config(name), nameTranslation)
    descriptionTranslation?.let { this.add(config("$name.description"), descriptionTranslation) }
}
private fun FabricLanguageProvider.TranslationBuilder.addConfigEnum(enum: TranslatableConfigEnumProvider, vararg translations: String) {
    enum.names.zip(translations).forEach { (key, translation) ->
        this.add(key, translation)
    }
}

private fun FabricLanguageProvider.TranslationBuilder.addGameRule(id: String, name: String, description: String) {
    this.add("gamerule.nine_lifes.$id", name)
    this.add("gamerule.nine_lifes.$id.description", description)
}
