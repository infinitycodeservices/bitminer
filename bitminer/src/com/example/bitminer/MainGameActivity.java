package com.example.bitminer;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.view.MotionEvent;

public class MainGameActivity extends SimpleBaseGameActivity {
	
	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	private Camera m_Camera;
	private Scene m_Scene;
	private BitmapTextureAtlas texMiner;
	private TiledTextureRegion regMiner;
	private AnimatedSprite sprMiner;
	private static int SPR_COLUMN = 6;
	private static int SPR_ROWS = 1;
	
	 @Override
	 public EngineOptions onCreateEngineOptions()
	 {
	  m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	  EngineOptions en = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
	    CAMERA_WIDTH, CAMERA_HEIGHT), m_Camera);
	 
	  return en;
	 }
	 
	 @Override
	 protected void onCreateResources()
	 {
	  // TODO Auto-generated method stub
	  texMiner = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
	  regMiner = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texMiner, this.getAssets(),
	    "gfx/miner_austin.png", 0, 0, SPR_COLUMN, SPR_ROWS);
	  texMiner.load();
	 
	 }
	 
	 @Override
	 protected Scene onCreateScene()
	 {
	  m_Scene = new Scene();
	  m_Scene.setBackground(new Background(Color.WHITE));
	 
	  sprMiner = new AnimatedSprite(150, 100, regMiner, this.getVertexBufferObjectManager())	{
		  @Override
		  public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)	{
			  if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)	{
				  sprMiner.animate(new long[] {150, 150, 150, 150, 200}, 0, 4, 3);
			  }
			  return true;
		  }
	  };
	  m_Scene.registerTouchArea(sprMiner);
	  m_Scene.attachChild(sprMiner);
	 
	  return m_Scene;
	 }

}
