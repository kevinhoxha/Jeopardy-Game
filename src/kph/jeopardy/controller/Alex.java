package kph.jeopardy.controller;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import kph.jeopardy.view.QuestionPane;
import kph.jeopardy.view.ScoresPane;

public class Alex extends Application {
	private static final List<Contestant> players = new ArrayList<>();
	private static int round = 1;
	private static BoardPane boardPane;
	private static ScoresPane scoresPane;
	private static final BorderPane gamePane = new BorderPane();
	
	static {
		players.add(new Contestant("Kevin", 0));
		players.add(new Contestant("Kaitlyn", 0));
		players.add(new Contestant("Mom", 0));
		players.add(new Contestant("Dad", 0));
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Game game = createGame(players); // create game
		//create main screen
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
		
		Text title = new Text("Jeopardy!");
		title.setFont(new Font(primaryScreenBounds.getMaxY() / 20));
		title.setFill(Color.WHITE);
		gamePane.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);
		
		Group root = new Group(gamePane);
		Scene scene = new Scene(root);
		scene.setFill(Color.DARKBLUE);
		primaryStage.setScene(scene);
		primaryStage.show();

		//ACTION
		((Button)scoresPane.lookup("#nextround")).setOnAction(click -> {
			round++;
			if (round == 2) {
				boardPane = new BoardPane().init(game, round);
				gamePane.setCenter(boardPane);
			}
			if (round == 3) {
				((Button) scoresPane.lookup("#nextround")).setText("Finish game");
				GridPane qPane = new QuestionPane().init(game, round, 0, 0);
				gamePane.setCenter(qPane);
			} else if (round > 3) {
				primaryStage.hide();
			}
		});

		//ACTION
		((Button)scoresPane.lookup("#goback")).setOnAction(click -> {
			gamePane.setCenter(boardPane);
		});
		
		//ACTION
		for (Contestant contestant : players) {
			Button up = (Button)scoresPane.lookup("#" + contestant.getName() + "-up");
			up.setOnAction(click -> {
				if (gamePane.getCenter().getClass().equals(QuestionPane.class)) {
					QuestionPane qPane = (QuestionPane) gamePane.getCenter();
					int val = Integer.parseInt(((Button)qPane.lookup("#value")).getText().replace("$", ""));
					contestant.changeScore(val);
					scoresPane.updateScores();
				}
			});

			Button down = (Button)scoresPane.lookup("#" + contestant.getName() + "-down");
			down.setOnAction(click -> {
				if (gamePane.getCenter().getClass().equals(QuestionPane.class)) {
					QuestionPane qPane = (QuestionPane) gamePane.getCenter();
					int val = - Integer.parseInt(((Button)qPane.lookup("#value")).getText().replace("$", ""));
					contestant.changeScore(val);
					scoresPane.updateScores();
				}
			});
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
					"Please enter a game number between 1 and 6605, with 1 being the oldest and 6605 being most recent:");
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
			for (Contestant player : playersList)
			{
				game.addPlayer(player);
			}
			return game;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
