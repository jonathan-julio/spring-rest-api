package com.jonathan.springrestapiapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@EqualsAndHashCode
@Getter
@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Profile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message = "{campo.texto.obrigatorio}")
    private String texto;
    @NotEmpty(message = "{campo.textoSecundario.obrigatorio}")
    private String textoSecundario;
    @Column(length = 4000)
    @NotEmpty(message = "{campo.about.obrigatorio}")
    private String about;
    private String color;
    private String background;

    @OneToOne
    private Person person;

    @ManyToMany(mappedBy = "profile", fetch = FetchType.EAGER)
    private List<Post> post;


    Person getPerson(){
        return person;
    }

    void setPerson(Person person){
        this.person  = person;
    }


    List<Post> getPost(){
        return post;
    }

    void setPost(List<Post> post){
        this.post  = post;
    }

    /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Um cliente pode ter muitos pedidos!
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column
    private LocalDate dataPedido;

    //1000.00
    @Column(name = "total", precision = 20,scale = 2)
    private BigDecimal total;


    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido status;

     */

    
    

    
    
}
