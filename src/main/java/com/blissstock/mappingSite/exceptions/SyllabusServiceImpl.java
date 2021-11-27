package com.blissstock.mappingSite.exceptions;

import com.blissstock.mappingSite.entity.Content;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.service.SyllabusService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SyllabusServiceImpl implements SyllabusService {

  @Override
  public void addSyllabus(Syllabus syllabus) {
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
