package processing.core;

import java.util.HashMap;

public class PShape
  implements PConstants
{
  protected String name;
  protected HashMap<String, PShape> nameTable;
  public static final int GROUP = 0;
  public static final int PRIMITIVE = 1;
  public static final int PATH = 2;
  public static final int GEOMETRY = 3;
  protected int family;
  protected int primitive;
  protected PMatrix matrix;
  protected PImage image;
  public float width;
  public float height;
  public float depth;
  protected boolean visible = true;
  protected boolean stroke;
  protected int strokeColor;
  protected float strokeWeight;
  protected int strokeCap;
  protected int strokeJoin;
  protected boolean fill;
  protected int fillColor;
  protected boolean style = true;
  protected float[] params;
  protected int vertexCount;
  protected float[][] vertices;
  public static final int VERTEX = 0;
  public static final int BEZIER_VERTEX = 1;
  public static final int QUAD_BEZIER_VERTEX = 2;
  public static final int CURVE_VERTEX = 3;
  public static final int BREAK = 4;
  protected int vertexCodeCount;
  protected int[] vertexCodes;
  protected boolean close;
  protected PShape parent;
  protected int childCount;
  protected PShape[] children;
  
  public PShape()
  {
    family = 0;
  }
  
  public PShape(int paramInt)
  {
    family = paramInt;
  }
  
  public void setName(String paramString)
  {
    name = paramString;
  }
  
  public String getName()
  {
    return name;
  }
  
  public boolean isVisible()
  {
    return visible;
  }
  
  public void setVisible(boolean paramBoolean)
  {
    visible = paramBoolean;
  }
  
  public void disableStyle()
  {
    style = false;
    for (int i = 0; i < childCount; i++) {
      children[i].disableStyle();
    }
  }
  
  public void enableStyle()
  {
    style = true;
    for (int i = 0; i < childCount; i++) {
      children[i].enableStyle();
    }
  }
  
  public float getWidth()
  {
    return width;
  }
  
  public float getHeight()
  {
    return height;
  }
  
  public float getDepth()
  {
    return depth;
  }
  
  public boolean is3D()
  {
    return false;
  }
  
  protected void pre(PGraphics paramPGraphics)
  {
    if (matrix != null)
    {
      paramPGraphics.pushMatrix();
      paramPGraphics.applyMatrix(matrix);
    }
    if (style)
    {
      paramPGraphics.pushStyle();
      styles(paramPGraphics);
    }
  }
  
  protected void styles(PGraphics paramPGraphics)
  {
    if (stroke)
    {
      paramPGraphics.stroke(strokeColor);
      paramPGraphics.strokeWeight(strokeWeight);
      paramPGraphics.strokeCap(strokeCap);
      paramPGraphics.strokeJoin(strokeJoin);
    }
    else
    {
      paramPGraphics.noStroke();
    }
    if (fill) {
      paramPGraphics.fill(fillColor);
    } else {
      paramPGraphics.noFill();
    }
  }
  
  public void post(PGraphics paramPGraphics)
  {
    if (matrix != null) {
      paramPGraphics.popMatrix();
    }
    if (style) {
      paramPGraphics.popStyle();
    }
  }
  
  public void draw(PGraphics paramPGraphics)
  {
    if (visible)
    {
      pre(paramPGraphics);
      drawImpl(paramPGraphics);
      post(paramPGraphics);
    }
  }
  
  public void drawImpl(PGraphics paramPGraphics)
  {
    if (family == 0) {
      drawGroup(paramPGraphics);
    } else if (family == 1) {
      drawPrimitive(paramPGraphics);
    } else if (family == 3) {
      drawGeometry(paramPGraphics);
    } else if (family == 2) {
      drawPath(paramPGraphics);
    }
  }
  
  protected void drawGroup(PGraphics paramPGraphics)
  {
    for (int i = 0; i < childCount; i++) {
      children[i].draw(paramPGraphics);
    }
  }
  
  protected void drawPrimitive(PGraphics paramPGraphics)
  {
    if (primitive == 2)
    {
      paramPGraphics.point(params[0], params[1]);
    }
    else if (primitive == 4)
    {
      if (params.length == 4) {
        paramPGraphics.line(params[0], params[1], params[2], params[3]);
      } else {
        paramPGraphics.line(params[0], params[1], params[2], params[3], params[4], params[5]);
      }
    }
    else if (primitive == 8)
    {
      paramPGraphics.triangle(params[0], params[1], params[2], params[3], params[4], params[5]);
    }
    else if (primitive == 16)
    {
      paramPGraphics.quad(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
    }
    else if (primitive == 30)
    {
      if (image != null)
      {
        paramPGraphics.imageMode(0);
        paramPGraphics.image(image, params[0], params[1], params[2], params[3]);
      }
      else
      {
        paramPGraphics.rectMode(0);
        paramPGraphics.rect(params[0], params[1], params[2], params[3]);
      }
    }
    else if (primitive == 31)
    {
      paramPGraphics.ellipseMode(0);
      paramPGraphics.ellipse(params[0], params[1], params[2], params[3]);
    }
    else if (primitive == 32)
    {
      paramPGraphics.ellipseMode(0);
      paramPGraphics.arc(params[0], params[1], params[2], params[3], params[4], params[5]);
    }
    else if (primitive == 41)
    {
      if (params.length == 1) {
        paramPGraphics.box(params[0]);
      } else {
        paramPGraphics.box(params[0], params[1], params[2]);
      }
    }
    else if (primitive == 40)
    {
      paramPGraphics.sphere(params[0]);
    }
  }
  
  protected void drawGeometry(PGraphics paramPGraphics)
  {
    paramPGraphics.beginShape(primitive);
    int i;
    if (style) {
      for (i = 0; i < vertexCount; i++) {
        paramPGraphics.vertex(vertices[i]);
      }
    } else {
      for (i = 0; i < vertexCount; i++)
      {
        float[] arrayOfFloat = vertices[i];
        if (arrayOfFloat[2] == 0.0F) {
          paramPGraphics.vertex(arrayOfFloat[0], arrayOfFloat[1]);
        } else {
          paramPGraphics.vertex(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
        }
      }
    }
    paramPGraphics.endShape();
  }
  
  protected void drawPath(PGraphics paramPGraphics)
  {
    if (vertices == null) {
      return;
    }
    paramPGraphics.beginShape();
    int i;
    if (vertexCodeCount == 0)
    {
      if (vertices[0].length == 2) {
        for (i = 0; i < vertexCount; i++) {
          paramPGraphics.vertex(vertices[i][0], vertices[i][1]);
        }
      } else {
        for (i = 0; i < vertexCount; i++) {
          paramPGraphics.vertex(vertices[i][0], vertices[i][1], vertices[i][2]);
        }
      }
    }
    else
    {
      i = 0;
      int j;
      if (vertices[0].length == 2) {
        for (j = 0; j < vertexCodeCount; j++) {
          switch (vertexCodes[j])
          {
          case 0: 
            paramPGraphics.vertex(vertices[i][0], vertices[i][1]);
            i++;
            break;
          case 2: 
            paramPGraphics.quadVertex(vertices[(i + 0)][0], vertices[(i + 0)][1], vertices[(i + 1)][0], vertices[(i + 1)][1]);
            i += 2;
            break;
          case 1: 
            paramPGraphics.bezierVertex(vertices[(i + 0)][0], vertices[(i + 0)][1], vertices[(i + 1)][0], vertices[(i + 1)][1], vertices[(i + 2)][0], vertices[(i + 2)][1]);
            i += 3;
            break;
          case 3: 
            paramPGraphics.curveVertex(vertices[i][0], vertices[i][1]);
            i++;
          case 4: 
            paramPGraphics.breakShape();
          }
        }
      } else {
        for (j = 0; j < vertexCodeCount; j++) {
          switch (vertexCodes[j])
          {
          case 0: 
            paramPGraphics.vertex(vertices[i][0], vertices[i][1], vertices[i][2]);
            i++;
            break;
          case 2: 
            paramPGraphics.quadVertex(vertices[(i + 0)][0], vertices[(i + 0)][1], vertices[(i + 0)][2], vertices[(i + 1)][0], vertices[(i + 1)][1], vertices[(i + 0)][2]);
            i += 2;
            break;
          case 1: 
            paramPGraphics.bezierVertex(vertices[(i + 0)][0], vertices[(i + 0)][1], vertices[(i + 0)][2], vertices[(i + 1)][0], vertices[(i + 1)][1], vertices[(i + 1)][2], vertices[(i + 2)][0], vertices[(i + 2)][1], vertices[(i + 2)][2]);
            i += 3;
            break;
          case 3: 
            paramPGraphics.curveVertex(vertices[i][0], vertices[i][1], vertices[i][2]);
            i++;
          case 4: 
            paramPGraphics.breakShape();
          }
        }
      }
    }
    paramPGraphics.endShape(close ? 2 : 1);
  }
  
  public PShape getParent()
  {
    return parent;
  }
  
  public int getChildCount()
  {
    return childCount;
  }
  
  public PShape[] getChildren()
  {
    return children;
  }
  
  public PShape getChild(int paramInt)
  {
    return children[paramInt];
  }
  
  public PShape getChild(String paramString)
  {
    if ((name != null) && (name.equals(paramString))) {
      return this;
    }
    if (nameTable != null)
    {
      PShape localPShape1 = (PShape)nameTable.get(paramString);
      if (localPShape1 != null) {
        return localPShape1;
      }
    }
    for (int i = 0; i < childCount; i++)
    {
      PShape localPShape2 = children[i].getChild(paramString);
      if (localPShape2 != null) {
        return localPShape2;
      }
    }
    return null;
  }
  
  public PShape findChild(String paramString)
  {
    if (parent == null) {
      return getChild(paramString);
    }
    return parent.findChild(paramString);
  }
  
  public void addChild(PShape paramPShape)
  {
    if (children == null) {
      children = new PShape[1];
    }
    if (childCount == children.length) {
      children = ((PShape[])PApplet.expand(children));
    }
    children[(childCount++)] = paramPShape;
    parent = this;
    if (paramPShape.getName() != null) {
      addName(paramPShape.getName(), paramPShape);
    }
  }
  
  public void addChild(PShape paramPShape, int paramInt)
  {
    if (paramInt < childCount)
    {
      if (childCount == children.length) {
        children = ((PShape[])PApplet.expand(children));
      }
      for (int i = childCount - 1; i >= paramInt; i--) {
        children[(i + 1)] = children[i];
      }
      childCount += 1;
      children[paramInt] = paramPShape;
      parent = this;
      if (paramPShape.getName() != null) {
        addName(paramPShape.getName(), paramPShape);
      }
    }
  }
  
  public void removeChild(int paramInt)
  {
    if (paramInt < childCount)
    {
      PShape localPShape = children[paramInt];
      for (int i = paramInt; i < childCount - 1; i++) {
        children[i] = children[(i + 1)];
      }
      childCount -= 1;
      if ((localPShape.getName() != null) && (nameTable != null)) {
        nameTable.remove(localPShape.getName());
      }
    }
  }
  
  public void addName(String paramString, PShape paramPShape)
  {
    if (parent != null)
    {
      parent.addName(paramString, paramPShape);
    }
    else
    {
      if (nameTable == null) {
        nameTable = new HashMap();
      }
      nameTable.put(paramString, paramPShape);
    }
  }
  
  public int getChildIndex(PShape paramPShape)
  {
    for (int i = 0; i < childCount; i++) {
      if (children[i] == paramPShape) {
        return i;
      }
    }
    return -1;
  }
  
  public int getFamily()
  {
    return family;
  }
  
  public int getPrimitive()
  {
    return primitive;
  }
  
  public float[] getParams()
  {
    return getParams(null);
  }
  
  public float[] getParams(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat == null) || (paramArrayOfFloat.length != params.length)) {
      paramArrayOfFloat = new float[params.length];
    }
    PApplet.arrayCopy(params, paramArrayOfFloat);
    return paramArrayOfFloat;
  }
  
  public float getParam(int paramInt)
  {
    return params[paramInt];
  }
  
  public int getVertexCount()
  {
    return vertexCount;
  }
  
  public float[] getVertex(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= vertexCount))
    {
      String str = "No vertex " + paramInt + " for this shape, " + "only vertices 0 through " + (vertexCount - 1) + ".";
      throw new IllegalArgumentException(str);
    }
    return vertices[paramInt];
  }
  
  public float getVertexX(int paramInt)
  {
    return vertices[paramInt][0];
  }
  
  public float getVertexY(int paramInt)
  {
    return vertices[paramInt][1];
  }
  
  public float getVertexZ(int paramInt)
  {
    return vertices[paramInt][2];
  }
  
  public int[] getVertexCodes()
  {
    if (vertexCodes == null) {
      return null;
    }
    if (vertexCodes.length != vertexCodeCount) {
      vertexCodes = PApplet.subset(vertexCodes, 0, vertexCodeCount);
    }
    return vertexCodes;
  }
  
  public int getVertexCodeCount()
  {
    return vertexCodeCount;
  }
  
  public int getVertexCode(int paramInt)
  {
    return vertexCodes[paramInt];
  }
  
  public boolean isClosed()
  {
    return close;
  }
  
  public boolean contains(float paramFloat1, float paramFloat2)
  {
    if (family == 2)
    {
      boolean bool = false;
      int i = 0;
      for (int j = vertexCount - 1; i < vertexCount; j = i++) {
        if ((vertices[i][1] > paramFloat2 ? 1 : 0) != (vertices[j][1] > paramFloat2 ? 1 : 0)) {
          if (paramFloat1 < (vertices[j][0] - vertices[i][0]) * (paramFloat2 - vertices[i][1]) / (vertices[j][1] - vertices[i][1]) + vertices[i][0]) {
            bool = !bool;
          }
        }
      }
      return bool;
    }
    throw new IllegalArgumentException("The contains() method is only implemented for paths.");
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    checkMatrix(2);
    matrix.translate(paramFloat1, paramFloat2);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    checkMatrix(3);
    matrix.translate(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void rotateX(float paramFloat)
  {
    rotate(paramFloat, 1.0F, 0.0F, 0.0F);
  }
  
  public void rotateY(float paramFloat)
  {
    rotate(paramFloat, 0.0F, 1.0F, 0.0F);
  }
  
  public void rotateZ(float paramFloat)
  {
    rotate(paramFloat, 0.0F, 0.0F, 1.0F);
  }
  
  public void rotate(float paramFloat)
  {
    checkMatrix(2);
    matrix.rotate(paramFloat);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkMatrix(3);
    matrix.rotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void scale(float paramFloat)
  {
    checkMatrix(2);
    matrix.scale(paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    checkMatrix(2);
    matrix.scale(paramFloat1, paramFloat2);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    checkMatrix(3);
    matrix.scale(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void resetMatrix()
  {
    checkMatrix(2);
    matrix.reset();
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
    applyMatrix(m00, m01, 0.0F, m02, m10, m11, 0.0F, m12, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    checkMatrix(2);
    matrix.apply(paramFloat1, paramFloat2, paramFloat3, 0.0F, paramFloat4, paramFloat5, paramFloat6, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public void apply(PMatrix3D paramPMatrix3D)
  {
    applyMatrix(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    checkMatrix(3);
    matrix.apply(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
  }
  
  protected void checkMatrix(int paramInt)
  {
    if (matrix == null)
    {
      if (paramInt == 2) {
        matrix = new PMatrix2D();
      } else {
        matrix = new PMatrix3D();
      }
    }
    else if ((paramInt == 3) && ((matrix instanceof PMatrix2D))) {
      matrix = new PMatrix3D(matrix);
    }
  }
}
