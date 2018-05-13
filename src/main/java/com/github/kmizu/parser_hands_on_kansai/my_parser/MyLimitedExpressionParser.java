package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.AbstractLimitedExpressionParser;
import com.github.kmizu.parser_hands_on_kansai.limited_expression.LimitedExpressionNode;

public class MyLimitedExpressionParser extends AbstractLimitedExpressionParser {

    ParseState state;

    @Override
    public LimitedExpressionNode parse(String input) {
        state = new ParseState(input);
        LimitedExpressionNode result = expression();
        if (!state.consumed())
            throw new ParseFailure("unconsumed input remains: " + input.substring(position));
        return result;
    }

    private LimitedExpressionNode expression() {
        int lhs = integer();
        if (state.accept('+')) return new LimitedExpressionNode.Addition(lhs, integer());
        if (state.accept('-')) return new LimitedExpressionNode.Subtraction(lhs, integer());
        if (state.accept('*')) return new LimitedExpressionNode.Multiplication(lhs, integer());
        if (state.accept('/')) return new LimitedExpressionNode.Division(lhs, integer());
        return new LimitedExpressionNode.ValueNode(lhs);
    }

    private int integer() {
        int result = state.acceptRange('0', '9') - '0';
        if (result == 0) {
            if (state.consumed()) return result;
            char ch = state.currentCharacter();
            if ('0' <= ch && ch <= '9')
                throw new ParseFailure("if number starts with 0, it cannot be follow by any digit");
            return result;
        }
        while (true) {
            try {
                result = result * 10 + (state.acceptRange('0', '9') - '0');
            } catch (ParseFailure e) {
                return result;
            }
        }
    }

}
