/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Nous avons créé une seule classe plateforme qui gère le comportement de
 * la plateforme selon son type. Le type est stocké sous forme d'un int de
 * 0 à 3. Nous avons opté pour cela plutôt que de créer quatre nouvelles
 * classes avec héritage, étant donné le petit nombre de types de plateformes.
 * Il serait facile d'ajouter d'autres types de plateformes, il suffirait ici
 * de spécifier son image et sa couleur.
 *
 * Leurs interactions avec le jumper sont gérées dans la classe Jumper.
 */


import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public final class Plateforme extends Entity{

    // Types de plateformes [0]orange, [1]vert, [2]jaune, [3]rouge
    private int typePlateforme;

    private final Image[] frames;
    private boolean jumperOn;



    /**
     * Constructeur
     * @param y         posY de la plateforme
     * @param type      type de plateform à creer
     */
    public Plateforme(double y, int type){

        this.haut = 10;

        // Chargement des images pour mode space
        this.frames = new Image[]{
                new Image("/img/orange.png"),
                new Image("/img/green.png"),
                new Image("/img/yellow.png"),
                new Image("/img/red.png")
        };

        makePlatForme(y,type);
        this.image = this.frames[this.typePlateforme];
    }



    /**
     * Création de la platforme hors contructeur, nous donne l'option
     * d'avoir des plateformes qui change pendant le jeu.
     * Voir commentaire principal de Level pour la logique.
     * @param y         posY de la plateforme
     * @param type      type de plateform a creer
     */
    public void makePlatForme(double y, int type){

        // Mise à jour selon position y et type voulus
        this.y = y;
        this.typePlat(type);

        //Mise à jour de la position x et la largeur au hasard
        this.x = Math.floor(Math.random()*250);
        this.larg = Math.random() * 95 + 80;

    }



    /**
     * Met à jour les propriétés dépendant du type de la platforme.
     * @param type  type de plateforme a créer
     */
    private void typePlat(int type){

        this.typePlateforme = type;
        this.image = this.frames[type];

        //plateforme orange
        if (type == 0){
            this.color = Color.rgb(230,134,58);
        }

        //plateforme verte
        else if (type == 1){
            this.color = Color.LIGHTGREEN;
        }

        //plateforme jaune
        else if (type == 2){
            this.color = Color.rgb(230,221,58);
        }

        //plateforme rouge
        else if (type == 3){
            this.color = Color.rgb(184, 15, 36);
        }

    }



    /**
     * Setter
     */
    public void setJumperOn(boolean value){
        this.jumperOn = value;
    }



    /**
     * Getters
     */

    public boolean getJumperOn(){
        return this.jumperOn;
    }


    public boolean isOrange(){
        return this.typePlateforme == 0;
    }

    public boolean isGreen(){
        return this.typePlateforme == 1;
    }

    public boolean isYellow(){
        return this.typePlateforme == 2;
    }

    public boolean isRed(){
        return this.typePlateforme == 3;
    }

}