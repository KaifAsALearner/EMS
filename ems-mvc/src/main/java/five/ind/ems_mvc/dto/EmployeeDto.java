package five.ind.ems_mvc.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// or for older versions
// import javax.validation.constraints.NotNull;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    @NotEmpty(message = "First name should not be empty")
    private String fName;

    @NotEmpty(message = "Last name should not be empty")
    private String lName;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private String gender;

    @NotNull(message = "Date of birth should not be empty")
    private LocalDate dob;
}
