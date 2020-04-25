package kph.jeopardy.view;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import kph.jeopardy.model.Game;

public class QuestionPane extends GridPane {

	private static final String CATEGORY_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 4em; -fx-alignment: CENTER; -fx-text-alignment: center";
	private static final String ANSWER_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 4em; -fx-alignment: CENTER; -fx-text-alignment: center";
	private static final String QUESTION_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-alignment: CENTER; -fx-text-alignment: center";

	public GridPane init(Game game, int round, int row, int column) {
		Button category = new Button(game.getCategory(round, column).getName());
		category.setStyle(CATEGORY_STYLE);
		category.setWrapText(true);
		
		Button answer = new Button("Show answer");
		answer.setStyle(ANSWER_STYLE);
		answer.setWrapText(true);
		answer.setOnAction(click -> {
			answer.setText(game.getQuestion(round, row, column).getAnswer());
		});
		
		Button question = new Button(game.getQuestion(round, row, column).getBody());
		question.setStyle(QUESTION_STYLE);
		question.setWrapText(true);

		Button value = new Button("$" + game.getQuestion(round, row, column).getValue());
		value.setStyle(ANSWER_STYLE);
		value.setId("value");

		GridPane box = new GridPane();
		box.add(answer, 0, 0);
		box.add(value, 1, 0);
		
		this.add(category, 0, 0);
		this.add(question, 0, 1);
		this.add(box, 0, 2);
		this.setHgap(5);
		this.setVgap(5);
		
		double width = Screen.getPrimary().getVisualBounds().getWidth();
		double height = Screen.getPrimary().getVisualBounds().getHeight();
		category.setPrefWidth(6 * width/8 + 25);
		answer.setPrefWidth(5 * width/8 + 13);
		value.setPrefWidth(width/8 + 12);
		question.setPrefWidth(6 * width/8 + 25);
		question.setPrefHeight(height / 2);
		return this;
	}
}
