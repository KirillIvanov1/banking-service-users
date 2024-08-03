package ru.banking_service.users.util.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Номер паспорта", example = "4017180403")
    @Size(min = 10, max = 10, message = "Номер паспорта должен содержать 10 цифр")
    @Digits(integer = 10, message = "Номер паспорта должен содержать только цифры", fraction = 0)
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String passportNumber;

    @Schema(description = "Адрес электронной почты", example = "burnehquad@gmail.com")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Адрес электронной почты должен быть в формате: burnehquad@gmail.com")
    private String email;

    @Schema(description = "Пароль", example = "my1_secret1_password")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть не менее 8 и не более 255 символов")
    private String password;

}
