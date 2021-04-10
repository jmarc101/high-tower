/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Addition du tableau SpaceLevel. Changement du background pour une l'image;
 * changement des couleurs et de la taille  des bulles; changement des
 * plateformes pour des images.
 */


import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public final class SpaceLevel extends Level {

    /**
     * Constructeur
     * @param width     Largeur du tableau
     * @param height    Hauteur du tableau
     */
    public SpaceLevel(double width, double height){

        super(width,height);

        this.jumper = new NyanCat(width/2-25,0, this);

        this.image = new Image("/img/space.gif");

    }



    /**
     * Override des bubbles pour avoir un look différent
     * @param numBubbles    nombre de bulles voulu
     */
    @Override
    public void makeBubbles(int numBubbles){

        double[] baseX = {
                Math.random() * width,
                Math.random() * width,
                Math.random() * width
        };

        this.bubbles = new Bubbles[numBubbles];

        for (int i = 0; i < bubbles.length; i++) {

            if (i % 3 == 0) {
                this.bubbles[i] = new Bubbles(baseX[2],
                        0.3,Color.rgb(255,0,0,0.8), height);
            } else if (i % 2 == 0) {
                this.bubbles[i] = new Bubbles(baseX[1],
                        0.3, Color.rgb(255,250,0,0.5), height);
            } else {
                this.bubbles[i] = new Bubbles(baseX[0],
                        0.3, Color.rgb(255,0,255,0.3), height);
            }
        }

    }
}
