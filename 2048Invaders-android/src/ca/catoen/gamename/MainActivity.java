package ca.catoen.gamename;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ca.catoen.gamename.GameMain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class MainActivity extends AndroidApplication implements
		GameHelperListener, ActionResolver {

	private GameHelper gameHelper;
	
	private String [] achievements = {"CgkIvrH8x6sSEAIQAA","CgkIvrH8x6sSEAIQAQ","CgkIvrH8x6sSEAIQAg",
			"CgkIvrH8x6sSEAIQAw","CgkIvrH8x6sSEAIQBA","CgkIvrH8x6sSEAIQBQ","CgkIvrH8x6sSEAIQCA"," CgkIvrH8x6sSEAIQCQ",
			"CgkIvrH8x6sSEAIQCg","CgkIvrH8x6sSEAIQCw","CgkIvrH8x6sSEAIQDA",
			"CgkIvrH8x6sSEAIQDQ","CgkIvrH8x6sSEAIQDg","CgkIvrH8x6sSEAIQDw","CgkIvrH8x6sSEAIQEA",
			"CgkIvrH8x6sSEAIQEQ","CgkIvrH8x6sSEAIQEg","CgkIvrH8x6sSEAIQEw","CgkIvrH8x6sSEAIQFA",
			"CgkIvrH8x6sSEAIQFQ" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		// cfg.hideStatusBar = true; //set to true by default

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.STATUS_BAR_VISIBLE);
			getWindow().getDecorView().setSystemUiVisibility(
					View.STATUS_BAR_HIDDEN);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
																	// bar
							| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
							| View.SYSTEM_UI_FLAG_IMMERSIVE);
		}

		cfg.useGL20 = false;

		initialize(new GameMain(this), cfg);

		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(true);
		}
		gameHelper.setup(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(),
				"CgkIvrH8x6sSEAIQBg", score);

	}

	@Override
	public void unlockAchievementGPGS(int achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievements[achievementId]);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							gameHelper.getApiClient(), "CgkIvrH8x6sSEAIQBg"),
					100);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}

	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}

	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub

	}

}