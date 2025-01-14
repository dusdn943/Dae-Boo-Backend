package com.example.teamproject.questionBoard.controller;

import com.example.teamproject.questionBoard.dto.*;
import com.example.teamproject.questionBoard.entity.Question;
import com.example.teamproject.questionBoard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class UserQuestionController {

    private final QuestionService questionService;

    // 내가 쓴 1:1 문의 목록 조회 API
    @GetMapping("/user/{userId}")
    public List<QuestionResponse> retrieveMyQuestionList(@PathVariable long userId) {
        return questionService.retrieveAllByWriter(userId);
    }

    // 1:1 문의 상세조회 API
    @GetMapping("/inquiry/{id}")
    public QuestionDetailResponse retrieveQuestionDetail(@PathVariable(name = "id") long questionId) {
        return questionService.retrieve(questionId);
    }

    // 1:1 문의 작성 API
    @PostMapping("/register")
    public QuestionDetailResponse writeQuestion (@RequestBody QuestionWriteRequest request) {
        return questionService.write(request);
    }

    // 1:1 문의 수정 API
    @PutMapping("/Modify/{id}")
    public QuestionDetailResponse modifyQuestion (
            @PathVariable(name = "id") long questionId,
            @RequestBody QuestionModifyRequest request
    ) {
        return questionService.modify(questionId, request);
    }

    // 1:1 문의 삭제 API
    @DeleteMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable(name = "id") long questionId) {
        questionService.delete(questionId);
    }

    // 관리자 - 1:1 게시판 불러오기
    @GetMapping("/list")
    public List<Question> managementQuestionList() {
        log.info("managementQuestionList()");
        return questionService.list();
    }

    // 관리자 - 1:1 게시판 해당 글 불러오기
    @GetMapping("/{questionId}")
    public QuestionDetailBoardResponse managementDetailQuestion(@PathVariable("questionId") Long questionId) {
        log.info("managementDetailQuestion()");
        return questionService.read(questionId);
    }

    // 관리자 - 1:1 게시판 해당 게시물에 댓글 달기
    @PostMapping("/managerAnswer")
    public boolean managementDetailQuestionAnswer(@RequestBody QuestionCommentRequest request) {
        log.info("managementDetailQuestionAnswer()");

        log.info("getQuestionId: "+ request.getQuestionId());
        log.info("getUserId: "+ request.getUserId());
        log.info("getAnswer: "+ request.getAnswer());

        return questionService.save(request);
    }
}
