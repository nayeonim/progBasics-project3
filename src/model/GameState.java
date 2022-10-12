package model;

/**
 * Represents the different states of a Trivia game.
 * @author stefanieim
 *
 */
public enum GameState {
  INTRO_CHOOSEMODE,             //the initial screen that shows the game's title and the START button
  INTRO_START,
  QUESTION_SCREEN,   //the screen displaying a question after the player has started the game
  POPUP_CORRECT,     //the popup that is displayed when the answer choice is correct, containing the NEXT button
  POPUP_INCORRECT,   //the popup that is displayed when the answer choice is incorrect, containing the NEXT button
  POPUP_RESET,       //the popup that is displayed when the RESET button is pressed, asking the user to confirm or cancel the resetting
  FINAL;             //final screen with the score message, and a START OVER button (which is the reset button)
}