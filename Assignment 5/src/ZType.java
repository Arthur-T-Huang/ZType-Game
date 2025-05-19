import tester.*;

import java.util.Random;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

class Utils {
  Random random;

  Utils(Random random) {
    this.random = random;
  }

  // generates a random 6 character word
  public String generateRandomWord(String s) {
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (s.length() == 6) {
      return s;
    }
    else {
      int index = this.random.nextInt(characters.length() - 1);
      return generateRandomWord(s + (characters.substring(index, index + 1)));
    }
  }

  // Template:
  /*
   * fields:
   * this.random...Random
   * methods:
   * this.generateRandomWord(String)...String
   * methods of fields: none
   */

}

//interface for ZTypeWorld class 
interface IZTypeWorld {
  int width = 600;
  int height = 400;
}

//represents a world class to animate a list of Dots on a scene
class ZTypeWorld extends World implements IZTypeWorld {
  ILoWord words;
  Random random;
  int timer;
  int score;

  ZTypeWorld(ILoWord w, int timer, int score) {
    this(w, new Random(), timer, score);
  }

  ZTypeWorld(ILoWord w, Random random, int timer, int score) {
    this.words = w;
    this.random = random;
    this.timer = timer;
    this.score = score;
  }

  // draws the words onto the background
  public WorldScene makeScene() {
    WorldImage img = new FromFileImage(
        "Images/mountainpic.jpg");
    return this.words.draw(new WorldScene(width, height).placeImageXY(img, 280, 200));
  }

  public WorldScene lastScene(String msg) {
    WorldImage img = new FromFileImage(
        "Images/abhi.jpg");
    return new WorldScene(width, height).placeImageXY(new TextImage(msg, 30, Color.blue), 480, 200)
        .placeImageXY(img, 200, 200)
        .placeImageXY(new TextImage("Score: " + Integer.toString(score), 30, Color.blue), 470, 240);
  }

  //move the words on the scene. Adds a new inactive word at a random location at every tick
  // of the clock

  public World onTick() {
    Utils u = new Utils(new Random());
    ILoWord add = this.words.addToEnd((new InactiveWord(u.generateRandomWord(""),
        (u.random.nextInt(580) + 10), (u.random.nextInt(20)))));

    if (add.yExceed()) {
      return this.endOfWorld("Game Over :(");
    }
    else if (timer % 60 == 0) {
      return new ZTypeWorld(add.move(), this.timer + 1, this.score);
    }
    else {
      return new ZTypeWorld(this.words.move(), this.timer + 1, this.score);
    }
  }

  //move the word on the scene. Adds a new inactive word at a random location at every tick
  // of the
  //  clock
  //uses a seeded random object for making the new word to make it easier for
  // testing since
  //we will be able to predict the numbers that will be produced
  public ZTypeWorld onTickForTesting() {
    Utils u = new Utils(new Random(420));
    ILoWord add = (this.words.addToEnd(new InactiveWord(u.generateRandomWord(""),
        (u.random.nextInt(580) + 10), (u.random.nextInt(20)))));
    return new ZTypeWorld(add.move(), this.timer + 1, this.score);
  }

  // add a key event to change the colors of all of the existing Dots in this
  // World to green when the "g" key is pressed
  public ZTypeWorld onKeyEvent(String key) {
    if (this.words.anyActiveWords()) {
      return new ZTypeWorld(
          this.words.checkAndReduceActive(key).checkAndReduce(key).filterOutEmpties(),
          this.timer + 1, this.score + 1);
    }
    else {
      return new ZTypeWorld(
          this.words.checkAndReduceActive(key).checkAndReduce(key).filterOutEmpties(),
          this.timer + 1, this.score);
    }
  }

  // Template:
  /*
   * fields:
   * this.words...ILoString
   * this.random ... Random
   * this.timer ... int
   * methods:
   * this.onTick()...World
   * this.onTickForTesting()...World
   * this.onKeyEvent(String) ... World
   * methods of fields:
   * this.checkAndReduce(String)...ILoWord
   * this.addToEnd(IWord)...ILoWord
   * this.filterOutEmpties()...ILoWord
   * this.draw(WorldScene)...WorldScene
   * methods of fields:
   * this.words.checkAndReduce(String)...ILoWord
   * this.words.addToEnd(IWord)...ILoWord
   * this.words.filterOutEmpties()...ILoWord
   * this.words.draw(WorldScene)...WorldScene
   * this.words.move() ... ILoWord
   * this.words.yExceed() ... boolean
   * this.words.findNewActive(String) ... ILoWord
   * this.words.anyActiveWords() ... boolean
   * this.words.checkAndReduceActive(String) ... ILoWord
   * 
   */
}

class ExamplesAnimation {

  // Color c = Color.magenta;
  WorldScene base = new WorldScene(600, 400).placeImageXY(
      new FromFileImage(
          "Images/mountainpic.jpg"),
      280, 200);
  IWord inactiveWord = new InactiveWord("Arthur", 6, 8);
  IWord activeWord = new ActiveWord("Gianni", 2, 0);
  IWord activeWord2 = new ActiveWord("letter", 8, 4);
  IWord inactiveWord2 = new InactiveWord("trains", 3, 2);
  IWord sameLetter1 = new ActiveWord("yogurt", 7, 9);
  IWord seed = new InactiveWord("dAREEw", 130, 8);
  ILoWord emptyList = new MtLoWord();
  ILoWord oneWordList = new ConsLoWord(this.activeWord, this.emptyList);
  ILoWord list1 = new ConsLoWord(this.inactiveWord, this.emptyList);
  ILoWord list2 = new ConsLoWord(this.activeWord2, this.list1);
  ILoWord list3 = new ConsLoWord(this.inactiveWord2, this.list2);
  ZTypeWorld zw = new ZTypeWorld(this.emptyList, new Random(420), 1, 0);
  ZTypeWorld mtZw = new ZTypeWorld(this.emptyList, 1, 1);
  ZTypeWorld oneZw = new ZTypeWorld(this.oneWordList, 1, 0);
  ZTypeWorld firstZw = new ZTypeWorld(this.list1, 1, 0);
  WorldScene ws = new WorldScene(300, 300);

