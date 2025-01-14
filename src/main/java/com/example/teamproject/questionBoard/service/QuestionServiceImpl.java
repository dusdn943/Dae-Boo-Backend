package com.example.teamproject.questionBoard.service;

import com.example.teamproject.answer.entity.Answer;
import com.example.teamproject.answer.repository.AnswerRepository;
import com.example.teamproject.questionBoard.dto.*;
import com.example.teamproject.questionBoard.entity.Question;
import com.example.teamproject.questionBoard.repository.QuestionRepository;
import com.example.teamproject.user.entity.User;
import com.example.teamproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Override
    public List<QuestionResponse> retrieveAllByWriter(long userId) {
        return questionRepository.findAllByWriter(userId).stream()
                .map(QuestionResponse::from)
                .toList();
    }

    @Transactional
    public QuestionDetailResponse retrieve(long questionId) {
        return questionRepository.findById(questionId)
                .map(QuestionDetailResponse::from)
                .orElseThrow(() -> new RuntimeException("해당 1:1 문의에 대한 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public QuestionDetailResponse write(QuestionWriteRequest request) {
        User writer = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("올바르지 않은 작성자입니다."));

        Question question = new Question(writer, request.getTitle(), request.getContents());
        Question savedQuestion = questionRepository.save(question);
        return QuestionDetailResponse.from(savedQuestion);
    }

    @Transactional
    public QuestionDetailResponse modify(long questionId, QuestionModifyRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("해당 1:1 문의에 대한 정보를 찾을 수 없습니다."));

        // 이미 답변이 작성되었으면 수정할 수 없다.
        if (question.isAnswerComplete()) {
            throw new RuntimeException("이미 답변이 작성된 1:1 문의라 수정할 수 없습니다.");
        }
        question.modify(request.getTitle(), request.getContents());
        return QuestionDetailResponse.from(question);
    }

    @Transactional
    public void delete(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("해당 1:1 문의에 대한 정보를 찾을 수 없습니다."));
        question.delete();
    }

    @Override
    public List<Question> list() {
        return questionRepository.findByIsDeletedFalse(Sort.by(Sort.Direction.DESC, "questionId"));
    }

    @Override
    public QuestionDetailBoardResponse read(Long questionId) {
        Optional<Question> maybeQuestion = questionRepository.findByQuestionId(questionId);

        if(maybeQuestion.isEmpty()) {
            log.info("존재하지 않는 문의 입니다.");
            return null;
        }

        Question question = maybeQuestion.get();
        return new QuestionDetailBoardResponse(question);
    }

    @Override
    public boolean save(QuestionCommentRequest request) {
        Optional<Question> maybeQuestion = questionRepository.findById(request.getQuestionId());
        if(maybeQuestion.isEmpty()){
            throw new EntityNotFoundException("존재하지 않는 문의입니다."); // 예외 발생
        }

        // 질문
        Question question = maybeQuestion.get();

        Optional<User> maybeUser = userRepository.findById(request.getUserId());
        if(maybeUser.isEmpty()){
            log.info("존재하지 않는 회원입니다.");
            return false;
        }

        // 관리자
        User user = maybeUser.get();

        // 답변
        Answer answer = new Answer(request.getAnswer(), question, user);
        answerRepository.save(answer);

        // 답변 등록 되었음을 의미
        question.setAnswerComplete(true);
        questionRepository.save(question);
        return true;
    }
}
