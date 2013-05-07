package com.reader.dao;

import java.util.List;

import com.reader.model.Reader;


public interface ReaderDao {
	public Reader getById(String id);

	public List<Reader> selectAll(int start, int limit);

	public int countAll();

	public Reader selectByUserId(String userId);

	public void add(Reader reader);

	public void delete(String id);

	public void update(Reader reader);

}
