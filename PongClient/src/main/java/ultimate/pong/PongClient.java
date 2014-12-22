package ultimate.pong;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ultimate.pong.data.model.Match;
import ultimate.pong.graphics.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class PongClient extends JFrame
{
	private static final long	serialVersionUID	= 1L;

	private ObjectMapper		mapper				= new ObjectMapper();
	private ObjectWriter		writer				= mapper.writer();
	private ObjectReader		reader				= mapper.reader().withType(Match.class);

	private View				view;
	private JPanel				connectionPanel;
	private JPanel				matchPanel;
	private JPanel				playerPanel;

	public PongClient()
	{
		this.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		this.view = new View();
		this.connectionPanel = new JPanel();
		this.matchPanel = new JPanel();
		this.playerPanel = new JPanel();
		
		this.connectionPanel.setBackground(Color.red);
		this.matchPanel.setBackground(Color.green);
		this.playerPanel.setBackground(Color.blue);

		gbc.fill = GridBagConstraints.BOTH;
		// @formatter:off
		gbc.gridx = 0;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 3;	gbc.weightx = 1;	gbc.weighty = 1;
		this.getContentPane().add(view, gbc);
		gbc.gridx = 1;	gbc.gridy = 0; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 0;
		this.getContentPane().add(connectionPanel, gbc);
		gbc.gridx = 1;	gbc.gridy = 1; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 1;
		this.getContentPane().add(matchPanel, gbc);
		gbc.gridx = 1;	gbc.gridy = 2; 	gbc.gridwidth = 1; 	gbc.gridheight = 1;	gbc.weightx = 0;	gbc.weighty = 0;
		this.getContentPane().add(playerPanel, gbc);
		// @formatter:on
	}

	public static void main(String[] args)
	{
		PongClient client = new PongClient();
		client.setVisible(true);
	}
}
