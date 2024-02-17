package com.alura.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HuespedController {
	
	public void editar() {
		
	}
	
	public void eliminar() {
		
	}
	
	public List<?> listar() throws SQLException{
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost/hotel_alura?useTimeZone=true&serverTimeZone=UTC",
				"root", "12345");
		
		Statement statement = con.createStatement();
		
		boolean result = statement.execute("SELECT * FROM huespedes");
		
		System.out.println(result);
		
		con.close();
		return new ArrayList<>();
	}
	
	public void guardar() {
		
	}
	
	public void busqueda() {
		
	}
}
