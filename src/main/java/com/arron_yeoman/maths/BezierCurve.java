package com.arron_yeoman.maths;



public class BezierCurve {
    
    private Vector4[] output;
    private Vector4[] controlPoints;
    private int numPoints;

    public BezierCurve(int numPoints, Vector4[] controlPoints){
        this.numPoints = numPoints;
        this.controlPoints = controlPoints;
        if (controlPoints != null){
            output = bezierCalc(numPoints, controlPoints);
        }
    }


    public Vector4[] bezierCalc (int numPoints, Vector4[] controlPoints) {
        float[] pointArray = genWeights(numPoints);
        Vector4[] outputPoints = new Vector4[numPoints];
        float[] xPoints = new float[controlPoints.length];
        float[] yPoints = new float[controlPoints.length];
        float[] zPoints = new float[controlPoints.length];

        for (int j=0 ; j < controlPoints.length; j++) {
            xPoints[j] = controlPoints[j].getX();
            yPoints[j] = controlPoints[j].getY();
            zPoints[j] = controlPoints[j].getZ();
        }
    
        if (controlPoints.length == 4)
        {
            for (int i = 0; i < numPoints; i++)
            {
                outputPoints[i] = new Vector4(
                    cubicBezier(xPoints, pointArray[i]), 
                    cubicBezier(yPoints, pointArray[i]),
                    cubicBezier(zPoints, pointArray[i])
                );
                //System.out.println("x: " + outputPoints[i].getX() + " Y: " + outputPoints[i].getY() + " Z: " + outputPoints[i].getZ());  
            }
        }
        else if (controlPoints.length == 3)
        {
            for (int i = 0; i < numPoints; i++)
            {
                outputPoints[i] = new Vector4(
                    quadraticBezier(xPoints, pointArray[i]), 
                    quadraticBezier(yPoints, pointArray[i]),
                    quadraticBezier(zPoints, pointArray[i]));
            }
        }
        else if (controlPoints.length == 2)
        {
            for (int i = 0; i < numPoints; i++)
            {
                outputPoints[i] = new Vector4(
                    linearInterpolate(xPoints, pointArray[i]), 
                    linearInterpolate(yPoints, pointArray[i]),
                    linearInterpolate(zPoints, pointArray[i]));
            }
        }

        return outputPoints;
    }

    private float[] genWeights(int numWeights) {
        float[] points = new float[numWeights];
        for (int k = 0; k < numWeights; k++){
            points[k]= (float)k / (float)(numWeights-1);
        }
        return points;
    }

    private float linearInterpolate (float[] w, float t) {
        return w[0] + t * (w[1] - w[0]);
    }

    private float quadraticBezier (float[] w, float t) {
        float tSquared = t * t;
        float oneMinusT = 1 - t;
        float oneMinusTSquared = oneMinusT * oneMinusT;

        return w[0]*(oneMinusTSquared) + (w[1] * (2 * oneMinusT * t)) + (w[2] * (tSquared));
    }

    private float cubicBezier (float[] w, float t) {
        float tSquared = t * t;
        float tCubed = tSquared * t;
        float oneMinusT = 1 - t;
        float oneMinusTSquared = oneMinusT * oneMinusT;
        float oneMinusTCubed = oneMinusTSquared * oneMinusT;

        return w[0] * oneMinusTCubed + (w[1] * 3 * oneMinusTSquared * t) + (w[2] * 3 * oneMinusT * tSquared) + (w[3] * tCubed);
    }
    public void setNumPoints(int numPoints){
        this.numPoints = numPoints;
    }
    public void setControlPoints(Vector4[] controls){
        this.controlPoints = controls;
    }

    public Vector4[] getOutput() {
        output = bezierCalc(this.numPoints, this.controlPoints);
        return this.output;
    }
}
