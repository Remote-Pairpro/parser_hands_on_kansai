package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.digit.AbstractDigitParser;

public class MyDigitParser extends AbstractDigitParser {
    @Override
    public Integer parse(String input) {
        if (input.length() != 1) throw new ParseFailure("文字長は１でないとなりません。");
        char oneCharacter = input.charAt(0);
        if (!('0' <= oneCharacter && oneCharacter <= '9')) throw new ParseFailure("先頭文字が数字文字列ではありません。");
        return oneCharacter - '0';
    }
}
