#include "TestBar.h"
#include <android/log.h>

//const String haarcascade_face = "/data/data/com.polysfactory.facerecognition/files/haarcascade_frontalface_alt.xml";
const String haarcascade_face = "/data/data/com.polysfactory.facerecognition/files/haarcascade_frontalface_default.xml";
const String haarcascade_eye = "/data/data/com.polysfactory.facerecognition/files/haarcascade_eye_tree_eyeglasses.xml";

BarBar::BarBar ()
{
  if(!cascade_face.load(haarcascade_face)){
    __android_log_write(ANDROID_LOG_DEBUG, "Face", "ERROR: Could not load classifier cascade");
    return;
  }
}

void BarBar::recognizeFace(int input_idx, image_pool* pool)
{
  double scale = 3.0;
  double t = 0;
  __android_log_write(ANDROID_LOG_DEBUG, "Face", "recognizeFace Start");
  Mat greyimage = pool->getGrey(input_idx);  
  Mat img = pool->getImage(input_idx);
  if (img.empty() || greyimage.empty()) {
    return;
  }
  
  //Mat small_img = cvCreateImage( cvSize( cvRound (img.rows()/scale),
  //                           cvRound (img.cols()/scale)), 8, 1 );

  // Mat target ( cvRound (img.rows/scale), cvRound(img.cols/scale), CV_8UC1 );
  Mat target = greyimage;
  
  int i=0;
  vector<Rect> faces;
  t = (double)cvGetTickCount();
  cascade_face.detectMultiScale(target, faces, 1.1, 2, 0
    //|CV_HAAR_FIND_BIGGEST_OBJECT
    //|CV_HAAR_DO_ROUGH_SEARCH
    |CV_HAAR_DO_CANNY_PRUNING
    //|CV_HAAR_SCALE_IMAGE);
    ,
    cvSize(30,30));
  
  t = (double)cvGetTickCount() - t;

  __android_log_print(ANDROID_LOG_DEBUG, "Face", "detection time = %g ms\n", t/((double)cvGetTickFrequency()*1000.) );
  __android_log_print(ANDROID_LOG_DEBUG, "Face", "%d faces detected", faces.size());
  for( vector<Rect>::const_iterator r = faces.begin(); r != faces.end(); r++, i++ )
  {
    Mat smallImgROI;
    vector<Rect> nestedObjects;

    // Scalar color = colors[i%8];
    Scalar color = CV_RGB(255,0,0);
    int radius;
    //center.x = cvRound((r->x + r->width*0.5)*scale);
    //center.y = cvRound((r->y + r->height*0.5)*scale);
    //radius = cvRound((r->width + r->height)*0.25*scale);
    Point center;
    center.x = cvRound((r->x + r->width*0.5));
    center.y = cvRound((r->y + r->height*0.5));
    Point pt1;
    pt1.x = cvRound(r->x);
    pt1.y = cvRound(r->y);
    Point pt2;
    pt2.x = cvRound(r->x + r->width);
    pt2.y = cvRound(r->y + r->height);
    radius = cvRound((r->width + r->height)*0.5);    
    //circle(img, center, radius, color, 3, 8, 0 );
    rectangle(img, pt1, pt2, color);         
    /*  Commenting eye detection part out to make it faster...
     //detecting eye
     if( nestedCascade.empty() )
     continue;
     smallImgROI = smallImg(*r);
     nestedCascade.detectMultiScale( smallImgROI, nestedObjects,
     1.1, 2, 0
     //|CV_HAAR_FIND_BIGGEST_OBJECT
     //|CV_HAAR_DO_ROUGH_SEARCH
     //|CV_HAAR_DO_CANNY_PRUNING
     |CV_HAAR_SCALE_IMAGE
     ,
     Size(30, 30) );
     
     
     for( vector<Rect>::const_iterator nr = nestedObjects.begin(); nr != nestedObjects.end(); nr++ )
     {
     center.x = cvRound((r->x + nr->x + nr->width*0.5)*scale);
     center.y = cvRound((r->y + nr->y + nr->height*0.5)*scale);
     radius = cvRound((nr->width + nr->height)*0.25*scale);
     circle( img, center, radius, color, 3, 8, 0 );
     }
     */
  }  
}
