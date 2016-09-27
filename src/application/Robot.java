package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Robot extends BorderPane {
	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	// Background color that the robot cover
    private Color c = new Color(0.7, 0.3, 0.4, 0.2);
    private BackgroundFill frbg = new BackgroundFill(c, null, null);
    private Background rbg = new Background(frbg);
    
	private int x;
	private int y;
	private int direction;
	private int frontLeftDis;
	private int frontStraDis;
	private int frontRightDis;
	private int leftDis;
	private int rightDis;
	private Grid[][] originMap = new Grid[20][15];
	private boolean done = false;
	private ImageView robot;
	
	public Robot() {
		super();
		
		x = 17;
		y = 0;
		direction = 0;
		
	    setBackground(rbg);
	    setPrefWidth(90);
	    setPrefHeight(90);

	    robot = new ImageView("images/ic_mdp_robot.png");
	    robot.setFitHeight(70);
	    robot.setFitWidth(70);
	    
	    setCenter(robot);
	    setVisible(false);
	}

	public int getFrontLeftDis() {
		return frontLeftDis;
	}

	public void setFrontLeftDis(int frontLeftDis) {
		this.frontLeftDis = frontLeftDis;
	}

	public int getFrontStraDis() {
		return frontStraDis;
	}

	public void setFrontStraDis(int frontStraDis) {
		this.frontStraDis = frontStraDis;
	}

	public int getFrontRightDis() {
		return frontRightDis;
	}

	public void setFrontRightDis(int frontRightDis) {
		this.frontRightDis = frontRightDis;
	}

	public int getLeftDis() {
		return leftDis;
	}

	public void setLeftDis(int leftDis) {
		this.leftDis = leftDis;
	}

	public int getRightDis() {
		return rightDis;
	}

	public void setRightDis(int rightDis) {
		this.rightDis = rightDis;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		setLayoutX(x*30);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		setLayoutY(y*30);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		robot.setRotate(direction);
	}
		
	//Read data from file now; Change to reading from sensor later!	
	public String mapFilePath = "maps/grid.txt";
	public void readMapFromFile() throws IOException {
		File file = new File(mapFilePath);
		Reader reader = null;
		try {
			int tempChar;
			reader = new InputStreamReader(new FileInputStream(file));
		    for (int row = 0; row < 20; row++) {
		    	for (int col = 0; col < 15; col++) {
		    		if ((tempChar = reader.read())==1)
		    			originMap[row][col].setFreeSpace(false);
		    		if ((tempChar = reader.read())==0)
		    			originMap[row][col].setFreeSpace(true);		    			
		    	}		    	
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public void getFront()
	{
		int i,j,dis;
		boolean reach_wall = false;
		dis = 0;
		for (i = 0; i < 3; i++)
		{
			for (j = 0; j < 5; j++)
			{
				switch (direction)
				{
					case 0:
						if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x + i - 1].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 1:
						if ((x + j + 2 <= 14)&&(originMap[y + 1 - i][x + j + 2].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 2:
						if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x - i + 1].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 3:
						if ((x - j - 2 >= 0)&&(originMap[y - 1 + i][x - j - 2].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					default:
						break;
				}
			}
			switch (i)
			{
				case 0:
					setFrontLeftDis(dis);
					break;
				case 1:
					setFrontStraDis(dis);
					break;
				case 2:
					setFrontRightDis(dis);
					break;
				default:
					break;
			}
			reach_wall = false;
			dis = 0;
		}
		return;
	}

	public void getLeftData()
	{
		int j, dis;
		boolean reach_wall = false;
		dis = 0;
		for (j = 0; j < 5; j++)
			{
				switch (direction)
				{
					case 0:
						if ((x - 2 - j >= 0)&&(originMap[y][x - 2 - j].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 1:
						if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 2:
						if ((x + 2 + j <= 14)&&(originMap[y][x + 2 + j].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 3:
						if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					default:
						break;
				}
			}
		setLeftDis(dis);
		return;
	}

	public void getRightData()
	{
		int j, dis;
		boolean reach_wall = false;
		dis = 0;
		for (j = 0; j < 8; j++)
		{
			switch (direction)
			{
				case 0:
					if ((x + 2 + j <= 14)&&(originMap[y][x + 2 + j].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 1:
					if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 2:
					if ((x - 2 - j >= 0)&&(originMap[y][x - 2 - j].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 3:
					if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				default:
					break;
			}
		}
		setRightDis(dis);
	}

	public void getData()
	{
		getLeftData();
		getFront();
		getRightData();
		return;
	}
	
	public void moveForward(int dis, int dir)
	{
		switch (dir)
		{
			case 0:
				setY(y += dis);
				break;

			case 1:
				setX(x += dis);
				break;

			case 2:
				setY(y -= dis);
				break;

			case 3:
				setX(x -= dis);
				break;
		}		
	}
	
	public void turnLeft() {
		setDirection((direction-90)%360);
	}
	
	public void turnRight() {
		setDirection((direction-90)%360);
	}
	
	public void updatePosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public boolean checkFront()
	{
		getFront();
		if ((frontLeftDis>0)&&(frontStraDis>0)&&(frontRightDis>0))
			return true;
		return false;
	}

	public boolean checkLeft()
	{
		boolean maybe = false;
		getLeftData();
		if (leftDis>0)
		{
			switch(direction)
			{
				case 0:
					if (originMap[y - 1][x - 2].isFreeSpace())
						maybe = true;
					break;
				
				case 1:
					if (originMap[y + 2][x - 1].isFreeSpace())
						maybe = true;
					break;

				case 2:
					if (originMap[y + 1][x + 2].isFreeSpace())
						maybe = true;
					break;

				case 3:
					if (originMap[y - 2][x + 1].isFreeSpace())
						maybe = true;
					break;
			}
			if (maybe)
			{
				//Remember to change here!
				direction = (direction + 3) % 4; 
				getFront();
				if (frontRightDis>0)
				{
					return true;
				}
			}
		}
		return false;
	}	
}