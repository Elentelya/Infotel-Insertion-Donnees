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
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class AuthorJdbcImporter {
	
	public void importAuthors() throws Exception {
		List<Author> authors =  loadAuthorsFromJson();
		
		try (
				Connection cn = getConnection();
			) {
			for (Author author : authors) {
				importAuthor(author, cn);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
		
	private void importAuthor(Author author, Connection cn) throws Exception {
		
		String sql = "insert into author (author_id, firstname, lastname, shortBio) values(?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			
			StringTokenizer tokenizer = new StringTokenizer(author.getName(), " ");
			String firstName = null;
			String lastName = author.getName();
			String shortBio = null;
			
			try {
				firstName = tokenizer.nextToken();
				lastName = tokenizer.nextToken();
			} catch (Exception e) {}
			
			ps = cn.prepareStatement(sql);
			ps.setInt(1, author.getId());
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, shortBio);
			
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
	
	private List<Author> loadAuthorsFromJson() throws Exception {
		List<Author>  authors = new LinkedList<>();
		
		Path path = Paths.get("C:\\Users\\maga\\eclipse-workspace\\ajoutBdd\\BiblioTools\\authors.json");
		String json = Files.readAllLines(path)
						 	.stream()
						 	.collect(Collectors.joining());
		
		JSONArray array = new JSONArray(json);
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			Author author = new Author();
			author.setId(obj.getInt("id"));
			author.setName(obj.getString("name"));
			
			authors.add(author);
		}
		
		return authors;
	}
}
