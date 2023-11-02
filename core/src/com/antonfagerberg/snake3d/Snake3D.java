package com.antonfagerberg.snake3d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.LinkedList;
import java.util.Random;

public class Snake3D extends ApplicationAdapter {

    LinkedList<ModelInstance> tail = new LinkedList<>();

    static class Triple {
        int x, y, z;

        public Triple(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        static Triple copy(Triple t) {
            return new Triple(t.x, t.y, t.z);
        }
    }

    Triple up = new Triple(0, 1, 0);
    Triple down = new Triple(0, -1, 0);
    Triple left = new Triple(-1, 0, 0);
    Triple right = new Triple(1, 0, 0);
    Triple backward = new Triple(0, 0, 1);
    Triple forward = new Triple(0, 0, -1);
    Triple direction = up;

    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public ModelInstance head;
    public ModelInstance dot;
    public ModelInstance world;
    public Environment environment;
    public CameraInputController camController;
    public ModelBuilder modelBuilder;

    Triple headCoordinate, dotCoordinate;
    int tick;
    Random random = new Random();

    ModelInstance spawnBlock(Color color) {
        return new ModelInstance(modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
    }

    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        modelBuilder = new ModelBuilder();

        head = spawnBlock(Color.FOREST);

        dot = spawnBlock(Color.WHITE);

        world = new ModelInstance(modelBuilder.createBox(10f, 10f, 10f,
                new Material(ColorAttribute.createDiffuse(1, 1, 1, 0.1f)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        dotCoordinate = new Triple(0, 0, 0);
        headCoordinate = new Triple(0, 0, 0);
        moveDot();
    }

    void moveDot() {
        dotCoordinate.x = -5 + random.nextInt(11);
        dotCoordinate.y = -5 + random.nextInt(11);
        dotCoordinate.z = -5 + random.nextInt(11);
        dot.transform.setToTranslation(dotCoordinate.x, dotCoordinate.y, dotCoordinate.z);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1, true);

        tick++;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction = up;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction = down;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction = left;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction = right;
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            direction = backward;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            direction = forward;
        }

        if (tick >= 30) {
            tick = 0;

            Triple previousCoordinate = Triple.copy(headCoordinate);

            headCoordinate.x += direction.x;
            headCoordinate.y += direction.y;
            headCoordinate.z += direction.z;

            ModelInstance tailPiece = spawnBlock(Color.GREEN);
            tailPiece.transform.setToTranslation(previousCoordinate.x, previousCoordinate.y, previousCoordinate.z);
            tail.addFirst(tailPiece);

            if (headCoordinate.x == dotCoordinate.x && headCoordinate.y == dotCoordinate.y && headCoordinate.z == dotCoordinate.z) {
                moveDot();
            } else {
                tail.removeLast();
            }

            head.transform.setToTranslation(headCoordinate.x, headCoordinate.y, headCoordinate.z);
        }




        modelBatch.begin(cam);
//        modelBatch.render(world, environment);
        modelBatch.render(head, environment);
        modelBatch.render(dot, environment);
        for (ModelInstance tailPiece : tail) {
            modelBatch.render(tailPiece, environment);
        }
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        head.model.dispose();
        dot.model.dispose();
    }
}
