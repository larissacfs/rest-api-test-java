package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {
	
	@BeforeClass
	public static void before() {
		RestAssured.baseURI = "https://restapi.wcaquino.me/";
	}
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Jose\", \"age\": 50}")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
			;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"age\": 50}")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(400) // bad request
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
			;
	}
	
	@Test
	public void deveSalvarUsuarioViaXml() {
		given()
			.log().all()
//			.contentType("application/xml")
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
			;
	}
	
	@Test
	public void devealterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
		.when()
			.put("/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	@Test
	public void devoCustomizarUrl() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
		.when()
			.put("/{entidade}/{user_id}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	@Test
	public void devoCustomizarUrlParte2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
			.pathParam("entidade", "users")
			.pathParam("user_id", 1)
		.when()
			.put("/{entidade}/{user_id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("/users/1")
		.then()
			.statusCode(204)
			.log().all()
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("/users/1000")
		.then()
			.statusCode(400)
			.log().all()
			.body("error", is("Registro inexistente"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Jose");
		map.put("age", 50);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(map)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
			;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		User usuario = new User("Jose", 50);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(usuario)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
			;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjetoEDeceseralizar() {
		User usuario = new User("Jose", 50);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(usuario)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals(usuario.getName(), usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getIdade(), is(50));
	}
	
	@Test
	public void deveSalvarUsuarioViaXmlUsandoObjeto() {
		User usuario = new User("Jose", 50);
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(usuario)
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
			;
	}
	
	@Test
	public void deveDesearializarUsuarioAoSalvarUsuarioViaXmlUsandoObjeto() {
		User usuario = new User("Jose", 50);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(usuario)
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals(usuario.getName(), usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getIdade(), is(50));
	}
}
