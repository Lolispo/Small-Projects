import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class chooseActivePlayers{

	private balancingSystem balanceS;

	private JFrame frame;
	private JPanel outerPanel;
	private JPanel panel;
	private JTextField info;
	private int selectedCounter;
	private int amountPlayers;
	private Player[] players;
	private JTextField[] text; 
	private JLabel[] name;
	private JCheckBox[] playing;
	private JPanel[] playerPanel;
	private ArrayList<Player> playersList;
//	private JCheckBox check;
//	private JCheckBox check2;	

	private ArrayList<Player> activePlayers;

	public ArrayList<Player> getActivePlayers(){
		return this.activePlayers;
	}

	public void initFrame(){
		frame = new JFrame();
		frame.setSize(600,800);
		frame.setTitle("Choose active players");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public chooseActivePlayers(ArrayList<Player> playersList, balancingSystem balanceS){
		initFrame();
		this.balanceS = balanceS;
		this.selectedCounter = 0;
		this.playersList = playersList;
		this.amountPlayers = playersList.size();
		initPanels();
		setUpPlayers(amountPlayers);
	}

	public void initPanels(){
		frame.getContentPane().removeAll();
		panel = new JPanel();
		outerPanel = new JPanel();		
		frame.add(outerPanel);
		outerPanel.add(panel);
		panel.setBackground(Color.BLACK);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		info = new JTextField();
		info.setBackground(Color.BLACK); // Bakgrund
		info.setForeground(Color.WHITE); // Text
		info.setText("Players selected: " + selectedCounter +"/10");
		panel.add(info);
		outerPanel.setBackground(Color.BLACK);
		outerPanel.setLayout(new FlowLayout()); 
		frame.revalidate();
		frame.repaint();
	}

	/**
	 * When amountOfPlayers is chosen, Name options are given.
	 */
	public void setUpPlayers(int amountOfPlayers){
		text = new JTextField[amountPlayers];
		playing = new JCheckBox[amountPlayers];
		name = new JLabel[amountPlayers];
		playerPanel = new JPanel[amountPlayers];
		for(int i = 0; i < amountPlayers; i++){
			name[i] = new JLabel("Name: ");
			name[i].setForeground(Color.WHITE);
			text[i] = new JTextField(playersList.get(i).getUserName(), 10);
			text[i].setEditable(false);
			playing[i] = new JCheckBox();
			playing[i].setBackground(Color.BLACK);
			playing[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					boolean selected = ((AbstractButton)e.getSource()).getModel().isSelected();
				    if(selected){
				        selectedCounter++;
				    }
				    else{
						selectedCounter--;	
				    }
				    info.setText("Players selected: " + selectedCounter +"/10");
				}
			});
			playerPanel[i] = new JPanel();
			playerPanel[i].setLayout(new FlowLayout());
			playerPanel[i].setBackground(Color.BLACK);
			playerPanel[i].add(playing[i]);
			playerPanel[i].add(name[i]);
			playerPanel[i].add(text[i]);
			panel.add(playerPanel[i]);
		}
		panel.add(Box.createVerticalStrut(100));
		final JButton done = new JButton("Choose these players");
		done.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int checkedCounter = 0;
				for(JCheckBox c : playing){
					if(c.isSelected()){			
						checkedCounter++;
					}
				}
				if(checkedCounter == 10){
					startButton();
					panel.remove(done);
				}
			}
		});
		panel.add(done, BorderLayout.SOUTH);
		frame.revalidate();
		frame.repaint();
	}

	/**
	 * Sets up the players with their stats if startsbutton is clicked
	 * Gives a final options to choose size of the table
	 */
	public void startButton(){
		int amount = getAmountPlayers();
		activePlayers = new ArrayList<Player>();
		for(int i = 0; i < amount; i++){
			if(playing[i].isSelected()){
				System.out.println("Player added: " + this.playersList.get(i).getUserName());
				activePlayers.add(this.playersList.get(i));
			}
		}

		//check.setFocusable(false);

		for(JTextField textF : text){
			panel.remove(textF);
		}
		for(JLabel nameF : name){
			panel.remove(nameF);
		}
		balanceS.sendActivePlayers(activePlayers);
		panel.removeAll();
		// Move to next section
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//		GameGenerator g = new GameGenerator(amountPlayers, frame, players, panel);
//		g.setRoles(check.isSelected());
		//frame.setSize(600,600);
		//frame.revalidate();
		//frame.repaint();
	}

	public int getAmountPlayers(){
		return amountPlayers;
	}
}

/*
	public void setUpDropdowns(){

		String[] numberList = {"2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
		frame.getContentPane().removeAll();

		outerPanel = new JPanel();		
		outerPanel.setLayout(new FlowLayout()); 
		frame.revalidate();
		frame.repaint();

		//JComboBox = Dropwdown list
		final JComboBox<String> dropdownList = new JComboBox<>(numberList);
		dropdownList.setSelectedIndex(0);
		//frame.getContentPane().add(dropdownList);
		final JLabel amountOfPlayers = new JLabel("Give the amount of players: ");
		amountOfPlayers.setForeground(Color.WHITE);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(amountOfPlayers);
		panel.add(dropdownList);
		JPanel extraRolesPanel = new JPanel();
		extraRolesPanel.setLayout(new FlowLayout());
		extraRolesPanel.setBackground(Color.BLACK);
		JLabel instructCheck = new JLabel("Extra Roles: ");
		instructCheck.setForeground(Color.WHITE);
		check = new JCheckBox();
		check.setBackground(Color.BLACK);
		extraRolesPanel.add(instructCheck);
		extraRolesPanel.add(check);
		panel.add(extraRolesPanel);


		JPanel fixedPlayersPanel = new JPanel();
		fixedPlayersPanel.setLayout(new FlowLayout());
		fixedPlayersPanel.setBackground(Color.BLACK);
		JLabel instructCheck2 = new JLabel("Fixed Players: ");
		instructCheck2.setForeground(Color.WHITE);
		check2 = new JCheckBox();
		check2.setBackground(Color.BLACK);
		fixedPlayersPanel.add(instructCheck2);
		fixedPlayersPanel.add(check2);
		panel.add(fixedPlayersPanel);



		JPanel uberOuterPanel = new JPanel(); // Is it neccessary?
		uberOuterPanel.setLayout(new FlowLayout());
		uberOuterPanel.setBackground(Color.BLACK);

		uberOuterPanel.add(outerPanel);
		outerPanel.add(panel);
		frame.add(uberOuterPanel);
		panel.setBackground(Color.BLACK);
		outerPanel.setBackground(Color.BLACK);

		outerPanel.add(Box.createHorizontalStrut(100));
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(1);
			}
		});
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new chooseActivePlayers(this.playersList);
			}
		});

		Dimension d = new Dimension(100,30);

		exit.setSize(d);
		exit.setMinimumSize(d);
		exit.setMaximumSize(d);
		exit.setPreferredSize(d);

		restart.setSize(d);
		restart.setMinimumSize(d);
		restart.setMaximumSize(d);
		restart.setPreferredSize(d);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

		buttonPanel.add(restart);
		buttonPanel.add(Box.createVerticalStrut(5));
		buttonPanel.add(exit);

		outerPanel.add(buttonPanel);


		frame.revalidate();
		frame.repaint();


		dropdownList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				amountPlayers = Integer.parseInt((String)cb.getSelectedItem());
				panel.remove(dropdownList);
				amountOfPlayers.setText("Type the player names");

				if(amountPlayers >= 13){
					frame.setSize(600,800);
				}
				setUpPlayers(amountOfPlayers);
			}	
		});
	}
*/