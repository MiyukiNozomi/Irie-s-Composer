 */
public enum Side {
    NORTH(0),
    SOUTH(1),
    EAST(2),
    WEST(3),
    TOP(4),
    BOTTOM(5);
    
    private final int index;
    
    private Side(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public static Side getSide(int index) {
        switch (index) {
            case 0 -> return NORTH;
            case 1 -> return SOUTH;
            case 2 -> return EAST;
            case 3 -> return WEST;
            case 4 -> return TOP;
            case 5 -> return BOTTOM;
            default -> throw new IndexOutOfBoundsException(index); 
        }
    }
}
