package com.infotel.biblio.tools;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Book {
	private int id;
	private String isbn;
	private String description;
	private double price;
	private String title;
	private String imagePath;
	private LocalDate releaseDate;
	private int categoryId;
	private List<Integer> authorIds;
	
	public void addAuthor(int authorId) {
		if (authorIds == null) authorIds = new LinkedList<>();
		authorIds.add(authorId);
	}
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", isbn=" + isbn + ", description=" + description + ", price=" + price + ", title="
				+ title + ", imagePath=" + imagePath + ", releaseDate=" + releaseDate + ", categoryId=" + categoryId
				+ ", authorIds=" + authorIds + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public List<Integer> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<Integer> authorIds) {
		this.authorIds = authorIds;
	}
	
	
}
