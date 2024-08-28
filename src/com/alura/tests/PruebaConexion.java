package com.alura.tests;

import java.sql.Connection;
import java.sql.SQLException;

import com.alura.factory.ConnectionFactory;

public class PruebaConexion {
	public static void main(String[] args) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		System.out.println("Cerrando la Conexión");
		con.close();
	}
}
