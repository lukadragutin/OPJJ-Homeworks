package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {
	

	@Test
	void less() {
		var less = ComparisonOperators.LESS;
		String s1 = "Ivo";
		String s2 = "Ivona";
		assertTrue(less.satisfied(s1, s2));
		assertFalse(less.satisfied(s2, s1));
	}

	@Test
	void lessOrEquals() {
		var lessOrEquals = ComparisonOperators.LESS_OR_EQUALS;
		var s1 = "Ivo";
		var s2 = "Matija";
		var s3 = "Matija";
		assertTrue(lessOrEquals.satisfied(s1, s2));
		assertTrue(lessOrEquals.satisfied(s2, s3));
		assertFalse(lessOrEquals.satisfied(s3, s1));
	}

	@Test
	void equals() {
		var equals = ComparisonOperators.EQUALS;
		var s1 = "Ivo";
		var s2 = "Matija";
		var s3 = "Matija";
		assertFalse(equals.satisfied(s1, s2));
		assertTrue(equals.satisfied(s2, s3));
		assertFalse(equals.satisfied(s3, s1));
	}

	@Test
	void greater() {
		var greater = ComparisonOperators.GREATER;
		var s1 = "Ivo";
		var s2 = "Matija";
		assertFalse(greater.satisfied(s1, s2));
		assertTrue(greater.satisfied(s2, s1));
	}

	@Test
	void greaterOrEquals() {
		var greaterOrEquals = ComparisonOperators.GREATER_OR_EQUALS;
		var s1 = "Ivo";
		var s2 = "Matija";
		var s3 = "Ivo";
		assertFalse(greaterOrEquals.satisfied(s1, s2));
		assertTrue(greaterOrEquals.satisfied(s2, s1));
		assertTrue(greaterOrEquals.satisfied(s1, s3));
	}

	@Test
	void notEquals() {
		var notEquals = ComparisonOperators.NOT_EQUALS;
		var s1 = "Ivo";
		var s2 = "Matija";
		var s3 = "Matija";
		assertTrue(notEquals.satisfied(s1, s2));
		assertTrue(notEquals.satisfied(s2, s1));
		assertFalse(notEquals.satisfied(s2, s3));
		assertFalse(notEquals.satisfied(s3, s2));
		assertTrue(notEquals.satisfied(s3, s1));
	}

	@Test
	void likeWrongFormat() {
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("Maedvjed", "Me*vj*"));
	}

	@Test
	void likeWithoutWildcard() {
		var like = ComparisonOperators.LIKE;
		var s1 = "Zagrebački";
		assertTrue(like.satisfied(s1, "Zagreb"));
		assertFalse(like.satisfied(s1, "Zdrav"));
	}

	@Test
	void likeWithWildcardInMiddle() {
		var like = ComparisonOperators.LIKE;
		var s1 = "AAA";
		var s2 = "AAAA";
		assertFalse(like.satisfied(s1, "AA*AA"));
		assertTrue(like.satisfied(s2, "AA*AA"));
	}

	@Test
	void likeWithWildcardAtTheBeggining() {
		var like = ComparisonOperators.LIKE;
		var s1 = "Zagrebački";
		assertTrue(like.satisfied(s1, "*ebački"));
		assertFalse(like.satisfied(s1, "*j"));
	}
	
	@Test
	void likeWithWildcardAtTheEnd() {
		var like = ComparisonOperators.LIKE;
		var s1 = "AAABBB";
		var s2 = "AABBBB";
		var s3 = "ABBBB";
		assertTrue(like.satisfied(s1, "AA*"));
		assertTrue(like.satisfied(s2, "AA*"));		
		assertFalse(like.satisfied(s3, "AA*"));
	}

}
