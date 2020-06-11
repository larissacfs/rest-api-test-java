package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.sun.corba.se.pept.encoding.OutputObject;

import io.restassured.response.ExtractableResponse;


public class FileTest {

	@Test
	public void deveObrigarEnvioArtigo() {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // dveria ser 400 (nad request), pq nao eh erro de nao encontrato
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test // para o caso de um arquivo que tenha um tamanho maior, o erro seria status code 413
	public void deveFazerUploadDoArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) 
			.time(lessThan(5000L)) //abaixo de 5s
			.body("name", is("users.pdf"))
		;
	}
	
	@Test 
	public void deveBaixarArquivo() throws IOException {
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			//.log().all()
			.statusCode(200) 
			.extract().asByteArray()
		;
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}
}
