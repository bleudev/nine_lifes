package com.bleudev.nine_lifes;

import de.marhali.json5.Json5;
import de.marhali.json5.Json5Element;
import de.marhali.json5.Json5Object;
import de.marhali.json5.config.DigitSeparatorStrategy;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class ServerDataStorage {
    private static final Json5 json5;
    private static final Path configPath;
    private static final String livesJsonPath;

    private static <T, V> V return_non_null(T value, Function<T, V> func, V def) {
        if (value == null) return def;
        return func.apply(value);
    }

    private static <V> HashMap<String, V> parse_map(Json5Element json_map, Function<Json5Element, V> func) {
        HashMap<String, V> res = new HashMap<>();
        json_map.getAsJson5Object().asMap().forEach((k, v) -> res.put(k, func.apply(v)));
        return res;
    }

    private static HashMap<String, Integer> lives;

    private static void save() {
        Json5Object json = new Json5Object();

        var lives_object = new Json5Object();
        lives.forEach(lives_object::addProperty);
        json.add(livesJsonPath, lives_object);

        try { Files.writeString(configPath, json5.serialize(json)); }
        catch (IOException e) { System.out.println("Can't save storage: " + e.getMessage()); }}
    private static void load() {
        try {
            if (Files.exists(configPath)) {
                Json5Object json  = json5.parse(Files.readString(configPath)).getAsJson5Object();

                lives = return_non_null(json.get(livesJsonPath), el -> parse_map(el, Json5Element::getAsInt), new HashMap<>());
            } else {
                lives = new HashMap<>();
                save();
            }
        } catch (IOException e) { System.out.println("Can't load storage: " + e.getMessage()); }
    }

    public static boolean contains_lives_of(String nick) {
        load();
        return lives.containsKey(nick);
    }

    public static int get_lives_of(String nick) {
        load();
        return Objects.requireNonNullElse(lives.get(nick), 9);
    }

    public static void set_lives_of(String nick, int new_lives) {
        load();
        lives.put(nick, new_lives);
        save();
    }

    static {
        json5 = Json5.builder(builder -> builder
            .quoteless()
            .quoteSingle()
            .trailingComma()
            .writeComments()
            .parseComments()
            .prettyPrinting()
            .digitSeparatorStrategy(DigitSeparatorStrategy.JAVA_STYLE)
            .build());
        configPath = FabricLoader.getInstance().getConfigDir().resolve("nl_storage.json5");
        livesJsonPath = "lives";
    }
}
