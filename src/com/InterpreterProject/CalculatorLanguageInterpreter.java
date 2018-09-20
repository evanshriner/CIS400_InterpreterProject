package com.InterpreterProject;

import java.util.Stack;

public class CalculatorLanguageInterpreter {

    private Stack<Character> buffer = new Stack<>();

    private String evaluationResult = null;

    public CalculatorLanguageInterpreter(String expression){

        for(Character c: expression.toCharArray()){
            buffer.push(c);
        }
    }

    public boolean parsable() {

        Character term;

        if (buffer.empty()){
           return true;
        } else {
           term = buffer.pop();
        }


        return true;
    }

    public String evaluate() {
        return evaluationResult;
    }



}
