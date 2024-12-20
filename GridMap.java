import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.io.Serializable;
public class GridMap extends JPanel implements Serializable{
    private static final long serialVersionUID = 1L;
    //makes 100 by 100 map
    private final int GRID_SIZE = 100;
    private final int VIEWPORT_SIZE = 10;
    private final int CELL_SIZE = 50;
    private MyHashTable<Location, DLList<GridObject>> gridMapTable = new MyHashTable<>(100);
    private Player player;
    private Obstacle obstacle; // Obstacle instance
    private Obstacle obstacle2;
    private Obstacle obstacle3;
    private Obstacle obstacle4;
 
    public GridMap(Player player) {
        if (player == null) {
            this.player = new Player(50, 50);  // Initialize with default position (0, 0)
        } else {
            this.player = player;  // Use the player passed to constructor (loaded state)
        }
       
        this.obstacle = new Obstacle(50, 50); // Starting position for the obstacle
        generateGermanyMap();
        obstacle2 = new Obstacle(80,35);
        obstacle3 = new Obstacle(20, 30);
        obstacle4 = new Obstacle(20, 70);
        // Start the obstacle movement thread
        Thread obstacleThread = new Thread(() -> {
            while (true) {
                obstacle.move();
                obstacle2.move();
                obstacle3.move();
                obstacle4.move();
                repaint();
                try {
                    Thread.sleep(1000); // Move the obstacle every 500ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        obstacleThread.start();

        
    }
    
    public void startObstacleThread() {
    Thread obstacleThread = new Thread(() -> {
        while (true) {
            obstacle.move();
            obstacle2.move();
            obstacle3.move();
            obstacle4.move();
            repaint();
            try {
                Thread.sleep(500); // Move the obstacle every 500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    obstacleThread.start();
}

    private void generateGermanyMap() {
        // Generate the map as before (with terrain, river, etc.)
        int germanyTop = 10, germanyBottom = 90;
        int germanyLeft = 10, germanyRight = 90;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                // Fill the map with terrain, water, and edges (same logic as before)
                if (row >= germanyTop && row <= germanyBottom && col >= germanyLeft && col <= germanyRight) {
                    if (row < germanyTop + 10 && Math.random() < 0.6) {
                        addObject(row, col, new GridObject("", Color.YELLOW));
                    } else {
                        double random = Math.random();
                        if (random < 0.4) {
                            addObject(row, col, new GridObject("", Color.GREEN));
                        } else if (random < 0.7) {
                            addObject(row, col, new GridObject("", new Color(139, 69, 19)));
                        } else if (random < 0.85) {
                            addObject(row, col, new GridObject("", new Color(0, 153, 0)));
                        } else {
                            addObject(row, col, new GridObject("", Color.GRAY));
                        }
                    }
                } else if (row < germanyTop) {
                    // Northern edge (Water - North Sea/Baltic Sea)
                    addObject(row, col, new GridObject("", Color.BLUE));
                } 
                else if (row >= germanyBottom && col >= germanyLeft && col <= germanyRight) {
                    // Bottom edge (Austria)
                    addObject(row, col, new GridObject("", new Color(2, 48, 32)));
                } 
                else if (col < germanyLeft) {
                    // Left edge (France)
                    addObject(row, col, new GridObject("", new Color (2, 48, 32)));
                } 
                else if (col >= germanyRight) {
                    // Right edge (Poland)
                    addObject(row, col, new GridObject("", new Color (2, 48, 32)));
                } 
                else {
                    // Remaining areas filled with water
                    addObject(row, col, new GridObject("Water", Color.BLUE));
                }
            }
        }
        drawRhineRiver();
        
    }

    private void drawRhineRiver() {
        // Add the river to the map as before
         // Define the path of the Rhine River
        int[][] rhinePath = {
            {85,10},{85,14},{85,13},{85,12},{85,11},{85, 15}, {84, 16}, {83, 16}, {82, 17}, {81, 18}, // Starting from the southwest
            {80, 18}, {79, 19}, {78, 19}, {77, 20}, {76, 21},
            {75, 22}, {74, 23}, {73, 23}, {72, 24}, {71, 25},
            {70, 25}, {69, 26}, {68, 27}, {67, 27}, {66, 28},
            {65, 29}, {64, 30}, {63, 30}, {62, 31}, {61, 32},
            {60, 33}, {59, 33}, {58, 34}, {57, 35}, {56, 35},
            {55, 36}, {54, 37}, {53, 38}, {52, 38}, {51, 39},
            {50, 40}, {49, 41}, {48, 41}, {47, 42}, {46, 43},
            {45, 44}, {44, 45}, {43, 45}, {42, 46}, {41, 47},
            {40, 48}, {39, 48}, {38, 49}, {37, 50}, {36, 51},
            {35, 52}, {34, 52}, {33, 53}, {32, 54}, {31, 55},
            {30, 55}, {29, 56}, {28, 57}, {27, 58}, {26, 58},
            {25, 59}, {24, 60}, {23, 60}, {22, 61}, {21, 62},
            {20, 63}, {19, 63}, {18, 64}, {17, 65}, {16, 66},
            {15, 66}, {14, 67}, {13, 68}, {12, 69}, {11, 69},{10,69} // Ending in the northwest
        };
    
        for (int[] location : rhinePath) {
            int row = location[0];
            int col = location[1];
            addObject(row, col, new GridObject("", Color.BLUE)); // Cyan color for river
        }

    }
    public Player getPlayer() {
        return player;
    }
    

    public boolean displayImage(){
        if((player.getRow() == 82-1 && player.getCol() == 15)||(player.getRow()==82+1 && player.getCol()==15)||(player.getCol() == 15+1 && player.getRow() == 82)||(player.getCol() == 15-1 && player.getRow() == 82)){
            return true;
        }
        if((player.getRow() == 40-1 && player.getCol() == 86)||(player.getRow()==40+1 && player.getCol()==86)||(player.getCol() == 86+1 && player.getRow() == 40)||(player.getCol() == 86-1 && player.getRow() == 40)){
            return true;
        }
        if((player.getRow() == 41-1 && player.getCol() == 89)||(player.getRow()==41+1 && player.getCol()==89)||(player.getCol() == 89+1 && player.getRow() == 41)||(player.getCol() == 89-1 && player.getRow() == 41)){
            return true;
        }
        if((player.getRow() == 88-1 && player.getCol() == 55)||(player.getRow()==88+1 && player.getCol()==55)||(player.getCol() == 55+1 && player.getRow() == 88)||(player.getCol() == 55-1 && player.getRow() == 88)){
            return true;
        }
        
        else{
            return false;
        }
    }
    public String landmarktype(){
        if((player.getRow() == 82-1 && player.getCol() == 15)||(player.getRow()==82+1 && player.getCol()==15)||(player.getCol() == 15+1 && player.getRow() == 82)||(player.getCol() == 15-1 && player.getRow() == 82)){
            return "Cologne Cathederal";
        }
        if((player.getRow() == 40-1 && player.getCol() == 86)||(player.getRow()==40+1 && player.getCol()==86)||(player.getCol() == 86+1 && player.getRow() == 40)||(player.getCol() == 86-1 && player.getRow() == 40)){
            return "Berlin Wall";
        }
        if((player.getRow() == 41-1 && player.getCol() == 89)||(player.getRow()==41+1 && player.getCol()==89)||(player.getCol() == 89+1 && player.getRow() == 41)||(player.getCol() == 89-1 && player.getRow() == 41)){
            return "Brandenburg Gate";
        }
        if((player.getRow() == 88-1 && player.getCol() == 55)||(player.getRow()==88+1 && player.getCol()==55)||(player.getCol() == 55+1 && player.getRow() == 88)||(player.getCol() == 55-1 && player.getRow() == 88)){
            return "Neuschwanstein Castle";
        }
        else{
            return "";
        }
    }

    public void addObject(int row, int col, GridObject obj) {
    Location loc = new Location(row, col);
    DLList<GridObject> objects = gridMapTable.get(loc);
    if (objects == null) {
        objects = new DLList<>();
        gridMapTable.put(loc, objects);
    }
    objects.add(obj); // Adjust DLList add method for single argument if needed
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the top-left corner of the viewport based on the player's position
        int viewportStartRow = Math.max(0, player.getRow() - VIEWPORT_SIZE / 2);
        int viewportStartCol = Math.max(0, player.getCol() - VIEWPORT_SIZE / 2);

        // Ensure the viewport does not exceed the map boundaries
        viewportStartRow = Math.min(viewportStartRow, GRID_SIZE - VIEWPORT_SIZE);
        viewportStartCol = Math.min(viewportStartCol, GRID_SIZE - VIEWPORT_SIZE);

        // Draw the 10x10 viewport as before
        
        for (int r = 0; r < VIEWPORT_SIZE; r++) {
            for (int c = 0; c < VIEWPORT_SIZE; c++) {
                int gridRow = viewportStartRow + r;
                int gridCol = viewportStartCol + c;

                Location loc = new Location(gridRow, gridCol);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (gridMapTable.containsKey(loc)) {
                    
                    for (GridObject obj : gridMapTable.get(loc)) {
                        obj.drawMe(g, c * CELL_SIZE, r * CELL_SIZE);
                    }
                }
            }
        }

        // Draw the player at their position within the viewport
         if (player != null) {
            int row = player.getRow();
            int col = player.getCol();
            
         }
         else {
        System.out.println("Player is null, unable to paint.");
        
        }
        int playerViewportX = (player.getCol() - viewportStartCol) * CELL_SIZE;
        int playerViewportY = (player.getRow() - viewportStartRow) * CELL_SIZE;

        // Draw the obstacle
        int obstacleViewportX = (obstacle.getCol() - viewportStartCol) * CELL_SIZE;
        int obstacleViewportY = (obstacle.getRow() - viewportStartRow) * CELL_SIZE;
        obstacle.drawMe(g, obstacleViewportX, obstacleViewportY, CELL_SIZE, "Audi");
        int obstacleViewportX1 = (obstacle2.getCol() - viewportStartCol) * CELL_SIZE;
        int obstacleViewportY1 = (obstacle2.getRow() - viewportStartRow) * CELL_SIZE;
        obstacle2.drawMe(g, obstacleViewportX1, obstacleViewportY1, CELL_SIZE,"Tank");
        int obstacleViewportX2 = (obstacle3.getCol() - viewportStartCol) * CELL_SIZE;
        int obstacleViewportY2 = (obstacle3.getRow() - viewportStartRow) * CELL_SIZE;
        obstacle3.drawMe(g, obstacleViewportX2, obstacleViewportY2, CELL_SIZE,"Audi");
        int obstacleViewportX3 = (obstacle4.getCol() - viewportStartCol) * CELL_SIZE;
        int obstacleViewportY3 = (obstacle4.getRow() - viewportStartRow) * CELL_SIZE;
        obstacle4.drawMe(g, obstacleViewportX3, obstacleViewportY3, CELL_SIZE, "Mercedes");

        int[][] bridges = {{84, 16},{48, 41},{27, 58}

        };

        ArrayList<Bridge> drawbridges = new ArrayList<>();
        for (int[] location : bridges) {
            int row = location[0];
            int col = location[1];
            drawbridges.add(new Bridge(row, col)); 
        }
        for(int i = 0; i < drawbridges.size(); i++){
            drawbridges.get(i).drawMe(g, (drawbridges.get(i).getCol() - viewportStartCol) * CELL_SIZE , (drawbridges.get(i).getRow() - viewportStartRow) * CELL_SIZE, CELL_SIZE);
        }
        int[][] landmarks = {
        /*cologne cathederal*/{82,15},/* Berlin wall*/{40,86}, /*brandenburg gate */ {41, 89}, /* Neuschwanstein Castle */{88,55}};
        
        ArrayList<Landmark> drawLandmarks = new ArrayList<>();
        drawLandmarks.add(new Landmark(landmarks[0][0], landmarks[0][1], "Cologne Cathederal"));
        drawLandmarks.add(new Landmark(landmarks[1][0], landmarks[1][1],"Berlin Wall"));
        drawLandmarks.add(new Landmark(landmarks[2][0], landmarks[2][1],"Brandenburg Gate"));
        drawLandmarks.add(new Landmark(landmarks[3][0], landmarks[3][1],"Neuschwanstein Castle"));
        for(int i = 0; i < drawLandmarks.size(); i++){
            drawLandmarks.get(i).drawMe(g, (drawLandmarks.get(i).getCol() - viewportStartCol) * CELL_SIZE , (drawLandmarks.get(i).getRow() - viewportStartRow) * CELL_SIZE, CELL_SIZE);
        }


        int[][] trees = {
            {15,15},{14,30},{12,11},{13,25},{14,20},{12,13},{17,35},{18,20},{12,15}
        };
        ArrayList<StandObstacle> drawTrees = new ArrayList<>();
        for (int[] location : trees) {
            int row = location[0];
            int col = location[1];
            drawTrees.add(new StandObstacle(row, col,"Tree")); 
        }
        
         for(int i = 0; i < drawTrees.size(); i++){
            drawTrees.get(i).drawMe(g, (drawTrees.get(i).getCol() - viewportStartCol) * CELL_SIZE , (drawTrees.get(i).getRow() - viewportStartRow) * CELL_SIZE, CELL_SIZE);
        }

        
         int[][] mountains = {
            {88,58},{85,50},{87,54},{90,57},{80,49},{83,47},{84,50},{89,53},{88,59}
        };
        ArrayList<StandObstacle> drawMountains = new ArrayList<>();
        for (int[] location : mountains) {
            int row = location[0];
            int col = location[1];
            drawMountains.add(new StandObstacle(row, col,"Mountain")); 
        }
        
         for(int i = 0; i < drawMountains.size(); i++){
            drawMountains.get(i).drawMe(g, (drawMountains.get(i).getCol() - viewportStartCol) * CELL_SIZE , (drawMountains.get(i).getRow() - viewportStartRow) * CELL_SIZE, CELL_SIZE);
        }


         int[][] boulders = {
            {15,80},{28,82},{40,84},{45,82},{50,80},{53,82},{51,78},{41,80},{45,84},{80,80},{80,50},{89,69},{80,75}
        };
        ArrayList<StandObstacle> drawBoulders = new ArrayList<>();
        for (int[] location : boulders) {
            int row = location[0];
            int col = location[1];
            drawBoulders.add(new StandObstacle(row, col,"Rock")); 
        }
        
         for(int i = 0; i < drawBoulders.size(); i++){
            drawBoulders.get(i).drawMe(g, (drawBoulders.get(i).getCol() - viewportStartCol) * CELL_SIZE , (drawBoulders.get(i).getRow() - viewportStartRow) * CELL_SIZE, CELL_SIZE);
        }
        drawBorders(g, viewportStartRow, viewportStartCol);
        player.drawMe(g, playerViewportX, playerViewportY, CELL_SIZE);
        if(obstacle.getCol() == player.getCol() && obstacle.getRow() == player.getRow()){
            player.restart();
        }
         if(obstacle2.getCol() == player.getCol() && obstacle2.getRow() == player.getRow()){
            player.restart();
        }
         if(obstacle3.getCol() == player.getCol() && obstacle3.getRow() == player.getRow()){
            player.restart();
        }
         if(obstacle4.getCol() == player.getCol() && obstacle4.getRow() == player.getRow()){
            player.restart();
        }
       

    }

    private void drawBorders(Graphics g, int viewportStartRow, int viewportStartCol) {
        // Draw borders as before
          g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));

        // Check if the viewport includes the labeled borders
        if (viewportStartRow < 10) {
            g.drawString("North Sea/Baltic Sea", getWidth() / 2 - 60, 15); // Top
        }
        if (viewportStartCol <= 10) {
            g.drawString("France", 10, getHeight() / 2); // Left side
        }
        if (viewportStartCol + VIEWPORT_SIZE > 90) {
            g.drawString("Poland", getWidth() - 60, getHeight() / 2); // Right side
        }
        if (viewportStartRow + VIEWPORT_SIZE > 90) {
            g.drawString("Austria", getWidth() / 2 - 30, getHeight() - 10); // Bottom
        }
    }
    public void saveData(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
}

public static GridMap loadData(String filename) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
        GridMap gridMap = (GridMap) in.readObject();
        gridMap.startObstacleThread(); // Add this line to restart the thread
        return gridMap;
    } catch (Exception e) {
        return null; // Return null if there's an error loading
    }
}

    
}
