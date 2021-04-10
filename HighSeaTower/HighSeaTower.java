/*
* Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
* Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
* Hugo Scherer  (957841) hugo.sch.42@gmail.com
*
* Voici notre TP2, un jeu de platforme créé sur Java avec la librairie JavaFX.
* Le jeu consiste à monter le plus haut possible dans le tableau. Le système de
* score est en mètres.
*
* Optimisation créées et ajouts hors de l'énoncé: voir BONUS.txt
*
*/


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;


public final class HighSeaTower extends Application {

    private static final int WIDTH = 350, HEIGHT = 480;



    public static void main(String[] args) { launch(args); }



    @Override
    public void start(Stage stage) {

        Controller controller = new Controller(this);


        // ***** Scene principale et canevas *****

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();

        // Affichage du score
        Text metres = new Text(0, 42, "0 m");
        metres.setWrappingWidth(WIDTH);
        metres.setTextAlignment(TextAlignment.CENTER);
        metres.setFill(Color.WHITE);
        metres.setFont(Font.font("Impact", 25));
        root.getChildren().add(metres);

        // Texte mode debug
        VBox debugMode = new VBox();
        Text position = new Text("");
        position.setFill(Color.WHITE);
        Text vitesse = new Text((""));
        vitesse.setFill(Color.WHITE);
        Text accel = new Text("");
        accel.setFill(Color.WHITE);
        Text plancher = new Text((""));
        plancher.setFill(Color.WHITE);
        debugMode.getChildren().addAll(position, vitesse, accel, plancher);
        root.getChildren().add(debugMode);


        // ***** Scene du main menu *****

        Pane mainMenu = new Pane();
        Scene menuScene = new Scene(mainMenu, WIDTH, HEIGHT);
        VBox menuBox = new VBox();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setSpacing(50);
        menuBox.setLayoutX(35);
        menuBox.setLayoutY(50);

        // Background main menu
        Image menu = new Image("/img/mainmenu.jpg");
        ImageView menuIm = new ImageView();
        menuIm.setImage(menu);
        menuIm.setFitWidth(WIDTH);
        menuIm.setFitHeight(HEIGHT);

        // Texte main menu
        Text menuText = new Text("High Sea Tower\n\n\n Please Choose Game Mode");
        menuText.setTextAlignment(TextAlignment.CENTER);
        menuText.setFont(Font.font("Impact", 25));
        menuText.setFill(Color.rgb(188, 230, 247));

        // Bouton main menu
        Image normalIma = new Image("/img/normalBut.png");
        Image spaceIma = new Image("/img/spaceBut.png");
        Image quitIm = new Image("/img/quitBut.png");
        ImageView normal = new ImageView();
        normal.setImage(normalIma);
        normal.setFitHeight(30);
        normal.setFitWidth(100);
        ImageView space = new ImageView();
        space.setImage(spaceIma);
        space.setFitHeight(30);
        space.setFitWidth(100);
        ImageView quit = new ImageView();
        quit.setImage(quitIm);
        quit.setFitHeight(15);
        quit.setFitWidth(50);
        quit.setX(270);
        quit.setY(20);

        menuBox.getChildren().addAll(menuText, normal, space);
        mainMenu.getChildren().addAll(menuIm, menuBox, quit);


        // ***** Scene Game Over *****

        Pane gameOver = new Pane();
        Scene gameOverScene = new Scene(gameOver, WIDTH, HEIGHT);
        Image overIm = new Image("/img/gameover.jpg");

        ImageView gameOverView = new ImageView(overIm);
        gameOverView.setFitWidth(WIDTH);
        gameOverView.setFitHeight(HEIGHT);

        Text score = new Text();
        score.setFont(Font.font("Impact", 25));
        score.setFill(Color.WHITE);
        score.setLayoutX(70);
        score.setLayoutY(100);
        Text gameOverText = new Text("Space to Restart Game\n\n" +
                " Press M for MainMenu \n\n Escape to Quit Game ");
        gameOver.getChildren().addAll( gameOverView, score, gameOverText);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setFont(Font.font("Impact", 15));
        gameOverText.setFill(Color.WHITE);
        gameOverText.setLayoutX(110);
        gameOverText.setLayoutY(350);



        // ********************
        // *** Timer Events ***
        // ********************

        AnimationTimer timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {

                if (lastTime == 0){
                    lastTime = now;
                    return;
                }


                // update le jeu
                double dt = (now - lastTime) * 1e-9;
                controller.updateLevel(dt, context);


                // affiche le score total
                metres.setText(controller.getHighScore() + " m");


                // ** DEBUG MODE TEXT **

                // Texte vide si pas en mode debug
                position.setText("");
                vitesse.setText("");
                accel.setText("");
                plancher.setText((""));

                /* Récupère les infos si en mode debug.
                 * Information fournie sous forme de tableau.
                 * L'info selon la position est:
                 * 0: position en x, 1: position en y,
                 * 2: vitesse en x, 3: vitesse en y,
                 * 4: accélération en x, 5: accélération en y,
                 * 6: vaut 0 si ne touche pas le sol, 1 si touche le sol.
                 */
                if (controller.getDebug()) {

                    int[] debugInfo = controller.getDebugInfo();

                    position.setText("Position = (" + debugInfo[0] +
                            ", " + debugInfo[1] + ")");
                    vitesse.setText("v = (" + debugInfo[2] + ", " +
                            debugInfo[3] + ")");
                    accel.setText("a = (" +  debugInfo[4] + ", " +
                            debugInfo[5] + ")");
                    plancher.setText("Touche le sol : Non");

                    if (debugInfo[6] == 1){
                        plancher.setText("Touche le sol : Oui");
                    }
                }


                // Si le jumper meurt
                if (controller.getDead()) {
                    score.setText("Score Final = " + controller.getHighScore() + " m");
                    stage.setScene(gameOverScene);
                }

                lastTime = now;
            }
        };

