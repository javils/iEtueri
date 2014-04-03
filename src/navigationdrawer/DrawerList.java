package navigationdrawer;

/**
 * Class for organize the List of the Navigation Drawer
 * 
 * @author Javier Luque Sanabria
 */
public class DrawerList {
	private int idImage;
	private int idText;

	public DrawerList(int idImage, int idText) {
		this.idImage = idImage;
		this.idText = idText;
	}

	public int getIdImage() {
		return this.idImage;
	}

	public int getIdText() {
		return this.idText;
	}
}
