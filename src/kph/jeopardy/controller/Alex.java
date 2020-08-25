package kph.jeopardy.controller;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
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
	private Map<LocalDate, Integer> gameDays = new HashMap<>();

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		// get game number
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
				resizeCategories();
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
		for (int i = 0; i < 6; i++)
		{
			Button test = (Button) getNodeByRowColumnIndex(0, i, boardPane);
			test.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: "
					+ Screen.getPrimary().getVisualBounds().getWidth() / 8
					+ "px; -fx-alignment: CENTER; -fx-max-height: 1234567px; -fx-text-alignment: center");
			test.setWrapText(true);
		}
	}

	public Game createGame(List<Contestant> playersList)
	{
		try
		{
			Dialog<String> dialog = new Dialog<>();
			dialog.setTitle("Jeopardy!");
			dialog.setHeaderText("Select game");
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			try
			{
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				Scanner gameReader = new Scanner(cl.getResourceAsStream("game_dates.dat"));
				while (gameReader.hasNextLine())
				{
					String[] str = gameReader.nextLine().split(" ");
					String[] l = str[0].split("-");
					gameDays.put(LocalDate.of(Integer.parseInt(l[0]), Integer.parseInt(l[1]), Integer.parseInt(l[2])), Integer.parseInt(str[1]));
				}
				gameReader.close();
			}
			catch (Exception w)
			{
				System.out.println(w.getMessage());
			}
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			DatePicker d = new DatePicker();
			d.setDayCellFactory(new Callback<DatePicker, DateCell>() {
				@Override
				public DateCell call(DatePicker param) {
					return new DateCell(){
						@Override
						public void updateItem(LocalDate item, boolean empty) {
							super.updateItem(item, empty);

							if (!empty && item != null) {
								if(gameDays.keySet().contains(item)) {
									this.setStyle("-fx-background-color: lightblue");
								}
							}
						}
					};
				}
			});
			grid.add(new Text("Pick the air date of the Jeopardy! game you would like to play. The days highlighted in blue have a corresponding game."), 0, 0);
			grid.add(d, 0, 1);
			dialog.getDialogPane().setContent(grid);
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == ButtonType.OK) {
			        return d.getValue().toString();
			    }
			    return null;
			});
			Optional<String> gameDate = dialog.showAndWait();
			int gameNum = gameDays.get(LocalDate.of(Integer.parseInt(gameDate.get().split("-")[0]), Integer.parseInt(gameDate.get().split("-")[1]), Integer.parseInt(gameDate.get().split("-")[2])));
			
			// create game object
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream fi = cl.getResourceAsStream("game" + gameNum + ".dat");
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
			if (playerCount.get().equals("*"))
			{
				players.add(new Contestant("Kevin", 0));
				players.add(new Contestant("Kaitlyn", 0));
				players.add(new Contestant("Mom", 0));
				players.add(new Contestant("Dad", 0));
			}
			else
			{
				for (int i = 1; i <= Integer.parseInt(playerCount.get()); i++)
				{
					TextInputDialog playerName = new TextInputDialog();
					playerName.setTitle("Jeopardy!");
					playerName.setHeaderText("Player " + i + "'s Name");
					playerName.setContentText("Enter Player " + i + "'s name:");
					Optional<String> name = playerName.showAndWait();
					players.add(new Contestant(name.get(), 0));
				}
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
