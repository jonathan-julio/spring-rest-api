package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UsuarioDTO {
    private Integer id;
}
