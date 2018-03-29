package com.infotel.biblio.tools;

public class Main {
	public static void main(String[] args) throws Exception {
		new AuthorJdbcImporter().importAuthors();
		new CategoryJdbcImporter().importCategories();
		new BookJdbcImporter().importBooks();
	}
}
