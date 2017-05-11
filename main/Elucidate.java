package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import format.ColouredString;
import format.Margin;

public class Elucidate {

    public JFrame app;
    public JPanel result;
    public Dimension defaultSize;
    public Margin margin;
    
    private final String TITLE = "Elucidate v0.1";
    private final boolean NUMBERS = true;
    private final boolean STRIPED = true;
    private final Color STRIPCOLOUR = new Color(225, 225, 225);
    
    private String formatter;
    
    public static void main(String[] args) {
        new Elucidate();
    }
    
    public Elucidate() {
        
    	String path = this.getClass().getClassLoader().getResource("./examples/test_file").getPath();
    	String logo = this.getClass().getClassLoader().getResource("./main/logo.png").getPath();
    	
        String[] lines = IO.LoadFromFile(path);
        
        if (lines.length < 10) { 
            formatter = "%01d";
        }  
        else 
        { 
            if (lines.length < 100) { 
                formatter = "%02d";
            } 
            else { 
                if (lines.length < 1000) { 
                    formatter = "%03d"; 
                } 
            }
        } 
            
        ArrayList<ColouredString> ParserResults = new ArrayList<>();
        
        for (String l : lines) {
            ParserResults.addAll(Parser.Parse(l));
        }
        
        defaultSize = new Dimension(700, 500);
        
        app = new JFrame(TITLE);
        
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(defaultSize);
        app.setResizable(false);
        app.setIconImage(new ImageIcon(logo).getImage());
        app.setLocationRelativeTo(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(defaultSize);
        
        result = new JPanel() {
                      
            @Override
            public void paint(Graphics g) {
                setPreferredSize(getPanelSize(g, lines));
                super.paint(g);
                g.setFont(new Font("Courier New", Font.PLAIN, 12));
                g.setColor(Color.white);
                g.fillRect(0, 0, result.getWidth(), result.getHeight());
                int y = margin.GetTop(), x = margin.GetLeft();
                FontMetrics fm = g.getFontMetrics();
                int line = 1;
                boolean drawn = false;
                for (ColouredString cs : ParserResults) {
                    
                    if (STRIPED && !drawn && line % 2 == 1) {
                        g.setColor(STRIPCOLOUR);
                        g.fillRect(x - margin.GetLeft(), y + 3, (int) defaultSize.getWidth(), (int) fm.getStringBounds(" ", g).getHeight());
                    }
                    
                    g.setColor(cs.colour);
                    
                    if (NUMBERS && !drawn) {
                        g.setColor(new Color(50, 155, 50));
                        String number = String.format(formatter, line) + " ";
                        Rectangle2D rect = fm.getStringBounds(number, g);
                        g.drawString(number, x, y + (int) rect.getHeight());
                        g.setColor(cs.colour);
                        x += rect.getWidth();
                        drawn = true;
                    }
                    
                    Rectangle2D rect = fm.getStringBounds(cs.string + " ", g);
                    g.drawString(cs.string, x, y + (int) rect.getHeight());
                    
                    x += rect.getWidth();
                    if (cs.string.contains("\n") || cs.string.contains("\r")) {
                        y += rect.getHeight();
                        if (y > result.getHeight()) {
                            result.setSize(result.getWidth(), y + 10);
                        }
                        x = margin.GetLeft();
                        line++;
                        drawn = false;
                    }
                }
                
                // text highlighting?!
                //g.setColor(new Color(1, 0, 0, 0.25f));
                //g.fillRect(30, 30, 30, 30);
            }
        };
        
        JMenuBar menu = new JMenuBar();
        menu.setSize((int) defaultSize.getWidth(), 30);
        JMenu file = new JMenu();
        file.setText("File");
        menu.add(file);
        
        JMenuItem open = new JMenuItem();
        open.setText("Open...");
        open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileDiag = new JFileChooser("Open..");
				fileDiag.setMultiSelectionEnabled(false);
				fileDiag.showOpenDialog(null);
				File openme = fileDiag.getSelectedFile();
				if (openme != null) {
					System.out.println(openme.getAbsolutePath());
				}
			}
		});
        file.add(open);
        
        JMenuItem exit = new JMenuItem();
        exit.setText("Exit...");
        exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
        
        file.add(exit);
        app.add(menu);
        
        
        app.add(scrollPane);
        scrollPane.setViewportView(result);
        
        
        
        app.setVisible(true);
    }
    
    private Dimension getPanelSize(Graphics g, String[] lines) {
        String largestLine = "";
        for (String line : lines) {
            if (line.length() > largestLine.length()) largestLine = line;
        }
        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        int width = (int) fm.getStringBounds(largestLine, g).getWidth();
        int height = (int) fm.getStringBounds(largestLine, g).getHeight() * lines.length;
        return new Dimension(width, height);
    }
    
}
