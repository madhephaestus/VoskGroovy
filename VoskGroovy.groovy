@Grab(group='net.java.dev.jna', module='jna', version='5.7.0')
@Grab(group='com.alphacephei', module='vosk', version='0.3.45')
@Grab(group='com.alphacephei', module='vosk-model-en', version='0.3.45')



import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer;
import org.vosk.LibVosk;
import org.vosk.Model;

println "Starting Vosk"

LibVosk.setLogLevel(LogLevel.DEBUG);

Model model = new Model("model");
InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("../../python/example/test.wav")));
Recognizer recognizer = new Recognizer(model, 16000)

int nbytes;
byte[] b = new byte[4096];
while ((nbytes = ais.read(b)) >= 0) {
	if (recognizer.acceptWaveForm(b, nbytes)) {
		System.out.println(recognizer.getResult());
	} else {
		System.out.println(recognizer.getPartialResult());
	}
}

System.out.println(recognizer.getFinalResult());