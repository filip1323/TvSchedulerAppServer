/*
 * Copyright 2014 Filip.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tvschedulerdebugserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 *
 * @author Filip
 */
public class Resources {

    /**
     *
     * @param name of soundfile
     * @return AudioInputStream of selected file
     */
    public static AudioInputStream getAudioStream(String name) {
	InputStream audioSrc = ClassLoader.getSystemResourceAsStream("resources/" + name);
	InputStream bufferedIn = new BufferedInputStream(audioSrc);
	try {
	    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
	    return audioStream;
	} catch (UnsupportedAudioFileException ex) {
	    ex.printStackTrace();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	return null;
    }

    /**
     *
     * @param name of file
     * @return stream of file
     */
    public static InputStream getStream(String name) {
	return ClassLoader.getSystemResourceAsStream("resources/" + name);
    }

    /**
     *
     * @param name of file
     * @return url to file
     */
    public static URL getUrl(String name) {
	return ClassLoader.getSystemResource("resources/" + name);
    }

    public static ImageIcon getImageIcon(String name) {
	return new ImageIcon(getUrl(name));
    }

}
