package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestQuestionCorrectAnswer;
import com.blissstock.mappingSite.model.AnswerModel;
import com.blissstock.mappingSite.model.ChoiceModel;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswer;
import com.blissstock.mappingSite.repository.TestQuestionCorrectAnswerRepositoy;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.service.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestQuestionController {
    private static Logger logger = LoggerFactory.getLogger(
            TestQuestionController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    TestQuestionCorrectAnswerRepositoy testQuestionCorrectAnswerRepositoy;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/questions" })
    private String getQuestionsByTest(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        for (TestQuestion testQuestion : testQuestions) {
            TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                    .getCorrectAnswerByQuestion(testQuestion.getId());
            List<ChoiceModel> choices = new ArrayList<>();
            List<AnswerModel> answers = new ArrayList<>();
            JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
            JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
            for (int i = 0; i < choiceArrary.length(); i++) {
                JSONObject jsonObject = choiceArrary.getJSONObject(i);
                String data = jsonObject.getString("choice");
                choices.add(new ChoiceModel(data));
            }

            for (int i = 0; i < answerArray.length(); i++) {
                JSONObject jsonObject = answerArray.getJSONObject(i);
                Integer data = jsonObject.getInt("answer");
                answers.add(new AnswerModel(data));
            }

            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(testQuestion.getId(),
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark(),
                    answers);
            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
        }
        model.addAttribute("questionList", questionAndCorrectAnswers);
        return "AT0007_TestQuestions.html";
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}