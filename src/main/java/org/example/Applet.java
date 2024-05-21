package escampe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Applet extends JApplet {
	// Constantes pour les pièces
	final private static int LICORNEBLANCHE = -2;
	final private static int PALADINBLANC = -1;
	final private static int LICORNENOIRE = 2;
	final private static int PALADINNOIR = 1;
	final private static int VIDE = 0;
	
	// Constantes pour le plateau
	final private static int LARGEUR = 6;
    final private static int HAUTEUR = 6;
    final private static int[][] lisereCase = {
		{1, 2, 2, 3, 1, 2},
		
		{3, 1, 3, 1, 3, 2}, 
		
		{2, 3, 1, 2, 1, 3},
		
		{2, 1, 3, 2, 3, 1},
		
		{1, 3, 1, 3, 1, 2},
		
		{3, 2, 2, 1, 3, 2}
	};
    
    // Constantes pour les couleurs
    Color DARK = new Color(155, 102, 95);
	Color LIGHT = new Color(239, 210, 158);
	Color BLACK = new Color(255, 255, 255);
	Color WHITE = new Color(0, 0, 0);
	Color HIGHLIGHT = new Color(255, 0, 0);
    
    // Constantes pour l'affichage
    final private static int TAILLECASE = 100;
    final private static int TAILLEPION = 60;
    final private static Dimension FRAMEDIMENSION = new Dimension(TAILLECASE*6 + 260,TAILLECASE*6 + 60);
    
    private static final long serialVersionUID = 1L;
    private JList brdList;
    private Board displayBoard;
    private JScrollPane scrollPane;
    private DefaultListModel listModel;
    private Frame myFrame;

    static int cpt = 0;
    
    // Autres constantes utiles pour l'affichage du plateau d'Escampe
    int mpiece = (int) (TAILLECASE - TAILLEPION)/2;
    
    int epaisseurCercle = (int) (TAILLECASE*0.1);
    int epaisseurInterCercle = (int) (TAILLECASE*0.05);
    
 	int diametre1e = TAILLECASE;                        // extérieur 1er cercle
 	int diametre1i = diametre1e - epaisseurCercle;      // intérieur 1er cercle
 	int diametre2e = diametre1i - epaisseurInterCercle; // extérieur 2eme cercle
 	int diametre2i = diametre2e - epaisseurCercle;      // intérieur 2eme cercle
 	int diametre3e = diametre2i - epaisseurInterCercle; // extérieur 3eme cercle
 	int diametre3i = diametre3e - epaisseurCercle;      // intérieur 3eme cercle
 	
 	int m1e = 0;
 	int m1i = (int) (TAILLECASE - diametre1i)/2;
 	int m2e = (int) (TAILLECASE - diametre2e)/2;
 	int m2i = (int) (TAILLECASE - diametre2i)/2;
 	int m3e = (int) (TAILLECASE - diametre3e)/2;
 	int m3i = (int) (TAILLECASE - diametre3i)/2;

    public void init() {
    	System.out.println("Initialisation BoardApplet" + cpt++);
    	buildUI(getContentPane());
    }

    public void buildUI(Container container) {
    	setBackground(Color.white);
    	
    	int[][] temp = new int[HAUTEUR][LARGEUR];
    	
    	for (int i = 0; i < HAUTEUR; i++)
    		for (int j = 0; j < LARGEUR; j++)
    			temp[i][j] = VIDE;
    	
    	displayBoard = new Board("Coups :", temp);
    	
    	listModel = new DefaultListModel();
    	listModel.addElement(displayBoard);
    	
    	brdList = new JList(listModel);
    	brdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	brdList.setSelectedIndex(0);
    	scrollPane = new JScrollPane(brdList);
    	Dimension d = scrollPane.getSize();
    	scrollPane.setPreferredSize(new Dimension(200, d.height));
    	
    	brdList.addKeyListener(new java.awt.event.KeyAdapter() {
    		public void keyPressed(KeyEvent e) {
    			brdList_keyPressed(e);
    		}
    	});
    	brdList.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(MouseEvent e) {
    			brdList_mouseClicked(e);
    		}
    	});
    	container.add(displayBoard, BorderLayout.CENTER);
    	container.add(scrollPane, BorderLayout.EAST);
    }

    public void update(Graphics g, Insets in) {
    	Insets tempIn = in;
    	g.translate(tempIn.left, tempIn.top);
    	paint(g);
    }

    public void paint(Graphics g) {
    	displayBoard.paint(g);
    }

    public void addBoard(String move, int[][] board) {
    	Board tempEntrop = new Board(move, board);
    	listModel.addElement(new Board(move, board));
    	brdList.setSelectedIndex(listModel.getSize() - 1);
    	brdList.ensureIndexIsVisible(listModel.getSize() - 1);
    	displayBoard = tempEntrop;
    	update(myFrame.getGraphics(), myFrame.getInsets());
    }

    public void setMyFrame(Frame f) {
    	myFrame = f;
    }

    void brdList_keyPressed(KeyEvent e) {
    	int index = brdList.getSelectedIndex();
    	if (e.getKeyCode() == KeyEvent.VK_UP && index > 0)
    		displayBoard = (Board) listModel.getElementAt(index - 1);
    	
    	if (e.getKeyCode() == KeyEvent.VK_DOWN && index < (listModel.getSize() - 1))
    		displayBoard = (Board) listModel.getElementAt(index + 1);
    	
    	update(myFrame.getGraphics(), myFrame.getInsets());
    }

    void brdList_mouseClicked(MouseEvent e) {
    	displayBoard = (Board) listModel.getElementAt(brdList.getSelectedIndex());
    	update(myFrame.getGraphics(), myFrame.getInsets());
    }
    
    public Dimension getDimension() {
    	return FRAMEDIMENSION;
    }
    
    // Sous classe qui dessine le plateau de jeu
    class Board extends JPanel {
    	
    	private static final long serialVersionUID = 1L;
    	private int[][] boardState;
    	String move;
    	int depCol = -1;
    	int depLin = -1;
    	int arvCol = -1;
    	int arvLin = -1;
    	
    	// The string will be the move details
    	// and the array the details of the board after the move has been applied.
    	public Board(String mv, int[][] bs) {
    		boardState = bs;
    		move = mv;
    		if (mv.length() == 5) {
    			String[] positions = mv.split("-");
    			depCol = (int) positions[0].charAt(0) - (int) 'A';
    			depLin = Integer.parseInt(positions[0].substring(1)) - 1;
    			arvCol = (int) positions[1].charAt(0) - (int) 'A';
    			arvLin = Integer.parseInt(positions[1].substring(1)) - 1;
    		}
    	}
    	
    	public void drawBoard(Graphics g) {
    		// First draw the lines
    		// Board
    		int bx = 30;
    		int by = 30;
    		
    		// axis labels
    		g.setColor(new Color(0, 0, 0));
    		for (int i = 1; i <= LARGEUR; i++) {
    			g.drawString("" + (char) ('A' + i - 1), bx + (int) ((i - 0.5)*TAILLECASE), 20);
    		}
    		for (int i = 1; i <= HAUTEUR; i++) {
    			g.drawString("" + i, 10, by + (int) ((i - 0.5)*TAILLECASE));
    		}
    		
    		// Draw the circles
    		Color c1 = DARK;
    		Color c2 = LIGHT;
    		
    		int casex;
    		int casey;
    		int lisere;
    		
    		// fond des cases
			g.setColor(c1);
			g.fillRect(bx, by, LARGEUR*TAILLECASE, HAUTEUR*TAILLECASE);
			
    		for (int j = 0; j < LARGEUR; j++) {
    			for (int i = 0; i < HAUTEUR; i++) {
    				casex = bx + j*TAILLECASE;
    				casey = by + i*TAILLECASE;
    				lisere = lisereCase[i][j];
    				c2 = (i == depLin && j == depCol) ? HIGHLIGHT : LIGHT;
    				
    				// 1er cercle
    				g.setColor(c2);
    				g.fillOval(casex + m1e, casey + m1e , diametre1e, diametre1e);
    				g.setColor(c1);
    				g.fillOval(casex + m1i, casey + m1i, diametre1i, diametre1i);
    				if (lisere > 1) {
    					// 2eme cercle
    					g.setColor(c2);
        				g.fillOval(casex + m2e, casey + m2e, diametre2e, diametre2e);
        				g.setColor(c1);
        				g.fillOval(casex + m2i, casey + m2i, diametre2i, diametre2i);
        				if (lisere > 2) {
        					// 3eme cercle
        					g.setColor(c2);
            				g.fillOval(casex + m3e, casey + m3e, diametre3e, diametre3e);
            				g.setColor(c1);
            				g.fillOval(casex + m3i, casey + m3i, diametre3i, diametre3i);
        				}
    				}
    			}
    		}
    		
    		// Draw the pieces by referencing boardState array
    		c1 = BLACK;
    		c2 = WHITE;
    		
    		for (int j = 0; j < LARGEUR; j++) {
    			for (int i = 0; i < HAUTEUR; i++) {
    				casex = mpiece + bx + j*TAILLECASE;
    				casey = mpiece + by + i*TAILLECASE;
    				
    				switch (boardState[i][j]) {
    					case (LICORNEBLANCHE):
    						g.setColor(c1);
    						g.fillRect(casex, casey, TAILLEPION, TAILLEPION);
    						break;
    					case (PALADINBLANC):
    						g.setColor(c1);
    						g.fillOval(casex, casey, TAILLEPION, TAILLEPION);
    						break;
    					case (LICORNENOIRE):
    						g.setColor(c2);
    						g.fillRect(casex, casey, TAILLEPION, TAILLEPION);
    						break;	
    					case (PALADINNOIR):
    						g.setColor(c2);
    						g.fillOval(casex, casey, TAILLEPION, TAILLEPION);
    						break;		    
    				}
    				
    				if (i == arvLin && j == arvCol) {
    					g.setColor(HIGHLIGHT);
						g.fillOval(casex + 20, casey + 20, TAILLEPION - 40, TAILLEPION - 40);
    				}
    			}
    		}
    	}
    	
    	public void paint(Graphics g) {
    		drawBoard(g);
    	}
    	
    	public void update(Graphics g) {
    		drawBoard(g);
    	}
    	
    	public String toString() {
    		return move;
    	}
    }
}
