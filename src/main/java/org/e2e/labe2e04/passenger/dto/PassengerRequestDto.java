package org.e2e.labe2e04.passenger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.user.domain.Role;

@Getter
@Setter
@NoArgsConstructor
public class PassengerRequestDto {
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @Size(min = 9, max = 15)
    @NotNull
    private String phoneNumber;

    @NotNull
    private Role role;
}
