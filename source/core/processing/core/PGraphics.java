package processing.core;

import java.awt.Color;
import java.awt.Image;
import java.io.PrintStream;
import java.util.HashMap;

public class PGraphics
  extends PImage
  implements PConstants
{
  protected int width1;
  protected int height1;
  public int pixelCount;
  public boolean smooth = false;
  protected boolean settingsInited;
  protected PGraphics raw;
  protected String path;
  protected boolean primarySurface;
  protected boolean[] hints = new boolean[10];
  public int colorMode;
  public float colorModeX;
  public float colorModeY;
  public float colorModeZ;
  public float colorModeA;
  boolean colorModeScale;
  boolean colorModeDefault;
  public boolean tint;
  public int tintColor;
  protected boolean tintAlpha;
  protected float tintR;
  protected float tintG;
  protected float tintB;
  protected float tintA;
  protected int tintRi;
  protected int tintGi;
  protected int tintBi;
  protected int tintAi;
  public boolean fill;
  public int fillColor = -1;
  protected boolean fillAlpha;
  protected float fillR;
  protected float fillG;
  protected float fillB;
  protected float fillA;
  protected int fillRi;
  protected int fillGi;
  protected int fillBi;
  protected int fillAi;
  public boolean stroke;
  public int strokeColor = -16777216;
  protected boolean strokeAlpha;
  protected float strokeR;
  protected float strokeG;
  protected float strokeB;
  protected float strokeA;
  protected int strokeRi;
  protected int strokeGi;
  protected int strokeBi;
  protected int strokeAi;
  protected static final float DEFAULT_STROKE_WEIGHT = 1.0F;
  protected static final int DEFAULT_STROKE_JOIN = 8;
  protected static final int DEFAULT_STROKE_CAP = 2;
  public float strokeWeight = 1.0F;
  public int strokeJoin = 8;
  public int strokeCap = 2;
  public int rectMode;
  public int ellipseMode;
  public int shapeMode;
  public int imageMode = 0;
  public PFont textFont;
  public int textAlign = 37;
  public int textAlignY = 0;
  public int textMode = 4;
  public float textSize;
  public float textLeading;
  public float ambientR;
  public float ambientG;
  public float ambientB;
  public float specularR;
  public float specularG;
  public float specularB;
  public float emissiveR;
  public float emissiveG;
  public float emissiveB;
  public float shininess;
  static final int STYLE_STACK_DEPTH = 64;
  PStyle[] styleStack = new PStyle[64];
  int styleStackDepth;
  public int backgroundColor = -3355444;
  protected boolean backgroundAlpha;
  protected float backgroundR;
  protected float backgroundG;
  protected float backgroundB;
  protected float backgroundA;
  protected int backgroundRi;
  protected int backgroundGi;
  protected int backgroundBi;
  protected int backgroundAi;
  static final int MATRIX_STACK_DEPTH = 32;
  public Image image;
  protected float calcR;
  protected float calcG;
  protected float calcB;
  protected float calcA;
  protected int calcRi;
  protected int calcGi;
  protected int calcBi;
  protected int calcAi;
  protected int calcColor;
  protected boolean calcAlpha;
  int cacheHsbKey;
  float[] cacheHsbValue = new float[3];
  protected int shape;
  public static final int DEFAULT_VERTICES = 512;
  protected float[][] vertices = new float['Ȁ'][37];
  protected int vertexCount;
  protected boolean bezierInited = false;
  public int bezierDetail = 20;
  protected PMatrix3D bezierBasisMatrix = new PMatrix3D(-1.0F, 3.0F, -3.0F, 1.0F, 3.0F, -6.0F, 3.0F, 0.0F, -3.0F, 3.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F);
  protected PMatrix3D bezierDrawMatrix;
  protected boolean curveInited = false;
  protected int curveDetail = 20;
  public float curveTightness = 0.0F;
  protected PMatrix3D curveBasisMatrix;
  protected PMatrix3D curveDrawMatrix;
  protected PMatrix3D bezierBasisInverse;
  protected PMatrix3D curveToBezierMatrix;
  protected float[][] curveVertices;
  protected int curveVertexCount;
  protected static final float[] sinLUT = new float['ː'];
  protected static final float[] cosLUT = new float['ː'];
  protected static final float SINCOS_PRECISION = 0.5F;
  protected static final int SINCOS_LENGTH = 720;
  protected float textX;
  protected float textY;
  protected float textZ;
  protected char[] textBuffer = new char[' '];
  protected char[] textWidthBuffer = new char[' '];
  protected int textBreakCount;
  protected int[] textBreakStart;
  protected int[] textBreakStop;
  public boolean edge = true;
  protected static final int NORMAL_MODE_AUTO = 0;
  protected static final int NORMAL_MODE_SHAPE = 1;
  protected static final int NORMAL_MODE_VERTEX = 2;
  protected int normalMode;
  protected boolean autoNormal;
  public float normalX;
  public float normalY;
  public float normalZ;
  public int textureMode;
  public float textureU;
  public float textureV;
  public PImage textureImage;
  float[] sphereX;
  float[] sphereY;
  float[] sphereZ;
  public int sphereDetailU = 0;
  public int sphereDetailV = 0;
  static float[] lerpColorHSB1;
  static float[] lerpColorHSB2;
  protected static HashMap<String, Object> warnings;
  
  public PGraphics() {}
  
  public void setParent(PApplet paramPApplet)
  {
    parent = paramPApplet;
  }
  
  public void setPrimary(boolean paramBoolean)
  {
    primarySurface = paramBoolean;
    if (primarySurface) {
      format = 1;
    }
  }
  
  public void setPath(String paramString)
  {
    path = paramString;
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    width = paramInt1;
    height = paramInt2;
    width1 = (width - 1);
    height1 = (height - 1);
    allocate();
    reapplySettings();
  }
  
  protected void allocate() {}
  
  public void dispose() {}
  
  public boolean canDraw()
  {
    return true;
  }
  
  public void beginDraw() {}
  
  public void endDraw() {}
  
  public void flush() {}
  
  protected void checkSettings()
  {
    if (!settingsInited) {
      defaultSettings();
    }
  }
  
  protected void defaultSettings()
  {
    noSmooth();
    colorMode(1, 255.0F);
    fill(255);
    stroke(0);
    strokeWeight(1.0F);
    strokeJoin(8);
    strokeCap(2);
    shape = 0;
    rectMode(0);
    ellipseMode(3);
    autoNormal = true;
    textFont = null;
    textSize = 12.0F;
    textLeading = 14.0F;
    textAlign = 37;
    textMode = 4;
    if (primarySurface) {
      background(backgroundColor);
    }
    settingsInited = true;
  }
  
  protected void reapplySettings()
  {
    if (!settingsInited) {
      return;
    }
    colorMode(colorMode, colorModeX, colorModeY, colorModeZ);
    if (fill) {
      fill(fillColor);
    } else {
      noFill();
    }
    if (stroke)
    {
      stroke(strokeColor);
      strokeWeight(strokeWeight);
      strokeCap(strokeCap);
      strokeJoin(strokeJoin);
    }
    else
    {
      noStroke();
    }
    if (tint) {
      tint(tintColor);
    } else {
      noTint();
    }
    if (smooth) {
      smooth();
    } else {
      noSmooth();
    }
    if (textFont != null)
    {
      float f = textLeading;
      textFont(textFont, textSize);
      textLeading(f);
    }
    textMode(textMode);
    textAlign(textAlign, textAlignY);
    background(backgroundColor);
  }
  
  public void hint(int paramInt)
  {
    if (paramInt > 0) {
      hints[paramInt] = true;
    } else {
      hints[(-paramInt)] = false;
    }
  }
  
  public boolean hintEnabled(int paramInt)
  {
    if (paramInt > 0) {
      return hints[paramInt];
    }
    return hints[(-paramInt)];
  }
  
  public void beginShape()
  {
    beginShape(20);
  }
  
  public void beginShape(int paramInt)
  {
    shape = paramInt;
  }
  
  public void edge(boolean paramBoolean)
  {
    edge = paramBoolean;
  }
  
  public void normal(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    normalX = paramFloat1;
    normalY = paramFloat2;
    normalZ = paramFloat3;
    if (shape != 0) {
      if (normalMode == 0) {
        normalMode = 1;
      } else if (normalMode == 1) {
        normalMode = 2;
      }
    }
  }
  
  public void textureMode(int paramInt)
  {
    textureMode = paramInt;
  }
  
  public void texture(PImage paramPImage)
  {
    textureImage = paramPImage;
  }
  
  public void noTexture()
  {
    textureImage = null;
  }
  
  protected void vertexCheck()
  {
    if (vertexCount == vertices.length)
    {
      float[][] arrayOfFloat = new float[vertexCount << 1][37];
      System.arraycopy(vertices, 0, arrayOfFloat, 0, vertexCount);
      vertices = arrayOfFloat;
    }
  }
  
  public void vertex(float paramFloat1, float paramFloat2)
  {
    vertexCheck();
    float[] arrayOfFloat = vertices[vertexCount];
    curveVertexCount = 0;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[12] = (edge ? 1.0F : 0.0F);
    int i = textureImage != null ? 1 : 0;
    if ((fill) || (i != 0)) {
      if (textureImage == null)
      {
        arrayOfFloat[3] = fillR;
        arrayOfFloat[4] = fillG;
        arrayOfFloat[5] = fillB;
        arrayOfFloat[6] = fillA;
      }
      else if (tint)
      {
        arrayOfFloat[3] = tintR;
        arrayOfFloat[4] = tintG;
        arrayOfFloat[5] = tintB;
        arrayOfFloat[6] = tintA;
      }
      else
      {
        arrayOfFloat[3] = 1.0F;
        arrayOfFloat[4] = 1.0F;
        arrayOfFloat[5] = 1.0F;
        arrayOfFloat[6] = 1.0F;
      }
    }
    if (stroke)
    {
      arrayOfFloat[13] = strokeR;
      arrayOfFloat[14] = strokeG;
      arrayOfFloat[15] = strokeB;
      arrayOfFloat[16] = strokeA;
      arrayOfFloat[17] = strokeWeight;
    }
    if (i != 0)
    {
      arrayOfFloat[7] = textureU;
      arrayOfFloat[8] = textureV;
    }
    if (autoNormal)
    {
      float f1 = normalX * normalX + normalY * normalY + normalZ * normalZ;
      if (f1 < 1.0E-4F)
      {
        arrayOfFloat[36] = 0.0F;
      }
      else
      {
        if (Math.abs(f1 - 1.0F) > 1.0E-4F)
        {
          float f2 = PApplet.sqrt(f1);
          normalX /= f2;
          normalY /= f2;
          normalZ /= f2;
        }
        arrayOfFloat[36] = 1.0F;
      }
    }
    else
    {
      arrayOfFloat[36] = 1.0F;
    }
    vertexCount += 1;
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    vertexCheck();
    float[] arrayOfFloat1 = vertices[vertexCount];
    if ((shape == 20) && (vertexCount > 0))
    {
      float[] arrayOfFloat2 = vertices[(vertexCount - 1)];
      if ((Math.abs(arrayOfFloat2[0] - paramFloat1) < 1.0E-4F) && (Math.abs(arrayOfFloat2[1] - paramFloat2) < 1.0E-4F) && (Math.abs(arrayOfFloat2[2] - paramFloat3) < 1.0E-4F)) {
        return;
      }
    }
    curveVertexCount = 0;
    arrayOfFloat1[0] = paramFloat1;
    arrayOfFloat1[1] = paramFloat2;
    arrayOfFloat1[2] = paramFloat3;
    arrayOfFloat1[12] = (edge ? 1.0F : 0.0F);
    int i = textureImage != null ? 1 : 0;
    if ((fill) || (i != 0))
    {
      if (textureImage == null)
      {
        arrayOfFloat1[3] = fillR;
        arrayOfFloat1[4] = fillG;
        arrayOfFloat1[5] = fillB;
        arrayOfFloat1[6] = fillA;
      }
      else if (tint)
      {
        arrayOfFloat1[3] = tintR;
        arrayOfFloat1[4] = tintG;
        arrayOfFloat1[5] = tintB;
        arrayOfFloat1[6] = tintA;
      }
      else
      {
        arrayOfFloat1[3] = 1.0F;
        arrayOfFloat1[4] = 1.0F;
        arrayOfFloat1[5] = 1.0F;
        arrayOfFloat1[6] = 1.0F;
      }
      arrayOfFloat1[25] = ambientR;
      arrayOfFloat1[26] = ambientG;
      arrayOfFloat1[27] = ambientB;
      arrayOfFloat1[28] = specularR;
      arrayOfFloat1[29] = specularG;
      arrayOfFloat1[30] = specularB;
      arrayOfFloat1[31] = shininess;
      arrayOfFloat1[32] = emissiveR;
      arrayOfFloat1[33] = emissiveG;
      arrayOfFloat1[34] = emissiveB;
    }
    if (stroke)
    {
      arrayOfFloat1[13] = strokeR;
      arrayOfFloat1[14] = strokeG;
      arrayOfFloat1[15] = strokeB;
      arrayOfFloat1[16] = strokeA;
      arrayOfFloat1[17] = strokeWeight;
    }
    if (i != 0)
    {
      arrayOfFloat1[7] = textureU;
      arrayOfFloat1[8] = textureV;
    }
    if (autoNormal)
    {
      float f1 = normalX * normalX + normalY * normalY + normalZ * normalZ;
      if (f1 < 1.0E-4F)
      {
        arrayOfFloat1[36] = 0.0F;
      }
      else
      {
        if (Math.abs(f1 - 1.0F) > 1.0E-4F)
        {
          float f2 = PApplet.sqrt(f1);
          normalX /= f2;
          normalY /= f2;
          normalZ /= f2;
        }
        arrayOfFloat1[36] = 1.0F;
      }
    }
    else
    {
      arrayOfFloat1[36] = 1.0F;
    }
    arrayOfFloat1[9] = normalX;
    arrayOfFloat1[10] = normalY;
    arrayOfFloat1[11] = normalZ;
    arrayOfFloat1[35] = 0.0F;
    vertexCount += 1;
  }
  
  public void vertexFields(float[] paramArrayOfFloat)
  {
    vertexCheck();
    curveVertexCount = 0;
    float[] arrayOfFloat = vertices[vertexCount];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, 37);
    vertexCount += 1;
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    vertexTexture(paramFloat3, paramFloat4);
    vertex(paramFloat1, paramFloat2);
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    vertexTexture(paramFloat4, paramFloat5);
    vertex(paramFloat1, paramFloat2, paramFloat3);
  }
  
  protected void vertexTexture(float paramFloat1, float paramFloat2)
  {
    if (textureImage == null) {
      throw new RuntimeException("You must first call texture() before using u and v coordinates with vertex()");
    }
    if (textureMode == 2)
    {
      paramFloat1 /= textureImage.width;
      paramFloat2 /= textureImage.height;
    }
    textureU = paramFloat1;
    textureV = paramFloat2;
    if (textureU < 0.0F) {
      textureU = 0.0F;
    } else if (textureU > 1.0F) {
      textureU = 1.0F;
    }
    if (textureV < 0.0F) {
      textureV = 0.0F;
    } else if (textureV > 1.0F) {
      textureV = 1.0F;
    }
  }
  
  public void breakShape()
  {
    showWarning("This renderer cannot currently handle concave shapes, or shapes with holes.");
  }
  
  public void endShape()
  {
    endShape(1);
  }
  
  public void endShape(int paramInt) {}
  
  protected void bezierVertexCheck()
  {
    if ((shape == 0) || (shape != 20)) {
      throw new RuntimeException("beginShape() or beginShape(POLYGON) must be used before bezierVertex() or quadVertex()");
    }
    if (vertexCount == 0) {
      throw new RuntimeException("vertex() must be used at least oncebefore bezierVertex() or quadVertex()");
    }
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    bezierInitCheck();
    bezierVertexCheck();
    PMatrix3D localPMatrix3D = bezierDrawMatrix;
    float[] arrayOfFloat = vertices[(vertexCount - 1)];
    float f1 = arrayOfFloat[0];
    float f2 = arrayOfFloat[1];
    float f3 = m10 * f1 + m11 * paramFloat1 + m12 * paramFloat3 + m13 * paramFloat5;
    float f4 = m20 * f1 + m21 * paramFloat1 + m22 * paramFloat3 + m23 * paramFloat5;
    float f5 = m30 * f1 + m31 * paramFloat1 + m32 * paramFloat3 + m33 * paramFloat5;
    float f6 = m10 * f2 + m11 * paramFloat2 + m12 * paramFloat4 + m13 * paramFloat6;
    float f7 = m20 * f2 + m21 * paramFloat2 + m22 * paramFloat4 + m23 * paramFloat6;
    float f8 = m30 * f2 + m31 * paramFloat2 + m32 * paramFloat4 + m33 * paramFloat6;
    for (int i = 0; i < bezierDetail; i++)
    {
      f1 += f3;
      f3 += f4;
      f4 += f5;
      f2 += f6;
      f6 += f7;
      f7 += f8;
      vertex(f1, f2);
    }
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    bezierInitCheck();
    bezierVertexCheck();
    PMatrix3D localPMatrix3D = bezierDrawMatrix;
    float[] arrayOfFloat = vertices[(vertexCount - 1)];
    float f1 = arrayOfFloat[0];
    float f2 = arrayOfFloat[1];
    float f3 = arrayOfFloat[2];
    float f4 = m10 * f1 + m11 * paramFloat1 + m12 * paramFloat4 + m13 * paramFloat7;
    float f5 = m20 * f1 + m21 * paramFloat1 + m22 * paramFloat4 + m23 * paramFloat7;
    float f6 = m30 * f1 + m31 * paramFloat1 + m32 * paramFloat4 + m33 * paramFloat7;
    float f7 = m10 * f2 + m11 * paramFloat2 + m12 * paramFloat5 + m13 * paramFloat8;
    float f8 = m20 * f2 + m21 * paramFloat2 + m22 * paramFloat5 + m23 * paramFloat8;
    float f9 = m30 * f2 + m31 * paramFloat2 + m32 * paramFloat5 + m33 * paramFloat8;
    float f10 = m10 * f3 + m11 * paramFloat3 + m12 * paramFloat6 + m13 * paramFloat9;
    float f11 = m20 * f3 + m21 * paramFloat3 + m22 * paramFloat6 + m23 * paramFloat9;
    float f12 = m30 * f3 + m31 * paramFloat3 + m32 * paramFloat6 + m33 * paramFloat9;
    for (int i = 0; i < bezierDetail; i++)
    {
      f1 += f4;
      f4 += f5;
      f5 += f6;
      f2 += f7;
      f7 += f8;
      f8 += f9;
      f3 += f10;
      f10 += f11;
      f11 += f12;
      vertex(f1, f2, f3);
    }
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float[] arrayOfFloat = vertices[(vertexCount - 1)];
    float f1 = arrayOfFloat[0];
    float f2 = arrayOfFloat[1];
    bezierVertex(f1 + (paramFloat1 - f1) * 2.0F / 3.0F, f2 + (paramFloat2 - f2) * 2.0F / 3.0F, paramFloat3 + (paramFloat1 - paramFloat3) * 2.0F / 3.0F, paramFloat4 + (paramFloat2 - paramFloat4) * 2.0F / 3.0F, paramFloat3, paramFloat4);
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float[] arrayOfFloat = vertices[(vertexCount - 1)];
    float f1 = arrayOfFloat[0];
    float f2 = arrayOfFloat[1];
    float f3 = arrayOfFloat[2];
    bezierVertex(f1 + (paramFloat1 - f1) * 2.0F / 3.0F, f2 + (paramFloat2 - f2) * 2.0F / 3.0F, f3 + (paramFloat3 - f3) * 2.0F / 3.0F, paramFloat4 + (paramFloat1 - paramFloat4) * 2.0F / 3.0F, paramFloat5 + (paramFloat2 - paramFloat5) * 2.0F / 3.0F, paramFloat6 + (paramFloat3 - paramFloat6) * 2.0F / 3.0F, paramFloat4, paramFloat5, paramFloat6);
  }
  
  protected void curveVertexCheck()
  {
    if (shape != 20) {
      throw new RuntimeException("You must use beginShape() or beginShape(POLYGON) before curveVertex()");
    }
    if (curveVertices == null) {
      curveVertices = new float[''][3];
    }
    if (curveVertexCount == curveVertices.length)
    {
      float[][] arrayOfFloat = new float[curveVertexCount << 1][3];
      System.arraycopy(curveVertices, 0, arrayOfFloat, 0, curveVertexCount);
      curveVertices = arrayOfFloat;
    }
    curveInitCheck();
  }
  
  public void curveVertex(float paramFloat1, float paramFloat2)
  {
    curveVertexCheck();
    float[] arrayOfFloat = curveVertices[curveVertexCount];
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    curveVertexCount += 1;
    if (curveVertexCount > 3) {
      curveVertexSegment(curveVertices[(curveVertexCount - 4)][0], curveVertices[(curveVertexCount - 4)][1], curveVertices[(curveVertexCount - 3)][0], curveVertices[(curveVertexCount - 3)][1], curveVertices[(curveVertexCount - 2)][0], curveVertices[(curveVertexCount - 2)][1], curveVertices[(curveVertexCount - 1)][0], curveVertices[(curveVertexCount - 1)][1]);
    }
  }
  
  public void curveVertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    curveVertexCheck();
    float[] arrayOfFloat = curveVertices[curveVertexCount];
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[2] = paramFloat3;
    curveVertexCount += 1;
    if (curveVertexCount > 3) {
      curveVertexSegment(curveVertices[(curveVertexCount - 4)][0], curveVertices[(curveVertexCount - 4)][1], curveVertices[(curveVertexCount - 4)][2], curveVertices[(curveVertexCount - 3)][0], curveVertices[(curveVertexCount - 3)][1], curveVertices[(curveVertexCount - 3)][2], curveVertices[(curveVertexCount - 2)][0], curveVertices[(curveVertexCount - 2)][1], curveVertices[(curveVertexCount - 2)][2], curveVertices[(curveVertexCount - 1)][0], curveVertices[(curveVertexCount - 1)][1], curveVertices[(curveVertexCount - 1)][2]);
    }
  }
  
  protected void curveVertexSegment(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    float f1 = paramFloat3;
    float f2 = paramFloat4;
    PMatrix3D localPMatrix3D = curveDrawMatrix;
    float f3 = m10 * paramFloat1 + m11 * paramFloat3 + m12 * paramFloat5 + m13 * paramFloat7;
    float f4 = m20 * paramFloat1 + m21 * paramFloat3 + m22 * paramFloat5 + m23 * paramFloat7;
    float f5 = m30 * paramFloat1 + m31 * paramFloat3 + m32 * paramFloat5 + m33 * paramFloat7;
    float f6 = m10 * paramFloat2 + m11 * paramFloat4 + m12 * paramFloat6 + m13 * paramFloat8;
    float f7 = m20 * paramFloat2 + m21 * paramFloat4 + m22 * paramFloat6 + m23 * paramFloat8;
    float f8 = m30 * paramFloat2 + m31 * paramFloat4 + m32 * paramFloat6 + m33 * paramFloat8;
    int i = curveVertexCount;
    vertex(f1, f2);
    for (int j = 0; j < curveDetail; j++)
    {
      f1 += f3;
      f3 += f4;
      f4 += f5;
      f2 += f6;
      f6 += f7;
      f7 += f8;
      vertex(f1, f2);
    }
    curveVertexCount = i;
  }
  
  protected void curveVertexSegment(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    float f1 = paramFloat4;
    float f2 = paramFloat5;
    float f3 = paramFloat6;
    PMatrix3D localPMatrix3D = curveDrawMatrix;
    float f4 = m10 * paramFloat1 + m11 * paramFloat4 + m12 * paramFloat7 + m13 * paramFloat10;
    float f5 = m20 * paramFloat1 + m21 * paramFloat4 + m22 * paramFloat7 + m23 * paramFloat10;
    float f6 = m30 * paramFloat1 + m31 * paramFloat4 + m32 * paramFloat7 + m33 * paramFloat10;
    float f7 = m10 * paramFloat2 + m11 * paramFloat5 + m12 * paramFloat8 + m13 * paramFloat11;
    float f8 = m20 * paramFloat2 + m21 * paramFloat5 + m22 * paramFloat8 + m23 * paramFloat11;
    float f9 = m30 * paramFloat2 + m31 * paramFloat5 + m32 * paramFloat8 + m33 * paramFloat11;
    int i = curveVertexCount;
    float f10 = m10 * paramFloat3 + m11 * paramFloat6 + m12 * paramFloat9 + m13 * paramFloat12;
    float f11 = m20 * paramFloat3 + m21 * paramFloat6 + m22 * paramFloat9 + m23 * paramFloat12;
    float f12 = m30 * paramFloat3 + m31 * paramFloat6 + m32 * paramFloat9 + m33 * paramFloat12;
    vertex(f1, f2, f3);
    for (int j = 0; j < curveDetail; j++)
    {
      f1 += f4;
      f4 += f5;
      f5 += f6;
      f2 += f7;
      f7 += f8;
      f8 += f9;
      f3 += f10;
      f10 += f11;
      f11 += f12;
      vertex(f1, f2, f3);
    }
    curveVertexCount = i;
  }
  
  public void point(float paramFloat1, float paramFloat2)
  {
    beginShape(2);
    vertex(paramFloat1, paramFloat2);
    endShape();
  }
  
  public void point(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    beginShape(2);
    vertex(paramFloat1, paramFloat2, paramFloat3);
    endShape();
  }
  
  public void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    beginShape(4);
    vertex(paramFloat1, paramFloat2);
    vertex(paramFloat3, paramFloat4);
    endShape();
  }
  
  public void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    beginShape(4);
    vertex(paramFloat1, paramFloat2, paramFloat3);
    vertex(paramFloat4, paramFloat5, paramFloat6);
    endShape();
  }
  
  public void triangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    beginShape(9);
    vertex(paramFloat1, paramFloat2);
    vertex(paramFloat3, paramFloat4);
    vertex(paramFloat5, paramFloat6);
    endShape();
  }
  
  public void quad(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    beginShape(16);
    vertex(paramFloat1, paramFloat2);
    vertex(paramFloat3, paramFloat4);
    vertex(paramFloat5, paramFloat6);
    vertex(paramFloat7, paramFloat8);
    endShape();
  }
  
  public void rectMode(int paramInt)
  {
    rectMode = paramInt;
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1;
    float f2;
    switch (rectMode)
    {
    case 1: 
      break;
    case 0: 
      paramFloat3 += paramFloat1;
      paramFloat4 += paramFloat2;
      break;
    case 2: 
      f1 = paramFloat3;
      f2 = paramFloat4;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
      break;
    case 3: 
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
    }
    float f3;
    if (paramFloat1 > paramFloat3)
    {
      f3 = paramFloat1;
      paramFloat1 = paramFloat3;
      paramFloat3 = f3;
    }
    if (paramFloat2 > paramFloat4)
    {
      f3 = paramFloat2;
      paramFloat2 = paramFloat4;
      paramFloat4 = f3;
    }
    rectImpl(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  protected void rectImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    quad(paramFloat1, paramFloat2, paramFloat3, paramFloat2, paramFloat3, paramFloat4, paramFloat1, paramFloat4);
  }
  
  private void quadraticVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float[] arrayOfFloat = vertices[(vertexCount - 1)];
    float f1 = arrayOfFloat[0];
    float f2 = arrayOfFloat[1];
    float f3 = f1 + 0.6666667F * (paramFloat1 - f1);
    float f4 = f2 + 0.6666667F * (paramFloat2 - f2);
    float f5 = f3 + (paramFloat3 - f1) / 3.0F;
    float f6 = f4 + (paramFloat4 - f2) / 3.0F;
    bezierVertex(f3, f4, f5, f6, paramFloat3, paramFloat4);
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1;
    float f2;
    switch (rectMode)
    {
    case 1: 
      break;
    case 0: 
      paramFloat3 += paramFloat1;
      paramFloat4 += paramFloat2;
      break;
    case 2: 
      f1 = paramFloat3;
      f2 = paramFloat4;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
      break;
    case 3: 
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
    }
    float f3;
    if (paramFloat1 > paramFloat3)
    {
      f3 = paramFloat1;
      paramFloat1 = paramFloat3;
      paramFloat3 = f3;
    }
    if (paramFloat2 > paramFloat4)
    {
      f3 = paramFloat2;
      paramFloat2 = paramFloat4;
      paramFloat4 = f3;
    }
    rectImpl(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  protected void rectImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    beginShape();
    vertex(paramFloat3 - paramFloat5, paramFloat2);
    quadraticVertex(paramFloat3, paramFloat2, paramFloat3, paramFloat2 + paramFloat6);
    vertex(paramFloat3, paramFloat4 - paramFloat6);
    quadraticVertex(paramFloat3, paramFloat4, paramFloat3 - paramFloat5, paramFloat4);
    vertex(paramFloat1 + paramFloat5, paramFloat4);
    quadraticVertex(paramFloat1, paramFloat4, paramFloat1, paramFloat4 - paramFloat6);
    vertex(paramFloat1, paramFloat2 + paramFloat6);
    quadraticVertex(paramFloat1, paramFloat2, paramFloat1 + paramFloat5, paramFloat2);
    endShape(2);
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    float f1;
    float f2;
    switch (rectMode)
    {
    case 1: 
      break;
    case 0: 
      paramFloat3 += paramFloat1;
      paramFloat4 += paramFloat2;
      break;
    case 2: 
      f1 = paramFloat3;
      f2 = paramFloat4;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
      break;
    case 3: 
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
    }
    float f3;
    if (paramFloat1 > paramFloat3)
    {
      f3 = paramFloat1;
      paramFloat1 = paramFloat3;
      paramFloat3 = f3;
    }
    if (paramFloat2 > paramFloat4)
    {
      f3 = paramFloat2;
      paramFloat2 = paramFloat4;
      paramFloat4 = f3;
    }
    rectImpl(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
  }
  
  protected void rectImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    beginShape();
    if (paramFloat6 != 0.0F)
    {
      vertex(paramFloat3 - paramFloat6, paramFloat2);
      quadraticVertex(paramFloat3, paramFloat2, paramFloat3, paramFloat2 + paramFloat6);
    }
    else
    {
      vertex(paramFloat3, paramFloat2);
    }
    if (paramFloat8 != 0.0F)
    {
      vertex(paramFloat3, paramFloat4 - paramFloat8);
      quadraticVertex(paramFloat3, paramFloat4, paramFloat3 - paramFloat8, paramFloat4);
    }
    else
    {
      vertex(paramFloat3, paramFloat4);
    }
    if (paramFloat7 != 0.0F)
    {
      vertex(paramFloat1 + paramFloat7, paramFloat4);
      quadraticVertex(paramFloat1, paramFloat4, paramFloat1, paramFloat4 - paramFloat7);
    }
    else
    {
      vertex(paramFloat1, paramFloat4);
    }
    if (paramFloat5 != 0.0F)
    {
      vertex(paramFloat1, paramFloat2 + paramFloat5);
      quadraticVertex(paramFloat1, paramFloat2, paramFloat1 + paramFloat5, paramFloat2);
    }
    else
    {
      vertex(paramFloat1, paramFloat2);
    }
    endShape(2);
  }
  
  public void ellipseMode(int paramInt)
  {
    ellipseMode = paramInt;
  }
  
  public void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = paramFloat1;
    float f2 = paramFloat2;
    float f3 = paramFloat3;
    float f4 = paramFloat4;
    if (ellipseMode == 1)
    {
      f3 = paramFloat3 - paramFloat1;
      f4 = paramFloat4 - paramFloat2;
    }
    else if (ellipseMode == 2)
    {
      f1 = paramFloat1 - paramFloat3;
      f2 = paramFloat2 - paramFloat4;
      f3 = paramFloat3 * 2.0F;
      f4 = paramFloat4 * 2.0F;
    }
    else if (ellipseMode == 3)
    {
      f1 = paramFloat1 - paramFloat3 / 2.0F;
      f2 = paramFloat2 - paramFloat4 / 2.0F;
    }
    if (f3 < 0.0F)
    {
      f1 += f3;
      f3 = -f3;
    }
    if (f4 < 0.0F)
    {
      f2 += f4;
      f4 = -f4;
    }
    ellipseImpl(f1, f2, f3, f4);
  }
  
  protected void ellipseImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {}
  
  public void arc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = paramFloat1;
    float f2 = paramFloat2;
    float f3 = paramFloat3;
    float f4 = paramFloat4;
    if (ellipseMode == 1)
    {
      f3 = paramFloat3 - paramFloat1;
      f4 = paramFloat4 - paramFloat2;
    }
    else if (ellipseMode == 2)
    {
      f1 = paramFloat1 - paramFloat3;
      f2 = paramFloat2 - paramFloat4;
      f3 = paramFloat3 * 2.0F;
      f4 = paramFloat4 * 2.0F;
    }
    else if (ellipseMode == 3)
    {
      f1 = paramFloat1 - paramFloat3 / 2.0F;
      f2 = paramFloat2 - paramFloat4 / 2.0F;
    }
    if ((Float.isInfinite(paramFloat5)) || (Float.isInfinite(paramFloat6))) {
      return;
    }
    if (paramFloat6 < paramFloat5) {
      return;
    }
    while (paramFloat5 < 0.0F)
    {
      paramFloat5 += 6.2831855F;
      paramFloat6 += 6.2831855F;
    }
    if (paramFloat6 - paramFloat5 > 6.2831855F)
    {
      paramFloat5 = 0.0F;
      paramFloat6 = 6.2831855F;
    }
    arcImpl(f1, f2, f3, f4, paramFloat5, paramFloat6);
  }
  
  protected void arcImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {}
  
  public void box(float paramFloat)
  {
    box(paramFloat, paramFloat, paramFloat);
  }
  
  public void box(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = -paramFloat1 / 2.0F;
    float f2 = paramFloat1 / 2.0F;
    float f3 = -paramFloat2 / 2.0F;
    float f4 = paramFloat2 / 2.0F;
    float f5 = -paramFloat3 / 2.0F;
    float f6 = paramFloat3 / 2.0F;
    beginShape(16);
    normal(0.0F, 0.0F, 1.0F);
    vertex(f1, f3, f5);
    vertex(f2, f3, f5);
    vertex(f2, f4, f5);
    vertex(f1, f4, f5);
    normal(1.0F, 0.0F, 0.0F);
    vertex(f2, f3, f5);
    vertex(f2, f3, f6);
    vertex(f2, f4, f6);
    vertex(f2, f4, f5);
    normal(0.0F, 0.0F, -1.0F);
    vertex(f2, f3, f6);
    vertex(f1, f3, f6);
    vertex(f1, f4, f6);
    vertex(f2, f4, f6);
    normal(-1.0F, 0.0F, 0.0F);
    vertex(f1, f3, f6);
    vertex(f1, f3, f5);
    vertex(f1, f4, f5);
    vertex(f1, f4, f6);
    normal(0.0F, 1.0F, 0.0F);
    vertex(f1, f3, f6);
    vertex(f2, f3, f6);
    vertex(f2, f3, f5);
    vertex(f1, f3, f5);
    normal(0.0F, -1.0F, 0.0F);
    vertex(f1, f4, f5);
    vertex(f2, f4, f5);
    vertex(f2, f4, f6);
    vertex(f1, f4, f6);
    endShape();
  }
  
  public void sphereDetail(int paramInt)
  {
    sphereDetail(paramInt, paramInt);
  }
  
  public void sphereDetail(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 3) {
      paramInt1 = 3;
    }
    if (paramInt2 < 2) {
      paramInt2 = 2;
    }
    if ((paramInt1 == sphereDetailU) && (paramInt2 == sphereDetailV)) {
      return;
    }
    float f1 = 720.0F / paramInt1;
    float[] arrayOfFloat1 = new float[paramInt1];
    float[] arrayOfFloat2 = new float[paramInt1];
    for (int i = 0; i < paramInt1; i++)
    {
      arrayOfFloat1[i] = cosLUT[((int)(i * f1) % 720)];
      arrayOfFloat2[i] = sinLUT[((int)(i * f1) % 720)];
    }
    i = paramInt1 * (paramInt2 - 1) + 2;
    int j = 0;
    sphereX = new float[i];
    sphereY = new float[i];
    sphereZ = new float[i];
    float f2 = 360.0F / paramInt2;
    float f3 = f2;
    for (int k = 1; k < paramInt2; k++)
    {
      float f4 = sinLUT[((int)f3 % 720)];
      float f5 = -cosLUT[((int)f3 % 720)];
      for (int m = 0; m < paramInt1; m++)
      {
        sphereX[j] = (arrayOfFloat1[m] * f4);
        sphereY[j] = f5;
        sphereZ[(j++)] = (arrayOfFloat2[m] * f4);
      }
      f3 += f2;
    }
    sphereDetailU = paramInt1;
    sphereDetailV = paramInt2;
  }
  
  public void sphere(float paramFloat)
  {
    if ((sphereDetailU < 3) || (sphereDetailV < 2)) {
      sphereDetail(30);
    }
    edge(false);
    beginShape(10);
    for (int i = 0; i < sphereDetailU; i++)
    {
      normal(0.0F, -1.0F, 0.0F);
      vertex(0.0F, -paramFloat, 0.0F);
      normal(sphereX[i], sphereY[i], sphereZ[i]);
      vertex(paramFloat * sphereX[i], paramFloat * sphereY[i], paramFloat * sphereZ[i]);
    }
    normal(0.0F, -1.0F, 0.0F);
    vertex(0.0F, -1.0F, 0.0F);
    normal(sphereX[0], sphereY[0], sphereZ[0]);
    vertex(paramFloat * sphereX[0], paramFloat * sphereY[0], paramFloat * sphereZ[0]);
    endShape();
    int m = 0;
    int k;
    for (int n = 2; n < sphereDetailV; n++)
    {
      int j;
      i = j = m;
      m += sphereDetailU;
      k = m;
      beginShape(10);
      for (int i1 = 0; i1 < sphereDetailU; i1++)
      {
        normal(sphereX[i], sphereY[i], sphereZ[i]);
        vertex(paramFloat * sphereX[i], paramFloat * sphereY[i], paramFloat * sphereZ[(i++)]);
        normal(sphereX[k], sphereY[k], sphereZ[k]);
        vertex(paramFloat * sphereX[k], paramFloat * sphereY[k], paramFloat * sphereZ[(k++)]);
      }
      i = j;
      k = m;
      normal(sphereX[i], sphereY[i], sphereZ[i]);
      vertex(paramFloat * sphereX[i], paramFloat * sphereY[i], sphereZ[i]);
      normal(sphereX[k], sphereY[k], sphereZ[k]);
      vertex(paramFloat * sphereX[k], paramFloat * sphereY[k], paramFloat * sphereZ[k]);
      endShape();
    }
    beginShape(10);
    for (n = 0; n < sphereDetailU; n++)
    {
      k = m + n;
      normal(sphereX[k], sphereY[k], sphereZ[k]);
      vertex(paramFloat * sphereX[k], paramFloat * sphereY[k], paramFloat * sphereZ[k]);
      normal(0.0F, 1.0F, 0.0F);
      vertex(0.0F, paramFloat, 0.0F);
    }
    normal(sphereX[m], sphereY[m], sphereZ[m]);
    vertex(paramFloat * sphereX[m], paramFloat * sphereY[m], paramFloat * sphereZ[m]);
    normal(0.0F, 1.0F, 0.0F);
    vertex(0.0F, paramFloat, 0.0F);
    endShape();
    edge(true);
  }
  
  public float bezierPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    float f = 1.0F - paramFloat5;
    return paramFloat1 * f * f * f + 3.0F * paramFloat2 * paramFloat5 * f * f + 3.0F * paramFloat3 * paramFloat5 * paramFloat5 * f + paramFloat4 * paramFloat5 * paramFloat5 * paramFloat5;
  }
  
  public float bezierTangent(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return 3.0F * paramFloat5 * paramFloat5 * (-paramFloat1 + 3.0F * paramFloat2 - 3.0F * paramFloat3 + paramFloat4) + 6.0F * paramFloat5 * (paramFloat1 - 2.0F * paramFloat2 + paramFloat3) + 3.0F * (-paramFloat1 + paramFloat2);
  }
  
  protected void bezierInitCheck()
  {
    if (!bezierInited) {
      bezierInit();
    }
  }
  
  protected void bezierInit()
  {
    bezierDetail(bezierDetail);
    bezierInited = true;
  }
  
  public void bezierDetail(int paramInt)
  {
    bezierDetail = paramInt;
    if (bezierDrawMatrix == null) {
      bezierDrawMatrix = new PMatrix3D();
    }
    splineForward(paramInt, bezierDrawMatrix);
    bezierDrawMatrix.apply(bezierBasisMatrix);
  }
  
  public void bezier(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    beginShape();
    vertex(paramFloat1, paramFloat2);
    bezierVertex(paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
    endShape();
  }
  
  public void bezier(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    beginShape();
    vertex(paramFloat1, paramFloat2, paramFloat3);
    bezierVertex(paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12);
    endShape();
  }
  
  public float curvePoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    curveInitCheck();
    float f1 = paramFloat5 * paramFloat5;
    float f2 = paramFloat5 * f1;
    PMatrix3D localPMatrix3D = curveBasisMatrix;
    return paramFloat1 * (f2 * m00 + f1 * m10 + paramFloat5 * m20 + m30) + paramFloat2 * (f2 * m01 + f1 * m11 + paramFloat5 * m21 + m31) + paramFloat3 * (f2 * m02 + f1 * m12 + paramFloat5 * m22 + m32) + paramFloat4 * (f2 * m03 + f1 * m13 + paramFloat5 * m23 + m33);
  }
  
  public float curveTangent(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    curveInitCheck();
    float f1 = paramFloat5 * paramFloat5 * 3.0F;
    float f2 = paramFloat5 * 2.0F;
    PMatrix3D localPMatrix3D = curveBasisMatrix;
    return paramFloat1 * (f1 * m00 + f2 * m10 + m20) + paramFloat2 * (f1 * m01 + f2 * m11 + m21) + paramFloat3 * (f1 * m02 + f2 * m12 + m22) + paramFloat4 * (f1 * m03 + f2 * m13 + m23);
  }
  
  public void curveDetail(int paramInt)
  {
    curveDetail = paramInt;
    curveInit();
  }
  
  public void curveTightness(float paramFloat)
  {
    curveTightness = paramFloat;
    curveInit();
  }
  
  protected void curveInitCheck()
  {
    if (!curveInited) {
      curveInit();
    }
  }
  
  protected void curveInit()
  {
    if (curveDrawMatrix == null)
    {
      curveBasisMatrix = new PMatrix3D();
      curveDrawMatrix = new PMatrix3D();
      curveInited = true;
    }
    float f = curveTightness;
    curveBasisMatrix.set((f - 1.0F) / 2.0F, (f + 3.0F) / 2.0F, (-3.0F - f) / 2.0F, (1.0F - f) / 2.0F, 1.0F - f, (-5.0F - f) / 2.0F, f + 2.0F, (f - 1.0F) / 2.0F, (f - 1.0F) / 2.0F, 0.0F, (1.0F - f) / 2.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
    splineForward(curveDetail, curveDrawMatrix);
    if (bezierBasisInverse == null)
    {
      bezierBasisInverse = bezierBasisMatrix.get();
      bezierBasisInverse.invert();
      curveToBezierMatrix = new PMatrix3D();
    }
    curveToBezierMatrix.set(curveBasisMatrix);
    curveToBezierMatrix.preApply(bezierBasisInverse);
    curveDrawMatrix.apply(curveBasisMatrix);
  }
  
  public void curve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    beginShape();
    curveVertex(paramFloat1, paramFloat2);
    curveVertex(paramFloat3, paramFloat4);
    curveVertex(paramFloat5, paramFloat6);
    curveVertex(paramFloat7, paramFloat8);
    endShape();
  }
  
  public void curve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    beginShape();
    curveVertex(paramFloat1, paramFloat2, paramFloat3);
    curveVertex(paramFloat4, paramFloat5, paramFloat6);
    curveVertex(paramFloat7, paramFloat8, paramFloat9);
    curveVertex(paramFloat10, paramFloat11, paramFloat12);
    endShape();
  }
  
  protected void splineForward(int paramInt, PMatrix3D paramPMatrix3D)
  {
    float f1 = 1.0F / paramInt;
    float f2 = f1 * f1;
    float f3 = f2 * f1;
    paramPMatrix3D.set(0.0F, 0.0F, 0.0F, 1.0F, f3, f2, f1, 0.0F, 6.0F * f3, 2.0F * f2, 0.0F, 0.0F, 6.0F * f3, 0.0F, 0.0F, 0.0F);
  }
  
  public void smooth()
  {
    smooth = true;
  }
  
  public void noSmooth()
  {
    smooth = false;
  }
  
  public void imageMode(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 1) || (paramInt == 3))
    {
      imageMode = paramInt;
    }
    else
    {
      String str = "imageMode() only works with CORNER, CORNERS, or CENTER";
      throw new RuntimeException(str);
    }
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2)
  {
    if ((width == -1) || (height == -1)) {
      return;
    }
    if ((imageMode == 0) || (imageMode == 1))
    {
      imageImpl(paramPImage, paramFloat1, paramFloat2, paramFloat1 + width, paramFloat2 + height, 0, 0, width, height);
    }
    else if (imageMode == 3)
    {
      float f1 = paramFloat1 - width / 2;
      float f2 = paramFloat2 - height / 2;
      imageImpl(paramPImage, f1, f2, f1 + width, f2 + height, 0, 0, width, height);
    }
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    image(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, 0, 0, width, height);
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((width == -1) || (height == -1)) {
      return;
    }
    if (imageMode == 0)
    {
      if (paramFloat3 < 0.0F)
      {
        paramFloat1 += paramFloat3;
        paramFloat3 = -paramFloat3;
      }
      if (paramFloat4 < 0.0F)
      {
        paramFloat2 += paramFloat4;
        paramFloat4 = -paramFloat4;
      }
      imageImpl(paramPImage, paramFloat1, paramFloat2, paramFloat1 + paramFloat3, paramFloat2 + paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    else
    {
      float f1;
      if (imageMode == 1)
      {
        if (paramFloat3 < paramFloat1)
        {
          f1 = paramFloat1;
          paramFloat1 = paramFloat3;
          paramFloat3 = f1;
        }
        if (paramFloat4 < paramFloat2)
        {
          f1 = paramFloat2;
          paramFloat2 = paramFloat4;
          paramFloat4 = f1;
        }
        imageImpl(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
      }
      else if (imageMode == 3)
      {
        if (paramFloat3 < 0.0F) {
          paramFloat3 = -paramFloat3;
        }
        if (paramFloat4 < 0.0F) {
          paramFloat4 = -paramFloat4;
        }
        f1 = paramFloat1 - paramFloat3 / 2.0F;
        float f2 = paramFloat2 - paramFloat4 / 2.0F;
        imageImpl(paramPImage, f1, f2, f1 + paramFloat3, f2 + paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
      }
    }
  }
  
  protected void imageImpl(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = stroke;
    int i = textureMode;
    stroke = false;
    textureMode = 2;
    beginShape(16);
    texture(paramPImage);
    vertex(paramFloat1, paramFloat2, paramInt1, paramInt2);
    vertex(paramFloat1, paramFloat4, paramInt1, paramInt4);
    vertex(paramFloat3, paramFloat4, paramInt3, paramInt4);
    vertex(paramFloat3, paramFloat2, paramInt3, paramInt2);
    endShape();
    stroke = bool;
    textureMode = i;
  }
  
  public void shapeMode(int paramInt)
  {
    shapeMode = paramInt;
  }
  
  public void shape(PShape paramPShape)
  {
    if (paramPShape.isVisible())
    {
      if (shapeMode == 3)
      {
        pushMatrix();
        translate(-paramPShape.getWidth() / 2.0F, -paramPShape.getHeight() / 2.0F);
      }
      paramPShape.draw(this);
      if (shapeMode == 3) {
        popMatrix();
      }
    }
  }
  
  public void shape(PShape paramPShape, float paramFloat1, float paramFloat2)
  {
    if (paramPShape.isVisible())
    {
      pushMatrix();
      if (shapeMode == 3) {
        translate(paramFloat1 - paramPShape.getWidth() / 2.0F, paramFloat2 - paramPShape.getHeight() / 2.0F);
      } else if ((shapeMode == 0) || (shapeMode == 1)) {
        translate(paramFloat1, paramFloat2);
      }
      paramPShape.draw(this);
      popMatrix();
    }
  }
  
  public void shape(PShape paramPShape, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (paramPShape.isVisible())
    {
      pushMatrix();
      if (shapeMode == 3)
      {
        translate(paramFloat1 - paramFloat3 / 2.0F, paramFloat2 - paramFloat4 / 2.0F);
        scale(paramFloat3 / paramPShape.getWidth(), paramFloat4 / paramPShape.getHeight());
      }
      else if (shapeMode == 0)
      {
        translate(paramFloat1, paramFloat2);
        scale(paramFloat3 / paramPShape.getWidth(), paramFloat4 / paramPShape.getHeight());
      }
      else if (shapeMode == 1)
      {
        paramFloat3 -= paramFloat1;
        paramFloat4 -= paramFloat2;
        translate(paramFloat1, paramFloat2);
        scale(paramFloat3 / paramPShape.getWidth(), paramFloat4 / paramPShape.getHeight());
      }
      paramPShape.draw(this);
      popMatrix();
    }
  }
  
  public void textAlign(int paramInt)
  {
    textAlign(paramInt, 0);
  }
  
  public void textAlign(int paramInt1, int paramInt2)
  {
    textAlign = paramInt1;
    textAlignY = paramInt2;
  }
  
  public float textAscent()
  {
    if (textFont == null) {
      defaultFontOrDeath("textAscent");
    }
    return textFont.ascent() * (textMode == 256 ? textFont.size : textSize);
  }
  
  public float textDescent()
  {
    if (textFont == null) {
      defaultFontOrDeath("textDescent");
    }
    return textFont.descent() * (textMode == 256 ? textFont.size : textSize);
  }
  
  public void textFont(PFont paramPFont)
  {
    if (paramPFont != null)
    {
      textFont = paramPFont;
      if (hints[3] != 0) {
        paramPFont.findFont();
      }
      textSize(size);
    }
    else
    {
      throw new RuntimeException("A null PFont was passed to textFont()");
    }
  }
  
  public void textFont(PFont paramPFont, float paramFloat)
  {
    textFont(paramPFont);
    textSize(paramFloat);
  }
  
  public void textLeading(float paramFloat)
  {
    textLeading = paramFloat;
  }
  
  public void textMode(int paramInt)
  {
    if ((paramInt == 37) || (paramInt == 39))
    {
      showWarning("Since Processing beta, textMode() is now textAlign().");
      return;
    }
    if (textModeCheck(paramInt))
    {
      textMode = paramInt;
    }
    else
    {
      String str = String.valueOf(paramInt);
      switch (paramInt)
      {
      case 256: 
        str = "SCREEN";
        break;
      case 4: 
        str = "MODEL";
        break;
      case 5: 
        str = "SHAPE";
      }
      showWarning("textMode(" + str + ") is not supported by this renderer.");
    }
  }
  
  protected boolean textModeCheck(int paramInt)
  {
    return true;
  }
  
  public void textSize(float paramFloat)
  {
    if (textFont == null) {
      defaultFontOrDeath("textSize", paramFloat);
    }
    textSize = paramFloat;
    textLeading = ((textAscent() + textDescent()) * 1.275F);
  }
  
  public float textWidth(char paramChar)
  {
    textWidthBuffer[0] = paramChar;
    return textWidthImpl(textWidthBuffer, 0, 1);
  }
  
  public float textWidth(String paramString)
  {
    if (textFont == null) {
      defaultFontOrDeath("textWidth");
    }
    int i = paramString.length();
    if (i > textWidthBuffer.length) {
      textWidthBuffer = new char[i + 10];
    }
    paramString.getChars(0, i, textWidthBuffer, 0);
    float f = 0.0F;
    int j = 0;
    int k = 0;
    while (j < i)
    {
      if (textWidthBuffer[j] == '\n')
      {
        f = Math.max(f, textWidthImpl(textWidthBuffer, k, j));
        k = j + 1;
      }
      j++;
    }
    if (k < i) {
      f = Math.max(f, textWidthImpl(textWidthBuffer, k, j));
    }
    return f;
  }
  
  public float textWidth(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return textWidthImpl(paramArrayOfChar, paramInt1, paramInt1 + paramInt2);
  }
  
  protected float textWidthImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    float f = 0.0F;
    for (int i = paramInt1; i < paramInt2; i++) {
      f += textFont.width(paramArrayOfChar[i]) * textSize;
    }
    return f;
  }
  
  public void text(char paramChar)
  {
    text(paramChar, textX, textY, textZ);
  }
  
  public void text(char paramChar, float paramFloat1, float paramFloat2)
  {
    if (textFont == null) {
      defaultFontOrDeath("text");
    }
    if (textMode == 256) {
      loadPixels();
    }
    if (textAlignY == 3) {
      paramFloat2 += textAscent() / 2.0F;
    } else if (textAlignY == 101) {
      paramFloat2 += textAscent();
    } else if (textAlignY == 102) {
      paramFloat2 -= textDescent();
    }
    textBuffer[0] = paramChar;
    textLineAlignImpl(textBuffer, 0, 1, paramFloat1, paramFloat2);
    if (textMode == 256) {
      updatePixels();
    }
  }
  
  public void text(char paramChar, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, paramFloat3);
    }
    text(paramChar, paramFloat1, paramFloat2);
    textZ = paramFloat3;
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, -paramFloat3);
    }
  }
  
  public void text(String paramString)
  {
    text(paramString, textX, textY, textZ);
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2)
  {
    if (textFont == null) {
      defaultFontOrDeath("text");
    }
    if (textMode == 256) {
      loadPixels();
    }
    int i = paramString.length();
    if (i > textBuffer.length) {
      textBuffer = new char[i + 10];
    }
    paramString.getChars(0, i, textBuffer, 0);
    text(textBuffer, 0, i, paramFloat1, paramFloat2);
  }
  
  public void text(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    float f = 0.0F;
    for (int i = paramInt1; i < paramInt2; i++) {
      if (paramArrayOfChar[i] == '\n') {
        f += textLeading;
      }
    }
    if (textAlignY == 3) {
      paramFloat2 += (textAscent() - f) / 2.0F;
    } else if (textAlignY == 101) {
      paramFloat2 += textAscent();
    } else if (textAlignY == 102) {
      paramFloat2 -= textDescent() + f;
    }
    for (i = 0; i < paramInt2; i++) {
      if (paramArrayOfChar[i] == '\n')
      {
        textLineAlignImpl(paramArrayOfChar, paramInt1, i, paramFloat1, paramFloat2);
        paramInt1 = i + 1;
        paramFloat2 += textLeading;
      }
    }
    if (paramInt1 < paramInt2) {
      textLineAlignImpl(paramArrayOfChar, paramInt1, i, paramFloat1, paramFloat2);
    }
    if (textMode == 256) {
      updatePixels();
    }
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, paramFloat3);
    }
    text(paramString, paramFloat1, paramFloat2);
    textZ = paramFloat3;
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, -paramFloat3);
    }
  }
  
  public void text(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, paramFloat3);
    }
    text(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2);
    textZ = paramFloat3;
    if (paramFloat3 != 0.0F) {
      translate(0.0F, 0.0F, -paramFloat3);
    }
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (textFont == null) {
      defaultFontOrDeath("text");
    }
    if (textMode == 256) {
      loadPixels();
    }
    float f1;
    float f2;
    switch (rectMode)
    {
    case 0: 
      paramFloat3 += paramFloat1;
      paramFloat4 += paramFloat2;
      break;
    case 2: 
      f1 = paramFloat3;
      f2 = paramFloat4;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
      break;
    case 3: 
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      paramFloat3 = paramFloat1 + f1;
      paramFloat4 = paramFloat2 + f2;
      paramFloat1 -= f1;
      paramFloat2 -= f2;
    }
    if (paramFloat3 < paramFloat1)
    {
      f3 = paramFloat1;
      paramFloat1 = paramFloat3;
      paramFloat3 = f3;
    }
    if (paramFloat4 < paramFloat2)
    {
      f3 = paramFloat2;
      paramFloat2 = paramFloat4;
      paramFloat4 = f3;
    }
    float f3 = paramFloat3 - paramFloat1;
    float f4 = textWidth(' ');
    if (textBreakStart == null)
    {
      textBreakStart = new int[20];
      textBreakStop = new int[20];
    }
    textBreakCount = 0;
    int i = paramString.length();
    if (i + 1 > textBuffer.length) {
      textBuffer = new char[i + 1];
    }
    paramString.getChars(0, i, textBuffer, 0);
    textBuffer[(i++)] = '\n';
    int j = 0;
    for (int k = 0; k < i; k++) {
      if (textBuffer[k] == '\n')
      {
        boolean bool = textSentence(textBuffer, j, k, f3, f4);
        if (!bool) {
          break;
        }
        j = k + 1;
      }
    }
    float f5 = paramFloat1;
    if (textAlign == 3) {
      f5 += f3 / 2.0F;
    } else if (textAlign == 39) {
      f5 = paramFloat3;
    }
    float f6 = paramFloat4 - paramFloat2;
    float f7 = textAscent() + textDescent();
    int m = 1 + PApplet.floor((f6 - f7) / textLeading);
    int n = Math.min(textBreakCount, m);
    float f8;
    if (textAlignY == 3)
    {
      f8 = textAscent() + textLeading * (n - 1);
      float f9 = paramFloat2 + textAscent() + (f6 - f8) / 2.0F;
      for (int i2 = 0; i2 < n; i2++)
      {
        textLineAlignImpl(textBuffer, textBreakStart[i2], textBreakStop[i2], f5, f9);
        f9 += textLeading;
      }
    }
    else
    {
      int i1;
      if (textAlignY == 102)
      {
        f8 = paramFloat4 - textDescent() - textLeading * (n - 1);
        for (i1 = 0; i1 < n; i1++)
        {
          textLineAlignImpl(textBuffer, textBreakStart[i1], textBreakStop[i1], f5, f8);
          f8 += textLeading;
        }
      }
      else
      {
        f8 = paramFloat2 + textAscent();
        for (i1 = 0; i1 < n; i1++)
        {
          textLineAlignImpl(textBuffer, textBreakStart[i1], textBreakStop[i1], f5, f8);
          f8 += textLeading;
        }
      }
    }
    if (textMode == 256) {
      updatePixels();
    }
  }
  
  protected boolean textSentence(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    float f1 = 0.0F;
    int i = paramInt1;
    int j = paramInt1;
    int k = paramInt1;
    while (k <= paramInt2) {
      if ((paramArrayOfChar[k] == ' ') || (k == paramInt2))
      {
        float f2 = textWidthImpl(paramArrayOfChar, j, k);
        if (f1 + f2 > paramFloat1)
        {
          if (f1 != 0.0F)
          {
            k = j;
            textSentenceBreak(i, k);
            while ((k < paramInt2) && (paramArrayOfChar[k] == ' ')) {
              k++;
            }
          }
          do
          {
            k--;
            if (k == j) {
              return false;
            }
            f2 = textWidthImpl(paramArrayOfChar, j, k);
          } while (f2 > paramFloat1);
          textSentenceBreak(i, k);
          i = k;
          j = k;
          f1 = 0.0F;
        }
        else if (k == paramInt2)
        {
          textSentenceBreak(i, k);
          k++;
        }
        else
        {
          f1 += f2 + paramFloat2;
          j = k + 1;
          k++;
        }
      }
      else
      {
        k++;
      }
    }
    return true;
  }
  
  protected void textSentenceBreak(int paramInt1, int paramInt2)
  {
    if (textBreakCount == textBreakStart.length)
    {
      textBreakStart = PApplet.expand(textBreakStart);
      textBreakStop = PApplet.expand(textBreakStop);
    }
    textBreakStart[textBreakCount] = paramInt1;
    textBreakStop[textBreakCount] = paramInt2;
    textBreakCount += 1;
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    if (paramFloat5 != 0.0F) {
      translate(0.0F, 0.0F, paramFloat5);
    }
    text(paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    textZ = paramFloat5;
    if (paramFloat5 != 0.0F) {
      translate(0.0F, 0.0F, -paramFloat5);
    }
  }
  
  public void text(int paramInt, float paramFloat1, float paramFloat2)
  {
    text(String.valueOf(paramInt), paramFloat1, paramFloat2);
  }
  
  public void text(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    text(String.valueOf(paramInt), paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    text(PApplet.nfs(paramFloat1, 0, 3), paramFloat2, paramFloat3);
  }
  
  public void text(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    text(PApplet.nfs(paramFloat1, 0, 3), paramFloat2, paramFloat3, paramFloat4);
  }
  
  protected void textLineAlignImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    if (textAlign == 3) {
      paramFloat1 -= textWidthImpl(paramArrayOfChar, paramInt1, paramInt2) / 2.0F;
    } else if (textAlign == 39) {
      paramFloat1 -= textWidthImpl(paramArrayOfChar, paramInt1, paramInt2);
    }
    textLineImpl(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2);
  }
  
  protected void textLineImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      textCharImpl(paramArrayOfChar[i], paramFloat1, paramFloat2);
      paramFloat1 += textWidth(paramArrayOfChar[i]);
    }
    textX = paramFloat1;
    textY = paramFloat2;
    textZ = 0.0F;
  }
  
  protected void textCharImpl(char paramChar, float paramFloat1, float paramFloat2)
  {
    PFont.Glyph localGlyph = textFont.getGlyph(paramChar);
    if (localGlyph != null) {
      if (textMode == 4)
      {
        float f1 = height / textFont.size;
        float f2 = width / textFont.size;
        float f3 = leftExtent / textFont.size;
        float f4 = topExtent / textFont.size;
        float f5 = paramFloat1 + f3 * textSize;
        float f6 = paramFloat2 - f4 * textSize;
        float f7 = f5 + f2 * textSize;
        float f8 = f6 + f1 * textSize;
        textCharModelImpl(image, f5, f6, f7, f8, width, height);
      }
      else if (textMode == 256)
      {
        int i = (int)paramFloat1 + leftExtent;
        int j = (int)paramFloat2 - topExtent;
        int k = width;
        int m = height;
        textCharScreenImpl(image, i, j, k, m);
      }
    }
  }
  
  protected void textCharModelImpl(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2)
  {
    boolean bool1 = tint;
    int i = tintColor;
    float f1 = tintR;
    float f2 = tintG;
    float f3 = tintB;
    float f4 = tintA;
    boolean bool2 = tintAlpha;
    tint = true;
    tintColor = fillColor;
    tintR = fillR;
    tintG = fillG;
    tintB = fillB;
    tintA = fillA;
    tintAlpha = fillAlpha;
    imageImpl(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, 0, 0, paramInt1, paramInt2);
    tint = bool1;
    tintColor = i;
    tintR = f1;
    tintG = f2;
    tintB = f3;
    tintA = f4;
    tintAlpha = bool2;
  }
  
  protected void textCharScreenImpl(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    int j = 0;
    if ((paramInt1 >= width) || (paramInt2 >= height) || (paramInt1 + paramInt3 < 0) || (paramInt2 + paramInt4 < 0)) {
      return;
    }
    if (paramInt1 < 0)
    {
      i -= paramInt1;
      paramInt3 += paramInt1;
      paramInt1 = 0;
    }
    if (paramInt2 < 0)
    {
      j -= paramInt2;
      paramInt4 += paramInt2;
      paramInt2 = 0;
    }
    if (paramInt1 + paramInt3 > width) {
      paramInt3 -= paramInt1 + paramInt3 - width;
    }
    if (paramInt2 + paramInt4 > height) {
      paramInt4 -= paramInt2 + paramInt4 - height;
    }
    int k = fillRi;
    int m = fillGi;
    int n = fillBi;
    int i1 = fillAi;
    int[] arrayOfInt = pixels;
    for (int i2 = j; i2 < j + paramInt4; i2++) {
      for (int i3 = i; i3 < i + paramInt3; i3++)
      {
        int i4 = i1 * arrayOfInt[(i2 * width + i3)] >> 8;
        int i5 = i4 ^ 0xFF;
        int i6 = pixels[((paramInt2 + i2 - j) * width + (paramInt1 + i3 - i))];
        pixels[((paramInt2 + i2 - j) * width + paramInt1 + i3 - i)] = (0xFF000000 | (i4 * k + i5 * (i6 >> 16 & 0xFF) & 0xFF00) << 8 | i4 * m + i5 * (i6 >> 8 & 0xFF) & 0xFF00 | i4 * n + i5 * (i6 & 0xFF) >> 8);
      }
    }
  }
  
  public void pushMatrix()
  {
    showMethodWarning("pushMatrix");
  }
  
  public void popMatrix()
  {
    showMethodWarning("popMatrix");
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    showMissingWarning("translate");
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("translate");
  }
  
  public void rotate(float paramFloat)
  {
    showMissingWarning("rotate");
  }
  
  public void rotateX(float paramFloat)
  {
    showMethodWarning("rotateX");
  }
  
  public void rotateY(float paramFloat)
  {
    showMethodWarning("rotateY");
  }
  
  public void rotateZ(float paramFloat)
  {
    showMethodWarning("rotateZ");
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showMissingWarning("rotate");
  }
  
  public void scale(float paramFloat)
  {
    showMissingWarning("scale");
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    showMissingWarning("scale");
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("scale");
  }
  
  public void shearX(float paramFloat)
  {
    showMissingWarning("shearX");
  }
  
  public void shearY(float paramFloat)
  {
    showMissingWarning("shearY");
  }
  
  public void resetMatrix()
  {
    showMethodWarning("resetMatrix");
  }
  
  public void applyMatrix(PMatrix paramPMatrix)
  {
    if ((paramPMatrix instanceof PMatrix2D)) {
      applyMatrix((PMatrix2D)paramPMatrix);
    } else if ((paramPMatrix instanceof PMatrix3D)) {
      applyMatrix((PMatrix3D)paramPMatrix);
    }
  }
  
  public void applyMatrix(PMatrix2D paramPMatrix2D)
  {
    applyMatrix(m00, m01, m02, m10, m11, m12);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMissingWarning("applyMatrix");
  }
  
  public void applyMatrix(PMatrix3D paramPMatrix3D)
  {
    applyMatrix(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    showMissingWarning("applyMatrix");
  }
  
  public PMatrix getMatrix()
  {
    showMissingWarning("getMatrix");
    return null;
  }
  
  public PMatrix2D getMatrix(PMatrix2D paramPMatrix2D)
  {
    showMissingWarning("getMatrix");
    return null;
  }
  
  public PMatrix3D getMatrix(PMatrix3D paramPMatrix3D)
  {
    showMissingWarning("getMatrix");
    return null;
  }
  
  public void setMatrix(PMatrix paramPMatrix)
  {
    if ((paramPMatrix instanceof PMatrix2D)) {
      setMatrix((PMatrix2D)paramPMatrix);
    } else if ((paramPMatrix instanceof PMatrix3D)) {
      setMatrix((PMatrix3D)paramPMatrix);
    }
  }
  
  public void setMatrix(PMatrix2D paramPMatrix2D)
  {
    showMissingWarning("setMatrix");
  }
  
  public void setMatrix(PMatrix3D paramPMatrix3D)
  {
    showMissingWarning("setMatrix");
  }
  
  public void printMatrix()
  {
    showMethodWarning("printMatrix");
  }
  
  public void beginCamera()
  {
    showMethodWarning("beginCamera");
  }
  
  public void endCamera()
  {
    showMethodWarning("endCamera");
  }
  
  public void camera()
  {
    showMissingWarning("camera");
  }
  
  public void camera(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    showMissingWarning("camera");
  }
  
  public void printCamera()
  {
    showMethodWarning("printCamera");
  }
  
  public void ortho()
  {
    showMissingWarning("ortho");
  }
  
  public void ortho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showMissingWarning("ortho");
  }
  
  public void ortho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMissingWarning("ortho");
  }
  
  public void perspective()
  {
    showMissingWarning("perspective");
  }
  
  public void perspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showMissingWarning("perspective");
  }
  
  public void frustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMethodWarning("frustum");
  }
  
  public void printProjection()
  {
    showMethodWarning("printCamera");
  }
  
  public float screenX(float paramFloat1, float paramFloat2)
  {
    showMissingWarning("screenX");
    return 0.0F;
  }
  
  public float screenY(float paramFloat1, float paramFloat2)
  {
    showMissingWarning("screenY");
    return 0.0F;
  }
  
  public float screenX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("screenX");
    return 0.0F;
  }
  
  public float screenY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("screenY");
    return 0.0F;
  }
  
  public float screenZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("screenZ");
    return 0.0F;
  }
  
  public float modelX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("modelX");
    return 0.0F;
  }
  
  public float modelY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("modelY");
    return 0.0F;
  }
  
  public float modelZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMissingWarning("modelZ");
    return 0.0F;
  }
  
  public void pushStyle()
  {
    if (styleStackDepth == styleStack.length) {
      styleStack = ((PStyle[])PApplet.expand(styleStack));
    }
    if (styleStack[styleStackDepth] == null) {
      styleStack[styleStackDepth] = new PStyle();
    }
    PStyle localPStyle = styleStack[(styleStackDepth++)];
    getStyle(localPStyle);
  }
  
  public void popStyle()
  {
    if (styleStackDepth == 0) {
      throw new RuntimeException("Too many popStyle() without enough pushStyle()");
    }
    styleStackDepth -= 1;
    style(styleStack[styleStackDepth]);
  }
  
  public void style(PStyle paramPStyle)
  {
    imageMode(imageMode);
    rectMode(rectMode);
    ellipseMode(ellipseMode);
    shapeMode(shapeMode);
    if (tint) {
      tint(tintColor);
    } else {
      noTint();
    }
    if (fill) {
      fill(fillColor);
    } else {
      noFill();
    }
    if (stroke) {
      stroke(strokeColor);
    } else {
      noStroke();
    }
    strokeWeight(strokeWeight);
    strokeCap(strokeCap);
    strokeJoin(strokeJoin);
    colorMode(1, 1.0F);
    ambient(ambientR, ambientG, ambientB);
    emissive(emissiveR, emissiveG, emissiveB);
    specular(specularR, specularG, specularB);
    shininess(shininess);
    colorMode(colorMode, colorModeX, colorModeY, colorModeZ, colorModeA);
    if (textFont != null)
    {
      textFont(textFont, textSize);
      textLeading(textLeading);
    }
    textAlign(textAlign, textAlignY);
    textMode(textMode);
  }
  
  public PStyle getStyle()
  {
    return getStyle(null);
  }
  
  public PStyle getStyle(PStyle paramPStyle)
  {
    if (paramPStyle == null) {
      paramPStyle = new PStyle();
    }
    imageMode = imageMode;
    rectMode = rectMode;
    ellipseMode = ellipseMode;
    shapeMode = shapeMode;
    colorMode = colorMode;
    colorModeX = colorModeX;
    colorModeY = colorModeY;
    colorModeZ = colorModeZ;
    colorModeA = colorModeA;
    tint = tint;
    tintColor = tintColor;
    fill = fill;
    fillColor = fillColor;
    stroke = stroke;
    strokeColor = strokeColor;
    strokeWeight = strokeWeight;
    strokeCap = strokeCap;
    strokeJoin = strokeJoin;
    ambientR = ambientR;
    ambientG = ambientG;
    ambientB = ambientB;
    specularR = specularR;
    specularG = specularG;
    specularB = specularB;
    emissiveR = emissiveR;
    emissiveG = emissiveG;
    emissiveB = emissiveB;
    shininess = shininess;
    textFont = textFont;
    textAlign = textAlign;
    textAlignY = textAlignY;
    textMode = textMode;
    textSize = textSize;
    textLeading = textLeading;
    return paramPStyle;
  }
  
  public void strokeWeight(float paramFloat)
  {
    strokeWeight = paramFloat;
  }
  
  public void strokeJoin(int paramInt)
  {
    strokeJoin = paramInt;
  }
  
  public void strokeCap(int paramInt)
  {
    strokeCap = paramInt;
  }
  
  public void noStroke()
  {
    stroke = false;
  }
  
  public void stroke(int paramInt)
  {
    colorCalc(paramInt);
    strokeFromCalc();
  }
  
  public void stroke(int paramInt, float paramFloat)
  {
    colorCalc(paramInt, paramFloat);
    strokeFromCalc();
  }
  
  public void stroke(float paramFloat)
  {
    colorCalc(paramFloat);
    strokeFromCalc();
  }
  
  public void stroke(float paramFloat1, float paramFloat2)
  {
    colorCalc(paramFloat1, paramFloat2);
    strokeFromCalc();
  }
  
  public void stroke(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    strokeFromCalc();
  }
  
  public void stroke(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    strokeFromCalc();
  }
  
  protected void strokeFromCalc()
  {
    stroke = true;
    strokeR = calcR;
    strokeG = calcG;
    strokeB = calcB;
    strokeA = calcA;
    strokeRi = calcRi;
    strokeGi = calcGi;
    strokeBi = calcBi;
    strokeAi = calcAi;
    strokeColor = calcColor;
    strokeAlpha = calcAlpha;
  }
  
  public void noTint()
  {
    tint = false;
  }
  
  public void tint(int paramInt)
  {
    colorCalc(paramInt);
    tintFromCalc();
  }
  
  public void tint(int paramInt, float paramFloat)
  {
    colorCalc(paramInt, paramFloat);
    tintFromCalc();
  }
  
  public void tint(float paramFloat)
  {
    colorCalc(paramFloat);
    tintFromCalc();
  }
  
  public void tint(float paramFloat1, float paramFloat2)
  {
    colorCalc(paramFloat1, paramFloat2);
    tintFromCalc();
  }
  
  public void tint(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    tintFromCalc();
  }
  
  public void tint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    tintFromCalc();
  }
  
  protected void tintFromCalc()
  {
    tint = true;
    tintR = calcR;
    tintG = calcG;
    tintB = calcB;
    tintA = calcA;
    tintRi = calcRi;
    tintGi = calcGi;
    tintBi = calcBi;
    tintAi = calcAi;
    tintColor = calcColor;
    tintAlpha = calcAlpha;
  }
  
  public void noFill()
  {
    fill = false;
  }
  
  public void fill(int paramInt)
  {
    colorCalc(paramInt);
    fillFromCalc();
  }
  
  public void fill(int paramInt, float paramFloat)
  {
    colorCalc(paramInt, paramFloat);
    fillFromCalc();
  }
  
  public void fill(float paramFloat)
  {
    colorCalc(paramFloat);
    fillFromCalc();
  }
  
  public void fill(float paramFloat1, float paramFloat2)
  {
    colorCalc(paramFloat1, paramFloat2);
    fillFromCalc();
  }
  
  public void fill(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    fillFromCalc();
  }
  
  public void fill(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    fillFromCalc();
  }
  
  protected void fillFromCalc()
  {
    fill = true;
    fillR = calcR;
    fillG = calcG;
    fillB = calcB;
    fillA = calcA;
    fillRi = calcRi;
    fillGi = calcGi;
    fillBi = calcBi;
    fillAi = calcAi;
    fillColor = calcColor;
    fillAlpha = calcAlpha;
  }
  
  public void ambient(int paramInt)
  {
    colorCalc(paramInt);
    ambientFromCalc();
  }
  
  public void ambient(float paramFloat)
  {
    colorCalc(paramFloat);
    ambientFromCalc();
  }
  
  public void ambient(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    ambientFromCalc();
  }
  
  protected void ambientFromCalc()
  {
    ambientR = calcR;
    ambientG = calcG;
    ambientB = calcB;
  }
  
  public void specular(int paramInt)
  {
    colorCalc(paramInt);
    specularFromCalc();
  }
  
  public void specular(float paramFloat)
  {
    colorCalc(paramFloat);
    specularFromCalc();
  }
  
  public void specular(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    specularFromCalc();
  }
  
  protected void specularFromCalc()
  {
    specularR = calcR;
    specularG = calcG;
    specularB = calcB;
  }
  
  public void shininess(float paramFloat)
  {
    shininess = paramFloat;
  }
  
  public void emissive(int paramInt)
  {
    colorCalc(paramInt);
    emissiveFromCalc();
  }
  
  public void emissive(float paramFloat)
  {
    colorCalc(paramFloat);
    emissiveFromCalc();
  }
  
  public void emissive(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    emissiveFromCalc();
  }
  
  protected void emissiveFromCalc()
  {
    emissiveR = calcR;
    emissiveG = calcG;
    emissiveB = calcB;
  }
  
  public void lights()
  {
    showMethodWarning("lights");
  }
  
  public void noLights()
  {
    showMethodWarning("noLights");
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMethodWarning("ambientLight");
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMethodWarning("ambientLight");
  }
  
  public void directionalLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMethodWarning("directionalLight");
  }
  
  public void pointLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showMethodWarning("pointLight");
  }
  
  public void spotLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11)
  {
    showMethodWarning("spotLight");
  }
  
  public void lightFalloff(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMethodWarning("lightFalloff");
  }
  
  public void lightSpecular(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMethodWarning("lightSpecular");
  }
  
  public void background(int paramInt)
  {
    colorCalc(paramInt);
    backgroundFromCalc();
  }
  
  public void background(int paramInt, float paramFloat)
  {
    colorCalc(paramInt, paramFloat);
    backgroundFromCalc();
  }
  
  public void background(float paramFloat)
  {
    colorCalc(paramFloat);
    backgroundFromCalc();
  }
  
  public void background(float paramFloat1, float paramFloat2)
  {
    if (format == 1)
    {
      background(paramFloat1);
    }
    else
    {
      colorCalc(paramFloat1, paramFloat2);
      backgroundFromCalc();
    }
  }
  
  public void background(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    backgroundFromCalc();
  }
  
  public void background(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    backgroundFromCalc();
  }
  
  protected void backgroundFromCalc()
  {
    backgroundR = calcR;
    backgroundG = calcG;
    backgroundB = calcB;
    backgroundA = (format == 1 ? colorModeA : calcA);
    backgroundRi = calcRi;
    backgroundGi = calcGi;
    backgroundBi = calcBi;
    backgroundAi = (format == 1 ? 255 : calcAi);
    backgroundAlpha = (format == 1 ? false : calcAlpha);
    backgroundColor = calcColor;
    backgroundImpl();
  }
  
  public void background(PImage paramPImage)
  {
    if ((width != width) || (height != height)) {
      throw new RuntimeException("background image must be the same size as your application");
    }
    if ((format != 1) && (format != 2)) {
      throw new RuntimeException("background images should be RGB or ARGB");
    }
    backgroundColor = 0;
    backgroundImpl(paramPImage);
  }
  
  protected void backgroundImpl(PImage paramPImage)
  {
    set(0, 0, paramPImage);
  }
  
  protected void backgroundImpl()
  {
    pushStyle();
    pushMatrix();
    resetMatrix();
    fill(backgroundColor);
    rect(0.0F, 0.0F, width, height);
    popMatrix();
    popStyle();
  }
  
  public void colorMode(int paramInt)
  {
    colorMode(paramInt, colorModeX, colorModeY, colorModeZ, colorModeA);
  }
  
  public void colorMode(int paramInt, float paramFloat)
  {
    colorMode(paramInt, paramFloat, paramFloat, paramFloat, paramFloat);
  }
  
  public void colorMode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorMode(paramInt, paramFloat1, paramFloat2, paramFloat3, colorModeA);
  }
  
  public void colorMode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorMode = paramInt;
    colorModeX = paramFloat1;
    colorModeY = paramFloat2;
    colorModeZ = paramFloat3;
    colorModeA = paramFloat4;
    colorModeScale = ((paramFloat4 != 1.0F) || (paramFloat1 != paramFloat2) || (paramFloat2 != paramFloat3) || (paramFloat3 != paramFloat4));
    colorModeDefault = ((colorMode == 1) && (colorModeA == 255.0F) && (colorModeX == 255.0F) && (colorModeY == 255.0F) && (colorModeZ == 255.0F));
  }
  
  protected void colorCalc(int paramInt)
  {
    if (((paramInt & 0xFF000000) == 0) && (paramInt <= colorModeX)) {
      colorCalc(paramInt);
    } else {
      colorCalcARGB(paramInt, colorModeA);
    }
  }
  
  protected void colorCalc(int paramInt, float paramFloat)
  {
    if (((paramInt & 0xFF000000) == 0) && (paramInt <= colorModeX)) {
      colorCalc(paramInt, paramFloat);
    } else {
      colorCalcARGB(paramInt, paramFloat);
    }
  }
  
  protected void colorCalc(float paramFloat)
  {
    colorCalc(paramFloat, colorModeA);
  }
  
  protected void colorCalc(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 > colorModeX) {
      paramFloat1 = colorModeX;
    }
    if (paramFloat2 > colorModeA) {
      paramFloat2 = colorModeA;
    }
    if (paramFloat1 < 0.0F) {
      paramFloat1 = 0.0F;
    }
    if (paramFloat2 < 0.0F) {
      paramFloat2 = 0.0F;
    }
    calcR = (colorModeScale ? paramFloat1 / colorModeX : paramFloat1);
    calcG = calcR;
    calcB = calcR;
    calcA = (colorModeScale ? paramFloat2 / colorModeA : paramFloat2);
    calcRi = ((int)(calcR * 255.0F));
    calcGi = ((int)(calcG * 255.0F));
    calcBi = ((int)(calcB * 255.0F));
    calcAi = ((int)(calcA * 255.0F));
    calcColor = (calcAi << 24 | calcRi << 16 | calcGi << 8 | calcBi);
    calcAlpha = (calcAi != 255);
  }
  
  protected void colorCalc(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, colorModeA);
  }
  
  protected void colorCalc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (paramFloat1 > colorModeX) {
      paramFloat1 = colorModeX;
    }
    if (paramFloat2 > colorModeY) {
      paramFloat2 = colorModeY;
    }
    if (paramFloat3 > colorModeZ) {
      paramFloat3 = colorModeZ;
    }
    if (paramFloat4 > colorModeA) {
      paramFloat4 = colorModeA;
    }
    if (paramFloat1 < 0.0F) {
      paramFloat1 = 0.0F;
    }
    if (paramFloat2 < 0.0F) {
      paramFloat2 = 0.0F;
    }
    if (paramFloat3 < 0.0F) {
      paramFloat3 = 0.0F;
    }
    if (paramFloat4 < 0.0F) {
      paramFloat4 = 0.0F;
    }
    switch (colorMode)
    {
    case 1: 
      if (colorModeScale)
      {
        calcR = (paramFloat1 / colorModeX);
        calcG = (paramFloat2 / colorModeY);
        calcB = (paramFloat3 / colorModeZ);
        calcA = (paramFloat4 / colorModeA);
      }
      else
      {
        calcR = paramFloat1;
        calcG = paramFloat2;
        calcB = paramFloat3;
        calcA = paramFloat4;
      }
      break;
    case 3: 
      paramFloat1 /= colorModeX;
      paramFloat2 /= colorModeY;
      paramFloat3 /= colorModeZ;
      calcA = (colorModeScale ? paramFloat4 / colorModeA : paramFloat4);
      if (paramFloat2 == 0.0F)
      {
        calcR = (this.calcG = this.calcB = paramFloat3);
      }
      else
      {
        float f1 = (paramFloat1 - (int)paramFloat1) * 6.0F;
        float f2 = f1 - (int)f1;
        float f3 = paramFloat3 * (1.0F - paramFloat2);
        float f4 = paramFloat3 * (1.0F - paramFloat2 * f2);
        float f5 = paramFloat3 * (1.0F - paramFloat2 * (1.0F - f2));
        switch ((int)f1)
        {
        case 0: 
          calcR = paramFloat3;
          calcG = f5;
          calcB = f3;
          break;
        case 1: 
          calcR = f4;
          calcG = paramFloat3;
          calcB = f3;
          break;
        case 2: 
          calcR = f3;
          calcG = paramFloat3;
          calcB = f5;
          break;
        case 3: 
          calcR = f3;
          calcG = f4;
          calcB = paramFloat3;
          break;
        case 4: 
          calcR = f5;
          calcG = f3;
          calcB = paramFloat3;
          break;
        case 5: 
          calcR = paramFloat3;
          calcG = f3;
          calcB = f4;
        }
      }
      break;
    }
    calcRi = ((int)(255.0F * calcR));
    calcGi = ((int)(255.0F * calcG));
    calcBi = ((int)(255.0F * calcB));
    calcAi = ((int)(255.0F * calcA));
    calcColor = (calcAi << 24 | calcRi << 16 | calcGi << 8 | calcBi);
    calcAlpha = (calcAi != 255);
  }
  
  protected void colorCalcARGB(int paramInt, float paramFloat)
  {
    if (paramFloat == colorModeA)
    {
      calcAi = (paramInt >> 24 & 0xFF);
      calcColor = paramInt;
    }
    else
    {
      calcAi = ((int)((paramInt >> 24 & 0xFF) * (paramFloat / colorModeA)));
      calcColor = (calcAi << 24 | paramInt & 0xFFFFFF);
    }
    calcRi = (paramInt >> 16 & 0xFF);
    calcGi = (paramInt >> 8 & 0xFF);
    calcBi = (paramInt & 0xFF);
    calcA = (calcAi / 255.0F);
    calcR = (calcRi / 255.0F);
    calcG = (calcGi / 255.0F);
    calcB = (calcBi / 255.0F);
    calcAlpha = (calcAi != 255);
  }
  
  public final int color(int paramInt)
  {
    colorCalc(paramInt);
    return calcColor;
  }
  
  public final int color(float paramFloat)
  {
    colorCalc(paramFloat);
    return calcColor;
  }
  
  public final int color(int paramInt1, int paramInt2)
  {
    colorCalc(paramInt1, paramInt2);
    return calcColor;
  }
  
  public final int color(int paramInt, float paramFloat)
  {
    colorCalc(paramInt, paramFloat);
    return calcColor;
  }
  
  public final int color(float paramFloat1, float paramFloat2)
  {
    colorCalc(paramFloat1, paramFloat2);
    return calcColor;
  }
  
  public final int color(int paramInt1, int paramInt2, int paramInt3)
  {
    colorCalc(paramInt1, paramInt2, paramInt3);
    return calcColor;
  }
  
  public final int color(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    return calcColor;
  }
  
  public final int color(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    colorCalc(paramInt1, paramInt2, paramInt3, paramInt4);
    return calcColor;
  }
  
  public final int color(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    return calcColor;
  }
  
  public final float alpha(int paramInt)
  {
    float f = paramInt >> 24 & 0xFF;
    if (colorModeA == 255.0F) {
      return f;
    }
    return f / 255.0F * colorModeA;
  }
  
  public final float red(int paramInt)
  {
    float f = paramInt >> 16 & 0xFF;
    if (colorModeDefault) {
      return f;
    }
    return f / 255.0F * colorModeX;
  }
  
  public final float green(int paramInt)
  {
    float f = paramInt >> 8 & 0xFF;
    if (colorModeDefault) {
      return f;
    }
    return f / 255.0F * colorModeY;
  }
  
  public final float blue(int paramInt)
  {
    float f = paramInt & 0xFF;
    if (colorModeDefault) {
      return f;
    }
    return f / 255.0F * colorModeZ;
  }
  
  public final float hue(int paramInt)
  {
    if (paramInt != cacheHsbKey)
    {
      Color.RGBtoHSB(paramInt >> 16 & 0xFF, paramInt >> 8 & 0xFF, paramInt & 0xFF, cacheHsbValue);
      cacheHsbKey = paramInt;
    }
    return cacheHsbValue[0] * colorModeX;
  }
  
  public final float saturation(int paramInt)
  {
    if (paramInt != cacheHsbKey)
    {
      Color.RGBtoHSB(paramInt >> 16 & 0xFF, paramInt >> 8 & 0xFF, paramInt & 0xFF, cacheHsbValue);
      cacheHsbKey = paramInt;
    }
    return cacheHsbValue[1] * colorModeY;
  }
  
  public final float brightness(int paramInt)
  {
    if (paramInt != cacheHsbKey)
    {
      Color.RGBtoHSB(paramInt >> 16 & 0xFF, paramInt >> 8 & 0xFF, paramInt & 0xFF, cacheHsbValue);
      cacheHsbKey = paramInt;
    }
    return cacheHsbValue[2] * colorModeZ;
  }
  
  public int lerpColor(int paramInt1, int paramInt2, float paramFloat)
  {
    return lerpColor(paramInt1, paramInt2, paramFloat, colorMode);
  }
  
  public static int lerpColor(int paramInt1, int paramInt2, float paramFloat, int paramInt3)
  {
    float f1;
    float f2;
    float f4;
    float f5;
    float f6;
    if (paramInt3 == 1)
    {
      f1 = paramInt1 >> 24 & 0xFF;
      f2 = paramInt1 >> 16 & 0xFF;
      float f3 = paramInt1 >> 8 & 0xFF;
      f4 = paramInt1 & 0xFF;
      f5 = paramInt2 >> 24 & 0xFF;
      f6 = paramInt2 >> 16 & 0xFF;
      float f7 = paramInt2 >> 8 & 0xFF;
      float f8 = paramInt2 & 0xFF;
      return (int)(f1 + (f5 - f1) * paramFloat) << 24 | (int)(f2 + (f6 - f2) * paramFloat) << 16 | (int)(f3 + (f7 - f3) * paramFloat) << 8 | (int)(f4 + (f8 - f4) * paramFloat);
    }
    if (paramInt3 == 3)
    {
      if (lerpColorHSB1 == null)
      {
        lerpColorHSB1 = new float[3];
        lerpColorHSB2 = new float[3];
      }
      f1 = paramInt1 >> 24 & 0xFF;
      f2 = paramInt2 >> 24 & 0xFF;
      int i = (int)(f1 + (f2 - f1) * paramFloat) << 24;
      Color.RGBtoHSB(paramInt1 >> 16 & 0xFF, paramInt1 >> 8 & 0xFF, paramInt1 & 0xFF, lerpColorHSB1);
      Color.RGBtoHSB(paramInt2 >> 16 & 0xFF, paramInt2 >> 8 & 0xFF, paramInt2 & 0xFF, lerpColorHSB2);
      f4 = PApplet.lerp(lerpColorHSB1[0], lerpColorHSB2[0], paramFloat);
      f5 = PApplet.lerp(lerpColorHSB1[1], lerpColorHSB2[1], paramFloat);
      f6 = PApplet.lerp(lerpColorHSB1[2], lerpColorHSB2[2], paramFloat);
      return i | Color.HSBtoRGB(f4, f5, f6) & 0xFFFFFF;
    }
    return 0;
  }
  
  public void beginRaw(PGraphics paramPGraphics)
  {
    raw = paramPGraphics;
    paramPGraphics.beginDraw();
  }
  
  public void endRaw()
  {
    if (raw != null)
    {
      flush();
      raw.endDraw();
      raw.dispose();
      raw = null;
    }
  }
  
  public static void showWarning(String paramString)
  {
    if (warnings == null) {
      warnings = new HashMap();
    }
    if (!warnings.containsKey(paramString))
    {
      System.err.println(paramString);
      warnings.put(paramString, new Object());
    }
  }
  
  public static void showDepthWarning(String paramString)
  {
    showWarning(paramString + "() can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL.");
  }
  
  public static void showDepthWarningXYZ(String paramString)
  {
    showWarning(paramString + "() with x, y, and z coordinates " + "can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL. " + "Use a version without a z-coordinate instead.");
  }
  
  public static void showMethodWarning(String paramString)
  {
    showWarning(paramString + "() is not available with this renderer.");
  }
  
  public static void showVariationWarning(String paramString)
  {
    showWarning(paramString + " is not available with this renderer.");
  }
  
  public static void showMissingWarning(String paramString)
  {
    showWarning(paramString + "(), or this particular variation of it, " + "is not available with this renderer.");
  }
  
  public static void showException(String paramString)
  {
    throw new RuntimeException(paramString);
  }
  
  protected void defaultFontOrDeath(String paramString)
  {
    defaultFontOrDeath(paramString, 12.0F);
  }
  
  protected void defaultFontOrDeath(String paramString, float paramFloat)
  {
    if (parent != null) {
      textFont = parent.createDefaultFont(paramFloat);
    } else {
      throw new RuntimeException("Use textFont() before " + paramString + "()");
    }
  }
  
  public boolean displayable()
  {
    return true;
  }
  
  public boolean is2D()
  {
    return true;
  }
  
  public boolean is3D()
  {
    return false;
  }
  
  protected String[] getSupportedShapeFormats()
  {
    showMissingWarning("getSupportedShapeFormats");
    return null;
  }
  
  protected PShape loadShape(String paramString, Object paramObject)
  {
    showMissingWarning("loadShape");
    return null;
  }
  
  protected PShape createShape(int paramInt, Object paramObject)
  {
    showMissingWarning("createShape");
    return null;
  }
  
  public void screenBlend(int paramInt)
  {
    showMissingWarning("screenBlend");
  }
  
  public void textureBlend(int paramInt)
  {
    showMissingWarning("textureBlend");
  }
  
  public PShape beginRecord()
  {
    showMissingWarning("beginRecord");
    return null;
  }
  
  public void endRecord()
  {
    showMissingWarning("endRecord");
  }
  
  public boolean isRecording()
  {
    showMissingWarning("isRecording");
    return false;
  }
  
  public void mergeShapes(boolean paramBoolean)
  {
    showMissingWarning("mergeShapes");
  }
  
  public void shapeName(String paramString)
  {
    showMissingWarning("shapeName");
  }
  
  public void autoNormal(boolean paramBoolean)
  {
    autoNormal = paramBoolean;
  }
  
  public void matrixMode(int paramInt)
  {
    showMissingWarning("matrixMode");
  }
  
  public void beginText()
  {
    showMissingWarning("beginText");
  }
  
  public void endText()
  {
    showMissingWarning("endText");
  }
  
  public void texture(PImage... paramVarArgs)
  {
    showMissingWarning("texture");
  }
  
  public void vertex(float... paramVarArgs)
  {
    showMissingWarning("vertex");
  }
  
  static
  {
    for (int i = 0; i < 720; i++)
    {
      sinLUT[i] = ((float)Math.sin(i * 0.017453292F * 0.5F));
      cosLUT[i] = ((float)Math.cos(i * 0.017453292F * 0.5F));
    }
  }
}
