package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

record Order(String orderCategory, String orderItem, Double price) {

}

public class CollectionPractice {
	Connection con = null;
	PreparedStatement pstmt = null;

	public int insertOrderIntoDatabase(Order orderObj) {
		int rows = 0;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "system", "student");
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement("insert into orders values (?,?,?,?)");
			pstmt.setString(1, String.valueOf(new Random().nextInt(10, 1000)));
			pstmt.setString(2, orderObj.orderCategory());
			pstmt.setString(3, orderObj.orderItem());
			pstmt.setDouble(4, orderObj.price());

			rows = pstmt.executeUpdate();
			
			con.commit();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rows;

	}

	public static void main(String args[]) {
		HashMap<String, ArrayList<HashMap<String, Double>>> menuMap = new HashMap<String, ArrayList<HashMap<String, Double>>>();

		HashMap<String, Double> itemS1 = new HashMap<String, Double>();

		itemS1.put("Dosa", 20.00);
		itemS1.put("idli", 15.00);
		
		HashMap<String, Double> itemC1 = new HashMap<String, Double>();

		itemC1.put("Fried Rice", 200.00);
		itemC1.put("Noodles", 1500.00);


		ArrayList<HashMap<String, Double>> listSI = new ArrayList<HashMap<String, Double>>();

		listSI.add(itemS1);
		
		ArrayList<HashMap<String, Double>> listCI = new ArrayList<HashMap<String, Double>>();

		listCI.add(itemC1);

		menuMap.put("SI", listSI);
		menuMap.put("CI", listCI);

		String orderItem = "Noodles";

		double price;
		String orderCategory;

		for (String key : menuMap.keySet()) {
			orderCategory = key;
			for (HashMap<String, Double> tempMap : menuMap.get(key)) {
				for (String inkey : tempMap.keySet()) {
					if (orderItem.equalsIgnoreCase(inkey)) {
						price = tempMap.get(inkey);
						int effectedRows = new CollectionPractice().insertOrderIntoDatabase(new Order(orderCategory, orderItem, price));
						if(effectedRows>0) {
							System.out.println("Order Placed!!!!!");
						}

					}
				}
			}
		}

	}

}