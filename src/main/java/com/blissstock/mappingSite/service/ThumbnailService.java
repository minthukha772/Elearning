package com.blissstock.mappingSite.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber.Exception;
import org.springframework.stereotype.Service;

@Service
public class ThumbnailService {

    public void generateThumbnail(InputStream videoFile,String thumbnailDirectory, String thumbnailName, String thumbnailFormat,
            int frameAtTime) throws IOException {

        File outputImage = new File(thumbnailDirectory + thumbnailName + "." + thumbnailFormat);
        
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile);
        grabber.start();
        grabber.setFrameNumber((int) Math.round((frameAtTime * grabber.getFrameRate())));

        Frame capturedFrame;
        Java2DFrameConverter converter = new Java2DFrameConverter();

        while ((capturedFrame = grabber.grabImage()) != null) {
            if (grabber.getTimestamp() >= frameAtTime) {
                BufferedImage bufferedImage = converter.getBufferedImage(capturedFrame);

                // Save the bufferedimage as an image file
                ImageIO.write(bufferedImage, thumbnailFormat, outputImage);

                break;
            }
        }
        
        grabber.stop();
        grabber.release();

    }
    
}
