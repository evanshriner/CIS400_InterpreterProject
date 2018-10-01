package Interpreter;

import java.text.ParseException;
import java.util.Optional;

class CalculatorLanguageInterpreter {

    private String buffer;

    private static final String parseNotExecuted = "ERROR: You have not parsed the calculation yet.";

    private int position = 0;

    private Double evaluationResult = null;

    private String parseErrorCause;

    CalculatorLanguageInterpreter(String expression){
        buffer = expression.replaceAll("\\s+","");
    }
    // test comment

    private void nextToken(){
        position++;
    }

    private Character getCurrentToken() throws ParseException {
        try {
            return buffer.charAt(position);
        } catch (StringIndexOutOfBoundsException e){
            throw new ParseException("Parse error at character ", position + 1);
        }
    }

    boolean parse() {
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

    private double expression() throws ParseException {
        double calculationResult = term();
        return termFollow(calculationResult);
    }

    private double term() throws ParseException {
        double value = value();
        return valueFollow(value);
    }

    private double value() throws ParseException {
        // checks if signed, if does increases position
        try {
            Optional<Character> sign = signed();

            // gets first part of unsigned value
            String value = unsigned();

            // checks for floating point value
            Optional<Character> floatingPoint = floating();

            // if floating point value, appends rest of unsigned
            if (floatingPoint.isPresent()) {
                value += '.' + unsigned();
            }

            if (sign.isPresent()) {
                if (sign.get() == '+') {
                    //return positive of value
                    return Double.parseDouble(value);
                } else {
                    // return negative of value
                    return -Double.parseDouble(value);
                }
            } else {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Parse error at character", position + 1 );
        }
    }

    private Optional<Character> floating() throws ParseException {

        if (getCurrentToken().equals('.')) {
            nextToken(); // increment so that the unsigned portion is not confused about the dot
            return Optional.of('.');
        } else {
            return Optional.empty();
        }
    }

    private String unsigned() throws ParseException {

        String tempDigits = "";

        while (digits()){
            tempDigits += getCurrentToken();
            nextToken();
        }
        return tempDigits;
    }

    private boolean digits() throws ParseException {
        return Character.isDigit(getCurrentToken());
    }

    private Optional<Character> signed() throws ParseException {

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


    private double termFollow(double calculationResult) throws ParseException {

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

    private double valueFollow(double calculationResult) throws ParseException {

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

    String evaluate() {
        if (evaluationResult != null) {
            if (evaluationResult == Math.floor(evaluationResult)) {
                return Integer.toString((int)Math.floor(evaluationResult));
            } else {
                return Double.toString(evaluationResult);
            }
        } else {
            return parseNotExecuted;
        }
    }

    String getParseErrorCause() {
        return parseErrorCause;
    }
}
