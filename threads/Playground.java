package threads;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Playground extends JFrame implements ActionListener
{
    private static final String ADD_BALL_COMMAND = "add_ball";
    private static final String CHOOSE_OPTION_COMMAND = "choose_option";
    private PlayPanel panel;
    private JButton newBallButton;
    
    public Playground() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Playground");
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        
        panel = new PlayPanel();
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(panel, constraints);
        
        
        newBallButton = new JButton("Add ball");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        getContentPane().add(newBallButton, constraints);
        newBallButton.setActionCommand(ADD_BALL_COMMAND);
        newBallButton.setPreferredSize( new Dimension(200, 30));
        newBallButton.addActionListener(this);
        
        PlayOption[] options = PlayOption.values();
        
        //indeksy od 0 - 5 oznaczaja poszczegolne warianty
        JComboBox optionList = new JComboBox(options);
        optionList.setSelectedIndex(0);
        optionList.setActionCommand(CHOOSE_OPTION_COMMAND);
        optionList.addActionListener(this);
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 2;
        getContentPane().add(optionList, constraints);
        
        JLabel optionListLabel = new JLabel("Wybierz wariant:");
        optionListLabel.setLabelFor(optionList);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 2;
        getContentPane().add(optionListLabel, constraints);
        
        pack();
        setSize(700, 700);
        setVisible(true);
        
        Box box = new Box(panel);
        panel.setBox(box);
        box.start();
        
        //graphics.fillOval(100, 100, 100, 100);
        
    }
    
    public static void main(String[] args) {
     
        new Playground();
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if(ADD_BALL_COMMAND.equals(e.getActionCommand()) ) {
            Ball b = new Ball(panel);
            panel.addBall(b);
            b.start();
        } else if(CHOOSE_OPTION_COMMAND.equals(e.getActionCommand()) ) {
            //wybór opcji działania programu
            JComboBox optionList = (JComboBox) e.getSource();
            PlayOption option = (PlayOption) optionList.getSelectedItem();
            panel.setPlayOption(option);
        }
    }

}
