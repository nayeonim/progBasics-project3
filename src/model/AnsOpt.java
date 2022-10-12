package model;

/**
 * Represents an answer option for a question.
 * @author stefanieim
 *
 */
public interface AnsOpt {
  
  /**
   * Returns the string content for this answer option.
   * @return the answer option string
   */
  String getAnsOptString();

  /**
   * Returns the string image url for this answer option object.
   * @return the answer option's image url
   * @throws IllegalStateException if the subclass is not visual.
   */
  String getAnsOptImageUrl() throws IllegalStateException;

}
