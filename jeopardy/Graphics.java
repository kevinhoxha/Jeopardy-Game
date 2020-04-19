package jeopardy;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Graphics extends Application
{

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
			BorderPane border = new BorderPane();
			GridPane questions = new GridPane();
			//add buttons to grid pane
			for (int i = 0; i < 6; i++)
			{
				for (int k = 0; k < 6; k++)
				{
					if (k == 0)
					{
						//questions.add(new Rectangle(primaryScreenBounds.getMaxX() * 2 / 15,
						//		primaryScreenBounds.getMaxY() * 2 / 20, Color.DARKBLUE), i, k);
						Text t = new Text(getListFromIterator(game.getJeopardy().getCategories()).get(i).getName());
						t.setFont(new Font(25));
						questions.add(t, i, k);
					} 
					else
					{
						//questions.add(new Rectangle(primaryScreenBounds.getMaxX() * 2 / 15,
							//	primaryScreenBounds.getMaxY() * 16 / 20 / 5, Color.BLUE), i, k);
						Button b = new Button("$" + Integer.toString(k * 200));
						final int newK = k;
						final int newI = i;
						b.setOnAction(click -> {
							List<String> choices = new ArrayList<>();
							for (Contestant p: game.getPlayers())
							{
								choices.add(p.getName());
							}
							choices.add("No one");
							ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose player", choices);
							dialog.setTitle("Jeopardy!");
							dialog.setHeaderText("$" + newK * 200 + " Question");
							dialog.setContentText(getListFromIterator(getListFromIterator(game.getJeopardy().getCategories()).get(newI).getQuestions()).get(newK - 1).getBody() + "\nWho will answer?");
							Optional<String> result = dialog.showAndWait();
						});
						questions.add(b, i, k);
					}
				}
			}
			questions.setGridLinesVisible(true);
			questions.setHgap(15);
			questions.setVgap(15);
			//Rectangle players = new Rectangle(primaryScreenBounds.getMaxX() / 5, primaryScreenBounds.getMaxY(),
			//		Color.DARKBLUE);
			String txt = "";
			for (Contestant p: game.getPlayers())
			{
				txt += p.getName() + ": $" + p.getScore() + "\n";
			}
			Text players = new Text(txt);
			players.setFont(new Font(40));
			border.setCenter(questions);
			border.setRight(players);
			Text t = new Text("Jeopardy!");
			t.setFont(new Font(primaryScreenBounds.getMaxY() / 20));
			border.setTop(t);
			Group root = new Group(border);
			Scene scene = new Scene(root);
			// scene.setFill(Color.DARKBLUE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e)
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
			FileInputStream fi = new FileInputStream(
					"C:\\eclipse-workspace\\jeopardy\\src\\games\\game" + gameNum.get() + ".txt");
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
			System.out.println(e.getMessage());
			return new Game();
		}
	}
	
	public static <T> List<T> 
    getListFromIterator(Iterator<T> iterator) 
    { 
  
        // Convert iterator to iterable 
        Iterable<T> iterable = () -> iterator; 
  
        // Create a List from the Iterable 
        List<T> list = StreamSupport 
                           .stream(iterable.spliterator(), false) 
                           .collect(Collectors.toList()); 
  
        // Return the List 
        return list; 
    } 
}
