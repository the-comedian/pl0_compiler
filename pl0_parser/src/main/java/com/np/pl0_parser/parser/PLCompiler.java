/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.pl0_parser.parser;

import com.np.pl0_parser.constants.Constants;
import com.np.pl0_parser.pojo.ParserException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author npetrov
 */
public class PLCompiler extends PLBase {

    private Map<String, Integer> constants = new HashMap<>();
    private Map<String, Integer> intVariables = new HashMap<>();
    private Map<String, String> stringVariables = new HashMap<>();

    public void compile(List<String> symbols) throws ParserException, ScriptException {
        this.symbols = symbols;
        nextSymbol();
        processBlock();
    }

    private void processBlock() throws ParserException, ScriptException {
        if (acceptSymbol(Constants.CONST)) {
            processConstants();
        }
        while (acceptSymbol(Constants.VAR_INT)) {
            processIntVariable();
        }
        while (acceptSymbol(Constants.VAR_STRING)) {
            processStringVariable();
        }
        processStatement();
    }

    private boolean processIdent() {
        if (this.currentSymbol.matches(Constants.IDENT_REGEX)) {
            nextSymbol();
            return true;
        }
        return false;
    }

    private void processStatement() throws ParserException, ScriptException {
        String inputSymbol = new String(currentSymbol);
        if (acceptSymbol(Constants.BEGIN)) {
            do {
                processStatement();
            } while (acceptSymbol(Constants.SEMICOLON));
            expectSymbol(Constants.END);
        } else if (acceptSymbol(Constants.IDENT_REGEX)) {
            String ident = inputSymbol;
            processIdent();
            expectSymbol(Constants.ASSIGN);
            String expressionResult = processExpression();
            if (intVariables.containsKey(ident)) {
                intVariables.put(ident, Integer.parseInt(expressionResult));
            } else if (stringVariables.containsKey(ident)) {
                stringVariables.put(ident, expressionResult);
            } else {
                throw new ParserException("unable to assign value to: " + ident);
            }
        }
    }

    private String processExpression() throws ParserException, ScriptException {
        StringBuilder expression = new StringBuilder();
        String currentOperand = currentSymbol;
        String returnType = "";
        while (!currentSymbol.equals(Constants.SEMICOLON) && !currentSymbol.equals(Constants.END) && !currentSymbol.equals(Constants.POINT)) {
            if (!currentOperand.matches(Constants.NUMBER_REGEX) && !Constants.DELIMITERS.contains(currentOperand)) {
                if (currentOperand.matches(Constants.IDENT_REGEX)) {
                    if (stringVariables.containsKey(currentOperand)) {
                        if (returnType.isEmpty()) {
                            returnType = "string";
                        }
                        if (stringVariables.get(currentOperand) != null) {
                            expression.append(stringVariables.get(currentOperand)).append(" ");
                        } else {
                            throw new ParserException("variable is null: " + currentOperand);
                        }
                    } else if (intVariables.containsKey(currentOperand) || constants.containsKey(currentOperand)) {
                        if (returnType.isEmpty()) {
                            returnType = "int";
                        }
                        if (intVariables.get(currentOperand) != null) {
                            expression.append(intVariables.get(currentOperand)).append(" ");
                        } else if (constants.get(currentOperand) != null) {
                            expression.append(constants.get(currentOperand)).append(" ");
                        } else {
                            throw new ParserException("variable is null: " + currentOperand);
                        }
                    } else {
                        throw new ParserException("unknown variable: " + currentOperand);
                    }
                }
            } else {
                expression.append(currentOperand).append(" ");
            }
            currentOperand = nextSymbol();
        }
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String countResult = engine.eval(expression.toString()).toString();
        if (returnType.equals("int")) {
            countResult = Math.round(Double.parseDouble(countResult)) + "";
        }
        return countResult;
    }

    private void processConstants() throws ParserException {
        do {
            String constantName = currentSymbol;
            expectSymbol(Constants.IDENT_REGEX);
            expectSymbol(Constants.EQUAL);
            Integer constantValue = Integer.parseInt(currentSymbol);
            expectSymbol(Constants.NUMBER_REGEX);
            if (constants.get(constantName) != null) {
                throw new ParserException("duplicate constant: " + currentSymbol);
            }
            constants.put(constantName, constantValue);
        } while (acceptSymbol(Constants.COMMA));
        expectSymbol(Constants.SEMICOLON);
    }

    private void processIntVariable() throws ParserException {
        do {
            String variableName = currentSymbol;
            if (intVariables.containsKey(variableName) || stringVariables.containsKey(currentSymbol)) {
                throw new ParserException("duplicate variable: " + currentSymbol);
            }
            intVariables.put(variableName, null);
            expectSymbol(Constants.IDENT_REGEX);
        } while (acceptSymbol(Constants.COMMA));
        expectSymbol(Constants.SEMICOLON);
    }

    private void processStringVariable() throws ParserException {
        do {
            String variableName = currentSymbol;
            if (intVariables.containsKey(variableName) || stringVariables.containsKey(currentSymbol)) {
                throw new ParserException("duplicate variable: " + currentSymbol);
            }
            stringVariables.put(variableName, null);
            expectSymbol(Constants.IDENT_REGEX);
        } while (acceptSymbol(Constants.COMMA));
        expectSymbol(Constants.SEMICOLON);
    }

}
