package kph.jeopardy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import kph.jeopardy.model.Category;
import kph.jeopardy.model.Game;
import kph.jeopardy.model.Question;
import kph.jeopardy.model.Round;

public class JeopardyHarvester
{
	private static List<String> dates = new ArrayList<>();
	private static int numGames = 7605;
	private static int numDone = 0;
	
	public static void main(String[] args)
	{
		ExecutorService executor = Executors.newFixedThreadPool(100);
		for (int i = 1; i <= numGames; i++)
		{
			executor.submit(createGame(i));
		}
		executor.shutdown();
	}

	static Callable<Boolean> createGame(int gameNum)
	{
		return () ->
		{
			try
			{
				final String qUrl = "http://www.j-archive.com/showgame.php?game_id=" + gameNum;
				final String aUrl = "http://www.j-archive.com/showgameresponses.php?game_id=" + gameNum;
				Game currentGame = new Game();
				final Document docQ = Jsoup.connect(qUrl).get();
				final Document docA = Jsoup.connect(aUrl).get();
				currentGame.setGameDate(docQ.select("#game_title").text());
				Round jeopardyRound = new Round(1);
				Round doubleJeopardyRound = new Round(2);
				Round finalJeopardyRound = new Round(3);
				for (Element div : docQ.select("div#jeopardy_round"))
				{
					for (int i = 1; i <= 6; i++)
					{
						Category c = new Category(div.select("td.category:nth-of-type(" + i + ")").text());
						;
						for (int k = 1; k <= 6; k++)
						{
							if (k != 1)
							{
								if (div.select("tr:nth-of-type(" + k + ")").select("td.clue:nth-of-type(" + i + ")")
										.text().equals(""))
								{
									c.addQuestion(new Question("", "", (k - 1) * 200, false));
								} else if (div.select("tr:nth-of-type(" + k + ")")
										.select("td.clue:nth-of-type(" + i + ")").select(".clue_value_daily_double")
										.text().equals(""))
								{
									String body = div.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".clue_text").text();
									String answer = docA.select("div#jeopardy_round")
											.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".correct_response")
											.text();
									int value = (k - 1) * 200;
									boolean dd = false;
									c.addQuestion(new Question(body, answer, value, dd));
								} else
								{
									String body = div.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".clue_text").text();
									String answer = docA.select("div#jeopardy_round")
											.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".correct_response")
											.text();
									int value = (k - 1) * 200;
									boolean dd = true;
									c.addQuestion(new Question(body, answer, value, dd));
								}
							}
						}
						jeopardyRound.addCategory(c);
					}
				}
				for (Element div : docQ.select("div#double_jeopardy_round"))
				{
					for (int i = 1; i <= 6; i++)
					{
						Category c = new Category(div.select("td.category:nth-of-type(" + i + ")").text());
						;
						for (int k = 1; k <= 6; k++)
						{
							if (k != 1)
							{
								if (div.select("tr:nth-of-type(" + k + ")").select("td.clue:nth-of-type(" + i + ")")
										.text().equals(""))
								{
									c.addQuestion(new Question("", "", (k - 1) * 400, false));
								} else if (div.select("tr:nth-of-type(" + k + ")")
										.select("td.clue:nth-of-type(" + i + ")").select(".clue_value_daily_double")
										.text().equals(""))
								{
									String body = div.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".clue_text").text();
									String answer = docA.select("div#double_jeopardy_round")
											.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".correct_response")
											.text();
									int value = (k - 1) * 400;
									boolean dd = false;
									c.addQuestion(new Question(body, answer, value, dd));
								} else
								{
									String body = div.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".clue_text").text();
									String answer = docA.select("div#double_jeopardy_round")
											.select("tr:nth-of-type(" + k + ")")
											.select("td.clue:nth-of-type(" + i + ")").select(".correct_response")
											.text();
									int value = (k - 1) * 400;
									boolean dd = true;
									c.addQuestion(new Question(body, answer, value, dd));
								}
							}
						}
						doubleJeopardyRound.addCategory(c);
					}
				}
				for (Element div : docQ.select("div#final_jeopardy_round"))
				{
					Category c = new Category(div.select(".category_name").text());
					String body = div.select(".clue_text").text();
					String answer = docA.select("div#final_jeopardy_round").select(".correct_response").text();
					int value = 0;
					boolean dd = false;
					c.addQuestion(new Question(body, answer, value, dd));
					finalJeopardyRound.addCategory(c);
				}
				currentGame.setJeopardy(jeopardyRound);
				currentGame.setDoubleJeopardy(doubleJeopardyRound);
				currentGame.setFinalJeopardy(finalJeopardyRound);
				FileOutputStream f = new FileOutputStream(
						new File("C:\\MyGithub\\Jeopardy-Game\\resources\\game" + gameNum + ".dat"));
				GZIPOutputStream g = new GZIPOutputStream(f);
				ObjectOutputStream o = new ObjectOutputStream(g);
				o.writeObject(currentGame);
				o.close();
				g.close();
				f.close();
				dates.add(currentGame.getDay()[2] + "-" + currentGame.getDay()[0] + "-" + currentGame.getDay()[1] + " " + gameNum);
				numDone++;
				if (gameNum % 100 == 0)
				{
					System.out.println(gameNum);
				}
				if (numDone >= numGames)
				{
					try
					{
						FileWriter fw = new FileWriter("C:\\MyGithub\\Jeopardy-Game\\resources\\game_dates.dat");
						for (String date: dates)
						{
							fw.write(date + "\n");
						}
						fw.close();
					}
					catch (Exception ex)
					{
						System.out.println(ex);
					}
				}
				return true;
			} catch (Exception e)
			{
				System.out.println(gameNum + ": " + e.getMessage());
				numDone++;
				return false;
			}
		};
	}
}
