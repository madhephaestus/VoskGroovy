@Grab(group='net.java.dev.jna', module='jna', version='5.7.0')
@Grab(group='com.alphacephei', module='vosk', version='0.3.45')

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.TargetDataLine
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer;

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

import org.vosk.LibVosk;
import org.vosk.Model;

println "Starting Vosk"

LibVosk.setLogLevel(LogLevel.DEBUG);

AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
TargetDataLine microphone;
SourceDataLine speakers;
// model downloade from https://alphacephei.com/vosk/models
Model model = new Model(ScriptingEngine.getWorkspace().getAbsolutePath()+"/vosk-model-en-us-0.22/");
Recognizer recognizer = new Recognizer(model, 120000)


microphone = (TargetDataLine) AudioSystem.getLine(info);
microphone.open(format);
microphone.start();

ByteArrayOutputStream out = new ByteArrayOutputStream();
int numBytesRead;
int CHUNK_SIZE = 1024;
int bytesRead = 0;

DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
speakers.open(format);
speakers.start();
byte[] b = new byte[4096];
try{
while (bytesRead <= 100000000 && !Thread.interrupted()) {
	Thread.sleep(0,1);
	numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
	bytesRead += numBytesRead;

	out.write(b, 0, numBytesRead);

	speakers.write(b, 0, numBytesRead);

	if (recognizer.acceptWaveForm(b, numBytesRead)) {
		System.out.println(recognizer.getResult());
	} else {
		System.out.println(recognizer.getPartialResult());
	}
}
}catch(Throwable t){
	t.printStackTrace()
}
System.out.println(recognizer.getFinalResult());
speakers.drain();
speakers.close();
microphone.close();


