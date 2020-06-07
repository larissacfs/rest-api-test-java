package rest;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class EnvioDadosTest {

	@Test
	public void deveEnviarValorViaQuery() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=xml")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
		;
	}
	
	@Test
	public void deveEnviarValorViaParam() {
		given()
			.log().all()
			.queryParam("format", "xml")
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(containsString("utf-8"))
		;
	}
	
	@Test
	public void deveEnviarValorViaHeader() {
		given()
			.log().all()
			.accept(ContentType.JSON) // diferente de .contentType(ContentType.JSON) pois estou informando n o tipo do envio mas o que vou aceitar
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}
}
