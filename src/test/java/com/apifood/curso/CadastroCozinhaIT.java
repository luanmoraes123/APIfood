package com.apifood.curso;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;


import com.apifood.curso.domain.model.Cozinha;
import com.apifood.curso.domain.repository.CozinhaRepository;
import com.apifood.curso.util.DatabaseCleaner;
import com.apifood.curso.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	private static final int COZINHA_ID_INEXISTENTE = 100;

	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;
	
	@BeforeEach
	public void setUp() {
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		
		RestAssured.given()				
		.accept(ContentType.JSON)
		.when().get()
		.then().statusCode(200);
	}
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {

		RestAssured.given()
		.accept(ContentType.JSON)
		.when().get()
		.then().body("", Matchers.hasSize(quantidadeCozinhasCadastradas));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastradaCozinha() {
		
		RestAssured.given()	
		.body(jsonCorretoCozinhaChinesa)
		.accept(ContentType.JSON)
		.contentType(ContentType.JSON)
		.when().post()
		.then().statusCode(201);
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		
		RestAssured.given()	
		.pathParam("cozinhaId", cozinhaAmericana.getId())
		.accept(ContentType.JSON)
		.when().get("/{cozinhaId}")
		.then().statusCode(200).body("nome", Matchers.equalTo(cozinhaAmericana.getNome()));
	}
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		
		RestAssured.given()	
		.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
		.accept(ContentType.JSON)
		.when().get("/{cozinhaId}")
		.then().statusCode(404);
	}
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
	    cozinhaTailandesa.setNome("Tailandesa");
	    cozinhaRepository.save(cozinhaTailandesa);

	    cozinhaAmericana = new Cozinha();
	    cozinhaAmericana.setNome("Americana");
	    cozinhaRepository.save(cozinhaAmericana);
	    
	    quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
		
	}
	
	

}
