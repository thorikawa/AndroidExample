/*
 * TestBar.h
 *
 *  Created on: Jul 17, 2010
 *      Author: ethan
 */

#ifndef TESTBAR_H_
#define TESTBAR_H_

#include <opencv2/opencv.hpp>

#include "image_pool.h"

using namespace std;
using namespace cv;

struct FooBarStruct {

	int pool_image_count(image_pool* pool){
		return pool->getCount();
	}

};

class BarBar{
public:
	BarBar();
	void recognizeFace(int idx, image_pool* pool);
private:
	CascadeClassifier cascade_face;
};

#endif /* TESTBAR_H_ */
