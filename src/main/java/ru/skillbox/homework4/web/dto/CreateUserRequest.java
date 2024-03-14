package ru.skillbox.homework4.web.dto;

import ru.skillbox.homework4.user.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String username;

    private String email;

    private Set<RoleType> roles;

    private String password;
}
