package principal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import factory.ConnectionFactory;

public class RegistrarBatida {

	public boolean registrar(int idUsuario) {

		String data = getData();
		String dia = getDia();
		String hora = getHora();

		boolean retorno = false;
		Connection conn = ConnectionFactory.getInstance();

		try {
			PreparedStatement stat = conn
					.prepareStatement(" INSERT INTO registro (idUsuario, data, dia, hora, tipo) VALUES ( ?, ?, ?, ?, ? ) ");
			stat.setInt(1, idUsuario);
			stat.setString(2, data);
			stat.setString(3, dia);
			stat.setString(4, hora);
			stat.setInt(5, 1);

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

	public String getData() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getHora() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public  String getDia() {
		Date d = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		String nome = "";
		int dia = c.get(c.DAY_OF_WEEK);
		switch (dia) {
		case Calendar.SUNDAY:
			nome = "Domingo";
			break;
		case Calendar.MONDAY:
			nome = "Segunda-Feira";
			break;
		case Calendar.TUESDAY:
			nome = "Terça-Feira";
			break;
		case Calendar.WEDNESDAY:
			nome = "Quarta-Feira";
			break;
		case Calendar.THURSDAY:
			nome = "Quinta-Feira";
			break;
		case Calendar.FRIDAY:
			nome = "Sexta-Feira";
			break;
		case Calendar.SATURDAY:
			nome = "sábado";
			break;
		}

		return nome;
	}

}
