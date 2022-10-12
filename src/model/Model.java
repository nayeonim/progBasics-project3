package model;

import java.util.Map;

import fileReader.VisualXmlFileReader;
import fileReader.XmlFileReader;
import main.Processing;

/**
 * Represents the model of the game, which stores all data necessary to play a Trivia game.
 * @author stefanieim
 */
public class Model {
  public enum ModelMode{SIMPLE, VISUAL;} //the model's mode options

  private final XmlFileReader reader;
  private GameState state;
  private int score;
  private Map<Integer, QuestionSet> allQuestionSets;
  private int currentQuestionSetIdx;

  /**
   * Constructs a model with a given XML reader object.
   * Sets up the game to be in it's INTRO stage, a score set to zero, the current question set's index (counter),
   * then calls the go() method of the reader, 
   * @param reader the given XML reader
   */
  public Model(XmlFileReader reader) {
    this.state = GameState.INTRO_CHOOSEMODE;
    this.score = 0;
    this.currentQuestionSetIdx = 0;
    this.reader = reader;
    this.reader.go();
    this.allQuestionSets = this.reader.getResultingQuestionSetList();
    //this.reader.consolePrint(); //uncomment for double-checking purposes
  }

  /**
   * Returns the index of the question set we are on.
   * @return the index of the current question set
   */
  public int getCurrentQuestionSetIdx() {
    return this.currentQuestionSetIdx;
  }

  /**
   * Moves onto the next question when called.
   */
  public void nextQuestionSet() {
    this.currentQuestionSetIdx++;
  }

  /**
   * Sets the model's state to the given GameState.
   * @param state the given state to set the model to
   */
  public void setState(GameState state) {
    this.state = state;
  }

  /**
   * Returns the current GameState of this model.
   * @return the current game state
   */
  public GameState getState() {
    return this.state;
  }

  /**
   * Returns the map of all question sets in this game model.
   * @return an indexed map of all QuestionSet objects
   */
  public  Map<Integer, QuestionSet> getAllQSets() {
    return this.allQuestionSets;
  }

  /**
   * Returns the QuestionSet object at the given index.
   * @param idx the given index at which the method retrieves a question set
   * @return the question set at the given index.
   */
  public QuestionSet getQSetAt(int idx) {
    return this.allQuestionSets.get(idx);
  }

  /**
   * Returns the current score of this game.
   * @return the current score
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Increases the score by one when called.
   */
  public void scoreUp() {
    this.score++;
  }
}



