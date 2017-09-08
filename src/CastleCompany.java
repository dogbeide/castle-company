import java.util.*;
import java.util.regex.*;


public class CastleCompany {
	
	static enum Slope {FLAT_DOWN, FLAT_UP, PEAKVALLEY, HILL} // Land descriptors
	
	public static void main(String[] args){
		int castles;
		List<Integer> land = new ArrayList<Integer>();
		
		// Get landscape design:
		Scanner feed = new Scanner(System.in);
		
		//Safe regex: multi-space, multi-comma, and newline delimiter/separator support
		Pattern antiRegex = Pattern.compile(System.getProperty("line.separator") + "|(\\s+,*)+|(,+\\s*)+");
		feed.useDelimiter(antiRegex);
		
		// Prompt architect:
		System.out.println("Enter a series of integers as your landscape:");
		
		
		// Extract land from blueprints, build blocks
		try {
			while(feed.hasNextInt()){
				int piece = feed.nextInt();
				land.add(piece);
			}
			castles = getCastleCount(land);
			System.out.println("Official landscape: " + land);
			System.out.println("Number of castle plots available: " + castles);
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("\nPlease enter only integers (separated by commas and/or spaces)\n");
		}
		finally {feed.close();} // Due diligence
		
		
		
	}
	
	public static int getCastleCount(List<Integer> land){
		int i, castles;
		int cur, next, prev;
		Slope state;
		
		// Due diligence for the land
		if (land == null){
			System.out.println("Empty blueprint, you didn't draw a landscape.");
			return 0;
		}
		else if (land.size() < 1){
			System.out.println("No land in blueprint (P.S. water-float-edition castles not supported).");
			return 0;
		}
		else if (land.size() >= 1 && land.size() < 3) return 1; // Too small for threesome
		
		//Prepare, init
		cur = land.get(0);
		next = land.get(1);
		state = Slope.PEAKVALLEY;
		castles = 1; // Always build @start, non-empty
		
		// Iterate over each piece of land until end of landscape
		for(i = 1; i < land.size() - 1; i++){
			// Get indices
			prev = cur;
			cur = next;
			next = land.get(i+1);
			
			// Going uphill
			if (cur > prev){
				if (cur < next)
					state = Slope.HILL; // Continue uphill
				else if (cur == next)
					state = Slope.FLAT_UP; // Begin upper-flat
				else if (cur > next){
					state = Slope.PEAKVALLEY; // Simple peak
					castles++; 
				}
			}
			// Going downhill
			else if (cur < prev){
				if (cur < next){
					state = Slope.PEAKVALLEY; // Simple peak
					castles++; 
				}
				else if (cur == next)
					state = Slope.FLAT_DOWN; // Begin lower-flat
				else if (cur > next){
					state = Slope.HILL; // Continue uphill
				}
			}
			// Going straight
			else if (cur == prev){
				if (cur < next){
					if (state == Slope.FLAT_UP) // False positive, not long peak
						continue;
					else if (state == Slope.FLAT_DOWN){ // Long peak
						castles++;
						state = Slope.FLAT_UP;
					}
				}
				else if (cur == next) // Flat threesome
					continue;
				else if (cur > next){
					if (state == Slope.FLAT_UP){ // Long valley
						castles++;
						state = Slope.FLAT_DOWN;
					}
					else if (state == Slope.FLAT_DOWN) // False positive, not long valley
						continue;
				}
			}
		}
		
		return castles;
	}

}
