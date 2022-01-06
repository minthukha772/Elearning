package com.blissstock.mappingSite.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.LeaveInfo;
// import com.blissstock.mappingSite.entity.LeaveTest;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
// import com.blissstock.mappingSite.repository.CourseTestingRepository;
import com.blissstock.mappingSite.repository.LeaveInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.UserSessionService;

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

    @Autowired
    JoinCourseUserRepository joinRepo;

    @Autowired
    UserSessionService userSessionService;

	@Valid
    @GetMapping(value={"/student/leave/{courseId}","/teacher/leave/{courseId}","/admin/leave/{courseId}"})
    private String getTakeALeaveForm(@PathVariable Long courseId,@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name,Model model) {
		  LeaveInfo leaveInfo = new LeaveInfo();
      
		  model.addAttribute("course",course);
		  model.addAttribute("name", name);
      model.addAttribute("leave", leaveInfo);
      model.addAttribute("postAction", "/admin/leave/"+courseId+"/confirm");
      return "ST0003_TakeALeaveScreen";
    }

    @PostMapping(value={"/student/leave/{courseId}/confirm","/teacher/leave/{courseId}/confirm","/admin/leave/{courseId}/confirm"})
	  public String postCourseInfoForm(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name, @Valid @ModelAttribute("leave") LeaveInfo leaveInfo, BindingResult bindingResult,@PathVariable Long courseId, Model model) {
	  //if(bindingResult.hasErrors()) {
     //return "AT00001_CourseRegisteration";
        //}
        // System.out.print("Take A Leave ID in confirm screen:"+leaveInfo.getLeaveId());
      model.addAttribute("course",course);
      model.addAttribute("name", name);
      model.addAttribute("postAction", "/admin/leave/"+courseId+"/complete");
        return "ST0004_LeaveConfirmScreen";
}

@PostMapping("/admin/leave/{courseId}/complete")
  public String saveLeaveRequestForm(@RequestParam(value = "record_date", defaultValue="Japanese N3") String course, @RequestParam(value = "user_name",defaultValue = "Nani") String name,
    Model model,
    @Valid @ModelAttribute("leave") LeaveInfo leaveInfo,
    BindingResult bindingResult,
    @PathVariable Long courseId,
    @RequestParam(value="action", required=true) String action,
    HttpServletRequest request
  ) {
    
	//LeaveInfo saveLeave = new LeaveInfo(null, leaveInfo.getLeaveDate(), leaveInfo.getLeaveStartTime(), leaveInfo.getLeaveEndTime(),leaveInfo.getReason(),leaveInfo.getJoin());
  System.out.print("New Leave Request:"+leaveInfo.getLeaveDate());
    //LeaveInfo leave= leaveRepo.findById(courseId).orElse(null);
   //leaveRepo.save(leaveInfo);
 
   Long uid = userSessionService.getUserAccount().getId();



   List<JoinCourseUser> joins=joinRepo.findByCourseUser(courseId, uid);
    for(JoinCourseUser join:joins){
      leaveInfo.setJoin(join);
      leaveRepo.save(leaveInfo);
      joinRepo.save(join);
      
    }


   return "redirect:/leave/complete";

}}