package kph.jeopardy.view;

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
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import kph.jeopardy.model.Contestant;
import kph.jeopardy.model.Game;

public class GUI extends Application
{

	private int increment = 0;
	private int[] increments;
	private	int round = 1;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			Game game = createGame(getPlayers()); // create game
			//create main screen
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setTitle("Jeopardy!");
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			//create border and grid pane
			BorderPane gamePane = new BorderPane();
			GridPane roundPane = new GridPane();
			GridPane rightPane = new GridPane();
			//add buttons to grid pane
			for (int i = 0; i < 6; i++)
			{
				for (int k = 0; k < 6; k++)
				{
					if (k == 0)
					{
						Button b = new Button(game.getCategory(1,  i).getName());
						b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: 250px; -fx-alignment: CENTER; -fx-text-alignment: center");
						b.setWrapText(true);
						b.setEffect(new DropShadow());
						roundPane.add(b, i, k);
					} 
					else
					{
						Button b = new Button("$" + Integer.toString(k * 200));
						b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-pref-width: 250px");
						b.setEffect(new DropShadow());
						final int newK = k;
						final int newI = i;
						b.setOnAction(click -> {
							try
							{
								if (!b.getText().equals("✓"))
								{
									b.setText("✓");
									b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 5em; -fx-pref-width: 250px");
									GridPane questionPane = new GridPane();
									Button categoryB = new Button(game.getCategory(1, newI).getName());
									categoryB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
									categoryB.setWrapText(true);
									Button answerB = new Button("Click to show answer");
									answerB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
									answerB.setWrapText(true);
									answerB.setOnAction(click2 -> {
										answerB.setText(game.getQuestion(1, newK, newI).getAnswer());
									});
									Button questionB = new Button(game.getQuestion(1, newK, newI).getBody());
									questionB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-pref-width: 1525px; -fx-pref-height: " + (roundPane.getHeight())+ "px; -fx-alignment: CENTER; -fx-text-alignment: center");
									questionB.setWrapText(true);
									questionPane.add(categoryB, 0, 0);
									questionPane.add(questionB, 0, 1);
									questionPane.add(answerB, 0, 2);
									questionPane.setHgap(5);
									questionPane.setVgap(5);
									gamePane.setCenter(questionPane);
									increment = game.getQuestion(1, newK, newI).getValue();
								}
							}
							catch(Exception e)
							{
								System.out.println(e.getMessage());
							}
						});
						roundPane.add(b, i, k);
					}
				}
			}
			roundPane.setHgap(5);
			roundPane.setVgap(5);
			int count = 0;
			for (Contestant p: game.getPlayers())
			{
				Text players = new Text(p.getName() + ": $" + p.getScore());
				final int newCount = count;
				Button upB = new Button("+");
				upB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center; -fx-pref-width: 25px");
				upB.setOnAction(click -> {
					if (round == 3)
					{
						p.changeScore(increments[game.getPlayers().indexOf(p)]);
						Text newText = (Text)getNodeByRowColumnIndex(newCount, 0, rightPane);
						newText.setText(p.getName() + ": $" + p.getScore());	
					}
					else
					{
						p.changeScore(increment);
						Text newText = (Text)getNodeByRowColumnIndex(newCount, 0, rightPane);
						newText.setText(p.getName() + ": $" + p.getScore());	
					}
				});
				Button downB = new Button("-");
				downB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center; -fx-pref-width: 25px");
				downB.setOnAction(click -> {
					if (round == 3)
					{
						p.changeScore(-1 * increments[game.getPlayers().indexOf(p)]);
						Text newText = (Text)getNodeByRowColumnIndex(newCount, 0, rightPane);
						newText.setText(p.getName() + ": $" + p.getScore());
					}
					else
					{
						p.changeScore(-1 * increment);
						Text newText = (Text)getNodeByRowColumnIndex(newCount, 0, rightPane);
						newText.setText(p.getName() + ": $" + p.getScore());	
					}
				});
				players.setFont(new Font(40));
				players.setFill(Color.WHITE);
				rightPane.add(players, 0, count);
				rightPane.add(upB, 1, count);
				rightPane.add(downB, 2, count);
				rightPane.setVgap(5);
				rightPane.setHgap(5);
				count++;
			}
			GridPane round2Pane = new GridPane();
			Button backButton = new Button("Back to Board");
			backButton.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center");
			backButton.setOnAction(click -> {
				if (round == 1)
				{
					gamePane.setCenter(roundPane);
				}
				else if (round == 2)
				{
					gamePane.setCenter(round2Pane);
				}
				increment = 0;
			});
			Button nextRound = new Button("Move to next round");
			nextRound.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-alignment: CENTER; -fx-text-alignment: center; -fx-font-size: 3em");
			nextRound.setOnAction(click -> {
				round += 1;
				if (round == 2)
				{
					for (int i = 0; i < 6; i++)
					{
						for (int k = 0; k < 6; k++)
						{
							if (k == 0)
							{
								Button b = new Button(game.getCategory(2,  i).getName());
								b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: 250px; -fx-alignment: CENTER; -fx-text-alignment: center");
								b.setWrapText(true);
								b.setEffect(new DropShadow());
								round2Pane.add(b, i, k);
							} 
							else
							{
								Button b = new Button("$" + Integer.toString(k * 400));
								b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-pref-width: 250px");
								b.setEffect(new DropShadow());
								final int newK = k;
								final int newI = i;
								b.setOnAction(click2 -> {
									try
									{
										if (!b.getText().equals("✓"))
										{
											b.setText("✓");
											b.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 5em; -fx-pref-width: 250px");
											GridPane questionPane = new GridPane();
											Button categoryB = new Button(game.getCategory(2, newI).getName());
											categoryB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
											categoryB.setWrapText(true);
											Button answerB = new Button("Click to show answer");
											answerB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
											answerB.setWrapText(true);
											answerB.setOnAction(click3 -> {
												answerB.setText(game.getQuestion(2, newK, newI).getAnswer());
											});
											Button questionB = new Button(game.getQuestion(2, newK, newI).getBody());
											questionB.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-pref-width: 1525px; -fx-pref-height: " + (roundPane.getHeight())+ "px; -fx-alignment: CENTER; -fx-text-alignment: center");
											questionB.setWrapText(true);
											questionPane.add(categoryB, 0, 0);
											questionPane.add(questionB, 0, 1);
											questionPane.add(answerB, 0, 2);
											questionPane.setHgap(5);
											questionPane.setVgap(5);
											gamePane.setCenter(questionPane);
											increment = game.getQuestion(2, newK, newI).getValue();
										}
									}
									catch(Exception e)
									{
										System.out.println(e.getMessage());
									}
								});
								round2Pane.add(b, i, k);
							}
						}
					}
					round2Pane.setHgap(5);
					round2Pane.setVgap(5);
					gamePane.setCenter(round2Pane);	
				}
				else if (round == 3)
				{
					for (int i = 0; i < increments.length; i++)
					{
						try
						{
							if (game.getPlayers().get(i).getScore() > 0)
							{
								TextInputDialog dialog = new TextInputDialog();
								dialog.setTitle("Jeopardy!");
								dialog.setHeaderText("Final Jeopardy Category: " + game.getFinalJeopardy().getCategory(0).getName());
								dialog.setContentText("Please enter " + game.getPlayers().get(i).getName() + "'s wager (must be less than $" + game.getPlayers().get(i).getScore() + "):");
								Optional<String> result = dialog.showAndWait();
								increments[i] = Integer.parseInt(result.get());
							}
							else
							{
								increments[i] = 0;
							}
						}
						catch (Exception e)
						{
							System.out.println(e.getMessage());
						}
					}
					GridPane round3Pane = new GridPane();
					Button finalCat = new Button(game.getFinalJeopardy().getCategory(0).getName());
					finalCat.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
					finalCat.setWrapText(true);
					Button finalQ = new Button(game.getFinalJeopardy().getCategory(0).getQuestions().next().getBody());
					finalQ.setStyle("-fx-background-color: #0000FF; -fx-text-fill: gold; -fx-font-size: 5em; -fx-pref-width: 1525px; -fx-pref-height: " + (round2Pane.getHeight())+ "px; -fx-alignment: CENTER; -fx-text-alignment: center");
					finalQ.setWrapText(true);
					Button finalA = new Button("Click to show answer");
					finalA.setStyle("-fx-background-color: #0000FF; -fx-text-fill: #66ff00; -fx-font-size: 4em; -fx-pref-width: 1525px; -fx-alignment: CENTER; -fx-text-alignment: center");
					finalA.setWrapText(true);
					finalA.setOnAction(click2 -> {
						finalA.setText(game.getFinalJeopardy().getCategory(0).getQuestions().next().getAnswer());
					});
					round3Pane.add(finalCat, 0, 0);
					round3Pane.add(finalQ, 0, 1);
					round3Pane.add(finalA, 0, 2);
					round3Pane.setHgap(5);
					round3Pane.setVgap(5);
					gamePane.setCenter(round3Pane);
					nextRound.setText("Finish Game");
				}
				else if (round == 4)
				{
					primaryStage.hide();
				}
			});
			rightPane.add(backButton, 0, count);
			gamePane.setCenter(roundPane);
			gamePane.setRight(rightPane);
			gamePane.setBottom(nextRound);
			Text t = new Text("Jeopardy!");
			t.setFont(new Font(primaryScreenBounds.getMaxY() / 20));
			t.setFill(Color.WHITE);
			t.setStyle("");
			gamePane.setTop(t);
			BorderPane.setAlignment(t, Pos.CENTER);
			Group root = new Group(gamePane);
			Scene scene = new Scene(root);
			scene.setFill(Color.DARKBLUE);
			primaryStage.setScene(scene);
			primaryStage.show();
			double maxHeight = 0;
			for(int i = 0; i < 6; i++)
			{
				Button test = (Button) getNodeByRowColumnIndex(0, i, roundPane);
				if (test.getHeight() > maxHeight)
				{
					maxHeight = test.getHeight();
				}
			}
			for (int i = 0; i < 6; i++)
			{
				Button test = (Button) getNodeByRowColumnIndex(0, i, roundPane);
				test.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white; -fx-font-size: 2em; -fx-pref-width: 250px; -fx-alignment: CENTER; -fx-pref-height: " + maxHeight + "px; -fx-text-alignment: center");
			}
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

	public ArrayList<Contestant> getPlayers()
	{
		// get player count
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Jeopardy!");
		dialog.setHeaderText("Player Count");
		dialog.setContentText("Please enter player count:");
		Optional<String> result = dialog.showAndWait();
		int playerCount = Integer.parseInt(result.get());
		// get player names
		ArrayList<Contestant> playersList = new ArrayList<>();
		for (int i = 1; i <= playerCount; i++)
		{
			TextInputDialog nameDialog = new TextInputDialog();
			nameDialog.setTitle("Jeopardy!");
			nameDialog.setHeaderText("Player " + i + "'s Name");
			nameDialog.setContentText("Please enter player " + i + "'s name:");
			Optional<String> name = nameDialog.showAndWait();
			playersList.add(new Contestant(name.get(), 0));
		}
		return playersList;
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
			increments = new int[playersList.size()];
			return game;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
	    Node result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
	}
}
