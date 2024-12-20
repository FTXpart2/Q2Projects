import java.util.Objects;
import java.io.Serializable;
public class Location implements Serializable{
    private static final long serialVersionUID = 1L;
    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return row == location.row && col == location.col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}