package com.serezka.telegram4j.session.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor @Getter
@Log4j2
public class MenuSessionConfiguration {
    final Page.Generator root;
    final Map<String, Page.Generator> pages = new HashMap<>();

    public MenuSessionConfiguration addPage(String alias, Page.Generator page) {
        if (pages.containsKey(alias)) log.warn("Page with alias '{}' already exists. {}", alias, page);
        pages.put(alias, page);
        return this;
    }

    public static MenuSessionConfiguration create(Page.Generator root) {
        return new MenuSessionConfiguration(root);
    }
}
