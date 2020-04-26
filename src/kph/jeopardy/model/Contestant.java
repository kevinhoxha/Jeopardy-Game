package kph.jeopardy.model;

import java.io.Serializable;

public class Contestant implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private int score;

	public Contestant(String n, int s)
	{
		this.name = n;
		this.score = s;
	}

	public String getName()
	{
		return name;
	}

	public int getScore()
	{
		return score;
	}

	public void changeScore(int increment)
	{
		this.score += increment;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Contestant [name=").append(name).append(", score=").append(score).append("]");
		return builder.toString();
	}
}
