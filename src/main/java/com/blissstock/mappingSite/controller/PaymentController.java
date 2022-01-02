package com.blissstock.mappingSite.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.blissstock.mappingSite.dto.StuPaymentDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.PaymentReceive;
//import com.blissstock.mappingSite.entity.PaymentTesting;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.PaymentRepository;
//import com.blissstock.mappingSite.repository.CourseTestingRepository;
//import com.blissstock.mappingSite.repository.TpaymentRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PaymentController {
    
    @Autowired
    UserRepository userRepo;

    @Autowired
    PaymentRepository paymentRepo;

    @Autowired
    CourseRepository courseRepo;


    /*get student payment screen */
    @Valid
    @GetMapping(value="student/payment-upload/{courseId}/{userId}")
    private String getPaymentUploadForm(@PathVariable Long courseId, @PathVariable Long userId,Model model) {
		//StuPaymentDTO payment = new StuPaymentDTO();
    PaymentReceive payment = new PaymentReceive();
        model.addAttribute("payment", payment);
        model.addAttribute("postAct", "/update-payment-slip/"+courseId+"/"+userId);
        return "AS0001_StudentPayment";
    }

    /*student upload payment ss and go to payment success screen */
    /*save ss in image folder and will appear in admin payment check screen */
    @PostMapping(value = "student/update-payment-slip/{courseId}/{userId}")
    public String savePhoto(@ModelAttribute("payment") PaymentReceive inputSlip,Model model, @PathVariable Long courseId, @PathVariable Long userId,
    @Valid 
    BindingResult bindingResult,
    HttpServletRequest request,
    @RequestParam("image") MultipartFile multipartFile) throws IOException {
      CourseInfo course = courseRepo.findById(courseId).orElse(null);
        UserInfo user = userRepo.findById(userId).orElse(null);
        //TODO Set Join
 /*        inputSlip.setCourseInfo(course);
        inputSlip.setUserInfo(user); */
  inputSlip.setPaymentStatus("Pending");
 
         
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        inputSlip.setSlip(fileName);
         
        PaymentReceive saveSlip = paymentRepo.save(inputSlip);
 
        String uploadDir = "slips/" + saveSlip.getPaymentReceiveId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        model.addAttribute("payment", saveSlip);
        
        //PaymentTesting saveSlip = new PaymentTesting(null, inputSlip.getSlip(),  inputSlip.getPaymentStatus(), inputSlip.getPaymentReceiveDate());
   //paymentRepo.save(saveSlip);
        return "StuPaymentSuccess";
        //return new RedirectView("/users", true);
    }

    /*@PostMapping(value="/payment-confirm")
	public String postImageToAdminForm( @Valid @ModelAttribute("payment") StuPaymentDTO studentpayment, BindingResult bindingResult, Model model) {
	//if(bindingResult.hasErrors()) {
    //return "image";
      // }
       //studentpayment.setPaymentStatus("Pending");
        return "takealeave";
		//save-course-register
	}*/
  /* @PostMapping("/payment-success")
  public String savePaymentSlipForm(
    Model model,
    @Valid @ModelAttribute("payment") PaymentTesting inputSlip,
    BindingResult bindingResult,
    HttpServletRequest request
  ) {
   PaymentTesting saveSlip = new PaymentTesting(null, inputSlip.getSlip(),  inputSlip.getPaymentStatus(), inputSlip.getPaymentReceiveDate());
   paymentRepo.save(saveSlip);
    return "takealeave";
  }
  @Valid
    @GetMapping(value="/users")
    private String getSlipForm(Model model) {
		//StuPaymentDTO payment = new StuPaymentDTO();
    PaymentTesting payment = new PaymentTesting();
        model.addAttribute("payment", payment);
        return "users";
    }
    */

    @GetMapping(value="/edit-payment-slip/{paymentReceiveId}")
    private String stuGetPaymentSlip(@PathVariable Long paymentReceiveId,Model model) {
      PaymentReceive paymentInfo= paymentRepo.findById(paymentReceiveId).orElse(null);
      //PaymentTesting payment = new PaymentTesting();
      model.addAttribute("payment", paymentInfo);
      model.addAttribute("error", paymentInfo);
      return "AS0001_StudentPayment";
	}
    /*get admin payment check screen */
    @GetMapping(value="/admin-check-stupayment/{paymentReceiveId}")
    private String adminGetStudentPaymentSlip(@PathVariable Long paymentReceiveId,Model model) {
        PaymentReceive paymentInfo= paymentRepo.findById(paymentReceiveId).orElse(null);
        model.addAttribute("payment", paymentInfo);
        model.addAttribute("error", paymentInfo);
	return "AS0001_AdminPaymentCheck";
	}
  /*@PostMapping("/update-stupayment-status") 
  public String updatePaymentStatus(@RequestParam("paymentReceiveId") Long paymentReceiveId, @Valid @ModelAttribute("payment") PaymentTesting paymentInfo, BindingResult result, Model model,RedirectAttributes redirectAttr) { 
  PaymentTesting payment= paymentRepo.findById(paymentReceiveId).orElse(null);
  //review.setStar(reviewInfo.getStar());
  //review.setFeedback(reviewInfo.getFeedback());
  paymentRepo.save(payment);
  return "redirect:/student-review?reviewId="+paymentReceiveId;
 
 }
 30.11.2021*/ 

    /*admin click submit button and go to payment complete screen . payment status-> complete */
 @PostMapping("/update-stupayment-status") 
 /*private String adminUpdatePaymentForm(PaymentTesting inputSlip,Model model,
 @Valid 
 BindingResult bindingResult,
 HttpServletRequest request){*/
  /*private String updatePaymentStatus(@RequestParam("paymentReceiveId") Long paymentReceiveId, @Valid @ModelAttribute("payment") PaymentTesting paymentInfo, BindingResult result, Model model,RedirectAttributes redirectAttr) { */
    private String updatePaymentStatus(@RequestParam("paymentReceiveId") Long paymentReceiveId, @Valid @ModelAttribute("payment") PaymentReceive paymentInfo, HttpServletRequest request, BindingResult result, Model model) {
    PaymentReceive payment= paymentRepo.findById(paymentReceiveId).orElse(null);
    payment.setPaymentStatus("Complete");
    paymentRepo.save(payment);
    return "AdminPaymentCheckSuccess";
 }

 /*admin click request to reupload button and modal appear and go to payment error screen . payment status-> error */
 @PostMapping("/payment-error-reason") 
 private String updatePaymentError(@RequestParam("paymentReceiveId") Long paymentReceiveId,@RequestParam("paymentErrStatus") String paymentErrStatus, @Valid @ModelAttribute("error") PaymentReceive paymentInfo, HttpServletRequest request, BindingResult result, Model model){
  
  PaymentReceive errorReason= paymentRepo.findById(paymentReceiveId).orElse(null);
  //TODO inspect here
  //errorReason.setPaymentErrStatus(paymentErrStatus);
  errorReason.setPaymentStatus("Error");;
  paymentRepo.save(errorReason);
  return "AdminPaymentCheckError";
 }

}
