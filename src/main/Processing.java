package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileReader.SimpleXmlFileReader;
import fileReader.VisualXmlFileReader;
import fileReader.XmlFileReader;
import model.AnsOpt;
import model.Colors;
import model.Model;
import model.Model.ModelMode;
import model.QuestionSet;
import model.TextSize;
import model.GameState;
import model.Model;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;



/**
 * This class acts as the View and Controller for a Trivia game. 
 * It draws the canvas, as well as listen to mouse clicks in order to make changes to the data contained in the Model of the game.
 * 
 * The top section, "CONSTANTS FOR MANIPULATION" contains constants that the user can manipulate in order to change
 * the xml file to be loaded, the mode of the game (text-only or visual), and the screen size ratio value.
 * 
 * @author stefanieim
 *
 */
public class Processing extends PApplet {
  //----------------------------------------------------------------------------------------------
  //--CONSTANTS FOR MANIPULATION -----------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  //  type the name of the file to change the set of questions to be loaded 
  //  (uncomment one of the two lines below for convenience)
  public final static String FILE_NAME = "dataSet01_visual";
  //public final static String FILE_NAME = "dataSet02_textOnly";
  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  //  manipulate the DIVISOR constants to change the canvas dimensions
  //  (increase the divisor number to decrease the screen size)
  public final static double DIVISOR = 1.0; 
  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  //---------------------------------------------------------------------------------------------- 

  //canvas dimensions
  public final static int CANVAS_X = (int)(1280/DIVISOR);
  public final static int CANVAS_Y = (int)(720/DIVISOR);

  //the Alphabet (used for answer option labels)
  public final static String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", 
      "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

  //CONSTANTS for buttons
  public final static int BTN_WIDTH = CANVAS_X/8;
  public final static int BTN_HEIGHT = CANVAS_Y/15;
  public final static int TEXT_Y_ADJUSTMENT = BTN_HEIGHT/24;
  public final static int BTN_TEXTSIZE = TextSize.MEDIUM;
  public final static int[] BTN_COLOR_TEXT = Colors.WHITE;
  public final static int[] BTN_COLOR_START = Colors.BRICK;
  public final static int[] BTN_COLOR_STARTHOVER = Colors.BRICKHOVER;
  public final static int[] BTN_COLOR_RESET = Colors.BRICK;
  public final static int[] BTN_COLOR_RESETHOVER = Colors.BRICKHOVER;
  public final static int[] BTN_COLOR_NEXTQ = Colors.PURPLE_DARKEST;
  public final static int[] BTN_COLOR_NEXTQHOVER = Colors.PURPLE_DARKESTHOVER;

  //the buttons
  PShape btnVisual, btnSimple, btnStart, btnNextQ, btnReset, btnResetCancel, btnResetConfirm;

  //the model.
  private Model model;
  public Model.ModelMode modelMode;




  //-----------------------------------------------------------------------------------------------
  //SETUP------------------------------------------------------------------------------------------
  //-----------------------------------------------------------------------------------------------

  /**
   * This function will set up the initial canvas by running once when the program starts.
   */
  public void settings() {
    size(CANVAS_X, CANVAS_Y);
  }

  /**
   * This function will set up all variables by running once when the program starts.
   */
  public void setup() {
    this.model = createModel();
    consolePrintQuestionSets();
    background(color(Colors.PURPLE[0],Colors.PURPLE[1],Colors.PURPLE[2]));
    textAlign(CENTER,CENTER);
    rectMode(CENTER);
    imageMode(CENTER);
    this.btnSimple = createShape();
    this.btnVisual = createShape();
    this.btnStart = createShape();
    this.btnReset = createShape();
    this.btnNextQ = createShape();
    this.btnResetCancel = createShape();
    this.btnResetConfirm = createShape();
  }

  /**
   * Creates a model depending on the specified model mode,
   *  after creating a file reader that is fed into the model.
   * @return a Model object of the specified model mode.
   */
  private Model createModel() {
    XmlFileReader reader;
    if (FILE_NAME.contains("textOnly")) {
      modelMode = ModelMode.SIMPLE;
      reader = new SimpleXmlFileReader();
    }
    else {
      modelMode = ModelMode.VISUAL;
      reader = new VisualXmlFileReader();
    }
    return new Model(reader);
  }

