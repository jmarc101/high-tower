/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Tableau de base demandé par l'énoncé du TP
 */


import javafx.scene.paint.Color;


public final class SeaLevel extends Level {

    /**
     * Constructeur
     * @param width     Largeur du tableau
     * @param height    Hauteur du tableau
     */
    public SeaLevel(double width, double height){

        super(width,height);

        this.color = Color.DARKBLUE;

        this.jumper = new Octopus(width/2-25,0, this);

    }

}
