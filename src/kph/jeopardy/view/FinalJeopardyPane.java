package kph.jeopardy.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import kph.jeopardy.model.Game;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class FinalJeopardyPane extends GridPane
{
	private static final String CATEGORY_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 4em; -fx-alignment: CENTER; -fx-text-alignment: center";
	private static final String ANSWER_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 4em; -fx-alignment: CENTER; -fx-text-alignment: center";
	private static final String QUESTION_STYLE = "-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-alignment: CENTER; -fx-text-alignment: center";

	public GridPane init(Game game)
	{
		GridPane enterWager = new GridPane();
		int count = 0;
		for (int i = 0; i < game.getPlayers().size(); i++)
		{
			if (game.getPlayers().get(i).getScore() > 0)
			{
				Text t = new Text("Enter " + game.getPlayers().get(i).getName() + "'s wager:");
				t.setFont(new Font(36));
				t.setFill(Color.WHITE);
				TextField tField = new TextField();
				tField.setId(game.getPlayers().get(i).getName() + "-wager");
				enterWager.add(t, 0, count);
				enterWager.add(tField, 1, count);
				count++;
			}
		}
		enterWager.setHgap(5);
		enterWager.setVgap(5);
		Button category = new Button(game.getCategory(3, 0).getName());
		category.setStyle(CATEGORY_STYLE);
		category.setWrapText(true);

		Button answer = new Button("Show answer");
		answer.setStyle(ANSWER_STYLE);
		answer.setWrapText(true);
		answer.setOnAction(click ->
		{
			answer.setText(game.getQuestion(3, 1, 0).getAnswer());
		});

		Button question = new Button("Show question");
		question.setStyle(QUESTION_STYLE);
		question.setWrapText(true);
		question.setOnAction(click ->
		{
			question.setText(game.getQuestion(3, 1, 0).getBody());
			try
			{
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				AudioStream audioStream = new AudioStream(cl.getResourceAsStream("finaljeopardy.snd"));
				AudioPlayer.player.start(audioStream);
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		});

		this.add(category, 0, 0);
		this.add(question, 0, 1);
		this.add(answer, 0, 2);
		this.add(enterWager, 0, 3);
		this.setHgap(5);
		this.setVgap(5);

		double width = Screen.getPrimary().getVisualBounds().getWidth();
		double height = Screen.getPrimary().getVisualBounds().getHeight();
		category.setPrefWidth(6 * width / 8 + 25);
		answer.setPrefWidth(6 * width / 8 + 25);
		question.setPrefWidth(6 * width / 8 + 25);
		question.setPrefHeight(height / 2);
		return this;
	}
}
