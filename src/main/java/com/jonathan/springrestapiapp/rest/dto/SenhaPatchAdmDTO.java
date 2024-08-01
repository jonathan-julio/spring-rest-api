package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenhaPatchAdmDTO {
    private Integer usuarioID;
    private String password;
}
