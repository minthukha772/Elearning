package com.blissstock.mappingSite.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blissstock.mappingSite.entity.Exam;
import com.blissstock.mappingSite.service.ExamsService;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class ExamsController {

    @Autowired
    private ExamsService examsService;

    @Autowired
    private UserSessionService userSessionService;

    /*
     * @GetMapping("/exam")
     * public String viewExamPage(Model model) {
     * model.addAttribute("examList", examsService.getByUidAndExamid(null));
     * model.addAttribute("examQty", examsService.getExamQty(null));
     * return "ST0005_Exam";
     * }
     */
    /*
     * @GetMapping(value = "/exam")
     * public String ViewExamPage(@PathVariable Long userId, Model model) {
     * List<Exam> exams = examsService.getByUidAndExamid(userId);
     * userId = userSessionService.getUserAccount().getAccountId();
     * model.addAttribute("examList", exams);
     * model.addAttribute("examQty", examsService.getExamQty(userId));
     * return "ST0005_Exam";
     * }
     */
    @GetMapping("/exam/{userId}")
    public String getExamById(Model model, @PathVariable Long userId) {

        List<Exam> exams = examsService.getByUidAndExamid(userId);
        model.addAttribute("examList", exams);
        model.addAttribute("examQty", examsService.getExamQty(userId));

        return "ST0005_Exam";
    }

    @GetMapping("/exam")
    public String ViewRedirectPage(Model model) {
        Long uid = userSessionService.getId();

        return getExamById(model, uid);
    }

    @GetMapping("/showExamQuestions/{id}")
    public String showExamQuestions(Model model) {
        return "ExamQuestions";
    }

    @GetMapping("/showExamResults/{id}")
    public String showExamResults(Model model) {
        return "ExamResults";
    }

    @RequestMapping("/exam")
    public String returnViewExamPage(Model model) {

        return "redirect:/exam";
    }
}
