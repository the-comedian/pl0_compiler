/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.np.pl0_parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author npetrov_2
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParserException {
        String programCode = readFile("input.txt", Charset.forName("utf-8"));
        Parser parser = new Parser();
        parser.parse(programCode);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
