package com.blissstock.mappingSite.service;

import java.util.Optional;

import javax.transaction.Transactional;
import com.blissstock.mappingSite.dto.JoinCourseDTO;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.UserInfo;

import org.checkerframework.checker.nullness.Opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.blissstock.mappingSite.exceptions.UserAlreadyExistException;

@Service
@Transactional
public class JoinCourseService {
    private static final Logger logger = LoggerFactory.getLogger(
            UserServiceImpl.class);

    @Autowired
    private final JoinCourseUserRepository joinCourseUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CourseInfoRepository courseRepository;

    @Autowired

    public JoinCourseService(JoinCourseUserRepository joinCourseUserRepository) {
        this.joinCourseUserRepository = joinCourseUserRepository;
    }

    public JoinCourseUser enrollStudent(JoinCourseDTO joinCourseDTO) throws Exception {
        System.out.println(joinCourseDTO.toString());
        System.out.println(joinCourseDTO.getCourseId() + " " + joinCourseDTO.getUid());

        if (this.isUserAlraedyJoined(joinCourseDTO.getUid(), joinCourseDTO.getCourseId())) {
            logger.warn("User with {} email already exists" + joinCourseDTO.getUid() + " cid"
                    + joinCourseDTO.getCourseId());
            throw new UserAlreadyExistException();
        }
        System.out.println("enroll student");
        Optional<UserInfo> getUserInfo = userRepository.findById(joinCourseDTO.getUid());

        Optional<CourseInfo> getCourseInfo = courseRepository.findById(joinCourseDTO.getCourseId());
        // System.out.println(getUserInfo.toString());
        // System.out.println(getCourseInfo.toString());
        if (getUserInfo.isPresent() && getCourseInfo.isPresent()) {
            JoinCourseUser joinCourseUser = new JoinCourseUser();
            joinCourseUser.setUserInfo(getUserInfo.get());
            joinCourseUser.setCourseInfo(getCourseInfo.get());

            // System.out.println("user is " + user.toString());
            JoinCourseUser savedJoinCourseUser = joinCourseUserRepository.save(joinCourseUser);
            return savedJoinCourseUser;
        }
        return null;
    }

    public boolean isUserAlraedyJoined(Long uid, Long courseId) {
        return joinCourseUserRepository.findByCourseUser(courseId, uid) == null;
    }

}
