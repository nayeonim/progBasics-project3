package model;

import java.util.Map;

/**
 * Represents a visual question set which contains all data required for a complete text-only question set,
 * as well as the image urls for the question and the answers
 * Contains an id, the question string, a map of answer options, and the index of the correct answer option.
 * @author stefanieim
 */
public class QuestionSetVisual extends QuestionSetSimple implements QuestionSet {
  private String qImageUrl;


  /**
   * Constructs a QuestionSetVisual with an id, question string, map of answer options, and the index for the correct answer,
   * as well as an image url for the question.
   * @param id the question's integer id (zero-based)
   * @param qString the question's content string (the actual question)
   * @param mapOfAnsOpts an indexed map of AnsOpt objects (zero-based)
   * @param correctAnsOptIdx the integer index of the correct answer option
   * @param imageUrl the string url for the question image.
   */
  public QuestionSetVisual(int id, String qString, Map<Integer, AnsOpt> mapOfAnsOpts, int correctAnsOptIdx, String imageUrl) {
    super(id, qString, mapOfAnsOpts, correctAnsOptIdx);
    this.qImageUrl = imageUrl;
  }


  @Override
  public String getQuestionImageUrl() throws IllegalStateException {
    return this.qImageUrl;
  }


}
