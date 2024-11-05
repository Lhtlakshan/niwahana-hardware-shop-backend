package org.icet.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.icet.crm.dto.UserDto;
import org.icet.crm.enums.UserRole;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;

    @Lob
    private byte[] img;

    public UserDto mapUserToUserDto(){
        return new UserDto(id,name,email,password,userRole);
    }
}
