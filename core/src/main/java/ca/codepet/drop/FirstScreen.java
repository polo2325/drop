package ca.codepet.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    private final GameRoot game;

    Sprite background;
    Sprite bucket;
    Array<Sprite> raindrops;

    long lastDropTime;

    public FirstScreen(GameRoot game) {
        this.game = game;
        raindrops = new Array<>();
    }

    @Override
    public void show() {
        // Prepare your screen here.

        // Blocks execution until all assets are loaded
        game.assetManager.finishLoading();
        Texture backgroundTexture = game.assetManager.get("images/background.png");
        background = new Sprite(backgroundTexture);
        background.setSize(800, 480);
        background.setPosition(0, 0);

        Texture bucketTexture = game.assetManager.get("images/bucket.png");
        bucket = new Sprite(bucketTexture);
        bucket.setSize(100, 100);
        bucket.setPosition(800 / 2 - 100 / 2, 20);

        Music music = game.assetManager.get("audio/music.mp3");
        music.setLooping(true);
        music.play();

        spawnRaindrop();
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing the sprites on the batch
        game.batch.begin();

        background.draw(game.batch);
        bucket.draw(game.batch);
        for (Sprite raindrop : raindrops) {
            raindrop.draw(game.batch);
        }

        // Stop drawing sprites
        game.batch.end();

        // handle input
        handleInput();

        // update raindrops
        updateRaindrops();
    }

    /**
     * Spawns a raindrop at a random position at the top of the screen.
     */
    private void spawnRaindrop() {
        Texture dropTexture = game.assetManager.get("images/drop.png");
        Sprite raindropSprite = new Sprite(dropTexture);
        raindropSprite.setSize(64, 64);
        raindropSprite.setPosition((float) (Math.random() * 800), 480);
        raindrops.add(raindropSprite);
        lastDropTime = TimeUtils.nanoTime();
    }

    /**
     * Handles input from the user.
     */
    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            bucket.setX(touchPos.x - bucket.getWidth() / 2);
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.setX(bucket.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.setX(bucket.getX() + 200 * Gdx.graphics.getDeltaTime());
    }

    /**
     * Updates the raindrops on the screen.
     */
    private void updateRaindrops() {
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        Sound dropSound = game.assetManager.get("audio/drop.mp3", Sound.class);
        for (Sprite raindrop : raindrops) {
            raindrop.translateY(-200 * Gdx.graphics.getDeltaTime());
            if (raindrop.getY() + 64 < 0)
                raindrops.removeValue(raindrop, true);
            if (raindrop.getBoundingRectangle().overlaps(bucket.getBoundingRectangle())) {
                dropSound.play();
                raindrops.removeValue(raindrop, true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

}