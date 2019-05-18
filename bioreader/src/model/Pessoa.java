package model;

public class Pessoa {
	
	public int id;
	public String cpf;
	public String nome;
	public byte[] digital; //No BD eh tipo BLOB
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public byte[] getDigital() {
		return digital;
	}
	public void setDigital(byte[] digital) {
		this.digital = digital;
	}
	
	 
	
	

}
