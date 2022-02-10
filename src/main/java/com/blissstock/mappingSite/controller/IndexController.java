package com.blissstock.mappingSite.controller;

import org.springframework.ui.Model;
import java.util.*;

import com.blissstock.mappingSite.dto.HomeCourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;

import com.blissstock.mappingSite.enums.ClassType;
import com.blissstock.mappingSite.model.FileInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.service.StorageService;

@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(CheckEmailController.class);
    @Autowired
    UserAccountRepository userAccRepo;
    @Autowired
    private CourseInfoRepository courseRepo;
    @Autowired
    StorageService storageService;

    @GetMapping("/")
    private String getCourses(Model model) {
        try {
            logger.info("GET request");

            List<CourseInfo> liveList = courseRepo.findByClassTypeIgnoreCase(ClassType.LIVE.getValue());

            List<CourseInfo> videoList = courseRepo.findByClassTypeIgnoreCase(ClassType.VIDEO.getValue());

            List<HomeCourseInfoDTO> liveInfoDTOs = new ArrayList<HomeCourseInfoDTO>();
            List<HomeCourseInfoDTO> videoInfoDTOs = new ArrayList<HomeCourseInfoDTO>();
            ;

            for (CourseInfo info : liveList) {
                HomeCourseInfoDTO dto = new HomeCourseInfoDTO();
                dto.setCourseID(info.getCourseId());
                dto.setPrice(info.getFees());
                dto.setTeacherName(info.getUserInfo().getUserName());
                dto.setCourseName(info.getCourseName());
                FileInfo fileInfo = storageService.loadProfileAsFileInfo(info.getUserInfo());

                // if profile is not found set as place holder
                if (fileInfo == null) {
                    dto.setProfilePic(new FileInfo("https://via.placeholder.com/150",
                            "https://via.placeholder.com/150"));
                } else {

                    dto.setProfilePic(fileInfo);
                }
                liveInfoDTOs.add(dto);

            }
            for (CourseInfo info : videoList) {
                HomeCourseInfoDTO dto = new HomeCourseInfoDTO();
                dto.setCourseID(info.getCourseId());
                dto.setPrice(info.getFees());
                dto.setTeacherName(info.getUserInfo().getUserName());
                dto.setCourseName(info.getCourseName());
                FileInfo fileInfo = storageService.loadProfileAsFileInfo(info.getUserInfo());

                // if profile is not found set as place holder
                if (fileInfo == null) {
                    dto.setProfilePic(new FileInfo("https://via.placeholder.com/150",
                            "https://via.placeholder.com/150"));
                } else {

                    dto.setProfilePic(fileInfo);
                }
                videoInfoDTOs.add(dto);

            }

            model.addAttribute("liveCourse", liveInfoDTOs);
            // todo change to video list
            model.addAttribute("recordedCourse", videoInfoDTOs);

            // System.out.print(liveList.toString());
        } catch (Exception e) {
            logger.info("Exception at index controller :: {}", e.toString());
            System.out.print(e.toString());
        }

        return "index.html";
    }

}
