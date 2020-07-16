package kph.jeopardy.view;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Calendar extends Application
{
	public List<LocalDate> gameDays = new ArrayList<>();
	
	public void start(Stage s)
	{
		gameDays.add(LocalDate.of(2020, Month.JULY, 4));
		s.setTitle("Jeopardy!");
		GridPane r = new GridPane();
		DatePicker d = new DatePicker();
		d.setDayCellFactory(new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(DatePicker param) {
				return new DateCell(){
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (!empty && item != null) {
							if(gameDays.contains(item)) {
								this.setStyle("-fx-background-color: blue");
							}
						}
					}
				};
			}
		});
		r.add(new Text("Please pick a date in the calendar below corresponding to the air date of the Jeopardy! game you would like to play."), 0, 0);
		r.add(d, 0, 1);
		Button b = new Button("OK");
		b.setOnAction(click -> 
		{
			s.close();
			System.out.println(d.getValue());
		}
		);
		r.add(b, 1, 2);
		Scene sc = new Scene(r, 700, 300);
		s.setScene(sc);
		s.show();
	}

	public static void main(String args[])
	{
		// launch the application
		launch(args);
	}
}
