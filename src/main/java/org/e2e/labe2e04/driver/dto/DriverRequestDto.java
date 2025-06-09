package org.e2e.labe2e04.driver.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.driver.domain.Category;
import org.e2e.labe2e04.user.domain.Role;
import org.e2e.labe2e04.vehicle.domain.Vehicle;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Valid
public class DriverRequestDto {
    @NotNull
    private Category category;

    @NotNull
    private Vehicle vehicle;

    @NotNull
    private Role role;

    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 9, max = 15)
    private String phoneNumber;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    private ZonedDateTime createdAt;
}
