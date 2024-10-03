package com.alura.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.factory.ConnectionFactory;

public class HuespedController {

	public void editar() {

	}

	public int eliminar(Integer id) throws SQLException {
		
		Connection con = new ConnectionFactory().recuperaConexion();

		Statement statement = con.createStatement();
		
		statement.execute("DELETE FROM huespedes WHERE ID = " + id);
		
		return statement.getUpdateCount();


	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();

		Statement statement = con.createStatement();

		statement.execute("SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO,"
				+ " NACIONALIDAD, TELEFONO, ID_RESERVA FROM huespedes");

		ResultSet resultSet = statement.getResultSet();

		List<Map<String, String>> resultado = new ArrayList<>();

		// Mientras haya una fila en el resulSet, podemos iterar este objeto mediante
		// resulSet.next()
		while (resultSet.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("ID")));
			fila.put("NOMBRE", resultSet.getString("NOMBRE"));
			fila.put("APELLIDO", resultSet.getString("APELLIDO"));
			fila.put("FECHA_DE_NACIMIENTO", String.valueOf(resultSet.getDate("FECHA_DE_NACIMIENTO")));
			fila.put("NACIONALIDAD", resultSet.getString("NACIONALIDAD"));
			fila.put("TELEFONO", resultSet.getString("TELEFONO"));
			fila.put("ID_RESERVA", String.valueOf(resultSet.getInt("ID_RESERVA")));

			resultado.add(fila);

		}
		;

		con.close();
		return resultado;
	}

	public void guardar(HashMap<String, String> huesped) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		Statement statement = con.createStatement();

		statement.execute(
				"INSERT INTO huespedes (NOMBRE, APELLIDO,FECHA_DE_NACIMIENTO, NACIONALIDAD, TELEFONO, ID_RESERVA) "
						+ "VALUES('" + huesped.get("NOMBRE") + "', '" + huesped.get("APELLIDO") + "', '"
						+ huesped.get("FECHA_DE_NACIMIENTO") + "', '" + huesped.get("NACIONALIDAD") + "', '"
						+ huesped.get("TELEFONO") + "', " + huesped.get("ID_RESERVA") + ")",
				Statement.RETURN_GENERATED_KEYS);

		ResultSet resultSet = statement.getGeneratedKeys();

		while (resultSet.next()) {
			System.out.println("Fue insertado el producto de ID " + resultSet.getInt(1));

		}
		con.close();
	}

	public List<Map<String, String>> busqueda(int idReserva, String apellido) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		String query = "SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO, NACIONALIDAD, TELEFONO, ID_RESERVA "
	            + "FROM huespedes WHERE ID_RESERVA = ? AND APELLIDO = ?";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setInt(1, idReserva);
		statement.setString(2, apellido);
		
		ResultSet resultSet = statement.executeQuery();
		

		List<Map<String, String>> resultado = new ArrayList<>();
		
		while (resultSet.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("ID")));
			fila.put("NOMBRE", resultSet.getString("NOMBRE"));
			fila.put("APELLIDO", resultSet.getString("APELLIDO"));
			fila.put("FECHA_DE_NACIMIENTO", String.valueOf(resultSet.getDate("FECHA_DE_NACIMIENTO")));
			fila.put("NACIONALIDAD", resultSet.getString("NACIONALIDAD"));
			fila.put("TELEFONO", resultSet.getString("TELEFONO"));
			fila.put("ID_RESERVA", String.valueOf(resultSet.getInt("ID_RESERVA")));

			resultado.add(fila);

		}
		;

		con.close();
		return resultado;
	}
}
