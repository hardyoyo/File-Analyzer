package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Statistics showing accumulated file counts within a directory structure.
 * @author TBrady
 *
 */
public class DirStats extends Stats {
	public static enum DirStatsItems implements StatsItemEnum {
		Dir(StatsItem.makeStringStatsItem("Dir", 100)),
		Count(StatsItem.makeLongStatsItem("Count")),
		CumulativeCount(StatsItem.makeLongStatsItem("Cumulative Count"));
		
		StatsItem si;
		DirStatsItems(StatsItem si) {this.si=si;}
		public StatsItem si() {return si;}
	}

	public static enum Generator implements StatsGenerator {
		INSTANCE;
		public DirStats create(String key) {return new DirStats(key);}
	}
	public static StatsItemConfig details = StatsItemConfig.create(DirStatsItems.class);
	
	private DirStats(String key) {
		super(DirStats.details, key);
	}
	
	public Object compute(File f, FileTest fileTest) {
		File root = fileTest.getRoot();
		for(File ftest = f.getParentFile(); ftest!=null; ftest = ftest.getParentFile()){
			DirStats stats = (DirStats)fileTest.getStats(fileTest.getKey(f,ftest));
			stats.accumulate(f, fileTest, ftest);
			if (ftest.equals(root)){
				break;
			}
		}
		return fileTest.fileTest(f);
	}
	
	public void accumulate(File f, FileTest fileTest, File parentdir) {
		if (f.getParentFile().equals(parentdir)){
			sumVal(DirStatsItems.Count, 1);
		}
		sumVal(DirStatsItems.CumulativeCount, 1);
	}
}
