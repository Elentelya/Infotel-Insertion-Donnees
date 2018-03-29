package com.infotel.biblio.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoryJdbcImporter {
	
	public void importCategories() throws Exception {
		List<Category> categories =  loadCategoriesFromJson();
		
		try (
				Connection cn = getConnection();
			) {
			for (Category category : categories) {
				importCategory(category, cn);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private void importCategory(Category category, Connection cn) throws Exception {
		
		String sql = "insert into category (category_id, name, description) values(?, ?, ?)";
		PreparedStatement ps = null;
		try {
			
			ps = cn.prepareStatement(sql);
			ps.setInt(1, category.getId());
			ps.setString(2, category.getName());
			ps.setString(3, category.getDescription());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				try {ps.close();} catch (SQLException e) {}
		}
	}
	
	private Connection getConnection() throws Exception {
		String url = "jdbc:mysql://localhost:3306/projetfinal";
		
		Connection cn = DriverManager.getConnection(url, "root", "");
		
		return cn;
	}
	
	private List<Category> loadCategoriesFromJson() throws Exception {
		List<Category> categories = new LinkedList<>();
		
		Path path = Paths.get("C:\\Users\\maga\\eclipse-workspace\\ajoutBdd\\BiblioTools\\categories.json");
		String json = Files.readAllLines(path)
						 	.stream()
						 	.collect(Collectors.joining());
		
		JSONArray array = new JSONArray(json);
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			Category cat = new Category();
			cat.setId(obj.getInt("id"));
			cat.setName(obj.getString("name"));
			
			categories.add(cat);
		}
		
		return categories;
	}

}
