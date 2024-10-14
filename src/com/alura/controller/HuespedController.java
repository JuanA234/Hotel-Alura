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

	public int editar(Map<int[], Object> cambios, Integer id) throws SQLException {
		
		Connection con = new ConnectionFactory().recuperaConexion();
		
		String[] nombresColumnas = {"", "NOMBRE", "APELLIDO", "FECHA_DE_NACIMIENTO", "NACIONALIDAD", 
				"TELEFONO", "ID_RESERVAS"};
		
		int filasActualizadas = 0;
		
		//Iterar sobre los cambios
		for(Map.Entry<int[], Object> entry: cambios.entrySet()) {
			int[] cell = entry.getKey();
			int fila = cell[0]; // Fila de la tabla
			int columna = cell[1]; // Fila de la columna
			
			Object nuevoValor = entry.getValue(); //Nuevo valor;
			
			String nombreColumna = nombresColumnas[columna];
			
			String query = "UPDATE huespedes SET " + nombreColumna + " = ? WHERE ID = ?";
			
			PreparedStatement statement = con.prepareStatement(query);
			
			if(nuevoValor instanceof String){
				statement.setString(1, (String) nuevoValor);
			}else if(nuevoValor instanceof Integer) {
				statement.setInt(1, (int) nuevoValor);
			}else if(nuevoValor instanceof Double) {
				statement.setDouble(1, (double) nuevoValor);
			}else {
				statement.setObject(1, nuevoValor); //Maneja otros tipos de datos
			}
			
			statement.setInt(2, id);
			filasActualizadas = statement.executeUpdate();
		}
		
		// Limpiar el mapa de cambios una vez que se han guardado
        cambios.clear();
    	con.close();
    	
    	return filasActualizadas;

	}

	public int eliminar(Integer id) throws SQLException {
		
		Connection con = new ConnectionFactory().recuperaConexion();

		Statement statement = con.createStatement();
		
		statement.execute("DELETE FROM huespedes WHERE ID = " + id);
		
		int cantidadEliminada = statement.getUpdateCount();
		
		con.close();
		
		return cantidadEliminada;


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
