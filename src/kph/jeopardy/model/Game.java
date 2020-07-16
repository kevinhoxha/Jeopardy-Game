package kph.jeopardy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Round jeopardy;
	private Round doubleJeopardy;
	private Round finalJeopardy;
	private List<Contestant> players = new ArrayList<>();
	private String gameDate;

	public Round getJeopardy()
	{
		return this.jeopardy;
	}

	public void setJeopardy(Round jeopardy)
	{
		this.jeopardy = jeopardy;
	}

	public Round getDoubleJeopardy()
	{
		return this.doubleJeopardy;
	}

	public void setDoubleJeopardy(Round doubleJeopardy)
	{
		this.doubleJeopardy = doubleJeopardy;
	}

	public Round getFinalJeopardy()
	{
		return this.finalJeopardy;
	}

	public void setFinalJeopardy(Round finalJeopardy)
	{
		this.finalJeopardy = finalJeopardy;
	}

	public void addPlayer(Contestant p)
	{
		this.players.add(p);
	}

	public void removePlayer(Contestant p)
	{
		this.players.remove(p);
	}

	public List<Contestant> getPlayers()
	{
		return players;
	}
	
	public void setGameDate(String g)
	{
		this.gameDate = g;
	}
	
	public String getGameDate()
	{
		return this.gameDate;
	}
	
	public int[] getDay()
	{
		int[] day = new int[3];
		String[] date1 = gameDate.replaceAll(",", "").split("-");
		String[] date = date1[1].split(" ");
		if (date[2].equals("January"))
		{
			day[0] = 1;
		}
		else if (date[2].equals("February"))
		{
			day[0] = 2;
		}
		else if (date[2].equals("March"))
		{
			day[0] = 3;
		}
		else if (date[2].equals("April"))
		{
			day[0] = 4;
		}
		else if (date[2].equals("May"))
		{
			day[0] = 5;
		}
		else if (date[2].equals("June"))
		{
			day[0] = 6;
		}
		else if (date[2].equals("July"))
		{
			day[0] = 7;
		}
		else if (date[2].equals("August"))
		{
			day[0] = 8;
		}
		else if (date[2].equals("September"))
		{
			day[0] = 9;
		}
		else if (date[2].equals("October"))
		{
			day[0] = 10;
		}
		else if (date[2].equals("November"))
		{
			day[0] = 11;
		}
		else if (date[2].equals("December"))
		{
			day[0] = 12;
		}
		day[1] = Integer.parseInt(date[3]);
		day[2] = Integer.parseInt(date[4]);
		return day;
	}

	public Category getCategory(int round, int column)
	{
		Round r;
		if (round == 1)
		{
			r = this.jeopardy;
		} else if (round == 2)
		{
			r = this.doubleJeopardy;
		} else
		{
			r = this.finalJeopardy;
		}
		return r.getCategory(column);
	}

	public Question getQuestion(int round, int row, int column)
	{
		Round r;
		if (round == 1)
		{
			r = this.jeopardy;
		} else if (round == 2)
		{
			r = this.doubleJeopardy;
		} else
		{
			r = this.finalJeopardy;
		}
		return r.getCategory(column).getQuestion(row);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Game [jeopardy=").append(jeopardy).append(", doubleJeopardy=").append(doubleJeopardy)
				.append(", finalJeopardy=").append(finalJeopardy).append(", players=").append(players).append(", gameDate=").append(gameDate).append("]");
		return builder.toString();
	}
}
