package com.blissstock.mappingSite.service;

import java.util.List;

import com.blissstock.mappingSite.entity.Syllabus;


public interface SyllabusService {

    public void addSyllabus(Syllabus syllabus);
    public void updateSyllabus(Syllabus syllabus);
    public List<Syllabus> getAllSyllabus(long id);
    public void deleteSyllabus(long syllabusId);
    
}
