package com.smirkstudio.passphrasegenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.text.ClipboardManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PassphraseGeneratorActivity extends Activity {
	/** Called when the activity is first created. */
	
	public String[] wordlist;
	public TextView numWordsLabel;
	public SeekBar numWords;
	public TextView passphraseOutput;
	public Button copyButton;
	public TextView outputLabel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		wordlist = getWordList();

		numWordsLabel = (TextView) findViewById(R.id.numWordsLabel);
		numWords = (SeekBar) findViewById(R.id.numWords);
		
		numWords.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// Change the label to show the current number of words, when the seekbar is changed.
				numWordsLabel.setText("Number of words: " + Integer.toString(progress));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
	}
	
	/**
	 * Grab the value of the slider, and create the outputted passphrase
	 * by getting that number of random values from the wordlist created
	 * in the getWordList() method.
	 * 
	 * @param view
	 */
	public void setPassphrase(View view) {
		numWords = (SeekBar) findViewById(R.id.numWords);
		int maxWords = numWords.getProgress();
		Random rand = new Random();
		int length = wordlist.length;
		
		String output = "";
		for(int i=0; i < maxWords; i++) {
			output = output + " " + wordlist[rand.nextInt(length)];
		}
		passphraseOutput = (TextView) findViewById(R.id.passphraseOutput);
		passphraseOutput.setText(output.trim());
		
		copyButton = (Button) findViewById(R.id.copyButton);
		outputLabel = (TextView) findViewById(R.id.outputLabel);
		if (!output.equals("")) {
			copyButton.setVisibility(0);
			outputLabel.setVisibility(0);
		} else {
			copyButton.setVisibility(1);
			outputLabel.setVisibility(1);
		}
	}
	
	/**
	 * Grab the generated passphrase and copy it to the Android clipboard.
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void copyToClipboard(View view) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
		clipboard.setText(passphraseOutput.getText());
	}
	
	/**
	 * Read through the text file of English words and convert
	 * them into an array for use by the app.
	 * 
	 * @return String[[ wordlist
	 *   A String array list of words from the file.
	 */
	public String[] getWordList() {
		// First, we get the text file from the assets folder.
		AssetManager assetManager = getAssets();
		InputStream stream = null;
		try {
			stream = assetManager.open("words.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Then, we convert it to an ArrayList using a BufferedReader to read it.
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Finally, that ArrayList becomes an array.
		final String[] wordlist = lines.toArray(new String[lines.size()]);
		return wordlist;
	}
}