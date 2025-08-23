package ir.tejarattrd.oms.demo.demo.Entity;// ... سایر import ها
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {

    // ... سایر فیلدها

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Column(nullable = false)
    private String first_name;

    @NotBlank(message = "نام خانوادگی نمی‌تواند خالی باشد")
    @Column(nullable = false)
    private String last_name;

    @NotBlank(message = "نام کاربری نمی‌تواند خالی باشد")
    @Size(min = 4, message = "نام کاربری باید حداقل ۴ کاراکتر باشد")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "ایمیل نمی‌تواند خالی باشد")
    @Email(message = "فرمت ایمیل صحیح نیست")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد")
    @Size(min = 6, message = "رمز عبور باید حداقل ۶ کاراکتر باشد")
    private String password;


}