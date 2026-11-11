package bricker.gameobjects;

import bricker.lives.Lives;
import danogl.GameObject;
import bricker.lives.LivesObserver;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class NumericLifeCounter extends GameObject  implements LivesObserver {

    private Lives live;
    
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

    }

    public void onLivesChanged(int lives) {

    }
}
