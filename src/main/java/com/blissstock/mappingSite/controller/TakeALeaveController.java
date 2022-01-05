package com.blissstock.mappingSite.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.blissstock.mappingSite.entity.LeaveInfo;
// import com.blissstock.mappingSite.entity.LeaveTest;
import com.blissstock.mappingSite.repository.CourseRepository;
// import com.blissstock.mappingSite.repository.CourseTestingRepository;
import com.blissstock.mappingSite.repository.LeaveInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TakeALeaveController {
    /*@RequestMapping(value="TakeALeave")
	public String TakeALeave(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name, Model model) {
		model.addAttribute("course",course);
		model.addAttribute("name", name);
		return "LeaveScreen";
	}*/

    // @RequestMapping("/TakeALeave")
	// public String TakeALeave() {
		
	// 	return "LeaveScreen";
	// }

	@Autowired
    UserRepository userRepo;

    // @Autowired
    // CourseTestingRepository courseTestRepo;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    LeaveInfoRepository leaveRepo;

	@Valid
    @GetMapping(value={"/student/leave/{courseId}","/teacher/leave/{courseId}","/admin/leave/{courseId}"})
    private String getTakeALeaveForm(@PathVariable Long courseId,@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name,Model model) {
		  LeaveInfo leaveInfo = new LeaveInfo();
		  model.addAttribute("course",course);
		  model.addAttribute("name", name);
      model.addAttribute("leave", leaveInfo);
      return "ST0003_TakeALeaveScreen";
    }

    @PostMapping(value="/leave-confirm")
	public String postCourseInfoForm(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name, @Valid @ModelAttribute("leave") LeaveInfo leaveInfo, BindingResult bindingResult, Model model) {
	//if(bindingResult.hasErrors()) {
     //return "AT00001_CourseRegisteration";
        //}
		model.addAttribute("course",course);
		model.addAttribute("name", name);
        return "ST0004_LeaveConfirmScreen";
}

@PostMapping("/save-leave-request")
  public String saveLeaveRequestForm(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name,
    Model model,
    @Valid @ModelAttribute("leave") LeaveInfo leaveInfo,
    BindingResult bindingResult,
    HttpServletRequest request
  ) {
	LeaveInfo saveLeave = new LeaveInfo(null, leaveInfo.getLeaveDate(), leaveInfo.getLeaveStartTime(), leaveInfo.getLeaveEndTime(),leaveInfo.getReason(),leaveInfo.getJoin());{
    //LeaveInfo leave= leaveRepo.findById(courseId).orElse(null);
   leaveRepo.save(saveLeave);
   return "redirect:/leave/complete";
  }
}}