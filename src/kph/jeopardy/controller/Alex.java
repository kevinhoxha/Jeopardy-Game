package kph.jeopardy.controller;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import kph.jeopardy.model.Contestant;
import kph.jeopardy.model.Game;
import kph.jeopardy.view.BoardPane;
import kph.jeopardy.view.FinalJeopardyPane;
import kph.jeopardy.view.QuestionPane;
import kph.jeopardy.view.ScoresPane;

public class Alex extends Application
{
	private static final List<Contestant> players = new ArrayList<>();
	private static int round = 1;
	private static BoardPane boardPane;
	private static ScoresPane scoresPane;
	private static final BorderPane gamePane = new BorderPane();

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Game game = createGame(players); // create game
		// create main screen
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setTitle("Jeopardy!");
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());

		boardPane = new BoardPane().init(game, round);
		gamePane.setCenter(boardPane);

		scoresPane = new ScoresPane().init(game);
		gamePane.setRight(scoresPane);

		Text title = new Text("Jeopardy! " + game.getGameDate());
		title.setFont(new Font(primaryScreenBounds.getMaxY() / 20));
		title.setFill(Color.WHITE);
		gamePane.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);

		Group root = new Group(gamePane);
		Scene scene = new Scene(root);
		scene.setFill(Color.DARKBLUE);
		primaryStage.setScene(scene);
		primaryStage.show();
		resizeCategories();

		// ACTION
		((Button) scoresPane.lookup("#nextround")).setOnAction(click ->
		{
			round++;
			if (round == 2)
			{
				boardPane = new BoardPane().init(game, round);
				gamePane.setCenter(boardPane);
				//resizeCategories();
			}
			if (round == 3)
			{
				((Button) scoresPane.lookup("#nextround")).setText("Finish game");
				GridPane qPane = new FinalJeopardyPane().init(game);
				gamePane.setCenter(qPane);
			} else if (round > 3)
			{
				primaryStage.hide();
			}
		});

		// ACTION
		((Button) scoresPane.lookup("#goback")).setOnAction(click ->
		{
			if (round != 3)
			{
				gamePane.setCenter(boardPane);
			}
		});

		// ACTION
		for (Contestant contestant : players)
		{
			Button up = (Button) scoresPane.lookup("#" + contestant.getName() + "-up");
			up.setOnAction(click ->
			{
				if (gamePane.getCenter().getClass().equals(QuestionPane.class))
				{
					QuestionPane qPane = (QuestionPane) gamePane.getCenter();
					if (qPane.checkDailyDouble())
					{
						int val = Integer.parseInt(((TextField) qPane.lookup("#value")).getText());
						contestant.changeScore(val);
						scoresPane.updateScores();
					}
					else
					{
						int val = Integer.parseInt(((Button) qPane.lookup("#value")).getText().replace("$", ""));
						contestant.changeScore(val);
						scoresPane.updateScores();	
					}
				} else if (gamePane.getCenter().getClass().equals(FinalJeopardyPane.class))
				{
					FinalJeopardyPane fPane = (FinalJeopardyPane) gamePane.getCenter();
					int val = 0;
					if (((TextField) fPane.lookup("#" + contestant.getName() + "-wager")) != null)
					{
						if (((TextField) fPane.lookup("#" + contestant.getName() + "-wager")).getText().equals(""))
						{
							val = 0;
						} else
						{
							val = Integer.parseInt(
									((TextField) fPane.lookup("#" + contestant.getName() + "-wager")).getText());

						}
					}
					contestant.changeScore(val);
					scoresPane.updateScores();
				}
			});

			Button down = (Button) scoresPane.lookup("#" + contestant.getName() + "-down");
			down.setOnAction(click ->
			{
				if (gamePane.getCenter().getClass().equals(QuestionPane.class))
				{
					QuestionPane qPane = (QuestionPane) gamePane.getCenter();
					if (qPane.checkDailyDouble())
					{
						int val = -Integer.parseInt(((TextField) qPane.lookup("#value")).getText());
						contestant.changeScore(val);
						scoresPane.updateScores();
					}
					else
					{
						int val = -Integer.parseInt(((Button) qPane.lookup("#value")).getText().replace("$", ""));
						contestant.changeScore(val);
						scoresPane.updateScores();	
					}
				} else if (gamePane.getCenter().getClass().equals(FinalJeopardyPane.class))
				{
					FinalJeopardyPane fPane = (FinalJeopardyPane) gamePane.getCenter();
					int val = 0;
					if (((TextField) fPane.lookup("#" + contestant.getName() + "-wager")) != null)
					{
						if (((TextField) fPane.lookup("#" + contestant.getName() + "-wager")).getText().equals(""))
						{
							val = 0;
						} else
						{
							val = -Integer.parseInt(
									((TextField) fPane.lookup("#" + contestant.getName() + "-wager")).getText());

						}
					}
					contestant.changeScore(val);
					scoresPane.updateScores();
				}
			});
		}
	}

	private void resizeCategories()
	{
		double maxHeight = 0;
		for (int i = 0; i < 6; i++)
		{
			Button test = (Button) getNodeByRowColumnIndex(0, i, boardPane);
			if (test.getHeight() > maxHeight)
			{
				maxHeight = test.getHeight();
			}
		}
		for (int i = 0; i < 6; i++)
		{
			Button test = (Button) getNodeByRowColumnIndex(0, i, boardPane);
			test.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: "
					+ Screen.getPrimary().getVisualBounds().getWidth() / 8
					+ "px; -fx-alignment: CENTER; -fx-pref-height: " + maxHeight + "px; -fx-text-alignment: center");
			test.setWrapText(true);
		}
	}

	public Game createGame(List<Contestant> playersList)
	{
		try
		{
			// get game number
			TextInputDialog gameNumDialog = new TextInputDialog();
			gameNumDialog.setTitle("Jeopardy!");
			gameNumDialog.setHeaderText("Select Game");
			gameNumDialog.setContentText(
					"Please enter a game number between 1 and 6672, with 1 being the oldest and 6672 being most recent:");
			Optional<String> gameNum = gameNumDialog.showAndWait();
			// create game object
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream fi = cl.getResourceAsStream("game" + gameNum.get() + ".dat");
			GZIPInputStream gi = new GZIPInputStream(fi);
			ObjectInputStream oi = new ObjectInputStream(gi);
			Game game = (Game) (oi.readObject());
			oi.close();
			gi.close();
			fi.close();
			TextInputDialog pCount = new TextInputDialog();
			pCount.setTitle("Jeopardy!");
			pCount.setHeaderText("Player Count");
			pCount.setContentText("Please enter the number of players:");
			Optional<String> playerCount = pCount.showAndWait();
			for (int i = 1; i <= Integer.parseInt(playerCount.get()); i++)
			{
				TextInputDialog playerName = new TextInputDialog();
				playerName.setTitle("Jeopardy!");
				playerName.setHeaderText("Player " + i + "'s Name");
				playerName.setContentText("Enter Player " + i + "'s name:");
				Optional<String> name = playerName.showAndWait();
				players.add(new Contestant(name.get(), 0));
			}
			for (Contestant player : playersList)
			{
				game.addPlayer(player);
			}
			return game;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane)
	{
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();

		for (Node node : childrens)
		{
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
			{
				result = node;
				break;
			}
		}

		return result;
	}
}
