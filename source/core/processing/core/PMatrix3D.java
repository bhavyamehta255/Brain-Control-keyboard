package processing.core;

import java.io.PrintStream;

public final class PMatrix3D
  implements PMatrix
{
  public float m00;
  public float m01;
  public float m02;
  public float m03;
  public float m10;
  public float m11;
  public float m12;
  public float m13;
  public float m20;
  public float m21;
  public float m22;
  public float m23;
  public float m30;
  public float m31;
  public float m32;
  public float m33;
  protected PMatrix3D inverseCopy;
  
  public PMatrix3D()
  {
    reset();
  }
  
  public PMatrix3D(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    set(paramFloat1, paramFloat2, paramFloat3, 0.0F, paramFloat4, paramFloat5, paramFloat6, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public PMatrix3D(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    set(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
  }
  
  public PMatrix3D(PMatrix paramPMatrix)
  {
    set(paramPMatrix);
  }
  
  public void reset()
  {
    set(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public PMatrix3D get()
  {
    PMatrix3D localPMatrix3D = new PMatrix3D();
    localPMatrix3D.set(this);
    return localPMatrix3D;
  }
  
  public float[] get(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat == null) || (paramArrayOfFloat.length != 16)) {
      paramArrayOfFloat = new float[16];
    }
    paramArrayOfFloat[0] = m00;
    paramArrayOfFloat[1] = m01;
    paramArrayOfFloat[2] = m02;
    paramArrayOfFloat[3] = m03;
    paramArrayOfFloat[4] = m10;
    paramArrayOfFloat[5] = m11;
    paramArrayOfFloat[6] = m12;
    paramArrayOfFloat[7] = m13;
    paramArrayOfFloat[8] = m20;
    paramArrayOfFloat[9] = m21;
    paramArrayOfFloat[10] = m22;
    paramArrayOfFloat[11] = m23;
    paramArrayOfFloat[12] = m30;
    paramArrayOfFloat[13] = m31;
    paramArrayOfFloat[14] = m32;
    paramArrayOfFloat[15] = m33;
    return paramArrayOfFloat;
  }
  
  public void set(PMatrix paramPMatrix)
  {
    Object localObject;
    if ((paramPMatrix instanceof PMatrix3D))
    {
      localObject = (PMatrix3D)paramPMatrix;
      set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }
    else
    {
      localObject = (PMatrix2D)paramPMatrix;
      set(m00, m01, 0.0F, m02, m10, m11, 0.0F, m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    }
  }
  
  public void set(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length == 6)
    {
      set(paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[4], paramArrayOfFloat[5]);
    }
    else if (paramArrayOfFloat.length == 16)
    {
      m00 = paramArrayOfFloat[0];
      m01 = paramArrayOfFloat[1];
      m02 = paramArrayOfFloat[2];
      m03 = paramArrayOfFloat[3];
      m10 = paramArrayOfFloat[4];
      m11 = paramArrayOfFloat[5];
      m12 = paramArrayOfFloat[6];
      m13 = paramArrayOfFloat[7];
      m20 = paramArrayOfFloat[8];
      m21 = paramArrayOfFloat[9];
      m22 = paramArrayOfFloat[10];
      m23 = paramArrayOfFloat[11];
      m30 = paramArrayOfFloat[12];
      m31 = paramArrayOfFloat[13];
      m32 = paramArrayOfFloat[14];
      m33 = paramArrayOfFloat[15];
    }
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    set(paramFloat1, paramFloat2, 0.0F, paramFloat3, paramFloat4, paramFloat5, 0.0F, paramFloat6, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    m00 = paramFloat1;
    m01 = paramFloat2;
    m02 = paramFloat3;
    m03 = paramFloat4;
    m10 = paramFloat5;
    m11 = paramFloat6;
    m12 = paramFloat7;
    m13 = paramFloat8;
    m20 = paramFloat9;
    m21 = paramFloat10;
    m22 = paramFloat11;
    m23 = paramFloat12;
    m30 = paramFloat13;
    m31 = paramFloat14;
    m32 = paramFloat15;
    m33 = paramFloat16;
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    translate(paramFloat1, paramFloat2, 0.0F);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    m03 += paramFloat1 * m00 + paramFloat2 * m01 + paramFloat3 * m02;
    m13 += paramFloat1 * m10 + paramFloat2 * m11 + paramFloat3 * m12;
    m23 += paramFloat1 * m20 + paramFloat2 * m21 + paramFloat3 * m22;
    m33 += paramFloat1 * m30 + paramFloat2 * m31 + paramFloat3 * m32;
  }
  
  public void rotate(float paramFloat)
  {
    rotateZ(paramFloat);
  }
  
  public void rotateX(float paramFloat)
  {
    float f1 = cos(paramFloat);
    float f2 = sin(paramFloat);
    apply(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, -f2, 0.0F, 0.0F, f2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void rotateY(float paramFloat)
  {
    float f1 = cos(paramFloat);
    float f2 = sin(paramFloat);
    apply(f1, 0.0F, f2, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -f2, 0.0F, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void rotateZ(float paramFloat)
  {
    float f1 = cos(paramFloat);
    float f2 = sin(paramFloat);
    apply(f1, -f2, 0.0F, 0.0F, f2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = cos(paramFloat1);
    float f2 = sin(paramFloat1);
    float f3 = 1.0F - f1;
    apply(f3 * paramFloat2 * paramFloat2 + f1, f3 * paramFloat2 * paramFloat3 - f2 * paramFloat4, f3 * paramFloat2 * paramFloat4 + f2 * paramFloat3, 0.0F, f3 * paramFloat2 * paramFloat3 + f2 * paramFloat4, f3 * paramFloat3 * paramFloat3 + f1, f3 * paramFloat3 * paramFloat4 - f2 * paramFloat2, 0.0F, f3 * paramFloat2 * paramFloat4 - f2 * paramFloat3, f3 * paramFloat3 * paramFloat4 + f2 * paramFloat2, f3 * paramFloat4 * paramFloat4 + f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void scale(float paramFloat)
  {
    scale(paramFloat, paramFloat, paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    scale(paramFloat1, paramFloat2, 1.0F);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    m00 *= paramFloat1;
    m01 *= paramFloat2;
    m02 *= paramFloat3;
    m10 *= paramFloat1;
    m11 *= paramFloat2;
    m12 *= paramFloat3;
    m20 *= paramFloat1;
    m21 *= paramFloat2;
    m22 *= paramFloat3;
    m30 *= paramFloat1;
    m31 *= paramFloat2;
    m32 *= paramFloat3;
  }
  
  public void shearX(float paramFloat)
  {
    float f = (float)Math.tan(paramFloat);
    apply(1.0F, f, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void shearY(float paramFloat)
  {
    float f = (float)Math.tan(paramFloat);
    apply(1.0F, 0.0F, 0.0F, 0.0F, f, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void apply(PMatrix paramPMatrix)
  {
    if ((paramPMatrix instanceof PMatrix2D)) {
      apply((PMatrix2D)paramPMatrix);
    } else if ((paramPMatrix instanceof PMatrix3D)) {
      apply((PMatrix3D)paramPMatrix);
    }
  }
  
  public void apply(PMatrix2D paramPMatrix2D)
  {
    apply(m00, m01, 0.0F, m02, m10, m11, 0.0F, m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void apply(PMatrix3D paramPMatrix3D)
  {
    apply(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }
  
  public void apply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    apply(paramFloat1, paramFloat2, 0.0F, paramFloat3, paramFloat4, paramFloat5, 0.0F, paramFloat6, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void apply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    float f1 = m00 * paramFloat1 + m01 * paramFloat5 + m02 * paramFloat9 + m03 * paramFloat13;
    float f2 = m00 * paramFloat2 + m01 * paramFloat6 + m02 * paramFloat10 + m03 * paramFloat14;
    float f3 = m00 * paramFloat3 + m01 * paramFloat7 + m02 * paramFloat11 + m03 * paramFloat15;
    float f4 = m00 * paramFloat4 + m01 * paramFloat8 + m02 * paramFloat12 + m03 * paramFloat16;
    float f5 = m10 * paramFloat1 + m11 * paramFloat5 + m12 * paramFloat9 + m13 * paramFloat13;
    float f6 = m10 * paramFloat2 + m11 * paramFloat6 + m12 * paramFloat10 + m13 * paramFloat14;
    float f7 = m10 * paramFloat3 + m11 * paramFloat7 + m12 * paramFloat11 + m13 * paramFloat15;
    float f8 = m10 * paramFloat4 + m11 * paramFloat8 + m12 * paramFloat12 + m13 * paramFloat16;
    float f9 = m20 * paramFloat1 + m21 * paramFloat5 + m22 * paramFloat9 + m23 * paramFloat13;
    float f10 = m20 * paramFloat2 + m21 * paramFloat6 + m22 * paramFloat10 + m23 * paramFloat14;
    float f11 = m20 * paramFloat3 + m21 * paramFloat7 + m22 * paramFloat11 + m23 * paramFloat15;
    float f12 = m20 * paramFloat4 + m21 * paramFloat8 + m22 * paramFloat12 + m23 * paramFloat16;
    float f13 = m30 * paramFloat1 + m31 * paramFloat5 + m32 * paramFloat9 + m33 * paramFloat13;
    float f14 = m30 * paramFloat2 + m31 * paramFloat6 + m32 * paramFloat10 + m33 * paramFloat14;
    float f15 = m30 * paramFloat3 + m31 * paramFloat7 + m32 * paramFloat11 + m33 * paramFloat15;
    float f16 = m30 * paramFloat4 + m31 * paramFloat8 + m32 * paramFloat12 + m33 * paramFloat16;
    m00 = f1;
    m01 = f2;
    m02 = f3;
    m03 = f4;
    m10 = f5;
    m11 = f6;
    m12 = f7;
    m13 = f8;
    m20 = f9;
    m21 = f10;
    m22 = f11;
    m23 = f12;
    m30 = f13;
    m31 = f14;
    m32 = f15;
    m33 = f16;
  }
  
  public void preApply(PMatrix2D paramPMatrix2D)
  {
    preApply(m00, m01, 0.0F, m02, m10, m11, 0.0F, m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void preApply(PMatrix3D paramPMatrix3D)
  {
    preApply(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }
  
  public void preApply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    preApply(paramFloat1, paramFloat2, 0.0F, paramFloat3, paramFloat4, paramFloat5, 0.0F, paramFloat6, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void preApply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    float f1 = paramFloat1 * m00 + paramFloat2 * m10 + paramFloat3 * m20 + paramFloat4 * m30;
    float f2 = paramFloat1 * m01 + paramFloat2 * m11 + paramFloat3 * m21 + paramFloat4 * m31;
    float f3 = paramFloat1 * m02 + paramFloat2 * m12 + paramFloat3 * m22 + paramFloat4 * m32;
    float f4 = paramFloat1 * m03 + paramFloat2 * m13 + paramFloat3 * m23 + paramFloat4 * m33;
    float f5 = paramFloat5 * m00 + paramFloat6 * m10 + paramFloat7 * m20 + paramFloat8 * m30;
    float f6 = paramFloat5 * m01 + paramFloat6 * m11 + paramFloat7 * m21 + paramFloat8 * m31;
    float f7 = paramFloat5 * m02 + paramFloat6 * m12 + paramFloat7 * m22 + paramFloat8 * m32;
    float f8 = paramFloat5 * m03 + paramFloat6 * m13 + paramFloat7 * m23 + paramFloat8 * m33;
    float f9 = paramFloat9 * m00 + paramFloat10 * m10 + paramFloat11 * m20 + paramFloat12 * m30;
    float f10 = paramFloat9 * m01 + paramFloat10 * m11 + paramFloat11 * m21 + paramFloat12 * m31;
    float f11 = paramFloat9 * m02 + paramFloat10 * m12 + paramFloat11 * m22 + paramFloat12 * m32;
    float f12 = paramFloat9 * m03 + paramFloat10 * m13 + paramFloat11 * m23 + paramFloat12 * m33;
    float f13 = paramFloat13 * m00 + paramFloat14 * m10 + paramFloat15 * m20 + paramFloat16 * m30;
    float f14 = paramFloat13 * m01 + paramFloat14 * m11 + paramFloat15 * m21 + paramFloat16 * m31;
    float f15 = paramFloat13 * m02 + paramFloat14 * m12 + paramFloat15 * m22 + paramFloat16 * m32;
    float f16 = paramFloat13 * m03 + paramFloat14 * m13 + paramFloat15 * m23 + paramFloat16 * m33;
    m00 = f1;
    m01 = f2;
    m02 = f3;
    m03 = f4;
    m10 = f5;
    m11 = f6;
    m12 = f7;
    m13 = f8;
    m20 = f9;
    m21 = f10;
    m22 = f11;
    m23 = f12;
    m30 = f13;
    m31 = f14;
    m32 = f15;
    m33 = f16;
  }
  
  public PVector mult(PVector paramPVector1, PVector paramPVector2)
  {
    if (paramPVector2 == null) {
      paramPVector2 = new PVector();
    }
    x = (m00 * x + m01 * y + m02 * z + m03);
    y = (m10 * x + m11 * y + m12 * z + m13);
    z = (m20 * x + m21 * y + m22 * z + m23);
    return paramPVector2;
  }
  
  public float[] mult(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if ((paramArrayOfFloat2 == null) || (paramArrayOfFloat2.length < 3)) {
      paramArrayOfFloat2 = new float[3];
    }
    if (paramArrayOfFloat1 == paramArrayOfFloat2) {
      throw new RuntimeException("The source and target vectors used in PMatrix3D.mult() cannot be identical.");
    }
    if (paramArrayOfFloat2.length == 3)
    {
      paramArrayOfFloat2[0] = (m00 * paramArrayOfFloat1[0] + m01 * paramArrayOfFloat1[1] + m02 * paramArrayOfFloat1[2] + m03);
      paramArrayOfFloat2[1] = (m10 * paramArrayOfFloat1[0] + m11 * paramArrayOfFloat1[1] + m12 * paramArrayOfFloat1[2] + m13);
      paramArrayOfFloat2[2] = (m20 * paramArrayOfFloat1[0] + m21 * paramArrayOfFloat1[1] + m22 * paramArrayOfFloat1[2] + m23);
    }
    else if (paramArrayOfFloat2.length > 3)
    {
      paramArrayOfFloat2[0] = (m00 * paramArrayOfFloat1[0] + m01 * paramArrayOfFloat1[1] + m02 * paramArrayOfFloat1[2] + m03 * paramArrayOfFloat1[3]);
      paramArrayOfFloat2[1] = (m10 * paramArrayOfFloat1[0] + m11 * paramArrayOfFloat1[1] + m12 * paramArrayOfFloat1[2] + m13 * paramArrayOfFloat1[3]);
      paramArrayOfFloat2[2] = (m20 * paramArrayOfFloat1[0] + m21 * paramArrayOfFloat1[1] + m22 * paramArrayOfFloat1[2] + m23 * paramArrayOfFloat1[3]);
      paramArrayOfFloat2[3] = (m30 * paramArrayOfFloat1[0] + m31 * paramArrayOfFloat1[1] + m32 * paramArrayOfFloat1[2] + m33 * paramArrayOfFloat1[3]);
    }
    return paramArrayOfFloat2;
  }
  
  public float multX(float paramFloat1, float paramFloat2)
  {
    return m00 * paramFloat1 + m01 * paramFloat2 + m03;
  }
  
  public float multY(float paramFloat1, float paramFloat2)
  {
    return m10 * paramFloat1 + m11 * paramFloat2 + m13;
  }
  
  public float multX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return m00 * paramFloat1 + m01 * paramFloat2 + m02 * paramFloat3 + m03;
  }
  
  public float multY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return m10 * paramFloat1 + m11 * paramFloat2 + m12 * paramFloat3 + m13;
  }
  
  public float multZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return m20 * paramFloat1 + m21 * paramFloat2 + m22 * paramFloat3 + m23;
  }
  
  public float multW(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return m30 * paramFloat1 + m31 * paramFloat2 + m32 * paramFloat3 + m33;
  }
  
  public float multX(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return m00 * paramFloat1 + m01 * paramFloat2 + m02 * paramFloat3 + m03 * paramFloat4;
  }
  
  public float multY(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return m10 * paramFloat1 + m11 * paramFloat2 + m12 * paramFloat3 + m13 * paramFloat4;
  }
  
  public float multZ(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return m20 * paramFloat1 + m21 * paramFloat2 + m22 * paramFloat3 + m23 * paramFloat4;
  }
  
  public float multW(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return m30 * paramFloat1 + m31 * paramFloat2 + m32 * paramFloat3 + m33 * paramFloat4;
  }
  
  public void transpose()
  {
    float f = m01;
    m01 = m10;
    m10 = f;
    f = m02;
    m02 = m20;
    m20 = f;
    f = m03;
    m03 = m30;
    m30 = f;
    f = m12;
    m12 = m21;
    m21 = f;
    f = m13;
    m13 = m31;
    m31 = f;
    f = m23;
    m23 = m32;
    m32 = f;
  }
  
  public boolean invert()
  {
    float f1 = determinant();
    if (f1 == 0.0F) {
      return false;
    }
    float f2 = determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33);
    float f3 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33);
    float f4 = determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33);
    float f5 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32);
    float f6 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33);
    float f7 = determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33);
    float f8 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33);
    float f9 = determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32);
    float f10 = determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33);
    float f11 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33);
    float f12 = determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33);
    float f13 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32);
    float f14 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23);
    float f15 = determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23);
    float f16 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23);
    float f17 = determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    m00 = (f2 / f1);
    m01 = (f6 / f1);
    m02 = (f10 / f1);
    m03 = (f14 / f1);
    m10 = (f3 / f1);
    m11 = (f7 / f1);
    m12 = (f11 / f1);
    m13 = (f15 / f1);
    m20 = (f4 / f1);
    m21 = (f8 / f1);
    m22 = (f12 / f1);
    m23 = (f16 / f1);
    m30 = (f5 / f1);
    m31 = (f9 / f1);
    m32 = (f13 / f1);
    m33 = (f17 / f1);
    return true;
  }
  
  private float determinant3x3(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    return paramFloat1 * (paramFloat5 * paramFloat9 - paramFloat6 * paramFloat8) + paramFloat2 * (paramFloat6 * paramFloat7 - paramFloat4 * paramFloat9) + paramFloat3 * (paramFloat4 * paramFloat8 - paramFloat5 * paramFloat7);
  }
  
  public float determinant()
  {
    float f = m00 * (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33);
    f -= m01 * (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32 - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33);
    f += m02 * (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31 - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33);
    f -= m03 * (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31 - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);
    return f;
  }
  
  protected void invTranslate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    preApply(1.0F, 0.0F, 0.0F, -paramFloat1, 0.0F, 1.0F, 0.0F, -paramFloat2, 0.0F, 0.0F, 1.0F, -paramFloat3, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected void invRotateX(float paramFloat)
  {
    float f1 = cos(-paramFloat);
    float f2 = sin(-paramFloat);
    preApply(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, -f2, 0.0F, 0.0F, f2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected void invRotateY(float paramFloat)
  {
    float f1 = cos(-paramFloat);
    float f2 = sin(-paramFloat);
    preApply(f1, 0.0F, f2, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -f2, 0.0F, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected void invRotateZ(float paramFloat)
  {
    float f1 = cos(-paramFloat);
    float f2 = sin(-paramFloat);
    preApply(f1, -f2, 0.0F, 0.0F, f2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected void invRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = cos(-paramFloat1);
    float f2 = sin(-paramFloat1);
    float f3 = 1.0F - f1;
    preApply(f3 * paramFloat2 * paramFloat2 + f1, f3 * paramFloat2 * paramFloat3 - f2 * paramFloat4, f3 * paramFloat2 * paramFloat4 + f2 * paramFloat3, 0.0F, f3 * paramFloat2 * paramFloat3 + f2 * paramFloat4, f3 * paramFloat3 * paramFloat3 + f1, f3 * paramFloat3 * paramFloat4 - f2 * paramFloat2, 0.0F, f3 * paramFloat2 * paramFloat4 - f2 * paramFloat3, f3 * paramFloat3 * paramFloat4 + f2 * paramFloat2, f3 * paramFloat4 * paramFloat4 + f1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected void invScale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    preApply(1.0F / paramFloat1, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / paramFloat2, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / paramFloat3, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  protected boolean invApply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    if (inverseCopy == null) {
      inverseCopy = new PMatrix3D();
    }
    inverseCopy.set(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
    if (!inverseCopy.invert()) {
      return false;
    }
    preApply(inverseCopy);
    return true;
  }
  
  public void print()
  {
    int i = (int)Math.abs(max(max(max(max(abs(m00), abs(m01)), max(abs(m02), abs(m03))), max(max(abs(m10), abs(m11)), max(abs(m12), abs(m13)))), max(max(max(abs(m20), abs(m21)), max(abs(m22), abs(m23))), max(max(abs(m30), abs(m31)), max(abs(m32), abs(m33))))));
    int j = 1;
    if ((Float.isNaN(i)) || (Float.isInfinite(i))) {
      j = 5;
    } else {
      while (i /= 10 != 0) {
        j++;
      }
    }
    System.out.println(PApplet.nfs(m00, j, 4) + " " + PApplet.nfs(m01, j, 4) + " " + PApplet.nfs(m02, j, 4) + " " + PApplet.nfs(m03, j, 4));
    System.out.println(PApplet.nfs(m10, j, 4) + " " + PApplet.nfs(m11, j, 4) + " " + PApplet.nfs(m12, j, 4) + " " + PApplet.nfs(m13, j, 4));
    System.out.println(PApplet.nfs(m20, j, 4) + " " + PApplet.nfs(m21, j, 4) + " " + PApplet.nfs(m22, j, 4) + " " + PApplet.nfs(m23, j, 4));
    System.out.println(PApplet.nfs(m30, j, 4) + " " + PApplet.nfs(m31, j, 4) + " " + PApplet.nfs(m32, j, 4) + " " + PApplet.nfs(m33, j, 4));
    System.out.println();
  }
  
  private final float max(float paramFloat1, float paramFloat2)
  {
    return paramFloat1 > paramFloat2 ? paramFloat1 : paramFloat2;
  }
  
  private final float abs(float paramFloat)
  {
    return paramFloat < 0.0F ? -paramFloat : paramFloat;
  }
  
  private final float sin(float paramFloat)
  {
    return (float)Math.sin(paramFloat);
  }
  
  private final float cos(float paramFloat)
  {
    return (float)Math.cos(paramFloat);
  }
}
