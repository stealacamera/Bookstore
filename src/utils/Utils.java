package utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utils {
	public static Stage getCurrentStage(ActionEvent event) {
		Node node = (Node) event.getSource();
		return (Stage) node.getScene().getWindow();
	}
	
	public static int getOrderedListIndex(int index) {			
		if(index < 0)
			index = (index * -1) - 1;
		
		return index;
	}
	
	public static Stage createPopupStage(String title, Parent view) {
		Stage stage = new Stage();
		stage.setTitle(title);
		stage.setScene(new Scene(view));
		stage.initModality(Modality.APPLICATION_MODAL);
		
		return stage;
	}
}
