package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class Arena extends AnchorPane implements EventHandler<Event> {
	public Grid[][] grids = new Grid[20][15];
	private int prev_row, prev_col;

	private Robot robot;
	private GridPane arena;
	private boolean changeCoordinate;

	public Arena(int i) {
		super();

		// initialize the robot icon
		//robot = new Robot(1, 18, 0, grids, true);
		
		// 
		changeCoordinate = false;
		
		// add the grid to the arena
		arena = new GridPane();

	    for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
			    Grid grid = new Grid(row, col);
			    grid.setMinWidth(30);
			    grid.setMinHeight(30);
			    grid.setOnMouseEntered(this);
			    grid.setOnMouseExited(this);
			    grid.setOnMouseClicked(this);
			    				    
			    arena.add(grid, col, row);
			    grids[row][col] = grid;
		    }
	    }
	    
	    arena.setGridLinesVisible(true);
	    
	    // add it into the arena
	    getChildren().add(arena);
	    //getChildren().add(robot);
	}
	
	public Arena() {
		super();

		// initialize the robot icon
		robot = new Robot(1, 18, 0, grids, true);
		
		// 
		changeCoordinate = false;
		
		// add the grid to the arena
		arena = new GridPane();

	    for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
			    Grid grid = new Grid(row, col);
			    grid.setMinWidth(30);
			    grid.setMinHeight(30);
			    grid.setOnMouseEntered(this);
			    grid.setOnMouseExited(this);
			    grid.setOnMouseClicked(this);
			    				    
			    arena.add(grid, col, row);
			    grids[row][col] = grid;
		    }
	    }
	    
	    arena.setGridLinesVisible(true);
	    
	    // add it into the arena
	    getChildren().add(arena);
	    getChildren().add(robot);
	}
	
	public Robot getRobot() {
		return robot;
	}
	
	public boolean isChangeCoordinate() {
		return changeCoordinate;
	}

	public void setChangeCoordinate(boolean changeCoordinate) {
		this.changeCoordinate = changeCoordinate;		
		robot.setVisible(!changeCoordinate);
	}
	
	public void readMapFromFile(File file) throws IOException {
		//File file = new File(mapFilePath);
		Reader reader = null;
		try {
			int tempChar;
			reader = new InputStreamReader(new FileInputStream(file));
			int row=19,col=0;
		    while ((tempChar = reader.read())!=-1) {		    			    	
		    	if (tempChar==49) {
	    	    	grids[row][col].setWall();	    	    
	    		}
		    	else if (tempChar==48) {
	    			grids[row][col].setFreeSpace();
	    		}	    	
		    	if ((row>16)&&(col<3))
		    		grids[row][col].setStart();
		    	else if ((row<3)&&(col>11))
		    		grids[row][col].setGoal();
		    	col++;
	    		if (col==15) {
		    		row--;
		    		col = 0;
		    	}
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String mdf) {
		int i;
		int index = 1;
		String[] bin = mdf.split("");
		
		for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
	    		i = Integer.parseInt(bin[index++]);
	    		grids[row][col].setFreeSpace(); 
		    }
	    }
	}
	
	public void printMap() {
		
	}
	
	public String generateMapDescriptor() {
		StringBuilder sb = new StringBuilder();		
		for (int row = 19; row >= 0; row--) {
	    	for (int col = 0; col < 15; col++) {
	    	
			    if (grids[row][col].isFreeSpace()) {
			    	sb.append(0);
			    } else {
			    	sb.append(1);
			    }			    
		    }
	    	
//	    	verify the map descriptor
//	    	sb.append("\n");
	    }		
		return sb.toString();
	}
	
	public String encodeMapDescriptor(int choice) {
		int index = 0;
		String md = null;
		if (choice==1) {
			md = getBinaryDesP1();
		}
		else if (choice==2) {
			md = getBinaryDesP2();
		}			
		StringBuilder sb = new StringBuilder();		
		
		while (index < md.length()) {
			sb.append(binaryToHex(md.substring(index, Math.min(index + 4, md.length()))));
            index += 4;
        }
		
		return sb.toString();
	}
	
	public String decodeMapDescriptor(String mdf) {
		int index = 0;
		
		StringBuilder sb = new StringBuilder();
		
		while (index < mdf.length()) {
			sb.append(hexToBinary(mdf.substring(index, Math.min(index + 1, mdf.length()))));
            index += 1;
        }
		
		return sb.toString();
	}
	
	public String hexToBinary(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);

        while (bin.length() < 4) {
            bin = '0' + bin;
        }

        return bin;
    }
	
	public String binaryToHex(String bin) {
        int i = Integer.parseInt(bin, 2);
        String hex = Integer.toString(i, 16);
        
        return hex;
    }
	
	public String getBinaryDesP1() {
		String desP1 = "11";
		for (int row = 19; row >= 0; row--) {
	    	for (int col = 0; col < 15; col++) {
	    		if (grids[row][col].isUnknown())
	    			desP1 += "0";
	    		else
	    			desP1 += "1";
		    }
	    }
		return desP1+"11";
	}
	
	public String getBinaryDesP2() {
		String desP2 = "";
		for (int row = 19; row >= 0; row--) {
	    	for (int col = 0; col < 15; col++) {
	    		if (grids[row][col].isFreeSpace())
	    			desP2 += "0";
	    		else if (grids[row][col].isWall())
	    			desP2 += "1";
		    }
	    }
		return desP2;
	}

	private void selectCoordinateHover(int row, int col) {
		if ((0 < row && row < 19) && (0 < col  && col < 14)) {
			int leftrow  = row-1;
			int rightrow = row+1;
			int topcol   = col-1;
			int bottomcol= col+1;

			grids[leftrow][col].hover();
			grids[leftrow][topcol].hover();
			grids[leftrow][bottomcol].hover();

			grids[row][col].hover();
			grids[row][topcol].hover();
			grids[row][bottomcol].hover();

			grids[rightrow][col].hover();
			grids[rightrow][topcol].hover();
			grids[rightrow][bottomcol].hover();
		}
	}
	
	private void selectCoordinateUnhover(int row, int col) {
		if ((0 < row && row < 19) && (0 < col  && col < 14)) {
			int leftrow  = row-1;
			int rightrow = row+1;
			int topcol   = col-1;
			int bottomcol= col+1;

			grids[leftrow][col].validate();
			grids[leftrow][topcol].validate();
			grids[leftrow][bottomcol].validate();

			grids[row][col].validate();
			grids[row][topcol].validate();
			grids[row][bottomcol].validate();

			grids[rightrow][col].validate();
			grids[rightrow][topcol].validate();
			grids[rightrow][bottomcol].validate();
		}
	}
	
	@Override
	public void handle(Event event) {
		String evt = event.getEventType().getName();
		Grid grid = (Grid) event.getSource();

		int row = grid.getRowIndex();
		int col = grid.getColumnIndex();

		if (changeCoordinate) {
			if (evt.equals("MOUSE_CLICKED")) {
				if ((0 < row && row < 19) && (0 < col  && col < 14)) {
					//selectCoordinateUnhover(row, col);
					
					robot.updatePosition(col,row);
					
					setChangeCoordinate(false);
				} else {
					System.out.println("out of bound");
				}
				// set robot coordinate
			} else if (evt.equals("MOUSE_ENTERED")) {			
				selectCoordinateHover(row, col);
			} else if (evt.equals("MOUSE_EXITED")) {	
				selectCoordinateUnhover(row, col);
			}
		} else {
			if (evt.equals("MOUSE_CLICKED")) {
				if (grid.isFreeSpace()) {
					grid.setWall();
				} else {
					grid.setFreeSpace();
				}
			} 
		}		
	}

	public void resetMap() {
		for (int row = 19; row >= 0; row--)
	    	for (int col = 0; col < 15; col++)
	    		grids[row][col].setFreeSpace();
	}

	public void resetMap(boolean result) {
		for (int row = 19; row >= 0; row--)
	    	for (int col = 0; col < 15; col++)
	    		grids[row][col].setUnknown();
		robot.setVisible(false);
		try {
			robot.setDirection(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}