package com.blissstock.mappingSite.interfaces;

import java.util.LinkedHashMap;

public interface Profile {
  public LinkedHashMap<String, String> toMap(boolean isSensitive);
  public LinkedHashMap<String, String> toMapStudent();
  public LinkedHashMap<String, String> toMapTeacher();
}
