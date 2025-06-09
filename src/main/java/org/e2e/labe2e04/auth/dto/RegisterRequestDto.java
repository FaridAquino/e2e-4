package org.e2e.labe2e04.auth.dto;

import lombok.*;
import org.e2e.labe2e04.driver.domain.Category;
import org.e2e.labe2e04.vehicle.dto.VehicleBasicDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Boolean isDriver = false;
    private Category category;
    private VehicleBasicDto vehicle;
}