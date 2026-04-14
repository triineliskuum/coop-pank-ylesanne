package ee.coop.coopbackend.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonalCodeUtilsTest {

    @Test
    void extractBirthDate_shouldReturnCorrectDateFor1900sCode() {
        LocalDate result = PersonalCodeUtils.extractBirthDate("49403136526");

        assertEquals(LocalDate.of(1994, 3, 13), result);
    }

    @Test
    void extractBirthDate_shouldReturnCorrectDateFor2000sCode() {
        LocalDate result = PersonalCodeUtils.extractBirthDate("50601010000");

        assertEquals(LocalDate.of(2006, 1, 1), result);
    }

    @Test
    void extractBirthDate_shouldThrowExceptionWhenPersonalCodeIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> PersonalCodeUtils.extractBirthDate(null)
        );

        assertEquals("Personal code must contain exactly 11 digits", ex.getMessage());
    }

    @Test
    void extractBirthDate_shouldThrowExceptionWhenPersonalCodeHasWrongLength() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> PersonalCodeUtils.extractBirthDate("123")
        );

        assertEquals("Personal code must contain exactly 11 digits", ex.getMessage());
    }

    @Test
    void extractBirthDate_shouldThrowExceptionWhenPersonalCodeContainsNonDigits() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> PersonalCodeUtils.extractBirthDate("4940313652A")
        );

        assertEquals("Personal code must contain exactly 11 digits", ex.getMessage());
    }

    @Test
    void extractBirthDate_shouldThrowExceptionWhenFirstDigitIsInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> PersonalCodeUtils.extractBirthDate("99403136526")
        );

        assertEquals("Invalid Estonian personal code first digit", ex.getMessage());
    }

    @Test
    void extractBirthDate_shouldThrowExceptionWhenDateIsInvalid() {
        assertThrows(
                Exception.class,
                () -> PersonalCodeUtils.extractBirthDate("49413336526")
        );
    }
}