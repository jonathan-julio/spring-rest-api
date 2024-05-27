package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Usuario;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TemporalType;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LogDTO {
    private Integer id;
    private UserDTO usuario;
    private String function;
    private String changer;
    private Date data;

}
