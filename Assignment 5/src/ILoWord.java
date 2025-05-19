import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color;

//represents a list of words
interface ILoWord {

  // eliminates the first letter of active words in this list if they
  // match the given letter
  ILoWord checkAndReduce(String letter);

  // add the given IWord to the end of the list
  ILoWord addToEnd(IWord end);

  // filters out words with empty strings
  ILoWord filterOutEmpties();

  // draws the words in the list onto the world scene
  WorldScene draw(WorldScene w);

  // moves words in an ILoWord
  ILoWord move();

  // checks to see if the y of any words exceed the background border
  boolean yExceed();

  // finds a new active word in a given IWord list
  ILoWord findNewActive(String key);

  // checks to see if there are any active words in an list of words
  boolean anyActiveWords();

  // checks to see if there are any active words, and looks for one if there is
  // not
  ILoWord checkAndReduceActive(String key);

}

//represents an empty list of words
class MtLoWord implements ILoWord {

  MtLoWord() {
  }

  // Template:
  /*
   * fields:
   * none
   * methods:
   * this.checkAndReduce(String)...ILoWord
   * this.addToEnd(IWord)...ILoWord
   * this.filterOutEmpties()...ILoWord
   * this.draw(WorldScene)...WorldScene
   * this.move() ... ILoWord
   * this.yExceed() ... boolean
   * this.findNewActive(String) ... ILoWord
   * this.anyActiveWords() ... boolean
   * this.checkAndReduceActive(String) ... ILoWord
   * methods of fields:
   * 
   */

  // eliminates the first letter of active words in this list if they
  // match the given letter
  public ILoWord checkAndReduce(String letter) {
    return this;
  }

  // add the given IWord to the end of the list
  public ILoWord addToEnd(IWord end) {
    return new ConsLoWord(end, new MtLoWord());
  }

  // filters out words with empty strings
  public ILoWord filterOutEmpties() {
    return new MtLoWord();
  }

  // draws the words in the list onto the world scene
  public WorldScene draw(WorldScene w) {
    return w;
  }

  // returns an MtLoWord since there are not any words to remove
  public ILoWord move() {
    return this;
  }

  // returns false since there is no IWord in an MtList
  public boolean yExceed() {
    // TODO Auto-generated method stub
    return false;
  }

  // returns the empty list since there are not any words in the empty list
  // that can be active
  public ILoWord findNewActive(String key) {
    // TODO Auto-generated method stub
    return this;
  }

  // returns false since the empty list does not contain any active words
  public boolean anyActiveWords() {
    // TODO Auto-generated method stub
    return false;
  }

  // returns the empty list since there are not any words in the empty list
  public ILoWord checkAndReduceActive(String key) {
    // TODO Auto-generated method stub
    return this;
  }
}

