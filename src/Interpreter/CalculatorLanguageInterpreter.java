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
        buffer = expression;
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
            parseErrorCause =  e.getMessage();
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
            throw new ParseException("no equals sign at end of expression", position);
        }

    }

    private double expression() throws ParseException {
        double calculationResult = term();
        return termFollow(calculationResult);
    }

    private double term() throws ParseException {
        Number value = value();

        if (value.doubleValue() == Math.ceil(value.doubleValue())) {
            return valueFollow(value.intValue());
        } else {
            return valueFollow(value.doubleValue());
        }
    }

    private int valueFollow(double doubleValue) {

    }

    private double value() throws ParseException {
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
                String signedValue = "-" + value;
                return Double.parseDouble(signedValue);
            }
        } else {
            return Double.parseDouble(value);
        }
    }

    private Optional<Character> floating() {
        if (getCurrentToken().equals('.'))
    }

    private String unsigned() {

        return digits();
    }

    private String digits() {

        String tempDigits = "";

        while (Character.isDigit(getCurrentToken())) {
            tempDigits += getCurrentToken();
            nextToken();
        }

        return tempDigits;
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
