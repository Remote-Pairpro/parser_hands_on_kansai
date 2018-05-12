package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.digit.AbstractDigitParser;

public class MyIntegerParser extends AbstractDigitParser {

    @Override
    public Integer parse(String input) {
        int allCharCount = input.length();
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            result = result * 10 + parseOneChar(input.charAt(i), i, allCharCount);
        }
        return result;
    }

    private int parseOneChar(char oneChar, int index, int allCharCount) {
        if (index == 0 && oneChar == '0' && allCharCount > 0) throw new ParseFailure("1文字目が0は数字文字列ではありません。");
        if (!('0' <= oneChar && oneChar <= '9')) throw new ParseFailure((index + 1) + "文字目が数字文字列ではありません。");
        return oneChar - '0';
    }

}
