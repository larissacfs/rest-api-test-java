package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTeste {
	
	@Test
	public void testeOlaMundo() {
		Response request = RestAssured.request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		Assert.assertTrue(request.getBody().asString().equals("Ola Mundo!"));
		
		// tres formas de usar o junit para validar o teste
		Assert.assertTrue(request.getStatusCode() == 200);
		Assert.assertTrue("O status code deveria ser 200", request.getStatusCode() == 200);
		Assert.assertEquals(200, request.getStatusCode());
		
		ValidatableResponse then = request.then();
		then.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response request = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse then = request.then();
		then.statusCode(200);
		
		// executa o mesmo codigo mostrado acima, get eh um metodo static importado acima
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		// Forma usando Gherkin (usado no BDD) given when then
		given() // pre condicoes
		.when() // acao
			.get("http://restapi.wcaquino.me/ola")
		.then() // assertivas
		.assertThat() // usado para legilibilidade, usar se quiser	
		.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchers() {
		// valor atual, expected
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(123, Matchers.isA(Integer.class));
		Assert.assertThat(123d, Matchers.isA(Double.class));
		Assert.assertThat(123, Matchers.greaterThan(110));
		
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1, 3, 5, 7, 9));
		assertThat(impares, containsInAnyOrder(1, 3, 5, 7, 9));
		assertThat(impares, hasItem(5));
		assertThat(impares, hasItems(1, 5));
		
		assertThat("Maria", is(not("Joao")));
		// como o is eh opcional
		assertThat("Maria", not("Joao"));
		assertThat("Maria", anyOf(is("Joao"), is("Maria")));
		assertThat("Maria", allOf(startsWith("Mar"), endsWith("ria"), containsString("ari"), is("Maria")));
	}
	
	@Test
	public void devoValidarBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
		.statusCode(200)
		.body(is("Ola Mundo!"))
		.body(containsString("Mundo"))
		.body(is(not(nullValue())));
	}
}
