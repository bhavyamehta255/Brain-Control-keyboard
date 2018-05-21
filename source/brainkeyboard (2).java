import processing.core.*; 
import processing.xml.*; 

import neurosky.*; 
import org.json.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class brainkeyboard extends PApplet {



ThinkGearSocket neuroSocket;
int attention = 0;
int blinkSt = 0;
int blink = 0;
int k=0;
/*------------------------------------*/
String[] letter = {"A","B","C","D","E","F","G","H","I","J","K","L"," "," ","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","-","-"};
/*------------------------------------*/
String input = " "; 
String lockinput=" ";
int t=0;
PFont font;
PImage bg;
int i=30,j=110;
/*------------------------------------*/
public void setup() 
{
  size (550, 300);
  ThinkGearSocket neuroSocket = new ThinkGearSocket(this);
  try 
  {
    neuroSocket.start();
  } 
  catch (ConnectException e) {
    e.printStackTrace();
  }
  fill(0);
  font = createFont("Courier",48);
  textFont(font);
  bg=loadImage("1.jpg");

}
/*------------------------------------*/
public void draw() {
  background(120);
  fill(255, 255, 0);
  textSize(12);
  text("Attention: " + attention,100, 10);
  strokeJoin(ROUND);
  stroke(50,100,150);
  strokeWeight(3);
  fill(255);
  rect(30,30,500,50);
 /*----------------------------------------*/
  if (blinkSt>0) 
   {      
        if(letter[k]=="-"){
             input = input.substring(0,input.length()-1);
           }
          else{
                input = input+letter[k];
                lockinput = input;
                 fill(0);
                 textSize(18);
                 text(input, 40, 48, 195, 30);
                 textSize(48);
          }
      }
/*-----------------------------------------------------*/
  
  fill(255, 255, 0);
  textSize(12);
  text("Blink: " + blinkSt, 10, 10);
  
  blinkSt=0;
 fill(0);
 textSize(18);
 text(lockinput, 40, 48, 500, 30);
 textSize(48);
/*------------------------------------------------------*/
  image(bg,30,110);
  noFill();
  stroke(255);
  rect(i,j,50,50);
   print( "i:"+i +" " + "j:"+ j + " " + "k:"+ k + "  " +"Blinkst:" + blinkSt);
                 /*---------------------------------------------*/
                 if(attention > 80)
                 {
                   t=50;
                   println("  Attention: " + attention);
                    attention=0;
                  }
                 
                 if(t ==50){
                 
                  if(i<450){
                          i=i+50;
                            }
                        else{
                              i=30;
                              if(j<200)
                                {
                                 j=j+50;
                                }
                                else{
                                    j=110;
                                  }              
                        }
                /*-------------------------------------*/
                
                
                         if(k<29)
                         {
                            k =k+1;
                          }
                          else
                          {k=0;}
                          t=0;
                         
                          
                 }
                 
                 t++;
}
/*------------------------------------*/
public void blinkEvent(int blinkStrength) 
{
  blinkSt = blinkStrength;
 // blink = 1;
}
/*------------------------------------*/ 
public void attentionEvent(int attentionLevel) 
{
  attention = attentionLevel;
}
/*------------------------------------*/ 
public void stop() {
  neuroSocket.stop();
  super.stop();
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#ECE9D8", "brainkeyboard" });
  }
}
