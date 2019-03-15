package RacingSimulator;

/**
 * Louis Hwang, Nathan Moore, and Svet Draganitchki
 * CS 225 - Project 3
 */

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;

@SuppressWarnings("Duplicates")

public class RacingSimulator extends Application {

    private Checkpoint[] checkpoints;
    private int counter;
    private boolean winner;
    private RaceCar blueCar;
    private RaceCar redCar;
    private RaceCar whiteCar;
    private RaceCar purpleCar;
    private Pane trackPanel;
    private String relativePath = "/RacingSimulator/images/";
    private Button start;
    private Button reset;
    private Label[] results;
    private DecimalFormat df;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //MISCELLANEOUS
        counter = 0;
        df = new DecimalFormat("#.##");
        
        //TITLE
        Text title = new Text("Racing Simulator");
        title.getStyleClass().add("title");

        //CHECKPOINTS
        checkpoints = new Checkpoint[4];
        
        Checkpoint checkpointA = new Checkpoint(-10, 15, 0);
        checkpoints[0] = checkpointA;
        Checkpoint checkpointB = new Checkpoint(410, 15, 90);
        checkpoints[1] = checkpointB;
        Checkpoint checkpointC = new Checkpoint(410, 435, 180);
        checkpoints[2] = checkpointC;
        Checkpoint checkpointD = new Checkpoint(-10, 435, 270);
        checkpoints[3] = checkpointD;

        //CARS 
        blueCar = new RaceCar("Blue ",0, 3, setUpCar(relativePath + "blue_car.png", checkpointA));
        redCar = new RaceCar("Red  ",1, 0, setUpCar(relativePath + "red_car.png", checkpointB));
        whiteCar = new RaceCar("White",2, 1, setUpCar(relativePath + "white_car.png", checkpointC));
        purpleCar = new RaceCar("Purple",3, 2, setUpCar(relativePath + "purple_car.png", checkpointD));

        //RACETRACK
        Image raceTrackImg = new Image(relativePath + "raceTrack.png");
        ImageView raceTrackView = new ImageView();
        raceTrackView.setImage(raceTrackImg);
        
        trackPanel = new Pane();
        trackPanel.getChildren().addAll(raceTrackView, blueCar.getImage(), redCar.getImage(), whiteCar.getImage(), purpleCar.getImage());

        //START AND RESET BUTTONS
        start = new Button("Start");
        start.getStyleClass().add("buttons");
        start.setStyle("-fx-background-radius: 15 0 0 0");
        start.setPrefWidth(150);
        start.setMinSize(100,50);
        start.setOnAction(event -> {
            simulator(blueCar, 1);
            simulator(redCar, 2);
            simulator(whiteCar, 3);
            simulator(purpleCar, 0);
            start.setDisable(true);
        });

        reset = new Button("Restart");
        reset.getStyleClass().add("buttons");
        reset.setStyle("-fx-background-radius: 0 15 0 0");
        reset.setPrefWidth(150);
        reset.setMinSize(100,50);
        reset.setDisable(true);
        reset.setOnAction(event -> {
            trackPanel.getChildren().removeAll(blueCar.getImage(), redCar.getImage(), whiteCar.getImage(), purpleCar.getImage());
            blueCar.setImage(setUpCar(relativePath + "blue_car.png",checkpointA));
            blueCar.randomizeValues();
            redCar.setImage(setUpCar(relativePath + "red_car.png", checkpointB));
            redCar.randomizeValues();
            whiteCar.setImage(setUpCar(relativePath + "white_car.png", checkpointC));
            whiteCar.randomizeValues();
            purpleCar.setImage(setUpCar(relativePath + "purple_car.png", checkpointD));
            purpleCar.randomizeValues();
            trackPanel.getChildren().addAll(blueCar.getImage(), redCar.getImage(), whiteCar.getImage(), purpleCar.getImage());
            for (Label result: results) {
                result.setText("");
            }

            counter = 0;
            winner = false;
            start.setDisable(false);
            reset.setDisable(true);
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(start, reset);
        buttons.setAlignment(Pos.CENTER);

        //RESULTS
        Label resultsTitle = new Label("Results");
        resultsTitle.getStyleClass().add("resultsTitle");
        VBox resultsPanel = new VBox();
        resultsPanel.setMinHeight(250);
        resultsPanel.setPadding(new Insets(0, 10, 0, 10));
        results = new Label[4];
        Label resultsHeading = new Label("\tCar\t\tTime");
        resultsHeading.getStyleClass().add("results");
        resultsPanel.getChildren().add(resultsHeading);
        for (int i=0; i<4; i++) {
            results[i] = new Label();
            results[i].getStyleClass().add("results");
            results[i].setMinHeight(35);
            resultsPanel.getChildren().add(results[i]);
        }
        
        //LEFT SIDE
        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(title, trackPanel);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setMinWidth(500);
        leftSide.setSpacing(20);

        //RIGHT SIDE
        VBox rightSide = new VBox();
        rightSide.getStyleClass().add("right_side");
        rightSide.getChildren().addAll(buttons, resultsTitle, resultsPanel);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(300);
        rightSide.setMaxHeight(300);
        rightSide.setSpacing(20);
        
        //MAIN LAYOUT
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(leftSide, rightSide);
        mainLayout.setSpacing(50);

        //SCENE AND STAGE
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setTitle("Racing Simulator");
        primaryStage.show();
    }

    private ImageView setUpCar(String carFilePath, Checkpoint checkpoint) {
        Image carImg = new Image(carFilePath);
        ImageView carView = new ImageView();
        carView.setImage(carImg);
        carView.setFitWidth(100);
        carView.setPreserveRatio(true);
        carView.setTranslateX(checkpoint.getXPos());
        carView.setTranslateY(checkpoint.getYPos());
        carView.setRotate(checkpoint.getAngle());
        return carView;
    }

    private void simulator(RaceCar car, int index) {
        TranslateTransition transition = new TranslateTransition();
        transition.setToX(checkpoints[index].getXPos());
        transition.setToY(checkpoints[index].getYPos());
        transition.setDuration(Duration.seconds(car.getCalculatedSpeed()/2.5));
        
        ImageView carImage = car.getImage();
        transition.setNode(carImage);
        double angle = checkpoints[index].getAngle();
       
        if (index != car.getEndPosition()) {
            transition.setOnFinished(event -> {
                int i = index;
                carImage.setRotate(angle);
                if (i == 3) {
                    i = 0;
                } else {
                    i++;
                }
                simulator(car, i);
            });
        } else  if (index == car.getEndPosition() && !winner){
            winner = true;
            transition.setOnFinished(event -> {
                double time = (car.getCalculatedSpeed()/2.5) * 4;
                results[counter].setText(counter + 1 + "\t" + car.getColor() + "\t" + df.format(time) + "s");
                results[counter].setWrapText(true);
                counter++;
            });
        } else if (index == car.getEndPosition()){
            transition.setOnFinished(event -> {
                double time = (car.getCalculatedSpeed()/2.5) * 4;
                results[counter].setText(counter + 1 + "\t" + car.getColor() + "\t" + df.format(time) + "s");
                results[counter].setWrapText(true);
                counter++;
                if (counter == 4){
                    reset.setDisable(false);
                }
            });
        }

        transition.setNode(carImage);
        transition.play();
    }
  
}
