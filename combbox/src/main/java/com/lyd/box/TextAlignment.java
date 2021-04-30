package com.lyd.box;

public  enum TextAlignment {
    START(0),
    END(1),
    CENTER(2);

    private final int id;

    TextAlignment(int id) {
        this.id = id;
    }

    static TextAlignment fromId(int id) {
        for (TextAlignment value : values()) {
            if (value.id == id) return value;
        }
        return CENTER;
    }
}
