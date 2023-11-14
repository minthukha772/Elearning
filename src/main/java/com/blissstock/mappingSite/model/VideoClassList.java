package com.blissstock.mappingSite.model;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class VideoClassList {
    Long id;
    Integer videoOrderNo;
    String thumbnail;
    String videoTitle;
    String videoDesc;
    
    
    
    public VideoClassList(Long id, Integer videoOrderNo, String thumbnail, String videoTitle, String videoDesc) {
        this.id = id;
        this.videoOrderNo = videoOrderNo;
        this.thumbnail = thumbnail;
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
    }
    

}
    

