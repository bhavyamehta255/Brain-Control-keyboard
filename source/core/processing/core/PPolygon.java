package processing.core;

public class PPolygon
  implements PConstants
{
  static final int DEFAULT_SIZE = 64;
  float[][] vertices = new float[64][36];
  int vertexCount;
  float[] r = new float[64];
  float[] dr = new float[64];
  float[] l = new float[64];
  float[] dl = new float[64];
  float[] sp = new float[64];
  float[] sdp = new float[64];
  protected boolean interpX;
  protected boolean interpUV;
  protected boolean interpARGB;
  private int rgba;
  private int r2;
  private int g2;
  private int b2;
  private int a2;
  private int a2orig;
  PGraphics parent;
  int[] pixels;
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
  
  private final int MODYRES(int paramInt)
  {
    return paramInt & 0x7;
  }
  
  public PPolygon(PGraphics paramPGraphics)
  {
    parent = paramPGraphics;
    reset(0);
  }
  
  protected void reset(int paramInt)
  {
    vertexCount = paramInt;
    interpX = true;
    interpUV = false;
    interpARGB = true;
    timage = null;
  }
  
  protected float[] nextVertex()
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
  
  protected void texture(PImage paramPImage)
  {
    timage = paramPImage;
    if (paramPImage != null)
    {
      tpixels = pixels;
      twidth = width;
      theight = height;
      tformat = format;
      twidth1 = (twidth - 1);
      theight1 = (theight - 1);
      interpUV = true;
    }
    else
    {
      interpUV = false;
    }
  }
  
  protected void renderPolygon(float[][] paramArrayOfFloat, int paramInt)
  {
    vertices = paramArrayOfFloat;
    vertexCount = paramInt;
    if (r.length < vertexCount)
    {
      r = new float[vertexCount];
      dr = new float[vertexCount];
      l = new float[vertexCount];
      dl = new float[vertexCount];
      sp = new float[vertexCount];
      sdp = new float[vertexCount];
    }
    render();
    checkExpand();
  }
  
  protected void renderTriangle(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    vertices[0] = paramArrayOfFloat1;
    vertices[1] = paramArrayOfFloat2;
    vertices[2] = paramArrayOfFloat3;
    render();
    checkExpand();
  }
  
  protected void checkExpand()
  {
    if (smooth) {
      for (int i = 0; i < vertexCount; i++)
      {
        vertices[i][18] /= 8.0F;
        vertices[i][19] /= 8.0F;
      }
    }
  }
  
  protected void render()
  {
    if (vertexCount < 3) {
      return;
    }
    pixels = parent.pixels;
    smooth = parent.smooth;
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
        vertices[i][18] *= 8.0F;
        vertices[i][19] *= 8.0F;
      }
      firstModY = -1;
    }
    i = 0;
    float f1 = vertices[0][19];
    float f2 = vertices[0][19];
    for (int j = 1; j < vertexCount; j++)
    {
      if (vertices[j][19] < f1)
      {
        f1 = vertices[j][19];
        i = j;
      }
      if (vertices[j][19] > f2) {
        f2 = vertices[j][19];
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
        incrementalizeY(vertices[j], vertices[i3], l, dl, m);
        n = (int)(vertices[i3][19] + 0.5F);
        j = i3;
      }
      while ((i1 <= m) && (i2 > 0))
      {
        i2--;
        i3 = k != vertexCount - 1 ? k + 1 : 0;
        incrementalizeY(vertices[k], vertices[i3], r, dr, m);
        i1 = (int)(vertices[i3][19] + 0.5F);
        k = i3;
      }
      while ((m < n) && (m < i1))
      {
        if ((m >= 0) && (m < height)) {
          if (l[18] <= r[18]) {
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
  
  private void scanline(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    for (int i = 0; i < vertexCount; i++)
    {
      sp[i] = 0.0F;
      sdp[i] = 0.0F;
    }
    i = (int)(paramArrayOfFloat1[18] + 0.49999F);
    if (i < 0) {
      i = 0;
    }
    int j = (int)(paramArrayOfFloat2[18] - 0.5F);
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
    incrementalizeX(paramArrayOfFloat1, paramArrayOfFloat2, sp, sdp, i);
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
      int i6;
      int i7;
      int i8;
      int i9;
      int i10;
      if (interpUV)
      {
        i6 = (int)(sp[7] * twidth);
        i7 = (int)(sp[8] * theight);
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
        i9 = (int)(255.0F * (sp[7] * twidth - i6));
        i10 = (int)(255.0F * (sp[8] * theight - i7));
        int i11 = 255 - i9;
        int i12 = 255 - i10;
        int i13 = tpixels[i8];
        int i14 = i7 < theight1 ? tpixels[(i8 + twidth)] : tpixels[i8];
        int i15 = i6 < twidth1 ? tpixels[(i8 + 1)] : tpixels[i8];
        int i16 = (i7 < theight1) && (i6 < twidth1) ? tpixels[(i8 + twidth + 1)] : tpixels[i8];
        int i21;
        int i22;
        int i4;
        int i17;
        int i18;
        int i19;
        int i20;
        if (tformat == 4)
        {
          i21 = i13 * i11 + i15 * i9 >> 8;
          i22 = i14 * i11 + i16 * i9 >> 8;
          i4 = (i21 * i12 + i22 * i10 >> 8) * (interpARGB ? (int)(sp[6] * 255.0F) : a2orig) >> 8;
        }
        else if (tformat == 2)
        {
          i17 = i13 >> 24 & 0xFF;
          i18 = i14 >> 24 & 0xFF;
          i19 = i15 >> 24 & 0xFF;
          i20 = i16 >> 24 & 0xFF;
          i21 = i17 * i11 + i19 * i9 >> 8;
          i22 = i18 * i11 + i20 * i9 >> 8;
          i4 = (i21 * i12 + i22 * i10 >> 8) * (interpARGB ? (int)(sp[6] * 255.0F) : a2orig) >> 8;
        }
        else
        {
          i4 = interpARGB ? (int)(sp[6] * 255.0F) : a2orig;
        }
        int i1;
        int i2;
        int i3;
        if ((tformat == 1) || (tformat == 2))
        {
          i17 = i13 >> 16 & 0xFF;
          i18 = i14 >> 16 & 0xFF;
          i19 = i15 >> 16 & 0xFF;
          i20 = i16 >> 16 & 0xFF;
          i21 = i17 * i11 + i19 * i9 >> 8;
          i22 = i18 * i11 + i20 * i9 >> 8;
          i1 = (i21 * i12 + i22 * i10 >> 8) * (interpARGB ? (int)(sp[3] * 255.0F) : r2) >> 8;
          i17 = i13 >> 8 & 0xFF;
          i18 = i14 >> 8 & 0xFF;
          i19 = i15 >> 8 & 0xFF;
          i20 = i16 >> 8 & 0xFF;
          i21 = i17 * i11 + i19 * i9 >> 8;
          i22 = i18 * i11 + i20 * i9 >> 8;
          i2 = (i21 * i12 + i22 * i10 >> 8) * (interpARGB ? (int)(sp[4] * 255.0F) : g2) >> 8;
          i17 = i13 & 0xFF;
          i18 = i14 & 0xFF;
          i19 = i15 & 0xFF;
          i20 = i16 & 0xFF;
          i21 = i17 * i11 + i19 * i9 >> 8;
          i22 = i18 * i11 + i20 * i9 >> 8;
          i3 = (i21 * i12 + i22 * i10 >> 8) * (interpARGB ? (int)(sp[5] * 255.0F) : b2) >> 8;
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
        int i23 = smooth ? coverage(i5) : 255;
        if (i23 != 255) {
          i4 = i4 * i23 >> 8;
        }
        if ((i4 == 254) || (i4 == 255))
        {
          pixels[(k + i5)] = (0xFF000000 | i1 << 16 | i2 << 8 | i3);
        }
        else
        {
          int i24 = 255 - i4;
          int i25 = pixels[(k + i5)] >> 16 & 0xFF;
          int i26 = pixels[(k + i5)] >> 8 & 0xFF;
          int i27 = pixels[(k + i5)] & 0xFF;
          pixels[(k + i5)] = (0xFF000000 | i1 * i4 + i25 * i24 >> 8 << 16 | i2 * i4 + i26 * i24 & 0xFF00 | i3 * i4 + i27 * i24 >> 8);
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
        }
        else
        {
          i7 = pixels[(k + i5)] >> 16 & 0xFF;
          i8 = pixels[(k + i5)] >> 8 & 0xFF;
          i9 = pixels[(k + i5)] & 0xFF;
          a2 = i6;
          i10 = 255 - a2;
          pixels[(k + i5)] = (0xFF000000 | i7 * i10 + r2 * a2 >> 8 << 16 | i8 * i10 + g2 * a2 >> 8 << 8 | i9 * i10 + b2 * a2 >> 8);
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
  
  private void incrementalizeY(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int paramInt)
  {
    float f1 = paramArrayOfFloat2[19] - paramArrayOfFloat1[19];
    if (f1 == 0.0F) {
      f1 = 1.0F;
    }
    float f2 = paramInt + 0.5F - paramArrayOfFloat1[19];
    if (interpX)
    {
      paramArrayOfFloat4[18] = ((paramArrayOfFloat2[18] - paramArrayOfFloat1[18]) / f1);
      paramArrayOfFloat1[18] += paramArrayOfFloat4[18] * f2;
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
  
  private void incrementalizeX(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int paramInt)
  {
    float f1 = paramArrayOfFloat2[18] - paramArrayOfFloat1[18];
    if (f1 == 0.0F) {
      f1 = 1.0F;
    }
    float f2 = paramInt + 0.5F - paramArrayOfFloat1[18];
    if (smooth)
    {
      f1 /= 8.0F;
      f2 /= 8.0F;
    }
    if (interpX)
    {
      paramArrayOfFloat4[18] = ((paramArrayOfFloat2[18] - paramArrayOfFloat1[18]) / f1);
      paramArrayOfFloat1[18] += paramArrayOfFloat4[18] * f2;
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
      paramArrayOfFloat1[18] += paramArrayOfFloat2[18];
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
}
