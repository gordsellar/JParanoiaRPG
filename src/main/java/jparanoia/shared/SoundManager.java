package jparanoia.shared;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.lang.invoke.MethodHandles;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioSystem.getMixer;
import static javax.sound.sampled.AudioSystem.getMixerInfo;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import static javax.sound.sampled.Mixer.Info;
import static jparanoia.shared.JParanoia.errorMessage;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class SoundManager {
    private final static Logger logger = getLogger( MethodHandles.lookup().lookupClass());

    static boolean stopLoop = false;
    String fileString;
    int mixerToUse = 0;
    AudioFormat format;
    DataLine.Info info;
    AudioInputStream[] audioStreams;
    Clip[] clipList;
    SoundPlayer player;
    SoundLooper looper;

    public SoundManager( String[] paramArrayOfFile ) {
        if ( paramArrayOfFile.length > 32 ) {
            logger.info( "WARNING: Preparing to attempt acquisition of more than 32 voices!" );
        }
        try {
            this.clipList = new Clip[paramArrayOfFile.length];
            this.audioStreams = new AudioInputStream[paramArrayOfFile.length];
            //looks like sound system init
            Info[] arrayOfInfo = getMixerInfo();
            if ( arrayOfInfo == null || arrayOfInfo.length == 0 ) {
                errorMessage( "No soundcard detected", "Soundcard init failed, you won't get any sounds!" );
                return;
            }
            Info info = arrayOfInfo[0];
            logger.info( info.toString() );
            Mixer localMixer = getMixer( info );
            //some archaic sound loading
            for ( int j = 0; j < paramArrayOfFile.length; j++ ) {
                this.fileString = paramArrayOfFile[j];
                try {
                    InputStream is = getClass().getResourceAsStream( "/" + paramArrayOfFile[j] );
                    this.audioStreams[j] = getAudioInputStream( new BufferedInputStream( is ) );
                } catch ( FileNotFoundException localFileNotFoundException ) {
                    errorMessage( "Sound not found", "JParanoia was unable to locate:\n" +
                            paramArrayOfFile[j] +
                            "\n\n" +
                            "Sound files should not be renamed or moved.\n\n" +
                            "If you have downloaded a JParanoia zip archive\n" +
                            "that did not include a sounds directory, you will\n" +
                            "need to copy one over from a previous installation\n" +
                            "or acquire one from the JParanoia download page or\n" +
                            "a fellow player.\n\n" +
                            "Alternately, you can play without sounds\n" +
                            "by setting bPlaySounds to false in the\n" +
                            "jpConfig.ini file and prevent this error\n" +
                            "from appearing again.\n\n" +
                            "JParanoia will now terminate." );
                    exit( 0 );
                }
                this.format = this.audioStreams[j].getFormat();
                this.info = new DataLine.Info( Clip.class, this.format );
                this.clipList[j] = (Clip) localMixer.getLine( this.info );
                this.clipList[j].open( this.audioStreams[j] );
            }
            logger.info( "SoundManager finished acquiring resources for audio playback." );
        } catch ( Exception localException ) {
            localException.printStackTrace();
        }
    }

    public static void stopLoop( boolean paramBoolean ) {
        stopLoop = paramBoolean;
    }

    public void play( int paramInt ) {
        this.player = new SoundPlayer( this.clipList[paramInt], this.audioStreams[paramInt] );
        this.player.start();
    }

    public void loopPlay( int paramInt ) {
        this.looper = new SoundLooper( this.clipList[paramInt], this.audioStreams[paramInt] );
        this.looper.start();
    }

    public void terminate() {
        for ( final Clip aClipList : this.clipList ) {
            aClipList.close();
        }
        logger.info( "Sound engine terminated." );
    }
}


/* Location:              C:\Users\noahc\Desktop\JParanoia(1.31.1)\JParanoia(1.31.1).jar!\jparanoia\shared\SoundManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
