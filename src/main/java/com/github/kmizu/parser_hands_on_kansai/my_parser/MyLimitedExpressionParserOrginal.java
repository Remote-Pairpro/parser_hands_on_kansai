package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.AbstractLimitedExpressionParser;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.LimitedExpressionNode;

public class MyLimitedExpressionParserOrginal extends AbstractLimitedExpressionParser {

    @Override
    public LimitedExpressionNode parse(String input) {
        char operator = ' ';
        int operatorPosition = -1;
        for (int i = 0; i < input.length(); i++) {
            char oneChar = input.charAt(i);
            if ("+-*/".contains(String.valueOf(oneChar))) {
                operatorPosition = i;
                operator = oneChar;
                break;
            }
        }
        if (operatorPosition < 0) return LimitedExpressionNode.v(parseInteger(input));
        String firstPart = input.substring(0, operatorPosition);
        String secondPart = input.substring(operatorPosition + 1);
        int firstDigit = parseInteger(firstPart);
        int secondDigit = parseInteger(secondPart);
        if (operator == '-') return LimitedExpressionNode.sub(firstDigit, secondDigit);
        if (operator == '*') return LimitedExpressionNode.mul(firstDigit, secondDigit);
        if (operator == '/') return LimitedExpressionNode.div(firstDigit, secondDigit);
        return LimitedExpressionNode.add(firstDigit, secondDigit);
    }

    private Integer parseInteger(String input) {
        int allCharCount = input.length();
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            result = result * 10 + parseOneChar(input.charAt(i), i, allCharCount);
        }
        return result;
    }

    private int parseOneChar(char oneChar, int index, int allCharCount) {
        if (index == 0 && oneChar == '0' && allCharCount > 1) throw new ParseFailure("1文字目が0は数字文字列ではありません。");
        if (!('0' <= oneChar && oneChar <= '9')) throw new ParseFailure((index + 1) + "文字目が数字文字列ではありません。");
        return oneChar - '0';
    }

}
