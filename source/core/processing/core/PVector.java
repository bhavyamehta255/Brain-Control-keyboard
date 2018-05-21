package processing.core;

import java.io.Serializable;

public class PVector
  implements Serializable
{
  private static final long serialVersionUID = -6717872085945400694L;
  public float x;
  public float y;
  public float z;
  protected transient float[] array;
  
  public PVector() {}
  
  public PVector(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
  }
  
  public PVector(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = 0.0F;
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
  }
  
  public void set(PVector paramPVector)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public void set(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length >= 2)
    {
      x = paramArrayOfFloat[0];
      y = paramArrayOfFloat[1];
    }
    if (paramArrayOfFloat.length >= 3) {
      z = paramArrayOfFloat[2];
    }
  }
  
  public PVector get()
  {
    return new PVector(x, y, z);
  }
  
  public float[] get(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null) {
      return new float[] { x, y, z };
    }
    if (paramArrayOfFloat.length >= 2)
    {
      paramArrayOfFloat[0] = x;
      paramArrayOfFloat[1] = y;
    }
    if (paramArrayOfFloat.length >= 3) {
      paramArrayOfFloat[2] = z;
    }
    return paramArrayOfFloat;
  }
  
  public float mag()
  {
    return (float)Math.sqrt(x * x + y * y + z * z);
  }
  
  public void add(PVector paramPVector)
  {
    x += x;
    y += y;
    z += z;
  }
  
  public void add(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x += paramFloat1;
    y += paramFloat2;
    z += paramFloat3;
  }
  
  public static PVector add(PVector paramPVector1, PVector paramPVector2)
  {
    return add(paramPVector1, paramPVector2, null);
  }
  
  public static PVector add(PVector paramPVector1, PVector paramPVector2, PVector paramPVector3)
  {
    if (paramPVector3 == null) {
      paramPVector3 = new PVector(x + x, y + y, z + z);
    } else {
      paramPVector3.set(x + x, y + y, z + z);
    }
    return paramPVector3;
  }
  
  public void sub(PVector paramPVector)
  {
    x -= x;
    y -= y;
    z -= z;
  }
  
  public void sub(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x -= paramFloat1;
    y -= paramFloat2;
    z -= paramFloat3;
  }
  
  public static PVector sub(PVector paramPVector1, PVector paramPVector2)
  {
    return sub(paramPVector1, paramPVector2, null);
  }
  
  public static PVector sub(PVector paramPVector1, PVector paramPVector2, PVector paramPVector3)
  {
    if (paramPVector3 == null) {
      paramPVector3 = new PVector(x - x, y - y, z - z);
    } else {
      paramPVector3.set(x - x, y - y, z - z);
    }
    return paramPVector3;
  }
  
  public void mult(float paramFloat)
  {
    x *= paramFloat;
    y *= paramFloat;
    z *= paramFloat;
  }
  
  public static PVector mult(PVector paramPVector, float paramFloat)
  {
    return mult(paramPVector, paramFloat, null);
  }
  
  public static PVector mult(PVector paramPVector1, float paramFloat, PVector paramPVector2)
  {
    if (paramPVector2 == null) {
      paramPVector2 = new PVector(x * paramFloat, y * paramFloat, z * paramFloat);
    } else {
      paramPVector2.set(x * paramFloat, y * paramFloat, z * paramFloat);
    }
    return paramPVector2;
  }
  
  public void mult(PVector paramPVector)
  {
    x *= x;
    y *= y;
    z *= z;
  }
  
  public static PVector mult(PVector paramPVector1, PVector paramPVector2)
  {
    return mult(paramPVector1, paramPVector2, null);
  }
  
  public static PVector mult(PVector paramPVector1, PVector paramPVector2, PVector paramPVector3)
  {
    if (paramPVector3 == null) {
      paramPVector3 = new PVector(x * x, y * y, z * z);
    } else {
      paramPVector3.set(x * x, y * y, z * z);
    }
    return paramPVector3;
  }
  
  public void div(float paramFloat)
  {
    x /= paramFloat;
    y /= paramFloat;
    z /= paramFloat;
  }
  
  public static PVector div(PVector paramPVector, float paramFloat)
  {
    return div(paramPVector, paramFloat, null);
  }
  
  public static PVector div(PVector paramPVector1, float paramFloat, PVector paramPVector2)
  {
    if (paramPVector2 == null) {
      paramPVector2 = new PVector(x / paramFloat, y / paramFloat, z / paramFloat);
    } else {
      paramPVector2.set(x / paramFloat, y / paramFloat, z / paramFloat);
    }
    return paramPVector2;
  }
  
  public void div(PVector paramPVector)
  {
    x /= x;
    y /= y;
    z /= z;
  }
  
  public static PVector div(PVector paramPVector1, PVector paramPVector2)
  {
    return div(paramPVector1, paramPVector2, null);
  }
  
  public static PVector div(PVector paramPVector1, PVector paramPVector2, PVector paramPVector3)
  {
    if (paramPVector3 == null) {
      paramPVector3 = new PVector(x / x, y / y, z / z);
    } else {
      paramPVector3.set(x / x, y / y, z / z);
    }
    return paramPVector3;
  }
  
  public float dist(PVector paramPVector)
  {
    float f1 = x - x;
    float f2 = y - y;
    float f3 = z - z;
    return (float)Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
  }
  
  public static float dist(PVector paramPVector1, PVector paramPVector2)
  {
    float f1 = x - x;
    float f2 = y - y;
    float f3 = z - z;
    return (float)Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
  }
  
  public float dot(PVector paramPVector)
  {
    return x * x + y * y + z * z;
  }
  
  public float dot(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return x * paramFloat1 + y * paramFloat2 + z * paramFloat3;
  }
  
  public static float dot(PVector paramPVector1, PVector paramPVector2)
  {
    return x * x + y * y + z * z;
  }
  
  public PVector cross(PVector paramPVector)
  {
    return cross(paramPVector, null);
  }
  
  public PVector cross(PVector paramPVector1, PVector paramPVector2)
  {
    float f1 = y * z - y * z;
    float f2 = z * x - z * x;
    float f3 = x * y - x * y;
    if (paramPVector2 == null) {
      paramPVector2 = new PVector(f1, f2, f3);
    } else {
      paramPVector2.set(f1, f2, f3);
    }
    return paramPVector2;
  }
  
  public static PVector cross(PVector paramPVector1, PVector paramPVector2, PVector paramPVector3)
  {
    float f1 = y * z - y * z;
    float f2 = z * x - z * x;
    float f3 = x * y - x * y;
    if (paramPVector3 == null) {
      paramPVector3 = new PVector(f1, f2, f3);
    } else {
      paramPVector3.set(f1, f2, f3);
    }
    return paramPVector3;
  }
  
  public void normalize()
  {
    float f = mag();
    if ((f != 0.0F) && (f != 1.0F)) {
      div(f);
    }
  }
  
  public PVector normalize(PVector paramPVector)
  {
    if (paramPVector == null) {
      paramPVector = new PVector();
    }
    float f = mag();
    if (f > 0.0F) {
      paramPVector.set(x / f, y / f, z / f);
    } else {
      paramPVector.set(x, y, z);
    }
    return paramPVector;
  }
  
  public void limit(float paramFloat)
  {
    if (mag() > paramFloat)
    {
      normalize();
      mult(paramFloat);
    }
  }
  
  public void scaleTo(float paramFloat)
  {
    normalize();
    mult(paramFloat);
  }
  
  public PVector scaleTo(PVector paramPVector, float paramFloat)
  {
    paramPVector = normalize(paramPVector);
    paramPVector.mult(paramFloat);
    return paramPVector;
  }
  
  public float heading2D()
  {
    float f = (float)Math.atan2(-y, x);
    return -1.0F * f;
  }
  
  public void rotate(float paramFloat)
  {
    float f = x;
    x = (x * PApplet.cos(paramFloat) - y * PApplet.sin(paramFloat));
    y = (f * PApplet.sin(paramFloat) + y * PApplet.cos(paramFloat));
  }
  
  public static float angleBetween(PVector paramPVector1, PVector paramPVector2)
  {
    double d1 = x * x + y * y + z * z;
    double d2 = Math.sqrt(x * x + y * y + z * z);
    double d3 = Math.sqrt(x * x + y * y + z * z);
    double d4 = d1 / (d2 * d3);
    if (d4 <= -1.0D) {
      return 3.1415927F;
    }
    if (d4 >= 1.0D) {
      return 0.0F;
    }
    return (float)Math.acos(d4);
  }
  
  public String toString()
  {
    return "[ " + x + ", " + y + ", " + z + " ]";
  }
  
  public float[] array()
  {
    if (array == null) {
      array = new float[3];
    }
    array[0] = x;
    array[1] = y;
    array[2] = z;
    return array;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PVector)) {
      return false;
    }
    PVector localPVector = (PVector)paramObject;
    return (x == x) && (y == y) && (z == z);
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + Float.floatToIntBits(x);
    i = 31 * i + Float.floatToIntBits(y);
    i = 31 * i + Float.floatToIntBits(z);
    return i;
  }
}
