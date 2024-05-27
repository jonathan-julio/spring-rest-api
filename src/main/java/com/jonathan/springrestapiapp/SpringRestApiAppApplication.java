package com.jonathan.springrestapiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SpringRestApiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApiAppApplication.class, args);
	}

}


/* 
{
	"login": "johndoe",
	"senha": "strongPassword789",
	"person": {
		"nome": "John Doe",
		"data": "1985-07-23",
		"sexo": "M",
		"profile": {
		"texto": "Hello, I m John!",
		"textoSecundario": "Software Developer at XYZ Corp.",
		"about": "Passionate about coding and technology.",
		"color": "blue",
		"background": "white"
		}
	}
}

{
	"login": "janedoe",
	"senha": "anotherSecurePassword456",
	"person": {
		"nome": "Jane Doe",
		"data": "1990-03-15",
		"sexo": "F",
		"profile": {
		"texto": "Hi, I m Jane!",
		"textoSecundario": "Graphic Designer at Creative Studio.",
		"about": "Lover of art, design, and innovation.",
		"color": "purple",
		"background": "lightgrey"
		}
	}
} 

{
	"login": "samsmith",
	"senha": "strongPassword789",
	"person": {
	  "nome": "Sam Smith",
	  "data": "1988-11-30",
	  "sexo": "M",
	  "profile": {
		"texto": "Hey, I m Sam!",
		"textoSecundario": "Data Scientist at Tech Analytics.",
		"about": "Enthusiastic about data science and AI.",
		"color": "green",
		"background": "black"
	  }
	}
}

{
	"login": "emilyjones",
	"senha": "superSecurePassword321",
	"person": {
	  "nome": "Emily Jones",
	  "data": "1992-06-10",
	  "sexo": "F",
	  "profile": {
		"texto": "Hello, I m Emily!",
		"textoSecundario": "Marketing Specialist at BrandCorp.",
		"about": "Passionate about marketing and brand strategy.",
		"color": "red",
		"background": "beige"
	  }
	}
}

  */
  
