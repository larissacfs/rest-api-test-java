package rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.xml.sax.SAXParseException;


public class SchemaTest {
	
	@Test
	public void deveValidarEsquemaXml() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/usersXMl")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
	}
	
	@Test(expected = SAXParseException.class)
	public void naoDeveValidarEsquemaXmlInvalido() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/invalidUsersXMl")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
	}

	@Test
	public void DeveValidarEsquemaJson() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
		;
	}
}
