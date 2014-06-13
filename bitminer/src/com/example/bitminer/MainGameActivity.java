package com.example.bitminer;

//@Author - Scott DeCota - Infinity Code Services

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.view.MotionEvent;

public class MainGameActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener{

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	private Camera m_Camera;
	private Scene m_Scene;
	private ITextureRegion mBackgroundTextureRegion;
	private BitmapTextureAtlas texMiner;
	private TiledTextureRegion regMiner;
	private AnimatedSprite sprMiner;
	private static int SPR_COLUMN = 8;
	private static int SPR_ROWS = 2;

	@Override
	public EngineOptions onCreateEngineOptions()
	{
		m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions en = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
				CAMERA_WIDTH, CAMERA_HEIGHT), m_Camera);

		return en;
	}

	@Override
	protected void onCreateResources()	{
		try {
			// Setup resources
			texMiner = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
			regMiner = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texMiner, this.getAssets(),
					"gfx/walking_austin.png", 0, 0, SPR_COLUMN, SPR_ROWS);
			texMiner.load();

			ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/bg.png");
				}
			});

			backgroundTexture.load();
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);

		}catch (IOException e) {
			Debug.e(e);
		}

	}

	@Override
	protected Scene onCreateScene()
	{
		m_Scene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		SpriteBackground bgSprite = new SpriteBackground(backgroundSprite);
		m_Scene.setBackground(bgSprite);

		sprMiner = new AnimatedSprite(150, 100, regMiner, this.getVertexBufferObjectManager())	{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)	{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)	{
					sprMiner.animate(new long[] {150, 150, 150, 150, 150}, 8, 12, 3);
				}
				return true;
			}
		};
		m_Scene.registerTouchArea(sprMiner);
		m_Scene.setOnSceneTouchListener(this);
		m_Scene.attachChild(sprMiner);

		return m_Scene;
	}
	
	@Override
	public boolean onSceneTouchEvent (Scene m_Scene, TouchEvent pSceneTouchEvent)	{
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			float touchX = pSceneTouchEvent.getX();
			float touchY = pSceneTouchEvent.getY();
			float[] minerLoc = sprMiner.getSceneCenterCoordinates();
			float minerX = minerLoc[0];
			float minerY = minerLoc[1];
			
			MoveModifier sprModifier = new MoveModifier(5, minerX, touchX, minerY, touchY)
			{
			        protected void onModifierStarted(IEntity pItem)
			        {
			                super.onModifierStarted(sprMiner);
			                // Start Walking Animation
			                sprMiner.animate(new long[] {150, 150, 150, 150, 150, 150, 150, 150}, 0, 7, true);
			        }

			        protected void onModifierFinished(IEntity pItem)
			        {
			                super.onModifierFinished(sprMiner);
			                //Stop Walking Animation
			                sprMiner.stopAnimation(0);
			        }
			};
			sprMiner.registerEntityModifier(sprModifier);
		}

		return false;
	}

}
