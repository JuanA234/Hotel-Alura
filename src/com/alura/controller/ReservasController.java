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

public class ReservasController {

	public List<Map<String, String>> listar() throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {
			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_DE_PAGO FROM reservas");
			try (statement) {

				statement.execute();

				ResultSet resultSet = statement.getResultSet();

				List<Map<String, String>> resultado = new ArrayList<>();

				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("ID")));
					fila.put("FECHA_ENTRADA", String.valueOf(resultSet.getDate("FECHA_ENTRADA")));
					fila.put("FECHA_SALIDA", String.valueOf(resultSet.getDate("FECHA_SALIDA")));
					fila.put("VALOR", String.valueOf(resultSet.getFloat("VALOR")));
					fila.put("FORMA_DE_PAGO", resultSet.getString("FORMA_DE_PAGO"));

					resultado.add(fila);

				}
				return resultado;
			}
		}

	}

	public void guardar(HashMap<String, String> reserva) throws SQLException {
		String fechaSalida = reserva.get("FECHA_SALIDA");
		String fechaEntrada = reserva.get("FECHA_ENTRADA");
		String valor = reserva.get("VALOR");
		String formaPago = reserva.get("FORMA_DE_PAGO");

		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {
			con.setAutoCommit(false);

			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO reservas (FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_DE_PAGO) " + "VALUES(?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				ejecutarRegistro(fechaSalida, fechaEntrada, valor, formaPago, con, statement);
				con.commit();
				System.out.println("COMMIT");
			} catch (Exception e) {
				con.rollback();
				System.out.println("ROLLBACK");
			}

		}

	}

	private void ejecutarRegistro(String fechaSalida, String fechaEntrada, String valor, String formaPago,
			Connection con, PreparedStatement statement) throws SQLException {
		statement.setString(1, fechaEntrada);
		statement.setString(2, fechaSalida);
		statement.setString(3, valor);
		statement.setString(4, formaPago);

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();

		try (resultSet) {
			while (resultSet.next()) {
				System.out.println("Fue insertado el producto de ID " + resultSet.getInt(1));
			}
		}
	}

	public int obtenerUltimaReserva() throws SQLException {
		int resultado = -1; // Inicializar el resultado con un valor que indique error*
		try (Connection con = new ConnectionFactory().recuperaConexion(); Statement statement = con.createStatement()) {

			// Ejecutar la consulta
			ResultSet resultSet = statement.executeQuery("SELECT * FROM reservas ORDER BY id DESC LIMIT 1;");

			// Verificar si hay resultado
			if (resultSet.next()) {
				resultado = resultSet.getInt("id");
			}
		}

		return resultado;
	}

	public List<Map<String, String>> busqueda(int idReserva) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();

		try (con) {

			String query = "SELECT ID, FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_DE_PAGO FROM reservas "
					+ "WHERE ID = ?";

			final PreparedStatement statement = con.prepareStatement(query);

			try (statement) {

				statement.setInt(1, idReserva);

				ResultSet resultSet = statement.executeQuery();

				List<Map<String, String>> resultado = new ArrayList<>();

				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("ID")));
					fila.put("FECHA_ENTRADA", String.valueOf(resultSet.getDate("FECHA_ENTRADA")));
					fila.put("FECHA_SALIDA", String.valueOf(resultSet.getDate("FECHA_SALIDA")));
					fila.put("VALOR", String.valueOf(resultSet.getFloat("VALOR")));
					fila.put("FORMA_DE_PAGO", resultSet.getString("FORMA_DE_PAGO"));

					resultado.add(fila);

				}
				return resultado;
			}
		}
	}

}
