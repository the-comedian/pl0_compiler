/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.pl0_parser.parser;

import com.np.pl0_parser.constants.Constants;
import com.np.pl0_parser.pojo.ParserException;
import java.util.List;

/**
 *
 * @author npetrov
 */
public class PLBase {
    
    protected List<String> symbols = null;
    protected int symbolId = 0;
    protected String currentSymbol = null;
    
    
    /**
     * Получение следующей лексемы
     *
     * @return
     */
    protected String nextSymbol() {
        this.currentSymbol = this.symbols.get(symbolId);
        symbolId++;
        return this.currentSymbol;
    }

    /**
     * Принимаемый символ
     *
     * @param symbol
     * @return
     */
    protected boolean acceptSymbol(String symbol) {
        if (symbol.equals(Constants.IDENT_REGEX)) {
            if (this.currentSymbol.matches(symbol)) {
                nextSymbol();
                return true;
            }
        }
        if (symbol.equals(Constants.NUMBER_REGEX)) {
             if (this.currentSymbol.matches(symbol)) {
                nextSymbol();
                return true;
            }
        }
        if (this.currentSymbol.equals(symbol)) {
            nextSymbol();
            return true;
        }
        return false;
    }

    /**
     * Ожидаемый символ
     *
     * @param symbol
     * @return
     * @throws ParserException
     */
    protected boolean expectSymbol(String symbol) throws ParserException {
        if (symbol.equals(Constants.NUMBER_REGEX)) {
            if (this.currentSymbol.matches(Constants.NUMBER_REGEX)) {
                nextSymbol();
                return true;
            } else {
                throw new ParserException("Error while parsing: invalid number " + this.currentSymbol);
            }
        }
        if (symbol.equals(Constants.IDENT_REGEX)) {
            if (this.currentSymbol.matches(Constants.IDENT_REGEX)) {
                nextSymbol();
                return true;
            } else {
                throw new ParserException("Error while parsing: invalid ident " + this.currentSymbol);
            }
        }
        if (this.currentSymbol.equals(symbol)) {
            nextSymbol();
            return true;
        }
        throw new ParserException("Error while parsing: expected " + symbol + " got " + this.currentSymbol);
    }

}
