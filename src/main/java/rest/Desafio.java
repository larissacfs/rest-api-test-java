package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Desafio {
	
	public static String token;
	
	@BeforeClass
	public static void beforeTest() {
		RestAssured.baseURI = "http://barrigarest.wcaquino.me/";
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "larissacfdasilva2@gmail.com");
		map.put("senha", "123456ui");
		token = given()
			.log().all()
			.body(map)
			.contentType(ContentType.JSON)
		.when()
			.post("signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		System.out.println("-------------------------------------");
	}
	
	public String randomNomeConta() {
		Random rand = new Random();
		return "Conta "+ rand.nextInt(100000);
	}
	
	@Test
	public void naoDeveAcessarApiSemToken() {
 		given()
 			.log().all()
		.when()
			.get("contas")
		.then()
			.log().all()
			.statusCode(401) //Unauthorized
		;
 		System.out.println("-------------------------------------");
	}
	
	public Response inserirConta(String nomeConta) {
 		return given()
 			.log().all()
 			.header("Authorization", "JWT " + token)
 			.contentType("application/json")
			.body("{ \"nome\": \"" + nomeConta +"\"}")
		.when()
			.post("contas")
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		String nomeConta = randomNomeConta();
 		Response response = this.inserirConta(nomeConta);
 		response.then()
			.log().all()
			.statusCode(201) //created
			.body("nome", is(nomeConta))
			.body("id", is(notNullValue()))
		;
 		System.out.println("-------------------------------------");
	}
	
	@Test
	public void devealterarContaComSucesso() {
		String nomeConta = randomNomeConta();
 		Response response = this.inserirConta(nomeConta);
 		//String id = response.jsonPath().getString("id");
 		int id = response.then()
 				.log().all()
 				.statusCode(201)
 				.extract().path("id");
 		System.out.println("-------------------------------------");
 		// alterando conta
 		nomeConta = randomNomeConta();
 		given()
			.log().all()
			.header("Authorization", "JWT " + token)
			.pathParam("conta_id", id)
			.contentType("application/json")
			.body("{ \"nome\": \"" + nomeConta +"\"}")
		.when()
			.put("contas/{conta_id}")
		.then()
			.log().all()
			.statusCode(200) 
			.body("nome", is(nomeConta))
		;
 		System.out.println("-------------------------------------");
	}
	
	@Test
	public void naoDeveIncluirContaComNomeRepetido() {
 		Response response = this.inserirConta("Conta de Teste");
 		response.then()
			.log().all()
			.statusCode(400) //bad request
			.body("error", is("Já existe uma conta com esse nome!"))
		;
 		System.out.println("-------------------------------------");
	}
	
	public Response inserirMovimentacao(String body) {
 		return given()
			.log().all()
			.header("Authorization", "JWT " + token)
			.contentType("application/json")
			.body(body)
		.when()
			.post("transacoes")
		;
	}
	
	@Test
	public void deveInserirMovivemtacaoComSucesso() {
		Calendar c = Calendar.getInstance();
		String hoje = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		String body = "{ \"conta_id\": \"187018\", \"descricao\": \"teste\", \"envolvido\": \"x\", \"tipo\": \"x\", \"tipo\": \"REC\", \"data_transacao\": \"" + hoje + "\", \"data_pagamento\": \"" + hoje + "\", \"valor\": 10.5, \"status\": \"true\"}";		
		Response response = inserirMovimentacao(body);
		response.then()
			.log().all()
			.statusCode(201) 
			.body("id", is(notNullValue()))
		;
		System.out.println("-------------------------------------");
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {
 		Response response = inserirMovimentacao("");
		response.then()
			.log().all()
			.statusCode(400)
			.body("param", hasItems("data_transacao", "data_pagamento", "descricao", "envolvido", "valor", "valor", "conta_id", "status"))
		;
 		System.out.println("-------------------------------------");
	}
	
	@Test
	public void naoDeveCadastrarMovimentacaoFutura() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String amanha = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		String body = "{ \"conta_id\": \"187018\", \"descricao\": \"teste\", \"envolvido\": \"x\", \"tipo\": \"x\", \"tipo\": \"REC\", \"data_transacao\": \"" + amanha + "\", \"data_pagamento\": \"01/01/2040\", \"valor\": 10.5, \"status\": \"true\"}";		
		Response response = inserirMovimentacao(body);
		response.then()
			.log().all()
			.statusCode(400) 
			.body("param", hasItem("data_transacao"))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
		System.out.println("-------------------------------------");
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		// criando a conta
		String nomeConta = randomNomeConta();
 		Response response = this.inserirConta(nomeConta);
 		int id = response.then()
			.log().all()
			.statusCode(201) //created
			.body("id", is(notNullValue()))
			.extract().path("id");
		;
 		System.out.println("-------------------------------------");
		// adicionando a movimentacao
 		String body = "{ \"conta_id\": \"" + id + "\", \"descricao\": \"teste\", \"envolvido\": \"x\", \"tipo\": \"x\", \"tipo\": \"REC\", \"data_transacao\": \"01/01/2020\", \"data_pagamento\": \"01/01/2020\", \"valor\": 10.5, \"status\": \"true\"}";		
 		response = inserirMovimentacao(body);
		response.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
		;
 		System.out.println("-------------------------------------");
 		// tentando remover a conta
 		given()
			.log().all()
			.header("Authorization", "JWT " + token)
			.pathParam("conta_id", id)
			.contentType("application/json")
		.when()
			.delete("contas/{conta_id}")
		.then()
			.log().all()
			.statusCode(500) 
		;
			System.out.println("-------------------------------------");
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		//TODO
		//get /saldos
	}
	
	@Test
	public void deveRemoverUmaMovimentacao() {
		//TODO
		// delete /transacoes/{id}
	}
}
