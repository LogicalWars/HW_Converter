package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) throws IllegalArgumentException {
        if (color < 0 || color > 255) throw new IllegalArgumentException("Invalid color: " + color);
        return symbols[(int) Math.round((color / 255.0) * (symbols.length - 1))];
    }
}
