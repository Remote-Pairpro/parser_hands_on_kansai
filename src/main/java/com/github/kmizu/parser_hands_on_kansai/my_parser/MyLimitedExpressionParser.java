package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.AbstractLimitedExpressionParser;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.LimitedExpressionNode;

public class MyLimitedExpressionParser extends AbstractLimitedExpressionParser {

    private String input;
    private int position;

    @Override
    public LimitedExpressionNode parse(String input) {
        this.input = input;
        this.position = 0;
        LimitedExpressionNode result = expression();
        if (position != input.length())
            throw new ParseFailure("unconsumed input remains: " + input.substring(position));
        return result;
    }

    private String accept(char ch) {
        if (position < input.length() && input.charAt(position) == ch) {
            position++;
            return ((Character) ch).toString();
        }
        throw new ParseFailure("current position is over range or current character is not " + ch);
    }

    private char acceptRange(char from, char to) {
        if (position < input.length()) {
            char ch = input.charAt(position);
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

    private LimitedExpressionNode expression() {
        int lhs = integer();
        int current = position;
        try {
            accept('+');
            return new LimitedExpressionNode.Addition(lhs, integer());
        } catch (ParseFailure e1) {
            position = current;
        }

        try {
            accept('-');
            return new LimitedExpressionNode.Subtraction(lhs, integer());
        } catch (ParseFailure e2) {
            position = current;
        }

        try {
            accept('*');
            return new LimitedExpressionNode.Multiplication(lhs, integer());
        } catch (ParseFailure e3) {
            position = current;
        }

        try {
            accept('/');
            return new LimitedExpressionNode.Division(lhs, integer());
        } catch (ParseFailure e4) {
            return new LimitedExpressionNode.ValueNode(lhs);
        }
    }

    private int integer() {
        int result = (acceptRange('0', '9') - '0');
        if (result == 0) {
            if (position >= input.length()) return result;
            char ch = input.charAt(position);
            if ('0' <= ch && ch <= '9')
                throw new ParseFailure("if number starts with 0, it cannot be follow by any digit");
            return result;
        }
        while (true) {
            try {
                result = result * 10 + (acceptRange('0', '9') - '0');
            } catch (ParseFailure e) {
                return result;
            }
        }
    }

}
