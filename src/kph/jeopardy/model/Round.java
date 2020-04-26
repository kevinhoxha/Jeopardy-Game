package kph.jeopardy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Round implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int round;
	private List<Category> categories;

	public Round(int r)
	{
		if (r < 1 || r > 3)
		{
			throw new IllegalArgumentException("Round number can only be 1, 2, or 3.");
		}
		this.round = r;
		this.categories = new ArrayList<>();
	}

	public void addCategory(Category c)
	{
		this.categories.add(c);
	}

	public void removeCategory(Category c)
	{
		this.categories.remove(c);
	}

	public int getRound()
	{
		return round;
	}

	public String getRoundName()
	{
		if (round == 1)
		{
			return "Jeopardy Round";
		} else if (round == 2)
		{
			return "Double Jeopardy Round";
		} else
		{
			return "Final Jeopardy Round";
		}
	}

	public Iterator<Category> getCategories()
	{
		return categories.iterator();
	}

	public Category getCategory(int i)
	{
		return this.categories.get(i);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Round [round=").append(round).append(", categories=\n");
		for (Category category : categories)
		{
			builder.append(category).append("\n");
		}
		builder.append("]");
		return builder.toString();
	}
}
