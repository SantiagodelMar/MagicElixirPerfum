package service.validations;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FormTypeValidator {

    static Scanner sc = new Scanner(System.in);

    public static int intValidator(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Solo se admiten numeros enteros");
                sc.nextLine();
            }
        }
    }

    public static double doubleValidator(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Solo se admiten numeros");
                sc.nextLine();
            }
        }
    }

    public static String stringValidator(String prompt) {
        while (true) {
            System.out.println(prompt);
            String value = sc.nextLine();
            if (!value.trim().isEmpty()) {
                return value;
            }
            System.out.println("El campo no puede estar vacio");
        }
    }

    public static LocalDate dateValidator(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String value = sc.nextLine();
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida, use el formato yyyy-MM-dd");
            }
        }
    }

}//Fin public class
