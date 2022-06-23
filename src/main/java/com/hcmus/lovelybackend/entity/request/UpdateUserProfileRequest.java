package com.hcmus.lovelybackend.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    @Size(min = 2, max = 64)
    private String name;

    private LocalDateTime dateOfBirth;

    @Email
    @Size(max = 100)
    private String email;
}
