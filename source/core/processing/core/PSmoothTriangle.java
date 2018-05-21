package processing.core;

public class PSmoothTriangle
  implements PConstants
{
  private static final boolean EWJORDAN = false;
  private static final boolean FRY = false;
  static final int X = 0;
  static final int Y = 1;
  static final int Z = 2;
  static final int R = 3;
  static final int G = 4;
  static final int B = 5;
  static final int A = 6;
  static final int U = 7;
  static final int V = 8;
  static final int DEFAULT_SIZE = 64;
  float[][] vertices = new float[64][36];
  int vertexCount;
  static final int ZBUFFER_MIN_COVERAGE = 204;
  float[] r = new float[64];
  float[] dr = new float[64];
  float[] l = new float[64];
  float[] dl = new float[64];
  float[] sp = new float[64];
  float[] sdp = new float[64];
  boolean interpX;
  boolean interpZ;
  boolean interpUV;
  boolean interpARGB;
  int rgba;
  int r2;
  int g2;
  int b2;
  int a2;
  int a2orig;
  boolean noDepthTest;
  PGraphics3D parent;
  int[] pixels;
  float[] zbuffer;
  int width;
  int height;
  int width1;
  int height1;
  PImage timage;
  int[] tpixels;
  int theight;
  int twidth;
  int theight1;
  int twidth1;
  int tformat;
  boolean texture_smooth;
  static final int SUBXRES = 8;
  static final int SUBXRES1 = 7;
  static final int SUBYRES = 8;
  static final int SUBYRES1 = 7;
  static final int MAX_COVERAGE = 64;
  boolean smooth;
  int firstModY;
  int lastModY;
  int lastY;
  int[] aaleft = new int[8];
  int[] aaright = new int[8];
  int aaleftmin;
  int aarightmin;
  int aaleftmax;
  int aarightmax;
  int aaleftfull;
  int aarightfull;
  private float[] camX = new float[3];
  private float[] camY = new float[3];
  private float[] camZ = new float[3];
  private float ax;
  private float ay;
  private float az;
  private float bx;
  private float by;
  private float bz;
  private float cx;
  private float cy;
  private float cz;
  private float nearPlaneWidth;
  private float nearPlaneHeight;
  private float nearPlaneDepth;
  private float xmult;
  private float ymult;
  
  private final int MODYRES(int paramInt)
  {
    return paramInt & 0x7;
  }
  
  public PSmoothTriangle(PGraphics3D paramPGraphics3D)
  {
    parent = paramPGraphics3D;
    reset(0);
  }
  
  public void reset(int paramInt)
  {
    vertexCount = paramInt;
    interpX = true;
    interpZ = true;
    interpUV = false;
    interpARGB = true;
    timage = null;
  }
  
  public float[] nextVertex()
  {
    if (vertexCount == vertices.length)
    {
      float[][] arrayOfFloat = new float[vertexCount << 1][36];
      System.arraycopy(vertices, 0, arrayOfFloat, 0, vertexCount);
      vertices = arrayOfFloat;
      r = new float[vertices.length];
      dr = new float[vertices.length];
      l = new float[vertices.length];
      dl = new float[vertices.length];
      sp = new float[vertices.length];
      sdp = new float[vertices.length];
    }
    return vertices[(vertexCount++)];
  }
  
  public void texture(PImage paramPImage)
  {
    timage = paramPImage;
    tpixels = pixels;
    twidth = width;
    theight = height;
    tformat = format;
    twidth1 = (twidth - 1);
    theight1 = (theight - 1);
    interpUV = true;
  }
  
  public void render()
  {
    if (vertexCount < 3) {
      return;
    }
    smooth = true;
    pixels = parent.pixels;
    zbuffer = parent.zbuffer;
    noDepthTest = false;
    texture_smooth = true;
    width = (smooth ? parent.width * 8 : parent.width);
    height = (smooth ? parent.height * 8 : parent.height);
    width1 = (width - 1);
    height1 = (height - 1);
    if (!interpARGB)
    {
      r2 = ((int)(vertices[0][3] * 255.0F));
      g2 = ((int)(vertices[0][4] * 255.0F));
      b2 = ((int)(vertices[0][5] * 255.0F));
      a2 = ((int)(vertices[0][6] * 255.0F));
      a2orig = a2;
      rgba = (0xFF000000 | r2 << 16 | g2 << 8 | b2);
    }
    for (int i = 0; i < vertexCount; i++)
    {
      r[i] = 0.0F;
      dr[i] = 0.0F;
      l[i] = 0.0F;
      dl[i] = 0.0F;
    }
    if (smooth)
    {
      for (i = 0; i < vertexCount; i++)
      {
        vertices[i][0] *= 8.0F;
        vertices[i][1] *= 8.0F;
      }
      firstModY = -1;
    }
    i = 0;
    float f1 = vertices[0][1];
    float f2 = vertices[0][1];
    for (int j = 1; j < vertexCount; j++)
    {
      if (vertices[j][1] < f1)
      {
        f1 = vertices[j][1];
        i = j;
      }
      if (vertices[j][1] > f2) {
        f2 = vertices[j][1];
      }
    }
    lastY = ((int)(f2 - 0.5F));
    j = i;
    int k = i;
    int m = (int)(f1 + 0.5F);
    int n = m - 1;
    int i1 = m - 1;
    interpX = true;
    int i2 = vertexCount;
    while (i2 > 0)
    {
      int i3;
      while ((n <= m) && (i2 > 0))
      {
        i2--;
        i3 = j != 0 ? j - 1 : vertexCount - 1;
        incrementalize_y(vertices[j], vertices[i3], l, dl, m);
        n = (int)(vertices[i3][1] + 0.5F);
        j = i3;
      }
      while ((i1 <= m) && (i2 > 0))
      {
        i2--;
        i3 = k != vertexCount - 1 ? k + 1 : 0;
        incrementalize_y(vertices[k], vertices[i3], r, dr, m);
        i1 = (int)(vertices[i3][1] + 0.5F);
        k = i3;
      }
      while ((m < n) && (m < i1))
      {
        if ((m >= 0) && (m < height)) {
          if (l[0] <= r[0]) {
            scanline(m, l, r);
          } else {
            scanline(m, r, l);
          }
        }
        m++;
        increment(l, dl);
        increment(r, dr);
      }
    }
  }
  
  public void unexpand()
  {
    if (smooth) {
      for (int i = 0; i < vertexCount; i++)
      {
        vertices[i][0] /= 8.0F;
        vertices[i][1] /= 8.0F;
      }
    }
  }
  
  private void scanline(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    for (int i = 0; i < vertexCount; i++)
    {
      sp[i] = 0.0F;
      sdp[i] = 0.0F;
    }
    i = (int)(paramArrayOfFloat1[0] + 0.49999F);
    if (i < 0) {
      i = 0;
    }
    int j = (int)(paramArrayOfFloat2[0] - 0.5F);
    if (j > width1) {
      j = width1;
    }
    if (i > j) {
      return;
    }
    if (smooth)
    {
      k = MODYRES(paramInt);
      aaleft[k] = i;
      aaright[k] = j;
      if (firstModY == -1)
      {
        firstModY = k;
        aaleftmin = i;
        aaleftmax = i;
        aarightmin = j;
        aarightmax = j;
      }
      else
      {
        if (aaleftmin > aaleft[k]) {
          aaleftmin = aaleft[k];
        }
        if (aaleftmax < aaleft[k]) {
          aaleftmax = aaleft[k];
        }
        if (aarightmin > aaright[k]) {
          aarightmin = aaright[k];
        }
        if (aarightmax < aaright[k]) {
          aarightmax = aaright[k];
        }
      }
      lastModY = k;
      if ((k != 7) && (paramInt != lastY)) {
        return;
      }
      aaleftfull = (aaleftmax / 8 + 1);
      aarightfull = (aarightmin / 8 - 1);
    }
    incrementalize_x(paramArrayOfFloat1, paramArrayOfFloat2, sp, sdp, i);
    int k = smooth ? parent.width * (paramInt / 8) : parent.width * paramInt;
    int m = 0;
    int n = 0;
    if (smooth)
    {
      m = i / 8;
      n = (j + 7) / 8;
      i = aaleftmin / 8;
      j = (aarightmax + 7) / 8;
      if (i < 0) {
        i = 0;
      }
      if (j > parent.width1) {
        j = parent.width1;
      }
    }
    interpX = false;
    for (int i5 = i; i5 <= j; i5++)
    {
      if ((noDepthTest) || (sp[2] <= zbuffer[(k + i5)]))
      {
        int i6;
        int i7;
        int i8;
        int i10;
        if (interpUV)
        {
          i6 = (int)sp[7];
          i7 = (int)sp[8];
          if (i6 > twidth1) {
            i6 = twidth1;
          }
          if (i7 > theight1) {
            i7 = theight1;
          }
          if (i6 < 0) {
            i6 = 0;
          }
          if (i7 < 0) {
            i7 = 0;
          }
          i8 = i7 * twidth + i6;
          float[] arrayOfFloat = new float[2];
          i8 = getTextureIndex(i5, paramInt * 1.0F / 8.0F, arrayOfFloat);
          i6 = (int)arrayOfFloat[0];
          i7 = (int)arrayOfFloat[1];
          i8 = twidth * i7 + i6;
          int i11;
          int i12;
          int i13;
          int i4;
          int i1;
          int i2;
          int i3;
          if ((smooth) || (texture_smooth))
          {
            i10 = (int)(255.0F * (arrayOfFloat[0] - i6));
            i11 = (int)(255.0F * (arrayOfFloat[1] - i7));
            i12 = 255 - i10;
            i13 = 255 - i11;
            int i14 = tpixels[i8];
            int i15 = i7 < theight1 ? tpixels[(i8 + twidth)] : tpixels[i8];
            int i16 = i6 < twidth1 ? tpixels[(i8 + 1)] : tpixels[i8];
            int i17 = (i7 < theight1) && (i6 < twidth1) ? tpixels[(i8 + twidth + 1)] : tpixels[i8];
            int i22;
            int i23;
            int i18;
            int i19;
            int i20;
            int i21;
            if (tformat == 4)
            {
              i22 = i14 * i12 + i16 * i10 >> 8;
              i23 = i15 * i12 + i17 * i10 >> 8;
              i4 = (i22 * i13 + i23 * i11 >> 8) * (interpARGB ? (int)(sp[6] * 255.0F) : a2orig) >> 8;
            }
            else if (tformat == 2)
            {
              i18 = i14 >> 24 & 0xFF;
              i19 = i15 >> 24 & 0xFF;
              i20 = i16 >> 24 & 0xFF;
              i21 = i17 >> 24 & 0xFF;
              i22 = i18 * i12 + i20 * i10 >> 8;
              i23 = i19 * i12 + i21 * i10 >> 8;
              i4 = (i22 * i13 + i23 * i11 >> 8) * (interpARGB ? (int)(sp[6] * 255.0F) : a2orig) >> 8;
            }
            else
            {
              i4 = interpARGB ? (int)(sp[6] * 255.0F) : a2orig;
            }
            if ((tformat == 1) || (tformat == 2))
            {
              i18 = i14 >> 16 & 0xFF;
              i19 = i15 >> 16 & 0xFF;
              i20 = i16 >> 16 & 0xFF;
              i21 = i17 >> 16 & 0xFF;
              i22 = i18 * i12 + i20 * i10 >> 8;
              i23 = i19 * i12 + i21 * i10 >> 8;
              i1 = (i22 * i13 + i23 * i11 >> 8) * (interpARGB ? (int)(sp[3] * 255.0F) : r2) >> 8;
              i18 = i14 >> 8 & 0xFF;
              i19 = i15 >> 8 & 0xFF;
              i20 = i16 >> 8 & 0xFF;
              i21 = i17 >> 8 & 0xFF;
              i22 = i18 * i12 + i20 * i10 >> 8;
              i23 = i19 * i12 + i21 * i10 >> 8;
              i2 = (i22 * i13 + i23 * i11 >> 8) * (interpARGB ? (int)(sp[4] * 255.0F) : g2) >> 8;
              i18 = i14 & 0xFF;
              i19 = i15 & 0xFF;
              i20 = i16 & 0xFF;
              i21 = i17 & 0xFF;
              i22 = i18 * i12 + i20 * i10 >> 8;
              i23 = i19 * i12 + i21 * i10 >> 8;
              i3 = (i22 * i13 + i23 * i11 >> 8) * (interpARGB ? (int)(sp[5] * 255.0F) : b2) >> 8;
            }
            else if (interpARGB)
            {
              i1 = (int)(sp[3] * 255.0F);
              i2 = (int)(sp[4] * 255.0F);
              i3 = (int)(sp[5] * 255.0F);
            }
            else
            {
              i1 = r2;
              i2 = g2;
              i3 = b2;
            }
            int i24 = smooth ? coverage(i5) : 255;
            if (i24 != 255) {
              i4 = i4 * i24 >> 8;
            }
          }
          else
          {
            i10 = tpixels[i8];
            if (tformat == 4)
            {
              i4 = i10;
              if (interpARGB)
              {
                i1 = (int)(sp[3] * 255.0F);
                i2 = (int)(sp[4] * 255.0F);
                i3 = (int)(sp[5] * 255.0F);
                if (sp[6] != 1.0F) {
                  i4 = (int)(sp[6] * 255.0F) * i4 >> 8;
                }
              }
              else
              {
                i1 = r2;
                i2 = g2;
                i3 = b2;
                i4 = a2orig * i4 >> 8;
              }
            }
            else
            {
              i4 = tformat == 1 ? 255 : i10 >> 24 & 0xFF;
              if (interpARGB)
              {
                i1 = (int)(sp[3] * 255.0F) * (i10 >> 16 & 0xFF) >> 8;
                i2 = (int)(sp[4] * 255.0F) * (i10 >> 8 & 0xFF) >> 8;
                i3 = (int)(sp[5] * 255.0F) * (i10 & 0xFF) >> 8;
                i4 = (int)(sp[6] * 255.0F) * i4 >> 8;
              }
              else
              {
                i1 = r2 * (i10 >> 16 & 0xFF) >> 8;
                i2 = g2 * (i10 >> 8 & 0xFF) >> 8;
                i3 = b2 * (i10 & 0xFF) >> 8;
                i4 = a2orig * i4 >> 8;
              }
            }
          }
          if ((i4 == 254) || (i4 == 255))
          {
            pixels[(k + i5)] = (0xFF000000 | i1 << 16 | i2 << 8 | i3);
            zbuffer[(k + i5)] = sp[2];
          }
          else
          {
            i10 = 255 - i4;
            i11 = pixels[(k + i5)] >> 16 & 0xFF;
            i12 = pixels[(k + i5)] >> 8 & 0xFF;
            i13 = pixels[(k + i5)] & 0xFF;
            pixels[(k + i5)] = (0xFF000000 | i1 * i4 + i11 * i10 >> 8 << 16 | i2 * i4 + i12 * i10 & 0xFF00 | i3 * i4 + i13 * i10 >> 8);
            if (i4 > 204) {
              zbuffer[(k + i5)] = sp[2];
            }
          }
        }
        else
        {
          i6 = smooth ? coverage(i5) : 255;
          if (interpARGB)
          {
            r2 = ((int)(sp[3] * 255.0F));
            g2 = ((int)(sp[4] * 255.0F));
            b2 = ((int)(sp[5] * 255.0F));
            if (sp[6] != 1.0F) {
              i6 = i6 * (int)(sp[6] * 255.0F) >> 8;
            }
            if (i6 == 255) {
              rgba = (0xFF000000 | r2 << 16 | g2 << 8 | b2);
            }
          }
          else if (a2orig != 255)
          {
            i6 = i6 * a2orig >> 8;
          }
          if (i6 == 255)
          {
            pixels[(k + i5)] = rgba;
            zbuffer[(k + i5)] = sp[2];
          }
          else
          {
            i7 = pixels[(k + i5)] >> 16 & 0xFF;
            i8 = pixels[(k + i5)] >> 8 & 0xFF;
            int i9 = pixels[(k + i5)] & 0xFF;
            a2 = i6;
            i10 = 255 - a2;
            pixels[(k + i5)] = (0xFF000000 | i7 * i10 + r2 * a2 >> 8 << 16 | i8 * i10 + g2 * a2 >> 8 << 8 | i9 * i10 + b2 * a2 >> 8);
            if (a2 > 204) {
              zbuffer[(k + i5)] = sp[2];
            }
          }
        }
      }
      if ((!smooth) || ((i5 >= m) && (i5 <= n))) {
        increment(sp, sdp);
      }
    }
    firstModY = -1;
    interpX = true;
  }
  
  private int coverage(int paramInt)
  {
    if ((paramInt >= aaleftfull) && (paramInt <= aarightfull) && (firstModY == 0) && (lastModY == 7)) {
      return 255;
    }
    int i = paramInt * 8;
    int j = i + 8;
    int k = 0;
    for (int m = firstModY; m <= lastModY; m++) {
      if ((aaleft[m] <= j) && (aaright[m] >= i)) {
        k += (aaright[m] < j ? aaright[m] : j) - (aaleft[m] > i ? aaleft[m] : i);
      }
    }
    k <<= 2;
    return k == 256 ? 255 : k;
  }
  
  private void incrementalize_y(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int paramInt)
  {
    float f1 = paramArrayOfFloat2[1] - paramArrayOfFloat1[1];
    if (f1 == 0.0F) {
      f1 = 1.0F;
    }
    float f2 = paramInt + 0.5F - paramArrayOfFloat1[1];
    if (interpX)
    {
      paramArrayOfFloat4[0] = ((paramArrayOfFloat2[0] - paramArrayOfFloat1[0]) / f1);
      paramArrayOfFloat1[0] += paramArrayOfFloat4[0] * f2;
    }
    if (interpZ)
    {
      paramArrayOfFloat4[2] = ((paramArrayOfFloat2[2] - paramArrayOfFloat1[2]) / f1);
      paramArrayOfFloat1[2] += paramArrayOfFloat4[2] * f2;
    }
    if (interpARGB)
    {
      paramArrayOfFloat4[3] = ((paramArrayOfFloat2[3] - paramArrayOfFloat1[3]) / f1);
      paramArrayOfFloat4[4] = ((paramArrayOfFloat2[4] - paramArrayOfFloat1[4]) / f1);
      paramArrayOfFloat4[5] = ((paramArrayOfFloat2[5] - paramArrayOfFloat1[5]) / f1);
      paramArrayOfFloat4[6] = ((paramArrayOfFloat2[6] - paramArrayOfFloat1[6]) / f1);
      paramArrayOfFloat1[3] += paramArrayOfFloat4[3] * f2;
      paramArrayOfFloat1[4] += paramArrayOfFloat4[4] * f2;
      paramArrayOfFloat1[5] += paramArrayOfFloat4[5] * f2;
      paramArrayOfFloat1[6] += paramArrayOfFloat4[6] * f2;
    }
    if (interpUV)
    {
      paramArrayOfFloat4[7] = ((paramArrayOfFloat2[7] - paramArrayOfFloat1[7]) / f1);
      paramArrayOfFloat4[8] = ((paramArrayOfFloat2[8] - paramArrayOfFloat1[8]) / f1);
      paramArrayOfFloat1[7] += paramArrayOfFloat4[7] * f2;
      paramArrayOfFloat1[8] += paramArrayOfFloat4[8] * f2;
    }
  }
  
  private void incrementalize_x(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int paramInt)
  {
    float f1 = paramArrayOfFloat2[0] - paramArrayOfFloat1[0];
    if (f1 == 0.0F) {
      f1 = 1.0F;
    }
    float f2 = paramInt + 0.5F - paramArrayOfFloat1[0];
    if (smooth)
    {
      f1 /= 8.0F;
      f2 /= 8.0F;
    }
    if (interpX)
    {
      paramArrayOfFloat4[0] = ((paramArrayOfFloat2[0] - paramArrayOfFloat1[0]) / f1);
      paramArrayOfFloat1[0] += paramArrayOfFloat4[0] * f2;
    }
    if (interpZ)
    {
      paramArrayOfFloat4[2] = ((paramArrayOfFloat2[2] - paramArrayOfFloat1[2]) / f1);
      paramArrayOfFloat1[2] += paramArrayOfFloat4[2] * f2;
    }
    if (interpARGB)
    {
      paramArrayOfFloat4[3] = ((paramArrayOfFloat2[3] - paramArrayOfFloat1[3]) / f1);
      paramArrayOfFloat4[4] = ((paramArrayOfFloat2[4] - paramArrayOfFloat1[4]) / f1);
      paramArrayOfFloat4[5] = ((paramArrayOfFloat2[5] - paramArrayOfFloat1[5]) / f1);
      paramArrayOfFloat4[6] = ((paramArrayOfFloat2[6] - paramArrayOfFloat1[6]) / f1);
      paramArrayOfFloat1[3] += paramArrayOfFloat4[3] * f2;
      paramArrayOfFloat1[4] += paramArrayOfFloat4[4] * f2;
      paramArrayOfFloat1[5] += paramArrayOfFloat4[5] * f2;
      paramArrayOfFloat1[6] += paramArrayOfFloat4[6] * f2;
    }
    if (interpUV)
    {
      paramArrayOfFloat4[7] = ((paramArrayOfFloat2[7] - paramArrayOfFloat1[7]) / f1);
      paramArrayOfFloat4[8] = ((paramArrayOfFloat2[8] - paramArrayOfFloat1[8]) / f1);
      paramArrayOfFloat1[7] += paramArrayOfFloat4[7] * f2;
      paramArrayOfFloat1[8] += paramArrayOfFloat4[8] * f2;
    }
  }
  
  private void increment(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (interpX) {
      paramArrayOfFloat1[0] += paramArrayOfFloat2[0];
    }
    if (interpZ) {
      paramArrayOfFloat1[2] += paramArrayOfFloat2[2];
    }
    if (interpARGB)
    {
      paramArrayOfFloat1[3] += paramArrayOfFloat2[3];
      paramArrayOfFloat1[4] += paramArrayOfFloat2[4];
      paramArrayOfFloat1[5] += paramArrayOfFloat2[5];
      paramArrayOfFloat1[6] += paramArrayOfFloat2[6];
    }
    if (interpUV)
    {
      paramArrayOfFloat1[7] += paramArrayOfFloat2[7];
      paramArrayOfFloat1[8] += paramArrayOfFloat2[8];
    }
  }
  
  public void setCamVertices(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    camX[0] = paramFloat1;
    camX[1] = paramFloat4;
    camX[2] = paramFloat7;
    camY[0] = paramFloat2;
    camY[1] = paramFloat5;
    camY[2] = paramFloat8;
    camZ[0] = paramFloat3;
    camZ[1] = paramFloat6;
    camZ[2] = paramFloat9;
  }
  
  public void setVertices(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    vertices[0][0] = paramFloat1;
    vertices[1][0] = paramFloat4;
    vertices[2][0] = paramFloat7;
    vertices[0][1] = paramFloat2;
    vertices[1][1] = paramFloat5;
    vertices[2][1] = paramFloat8;
    vertices[0][2] = paramFloat3;
    vertices[1][2] = paramFloat6;
    vertices[2][2] = paramFloat9;
  }
  
  boolean precomputeAccurateTexturing()
  {
    int i = 0;
    int j = 1;
    int k = 2;
    PMatrix3D localPMatrix3D = new PMatrix3D(vertices[i][7], vertices[i][8], 1.0F, 0.0F, vertices[j][7], vertices[j][8], 1.0F, 0.0F, vertices[k][7], vertices[k][8], 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    boolean bool = localPMatrix3D.invert();
    if (!bool) {
      return false;
    }
    float f1 = m00 * camX[i] + m01 * camX[j] + m02 * camX[k];
    float f2 = m10 * camX[i] + m11 * camX[j] + m12 * camX[k];
    float f3 = m20 * camX[i] + m21 * camX[j] + m22 * camX[k];
    float f4 = m00 * camY[i] + m01 * camY[j] + m02 * camY[k];
    float f5 = m10 * camY[i] + m11 * camY[j] + m12 * camY[k];
    float f6 = m20 * camY[i] + m21 * camY[j] + m22 * camY[k];
    float f7 = -(m00 * camZ[i] + m01 * camZ[j] + m02 * camZ[k]);
    float f8 = -(m10 * camZ[i] + m11 * camZ[j] + m12 * camZ[k]);
    float f9 = -(m20 * camZ[i] + m21 * camZ[j] + m22 * camZ[k]);
    float f10 = f3;
    float f11 = f6;
    float f12 = f9;
    float f13 = twidth;
    float f14 = theight;
    float f15 = f1 * f13 + f3;
    float f16 = f4 * f13 + f6;
    float f17 = f7 * f13 + f9;
    float f18 = f2 * f14 + f3;
    float f19 = f5 * f14 + f6;
    float f20 = f8 * f14 + f9;
    float f21 = f15 - f3;
    float f22 = f16 - f6;
    float f23 = f17 - f9;
    float f24 = f18 - f3;
    float f25 = f19 - f6;
    float f26 = f20 - f9;
    ax = ((f11 * f26 - f12 * f25) * f13);
    ay = ((f12 * f24 - f10 * f26) * f13);
    az = ((f10 * f25 - f11 * f24) * f13);
    bx = ((f22 * f12 - f23 * f11) * f14);
    by = ((f23 * f10 - f21 * f12) * f14);
    bz = ((f21 * f11 - f22 * f10) * f14);
    cx = (f25 * f23 - f26 * f22);
    cy = (f26 * f21 - f24 * f23);
    cz = (f24 * f22 - f25 * f21);
    nearPlaneWidth = (parent.rightScreen - parent.leftScreen);
    nearPlaneHeight = (parent.topScreen - parent.bottomScreen);
    nearPlaneDepth = parent.nearPlane;
    xmult = (nearPlaneWidth / parent.width);
    ymult = (nearPlaneHeight / parent.height);
    return true;
  }
  
  private int getTextureIndex(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat)
  {
    paramFloat1 = xmult * (paramFloat1 - parent.width / 2.0F + 0.5F);
    paramFloat2 = ymult * (paramFloat2 - parent.height / 2.0F + 0.5F);
    float f1 = nearPlaneDepth;
    float f2 = paramFloat1 * ax + paramFloat2 * ay + f1 * az;
    float f3 = paramFloat1 * bx + paramFloat2 * by + f1 * bz;
    float f4 = paramFloat1 * cx + paramFloat2 * cy + f1 * cz;
    int i = (int)(f2 / f4);
    int j = (int)(f3 / f4);
    paramArrayOfFloat[0] = (f2 / f4);
    paramArrayOfFloat[1] = (f3 / f4);
    if (paramArrayOfFloat[0] < 0.0F)
    {
      int tmp160_159 = 0;
      i = tmp160_159;
      paramArrayOfFloat[0] = tmp160_159;
    }
    if (paramArrayOfFloat[1] < 0.0F)
    {
      int tmp176_175 = 0;
      j = tmp176_175;
      paramArrayOfFloat[1] = tmp176_175;
    }
    if (paramArrayOfFloat[0] >= twidth)
    {
      paramArrayOfFloat[0] = (twidth - 1);
      i = twidth - 1;
    }
    if (paramArrayOfFloat[1] >= theight)
    {
      paramArrayOfFloat[1] = (theight - 1);
      j = theight - 1;
    }
    int k = j * twidth + i;
    return k;
  }
  
  public void setIntensities(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    vertices[0][3] = paramFloat1;
    vertices[0][4] = paramFloat2;
    vertices[0][5] = paramFloat3;
    vertices[0][6] = paramFloat4;
    vertices[1][3] = paramFloat5;
    vertices[1][4] = paramFloat6;
    vertices[1][5] = paramFloat7;
    vertices[1][6] = paramFloat8;
    vertices[2][3] = paramFloat9;
    vertices[2][4] = paramFloat10;
    vertices[2][5] = paramFloat11;
    vertices[2][6] = paramFloat12;
  }
}
