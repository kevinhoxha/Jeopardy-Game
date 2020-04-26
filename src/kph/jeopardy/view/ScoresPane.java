package kph.jeopardy.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import kph.jeopardy.model.Contestant;
import kph.jeopardy.model.Game;

public class ScoresPane extends GridPane
{
	private Game game;

	public ScoresPane init(Game game)
	{
		this.game = game;
		int gridRow = 0;
		for (Contestant p : game.getPlayers())
		{
			Text name = new Text(p.getName() + ":");
			name.setFont(new Font(36));
			name.setFill(Color.WHITE);
			Text score = new Text("");
			score.setId("score" + p.getName());

			Button upB = new Button("+");
			upB.setStyle(
					"-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center; -fx-pref-width: 25px");
			upB.setId(p.getName() + "-up");
			Button downB = new Button("-");
			downB.setStyle(
					"-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center; -fx-pref-width: 25px");
			downB.setId(p.getName() + "-down");
			this.add(name, 0, gridRow);
			this.add(score, 1, gridRow);
			this.add(upB, 2, gridRow);
			this.add(downB, 3, gridRow);
			gridRow++;
		}
		updateScores();
		this.setVgap(5);
		this.setHgap(5);

		Button backButton = new Button("Back to Board");
		backButton.setStyle(
				"-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center");
		backButton.setId("goback");
		this.add(backButton, 0, gridRow + 10);

		Button nextRoundButton = new Button("Next Round");
		nextRoundButton.setStyle(
				"-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center");
		nextRoundButton.setId("nextround");
		this.add(nextRoundButton, 1, gridRow + 10);

		return this;
	}

	public void updateScores()
	{
		for (Contestant p : game.getPlayers())
		{
			Text score = (Text) this.lookup("#score" + p.getName());
			if (score == null)
				return;
			NumberFormat nf = new DecimalFormat("$#,##0");
			score.setFont(new Font(36));
			if (p.getScore() < 0)
			{
				score.setText(nf.format(-p.getScore()));
				score.setFill(Color.RED);
			} else
			{
				score.setText(nf.format(p.getScore()));
				score.setFill(Color.LIMEGREEN);
			}
		}
	}

}
