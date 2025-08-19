import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

class ResultReaderTest {

	@Test
	void testResultReader() {
		ResultReader rr = new ResultReader(new File("data/results.xml"));
		
		assertEquals(3, rr.getSeasons().size());
		
		Season sea = rr.getSeasons().get(0);
		
		assertEquals(1, sea.num);
		
		Episode ep = sea.episodes.get(0);
		
		assertEquals(1, ep.num);
	}

}
