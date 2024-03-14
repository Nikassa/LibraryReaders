package ru.my.task.libraryreaders.annotation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InsuranceNumberConstraintValidatorTest {

    private InsuranceNumberConstraintValidator insuranceNumberConstraintValidator;

    @BeforeEach
    void init() {
        insuranceNumberConstraintValidator = spy(InsuranceNumberConstraintValidator.class);
    }

    @ParameterizedTest
    @MethodSource("insuranceNumberSource")
    void whenIsValid_thenCorrectInsuranceNumber(String insuranceNumber, boolean checkFormat, boolean checkControlSum, boolean result) {
        doReturn(checkFormat).when(insuranceNumberConstraintValidator).checkFormatInsuranceNumber(insuranceNumber);
        doReturn(checkControlSum).when(insuranceNumberConstraintValidator).checkControlSum(insuranceNumber);
        assertEquals(result, insuranceNumberConstraintValidator.isValid(insuranceNumber, null));
    }

    private static Stream<Arguments> insuranceNumberSource() {
        return Stream.of(
                Arguments.of("160-722-773 54", true, true, true),
                Arguments.of(null, true, true, false),
                Arguments.of("", true, true, false),
                Arguments.of("160-7227-73 54", false, true, false),
                Arguments.of("160-722-773 53", true, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("checkFormatSource")
    void whenCheckFormat_thenCorrectFormat(String insuranceNumber, boolean result) {
        assertEquals(result, insuranceNumberConstraintValidator.checkFormatInsuranceNumber(insuranceNumber));
    }

    private static Stream<Arguments> checkFormatSource() {
        return Stream.of(
                Arguments.of("160-722-773 54", true),
                Arguments.of("513-966-262 00", true),
                Arguments.of("050-411-810 99", true),
                Arguments.of(null, false),
                Arguments.of("", false),
                Arguments.of("16072277354", false),
                Arguments.of("160-72!-77 54", false)
        );
    }


    @ParameterizedTest
    @MethodSource("checkControlSumSource")
    void whenCheckControlSum_thenCorrectControlSum(String insuranceNumber, List<Integer> insuranceNumberList, String digit, boolean result) {
        doReturn(insuranceNumberList).when(insuranceNumberConstraintValidator).convertFieldToIntList(insuranceNumber);
        doReturn(digit).when(insuranceNumberConstraintValidator).calculateControlSum(anyInt());
        assertEquals(result, insuranceNumberConstraintValidator.checkControlSum(insuranceNumber));
    }

    private static Stream<Arguments> checkControlSumSource() {
        return Stream.of(
                Arguments.of("160-722-773 54", Arrays.asList(1, 6, 0, 7, 2, 2, 7, 7, 3, 5, 4), "54", true),
                Arguments.of("513-966-262 00", Arrays.asList(5, 1, 3, 9, 6, 6, 2, 6, 2, 0, 0), "00", true),
                Arguments.of("050-411-810 99", Arrays.asList(0, 5, 0, 4, 1, 1, 8, 1, 0, 9, 9), "99", true),
                Arguments.of("160-722-773 53", Arrays.asList(1, 6, 0, 7, 2, 2, 7, 7, 3, 5, 3), "54", false),
                Arguments.of("160-722-773 54$", new ArrayList<>(), null, false),
                Arguments.of(null, new ArrayList<>(), null, false),
                Arguments.of("", new ArrayList<>(), null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("ConvertFieldToIntListSource")
    void whenConvertFieldToIntList_thenReturnConvertedAndParsedList(String insuranceNumber, List<Integer> result) {
        assertEquals(result, insuranceNumberConstraintValidator.convertFieldToIntList(insuranceNumber));
    }

    private static Stream<Arguments> ConvertFieldToIntListSource() {
        return Stream.of(
                Arguments.of("160-722-773 54", Arrays.asList(1, 6, 0, 7, 2, 2, 7, 7, 3, 5, 4)),
                Arguments.of("513-966-262 00", Arrays.asList(5, 1, 3, 9, 6, 6, 2, 6, 2, 0, 0)),
                Arguments.of("050-411-810 99", Arrays.asList(0, 5, 0, 4, 1, 1, 8, 1, 0, 9, 9)),
                Arguments.of("160-722-773 53", Arrays.asList(1, 6, 0, 7, 2, 2, 7, 7, 3, 5, 3)),
                Arguments.of("160-722-773 54$", new ArrayList<>()),
                Arguments.of(null, new ArrayList<>()),
                Arguments.of("", new ArrayList<>())
        );
    }

    @Test
    void whenCalculateControlSum_thenReturnCalculatedSum() {
        assertEquals("54", insuranceNumberConstraintValidator.calculateControlSum(155));
        assertEquals("00", insuranceNumberConstraintValidator.calculateControlSum(202));
        assertEquals("99", insuranceNumberConstraintValidator.calculateControlSum(99));
    }
}