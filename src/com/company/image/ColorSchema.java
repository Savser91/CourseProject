package com.company.image;

public class ColorSchema implements TextColorSchema{
    @Override
    public char convert(int color) {
        return (color > 128) ? ((color < 160) ? '%' : (color < 192) ? '@' : (color < 224) ? '$' : '#') :
                ((color > 96) ? '*' : (color > 64) ? '+' : (color > 32) ? '-' : '`');
    }
}
