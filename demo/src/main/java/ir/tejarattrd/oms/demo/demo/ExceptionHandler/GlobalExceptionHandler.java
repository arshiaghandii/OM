package ir.tejarattrd.oms.demo.demo.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * این متد خطاهای اعتبارسنجی (Validation) را مدیریت می‌کند.
     * زمانی که یک درخواست با داده‌های نامعتبر به کنترلر ارسال شود، این متد فعال می‌شود.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * این متد خطاهای عمومی و پیش‌بینی نشده را مدیریت می‌کند.
     * برای مثال، اگر در سرویس شما یک RuntimeException رخ دهد، این متد آن را دریافت می‌کند.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "یک خطای داخلی در سرور رخ داده است.");
        errorResponse.put("message", ex.getMessage()); // پیام اصلی خطا برای دیباگ
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * می‌توانید برای خطاهای خاص خودتان هم Handler های جداگانه بنویسید.
     * مثلا برای خطای پیدا نشدن کاربر.
     */
    // @ExceptionHandler(UserNotFoundException.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    // public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
    //     Map<String, String> errorResponse = new HashMap<>();
    //     errorResponse.put("error", ex.getMessage());
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    // }
}