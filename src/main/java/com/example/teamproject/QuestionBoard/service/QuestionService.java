package com.example.teamproject.QuestionBoard.service;

import com.example.teamproject.QuestionBoard.dto.QuestionResponse;

import java.util.List;

public interface QuestionService {
    List<QuestionResponse> retrieveAllByWriter(long userId);
}
