package processing.core;

import java.io.PrintStream;

public class PMatrix2D
  implements PMatrix
{
  public float m00;
  public float m01;
  public float m02;
  public float m10;
  public float m11;
  public float m12;
  
  public PMatrix2D()
  {
    reset();
  }
  
  public PMatrix2D(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    set(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public PMatrix2D(PMatrix paramPMatrix)
  {
    set(paramPMatrix);
  }
  
  public void reset()
  {
    set(1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
  }
  
  public PMatrix2D get()
  {
    PMatrix2D localPMatrix2D = new PMatrix2D();
    localPMatrix2D.set(this);
    return localPMatrix2D;
  }
  
  public float[] get(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat == null) || (paramArrayOfFloat.length != 6)) {
      paramArrayOfFloat = new float[6];
    }
    paramArrayOfFloat[0] = m00;
    paramArrayOfFloat[1] = m01;
    paramArrayOfFloat[2] = m02;
    paramArrayOfFloat[3] = m10;
    paramArrayOfFloat[4] = m11;
    paramArrayOfFloat[5] = m12;
    return paramArrayOfFloat;
  }
  
  public void set(PMatrix paramPMatrix)
  {
    if ((paramPMatrix instanceof PMatrix2D))
    {
      PMatrix2D localPMatrix2D = (PMatrix2D)paramPMatrix;
      set(m00, m01, m02, m10, m11, m12);
    }
    else
    {
      throw new IllegalArgumentException("PMatrix2D.set() only accepts PMatrix2D objects.");
    }
  }
  
  public void set(PMatrix3D paramPMatrix3D) {}
  
  public void set(float[] paramArrayOfFloat)
  {
    m00 = paramArrayOfFloat[0];
    m01 = paramArrayOfFloat[1];
    m02 = paramArrayOfFloat[2];
    m10 = paramArrayOfFloat[3];
    m11 = paramArrayOfFloat[4];
    m12 = paramArrayOfFloat[5];
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    m00 = paramFloat1;
    m01 = paramFloat2;
    m02 = paramFloat3;
    m10 = paramFloat4;
    m11 = paramFloat5;
    m12 = paramFloat6;
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16) {}
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    m02 = (paramFloat1 * m00 + paramFloat2 * m01 + m02);
    m12 = (paramFloat1 * m10 + paramFloat2 * m11 + m12);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    throw new IllegalArgumentException("Cannot use translate(x, y, z) on a PMatrix2D.");
  }
  
  public void rotate(float paramFloat)
  {
    float f1 = sin(paramFloat);
    float f2 = cos(paramFloat);
    float f3 = m00;
    float f4 = m01;
    m00 = (f2 * f3 + f1 * f4);
    m01 = (-f1 * f3 + f2 * f4);
    f3 = m10;
    f4 = m11;
    m10 = (f2 * f3 + f1 * f4);
    m11 = (-f1 * f3 + f2 * f4);
  }
  
  public void rotateX(float paramFloat)
  {
    throw new IllegalArgumentException("Cannot use rotateX() on a PMatrix2D.");
  }
  
  public void rotateY(float paramFloat)
  {
    throw new IllegalArgumentException("Cannot use rotateY() on a PMatrix2D.");
  }
  
  public void rotateZ(float paramFloat)
  {
    rotate(paramFloat);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    throw new IllegalArgumentException("Cannot use this version of rotate() on a PMatrix2D.");
  }
  
  public void scale(float paramFloat)
  {
    scale(paramFloat, paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    m00 *= paramFloat1;
    m01 *= paramFloat2;
    m10 *= paramFloat1;
    m11 *= paramFloat2;
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    throw new IllegalArgumentException("Cannot use this version of scale() on a PMatrix2D.");
  }
  
  public void shearX(float paramFloat)
  {
    apply(1.0F, 0.0F, 1.0F, tan(paramFloat), 0.0F, 0.0F);
  }
  
  public void shearY(float paramFloat)
  {
    apply(1.0F, 0.0F, 1.0F, 0.0F, tan(paramFloat), 0.0F);
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
    apply(m00, m01, m02, m10, m11, m12);
  }
  
  public void apply(PMatrix3D paramPMatrix3D)
  {
    throw new IllegalArgumentException("Cannot use apply(PMatrix3D) on a PMatrix2D.");
  }
  
  public void apply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = m00;
    float f2 = m01;
    m00 = (paramFloat1 * f1 + paramFloat4 * f2);
    m01 = (paramFloat2 * f1 + paramFloat5 * f2);
    m02 += paramFloat3 * f1 + paramFloat6 * f2;
    f1 = m10;
    f2 = m11;
    m10 = (paramFloat1 * f1 + paramFloat4 * f2);
    m11 = (paramFloat2 * f1 + paramFloat5 * f2);
    m12 += paramFloat3 * f1 + paramFloat6 * f2;
  }
  
  public void apply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    throw new IllegalArgumentException("Cannot use this version of apply() on a PMatrix2D.");
  }
  
  public void preApply(PMatrix2D paramPMatrix2D)
  {
    preApply(m00, m01, m02, m10, m11, m12);
  }
  
  public void preApply(PMatrix3D paramPMatrix3D)
  {
    throw new IllegalArgumentException("Cannot use preApply(PMatrix3D) on a PMatrix2D.");
  }
  
  public void preApply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = m02;
    float f2 = m12;
    paramFloat3 += f1 * paramFloat1 + f2 * paramFloat2;
    paramFloat6 += f1 * paramFloat4 + f2 * paramFloat5;
    m02 = paramFloat3;
    m12 = paramFloat6;
    f1 = m00;
    f2 = m10;
    m00 = (f1 * paramFloat1 + f2 * paramFloat2);
    m10 = (f1 * paramFloat4 + f2 * paramFloat5);
    f1 = m01;
    f2 = m11;
    m01 = (f1 * paramFloat1 + f2 * paramFloat2);
    m11 = (f1 * paramFloat4 + f2 * paramFloat5);
  }
  
  public void preApply(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    throw new IllegalArgumentException("Cannot use this version of preApply() on a PMatrix2D.");
  }
  
  public PVector mult(PVector paramPVector1, PVector paramPVector2)
  {
    if (paramPVector2 == null) {
      paramPVector2 = new PVector();
    }
    x = (m00 * x + m01 * y + m02);
    y = (m10 * x + m11 * y + m12);
    return paramPVector2;
  }
  
  public float[] mult(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if ((paramArrayOfFloat2 == null) || (paramArrayOfFloat2.length != 2)) {
      paramArrayOfFloat2 = new float[2];
    }
    if (paramArrayOfFloat1 == paramArrayOfFloat2)
    {
      float f1 = m00 * paramArrayOfFloat1[0] + m01 * paramArrayOfFloat1[1] + m02;
      float f2 = m10 * paramArrayOfFloat1[0] + m11 * paramArrayOfFloat1[1] + m12;
      paramArrayOfFloat2[0] = f1;
      paramArrayOfFloat2[1] = f2;
    }
    else
    {
      paramArrayOfFloat2[0] = (m00 * paramArrayOfFloat1[0] + m01 * paramArrayOfFloat1[1] + m02);
      paramArrayOfFloat2[1] = (m10 * paramArrayOfFloat1[0] + m11 * paramArrayOfFloat1[1] + m12);
    }
    return paramArrayOfFloat2;
  }
  
  public float multX(float paramFloat1, float paramFloat2)
  {
    return m00 * paramFloat1 + m01 * paramFloat2 + m02;
  }
  
  public float multY(float paramFloat1, float paramFloat2)
  {
    return m10 * paramFloat1 + m11 * paramFloat2 + m12;
  }
  
  public void transpose() {}
  
  public boolean invert()
  {
    float f1 = determinant();
    if (Math.abs(f1) <= Float.MIN_VALUE) {
      return false;
    }
    float f2 = m00;
    float f3 = m01;
    float f4 = m02;
    float f5 = m10;
    float f6 = m11;
    float f7 = m12;
    m00 = (f6 / f1);
    m10 = (-f5 / f1);
    m01 = (-f3 / f1);
    m11 = (f2 / f1);
    m02 = ((f3 * f7 - f6 * f4) / f1);
    m12 = ((f5 * f4 - f2 * f7) / f1);
    return true;
  }
  
  public float determinant()
  {
    return m00 * m11 - m01 * m10;
  }
  
  public void print()
  {
    int i = (int)abs(max(PApplet.max(abs(m00), abs(m01), abs(m02)), PApplet.max(abs(m10), abs(m11), abs(m12))));
    int j = 1;
    if ((Float.isNaN(i)) || (Float.isInfinite(i))) {
      j = 5;
    } else {
      while (i /= 10 != 0) {
        j++;
      }
    }
    System.out.println(PApplet.nfs(m00, j, 4) + " " + PApplet.nfs(m01, j, 4) + " " + PApplet.nfs(m02, j, 4));
    System.out.println(PApplet.nfs(m10, j, 4) + " " + PApplet.nfs(m11, j, 4) + " " + PApplet.nfs(m12, j, 4));
    System.out.println();
  }
  
  protected boolean isIdentity()
  {
    return (m00 == 1.0F) && (m01 == 0.0F) && (m02 == 0.0F) && (m10 == 0.0F) && (m11 == 1.0F) && (m12 == 0.0F);
  }
  
  protected boolean isWarped()
  {
    return (m00 != 1.0F) || ((m01 != 0.0F) && (m10 != 0.0F)) || (m11 != 1.0F);
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
  
  private final float tan(float paramFloat)
  {
    return (float)Math.tan(paramFloat);
  }
}
