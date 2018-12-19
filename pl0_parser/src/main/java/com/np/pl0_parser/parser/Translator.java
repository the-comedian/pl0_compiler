package com.np.pl0_parser.parser;

import com.np.pl0_parser.constants.Constants;
import com.np.pl0_parser.pojo.ParserException;

import java.util.List;

public class Translator extends PLBase {

    private StringBuilder translatedCode = null;

    public String translate(List<String> symbols) throws ParserException{
        this.symbols = symbols;
        return this.makeTranslate();
    }

    private String makeTranslate() throws ParserException {
        translatedCode = new StringBuilder();
        translatedCode.append("var readline = require('readline-sync');").append(System.getProperty("line.separator"));
        nextSymbol();
        processBlock();
        return translatedCode.toString();
    }


    private void processBlock() throws ParserException {
        if (acceptSymbol(Constants.CONST)) {
            processConstants();
        }
        while (acceptSymbol(Constants.VAR_INT)) {
            processIntVariable();
        }
        while (acceptSymbol(Constants.VAR_STRING)) {
            processStringVariable();
        }
        while (acceptSymbol(Constants.PROCEDURE)) {
            processProcedure();
        }
        processStatement();
    }

    /**
     * Обработка констант
     */
    private void processConstants() throws ParserException {
        translatedCode.append("const ");
        do {
            String ident = new String(currentSymbol);
            expectSymbol(Constants.IDENT_REGEX);
            expectSymbol(Constants.EQUAL);
            String value = new String(currentSymbol);
            expectSymbol(Constants.NUMBER_REGEX);
            translatedCode.append(ident).append(" = ").append(value).append(",");
        } while (acceptSymbol(Constants.COMMA));
        translatedCode.setLength(translatedCode.length() - 1);
        expectSymbol(Constants.SEMICOLON);
        translatedCode.append(";").append(System.getProperty("line.separator"));
    }

    /**
     * Обработка строковых переменных
     */
    private void processStringVariable() throws ParserException {
        translatedCode.append("var ");
        do {
            String ident = new String(currentSymbol);
            expectSymbol(Constants.IDENT_REGEX);
            translatedCode.append(ident).append(",");
        } while (acceptSymbol(Constants.COMMA));
        translatedCode.setLength(translatedCode.length() - 1);
        expectSymbol(Constants.SEMICOLON);
        translatedCode.append(";").append(System.getProperty("line.separator"));
    }

    /**
     * Обработка переменных целочисленного типа
     */
    private void processIntVariable() throws ParserException {
        translatedCode.append("var ");
        do {
            String ident = new String(currentSymbol);
            expectSymbol(Constants.IDENT_REGEX);
            translatedCode.append(ident).append(",");
        } while (acceptSymbol(Constants.COMMA));
        translatedCode.setLength(translatedCode.length() - 1);
        expectSymbol(Constants.SEMICOLON);
        translatedCode.append(";").append(System.getProperty("line.separator"));
    }

    /**
     * Обработка процедуры
     *
     * @throws ParserException
     */
    private void processProcedure() throws ParserException {
        translatedCode.append("function ");
        String ident = new String(currentSymbol);
        expectSymbol(Constants.IDENT_REGEX);
        translatedCode.append(ident).append("() {").append(System.getProperty("line.separator"));
        expectSymbol(Constants.SEMICOLON);
        processBlock();
        expectSymbol(Constants.SEMICOLON);
        translatedCode.append("}");
    }

    private void processStatement() throws ParserException {
        String currentIdent = new String(currentSymbol);
        if (acceptSymbol(Constants.CALL)) {
            String ident = new String(currentSymbol);
            expectSymbol(Constants.IDENT_REGEX);
            translatedCode.append(ident).append("();");
        } else if (acceptSymbol(Constants.BEGIN)) {
            translatedCode.append("{").append(System.getProperty("line.separator"));
            do {
                processStatement();
            } while (acceptSymbol(Constants.SEMICOLON));
            expectSymbol(Constants.END);
            translatedCode.append("}").append(System.getProperty("line.separator"));;
        } else if (acceptSymbol(Constants.IF)) {
            translatedCode.append("if (");
            processCondition();
            translatedCode.append(")");
            expectSymbol(Constants.THEN);
            processStatement();
            translatedCode.append(System.getProperty("line.separator"));
        } else if (acceptSymbol(Constants.WHILE)) {
            translatedCode.append("while (");
            processCondition();
            translatedCode.append(")");
            expectSymbol(Constants.DO);
            processStatement();
            translatedCode.append(System.getProperty("line.separator"));
        } else if (acceptSymbol(Constants.IN)) {
            translatedCode.append(currentSymbol).append(" = +readline.question()").append(System.getProperty("line.separator"));
            processIdent();
        } else if (acceptSymbol(Constants.OUT)) {
            translatedCode.append("console.log(");
            processExpression();
            translatedCode.append(");").append(System.getProperty("line.separator"));
        } else if (acceptSymbol(Constants.IDENT_REGEX)) {
            translatedCode.append(currentIdent).append(" = ");
            expectSymbol(Constants.ASSIGN);
            processExpression();
            translatedCode.append(System.getProperty("line.separator"));
        } else {
            throw new ParserException("Syntax error");
        }
    }

    private void processCondition() throws ParserException {
        if (acceptSymbol(Constants.ODD)) {
            processExpression();
        } else {
            processExpression();
            if (this.currentSymbol.equals(Constants.EQUAL) || this.currentSymbol.equals(Constants.NOT_EQUAL)
                    || this.currentSymbol.equals(Constants.LESS) || this.currentSymbol.equals(Constants.GREATER)) {
                if (currentSymbol.equals(Constants.EQUAL)) {
                    currentSymbol = "==";
                }
                if (currentSymbol.equals(Constants.NOT_EQUAL)) {
                    currentSymbol = "!=";
                }
                translatedCode.append(currentSymbol);
                nextSymbol();
                processExpression();
                translatedCode.append(System.getProperty("line.separator"));
            } else {
                throw new ParserException("invalid operator");
            }
        }
    }

    private boolean processIdent() {
        if (this.currentSymbol.matches(Constants.IDENT_REGEX)) {
            nextSymbol();
            return true;
        }
        return false;
    }

    private void processFactor() throws ParserException {
        String currentOperand = new String(currentSymbol);
        if (processIdent()) {
            translatedCode.append(currentOperand);
        } else if (acceptSymbol(Constants.NUMBER_REGEX)) {
            translatedCode.append(currentOperand);
        } else if (acceptSymbol(Constants.STRING_REGEX)) {
            translatedCode.append(currentOperand);
        } else if (acceptSymbol(Constants.LPAREN)) {
            translatedCode.append("(");
            processExpression();
            expectSymbol(Constants.RPAREN);
            translatedCode.append(")");
        } else {
            throw new ParserException("syntax error");
        }
    }

    private void processTerm() throws ParserException {
        processFactor();
        while (this.currentSymbol.equals(Constants.TIMES) || this.currentSymbol.equals(Constants.SLASH)) {
            translatedCode.append(currentSymbol);
            nextSymbol();
            processFactor();
        }
    }

    private void processExpression() throws ParserException {
        if (this.currentSymbol.equals(Constants.PLUS) || this.currentSymbol.equals(Constants.MINUS)) {
            translatedCode.append(currentSymbol);
            nextSymbol();
        }
        processTerm();
        while (this.currentSymbol.equals(Constants.PLUS) || this.currentSymbol.equals(Constants.MINUS)) {
            translatedCode.append(currentSymbol);
            nextSymbol();
            processTerm();
        }
    }

}
