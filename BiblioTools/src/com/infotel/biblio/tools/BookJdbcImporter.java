package com.infotel.biblio.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookJdbcImporter {
	
	public void importBooks() throws Exception {
		List<Book> books =  loadBooksFromJson();
		
		try (
				Connection cn = getConnection();
			) {
			for (Book book : books) {
				importBook(book, cn);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
		
	private void importBook(Book book, Connection cn) throws Exception {
		
		String sql = "insert into book (book_id, title, description, price, publicationDate, popularBook, bookImage, bookCategory_category_id, bookEditor_editor_id) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			
			ps = cn.prepareStatement(sql);
			ps.setInt(1, book.getId());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getDescription());
			ps.setDouble(4, book.getPrice());
			ps.setString(5, book.getReleaseDate().toString());
			ps.setInt(6, 0);
			ps.setString(7, book.getImagePath());
			ps.setInt(8, book.getCategoryId());
			ps.setInt(9, 1);
			
			ps.executeUpdate();
			
			book.getAuthorIds()
			    .stream()
			    .forEach(authorId -> {
					try {
						importAuthor(book.getId(), authorId, cn);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				try {ps.close();} catch (SQLException e) {}
		}
	}
	
	private void importAuthor(int bookId, int authorId, Connection cn) throws Exception {
		
		String sql = "insert into author_book (book_id, author_id) "
				+ "values(?, ?)";
		PreparedStatement ps = null;
		try {
			
			ps = cn.prepareStatement(sql);
			ps.setInt(1, bookId);
			ps.setInt(2, authorId);
			
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
	
	private List<Book> loadBooksFromJson() throws Exception {
		List<Book> books = new LinkedList<>();
		
		Path path = Paths.get("C:\\Users\\maga\\eclipse-workspace\\ajoutBdd\\BiblioTools\\books.json");
		String json = Files.readAllLines(path)
						 	.stream()
						 	.collect(Collectors.joining());
		
		JSONArray array = new JSONArray(json);
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			Book b = new Book();
			b.setId(obj.getInt("id"));
			b.setTitle(obj.getString("title"));
			b.setDescription(obj.optString("description"));
//			b.setIsbn(obj.getString("isbn"));
			b.setPrice(obj.getDouble("price"));
			b.setReleaseDate(LocalDate.parse(obj.getString("releaseDate")));
			b.setImagePath(obj.getInt("imageId") + ".png");
			b.setCategoryId(obj.getInt("categoryId"));
			
			JSONArray authors = obj.optJSONArray("authorIds");
			if (authors != null) {
				for (int j = 0; j < authors.length(); j++) {
					b.addAuthor(authors.getInt(j));
				}
			}
			
			books.add(b);
		}
		
		return books;
	}

}
