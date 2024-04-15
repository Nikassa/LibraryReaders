package ru.my.task.libraryreaders.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InsuranceNumberConstraintValidator implements ConstraintValidator<InsuranceNumber, String> {

    @Override
    public boolean isValid(String insuranceNumberField, ConstraintValidatorContext cxt) {
        return insuranceNumberField != null && !insuranceNumberField.isEmpty()
                && checkFormatInsuranceNumber(insuranceNumberField)
                && checkControlSum(insuranceNumberField);
    }

    boolean checkFormatInsuranceNumber(String insuranceNumberField) {
        return insuranceNumberField != null && insuranceNumberField.matches("^\\d{3}-\\d{3}-\\d{3}\\s\\d{2}$"); //format: 000-000-000 00
    }

    boolean checkControlSum(String insuranceNumberField) {
        List<Integer> insuranceNumberConverted = convertFieldToIntList(insuranceNumberField);
        if (insuranceNumberConverted.size() == 11) {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum = sum + insuranceNumberConverted.get(i) * (9 - i);
            }
            String calculatedControlSum = calculateControlSum(sum);
            return calculatedControlSum.equals(insuranceNumberField.substring(12, 14));
        }
        return false;
    }

    List<Integer> convertFieldToIntList(String inputString) {
        List<Integer> convertedInputString = new ArrayList<>();
        try {
            convertedInputString = Arrays.stream(inputString.split(""))
                    .filter(i -> !i.equals(" ") && !i.equals("-"))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());
        } catch (NumberFormatException | NullPointerException ex) {
            log.error("Unable to parse insuranceNumber: " + inputString, ex);
            throw ex;
        } finally {
            return convertedInputString;
        }
    }

    String calculateControlSum(int sum) {
        String checkDigit = "";
        if (sum < 100) {
            checkDigit = String.valueOf(sum);
        } else if (sum > 101) {
            checkDigit = String.valueOf(sum % 101);
        }
        return checkDigit.equals("0") ? "00" : checkDigit;
    }
}
