package com.bleudev.nine_lifes.util;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.net.URI;

public class TextUtils {
    public static MutableText link(MutableText text, String uri) {
        return text.setStyle(text.getStyle().withClickEvent(new ClickEvent.OpenUrl(URI.create(uri))));
    }
    public static MutableText link(String uri) {
        return literal_link(uri, uri);
    }

    public static MutableText literal_link(String string, String uri) {
        return link(Text.literal(string), uri);
    }
}
