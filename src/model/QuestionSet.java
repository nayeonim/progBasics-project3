package model;

import java.util.Map;

/**
 * Represents a question set which contains data required for each question in the Trivia game.
 * @author stefanieim
 *
 */
public interface QuestionSet {

  /**
   * Returns the id of this question set.
   * @return
   */
  int getId();

  /**
   * Returns the question string of this question set.
   * @return
   */
  String getQString();

  /**
   * Returns the map of answer options as indexed AnsOpt objects.
   * @return
   */
  Map<Integer, AnsOpt> getMapOfAnsOpts();

  /**
   * 
   * @return
   */
  int getCorrectAnsOptIdx();

  /**
   * Returns the question image url.
   * @return the question image url as a string
   * @throws IllegalStateException if question set is simple, therefore, does not have image urls.
   */
  String getQuestionImageUrl() throws IllegalStateException;

}
