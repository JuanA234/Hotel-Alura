package com.alura.controller;

import java.sql.Connection;
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
		Connection con = new ConnectionFactory().recuperaConexion();

		Statement statement = con.createStatement();

		statement.execute("SELECT ID, FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_DE_PAGO FROM reservas");

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
		con.close();
		return resultado;

	}
	
	public void guardar(Map<String, String> reserva) throws SQLException {
		 Connection con = new ConnectionFactory().recuperaConexion();
		 Statement statement = con.createStatement();
		 
		 statement.execute("INSERT INTO reservas (FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_DE_PAGO) "
		 		+ "VALUES('" + reserva.get("FECHA_ENTRADA") + "', '" 
		 		+ reserva.get("FECHA_SALIDA") + "', "
		 		+ reserva.get("VALOR") + ", '"
		 		+ reserva.get("FORMA_DE_PAGO") + "')", Statement.RETURN_GENERATED_KEYS);
		 
		 ResultSet resultSet = statement.getGeneratedKeys();
		 
		 while(resultSet.next()) {
			 System.out.println("Fue insertado el producto de ID " + resultSet.getInt(1));
			 
		 }
		 
	}
	
	public int obtenerUltimaReserva() throws SQLException {
	    int resultado = -1; // Inicializar el resultado con un valor que indique error
	    try (Connection con = new ConnectionFactory().recuperaConexion();
	         Statement statement = con.createStatement()) {
	         
	        // Ejecutar la consulta
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM reservas ORDER BY id DESC LIMIT 1;");
	        
	        // Verificar si hay resultado
	        if (resultSet.next()) {
	            resultado = resultSet.getInt("id");
	        }
	    }
	    
	    return resultado;
	}
	
}
