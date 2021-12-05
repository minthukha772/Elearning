package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import com.blissstock.mappingSite.entity.LeaveInfo;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.LeaveInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TakeALeaveController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CourseRepository courseRepo;
	
	@Autowired
	LeaveInfoRepository leaveRepo;
	
	@Valid
	@RequestMapping("/TakeALeave")
	public String TakeALeave(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name, Model model) {
		LeaveInfo leaveInfo = new LeaveInfo();
		model.addAttribute("course",course);
		model.addAttribute("name", name);
		model.addAttribute("leave", leaveInfo);
		return "ST0003_TakeALeaveScreen";
	}
	
	// @RequestMapping("/TakeALeave")
	// public String TakeALeave() {
		
		// 	return "LeaveScreen";
		// }
		
}
	