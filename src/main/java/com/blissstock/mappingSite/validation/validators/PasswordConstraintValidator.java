package com.blissstock.mappingSite.validation.validators;

import com.blissstock.mappingSite.validation.constrains.ValidPassword;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator
  implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(ValidPassword arg0) {}

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    PasswordValidator validator = new PasswordValidator(
      Arrays.asList(
        new LengthRule(8, 60),
        //at least one alpahbet
        new CharacterRule(EnglishCharacterData.Alphabetical, 1),
        //as least one digit
        new CharacterRule(EnglishCharacterData.Digit, 1)
      )
    );

    RuleResult result = validator.validate(new PasswordData(password));
    if (result.isValid()) {
      return true;
    }
    String messageTemplate = String.join(",", validator.getMessages(result));
    context.disableDefaultConstraintViolation();
    context
      .buildConstraintViolationWithTemplate(messageTemplate)
      .addConstraintViolation();
    return false;
  }
}
