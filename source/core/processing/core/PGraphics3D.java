package processing.core;

import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;

public class PGraphics3D
  extends PGraphics
{
  public float[] zbuffer;
  public PMatrix3D modelview;
  public PMatrix3D modelviewInv;
  protected boolean sizeChanged;
  public PMatrix3D camera;
  protected PMatrix3D cameraInv;
  public float cameraFOV;
  public float cameraX;
  public float cameraY;
  public float cameraZ;
  public float cameraNear;
  public float cameraFar;
  public float cameraAspect;
  public PMatrix3D projection;
  public static final int MAX_LIGHTS = 8;
  public int lightCount = 0;
  public int[] lightType;
  public PVector[] lightPosition;
  public PVector[] lightNormal;
  public float[] lightFalloffConstant;
  public float[] lightFalloffLinear;
  public float[] lightFalloffQuadratic;
  public float[] lightSpotAngle;
  public float[] lightSpotAngleCos;
  public float[] lightSpotConcentration;
  public float[][] lightDiffuse;
  public float[][] lightSpecular;
  public float[] currentLightSpecular;
  public float currentLightFalloffConstant;
  public float currentLightFalloffLinear;
  public float currentLightFalloffQuadratic;
  public static final int TRI_DIFFUSE_R = 0;
  public static final int TRI_DIFFUSE_G = 1;
  public static final int TRI_DIFFUSE_B = 2;
  public static final int TRI_DIFFUSE_A = 3;
  public static final int TRI_SPECULAR_R = 4;
  public static final int TRI_SPECULAR_G = 5;
  public static final int TRI_SPECULAR_B = 6;
  public static final int TRI_COLOR_COUNT = 7;
  private boolean lightingDependsOnVertexPosition;
  static final int LIGHT_AMBIENT_R = 0;
  static final int LIGHT_AMBIENT_G = 1;
  static final int LIGHT_AMBIENT_B = 2;
  static final int LIGHT_DIFFUSE_R = 3;
  static final int LIGHT_DIFFUSE_G = 4;
  static final int LIGHT_DIFFUSE_B = 5;
  static final int LIGHT_SPECULAR_R = 6;
  static final int LIGHT_SPECULAR_G = 7;
  static final int LIGHT_SPECULAR_B = 8;
  static final int LIGHT_COLOR_COUNT = 9;
  protected float[] tempLightingContribution = new float[9];
  protected PVector lightTriangleNorm = new PVector();
  protected boolean manipulatingCamera;
  float[][] matrixStack = new float[32][16];
  float[][] matrixInvStack = new float[32][16];
  int matrixStackDepth;
  protected int matrixMode = 1;
  float[][] pmatrixStack = new float[32][16];
  int pmatrixStackDepth;
  protected PMatrix3D forwardTransform;
  protected PMatrix3D reverseTransform;
  protected float leftScreen;
  protected float rightScreen;
  protected float topScreen;
  protected float bottomScreen;
  protected float nearPlane;
  private boolean frustumMode = false;
  protected static boolean s_enableAccurateTextures = false;
  public PSmoothTriangle smoothTriangle;
  protected int shapeFirst;
  protected int shapeLast;
  protected int shapeLastPlusClipped;
  protected int[] vertexOrder = new int['Ȁ'];
  protected int pathCount;
  protected int[] pathOffset = new int[64];
  protected int[] pathLength = new int[64];
  protected static final int VERTEX1 = 0;
  protected static final int VERTEX2 = 1;
  protected static final int VERTEX3 = 2;
  protected static final int STROKE_COLOR = 1;
  protected static final int TEXTURE_INDEX = 3;
  protected static final int POINT_FIELD_COUNT = 2;
  protected static final int LINE_FIELD_COUNT = 2;
  protected static final int TRIANGLE_FIELD_COUNT = 4;
  static final int DEFAULT_POINTS = 512;
  protected int[][] points = new int['Ȁ'][2];
  protected int pointCount;
  static final int DEFAULT_LINES = 512;
  public PLine line;
  protected int[][] lines = new int['Ȁ'][2];
  protected int lineCount;
  static final int DEFAULT_TRIANGLES = 256;
  public PTriangle triangle;
  protected int[][] triangles = new int['Ā'][4];
  protected float[][][] triangleColors = new float['Ā'][3][7];
  protected int triangleCount;
  static final int DEFAULT_TEXTURES = 3;
  protected PImage[] textures = new PImage[3];
  int textureIndex;
  DirectColorModel cm;
  MemoryImageSource mis;
  float[] worldNormal = new float[4];
  PVector lightPositionVec = new PVector();
  PVector lightDirectionVec = new PVector();
  
  public PGraphics3D() {}
  
  public void setSize(int paramInt1, int paramInt2)
  {
    width = paramInt1;
    height = paramInt2;
    width1 = (width - 1);
    height1 = (height - 1);
    allocate();
    reapplySettings();
    lightType = new int[8];
    lightPosition = new PVector[8];
    lightNormal = new PVector[8];
    for (int i = 0; i < 8; i++)
    {
      lightPosition[i] = new PVector();
      lightNormal[i] = new PVector();
    }
    lightDiffuse = new float[8][3];
    lightSpecular = new float[8][3];
    lightFalloffConstant = new float[8];
    lightFalloffLinear = new float[8];
    lightFalloffQuadratic = new float[8];
    lightSpotAngle = new float[8];
    lightSpotAngleCos = new float[8];
    lightSpotConcentration = new float[8];
    currentLightSpecular = new float[3];
    projection = new PMatrix3D();
    modelview = new PMatrix3D();
    modelviewInv = new PMatrix3D();
    forwardTransform = modelview;
    reverseTransform = modelviewInv;
    cameraFOV = 1.0471976F;
    cameraX = (width / 2.0F);
    cameraY = (height / 2.0F);
    cameraZ = (cameraY / (float)Math.tan(cameraFOV / 2.0F));
    cameraNear = (cameraZ / 10.0F);
    cameraFar = (cameraZ * 10.0F);
    cameraAspect = (width / height);
    camera = new PMatrix3D();
    cameraInv = new PMatrix3D();
    sizeChanged = true;
  }
  
  protected void allocate()
  {
    pixelCount = (width * height);
    pixels = new int[pixelCount];
    zbuffer = new float[pixelCount];
    if (primarySurface)
    {
      cm = new DirectColorModel(32, 16711680, 65280, 255);
      mis = new MemoryImageSource(width, height, pixels, 0, width);
      mis.setFullBufferUpdates(true);
      mis.setAnimated(true);
      image = Toolkit.getDefaultToolkit().createImage(mis);
    }
    else
    {
      Arrays.fill(zbuffer, Float.MAX_VALUE);
    }
    line = new PLine(this);
    triangle = new PTriangle(this);
    smoothTriangle = new PSmoothTriangle(this);
  }
  
  public void beginDraw()
  {
    if (!settingsInited) {
      defaultSettings();
    }
    if (sizeChanged)
    {
      camera();
      perspective();
      sizeChanged = false;
    }
    resetMatrix();
    vertexCount = 0;
    modelview.set(camera);
    modelviewInv.set(cameraInv);
    lightCount = 0;
    lightingDependsOnVertexPosition = false;
    lightFalloff(1.0F, 0.0F, 0.0F);
    lightSpecular(0.0F, 0.0F, 0.0F);
    shapeFirst = 0;
    Arrays.fill(textures, null);
    textureIndex = 0;
    normal(0.0F, 0.0F, 1.0F);
  }
  
  public void endDraw()
  {
    if (hints[5] != 0) {
      flush();
    }
    if (mis != null) {
      mis.newPixels(pixels, cm, 0, width);
    }
    updatePixels();
  }
  
  protected void defaultSettings()
  {
    super.defaultSettings();
    manipulatingCamera = false;
    forwardTransform = modelview;
    reverseTransform = modelviewInv;
    camera();
    perspective();
    textureMode(2);
    emissive(0.0F);
    specular(0.5F);
    shininess(1.0F);
  }
  
  public void hint(int paramInt)
  {
    if (paramInt == -5) {
      flush();
    } else if ((paramInt == 4) && (zbuffer != null)) {
      Arrays.fill(zbuffer, Float.MAX_VALUE);
    }
    super.hint(paramInt);
  }
  
  public void beginShape(int paramInt)
  {
    shape = paramInt;
    if (hints[5] != 0)
    {
      shapeFirst = vertexCount;
      shapeLast = 0;
    }
    else
    {
      vertexCount = 0;
      if (line != null) {
        line.reset();
      }
      lineCount = 0;
      if (triangle != null) {
        triangle.reset();
      }
      triangleCount = 0;
    }
    textureImage = null;
    curveVertexCount = 0;
    normalMode = 0;
  }
  
  public void texture(PImage paramPImage)
  {
    textureImage = paramPImage;
    if (textureIndex == textures.length - 1) {
      textures = ((PImage[])PApplet.expand(textures));
    }
    if (textures[textureIndex] != null) {
      textureIndex += 1;
    }
    textures[textureIndex] = paramPImage;
  }
  
  public void vertex(float paramFloat1, float paramFloat2)
  {
    vertex(paramFloat1, paramFloat2, 0.0F);
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    vertex(paramFloat1, paramFloat2, 0.0F, paramFloat3, paramFloat4);
  }
  
  public void endShape(int paramInt)
  {
    shapeLast = vertexCount;
    shapeLastPlusClipped = shapeLast;
    if (vertexCount == 0)
    {
      shape = 0;
      return;
    }
    endShapeModelToCamera(shapeFirst, shapeLast);
    if (stroke) {
      endShapeStroke(paramInt);
    }
    if ((fill) || (textureImage != null)) {
      endShapeFill();
    }
    endShapeLighting((lightCount > 0) && (fill));
    endShapeCameraToScreen(shapeFirst, shapeLastPlusClipped);
    if (hints[5] == 0)
    {
      if (((fill) || (textureImage != null)) && (triangleCount > 0))
      {
        renderTriangles(0, triangleCount);
        if (raw != null) {
          rawTriangles(0, triangleCount);
        }
        triangleCount = 0;
      }
      if (stroke)
      {
        if (pointCount > 0)
        {
          renderPoints(0, pointCount);
          if (raw != null) {
            rawPoints(0, pointCount);
          }
          pointCount = 0;
        }
        if (lineCount > 0)
        {
          renderLines(0, lineCount);
          if (raw != null) {
            rawLines(0, lineCount);
          }
          lineCount = 0;
        }
      }
      pathCount = 0;
    }
    shape = 0;
  }
  
  protected void endShapeModelToCamera(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat = vertices[i];
      arrayOfFloat[21] = (modelview.m00 * arrayOfFloat[0] + modelview.m01 * arrayOfFloat[1] + modelview.m02 * arrayOfFloat[2] + modelview.m03);
      arrayOfFloat[22] = (modelview.m10 * arrayOfFloat[0] + modelview.m11 * arrayOfFloat[1] + modelview.m12 * arrayOfFloat[2] + modelview.m13);
      arrayOfFloat[23] = (modelview.m20 * arrayOfFloat[0] + modelview.m21 * arrayOfFloat[1] + modelview.m22 * arrayOfFloat[2] + modelview.m23);
      arrayOfFloat[24] = (modelview.m30 * arrayOfFloat[0] + modelview.m31 * arrayOfFloat[1] + modelview.m32 * arrayOfFloat[2] + modelview.m33);
      if ((arrayOfFloat[24] != 0.0F) && (arrayOfFloat[24] != 1.0F))
      {
        arrayOfFloat[21] /= arrayOfFloat[24];
        arrayOfFloat[22] /= arrayOfFloat[24];
        arrayOfFloat[23] /= arrayOfFloat[24];
      }
      arrayOfFloat[24] = 1.0F;
    }
  }
  
  protected void endShapeStroke(int paramInt)
  {
    int i;
    int j;
    switch (shape)
    {
    case 2: 
      i = shapeLast;
      for (j = shapeFirst; j < i; j++) {
        addPoint(j);
      }
      break;
    case 4: 
      i = lineCount;
      j = shapeLast - 1;
      if (shape != 4) {
        addLineBreak();
      }
      for (int k = shapeFirst; k < j; k += 2)
      {
        if (shape == 4) {
          addLineBreak();
        }
        addLine(k, k + 1);
      }
      if (paramInt == 2) {
        addLine(j, lines[i][0]);
      }
      break;
    case 9: 
      for (i = shapeFirst; i < shapeLast - 2; i += 3)
      {
        addLineBreak();
        addLine(i + 0, i + 1);
        addLine(i + 1, i + 2);
        addLine(i + 2, i + 0);
      }
      break;
    case 10: 
      i = shapeLast - 1;
      addLineBreak();
      for (j = shapeFirst; j < i; j++) {
        addLine(j, j + 1);
      }
      i = shapeLast - 2;
      for (j = shapeFirst; j < i; j++)
      {
        addLineBreak();
        addLine(j, j + 2);
      }
      break;
    case 11: 
      for (i = shapeFirst + 1; i < shapeLast; i++)
      {
        addLineBreak();
        addLine(shapeFirst, i);
      }
      addLineBreak();
      for (i = shapeFirst + 1; i < shapeLast - 1; i++) {
        addLine(i, i + 1);
      }
      addLine(shapeLast - 1, shapeFirst + 1);
      break;
    case 16: 
      for (i = shapeFirst; i < shapeLast; i += 4)
      {
        addLineBreak();
        addLine(i + 0, i + 1);
        addLine(i + 1, i + 2);
        addLine(i + 2, i + 3);
        addLine(i + 3, i + 0);
      }
      break;
    case 17: 
      for (i = shapeFirst; i < shapeLast - 3; i += 2)
      {
        addLineBreak();
        addLine(i + 0, i + 2);
        addLine(i + 2, i + 3);
        addLine(i + 3, i + 1);
        addLine(i + 1, i + 0);
      }
      break;
    case 20: 
      i = shapeLast - 1;
      addLineBreak();
      for (j = shapeFirst; j < i; j++) {
        addLine(j, j + 1);
      }
      if (paramInt == 2) {
        addLine(i, shapeFirst);
      }
      break;
    }
  }
  
  protected void endShapeFill()
  {
    int i;
    int j;
    switch (shape)
    {
    case 11: 
      i = shapeLast - 1;
      for (j = shapeFirst + 1; j < i; j++) {
        addTriangle(shapeFirst, j, j + 1);
      }
      break;
    case 9: 
      i = shapeLast - 2;
      for (j = shapeFirst; j < i; j += 3) {
        if (j % 2 == 0) {
          addTriangle(j, j + 2, j + 1);
        } else {
          addTriangle(j, j + 1, j + 2);
        }
      }
      break;
    case 10: 
      i = shapeLast - 2;
      for (j = shapeFirst; j < i; j++) {
        if (j % 2 == 0) {
          addTriangle(j, j + 2, j + 1);
        } else {
          addTriangle(j, j + 1, j + 2);
        }
      }
      break;
    case 16: 
      i = vertexCount - 3;
      for (j = shapeFirst; j < i; j += 4)
      {
        addTriangle(j, j + 1, j + 2);
        addTriangle(j, j + 2, j + 3);
      }
      break;
    case 17: 
      i = vertexCount - 3;
      for (j = shapeFirst; j < i; j += 2)
      {
        addTriangle(j + 0, j + 2, j + 1);
        addTriangle(j + 2, j + 3, j + 1);
      }
      break;
    case 20: 
      addPolygonTriangles();
    }
  }
  
  protected void endShapeLighting(boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
    {
      if ((!lightingDependsOnVertexPosition) && (normalMode == 1))
      {
        calcLightingContribution(shapeFirst, tempLightingContribution);
        for (i = 0; i < triangleCount; i++) {
          lightTriangle(i, tempLightingContribution);
        }
      }
      else
      {
        for (i = 0; i < triangleCount; i++) {
          lightTriangle(i);
        }
      }
    }
    else {
      for (i = 0; i < triangleCount; i++)
      {
        int j = triangles[i][0];
        copyPrelitVertexColor(i, j, 0);
        j = triangles[i][1];
        copyPrelitVertexColor(i, j, 1);
        j = triangles[i][2];
        copyPrelitVertexColor(i, j, 2);
      }
    }
  }
  
  protected void endShapeCameraToScreen(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat = vertices[i];
      float f1 = projection.m00 * arrayOfFloat[21] + projection.m01 * arrayOfFloat[22] + projection.m02 * arrayOfFloat[23] + projection.m03 * arrayOfFloat[24];
      float f2 = projection.m10 * arrayOfFloat[21] + projection.m11 * arrayOfFloat[22] + projection.m12 * arrayOfFloat[23] + projection.m13 * arrayOfFloat[24];
      float f3 = projection.m20 * arrayOfFloat[21] + projection.m21 * arrayOfFloat[22] + projection.m22 * arrayOfFloat[23] + projection.m23 * arrayOfFloat[24];
      float f4 = projection.m30 * arrayOfFloat[21] + projection.m31 * arrayOfFloat[22] + projection.m32 * arrayOfFloat[23] + projection.m33 * arrayOfFloat[24];
      if ((f4 != 0.0F) && (f4 != 1.0F))
      {
        f1 /= f4;
        f2 /= f4;
        f3 /= f4;
      }
      arrayOfFloat[18] = (width * (1.0F + f1) / 2.0F);
      arrayOfFloat[19] = (height * (1.0F + f2) / 2.0F);
      arrayOfFloat[20] = ((f3 + 1.0F) / 2.0F);
    }
  }
  
  protected void addPoint(int paramInt)
  {
    if (pointCount == points.length)
    {
      int[][] arrayOfInt = new int[pointCount << 1][2];
      System.arraycopy(points, 0, arrayOfInt, 0, pointCount);
      points = arrayOfInt;
    }
    points[pointCount][0] = paramInt;
    points[pointCount][1] = strokeColor;
    pointCount += 1;
  }
  
  protected void renderPoints(int paramInt1, int paramInt2)
  {
    int i;
    float[] arrayOfFloat;
    if (strokeWeight != 1.0F) {
      for (i = paramInt1; i < paramInt2; i++)
      {
        arrayOfFloat = vertices[points[i][0]];
        renderLineVertices(arrayOfFloat, arrayOfFloat);
      }
    } else {
      for (i = paramInt1; i < paramInt2; i++)
      {
        arrayOfFloat = vertices[points[i][0]];
        int j = (int)(arrayOfFloat[18] + 0.4999F);
        int k = (int)(arrayOfFloat[19] + 0.4999F);
        if ((j >= 0) && (j < width) && (k >= 0) && (k < height))
        {
          int m = k * width + j;
          pixels[m] = points[i][1];
          zbuffer[m] = arrayOfFloat[20];
        }
      }
    }
  }
  
  protected void rawPoints(int paramInt1, int paramInt2)
  {
    raw.colorMode(1, 1.0F);
    raw.noFill();
    raw.strokeWeight(vertices[lines[paramInt1][0]][17]);
    raw.beginShape(2);
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat = vertices[lines[i][0]];
      if (raw.is3D())
      {
        if (arrayOfFloat[24] != 0.0F)
        {
          raw.stroke(arrayOfFloat[13], arrayOfFloat[14], arrayOfFloat[15], arrayOfFloat[16]);
          raw.vertex(arrayOfFloat[21] / arrayOfFloat[24], arrayOfFloat[22] / arrayOfFloat[24], arrayOfFloat[23] / arrayOfFloat[24]);
        }
      }
      else
      {
        raw.stroke(arrayOfFloat[13], arrayOfFloat[14], arrayOfFloat[15], arrayOfFloat[16]);
        raw.vertex(arrayOfFloat[18], arrayOfFloat[19]);
      }
    }
    raw.endShape();
  }
  
  protected final void addLineBreak()
  {
    if (pathCount == pathOffset.length)
    {
      pathOffset = PApplet.expand(pathOffset);
      pathLength = PApplet.expand(pathLength);
    }
    pathOffset[pathCount] = lineCount;
    pathLength[pathCount] = 0;
    pathCount += 1;
  }
  
  protected void addLine(int paramInt1, int paramInt2)
  {
    addLineWithClip(paramInt1, paramInt2);
  }
  
  protected final void addLineWithClip(int paramInt1, int paramInt2)
  {
    float f1 = vertices[paramInt1][23];
    float f2 = vertices[paramInt2][23];
    if (f1 > cameraNear)
    {
      if (f2 > cameraNear) {
        return;
      }
      i = interpolateClipVertex(paramInt1, paramInt2);
      addLineWithoutClip(i, paramInt2);
      return;
    }
    if (f2 <= cameraNear)
    {
      addLineWithoutClip(paramInt1, paramInt2);
      return;
    }
    int i = interpolateClipVertex(paramInt1, paramInt2);
    addLineWithoutClip(paramInt1, i);
  }
  
  protected final void addLineWithoutClip(int paramInt1, int paramInt2)
  {
    if (lineCount == lines.length)
    {
      int[][] arrayOfInt = new int[lineCount << 1][2];
      System.arraycopy(lines, 0, arrayOfInt, 0, lineCount);
      lines = arrayOfInt;
    }
    lines[lineCount][0] = paramInt1;
    lines[lineCount][1] = paramInt2;
    lineCount += 1;
    pathLength[(pathCount - 1)] += 1;
  }
  
  protected void renderLines(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++) {
      renderLineVertices(vertices[lines[i][0]], vertices[lines[i][1]]);
    }
  }
  
  protected void renderLineVertices(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if ((paramArrayOfFloat1[17] > 1.25F) || (paramArrayOfFloat1[17] < 0.75F))
    {
      float f1 = paramArrayOfFloat1[18];
      float f2 = paramArrayOfFloat1[19];
      float f3 = paramArrayOfFloat2[18];
      float f4 = paramArrayOfFloat2[19];
      float f5 = paramArrayOfFloat1[17] / 2.0F;
      if ((f1 == f3) && (f2 == f4))
      {
        f2 -= f5;
        f4 += f5;
      }
      float f6 = f3 - f1 + 1.0E-4F;
      float f7 = f4 - f2 + 1.0E-4F;
      float f8 = (float)Math.sqrt(f6 * f6 + f7 * f7);
      float f9 = f5 / f8;
      float f10 = f9 * f7;
      float f11 = f9 * f6;
      float f12 = f9 * f7;
      float f13 = f9 * f6;
      float f14 = f1 + f10;
      float f15 = f2 - f11;
      float f16 = f1 - f10;
      float f17 = f2 + f11;
      float f18 = f3 + f12;
      float f19 = f4 - f13;
      float f20 = f3 - f12;
      float f21 = f4 + f13;
      if (smooth)
      {
        smoothTriangle.reset(3);
        smoothTriangle.smooth = true;
        smoothTriangle.interpARGB = true;
        smoothTriangle.setVertices(f14, f15, paramArrayOfFloat1[20], f20, f21, paramArrayOfFloat2[20], f16, f17, paramArrayOfFloat1[20]);
        smoothTriangle.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16], paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16]);
        smoothTriangle.render();
        smoothTriangle.setVertices(f14, f15, paramArrayOfFloat1[20], f20, f21, paramArrayOfFloat2[20], f18, f19, paramArrayOfFloat2[20]);
        smoothTriangle.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16]);
        smoothTriangle.render();
      }
      else
      {
        triangle.reset();
        triangle.setVertices(f14, f15, paramArrayOfFloat1[20], f20, f21, paramArrayOfFloat2[20], f16, f17, paramArrayOfFloat1[20]);
        triangle.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16], paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16]);
        triangle.render();
        triangle.setVertices(f14, f15, paramArrayOfFloat1[20], f20, f21, paramArrayOfFloat2[20], f18, f19, paramArrayOfFloat2[20]);
        triangle.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16]);
        triangle.render();
      }
    }
    else
    {
      line.reset();
      line.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16]);
      line.setVertices(paramArrayOfFloat1[18], paramArrayOfFloat1[19], paramArrayOfFloat1[20], paramArrayOfFloat2[18], paramArrayOfFloat2[19], paramArrayOfFloat2[20]);
      line.draw();
    }
  }
  
  protected void rawLines(int paramInt1, int paramInt2)
  {
    raw.colorMode(1, 1.0F);
    raw.noFill();
    raw.beginShape(4);
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat1 = vertices[lines[i][0]];
      float[] arrayOfFloat2 = vertices[lines[i][1]];
      raw.strokeWeight(vertices[lines[i][1]][17]);
      if (raw.is3D())
      {
        if ((arrayOfFloat1[24] != 0.0F) && (arrayOfFloat2[24] != 0.0F))
        {
          raw.stroke(arrayOfFloat1[13], arrayOfFloat1[14], arrayOfFloat1[15], arrayOfFloat1[16]);
          raw.vertex(arrayOfFloat1[21] / arrayOfFloat1[24], arrayOfFloat1[22] / arrayOfFloat1[24], arrayOfFloat1[23] / arrayOfFloat1[24]);
          raw.stroke(arrayOfFloat2[13], arrayOfFloat2[14], arrayOfFloat2[15], arrayOfFloat2[16]);
          raw.vertex(arrayOfFloat2[21] / arrayOfFloat2[24], arrayOfFloat2[22] / arrayOfFloat2[24], arrayOfFloat2[23] / arrayOfFloat2[24]);
        }
      }
      else if (raw.is2D())
      {
        raw.stroke(arrayOfFloat1[13], arrayOfFloat1[14], arrayOfFloat1[15], arrayOfFloat1[16]);
        raw.vertex(arrayOfFloat1[18], arrayOfFloat1[19]);
        raw.stroke(arrayOfFloat2[13], arrayOfFloat2[14], arrayOfFloat2[15], arrayOfFloat2[16]);
        raw.vertex(arrayOfFloat2[18], arrayOfFloat2[19]);
      }
    }
    raw.endShape();
  }
  
  protected void addTriangle(int paramInt1, int paramInt2, int paramInt3)
  {
    addTriangleWithClip(paramInt1, paramInt2, paramInt3);
  }
  
  protected final void addTriangleWithClip(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    cameraNear = -8.0F;
    if (vertices[paramInt1][23] > cameraNear)
    {
      i = 1;
      k++;
    }
    if (vertices[paramInt2][23] > cameraNear)
    {
      j = 1;
      k++;
    }
    if (vertices[paramInt3][23] > cameraNear) {
      k++;
    }
    if (k == 0)
    {
      addTriangleWithoutClip(paramInt1, paramInt2, paramInt3);
    }
    else if (k != 3)
    {
      int m;
      int n;
      int i1;
      int i2;
      int i3;
      if (k == 2)
      {
        if (i == 0)
        {
          m = paramInt1;
          n = paramInt2;
          i1 = paramInt3;
        }
        else if (j == 0)
        {
          m = paramInt2;
          n = paramInt1;
          i1 = paramInt3;
        }
        else
        {
          m = paramInt3;
          n = paramInt2;
          i1 = paramInt1;
        }
        i2 = interpolateClipVertex(m, n);
        i3 = interpolateClipVertex(m, i1);
        addTriangleWithoutClip(m, i2, i3);
      }
      else
      {
        if (i != 0)
        {
          m = paramInt3;
          n = paramInt2;
          i1 = paramInt1;
        }
        else if (j != 0)
        {
          m = paramInt1;
          n = paramInt3;
          i1 = paramInt2;
        }
        else
        {
          m = paramInt1;
          n = paramInt2;
          i1 = paramInt3;
        }
        i2 = interpolateClipVertex(m, i1);
        i3 = interpolateClipVertex(n, i1);
        addTriangleWithoutClip(m, i2, n);
        addTriangleWithoutClip(n, i2, i3);
      }
    }
  }
  
  protected final int interpolateClipVertex(int paramInt1, int paramInt2)
  {
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    if (vertices[paramInt1][23] < vertices[paramInt2][23])
    {
      arrayOfFloat1 = vertices[paramInt2];
      arrayOfFloat2 = vertices[paramInt1];
    }
    else
    {
      arrayOfFloat1 = vertices[paramInt1];
      arrayOfFloat2 = vertices[paramInt2];
    }
    float f1 = arrayOfFloat1[23];
    float f2 = arrayOfFloat2[23];
    float f3 = f1 - f2;
    if (f3 == 0.0F) {
      return paramInt1;
    }
    float f4 = (cameraNear - f2) / f3;
    float f5 = 1.0F - f4;
    vertex(f4 * arrayOfFloat1[0] + f5 * arrayOfFloat2[0], f4 * arrayOfFloat1[1] + f5 * arrayOfFloat2[1], f4 * arrayOfFloat1[2] + f5 * arrayOfFloat2[2]);
    int i = vertexCount - 1;
    shapeLastPlusClipped += 1;
    float[] arrayOfFloat3 = vertices[i];
    arrayOfFloat3[18] = (f4 * arrayOfFloat1[18] + f5 * arrayOfFloat2[18]);
    arrayOfFloat3[19] = (f4 * arrayOfFloat1[19] + f5 * arrayOfFloat2[19]);
    arrayOfFloat3[20] = (f4 * arrayOfFloat1[20] + f5 * arrayOfFloat2[20]);
    arrayOfFloat3[21] = (f4 * arrayOfFloat1[21] + f5 * arrayOfFloat2[21]);
    arrayOfFloat3[22] = (f4 * arrayOfFloat1[22] + f5 * arrayOfFloat2[22]);
    arrayOfFloat3[23] = (f4 * arrayOfFloat1[23] + f5 * arrayOfFloat2[23]);
    arrayOfFloat3[24] = (f4 * arrayOfFloat1[24] + f5 * arrayOfFloat2[24]);
    arrayOfFloat3[3] = (f4 * arrayOfFloat1[3] + f5 * arrayOfFloat2[3]);
    arrayOfFloat3[4] = (f4 * arrayOfFloat1[4] + f5 * arrayOfFloat2[4]);
    arrayOfFloat3[5] = (f4 * arrayOfFloat1[5] + f5 * arrayOfFloat2[5]);
    arrayOfFloat3[6] = (f4 * arrayOfFloat1[6] + f5 * arrayOfFloat2[6]);
    arrayOfFloat3[7] = (f4 * arrayOfFloat1[7] + f5 * arrayOfFloat2[7]);
    arrayOfFloat3[8] = (f4 * arrayOfFloat1[8] + f5 * arrayOfFloat2[8]);
    arrayOfFloat3[13] = (f4 * arrayOfFloat1[13] + f5 * arrayOfFloat2[13]);
    arrayOfFloat3[14] = (f4 * arrayOfFloat1[14] + f5 * arrayOfFloat2[14]);
    arrayOfFloat3[15] = (f4 * arrayOfFloat1[15] + f5 * arrayOfFloat2[15]);
    arrayOfFloat3[16] = (f4 * arrayOfFloat1[16] + f5 * arrayOfFloat2[16]);
    arrayOfFloat3[9] = (f4 * arrayOfFloat1[9] + f5 * arrayOfFloat2[9]);
    arrayOfFloat3[10] = (f4 * arrayOfFloat1[10] + f5 * arrayOfFloat2[10]);
    arrayOfFloat3[11] = (f4 * arrayOfFloat1[11] + f5 * arrayOfFloat2[11]);
    arrayOfFloat3[25] = (f4 * arrayOfFloat1[25] + f5 * arrayOfFloat2[25]);
    arrayOfFloat3[26] = (f4 * arrayOfFloat1[26] + f5 * arrayOfFloat2[26]);
    arrayOfFloat3[27] = (f4 * arrayOfFloat1[27] + f5 * arrayOfFloat2[27]);
    arrayOfFloat3[28] = (f4 * arrayOfFloat1[28] + f5 * arrayOfFloat2[28]);
    arrayOfFloat3[29] = (f4 * arrayOfFloat1[29] + f5 * arrayOfFloat2[29]);
    arrayOfFloat3[30] = (f4 * arrayOfFloat1[30] + f5 * arrayOfFloat2[30]);
    arrayOfFloat3[32] = (f4 * arrayOfFloat1[32] + f5 * arrayOfFloat2[32]);
    arrayOfFloat3[33] = (f4 * arrayOfFloat1[33] + f5 * arrayOfFloat2[33]);
    arrayOfFloat3[34] = (f4 * arrayOfFloat1[34] + f5 * arrayOfFloat2[34]);
    arrayOfFloat3[31] = (f4 * arrayOfFloat1[31] + f5 * arrayOfFloat2[31]);
    arrayOfFloat3[35] = 0.0F;
    return i;
  }
  
  protected final void addTriangleWithoutClip(int paramInt1, int paramInt2, int paramInt3)
  {
    if (triangleCount == triangles.length)
    {
      int[][] arrayOfInt = new int[triangleCount << 1][4];
      System.arraycopy(triangles, 0, arrayOfInt, 0, triangleCount);
      triangles = arrayOfInt;
      float[][][] arrayOfFloat = new float[triangleCount << 1][3][7];
      System.arraycopy(triangleColors, 0, arrayOfFloat, 0, triangleCount);
      triangleColors = arrayOfFloat;
    }
    triangles[triangleCount][0] = paramInt1;
    triangles[triangleCount][1] = paramInt2;
    triangles[triangleCount][2] = paramInt3;
    if (textureImage == null) {
      triangles[triangleCount][3] = -1;
    } else {
      triangles[triangleCount][3] = textureIndex;
    }
    triangleCount += 1;
  }
  
  protected void addPolygonTriangles()
  {
    if (vertexOrder.length != vertices.length)
    {
      int[] arrayOfInt = new int[vertices.length];
      PApplet.arrayCopy(vertexOrder, arrayOfInt, vertexOrder.length);
      vertexOrder = arrayOfInt;
    }
    int i = 0;
    int j = 1;
    float f = 0.0F;
    int k = shapeLast - 1;
    int m = shapeFirst;
    while (m < shapeLast)
    {
      f += vertices[m][i] * vertices[k][j] - vertices[k][i] * vertices[m][j];
      k = m++;
    }
    if (f == 0.0F)
    {
      k = 0;
      m = 0;
      for (n = shapeFirst; n < shapeLast; n++) {
        for (i1 = n; i1 < shapeLast; i1++)
        {
          if (vertices[n][0] != vertices[i1][0]) {
            k = 1;
          }
          if (vertices[n][1] != vertices[i1][1]) {
            m = 1;
          }
        }
      }
      if (k != 0)
      {
        j = 2;
      }
      else if (m != 0)
      {
        i = 1;
        j = 2;
      }
      else
      {
        return;
      }
      n = shapeLast - 1;
      i1 = shapeFirst;
      while (i1 < shapeLast)
      {
        f += vertices[i1][i] * vertices[n][j] - vertices[n][i] * vertices[i1][j];
        n = i1++;
      }
    }
    float[] arrayOfFloat1 = vertices[shapeFirst];
    float[] arrayOfFloat2 = vertices[(shapeLast - 1)];
    if ((abs(arrayOfFloat1[0] - arrayOfFloat2[0]) < 1.0E-4F) && (abs(arrayOfFloat1[1] - arrayOfFloat2[1]) < 1.0E-4F) && (abs(arrayOfFloat1[2] - arrayOfFloat2[2]) < 1.0E-4F)) {
      shapeLast -= 1;
    }
    int n = 0;
    if (f > 0.0F) {
      for (i1 = shapeFirst; i1 < shapeLast; i1++)
      {
        n = i1 - shapeFirst;
        vertexOrder[n] = i1;
      }
    } else {
      for (i1 = shapeFirst; i1 < shapeLast; i1++)
      {
        n = i1 - shapeFirst;
        vertexOrder[n] = (shapeLast - 1 - n);
      }
    }
    int i1 = shapeLast - shapeFirst;
    int i2 = 2 * i1;
    int i3 = 0;
    int i4 = i1 - 1;
    while (i1 > 2)
    {
      int i5 = 1;
      if (0 >= i2--) {
        break;
      }
      int i6 = i4;
      if (i1 <= i6) {
        i6 = 0;
      }
      i4 = i6 + 1;
      if (i1 <= i4) {
        i4 = 0;
      }
      int i7 = i4 + 1;
      if (i1 <= i7) {
        i7 = 0;
      }
      double d1 = -10.0F * vertices[vertexOrder[i6]][i];
      double d2 = 10.0F * vertices[vertexOrder[i6]][j];
      double d3 = -10.0F * vertices[vertexOrder[i4]][i];
      double d4 = 10.0F * vertices[vertexOrder[i4]][j];
      double d5 = -10.0F * vertices[vertexOrder[i7]][i];
      double d6 = 10.0F * vertices[vertexOrder[i7]][j];
      if (9.999999747378752E-5D <= (d3 - d1) * (d6 - d2) - (d4 - d2) * (d5 - d1))
      {
        for (int i8 = 0; i8 < i1; i8++) {
          if ((i8 != i6) && (i8 != i4) && (i8 != i7))
          {
            double d7 = -10.0F * vertices[vertexOrder[i8]][i];
            double d8 = 10.0F * vertices[vertexOrder[i8]][j];
            double d9 = d5 - d3;
            double d10 = d6 - d4;
            double d11 = d1 - d5;
            double d12 = d2 - d6;
            double d13 = d3 - d1;
            double d14 = d4 - d2;
            double d15 = d7 - d1;
            double d16 = d8 - d2;
            double d17 = d7 - d3;
            double d18 = d8 - d4;
            double d19 = d7 - d5;
            double d20 = d8 - d6;
            double d21 = d9 * d18 - d10 * d17;
            double d22 = d13 * d16 - d14 * d15;
            double d23 = d11 * d20 - d12 * d19;
            if ((d21 >= 0.0D) && (d23 >= 0.0D) && (d22 >= 0.0D)) {
              i5 = 0;
            }
          }
        }
        if (i5 != 0)
        {
          addTriangle(vertexOrder[i6], vertexOrder[i4], vertexOrder[i7]);
          i3++;
          i8 = i4;
          for (int i9 = i4 + 1; i9 < i1; i9++)
          {
            vertexOrder[i8] = vertexOrder[i9];
            i8++;
          }
          i1--;
          i2 = 2 * i1;
        }
      }
    }
  }
  
  private void toWorldNormal(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOfFloat)
  {
    paramArrayOfFloat[0] = (modelviewInv.m00 * paramFloat1 + modelviewInv.m10 * paramFloat2 + modelviewInv.m20 * paramFloat3 + modelviewInv.m30);
    paramArrayOfFloat[1] = (modelviewInv.m01 * paramFloat1 + modelviewInv.m11 * paramFloat2 + modelviewInv.m21 * paramFloat3 + modelviewInv.m31);
    paramArrayOfFloat[2] = (modelviewInv.m02 * paramFloat1 + modelviewInv.m12 * paramFloat2 + modelviewInv.m22 * paramFloat3 + modelviewInv.m32);
    paramArrayOfFloat[3] = (modelviewInv.m03 * paramFloat1 + modelviewInv.m13 * paramFloat2 + modelviewInv.m23 * paramFloat3 + modelviewInv.m33);
    if ((paramArrayOfFloat[3] != 0.0F) && (paramArrayOfFloat[3] != 1.0F))
    {
      paramArrayOfFloat[0] /= paramArrayOfFloat[3];
      paramArrayOfFloat[1] /= paramArrayOfFloat[3];
      paramArrayOfFloat[2] /= paramArrayOfFloat[3];
    }
    paramArrayOfFloat[3] = 1.0F;
    float f = mag(paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2]);
    if ((f != 0.0F) && (f != 1.0F))
    {
      paramArrayOfFloat[0] /= f;
      paramArrayOfFloat[1] /= f;
      paramArrayOfFloat[2] /= f;
    }
  }
  
  private void calcLightingContribution(int paramInt, float[] paramArrayOfFloat)
  {
    calcLightingContribution(paramInt, paramArrayOfFloat, false);
  }
  
  private void calcLightingContribution(int paramInt, float[] paramArrayOfFloat, boolean paramBoolean)
  {
    float[] arrayOfFloat = vertices[paramInt];
    float f1 = arrayOfFloat[28];
    float f2 = arrayOfFloat[29];
    float f3 = arrayOfFloat[30];
    float f4 = arrayOfFloat[21];
    float f5 = arrayOfFloat[22];
    float f6 = arrayOfFloat[23];
    float f7 = arrayOfFloat[31];
    float f8 = arrayOfFloat[9];
    float f9 = arrayOfFloat[10];
    float f10 = arrayOfFloat[11];
    if (!paramBoolean)
    {
      toWorldNormal(arrayOfFloat[9], arrayOfFloat[10], arrayOfFloat[11], worldNormal);
      f8 = worldNormal[0];
      f9 = worldNormal[1];
      f10 = worldNormal[2];
    }
    else
    {
      f8 = arrayOfFloat[9];
      f9 = arrayOfFloat[10];
      f10 = arrayOfFloat[11];
    }
    float f11 = dot(f8, f9, f10, -f4, -f5, -f6);
    if (f11 < 0.0F)
    {
      f8 = -f8;
      f9 = -f9;
      f10 = -f10;
    }
    paramArrayOfFloat[0] = 0.0F;
    paramArrayOfFloat[1] = 0.0F;
    paramArrayOfFloat[2] = 0.0F;
    paramArrayOfFloat[3] = 0.0F;
    paramArrayOfFloat[4] = 0.0F;
    paramArrayOfFloat[5] = 0.0F;
    paramArrayOfFloat[6] = 0.0F;
    paramArrayOfFloat[7] = 0.0F;
    paramArrayOfFloat[8] = 0.0F;
    for (int i = 0; i < lightCount; i++)
    {
      float f12 = lightFalloffConstant[i];
      float f13 = 1.0F;
      float f14;
      if (lightType[i] == 0)
      {
        if ((lightFalloffQuadratic[i] != 0.0F) || (lightFalloffLinear[i] != 0.0F))
        {
          f14 = mag(lightPosition[i].x - f4, lightPosition[i].y - f5, lightPosition[i].z - f6);
          f12 += lightFalloffQuadratic[i] * f14 + lightFalloffLinear[i] * sqrt(f14);
        }
        if (f12 == 0.0F) {
          f12 = 1.0F;
        }
        paramArrayOfFloat[0] += lightDiffuse[i][0] / f12;
        paramArrayOfFloat[1] += lightDiffuse[i][1] / f12;
        paramArrayOfFloat[2] += lightDiffuse[i][2] / f12;
      }
      else
      {
        float f17 = 0.0F;
        float f18 = 0.0F;
        float f15;
        float f16;
        if (lightType[i] == 1)
        {
          f14 = -lightNormal[i].x;
          f15 = -lightNormal[i].y;
          f16 = -lightNormal[i].z;
          f12 = 1.0F;
          f18 = f8 * f14 + f9 * f15 + f10 * f16;
          if (f18 <= 0.0F) {
            continue;
          }
        }
        else
        {
          f14 = lightPosition[i].x - f4;
          f15 = lightPosition[i].y - f5;
          f16 = lightPosition[i].z - f6;
          f19 = mag(f14, f15, f16);
          if (f19 != 0.0F)
          {
            f14 /= f19;
            f15 /= f19;
            f16 /= f19;
          }
          f18 = f8 * f14 + f9 * f15 + f10 * f16;
          if (f18 <= 0.0F) {
            continue;
          }
          if (lightType[i] == 3)
          {
            f17 = -(lightNormal[i].x * f14 + lightNormal[i].y * f15 + lightNormal[i].z * f16);
            if (f17 <= lightSpotAngleCos[i]) {
              continue;
            }
            f13 = (float)Math.pow(f17, lightSpotConcentration[i]);
          }
          if ((lightFalloffQuadratic[i] != 0.0F) || (lightFalloffLinear[i] != 0.0F)) {
            f12 += lightFalloffQuadratic[i] * f19 + lightFalloffLinear[i] * sqrt(f19);
          }
        }
        if (f12 == 0.0F) {
          f12 = 1.0F;
        }
        float f19 = f18 * f13 / f12;
        paramArrayOfFloat[3] += lightDiffuse[i][0] * f19;
        paramArrayOfFloat[4] += lightDiffuse[i][1] * f19;
        paramArrayOfFloat[5] += lightDiffuse[i][2] * f19;
        if (((f1 > 0.0F) || (f2 > 0.0F) || (f3 > 0.0F)) && ((lightSpecular[i][0] > 0.0F) || (lightSpecular[i][1] > 0.0F) || (lightSpecular[i][2] > 0.0F)))
        {
          float f20 = mag(f4, f5, f6);
          if (f20 != 0.0F)
          {
            f4 /= f20;
            f5 /= f20;
            f6 /= f20;
          }
          float f21 = f14 - f4;
          float f22 = f15 - f5;
          float f23 = f16 - f6;
          f20 = mag(f21, f22, f23);
          if (f20 != 0.0F)
          {
            f21 /= f20;
            f22 /= f20;
            f23 /= f20;
          }
          float f24 = f21 * f8 + f22 * f9 + f23 * f10;
          if (f24 > 0.0F)
          {
            f24 = (float)Math.pow(f24, f7);
            f19 = f24 * f13 / f12;
            paramArrayOfFloat[6] += lightSpecular[i][0] * f19;
            paramArrayOfFloat[7] += lightSpecular[i][1] * f19;
            paramArrayOfFloat[8] += lightSpecular[i][2] * f19;
          }
        }
      }
    }
  }
  
  private void applyLightingContribution(int paramInt, float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat = vertices[paramInt];
    arrayOfFloat[3] = clamp(arrayOfFloat[32] + arrayOfFloat[25] * paramArrayOfFloat[0] + arrayOfFloat[3] * paramArrayOfFloat[3]);
    arrayOfFloat[4] = clamp(arrayOfFloat[33] + arrayOfFloat[26] * paramArrayOfFloat[1] + arrayOfFloat[4] * paramArrayOfFloat[4]);
    arrayOfFloat[5] = clamp(arrayOfFloat[34] + arrayOfFloat[27] * paramArrayOfFloat[2] + arrayOfFloat[5] * paramArrayOfFloat[5]);
    arrayOfFloat[6] = clamp(arrayOfFloat[6]);
    arrayOfFloat[28] = clamp(arrayOfFloat[28] * paramArrayOfFloat[6]);
    arrayOfFloat[29] = clamp(arrayOfFloat[29] * paramArrayOfFloat[7]);
    arrayOfFloat[30] = clamp(arrayOfFloat[30] * paramArrayOfFloat[8]);
    arrayOfFloat[35] = 1.0F;
  }
  
  private void lightVertex(int paramInt, float[] paramArrayOfFloat)
  {
    calcLightingContribution(paramInt, paramArrayOfFloat);
    applyLightingContribution(paramInt, paramArrayOfFloat);
  }
  
  private void lightUnlitVertex(int paramInt, float[] paramArrayOfFloat)
  {
    if (vertices[paramInt][35] == 0.0F) {
      lightVertex(paramInt, paramArrayOfFloat);
    }
  }
  
  private void copyPrelitVertexColor(int paramInt1, int paramInt2, int paramInt3)
  {
    float[] arrayOfFloat1 = triangleColors[paramInt1][paramInt3];
    float[] arrayOfFloat2 = vertices[paramInt2];
    arrayOfFloat1[0] = arrayOfFloat2[3];
    arrayOfFloat1[1] = arrayOfFloat2[4];
    arrayOfFloat1[2] = arrayOfFloat2[5];
    arrayOfFloat1[3] = arrayOfFloat2[6];
    arrayOfFloat1[4] = arrayOfFloat2[28];
    arrayOfFloat1[5] = arrayOfFloat2[29];
    arrayOfFloat1[6] = arrayOfFloat2[30];
  }
  
  private void copyVertexColor(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat1 = triangleColors[paramInt1][paramInt3];
    float[] arrayOfFloat2 = vertices[paramInt2];
    arrayOfFloat1[0] = clamp(arrayOfFloat2[32] + arrayOfFloat2[25] * paramArrayOfFloat[0] + arrayOfFloat2[3] * paramArrayOfFloat[3]);
    arrayOfFloat1[1] = clamp(arrayOfFloat2[33] + arrayOfFloat2[26] * paramArrayOfFloat[1] + arrayOfFloat2[4] * paramArrayOfFloat[4]);
    arrayOfFloat1[2] = clamp(arrayOfFloat2[34] + arrayOfFloat2[27] * paramArrayOfFloat[2] + arrayOfFloat2[5] * paramArrayOfFloat[5]);
    arrayOfFloat1[3] = clamp(arrayOfFloat2[6]);
    arrayOfFloat1[4] = clamp(arrayOfFloat2[28] * paramArrayOfFloat[6]);
    arrayOfFloat1[5] = clamp(arrayOfFloat2[29] * paramArrayOfFloat[7]);
    arrayOfFloat1[6] = clamp(arrayOfFloat2[30] * paramArrayOfFloat[8]);
  }
  
  private void lightTriangle(int paramInt, float[] paramArrayOfFloat)
  {
    int i = triangles[paramInt][0];
    copyVertexColor(paramInt, i, 0, paramArrayOfFloat);
    i = triangles[paramInt][1];
    copyVertexColor(paramInt, i, 1, paramArrayOfFloat);
    i = triangles[paramInt][2];
    copyVertexColor(paramInt, i, 2, paramArrayOfFloat);
  }
  
  private void lightTriangle(int paramInt)
  {
    int i;
    if (normalMode == 2)
    {
      i = triangles[paramInt][0];
      lightUnlitVertex(i, tempLightingContribution);
      copyPrelitVertexColor(paramInt, i, 0);
      i = triangles[paramInt][1];
      lightUnlitVertex(i, tempLightingContribution);
      copyPrelitVertexColor(paramInt, i, 1);
      i = triangles[paramInt][2];
      lightUnlitVertex(i, tempLightingContribution);
      copyPrelitVertexColor(paramInt, i, 2);
    }
    else
    {
      int j;
      int k;
      if (!lightingDependsOnVertexPosition)
      {
        i = triangles[paramInt][0];
        j = triangles[paramInt][1];
        k = triangles[paramInt][2];
        cross(vertices[j][21] - vertices[i][21], vertices[j][22] - vertices[i][22], vertices[j][23] - vertices[i][23], vertices[k][21] - vertices[i][21], vertices[k][22] - vertices[i][22], vertices[k][23] - vertices[i][23], lightTriangleNorm);
        lightTriangleNorm.normalize();
        vertices[i][9] = lightTriangleNorm.x;
        vertices[i][10] = lightTriangleNorm.y;
        vertices[i][11] = lightTriangleNorm.z;
        calcLightingContribution(i, tempLightingContribution, true);
        copyVertexColor(paramInt, i, 0, tempLightingContribution);
        copyVertexColor(paramInt, j, 1, tempLightingContribution);
        copyVertexColor(paramInt, k, 2, tempLightingContribution);
      }
      else if (normalMode == 1)
      {
        i = triangles[paramInt][0];
        vertices[i][9] = vertices[shapeFirst][9];
        vertices[i][10] = vertices[shapeFirst][10];
        vertices[i][11] = vertices[shapeFirst][11];
        calcLightingContribution(i, tempLightingContribution);
        copyVertexColor(paramInt, i, 0, tempLightingContribution);
        i = triangles[paramInt][1];
        vertices[i][9] = vertices[shapeFirst][9];
        vertices[i][10] = vertices[shapeFirst][10];
        vertices[i][11] = vertices[shapeFirst][11];
        calcLightingContribution(i, tempLightingContribution);
        copyVertexColor(paramInt, i, 1, tempLightingContribution);
        i = triangles[paramInt][2];
        vertices[i][9] = vertices[shapeFirst][9];
        vertices[i][10] = vertices[shapeFirst][10];
        vertices[i][11] = vertices[shapeFirst][11];
        calcLightingContribution(i, tempLightingContribution);
        copyVertexColor(paramInt, i, 2, tempLightingContribution);
      }
      else
      {
        i = triangles[paramInt][0];
        j = triangles[paramInt][1];
        k = triangles[paramInt][2];
        cross(vertices[j][21] - vertices[i][21], vertices[j][22] - vertices[i][22], vertices[j][23] - vertices[i][23], vertices[k][21] - vertices[i][21], vertices[k][22] - vertices[i][22], vertices[k][23] - vertices[i][23], lightTriangleNorm);
        lightTriangleNorm.normalize();
        vertices[i][9] = lightTriangleNorm.x;
        vertices[i][10] = lightTriangleNorm.y;
        vertices[i][11] = lightTriangleNorm.z;
        calcLightingContribution(i, tempLightingContribution, true);
        copyVertexColor(paramInt, i, 0, tempLightingContribution);
        vertices[j][9] = lightTriangleNorm.x;
        vertices[j][10] = lightTriangleNorm.y;
        vertices[j][11] = lightTriangleNorm.z;
        calcLightingContribution(j, tempLightingContribution, true);
        copyVertexColor(paramInt, j, 1, tempLightingContribution);
        vertices[k][9] = lightTriangleNorm.x;
        vertices[k][10] = lightTriangleNorm.y;
        vertices[k][11] = lightTriangleNorm.z;
        calcLightingContribution(k, tempLightingContribution, true);
        copyVertexColor(paramInt, k, 2, tempLightingContribution);
      }
    }
  }
  
  protected void renderTriangles(int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat1 = vertices[triangles[i][0]];
      float[] arrayOfFloat2 = vertices[triangles[i][1]];
      float[] arrayOfFloat3 = vertices[triangles[i][2]];
      int j = triangles[i][3];
      triangle.reset();
      float f1 = clamp(triangleColors[i][0][0] + triangleColors[i][0][4]);
      float f2 = clamp(triangleColors[i][0][1] + triangleColors[i][0][5]);
      float f3 = clamp(triangleColors[i][0][2] + triangleColors[i][0][6]);
      float f4 = clamp(triangleColors[i][1][0] + triangleColors[i][1][4]);
      float f5 = clamp(triangleColors[i][1][1] + triangleColors[i][1][5]);
      float f6 = clamp(triangleColors[i][1][2] + triangleColors[i][1][6]);
      float f7 = clamp(triangleColors[i][2][0] + triangleColors[i][2][4]);
      float f8 = clamp(triangleColors[i][2][1] + triangleColors[i][2][5]);
      float f9 = clamp(triangleColors[i][2][2] + triangleColors[i][2][6]);
      int k = 0;
      if ((s_enableAccurateTextures) && (frustumMode))
      {
        int m = 1;
        smoothTriangle.reset(3);
        smoothTriangle.smooth = true;
        smoothTriangle.interpARGB = true;
        smoothTriangle.setIntensities(f1, f2, f3, arrayOfFloat1[6], f4, f5, f6, arrayOfFloat2[6], f7, f8, f9, arrayOfFloat3[6]);
        if ((j > -1) && (textures[j] != null))
        {
          smoothTriangle.setCamVertices(arrayOfFloat1[21], arrayOfFloat1[22], arrayOfFloat1[23], arrayOfFloat2[21], arrayOfFloat2[22], arrayOfFloat2[23], arrayOfFloat3[21], arrayOfFloat3[22], arrayOfFloat3[23]);
          smoothTriangle.interpUV = true;
          smoothTriangle.texture(textures[j]);
          float f10 = textures[j].width;
          float f11 = textures[j].height;
          smoothTriangle.vertices[0][7] = (arrayOfFloat1[7] * f10);
          smoothTriangle.vertices[0][8] = (arrayOfFloat1[8] * f11);
          smoothTriangle.vertices[1][7] = (arrayOfFloat2[7] * f10);
          smoothTriangle.vertices[1][8] = (arrayOfFloat2[8] * f11);
          smoothTriangle.vertices[2][7] = (arrayOfFloat3[7] * f10);
          smoothTriangle.vertices[2][8] = (arrayOfFloat3[8] * f11);
        }
        else
        {
          smoothTriangle.interpUV = false;
          m = 0;
        }
        smoothTriangle.setVertices(arrayOfFloat1[18], arrayOfFloat1[19], arrayOfFloat1[20], arrayOfFloat2[18], arrayOfFloat2[19], arrayOfFloat2[20], arrayOfFloat3[18], arrayOfFloat3[19], arrayOfFloat3[20]);
        if ((m == 0) || (smoothTriangle.precomputeAccurateTexturing())) {
          smoothTriangle.render();
        } else {
          k = 1;
        }
      }
      if ((!s_enableAccurateTextures) || (k != 0) || (!frustumMode))
      {
        if ((j > -1) && (textures[j] != null))
        {
          triangle.setTexture(textures[j]);
          triangle.setUV(arrayOfFloat1[7], arrayOfFloat1[8], arrayOfFloat2[7], arrayOfFloat2[8], arrayOfFloat3[7], arrayOfFloat3[8]);
        }
        triangle.setIntensities(f1, f2, f3, arrayOfFloat1[6], f4, f5, f6, arrayOfFloat2[6], f7, f8, f9, arrayOfFloat3[6]);
        triangle.setVertices(arrayOfFloat1[18], arrayOfFloat1[19], arrayOfFloat1[20], arrayOfFloat2[18], arrayOfFloat2[19], arrayOfFloat2[20], arrayOfFloat3[18], arrayOfFloat3[19], arrayOfFloat3[20]);
        triangle.render();
      }
    }
  }
  
  protected void rawTriangles(int paramInt1, int paramInt2)
  {
    raw.colorMode(1, 1.0F);
    raw.noStroke();
    raw.beginShape(9);
    for (int i = paramInt1; i < paramInt2; i++)
    {
      float[] arrayOfFloat1 = vertices[triangles[i][0]];
      float[] arrayOfFloat2 = vertices[triangles[i][1]];
      float[] arrayOfFloat3 = vertices[triangles[i][2]];
      float f1 = clamp(triangleColors[i][0][0] + triangleColors[i][0][4]);
      float f2 = clamp(triangleColors[i][0][1] + triangleColors[i][0][5]);
      float f3 = clamp(triangleColors[i][0][2] + triangleColors[i][0][6]);
      float f4 = clamp(triangleColors[i][1][0] + triangleColors[i][1][4]);
      float f5 = clamp(triangleColors[i][1][1] + triangleColors[i][1][5]);
      float f6 = clamp(triangleColors[i][1][2] + triangleColors[i][1][6]);
      float f7 = clamp(triangleColors[i][2][0] + triangleColors[i][2][4]);
      float f8 = clamp(triangleColors[i][2][1] + triangleColors[i][2][5]);
      float f9 = clamp(triangleColors[i][2][2] + triangleColors[i][2][6]);
      int j = triangles[i][3];
      Object localObject = j > -1 ? textures[j] : null;
      if (localObject != null)
      {
        if (raw.is3D())
        {
          if ((arrayOfFloat1[24] != 0.0F) && (arrayOfFloat2[24] != 0.0F) && (arrayOfFloat3[24] != 0.0F))
          {
            raw.fill(f1, f2, f3, arrayOfFloat1[6]);
            raw.vertex(arrayOfFloat1[21] / arrayOfFloat1[24], arrayOfFloat1[22] / arrayOfFloat1[24], arrayOfFloat1[23] / arrayOfFloat1[24], arrayOfFloat1[7], arrayOfFloat1[8]);
            raw.fill(f4, f5, f6, arrayOfFloat2[6]);
            raw.vertex(arrayOfFloat2[21] / arrayOfFloat2[24], arrayOfFloat2[22] / arrayOfFloat2[24], arrayOfFloat2[23] / arrayOfFloat2[24], arrayOfFloat2[7], arrayOfFloat2[8]);
            raw.fill(f7, f8, f9, arrayOfFloat3[6]);
            raw.vertex(arrayOfFloat3[21] / arrayOfFloat3[24], arrayOfFloat3[22] / arrayOfFloat3[24], arrayOfFloat3[23] / arrayOfFloat3[24], arrayOfFloat3[7], arrayOfFloat3[8]);
          }
        }
        else if (raw.is2D())
        {
          raw.fill(f1, f2, f3, arrayOfFloat1[6]);
          raw.vertex(arrayOfFloat1[18], arrayOfFloat1[19], arrayOfFloat1[7], arrayOfFloat1[8]);
          raw.fill(f4, f5, f6, arrayOfFloat2[6]);
          raw.vertex(arrayOfFloat2[18], arrayOfFloat2[19], arrayOfFloat2[7], arrayOfFloat2[8]);
          raw.fill(f7, f8, f9, arrayOfFloat3[6]);
          raw.vertex(arrayOfFloat3[18], arrayOfFloat3[19], arrayOfFloat3[7], arrayOfFloat3[8]);
        }
      }
      else if (raw.is3D())
      {
        if ((arrayOfFloat1[24] != 0.0F) && (arrayOfFloat2[24] != 0.0F) && (arrayOfFloat3[24] != 0.0F))
        {
          raw.fill(f1, f2, f3, arrayOfFloat1[6]);
          raw.vertex(arrayOfFloat1[21] / arrayOfFloat1[24], arrayOfFloat1[22] / arrayOfFloat1[24], arrayOfFloat1[23] / arrayOfFloat1[24]);
          raw.fill(f4, f5, f6, arrayOfFloat2[6]);
          raw.vertex(arrayOfFloat2[21] / arrayOfFloat2[24], arrayOfFloat2[22] / arrayOfFloat2[24], arrayOfFloat2[23] / arrayOfFloat2[24]);
          raw.fill(f7, f8, f9, arrayOfFloat3[6]);
          raw.vertex(arrayOfFloat3[21] / arrayOfFloat3[24], arrayOfFloat3[22] / arrayOfFloat3[24], arrayOfFloat3[23] / arrayOfFloat3[24]);
        }
      }
      else if (raw.is2D())
      {
        raw.fill(f1, f2, f3, arrayOfFloat1[6]);
        raw.vertex(arrayOfFloat1[18], arrayOfFloat1[19]);
        raw.fill(f4, f5, f6, arrayOfFloat2[6]);
        raw.vertex(arrayOfFloat2[18], arrayOfFloat2[19]);
        raw.fill(f7, f8, f9, arrayOfFloat3[6]);
        raw.vertex(arrayOfFloat3[18], arrayOfFloat3[19]);
      }
    }
    raw.endShape();
  }
  
  public void flush()
  {
    if (hints[5] != 0) {
      sort();
    }
    render();
  }
  
  protected void render()
  {
    if (pointCount > 0)
    {
      renderPoints(0, pointCount);
      if (raw != null) {
        rawPoints(0, pointCount);
      }
      pointCount = 0;
    }
    if (lineCount > 0)
    {
      renderLines(0, lineCount);
      if (raw != null) {
        rawLines(0, lineCount);
      }
      lineCount = 0;
      pathCount = 0;
    }
    if (triangleCount > 0)
    {
      renderTriangles(0, triangleCount);
      if (raw != null) {
        rawTriangles(0, triangleCount);
      }
      triangleCount = 0;
    }
  }
  
  protected void sort()
  {
    if (triangleCount > 0) {
      sortTrianglesInternal(0, triangleCount - 1);
    }
  }
  
  private void sortTrianglesInternal(int paramInt1, int paramInt2)
  {
    int i = (paramInt1 + paramInt2) / 2;
    sortTrianglesSwap(i, paramInt2);
    int j = sortTrianglesPartition(paramInt1 - 1, paramInt2);
    sortTrianglesSwap(j, paramInt2);
    if (j - paramInt1 > 1) {
      sortTrianglesInternal(paramInt1, j - 1);
    }
    if (paramInt2 - j > 1) {
      sortTrianglesInternal(j + 1, paramInt2);
    }
  }
  
  private int sortTrianglesPartition(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    do
    {
      while (sortTrianglesCompare(++paramInt1, i) < 0.0F) {}
      while ((paramInt2 != 0) && (sortTrianglesCompare(--paramInt2, i) > 0.0F)) {}
      sortTrianglesSwap(paramInt1, paramInt2);
    } while (paramInt1 < paramInt2);
    sortTrianglesSwap(paramInt1, paramInt2);
    return paramInt1;
  }
  
  private void sortTrianglesSwap(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = triangles[paramInt1];
    triangles[paramInt1] = triangles[paramInt2];
    triangles[paramInt2] = arrayOfInt;
    float[][] arrayOfFloat = triangleColors[paramInt1];
    triangleColors[paramInt1] = triangleColors[paramInt2];
    triangleColors[paramInt2] = arrayOfFloat;
  }
  
  private float sortTrianglesCompare(int paramInt1, int paramInt2)
  {
    return vertices[triangles[paramInt2][0]][20] + vertices[triangles[paramInt2][1]][20] + vertices[triangles[paramInt2][2]][20] - (vertices[triangles[paramInt1][0]][20] + vertices[triangles[paramInt1][1]][20] + vertices[triangles[paramInt1][2]][20]);
  }
  
  protected void ellipseImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = paramFloat3 / 2.0F;
    float f2 = paramFloat4 / 2.0F;
    float f3 = paramFloat1 + f1;
    float f4 = paramFloat2 + f2;
    int i = (int)(4.0D + Math.sqrt(paramFloat3 + paramFloat4) * 3.0D);
    int j = PApplet.constrain(i, 6, 100);
    float f5;
    float f6;
    boolean bool;
    int k;
    if (fill)
    {
      f5 = 720.0F / j;
      f6 = 0.0F;
      bool = stroke;
      stroke = false;
      k = smooth;
      if ((smooth) && (stroke)) {
        smooth = false;
      }
      beginShape(11);
      normal(0.0F, 0.0F, 1.0F);
      vertex(f3, f4);
      for (int m = 0; m < j; m++)
      {
        vertex(f3 + cosLUT[((int)f6)] * f1, f4 + sinLUT[((int)f6)] * f2);
        f6 = (f6 + f5) % 720.0F;
      }
      vertex(f3 + cosLUT[0] * f1, f4 + sinLUT[0] * f2);
      endShape();
      stroke = bool;
      smooth = k;
    }
    if (stroke)
    {
      f5 = 720.0F / j;
      f6 = 0.0F;
      bool = fill;
      fill = false;
      f6 = 0.0F;
      beginShape();
      for (k = 0; k < j; k++)
      {
        vertex(f3 + cosLUT[((int)f6)] * f1, f4 + sinLUT[((int)f6)] * f2);
        f6 = (f6 + f5) % 720.0F;
      }
      endShape(2);
      fill = bool;
    }
  }
  
  protected void arcImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = paramFloat3 / 2.0F;
    float f2 = paramFloat4 / 2.0F;
    float f3 = paramFloat1 + f1;
    float f4 = paramFloat2 + f2;
    boolean bool;
    int i;
    int j;
    int k;
    int m;
    int n;
    if (fill)
    {
      bool = stroke;
      stroke = false;
      i = (int)(0.5F + paramFloat5 / 6.2831855F * 720.0F);
      j = (int)(0.5F + paramFloat6 / 6.2831855F * 720.0F);
      beginShape(11);
      vertex(f3, f4);
      k = 1;
      m = i;
      while (m < j)
      {
        n = m % 720;
        if (n < 0) {
          n += 720;
        }
        vertex(f3 + cosLUT[n] * f1, f4 + sinLUT[n] * f2);
        m += k;
      }
      vertex(f3 + cosLUT[(j % 720)] * f1, f4 + sinLUT[(j % 720)] * f2);
      endShape();
      stroke = bool;
    }
    if (stroke)
    {
      bool = fill;
      fill = false;
      i = (int)(0.5F + paramFloat5 / 6.2831855F * 720.0F);
      j = (int)(0.5F + paramFloat6 / 6.2831855F * 720.0F);
      beginShape();
      k = 1;
      m = i;
      while (m < j)
      {
        n = m % 720;
        if (n < 0) {
          n += 720;
        }
        vertex(f3 + cosLUT[n] * f1, f4 + sinLUT[n] * f2);
        m += k;
      }
      vertex(f3 + cosLUT[(j % 720)] * f1, f4 + sinLUT[(j % 720)] * f2);
      endShape();
      fill = bool;
    }
  }
  
  public void box(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (triangle != null) {
      triangle.setCulling(true);
    }
    super.box(paramFloat1, paramFloat2, paramFloat3);
    if (triangle != null) {
      triangle.setCulling(false);
    }
  }
  
  public void sphere(float paramFloat)
  {
    if (triangle != null) {
      triangle.setCulling(true);
    }
    super.sphere(paramFloat);
    if (triangle != null) {
      triangle.setCulling(false);
    }
  }
  
  public void smooth()
  {
    s_enableAccurateTextures = true;
    smooth = true;
  }
  
  public void noSmooth()
  {
    s_enableAccurateTextures = false;
    smooth = false;
  }
  
  protected boolean textModeCheck(int paramInt)
  {
    return (textMode == 4) || (textMode == 256);
  }
  
  public void pushMatrix()
  {
    if (matrixMode == 0)
    {
      if (pmatrixStackDepth == 32) {
        throw new RuntimeException("Too many calls to pushMatrix().");
      }
      projection.get(pmatrixStack[pmatrixStackDepth]);
      pmatrixStackDepth += 1;
    }
    else
    {
      if (matrixStackDepth == 32) {
        throw new RuntimeException("Too many calls to pushMatrix().");
      }
      modelview.get(matrixStack[matrixStackDepth]);
      modelviewInv.get(matrixInvStack[matrixStackDepth]);
      matrixStackDepth += 1;
    }
  }
  
  public void popMatrix()
  {
    if (matrixMode == 0)
    {
      if (pmatrixStackDepth == 0) {
        throw new RuntimeException("Too many calls to popMatrix(), and not enough to pushMatrix().");
      }
      pmatrixStackDepth -= 1;
      projection.set(pmatrixStack[pmatrixStackDepth]);
    }
    else
    {
      if (matrixStackDepth == 0) {
        throw new RuntimeException("Too many calls to popMatrix(), and not enough to pushMatrix().");
      }
      matrixStackDepth -= 1;
      modelview.set(matrixStack[matrixStackDepth]);
      modelviewInv.set(matrixInvStack[matrixStackDepth]);
    }
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    translate(paramFloat1, paramFloat2, 0.0F);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (matrixMode == 0)
    {
      projection.translate(paramFloat1, paramFloat2, paramFloat3);
    }
    else
    {
      forwardTransform.translate(paramFloat1, paramFloat2, paramFloat3);
      reverseTransform.invTranslate(paramFloat1, paramFloat2, paramFloat3);
    }
  }
  
  public void rotate(float paramFloat)
  {
    rotateZ(paramFloat);
  }
  
  public void rotateX(float paramFloat)
  {
    if (matrixMode == 0)
    {
      projection.rotateX(paramFloat);
    }
    else
    {
      forwardTransform.rotateX(paramFloat);
      reverseTransform.invRotateX(paramFloat);
    }
  }
  
  public void rotateY(float paramFloat)
  {
    if (matrixMode == 0)
    {
      projection.rotateY(paramFloat);
    }
    else
    {
      forwardTransform.rotateY(paramFloat);
      reverseTransform.invRotateY(paramFloat);
    }
  }
  
  public void rotateZ(float paramFloat)
  {
    if (matrixMode == 0)
    {
      projection.rotateZ(paramFloat);
    }
    else
    {
      forwardTransform.rotateZ(paramFloat);
      reverseTransform.invRotateZ(paramFloat);
    }
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (matrixMode == 0)
    {
      projection.rotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    else
    {
      forwardTransform.rotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      reverseTransform.invRotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
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
    if (matrixMode == 0)
    {
      projection.scale(paramFloat1, paramFloat2, paramFloat3);
    }
    else
    {
      forwardTransform.scale(paramFloat1, paramFloat2, paramFloat3);
      reverseTransform.invScale(paramFloat1, paramFloat2, paramFloat3);
    }
  }
  
  public void shearX(float paramFloat)
  {
    float f = (float)Math.tan(paramFloat);
    applyMatrix(1.0F, f, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void shearY(float paramFloat)
  {
    float f = (float)Math.tan(paramFloat);
    applyMatrix(1.0F, 0.0F, 0.0F, 0.0F, f, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void resetMatrix()
  {
    if (matrixMode == 0)
    {
      projection.reset();
    }
    else
    {
      forwardTransform.reset();
      reverseTransform.reset();
    }
  }
  
  public void applyMatrix(PMatrix2D paramPMatrix2D)
  {
    applyMatrix(m00, m01, m02, m10, m11, m12);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    applyMatrix(paramFloat1, paramFloat2, paramFloat3, 0.0F, paramFloat4, paramFloat5, paramFloat6, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void applyMatrix(PMatrix3D paramPMatrix3D)
  {
    applyMatrix(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    if (matrixMode == 0)
    {
      projection.apply(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
    }
    else
    {
      forwardTransform.apply(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
      reverseTransform.invApply(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
    }
  }
  
  public PMatrix getMatrix()
  {
    if (matrixMode == 0) {
      return projection.get();
    }
    return modelview.get();
  }
  
  public PMatrix3D getMatrix(PMatrix3D paramPMatrix3D)
  {
    if (paramPMatrix3D == null) {
      paramPMatrix3D = new PMatrix3D();
    }
    if (matrixMode == 0) {
      paramPMatrix3D.set(projection);
    } else {
      paramPMatrix3D.set(modelview);
    }
    return paramPMatrix3D;
  }
  
  public void setMatrix(PMatrix2D paramPMatrix2D)
  {
    resetMatrix();
    applyMatrix(paramPMatrix2D);
  }
  
  public void setMatrix(PMatrix3D paramPMatrix3D)
  {
    resetMatrix();
    applyMatrix(paramPMatrix3D);
  }
  
  public void printMatrix()
  {
    if (matrixMode == 0) {
      projection.print();
    } else {
      modelview.print();
    }
  }
  
  public void beginCamera()
  {
    if (manipulatingCamera) {
      throw new RuntimeException("beginCamera() cannot be called again before endCamera()");
    }
    manipulatingCamera = true;
    forwardTransform = cameraInv;
    reverseTransform = camera;
  }
  
  public void endCamera()
  {
    if (!manipulatingCamera) {
      throw new RuntimeException("Cannot call endCamera() without first calling beginCamera()");
    }
    modelview.set(camera);
    modelviewInv.set(cameraInv);
    forwardTransform = modelview;
    reverseTransform = modelviewInv;
    manipulatingCamera = false;
  }
  
  public void camera()
  {
    camera(cameraX, cameraY, cameraZ, cameraX, cameraY, 0.0F, 0.0F, 1.0F, 0.0F);
  }
  
  public void camera(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    float f1 = paramFloat1 - paramFloat4;
    float f2 = paramFloat2 - paramFloat5;
    float f3 = paramFloat3 - paramFloat6;
    float f4 = sqrt(f1 * f1 + f2 * f2 + f3 * f3);
    if (f4 != 0.0F)
    {
      f1 /= f4;
      f2 /= f4;
      f3 /= f4;
    }
    float f5 = paramFloat7;
    float f6 = paramFloat8;
    float f7 = paramFloat9;
    float f8 = f6 * f3 - f7 * f2;
    float f9 = -f5 * f3 + f7 * f1;
    float f10 = f5 * f2 - f6 * f1;
    f5 = f2 * f10 - f3 * f9;
    f6 = -f1 * f10 + f3 * f8;
    f7 = f1 * f9 - f2 * f8;
    f4 = sqrt(f8 * f8 + f9 * f9 + f10 * f10);
    if (f4 != 0.0F)
    {
      f8 /= f4;
      f9 /= f4;
      f10 /= f4;
    }
    f4 = sqrt(f5 * f5 + f6 * f6 + f7 * f7);
    if (f4 != 0.0F)
    {
      f5 /= f4;
      f6 /= f4;
      f7 /= f4;
    }
    camera.set(f8, f9, f10, 0.0F, f5, f6, f7, 0.0F, f1, f2, f3, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    camera.translate(-paramFloat1, -paramFloat2, -paramFloat3);
    cameraInv.reset();
    cameraInv.invApply(f8, f9, f10, 0.0F, f5, f6, f7, 0.0F, f1, f2, f3, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    cameraInv.translate(paramFloat1, paramFloat2, paramFloat3);
    modelview.set(camera);
    modelviewInv.set(cameraInv);
  }
  
  public void printCamera()
  {
    camera.print();
  }
  
  public void ortho()
  {
    ortho(0.0F, width, 0.0F, height, -10.0F, 10.0F);
  }
  
  public void ortho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = 2.0F / (paramFloat2 - paramFloat1);
    float f2 = 2.0F / (paramFloat4 - paramFloat3);
    float f3 = -2.0F / (paramFloat6 - paramFloat5);
    float f4 = -(paramFloat2 + paramFloat1) / (paramFloat2 - paramFloat1);
    float f5 = -(paramFloat4 + paramFloat3) / (paramFloat4 - paramFloat3);
    float f6 = -(paramFloat6 + paramFloat5) / (paramFloat6 - paramFloat5);
    projection.set(f1, 0.0F, 0.0F, f4, 0.0F, f2, 0.0F, f5, 0.0F, 0.0F, f3, f6, 0.0F, 0.0F, 0.0F, 1.0F);
    updateProjection();
    frustumMode = false;
  }
  
  public void perspective()
  {
    perspective(cameraFOV, cameraAspect, cameraNear, cameraFar);
  }
  
  public void perspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = paramFloat3 * (float)Math.tan(paramFloat1 / 2.0F);
    float f2 = -f1;
    float f3 = f2 * paramFloat2;
    float f4 = f1 * paramFloat2;
    frustum(f3, f4, f2, f1, paramFloat3, paramFloat4);
  }
  
  public void frustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    leftScreen = paramFloat1;
    rightScreen = paramFloat2;
    bottomScreen = paramFloat3;
    topScreen = paramFloat4;
    nearPlane = paramFloat5;
    frustumMode = true;
    projection.set(2.0F * paramFloat5 / (paramFloat2 - paramFloat1), 0.0F, (paramFloat2 + paramFloat1) / (paramFloat2 - paramFloat1), 0.0F, 0.0F, 2.0F * paramFloat5 / (paramFloat4 - paramFloat3), (paramFloat4 + paramFloat3) / (paramFloat4 - paramFloat3), 0.0F, 0.0F, 0.0F, -(paramFloat6 + paramFloat5) / (paramFloat6 - paramFloat5), -(2.0F * paramFloat6 * paramFloat5) / (paramFloat6 - paramFloat5), 0.0F, 0.0F, -1.0F, 0.0F);
    updateProjection();
  }
  
  protected void updateProjection() {}
  
  public void printProjection()
  {
    projection.print();
  }
  
  public PMatrix getProjection()
  {
    return projection.get();
  }
  
  public void matrixMode(int paramInt)
  {
    if (paramInt == 0) {
      matrixMode = 0;
    } else if (paramInt == 1) {
      matrixMode = 1;
    } else {
      showWarning("Invalid matrix mode. Use PROJECTION or MODELVIEW");
    }
  }
  
  public float screenX(float paramFloat1, float paramFloat2)
  {
    return screenX(paramFloat1, paramFloat2, 0.0F);
  }
  
  public float screenY(float paramFloat1, float paramFloat2)
  {
    return screenY(paramFloat1, paramFloat2, 0.0F);
  }
  
  public float screenX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = projection.m00 * f1 + projection.m01 * f2 + projection.m02 * f3 + projection.m03 * f4;
    float f6 = projection.m30 * f1 + projection.m31 * f2 + projection.m32 * f3 + projection.m33 * f4;
    if (f6 != 0.0F) {
      f5 /= f6;
    }
    return width * (1.0F + f5) / 2.0F;
  }
  
  public float screenY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = projection.m10 * f1 + projection.m11 * f2 + projection.m12 * f3 + projection.m13 * f4;
    float f6 = projection.m30 * f1 + projection.m31 * f2 + projection.m32 * f3 + projection.m33 * f4;
    if (f6 != 0.0F) {
      f5 /= f6;
    }
    return height * (1.0F + f5) / 2.0F;
  }
  
  public float screenZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = projection.m20 * f1 + projection.m21 * f2 + projection.m22 * f3 + projection.m23 * f4;
    float f6 = projection.m30 * f1 + projection.m31 * f2 + projection.m32 * f3 + projection.m33 * f4;
    if (f6 != 0.0F) {
      f5 /= f6;
    }
    return (f5 + 1.0F) / 2.0F;
  }
  
  public float modelX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = cameraInv.m00 * f1 + cameraInv.m01 * f2 + cameraInv.m02 * f3 + cameraInv.m03 * f4;
    float f6 = cameraInv.m30 * f1 + cameraInv.m31 * f2 + cameraInv.m32 * f3 + cameraInv.m33 * f4;
    return f6 != 0.0F ? f5 / f6 : f5;
  }
  
  public float modelY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = cameraInv.m10 * f1 + cameraInv.m11 * f2 + cameraInv.m12 * f3 + cameraInv.m13 * f4;
    float f6 = cameraInv.m30 * f1 + cameraInv.m31 * f2 + cameraInv.m32 * f3 + cameraInv.m33 * f4;
    return f6 != 0.0F ? f5 / f6 : f5;
  }
  
  public float modelZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = modelview.m00 * paramFloat1 + modelview.m01 * paramFloat2 + modelview.m02 * paramFloat3 + modelview.m03;
    float f2 = modelview.m10 * paramFloat1 + modelview.m11 * paramFloat2 + modelview.m12 * paramFloat3 + modelview.m13;
    float f3 = modelview.m20 * paramFloat1 + modelview.m21 * paramFloat2 + modelview.m22 * paramFloat3 + modelview.m23;
    float f4 = modelview.m30 * paramFloat1 + modelview.m31 * paramFloat2 + modelview.m32 * paramFloat3 + modelview.m33;
    float f5 = cameraInv.m20 * f1 + cameraInv.m21 * f2 + cameraInv.m22 * f3 + cameraInv.m23 * f4;
    float f6 = cameraInv.m30 * f1 + cameraInv.m31 * f2 + cameraInv.m32 * f3 + cameraInv.m33 * f4;
    return f6 != 0.0F ? f5 / f6 : f5;
  }
  
  public void strokeJoin(int paramInt)
  {
    if (paramInt != 8) {
      showMethodWarning("strokeJoin");
    }
  }
  
  public void strokeCap(int paramInt)
  {
    if (paramInt != 2) {
      showMethodWarning("strokeCap");
    }
  }
  
  protected void fillFromCalc()
  {
    super.fillFromCalc();
    ambientFromCalc();
  }
  
  public void lights()
  {
    int i = colorMode;
    colorMode = 1;
    lightFalloff(1.0F, 0.0F, 0.0F);
    lightSpecular(0.0F, 0.0F, 0.0F);
    ambientLight(colorModeX * 0.5F, colorModeY * 0.5F, colorModeZ * 0.5F);
    directionalLight(colorModeX * 0.5F, colorModeY * 0.5F, colorModeZ * 0.5F, 0.0F, 0.0F, -1.0F);
    colorMode = i;
    lightingDependsOnVertexPosition = false;
  }
  
  public void noLights()
  {
    flush();
    lightCount = 0;
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    ambientLight(paramFloat1, paramFloat2, paramFloat3, 0.0F, 0.0F, 0.0F);
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (lightCount == 8) {
      throw new RuntimeException("can only create 8 lights");
    }
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    lightDiffuse[lightCount][0] = calcR;
    lightDiffuse[lightCount][1] = calcG;
    lightDiffuse[lightCount][2] = calcB;
    lightType[lightCount] = 0;
    lightFalloffConstant[lightCount] = currentLightFalloffConstant;
    lightFalloffLinear[lightCount] = currentLightFalloffLinear;
    lightFalloffQuadratic[lightCount] = currentLightFalloffQuadratic;
    lightPosition(lightCount, paramFloat4, paramFloat5, paramFloat6);
    lightCount += 1;
  }
  
  public void directionalLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (lightCount == 8) {
      throw new RuntimeException("can only create 8 lights");
    }
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    lightDiffuse[lightCount][0] = calcR;
    lightDiffuse[lightCount][1] = calcG;
    lightDiffuse[lightCount][2] = calcB;
    lightType[lightCount] = 1;
    lightFalloffConstant[lightCount] = currentLightFalloffConstant;
    lightFalloffLinear[lightCount] = currentLightFalloffLinear;
    lightFalloffQuadratic[lightCount] = currentLightFalloffQuadratic;
    lightSpecular[lightCount][0] = currentLightSpecular[0];
    lightSpecular[lightCount][1] = currentLightSpecular[1];
    lightSpecular[lightCount][2] = currentLightSpecular[2];
    lightDirection(lightCount, paramFloat4, paramFloat5, paramFloat6);
    lightCount += 1;
  }
  
  public void pointLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (lightCount == 8) {
      throw new RuntimeException("can only create 8 lights");
    }
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    lightDiffuse[lightCount][0] = calcR;
    lightDiffuse[lightCount][1] = calcG;
    lightDiffuse[lightCount][2] = calcB;
    lightType[lightCount] = 2;
    lightFalloffConstant[lightCount] = currentLightFalloffConstant;
    lightFalloffLinear[lightCount] = currentLightFalloffLinear;
    lightFalloffQuadratic[lightCount] = currentLightFalloffQuadratic;
    lightSpecular[lightCount][0] = currentLightSpecular[0];
    lightSpecular[lightCount][1] = currentLightSpecular[1];
    lightSpecular[lightCount][2] = currentLightSpecular[2];
    lightPosition(lightCount, paramFloat4, paramFloat5, paramFloat6);
    lightCount += 1;
    lightingDependsOnVertexPosition = true;
  }
  
  public void spotLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11)
  {
    if (lightCount == 8) {
      throw new RuntimeException("can only create 8 lights");
    }
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    lightDiffuse[lightCount][0] = calcR;
    lightDiffuse[lightCount][1] = calcG;
    lightDiffuse[lightCount][2] = calcB;
    lightType[lightCount] = 3;
    lightFalloffConstant[lightCount] = currentLightFalloffConstant;
    lightFalloffLinear[lightCount] = currentLightFalloffLinear;
    lightFalloffQuadratic[lightCount] = currentLightFalloffQuadratic;
    lightSpecular[lightCount][0] = currentLightSpecular[0];
    lightSpecular[lightCount][1] = currentLightSpecular[1];
    lightSpecular[lightCount][2] = currentLightSpecular[2];
    lightPosition(lightCount, paramFloat4, paramFloat5, paramFloat6);
    lightDirection(lightCount, paramFloat7, paramFloat8, paramFloat9);
    lightSpotAngle[lightCount] = paramFloat10;
    lightSpotAngleCos[lightCount] = Math.max(0.0F, (float)Math.cos(paramFloat10));
    lightSpotConcentration[lightCount] = paramFloat11;
    lightCount += 1;
    lightingDependsOnVertexPosition = true;
  }
  
  public void lightFalloff(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    currentLightFalloffConstant = paramFloat1;
    currentLightFalloffLinear = paramFloat2;
    currentLightFalloffQuadratic = paramFloat3;
    lightingDependsOnVertexPosition = true;
  }
  
  public void lightSpecular(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    colorCalc(paramFloat1, paramFloat2, paramFloat3);
    currentLightSpecular[0] = calcR;
    currentLightSpecular[1] = calcG;
    currentLightSpecular[2] = calcB;
    lightingDependsOnVertexPosition = true;
  }
  
  protected void lightPosition(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    lightPositionVec.set(paramFloat1, paramFloat2, paramFloat3);
    modelview.mult(lightPositionVec, lightPosition[paramInt]);
  }
  
  protected void lightDirection(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    lightNormal[paramInt].set(modelviewInv.m00 * paramFloat1 + modelviewInv.m10 * paramFloat2 + modelviewInv.m20 * paramFloat3 + modelviewInv.m30, modelviewInv.m01 * paramFloat1 + modelviewInv.m11 * paramFloat2 + modelviewInv.m21 * paramFloat3 + modelviewInv.m31, modelviewInv.m02 * paramFloat1 + modelviewInv.m12 * paramFloat2 + modelviewInv.m22 * paramFloat3 + modelviewInv.m32);
    lightNormal[paramInt].normalize();
  }
  
  protected void backgroundImpl(PImage paramPImage)
  {
    System.arraycopy(pixels, 0, pixels, 0, pixels.length);
    Arrays.fill(zbuffer, Float.MAX_VALUE);
  }
  
  protected void backgroundImpl()
  {
    Arrays.fill(pixels, backgroundColor);
    Arrays.fill(zbuffer, Float.MAX_VALUE);
  }
  
  public boolean is2D()
  {
    return false;
  }
  
  public boolean is3D()
  {
    return true;
  }
  
  private final float sqrt(float paramFloat)
  {
    return (float)Math.sqrt(paramFloat);
  }
  
  private final float mag(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3);
  }
  
  private final float clamp(float paramFloat)
  {
    return paramFloat < 1.0F ? paramFloat : 1.0F;
  }
  
  private final float abs(float paramFloat)
  {
    return paramFloat < 0.0F ? -paramFloat : paramFloat;
  }
  
  private float dot(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    return paramFloat1 * paramFloat4 + paramFloat2 * paramFloat5 + paramFloat3 * paramFloat6;
  }
  
  private final void cross(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, PVector paramPVector)
  {
    x = (paramFloat2 * paramFloat6 - paramFloat3 * paramFloat5);
    y = (paramFloat3 * paramFloat4 - paramFloat1 * paramFloat6);
    z = (paramFloat1 * paramFloat5 - paramFloat2 * paramFloat4);
  }
}
