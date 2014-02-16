#include <iostream>

#include <opencv/cv.h>
#include <opencv/highgui.h>

using namespace cv;

cv::Point2f computeIntersect(cv::Vec4i a, cv::Vec4i b)
{
int x1 = a[0], y1 = a[1], x2 = a[2], y2 = a[3], x3 = b[0], y3 = b[1], x4 = b[2], y4 = b[3];
float denom;

if (float d = ((float)(x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4)))
{
cv::Point2f pt;
pt.x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
pt.y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
return pt;
}
else
return cv::Point2f(-1, -1);
}

int main()
{

    Mat frame, edges;
    namedWindow("goal",1);
	
	frame = cv::imread("test_images/test1.png");
	cvtColor(frame, edges, CV_RGB2HSL);
	
	c
	
	GaussianBlur(edges, edges, Size(7, 7), 1.5, 1.5);
	
	imshow("goal", edges);
	
	while(!(waitKey(30) >= 0)){}
	
	Canny(edges, edges, 40, 150);
	
	imshow("goal", edges);
	
	while(!(waitKey(30) >= 0)){}
	
	std::vector<cv::Vec4i> lines;
	cv::HoughLinesP(edges, lines, 1, CV_PI/90, 1, 40, 10);
	
	Mat output = Mat::zeros(frame.rows, frame.cols, CV_32F);;
	
	for(int counter = 0; counter < lines.size(); ++counter)
	{
		cv::Vec4i v = lines[counter];
		
		cv::line(output, cv::Point(v[0], v[1]), cv::Point(v[2], v[3]), CV_RGB(0,255,255));
	}
	
	std::vector<cv::Point2f> corners;
	for (int i = 0; i < lines.size(); i++)
	{
		for (int j = i+1; j < lines.size(); j++)
		{
			cv::Point2f pt = computeIntersect(lines[i], lines[j]);
			if (pt.x >= 0 && pt.y >= 0)
			corners.push_back(pt);
		}
	}
	
	for(cv::Point2f point : corners)
	{
		//visualize points on image
		cv::circle(output, point, 3, Scalar(100, 0, 100), -1, 8); 
	}
	
	imshow("goal", output);
	
	while(!(waitKey(30) >= 0)){}

    return 0;
}