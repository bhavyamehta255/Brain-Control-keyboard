package processing.core;

public class PTriangle
  implements PConstants
{
  static final float PIXEL_CENTER = 0.5F;
  static final int R_GOURAUD = 1;
  static final int R_TEXTURE8 = 2;
  static final int R_TEXTURE24 = 4;
  static final int R_TEXTURE32 = 8;
  static final int R_ALPHA = 16;
  private int[] m_pixels;
  private int[] m_texture;
  private float[] m_zbuffer;
  private int SCREEN_WIDTH;
  private int SCREEN_HEIGHT;
  private int TEX_WIDTH;
  private int TEX_HEIGHT;
  private float F_TEX_WIDTH;
  private float F_TEX_HEIGHT;
  public boolean INTERPOLATE_UV;
  public boolean INTERPOLATE_RGB;
  public boolean INTERPOLATE_ALPHA;
  private static final int DEFAULT_INTERP_POWER = 3;
  private static int TEX_INTERP_POWER = 3;
  private float[] x_array = new float[3];
  private float[] y_array = new float[3];
  private float[] z_array = new float[3];
  private float[] camX = new float[3];
  private float[] camY = new float[3];
  private float[] camZ = new float[3];
  private float[] u_array = new float[3];
  private float[] v_array = new float[3];
  private float[] r_array = new float[3];
  private float[] g_array = new float[3];
  private float[] b_array = new float[3];
  private float[] a_array = new float[3];
  private int o0;
  private int o1;
  private int o2;
  private float r0;
  private float r1;
  private float r2;
  private float g0;
  private float g1;
  private float g2;
  private float b0;
  private float b1;
  private float b2;
  private float a0;
  private float a1;
  private float a2;
  private float u0;
  private float u1;
  private float u2;
  private float v0;
  private float v1;
  private float v2;
  private float dx2;
  private float dy0;
  private float dy1;
  private float dy2;
  private float dz0;
  private float dz2;
  private float du0;
  private float du2;
  private float dv0;
  private float dv2;
  private float dr0;
  private float dr2;
  private float dg0;
  private float dg2;
  private float db0;
  private float db2;
  private float da0;
  private float da2;
  private float uleft;
  private float vleft;
  private float uleftadd;
  private float vleftadd;
  private float xleft;
  private float xrght;
  private float xadd1;
  private float xadd2;
  private float zleft;
  private float zleftadd;
  private float rleft;
  private float gleft;
  private float bleft;
  private float aleft;
  private float rleftadd;
  private float gleftadd;
  private float bleftadd;
  private float aleftadd;
  private float dta;
  private float temp;
  private float width;
  private int iuadd;
  private int ivadd;
  private int iradd;
  private int igadd;
  private int ibadd;
  private int iaadd;
  private float izadd;
  private int m_fill;
  public int m_drawFlags;
  private PGraphics3D parent;
  private boolean noDepthTest;
  private boolean m_culling;
  private boolean m_singleRight;
  private boolean m_bilinear = true;
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
  private float newax;
  private float newbx;
  private float newcx;
  private boolean firstSegment;
  
  public PTriangle(PGraphics3D paramPGraphics3D)
  {
    parent = paramPGraphics3D;
    reset();
  }
  
  public void reset()
  {
    SCREEN_WIDTH = parent.width;
    SCREEN_HEIGHT = parent.height;
    m_pixels = parent.pixels;
    m_zbuffer = parent.zbuffer;
    noDepthTest = parent.hints[4];
    INTERPOLATE_UV = false;
    INTERPOLATE_RGB = false;
    INTERPOLATE_ALPHA = false;
    m_texture = null;
    m_drawFlags = 0;
  }
  
  public void setCulling(boolean paramBoolean)
  {
    m_culling = paramBoolean;
  }
  
  public void setVertices(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    x_array[0] = paramFloat1;
    x_array[1] = paramFloat4;
    x_array[2] = paramFloat7;
    y_array[0] = paramFloat2;
    y_array[1] = paramFloat5;
    y_array[2] = paramFloat8;
    z_array[0] = paramFloat3;
    z_array[1] = paramFloat6;
    z_array[2] = paramFloat9;
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
  
  public void setUV(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    u_array[0] = ((paramFloat1 * F_TEX_WIDTH + 0.5F) * 65536.0F);
    u_array[1] = ((paramFloat3 * F_TEX_WIDTH + 0.5F) * 65536.0F);
    u_array[2] = ((paramFloat5 * F_TEX_WIDTH + 0.5F) * 65536.0F);
    v_array[0] = ((paramFloat2 * F_TEX_HEIGHT + 0.5F) * 65536.0F);
    v_array[1] = ((paramFloat4 * F_TEX_HEIGHT + 0.5F) * 65536.0F);
    v_array[2] = ((paramFloat6 * F_TEX_HEIGHT + 0.5F) * 65536.0F);
  }
  
  public void setIntensities(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    if ((paramFloat4 != 1.0F) || (paramFloat8 != 1.0F) || (paramFloat12 != 1.0F))
    {
      INTERPOLATE_ALPHA = true;
      a_array[0] = ((paramFloat4 * 253.0F + 1.0F) * 65536.0F);
      a_array[1] = ((paramFloat8 * 253.0F + 1.0F) * 65536.0F);
      a_array[2] = ((paramFloat12 * 253.0F + 1.0F) * 65536.0F);
      m_drawFlags |= 0x10;
    }
    else
    {
      INTERPOLATE_ALPHA = false;
      m_drawFlags &= 0xFFFFFFEF;
    }
    if ((paramFloat1 != paramFloat5) || (paramFloat5 != paramFloat9))
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else if ((paramFloat2 != paramFloat6) || (paramFloat6 != paramFloat10))
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else if ((paramFloat3 != paramFloat7) || (paramFloat7 != paramFloat11))
    {
      INTERPOLATE_RGB = true;
      m_drawFlags |= 0x1;
    }
    else
    {
      m_drawFlags &= 0xFFFFFFFE;
    }
    r_array[0] = ((paramFloat1 * 253.0F + 1.0F) * 65536.0F);
    r_array[1] = ((paramFloat5 * 253.0F + 1.0F) * 65536.0F);
    r_array[2] = ((paramFloat9 * 253.0F + 1.0F) * 65536.0F);
    g_array[0] = ((paramFloat2 * 253.0F + 1.0F) * 65536.0F);
    g_array[1] = ((paramFloat6 * 253.0F + 1.0F) * 65536.0F);
    g_array[2] = ((paramFloat10 * 253.0F + 1.0F) * 65536.0F);
    b_array[0] = ((paramFloat3 * 253.0F + 1.0F) * 65536.0F);
    b_array[1] = ((paramFloat7 * 253.0F + 1.0F) * 65536.0F);
    b_array[2] = ((paramFloat11 * 253.0F + 1.0F) * 65536.0F);
    m_fill = (0xFF000000 | (int)(255.0F * paramFloat1) << 16 | (int)(255.0F * paramFloat2) << 8 | (int)(255.0F * paramFloat3));
  }
  
  public void setTexture(PImage paramPImage)
  {
    m_texture = pixels;
    TEX_WIDTH = width;
    TEX_HEIGHT = height;
    F_TEX_WIDTH = (TEX_WIDTH - 1);
    F_TEX_HEIGHT = (TEX_HEIGHT - 1);
    INTERPOLATE_UV = true;
    if (format == 2) {
      m_drawFlags |= 0x8;
    } else if (format == 1) {
      m_drawFlags |= 0x4;
    } else if (format == 4) {
      m_drawFlags |= 0x2;
    }
  }
  
  public void setUV(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (m_bilinear)
    {
      u_array[0] = (paramArrayOfFloat1[0] * F_TEX_WIDTH * 65500.0F);
      u_array[1] = (paramArrayOfFloat1[1] * F_TEX_WIDTH * 65500.0F);
      u_array[2] = (paramArrayOfFloat1[2] * F_TEX_WIDTH * 65500.0F);
      v_array[0] = (paramArrayOfFloat2[0] * F_TEX_HEIGHT * 65500.0F);
      v_array[1] = (paramArrayOfFloat2[1] * F_TEX_HEIGHT * 65500.0F);
      v_array[2] = (paramArrayOfFloat2[2] * F_TEX_HEIGHT * 65500.0F);
    }
    else
    {
      u_array[0] = (paramArrayOfFloat1[0] * TEX_WIDTH * 65500.0F);
      u_array[1] = (paramArrayOfFloat1[1] * TEX_WIDTH * 65500.0F);
      u_array[2] = (paramArrayOfFloat1[2] * TEX_WIDTH * 65500.0F);
      v_array[0] = (paramArrayOfFloat2[0] * TEX_HEIGHT * 65500.0F);
      v_array[1] = (paramArrayOfFloat2[1] * TEX_HEIGHT * 65500.0F);
      v_array[2] = (paramArrayOfFloat2[2] * TEX_HEIGHT * 65500.0F);
    }
  }
  
  public void render()
  {
    float f7 = y_array[0];
    float f8 = y_array[1];
    float f9 = y_array[2];
    firstSegment = true;
    float f1;
    if (m_culling)
    {
      f1 = x_array[0];
      if ((x_array[2] - f1) * (f8 - f7) < (x_array[1] - f1) * (f9 - f7)) {
        return;
      }
    }
    if (f7 < f8)
    {
      if (f9 < f8)
      {
        if (f9 < f7)
        {
          o0 = 2;
          o1 = 0;
          o2 = 1;
        }
        else
        {
          o0 = 0;
          o1 = 2;
          o2 = 1;
        }
      }
      else
      {
        o0 = 0;
        o1 = 1;
        o2 = 2;
      }
    }
    else if (f9 > f8)
    {
      if (f9 < f7)
      {
        o0 = 1;
        o1 = 2;
        o2 = 0;
      }
      else
      {
        o0 = 1;
        o1 = 0;
        o2 = 2;
      }
    }
    else
    {
      o0 = 2;
      o1 = 1;
      o2 = 0;
    }
    f7 = y_array[o0];
    int i = (int)(f7 + 0.5F);
    if (i > SCREEN_HEIGHT) {
      return;
    }
    if (i < 0) {
      i = 0;
    }
    f9 = y_array[o2];
    int j = (int)(f9 + 0.5F);
    if (j < 0) {
      return;
    }
    if (j > SCREEN_HEIGHT) {
      j = SCREEN_HEIGHT;
    }
    if (j > i)
    {
      f1 = x_array[o0];
      float f2 = x_array[o1];
      float f3 = x_array[o2];
      f8 = y_array[o1];
      int k = (int)(f8 + 0.5F);
      if (k < 0) {
        k = 0;
      }
      if (k > SCREEN_HEIGHT) {
        k = SCREEN_HEIGHT;
      }
      dx2 = (f3 - f1);
      dy0 = (f8 - f7);
      dy2 = (f9 - f7);
      xadd2 = (dx2 / dy2);
      temp = (dy0 / dy2);
      width = (temp * dx2 + f1 - f2);
      if (INTERPOLATE_ALPHA)
      {
        a0 = a_array[o0];
        a1 = a_array[o1];
        a2 = a_array[o2];
        da0 = (a1 - a0);
        da2 = (a2 - a0);
        iaadd = ((int)((temp * da2 - da0) / width));
      }
      if (INTERPOLATE_RGB)
      {
        r0 = r_array[o0];
        r1 = r_array[o1];
        r2 = r_array[o2];
        g0 = g_array[o0];
        g1 = g_array[o1];
        g2 = g_array[o2];
        b0 = b_array[o0];
        b1 = b_array[o1];
        b2 = b_array[o2];
        dr0 = (r1 - r0);
        dg0 = (g1 - g0);
        db0 = (b1 - b0);
        dr2 = (r2 - r0);
        dg2 = (g2 - g0);
        db2 = (b2 - b0);
        iradd = ((int)((temp * dr2 - dr0) / width));
        igadd = ((int)((temp * dg2 - dg0) / width));
        ibadd = ((int)((temp * db2 - db0) / width));
      }
      if (INTERPOLATE_UV)
      {
        u0 = u_array[o0];
        u1 = u_array[o1];
        u2 = u_array[o2];
        v0 = v_array[o0];
        v1 = v_array[o1];
        v2 = v_array[o2];
        du0 = (u1 - u0);
        dv0 = (v1 - v0);
        du2 = (u2 - u0);
        dv2 = (v2 - v0);
        iuadd = ((int)((temp * du2 - du0) / width));
        ivadd = ((int)((temp * dv2 - dv0) / width));
      }
      float f4 = z_array[o0];
      float f5 = z_array[o1];
      float f6 = z_array[o2];
      dz0 = (f5 - f4);
      dz2 = (f6 - f4);
      izadd = ((temp * dz2 - dz0) / width);
      if (k > i)
      {
        dta = (i + 0.5F - f7);
        xadd1 = ((f2 - f1) / dy0);
        if (xadd2 > xadd1)
        {
          xleft = (f1 + dta * xadd1);
          xrght = (f1 + dta * xadd2);
          zleftadd = (dz0 / dy0);
          zleft = (dta * zleftadd + f4);
          if (INTERPOLATE_UV)
          {
            uleftadd = (du0 / dy0);
            vleftadd = (dv0 / dy0);
            uleft = (dta * uleftadd + u0);
            vleft = (dta * vleftadd + v0);
          }
          if (INTERPOLATE_RGB)
          {
            rleftadd = (dr0 / dy0);
            gleftadd = (dg0 / dy0);
            bleftadd = (db0 / dy0);
            rleft = (dta * rleftadd + r0);
            gleft = (dta * gleftadd + g0);
            bleft = (dta * bleftadd + b0);
          }
          if (INTERPOLATE_ALPHA)
          {
            aleftadd = (da0 / dy0);
            aleft = (dta * aleftadd + a0);
            if (m_drawFlags == 16) {
              drawsegment_plain_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 17) {
              drawsegment_gouraud_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 18) {
              drawsegment_texture8_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 20) {
              drawsegment_texture24_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 24) {
              drawsegment_texture32_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 19) {
              drawsegment_gouraud_texture8_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 21) {
              drawsegment_gouraud_texture24_alpha(xadd1, xadd2, i, k);
            } else if (m_drawFlags == 25) {
              drawsegment_gouraud_texture32_alpha(xadd1, xadd2, i, k);
            }
          }
          else if (m_drawFlags == 0)
          {
            drawsegment_plain(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 1)
          {
            drawsegment_gouraud(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 2)
          {
            drawsegment_texture8(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 4)
          {
            drawsegment_texture24(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 8)
          {
            drawsegment_texture32(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 3)
          {
            drawsegment_gouraud_texture8(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 5)
          {
            drawsegment_gouraud_texture24(xadd1, xadd2, i, k);
          }
          else if (m_drawFlags == 9)
          {
            drawsegment_gouraud_texture32(xadd1, xadd2, i, k);
          }
          m_singleRight = true;
        }
        else
        {
          xleft = (f1 + dta * xadd2);
          xrght = (f1 + dta * xadd1);
          zleftadd = (dz2 / dy2);
          zleft = (dta * zleftadd + f4);
          if (INTERPOLATE_UV)
          {
            uleftadd = (du2 / dy2);
            vleftadd = (dv2 / dy2);
            uleft = (dta * uleftadd + u0);
            vleft = (dta * vleftadd + v0);
          }
          if (INTERPOLATE_RGB)
          {
            rleftadd = (dr2 / dy2);
            gleftadd = (dg2 / dy2);
            bleftadd = (db2 / dy2);
            rleft = (dta * rleftadd + r0);
            gleft = (dta * gleftadd + g0);
            bleft = (dta * bleftadd + b0);
          }
          if (INTERPOLATE_ALPHA)
          {
            aleftadd = (da2 / dy2);
            aleft = (dta * aleftadd + a0);
            if (m_drawFlags == 16) {
              drawsegment_plain_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 17) {
              drawsegment_gouraud_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 18) {
              drawsegment_texture8_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 20) {
              drawsegment_texture24_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 24) {
              drawsegment_texture32_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 19) {
              drawsegment_gouraud_texture8_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 21) {
              drawsegment_gouraud_texture24_alpha(xadd2, xadd1, i, k);
            } else if (m_drawFlags == 25) {
              drawsegment_gouraud_texture32_alpha(xadd2, xadd1, i, k);
            }
          }
          else if (m_drawFlags == 0)
          {
            drawsegment_plain(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 1)
          {
            drawsegment_gouraud(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 2)
          {
            drawsegment_texture8(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 4)
          {
            drawsegment_texture24(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 8)
          {
            drawsegment_texture32(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 3)
          {
            drawsegment_gouraud_texture8(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 5)
          {
            drawsegment_gouraud_texture24(xadd2, xadd1, i, k);
          }
          else if (m_drawFlags == 9)
          {
            drawsegment_gouraud_texture32(xadd2, xadd1, i, k);
          }
          m_singleRight = false;
        }
        if (j == k) {
          return;
        }
        dy1 = (f9 - f8);
        xadd1 = ((f3 - f2) / dy1);
      }
      else
      {
        dy1 = (f9 - f8);
        xadd1 = ((f3 - f2) / dy1);
        if (xadd2 < xadd1)
        {
          xrght = ((k + 0.5F - f7) * xadd2 + f1);
          m_singleRight = true;
        }
        else
        {
          dta = (k + 0.5F - f7);
          xleft = (dta * xadd2 + f1);
          zleftadd = (dz2 / dy2);
          zleft = (dta * zleftadd + f4);
          if (INTERPOLATE_UV)
          {
            uleftadd = (du2 / dy2);
            vleftadd = (dv2 / dy2);
            uleft = (dta * uleftadd + u0);
            vleft = (dta * vleftadd + v0);
          }
          if (INTERPOLATE_RGB)
          {
            rleftadd = (dr2 / dy2);
            gleftadd = (dg2 / dy2);
            bleftadd = (db2 / dy2);
            rleft = (dta * rleftadd + r0);
            gleft = (dta * gleftadd + g0);
            bleft = (dta * bleftadd + b0);
          }
          if (INTERPOLATE_ALPHA)
          {
            aleftadd = (da2 / dy2);
            aleft = (dta * aleftadd + a0);
          }
          m_singleRight = false;
        }
      }
      if (m_singleRight)
      {
        dta = (k + 0.5F - f8);
        xleft = (dta * xadd1 + f2);
        zleftadd = ((f6 - f5) / dy1);
        zleft = (dta * zleftadd + f5);
        if (INTERPOLATE_UV)
        {
          uleftadd = ((u2 - u1) / dy1);
          vleftadd = ((v2 - v1) / dy1);
          uleft = (dta * uleftadd + u1);
          vleft = (dta * vleftadd + v1);
        }
        if (INTERPOLATE_RGB)
        {
          rleftadd = ((r2 - r1) / dy1);
          gleftadd = ((g2 - g1) / dy1);
          bleftadd = ((b2 - b1) / dy1);
          rleft = (dta * rleftadd + r1);
          gleft = (dta * gleftadd + g1);
          bleft = (dta * bleftadd + b1);
        }
        if (INTERPOLATE_ALPHA)
        {
          aleftadd = ((a2 - a1) / dy1);
          aleft = (dta * aleftadd + a1);
          if (m_drawFlags == 16) {
            drawsegment_plain_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 17) {
            drawsegment_gouraud_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 18) {
            drawsegment_texture8_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 20) {
            drawsegment_texture24_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 24) {
            drawsegment_texture32_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 19) {
            drawsegment_gouraud_texture8_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 21) {
            drawsegment_gouraud_texture24_alpha(xadd1, xadd2, k, j);
          } else if (m_drawFlags == 25) {
            drawsegment_gouraud_texture32_alpha(xadd1, xadd2, k, j);
          }
        }
        else if (m_drawFlags == 0)
        {
          drawsegment_plain(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 1)
        {
          drawsegment_gouraud(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 2)
        {
          drawsegment_texture8(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 4)
        {
          drawsegment_texture24(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 8)
        {
          drawsegment_texture32(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 3)
        {
          drawsegment_gouraud_texture8(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 5)
        {
          drawsegment_gouraud_texture24(xadd1, xadd2, k, j);
        }
        else if (m_drawFlags == 9)
        {
          drawsegment_gouraud_texture32(xadd1, xadd2, k, j);
        }
      }
      else
      {
        xrght = ((k + 0.5F - f8) * xadd1 + f2);
        if (INTERPOLATE_ALPHA)
        {
          if (m_drawFlags == 16) {
            drawsegment_plain_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 17) {
            drawsegment_gouraud_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 18) {
            drawsegment_texture8_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 20) {
            drawsegment_texture24_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 24) {
            drawsegment_texture32_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 19) {
            drawsegment_gouraud_texture8_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 21) {
            drawsegment_gouraud_texture24_alpha(xadd2, xadd1, k, j);
          } else if (m_drawFlags == 25) {
            drawsegment_gouraud_texture32_alpha(xadd2, xadd1, k, j);
          }
        }
        else if (m_drawFlags == 0) {
          drawsegment_plain(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 1) {
          drawsegment_gouraud(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 2) {
          drawsegment_texture8(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 4) {
          drawsegment_texture24(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 8) {
          drawsegment_texture32(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 3) {
          drawsegment_gouraud_texture8(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 5) {
          drawsegment_gouraud_texture24(xadd2, xadd1, k, j);
        } else if (m_drawFlags == 9) {
          drawsegment_gouraud_texture32(xadd2, xadd1, k, j);
        }
      }
    }
  }
  
  private boolean precomputeAccurateTexturing()
  {
    float f1 = 65500.0F;
    float f2 = 65500.0F;
    if (firstSegment)
    {
      PMatrix3D localPMatrix3D = new PMatrix3D(u_array[o0] / f1, v_array[o0] / f2, 1.0F, 0.0F, u_array[o1] / f1, v_array[o1] / f2, 1.0F, 0.0F, u_array[o2] / f1, v_array[o2] / f2, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
      if (!localPMatrix3D.invert()) {
        return false;
      }
      float f3 = m00 * camX[o0] + m01 * camX[o1] + m02 * camX[o2];
      float f4 = m10 * camX[o0] + m11 * camX[o1] + m12 * camX[o2];
      float f5 = m20 * camX[o0] + m21 * camX[o1] + m22 * camX[o2];
      float f6 = m00 * camY[o0] + m01 * camY[o1] + m02 * camY[o2];
      float f7 = m10 * camY[o0] + m11 * camY[o1] + m12 * camY[o2];
      float f8 = m20 * camY[o0] + m21 * camY[o1] + m22 * camY[o2];
      float f9 = -(m00 * camZ[o0] + m01 * camZ[o1] + m02 * camZ[o2]);
      float f10 = -(m10 * camZ[o0] + m11 * camZ[o1] + m12 * camZ[o2]);
      float f11 = -(m20 * camZ[o0] + m21 * camZ[o1] + m22 * camZ[o2]);
      float f12 = f5;
      float f13 = f8;
      float f14 = f11;
      float f15 = f3 * TEX_WIDTH + f5;
      float f16 = f6 * TEX_WIDTH + f8;
      float f17 = f9 * TEX_WIDTH + f11;
      float f18 = f4 * TEX_HEIGHT + f5;
      float f19 = f7 * TEX_HEIGHT + f8;
      float f20 = f10 * TEX_HEIGHT + f11;
      float f21 = f15 - f5;
      float f22 = f16 - f8;
      float f23 = f17 - f11;
      float f24 = f18 - f5;
      float f25 = f19 - f8;
      float f26 = f20 - f11;
      ax = ((f13 * f26 - f14 * f25) * TEX_WIDTH);
      ay = ((f14 * f24 - f12 * f26) * TEX_WIDTH);
      az = ((f12 * f25 - f13 * f24) * TEX_WIDTH);
      bx = ((f22 * f14 - f23 * f13) * TEX_HEIGHT);
      by = ((f23 * f12 - f21 * f14) * TEX_HEIGHT);
      bz = ((f21 * f13 - f22 * f12) * TEX_HEIGHT);
      cx = (f25 * f23 - f26 * f22);
      cy = (f26 * f21 - f24 * f23);
      cz = (f24 * f22 - f25 * f21);
    }
    nearPlaneWidth = (parent.rightScreen - parent.leftScreen);
    nearPlaneHeight = (parent.topScreen - parent.bottomScreen);
    nearPlaneDepth = parent.nearPlane;
    xmult = (nearPlaneWidth / SCREEN_WIDTH);
    ymult = (nearPlaneHeight / SCREEN_HEIGHT);
    newax = (ax * xmult);
    newbx = (bx * xmult);
    newcx = (cx * xmult);
    return true;
  }
  
  public static void setInterpPower(int paramInt)
  {
    TEX_INTERP_POWER = paramInt;
  }
  
  private void drawsegment_plain(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    while (paramInt1 < paramInt2)
    {
      int i = (int)(xleft + 0.5F);
      if (i < 0) {
        i = 0;
      }
      int j = (int)(xrght + 0.5F);
      if (j > SCREEN_WIDTH) {
        j = SCREEN_WIDTH;
      }
      float f1 = i + 0.5F - xleft;
      float f2 = izadd * f1 + zleft;
      i += paramInt1;
      j += paramInt1;
      while (i < j)
      {
        if ((noDepthTest) || (f2 <= m_zbuffer[i]))
        {
          m_zbuffer[i] = f2;
          m_pixels[i] = m_fill;
        }
        f2 += izadd;
        i++;
      }
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_plain_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    int i = m_fill & 0xFF0000;
    int j = m_fill & 0xFF00;
    int k = m_fill & 0xFF;
    float f1 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int m = (int)(xleft + 0.5F);
      if (m < 0) {
        m = 0;
      }
      int n = (int)(xrght + 0.5F);
      if (n > SCREEN_WIDTH) {
        n = SCREEN_WIDTH;
      }
      float f2 = m + 0.5F - xleft;
      float f3 = izadd * f2 + zleft;
      int i1 = (int)(f1 * f2 + aleft);
      m += paramInt1;
      n += paramInt1;
      while (m < n)
      {
        if ((noDepthTest) || (f3 <= m_zbuffer[m]))
        {
          int i2 = i1 >> 16;
          int i3 = m_pixels[m];
          int i4 = i3 & 0xFF00;
          int i5 = i3 & 0xFF;
          i3 &= 0xFF0000;
          i3 += ((i - i3) * i2 >> 8);
          i4 += ((j - i4) * i2 >> 8);
          i5 += ((k - i5) * i2 >> 8);
          m_pixels[m] = (0xFF000000 | i3 & 0xFF0000 | i4 & 0xFF00 | i5 & 0xFF);
        }
        f3 += izadd;
        i1 += iaadd;
        m++;
      }
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    float f1 = iradd;
    float f2 = igadd;
    float f3 = ibadd;
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    while (paramInt1 < paramInt2)
    {
      int i = (int)(xleft + 0.5F);
      if (i < 0) {
        i = 0;
      }
      int j = (int)(xrght + 0.5F);
      if (j > SCREEN_WIDTH) {
        j = SCREEN_WIDTH;
      }
      float f4 = i + 0.5F - xleft;
      int k = (int)(f1 * f4 + rleft);
      int m = (int)(f2 * f4 + gleft);
      int n = (int)(f3 * f4 + bleft);
      float f5 = izadd * f4 + zleft;
      i += paramInt1;
      j += paramInt1;
      while (i < j)
      {
        if ((noDepthTest) || (f5 <= m_zbuffer[i]))
        {
          m_zbuffer[i] = f5;
          m_pixels[i] = (0xFF000000 | k & 0xFF0000 | m >> 8 & 0xFF00 | n >> 16);
        }
        k += iradd;
        m += igadd;
        n += ibadd;
        f5 += izadd;
        i++;
      }
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f1 = iradd;
    float f2 = igadd;
    float f3 = ibadd;
    float f4 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i = (int)(xleft + 0.5F);
      if (i < 0) {
        i = 0;
      }
      int j = (int)(xrght + 0.5F);
      if (j > SCREEN_WIDTH) {
        j = SCREEN_WIDTH;
      }
      float f5 = i + 0.5F - xleft;
      int k = (int)(f1 * f5 + rleft);
      int m = (int)(f2 * f5 + gleft);
      int n = (int)(f3 * f5 + bleft);
      int i1 = (int)(f4 * f5 + aleft);
      float f6 = izadd * f5 + zleft;
      i += paramInt1;
      j += paramInt1;
      while (i < j)
      {
        if ((noDepthTest) || (f6 <= m_zbuffer[i]))
        {
          int i2 = k & 0xFF0000;
          int i3 = m >> 8 & 0xFF00;
          int i4 = n >> 16;
          int i5 = m_pixels[i];
          int i6 = i5 & 0xFF0000;
          int i7 = i5 & 0xFF00;
          i5 &= 0xFF;
          int i8 = i1 >> 16;
          m_pixels[i] = (0xFF000000 | i6 + ((i2 - i6) * i8 >> 8) & 0xFF0000 | i7 + ((i3 - i7) * i8 >> 8) & 0xFF00 | i5 + ((i4 - i5) * i8 >> 8) & 0xFF);
        }
        k += iradd;
        m += igadd;
        n += ibadd;
        i1 += iaadd;
        f6 += izadd;
        i++;
      }
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      aleft += aleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_texture8(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    int i1 = m_fill & 0xFF0000;
    int i2 = m_fill & 0xFF00;
    int i3 = m_fill & 0xFF;
    while (paramInt1 < paramInt2)
    {
      int i4 = (int)(xleft + 0.5F);
      if (i4 < 0) {
        i4 = 0;
      }
      int i5 = i4;
      int i6 = (int)(xrght + 0.5F);
      if (i6 > SCREEN_WIDTH) {
        i6 = SCREEN_WIDTH;
      }
      float f9 = i4 + 0.5F - xleft;
      int i7 = (int)(f7 * f9 + uleft);
      int i8 = (int)(f8 * f9 + vleft);
      float f10 = izadd * f9 + zleft;
      i4 += paramInt1;
      i6 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i5 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i9 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i10 = 0;
      int i11 = 0;
      int i12 = 0;
      float f11 = 0.0F;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      int i15;
      float f15;
      if ((k != 0) && (i9 != 0))
      {
        int i13 = (i6 - i4 - 1) % n;
        i15 = n - i13;
        float f16 = i13 / n;
        float f17 = i15 / n;
        i10 = i15;
        float f18 = f4 - f17 * newax;
        float f19 = f5 - f17 * newbx;
        float f20 = f6 - f17 * newcx;
        float f21 = 65536.0F / f20;
        f13 = f18 * f21;
        f14 = f19 * f21;
        f4 += f16 * newax;
        f5 += f16 * newbx;
        f6 += f16 * newcx;
        f21 = 65536.0F / f6;
        f11 = f4 * f21;
        f12 = f5 * f21;
        i11 = (int)(f11 - f13) >> m;
        i12 = (int)(f12 - f14) >> m;
        i7 = (int)f13 + (i15 - 1) * i11;
        i8 = (int)f14 + (i15 - 1) * i12;
      }
      else
      {
        f15 = 65536.0F / f6;
        f11 = f4 * f15;
        f12 = f5 * f15;
      }
      while (i4 < i6)
      {
        if (k != 0)
        {
          if (i10 == n) {
            i10 = 0;
          }
          if (i10 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f15 = 65536.0F / f6;
            f13 = f11;
            f14 = f12;
            f11 = f4 * f15;
            f12 = f5 * f15;
            i7 = (int)f13;
            i8 = (int)f14;
            i11 = (int)(f11 - f13) >> m;
            i12 = (int)(f12 - f14) >> m;
          }
          else
          {
            i7 += i11;
            i8 += i12;
          }
          i10++;
        }
        try
        {
          if ((noDepthTest) || (f10 <= m_zbuffer[i4]))
          {
            int i14;
            if (m_bilinear)
            {
              i15 = (i8 >> 16) * TEX_WIDTH + (i7 >> 16);
              i16 = i7 & 0xFFFF;
              i14 = m_texture[i15] & 0xFF;
              i17 = m_texture[(i15 + 1)] & 0xFF;
              if (i15 < j) {
                i15 += TEX_WIDTH;
              }
              int i18 = m_texture[i15] & 0xFF;
              int i19 = m_texture[(i15 + 1)] & 0xFF;
              i14 += ((i17 - i14) * i16 >> 16);
              i18 += ((i19 - i18) * i16 >> 16);
              i14 += ((i18 - i14) * (i8 & 0xFFFF) >> 16);
            }
            else
            {
              i14 = m_texture[((i8 >> 16) * TEX_WIDTH + (i7 >> 16))] & 0xFF;
            }
            i15 = m_pixels[i4];
            int i16 = i15 & 0xFF00;
            int i17 = i15 & 0xFF;
            i15 &= 0xFF0000;
            m_pixels[i4] = (0xFF000000 | i15 + ((i1 - i15) * i14 >> 8) & 0xFF0000 | i16 + ((i2 - i16) * i14 >> 8) & 0xFF00 | i17 + ((i3 - i17) * i14 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i5++;
        if (k == 0)
        {
          i7 += iuadd;
          i8 += ivadd;
        }
        f10 += izadd;
        i4++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_texture8_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iaadd;
    int i1 = m_fill & 0xFF0000;
    int i2 = m_fill & 0xFF00;
    int i3 = m_fill & 0xFF;
    while (paramInt1 < paramInt2)
    {
      int i4 = (int)(xleft + 0.5F);
      if (i4 < 0) {
        i4 = 0;
      }
      int i5 = i4;
      int i6 = (int)(xrght + 0.5F);
      if (i6 > SCREEN_WIDTH) {
        i6 = SCREEN_WIDTH;
      }
      float f10 = i4 + 0.5F - xleft;
      int i7 = (int)(f7 * f10 + uleft);
      int i8 = (int)(f8 * f10 + vleft);
      int i9 = (int)(f9 * f10 + aleft);
      float f11 = izadd * f10 + zleft;
      i4 += paramInt1;
      i6 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i5 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      float f15 = 0.0F;
      int i16;
      float f16;
      if ((k != 0) && (i10 != 0))
      {
        int i14 = (i6 - i4 - 1) % n;
        i16 = n - i14;
        float f17 = i14 / n;
        float f18 = i16 / n;
        i11 = i16;
        float f19 = f4 - f18 * newax;
        float f20 = f5 - f18 * newbx;
        float f21 = f6 - f18 * newcx;
        float f22 = 65536.0F / f21;
        f14 = f19 * f22;
        f15 = f20 * f22;
        f4 += f17 * newax;
        f5 += f17 * newbx;
        f6 += f17 * newcx;
        f22 = 65536.0F / f6;
        f12 = f4 * f22;
        f13 = f5 * f22;
        i12 = (int)(f12 - f14) >> m;
        i13 = (int)(f13 - f15) >> m;
        i7 = (int)f14 + (i16 - 1) * i12;
        i8 = (int)f15 + (i16 - 1) * i13;
      }
      else
      {
        f16 = 65536.0F / f6;
        f12 = f4 * f16;
        f13 = f5 * f16;
      }
      while (i4 < i6)
      {
        if (k != 0)
        {
          if (i11 == n) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f16 = 65536.0F / f6;
            f14 = f12;
            f15 = f13;
            f12 = f4 * f16;
            f13 = f5 * f16;
            i7 = (int)f14;
            i8 = (int)f15;
            i12 = (int)(f12 - f14) >> m;
            i13 = (int)(f13 - f15) >> m;
          }
          else
          {
            i7 += i12;
            i8 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f11 <= m_zbuffer[i4]))
          {
            if (m_bilinear)
            {
              i16 = (i8 >> 16) * TEX_WIDTH + (i7 >> 16);
              i17 = i7 & 0xFFFF;
              i15 = m_texture[i16] & 0xFF;
              i18 = m_texture[(i16 + 1)] & 0xFF;
              if (i16 < j) {
                i16 += TEX_WIDTH;
              }
              int i19 = m_texture[i16] & 0xFF;
              int i20 = m_texture[(i16 + 1)] & 0xFF;
              i15 += ((i18 - i15) * i17 >> 16);
              i19 += ((i20 - i19) * i17 >> 16);
              i15 += ((i19 - i15) * (i8 & 0xFFFF) >> 16);
            }
            else
            {
              i15 = m_texture[((i8 >> 16) * TEX_WIDTH + (i7 >> 16))] & 0xFF;
            }
            int i15 = i15 * (i9 >> 16) >> 8;
            i16 = m_pixels[i4];
            int i17 = i16 & 0xFF00;
            int i18 = i16 & 0xFF;
            i16 &= 0xFF0000;
            m_pixels[i4] = (0xFF000000 | i16 + ((i1 - i16) * i15 >> 8) & 0xFF0000 | i17 + ((i2 - i17) * i15 >> 8) & 0xFF00 | i18 + ((i3 - i18) * i15 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i5++;
        if (k == 0)
        {
          i7 += iuadd;
          i8 += ivadd;
        }
        f11 += izadd;
        i9 += iaadd;
        i4++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      zleft += zleftadd;
      aleft += aleftadd;
    }
  }
  
  private void drawsegment_texture24(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f1 = iuadd;
    float f2 = ivadd;
    int i = (m_fill & 0xFFFFFF) != 16777215 ? 1 : 0;
    int j = m_fill >> 16 & 0xFF;
    int k = m_fill >> 8 & 0xFF;
    int m = m_fill & 0xFF;
    int n = paramInt1 / SCREEN_WIDTH;
    int i1 = m_texture.length - TEX_WIDTH - 2;
    int i2 = parent.hints[7];
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    float f7 = 0.0F;
    float f8 = 0.0F;
    int i3 = TEX_INTERP_POWER;
    int i4 = 1 << i3;
    if (i2 != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= i4;
        newbx *= i4;
        newcx *= i4;
        f5 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        i2 = 0;
      }
    }
    while (paramInt1 < paramInt2)
    {
      int i5 = (int)(xleft + 0.5F);
      if (i5 < 0) {
        i5 = 0;
      }
      int i6 = i5;
      int i7 = (int)(xrght + 0.5F);
      if (i7 > SCREEN_WIDTH) {
        i7 = SCREEN_WIDTH;
      }
      float f9 = i5 + 0.5F - xleft;
      int i8 = (int)(f1 * f9 + uleft);
      int i9 = (int)(f2 * f9 + vleft);
      float f10 = izadd * f9 + zleft;
      i5 += paramInt1;
      i7 += paramInt1;
      if (i2 != 0)
      {
        f3 = xmult * (i6 + 0.5F - SCREEN_WIDTH / 2.0F);
        f4 = ymult * (n + 0.5F - SCREEN_HEIGHT / 2.0F);
        f6 = f3 * ax + f4 * ay + f5 * az;
        f7 = f3 * bx + f4 * by + f5 * bz;
        f8 = f3 * cx + f4 * cy + f5 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f8 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f11 = 0.0F;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      int i16;
      float f15;
      if ((i2 != 0) && (i10 != 0))
      {
        int i14 = (i7 - i5 - 1) % i4;
        i16 = i4 - i14;
        float f16 = i14 / i4;
        float f17 = i16 / i4;
        i11 = i16;
        float f18 = f6 - f17 * newax;
        float f19 = f7 - f17 * newbx;
        float f20 = f8 - f17 * newcx;
        float f21 = 65536.0F / f20;
        f13 = f18 * f21;
        f14 = f19 * f21;
        f6 += f16 * newax;
        f7 += f16 * newbx;
        f8 += f16 * newcx;
        f21 = 65536.0F / f8;
        f11 = f6 * f21;
        f12 = f7 * f21;
        i12 = (int)(f11 - f13) >> i3;
        i13 = (int)(f12 - f14) >> i3;
        i8 = (int)f13 + (i16 - 1) * i12;
        i9 = (int)f14 + (i16 - 1) * i13;
      }
      else
      {
        f15 = 65536.0F / f8;
        f11 = f6 * f15;
        f12 = f7 * f15;
      }
      while (i5 < i7)
      {
        if (i2 != 0)
        {
          if (i11 == i4) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f6 += newax;
            f7 += newbx;
            f8 += newcx;
            f15 = 65536.0F / f8;
            f13 = f11;
            f14 = f12;
            f11 = f6 * f15;
            f12 = f7 * f15;
            i8 = (int)f13;
            i9 = (int)f14;
            i12 = (int)(f11 - f13) >> i3;
            i13 = (int)(f12 - f14) >> i3;
          }
          else
          {
            i8 += i12;
            i9 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f10 <= m_zbuffer[i5]))
          {
            m_zbuffer[i5] = f10;
            if (m_bilinear)
            {
              int i15 = (i9 >> 16) * TEX_WIDTH + (i8 >> 16);
              i16 = (i8 & 0xFFFF) >> 9;
              int i17 = (i9 & 0xFFFF) >> 9;
              int i18 = m_texture[i15];
              int i19 = m_texture[(i15 + 1)];
              if (i15 < i1) {
                i15 += TEX_WIDTH;
              }
              int i20 = m_texture[i15];
              int i21 = m_texture[(i15 + 1)];
              int i22 = i18 & 0xFF0000;
              int i23 = i20 & 0xFF0000;
              int i24 = i22 + (((i19 & 0xFF0000) - i22) * i16 >> 7);
              int i25 = i23 + (((i21 & 0xFF0000) - i23) * i16 >> 7);
              int i26 = i24 + ((i25 - i24) * i17 >> 7);
              if (i != 0) {
                i26 = i26 * j >> 8 & 0xFF0000;
              }
              i22 = i18 & 0xFF00;
              i23 = i20 & 0xFF00;
              i24 = i22 + (((i19 & 0xFF00) - i22) * i16 >> 7);
              i25 = i23 + (((i21 & 0xFF00) - i23) * i16 >> 7);
              int i27 = i24 + ((i25 - i24) * i17 >> 7);
              if (i != 0) {
                i27 = i27 * k >> 8 & 0xFF00;
              }
              i22 = i18 & 0xFF;
              i23 = i20 & 0xFF;
              i24 = i22 + (((i19 & 0xFF) - i22) * i16 >> 7);
              i25 = i23 + (((i21 & 0xFF) - i23) * i16 >> 7);
              int i28 = i24 + ((i25 - i24) * i17 >> 7);
              if (i != 0) {
                i28 = i28 * m >> 8 & 0xFF;
              }
              m_pixels[i5] = (0xFF000000 | i26 & 0xFF0000 | i27 & 0xFF00 | i28 & 0xFF);
            }
            else
            {
              m_pixels[i5] = m_texture[((i9 >> 16) * TEX_WIDTH + (i8 >> 16))];
            }
          }
        }
        catch (Exception localException) {}
        f10 += izadd;
        i6++;
        if (i2 == 0)
        {
          i8 += iuadd;
          i9 += ivadd;
        }
        i5++;
      }
      n++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      zleft += zleftadd;
      uleft += uleftadd;
      vleft += vleftadd;
    }
  }
  
  private void drawsegment_texture24_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    int i1 = (m_fill & 0xFFFFFF) != 16777215 ? 1 : 0;
    int i2 = m_fill >> 16 & 0xFF;
    int i3 = m_fill >> 8 & 0xFF;
    int i4 = m_fill & 0xFF;
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i5 = (int)(xleft + 0.5F);
      if (i5 < 0) {
        i5 = 0;
      }
      int i6 = i5;
      int i7 = (int)(xrght + 0.5F);
      if (i7 > SCREEN_WIDTH) {
        i7 = SCREEN_WIDTH;
      }
      float f10 = i5 + 0.5F - xleft;
      int i8 = (int)(f7 * f10 + uleft);
      int i9 = (int)(f8 * f10 + vleft);
      int i10 = (int)(f9 * f10 + aleft);
      float f11 = izadd * f10 + zleft;
      i5 += paramInt1;
      i7 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i6 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i11 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i12 = 0;
      int i13 = 0;
      int i14 = 0;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      float f15 = 0.0F;
      int i17;
      float f16;
      if ((k != 0) && (i11 != 0))
      {
        int i15 = (i7 - i5 - 1) % n;
        i17 = n - i15;
        float f17 = i15 / n;
        float f18 = i17 / n;
        i12 = i17;
        float f19 = f4 - f18 * newax;
        float f20 = f5 - f18 * newbx;
        float f21 = f6 - f18 * newcx;
        float f22 = 65536.0F / f21;
        f14 = f19 * f22;
        f15 = f20 * f22;
        f4 += f17 * newax;
        f5 += f17 * newbx;
        f6 += f17 * newcx;
        f22 = 65536.0F / f6;
        f12 = f4 * f22;
        f13 = f5 * f22;
        i13 = (int)(f12 - f14) >> m;
        i14 = (int)(f13 - f15) >> m;
        i8 = (int)f14 + (i17 - 1) * i13;
        i9 = (int)f15 + (i17 - 1) * i14;
      }
      else
      {
        f16 = 65536.0F / f6;
        f12 = f4 * f16;
        f13 = f5 * f16;
      }
      while (i5 < i7)
      {
        if (k != 0)
        {
          if (i12 == n) {
            i12 = 0;
          }
          if (i12 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f16 = 65536.0F / f6;
            f14 = f12;
            f15 = f13;
            f12 = f4 * f16;
            f13 = f5 * f16;
            i8 = (int)f14;
            i9 = (int)f15;
            i13 = (int)(f12 - f14) >> m;
            i14 = (int)(f13 - f15) >> m;
          }
          else
          {
            i8 += i13;
            i9 += i14;
          }
          i12++;
        }
        try
        {
          if ((noDepthTest) || (f11 <= m_zbuffer[i5]))
          {
            int i16 = i10 >> 16;
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            if (m_bilinear)
            {
              i17 = (i9 >> 16) * TEX_WIDTH + (i8 >> 16);
              i18 = (i8 & 0xFFFF) >> 9;
              i19 = (i9 & 0xFFFF) >> 9;
              i20 = m_texture[i17];
              i21 = m_texture[(i17 + 1)];
              if (i17 < j) {
                i17 += TEX_WIDTH;
              }
              i22 = m_texture[i17];
              int i23 = m_texture[(i17 + 1)];
              int i24 = i20 & 0xFF0000;
              int i25 = i22 & 0xFF0000;
              int i26 = i24 + (((i21 & 0xFF0000) - i24) * i18 >> 7);
              int i27 = i25 + (((i23 & 0xFF0000) - i25) * i18 >> 7);
              int i28 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i28 = i28 * i2 >> 8 & 0xFF0000;
              }
              i24 = i20 & 0xFF00;
              i25 = i22 & 0xFF00;
              i26 = i24 + (((i21 & 0xFF00) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF00) - i25) * i18 >> 7);
              int i29 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i29 = i29 * i3 >> 8 & 0xFF00;
              }
              i24 = i20 & 0xFF;
              i25 = i22 & 0xFF;
              i26 = i24 + (((i21 & 0xFF) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF) - i25) * i18 >> 7);
              int i30 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i30 = i30 * i4 >> 8 & 0xFF;
              }
              int i31 = m_pixels[i5];
              int i32 = i31 & 0xFF0000;
              int i33 = i31 & 0xFF00;
              i31 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i32 + ((i28 - i32) * i16 >> 8) & 0xFF0000 | i33 + ((i29 - i33) * i16 >> 8) & 0xFF00 | i31 + ((i30 - i31) * i16 >> 8) & 0xFF);
            }
            else
            {
              i17 = m_texture[((i9 >> 16) * TEX_WIDTH + (i8 >> 16))];
              i18 = i17 & 0xFF00;
              i19 = i17 & 0xFF;
              i17 &= 0xFF0000;
              i20 = m_pixels[i5];
              i21 = i20 & 0xFF0000;
              i22 = i20 & 0xFF00;
              i20 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i21 + ((i17 - i21) * i16 >> 8) & 0xFF0000 | i22 + ((i18 - i22) * i16 >> 8) & 0xFF00 | i20 + ((i19 - i20) * i16 >> 8) & 0xFF);
            }
          }
        }
        catch (Exception localException) {}
        i6++;
        if (k == 0)
        {
          i8 += iuadd;
          i9 += ivadd;
        }
        i10 += iaadd;
        f11 += izadd;
        i5++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      zleft += zleftadd;
      aleft += aleftadd;
    }
  }
  
  private void drawsegment_texture32(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    int i1 = m_fill != -1 ? 1 : 0;
    int i2 = m_fill >> 16 & 0xFF;
    int i3 = m_fill >> 8 & 0xFF;
    int i4 = m_fill & 0xFF;
    float f7 = iuadd;
    float f8 = ivadd;
    while (paramInt1 < paramInt2)
    {
      int i5 = (int)(xleft + 0.5F);
      if (i5 < 0) {
        i5 = 0;
      }
      int i6 = i5;
      int i7 = (int)(xrght + 0.5F);
      if (i7 > SCREEN_WIDTH) {
        i7 = SCREEN_WIDTH;
      }
      float f9 = i5 + 0.5F - xleft;
      int i8 = (int)(f7 * f9 + uleft);
      int i9 = (int)(f8 * f9 + vleft);
      float f10 = izadd * f9 + zleft;
      i5 += paramInt1;
      i7 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i6 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f11 = 0.0F;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      int i16;
      float f15;
      if ((k != 0) && (i10 != 0))
      {
        int i14 = (i7 - i5 - 1) % n;
        i16 = n - i14;
        float f16 = i14 / n;
        float f17 = i16 / n;
        i11 = i16;
        float f18 = f4 - f17 * newax;
        float f19 = f5 - f17 * newbx;
        float f20 = f6 - f17 * newcx;
        float f21 = 65536.0F / f20;
        f13 = f18 * f21;
        f14 = f19 * f21;
        f4 += f16 * newax;
        f5 += f16 * newbx;
        f6 += f16 * newcx;
        f21 = 65536.0F / f6;
        f11 = f4 * f21;
        f12 = f5 * f21;
        i12 = (int)(f11 - f13) >> m;
        i13 = (int)(f12 - f14) >> m;
        i8 = (int)f13 + (i16 - 1) * i12;
        i9 = (int)f14 + (i16 - 1) * i13;
      }
      else
      {
        f15 = 65536.0F / f6;
        f11 = f4 * f15;
        f12 = f5 * f15;
      }
      while (i5 < i7)
      {
        if (k != 0)
        {
          if (i11 == n) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f15 = 65536.0F / f6;
            f13 = f11;
            f14 = f12;
            f11 = f4 * f15;
            f12 = f5 * f15;
            i8 = (int)f13;
            i9 = (int)f14;
            i12 = (int)(f11 - f13) >> m;
            i13 = (int)(f12 - f14) >> m;
          }
          else
          {
            i8 += i12;
            i9 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f10 <= m_zbuffer[i5]))
          {
            int i15;
            int i17;
            int i18;
            int i19;
            int i20;
            int i21;
            if (m_bilinear)
            {
              i15 = (i9 >> 16) * TEX_WIDTH + (i8 >> 16);
              i16 = (i8 & 0xFFFF) >> 9;
              i17 = (i9 & 0xFFFF) >> 9;
              i18 = m_texture[i15];
              i19 = m_texture[(i15 + 1)];
              if (i15 < j) {
                i15 += TEX_WIDTH;
              }
              i20 = m_texture[i15];
              i21 = m_texture[(i15 + 1)];
              int i22 = i18 & 0xFF0000;
              int i23 = i20 & 0xFF0000;
              int i24 = i22 + (((i19 & 0xFF0000) - i22) * i16 >> 7);
              int i25 = i23 + (((i21 & 0xFF0000) - i23) * i16 >> 7);
              int i26 = i24 + ((i25 - i24) * i17 >> 7);
              if (i1 != 0) {
                i26 = i26 * i2 >> 8 & 0xFF0000;
              }
              i22 = i18 & 0xFF00;
              i23 = i20 & 0xFF00;
              i24 = i22 + (((i19 & 0xFF00) - i22) * i16 >> 7);
              i25 = i23 + (((i21 & 0xFF00) - i23) * i16 >> 7);
              int i27 = i24 + ((i25 - i24) * i17 >> 7);
              if (i1 != 0) {
                i27 = i27 * i3 >> 8 & 0xFF00;
              }
              i22 = i18 & 0xFF;
              i23 = i20 & 0xFF;
              i24 = i22 + (((i19 & 0xFF) - i22) * i16 >> 7);
              i25 = i23 + (((i21 & 0xFF) - i23) * i16 >> 7);
              int i28 = i24 + ((i25 - i24) * i17 >> 7);
              if (i1 != 0) {
                i28 = i28 * i4 >> 8 & 0xFF;
              }
              i18 >>>= 24;
              i20 >>>= 24;
              i24 = i18 + (((i19 >>> 24) - i18) * i16 >> 7);
              i25 = i20 + (((i21 >>> 24) - i20) * i16 >> 7);
              int i29 = i24 + ((i25 - i24) * i17 >> 7);
              int i30 = m_pixels[i5];
              int i31 = i30 & 0xFF0000;
              int i32 = i30 & 0xFF00;
              i30 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i31 + ((i26 - i31) * i29 >> 8) & 0xFF0000 | i32 + ((i27 - i32) * i29 >> 8) & 0xFF00 | i30 + ((i28 - i30) * i29 >> 8) & 0xFF);
            }
            else
            {
              i15 = m_texture[((i9 >> 16) * TEX_WIDTH + (i8 >> 16))];
              i16 = i15 >>> 24;
              i17 = i15 & 0xFF00;
              i18 = i15 & 0xFF;
              i15 &= 0xFF0000;
              i19 = m_pixels[i5];
              i20 = i19 & 0xFF0000;
              i21 = i19 & 0xFF00;
              i19 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i20 + ((i15 - i20) * i16 >> 8) & 0xFF0000 | i21 + ((i17 - i21) * i16 >> 8) & 0xFF00 | i19 + ((i18 - i19) * i16 >> 8) & 0xFF);
            }
          }
        }
        catch (Exception localException) {}
        i6++;
        if (k == 0)
        {
          i8 += iuadd;
          i9 += ivadd;
        }
        f10 += izadd;
        i5++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      zleft += zleftadd;
      aleft += aleftadd;
    }
  }
  
  private void drawsegment_texture32_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    int i1 = (m_fill & 0xFFFFFF) != 16777215 ? 1 : 0;
    int i2 = m_fill >> 16 & 0xFF;
    int i3 = m_fill >> 8 & 0xFF;
    int i4 = m_fill & 0xFF;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i5 = (int)(xleft + 0.5F);
      if (i5 < 0) {
        i5 = 0;
      }
      int i6 = i5;
      int i7 = (int)(xrght + 0.5F);
      if (i7 > SCREEN_WIDTH) {
        i7 = SCREEN_WIDTH;
      }
      float f10 = i5 + 0.5F - xleft;
      int i8 = (int)(f7 * f10 + uleft);
      int i9 = (int)(f8 * f10 + vleft);
      int i10 = (int)(f9 * f10 + aleft);
      float f11 = izadd * f10 + zleft;
      i5 += paramInt1;
      i7 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i6 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i11 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i12 = 0;
      int i13 = 0;
      int i14 = 0;
      float f12 = 0.0F;
      float f13 = 0.0F;
      float f14 = 0.0F;
      float f15 = 0.0F;
      int i17;
      float f16;
      if ((k != 0) && (i11 != 0))
      {
        int i15 = (i7 - i5 - 1) % n;
        i17 = n - i15;
        float f17 = i15 / n;
        float f18 = i17 / n;
        i12 = i17;
        float f19 = f4 - f18 * newax;
        float f20 = f5 - f18 * newbx;
        float f21 = f6 - f18 * newcx;
        float f22 = 65536.0F / f21;
        f14 = f19 * f22;
        f15 = f20 * f22;
        f4 += f17 * newax;
        f5 += f17 * newbx;
        f6 += f17 * newcx;
        f22 = 65536.0F / f6;
        f12 = f4 * f22;
        f13 = f5 * f22;
        i13 = (int)(f12 - f14) >> m;
        i14 = (int)(f13 - f15) >> m;
        i8 = (int)f14 + (i17 - 1) * i13;
        i9 = (int)f15 + (i17 - 1) * i14;
      }
      else
      {
        f16 = 65536.0F / f6;
        f12 = f4 * f16;
        f13 = f5 * f16;
      }
      while (i5 < i7)
      {
        if (k != 0)
        {
          if (i12 == n) {
            i12 = 0;
          }
          if (i12 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f16 = 65536.0F / f6;
            f14 = f12;
            f15 = f13;
            f12 = f4 * f16;
            f13 = f5 * f16;
            i8 = (int)f14;
            i9 = (int)f15;
            i13 = (int)(f12 - f14) >> m;
            i14 = (int)(f13 - f15) >> m;
          }
          else
          {
            i8 += i13;
            i9 += i14;
          }
          i12++;
        }
        try
        {
          if ((noDepthTest) || (f11 <= m_zbuffer[i5]))
          {
            int i16 = i10 >> 16;
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            if (m_bilinear)
            {
              i17 = (i9 >> 16) * TEX_WIDTH + (i8 >> 16);
              i18 = (i8 & 0xFFFF) >> 9;
              i19 = (i9 & 0xFFFF) >> 9;
              i20 = m_texture[i17];
              i21 = m_texture[(i17 + 1)];
              if (i17 < j) {
                i17 += TEX_WIDTH;
              }
              i22 = m_texture[i17];
              int i23 = m_texture[(i17 + 1)];
              int i24 = i20 & 0xFF0000;
              int i25 = i22 & 0xFF0000;
              int i26 = i24 + (((i21 & 0xFF0000) - i24) * i18 >> 7);
              int i27 = i25 + (((i23 & 0xFF0000) - i25) * i18 >> 7);
              int i28 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i28 = i28 * i2 >> 8 & 0xFF0000;
              }
              i24 = i20 & 0xFF00;
              i25 = i22 & 0xFF00;
              i26 = i24 + (((i21 & 0xFF00) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF00) - i25) * i18 >> 7);
              int i29 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i29 = i29 * i3 >> 8 & 0xFF00;
              }
              i24 = i20 & 0xFF;
              i25 = i22 & 0xFF;
              i26 = i24 + (((i21 & 0xFF) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF) - i25) * i18 >> 7);
              int i30 = i26 + ((i27 - i26) * i19 >> 7);
              if (i1 != 0) {
                i30 = i30 * i4 >> 8 & 0xFF;
              }
              i20 >>>= 24;
              i22 >>>= 24;
              i26 = i20 + (((i21 >>> 24) - i20) * i18 >> 7);
              i27 = i22 + (((i23 >>> 24) - i22) * i18 >> 7);
              i16 = i16 * (i26 + ((i27 - i26) * i19 >> 7)) >> 8;
              int i31 = m_pixels[i5];
              int i32 = i31 & 0xFF0000;
              int i33 = i31 & 0xFF00;
              i31 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i32 + ((i28 - i32) * i16 >> 8) & 0xFF0000 | i33 + ((i29 - i33) * i16 >> 8) & 0xFF00 | i31 + ((i30 - i31) * i16 >> 8) & 0xFF);
            }
            else
            {
              i17 = m_texture[((i9 >> 16) * TEX_WIDTH + (i8 >> 16))];
              i16 = i16 * (i17 >>> 24) >> 8;
              i18 = i17 & 0xFF00;
              i19 = i17 & 0xFF;
              i17 &= 0xFF0000;
              i20 = m_pixels[i5];
              i21 = i20 & 0xFF0000;
              i22 = i20 & 0xFF00;
              i20 &= 0xFF;
              m_pixels[i5] = (0xFF000000 | i21 + ((i17 - i21) * i16 >> 8) & 0xFF0000 | i22 + ((i18 - i22) * i16 >> 8) & 0xFF00 | i20 + ((i19 - i20) * i16 >> 8) & 0xFF);
            }
          }
        }
        catch (Exception localException) {}
        i6++;
        if (k == 0)
        {
          i8 += iuadd;
          i9 += ivadd;
        }
        i10 += iaadd;
        f11 += izadd;
        i5++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      zleft += zleftadd;
      aleft += aleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture8(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f12 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f12 + uleft);
      int i5 = (int)(f8 * f12 + vleft);
      int i6 = (int)(f9 * f12 + rleft);
      int i7 = (int)(f10 * f12 + gleft);
      int i8 = (int)(f11 * f12 + bleft);
      float f13 = izadd * f12 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i9 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i10 = 0;
      int i11 = 0;
      int i12 = 0;
      float f14 = 0.0F;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      int i15;
      float f18;
      if ((k != 0) && (i9 != 0))
      {
        int i13 = (i3 - i1 - 1) % n;
        i15 = n - i13;
        float f19 = i13 / n;
        float f20 = i15 / n;
        i10 = i15;
        float f21 = f4 - f20 * newax;
        float f22 = f5 - f20 * newbx;
        float f23 = f6 - f20 * newcx;
        float f24 = 65536.0F / f23;
        f16 = f21 * f24;
        f17 = f22 * f24;
        f4 += f19 * newax;
        f5 += f19 * newbx;
        f6 += f19 * newcx;
        f24 = 65536.0F / f6;
        f14 = f4 * f24;
        f15 = f5 * f24;
        i11 = (int)(f14 - f16) >> m;
        i12 = (int)(f15 - f17) >> m;
        i4 = (int)f16 + (i15 - 1) * i11;
        i5 = (int)f17 + (i15 - 1) * i12;
      }
      else
      {
        f18 = 65536.0F / f6;
        f14 = f4 * f18;
        f15 = f5 * f18;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i10 == n) {
            i10 = 0;
          }
          if (i10 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f18 = 65536.0F / f6;
            f16 = f14;
            f17 = f15;
            f14 = f4 * f18;
            f15 = f5 * f18;
            i4 = (int)f16;
            i5 = (int)f17;
            i11 = (int)(f14 - f16) >> m;
            i12 = (int)(f15 - f17) >> m;
          }
          else
          {
            i4 += i11;
            i5 += i12;
          }
          i10++;
        }
        try
        {
          if ((noDepthTest) || (f13 <= m_zbuffer[i1]))
          {
            int i14;
            if (m_bilinear)
            {
              i15 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i16 = i4 & 0xFFFF;
              i14 = m_texture[i15] & 0xFF;
              i17 = m_texture[(i15 + 1)] & 0xFF;
              if (i15 < j) {
                i15 += TEX_WIDTH;
              }
              i18 = m_texture[i15] & 0xFF;
              i19 = m_texture[(i15 + 1)] & 0xFF;
              i14 += ((i17 - i14) * i16 >> 16);
              i18 += ((i19 - i18) * i16 >> 16);
              i14 += ((i18 - i14) * (i5 & 0xFFFF) >> 16);
            }
            else
            {
              i14 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))] & 0xFF;
            }
            i15 = i6 & 0xFF0000;
            int i16 = i7 >> 8 & 0xFF00;
            int i17 = i8 >> 16;
            int i18 = m_pixels[i1];
            int i19 = i18 & 0xFF0000;
            int i20 = i18 & 0xFF00;
            i18 &= 0xFF;
            m_pixels[i1] = (0xFF000000 | i19 + ((i15 - i19) * i14 >> 8) & 0xFF0000 | i20 + ((i16 - i20) * i14 >> 8) & 0xFF00 | i18 + ((i17 - i18) * i14 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        f13 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture8_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    float f12 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f13 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f13 + uleft);
      int i5 = (int)(f8 * f13 + vleft);
      int i6 = (int)(f9 * f13 + rleft);
      int i7 = (int)(f10 * f13 + gleft);
      int i8 = (int)(f11 * f13 + bleft);
      int i9 = (int)(f12 * f13 + aleft);
      float f14 = izadd * f13 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      float f18 = 0.0F;
      int i16;
      float f19;
      if ((k != 0) && (i10 != 0))
      {
        int i14 = (i3 - i1 - 1) % n;
        i16 = n - i14;
        float f20 = i14 / n;
        float f21 = i16 / n;
        i11 = i16;
        float f22 = f4 - f21 * newax;
        float f23 = f5 - f21 * newbx;
        float f24 = f6 - f21 * newcx;
        float f25 = 65536.0F / f24;
        f17 = f22 * f25;
        f18 = f23 * f25;
        f4 += f20 * newax;
        f5 += f20 * newbx;
        f6 += f20 * newcx;
        f25 = 65536.0F / f6;
        f15 = f4 * f25;
        f16 = f5 * f25;
        i12 = (int)(f15 - f17) >> m;
        i13 = (int)(f16 - f18) >> m;
        i4 = (int)f17 + (i16 - 1) * i12;
        i5 = (int)f18 + (i16 - 1) * i13;
      }
      else
      {
        f19 = 65536.0F / f6;
        f15 = f4 * f19;
        f16 = f5 * f19;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i11 == n) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f19 = 65536.0F / f6;
            f17 = f15;
            f18 = f16;
            f15 = f4 * f19;
            f16 = f5 * f19;
            i4 = (int)f17;
            i5 = (int)f18;
            i12 = (int)(f15 - f17) >> m;
            i13 = (int)(f16 - f18) >> m;
          }
          else
          {
            i4 += i12;
            i5 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f14 <= m_zbuffer[i1]))
          {
            if (m_bilinear)
            {
              i16 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i17 = i4 & 0xFFFF;
              i15 = m_texture[i16] & 0xFF;
              i18 = m_texture[(i16 + 1)] & 0xFF;
              if (i16 < j) {
                i16 += TEX_WIDTH;
              }
              i19 = m_texture[i16] & 0xFF;
              i20 = m_texture[(i16 + 1)] & 0xFF;
              i15 += ((i18 - i15) * i17 >> 16);
              i19 += ((i20 - i19) * i17 >> 16);
              i15 += ((i19 - i15) * (i5 & 0xFFFF) >> 16);
            }
            else
            {
              i15 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))] & 0xFF;
            }
            int i15 = i15 * (i9 >> 16) >> 8;
            i16 = i6 & 0xFF0000;
            int i17 = i7 >> 8 & 0xFF00;
            int i18 = i8 >> 16;
            int i19 = m_pixels[i1];
            int i20 = i19 & 0xFF0000;
            int i21 = i19 & 0xFF00;
            i19 &= 0xFF;
            m_pixels[i1] = (0xFF000000 | i20 + ((i16 - i20) * i15 >> 8) & 0xFF0000 | i21 + ((i17 - i21) * i15 >> 8) & 0xFF00 | i19 + ((i18 - i19) * i15 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        i9 += iaadd;
        f14 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      aleft += aleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture24(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f12 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f12 + uleft);
      int i5 = (int)(f8 * f12 + vleft);
      int i6 = (int)(f9 * f12 + rleft);
      int i7 = (int)(f10 * f12 + gleft);
      int i8 = (int)(f11 * f12 + bleft);
      float f13 = izadd * f12 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i9 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i10 = 0;
      int i11 = 0;
      int i12 = 0;
      float f14 = 0.0F;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      int i15;
      float f18;
      if ((k != 0) && (i9 != 0))
      {
        int i13 = (i3 - i1 - 1) % n;
        i15 = n - i13;
        float f19 = i13 / n;
        float f20 = i15 / n;
        i10 = i15;
        float f21 = f4 - f20 * newax;
        float f22 = f5 - f20 * newbx;
        float f23 = f6 - f20 * newcx;
        float f24 = 65536.0F / f23;
        f16 = f21 * f24;
        f17 = f22 * f24;
        f4 += f19 * newax;
        f5 += f19 * newbx;
        f6 += f19 * newcx;
        f24 = 65536.0F / f6;
        f14 = f4 * f24;
        f15 = f5 * f24;
        i11 = (int)(f14 - f16) >> m;
        i12 = (int)(f15 - f17) >> m;
        i4 = (int)f16 + (i15 - 1) * i11;
        i5 = (int)f17 + (i15 - 1) * i12;
      }
      else
      {
        f18 = 65536.0F / f6;
        f14 = f4 * f18;
        f15 = f5 * f18;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i10 == n) {
            i10 = 0;
          }
          if (i10 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f18 = 65536.0F / f6;
            f16 = f14;
            f17 = f15;
            f14 = f4 * f18;
            f15 = f5 * f18;
            i4 = (int)f16;
            i5 = (int)f17;
            i11 = (int)(f14 - f16) >> m;
            i12 = (int)(f15 - f17) >> m;
          }
          else
          {
            i4 += i11;
            i5 += i12;
          }
          i10++;
        }
        try
        {
          if ((noDepthTest) || (f13 <= m_zbuffer[i1]))
          {
            m_zbuffer[i1] = f13;
            int i14;
            int i16;
            if (m_bilinear)
            {
              i17 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i18 = (i4 & 0xFFFF) >> 9;
              i19 = (i5 & 0xFFFF) >> 9;
              int i20 = m_texture[i17];
              int i21 = m_texture[(i17 + 1)];
              if (i17 < j) {
                i17 += TEX_WIDTH;
              }
              int i22 = m_texture[i17];
              int i23 = m_texture[(i17 + 1)];
              int i24 = i20 & 0xFF0000;
              int i25 = i22 & 0xFF0000;
              int i26 = i24 + (((i21 & 0xFF0000) - i24) * i18 >> 7);
              int i27 = i25 + (((i23 & 0xFF0000) - i25) * i18 >> 7);
              i14 = i26 + ((i27 - i26) * i19 >> 7);
              i24 = i20 & 0xFF00;
              i25 = i22 & 0xFF00;
              i26 = i24 + (((i21 & 0xFF00) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF00) - i25) * i18 >> 7);
              i15 = i26 + ((i27 - i26) * i19 >> 7);
              i24 = i20 & 0xFF;
              i25 = i22 & 0xFF;
              i26 = i24 + (((i21 & 0xFF) - i24) * i18 >> 7);
              i27 = i25 + (((i23 & 0xFF) - i25) * i18 >> 7);
              i16 = i26 + ((i27 - i26) * i19 >> 7);
            }
            else
            {
              i16 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))];
              i14 = i16 & 0xFF0000;
              i15 = i16 & 0xFF00;
              i16 &= 0xFF;
            }
            int i17 = i6 >> 16;
            int i18 = i7 >> 16;
            int i19 = i8 >> 16;
            m_pixels[i1] = (0xFF000000 | (i14 * i17 & 0xFF000000 | i15 * i18 & 0xFF0000 | i16 * i19) >> 8);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        f13 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture24_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    float f12 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f13 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f13 + uleft);
      int i5 = (int)(f8 * f13 + vleft);
      int i6 = (int)(f9 * f13 + rleft);
      int i7 = (int)(f10 * f13 + gleft);
      int i8 = (int)(f11 * f13 + bleft);
      int i9 = (int)(f12 * f13 + aleft);
      float f14 = izadd * f13 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      float f18 = 0.0F;
      int i16;
      float f19;
      if ((k != 0) && (i10 != 0))
      {
        int i14 = (i3 - i1 - 1) % n;
        i16 = n - i14;
        float f20 = i14 / n;
        float f21 = i16 / n;
        i11 = i16;
        float f22 = f4 - f21 * newax;
        float f23 = f5 - f21 * newbx;
        float f24 = f6 - f21 * newcx;
        float f25 = 65536.0F / f24;
        f17 = f22 * f25;
        f18 = f23 * f25;
        f4 += f20 * newax;
        f5 += f20 * newbx;
        f6 += f20 * newcx;
        f25 = 65536.0F / f6;
        f15 = f4 * f25;
        f16 = f5 * f25;
        i12 = (int)(f15 - f17) >> m;
        i13 = (int)(f16 - f18) >> m;
        i4 = (int)f17 + (i16 - 1) * i12;
        i5 = (int)f18 + (i16 - 1) * i13;
      }
      else
      {
        f19 = 65536.0F / f6;
        f15 = f4 * f19;
        f16 = f5 * f19;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i11 == n) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f19 = 65536.0F / f6;
            f17 = f15;
            f18 = f16;
            f15 = f4 * f19;
            f16 = f5 * f19;
            i4 = (int)f17;
            i5 = (int)f18;
            i12 = (int)(f15 - f17) >> m;
            i13 = (int)(f16 - f18) >> m;
          }
          else
          {
            i4 += i12;
            i5 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f14 <= m_zbuffer[i1]))
          {
            int i15 = i9 >> 16;
            if (m_bilinear)
            {
              i19 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i20 = (i4 & 0xFFFF) >> 9;
              i21 = (i5 & 0xFFFF) >> 9;
              int i22 = m_texture[i19];
              int i23 = m_texture[(i19 + 1)];
              if (i19 < j) {
                i19 += TEX_WIDTH;
              }
              int i24 = m_texture[i19];
              int i25 = m_texture[(i19 + 1)];
              int i26 = i22 & 0xFF0000;
              int i27 = i24 & 0xFF0000;
              int i28 = i26 + (((i23 & 0xFF0000) - i26) * i20 >> 7);
              int i29 = i27 + (((i25 & 0xFF0000) - i27) * i20 >> 7);
              i16 = i28 + ((i29 - i28) * i21 >> 7) >> 16;
              i26 = i22 & 0xFF00;
              i27 = i24 & 0xFF00;
              i28 = i26 + (((i23 & 0xFF00) - i26) * i20 >> 7);
              i29 = i27 + (((i25 & 0xFF00) - i27) * i20 >> 7);
              i17 = i28 + ((i29 - i28) * i21 >> 7) >> 8;
              i26 = i22 & 0xFF;
              i27 = i24 & 0xFF;
              i28 = i26 + (((i23 & 0xFF) - i26) * i20 >> 7);
              i29 = i27 + (((i25 & 0xFF) - i27) * i20 >> 7);
              i18 = i28 + ((i29 - i28) * i21 >> 7);
            }
            else
            {
              i18 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))];
              i16 = (i18 & 0xFF0000) >> 16;
              i17 = (i18 & 0xFF00) >> 8;
              i18 &= 0xFF;
            }
            i16 = i16 * i6 >>> 8;
            int i17 = i17 * i7 >>> 16;
            int i18 = i18 * i8 >>> 24;
            int i19 = m_pixels[i1];
            int i20 = i19 & 0xFF0000;
            int i21 = i19 & 0xFF00;
            i19 &= 0xFF;
            m_pixels[i1] = (0xFF000000 | i20 + ((i16 - i20) * i15 >> 8) & 0xFF0000 | i21 + ((i17 - i21) * i15 >> 8) & 0xFF00 | i19 + ((i18 - i19) * i15 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        i9 += iaadd;
        f14 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      aleft += aleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture32(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f12 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f12 + uleft);
      int i5 = (int)(f8 * f12 + vleft);
      int i6 = (int)(f9 * f12 + rleft);
      int i7 = (int)(f10 * f12 + gleft);
      int i8 = (int)(f11 * f12 + bleft);
      float f13 = izadd * f12 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i9 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i10 = 0;
      int i11 = 0;
      int i12 = 0;
      float f14 = 0.0F;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      int i15;
      float f18;
      if ((k != 0) && (i9 != 0))
      {
        int i13 = (i3 - i1 - 1) % n;
        i15 = n - i13;
        float f19 = i13 / n;
        float f20 = i15 / n;
        i10 = i15;
        float f21 = f4 - f20 * newax;
        float f22 = f5 - f20 * newbx;
        float f23 = f6 - f20 * newcx;
        float f24 = 65536.0F / f23;
        f16 = f21 * f24;
        f17 = f22 * f24;
        f4 += f19 * newax;
        f5 += f19 * newbx;
        f6 += f19 * newcx;
        f24 = 65536.0F / f6;
        f14 = f4 * f24;
        f15 = f5 * f24;
        i11 = (int)(f14 - f16) >> m;
        i12 = (int)(f15 - f17) >> m;
        i4 = (int)f16 + (i15 - 1) * i11;
        i5 = (int)f17 + (i15 - 1) * i12;
      }
      else
      {
        f18 = 65536.0F / f6;
        f14 = f4 * f18;
        f15 = f5 * f18;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i10 == n) {
            i10 = 0;
          }
          if (i10 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f18 = 65536.0F / f6;
            f16 = f14;
            f17 = f15;
            f14 = f4 * f18;
            f15 = f5 * f18;
            i4 = (int)f16;
            i5 = (int)f17;
            i11 = (int)(f14 - f16) >> m;
            i12 = (int)(f15 - f17) >> m;
          }
          else
          {
            i4 += i11;
            i5 += i12;
          }
          i10++;
        }
        try
        {
          if ((noDepthTest) || (f13 <= m_zbuffer[i1]))
          {
            int i17;
            if (m_bilinear)
            {
              i18 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i19 = (i4 & 0xFFFF) >> 9;
              i20 = (i5 & 0xFFFF) >> 9;
              int i21 = m_texture[i18];
              int i22 = m_texture[(i18 + 1)];
              if (i18 < j) {
                i18 += TEX_WIDTH;
              }
              int i23 = m_texture[i18];
              int i24 = m_texture[(i18 + 1)];
              int i25 = i21 & 0xFF0000;
              int i26 = i23 & 0xFF0000;
              int i27 = i25 + (((i22 & 0xFF0000) - i25) * i19 >> 7);
              int i28 = i26 + (((i24 & 0xFF0000) - i26) * i19 >> 7);
              i14 = i27 + ((i28 - i27) * i20 >> 7) >> 16;
              i25 = i21 & 0xFF00;
              i26 = i23 & 0xFF00;
              i27 = i25 + (((i22 & 0xFF00) - i25) * i19 >> 7);
              i28 = i26 + (((i24 & 0xFF00) - i26) * i19 >> 7);
              i15 = i27 + ((i28 - i27) * i20 >> 7) >> 8;
              i25 = i21 & 0xFF;
              i26 = i23 & 0xFF;
              i27 = i25 + (((i22 & 0xFF) - i25) * i19 >> 7);
              i28 = i26 + (((i24 & 0xFF) - i26) * i19 >> 7);
              i16 = i27 + ((i28 - i27) * i20 >> 7);
              i21 >>>= 24;
              i23 >>>= 24;
              i27 = i21 + (((i22 >>> 24) - i21) * i19 >> 7);
              i28 = i23 + (((i24 >>> 24) - i23) * i19 >> 7);
              i17 = i27 + ((i28 - i27) * i20 >> 7);
            }
            else
            {
              i16 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))];
              i17 = i16 >>> 24;
              i14 = (i16 & 0xFF0000) >> 16;
              i15 = (i16 & 0xFF00) >> 8;
              i16 &= 0xFF;
            }
            int i14 = i14 * i6 >>> 8;
            i15 = i15 * i7 >>> 16;
            int i16 = i16 * i8 >>> 24;
            int i18 = m_pixels[i1];
            int i19 = i18 & 0xFF0000;
            int i20 = i18 & 0xFF00;
            i18 &= 0xFF;
            m_pixels[i1] = (0xFF000000 | i19 + ((i14 - i19) * i17 >> 8) & 0xFF0000 | i20 + ((i15 - i20) * i17 >> 8) & 0xFF00 | i18 + ((i16 - i18) * i17 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        f13 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      zleft += zleftadd;
    }
  }
  
  private void drawsegment_gouraud_texture32_alpha(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = m_texture.length - TEX_WIDTH - 2;
    int k = parent.hints[7];
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    int m = TEX_INTERP_POWER;
    int n = 1 << m;
    if (k != 0) {
      if (precomputeAccurateTexturing())
      {
        newax *= n;
        newbx *= n;
        newcx *= n;
        f3 = nearPlaneDepth;
        firstSegment = false;
      }
      else
      {
        k = 0;
      }
    }
    paramInt1 *= SCREEN_WIDTH;
    paramInt2 *= SCREEN_WIDTH;
    float f7 = iuadd;
    float f8 = ivadd;
    float f9 = iradd;
    float f10 = igadd;
    float f11 = ibadd;
    float f12 = iaadd;
    while (paramInt1 < paramInt2)
    {
      int i1 = (int)(xleft + 0.5F);
      if (i1 < 0) {
        i1 = 0;
      }
      int i2 = i1;
      int i3 = (int)(xrght + 0.5F);
      if (i3 > SCREEN_WIDTH) {
        i3 = SCREEN_WIDTH;
      }
      float f13 = i1 + 0.5F - xleft;
      int i4 = (int)(f7 * f13 + uleft);
      int i5 = (int)(f8 * f13 + vleft);
      int i6 = (int)(f9 * f13 + rleft);
      int i7 = (int)(f10 * f13 + gleft);
      int i8 = (int)(f11 * f13 + bleft);
      int i9 = (int)(f12 * f13 + aleft);
      float f14 = izadd * f13 + zleft;
      i1 += paramInt1;
      i3 += paramInt1;
      if (k != 0)
      {
        f1 = xmult * (i2 + 0.5F - SCREEN_WIDTH / 2.0F);
        f2 = ymult * (i + 0.5F - SCREEN_HEIGHT / 2.0F);
        f4 = f1 * ax + f2 * ay + f3 * az;
        f5 = f1 * bx + f2 * by + f3 * bz;
        f6 = f1 * cx + f2 * cy + f3 * cz;
      }
      int i10 = (newcx > 0.0F ? 1 : 0) == (f6 > 0.0F ? 1 : 0) ? 0 : 1;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      float f15 = 0.0F;
      float f16 = 0.0F;
      float f17 = 0.0F;
      float f18 = 0.0F;
      int i16;
      float f19;
      if ((k != 0) && (i10 != 0))
      {
        int i14 = (i3 - i1 - 1) % n;
        i16 = n - i14;
        float f20 = i14 / n;
        float f21 = i16 / n;
        i11 = i16;
        float f22 = f4 - f21 * newax;
        float f23 = f5 - f21 * newbx;
        float f24 = f6 - f21 * newcx;
        float f25 = 65536.0F / f24;
        f17 = f22 * f25;
        f18 = f23 * f25;
        f4 += f20 * newax;
        f5 += f20 * newbx;
        f6 += f20 * newcx;
        f25 = 65536.0F / f6;
        f15 = f4 * f25;
        f16 = f5 * f25;
        i12 = (int)(f15 - f17) >> m;
        i13 = (int)(f16 - f18) >> m;
        i4 = (int)f17 + (i16 - 1) * i12;
        i5 = (int)f18 + (i16 - 1) * i13;
      }
      else
      {
        f19 = 65536.0F / f6;
        f15 = f4 * f19;
        f16 = f5 * f19;
      }
      while (i1 < i3)
      {
        if (k != 0)
        {
          if (i11 == n) {
            i11 = 0;
          }
          if (i11 == 0)
          {
            f4 += newax;
            f5 += newbx;
            f6 += newcx;
            f19 = 65536.0F / f6;
            f17 = f15;
            f18 = f16;
            f15 = f4 * f19;
            f16 = f5 * f19;
            i4 = (int)f17;
            i5 = (int)f18;
            i12 = (int)(f15 - f17) >> m;
            i13 = (int)(f16 - f18) >> m;
          }
          else
          {
            i4 += i12;
            i5 += i13;
          }
          i11++;
        }
        try
        {
          if ((noDepthTest) || (f14 <= m_zbuffer[i1]))
          {
            int i15 = i9 >> 16;
            if (m_bilinear)
            {
              i19 = (i5 >> 16) * TEX_WIDTH + (i4 >> 16);
              i20 = (i4 & 0xFFFF) >> 9;
              i21 = (i5 & 0xFFFF) >> 9;
              int i22 = m_texture[i19];
              int i23 = m_texture[(i19 + 1)];
              if (i19 < j) {
                i19 += TEX_WIDTH;
              }
              int i24 = m_texture[i19];
              int i25 = m_texture[(i19 + 1)];
              int i26 = i22 & 0xFF0000;
              int i27 = i24 & 0xFF0000;
              int i28 = i26 + (((i23 & 0xFF0000) - i26) * i20 >> 7);
              int i29 = i27 + (((i25 & 0xFF0000) - i27) * i20 >> 7);
              i16 = i28 + ((i29 - i28) * i21 >> 7) >> 16;
              i26 = i22 & 0xFF00;
              i27 = i24 & 0xFF00;
              i28 = i26 + (((i23 & 0xFF00) - i26) * i20 >> 7);
              i29 = i27 + (((i25 & 0xFF00) - i27) * i20 >> 7);
              i17 = i28 + ((i29 - i28) * i21 >> 7) >> 8;
              i26 = i22 & 0xFF;
              i27 = i24 & 0xFF;
              i28 = i26 + (((i23 & 0xFF) - i26) * i20 >> 7);
              i29 = i27 + (((i25 & 0xFF) - i27) * i20 >> 7);
              i18 = i28 + ((i29 - i28) * i21 >> 7);
              i22 >>>= 24;
              i24 >>>= 24;
              i28 = i22 + (((i23 >>> 24) - i22) * i20 >> 7);
              i29 = i24 + (((i25 >>> 24) - i24) * i20 >> 7);
              i15 = i15 * (i28 + ((i29 - i28) * i21 >> 7)) >> 8;
            }
            else
            {
              i18 = m_texture[((i5 >> 16) * TEX_WIDTH + (i4 >> 16))];
              i15 = i15 * (i18 >>> 24) >> 8;
              i16 = (i18 & 0xFF0000) >> 16;
              i17 = (i18 & 0xFF00) >> 8;
              i18 &= 0xFF;
            }
            i16 = i16 * i6 >>> 8;
            int i17 = i17 * i7 >>> 16;
            int i18 = i18 * i8 >>> 24;
            int i19 = m_pixels[i1];
            int i20 = i19 & 0xFF0000;
            int i21 = i19 & 0xFF00;
            i19 &= 0xFF;
            m_pixels[i1] = (0xFF000000 | i20 + ((i16 - i20) * i15 >> 8) & 0xFF0000 | i21 + ((i17 - i21) * i15 >> 8) & 0xFF00 | i19 + ((i18 - i19) * i15 >> 8) & 0xFF);
          }
        }
        catch (Exception localException) {}
        i2++;
        if (k == 0)
        {
          i4 += iuadd;
          i5 += ivadd;
        }
        i6 += iradd;
        i7 += igadd;
        i8 += ibadd;
        i9 += iaadd;
        f14 += izadd;
        i1++;
      }
      i++;
      paramInt1 += SCREEN_WIDTH;
      xleft += paramFloat1;
      xrght += paramFloat2;
      uleft += uleftadd;
      vleft += vleftadd;
      rleft += rleftadd;
      gleft += gleftadd;
      bleft += bleftadd;
      aleft += aleftadd;
      zleft += zleftadd;
    }
  }
}
