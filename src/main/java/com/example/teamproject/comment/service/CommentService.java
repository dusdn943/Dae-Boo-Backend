package com.example.teamproject.comment.service;

import com.example.teamproject.comment.controller.form.RequestCommentForm;
import com.example.teamproject.comment.dto.CommentDto;
import com.example.teamproject.comment.entity.Comment;
import com.example.teamproject.user.entity.User;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentsByBoardId(Long boardId);
    Comment register(Comment comment);
    void delete(Long commentId);
    Comment modify(Long commentId, RequestCommentForm request);
    Comment createComment(CommentDto commentDto);

    List<Comment> findCommentByLoginUser(User loginUser);

    void commnetListDelete(List<Long> commentIds);
}
