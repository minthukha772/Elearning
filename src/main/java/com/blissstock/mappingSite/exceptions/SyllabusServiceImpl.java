package com.blissstock.mappingSite.exceptions;

import com.blissstock.mappingSite.entity.Content;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.service.SyllabusService;
import com.blissstock.mappingSite.service.UserSessionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    UserSessionService userSessionService;

  @Override
  public void addSyllabus(Syllabus syllabus) {
        String courseId = "";
        String teacherEmail = courseId;
        if(teacherEmail != userSessionService.getEmail()){
            throw new InsuffientPermssionException();
        }
      syllabus.getSyllabusId();
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSyllabus(Syllabus syllabus) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Syllabus> getAllSyllabus(long courseId) {
    // TODO @metion:Ma Thant, Fetch Data from database
    List<Syllabus> syllabusList = new ArrayList<Syllabus>();
    for (int i = 0; i < 4; i++) {
      Syllabus syllabus = new Syllabus();
      syllabus.setSyllabusId((long) i);
      syllabus.setTitle("Title " + i);
      syllabus.setContent(new ArrayList<Content>());
      for (int j = 0; j < 10; j++) {
        Content content = new Content();
        content.setContentId((long)j);
        content.setContent("Content " + j);
        syllabus.getContent().add(content);
      }
      syllabusList.add(syllabus);
    }

    return syllabusList;
  }

  @Override
  public void deleteSyllabus(long syllabusId) {
    // TODO Auto-generated method stub

  }
}
