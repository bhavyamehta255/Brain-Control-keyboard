package processing.core;

import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;

public class PGraphics2D
  extends PGraphics
{
  PMatrix2D ctm = new PMatrix2D();
  PPolygon fpolygon;
  PPolygon spolygon;
  float[][] svertices;
  PPolygon tpolygon;
  int[] vertexOrder;
  PLine line;
  float[][] matrixStack = new float[32][6];
  int matrixStackDepth;
  DirectColorModel cm;
  MemoryImageSource mis;
  
  public PGraphics2D() {}
  
  protected void allocate()
  {
    pixelCount = (width * height);
    pixels = new int[pixelCount];
    if (primarySurface)
    {
      cm = new DirectColorModel(32, 16711680, 65280, 255);
      mis = new MemoryImageSource(width, height, pixels, 0, width);
      mis.setFullBufferUpdates(true);
      mis.setAnimated(true);
      image = Toolkit.getDefaultToolkit().createImage(mis);
    }
  }
  
  public boolean canDraw()
  {
    return true;
  }
  
  public void beginDraw()
  {
    if (!settingsInited)
    {
      defaultSettings();
      fpolygon = new PPolygon(this);
      spolygon = new PPolygon(this);
      spolygon.vertexCount = 4;
      svertices = new float[2][];
    }
    resetMatrix();
    vertexCount = 0;
  }
  
  public void endDraw()
  {
    if (mis != null) {
      mis.newPixels(pixels, cm, 0, width);
    }
    updatePixels();
  }
  
  public void beginShape(int paramInt)
  {
    shape = paramInt;
    vertexCount = 0;
    curveVertexCount = 0;
    fpolygon.reset(4);
    spolygon.reset(4);
    textureImage = null;
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("vertex");
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    showDepthWarningXYZ("vertex");
  }
  
  public void breakShape()
  {
    showWarning("This renderer cannot handle concave shapes or shapes with holes.");
  }
  
  public void endShape(int paramInt)
  {
    int i;
    if (ctm.isIdentity()) {
      for (i = 0; i < vertexCount; i++)
      {
        vertices[i][18] = vertices[i][0];
        vertices[i][19] = vertices[i][1];
      }
    } else {
      for (i = 0; i < vertexCount; i++)
      {
        vertices[i][18] = ctm.multX(vertices[i][0], vertices[i][1]);
        vertices[i][19] = ctm.multY(vertices[i][0], vertices[i][1]);
      }
    }
    fpolygon.texture(textureImage);
    spolygon.interpARGB = true;
    fpolygon.interpARGB = true;
    int j;
    int k;
    int m;
    switch (shape)
    {
    case 2: 
      if (stroke) {
        if ((ctm.m00 == ctm.m11) && (strokeWeight == 1.0F)) {
          for (j = 0; j < vertexCount; j++) {
            thin_point(vertices[j][18], vertices[j][19], strokeColor);
          }
        } else {
          for (j = 0; j < vertexCount; j++)
          {
            float[] arrayOfFloat = vertices[j];
            thick_point(arrayOfFloat[18], arrayOfFloat[19], arrayOfFloat[20], arrayOfFloat[13], arrayOfFloat[14], arrayOfFloat[15], arrayOfFloat[16]);
          }
        }
      }
      break;
    case 4: 
      if (stroke)
      {
        i = shape == 4 ? 2 : 1;
        draw_lines(vertices, vertexCount - 1, 1, i, 0);
      }
      break;
    case 11: 
      if ((fill) || (textureImage != null))
      {
        fpolygon.vertexCount = 3;
        for (j = 1; j < vertexCount - 1; j++)
        {
          fpolygon.vertices[2][3] = vertices[0][3];
          fpolygon.vertices[2][4] = vertices[0][4];
          fpolygon.vertices[2][5] = vertices[0][5];
          fpolygon.vertices[2][6] = vertices[0][6];
          fpolygon.vertices[2][18] = vertices[0][18];
          fpolygon.vertices[2][19] = vertices[0][19];
          if (textureImage != null)
          {
            fpolygon.vertices[2][7] = vertices[0][7];
            fpolygon.vertices[2][8] = vertices[0][8];
          }
          for (k = 0; k < 2; k++)
          {
            fpolygon.vertices[k][3] = vertices[(j + k)][3];
            fpolygon.vertices[k][4] = vertices[(j + k)][4];
            fpolygon.vertices[k][5] = vertices[(j + k)][5];
            fpolygon.vertices[k][6] = vertices[(j + k)][6];
            fpolygon.vertices[k][18] = vertices[(j + k)][18];
            fpolygon.vertices[k][19] = vertices[(j + k)][19];
            if (textureImage != null)
            {
              fpolygon.vertices[k][7] = vertices[(j + k)][7];
              fpolygon.vertices[k][8] = vertices[(j + k)][8];
            }
          }
          fpolygon.render();
        }
      }
      if (stroke)
      {
        for (j = 1; j < vertexCount; j++) {
          draw_line(vertices[0], vertices[j]);
        }
        for (j = 1; j < vertexCount - 1; j++) {
          draw_line(vertices[j], vertices[(j + 1)]);
        }
        draw_line(vertices[(vertexCount - 1)], vertices[1]);
      }
      break;
    case 9: 
    case 10: 
      i = shape == 9 ? 3 : 1;
      if ((fill) || (textureImage != null))
      {
        fpolygon.vertexCount = 3;
        j = 0;
        while (j < vertexCount - 2)
        {
          for (k = 0; k < 3; k++)
          {
            fpolygon.vertices[k][3] = vertices[(j + k)][3];
            fpolygon.vertices[k][4] = vertices[(j + k)][4];
            fpolygon.vertices[k][5] = vertices[(j + k)][5];
            fpolygon.vertices[k][6] = vertices[(j + k)][6];
            fpolygon.vertices[k][18] = vertices[(j + k)][18];
            fpolygon.vertices[k][19] = vertices[(j + k)][19];
            fpolygon.vertices[k][20] = vertices[(j + k)][20];
            if (textureImage != null)
            {
              fpolygon.vertices[k][7] = vertices[(j + k)][7];
              fpolygon.vertices[k][8] = vertices[(j + k)][8];
            }
          }
          fpolygon.render();
          j += i;
        }
      }
      if (stroke)
      {
        if (shape == 10) {
          draw_lines(vertices, vertexCount - 1, 1, 1, 0);
        } else {
          draw_lines(vertices, vertexCount - 1, 1, 1, 3);
        }
        draw_lines(vertices, vertexCount - 2, 2, i, 0);
      }
      break;
    case 16: 
      if ((fill) || (textureImage != null))
      {
        fpolygon.vertexCount = 4;
        for (j = 0; j < vertexCount - 3; j += 4)
        {
          for (k = 0; k < 4; k++)
          {
            m = j + k;
            fpolygon.vertices[k][3] = vertices[m][3];
            fpolygon.vertices[k][4] = vertices[m][4];
            fpolygon.vertices[k][5] = vertices[m][5];
            fpolygon.vertices[k][6] = vertices[m][6];
            fpolygon.vertices[k][18] = vertices[m][18];
            fpolygon.vertices[k][19] = vertices[m][19];
            fpolygon.vertices[k][20] = vertices[m][20];
            if (textureImage != null)
            {
              fpolygon.vertices[k][7] = vertices[m][7];
              fpolygon.vertices[k][8] = vertices[m][8];
            }
          }
          fpolygon.render();
        }
      }
      if (stroke) {
        for (j = 0; j < vertexCount - 3; j += 4)
        {
          draw_line(vertices[(j + 0)], vertices[(j + 1)]);
          draw_line(vertices[(j + 1)], vertices[(j + 2)]);
          draw_line(vertices[(j + 2)], vertices[(j + 3)]);
          draw_line(vertices[(j + 3)], vertices[(j + 0)]);
        }
      }
      break;
    case 17: 
      if ((fill) || (textureImage != null))
      {
        fpolygon.vertexCount = 4;
        for (j = 0; j < vertexCount - 3; j += 2)
        {
          for (k = 0; k < 4; k++)
          {
            m = j + k;
            if (k == 2) {
              m = j + 3;
            }
            if (k == 3) {
              m = j + 2;
            }
            fpolygon.vertices[k][3] = vertices[m][3];
            fpolygon.vertices[k][4] = vertices[m][4];
            fpolygon.vertices[k][5] = vertices[m][5];
            fpolygon.vertices[k][6] = vertices[m][6];
            fpolygon.vertices[k][18] = vertices[m][18];
            fpolygon.vertices[k][19] = vertices[m][19];
            fpolygon.vertices[k][20] = vertices[m][20];
            if (textureImage != null)
            {
              fpolygon.vertices[k][7] = vertices[m][7];
              fpolygon.vertices[k][8] = vertices[m][8];
            }
          }
          fpolygon.render();
        }
      }
      if (stroke)
      {
        draw_lines(vertices, vertexCount - 1, 1, 2, 0);
        draw_lines(vertices, vertexCount - 2, 2, 1, 0);
      }
      break;
    case 20: 
      if (isConvex())
      {
        if ((fill) || (textureImage != null)) {
          fpolygon.renderPolygon(vertices, vertexCount);
        }
        if (stroke)
        {
          draw_lines(vertices, vertexCount - 1, 1, 1, 0);
          if (paramInt == 2) {
            draw_line(vertices[(vertexCount - 1)], vertices[0]);
          }
        }
      }
      else
      {
        if ((fill) || (textureImage != null))
        {
          boolean bool = smooth;
          if (stroke) {
            smooth = false;
          }
          concaveRender();
          if (stroke) {
            smooth = bool;
          }
        }
        if (stroke)
        {
          draw_lines(vertices, vertexCount - 1, 1, 1, 0);
          if (paramInt == 2) {
            draw_line(vertices[(vertexCount - 1)], vertices[0]);
          }
        }
      }
      break;
    }
    shape = 0;
  }
  
  private boolean isConvex()
  {
    if (vertexCount < 3) {
      return true;
    }
    int i = 0;
    for (int j = 0; j < vertexCount; j++)
    {
      float[] arrayOfFloat1 = vertices[j];
      float[] arrayOfFloat2 = vertices[((j + 1) % vertexCount)];
      float[] arrayOfFloat3 = vertices[((j + 2) % vertexCount)];
      float f = (arrayOfFloat2[18] - arrayOfFloat1[18]) * (arrayOfFloat3[19] - arrayOfFloat2[19]) - (arrayOfFloat2[19] - arrayOfFloat1[19]) * (arrayOfFloat3[18] - arrayOfFloat2[18]);
      if (f < 0.0F) {
        i |= 0x1;
      } else if (f > 0.0F) {
        i |= 0x2;
      }
      if (i == 3) {
        return false;
      }
    }
    return i != 0;
  }
  
  protected void concaveRender()
  {
    if ((vertexOrder == null) || (vertexOrder.length != vertices.length)) {
      vertexOrder = new int[vertices.length];
    }
    if (tpolygon == null) {
      tpolygon = new PPolygon(this);
    }
    tpolygon.reset(3);
    float f = 0.0F;
    int i = vertexCount - 1;
    int j = 0;
    while (j < vertexCount)
    {
      f += vertices[j][0] * vertices[i][1] - vertices[i][0] * vertices[j][1];
      i = j++;
    }
    if (f == 0.0F) {
      return;
    }
    float[] arrayOfFloat1 = vertices[0];
    float[] arrayOfFloat2 = vertices[(vertexCount - 1)];
    if ((Math.abs(arrayOfFloat1[0] - arrayOfFloat2[0]) < 1.0E-4F) && (Math.abs(arrayOfFloat1[1] - arrayOfFloat2[1]) < 1.0E-4F) && (Math.abs(arrayOfFloat1[2] - arrayOfFloat2[2]) < 1.0E-4F)) {
      vertexCount -= 1;
    }
    for (int k = 0; k < vertexCount; k++) {
      vertexOrder[k] = (f > 0.0F ? k : vertexCount - 1 - k);
    }
    k = vertexCount;
    int m = 2 * k;
    int n = 0;
    int i1 = k - 1;
    while (k > 2)
    {
      int i2 = 1;
      if (0 >= m--) {
        break;
      }
      int i3 = i1;
      if (k <= i3) {
        i3 = 0;
      }
      i1 = i3 + 1;
      if (k <= i1) {
        i1 = 0;
      }
      int i4 = i1 + 1;
      if (k <= i4) {
        i4 = 0;
      }
      double d1 = -10.0F * vertices[vertexOrder[i3]][0];
      double d2 = 10.0F * vertices[vertexOrder[i3]][1];
      double d3 = -10.0F * vertices[vertexOrder[i1]][0];
      double d4 = 10.0F * vertices[vertexOrder[i1]][1];
      double d5 = -10.0F * vertices[vertexOrder[i4]][0];
      double d6 = 10.0F * vertices[vertexOrder[i4]][1];
      if (9.999999747378752E-5D <= (d3 - d1) * (d6 - d2) - (d4 - d2) * (d5 - d1))
      {
        for (int i5 = 0; i5 < k; i5++) {
          if ((i5 != i3) && (i5 != i1) && (i5 != i4))
          {
            double d7 = -10.0F * vertices[vertexOrder[i5]][0];
            double d8 = 10.0F * vertices[vertexOrder[i5]][1];
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
              i2 = 0;
            }
          }
        }
        if (i2 != 0)
        {
          tpolygon.renderTriangle(vertices[vertexOrder[i3]], vertices[vertexOrder[i1]], vertices[vertexOrder[i4]]);
          n++;
          i5 = i1;
          for (int i6 = i1 + 1; i6 < k; i6++)
          {
            vertexOrder[i5] = vertexOrder[i6];
            i5++;
          }
          k--;
          m = 2 * k;
        }
      }
    }
  }
  
  public void point(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("point");
  }
  
  protected void rectImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if ((smooth) || (strokeAlpha) || (ctm.isWarped()))
    {
      super.rectImpl(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    else
    {
      int i = (int)(paramFloat1 + ctm.m02);
      int j = (int)(paramFloat2 + ctm.m12);
      int k = (int)(paramFloat3 + ctm.m02);
      int m = (int)(paramFloat4 + ctm.m12);
      if (fill) {
        simple_rect_fill(i, j, k, m);
      }
      if (stroke) {
        if (strokeWeight == 1.0F)
        {
          thin_flat_line(i, j, k, j);
          thin_flat_line(k, j, k, m);
          thin_flat_line(k, m, i, m);
          thin_flat_line(i, m, i, j);
        }
        else
        {
          thick_flat_line(i, j, strokeR, strokeG, strokeB, strokeA, k, j, strokeR, strokeG, strokeB, strokeA);
          thick_flat_line(k, j, strokeR, strokeG, strokeB, strokeA, k, m, strokeR, strokeG, strokeB, strokeA);
          thick_flat_line(k, m, strokeR, strokeG, strokeB, strokeA, i, m, strokeR, strokeG, strokeB, strokeA);
          thick_flat_line(i, m, strokeR, strokeG, strokeB, strokeA, i, j, strokeR, strokeG, strokeB, strokeA);
        }
      }
    }
  }
  
  private void simple_rect_fill(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt4 < paramInt2)
    {
      i = paramInt2;
      paramInt2 = paramInt4;
      paramInt4 = i;
    }
    if (paramInt3 < paramInt1)
    {
      i = paramInt1;
      paramInt1 = paramInt3;
      paramInt3 = i;
    }
    if ((paramInt1 > width1) || (paramInt3 < 0) || (paramInt2 > height1) || (paramInt4 < 0)) {
      return;
    }
    if (paramInt1 < 0) {
      paramInt1 = 0;
    }
    if (paramInt3 > width) {
      paramInt3 = width;
    }
    if (paramInt2 < 0) {
      paramInt2 = 0;
    }
    if (paramInt4 > height) {
      paramInt4 = height;
    }
    int i = paramInt3 - paramInt1;
    int j;
    int k;
    int m;
    if (fillAlpha)
    {
      for (j = paramInt2; j < paramInt4; j++)
      {
        k = j * width + paramInt1;
        for (m = 0; m < i; m++)
        {
          pixels[k] = blend_fill(pixels[k]);
          k++;
        }
      }
    }
    else
    {
      j = paramInt4 - paramInt2;
      k = paramInt2 * width + paramInt1;
      m = k;
      for (int n = 0; n < i; n++) {
        pixels[(k + n)] = fillColor;
      }
      for (n = 0; n < j; n++)
      {
        System.arraycopy(pixels, m, pixels, k, i);
        k += width;
      }
    }
  }
  
  protected void ellipseImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1;
    float f2;
    if ((smooth) || (strokeWeight != 1.0F) || (fillAlpha) || (strokeAlpha) || (ctm.isWarped()))
    {
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      float f3 = paramFloat1 + f1;
      float f4 = paramFloat2 + f2;
      float f5 = screenX(paramFloat1, paramFloat2);
      float f6 = screenY(paramFloat1, paramFloat2);
      float f7 = screenX(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
      float f8 = screenY(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
      int n = (int)(6.2831855F * PApplet.dist(f5, f6, f7, f8) / 8.0F);
      if (n < 4) {
        return;
      }
      float f9 = 720.0F / n;
      float f10 = 0.0F;
      boolean bool;
      int i1;
      if (fill)
      {
        bool = stroke;
        stroke = false;
        beginShape();
        for (i1 = 0; i1 < n; i1++)
        {
          vertex(f3 + cosLUT[((int)f10)] * f1, f4 + sinLUT[((int)f10)] * f2);
          f10 += f9;
        }
        endShape(2);
        stroke = bool;
      }
      if (stroke)
      {
        bool = fill;
        fill = false;
        f10 = 0.0F;
        beginShape();
        for (i1 = 0; i1 < n; i1++)
        {
          vertex(f3 + cosLUT[((int)f10)] * f1, f4 + sinLUT[((int)f10)] * f2);
          f10 += f9;
        }
        endShape(2);
        fill = bool;
      }
    }
    else
    {
      f1 = paramFloat3 / 2.0F;
      f2 = paramFloat4 / 2.0F;
      int i = (int)(paramFloat1 + f1 + ctm.m02);
      int j = (int)(paramFloat2 + f2 + ctm.m12);
      int k = (int)f1;
      int m = (int)f2;
      if (k == m)
      {
        if (fill) {
          flat_circle_fill(i, j, k);
        }
        if (stroke) {
          flat_circle_stroke(i, j, k);
        }
      }
      else
      {
        if (fill) {
          flat_ellipse_internal(i, j, k, m, true);
        }
        if (stroke) {
          flat_ellipse_internal(i, j, k, m, false);
        }
      }
    }
  }
  
  private void flat_circle_stroke(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = paramInt3;
    int k = 1;
    int m = 2 * paramInt3 - 1;
    int n = 0;
    while (i < j)
    {
      thin_point(paramInt1 + i, paramInt2 + j, strokeColor);
      thin_point(paramInt1 + j, paramInt2 - i, strokeColor);
      thin_point(paramInt1 - i, paramInt2 - j, strokeColor);
      thin_point(paramInt1 - j, paramInt2 + i, strokeColor);
      i++;
      n += k;
      k += 2;
      if (m < 2 * n)
      {
        j--;
        n -= m;
        m -= 2;
      }
      if (i > j) {
        break;
      }
      thin_point(paramInt1 + j, paramInt2 + i, strokeColor);
      thin_point(paramInt1 + i, paramInt2 - j, strokeColor);
      thin_point(paramInt1 - j, paramInt2 - i, strokeColor);
      thin_point(paramInt1 - i, paramInt2 + j, strokeColor);
    }
  }
  
  private void flat_circle_fill(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = paramInt3;
    int k = 1;
    int m = 2 * paramInt3 - 1;
    int n = 0;
    while (i < j)
    {
      for (int i1 = paramInt1; i1 < paramInt1 + i; i1++) {
        thin_point(i1, paramInt2 + j, fillColor);
      }
      for (i1 = paramInt1; i1 < paramInt1 + j; i1++) {
        thin_point(i1, paramInt2 - i, fillColor);
      }
      for (i1 = paramInt1 - i; i1 < paramInt1; i1++) {
        thin_point(i1, paramInt2 - j, fillColor);
      }
      for (i1 = paramInt1 - j; i1 < paramInt1; i1++) {
        thin_point(i1, paramInt2 + i, fillColor);
      }
      i++;
      n += k;
      k += 2;
      if (m < 2 * n)
      {
        j--;
        n -= m;
        m -= 2;
      }
      if (i > j) {
        break;
      }
      for (i1 = paramInt1; i1 < paramInt1 + j; i1++) {
        thin_point(i1, paramInt2 + i, fillColor);
      }
      for (i1 = paramInt1; i1 < paramInt1 + i; i1++) {
        thin_point(i1, paramInt2 - j, fillColor);
      }
      for (i1 = paramInt1 - j; i1 < paramInt1; i1++) {
        thin_point(i1, paramInt2 - i, fillColor);
      }
      for (i1 = paramInt1 - i; i1 < paramInt1; i1++) {
        thin_point(i1, paramInt2 + j, fillColor);
      }
    }
  }
  
  private final void flat_ellipse_symmetry(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      for (int i = paramInt1 - paramInt3 + 1; i < paramInt1 + paramInt3; i++)
      {
        thin_point(i, paramInt2 - paramInt4, fillColor);
        thin_point(i, paramInt2 + paramInt4, fillColor);
      }
    }
    else
    {
      thin_point(paramInt1 - paramInt3, paramInt2 + paramInt4, strokeColor);
      thin_point(paramInt1 + paramInt3, paramInt2 + paramInt4, strokeColor);
      thin_point(paramInt1 - paramInt3, paramInt2 - paramInt4, strokeColor);
      thin_point(paramInt1 + paramInt3, paramInt2 - paramInt4, strokeColor);
    }
  }
  
  private void flat_ellipse_internal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int k = paramInt3 * paramInt3;
    int m = paramInt4 * paramInt4;
    int i = 0;
    int j = paramInt4;
    int n = k * (1 - 2 * paramInt4) + 2 * m;
    int i1 = m - 2 * k * (2 * paramInt4 - 1);
    flat_ellipse_symmetry(paramInt1, paramInt2, i, j, paramBoolean);
    do
    {
      if (n < 0)
      {
        n += 2 * m * (2 * i + 3);
        i1 += 4 * m * (i + 1);
        i++;
      }
      else if (i1 < 0)
      {
        n += 2 * m * (2 * i + 3) - 4 * k * (j - 1);
        i1 += 4 * m * (i + 1) - 2 * k * (2 * j - 3);
        i++;
        j--;
      }
      else
      {
        n -= 4 * k * (j - 1);
        i1 -= 2 * k * (2 * j - 3);
        j--;
      }
      flat_ellipse_symmetry(paramInt1, paramInt2, i, j, paramBoolean);
    } while (j > 0);
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
    if (fill)
    {
      bool = stroke;
      stroke = false;
      i = (int)(-0.5F + paramFloat5 / 6.2831855F * 720.0F);
      j = (int)(0.5F + paramFloat6 / 6.2831855F * 720.0F);
      beginShape();
      vertex(f3, f4);
      for (k = i; k < j; k++)
      {
        m = k % 720;
        if (m < 0) {
          m += 720;
        }
        vertex(f3 + cosLUT[m] * f1, f4 + sinLUT[m] * f2);
      }
      endShape(2);
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
        int n = m % 720;
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
  
  public void box(float paramFloat)
  {
    showDepthWarning("box");
  }
  
  public void box(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarning("box");
  }
  
  public void sphereDetail(int paramInt)
  {
    showDepthWarning("sphereDetail");
  }
  
  public void sphereDetail(int paramInt1, int paramInt2)
  {
    showDepthWarning("sphereDetail");
  }
  
  public void sphere(float paramFloat)
  {
    showDepthWarning("sphere");
  }
  
  public void bezier(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    showDepthWarningXYZ("bezier");
  }
  
  public void curve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    showDepthWarningXYZ("curve");
  }
  
  protected void imageImpl(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramFloat3 - paramFloat1 == width) && (paramFloat4 - paramFloat2 == height) && (!tint) && (!ctm.isWarped())) {
      simple_image(paramPImage, (int)(paramFloat1 + ctm.m02), (int)(paramFloat2 + ctm.m12), paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      super.imageImpl(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  private void simple_image(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = paramInt1 + width;
    int j = paramInt2 + height;
    if ((paramInt1 > width1) || (i < 0) || (paramInt2 > height1) || (j < 0)) {
      return;
    }
    if (paramInt1 < 0)
    {
      paramInt3 -= paramInt1;
      paramInt1 = 0;
    }
    if (paramInt2 < 0)
    {
      paramInt4 -= paramInt2;
      paramInt2 = 0;
    }
    if (i > width)
    {
      paramInt5 -= i - width;
      i = width;
    }
    if (j > height)
    {
      paramInt6 -= j - height;
      j = height;
    }
    int k = paramInt4 * width + paramInt3;
    int m = paramInt2 * width;
    int n;
    int i1;
    int i2;
    if (format == 2)
    {
      for (n = paramInt2; n < j; n++)
      {
        i1 = 0;
        for (i2 = paramInt1; i2 < i; i2++) {
          pixels[(m + i2)] = blend_color(pixels[(m + i2)], pixels[(k + i1++)]);
        }
        k += width;
        m += width;
      }
    }
    else if (format == 4)
    {
      for (n = paramInt2; n < j; n++)
      {
        i1 = 0;
        for (i2 = paramInt1; i2 < i; i2++) {
          pixels[(m + i2)] = blend_color_alpha(pixels[(m + i2)], fillColor, pixels[(k + i1++)]);
        }
        k += width;
        m += width;
      }
    }
    else if (format == 1)
    {
      m += paramInt1;
      n = i - paramInt1;
      for (i1 = paramInt2; i1 < j; i1++)
      {
        System.arraycopy(pixels, k, pixels, m, n);
        k += width;
        m += width;
      }
    }
  }
  
  private void thin_point_at(int paramInt1, int paramInt2, float paramFloat, int paramInt3)
  {
    int i = paramInt2 * width + paramInt1;
    pixels[i] = paramInt3;
  }
  
  private void thin_point_at_index(int paramInt1, float paramFloat, int paramInt2)
  {
    pixels[paramInt1] = paramInt2;
  }
  
  private void thick_point(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7)
  {
    spolygon.reset(4);
    spolygon.interpARGB = false;
    float f = strokeWeight / 2.0F;
    float[] arrayOfFloat = spolygon.vertices[0];
    arrayOfFloat[18] = (paramFloat1 - f);
    arrayOfFloat[19] = (paramFloat2 - f);
    arrayOfFloat[20] = paramFloat3;
    arrayOfFloat[3] = paramFloat4;
    arrayOfFloat[4] = paramFloat5;
    arrayOfFloat[5] = paramFloat6;
    arrayOfFloat[6] = paramFloat7;
    arrayOfFloat = spolygon.vertices[1];
    arrayOfFloat[18] = (paramFloat1 + f);
    arrayOfFloat[19] = (paramFloat2 - f);
    arrayOfFloat[20] = paramFloat3;
    arrayOfFloat = spolygon.vertices[2];
    arrayOfFloat[18] = (paramFloat1 + f);
    arrayOfFloat[19] = (paramFloat2 + f);
    arrayOfFloat[20] = paramFloat3;
    arrayOfFloat = spolygon.vertices[3];
    arrayOfFloat[18] = (paramFloat1 - f);
    arrayOfFloat[19] = (paramFloat2 + f);
    arrayOfFloat[20] = paramFloat3;
    spolygon.render();
  }
  
  private void thin_flat_line(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int n = thin_flat_line_clip_code(paramInt1, paramInt2);
    int i1 = thin_flat_line_clip_code(paramInt3, paramInt4);
    if ((n & i1) != 0) {
      return;
    }
    int i2 = n | i1;
    float f5;
    int i;
    int j;
    int k;
    int m;
    if (i2 != 0)
    {
      float f1 = 0.0F;
      float f3 = 1.0F;
      f5 = 0.0F;
      for (i4 = 0; i4 < 4; i4++) {
        if ((i2 >> i4) % 2 == 1)
        {
          f5 = thin_flat_line_slope(paramInt1, paramInt2, paramInt3, paramInt4, i4 + 1);
          if ((n >> i4) % 2 == 1) {
            f1 = Math.max(f5, f1);
          } else {
            f3 = Math.min(f5, f3);
          }
        }
      }
      if (f1 > f3) {
        return;
      }
      i = (int)(paramInt1 + f1 * (paramInt3 - paramInt1));
      j = (int)(paramInt2 + f1 * (paramInt4 - paramInt2));
      k = (int)(paramInt1 + f3 * (paramInt3 - paramInt1));
      m = (int)(paramInt2 + f3 * (paramInt4 - paramInt2));
    }
    else
    {
      i = paramInt1;
      k = paramInt3;
      j = paramInt2;
      m = paramInt4;
    }
    i2 = 0;
    float f2 = m - j;
    float f4 = k - i;
    if (Math.abs(f2) > Math.abs(f4))
    {
      f5 = f2;
      f2 = f4;
      f4 = f5;
      i2 = 1;
    }
    int i3;
    if (f4 == 0) {
      i3 = 0;
    } else {
      i3 = (f2 << 16) / f4;
    }
    int i5;
    if (i == k)
    {
      if (j > m)
      {
        i4 = j;
        j = m;
        m = i4;
      }
      i4 = j * width + i;
      for (i5 = j; i5 <= m; i5++)
      {
        thin_point_at_index(i4, 0.0F, strokeColor);
        i4 += width;
      }
      return;
    }
    if (j == m)
    {
      if (i > k)
      {
        i4 = i;
        i = k;
        k = i4;
      }
      i4 = j * width + i;
      for (i5 = i; i5 <= k; i5++) {
        thin_point_at_index(i4++, 0.0F, strokeColor);
      }
      return;
    }
    if (i2 != 0)
    {
      if (f4 > 0)
      {
        f4 += j;
        i4 = 32768 + (i << 16);
        while (j <= f4)
        {
          thin_point_at(i4 >> 16, j, 0.0F, strokeColor);
          i4 += i3;
          j++;
        }
        return;
      }
      f4 += j;
      i4 = 32768 + (i << 16);
      while (j >= f4)
      {
        thin_point_at(i4 >> 16, j, 0.0F, strokeColor);
        i4 -= i3;
        j--;
      }
      return;
    }
    if (f4 > 0)
    {
      f4 += i;
      i4 = 32768 + (j << 16);
      while (i <= f4)
      {
        thin_point_at(i, i4 >> 16, 0.0F, strokeColor);
        i4 += i3;
        i++;
      }
      return;
    }
    f4 += i;
    int i4 = 32768 + (j << 16);
    while (i >= f4)
    {
      thin_point_at(i, i4 >> 16, 0.0F, strokeColor);
      i4 -= i3;
      i--;
    }
  }
  
  private int thin_flat_line_clip_code(float paramFloat1, float paramFloat2)
  {
    return (paramFloat2 < 0.0F ? 8 : 0) | (paramFloat2 > height1 ? 4 : 0) | (paramFloat1 < 0.0F ? 2 : 0) | (paramFloat1 > width1 ? 1 : 0);
  }
  
  private float thin_flat_line_slope(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
  {
    switch (paramInt)
    {
    case 4: 
      return -paramFloat2 / (paramFloat4 - paramFloat2);
    case 3: 
      return (height1 - paramFloat2) / (paramFloat4 - paramFloat2);
    case 2: 
      return -paramFloat1 / (paramFloat3 - paramFloat1);
    case 1: 
      return (width1 - paramFloat1) / (paramFloat3 - paramFloat1);
    }
    return -1.0F;
  }
  
  private void thick_flat_line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    spolygon.interpARGB = ((paramFloat3 != paramFloat9) || (paramFloat4 != paramFloat10) || (paramFloat5 != paramFloat11) || (paramFloat6 != paramFloat12));
    float f1 = paramFloat7 - paramFloat1 + 1.0E-4F;
    float f2 = paramFloat8 - paramFloat2 + 1.0E-4F;
    float f3 = (float)Math.sqrt(f1 * f1 + f2 * f2);
    float f4 = strokeWeight / f3 / 2.0F;
    float f5 = f4 * f2;
    float f6 = f4 * f1;
    float f7 = f4 * f2;
    float f8 = f4 * f1;
    spolygon.reset(4);
    float[] arrayOfFloat = spolygon.vertices[0];
    arrayOfFloat[18] = (paramFloat1 + f5);
    arrayOfFloat[19] = (paramFloat2 - f6);
    arrayOfFloat[3] = paramFloat3;
    arrayOfFloat[4] = paramFloat4;
    arrayOfFloat[5] = paramFloat5;
    arrayOfFloat[6] = paramFloat6;
    arrayOfFloat = spolygon.vertices[1];
    arrayOfFloat[18] = (paramFloat1 - f5);
    arrayOfFloat[19] = (paramFloat2 + f6);
    arrayOfFloat[3] = paramFloat3;
    arrayOfFloat[4] = paramFloat4;
    arrayOfFloat[5] = paramFloat5;
    arrayOfFloat[6] = paramFloat6;
    arrayOfFloat = spolygon.vertices[2];
    arrayOfFloat[18] = (paramFloat7 - f7);
    arrayOfFloat[19] = (paramFloat8 + f8);
    arrayOfFloat[3] = paramFloat9;
    arrayOfFloat[4] = paramFloat10;
    arrayOfFloat[5] = paramFloat11;
    arrayOfFloat[6] = paramFloat12;
    arrayOfFloat = spolygon.vertices[3];
    arrayOfFloat[18] = (paramFloat7 + f7);
    arrayOfFloat[19] = (paramFloat8 - f8);
    arrayOfFloat[3] = paramFloat9;
    arrayOfFloat[4] = paramFloat10;
    arrayOfFloat[5] = paramFloat11;
    arrayOfFloat[6] = paramFloat12;
    spolygon.render();
  }
  
  private void draw_line(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (strokeWeight == 1.0F)
    {
      if (line == null) {
        line = new PLine(this);
      }
      line.reset();
      line.setIntensities(paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16]);
      line.setVertices(paramArrayOfFloat1[18], paramArrayOfFloat1[19], paramArrayOfFloat1[20], paramArrayOfFloat2[18], paramArrayOfFloat2[19], paramArrayOfFloat2[20]);
      line.draw();
    }
    else
    {
      thick_flat_line(paramArrayOfFloat1[18], paramArrayOfFloat1[19], paramArrayOfFloat1[13], paramArrayOfFloat1[14], paramArrayOfFloat1[15], paramArrayOfFloat1[16], paramArrayOfFloat2[18], paramArrayOfFloat2[19], paramArrayOfFloat2[13], paramArrayOfFloat2[14], paramArrayOfFloat2[15], paramArrayOfFloat2[16]);
    }
  }
  
  private void draw_lines(float[][] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i;
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    if (strokeWeight == 1.0F)
    {
      i = 0;
      while (i < paramInt1)
      {
        if ((paramInt4 == 0) || ((i + paramInt2) % paramInt4 != 0))
        {
          arrayOfFloat1 = paramArrayOfFloat[i];
          arrayOfFloat2 = paramArrayOfFloat[(i + paramInt2)];
          if (line == null) {
            line = new PLine(this);
          }
          line.reset();
          line.setIntensities(arrayOfFloat1[13], arrayOfFloat1[14], arrayOfFloat1[15], arrayOfFloat1[16], arrayOfFloat2[13], arrayOfFloat2[14], arrayOfFloat2[15], arrayOfFloat2[16]);
          line.setVertices(arrayOfFloat1[18], arrayOfFloat1[19], arrayOfFloat1[20], arrayOfFloat2[18], arrayOfFloat2[19], arrayOfFloat2[20]);
          line.draw();
        }
        i += paramInt3;
      }
    }
    else
    {
      i = 0;
      while (i < paramInt1)
      {
        if ((paramInt4 == 0) || ((i + paramInt2) % paramInt4 != 0))
        {
          arrayOfFloat1 = paramArrayOfFloat[i];
          arrayOfFloat2 = paramArrayOfFloat[(i + paramInt2)];
          thick_flat_line(arrayOfFloat1[18], arrayOfFloat1[19], arrayOfFloat1[13], arrayOfFloat1[14], arrayOfFloat1[15], arrayOfFloat1[16], arrayOfFloat2[18], arrayOfFloat2[19], arrayOfFloat2[13], arrayOfFloat2[14], arrayOfFloat2[15], arrayOfFloat2[16]);
        }
        i += paramInt3;
      }
    }
  }
  
  private void thin_point(float paramFloat1, float paramFloat2, int paramInt)
  {
    int i = (int)(paramFloat1 + 0.4999F);
    int j = (int)(paramFloat2 + 0.4999F);
    if ((i < 0) || (i > width1) || (j < 0) || (j > height1)) {
      return;
    }
    int k = j * width + i;
    if ((paramInt & 0xFF000000) == -16777216)
    {
      pixels[k] = paramInt;
    }
    else
    {
      int m = paramInt >> 24 & 0xFF;
      int n = m ^ 0xFF;
      int i1 = strokeColor;
      int i2 = pixels[k];
      int i3 = n * (i2 >> 16 & 0xFF) + m * (i1 >> 16 & 0xFF) & 0xFF00;
      int i4 = n * (i2 >> 8 & 0xFF) + m * (i1 >> 8 & 0xFF) & 0xFF00;
      int i5 = n * (i2 & 0xFF) + m * (i1 & 0xFF) >> 8;
      pixels[k] = (0xFF000000 | i3 << 8 | i4 | i5);
    }
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    ctm.translate(paramFloat1, paramFloat2);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("translate");
  }
  
  public void rotate(float paramFloat)
  {
    ctm.rotate(paramFloat);
  }
  
  public void rotateX(float paramFloat)
  {
    showDepthWarning("rotateX");
  }
  
  public void rotateY(float paramFloat)
  {
    showDepthWarning("rotateY");
  }
  
  public void rotateZ(float paramFloat)
  {
    showDepthWarning("rotateZ");
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showVariationWarning("rotate(angle, x, y, z)");
  }
  
  public void scale(float paramFloat)
  {
    ctm.scale(paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    ctm.scale(paramFloat1, paramFloat2);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("scale");
  }
  
  public void skewX(float paramFloat)
  {
    ctm.shearX(paramFloat);
  }
  
  public void skewY(float paramFloat)
  {
    ctm.shearY(paramFloat);
  }
  
  public void pushMatrix()
  {
    if (matrixStackDepth == 32) {
      throw new RuntimeException("Too many calls to pushMatrix().");
    }
    ctm.get(matrixStack[matrixStackDepth]);
    matrixStackDepth += 1;
  }
  
  public void popMatrix()
  {
    if (matrixStackDepth == 0) {
      throw new RuntimeException("Too many calls to popMatrix(), and not enough to pushMatrix().");
    }
    matrixStackDepth -= 1;
    ctm.set(matrixStack[matrixStackDepth]);
  }
  
  public void resetMatrix()
  {
    ctm.reset();
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    ctm.apply(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    showDepthWarningXYZ("applyMatrix");
  }
  
  public void printMatrix()
  {
    ctm.print();
  }
  
  public float screenX(float paramFloat1, float paramFloat2)
  {
    return ctm.m00 * paramFloat1 + ctm.m01 * paramFloat2 + ctm.m02;
  }
  
  public float screenY(float paramFloat1, float paramFloat2)
  {
    return ctm.m10 * paramFloat1 + ctm.m11 * paramFloat2 + ctm.m12;
  }
  
  protected void backgroundImpl()
  {
    Arrays.fill(pixels, backgroundColor);
  }
  
  private final int blend_fill(int paramInt)
  {
    int i = fillAi;
    int j = i ^ 0xFF;
    int k = j * (paramInt >> 16 & 0xFF) + i * fillRi & 0xFF00;
    int m = j * (paramInt >> 8 & 0xFF) + i * fillGi & 0xFF00;
    int n = j * (paramInt & 0xFF) + i * fillBi & 0xFF00;
    return 0xFF000000 | k << 8 | m | n >> 8;
  }
  
  private final int blend_color(int paramInt1, int paramInt2)
  {
    int i = paramInt2 >>> 24;
    if (i == 255) {
      return paramInt2;
    }
    int j = i ^ 0xFF;
    int k = j * (paramInt1 >> 16 & 0xFF) + i * (paramInt2 >> 16 & 0xFF) & 0xFF00;
    int m = j * (paramInt1 >> 8 & 0xFF) + i * (paramInt2 >> 8 & 0xFF) & 0xFF00;
    int n = j * (paramInt1 & 0xFF) + i * (paramInt2 & 0xFF) >> 8;
    return 0xFF000000 | k << 8 | m | n;
  }
  
  private final int blend_color_alpha(int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt3 = paramInt3 * (paramInt2 >>> 24) >> 8;
    int i = paramInt3 ^ 0xFF;
    int j = i * (paramInt1 >> 16 & 0xFF) + paramInt3 * (paramInt2 >> 16 & 0xFF) & 0xFF00;
    int k = i * (paramInt1 >> 8 & 0xFF) + paramInt3 * (paramInt2 >> 8 & 0xFF) & 0xFF00;
    int m = i * (paramInt1 & 0xFF) + paramInt3 * (paramInt2 & 0xFF) >> 8;
    return 0xFF000000 | j << 8 | k | m;
  }
}
