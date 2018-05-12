package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.expression.AbstractExpressionParser;
import com.github.kmizu.parser_hands_on_kansai.expression.ExpressionNode;

public class MyExpressionParser extends AbstractExpressionParser {

    String input;

    @Override
    public ExpressionNode parse(String input) {
        this.input = input;
        this.position = 0;
        return expression();
    }

    public ExpressionNode expression() {
        return additive();
    }

    public ExpressionNode additive() {
        ExpressionNode result = multitive();
        while (true) {
            save();
            try {
                accept('+');
                result = new ExpressionNode.Addition(result, multitive());
            } catch (ParseFailure e1) {
                restore();
                try {
                    accept('-');
                    result = new ExpressionNode.Subtraction(result, multitive());
                } catch (ParseFailure e2) {
                    return result;
                }
            }
        }
    }

    private ExpressionNode multitive() {
        ExpressionNode result = primary();
        while (true) {
            save();
            try {
                accept('*');
                result = new ExpressionNode.Multiplication(result, primary());
            } catch (ParseFailure e1) {
                restore();
                try {
                    // '/' primary
                    accept('/');
                    result = new ExpressionNode.Division(result, primary());
                } catch (ParseFailure e2) {
                    return result;
                }
            }
        }
    }

    private ExpressionNode primary() {
        try {
            save();
            accept('(');
            ExpressionNode result = expression();
            accept(')');
            return result;
        } catch (ParseFailure e2) {
            restore();
            return integer();
        }
    }

    private ExpressionNode integer() {
        int result = (acceptRange('0', '9') - '0');
        if (result == 0) {
            if (position >= input.length()) return new ExpressionNode.ValueNode(result);

            char ch = input.charAt(position);
            if ('0' <= ch && ch <= '9')
                throw new ParseFailure("if number starts with 0, it cannot be follow by any digit");
            return new ExpressionNode.ValueNode(result);
        }
        while (true) {
            try {
                result = result * 10 + (acceptRange('0', '9') - '0');
            } catch (ParseFailure e) {
                return new ExpressionNode.ValueNode(result);
            }
        }
    }

    public char acceptRange(char from, char to) {
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

    public void accept(char ch) {
        if (position < input.length() && input.charAt(position) == ch) {
            position++;
            return;
        }
        throw new ParseFailure("current position is over range or current character is not " + ch);
    }

}
