package model;

import java.util.Map;

/**
 * Represents a simple question set which contains all data required for a complete text-only question set.
 * Contains an id, the question string, a map of answer options, and the index of the correct answer option.
 * @author stefanieim
 */
public class QuestionSetSimple implements QuestionSet {
  private int id;
  private String qString;
  private Map<Integer, AnsOpt> mapOfAnsOpts;
  private int correctAnsOptIdx;

  /**
   * Constructs a QuestionSetSimple with an id, question string, map of answer options, and the index for the correct answer.
   * @param id the question's integer id (zero-based)
   * @param qString the question's content string (the actual question)
   * @param mapOfAnsOpts an indexed map of AnsOpt objects (zero-based)
   * @param correctAnsOptIdx the integer index of the correct answer option
   */
  public QuestionSetSimple(int id, String qString, Map<Integer, AnsOpt> mapOfAnsOpts, int correctAnsOptIdx) {
    this.id = id;
    this.qString = qString;
    this.mapOfAnsOpts = mapOfAnsOpts;
    this.correctAnsOptIdx = correctAnsOptIdx;
  }
  
  @Override
  public int getId() {
    return this.id;
  }
  
  @Override
  public String getQString() {
    return this.qString;
  }
  
  @Override
  public Map<Integer, AnsOpt> getMapOfAnsOpts() {
    return this.mapOfAnsOpts;
  }
  
  @Override
  public int getCorrectAnsOptIdx() {
    return this.correctAnsOptIdx;
  }


  @Override
  public String getQuestionImageUrl() throws IllegalStateException{
    throw new IllegalStateException("Simple QuestionSet does not have question image");
  }

  
}