        timer.start();



        // ***************
        // *** CONTROL ***
        // ***************


        // *** Scene main menu ***

        // Boutons
        space.setOnMouseClicked(e->{
            controller.setSpaceMode();
            stage.setScene(scene);
        });

        normal.setOnMouseClicked(e->{
            controller.setNormalMode();
            stage.setScene(scene);
        });

        quit.setOnMouseClicked(e->{
            Platform.exit();
            System.exit(0);
        });

        // Clavier
        menuScene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
                System.exit(0);
            }
        });



        // *** Scene game over ***

        // Clavier
        gameOverScene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.SPACE) {
                controller.restartGame();
                stage.setScene(scene);
            }
            else if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
                System.exit(0);
            }
            else if (e.getCode() == KeyCode.M){
                controller.reInit();
                stage.setScene(menuScene);
            }
        });


        // *** Scene principale (level) ***

        // Contrôle durant le jeu avec le clavier
        scene.setOnKeyPressed((value) -> {

            controller.startGame();

            if (value.getCode() == KeyCode.SPACE || (value.getCode() == KeyCode.UP )) {
                controller.jump();
            }

            else if (value.getCode() == KeyCode.RIGHT) {
                controller.right();
            }

            else if (value.getCode() == KeyCode.LEFT) {
                controller.left();
            }

            else if (value.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
                System.exit(0);
            }

            //DEBUG MODE
            else if (value.getCode() == KeyCode.T) {
                controller.setDebugMode();
            }

        });

        scene.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.RIGHT) {
                controller.stop();
            }
            else if (e.getCode() == KeyCode.LEFT) {
                controller.stop();
            }
        });



        // *** Stage ***

        stage.setTitle("High Sea Tower");
        stage.getIcons().add(new Image("/img/jellyfish1.png"));
        stage.setResizable(false);
        stage.setScene(menuScene);
        stage.show();

        // Si l'utilisateur ferme avec le X de la fenêtre, fermeture de l'application
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,event ->{
            Platform.exit();
            System.exit(0);
        });
    }



    /**
     * Getters
     */

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}
