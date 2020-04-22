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
	
	public Category getCategory(int round, int column)
	{
		Round r;
		if (round == 1)
		{
			r = this.jeopardy;
		}
		else if (round == 2)
		{
			r = this.doubleJeopardy;
		}
		else
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
		}
		else if (round == 2)
		{
			r = this.doubleJeopardy;
		}
		else
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
				.append(", finalJeopardy=").append(finalJeopardy).append(", players=").append(players).append("]");
		return builder.toString();
	}
}
