package jparanoia.shared;
import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import static java.lang.System.exit;
import java.lang.invoke.MethodHandles;
import static java.lang.invoke.MethodHandles.lookup;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.FocusManager;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.ToolTipManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class JParanoia {
    private final static Logger logger = getLogger( MethodHandles.lookup().lookupClass());

    public static final String JPARANOIA_WEBSITE = "http://www.byronbarry.com/jparanoia/";
    public static final String WHITE_ON_BLACK = "White on Black";
    public static final String BLACK_ON_WHITE = "Black on White";
    public static JFrame frame = new JFrame();
    public static Prefs prefs = new Prefs();
    public static JPSounds soundPlayer;
    public static SoundMenu soundMenu;
    public static boolean soundIsOn;
    public static boolean combatMusicIsPlaying;
    public static boolean logBroken = false;
    public static boolean autoScroll = true;
    public static boolean announceObservers = true;
    public static int previousKey = 0;
    public static int thisKey = 100;
    public static String lastNameCompleted = "";
    public static int lastCompletionPlayer = 99;
    public static ArrayList sortedNames = new ArrayList<>( 8 );
    public static Vector obsNames = new Vector( 40 );
    public static GameLogger log;
    public static ErrorLogger errLog;
    public static JMenuItem aboutBoxMenuItem;
    public static StringTokenizer st;
    public static Color textColor = Color.white;
    public static JTextPane displayArea;
    public static SimpleAttributeSet textAttributes;
    public static StyledDocument chatDocument;
    public static ObserversFrame obsFrame = new ObserversFrame();
    public static ToolTipManager ttm = ToolTipManager.sharedInstance();
    protected static URL aboutIconURL;
    protected static ImageIcon aboutIcon;
    protected static String appInfo;
    static NoTabFocusManager ntfm = new NoTabFocusManager();

    public JParanoia() {
        if ( (Boolean) prefs.getPref( 0 ) ) {
            soundPlayer = new JPSounds();
            soundIsOn = true;
        }
        soundMenu = new SoundMenu( "Sounds", prefs, this );
        if ( soundIsOn && soundMenu.startupMenuItem.isSelected() ) {
            soundPlayer.play( 0 );
        }
        try {
            aboutIconURL = new URL( "http://www.byronbarry.com/jparanoia/aboutIcon.jpg" );
        } catch ( Exception localException ) {
            localException.printStackTrace();
        }
        aboutIcon = new ImageIcon( aboutIconURL );
        aboutBoxMenuItem = new JMenuItem( "About JParanoia..." );
        aboutBoxMenuItem.addActionListener( paramAnonymousActionEvent -> {
            String str = "\n\nCoded by Byron Barry\nIcons by Andy Fitzpatrick\n\nJParanoia is Freeware\n(Donations accepted.)\nPermission required to re-distribute\nConsult included LICENSE.TXT for details\n\nThe JParanoia website is\nhttp://www.byronbarry.com/jparanoia/\n\nSearch for JParanoia on http://dmoz.org/\nif the above URL is no longer in use.";
            new JOptionPane();
            JOptionPane.showMessageDialog( null, JParanoia.appInfo +
                    "\n© 2002-2004 Byron Barry" +
                    str, "About JParanoia", JOptionPane.INFORMATION_MESSAGE, JParanoia.aboutIcon );
        } );
        FocusManager.setCurrentManager( ntfm );
        ttm.setDismissDelay( 100000000 );
    }

    static void toggleSoundEngine() {
        if ( soundPlayer != null ) {
            soundPlayer.close();
            soundPlayer = null;
            soundIsOn = false;
            combatMusicIsPlaying = false;
        } else {
            try {
                soundPlayer = new JPSounds();
                soundIsOn = true;
            } catch ( NoClassDefFoundError localNoClassDefFoundError ) {
                errLog = new ErrorLogger( "cmbt", localNoClassDefFoundError.toString() + " in JPServer.startCombat()" );
                localNoClassDefFoundError.printStackTrace( errLog.out );
                errLog.closeLog();
                errLog = null;
                errorMessage( "CombatFrame - class definition not found", "The CombatFrame class failed to load. The combat manager\nis not available. An error log has been created in your logs\ndirectory. Please notify the author. You will need to exit\nand relaunch the server to correct this problem." );
            }
        }
    }

    public static void errorMessage( String paramString1, String paramString2 ) {
        JOptionPane.showMessageDialog( frame, paramString2, paramString1, JOptionPane.ERROR_MESSAGE );
    }

    public static void displayImage( String paramString ) {
        try {
            String str = paramString.substring( 0, paramString.indexOf( "http://" ) );
            URL localURL = new URL( paramString.substring( paramString.indexOf( "http://" ) ) );
            JTextPane localJTextPane = new JTextPane();
            localJTextPane.setEnabled( false );
            JFrame localJFrame = new JFrame();
            localJFrame.setResizable( false );
            localJFrame.setSize( 200, 200 );
            ImageIcon localImageIcon = new ImageIcon( localURL );
            localJTextPane.insertIcon( localImageIcon );
            localJFrame.setSize( localImageIcon.getIconWidth() + 8 + 6, localImageIcon.getIconHeight() + 26 + 6 );
            Container localContainer = localJFrame.getContentPane();
            localContainer.add( localJTextPane );
            localJFrame.setTitle( str );
            localJTextPane.setToolTipText( str );
            try {
                localJFrame.setIconImage( Toolkit.getDefaultToolkit()
                        .getImage( lookup().lookupClass()
                                .getClassLoader()
                                .getResource( "graphics/jparanoiaIcon.jpg" ) ) );
            } catch ( Exception localException2 ) {
                logger.info( "Error getting icon." );
                localException2.printStackTrace();
                exit( -1 );
            }
            localJFrame.setVisible( true );
        } catch ( Exception localException1 ) {
            errLog = new ErrorLogger( "img", localException1.toString() + " in JParanoia.displayImage()" );
            localException1.printStackTrace( errLog.out );
            errLog.closeLog();
            errLog = null;
            errorMessage( "Error displaying image", "There was an error displaying the image.\nAn error log has been created in the logs\ndirectory." );
        }
    }

    public static void warningMessage( String paramString1, String paramString2 ) {
        JOptionPane.showMessageDialog( frame, paramString2, paramString1, JOptionPane.WARNING_MESSAGE );
    }

    public static void displayWrite( Color paramColor, String paramString ) {
        try {
            textAttributes.addAttribute( StyleConstants.CharacterConstants.Foreground, paramColor );
            chatDocument.insertString( chatDocument.getLength(), paramString, textAttributes );
            displayArea.setDocument( chatDocument );
            if ( autoScroll ) {
                displayArea.setCaretPosition( chatDocument.getLength() );
            }
        } catch ( BadLocationException localBadLocationException ) {
            System.err.println( "Unhandled exception. (Bad Location)" );
        }
        textAttributes.addAttribute( StyleConstants.CharacterConstants.Foreground, textColor );
    }

    public static void addObsName( String paramString ) {
        Vector localVector = new Vector( 1 );
        localVector.add( paramString );
        obsNames.add( localVector );
        obsFrame.jt.repaint();
    }

    public static void removeObsName( String paramString ) {
        for ( int i = 0; i < obsNames.size(); i++ ) {
            Vector localVector = (Vector) obsNames.elementAt( i );
            String str = (String) localVector.elementAt( 0 );
            if ( str.equals( paramString ) ) {
                obsNames.remove( i );
                obsFrame.jt.repaint();
                return;
            }
        }
    }

    public static void observerHasJoined( String paramString ) {
        logger.info( "WRONG METHOD CALLED. (joined)" );
    }

    public static void observerHasLeft( String paramString ) {
        logger.info( "WRONG METHOD CALLED. (disconnected)" );
    }
}


/* Location:              C:\Users\noahc\Desktop\JParanoia(1.31.1)\JParanoia(1.31.1).jar!\jparanoia\shared\JParanoia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
