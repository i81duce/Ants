package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    static final int WIDTH = 800; // setting a permanent value to variables
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 100;

    ArrayList<Ant> ants; // creating an empty arrayList of Ants called ants;
    long lastTimestamp = 0; //

    static ArrayList<Ant> createAnts() { // creating method that creates ants!!!! Returns an arrayList of ants
        ArrayList<Ant> ants = new ArrayList<>(); // creating an empty arrayList of Ants called ants;
        for (int i = 0; i < ANT_COUNT; i++) { // for-loop that loops from 0-99. ANT COUNT IS 100
            Random r = new Random(); // creates a Random generator that we're calling 'r'
            ants.add(new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT), Color.BLACK)); // adding new Ant object which contains an 'x' int parameter, a 'y' int parameter, and a Color parameter.
        }                                                                        // picks a random # between 0-800 for width, 0-600 for height, and assigns it to Ant, 99 (100?) instances
        return ants; // returning the arrayList that we called 'ants' which now contains 99 (100?) new Ants which all have unique random coordinates
    }

    void drawAnts(GraphicsContext context) { // GraphicsContext allows for COOL THINGS TO HAPPEN, visually, in this case. Calling it 'context'
        context.clearRect(0, 0, WIDTH, HEIGHT); //
        for (Ant ant : ants) { // for every ant in ants...
            context.setFill(ant.color); // give them the color BLACK..
            context.fillOval(ant.x, ant.y, 5, 5); // and their size is 5x5 px?
        }
    }

//    Ant aggravateAnt(Ant ant) {
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Ant> nearAnts = ants.stream()
//                .filter(ant2 -> {
//                    return (Math.abs(ant.x - ant2.x) < 10) && (Math.abs(ant.y - ant2.y) < 10);
//                })
//                .collect(Collectors.toCollection(ArrayList<Ant>::new));
//        if (nearAnts.size() > 1) {
//            ant.color = Color.RED;
//        } else {
//            ant.color = Color.BLACK;
//        }
//        return ant;
//    }

    static double randomStep() { // creating this method to make the ants move!!!! RANDOMLY!!!
        return Math.random() * 2 - 1; // just returns a random number between 0.0-1.0 and multiplies it by 2, then divides be 1, meaning...
    }                                 // ...the number is now between -1.0 and 1.0. I think this allows the "ants" to move in a negative direction/backwards

    Ant moveAnt(Ant ant) { // never seen a method without 'static', 'public', 'void', or 'private' before. Neat!! ...
        try {              // ...We're moving ants here!!!!! Kinda. More like setting it up for them to move. Takes an Ant object as the argument. We'll be returning an updated Ant position
            Thread.sleep(1); // suspends execution by 1 nanosecond? Pauses.
        } catch (InterruptedException e) { // won't really ever reach the catch using this
            e.printStackTrace();
        }
        ant.x += randomStep(); // ant.x + randomStep = ant.x. New value
        ant.y += randomStep(); // ant.y + randomStep = ant.y. New value
        return ant; // returning Ant object
    }

    void updateAnts() { // no return type. THIS IS WHERE WE MOVE ANTS!!!!
        ants = ants.parallelStream() //
//                .map(this::aggravateAnt)
                .map(this::moveAnt)
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
    }

    int fps(long now) {
        double diff = now - lastTimestamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1 / diffSeconds);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = (Canvas) scene.lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();
        Label fpsLabel = (Label) scene.lookup("#fps");

        primaryStage.setTitle("Ants");
        primaryStage.setScene(scene);
        primaryStage.show();

        ants = createAnts();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsLabel.setText(fps(now) + "");
                lastTimestamp = now;
                updateAnts();
                drawAnts(context);
            }
        };
        timer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