  /**
   * Prints all the data within the map of question sets contained in the model, to the console.
   */
  private void consolePrintQuestionSets() {
    for (int q = 0; q < model.getAllQSets().size(); q++) {
      System.out.println("ID: " + model.getQSetAt(q).getId());
      System.out.print(model.getQSetAt(q).getQString());
      if (modelMode == ModelMode.VISUAL) {
        System.out.print(" (imageUrl: ");
        System.out.print(model.getQSetAt(q).getQuestionImageUrl() + ")");
      }
      System.out.println();
      for (int i = 0; i < model.getQSetAt(q).getMapOfAnsOpts().size(); i++) {
        System.out.print(ALPHABET[i] + ": ");
        System.out.print(model.getQSetAt(q).getMapOfAnsOpts().get(i).getAnsOptString());
        if (modelMode == ModelMode.VISUAL) {
          System.out.print(" (imageUrl: ");
          System.out.print(model.getQSetAt(q).getMapOfAnsOpts().get(i).getAnsOptImageUrl() + ")");
        }
        System.out.println();
      }
      System.out.println("correct: " + ALPHABET[model.getQSetAt(q).getCorrectAnsOptIdx()]);
      System.out.println();
    }
  }





  //-----------------------------------------------------------------------------------------------
  //DRAW-------------------------------------------------------------------------------------------
  //-----------------------------------------------------------------------------------------------

