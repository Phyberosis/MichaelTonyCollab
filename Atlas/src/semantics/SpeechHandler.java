package semantics;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import gui.Interface;
import gui.Log;

public class SpeechHandler {

	// Logger
	//private Logger logger = Logger.getLogger(getClass().getName());

	// Variables
	private String result;
	private Interface mInterface;
	// Threads
	Thread	speechThread;
	Thread	resourcesThread;

	// LiveRecognizer
	private LiveSpeechRecognizer recognizer;

	/**
	 * Constructor
	 */
	public SpeechHandler(Interface i) {

		// Loading Message
		Log.println("Loading speech recognition...", true);
		//logger.log(Level.INFO, "Loading speech recognition...");
		
		// Configuration
		Configuration configuration = new Configuration();
		mInterface = i;
		// Load model from the jar
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

		// if you want to use LanguageModelPath disable the 3 lines after which
		// are setting a custom grammar->

		// configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin")

		// Grammar
		configuration.setGrammarPath("resource:/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			Log.immediateMsg("Speech Handler >> ERROR: "+ ex.getMessage());
		}

		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);

		// Start the Thread
		startSpeechThread();
		startResourcesThread();
	}

	/**
	 * Starting the main Thread of speech recognition
	 */
	protected void startSpeechThread() {

		// alive?
		if (speechThread != null && speechThread.isAlive())
			return;

		// initialise
		speechThread = new Thread(() -> {
			Log.println("speech recognition active", true);
			//logger.log(Level.INFO, "speech recognition active");
			try {
				while (true) {
					/*
					 * This method will return when the end of speech is
					 * reached. Note that the end pointer will determine the end
					 * of speech.
					 */
					SpeechResult speechResult = recognizer.getResult();
					if (speechResult != null) {

						result = speechResult.getHypothesis();
						Log.println("> " + result, true);
						mInterface.voiceInput(result);
						// logger.log(Level.INFO, "You said: " + result + "\n")

					} else
						Log.println("I can't understand what you said.", true);
						//logger.log(Level.INFO, "I can't understand what you said.\n");

				}
			} catch (Exception ex) {
				Log.immediateMsg("Speech Handler >> WARNING: " + ex.getMessage());
				//logger.log(Level.WARNING, null, ex);
			}

			Log.println("SpeechThread has exited", true);
			//logger.log(Level.INFO, "SpeechThread has exited...");
		});

		// Start
		speechThread.start();

	}

	/**
	 * Starting a Thread that checks if the resources needed to the
	 * SpeechRecognition library are available
	 */
	protected void startResourcesThread() {

		// alive?
		if (resourcesThread != null && resourcesThread.isAlive())
			return;

		resourcesThread = new Thread(() -> {
			try {

				// Detect if the microphone is available
				while (true) {
					if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
						// logger.log(Level.INFO, "Microphone is available.\n")
					} else {
						// logger.log(Level.INFO, "Microphone is not
						// available.\n")

					}

					// Sleep some period
					Thread.sleep(500);
				}

			} catch (InterruptedException ex) {
				Log.immediateMsg("Speech Handler >> WARNING: " + ex.getMessage());
				//logger.log(Level.WARNING, null, ex);
				resourcesThread.interrupt();
			}
		});

		// Start
		resourcesThread.start();
	}

	/**
	 * Takes a decision based on the given result
	 */
	public void makeDesicion(String result) {
		//implemented in the part 2
	}


}