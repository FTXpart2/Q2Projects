import java.awt.Graphics;
import java.awt.Color;

public class GridObject {
    private String name;
    private Color color;

    public GridObject(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public void drawMe(Graphics g, int x, int y) {
        /* 
        switch(name){
            case "water": 
                g.setColor(Color.BLUE);
                g.fillRect(x,y,50,50);
                break;

            case "grass":
                g.setColor(Color.GREEN);
                g.fillRect(x,y,50,50);
                break;
            case "dirt":
                g.setColor(Color.GRAY);
                g.fillRect(x,y,50,50);
                break;
            case "sand":
                g.setColor(Color.YELLOW);
                g.fillRect(x,y,50,50);
            default: 
                System.out.println("Nothing");
        */
        g.setColor(color);
        g.fillRect(x, y, 50, 50);
        g.setColor(Color.BLACK);
        g.drawString(name, x + 5, y + 25);
    }

    public String getName() {
        return name;
    }
}