  /**
   * This method is executed repeatedly to draw onto the canvas at every frame.
   */
  public void draw() {

    switch (model.getState()) {

      case INTRO_CHOOSEMODE:
        background(color(Colors.PURPLE[0],Colors.PURPLE[1],Colors.PURPLE[2]));
        //title
        fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
        stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
        textSize(BTN_TEXTSIZE);
        text("Trivia!", CANVAS_X/2, CANVAS_Y/2 - (2*BTN_HEIGHT));
        //draw the choose mode msg
        drawModeMsg();
        //reset-CANCEL button
        drawButton(this.btnSimple, (int)((CANVAS_X/2)-(BTN_WIDTH/1.5)), CANVAS_Y/2 + BTN_HEIGHT,
            Colors.PURPLE_DARKER[0], Colors.PURPLE_DARKER[1], Colors.PURPLE_DARKER[2], 
            Colors.PURPLE_DARKERHOVER[0], Colors.PURPLE_DARKERHOVER[1], Colors.PURPLE_DARKERHOVER[2], 
            "TEXT-ONLY", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        //reset-CONFIRM button
        if (FILE_NAME.contains("textOnly")) {
          drawButton(this.btnVisual, (int)((CANVAS_X/2)+(BTN_WIDTH/1.5)), CANVAS_Y/2 + BTN_HEIGHT,
              Colors.GREY[0], Colors.GREY[1], Colors.GREY[2], 
              Colors.GREY[0], Colors.GREY[1], Colors.GREY[2], 
              "(DISABLED)", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        }
        else {
          drawButton(this.btnVisual, (int)((CANVAS_X/2)+(BTN_WIDTH/1.5)), CANVAS_Y/2 + BTN_HEIGHT,
              Colors.PURPLE_DARKER[0], Colors.PURPLE_DARKER[1], Colors.PURPLE_DARKER[2], 
              Colors.PURPLE_DARKERHOVER[0], Colors.PURPLE_DARKERHOVER[1], Colors.PURPLE_DARKERHOVER[2], 
              "VISUAL", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        }
        break;//-----------------------------------------------------------------------------------

      case INTRO_START: 
        background(color(Colors.PURPLE[0],Colors.PURPLE[1],Colors.PURPLE[2]));
        //title
        fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
        stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
        textSize(BTN_TEXTSIZE);
        text("Trivia!", CANVAS_X/2, CANVAS_Y/2 - (2*BTN_HEIGHT));
        drawModeMsg();
        //START button
        drawButton(this.btnStart, CANVAS_X/2, CANVAS_Y/2 + BTN_HEIGHT,
            Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2], 
            Colors.PURPLE_DARKESTHOVER[0], Colors.PURPLE_DARKESTHOVER[1], Colors.PURPLE_DARKESTHOVER[2],
            "START", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        //RESET button
        drawButton(this.btnReset, CANVAS_X-((BTN_WIDTH/2) + CANVAS_X/60), (int)(CANVAS_Y-(CANVAS_Y/8)*7.5),
            BTN_COLOR_START[0], BTN_COLOR_START[1], BTN_COLOR_START[2], 
            BTN_COLOR_STARTHOVER[0], BTN_COLOR_STARTHOVER[1], BTN_COLOR_STARTHOVER[2],
            "RESET", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      case QUESTION_SCREEN:
        background(color(Colors.PURPLE[0],Colors.PURPLE[1],Colors.PURPLE[2]));
        drawSectionBackgrounds();
        //current score
        drawCurrentScore();
        //current questionSet
        QuestionSet currentQSet = model.getQSetAt(model.getCurrentQuestionSetIdx());
        //current question number
        drawQuestionNumber(currentQSet.getId());
        //current question String
        drawQuestionString(currentQSet.getQString());
        //current question image
        drawQImageSquare(currentQSet);
        //current answer options
        drawAnsOpts(currentQSet.getMapOfAnsOpts());
        //RESET button
        drawButton(this.btnReset, CANVAS_X-((BTN_WIDTH/2) + CANVAS_X/60), (int)(CANVAS_Y-(CANVAS_Y/8)*7.5),
            BTN_COLOR_START[0], BTN_COLOR_START[1], BTN_COLOR_START[2], 
            BTN_COLOR_STARTHOVER[0], BTN_COLOR_STARTHOVER[1], BTN_COLOR_STARTHOVER[2],
            "RESET", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      case POPUP_CORRECT:
        //incorrect message pop up background
        drawResultPopUp(true);
        drawButton(this.btnReset, CANVAS_X-((BTN_WIDTH/2) + CANVAS_X/60), (int)(CANVAS_Y-(CANVAS_Y/8)*7.5),
            BTN_COLOR_START[0], BTN_COLOR_START[1], BTN_COLOR_START[2], 
            BTN_COLOR_STARTHOVER[0], BTN_COLOR_STARTHOVER[1], BTN_COLOR_STARTHOVER[2],
            "RESET", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      case POPUP_INCORRECT:
        //incorrect message pop up background
        drawResultPopUp(false);
        //RESET button
        drawButton(this.btnReset, CANVAS_X-((BTN_WIDTH/2) + CANVAS_X/60), (int)(CANVAS_Y-(CANVAS_Y/8)*7.5),
            BTN_COLOR_START[0], BTN_COLOR_START[1], BTN_COLOR_START[2], 
            BTN_COLOR_STARTHOVER[0], BTN_COLOR_STARTHOVER[1], BTN_COLOR_STARTHOVER[2],
            "RESET", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      case POPUP_RESET:
        drawResetPopUp();
        //reset-CANCEL button
        drawButton(this.btnResetCancel, (int)((CANVAS_X/2)-(BTN_WIDTH/1.5)), CANVAS_Y/2 + BTN_HEIGHT,
            Colors.BRICKLIGHTER[0], Colors.BRICKLIGHTER[1], Colors.BRICKLIGHTER[2], 
            Colors.BRICKLIGHTERHOVER[0], Colors.BRICKLIGHTERHOVER[1], Colors.BRICKLIGHTERHOVER[2], 
            "NVM", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        //reset-CONFIRM button
        drawButton(this.btnResetConfirm, (int)((CANVAS_X/2)+(BTN_WIDTH/1.5)), CANVAS_Y/2 + BTN_HEIGHT,
            Colors.BRICKDARKER[0], Colors.BRICKDARKER[1], Colors.BRICKDARKER[2], 
            Colors.BRICKDARKERHOVER[0], Colors.BRICKDARKERHOVER[1], Colors.BRICKDARKERHOVER[2], 
            "RESET", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      case FINAL:
        background(color(Colors.PURPLE_DARKEST[0],Colors.PURPLE_DARKEST[1],Colors.PURPLE_DARKEST[2]));
        //final score
        drawFinalScore();
        //button
        drawButton(this.btnReset, CANVAS_X-((BTN_WIDTH/2) + CANVAS_X/60), (int)(CANVAS_Y-(CANVAS_Y/8)*7.5),
            BTN_COLOR_START[0], BTN_COLOR_START[1], BTN_COLOR_START[2], 
            BTN_COLOR_STARTHOVER[0], BTN_COLOR_STARTHOVER[1], BTN_COLOR_STARTHOVER[2],
            "PLAY AGAIN", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
        break;//-----------------------------------------------------------------------------------

      default:
        throw new IllegalStateException("nonexistent game state");
    }
  }

  //DRAW helpers-----------------------------------------------------------------------------------

  /**
   * Draws a button onto the canvas.
   * @param button the PShape object of this button
   * @param centerX
   * @param centerY
   * @param r the r value for this button's color (rgb)
   * @param g the g value for this button's color (rgb)
   * @param b b the b value for this button's color (rgb)
   * @param hoverR the r value for this button's hover state color (rgb)
   * @param hoverG the g value for this button's hover state color (rgb)
   * @param hoverB the b value for this button's hover state color (rgb)
   * @param text the text to be displayed for this button
   * @param textR the r value for this button's text color (rgb)
   * @param textG the g value for this button's text color (rgb)
   * @param textB the b value for this button's text color (rgb)
   */
  private void drawButton(PShape button, int centerX, int centerY,
      int r, int g, int b, int hoverR, int hoverG, int hoverB, 
      String text, int textR, int textG, int textB) {
    button.beginShape();
    //hover
    if (((centerX - BTN_WIDTH/2) <= mouseX && mouseX <= (centerX + BTN_WIDTH/2))
        && ((centerY - BTN_HEIGHT/2) <= mouseY && mouseY <= (centerY + BTN_HEIGHT/2))) { 
      button.fill(color(hoverR,hoverG,hoverB));
      button.stroke(color(hoverR,hoverG,hoverB));
    }
    //normal
    else {
      button.fill(color(r,g,b));
      button.stroke(color(r,g,b));
    }
    //top left
    button.vertex((centerX - BTN_WIDTH/2), (centerY - BTN_HEIGHT/2)); 
    //top right
    button.vertex((centerX + BTN_WIDTH/2), (centerY - BTN_HEIGHT/2)); 
    //bottom right
    button.vertex((centerX + BTN_WIDTH/2), (centerY + BTN_HEIGHT/2)); 
    //bottom left
    button.vertex((centerX - BTN_WIDTH/2), (centerY + BTN_HEIGHT/2)); 
    button.endShape();
    shape(button);
    //text
    fill(color(textR, textG, textB));
    if ((button.equals(this.btnReset) && model.getState() == GameState.FINAL)
        || (button.equals(this.btnSimple) && model.getState() == GameState.INTRO_CHOOSEMODE)
        || (button.equals(this.btnVisual) && model.getState() == GameState.INTRO_CHOOSEMODE)){
      textSize(TextSize.SMALL);
    }
    else {
      textSize(BTN_TEXTSIZE);
    }
    text(text, centerX, centerY-TEXT_Y_ADJUSTMENT);
  }

  /**
   * Draws messages related to the mode selection (simple vs visual model).
   */
  private void drawModeMsg() {
    String mode;
    //before user choosing
    if (model.getState() == GameState.INTRO_CHOOSEMODE) { 
      mode = "choose the mode";
    }
    //after user choosing
    else {
      if (modelMode == Model.ModelMode.SIMPLE) {
        mode = "MODE: Text-Only";
      } else {
        mode = "MODE: Visual";
      }
    }
    fill(color(Colors.PURPLE_LIGHTER[0], Colors.PURPLE_LIGHTER[1], Colors.PURPLE_LIGHTER[2]));
    stroke(color(Colors.PURPLE_LIGHTER[0], Colors.PURPLE_LIGHTER[1], Colors.PURPLE_LIGHTER[2]));
    textSize(TextSize.SMALL);
    text(mode, CANVAS_X/2, CANVAS_Y/2 - BTN_HEIGHT);
  }

  /**
   * Draws the current score.
   */
  private void drawCurrentScore() {
    //draw score
    fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    String scoreStr = Integer.toString(model.getScore());
    textAlign(LEFT, CENTER);
    text("Score: " + scoreStr, CANVAS_X/60, CANVAS_X/30);
    textAlign(CENTER, CENTER);
  }

  /**
   * Draws the 3-sectioned background for the question/answer stage of the game.
   * The left box containing 
   */
  private void drawSectionBackgrounds() {
    //draw left box
    fill(color(Colors.PURPLE_LIGHTER[0],Colors.PURPLE_LIGHTER[1],Colors.PURPLE_LIGHTER[2]));
    stroke(color(Colors.PURPLE_LIGHTER[0],Colors.PURPLE_LIGHTER[1],Colors.PURPLE_LIGHTER[2]));
    rect((float)(CANVAS_X/6), (float)(CANVAS_Y-(((CANVAS_Y/8)*7))/2), 
        (float)(CANVAS_X/3), (float)((CANVAS_Y/8)*7));
    //draw right box
    fill(color(Colors.WHITE[0], Colors.WHITE[1],Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1],Colors.WHITE[2]));
    rect((float)((CANVAS_X/6)*4), (float)(CANVAS_Y-(((CANVAS_Y/8)*7))/2), 
        (float)(CANVAS_X-(CANVAS_X/3)), (float)((CANVAS_Y/8)*7));
  }

  /**
   * Draws the pop up screen of the result after a user chooses an answer,
   * depending on whether, the chosen answer is correct or incorrect.
   * @param choseCorrect whether the user has chosen the correct answer or not
   */
  private void drawResultPopUp(boolean choseCorrect) {
    String msg = "";
    if (choseCorrect) {
      fill(color(Colors.GREEN[0], Colors.GREEN[1], Colors.GREEN[2]));
      stroke(color(Colors.GREEN[0], Colors.GREEN[1], Colors.GREEN[2]));
      msg = "Correct!";
      rect((float)(CANVAS_X/2), (float)(CANVAS_Y/2), (float)(CANVAS_X/1.5), (float)(CANVAS_Y/2));
    }
    else {
      fill(color(Colors.RED[0], Colors.RED[1], Colors.RED[2]));
      stroke(color(Colors.RED[0], Colors.RED[1], Colors.RED[2]));
      rect((float)(CANVAS_X/2), (float)(CANVAS_Y/2), (float)(CANVAS_X/1.5), (float)(CANVAS_Y/2));
      int correctIdx = model.getQSetAt(model.getCurrentQuestionSetIdx()).getCorrectAnsOptIdx();
      String correctAnsString = model.getQSetAt(model.getCurrentQuestionSetIdx()).getMapOfAnsOpts().get(correctIdx).getAnsOptString();
      String correctAnsMsg = "The correct answer was... " + ALPHABET[correctIdx] + ": " + correctAnsString;
      textSize(TextSize.SMALL);
      fill(color(Colors.PINK[0], Colors.PINK[1], Colors.PINK[2]));
      stroke(color(Colors.PINK[0], Colors.PINK[1], Colors.PINK[2]));
      text(correctAnsMsg, CANVAS_X/2, (int)(CANVAS_Y/2.2));
      msg = "Incorrect!";
    }
    textSize(TextSize.MEDIUM);
    fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    text(msg, CANVAS_X/2, (int)(CANVAS_Y/2.5));
    drawButton(this.btnNextQ, CANVAS_X/2, CANVAS_Y/2 + BTN_HEIGHT,
        Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2], 
        Colors.PURPLE_DARKESTHOVER[0], Colors.PURPLE_DARKESTHOVER[1], Colors.PURPLE_DARKESTHOVER[2], 
        "NEXT", BTN_COLOR_TEXT[0], BTN_COLOR_TEXT[1], BTN_COLOR_TEXT[2]);
  }

  /**
   * Draws the given question number on the canvas.
   * @param qSetId the id of the question set to television.
   */
  private void drawQuestionNumber(int qSetId) {
    fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    textSize(TextSize.SMALL);
    text("Question " + (qSetId+1) + " out of " + model.getAllQSets().size(), (CANVAS_X/6), (CANVAS_Y/5));
  }

  /**
   * Draws the given question String onto the canvas. 
   * @param qString the given question string to draw
   */
  private void drawQuestionString(String qString) {
    fill(color(Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2]));
    stroke(color(Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2]));
    textSize(TextSize.SMALL);
    //
    String str = qString;
    int begin=0;
    int end = qString.length();
    int lengthToWrap = 24;
    //---referenced from https://stackoverflow.com/questions/23421306/splitting-a-string-over-multiple-lines
    List<String> brokenLines = new ArrayList<>();
    while (str.length() > lengthToWrap) {
      brokenLines.add(str.substring(begin,begin+lengthToWrap));
      str=str.substring(begin+lengthToWrap,end); //left over string after taking away substring
      end=end-lengthToWrap;
    }
    //---
    if (str != "") { //add the leftover string
      brokenLines.add(str);
    }
    String splitQString = "";
    for (int i = 0; i < brokenLines.size(); i++) {
      splitQString = splitQString + brokenLines.get(i) + "\n";
    }
    text(splitQString, (CANVAS_X/6), (CANVAS_Y/3));
  }

  /**
   * Draws either a blank square or an image for a given question set, depending on whether the game
   * is in text-only mode or image mode.
   * @param currentQSet the given question set to draw either a square or image for
   */
  private void drawQImageSquare(QuestionSet currentQSet) {
    //draw question image placeholder
    String qImageUrl;
    fill(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
    stroke(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
    rect((CANVAS_X/6), (int)((CANVAS_Y/6)*4.2), CANVAS_X/4, CANVAS_X/4);
    //draw question image if in visual mode
    if (modelMode == modelMode.VISUAL) {
      qImageUrl = currentQSet.getQuestionImageUrl();
      PImage qImage = loadImage("src/resources/"+FILE_NAME+"_images/"+qImageUrl);
      image(qImage, (CANVAS_X/6), (int)((CANVAS_Y/6)*4.2), CANVAS_X/4, CANVAS_X/4); //xywh
    }
  }

  /**
   * Draws the given map of answer options onto the canvas.
   * The current maximum number of answer options in the map for optimal visualization, 
   * without implementing a scroll function is five. From the sixth answer option, it will get cut off.
   * @param mapOfAnsOpts the map of answer options to draw
   */
  private void drawAnsOpts(Map<Integer, AnsOpt> mapOfAnsOpts) {
    for (int i = 0; i < mapOfAnsOpts.size(); i++) {
      //measurements: long rect (textbox)
      float longRectPosX = (float)(CANVAS_X-(CANVAS_X/3));
      float longRectWidth = CANVAS_Y;
      float longRectHeight = (float)(CANVAS_Y/10.8);
      //measurements: short rect (square)
      float shortRectHeight = (float)(CANVAS_Y/10.8);
      float shortRectWidth = shortRectHeight;
      float shortRectPosX = longRectPosX-(longRectWidth/2)+(shortRectWidth/2);
      //measurements: y
      int yStart = (int)((CANVAS_Y/5) + (longRectHeight/2)); //center offSet
      int ansOptBoxHeight = (int)(CANVAS_Y/10.8);
      int yGap = (int)(ansOptBoxHeight + (CANVAS_Y/18)); //1
      int posYbothRect = yStart + (yGap * i);
      //--------------
      //draw long rect
      //hover
      if ((longRectPosX-(longRectWidth/2) <= mouseX && mouseX <= (longRectPosX+(longRectWidth/2)))
          && (posYbothRect-(longRectHeight/2) <= mouseY && mouseY <= (posYbothRect+(longRectHeight/2)))) {
        fill(color(Colors.PURPLE_LIGHTESTHOVER[0], Colors.PURPLE_LIGHTESTHOVER[1], Colors.PURPLE_LIGHTESTHOVER[2]));
        stroke(color(Colors.PURPLE_LIGHTESTHOVER[0], Colors.PURPLE_LIGHTESTHOVER[1], Colors.PURPLE_LIGHTESTHOVER[2]));
      }
      //normal
      else {
        fill(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
        stroke(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
      }
      rect(longRectPosX, posYbothRect, longRectWidth, longRectHeight);
      //--------------
      //draw short rect
      // hover
      if ((longRectPosX-(longRectWidth/2) <= mouseX && mouseX <= (longRectPosX+(longRectWidth/2)))
          && (posYbothRect-(longRectHeight/2) <= mouseY && mouseY <= (posYbothRect+(longRectHeight/2)))) {
        fill(color(Colors.PURPLE_LIGHTERHOVER[0], Colors.PURPLE_LIGHTERHOVER[1], Colors.PURPLE_LIGHTERHOVER[2]));
        stroke(color(Colors.PURPLE_LIGHTERHOVER[0], Colors.PURPLE_LIGHTERHOVER[1], Colors.PURPLE_LIGHTERHOVER[2]));
      }
      // normal
      else {
        fill(color(Colors.PURPLE_LIGHTER[0], Colors.PURPLE_LIGHTER[1], Colors.PURPLE_LIGHTER[2]));
        stroke(color(Colors.PURPLE_LIGHTER[0], Colors.PURPLE_LIGHTER[1], Colors.PURPLE_LIGHTER[2]));
      }
      rect(shortRectPosX, posYbothRect, shortRectWidth, shortRectHeight);
      //--------------
      //draw the text
      textSize(TextSize.MEDIUM);
      textAlign(CENTER, CENTER);
      AnsOpt thisAnsOpt = mapOfAnsOpts.get(i);
      fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
      stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
      text(ALPHABET[i], shortRectPosX,posYbothRect);
      textAlign(LEFT, CENTER);
      String ansOptString = thisAnsOpt.getAnsOptString();
      if ((longRectPosX-(longRectWidth/2) <= mouseX && mouseX <= (longRectPosX+(longRectWidth/2)))
          && (posYbothRect-(longRectHeight/2) <= mouseY && mouseY <= (posYbothRect+(longRectHeight/2)))) {
        fill(color(Colors.PURPLE[0], Colors.PURPLE[1], Colors.PURPLE[2]));
        stroke(color(Colors.PURPLE[0], Colors.PURPLE[1], Colors.PURPLE[2]));
      }
      //normal
      else {
        fill(color(Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2]));
        stroke(color(Colors.PURPLE_DARKEST[0], Colors.PURPLE_DARKEST[1], Colors.PURPLE_DARKEST[2]));
      }
      text(ansOptString,
          (int)(longRectPosX-(longRectWidth/2)+(shortRectWidth*1.5)),
          posYbothRect);
      if (modelMode == modelMode.VISUAL) {
        String ansOptImageUrl = thisAnsOpt.getAnsOptImageUrl();
        PImage aOImage = loadImage("src/resources/"+FILE_NAME+"_images/"+ansOptImageUrl);
        image(aOImage, (float)(shortRectPosX+0.2), (float)(posYbothRect+0.2), shortRectWidth, shortRectHeight);
      }
      textAlign(CENTER, CENTER);
    }
  }

  /**
   * Draws the pop up screen when a user clicks on the reset button.
   * Asks the user if they are sure they want to start over.
   */
  private void drawResetPopUp() {
    //bg rect
    fill(color(Colors.BRICK[0], Colors.BRICK[1], Colors.BRICK[2]));
    stroke(color(Colors.BRICK[0], Colors.BRICK[1], Colors.BRICK[2]));
    rect((float)(CANVAS_X/2), (float)(CANVAS_Y/2), (float)(CANVAS_X/1.5), (float)(CANVAS_Y/2));
    //text
    textSize(TextSize.MEDIUM);
    fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    text("Are you sure you want to start over?", CANVAS_X/2, (int)(CANVAS_Y/2.5));
  }

  /**
   * Draws the final screen, containing the final score, onto the canvas.
   */
  private void drawFinalScore() {
    //msg
    textSize(TextSize.MEDIUM);
    fill(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
    stroke(color(Colors.PURPLE_LIGHTEST[0], Colors.PURPLE_LIGHTEST[1], Colors.PURPLE_LIGHTEST[2]));
    text("Your Final Score is...", CANVAS_X/2, (int)(CANVAS_Y/2.5));
    //score
    textSize(TextSize.LARGE);
    fill(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    stroke(color(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]));
    text(model.getScore(), CANVAS_X/2, (int)(CANVAS_Y/2));
  }





  //-----------------------------------------------------------------------------------------------
  //MOUSECLICK-------------------------------------------------------------------------------------
  //-----------------------------------------------------------------------------------------------

  /**
   * Executes commands for when a mouse click occurs while the program is running
   */
  public void mouseClicked() {
    switch (model.getState()) {
      case INTRO_CHOOSEMODE:
        modeButtonListener();
        break;
      case INTRO_START: 
        startButtonListener();
        resetButtonListener();
        break;
      case QUESTION_SCREEN:
        answerOptionClickListener();
        resetButtonListener();
        break;
      case POPUP_CORRECT:
        nextButtonListener();
        resetButtonListener();
        break;
      case POPUP_INCORRECT:
        nextButtonListener();
        resetButtonListener();
        break;
      case POPUP_RESET:
        resetConfirmButtonListener();
        resetCancelButtonListener();
        break;
      case FINAL:
        background(color(Colors.PURPLE_DARKEST[0],Colors.PURPLE_DARKEST[1],Colors.PURPLE_DARKEST[2]));
        resetButtonListener();
        break;
      default:
        throw new IllegalStateException("nonexistent game state");
    }
  }

  //MOUSECLICK helpers-----------------------------------------------------------------------------

  /**
   * Listens to whether the user has clicked on one of two mode choice buttons: 
   * the simple(text-only) mode versus the visual(image) mode.
   * Sets the game model mode to the corresponding mode to the button clicked.
   */
  private void modeButtonListener() {
    if (mouseOnButton(this.btnSimple)) {
      modelMode = Model.ModelMode.SIMPLE;
      model.setState(GameState.INTRO_START);
    }
    //disable the visual mode if the loaded file is text only
    if (!(FILE_NAME.contains("textOnly"))) { 
      if (mouseOnButton(this.btnVisual)) {
        modelMode = Model.ModelMode.VISUAL;
        model.setState(GameState.INTRO_START);
      }
    }
  }

  /**
   * Listens to whether the user has clicked on the START button.
   * Sets the game to the next state (the question/answer game-play stage)
   */
  private void startButtonListener() {
    if (mouseOnButton(this.btnStart)) {
      model.setState(GameState.QUESTION_SCREEN);
    }
  }

  /**
   * Listens to whether the user has clicked on an answer option.
   * If the clicked answer is correct, the score is incremented by one and the correct pop up message is shown.
   * If the clicked answer is incorrect, the incorrect pop up message is shown.
   */
  private void answerOptionClickListener() {
    Map<Integer, AnsOpt> mapOfAnsOpts = model.getQSetAt(model.getCurrentQuestionSetIdx()).getMapOfAnsOpts();
    for (int i = 0; i < mapOfAnsOpts.size(); i++) {
      //measurements: long rect (textbox)
      float longRectPosX = (float)(CANVAS_X-(CANVAS_X/3));
      float longRectWidth = CANVAS_Y;
      float longRectHeight = (float)(CANVAS_Y/10.8);
      //measurements: short rect (square)
      float shortRectHeight = (float)(CANVAS_Y/10.8);
      float shortRectWidth = shortRectHeight;
      float shortRectPosX = longRectPosX-(longRectWidth/2)+(shortRectWidth/2);
      //measurements: y
      int yStart = (int)((CANVAS_Y/5) + (longRectHeight/2)); //center offSet
      int ansOptBoxHeight = (int)(CANVAS_Y/10.8);
      int yGap = (int)(ansOptBoxHeight + (CANVAS_Y/18)); //1
      int posYbothRect = yStart + (yGap * i);
      //correctAnsIndex
      int correctAnsIndex = model.getQSetAt(model.getCurrentQuestionSetIdx()).getCorrectAnsOptIdx();
      if ((longRectPosX-(longRectWidth/2) <= mouseX && mouseX <= (longRectPosX+(longRectWidth/2)))
          && (posYbothRect-(longRectHeight/2) <= mouseY && mouseY <= (posYbothRect+(longRectHeight/2)))) {
        if (correctAnsIndex == i) { //correct
          model.scoreUp();
          model.setState(GameState.POPUP_CORRECT);
        }
        else { //incorrect
          model.setState(GameState.POPUP_INCORRECT);
        }
      }
    }
  }

  /**
   * Listens to the NEXT button in the correct/incorrect pop-up message. 
   * Moves onto the next question, and closes the pop-up, except for when the user is on the last question,
   * for which the final screen is shown.
   */
  private void nextButtonListener() {
    if (mouseOnButton(this.btnNextQ)) {
      if (model.getCurrentQuestionSetIdx() == model.getAllQSets().size() - 1) { //we are on the last question
        model.setState(GameState.FINAL);
      }
      else {
        model.nextQuestionSet();
        model.setState(GameState.QUESTION_SCREEN);
      }
    }
  }

  /**
   * Listens to the RESET button in the top right corner of the canvas.
   * Shows an "are you sure?" popup when clicked on.
   */
  private void resetButtonListener() {
    if (mouseOnButton(this.btnReset)) {
      if (model.getState()==GameState.FINAL) {
        model = createModel();
      }
      else {
        model.setState(GameState.POPUP_RESET);
      }
    }
  }

  /**
   * Listens to the reset confirm button on the "are you sure?" popup message and resets the game when clicked on.
   */
  private void resetConfirmButtonListener() {
    if (mouseOnButton(this.btnResetConfirm)) {
      model = createModel();
    }
  }

  /**
   *  Listens to the cancel button on the "are you sure?" pop-up message. Closes the pop-up.
   */
  private void resetCancelButtonListener() {
    if (mouseOnButton(this.btnResetCancel)) {
      model.setState(GameState.QUESTION_SCREEN);
    }
  }

  /**
   * Determines if the mouse has clicked on the given button
   * @param btn the given button to be checked for mouse click
   * @return true if the mouse click's position is within the given button's area
   */
  private boolean mouseOnButton(PShape button) {
    return (button.getVertexX(0) <= mouseX && mouseX <= button.getVertexX(0)+ BTN_WIDTH)
        && (button.getVertexY(0) <= mouseY && mouseY <= button.getVertexY(0)+ BTN_HEIGHT);
  }





  //----------------------------------------------------------------------------
  //MAIN------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  // Driver code
  public static void main(String[] args) {
    PApplet.main(new String[] {"--present", "main.Processing"});
  }

}