class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  // Template:
  /*
   * fields:
   * this.first...IWord
   * this.rest...ILoWord
   * methods:
   * this.checkAndReduce(String)...ILoWord
   * this.addToEnd(IWord)...ILoWord
   * this.filterOutEmpties()...ILoWord
   * this.draw(WorldScene)...WorldScene
   * this.move() ... ILoWord
   * this.yExceed() ... boolean
   * this.findNewActive(String) ... ILoWord
   * this.anyActiveWords() ... boolean
   * this.checkAndReduceActive(String) ... ILoWord
   * methods of fields:
   * this.first.beforeWord(IWord)...boolean
   * this.first.helperWord()...String
   * this.first.reduceWord(String)...IWord
   * this.first.drawTheWord(WorldScene)...WorldScene
   * this.first.move() ... IWord
   * this.first.greaterThanY() ... boolean
   * this.first.isActive(IWord) ... boolean
   * this.first.activeHuh(ActiveWord) ... boolean
   * this.first.inactiveHuh(InactiveWord) ... boolean
   * this.first.switchActive() ... IWord
   */

  // eliminates the first letter of active words in this list if they
  // match the given letter
  public ILoWord checkAndReduce(String letter) {

    /*
     * IWord reducedFirst = this.first.reduceWord(letter);
     * if(!reducedFirst.equals(this.first) && this.rest.highestY(reducedFirst)) {
     * return new ConsLoWord(reducedFirst, this.rest);
     * }
     * else {
     * return new ConsLoWord(this.first, this.rest.checkAndReduce(letter));
     * }
     */
    return new ConsLoWord(this.first.reduceWord(letter), this.rest.checkAndReduce(letter));
  }

  // add the given IWord to the end of the list
  public ILoWord addToEnd(IWord end) {
    return new ConsLoWord(this.first, this.rest.addToEnd(end));
  }

  // filters out words with empty strings
  public ILoWord filterOutEmpties() {
    if (this.first.isEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // draws the words in the list onto the world scene
  public WorldScene draw(WorldScene w) {
    return this.rest.draw(this.first.drawTheWord(w));
  }

  // moves the words in a list of words
  public ILoWord move() {
    return new ConsLoWord(this.first.move(), this.rest.move());
  }

  // checks to see if any words in a list exceed a certain y value
  public boolean yExceed() {
    // TODO Auto-generated method stub
    return this.first.greaterThanY() || this.rest.yExceed();
  }

  @Override
  public ILoWord findNewActive(String key) {
    if (this.first.switchActive(key).isActive(this.first.switchActive(key))) {
      return new ConsLoWord(this.first.switchActive(key).reduceWord(key), this.rest);
    }
    else {
      return new ConsLoWord(this.first, this.rest.findNewActive(key));
    }
  }

  @Override
  public boolean anyActiveWords() {
    // TODO Auto-generated method stub
    return this.first.isActive(this.first) || this.rest.anyActiveWords();
  }

  @Override
  public ILoWord checkAndReduceActive(String key) {
    if (this.anyActiveWords()) {
      return this;
    }
    else {
      return this.findNewActive(key);
    }
  }

}

//represents a word in the ZType game
interface IWord {
  // reduces a letter from the word if it matches the given one
  IWord reduceWord(String letter);

  // draws the word on the worldscene
  WorldScene drawTheWord(WorldScene w);

  // moves the word down the screen
  IWord move();

  // checks if the word is empty or not
  boolean isEmpty();

  // checks to see if the y value of a word exceeds a certain point
  boolean greaterThanY();

  // checks to see if a word is active
  boolean isActive(IWord w);

  // double dispatch to see if a word is active
  boolean activeHuh(ActiveWord w);

  //double dispatch to see if a word is active
  boolean inactiveHuh(InactiveWord w);

  // switches a word's type
  IWord switchActive(String s);

}

abstract class AWord implements IWord {
  String word;
  int x;
  int y;

  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  public boolean isEmpty() {
    return this.word.equals("");
  }

  // Template:
  /*
   * fields:
   * this.word...String
   * this.x...int
   * this.y...int
   * methods:
   * this.isEmpty()...boolean
   * methods of fields: none
   */

}

//represents an active word in the ZType game
class ActiveWord extends AWord {

  ActiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  // Template:
  /*
   * fields:
   * this.word...Word
   * this.x...int
   * this.y...y
   * methods:
   * this.reduceWord(String)...IWord
   * this.drawsTheWord(WorldScene)...WorldScene
   * this.move() ... IWord
   * this.greaterThanY() ... boolean
   * this.isActive(IWord) ... boolean
   * this.activeHuh(ActiveWord) ... boolean
   * this.inactiveHuh(InactiveWord) ... boolean
   * this.switchActive() ... IWord
   */

  // reduces a letter from the word if it matches the given one
  public IWord reduceWord(String letter) {
    if (this.word.substring(0, 1).equals(letter)) {
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
    else {
      return this;
    }
  }

  // draws the word on the WorldScene
  public WorldScene drawTheWord(WorldScene w) {
    return w.placeImageXY(new TextImage(this.word, 15, FontStyle.BOLD, Color.GREEN), this.x,
        this.y);
  }

  // moves the word down the screen
  public IWord move() {
    return new ActiveWord(this.word, this.x, this.y + 1);
  }

  // checks to see if the y of a given word exceeds 400
  public boolean greaterThanY() {
    // TODO Auto-generated method stub
    return this.y >= 400;
  }

  // checks to see if a word is active
  public boolean isActive(IWord w) {
    // TODO Auto-generated method stub
    return w.activeHuh(this);
  }

  // double dispatch to see if a word is active
  public boolean activeHuh(ActiveWord w) {
    return this.x == w.x && this.word.equals(w.word) && this.y == w.y;
  }

  // double dispatch to see if a word is active
  public boolean inactiveHuh(InactiveWord w) {
    return false;
  }

  // switches a word's type
  public IWord switchActive(String s) {
    // TODO Auto-generated method stub
    return this;
  }

}

//represents an inactive word in the ZType game
class InactiveWord extends AWord {

  InactiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  // Template:
  /*
   * fields:
   * this.word...String
   * this.x...int
   * this.y...int
   * methods:
   * this.reduceWord(String)...IWord
   * this.drawTheWorld(WorldScene)...WorldScene
   * this.move() ... IWord
   * this.greaterThanY() ... boolean
   * this.isActive(IWord) ... boolean
   * this.activeHuh(ActiveWord) ... boolean
   * this.inactiveHuh(InactiveWord) ... boolean
   * this.switchActive() ... IWord
   */

  // reduces a letter from the word if it matches the given one
  public IWord reduceWord(String letter) {
    return this;
  }

  // draws the word on the WorldScene
  public WorldScene drawTheWord(WorldScene w) {
    return w.placeImageXY(new TextImage(this.word, 15, FontStyle.BOLD, Color.RED), this.x, this.y);
  }

  // moves the word down the screen
  public IWord move() {
    return new InactiveWord(this.word, this.x, this.y + 1);
  }

  // checks to see if the y of a given word exceeds 400
  public boolean greaterThanY() {
    return this.y >= 400;
  }

  // checks to see if a word is active
  public boolean isActive(IWord w) {
    // TODO Auto-generated method stub
    return false;
  }

  // double dispatch to see if a word is active
  public boolean activeHuh(ActiveWord w) {
    return false;
  }

  // double dispatch to see if a word is active
  public boolean inactiveHuh(InactiveWord w) {
    return this.x == w.x && this.word.equals(w.word) && this.y == w.y;
  }

  // switches a word's type
  public IWord switchActive(String s) {
    // TODO Auto-generated method stub
    if (this.word.substring(0, 1).equals(s)) {
      return new ActiveWord(this.word, this.x, this.y);
    }
    else {
      return this;
    }
  }

}

//all examples and tests for ILoWord
class ExamplesWordLists {

  IWord inactiveWord = new InactiveWord("Hello", 6, 8);
  IWord activeWord = new ActiveWord("Fundies", 2, 0);
  IWord activeWord2 = new ActiveWord("Free", 8, 4);
  IWord inactiveWord2 = new InactiveWord("For", 3, 2);
  IWord sameLetter1 = new ActiveWord("Frolicking", 7, 9);
  IWord exceedActiveWord = new ActiveWord("exceed", 10, 800);
  IWord exceedInactiveWord = new InactiveWord("exceed", 10, 800);
  ILoWord emptyList = new MtLoWord();
  ILoWord moreThanEmptyList = new ConsLoWord(inactiveWord,
      new ConsLoWord(activeWord, new MtLoWord()));
  ILoWord oneWordList = new ConsLoWord(activeWord, emptyList);
  ILoWord oneInactiveWordList = new ConsLoWord(inactiveWord, emptyList);
  ILoWord listWithSameLetterStart = new ConsLoWord(activeWord,
      new ConsLoWord(activeWord2, new ConsLoWord(inactiveWord2, new MtLoWord())));
  ILoWord threeItems = new ConsLoWord(inactiveWord, new ConsLoWord(new ActiveWord("Mehana", 1, 1),
      new ConsLoWord(new InactiveWord("Arthur", 6, 9), new MtLoWord())));
  ILoWord sortedList1 = new ConsLoWord(activeWord, new ConsLoWord(inactiveWord, new MtLoWord()));
  ILoWord sortedList2 = new ConsLoWord(inactiveWord2,
      new ConsLoWord(activeWord2, new ConsLoWord(activeWord, new MtLoWord())));
  ILoWord sortedList3 = new ConsLoWord(new InactiveWord("Arthur", 6, 9),
      new ConsLoWord(inactiveWord, new ConsLoWord(new ActiveWord("Mehana", 1, 1), new MtLoWord())));
  ILoWord singleSorted = new ConsLoWord(new InactiveWord("Good", 3, 7), new MtLoWord());
  ILoWord activeWordsSameLetter = new ConsLoWord(activeWord, new ConsLoWord(activeWord2,
      new ConsLoWord(sameLetter1, new ConsLoWord(inactiveWord, new MtLoWord()))));
  ILoWord someActiveWordsDifLetters = new ConsLoWord(new ActiveWord("undies", 2, 0),
      new ConsLoWord(new ActiveWord("ree", 8, 4), new ConsLoWord(new ActiveWord("Frolicking", 7, 9),
          new ConsLoWord(inactiveWord, new MtLoWord()))));
  ILoWord upperAndLower = new ConsLoWord(new ActiveWord("GIANNI", 9, 3),
      new ConsLoWord(new InactiveWord("abigail", 5, 0),
          new ConsLoWord(new ActiveWord("CHASE", 1, 9),
              new ConsLoWord(new InactiveWord("sawyer", 4, 5), new MtLoWord()))));
  ILoWord upperAndLowerSorted = new ConsLoWord(new InactiveWord("abigail", 5, 0),
      new ConsLoWord(new ActiveWord("CHASE", 1, 9), new ConsLoWord(new ActiveWord("GIANNI", 9, 3),
          new ConsLoWord(new InactiveWord("sawyer", 4, 5), new MtLoWord()))));

  // tests for checkAndReduce
  boolean testCheckAndReduce(Tester t) {
    // empty list -> empty list
    // list where none of the active words start with the given letter (inactive
    // words in list as well)
    // list where all of the active words start with the given letter (inactive
    // words in list as well)
    // list where some of the active words start with the given letter (inactive
    // words in list as well)
    return t.checkExpect(this.emptyList.checkAndReduce("G"), this.emptyList)
        && t.checkExpect(this.moreThanEmptyList.checkAndReduce("G"), this.moreThanEmptyList)
        && t.checkExpect(this.activeWordsSameLetter.checkAndReduce("F"),
            new ConsLoWord(new ActiveWord("undies", 2, 0),
                new ConsLoWord(new ActiveWord("ree", 8, 4),
                    new ConsLoWord(new ActiveWord("rolicking", 7, 9),
                        new ConsLoWord(inactiveWord, new MtLoWord())))))
        && t.checkExpect(this.someActiveWordsDifLetters.checkAndReduce("F"),
            new ConsLoWord(new ActiveWord("undies", 2, 0),
                new ConsLoWord(new ActiveWord("ree", 8, 4),
                    new ConsLoWord(new ActiveWord("rolicking", 7, 9),
                        new ConsLoWord(this.inactiveWord, new MtLoWord())))));
  }

  // test for reduceWord
  boolean testReduceWord(Tester t) {
    // inactiveWord with matching letter -> do nothing
    // activeWord with matching letter -> return with letter reduced
    // inactiveWord with not matching letter -> do nothing
    // activeWord with not matching letter -> do nothing
    return t.checkExpect(this.inactiveWord.reduceWord("H"), this.inactiveWord)
        && t.checkExpect(this.activeWord.reduceWord("F"), new ActiveWord("undies", 2, 0))
        && t.checkExpect(this.inactiveWord.reduceWord("j"), this.inactiveWord)
        && t.checkExpect(this.activeWord.reduceWord("y"), this.activeWord);
  }

  // tests for addToEnd
  boolean testAddToEnd(Tester t) {
    // empty list, empty word
    // empty list, active word with content
    // one thing in list, inactive word
    // many things in list, active word
    return t.checkExpect(this.emptyList.addToEnd(new ActiveWord("", 7, 0)),
        new ConsLoWord(new ActiveWord("", 7, 0), new MtLoWord()))
        && t.checkExpect(this.emptyList.addToEnd(activeWord),
            new ConsLoWord(this.activeWord, new MtLoWord()))
        && t.checkExpect(
            new ConsLoWord(this.activeWord, new MtLoWord()).addToEnd(this.inactiveWord),
            new ConsLoWord(this.activeWord, new ConsLoWord(this.inactiveWord, new MtLoWord())))
        && t.checkExpect(this.listWithSameLetterStart.addToEnd(this.sameLetter1),
            new ConsLoWord(this.activeWord,
                new ConsLoWord(this.activeWord2, new ConsLoWord(this.inactiveWord2,
                    new ConsLoWord(this.sameLetter1, new MtLoWord())))));
  }

  // test filterOutEmpties
  boolean testFilterOutEmpties(Tester t) {
    // empty list -> do nothing
    // list with one empty word - gets rid of it
    // list with more than one empty word - gets rid of both instances
    // list with no empty words -> do nothing
    return t.checkExpect(this.emptyList.filterOutEmpties(), this.emptyList)
        && t.checkExpect(
            new ConsLoWord(new ActiveWord("", 4, 5),
                new ConsLoWord(this.activeWord,
                    new ConsLoWord(this.sameLetter1, new ConsLoWord(this.inactiveWord,
                        new MtLoWord()))))
                .filterOutEmpties(),
            new ConsLoWord(this.activeWord,
                new ConsLoWord(this.sameLetter1,
                    new ConsLoWord(this.inactiveWord, new MtLoWord()))))
        && t.checkExpect(
            new ConsLoWord(this.activeWord2,
                new ConsLoWord(new InactiveWord("", 2, 5),
                    new ConsLoWord(this.inactiveWord2,
                        new ConsLoWord(new ActiveWord("", 4, 5),
                            new ConsLoWord(this.sameLetter1, new MtLoWord())))))
                .filterOutEmpties(),
            new ConsLoWord(this.activeWord2,
                new ConsLoWord(this.inactiveWord2,
                    new ConsLoWord(this.sameLetter1, new MtLoWord()))))
        && t.checkExpect(this.sortedList1.filterOutEmpties(), this.sortedList1);
  }

  // tests the move method for ILoWords
  boolean testMove(Tester t) {
    // 1. tests to see if the method returns the empty list if given the empty list
    // 2. tests to see if the method moves an active word if given a list with an
    // active word
    // 3. tests to see if the method moves an inactive word if given a list with an
    // inactive word
    return t.checkExpect(this.emptyList.move(), this.emptyList)
        && t.checkExpect(this.oneWordList.move(),
            new ConsLoWord(new ActiveWord("Fundies", 2, 1), this.emptyList))
        && t.checkExpect(this.oneInactiveWordList.move(),
            new ConsLoWord(new InactiveWord("Hello", 6, 9), this.emptyList));
  }

  // tests the yExceed method
  boolean testYExceed(Tester t) {
    // 1. tests to see if the method returns false if given the empty list
    // 2. test to see if the method returns false if all items in the list do not
    // exceed a given y value
    // 3. tests to see if the method returns true if one or more items exceeds a
    // given y value
    return t.checkExpect(this.emptyList.yExceed(), false)
        && t.checkExpect(this.activeWordsSameLetter.yExceed(), false)
        && t.checkExpect(new ConsLoWord(this.exceedActiveWord, this.emptyList).yExceed(), true);
  }

  // tests the findNewActive method
  boolean testFindNewActive(Tester t) {
    /*
     * 1. tests to see if the method returns the same list if given the empty list
     * 2. tests to see if the method returns the same list if there is an active
     * word
     * in a list of words
     * 3. tests to see if the method returns a list with an active word with its
     * first letter removed
     * if given an list with one active word that begins with the letter in the
     * given inputed key.
     */
    return t.checkExpect(this.emptyList.findNewActive("k"), this.emptyList)
        && t.checkExpect(this.oneWordList.findNewActive("j"), this.oneWordList)
        && t.checkExpect(this.oneInactiveWordList.findNewActive("H"),
            new ConsLoWord(new ActiveWord("ello", 6, 8), this.emptyList));
  }

  // tests the anyActive method
  boolean testAnyActiveWords(Tester t) {
    /*
     * 1. tests to see if the method returns false if given the empty list
     * 2. tests to see if the method returns true if given a list with an active
     * word in it
     * 3. tests to see if the method returns false if given a list without an
     * active word in it
     * 
     */
    return t.checkExpect(this.emptyList.anyActiveWords(), false)
        && t.checkExpect(this.oneWordList.anyActiveWords(), true)
        && t.checkExpect(this.oneInactiveWordList.anyActiveWords(), false);
  }

  // tests the checkAndReduceActive method
  boolean testCheckAndReduceActive(Tester t) {
    /*
     * 1. tests to see if the method returns the empty list if given the empty list
     * 2. test to see if the method rturns the same list if given a list that
     * already
     * contains an active word
     * 3. tests to see if the method returns a new list with an active word that has
     * its first letter
     * removed if given an inactive list who's word's first character matches the
     * given key.
     * 
     */
    return t.checkExpect(this.emptyList.checkAndReduceActive("k"), this.emptyList)
        && t.checkExpect(this.oneWordList.checkAndReduceActive("F"), this.oneWordList)
        && t.checkExpect(this.oneInactiveWordList.checkAndReduceActive("H"),
            new ConsLoWord(new ActiveWord("ello", 6, 8), this.emptyList));
  }

  // tests the move method for IWord
  boolean testMoveIWord(Tester t) {
    return t.checkExpect(this.activeWord.move(), new ActiveWord("Fundies", 2, 1))
        && t.checkExpect(this.inactiveWord.move(), new InactiveWord("Hello", 6, 9));
  }

  // need tests for drawTheWord method
  // tests the drawTheWord method
  boolean testdrawTheWord(Tester t) {
    /*
     * 1. tests to see if the method properly draws the invoked inactive word onto
     * the given world scene
     * 2. tests to see if the method properly draws the invoked active word onto
     * the given world scene
     */
    return t.checkExpect(this.inactiveWord.drawTheWord(new WorldScene(600, 400)),
        new WorldScene(600, 400).placeImageXY(new TextImage("Hello", 15, FontStyle.BOLD, Color.RED),
            6, 8))
        && t.checkExpect(this.activeWord.drawTheWord(new WorldScene(600, 400)),
            new WorldScene(600, 400)
                .placeImageXY(new TextImage("Fundies", 15, FontStyle.BOLD, Color.GREEN), 2, 0));
  }

  // tests the greaterThanY method
  boolean testGreaterThanY(Tester t) {
    /*
     * 1. tests to see if the method returns false if given an active word
     * who's y does not exceed the set maximum
     * 2. tests to see if the method returns false if given an inactive word
     * who's y does not exceed the set maximum
     * 3. tests to see if the method returns true if given an active word
     * that exceeds the set maximum
     * 4. tests to see if the method returns true if given an inactive word
     * that exceeds the set maximum
     */
    return t.checkExpect(this.activeWord.greaterThanY(), false)
        && t.checkExpect(this.inactiveWord.greaterThanY(), false)
        && t.checkExpect(this.exceedActiveWord.greaterThanY(), true)
        && t.checkExpect(this.exceedInactiveWord.greaterThanY(), true);
  }

  // tests the isActive method
  /*
   * 1. tests if the method returns true if a word is an active word
   * 2. tests if the method returns false if a word is an inactive word
   */
  boolean testIsActive(Tester t) {
    return t.checkExpect(this.activeWord.isActive(this.activeWord), true)
        && t.checkExpect(this.inactiveWord.isActive(this.inactiveWord), false);
  }

  // tests the activeHuh method
  boolean testActiveHuh(Tester t) {
    /*
     * 1. tests if the method returns true if in invoked word and the inputed word
     * are both the same active words
     * 2. tests if the method returns false if the invoked word is an inactive word
     * since the given word is an active word
     */
    return t.checkExpect(this.activeWord.activeHuh(new ActiveWord("Fundies", 2, 0)), true)
        && t.checkExpect(this.inactiveWord.activeHuh(new ActiveWord("Fundies", 2, 0)), false);
  }

  //tests the inactiveHuh method
  boolean testInactiveHuh(Tester t) {
    /*
     * 1. tests if the method returns true if in invoked word and the inputed word
     * are both the same inactive words
     * 2. tests if the method returns false if the invoked word is an active word
     * since the given word is an inactive word
     */
    return t.checkExpect(this.inactiveWord.inactiveHuh(new InactiveWord("Hello", 6, 8)), true)
        && t.checkExpect(this.activeWord.inactiveHuh(new InactiveWord("Fundies", 2, 0)), false);
  }

  // tests the switchActive method
  boolean testSwitchActive(Tester t) {
    return t.checkExpect(this.activeWord.switchActive("F"), this.activeWord)
        && t.checkExpect(this.inactiveWord.switchActive("H"), new ActiveWord("Hello", 6, 8));
  }

  // tests for draw function

  boolean testDraw(Tester t) {
    // empty list -> empty WorldScene
    // list with one word (active) -> drawn on scene in green
    // list with multiple words (active and inactive) -> drawn on scene
    // (green=active, red inactive)
    // one word list being an emptyString -> "drawn" on scene
    return t.checkExpect(
        this.emptyList.draw(new WorldScene(100, 100)
            .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)),
        new WorldScene(100, 100)
            .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0))
        && t.checkExpect(
            new ConsLoWord(this.activeWord, new MtLoWord()).draw(new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)),
            new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)
                .placeImageXY(new TextImage("Fundies", 15, FontStyle.BOLD, Color.GREEN), 2, 0))
        && t.checkExpect(
            this.listWithSameLetterStart.draw(new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)),
            new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)
                .placeImageXY(new TextImage("Fundies", 15, FontStyle.BOLD, Color.GREEN), 2, 0)
                .placeImageXY(new TextImage("Free", 15, FontStyle.BOLD, Color.GREEN), 8, 4)
                .placeImageXY(new TextImage("For", 15, FontStyle.BOLD, Color.RED), 3, 2))
        && t.checkExpect(
            new ConsLoWord(new InactiveWord("", 2, 2), new MtLoWord()).draw(new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)),
            new WorldScene(100, 100)
                .placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.ORANGE), 0, 0)
                .placeImageXY(new TextImage("", 4, FontStyle.BOLD, Color.RED), 2, 2));
  }

}