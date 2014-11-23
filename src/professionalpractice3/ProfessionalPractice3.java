package professionalpractice3;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.jsoup.Jsoup;

/**
 *@author Paul
 * 
 * This program fetches Trade Net data for a list of allowed companies.
 * A GUI allows a user to enter one of the valid symbols and then 
 * the relevant data items are fetched and displayed. The GUI uses a simple
 * form with a single button and text field.
 * 
 * Example tutorial from http://math.hws.edu/javanotes/c6/s1.html followed
 * for implementing basic GUI.
 */
public class ProfessionalPractice3 {
   
static JTextField inputField = new JTextField();
static DisplayPanel displayPanel = new DisplayPanel();
static boolean buttonPressed = false;

//list of approved symbols
static ArrayList<String> validSymbols = new ArrayList<String>(Arrays.asList("FB","AAPL",
        "GOOG", "MSFT", "CRM", "TWTR", "BABA", "SPY", "QQQ", "DIA"));

        //creates the display panel for the gui
     private static class DisplayPanel extends JPanel {
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         if(buttonPressed == true)
            {
            buttonPressed = true;
            String values[];
             try {
                 
                 String fieldText = inputField.getText();
                 if(validSymbols.contains(fieldText.toUpperCase()))
                    {
                     values = getValues(fieldText);
                     for(int i = 0; i < 10; i++)
                       g.drawString(values[i], 10, 30+i*12);   
                    }
                 else
                 {
                 g.drawString("Invalid symbol!", 10, 30);
                 g.drawString("Valid symbols are FB, AAPL, GOOG,", 10, 42);
                 g.drawString("MSFT, CRM, TWTR, BABA, SPY,", 10, 54);
                 g.drawString("QQQ, and DIA.", 10, 66);
                 }
                     
             } catch (IOException ex) {
                 Logger.getLogger(ProfessionalPractice3.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
         else
            {
            g.drawString( "Enter a symbol then press \"Search\"", 10, 30 );
            g.drawString("Valid symbols are FB, AAPL, GOOG,", 10, 42);
            g.drawString("MSFT, CRM, TWTR, BABA, SPY,", 10, 54);
            g.drawString("QQQ, and DIA.", 10, 66);
            }
      }
      
      public void paintValues(Graphics g, String[] values) {
          super.paintComponent(g);
      }
   }
   
   //this code executes on button press - a press is logged then a repaint is done
   private static class ButtonHandler implements ActionListener {
      public void actionPerformed(ActionEvent e) {
          buttonPressed = true;
          displayPanel.repaint();
      }
   }
    
   //this sets up the gui and the button press calls other relevant code
    public static void main(String[] args) {
      String[] temp = {"test", "test"};
      validSymbols.add("QQQ");
       
        
      JButton okButton = new JButton("Search");
      ButtonHandler listener = new ButtonHandler();
      okButton.addActionListener(listener);
      JPanel content = new JPanel();
      content.setLayout(new BorderLayout());
      content.add(displayPanel, BorderLayout.CENTER);
      content.add(okButton, BorderLayout.SOUTH);
      content.add(inputField, BorderLayout.NORTH);

      JFrame window = new JFrame("Trade Net Market Data");
      window.setContentPane(content);
      window.setSize(350, 250);
      //window.setLocation(0,0);
      window.setVisible(true);       
    }
    
    
    //fetches the data using getclient and parses out relevant pieces into a string array
    public static String[] getValues(String symbolInput) throws IOException {
        //Symbol, Description, Exchange, Closing Price, Daily Net Change & Percentage, Volume, Average Volume, 52-Week High & Low
        String response = GETClient.getRequest(symbolInput);
        String[] values;
        String symbol = getValue(response, "symbol");
        String description = getValue(response, "description");
        String exchange = getValue(response, "exch");
        String closing = getValue(response, "last");
        String netChange = getValue(response, "change");
        String percChange = getValue(response, "change_percentage");
        String volume = getValue(response, "volume");
        String avgVolume = getValue(response, "average_volume");
        String high52 = getValue(response, "week_52_high");
        String low52 = getValue(response, "week_52_low");
        values = new String[]{"Symbol:"+symbol, "Description:"+description,
            "Exchange:"+exchange, "Closing:"+closing, "Net Change:"+netChange, 
            "% Change:"+percChange, "Volume:"+volume, "Average Volume:"+avgVolume, 
            "52 Week High:"+high52, "52 Week Low:"+low52};
        return values;
    }
    
    //assists getValues in getting a single target value from an input string
    public static String getValue(String input, String target) {
        return Jsoup.parse(input).select(target).first().text();
    }
}


