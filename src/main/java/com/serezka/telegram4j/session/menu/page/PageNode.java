package com.serezka.telegram4j.session.menu.page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class PageNode {
    Page.Generator root, self;
    List<PageNode> child = new ArrayList<>();

    public static PageNode of(Page.Generator root, Page.Generator self) {
        return new PageNode(root, self);
    }

    public void addChild(Page.Generator page) {
        child.add(PageNode.of(this.self, page));
    }
}
