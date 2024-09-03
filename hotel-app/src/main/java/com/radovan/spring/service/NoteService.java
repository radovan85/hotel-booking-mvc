package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.NoteDto;

public interface NoteService {

	NoteDto getNoteById(Integer noteId);
	
	void deleteNote(Integer noteId);
	
	List<NoteDto> listAll();
	
	List<NoteDto> listAllForToday();

	void deleteAllNotes();
}
