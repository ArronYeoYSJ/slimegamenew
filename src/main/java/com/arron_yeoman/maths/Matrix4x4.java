package com.arron_yeoman.maths;

public class Matrix4x4 {

private float[][] matrix;

    public Matrix4x4 () {
        this.matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.matrix[i][j] = 0.0f;
            }
        }
    }

    public static Matrix4x4 identity() {
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = 0.0f;
            }
        }
        for (int i = 0; i < 4; i++) {
            result.matrix[i][i] = 1.0f;
        }
        //result.logMatrix();
        return result;
    }

    public Matrix4x4(float[] values) {
        if (values.length != 16) {
            throw new IllegalArgumentException("Matrix4x4 must have 16 values");
        }
        this.matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.matrix[i][j] = values[(i * 4) + j];
            }
        }
    }

    public float get(int x, int y){
        return matrix[x][y];
    }

    public float[] getAll() {
        float[] values = new float[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                values[(i * 4) + j] = matrix[i][j];
            }
        }
        return values;
    }

    public static Matrix4x4 rotate(float angle, Vector4 axis){
        Matrix4x4 result = Matrix4x4.identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);
        float oneLessCos = 1.0f - cos;
        float axisX = axis.getX();
        float axisY = axis.getY();
        float axisZ = axis.getZ();
        // float length = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        // if (length != 0.0f) {
        //     axisX /= length;
        //     axisY /= length;
        //     axisZ /= length;
        // }

        result.matrix[0][0] = cos   + axisX * axisX      * oneLessCos;
        result.matrix[0][1] = axisX * axisY * oneLessCos - axisZ * sin;
        result.matrix[0][2] = axisX * axisZ * oneLessCos + axisY * sin;
        result.matrix[1][0] = axisY * axisX * oneLessCos + axisZ * sin;
        result.matrix[1][1] = cos   + axisY * axisY      * oneLessCos;
        result.matrix[1][2] = axisY * axisZ * oneLessCos - axisX * sin;
        result.matrix[2][0] = axisZ * axisX * oneLessCos - axisY * sin;
        result.matrix[2][1] = axisZ * axisY * oneLessCos + axisX * sin;
        result.matrix[2][2] = cos   + axisZ * axisZ      * oneLessCos;
        return result;
    }

    public static Matrix4x4 scale(Vector4 scale){
        Matrix4x4 result = Matrix4x4.identity();
        result.matrix[0][0] = scale.getX();
        result.matrix[1][1] = scale.getY();
        result.matrix[2][2] = scale.getZ();
        result.matrix[3][3] = scale.getW();
        return result;
    }
    
    public static Matrix4x4 translate(Vector4 translation){
        Matrix4x4 result = Matrix4x4.identity();
        result.matrix[0][3] = translation.getX();
        result.matrix[1][3] = translation.getY();
        result.matrix[2][3] = translation.getZ();
        result.matrix[3][3] = translation.getW();
        //result.logMatrix();
        return result;
    }

    //test verified
    public static Matrix4x4 multiply(Matrix4x4 matrix, Matrix4x4 other) {
        if (matrix == null || other == null) {
            throw new IllegalArgumentException("Input matrices cannot be null");
        }
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = 0.0f;    // Initialize to zero
                for (int k = 0; k < 4; k++) {  // Iterate over the row/column
                    result.matrix[i][j] += matrix.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        //result.logMatrix();
        return result;
    }

    public static Matrix4x4 transform(Vector4 position, Vector4 rotation, Vector4 scale){
        Matrix4x4 result = Matrix4x4.identity();
        Matrix4x4 translation = translate(position);
        Matrix4x4 rotationMatrix =  Matrix4x4.multiply(rotate(rotation.getX(), new Vector4(1, 0, 0)),
                                    Matrix4x4.multiply(rotate(rotation.getY(), new Vector4(0, 1, 0)),
                                    rotate(rotation.getZ(), new Vector4(0, 0, 1))));
        Matrix4x4 scaleMatrix = Matrix4x4.scale(scale);
        result = Matrix4x4.multiply(translation, Matrix4x4.multiply(rotationMatrix, scaleMatrix));
        //System.out.println("Transformed Matrix");
        //result.logMatrix();
        return result;
    }

    public static Matrix4x4 projection(float fov, float aspectRatio, float near, float far){
        Matrix4x4 result = new Matrix4x4();

        //terms are based off of this image https://i.sstatic.net/zPcST.png
        
        float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
		float range = far - near;
        //assign terms to corresponding matrix values
        result.matrix[0][0] = 1.0f / (tanFOV * aspectRatio);
        result.matrix[1][1] = 1.0f / tanFOV;
        result.matrix[2][2] = -((far + near) / range);
        result.matrix[2][3] = -((2 * far * near) / range);
        result.matrix[3][2] = -1.0f;
        result.matrix[3][3] = 0f;

        
        //System.out.println("Proj Matrix");
        //result.logMatrix();        
        return result;
    }

    public static Matrix4x4 orthographic(float left, float right, float bottom, float top, float near, float far){
        Matrix4x4 result = new Matrix4x4();
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;
        result.matrix[0][0] = 2.0f / width;
        result.matrix[1][1] = 2.0f / height;
        result.matrix[2][2] = -2.0f / depth;
        result.matrix[0][3] = -(right + left) / width;
        result.matrix[1][3] = -(top + bottom) / height;
        result.matrix[2][3] = -(far + near) / depth;
        result.matrix[3][3] = 1.0f;
        //System.out.println("Ortho Matrix");
        //result.logMatrix();
        return result;
    }

    public static Matrix4x4 view(Vector4 position, Vector4 rotation){
        Matrix4x4 result = Matrix4x4.identity();
		
		Vector4 negative = new Vector4(-position.getX(), -position.getY(), -position.getZ(), 1f);
		Matrix4x4 translationMatrix = Matrix4x4.translate(negative);
		Matrix4x4 rotXMatrix = Matrix4x4.rotate(-rotation.getX(), new Vector4(1, 0, 0));
		Matrix4x4 rotYMatrix = Matrix4x4.rotate(-rotation.getY(), new Vector4(0, 1, 0));
		Matrix4x4 rotZMatrix = Matrix4x4.rotate(-rotation.getZ(), new Vector4(0, 0, 1));
		
		Matrix4x4 rotationMatrix = Matrix4x4.multiply(rotXMatrix, Matrix4x4.multiply(rotYMatrix, rotZMatrix));
		
		result = Matrix4x4.multiply(rotationMatrix, translationMatrix) ;
        // System.out.println("View Matrix");
        //result.logMatrix();
		return result;
    }

    public Matrix4x4 transpose() {
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = matrix[j][i];
            }
        }
        return result;
    }
        

    public Matrix4x4 getMatrix() {
        return this;
    }

    public void logMatrix() {
        System.out.println("Matrix4x4");
        for (float[] row : this.matrix) {
            for (float value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

}