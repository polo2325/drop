package ca.codepet.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class GameRoot extends Game {
    public SpriteBatch batch;
    public AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // Load assets asynchronously
        assetManager.load("images/drop.png", Texture.class);
        assetManager.load("images/bucket.png", Texture.class);
        assetManager.load("images/background.png", Texture.class);
        assetManager.load("audio/drop.mp3", Sound.class);
        assetManager.load("audio/music.mp3", Music.class);

        setScreen(new FirstScreen(this));
    }
}