package jeopardy;

import java.io.Serializable;

public class Question implements Comparable<Question>, Serializable
{
	private static final long serialVersionUID = 1L;
	private String body;
	private String answer;
	private int value;
	private boolean isDailyDouble;

	public Question(String body, String answer, int value, boolean isDailyDouble)
	{
		super();
		this.body = body;
		this.answer = answer;
		this.value = value;
		this.isDailyDouble = isDailyDouble;
	}

	public String getBody()
	{
		return body;
	}

	public String getAnswer()
	{
		return answer;
	}

	public int getValue()
	{
		return value;
	}

	public boolean isDailyDouble()
	{
		return isDailyDouble;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Question [body=").append(body).append(", answer=").append(answer).append(", value=")
				.append(value).append(", isDailyDouble=").append(isDailyDouble).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + (isDailyDouble ? 1231 : 1237);
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (answer == null)
		{
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (body == null)
		{
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (isDailyDouble != other.isDailyDouble)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public int compareTo(Question o)
	{
		return this.value - o.value;
	}
	
	
}
