package Project2;

//game reset and counter/score --WIP
//-score is not counting properly and reset is a little weird on running at the intended time, due in part to the possiblity of two or more transitions happening at once
//score display for player
//win condition thing
//algorithm step the step
//screenshots

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.PixelReader;

public class CardGame extends Application {

	private int counter = 0;
	private int score = 0;
	private GridPane gpane = new GridPane();
	private Image[] images = new Image[16];
	//not working
	private Image lastCard = new Image("image/num1.png");
	
	
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
		
		//sets the title size and then shows the scene
		Scene scene = new Scene(gpane,350,600);
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
		
		//random number var, used so that image usage can be counted in seperate array
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
		ImageView[] cards = new ImageView[16];
		
		for (int i = 0; i < 16; i++) {
			//sets each card to show its "back", adjusting size with window while preserving aspect ratio
			ImageView image = new ImageView("image/front.png");
			
			image.setPreserveRatio(true);
			image.fitHeightProperty().bind(gpane.heightProperty().subtract(gpane.heightProperty().divide(5)).divide(4));
			image.fitWidthProperty().bind(gpane.widthProperty().subtract(20).divide(4));
			
			//sets the "back" to the array
			cards[i] = image;
			
			//two transitions, one to fade to transparent, one to fade back to original opacity
			FadeTransition ft1 = new FadeTransition(Duration.millis(500), image);
			ft1.setFromValue(1.0);
			ft1.setToValue(0);
			
			FadeTransition ft2 = new FadeTransition(Duration.millis(1500), image);
			ft2.setToValue(1.0);
			
			//final so that cards event can actually use it, the value is reset on each loop
			final int x = i;
			
			//here be dragons
			//lots o' bugs/not fully/properly implemented things
			//the bulk of the game logic involved in "flipping" the cards, playing animations, keeping score, etc.
			cards[i].setOnMousePressed(e -> {
				
				//checks if the card is flipped, if it is not the animation is called and it is counted as one card flipped
				//otherwise the code skips this
				if(compareImages(image.getImage(), new Image("image/front.png"))) { 
					counter += 1;
					ft1.play();
				}
				
				//when the fade transition to blank is finished, the image is swapped with the "front" of the card and fades back to 
				//normal opacity
				ft1.setOnFinished(n -> {
					image.setImage(images[x]);
					ft2.play();
					
				});
				
				//checks if the user score is equal to three, if so the game stops running and announces they have won
				//otherwise if the user has clicked two cards the flipped counter is reset along with the pane, "flipping"
				//the cards back to their "back". The cards however stay the same, as that is only set on first run
				ft2.setOnFinished(n -> {			
					if(score == 3) {
						System.out.println("You have won");
					} else if (counter >= 2) {
						counter = 0;
						setPane();
					}
					
					//TODO:Fix
					//Should be used to keep last image clicked so that a comparision can be used, but currently bugged
					//lastCard = images[x];
				});
				
			});
		}
		
		//adds all the cards to the pane
		int x = 0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gpane.add(cards[x], j, i);
				x+=1;
				
			}
		}
		
		
	}
	
	//method to compare two passed images
		public boolean compareImages(Image pic1, Image pic2) {
			boolean isSame = true;
			
			//compares the images first by pixel width/height, then compares the color of each pixel to the other,
			//breaks out of the loop as soon as differences are found
			if(pic1.getWidth()==pic2.getWidth()) {
				if(pic1.getHeight()==pic2.getHeight()) {
					for (int i = 0; pic1.getWidth() > i;i++) {
						for (int j = 0; pic1.getHeight() > j; j++) {
							if (pic1.getPixelReader().getArgb(i, j) == 
									pic2.getPixelReader().getArgb(i, j)) {
							} else {
								isSame = false;
								break;
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
	
	//TODO: Maybe throw a overloaded setPane() down here, called when game is won to show some victory things
	//not at all needed, so feel free to ignore
}



