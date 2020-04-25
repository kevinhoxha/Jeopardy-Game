package kph.jeopardy.view;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import kph.jeopardy.model.Game;

public class BoardPane extends GridPane {
	
	public BoardPane init(Game game, int round) {
		double columnWidth = Screen.getPrimary().getVisualBounds().getWidth()/8;

		for (int column = 0; column < 6; column++)
		{
			for (int row = 0; row < 6; row++)
			{
				if (row == 0)
				{
					Button b = new Button(game.getCategory(round,  column).getName());
					b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: "+ columnWidth + "px; -fx-alignment: CENTER; -fx-text-alignment: center");
					b.setWrapText(true);
					b.setEffect(new DropShadow());
					b.setId("cat" + column);
					this.add(b, column, row);
				}  
				else
				{
					Button b = new Button("$" + game.getQuestion(round, row, column).getValue());
					b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 3em; -fx-pref-width: " + columnWidth + "px");
					b.setEffect(new DropShadow());
					//b.setId("r" + round + "q" + row + "" + column);
					final int r = row, c = column;
					b.setOnAction(click -> {
						b.setText("✓");
						b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 3em; -fx-pref-width: " + columnWidth + "px");
						GridPane questionPane = new QuestionPane().init(game, round, r, c);
						((BorderPane)this.getParent()).setCenter(questionPane);
					});
					
					this.add(b, column, row);
				}
			}
		}
		this.setHgap(5);
		this.setVgap(5);
		return this;
	}

}
