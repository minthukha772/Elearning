package com.blissstock.mappingSite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blissstock.mappingSite.entity.Exam;
import com.blissstock.mappingSite.repository.ExamRepository;

@Service
public class ExamsService {

    @Autowired
    private ExamRepository examRepository;

    public List<Exam> getByUidAndExamid(Long userId) {

        return examRepository.findExamByUserId(userId);
    }

    public long getExamQty(Long userId2) {

        return examRepository.count2(userId2);
    }

}
