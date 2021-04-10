/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Personnage créé à partir de la classe abstraite Jumper
 * avec images et grandeurs appropriées.
 *
 */


import javafx.scene.image.Image;


public final class Octopus extends Jumper {

    /**
     * Constructeur
     * @param x position x initiale du jumper
     * @param y position y initiale du jumper
     * @param level le niveau dans lequel le personnage est
     */
    public Octopus(double x, double y, Level level){
        super(x,y, level);

        this.haut = 50;
        this.larg = 50;
        this.frameRate = 8.0;

        this.frames = new Image[]{
                new Image("/img/jellyfish1.png"),
                new Image("/img/jellyfish2.png"),
                new Image("/img/jellyfish3.png"),
                new Image("/img/jellyfish4.png"),
                new Image("/img/jellyfish5.png"),
                new Image("/img/jellyfish6.png")
        };

        this.framesLeft = new Image[]{
                new Image("/img/jellyfish1g.png"),
                new Image("/img/jellyfish2g.png"),
                new Image("/img/jellyfish3g.png"),
                new Image("/img/jellyfish4g.png"),
                new Image("/img/jellyfish5g.png"),
                new Image("/img/jellyfish6g.png")
        };

        this.image = this.frames[0];
    }
}
