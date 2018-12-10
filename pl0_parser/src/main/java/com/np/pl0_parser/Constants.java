/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.pl0_parser;

import java.util.ArrayList;

/**
 *
 * @author npetrov_2
 */
public class Constants {
    
    public static final String POINT = ".";
    public static final String SEMICOLON = ";";
    // разделители
    public static final ArrayList<String> DELIMITERS = new ArrayList<String>() {
        {
            add(POINT);
            add(SEMICOLON);
            add(IN);
            add(OUT);
            add(COMMA);
            add(ASSIGN);
            add(PLUS);
            add(MINUS);
            add(TIMES);
            add(SLASH);
            add(EQUAL);
            add(GREATER);
            add(LESS);
            add(GREATER_OR_EQUAL);
            add(LESS_OR_EQUAL);
            add(NOT_EQUAL);
        }
    };
    // служебные слова
    public static final String BEGIN = "begin";
    public static final String END = "end";
    public static final String CONST = "const";
    public static final String PROCEDURE = "procedure";
    public static final String IN = "?";
    public static final String OUT = "!";
    public static final String VAR_INT = "var_int";
    public static final String VAR_STRING = "var_string";
    public static final String COMMA = ",";
    // арифметические операции
    public static final String ASSIGN = "@";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String TIMES = "*";
    public static final String SLASH = "/";
    // операторы сравнения
    public static final String EQUAL = "=";
    public static final String GREATER = ">";
    public static final String LESS = "<";
    public static final String GREATER_OR_EQUAL = ">=";
    public static final String LESS_OR_EQUAL = "<=";
    public static final String NOT_EQUAL = "#";
    
    public static final String IF = "if";
    public static final String THEN = "then";
    public static final String ELSE = "else";
    public static final String WHILE = "while";
    public static final String DO = "do";
    public static final String CALL = "call";
    public static final String ODD = "odd";
    
    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    
    public static final String IDENT_REGEX = "^[a-zA-Z_][a-zA-Z_0-9]*";
    public static final String NUMBER_REGEX = "^[-+]?\\d*$";
   
}
