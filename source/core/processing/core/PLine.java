package processing.core;

public class PLine
  implements PConstants
{
  private int[] m_pixels;
  private float[] m_zbuffer;
  private int m_index;
  static final int R_COLOR = 1;
  static final int R_ALPHA = 2;
  static final int R_SPATIAL = 8;
  static final int R_THICK = 4;
  static final int R_SMOOTH = 16;
  private int SCREEN_WIDTH;
  private int SCREEN_HEIGHT;
  private int SCREEN_WIDTH1;
  private int SCREEN_HEIGHT1;
  public boolean INTERPOLATE_RGB;
  public boolean INTERPOLATE_ALPHA;
  public boolean INTERPOLATE_Z = false;
  public boolean INTERPOLATE_THICK;
  private boolean SMOOTH;
  private int m_stroke;
  public int m_drawFlags;
  private float[] x_array = new float[2];
  private float[] y_array = new float[2];
  private float[] z_array = new float[2];
  private float[] r_array = new float[2];
  private float[] g_array = new float[2];
  private float[] b_array = new float[2];
  private float[] a_array = new float[2];
  private int o0;
  private int o1;
  private float m_r0;
  private float m_g0;
  private float m_b0;
  private float m_a0;
  private float m_z0;
  private float dz;
  private float dr;
  private float dg;
  private float db;
  private float da;
  private PGraphics parent;
  
  public PLine(PGraphics paramPGraphics)
  {
    parent = paramPGraphics;
  }
  
  public void reset()
  {
    SCREEN_WIDTH = parent.width;
    SCREEN_HEIGHT = parent.height;
    SCREEN_WIDTH1 = (SCREEN_WIDTH - 1);
    SCREEN_HEIGHT1 = (SCREEN_HEIGHT - 1);
    m_pixels = parent.pixels;
    if ((parent instanceof PGraphics3D)) {
      m_zbuffer = parent).zbuffer;
    }
    INTERPOLATE_RGB = false;
    INTERPOLATE_ALPHA = false;
    m_drawFlags = 0;
    m_index = 0;
  }
  
  public void setVertices(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if ((paramFloat3 != paramFloat6) || (paramFloat3 != 0.0F) || (paramFloat6 != 0.0F) || (INTERPOLATE_Z))
    {
      INTERPOLATE_Z = true;
      m_drawFlags |= 0x8;
    }
    else
    {
      INTERPOLATE_Z = false;
      m_drawFlags &= 0xFFFFFFF7;
    }
    z_array[0] = paramFloat3;
    z_array[1] = paramFloat6;
    x_array[0] = paramFloat1;
    x_array[1] = paramFloat4;
    y_array[0] = paramFloat2;
    y_array[1] = paramFloat5;
  }
  
  public void setIntensities(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    a_array[0] = ((paramFloat4 * 253.0F + 1.0F) * 65536.0F);
    a_array[1] = ((paramFloat8 * 253.0F + 1.0F) * 65536.0F);
    if ((paramFloat4 != 1.0F) || (paramFloat8 != 1.0F))
    {
      INTERPOLATE_ALPHA = true;
      m_drawFlags |= 0x2;
    }
    else
    {
      INTERPOLATE_ALPHA = false;
      m_drawFlags &= 0xFFFFFFFD;
    }
    r_array[0] = ((paramFloat1 * 253.0F + 1.0F) * 65536.0F);
    r_array[1] = ((paramFloat5 * 253.0F + 1.0F) * 65536.0F);
    g_array[0] = ((paramFloat2 * 253.0F + 1.0F) * 65536.0F);
    g_array[1] = ((paramFloat6 * 253.0F + 1.0F) * 65536.0F);
    b_array[0] = ((paramFloat3 * 253.0F + 1.0F) * 65536.0F);
    b_array[1] = ((paramFloat7 * 253.0F + 1.0F) * 65536.0F);
    if (paramFloat1 != paramFloat5)
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else if (paramFloat2 != paramFloat6)
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else if (paramFloat3 != paramFloat7)
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else
    {
      m_stroke = (0xFF000000 | (int)(255.0F * paramFloat1) << 16 | (int)(255.0F * paramFloat2) << 8 | (int)(255.0F * paramFloat3));
      INTERPOLATE_RGB = false;
      m_drawFlags &= 0xFFFFFFFE;
    }
  }
  
  public void setIndex(int paramInt)
  {
    m_index = paramInt;
    if (m_index == -1) {
      m_index = 0;
    }
  }
  
  public void draw()
  {
    boolean bool1 = true;
    if (parent.smooth)
    {
      SMOOTH = true;
      m_drawFlags |= 0x10;
    }
    else
    {
      SMOOTH = false;
      m_drawFlags &= 0xFFFFFFEF;
    }
    bool1 = lineClipping();
    if (!bool1) {
      return;
    }
    boolean bool2 = false;
    float f3;
    if (x_array[1] < x_array[0])
    {
      f3 = x_array[1];
      x_array[1] = x_array[0];
      x_array[0] = f3;
      f3 = y_array[1];
      y_array[1] = y_array[0];
      y_array[0] = f3;
      f3 = z_array[1];
      z_array[1] = z_array[0];
      z_array[0] = f3;
      f3 = r_array[1];
      r_array[1] = r_array[0];
      r_array[0] = f3;
      f3 = g_array[1];
      g_array[1] = g_array[0];
      g_array[0] = f3;
      f3 = b_array[1];
      b_array[1] = b_array[0];
      b_array[0] = f3;
      f3 = a_array[1];
      a_array[1] = a_array[0];
      a_array[0] = f3;
    }
    float f2 = (int)x_array[1] - (int)x_array[0];
    float f1 = (int)y_array[1] - (int)y_array[0];
    int m;
    if (Math.abs(f1) > Math.abs(f2))
    {
      f3 = f1;
      f1 = f2;
      m = f3;
      bool2 = true;
    }
    int i;
    int j;
    int k;
    if (m < 0)
    {
      o0 = 1;
      o1 = 0;
      i = (int)x_array[1];
      j = (int)y_array[1];
      k = -m;
    }
    else
    {
      o0 = 0;
      o1 = 1;
      i = (int)x_array[0];
      j = (int)y_array[0];
      k = m;
    }
    int n;
    if (k == 0) {
      n = 0;
    } else {
      n = (f1 << 16) / m;
    }
    m_r0 = r_array[o0];
    m_g0 = g_array[o0];
    m_b0 = b_array[o0];
    if (INTERPOLATE_RGB)
    {
      dr = ((r_array[o1] - r_array[o0]) / k);
      dg = ((g_array[o1] - g_array[o0]) / k);
      db = ((b_array[o1] - b_array[o0]) / k);
    }
    else
    {
      dr = 0.0F;
      dg = 0.0F;
      db = 0.0F;
    }
    m_a0 = a_array[o0];
    if (INTERPOLATE_ALPHA) {
      da = ((a_array[o1] - a_array[o0]) / k);
    } else {
      da = 0.0F;
    }
    m_z0 = z_array[o0];
    if (INTERPOLATE_Z) {
      dz = ((z_array[o1] - z_array[o0]) / k);
    } else {
      dz = 0.0F;
    }
    if (k == 0)
    {
      if (INTERPOLATE_ALPHA) {
        drawPoint_alpha(i, j);
      } else {
        drawPoint(i, j);
      }
      return;
    }
    if (SMOOTH) {
      drawLine_smooth(i, j, n, k, bool2);
    } else if (m_drawFlags == 0) {
      drawLine_plain(i, j, n, k, bool2);
    } else if (m_drawFlags == 2) {
      drawLine_plain_alpha(i, j, n, k, bool2);
    } else if (m_drawFlags == 1) {
      drawLine_color(i, j, n, k, bool2);
    } else if (m_drawFlags == 3) {
      drawLine_color_alpha(i, j, n, k, bool2);
    } else if (m_drawFlags == 8) {
      drawLine_plain_spatial(i, j, n, k, bool2);
    } else if (m_drawFlags == 10) {
      drawLine_plain_alpha_spatial(i, j, n, k, bool2);
    } else if (m_drawFlags == 9) {
      drawLine_color_spatial(i, j, n, k, bool2);
    } else if (m_drawFlags == 11) {
      drawLine_color_alpha_spatial(i, j, n, k, bool2);
    }
  }
  
  public boolean lineClipping()
  {
    int i = lineClipCode(x_array[0], y_array[0]);
    int j = lineClipCode(x_array[1], y_array[1]);
    int k = i | j;
    if ((i & j) != 0) {
      return false;
    }
    if (k != 0)
    {
      float f1 = 0.0F;
      float f2 = 1.0F;
      float f3 = 0.0F;
      for (int m = 0; m < 4; m++) {
        if ((k >> m) % 2 == 1)
        {
          f3 = lineSlope(x_array[0], y_array[0], x_array[1], y_array[1], m + 1);
          if ((i >> m) % 2 == 1) {
            f1 = f3 > f1 ? f3 : f1;
          } else {
            f2 = f3 < f2 ? f3 : f2;
          }
        }
      }
      if (f1 > f2) {
        return false;
      }
      float f4 = x_array[0];
      float f5 = y_array[0];
      x_array[0] = (f4 + f1 * (x_array[1] - f4));
      y_array[0] = (f5 + f1 * (y_array[1] - f5));
      x_array[1] = (f4 + f2 * (x_array[1] - f4));
      y_array[1] = (f5 + f2 * (y_array[1] - f5));
      float f6;
      if (INTERPOLATE_RGB)
      {
        f6 = r_array[0];
        r_array[0] = (f6 + f1 * (r_array[1] - f6));
        r_array[1] = (f6 + f2 * (r_array[1] - f6));
        f6 = g_array[0];
        g_array[0] = (f6 + f1 * (g_array[1] - f6));
        g_array[1] = (f6 + f2 * (g_array[1] - f6));
        f6 = b_array[0];
        b_array[0] = (f6 + f1 * (b_array[1] - f6));
        b_array[1] = (f6 + f2 * (b_array[1] - f6));
      }
      if (INTERPOLATE_ALPHA)
      {
        f6 = a_array[0];
        a_array[0] = (f6 + f1 * (a_array[1] - f6));
        a_array[1] = (f6 + f2 * (a_array[1] - f6));
      }
    }
    return true;
  }
  
  private int lineClipCode(float paramFloat1, float paramFloat2)
  {
    int i = 0;
    int j = 0;
    int k = SCREEN_WIDTH1;
    int m = SCREEN_HEIGHT1;
    return (paramFloat2 < j ? 8 : 0) | ((int)paramFloat2 > m ? 4 : 0) | (paramFloat1 < i ? 2 : 0) | ((int)paramFloat1 > k ? 1 : 0);
  }
  
  private float lineSlope(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = SCREEN_WIDTH1;
    int m = SCREEN_HEIGHT1;
    switch (paramInt)
    {
    case 4: 
      return (j - paramFloat2) / (paramFloat4 - paramFloat2);
    case 3: 
      return (m - paramFloat2) / (paramFloat4 - paramFloat2);
    case 2: 
      return (i - paramFloat1) / (paramFloat3 - paramFloat1);
    case 1: 
      return (k - paramFloat1) / (paramFloat3 - paramFloat1);
    }
    return -1.0F;
  }
  
  private void drawPoint(int paramInt1, int paramInt2)
  {
    float f = m_z0;
    int i = paramInt2 * SCREEN_WIDTH + paramInt1;
    if (m_zbuffer == null)
    {
      m_pixels[i] = m_stroke;
    }
    else if (f <= m_zbuffer[i])
    {
      m_pixels[i] = m_stroke;
      m_zbuffer[i] = f;
    }
  }
  
  private void drawPoint_alpha(int paramInt1, int paramInt2)
  {
    int i = (int)a_array[0];
    int j = m_stroke & 0xFF0000;
    int k = m_stroke & 0xFF00;
    int m = m_stroke & 0xFF;
    float f = m_z0;
    int n = paramInt2 * SCREEN_WIDTH + paramInt1;
    if ((m_zbuffer == null) || (f <= m_zbuffer[n]))
    {
      int i1 = i >> 16;
      int i2 = m_pixels[n];
      int i3 = i2 & 0xFF00;
      int i4 = i2 & 0xFF;
      i2 &= 0xFF0000;
      i2 += ((j - i2) * i1 >> 8);
      i3 += ((k - i3) * i1 >> 8);
      i4 += ((m - i4) * i1 >> 8);
      m_pixels[n] = (0xFF000000 | i2 & 0xFF0000 | i3 & 0xFF00 | i4 & 0xFF);
      if (m_zbuffer != null) {
        m_zbuffer[n] = f;
      }
    }
  }
  
  private void drawLine_plain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    int j;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      j = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (j >> 16);
        m_pixels[i] = m_stroke;
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      j = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (j >> 16) * SCREEN_WIDTH + paramInt1;
        m_pixels[i] = m_stroke;
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_plain_alpha(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    int j = m_stroke & 0xFF0000;
    int k = m_stroke & 0xFF00;
    int m = m_stroke & 0xFF;
    int n = (int)m_a0;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      i1 = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (i1 >> 16);
        i2 = n >> 16;
        i3 = m_pixels[i];
        i4 = i3 & 0xFF00;
        i5 = i3 & 0xFF;
        i3 &= 0xFF0000;
        i3 += ((j - i3) * i2 >> 8);
        i4 += ((k - i4) * i2 >> 8);
        i5 += ((m - i5) * i2 >> 8);
        m_pixels[i] = (0xFF000000 | i3 & 0xFF0000 | i4 & 0xFF00 | i5 & 0xFF);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      i1 = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (i1 >> 16) * SCREEN_WIDTH + paramInt1;
        i2 = n >> 16;
        i3 = m_pixels[i];
        i4 = i3 & 0xFF00;
        i5 = i3 & 0xFF;
        i3 &= 0xFF0000;
        i3 += ((j - i3) * i2 >> 8);
        i4 += ((k - i4) * i2 >> 8);
        i5 += ((m - i5) * i2 >> 8);
        m_pixels[i] = (0xFF000000 | i3 & 0xFF0000 | i4 & 0xFF00 | i5 & 0xFF);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_color(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    int j = (int)m_r0;
    int k = (int)m_g0;
    int m = (int)m_b0;
    int n;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      n = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (n >> 16);
        m_pixels[i] = (0xFF000000 | j & 0xFF0000 | k >> 8 & 0xFF00 | m >> 16);
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      n = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (n >> 16) * SCREEN_WIDTH + paramInt1;
        m_pixels[i] = (0xFF000000 | j & 0xFF0000 | k >> 8 & 0xFF00 | m >> 16);
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_color_alpha(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    int j = (int)m_r0;
    int k = (int)m_g0;
    int m = (int)m_b0;
    int n = (int)m_a0;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      i1 = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (i1 >> 16);
        i2 = j & 0xFF0000;
        i3 = k >> 8 & 0xFF00;
        i4 = m >> 16;
        i5 = m_pixels[i];
        i6 = i5 & 0xFF00;
        i7 = i5 & 0xFF;
        i5 &= 0xFF0000;
        i8 = n >> 16;
        i5 += ((i2 - i5) * i8 >> 8);
        i6 += ((i3 - i6) * i8 >> 8);
        i7 += ((i4 - i7) * i8 >> 8);
        m_pixels[i] = (0xFF000000 | i5 & 0xFF0000 | i6 & 0xFF00 | i7 & 0xFF);
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      i1 = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (i1 >> 16) * SCREEN_WIDTH + paramInt1;
        i2 = j & 0xFF0000;
        i3 = k >> 8 & 0xFF00;
        i4 = m >> 16;
        i5 = m_pixels[i];
        i6 = i5 & 0xFF00;
        i7 = i5 & 0xFF;
        i5 &= 0xFF0000;
        i8 = n >> 16;
        i5 += ((i2 - i5) * i8 >> 8);
        i6 += ((i3 - i6) * i8 >> 8);
        i7 += ((i4 - i7) * i8 >> 8);
        m_pixels[i] = (0xFF000000 | i5 & 0xFF0000 | i6 & 0xFF00 | i7 & 0xFF);
        if (m_zbuffer != null) {
          m_zbuffer[i] = m_z0;
        }
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_plain_spatial(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    float f = m_z0;
    int j;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      j = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (j >> 16);
        if ((i < m_pixels.length) && (f <= m_zbuffer[i]))
        {
          m_pixels[i] = m_stroke;
          m_zbuffer[i] = f;
        }
        f += dz;
        j += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      j = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (j >> 16) * SCREEN_WIDTH + paramInt1;
        if ((i < m_pixels.length) && (f <= m_zbuffer[i]))
        {
          m_pixels[i] = m_stroke;
          m_zbuffer[i] = f;
        }
        f += dz;
        j += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_plain_alpha_spatial(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    float f = m_z0;
    int j = m_stroke & 0xFF0000;
    int k = m_stroke & 0xFF00;
    int m = m_stroke & 0xFF;
    int n = (int)m_a0;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      i1 = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (i1 >> 16);
        if ((i < m_pixels.length) && (f <= m_zbuffer[i]))
        {
          i2 = n >> 16;
          i3 = m_pixels[i];
          i4 = i3 & 0xFF00;
          i5 = i3 & 0xFF;
          i3 &= 0xFF0000;
          i3 += ((j - i3) * i2 >> 8);
          i4 += ((k - i4) * i2 >> 8);
          i5 += ((m - i5) * i2 >> 8);
          m_pixels[i] = (0xFF000000 | i3 & 0xFF0000 | i4 & 0xFF00 | i5 & 0xFF);
          m_zbuffer[i] = f;
        }
        f += dz;
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      i1 = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (i1 >> 16) * SCREEN_WIDTH + paramInt1;
        if ((i < m_pixels.length) && (f <= m_zbuffer[i]))
        {
          i2 = n >> 16;
          i3 = m_pixels[i];
          i4 = i3 & 0xFF00;
          i5 = i3 & 0xFF;
          i3 &= 0xFF0000;
          i3 += ((j - i3) * i2 >> 8);
          i4 += ((k - i4) * i2 >> 8);
          i5 += ((m - i5) * i2 >> 8);
          m_pixels[i] = (0xFF000000 | i3 & 0xFF0000 | i4 & 0xFF00 | i5 & 0xFF);
          m_zbuffer[i] = f;
        }
        f += dz;
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_color_spatial(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    float f = m_z0;
    int j = (int)m_r0;
    int k = (int)m_g0;
    int m = (int)m_b0;
    int n;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      n = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (n >> 16);
        if (f <= m_zbuffer[i])
        {
          m_pixels[i] = (0xFF000000 | j & 0xFF0000 | k >> 8 & 0xFF00 | m >> 16);
          m_zbuffer[i] = f;
        }
        f += dz;
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      n = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (n >> 16) * SCREEN_WIDTH + paramInt1;
        if (f <= m_zbuffer[i])
        {
          m_pixels[i] = (0xFF000000 | j & 0xFF0000 | k >> 8 & 0xFF00 | m >> 16);
          m_zbuffer[i] = f;
        }
        f += dz;
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n += paramInt3;
        paramInt1++;
      }
      return;
    }
  }
  
  private void drawLine_color_alpha_spatial(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = 0;
    float f = m_z0;
    int j = (int)m_r0;
    int k = (int)m_g0;
    int m = (int)m_b0;
    int n = (int)m_a0;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    if (paramBoolean)
    {
      paramInt4 += paramInt2;
      i1 = 32768 + (paramInt1 << 16);
      while (paramInt2 <= paramInt4)
      {
        i = paramInt2 * SCREEN_WIDTH + (i1 >> 16);
        if (f <= m_zbuffer[i])
        {
          i2 = j & 0xFF0000;
          i3 = k >> 8 & 0xFF00;
          i4 = m >> 16;
          i5 = m_pixels[i];
          i6 = i5 & 0xFF00;
          i7 = i5 & 0xFF;
          i5 &= 0xFF0000;
          i8 = n >> 16;
          i5 += ((i2 - i5) * i8 >> 8);
          i6 += ((i3 - i6) * i8 >> 8);
          i7 += ((i4 - i7) * i8 >> 8);
          m_pixels[i] = (0xFF000000 | i5 & 0xFF0000 | i6 & 0xFF00 | i7 & 0xFF);
          m_zbuffer[i] = f;
        }
        f += dz;
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt2++;
      }
    }
    else
    {
      paramInt4 += paramInt1;
      i1 = 32768 + (paramInt2 << 16);
      while (paramInt1 <= paramInt4)
      {
        i = (i1 >> 16) * SCREEN_WIDTH + paramInt1;
        if (f <= m_zbuffer[i])
        {
          i2 = j & 0xFF0000;
          i3 = k >> 8 & 0xFF00;
          i4 = m >> 16;
          i5 = m_pixels[i];
          i6 = i5 & 0xFF00;
          i7 = i5 & 0xFF;
          i5 &= 0xFF0000;
          i8 = n >> 16;
          i5 += ((i2 - i5) * i8 >> 8);
          i6 += ((i3 - i6) * i8 >> 8);
          i7 += ((i4 - i7) * i8 >> 8);
          m_pixels[i] = (0xFF000000 | i5 & 0xFF0000 | i6 & 0xFF00 | i7 & 0xFF);
          m_zbuffer[i] = f;
        }
        f += dz;
        j = (int)(j + dr);
        k = (int)(k + dg);
        m = (int)(m + db);
        n = (int)(n + da);
        i1 += paramInt3;
        paramInt1++;
      }
    }
  }
  
  private void drawLine_smooth(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int k = 0;
    float f = m_z0;
    int i1 = (int)m_r0;
    int i2 = (int)m_g0;
    int i3 = (int)m_b0;
    int i4 = (int)m_a0;
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    int i10;
    int i11;
    int m;
    if (paramBoolean)
    {
      i = paramInt1 << 16;
      j = paramInt2 << 16;
      n = paramInt4 + paramInt2;
      while (j >> 16 < n)
      {
        k = (j >> 16) * SCREEN_WIDTH + (i >> 16);
        i5 = i1 & 0xFF0000;
        i6 = i2 >> 8 & 0xFF00;
        i7 = i3 >> 16;
        if ((m_zbuffer == null) || (f <= m_zbuffer[k]))
        {
          i8 = ((i ^ 0xFFFFFFFF) >> 8 & 0xFF) * (i4 >> 16) >> 8;
          i9 = m_pixels[k];
          i10 = i9 & 0xFF00;
          i11 = i9 & 0xFF;
          i9 &= 0xFF0000;
          i9 += ((i5 - i9) * i8 >> 8);
          i10 += ((i6 - i10) * i8 >> 8);
          i11 += ((i7 - i11) * i8 >> 8);
          m_pixels[k] = (0xFF000000 | i9 & 0xFF0000 | i10 & 0xFF00 | i11 & 0xFF);
          if (m_zbuffer != null) {
            m_zbuffer[k] = f;
          }
        }
        m = (i >> 16) + 1;
        if (m >= SCREEN_WIDTH)
        {
          i += paramInt3;
          j += 65536;
        }
        else
        {
          k = (j >> 16) * SCREEN_WIDTH + m;
          if ((m_zbuffer == null) || (f <= m_zbuffer[k]))
          {
            i8 = (i >> 8 & 0xFF) * (i4 >> 16) >> 8;
            i9 = m_pixels[k];
            i10 = i9 & 0xFF00;
            i11 = i9 & 0xFF;
            i9 &= 0xFF0000;
            i9 += ((i5 - i9) * i8 >> 8);
            i10 += ((i6 - i10) * i8 >> 8);
            i11 += ((i7 - i11) * i8 >> 8);
            m_pixels[k] = (0xFF000000 | i9 & 0xFF0000 | i10 & 0xFF00 | i11 & 0xFF);
            if (m_zbuffer != null) {
              m_zbuffer[k] = f;
            }
          }
          i += paramInt3;
          j += 65536;
          f += dz;
          i1 = (int)(i1 + dr);
          i2 = (int)(i2 + dg);
          i3 = (int)(i3 + db);
          i4 = (int)(i4 + da);
        }
      }
    }
    int i = paramInt1 << 16;
    int j = paramInt2 << 16;
    int n = paramInt4 + paramInt1;
    while (i >> 16 < n)
    {
      k = (j >> 16) * SCREEN_WIDTH + (i >> 16);
      i5 = i1 & 0xFF0000;
      i6 = i2 >> 8 & 0xFF00;
      i7 = i3 >> 16;
      if ((m_zbuffer == null) || (f <= m_zbuffer[k]))
      {
        i8 = ((j ^ 0xFFFFFFFF) >> 8 & 0xFF) * (i4 >> 16) >> 8;
        i9 = m_pixels[k];
        i10 = i9 & 0xFF00;
        i11 = i9 & 0xFF;
        i9 &= 0xFF0000;
        i9 += ((i5 - i9) * i8 >> 8);
        i10 += ((i6 - i10) * i8 >> 8);
        i11 += ((i7 - i11) * i8 >> 8);
        m_pixels[k] = (0xFF000000 | i9 & 0xFF0000 | i10 & 0xFF00 | i11 & 0xFF);
        if (m_zbuffer != null) {
          m_zbuffer[k] = f;
        }
      }
      m = (j >> 16) + 1;
      if (m >= SCREEN_HEIGHT)
      {
        i += 65536;
        j += paramInt3;
      }
      else
      {
        k = m * SCREEN_WIDTH + (i >> 16);
        if ((m_zbuffer == null) || (f <= m_zbuffer[k]))
        {
          i8 = (j >> 8 & 0xFF) * (i4 >> 16) >> 8;
          i9 = m_pixels[k];
          i10 = i9 & 0xFF00;
          i11 = i9 & 0xFF;
          i9 &= 0xFF0000;
          i9 += ((i5 - i9) * i8 >> 8);
          i10 += ((i6 - i10) * i8 >> 8);
          i11 += ((i7 - i11) * i8 >> 8);
          m_pixels[k] = (0xFF000000 | i9 & 0xFF0000 | i10 & 0xFF00 | i11 & 0xFF);
          if (m_zbuffer != null) {
            m_zbuffer[k] = f;
          }
        }
        i += 65536;
        j += paramInt3;
        f += dz;
        i1 = (int)(i1 + dr);
        i2 = (int)(i2 + dg);
        i3 = (int)(i3 + db);
        i4 = (int)(i4 + da);
      }
    }
  }
}
