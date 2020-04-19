package jeopardy;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Category implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private Set<Question> questions;

	public Category(String n)
	{
		this.name = n;
		this.questions = new TreeSet<>();
	}

	public void addQuestion(Question q)
	{
		this.questions.add(q);
	}

	public void removeQuestion(Question q)
	{
		this.questions.remove(q);
	}

	public String getName()
	{
		return name;
	}

	public Iterator<Question> getQuestions()
	{
		return questions.iterator();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Category [name=").append(name).append(", questions=\n");
		for (Question question : questions)
		{
			builder.append("\t").append(question).append("\n");
		} 
		builder.append("]");
		return builder.toString();
	}
}
