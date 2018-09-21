package Interpreter;

import java.text.ParseException;
import java.util.Optional;

public class CalculatorLanguageInterpreter {

    private String buffer;

    private static final String parseNotExecuted = "ERROR: You have not parsed the calculation yet.";

    private int position = 0;

    private Double evaluationResult = null;

    private String parseErrorCause;

    public CalculatorLanguageInterpreter(String expression){
        buffer = expression.replaceAll("\\s+","");
    }

    private void nextToken(){
        position++;
    }

    private Character getCurrentToken() {
        return buffer.charAt(position);
    }

    public boolean parse() {
        double result;

        try {
            result = calculation();
        } catch (ParseException e) {
            parseErrorCause =  e.getMessage() + e.getErrorOffset();
            return false;
        }

        evaluationResult = result;
        return true;
    }

    private double calculation() throws ParseException {

        double calculationResult = expression();

        if (getCurrentToken().equals('=')){
            return calculationResult;
        } else {
            throw new ParseException("Parse error at character ", position + 1);
        }

    }

    private double expression() {
        double calculationResult = term();
        return termFollow(calculationResult);
    }

    private double termFollow(double calculationResult) {

        if (getCurrentToken() == '-')
        {
            nextToken();
            return calculationResult - expression();
        }
        else if (getCurrentToken() == '/')
        {
            nextToken();
            return calculationResult / expression();
        }
        else
            return calculationResult;

    }

    private double term() {
        double value = value();
        return valueFollow(value);
    }

    private double valueFollow(double calculationResult) {

        if (getCurrentToken() == '+')
        {
            nextToken();
            double nextValue = term();
            return termFollow(calculationResult + nextValue);
        }
        else if (getCurrentToken() == '*')
        {
            nextToken();
            double nextValue = term();
            return termFollow(calculationResult * nextValue);
        }
        else
            return calculationResult;

    }

    private double value() {
        // checks if signed, if does increases position
        Optional<Character> sign = signed();

        // gets first part of unsigned value
        String value = unsigned();

        // checks for floating point value
        Optional<Character> floatingPoint = floating();

        // if floating point value, appends rest of unsigned
        if (floatingPoint.isPresent()) {
            value += '.' + unsigned();
        }

        if (sign.isPresent()){
            if (sign.equals('+')) {
                //return positive of value
                return Double.parseDouble(value);
            } else {
                // return negative of value
                return -Double.parseDouble(value);
            }
        } else {
            return Double.parseDouble(value);
        }
    }

    private Optional<Character> floating() {

        if (getCurrentToken().equals('.')) {
            return Optional.of('.');
        } else {
            return Optional.empty();
        }
    }

    private String unsigned() {

        String tempDigits = "";

        while (digits()){
            tempDigits += getCurrentToken();
            nextToken();
        }
        return tempDigits;
    }

    private boolean digits() {
        return Character.isDigit(getCurrentToken());
    }

    private Optional<Character> signed() {

        if (getCurrentToken().equals('+')) {
            nextToken();
            return Optional.of('+');
        } else if (getCurrentToken().equals('-')) {
            nextToken();
            return Optional.of('-');
        } else {
            return Optional.empty();
        }

    }


    public String evaluate() {
        if (evaluationResult != null) {
            return Double.toString(evaluationResult);
        } else {
            return parseNotExecuted;
        }
    }

    public String getParseErrorCause() {
        return parseErrorCause;
    }
}
