package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.NoteDto;
import com.radovan.spring.entity.NoteEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.NoteRepository;
import com.radovan.spring.service.NoteService;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public NoteDto getNoteById(Integer noteId) {
		// TODO Auto-generated method stub
		NoteEntity noteEntity = noteRepository.findById(noteId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The note has not been found!")));
		return tempConverter.noteEntityToDto(noteEntity);
	}

	@Override
	public void deleteNote(Integer noteId) {
		// TODO Auto-generated method stub
		getNoteById(noteId);
		noteRepository.deleteById(noteId);
		noteRepository.flush();
	}

	@Override
	public List<NoteDto> listAll() {
		// TODO Auto-generated method stub
		List<NoteEntity> allNotes = noteRepository.findAll();
		return allNotes.stream().map(tempConverter::noteEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<NoteDto> listAllForToday() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
		LocalDateTime startOfDayLocal = now.toLocalDate().atStartOfDay();
		LocalDateTime endOfDayLocal = now.toLocalDate().atTime(23, 59, 59, 999000000);

		Timestamp startOfDay = Timestamp.valueOf(startOfDayLocal);
		Timestamp endOfDay = Timestamp.valueOf(endOfDayLocal);

		List<NoteEntity> notes = noteRepository.findAll().stream()
				.filter(note -> note.getCreatedAt().after(startOfDay) && note.getCreatedAt().before(endOfDay)).toList();

		return notes.stream().map(note -> tempConverter.noteEntityToDto(note)).collect(Collectors.toList());
	}

	@Override
	public void deleteAllNotes() {
		// TODO Auto-generated method stub
		noteRepository.deleteAll();
		noteRepository.flush();
	}

}
