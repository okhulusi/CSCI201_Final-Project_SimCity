package city.restaurant.omar;

public class Table {
	
	OmarCustomerRole occupiedBy;
	int tableNumber;
	
	int tableX;
	int tableY;

	Table(int tableNumber, int x, int y) {
		this.tableNumber = tableNumber;
		this.tableX = x;
		this.tableY = y;
		setUnoccupied();
	}
		
	void setOccupant(OmarCustomerRole cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	OmarCustomerRole getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
	
	public int getTableNumber(){
		return tableNumber;
	}
	
	public void setTableNumber(int tableNumber){
		this.tableNumber = tableNumber;
	}
	
	public int getX(){
		return tableX;
	}
	
	public int getY(){
		return tableY;
	}
}
