package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilTest {

	@SuppressWarnings("unused")
	@Test
	void hexToByte() {
		var bytes = Util.hextobyte("01aE22");
		var expected = new byte[] {1, -82, 34};
		for(int i = 0; i < bytes.length; i++) {
			assertEquals(expected[i], bytes[i]);
		}
	}
	
	@Test
	void hexToByteEmpty() {
		var bytes = Util.hextobyte("");
		assertTrue(bytes.length == 0);
	}
	
	@Test
	void hexToByteIllegal() {
		assertThrows(IllegalArgumentException.class, ()-> Util.hextobyte("01Ae2G"));
		assertThrows(IllegalArgumentException.class, ()-> Util.hextobyte("01Ae2"));
	}
	
	@Test
	void testByteToHex() {
		String hex = Util.bytetohex(new byte[] {1, -82, 34});
		String expected = "01ae22";
		assertEquals(expected, hex);
	}
	
	@Test
	void testByteToHexCase() {
		String hex = Util.bytetohex(new byte[] {1, -82, 34});
		String unExpected1 = "01AE22";
		String unExpected2 = "01aE22";
		assertNotEquals(unExpected1, hex);
		assertNotEquals(unExpected2, hex);
		assertEquals(unExpected1.toLowerCase(), hex);
		assertEquals(unExpected2.toLowerCase(), hex);
	}
	
	@Test
	void byteToHexEmpty() {
		assertEquals("", Util.bytetohex(new byte[0]));
	}

}
