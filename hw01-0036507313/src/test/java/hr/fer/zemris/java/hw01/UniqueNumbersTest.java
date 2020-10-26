package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;

import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

import org.junit.jupiter.api.Test;

class UniqueNumbersTest {

	//Tests to see if values are properly added to the tree
	//and read from the tree
	@Test
	void test1() {
		TreeNode root = null;
		root = UniqueNumbers.addNode(root, 1);
		assertTrue(root.value == 1);
	}
	
	@Test
	void test2() {
		TreeNode root = null;
		root = new TreeNode();
		root.value = 1;
		root.left = new TreeNode();
		root.left.value = 0;
		root.right = new TreeNode();
		root.right.value = 2;
		assertEquals(3, UniqueNumbers.treeSize(root));
	}
	
	@Test
	void test3() {
		TreeNode root = null;
		root = new TreeNode();
		root.value = 2;
		root.left = new TreeNode();
		root.left.value = 1;
		root.right = new TreeNode();
		root.right.value = 3;
		assertTrue(UniqueNumbers.containsValue(root, 2));
		assertFalse(UniqueNumbers.containsValue(root, 4));
	}
}
