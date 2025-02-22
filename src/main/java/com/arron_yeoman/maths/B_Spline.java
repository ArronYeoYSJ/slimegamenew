package com.arron_yeoman.maths;

public class B_Spline {
    
    private Vector4[] output;
    private Vector4[] controlPoints;
    private float[] knots;
    private int degrees;

    public B_Spline(Vector4[] controlPoints, int degree){
        this.controlPoints = controlPoints;
        this.degrees = degree;
        this.knots = generateUniKnotVec(controlPoints, degree);
        
    }

    private float[] generateUniKnotVec(Vector4[] controlPoints, int degree){
        int n = controlPoints.length - 1;
        int m = n + degree + 1;
        float[] knots = new float[m + 1];
        
        for (int i = 0; i<= m; i++){
            if (i < degree){
                knots[i] = 0f;
            }
            else if (i <= m - degree){
                knots[i] = (float) (i - degree + 1) / (m - 2 * degree + 1);
            }
            else{
                knots[i] = 1f;
            }
        }
        return knots;
    }

    public Vector4 calculate(float numSlices){
        Vector4 result = new Vector4(0,0,0,1);
        int n = controlPoints.length - 1;
        for (int i = 0; i <= n; i++) {
            float b = basis(i, degrees, numSlices);
            // Multiply the control point by its basis value and add it to the result.
            result = Vector4.add(result, Vector4.multiplyByScalar(controlPoints[i], b));
        }
        // Set homogeneous coordinate to 1 for points.
        result.setW(1);
        return result;
    }


    private float basis(int i, int p, float t){
        if (p == 0){
            if (t == 1.0f && i == controlPoints.length - 1){
                return 1f;
            }
            else{
                return (knots[i] <= t && t < knots[i + 1]) ? 1f : 0f;
            }
        }
        float denom1 = knots[i+p] - knots[i];
        float term1 = 0.0f;
        if (denom1 != 0) {
            term1 = ((t - knots[i]) / denom1) * basis(i, p-1, t);
        }

        float denom2 = knots[i+p+1] - knots[i+1];
        float term2 = 0.0f;
        if (denom2 != 0) {
            term2 = ((knots[i+p+1] - t) / denom2) * basis(i+1, p-1, t);
        }

        return term1 + term2;
    }
    public Vector4[] sampleCurve(int numSamples) {
        Vector4[] samples = new Vector4[numSamples];
        // Ensure that t goes from 0 to 1 inclusive.
        for (int i = 0; i < numSamples; i++) {
            float t = i / (float)(numSamples - 1);
            samples[i] = calculate(t);
        }
        return samples;
    }

    // private int factorial(int n){
    //     if (n == 0){
    //         return 1;
    //     }
    //     else{
    //         return n * factorial(n - 1);
    //     }
    // }
}
