package Project2;

//game reset and counter/score --WIP
//score display for player
//win condition thing
//algorithm step the step
//screenshots

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
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
	private GridPane gpane = new GridPane();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		gpane.setHgap(5);
		gpane.setVgap(5);
		
		setPane();
			
			

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
	
	//returns a random image from array of card images
	public Image randImage() {
		Image rand;
		Image[] cards = {new Image("image/num1.png"), new Image("image/num2.png"),new Image("image/num3.png"),new Image("image/num4.png"),
				new Image("image/num5.png"),new Image("image/num6.png"),new Image("image/num7.png"),new Image("image/num8.png")};				
			
		rand = cards[(int) (Math.random()*8)];
		
		return rand;
		
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
	
	public void setPane() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				ImageView image = new ImageView("image/front.png");
				
				image.setPreserveRatio(true);
				image.fitHeightProperty().bind(gpane.heightProperty().subtract(gpane.heightProperty().divide(5)).divide(4));
				image.fitWidthProperty().bind(gpane.widthProperty().subtract(20).divide(4));
				
				gpane.add(image, i, j);		
				
				
				image.setOnMousePressed(e -> {
					//checks if the card is flipped, if not a new image is assigned to it
					if(compareImages(image.getImage(), new Image("image/front.png")) || counter == 2) {
						System.out.println("test1");
						image.setImage(new Image("image/num2.png"));
						
						counter += 1;
						System.out.println(counter);
					}	
					if(counter == 2) {
						counter = 0;
						
						
						//check if flipped images are matching, raise score if true
						
						try {
							Thread.sleep(1000);
						} 
						catch(InterruptedException m) {
							m.printStackTrace();
						}
						
						//setPane();
						
					}
				});
			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}



