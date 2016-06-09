package comp110;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * This class is a sample application for playing sound files in a JavaFX
 * application.
 * 
 * It makes use of the MediaPlayer class in JavaFX which controls the output of
 * media files with start/stop/pause methods. The official documentation can be
 * found here:
 * 
 * docs.oracle.com/javase/8/javafx/api/javafx/scene/media/MediaPlayer.html
 * 
 * @author Kris Jordan <kris@cs.unc.edu>
 */
public class SoundsDemo extends Application {

  /**
   * We'll add MediaPlayers to a queue so that they are played one after
   * another.
   * 
   * One classic data structure we don't talk about in 110 are queues.
   * 
   * A queue is, as you might expect, just like a line at a grocery store. Like
   * an array, it stores many items. Unlike an array, you can only add items to
   * the end of it and read items from the front of it. The way you add to the
   * end is simply the add method. The way you *read* an item at the front of
   * the queue is to use the *peek* method. If you want to *remove* the item
   * from the front of the queue you use the *poll* method.
   * 
   * We use a queue to play our sounds in the order of the buttons being pressed
   * and to avoid sounds overlapping one another.
   */
  private Queue<MediaPlayer> _soundQueue = new LinkedList<MediaPlayer>();

  /**
   * Stage is our application's window.
   */
  private Stage _stage;

  /**
   * Scene is what fills a stage.
   */
  private Scene _scene;

  /**
   * Constant to represent the amount of padding in our VBox.
   */
  private final static double PADDING = 16.0;

  /**
   * The entry point into the program. It calls the inherited launch() method
   * which is JavaFX magic for starting the application. The launch method
   * ultimately calls our start method.
   */
  public static void main(String[] args) {
    SoundsDemo.launch();
  }

  public void start(Stage stage) {
    _stage = stage;
    _scene = this.initScene();
    _stage.setScene(_scene);
    _stage.show();
  }

  /**
   * This is where we setup the GUI.
   */
  private Scene initScene() {
    VBox container = new VBox(SoundsDemo.PADDING);
    container.setPadding(new Insets(SoundsDemo.PADDING));

    // Setup the Label
    Label appLabel = new Label("DJ Khaled Soundbox");

    // Setup the Lion button
    Button lionButton = new Button("Lion");
    lionButton.setOnAction(this::handleLionPress);

    // Setup the Enjoy Life button
    Button enjoyLifeButton = new Button("Enjoy Life");
    enjoyLifeButton.setOnAction(this::handleEnjoyLifePress);

    container.getChildren().addAll(appLabel, lionButton, enjoyLifeButton);

    return new Scene(container);
  }

  /**
   * The event handlers for button presses simply dispatch to the playSound
   * method.
   */
  private void handleLionPress(ActionEvent e) {
    this.playSound("liiiooon.mp3");
  }

  private void handleEnjoyLifePress(ActionEvent e) {
    this.playSound("enjoylife.mp3");
  }

  /**
   * The following method will add a sound to the queue based on the sound's
   * filename in the comp110/sounds folder.
   */
  private void playSound(String filename) {

    /*
     * Step 1) Get a "URL" to the file. In this case we're using a "resource"
     * which is simply a file that is bundled alongside the application. You'll
     * notice in the application's packages that there is a comp110.sounds
     * folder where these files are stored. When this project is exported as a
     * runnable JAR, the sound files will be included in the JAR.
     */
    URL resource = this.getClass().getResource("sounds/" + filename);

    /* Step 2) Setup a Media object that references the filename. */
    Media media = new Media(resource.toString());

    /*
     * Step 3) Setup a MediaPlayer for that Media object. I believe there are
     * other ways this could be done in order to reuse media players, but this
     * model of a media player per media item turns out to be pretty simple.
     */
    MediaPlayer player = new MediaPlayer(media);

    /*
     * Step 4) We haven't yet started playing the sound, or added it to the
     * queue. Before doing either we want to setup the player to call the
     * soundEnded method when it reaches the end of the clip. This is just like
     * a keyPress or mouseClick event. It gets fired at some time in the future
     * after the sound clip finishes.
     */
    player.setOnEndOfMedia(this::soundEnded);

    /*
     * Step 4) We add the media player to our _players Queue of media players.
     * Then we call the playNextSound method that will process the queue.
     */
    _soundQueue.add(player);
    this.playNextSound();
  }

  /**
   * This is a clever, somewhat hacky, method for processing our sound queue.
   * 
   * When this method is called it will check to see if there is at least one
   * MediaPlayer in the queue and attempt to play it if so. This is hacky
   * because we may already be playing a sound. Rather than checking the status
   * of the MediaPlayer at the front of the queue we just call the play method
   * on it no matter what. If it was already playing a sound, it has no effect.
   */
  private void playNextSound() {
    if (_soundQueue.isEmpty() == false) {
      _soundQueue.peek().play();
    }
  }

  /**
   * When a sound finishes playing this method will be called. It simply removes
   * the first MediaPlayer from the front of the queue and does nothing with it.
   * It then calls the playNextSound method which will start the next sound
   * clip, if there is another queued up to play.
   */
  private void soundEnded() {
    _soundQueue.poll();
    this.playNextSound();
  }

}