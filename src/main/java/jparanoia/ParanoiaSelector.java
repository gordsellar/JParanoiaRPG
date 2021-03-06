package jparanoia;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.System.exit;
import java.lang.invoke.MethodHandles;
import static java.lang.invoke.MethodHandles.lookup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jparanoia.client.JPClient;
import jparanoia.server.JPServer;
import static jparanoia.server.JPServer.getVersionName;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class ParanoiaSelector {
    static {
        System.setProperty( "swing.metalTheme", "steel" );
    }

    private final static Logger logger = getLogger( MethodHandles.lookup().lookupClass());

    static JFrame frame = new JFrame( "Launch JParanoia" );
    static JButton serverButton = new JButton( "Server " + JPServer.getVersionName() );
    static JButton clientButton = new JButton( "Client " + JPClient.getVersionName() );
    static Container contentPane = frame.getContentPane();

    public static void main( String[] paramArrayOfString ) {
        logger.info( "JPSERVER VERSION: " + getVersionName() );
        logger.info( "JPCLIENT VERSION: " + JPClient.getVersionName() );
        contentPane.setLayout( null );
        Dimension localDimension = getDefaultToolkit().getScreenSize();
        int i = (int) localDimension.getWidth();
        int j = (int) localDimension.getHeight();
        frame.setSize( 610, 360 );
        frame.setLocation( i / 2 - frame.getWidth() / 2, j / 2 - frame.getHeight() / 2 );
        ImageIcon localImageIcon = null;
        try {
            localImageIcon = new ImageIcon( getDefaultToolkit()
                    .getImage( lookup().lookupClass().getClassLoader().getResource( "graphics/jpsplash.jpg" ) ) );
        } catch ( Exception localException1 ) {
            localException1.printStackTrace();
            exit( -1 );
        }
        JLabel localJLabel = new JLabel( localImageIcon );
        JPanel localJPanel = new JPanel();
        localJPanel.setLayout( new GridLayout( 1, 2, 4, 4 ) );
        localJPanel.add( serverButton );
        localJPanel.add( clientButton );
        serverButton.addActionListener( paramAnonymousActionEvent -> {
            frame.dispose();
            JPServer.main( null );
        } );
        clientButton.addActionListener( paramAnonymousActionEvent -> {
            frame.dispose();
            JPClient.main( null );
        } );
        contentPane.add( localJLabel );
        contentPane.add( localJPanel );
        Insets localInsets = contentPane.getInsets();
        localJLabel.setBounds( 0 + localInsets.left, 0 + localInsets.top, 600, 295 );
        localJPanel.setBounds( 0 + localInsets.left, 298 + localInsets.top, 600, 35 );
        try {
            frame.setIconImage( getDefaultToolkit()
                    .getImage( lookup().lookupClass().getClassLoader().getResource( "graphics/jparanoiaIcon.jpg" ) ) );
        } catch ( Exception localException2 ) {
            localException2.printStackTrace();
            exit( -1 );
        }
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent paramAnonymousWindowEvent ) {
                exit( 0 );
            }
        } );
        frame.setResizable( false );
        frame.setVisible( true );
    }
}


/* Location:              C:\Users\noahc\Desktop\JParanoia(1.31.1)\JParanoia(1.31.1).jar!\jparanoia\ParanoiaSelector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
