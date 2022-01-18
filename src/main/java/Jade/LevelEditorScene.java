package Jade;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene{

    public LevelEditorScene (){
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        for(int x=0; x < 500; x++) {

            GameObject go = new GameObject("Obj" + x + ", " + 100, new Transform(new Vector2f(x *10, 10 + (x*10)), new Vector2f(10, 10)));
            go.addComponent(new SpriteRenderer(new Vector4f(0.01f * x, 1, 1, 1)));
            this.addGameObjectToScene(go);

        }
    }

    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }


}
