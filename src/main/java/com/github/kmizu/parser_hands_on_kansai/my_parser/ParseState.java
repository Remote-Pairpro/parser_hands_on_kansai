package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;

public class ParseState {

    private final String text;
    private int position;

    public boolean accept(char hitTestChar) {
        if (position < text.length() && text.charAt(position) == hitTestChar) {
            position++;
            return true;
        }
        return false;
    }

    public char acceptRange(char from, char to) {
        if (position < text.length()) {
            char ch = text.charAt(position);
            if (from <= ch && ch <= to) {
                position++;
                return ch;
            } else {
                throw new ParseFailure("current character is out of range: [" + from + "..." + to + "]");
            }
        } else {
            throw new ParseFailure("unexpected EOF");
        }
    }

    public boolean consumed() {
        return position >= text.length();
    }

    public char currentCharacter() {
        return text.charAt(position);
    }

    public boolean pointerInRange() {
        return position < text.length();
    }

    public ParseState(String text) {
        this.text = text;
    }

}
