package com.serezka.telegram4j.session.menu.page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PageTree {
    final PageNode master;
    @Setter PageNode current;

    public PageTree(PageNode master) {
        this.master = master;
        this.current = this.master;
    }
}
