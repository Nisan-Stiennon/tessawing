//package Guacamole;

import javax.swing.*;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import java.awt.event.*;
import com.sun.j3d.utils.geometry.*;
import java.util.*;
import javax.vecmath.*;
import Jama.*;

public class Guacamole implements KeyListener{

// windowing stuff:
final static String appName = "Guacamole";
JFrame window;
CardLayout layout;
final String CARD_MENU = "menu";
final String CARD_LOADING = "loading";
final String CARD_GAME = "game";
// gaming stuff:
Root scene;
// keyboard stuff:
long lastTime = 0;
Hashtable<Integer, Integer> keyAxis = new Hashtable<Integer, Integer>();
Hashtable<Integer, Integer> keySign = new Hashtable<Integer, Integer>();
int[][] keymap = {
	{KeyEvent.VK_UP, 0, 1},
	{KeyEvent.VK_DOWN, 0, -1},
	{KeyEvent.VK_RIGHT, 1, 1},
	{KeyEvent.VK_LEFT, 1, -1},
	{KeyEvent.VK_SHIFT, 2, 1},
	{KeyEvent.VK_SLASH, 2, -1},
	{KeyEvent.VK_W, 3, 1},
	{KeyEvent.VK_S, 3, -1},
	{KeyEvent.VK_D, 4, 1},
	{KeyEvent.VK_A, 4, -1},
	{KeyEvent.VK_E, 5, 1},
	{KeyEvent.VK_Q, 5, -1},
	{KeyEvent.VK_SPACE, 6, 1},
	{KeyEvent.VK_Z, 6, -1}};

public Guacamole(){
	for(int i=0; i<keymap.length; i++){
		keyAxis.put(keymap[i][0], keymap[i][1]);
		keySign.put(keymap[i][0], keymap[i][2]);
		}
	// set up the window
	window = new JFrame(appName);
	window.setSize(600, 600);
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	layout = new CardLayout();
	window.getContentPane().setLayout(layout);
	window.setVisible(true);
	window.requestFocus();
	window.addKeyListener(this);
	}

public static void main(String[] args){
	// Swing isn't thread-safe, so we have to do this:
	javax.swing.SwingUtilities.invokeLater(new Runnable(){
		public void run(){
			// standard hack to prevent the Canvas3D from disappearing randomly:
			System.setProperty("sun.awt.noerasebackground", "true");
			Guacamole guacamole = new Guacamole();
			guacamole.loadWorld();	
            }
		});
	}

// the CardLayout doesn't work correctly at the moment. Java3D isn't very compatible with Swing.
public void loadWorld(){
	window.add(new JLabel("Loading..."), CARD_LOADING);
	layout.show(window.getContentPane(), CARD_LOADING);
	Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	SimpleUniverse universe = new SimpleUniverse(canvas);
	universe.getViewer().getView().setMinimumFrameCycleTime(5);
	scene = World_Test.initializeWorld(universe);
	/* test code:
	BranchGroup bg = new BranchGroup();
	bg.addChild(new Temp3());
	universe.getLocale().addBranchGraph(bg);
	*/
	window.add(canvas, CARD_GAME);
	layout.show(window.getContentPane(), CARD_GAME);
	}

public void keyPressed(KeyEvent e){
	/* when the user holds down a key, the system will rapidly fire off keyPressed events. If every one of those events required us to do lots of computation, we'd be overwhelmed. So we drop the event unless it's the first one in a while.
	if(System.currentTimeMillis() - lastTime > 100){
		lastTime = System.currentTimeMillis();
		handleKeyboardEvent(e);
		}
	*/
	handleKeyboardEvent(e);
	}

public void keyReleased(KeyEvent e){
	handleKeyboardEvent(e);
	}
	
public void keyTyped(KeyEvent e){
	}
	
void handleKeyboardEvent(KeyEvent e){
	int key = e.getKeyCode();
	// currently, we only handle keypresses that have to do with navigation
	if(!keyAxis.containsKey(key)) return;
	int terminus = (e.getID() == KeyEvent.KEY_PRESSED ? 1 : 0);
	int axis = keyAxis.get(key);
	int sign = keySign.get(key);
	if(axis == 6) scene.targetVelocity = scene.maximumVelocity * sign * terminus;
	else scene.targetAngularVelocity.set(axis, 0, scene.maximumAngularVelocityComponent * sign * terminus);
	}

}