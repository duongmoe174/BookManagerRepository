package com.duong.bookrepo.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
            return BookForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookForm bookForm = (BookForm) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        String price = String.valueOf(bookForm.getPrice());
        if(!price.matches("(^$|[0-9]*$)")) {
            errors.rejectValue("price", "price.matches");
        }
        MultipartFile multipartFile = bookForm.getImage();
        if(multipartFile == null) {
            errors.rejectValue("image", "image.empty");
        }
    }
}
