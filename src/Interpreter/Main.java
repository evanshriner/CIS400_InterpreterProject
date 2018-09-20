package Interpreter;

import java.util.Scanner;

public class Main {

    private static final String startPrompt = "Please input all expressions you would like to test, followed" +
                                              " by enter (carriage return) when you would like to evaluate it. Enter x to quit.";

    private static final String endPrompt = "Ending program...";

    public static void main(String[] args) {

        String expression;
        boolean continueInput = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println(startPrompt + '\r');

        while (continueInput) {

            // There is no portable way to read raw characters from a Java console.
            // Therefore, the entire line must be read
            expression = scanner.nextLine();

            if (expression.equals("x")) {
                System.out.println(endPrompt);
                continueInput = false;
            } else {

                CalculatorLanguageInterpreter interpreter = new CalculatorLanguageInterpreter(expression);

                if (interpreter.parse()) {
                    System.out.println(interpreter.evaluate());
                } else {
                    System.out.println(interpreter.getParseErrorCause());
                }

            }
        }
    }







}
