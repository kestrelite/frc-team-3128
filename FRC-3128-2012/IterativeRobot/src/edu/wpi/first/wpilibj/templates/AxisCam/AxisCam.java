package edu.wpi.first.wpilibj.templates.AxisCam;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.MonoImage;
import edu.wpi.first.wpilibj.image.CurveOptions;
public class AxisCam {
    ColorImage c;
    
    private ColorImage getNext() throws AxisCameraException, NIVisionException
    {
        if(AxisCamera.getInstance().freshImage()) {return AxisCamera.getInstance().getImage();} else {return c;}
    }
    
    public void getImg() throws AxisCameraException, NIVisionException
    {
        getNext();
        MonoImage m = c.getHSLSaturationPlane();
        c.colorEqualize();
        c.luminanceEqualize();
        
    }
}