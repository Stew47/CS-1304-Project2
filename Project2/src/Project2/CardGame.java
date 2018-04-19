/*CS 1302 
 * Project 2
 * Edgar Perez & Tyler Stewart
*/
package Project2;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CardGame extends Application {

	private int counter = 0;
	private int score = 0;
	private Label scoreBoard = new Label("Your score: 0");
	private Label win = new Label();
	private GridPane gpane = new GridPane();
	private Image[] images = new Image[16];
	//keeps track of last image clicked, with a default image that is never displayed/used elsewhere
	private int lastCard = 16;
	private boolean animationRunning = false;
	//keeps track which cards have been matched
	private boolean[] matched = {false, false, false, false, false, false, false, false, 
			false, false, false, false, false, false, false, false, false};
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		gpane.setHgap(5);
		gpane.setVgap(5);
		
		//randomizes a array of images to build the game
		randImage();
		
		//calls method to set randomized image array to game, together with all p
		//properties of the cards needed (animations, setonClicked(), etc,)
		setPane();

		//quit button
		Button btQuit = new Button("Quit");
		gpane.add(btQuit,0,5);
		
		//Label for score and win added to pane
		gpane.add(scoreBoard, 3, 4);
		gpane.add(win, 2, 4);
		
		
		
		//sets the title size and then shows the scene
		Scene scene = new Scene(gpane,375,600);
		primaryStage.setTitle("Memory Card Game");
		primaryStage.setScene(scene);
		primaryStage.show();
				
		//closes window when button is clicked
		btQuit.setOnAction(e -> {
			primaryStage.close();
		});
	}
	
	//randomly generates array of images, using each image only twice
	public void randImage() {
		Image[] cards = {new Image("image/num1.png"), new Image("image/num2.png"),new Image("image/num3.png"),new Image("image/num4.png"),
				new Image("image/num5.png"),new Image("image/num6.png"),new Image("image/num7.png"),new Image("image/num8.png")};				
		int[] CardCount = {0,0,0,0,0,0,0,0};
		
		//random number var, used so that image usage can be counted in separate array
		int randnum;
		
		for (int i = 0; i < 16;i++) {
			do {
				randnum = (int)(Math.random()*8);
			} while (CardCount[randnum] == 2);
			
			images[i] = cards[randnum];
			CardCount[randnum] += 1;
		}
		
	}
	
	//sets pane images to show back of the card, with each card having the necessary logic to run the game
	public void setPane() {
		ImageView[] cards = new ImageView[17];
		
		//creates a default card which should never be seen by the user
		cards[16] = new ImageView(new Image("image/blank.png"));
		cards[16].setPreserveRatio(true);
		cards[16].fitHeightProperty().bind(gpane.heightProperty().subtract(gpane.heightProperty().divide(5)).divide(4));
		cards[16].fitWidthProperty().bind(gpane.widthProperty().subtract(20).divide(4));
		
		for (int i = 0; i < 16; i++) {
			//sets each card to show its "back", adjusting size with window while preserving aspect ratio
			ImageView image = new ImageView("image/front.png");
			
			image.setPreserveRatio(true);
			image.fitHeightProperty().bind(gpane.heightProperty().subtract(gpane.heightProperty().divide(5)).divide(4));
			image.fitWidthProperty().bind(gpane.widthProperty().subtract(20).divide(4));
			
			//sets the card "back" to the array
			cards[i] = image;
			
			//checks array to see if cards have already been matched, if so they are disabled and pre-flipped
			cards[i].setDisable(matched[i]);
			
			if(cards[i].isDisable()) {
				cards[i].setImage(images[i]);
			}
			
			//two transitions, one to fade to transparent, one to fade back to original opacity
			FadeTransition ft1 = new FadeTransition(Duration.millis(500), image);
			ft1.setFromValue(1.0);
			ft1.setToValue(0);
			
			FadeTransition ft2 = new FadeTransition(Duration.millis(600), image);
			ft2.setToValue(1.0);
			
			//final so that cards[i] event can actually use it, the value is set to each object and represents the position in the array
			final int x = i;
			
			//the bulk of the game logic involved in "flipping" the cards, playing animations, keeping score, etc.		
			cards[i].setOnMousePressed(e -> {
				//checks if the card is flipped and a animation is already running, if it is not 
				//the animation is called and it is counted as one card flipped otherwise the code skips this
				if(compareImages(image.getImage(), new Image("image/front.png")) && !animationRunning) { 
					counter += 1;
					cards[x].setDisable(true);
					
					//set var to false, preventing additional clicks from running until animation is complete
					animationRunning = true;
					
					ft1.play();
				}
				
				//when the fade transition to blank is finished, the image is swapped with the "front" of the card and fades back to 
				//normal opacity
				ft1.setOnFinished(n -> {
					image.setImage(images[x]);
					
					if(compareImages(image.getImage(), cards[lastCard].getImage())) {
						//marks both cards as matched, preventing them from being clickable on next reset
						matched[x] = true;
						matched[lastCard] = true;
						
						score += 1;	
						
						//updates scoreboard
						scoreBoard.setText("Your Score: " + score);
					} else if(!compareImages(image.getImage(),cards[lastCard].getImage())) {
						cards[x].setDisable(false);
					}
					
					//set to current card, so that next run will know which card was clicked before it
					lastCard = x;
					ft2.play();
				});
				
				//checks if the user score is equal to three, if so the game stops running and announces they have won
				//otherwise if the user has clicked two cards the flipped counter is reset along with the pane, "flipping"
				//the cards back to their "back". The cards however stay the same, as that is only set on first run
				ft2.setOnFinished(n -> {			
					if(score == 3) {
						win.setText("You win!");
						
						//calls a method, not required but fun
						win();
					} else if (counter == 2 && score != 3) {
						counter = 0;
						
						//reset to default value
						lastCard = 16;
						
						setPane();
					} 
					//lets next click begin the animation
					animationRunning = false;
				});
				
			});
		}
		
		//adds all the cards to the pane
		int k = 0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gpane.add(cards[k], j, i);
				k+=1;				
			}
		}
		
		
	}
	
	//method to compare two passed images
		public boolean compareImages(Image pic1, Image pic2) {
			boolean isSame = true;
			
			//compares the images first by pixel width/height, then compares the color of each pixel to the other
			if(pic1.getWidth()==pic2.getWidth()) {
				if(pic1.getHeight()==pic2.getHeight()) {
					for (int i = 0; pic1.getWidth() > i;i++) {
						for (int j = 0; pic1.getHeight() > j; j++) {
							if (pic1.getPixelReader().getArgb(i, j) == 
									pic2.getPixelReader().getArgb(i, j)) {
							} else {
								isSame = false;
							}
						}
					}
				} else {
					isSame = false;
				}
			} else {
				isSame = false;
			}		
			return isSame;
		}
		
	public static void main(String[] args) {
		launch(args);
	}
	
	//left at bottom as this is not required, just a fun thing for the win condition
	public void win() {
		gpane.getChildren().clear();
		
		Label text = new Label("You Win!");
		text.setFont(new Font(30));
		gpane.add(text, 1, 0);
		
		ImageView fireworks = new ImageView(new Image("image/fireworks.gif"));
		fireworks.fitWidthProperty().bind(gpane.widthProperty());
		fireworks.fitHeightProperty().bind(gpane.heightProperty().subtract(100));
		gpane.add(fireworks, 1, 1);
		
		
		
		
		
		
	}
}



