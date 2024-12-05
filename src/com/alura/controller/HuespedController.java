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
import com.mysql.cj.jdbc.DatabaseMetaData;

public class HuespedController {

	public int editar(Map<int[], Object> cambios, Integer id) throws SQLException{

		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {

			String[] nombresColumnas = { "", "NOMBRE", "APELLIDO", "FECHA_DE_NACIMIENTO", "NACIONALIDAD", "TELEFONO",
					"ID_RESERVAS" };

			int filasActualizadas = 0;

			// Iterar sobre los cambios
			for (Map.Entry<int[], Object> entry : cambios.entrySet()) {
				int[] cell = entry.getKey();
				int fila = cell[0]; // Fila de la tabla
				int columna = cell[1]; // Fila de la columna

				Object nuevoValor = entry.getValue(); // Nuevo valor;

				String nombreColumna = nombresColumnas[columna];

				String query = "UPDATE huespedes SET " + nombreColumna + " = ? WHERE ID = ?";

				final PreparedStatement statement = con.prepareStatement(query);

				try (statement) {
					if (nuevoValor instanceof String) {
						statement.setString(1, (String) nuevoValor);
					} else if (nuevoValor instanceof Integer) {
						statement.setInt(1, (int) nuevoValor);
					} else if (nuevoValor instanceof Double) {
						statement.setDouble(1, (double) nuevoValor);
					} else {
						statement.setObject(1, nuevoValor); // Maneja otros tipos de datos
					}

					statement.setInt(2, id);
					filasActualizadas = statement.executeUpdate();
					// Limpiar el mapa de cambios una vez que se han guardado
					cambios.clear();
			
				}
		
			}
			return filasActualizadas;
		}

	}

	public int eliminar(Integer id) throws SQLException {

		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM huespedes WHERE ID = ? ");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				int cantidadEliminada = statement.getUpdateCount();
				return cantidadEliminada;
			}

		}

	}

	public List<Map<String, String>> listar() throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {

			final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO,"
					+ " NACIONALIDAD, TELEFONO, ID_RESERVA FROM huespedes");

			try (statement) {

				statement.execute();

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
				return resultado;
			}

		}

	}

	public void guardar(HashMap<String, String> huesped) throws SQLException {

		String nombre = huesped.get("NOMBRE");
		String apellido = huesped.get("APELLIDO");
		String fechaDeNacimiento = huesped.get("FECHA_DE_NACIMIENTO");
		String nacionalidad = huesped.get("NACIONALIDAD");
		String telefono = huesped.get("TELEFONO");
		Integer idReserva = Integer.valueOf(huesped.get("ID_RESERVA"));

		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {

			con.setAutoCommit(false);

			final PreparedStatement statement = con
					.prepareStatement(
							"INSERT INTO huespedes (NOMBRE, APELLIDO,FECHA_DE_NACIMIENTO, NACIONALIDAD,"
									+ " TELEFONO, ID_RESERVA) VALUES(?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				ejecutarRegistro(nombre, apellido, fechaDeNacimiento, nacionalidad, telefono, idReserva, con,
						statement);
				con.commit();
				System.out.println("COMMIT");
			} catch (Exception e) {
				con.rollback();
				System.out.println("ROLLBACK");
			}
		}
	}

	private void ejecutarRegistro(String nombre, String apellido, String fechaDeNacimiento, String nacionalidad,
			String telefono, Integer idReserva, Connection con, PreparedStatement statement) throws SQLException {
		statement.setString(1, nombre);
		statement.setString(2, apellido);
		statement.setString(3, fechaDeNacimiento);
		statement.setString(4, nacionalidad);
		statement.setString(5, telefono);
		statement.setInt(6, idReserva);

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();

		try (resultSet) {
			while (resultSet.next()) {
				System.out.println("Fue insertado el producto de ID: " + resultSet.getInt(1));
			}
		}
	}

	public List<Map<String, String>> busqueda(int idReserva, String apellido) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {

			String query = "SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO, NACIONALIDAD, TELEFONO, ID_RESERVA "
					+ "FROM huespedes WHERE ID_RESERVA = ? AND APELLIDO = ?";

			final PreparedStatement statement = con.prepareStatement(query);
			try (statement) {

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
				return resultado;
			}
		}
	}
}
