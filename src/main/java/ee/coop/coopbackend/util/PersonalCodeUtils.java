package ee.coop.coopbackend.util;

import java.time.LocalDate;

/**
 * Utility class for extracting data from Estonian personal codes.
 */
public class PersonalCodeUtils {

    /**
     * Extracts birth date from Estonian personal code.
     * Validates format and determines correct century.
     */
    public static LocalDate extractBirthDate(String personalCode) {
        if (personalCode == null || personalCode.length() != 11 || !personalCode.matches("\\d{11}")) {
            throw new IllegalArgumentException("Personal code must contain exactly 11 digits");
        }

        int firstDigit = Character.getNumericValue(personalCode.charAt(0));
        int year = Integer.parseInt(personalCode.substring(1, 3));
        int month = Integer.parseInt(personalCode.substring(3, 5));
        int day = Integer.parseInt(personalCode.substring(5, 7));

        int century;
        switch (firstDigit) {
            case 1, 2 -> century = 1800;
            case 3, 4 -> century = 1900;
            case 5, 6 -> century = 2000;
            case 7, 8 -> century = 2100;
            default -> throw new IllegalArgumentException("Invalid Estonian personal code first digit");
        }

        return LocalDate.of(century + year, month, day);
    }
}