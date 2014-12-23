package ultimate.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;
import ultimate.pong.net.Client;
import ultimate.pong.net.Handler;
import ultimate.pong.net.SocketClient;
import ultimate.pong.ui.Blinker;
import ultimate.pong.ui.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class PongClient extends JFrame implements WindowListener, ActionListener, Handler
{
	protected static transient final Logger	logger				= LoggerFactory.getLogger(PongClient.class);

	private static final long				serialVersionUID	= 1L;
	private static PongClient				client;

	public static final Dimension			MIN_SIZE			= new Dimension(800, 600);
	public static final int					INSET				= 5;
	public static final int					COLUMNS				= 10;
	public static final String				DELIM				= "\n\n";
	public static final int					HIGHLIGHT_INTERVAL	= 1000;
	public static final Color				HIGHLIGHT_COLOR		= new Color(255, 127, 127);

	private ObjectMapper					mapper				= new ObjectMapper();
	private ObjectWriter					writer				= mapper.writer();
	private ObjectReader					reader				= mapper.reader().withType(Match.class);

	private Client							socketClient;
	private Match							match;
	private Player							player;

	private int								continuousErrors	= 0;

	private View							view;

	private JPanel							connectionPanel;
	private JLabel							hostLabel;
	private JTextField						hostTF;
	private JLabel							portLabel;
	private JTextField						portTF;
	private JButton							connectButton;
	private JButton							disconnectButton;

	private JPanel							matchPanel;

	private JPanel							playerPanel;
	private JLabel							nameLabel;
	private JTextField						nameTF;
	private JLabel							colorLabel;
	private JPanel							colorValuePanel;
	private JButton							colorButton;
	private JColorChooser					colorCC;
	private JLabel							readyLabel;
	private JCheckBox						readyCB;
	private JButton							participateButton;

	public PongClient()
	{
		super("Pong");

		// configure window
		this.getContentPane().setLayout(new GridBagLayout());
		this.setMinimumSize(MIN_SIZE);
		this.addWindowListener(this);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(INSET, INSET, INSET, INSET);

		this.view = new View();

		// setup connectionPanel
		this.connectionPanel = new JPanel();
		this.connectionPanel.setLayout(new GridBagLayout());
		this.connectionPanel.setBorder(new EtchedBorder());
		// create components
		this.hostLabel = new JLabel("Host:");
		this.hostTF = new JTextField(COLUMNS);
		this.portLabel = new JLabel("Port:");
		this.portTF = new JTextField(COLUMNS);
		this.connectButton = new JButton("Connect");
		this.connectButton.addActionListener(this);
		this.disconnectButton = new JButton("Disconnect");
		this.disconnectButton.addActionListener(this);
		// add components
		// @formatter:off
		gbc.gridx = 0;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(hostLabel, gbc);
		gbc.gridx = 1;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(hostTF, gbc);
		gbc.gridx = 0;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(portLabel, gbc);
		gbc.gridx = 1;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(portTF, gbc);
		gbc.gridx = 0;	gbc.gridy = 2; 	gbc.gridwidth = 2; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(connectButton, gbc);
		gbc.gridx = 0;	gbc.gridy = 3; 	gbc.gridwidth = 2; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.connectionPanel.add(disconnectButton, gbc);
		// @formatter:on

		this.matchPanel = new JPanel();
		this.matchPanel.setLayout(new GridBagLayout());
		this.matchPanel.setBorder(new EtchedBorder());

		// setup playerPanel
		this.playerPanel = new JPanel();
		this.playerPanel.setLayout(new GridBagLayout());
		this.playerPanel.setBorder(new EtchedBorder());
		// create components
		this.nameLabel = new JLabel("Name:");
		this.nameTF = new JTextField(COLUMNS);
		this.colorLabel = new JLabel("Color:");
		this.colorValuePanel = new JPanel();
		this.colorValuePanel.setBorder(new EtchedBorder());
		this.colorButton = new JButton("Select...");
		this.colorButton.addActionListener(this);
		this.colorCC = new JColorChooser();
		this.readyLabel = new JLabel("Ready:");
		this.readyCB = new JCheckBox();
		this.participateButton = new JButton("Participate");
		this.participateButton.addActionListener(this);
		// add components
		// @formatter:off
		gbc.gridx = 0;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(nameLabel, gbc);
		gbc.gridx = 1;	gbc.gridy = 0; 	gbc.gridwidth = 2; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(nameTF, gbc);
		gbc.gridx = 0;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(colorLabel, gbc);
		gbc.gridx = 1;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(colorValuePanel, gbc);
		gbc.gridx = 2;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 1;
		this.playerPanel.add(colorButton, gbc);
		gbc.gridx = 0;	gbc.gridy = 2; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(readyLabel, gbc);
		gbc.gridx = 1;	gbc.gridy = 2; 	gbc.gridwidth = 2; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(readyCB, gbc);
		gbc.gridx = 0;	gbc.gridy = 3; 	gbc.gridwidth = 3; 	gbc.gridheight = 1;	gbc.weightx = 1;	gbc.weighty = 1;
		this.playerPanel.add(participateButton, gbc);
		// @formatter:on

		// @formatter:off
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 3;	gbc.weightx = 1;	gbc.weighty = 1;
		this.getContentPane().add(view, gbc);
		gbc.gridx = 1;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 0;
		this.getContentPane().add(connectionPanel, gbc);
		gbc.gridx = 1;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 1;
		this.getContentPane().add(matchPanel, gbc);
		gbc.gridx = 1;	gbc.gridy = 2; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 0;
		this.getContentPane().add(playerPanel, gbc);
		// @formatter:on

		// select some random values
		Random r = new Random();
		this.nameTF.setText("unnamed" + r.nextInt(10000));
		this.colorCC.setColor(new Color(Color.HSBtoRGB(r.nextFloat(), 1.0F, 1.0F)));
		this.colorValuePanel.setBackground(this.colorCC.getColor());
		this.hostTF.setText("localhost");
		this.portTF.setText("55555");

		// update inputs (enable/disable)
		this.updateInputs();
	}

	protected void updateInputs()
	{
		boolean enableConnectionInput = (this.socketClient == null);
		this.hostTF.setEnabled(enableConnectionInput);
		this.portTF.setEnabled(enableConnectionInput);
		this.connectButton.setEnabled(enableConnectionInput);
		this.disconnectButton.setEnabled(!enableConnectionInput);

		boolean enablePlayerInput = (this.socketClient != null && this.player == null && this.match != null && this.match.getState() == EnumMatchState.waiting);
		this.nameTF.setEnabled(enablePlayerInput);
		this.colorButton.setEnabled(enablePlayerInput);
		this.readyCB.setEnabled(enablePlayerInput);
		this.participateButton.setEnabled(enablePlayerInput);
	}

	@Override
	public void setup() throws IOException
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMessage(String message) throws IOException
	{
		try
		{
			this.match = reader.readValue(message);

			// logger.debug("message received: map=" + this.match.getMap());

			this.view.update(this.match.getMap());

			updateInputs();
			
			continuousErrors = 0;
		}
		catch(Exception e)
		{
			// catch exception to prevent thread from dying
			logger.warn("error handling message", e);
			continuousErrors++;
			if(continuousErrors > 10)
			{
				disconnect();
			}
		}
	}

	@Override
	public void teardown() throws IOException
	{
		disconnect(); // just in case we got here due to an error
	}

	public void connect()
	{
		String host = this.hostTF.getText();
		if(host == null || host.isEmpty())
		{
			blink(this.hostTF);

			return;
		}
		int port;
		try
		{
			port = Integer.parseInt(this.portTF.getText());
		}
		catch(NumberFormatException ex)
		{
			blink(this.portTF);
			return;
		}
		if(port < 0 || port > 65535)
		{
			blink(this.portTF);
			return;
		}

		logger.info("connecting to " + host + ":" + port);

		try
		{
			Socket socket = new Socket(host, port);
			this.socketClient = new SocketClient(DELIM, socket);
		}
		catch(IOException ex)
		{
			logger.error("error connecting to host", ex);
			blink(this.hostTF);
			blink(this.portTF);
			return;
		}

		this.socketClient.listen(this);

		updateInputs();
	}

	public void disconnect()
	{
		try
		{
			if(this.socketClient != null)
			{
				this.socketClient.disconnect();
				this.socketClient = null;
			}
		}
		catch(IOException e)
		{
			logger.error("error disconnecting from host", e);
		}

		updateInputs();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == this.connectButton)
		{
			logger.debug("connect");
			connect();
		}
		else if(e.getSource() == this.disconnectButton)
		{
			logger.debug("disconnect");
			disconnect();
		}
		else if(e.getSource() == this.participateButton)
		{
			logger.debug("participate");
			updateInputs();
		}
		else if(e.getSource() == this.colorButton)
		{
			logger.debug("color");
			int result = JOptionPane.showOptionDialog(this, this.colorCC, "Choose your color...", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			logger.debug("result=" + result);
			if(result == JOptionPane.OK_OPTION)
				this.colorValuePanel.setBackground(this.colorCC.getColor());
		}

	}

	protected void blink(JComponent component)
	{
		new Blinker(component, 2, HIGHLIGHT_COLOR, HIGHLIGHT_INTERVAL).start();
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		exit();
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	public static void main(String[] args)
	{
		logger.info("creating client");

		client = new PongClient();
		client.setLocationRelativeTo(null);
		client.setVisible(true);
		client.requestFocus();
	}

	public static void exit()
	{
		logger.info("exiting client");

		if(client != null)
		{
			client.disconnect();
			client.setVisible(false);
			client.dispose();
		}

		System.exit(0);
	}
}
