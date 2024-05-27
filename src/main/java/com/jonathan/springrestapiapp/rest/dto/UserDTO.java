package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.model.Post;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    private String login;
    private UserRole admin;
    private PersonDTO person;
    private List<Post> posts;
}
