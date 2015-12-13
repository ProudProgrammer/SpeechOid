package gaborbalazs.speechoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnPreparedListener, OnCompletionListener, OnLongClickListener {

	private Button btPanic;
	private Button btBankutiElmegygeci;
	private Button btBankutiMajdhafuggolegesenleszminden;
	private Button btBankutiNincsfuggolegesen;
	private Button btBankutiNyomjadtegeci;
	private Button btBankutiSzopjatokki;
	private Button btMabelAzfinomlesz;
	private Button btMabelBocsanathogyelek;
	private Button btMabelKikuldtekaszobabol;
	private Button btMabelMicsodakiskurva;
	private Button btAlmasikristofSzalmazsak;
	private Button btQuagmireKapszegymelegarcpakolast;
	
	private Animation animButtons;
	
	private MediaPlayer mpBankutiElmegygeci;
	private MediaPlayer mpBankutiMajdhafuggolegesenleszminden;
	private MediaPlayer mpBankutiNincsfuggolegesen;
	private MediaPlayer mpBankutiNyomjadtegeci;
	private MediaPlayer mpBankutiSzopjatokki;
	private MediaPlayer mpMabelAzfinomlesz;
	private MediaPlayer mpMabelBocsanathogyelek;
	private MediaPlayer mpMabelKikuldtekaszobabol;
	private MediaPlayer mpMabelMicsodakiskurva;
	private MediaPlayer mpAlmasikristofSzalmazsak;
	private MediaPlayer mpQuagmireKapszegymelegarcpakolast;
	
	private MediaPlayer mpCurrent;
	private MediaPlayer mpStarting;
	
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	
	private final String PREFERENCE_1 = "gaborbalazs.speechoid.StartingMediaPlayer";
	
	private Button storedButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speechoid_main);
		
		/* Set audio volume max */
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		
		/* Set Action Bar icon */
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		
		/* Initialize Buttons */
		initializeButtons();
		disableButtons();
		
		/* Initialize Audio files */
		initializeMediaPlayers();
		
		/* Load and Set Preferences */
		sharedPref = getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		setStoredState();
	}
	
	private void initializeButtons() {
		btPanic = (Button) findViewById(R.id.bt_panic);
		btBankutiElmegygeci = (Button) findViewById(R.id.bt_bankuti_elmegygeci);
		btBankutiMajdhafuggolegesenleszminden = (Button) findViewById(R.id.bt_bankuti_majdhafuggolegesenleszminden);
		btBankutiNincsfuggolegesen = (Button) findViewById(R.id.bt_bankuti_nincsfuggolegesen);
		btBankutiNyomjadtegeci = (Button) findViewById(R.id.bt_bankuti_nyomjadtegeci);
		btBankutiSzopjatokki = (Button) findViewById(R.id.bt_bankuti_szopjatokki);
		btMabelAzfinomlesz = (Button) findViewById(R.id.bt_mabel_azfinomlesz);
		btMabelBocsanathogyelek = (Button) findViewById(R.id.bt_mabel_bocsanathogyelek);
		btMabelKikuldtekaszobabol = (Button) findViewById(R.id.bt_mabel_kikuldtekaszobabol);
		btMabelMicsodakiskurva = (Button) findViewById(R.id.bt_mabel_micsodakiskurva);
		btAlmasikristofSzalmazsak = (Button) findViewById(R.id.bt_almasikristof_szalmazsak);
		btQuagmireKapszegymelegarcpakolast = (Button) findViewById(R.id.bt_quagmire_kapszegymelegarcpakolast);
		
		btPanic.setOnLongClickListener(this);
		btBankutiElmegygeci.setOnLongClickListener(this);
		btBankutiMajdhafuggolegesenleszminden.setOnLongClickListener(this);
		btBankutiNincsfuggolegesen.setOnLongClickListener(this);
		btBankutiNyomjadtegeci.setOnLongClickListener(this);
		btBankutiSzopjatokki.setOnLongClickListener(this);
		btMabelAzfinomlesz.setOnLongClickListener(this);
		btMabelBocsanathogyelek.setOnLongClickListener(this);
		btMabelKikuldtekaszobabol.setOnLongClickListener(this);
		btMabelMicsodakiskurva.setOnLongClickListener(this);
		btAlmasikristofSzalmazsak.setOnLongClickListener(this);
		btQuagmireKapszegymelegarcpakolast.setOnLongClickListener(this);
		
		animButtons = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
	}
	
	private void initializeMediaPlayers() {
		mpBankutiElmegygeci = MediaPlayer.create(this, R.raw.bankuti_elmegygeci);
		mpBankutiMajdhafuggolegesenleszminden = MediaPlayer.create(this, R.raw.bankuti_majdhafuggolegesenleszminden);
		mpBankutiNincsfuggolegesen = MediaPlayer.create(this, R.raw.bankuti_nincsfuggolegesen);
		mpBankutiNyomjadtegeci = MediaPlayer.create(this, R.raw.bankuti_nyomjadtegeci);
		mpBankutiSzopjatokki = MediaPlayer.create(this, R.raw.bankuti_szopjatokki);
		mpMabelAzfinomlesz = MediaPlayer.create(this, R.raw.mabel_azfinomlesz);
		mpMabelBocsanathogyelek = MediaPlayer.create(this, R.raw.mabel_bocsanathogyelek);
		mpMabelKikuldtekaszobabol = MediaPlayer.create(this, R.raw.mabel_kikuldtekaszobabol);
		mpMabelMicsodakiskurva = MediaPlayer.create(this, R.raw.mabel_micsodakiskurva);
		mpAlmasikristofSzalmazsak = MediaPlayer.create(this, R.raw.almasikristof_szalmazsak);
		mpQuagmireKapszegymelegarcpakolast = MediaPlayer.create(this, R.raw.quagmire_kapszegymelegarcpakolast);
		
		mpBankutiElmegygeci.setOnCompletionListener(this);
		mpBankutiMajdhafuggolegesenleszminden.setOnCompletionListener(this);
		mpBankutiNincsfuggolegesen.setOnCompletionListener(this);
		mpBankutiNyomjadtegeci.setOnCompletionListener(this);
		mpBankutiSzopjatokki.setOnCompletionListener(this);
		mpMabelAzfinomlesz.setOnCompletionListener(this);
		mpMabelBocsanathogyelek.setOnCompletionListener(this);
		mpMabelKikuldtekaszobabol.setOnCompletionListener(this);
		mpMabelMicsodakiskurva.setOnCompletionListener(this);
		mpAlmasikristofSzalmazsak.setOnCompletionListener(this);
		mpQuagmireKapszegymelegarcpakolast.setOnCompletionListener(this);
		
		mpBankutiElmegygeci.setOnPreparedListener(this);
		mpBankutiMajdhafuggolegesenleszminden.setOnPreparedListener(this);
		mpBankutiNincsfuggolegesen.setOnPreparedListener(this);
		mpBankutiNyomjadtegeci.setOnPreparedListener(this);
		mpBankutiSzopjatokki.setOnPreparedListener(this);
		mpMabelAzfinomlesz.setOnPreparedListener(this);
		mpMabelBocsanathogyelek.setOnPreparedListener(this);
		mpMabelKikuldtekaszobabol.setOnPreparedListener(this);
		mpMabelMicsodakiskurva.setOnPreparedListener(this);
		mpAlmasikristofSzalmazsak.setOnPreparedListener(this);
		mpQuagmireKapszegymelegarcpakolast.setOnPreparedListener(this);
	}
	
	private void setStoredState() {
		String storedState = sharedPref.getString(PREFERENCE_1, "");
		if(storedState.equals("")) {
			mpStarting = null;
		} else if(storedState.equals("BankutiElmegygeci")) {
			mpStarting = mpBankutiElmegygeci;
			storedButton = btBankutiElmegygeci;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("BankutiMajdhafuggolegesenleszminden")) {
			mpStarting = mpBankutiMajdhafuggolegesenleszminden;
			storedButton = btBankutiMajdhafuggolegesenleszminden;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("BankutiNincsfuggolegesen")) {
			mpStarting = mpBankutiNincsfuggolegesen;
			storedButton = btBankutiNincsfuggolegesen;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("BankutiNyomjadtegeci")) {
			mpStarting = mpBankutiNyomjadtegeci;
			storedButton = btBankutiNyomjadtegeci;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("BankutiSzopjatokki")) {
			mpStarting = mpBankutiSzopjatokki;
			storedButton = btBankutiSzopjatokki;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("MabelAzfinomlesz")) {
			mpStarting = mpMabelAzfinomlesz;
			storedButton = btMabelAzfinomlesz;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("MabelBocsanathogyelek")) {
			mpStarting = mpMabelBocsanathogyelek;
			storedButton = btMabelBocsanathogyelek;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("MabelKikuldtekaszobabol")) {
			mpStarting = mpMabelKikuldtekaszobabol;
			storedButton = btMabelKikuldtekaszobabol;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("MabelMicsodakiskurva")) {
			mpStarting = mpMabelMicsodakiskurva;
			storedButton = btMabelMicsodakiskurva;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("AlmasikristofSzalmazsak")) {
			mpStarting = mpAlmasikristofSzalmazsak;
			storedButton = btAlmasikristofSzalmazsak;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		} else if(storedState.equals("QuagmireKapszegymelegarcpakolast")) {
			mpStarting = mpQuagmireKapszegymelegarcpakolast;
			storedButton = btQuagmireKapszegymelegarcpakolast;
			storedButton.setBackgroundResource(R.drawable.button_stored);
		}
	}
	
	private void setButtonsBackground(Button longClickedButton) {
		if(longClickedButton == storedButton) {
			return;
		} else if(longClickedButton.getId() == R.id.bt_panic) {
			if(storedButton != null) {
				storedButton.setBackgroundResource(R.drawable.button);
				storedButton = null;
			}
		} else {
			if(storedButton != null) {
				storedButton.setBackgroundResource(R.drawable.button);
			}
			longClickedButton.setBackgroundResource(R.drawable.button_stored);
			storedButton = longClickedButton;
		}
	}
	
	private void showAlertMessage(int message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
	private void disableButtons() {
		btBankutiElmegygeci.setEnabled(false);
		btBankutiMajdhafuggolegesenleszminden.setEnabled(false);
		btBankutiNincsfuggolegesen.setEnabled(false);
		btBankutiNyomjadtegeci.setEnabled(false);
		btBankutiSzopjatokki.setEnabled(false);
		btMabelAzfinomlesz.setEnabled(false);
		btMabelBocsanathogyelek.setEnabled(false);
		btMabelKikuldtekaszobabol.setEnabled(false);
		btMabelMicsodakiskurva.setEnabled(false);
		btAlmasikristofSzalmazsak.setEnabled(false);
		btQuagmireKapszegymelegarcpakolast.setEnabled(false);
	}
	
	private void enableButtons() {
		btBankutiElmegygeci.setEnabled(true);
		btBankutiMajdhafuggolegesenleszminden.setEnabled(true);
		btBankutiNincsfuggolegesen.setEnabled(true);
		btBankutiNyomjadtegeci.setEnabled(true);
		btBankutiSzopjatokki.setEnabled(true);
		btMabelAzfinomlesz.setEnabled(true);
		btMabelBocsanathogyelek.setEnabled(true);
		btMabelKikuldtekaszobabol.setEnabled(true);
		btMabelMicsodakiskurva.setEnabled(true);
		btAlmasikristofSzalmazsak.setEnabled(true);
		btQuagmireKapszegymelegarcpakolast.setEnabled(true);
	}
	
	private void stopMediaPlayer() {
		if(mpCurrent != null) {
			mpCurrent.seekTo(0);
			mpCurrent.pause();
			enableButtons();
		}
	}
	
	/*
	 * Event Handlers
	 */

	@Override
	public void onPrepared(MediaPlayer mp) {
		if(mpStarting != null) {
			mpCurrent = mpStarting;
			mpStarting.start();
		} else {
			enableButtons();
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		enableButtons();
	}

	@Override
	public boolean onLongClick(View v) {
		if(v.getId() == R.id.bt_panic) {
			editor.putString(PREFERENCE_1, "");
			editor.commit();
			setButtonsBackground(btPanic);
			showAlertMessage(R.string.starting_media_has_been_deleted);
		} else if(v.getId() == R.id.bt_bankuti_elmegygeci) {
			editor.putString(PREFERENCE_1, "BankutiElmegygeci");
			editor.commit();
			setButtonsBackground(btBankutiElmegygeci);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_bankuti_majdhafuggolegesenleszminden) {
			editor.putString(PREFERENCE_1, "BankutiMajdhafuggolegesenleszminden");
			editor.commit();
			setButtonsBackground(btBankutiMajdhafuggolegesenleszminden);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_bankuti_nincsfuggolegesen) {
			editor.putString(PREFERENCE_1, "BankutiNincsfuggolegesen");
			editor.commit();
			setButtonsBackground(btBankutiNincsfuggolegesen);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_bankuti_nyomjadtegeci) {
			editor.putString(PREFERENCE_1, "BankutiNyomjadtegeci");
			editor.commit();
			setButtonsBackground(btBankutiNyomjadtegeci);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_bankuti_szopjatokki) {
			editor.putString(PREFERENCE_1, "BankutiSzopjatokki");
			editor.commit();
			setButtonsBackground(btBankutiSzopjatokki);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_mabel_azfinomlesz) {
			editor.putString(PREFERENCE_1, "MabelAzfinomlesz");
			editor.commit();
			setButtonsBackground(btMabelAzfinomlesz);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_mabel_bocsanathogyelek) {
			editor.putString(PREFERENCE_1, "MabelBocsanathogyelek");
			editor.commit();
			setButtonsBackground(btMabelBocsanathogyelek);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_mabel_kikuldtekaszobabol) {
			editor.putString(PREFERENCE_1, "MabelKikuldtekaszobabol");
			editor.commit();
			setButtonsBackground(btMabelKikuldtekaszobabol);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_mabel_micsodakiskurva) {
			editor.putString(PREFERENCE_1, "MabelMicsodakiskurva");
			editor.commit();
			setButtonsBackground(btMabelMicsodakiskurva);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_almasikristof_szalmazsak) {
			editor.putString(PREFERENCE_1, "AlmasikristofSzalmazsak");
			editor.commit();
			setButtonsBackground(btAlmasikristofSzalmazsak);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		} else if(v.getId() == R.id.bt_quagmire_kapszegymelegarcpakolast) {
			editor.putString(PREFERENCE_1, "QuagmireKapszegymelegarcpakolast");
			editor.commit();
			setButtonsBackground(btQuagmireKapszegymelegarcpakolast);
			showAlertMessage(R.string.starting_media_has_been_adjusted);
		}
		return true;
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.bt_panic) {
			stopMediaPlayer();
		} else if(v.getId() == R.id.bt_bankuti_elmegygeci) {
			mpCurrent = mpBankutiElmegygeci;
			disableButtons();
			btBankutiElmegygeci.startAnimation(animButtons);
			mpBankutiElmegygeci.start();
		} else if(v.getId() == R.id.bt_bankuti_majdhafuggolegesenleszminden) {
			mpCurrent = mpBankutiMajdhafuggolegesenleszminden;
			disableButtons();
			btBankutiMajdhafuggolegesenleszminden.startAnimation(animButtons);
			mpBankutiMajdhafuggolegesenleszminden.start();
		} else if(v.getId() == R.id.bt_bankuti_nincsfuggolegesen) {
			mpCurrent = mpBankutiNincsfuggolegesen;
			disableButtons();
			btBankutiNincsfuggolegesen.startAnimation(animButtons);
			mpBankutiNincsfuggolegesen.start();
		} else if(v.getId() == R.id.bt_bankuti_nyomjadtegeci) {
			mpCurrent = mpBankutiNyomjadtegeci;
			disableButtons();
			btBankutiNyomjadtegeci.startAnimation(animButtons);
			mpBankutiNyomjadtegeci.start();
		} else if(v.getId() == R.id.bt_bankuti_szopjatokki) {
			mpCurrent = mpBankutiSzopjatokki;
			disableButtons();
			btBankutiSzopjatokki.startAnimation(animButtons);
			mpBankutiSzopjatokki.start();
		} else if(v.getId() == R.id.bt_mabel_azfinomlesz) {
			mpCurrent = mpMabelAzfinomlesz;
			disableButtons();
			btMabelAzfinomlesz.startAnimation(animButtons);
			mpMabelAzfinomlesz.start();
		} else if(v.getId() == R.id.bt_mabel_bocsanathogyelek) {
			mpCurrent = mpMabelBocsanathogyelek;
			disableButtons();
			btMabelBocsanathogyelek.startAnimation(animButtons);
			mpMabelBocsanathogyelek.start();
		} else if(v.getId() == R.id.bt_mabel_kikuldtekaszobabol) {
			mpCurrent = mpMabelKikuldtekaszobabol;
			disableButtons();
			btMabelKikuldtekaszobabol.startAnimation(animButtons);
			mpMabelKikuldtekaszobabol.start();
		} else if(v.getId() == R.id.bt_mabel_micsodakiskurva) {
			mpCurrent = mpMabelMicsodakiskurva;
			disableButtons();
			btMabelMicsodakiskurva.startAnimation(animButtons);
			mpMabelMicsodakiskurva.start();
		} else if(v.getId() == R.id.bt_almasikristof_szalmazsak) {
			mpCurrent = mpAlmasikristofSzalmazsak;
			disableButtons();
			btAlmasikristofSzalmazsak.startAnimation(animButtons);
			mpAlmasikristofSzalmazsak.start();
		} else if(v.getId() == R.id.bt_quagmire_kapszegymelegarcpakolast) {
			mpCurrent = mpQuagmireKapszegymelegarcpakolast;
			disableButtons();
			btQuagmireKapszegymelegarcpakolast.startAnimation(animButtons);
			mpQuagmireKapszegymelegarcpakolast.start();
		}
	}
}