  // testing onTick and accounting for the randomness
  boolean testOnTickWithSeededRandom(Tester t) {
    return t.checkExpect(this.mtZw.onTickForTesting(),
        new ZTypeWorld(new ConsLoWord(this.seed, this.emptyList), 2, 1))
        && t.checkExpect(this.oneZw.onTickForTesting(),
            new ZTypeWorld(new ConsLoWord(new ActiveWord("Gianni", 2, 1),
                new ConsLoWord(this.seed, this.emptyList)), 2, 0));
  }

  // tests the makeScene method
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.mtZw.makeScene(), this.base)
        && t.checkExpect(this.oneZw.makeScene(),
            this.base.placeImageXY(new TextImage("Gianni", 15, FontStyle.BOLD, Color.GREEN), 2, 0))
        && t.checkExpect(this.firstZw.makeScene(),
            this.base.placeImageXY(new TextImage("Arthur", 15, FontStyle.BOLD, Color.RED), 6, 8));
  }

  // tests the lastScene method
  /*
   * 1. tests to see if the method returns the last scene with the inputed message
   * if
   * the empty ZTypeWorld is invoked
   * 2. tests to see if the method returns the last scene with the inputted
   * message if
   * a ZTypeWorld with a list of words is invoked
   */
  boolean testLastScene(Tester t) {
    return t
        .checkExpect(this.mtZw.lastScene("Game Over :("), new WorldScene(600, 400)
            .placeImageXY(new TextImage("Game Over :(", 30, Color.blue), 480, 200)
            .placeImageXY(new FromFileImage(
                "Images/abhi.jpg"),
                200, 200)
            .placeImageXY(new TextImage("Score: " + Integer.toString(1), 30, Color.blue), 470, 240))
        && t.checkExpect(this.oneZw.lastScene("Game Over :("), new WorldScene(600, 400)
            .placeImageXY(new TextImage("Game Over :(", 30, Color.blue), 480, 200)
            .placeImageXY(new FromFileImage(
                "Images/abhi.jpg"),
                200, 200)
            .placeImageXY(new TextImage("Score: " + Integer.toString(0), 30, Color.blue), 470,
                240));
  }

  // tests the onKeyEvent method
  boolean testOnKeyEvent(Tester t) {
    /*
     * 1. tests to see if the method returns the empty list with a timer increase of
     * 1 if
     * the empty list is invoked
     * 2. tests to see if the method returns the list of words with the first letter
     * of an active word
     * removed when the first letter matches the given key, and the timer is
     * increased by 1
     * 3. tests to see if the method returns the list of words with the first letter
     * of an inactive word
     * removed and the inactive word is turned into an active word
     * when the first letter matches the given key, and the timer is increased by 1
     * 4. tests to see if the method returns the original word list with a timer
     * increase of 1
     * if the first letter of an active word in a list does not match the given key.
     * 5. tests to see if the method returns the original word list with a timer
     * increase of 1
     * if the first letter of an inactive word in a list does not match the given
     * key.
     */
    return t.checkExpect(this.mtZw.onKeyEvent("l"), new ZTypeWorld(this.emptyList, 2, 1))
        && t.checkExpect(this.oneZw.onKeyEvent("G"),
            new ZTypeWorld(new ConsLoWord(new ActiveWord("ianni", 2, 0), this.emptyList), 2, 1))
        && t.checkExpect(this.firstZw.onKeyEvent("A"),
            new ZTypeWorld(new ConsLoWord(new ActiveWord("rthur", 6, 8), this.emptyList), 2, 0))
        && t.checkExpect(this.oneZw.onKeyEvent("i"), new ZTypeWorld(this.oneWordList, 2, 1))
        && t.checkExpect(this.firstZw.onKeyEvent("i"), new ZTypeWorld(this.list1, 2, 0));
  }

  /*
   * boolean testDraw(Tester t) {
   * return t.checkExpect(this.d4.draw(this.ws),
   * ws.placeImageXY(new CircleImage(10, "solid", Color.black), 53, 236))
   * && t.checkExpect(new ConsLoDot(this.d4, this.mt).draw(this.ws),
   * ws.placeImageXY(new CircleImage(10, "solid", Color.black), 53, 236))
   * && t.checkExpect(this.init.makeScene(), new WorldScene(600, 400))
   * && t.checkExpect(this.dw.makeScene(), new WorldScene(600, 400)
   * .placeImageXY(new CircleImage(10, "solid", Color.magenta), 58, 239));
   * }
   * 
   * boolean testMove(Tester t) {
   * return t.checkExpect(this.d4.move(), new Dot(10, Color.black, new Random(),
   * 58, 239))
   * && t.checkExpect(new ConsLoDot(this.d4, this.mt).move(),
   * new ConsLoDot(new Dot(10, Color.black, new Random(), 58, 239), this.mt));
   * }
   */

  //need more tests!
  boolean testBigBang(Tester t) {
    ZTypeWorld world = new ZTypeWorld(this.emptyList, 1, 0);
    int worldWidth = 600;
    int worldHeight = 400;
    double tickRate = 0.04;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}