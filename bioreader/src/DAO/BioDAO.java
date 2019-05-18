package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import factory.ConnectionFactory;
import model.Pessoa;

public class BioDAO {

	public boolean update(String cpf, byte[] biometria) {
		boolean retorno = false;
		Connection conn = ConnectionFactory.getInstance();

		try {
			PreparedStatement stat = conn.prepareStatement(" UPDATE usuario SET digital = ? WHERE cpf = ? ");
			stat.setBytes(1, biometria); // no banco de dados esse tipo se chama BLOB
			stat.setString(2, cpf);

			stat.execute();

			retorno = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return retorno;

	}

	public byte[] get() {
		Connection conn = ConnectionFactory.getInstance();
		ResultSet rs;
		PreparedStatement st;
		byte[] digital = null;
		try {
			st = conn.prepareStatement("SELECT * FROM usuario where cpf = '02833805195' ");
			rs = st.executeQuery();
			if (rs.next())
				digital = rs.getBytes("digital");
			else
				System.out.println("Biometria não encontrado");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return digital;
	}
	List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
	
	public void setListaPessoa() {
		
		ResultSet rs;
		Connection conn = ConnectionFactory.getInstance();
		PreparedStatement stat;
		try {
			stat = conn.prepareStatement("SELECT * FROM usuario ");
			rs = stat.executeQuery();

			while (rs.next()) {
				if(rs.getBytes("digital") != null) {
					Pessoa p = new Pessoa();
					
					p.setId(rs.getInt("id"));
					p.setCpf(rs.getString("cpf"));
					p.setNome(rs.getString("nome"));
					p.setDigital(rs.getBytes("digital"));
					listaPessoa.add(p);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	public List<Pessoa> getListaPessoa(){
		if(listaPessoa == null)
			listaPessoa = new  ArrayList<Pessoa>();
		return listaPessoa;
	}

}
