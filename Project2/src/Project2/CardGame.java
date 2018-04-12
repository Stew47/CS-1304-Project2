package Project2;

/*Update 4/12, 12AM
 * currently have the random selection of cards "working" including the 2 cards limit,
 * although there is nothing to stop user from "flipping" the card even after the first click
 * so we end up with a infinite loop from the random card picker once all the cards are used,
 * breaking the game. 
 * Looking into an image comparison method, primarily looking into the option of pixel reader as 
 * setId does not work with plane image objects
 * havent done anything with the game reset, counter, quit button, etc.
 * 
 * P.S. still have my images stored differently then you, will fix it tommorow also
*/

//randomizer, not so random with 2 of each ---done
//game reset and counter 
//image compairing --done
//quit button
//win condition thing
//algorithm step the step
//screenshots

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.PixelReader;

public class CardGame extends Application {

	private int counter = 0;
	private int score = 0;
	private int[] cardCount = new int[8];
	ImageView test = new ImageView("image/front.png");

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane gpane = new GridPane();
		gpane.setHgap(5);
		gpane.setVgap(5);
		
		
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				ImageView image = new ImageView("image/front.png");
				
				image.setPreserveRatio(true);
				image.fitHeightProperty().bind(gpane.heightProperty().subtract(gpane.heightProperty().divide(5)).divide(4));
				image.fitWidthProperty().bind(gpane.widthProperty().subtract(20).divide(4));
				
				gpane.add(image, i, j);		
				
				
				image.setOnMousePressed(e -> {
					//randomizer, array of image files selected
					if(compareImages(image.getImage(), test.getImage())) {
						image.setImage(randImage());
						counter = counter + 1;
					}					
				});
				
			}
		}

		Button btQuit = new Button("Quit");
		gpane.add(btQuit,0,5);
		
		//sets the title size and then shows the scene
				Scene scene = new Scene(gpane,350,600);
				primaryStage.setTitle("Memory Card Game");
				primaryStage.setScene(scene);
				primaryStage.show();
				
				
		btQuit.setOnAction(e -> {
			primaryStage.close();
		});
	}
	
	//returns random image from array of card images
	public Image randImage() {
		Image rand;
		Image[] cards = {new Image("image/num1.png"), new Image("image/num2.png"),new Image("image/num3.png"),new Image("image/num4.png"),
				new Image("image/num5.png"),new Image("image/num6.png"),new Image("image/num7.png"),new Image("image/num8.png")};		
		int randNum;
		
		//checks if card has already been called twice, re-rolls randnum until a card without two uses is found
		do {
			randNum = (int) (Math.random()*8);
			
			//create a check to prevent infinite loop upon depleting array of images
			
		} while (cardCount[randNum] == 2);
		
		rand = cards[randNum];
		
		cardCount[randNum] = cardCount[randNum] +1;
		
		return rand;
		
	}
	
	//method to compare two passed images
	//not currently in use, will work on this tomorrow before we meet. just ignore it unless you want to give it a try
	public boolean compareImages(Image pic1, Image pic2) {
		boolean isSame = true;
		
		
		//fix height/width mismatch
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
}



