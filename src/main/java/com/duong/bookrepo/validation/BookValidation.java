package com.duong.bookrepo.validation;

import com.duong.bookrepo.model.BookForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class BookValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookForm bookForm = (BookForm) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        int price = bookForm.getPrice();
        String priceStr = Integer.toString(price);
        if(!priceStr.matches("(^$|[0-9]*$)")) {
            errors.rejectValue("price", "price.matches");
        }
    }
}
