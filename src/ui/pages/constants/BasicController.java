package ui.pages.constants;

import javafx.scene.Node;
import ui.pages.PageKeeper;



public interface BasicController {
    public Node getRoot();
    public void setPageKeeper(PageKeeper pg);
    public void refreshPage();
}
