package rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	@XmlAttribute
	private long id;
	private String name;
	private int age;
	private double salario;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User(String name, int idade) {
		super();
		this.name = name;
		this.age = idade;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIdade() {
		return age;
	}
	
	public void setIdade(int idade) {
		this.age = idade;
	}
	
	public double getSalario() {
		return salario;
	}
	
	public void setSalario(double salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", salario=" + salario + ", id=" + id + "]";
	}
	
	
}
