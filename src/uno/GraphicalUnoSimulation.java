
package uno;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphicalUnoSimulation extends JFrame {

    public static int WINDOW_WIDTH=1150;
    public static final int WINDOW_HEIGHT=750;

    public static final String PLAYER_FILENAME = "players.txt";

    private ArrayList<String> playerNames;
    private ArrayList<String> playerClasses;

    public static void main(String args[]) {
        int numGames = 0;
        if (args.length != 1  &&  args.length != 2) {
            System.out.println("Usage: UnoSimulation numberOfGames.");
            System.exit(1);
        }
        numGames = Integer.valueOf(args[0]);
        try {
            new GraphicalUnoSimulation(numGames);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<JProgressBar> scoreProgressBars;
    private JProgressBar numGamesProgressBar;
    private ArrayList<JLabel> playerLabels;
    private ArrayList<JLabel> scoreLabels;

    private int numGames;

    private void loadPlayerData() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(
            PLAYER_FILENAME));
        String playerLine = br.readLine();
        while (playerLine != null) {
            Scanner line = new Scanner(playerLine).useDelimiter(",");
            playerNames.add(line.next());
            playerClasses.add("uno." + line.next() + "_UnoPlayer");
            playerLine = br.readLine();
        }
    }

    private GraphicalUnoSimulation(int numGames) throws Exception {

        playerNames = new ArrayList<String>();
        playerClasses = new ArrayList<String>();

        loadPlayerData();

        this.numGames = numGames;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the score progress bars.
        scoreProgressBars = new ArrayList<JProgressBar>();
        for (int i=0; i<playerClasses.size(); i++) {
            JProgressBar bar = new JProgressBar(
                SwingConstants.VERTICAL, 0, numGames * 7 * playerNames.size());
            bar.setBackground(new Color(230,230,230));
            bar.setBorderPainted(false);
            scoreProgressBars.add(bar);
        }

        // Create the number of games progress bar.
        numGamesProgressBar = new JProgressBar(0,numGames);
        numGamesProgressBar.setStringPainted(true);
        numGamesProgressBar.setString("0 games");
        numGamesProgressBar.setForeground(new Color(139,0,0));

        // Create the player and score labels.
        playerLabels = new ArrayList<JLabel>();
        scoreLabels = new ArrayList<JLabel>();
        for (int i=0; i<playerClasses.size(); i++) {
            playerLabels.add(new JLabel(playerNames.get(i),
                SwingConstants.CENTER));
            scoreLabels.add(new JLabel("0", SwingConstants.CENTER));
            playerLabels.get(i).setFont(new Font("Planet Benson 2",Font.PLAIN, 26));
            scoreLabels.get(i).setFont(new Font("Monospace",Font.PLAIN, 16));
        }
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);

        // Add some titlish text to the JFrame.
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20,20,20,20); // x
        c.gridwidth = playerNames.size();
        c.weighty = 1.0;
        JLabel title = new JLabel("Stephen's Smokin' UNO Simulator",
            SwingConstants.CENTER);
        title.setFont(new Font("Living by Numbers",Font.PLAIN, 55));
        title.setForeground(new Color(139,0,0));
        gridbag.setConstraints(title, c);
        add(title);

        // Actually add the number of games progress bar (and label) to
        //   the JFrame.
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20,80,20,80); // x
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = playerNames.size() - 1;
        gridbag.setConstraints(numGamesProgressBar, c);
        add(numGamesProgressBar);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0,0,0,0);
        ImageIcon card =
            new ImageIcon(ImageIO.read(new File("draw4.jpg")).
            getScaledInstance(80,115,0));
        JButton cardButton = new JButton(card);
        Insets insets = cardButton.getInsets();
        insets.top=4;
        insets.bottom=4;
        insets.left=4;
        insets.right=4;
        cardButton.setMargin(insets);
        c.fill = GridBagConstraints.NONE;
        cardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        simulate();
                    }
                }).start();
            }
        });
        gridbag.setConstraints(cardButton, c);
        add(cardButton);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        
        c.gridy=2;
        // Actually add the score progress bars (and labels) to the JFrame.
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20,90,20,90);
        c.gridwidth = 1;
        c.weighty = 10.0;
        c.ipady=400;
        for (int i=0; i<scoreProgressBars.size(); i++) {
            gridbag.setConstraints(scoreProgressBars.get(i), c);
            add(scoreProgressBars.get(i));
        }
        c.insets = new Insets(20,10,20,10);
        c.ipady=0;
        c.gridy=3;
        c.weighty = 0.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        for (int i=0; i<playerLabels.size(); i++) {
            gridbag.setConstraints(playerLabels.get(i), c);
            add(playerLabels.get(i));
        }
        c.gridy=4;
        c.weighty = 0.0;
        c.gridwidth = 1;
        for (int i=0; i<scoreLabels.size(); i++) {
            gridbag.setConstraints(scoreLabels.get(i), c);
            add(scoreLabels.get(i));
        }


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);

        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(230,230,230));
    }

    private void simulate() {
        UnoSimulation.PRINT_VERBOSE = false;
        
        for (int j=0; j<playerClasses.size(); j++) {
            playerLabels.get(j).setForeground(Color.BLACK);
            scoreProgressBars.get(j).setForeground(Color.BLUE);
        }
        try {
            Scoreboard s = new Scoreboard(playerNames.toArray(new String[0]));
            for (int i=1; i<=numGames; i++) {
                Game g = new Game(s,playerClasses);
                g.play();
                numGamesProgressBar.setString(i + " games");
                numGamesProgressBar.setValue(i);
                for (int j=0; j<playerClasses.size(); j++) {
                    scoreProgressBars.get(j).setValue(s.getScore(j));
                    scoreLabels.get(j).setText(""+s.getScore(j));
                }
            }
            playerLabels.get(s.getWinner()).setForeground(Color.RED);
            scoreProgressBars.get(s.getWinner()).setForeground(Color.RED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
