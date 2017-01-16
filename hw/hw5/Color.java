import java.util.*;
import java.io.*;


// Use the following data class:
public class Color 
{	
	public static final int MAX_COLOR_CODE = 0xFFFFFF;
	
	private int code=0;
	private String name="";

	public Color(){}

	public Color(int c, String n)
	{
		setCode(c);
		setName(n);
	}
	
	public boolean setCode(int c)
	{
		if( c < 0 || c > MAX_COLOR_CODE )
			return false;
		code = c;
		return true;
	}
	
	public boolean setName( String s )
	{
		if( s==null || s.length() == 0 )
			return false;
		name = s;
		return true;
	}
	
	public int getCode(){ return code; }
	
	public String getName(){ return name; }

	public static Scanner userScanner = new Scanner(System.in);

	// opens a text file for input, returns a Scanner:
	public static Scanner openInputFile()
	{
		String filename;
        Scanner scanner=null;
        
		System.out.print("Enter the input filename: ");
		filename = userScanner.nextLine();
		File file= new File(filename);

		try{
			scanner = new Scanner(file);
		}// end try
		catch(FileNotFoundException fe){
		   	System.out.println("Can't open input file\n");
		    	return null; // array of 0 elements
		} // end catch
		return scanner;
	}

// Call the following in main for both HashTables (in one call):

	public static void testHashTables(HashTable<Color> tableSC,
							HashTable<Color> tableQP)
	{
		Color targetColor = null;
		Color tempColor;
		boolean containsResultSC, containsResultQP;
		int inputCode;
		String inputName;
		Scanner infile;
		
		System.out.println("\nTesting hash tables, please enter the same filename just used, ");
		infile = openInputFile();
		if( infile == null )
			return ;		

		containsResultSC = containsResultQP = false;
		while( infile.hasNext() && !(containsResultSC && containsResultQP) ){
			inputCode = infile.nextInt(16);
			inputName = infile.nextLine().trim();
			targetColor = new Color(inputCode, inputName);
			containsResultSC = tableSC.contains(targetColor); // test contains in HashSC
			containsResultQP = tableQP.contains(targetColor); // test contains in HashQP
			System.out.println("Result of calling contains for "+ targetColor.getName() + " in HashSC = " +
							containsResultSC);
			System.out.println("Result of calling contains for "+ targetColor.getName() + " in HashQP = " +
					containsResultQP);
		}
		
		// found a Color in table1 in table2
		tempColor = tableSC.getEntry(targetColor);
		if( tempColor != null )
		{
			System.out.println("Retrieved in HashSC, Color: " + tempColor.getName() + ", now trying to delete it");
			// now delete it 
			if( tableSC.remove(targetColor))
				System.out.println("Successfully removed from HashSC: " + targetColor.getName());
			else
				System.out.println("Unsuccessful attempt to remove from HashSC: " + targetColor.getName());
		}
		else
			System.out.println("Error in HashSC: can't retrieve "+ targetColor.getName());
		
		tempColor = tableQP.getEntry(targetColor);
		if( tempColor != null )
		{
			System.out.println("Retrieved in HashQP, Color: " + tempColor.getName() + ", now trying to delete it");
			// now delete it 
			if( tableQP.remove(targetColor))
				System.out.println("Successfully removed from HashQP: " + targetColor.getName());
			else
				System.out.println("Unsuccessful attempt to remove from HashQP: " + targetColor.getName());
		}else
			System.out.println("Error in HashQP: can't retrieve "+ targetColor.getName());

	} // testHashTables

	public static void initialTest(HashSC<Color> hsc, HashQP<Color> hsq) 
	{
		System.out.println("HashSC with the int key has:");
		hsc.traverseTable(new ColorCodeVisitor());

		hsc.displayStatistics();

		System.out.println("\nHashQP with the String key has:");
		hsq.traverseTable(new ColorNameVisitor());

		hsq.displayStatistics();
	}

	public static boolean readInInput(HashSC<Color> hsc, HashQP<Color> hsq) 
	{
		int c;
		String n;
		Color clr;
		userScanner = openInputFile();
		if(userScanner == null) {
			return false;
		}

		while(userScanner.hasNextLine())
		{
			c = userScanner.nextInt(16);
			// System.out.println(String.format("%06X",c));
			n = userScanner.nextLine().trim();
			// System.out.println(n);
			clr = new Color(c,n);

			hsc.insert(clr);
			hsq.insert(clr);
		}
		return true;
	}

	public static void traverseHashTables(HashSC<Color> hsc, HashQP<Color> hsq) 
	{
		System.out.println("HashSC with the int key now has:");
		hsc.traverseTable(new ColorCodeVisitor());

		System.out.println("\nHashQP with the String key now has:");
		hsq.traverseTable(new ColorNameVisitor());
	}

	public static void main(String [] args) {
		HashSC<Color> hsc = new HashSC<Color>(new ColorCodeHasher(), new ColorCodeComparator());
		HashQP<Color> hsq = new HashQP<Color>(new ColorNameHasher(), new ColorNameComparator());

		if(!readInInput(hsc, hsq)){
			System.out.println("ending program");
			System.exit(1);
		}
		
		initialTest(hsc, hsq);

		userScanner = new Scanner(System.in);
		testHashTables(hsc, hsq);

		traverseHashTables(hsc, hsq);
	}
	
} // end class Color

class ColorCodeVisitor implements Visitor<Color> {
// so when you override the visit method, it displays the Color's value (in hexadecimal), then its String (you may use System.out.printf with %06X to display in hex)
	@Override
	public void visit(Color c) {
		System.out.println(String.format("%06X", c.getCode()) + " " + c.getName());
	}

}

class ColorNameVisitor implements Visitor<Color> {
// so when you override the visit method, it displays the Color's String, then its value in hexadecimal (see example runs for format)
	@Override
	public void visit(Color c) {
		System.out.println(c.getName() + " " + String.format("%06X", c.getCode()));
	}
}

class ColorCodeComparator implements Comparator<Color> {
// so it overrides only the compare method, return the difference between the first parameter's value - second parameter's value
	@Override
	public int compare(Color left, Color right) 
	{
		return left.getCode() - right.getCode();
	}
}

class ColorNameComparator implements Comparator<Color> {
// so it overrides only the compare method, return the result of first parameter's String's compareToIgnoreCase (passing the second parameter's String)
	@Override
	public int compare(Color left, Color right) 
	{
		return left.getName().compareToIgnoreCase(right.getName());
	}
}


/* OUTPUT :
Enter the input filename: HW5 Input.txt
HashSC with the int key has:
000000 Black
CF0234 Cherry
1A5798 Indigo Dye
7EFBB3 Light Blue Green
C9D179 Greenish Beige
CAABD0 Light Medium Violet
00A877 Green (munsell)
95D0FC Light Blue
D6CE7B Muted Lime
2EE8BB Aqua Marine
735C12 Mud
FF5B00 Bright Orange
9A0200 Deep Red
25937E Light Zydeco
FF9966 Atomic Tangerine, Pink-orange
66B032 Green (ryb)
044A05 Bottle Green
A99A86 Grullo
B04E0F Burnt Sienna
028F1E Emerald Green
B8C2C2 Light Sirocco
568203 Avocado
F5D3E6 Light Kobi
DEEBEC Light Jungle Mist
ED9121 Carrot Orange
76FDA8 Light Bluish Green
AC4F06 Cinnamon
B57EDC Lavender (floral)
CBE3D7 Edgewater
CB6D51 Copper Red
811453 French Plum
B8B654 Light Olivetone
758DA3 Blue/grey
F19CBB Amaranth Pink
E4BEEB Deep Mauve Pink
C1C6FC Light Periwinkle
048243 Jungle Green
035096 Medium Electric Blue
062A78 Catalina Blue
1034A6 Egyptian Blue
20C073 Dark Mint Green
4997D0 Celestial Blue
843F5B Deep Ruby
FFF0E0 Light Peach Puff
5218FA Han Purple
3A6960 Japanese Blue-green
AF868E Mauve Taupe
FFB280 Italian Apricot
0F8C3F Light Lavender Magenta
00FFFF Cyan
F64A8A French Rose
ACBF69 Light Olive
2BB179 Bluey Green
0B5509 Forest
F1D4F1 Light Pale Plum
FFBF00 Amber, Fluorescent Orange
4D0135 Blackberry
C4FE82 Light Pea Green
94AC02 Barf Green
FAEBD7 Antique White, Moccasin
FFF0EE Light Peach Schnapps
FF9999 Light Salmon Pink
F26476 Light Carmine Pink
247AFD Clear Blue
F2F3F4 Anti-flash White
FFF0F5 Lavender Blush
BB3F3F Dull Red
B22222 Firebrick
658B38 Moss Green
FFFD74 Butter Yellow
D5B60A Dark Yellow
26619C Lapis Lazuli
AA23FF Electric Purple
BE0032 Crimson Glory
AEFD6C Light Lime
FFFD78 Custard
A2006D Flirt
7D7F7C Medium Grey
6EAEA1 Green Sheen
FFA62B Mango
A40000 Dark Candy Apple Red
CDB7F0 Light Medium Purple
1F75FE Blue (crayola)
B0C4DE Light Steel Blue
D2BD0A Mustard Yellow
873260 Boysenberry
FFD1DF Light Pink
FBA0E3 Lavender Rose
FFCBA4 Deep Peach, Peach (crayola)
836539 Dirt Brown
996666 Copper Rose
004B49 Deep Jungle Green
A3C1AD Cambridge Blue
9DBCD4 Light Grey Blue
1F6357 Dark Green Blue
7C1C05 Kenyan Copper
BCD4E6 Beau Blue, Pale Aqua
C6F808 Greeny Yellow
9B5FC0 Amethyst
01F9C6 Bright Teal
F8F8FF Ghost White
E3E4E5 Light Silver Sand
FDBFB7 French Peach
0BF77D Minty Green
01386A Marine Blue
C45655 Fuzzy Wuzzy Brown
5170D7 Cornflower Blue
FEB308 Amber
FFE4B5 Moccasin
05696B Dark Aqua
D58A94 Dusty Pink
C88D94 Greyish Pink
A2A2D0 Blue Bell
FF4F00 International Orange (aerospace)
F5FFFA Mint Cream
A8FF04 Electric Lime
916E99 Faded Purple
86775F Brownish Grey
C41E3A Cardinal
E69966 Chinese Apricot
F5FFFF Light Bubbles
E5DE43 Medium Bitter Lemon
BC3F4A Deep Carmine Red
50C878 Emerald, Paris Green
FCC006 Marigold
8B88F8 Lavender Blue
4C9141 May Green
BDBBD7 Lavender Blue-grey
699D4C Flat Green
BE3262 Deep Carmine Ruby
4B5320 Army Green
00009C Duke Blue
CC4E5C Dark Terra Cotta
555D50 Ebony
464196 Blueberry
87A922 Avocado Green
3A18B1 Indigo Blue
71D9E2 Aquamarine Blue
F7CDB9 Light Medium Apricot
990F4B Berry
02066F Dark Royal Blue
FFFDD0 Cream
020035 Midnight Blue
880085 Mardi Gras
FCD917 Candlelight
FFE4E1 Misty Rose
F9F8FE Light Selago
0070FF Brandeis Blue
FFD230 Light California Gold
DFFF00 Chartreuse Yellow
F400A1 Hollywood Cerise
EEDC5B Dull Yellow
825F87 Dusty Purple
DDECC0 Lime Cream
41FDFE Bright Cyan
9CEF43 Kiwi
C9B93B Earls Green
DE3163 Cerise, Cherry
AC1DB8 Barney
FBCCE7 Classic Rose
AD8150 Light Brown
34013F Dark Violet
FEFE22 Laser Lemon
DBFFF8 Frosted Mint
B7C9E2 Light Blue Grey
56AE57 Dark Pastel Green
8C82B6 Mogul
3B444B Arsenic
FFC5CB Light Rose
1D5DEC Azul
665FD1 Dark Periwinkle
C2FF89 Light Yellowish Green
CDFD02 Greenish Yellow
9966CC Medium Deep Lavender
A55790 Meadow Mauve
D65282 Mystic Pearl
8A2BE2 Deep Indigo
872657 Dark Raspberry
AB5C6D Mauvewood
B27A01 Golden Brown
9D0216 Carmine
DADADA Light Silver Chalice
728F02 Dark Yellow Green
80CA2B Dizzy Lizzy
FEC615 Golden Yellow
534B4F Liver
4DA409 Lawn Green
EEDC82 Flax
E79FC4 Kobi
B6C406 Baby Puke Green
B2BEB5 Ash Grey
ADF802 Lemon Green
8CFF9E Baby Green
A32638 Alabama Crimson
069B81 Gossamer
F4BBFF Brilliant Lavender, Electric Lavender
7BB274 Faded Green
FF0490 Electric Pink
AD0AFD Bright Violet
00CED1 Deep Turquoise
DA3287 Deep Cerise
85A3B2 Bluegrey
26F7FD Bright Light Blue
FFD8B1 Light Peach
A9BA9D Laurel Green
CDC50A Dirty Yellow
CDB891 Ecru
A7FFB5 Light Seafoam Green
0000FF Blue
98F6B0 Light Sea Green
49796B Hooker's Green
850E04 Indian Red
FBEC5D Maize
C48EFD Liliac
06B48B Green Blue
6241C7 Bluey Purple
C74375 Fuchsia Rose
05FFA6 Bright Sea Green
3D2B1F Bistre
177245 Dark Spring Green
BB3385 Medium Red-violet
FCE5F2 Chantilly Light
A88905 Dark Mustard
B2996E Dust
00FA9A Medium Spring Green
CCFD7F Light Yellow Green
734A65 Dirty Purple
8E7618 Hazel
08FF08 Fluorescent Green
FFFE40 Canary Yellow
3D0C02 Black Bean
00AFCA Khazakstani Azure
FC8EAC Flamingo Pink
CC7A8B Dusky Pink
3F9B0B Grass Green
C53151 Dingy Dungeon
A88F59 Dark Sand
7C0A02 Barn Red
8CFFDB Light Aqua
DE0C62 Cerise
915C83 Antique Fuchsia
264348 Japanese Indigo
DA2647 Deep Cherry Red
006B3C Cadmium Green
D5CFDE Light Amethyst Smoke
005249 Dark Blue Green
FFA700 Chrome Yellow
DE5D83 Blush
195905 Lincoln Green
C6E610 Las Palmas
36013F Deep Purple
CD9999 Copper Rust Light
485470 Blue Indigo
8FAE22 Icky Green
BBE261 Light Lima
BB466F Deep Carmine Rose
FFF1EB Light Watusi
CCA01D Lemon Curry
A0785A Chamoisee
893F45 Cordovan
3FFF00 Harlequin
7E73B8 Fortune
8F509D Dark Vivid Violet
894585 Light Eggplant
AC1E44 French Wine
AD4379 Mystic Maroon
5FA052 Muted Green
2E5A88 Light Navy Blue
ACE5EE Blizzard Blue
84B701 Dark Lime
FFFE7A Light Yellow
A2653E Earth
F0DC82 Buff
4F9153 Light Forest Green
7F4E1E Milk Chocolate
D591A4 Can Can
CC5555 Girlsenberry
002D04 Dark Forest Green
FFEBCD Blanched Almond
749551 Drab Green
3C0008 Dark Maroon
84597E Dull Purple
A757AB Chinese Purple
A8B504 Mustard Green
7BFDC7 Light Aquamarine
42B395 Greeny Blue
8A9CC4 Lavender Lustre
EBE3FC Light Perfume
C7FF00 Fluorescent Lime
887191 Greyish Purple
A98F64 California Gold
874C62 Dark Mauve
E7E4EF Light Melrose
CF1020 Lava
670F25 Crimson Lake
841B2D Dark Carmine
1FB57A Dark Seafoam
FE6F5E Bittersweet
F88379 Coral Pink
602FFB Imperial
003366 Dark Midnight Blue
FADA5E Jonquil, Naples Yellow, Royal Yellow, Stil De Grain Yellow
D90166 Dark Hot Pink
000181 Forget-me-not
A17E9A Dull Mauve
526525 Camo Green
D98AA8 Light Night Shadz
926F5B Beaver
889717 Baby Shit Green
350E47 Jagger
0AFF02 Fluro Green
5E819D Greyish Blue
AD6F69 Copper Penny
FFFEB6 Light Beige
FEE600 Deep Lemon Yellow
FFB3DE Hot Pink Light
E97451 Burnt Sienna, Light Red Ochre
D0868E Bright Mauve Glow
4F3A3C Dark Puce
C1BBEC Light Moody Blue
0A888A Dark Cyan
875F42 Cocoa
EC3B83 Cerise Pink
FDFF38 Lemon Yellow
8FFF9F Mint Green
A8516E Mallow Purple
CA7B80 Dirty Pink
6F00FF Indigo
BD33A4 Byzantine
E3FF00 Lemon-lime
AF6F09 Caramel
3F829D Dirty Blue
1A2118 Charleston Green
A6C875 Light Moss Green
EC5486 Japanese Atomic Carmine Pink
D2A560 Light Hawaiian Tan
25FF29 Hot Green
003399 Italian Electric Blue
696006 Greeny Brown
A57164 Blast Off Bronze
C4FFF7 Eggshell Blue
C077A6 Medium Raspberry
10A674 Bluish Green
B96CCA Light Seance
679267 Copper Green
AD8884 Mauve Glow
FDFF52 Lemon
8B3F43 Cha Cha
AA381E Chinese Red
93CCEA Light Cornflower Blue
E49B0F Gamboge
A65E7E Deep Medium Red-violet
E53D43 Deep Carmine Pink-orange
FFCCFF Bright Pale Rose
CFB8DE Light Ce Soir
1560BD Denim
44944A Medium Harlequin
F7E7CE Champagne
730039 Merlot
BA9E88 Mushroom
B8860B Dark Goldenrod
FDFF63 Canary
BF77F6 Light Purple
D5AB09 Burnt Yellow
B5485D Dark Rose
E5CCC9 Dust Storm
3E82FC Dodger Blue
4EA4BA Maui Blue
A0025C Deep Magenta
343434 Jet
922B05 Brown Red
658CBB Faded Blue
9C6D57 Brownish
48D1CC Medium Turquoise
082567 Dark Sapphire
B898CD Light Lavender Affair
778899 Light Slate Gray
966EBD Deep Lilac
126180 Blue Sapphire
F7FA97 Light Starship
030764 Darkblue
FF5CCD Deep Pink Light
FFFF00 Electric Yellow, Yellow
DA9100 Harvest Gold
ADFF2F Green-yellow
997A8D Mountbatten Pink
03012D Midnight
66DDAA Medium Aquamarine
FF8243 Mango Tango
EF98AA Mauvelous
D6E94B Light Bahia
937C00 Baby Poop
C87606 Dirty Orange
00B6D9 Blue Atoll
FE828C Blush Pink
9BDDFF Columbia Blue
AF884A Dark Tan
76FF7B Lightgreen
FFF8DC Cornsilk
67032D Black Rose
A5A391 Cement
EAC592 Light Peru
FFD9B2 Light Italian Apricot
430541 Eggplant Purple
EB9373 Medium Apricot
77A1B5 Greyblue
FFF8E7 Cosmic Latte
FFF8ED Light Papaya Whip
D498CB Bright Mallow Pink
A484AC Heather
A2CFFE Baby Blue
11875D Dark Sea Green
C9FF27 Green Yellow
88786A Medium Taupe Gray
808080 Grey
FFFF31 Daffodil
CCAD60 Desert
F7D560 Light Mustard
F0B7CD Light Pale Red-violet
4B6113 Camouflage Green
92A1CF Ceil
E1C7C3 Brandy Rose Light
F5E25C Medium Lemon
6B8BA4 Grey Blue
B5A642 Brass
13EAC9 Aqua
E2062C Medium Candy Apple Red
9C6DA5 Dark Lilac
FEFF7F Faded Yellow
900020 Burgundy
A397B4 Amethyst Smoke
F9BC08 Golden Rod
9874D3 Lilac Bush
00FBB0 Greenish Turquoise
7058A3 Light Japanese Violet
9AF764 Light Grass Green
FEB4B1 Chinese Peach
00022E Dark Navy Blue
02590F Deep Green
070D0D Almost Black
789B73 Grey Green
AE0C00 Mordant Red 19
681C23 Internet Puce
DA467D Darkish Pink
D19FE8 Medium Bright Lavender
EBCBD6 Can Can Light
657432 Muddy Green
C120A2 Deep Magenta Rose
FF69AF Bubble Gum Pink
FFA812 Dark Tangerine
826D8C Grey Purple
63F7B4 Light Greenish Blue
51B73B Leafy Green
7B68EE Medium Slate Blue
C071FE Easter Purple
CD81B8 Bright Opera Mauve
979AAA Manatee
98817B Cinerous
DC143C Crimson
E3A857 Indian Yellow
FBBBC4 Light Froly
967BB6 Lavender Purple
AEFF6E Key Lime
C8FFB0 Light Light Green
FFFF7E Banana
C9AE5D Copper Yellow
4F284B Japanese Purple
CAE00D Bitter Lemon
E4D3D2 Mauve Chalk
FFFF81 Butter
CD5C5C Chestnut, Indian Red
FAE7B5 Banana Mania
7EA07A Greeny Grey
D8DCD6 Light Grey
2AFEB7 Greenish Cyan
EBE4F4 Light Prelude
B94E48 Deep Chestnut
8DB600 Apple Green
A552E6 Lightish Purple
380835 Eggplant
9A3001 Auburn
C3909B Grey Pink
FE0002 Fire Engine Red
FF4466 Magic Potion
0165FC Bright Blue
F9C8CB Crystal Rose
6C3461 Grape
E3F988 Mindaro
856088 Chinese Violet
C54B8C Mulberry
ACFFFC Light Cyan
287C37 Darkish Green
0D75F8 Deep Sky Blue
8D5EB7 Deep Lavender
58BC08 Frog Green
3B719F Muted Blue
A1C42C Medium Lime Green
9955BB Deep Rich Lavender
66424D Deep Tuscan Red
1BFC06 Highlighter Green
318CE7 Bleu De France
96B403 Booger Green
FF3800 Coquelicot
CCCCFF Lavender Blue, Periwinkle
D473D4 French Mauve
805B87 Muted Purple
751973 Darkish Purple
0BDA51 Malachite
E52B50 Amaranth
AB9004 Baby Poo
C2E20D Go Bunny Go
871550 Disco
FC51AF Bright Magenta Rose
5539CC Blurple
1CC3B4 Light Deep Sea
5E9B8A Grey Teal
BA160C International Orange (engineering)
FBCEB1 Apricot
8080FF Deep Periwinkle
E8CCD7 Mauvette
DC9DB3 Cadillac Light
79443B Bole, Medium Tuscan Red
E30022 Cadmium Red
FE4B03 Blood Orange
3D0734 Aubergine
FCC200 Golden Poppy
411900 Chocolate Brown
E7FEFF Bubbles
419C03 Grassy Green
341C02 Dark Brown
FFFFB6 Creme
7F684E Dark Taupe
A9203E Deep Carmine
E8F24E Light Bitter Lemon
CCFF00 Electric Lime, Fluorescent Yellow, French Lime
4E1609 French Puce
63B365 Boring Green
3EAF76 Dark Seafoam Green
448EE4 Dark Sky Blue
F5E2DE Dull Pallid Rose Light
D0FE1D Lime Yellow
6F6C0A Browny Green
B59410 Dark Gold
6699CC Blue Gray
98DF62 Go Go Go
D6CADD Languid Lavender
B9484E Dusty Red
0FFEF9 Bright Turquoise
F1B82D Mu Gold
4E5481 Dusk
9EFD38 French Lime
86608E French Lilac
014D4E Dark Teal
FF000D Bright Red
DE5285 Fandango Pink
FFFFD4 Eggshell
5B3256 Japanese Violet
2DFE54 Bright Light Green
E47698 Bright Blush
9D9FE9 Dadt Repeal Lavender
8D8468 Brown Grey
08457E Dark Cerulean
3C1414 Dark Sienna
F8F4FF Magnolia
002E63 Cool Black
FF63E9 Candy Pink
5A06EF Blue/purple
E4D430 Light Buddha Gold
D73B3E Jasper
696112 Greenish Brown
C90016 Harvard Crimson
C27E79 Brownish Pink
33B864 Cool Green
4F738E Metallic Blue
FAFAD2 Light Goldenrod Yellow
D9603B Medium Vermilion, Vermilion (plochere)
C0022F Lipstick Red
8F5E99 Medium Violet
FFFFF0 Ivory
4B3621 Coffee
D5869D Dull Pink
FF7077 Deep Pink-orange
2F4F4F Dark Slate Gray
C7C1FF Melrose
C72C48 French Raspberry
3560A6 Light Gulf Blue
65FE08 Bright Lime Green
3AB09E Keppel
BFDCD4 Light Acapulco
FF0038 Carmine Red
32293A Black Currant
BB8CAA Light Cosmic
E3DAC9 Bone
DE7E5D Dark Peach
71AA34 Leaf
FF003F Electric Crimson
8C7853 Deep Bronze
706C11 Brown Green
73A9C2 Moonstone Blue
E1A95F Earth Yellow
F5F5DC Beige
CA9BF7 Baby Purple
F08080 Light Coral
B2EC5D Inchworm
D399E6 Medium Mauve
6A79F7 Cornflower
E9692C Deep Carrot Orange
0BF9EA Bright Aqua
966FD6 Dark Pastel Purple
FF004F Folly
FFF39A Dark Cream
DE4C8A Heather Violet
715F6D Deep Taupe
DC4D01 Deep Orange
F7022A Cherry Red
6C541E Medium Bronze
3B7861 Dark Erin
DE9DAC Faded Pink
E25822 Flame
007FBF Honolulu Blue
0066CC Bright Navy Blue
5D76CB Bright Indigo
1E4D2B Cal Poly Green
A6FBB2 Light Mint Green
A1CAF1 Baby Blue Eyes
A23B3C Dark Cherry
AFA88B Bland
AD900D Baby Shit Brown
C8A2C8 Medium Lavender Grey
C65EA0 Light Disco
F5F5FD Light Lavender Mist
00416A Dark Imperial Blue, Indigo (dye)
FFC1CC Bubble Gum
3F00FF Electric Ultramarine
714693 Lavender Affair
9F2305 Burnt Red
E29CD2 Light Orchid
0047AB Cobalt Blue
D3D86E Light Citron
0F9B8E Blue/green
4EFD54 Light Neon Green
82A67D Greyish Green
5CA904 Leaf Green
FFF9FB Light Lavender Blush
02A4D3 Bright Cerulean
CAFFFB Light Light Blue
F653A6 Brilliant Rose
D8863B Dull Orange
FDEE00 Aureolin
AAA9CD Logan
C7458B Bright Raspberry Rose
6082B6 Glaucous
ED3CCA Amaranth Magenta
AA8A9E Lavender Brown
E4007C Mexican Rose
6E7F80 Aurometalsaurus
73C2FB Maya Blue
986960 Dark Chestnut
138808 India Green
C3B091 Khaki (html/css) (khaki)
F984EF Light Fuchsia Pink
6050DC Majorelle Blue
8FBC8F Grayish Sea Green
48C072 Dark Mint
D2E543 Light Limerick
0066FF Deep Azure
FF008F Fluorescent Pink
FE83CC Bubblegum Pink
E32636 Alizarin Crimson, Rose Madder
B12B7F Black Rose Light
DF84B5 Light Hibiscus
607C8E Blue Grey
02AB2E Kelly Green
9E3623 Brownish Red
3F012C Dark Plum
007FFF Azure
FF3F00 Electric Vermilion
856798 Dark Lavender
D47494 Charm
FF009F Electric Hollywood Cerise
9370DB Medium Purple
755258 Galaxy
96AE8D Greenish Grey
76A973 Dusty Green
E6E6FA Lavender Mist
DAA520 Goldenrod
0652FF Electric Blue
009900 Islamic Green
C5C2EF Light Chetwode Blue
E68FAC Charm Pink, Light Thulian Pink
EFF7AA Australian Mint
FF00AF Magenta Rose
DCDCDC Gainsboro
ACE1AF Celadon
0CB577 Green Teal
9C87CD Dull Deep Lavender
FBEEAC Light Tan
A1C50A Citrus
96F97B Light Green
8FB67B Lichen
2C6FBB Medium Blue
CC0000 Boston University Red
00035B Dark Blue
014182 Darkish Blue
C32148 Bright Maroon, Maroon (crayola)
040273 Deep Blue
2C75FF Deep Electric Blue
9BE5AA Hospital Green
C77DF3 Light Blue-violet
017371 Dark Aquamarine
A0BF16 Gross Green
5D06E9 Blue Violet
EFDECD Almond
276AB3 Mid Blue
D3494E Faded Red
87FD05 Bright Lime
763950 Cosmic
21C36F Algae Green
2976BB Bluish
32BF84 Greenish Teal
653700 Brown
7A6A4F Greyish Brown
63313A Medium Burgundy
FF00CC Hot Magenta
49759C Dull Blue
EA88A8 Carissma
A5CA77 Jade Lime
FE9D04 Bright California Gold
69D84F Fresh Green
009F6B Green (ncs)
6A6E09 Brownish Green
AC7E04 Mustard Brown
6C7A0E Murky Green
40A368 Greenish
1C352D Medium Jungle Green
EF3038 Deep Carmine Pink
228B22 Forest Green (web)
592720 Caput Mortuum
F8C3DF Chantilly
B36FF6 Light Urple
9457EB Lavender Indigo
B3C110 La Rioja
087830 La Salle Green
3C4D03 Dark Olive Green
BCCB7A Greenish Tan
964B00 Brown (traditional)
F7E98E Flavescent
FEA993 Light Salmon
E8E08E Light Earls Green
C327B2 Japanese Crimson
BA8759 Deer
7FFF00 Chartreuse Green
5CA345 Groovy
7B002C Bordeaux
C88A65 Antique Brass
A4BE5C Light Olive Green
FF64C5 Brilliant Electric Hollywood Cerise
E8000D Ku Crimson
FFFA86 Manilla
FF00FF Magenta
A24857 Light Maroon
B87B6B Brilliant Copper
042E60 Marine
FCE883 Medium Yellow
FFDB58 Mustard
E48400 Fulvous
6D5ACF Light Indigo
015482 Deep Sea Blue
6E1C34 Claret Violet
AA786D Medium Girlsenberry
33CC00 Deep Harlequin
ACC2D9 Cloudy Blue
A8A495 Greyish
A03623 Brick
388004 Dark Grass Green
801818 Falu Red
05472A Evergreen
C3FBF4 Duck Egg Blue
960056 Dark Magenta
BEBEBE Gray (x11 Gray)
A85387 Dahlia Mauve
91A3B0 Cadet Grey
CB7723 Brownish Orange
C04E01 Burnt Orange
D70A53 Debian Red
009337 Kelley Green
FFAFC8 Light Puce
B0B583 Medium Winter Pear
99637B Mellow Mauve
9D5616 Hawaiian Tan
FDD5B1 Light Apricot
FF2052 Awesome
EB6FCB Brilliant Red-violet
C0737A Dusty Rose
D5174E Lipstick
AA4069 Medium Ruby
BC2350 Carminorubaceous
033500 Dark Green
8DDCD3 Light Keppel
74C365 Mantis
544E03 Green Brown
D2691E Light Chocolate
06470C Forest Green
CFBAEC Light Lilac Bush
08787F Deep Aqua
B2FFFF Celeste (colour)
E4717A Candy Pink, Tango Pink
0247FE Blue (ryb)
BA6873 Dusky Rose
CA6B02 Browny Orange
B66325 Copper
980002 Blood Red
AB274F Amaranth Purple
FF7E00 Automotive Amber
DD85D7 Lavender Pink
FA5FF7 Light Magenta
7FFF55 Brilliant Harlequin
AE7181 Mauve
21FC0D Electric Green
FFFACD Lemon Chiffon
5D3954 Dark Byzantium
480607 Bulgarian Rose
7A83BF Medium Periwinkle
BA55D3 Medium Orchid
C4C3D0 Lavender Grey
BFAC05 Muddy Yellow
B10097 Light Nightclub
E7F3EB Light Edgewater
F4F0EC Isabelline
B11355 Dove Cherry
002FA7 International Klein Blue
6F4E37 Coffee, Tuscan Brown
F0944D Faded Orange
1F0954 Dark Indigo
C9B003 Brownish Yellow
6ECB3C Apple
7B3F00 Chocolate (traditional)
FDAA48 Light Orange
A5CB0C Bahia
C23B22 Dark Pastel Red
B6316C Hibiscus
F8DD5C Energy Yellow
FFFAF0 Floral White
E62020 Lust
A00498 Barney Purple
1F3B4D Dark Blue Grey
C95EFB Bright Lilac
8F9805 Baby Poop Green
0F3F00 Mandarin Red
CB00F5 Hot Purple
915F6D Mauve Taupe, Raspberry Glace
FE01B1 Bright Pink
E4C2D5 Melanie
FF77FF Fuchsia Pink
F3FDC6 Light Mindaro
B76969 Light Persian Plum
BE03FD Bright Purple
007AA5 Cg Blue
214761 Dark Slate Blue
598556 Dark Sage
748B97 Bluish Grey
5D8AA8 Air Force Blue
7F8F4E Camo
876E4B Dull Brown
FD5956 Grapefruit
B96902 Brown Orange
017A79 Bluegreen
0B4008 Hunter Green
FDDC5C Light Gold
76BD17 Lima
2D5DA1 Medium Sapphire
C292A1 Light Mauve
280137 Midnight Purple
7B4BAB Light Violent Violet
0093AF Blue (munsell)
560319 Dark Scarlet
000435 Dark Navy
56FCA2 Light Green Blue
990024 Medium Tyrian Purple
3A2EFE Light Royal Blue
480656 Clairvoyant
A55AF4 Lighter Purple
9DFF00 Bright Yellow Green
3C4142 Charcoal Grey
9C7C3B Metallic Sunburst
DE98B2 Blush Light
FCD667 Medium Goldenrod
30BA8F Mountain Meadow
154406 Forrest Green
1CAC78 Green (crayola)
00555A Deep Teal
B19CD9 Light Pastel Purple
DD5A91 Carmine Rose
155084 Light Navy
A8415B Light Burgundy
667C3E Military Green
B0BC4A Medium Chartreuse
F95A61 Carnation
7F5112 Medium Brown
9683EC French Lavender
E09214 Deep Apricot
B06500 Ginger
5D1451 Grape Purple
FFBCD9 Cotton Candy
665D1E Antique Bronze
1A1110 Licorice
7F7053 Grey Brown
742802 Chestnut
886806 Muddy Brown
EDBACD Charm Light
9FFEB0 Mint
7FFFD4 Aquamarine
A18178 Desert Taupe
9B8F55 Dark Khaki
96C8A2 Eton Blue
FF0800 Candy Apple Red
002395 Imperial Blue
490648 Deep Violet
FE46A5 Barbie Pink
FF7855 Melon
61E160 Lightish Green
BFD833 Lime Punch
B5CE08 Green/yellow
F03D6F Carmine Ruby
C154C1 Deep Fuchsia, Fuchsia (crayola)
FFB6C1 Medium Pink
EFB435 Macaroni And Cheese
85754E Gold Fusion
F6EABE Lemon Meringue
B6FFBB Light Mint
FF1493 Deep Pink, Fluorescent Pink
FF4040 Coral Red
CB0162 Deep Pink
410200 Deep Brown
44719B Agate Blue
E67E30 French Apricot
895B7B Dusky Purple
D1768F Muted Pink
728639 Khaki Green
AD9194 Deauville Mauve
B2713D Clay Brown
8581D9 Chetwode Blue
B695C0 Mystic Lilac
A90308 Darkish Red
39D1C2 Light Gossamer
01A049 Emerald
F00BA4 Foobar
F8B878 Mellow Apricot
05480D British Racing Green
9C2542 Big Dip O'ruby
436BAD French Blue
CEA2FD Lilac
95A3A6 Cool Grey
1E488F Cobalt
7F76D3 Moody Blue
54AC68 Algae
703BE7 Bluish Purple
BFBF6C Green Chartreuse Liqueur
FB607F Brink Pink
9CBB04 Bright Olive
B48395 English Lavender
3D1C02 Chocolate
D4E646 Light Citrus
CD2682 Amaranth Cerise
A0FEBF Light Seafoam
548D44 Fern Green
CC397B Fuchsia Purple
D3D3D3 Light Gray
B04C6A Cadillac
650021 Maroon
F5C5C2 English Rose
1A2421 Dark Jungle Green
8AB8FE Carolina Blue
719F91 Greyish Teal
004953 Midnight Green
C08D5E Copper Canyon Light
C18D25 Light Peru Tan
8F4155 Japanese Plum
3C73A8 Flat Blue
FEEF61 Light Candlelight
AF4035 Medium Carmine, Pale Carmine
5EDC1F Green Apple
7BC8F6 Lightblue
704241 Deep Coffee
0087BD Blue (ncs)
21ABCD Ball Blue
FFAA8D Goddess
9C7687 Dusky Orchid
FDB147 Butterscotch
71291D Dark Copper
051657 Gulf Blue
B66A50 Clay
DDE454 Light La Rioja
EFC0FE Light Lavendar
9D7651 Mocha
98777B Bazaar
D39BCB Light Medium Orchid
18A7B5 Light Teal Blue
50A747 Mid Green
BF4F51 Bittersweet Shimmer
702963 Byzantium
E4D00A Citrine
0B8B87 Greenish Blue
D987B9 Light Royal Heath
8A6E45 Dirt
B9FF66 Light Lime Green
6CA0DC Little Boy Blue
696969 Dim Grey
C9DC87 Medium Spring Bud
C0362C International Orange (golden Gate Bridge)
00BFFF Capri, Deep Sky Blue
3C8043 Area 51
60460F Mud Brown
9BB53C Booger
C1FD95 Celery
EE82EE Lavender Magenta, Violet (web)
C1A004 Buddha Gold
F0F8FF Alice Blue
89FE05 Lime Green
E5AA70 Fawn
D46FF9 Light Veronica
DEB887 Burlywood
C39953 Aztec Gold
B89BDD Light Studio
7E5E60 Deep Rose Taupe
E7BCB4 Dull Pallid Rose
00308F Air Force Blue (usaf
B29705 Brown Yellow
D9D8EA Light Logan
D94972 Cabaret
3B7A57 Amazon
EF9BB9 Cabaret Light
C2F732 French Chartreuse
29465B Dark Grey Blue
F3E5AB Medium Champagne, Vanilla
458AC6 Medium Azure
9932CC Dark Orchid
A75502 French Tan
7DA98D Bay Leaf
26538D Dusk Blue
00CC99 Caribbean Green
BFFE28 Lemon Lime
C74767 Deep Rose
663854 Halaya Ube
63A950 Fern
23C48B Greenblue
1B2431 Dark
770F05 Deep Burgundy
4D5D53 Feldgrau
C3103A Kermes
9771B5 Ce Soir
EFCDB8 Desert Sand
8CFD7E Easter Green
533CC6 Blue With A Hint Of Purple
536872 Cadet
536878 Dark Electric Blue
21421E Myrtle
555555 Davy's Grey
89A0B0 Bluey Grey
C51C56 Light Red Devil
647D8E Grey/blue
007BA7 Cerulean
E6F554 Light Las Palmas
F0E68C Light Khaki
12E193 Aqua Green
FAF0BE Blond
F8DE7E Mellow Yellow
9D5783 Light Plum
FF7F50 Coral
FF028D Hot Pink
9FFF7F Light Harlequin
3B3C36 Black Olive
E5B73B Meat Brown
9F2B68 Amaranth Deep Purple
FF2800 Ferrari Red
A4C639 Android Green
D94FF5 Heliotrope
86A17D Grey/green
7D26CD Internet Purple
536267 Gunmetal
53FE5C Light Bright Green
CF524E Dark Coral
B80049 Bright Tyrian Purple
E6E8FA Glitter
02D8E9 Aqua Blue
828344 Drab
C49FBA Bright Mauve Mist
363737 Dark Grey
FF6CB5 Bubblegum
FFC40C Mikado Yellow
FF08E8 Bright Magenta
FF474C Light Red
6258C4 Iris
FDE9F5 Classic Rose Light
779ECB Dark Pastel Blue
FAF0E6 Linen
C08181 Medium Carmine Pink
FFF600 Cadmium Yellow
A8E4A0 Granny Smith Apple
D71868 Dogwood Rose
680018 Claret
C69F59 Camel
3B5B92 Denim Blue
77AB56 Asparagus
F9EAF3 Amour
8E616A Medium Puce
FF8C00 Dark Orange
660099 Generic Purple
934D91 Hyacinth Violet
9A393D Bulgarian Rose Light
C32350 Medium Rich Magenta
FBBEDA Cupid
C85A53 Dark Salmon
9F381D Cognac
77926F Green Grey
C6FCFF Light Sky Blue
BADA55 Badass
F4E5ED Light Melanie
9E003A Cranberry
D1E189 Lime Pulp
9F8303 Diarrhea
840000 Dark Red
A899E6 Light French Lavender
00FF00 Green
57D200 Deep Yellow-green
FFB7C5 Cherry Blossom Pink
41415D Dotcom
39AD48 Medium Green
AAF0D1 Magic Mint
FF7FA7 Carnation Pink
C19A6B Fallow
BCECAC Light Sage
FF9EDA Light Electric Hollywood Cerise
FFB7CE Baby Pink
FFD700 Golden
C6930A Cal Poly Pomona Gold
FDE3F0 Cupid Light
253529 Black Leather Jacket
EBE1DF Almost Mauve
F57584 Froly
56365C Lilac Shadow
9F1F4C Medium Cerise
52503C Dark Bronze
054907 Darkgreen
C1F80A Chartreuse
F0FFF0 Honeydew
944747 Copper Rust
4A0100 Mahogany
7EF4CC Light Turquoise
465945 Gray-asparagus
606602 Mud Green
C760FF Bright Lavender
AE98AA Lilac Luster
F0FFFC Light Frosted Mint
F0833A Dusty Orange
F0FFFF Azure Mist/web
01C08D Green/blue
713036 Crimson Cherry
CFBCBE Bazaar Light
9DC209 Limerick
D02090 Bright Red-violet
EB4C42 Carmine Pink
B75203 Burnt Siena
B37E9A Mauve Orchid
BA067A Deep Raspberry
B0665C Light Cherrywood
B89CA9 Mauve Shadows
651A14 Cherrywood
6B7C85 Battleship Grey
5A86AD Dusty Blue
B2FBA5 Light Pastel Green
5729CE Blue Purple
71A6D2 Iceberg
00FF3F Erin
D6B4FC Light Violet
01FF07 Bright Green
A50B5E Jazzberry Jam
D4AF37 Metallic Gold
BF6935 Kenyan Copper Light
AD1C42 Deep Crimson
BAA8C1 Lavender Frost
902933 Japanese Carmine
E03C31 Cg Red
EBDBDB Mauve Morn
5F9E8F Dull Teal
B7E1A1 Light Grey Green
4B0101 Dried Blood
5CB200 Kermit Green
343837 Charcoal
BFFF00 Lime
EFBBCC Cameo Pink
02CCFE Bright Sky Blue
FD3592 French Fuchsia
B44668 Deep Blush
FDF6FA Amour Light
5F9EA0 Cadet Blue
D6FFFA Ice
FFFCC4 Egg Shell
AC86A8 Dusty Lavender
75FD63 Lighter Green
FF033E American Rose
769958 Moss
5D3B4C Mauve Wine
01826B Deep Sea
475F94 Dusky Blue
019529 Irish Green
B53389 Fandango
B5C306 Bile
CC6666 Fuzzy Wuzzy
AAA662 Khaki
503E5C Deep Dark Indigo
045C5A Dark Turquoise
BB8983 Brandy Rose
7EBD01 Dark Lime Green
DBB40C Gold
85BB65 Dollar Bill
446CCF Han Blue
D290B5 Moonlite Mauve
FF5470 Fiery Rose
74A662 Dull Green
014421 Forest Green (traditional), Up Forest Green
B31B1B Carnelian, Cornell Red
35063E Dark Purple
2242C7 Blue Blue
C46210 Alloy Orange
0D98BA Blue-green
FCF75E Icterine
F6C6D8 Carissma Light
B38B6D Light Taupe
CD7F32 Bronze
638B27 Mossy Green
F0E130 Dandelion
FE2F4A Lightish Red
94417B Deep Orchid
B4CFD3 Jungle Mist
6700FF Lobelia
7E3A15 Copper Canyon
056EEE Cerulean Blue
F5C71A Deep Lemon
137E6D Blue Green
3D7AFD Lightish Blue
DA3163 Cerise Magenta
76D7EA Medium Sky Blue
4984B8 Cool Blue
CB4154 Brick Red
FF6103 Cadmium Orange
373E02 Dark Olive
2F847C Celadon Green
F56991 Light Crimson
4C9085 Dusty Teal
ED0DD9 Fuchsia
EDC8FF Light Lilac
DFC5FE Light Lavender
FA4F7B Atomic Carmine Pink
FFFD01 Bright Yellow
AD1022 Crimson Red
9D0759 Dark Fuchsia
D7FFFE Ice Blue
E9D66B Arylide Yellow, Hansa Yellow
AC9362 Dark Beige
72A0C1 Air Superiority Blue
A0450E Burnt Umber
3CB371 Medium Sea Green
90FDA9 Foam Green
AC7434 Leather
BDDA57 June Bud
D500B6 Flirt Light
E18731 British Apricot
9FA91F Citron
18453B Msu Green
7E4071 Bruise
E79EC5 Light Red-violet
CB416B Dark Pink
DD4384 Light Shiraz
76424E Brownish Purple
1FA774 Jade
90E4C1 Light Teal
C79FEF Lavender
AEBEA6 Light Amazon
B33A7F Fuchsia Red
00703C Dartmouth Green
350036 Dark Mardi Gras
667E2C Dirty Green
8EE53F Kiwi Green
6D9BC3 Cerulean Frost
74ACDF Argentinian Azure
00A86B Jade Green
B2A4D4 Deep Lavender Blue-gray
C14A09 Brick Orange
0095B6 Bondi Blue
E34234 Cinnabar, Vermilion (cinnabar)
5CAC2D Grass
6832E3 Burple
FAFE4B Banana Yellow

In the HashSC class:

Table Size = 1597
Number of entries = 1294
Load factor = 0.8102692548528491
Number of collisions = 526
Longest Linked List = 5

HashQP with the String key has:
Dotcom 41415D
Brownish Pink C27E79
Leafy Green 51B73B
Amaranth Pink F19CBB
Blush Pink FE828C
Lawn Green 4DA409
Forest Green 06470C
Light Grass Green 9AF764
Midnight Purple 280137
Light Steel Blue B0C4DE
Cherry Blossom Pink FFB7C5
Ginger B06500
Muted Purple 805B87
Dark Puce 4F3A3C
Coral Red FF4040
Brown Red 922B05
Brink Pink FB607F
Deep Jungle Green 004B49
Bright Pale Rose FFCCFF
Chinese Peach FEB4B1
Cosmic Latte FFF8E7
Dark Sienna 3C1414
Medium Ruby AA4069
Deep Turquoise 00CED1
Awesome FF2052
Bubble Gum Pink FF69AF
Camo Green 526525
Camel C69F59
Dark Blue Grey 1F3B4D
Cognac 9F381D
Dark Sand A88F59
Erin 00FF3F
Cherrywood 651A14
Deep Fuchsia, Fuchsia (crayola) C154C1
Manatee 979AAA
Goddess FFAA8D
Dried Blood 4B0101
Egg Shell FFFCC4
Deep Burgundy 770F05
Light Cherrywood B0665C
Cool Green 33B864
Copper Red CB6D51
Go Go Go 98DF62
Desert CCAD60
Celadon Green 2F847C
Deep Carmine A9203E
Hollywood Cerise F400A1
Dark Violet 34013F
Deep Rose C74767
Light Red FF474C
Melrose C7C1FF
Mauve Taupe AF868E
Mud Green 606602
Dull Teal 5F9E8F
Medium Jungle Green 1C352D
Cherry Red F7022A
Light Yellowish Green C2FF89
Deep Peach, Peach (crayola) FFCBA4
Dark Taupe 7F684E
Light Prelude EBE4F4
Cadet Grey 91A3B0
Atomic Carmine Pink FA4F7B
Light Bitter Lemon E8F24E
Hot Magenta FF00CC
Light Neon Green 4EFD54
Brownish 9C6D57
Light Lavendar EFC0FE
Brownish Green 6A6E09
Agate Blue 44719B
Ivory FFFFF0
Gamboge E49B0F
Light Greenish Blue 63F7B4
Blue With A Hint Of Purple 533CC6
Light Amethyst Smoke D5CFDE
Lilac CEA2FD
Green 00FF00
Bright Lime Green 65FE08
Medium Sky Blue 76D7EA
Kiwi Green 8EE53F
Celestial Blue 4997D0
Black Currant 32293A
Dusty Teal 4C9085
Dadt Repeal Lavender 9D9FE9
Deep Sea 01826B
Greenish 40A368
Khazakstani Azure 00AFCA
Atomic Tangerine, Pink-orange FF9966
Kelley Green 009337
American Rose FF033E
Grey Pink C3909B
Grey Purple 826D8C
Fortune 7E73B8
Dusky Blue 475F94
Iceberg 71A6D2
Brilliant Electric Hollywood Cerise FF64C5
Medium Spring Green 00FA9A
Italian Apricot FFB280
Darkish Blue 014182
Grass Green 3F9B0B
Indigo Dye 1A5798
Light Froly FBBBC4
Light Lavender DFC5FE
Dirty Pink CA7B80
Light Earls Green E8E08E
Misty Rose FFE4E1
Light Blue-violet C77DF3
Electric Yellow, Yellow FFFF00
Deep Coffee 704241
Internet Puce 681C23
Mid Blue 276AB3
Green Sheen 6EAEA1
Cg Blue 007AA5
Brilliant Rose F653A6
Dove Cherry B11355
Lavender Rose FBA0E3
Manilla FFFA86
Bittersweet Shimmer BF4F51
Dark Imperial Blue, Indigo (dye) 00416A
Deep Yellow-green 57D200
Jasper D73B3E
Liver 534B4F
Light Light Green C8FFB0
Moonstone Blue 73A9C2
Jonquil, Naples Yellow, Royal Yellow, Stil De Grain Yellow FADA5E
Mellow Apricot F8B878
Candy Pink, Tango Pink E4717A
Deep Raspberry BA067A
Dartmouth Green 00703C
Bright Yellow Green 9DFF00
Milk Chocolate 7F4E1E
Lightish Blue 3D7AFD
Medium Brown 7F5112
Apricot FBCEB1
Dark Pastel Purple 966FD6
Brown Yellow B29705
Dirt 8A6E45
Little Boy Blue 6CA0DC
Cha Cha 8B3F43
French Tan A75502
Electric Blue 0652FF
Mandarin Red 0F3F00
Dirt Brown 836539
Fuchsia Red B33A7F
Hyacinth Violet 934D91
Light Medium Purple CDB7F0
Blue (crayola) 1F75FE
Deep Sea Blue 015482
May Green 4C9141
Khaki Green 728639
Mahogany 4A0100
Drab Green 749551
Dark Terra Cotta CC4E5C
Jade Green 00A86B
Lust E62020
Beaver 926F5B
Light Seance B96CCA
Dark Khaki 9B8F55
Floral White FFFAF0
Light Harlequin 9FFF7F
Lightgreen 76FF7B
Deep Lilac 966EBD
Dark Seafoam Green 3EAF76
Deep Green 02590F
Capri, Deep Sky Blue 00BFFF
Light California Gold FFD230
Cornflower 6A79F7
Fuzzy Wuzzy Brown C45655
Amethyst 9B5FC0
Light Deep Sea 1CC3B4
Fallow C19A6B
Han Purple 5218FA
Lapis Lazuli 26619C
Dark Hot Pink D90166
Booger 9BB53C
Dark Copper 71291D
Light Gulf Blue 3560A6
Electric Lime A8FF04
Leaf 71AA34
Diarrhea 9F8303
Battleship Grey 6B7C85
Lava CF1020
Chestnut, Indian Red CD5C5C
Medium Violet 8F5E99
Dark Sky Blue 448EE4
Bright Opera Mauve CD81B8
Burgundy 900020
Galaxy 755258
Amaranth E52B50
Meadow Mauve A55790
Light Sirocco B8C2C2
Hunter Green 0B4008
Light Teal Blue 18A7B5
Dark Olive 373E02
Clairvoyant 480656
Light Studio B89BDD
Deep Bronze 8C7853
Earls Green C9B93B
Dull Pink D5869D
Carolina Blue 8AB8FE
Light Chocolate D2691E
Lobelia 6700FF
Glitter E6E8FA
Muted Blue 3B719F
Eton Blue 96C8A2
Light Mauve C292A1
Deep Electric Blue 2C75FF
Crimson Red AD1022
Duke Blue 00009C
Blue/purple 5A06EF
Forest Green (traditional), Up Forest Green 014421
Minty Green 0BF77D
Mustard Green A8B504
Cerulean Blue 056EEE
Light Peach FFD8B1
Light Red Devil C51C56
Dark Slate Blue 214761
Aureolin FDEE00
Antique Fuchsia 915C83
Cobalt Blue 0047AB
Light Candlelight FEEF61
Las Palmas C6E610
Dusty Pink D58A94
Alloy Orange C46210
Cinnamon AC4F06
Light Lavender Mist F5F5FD
Deep Dark Indigo 503E5C
Dodger Blue 3E82FC
Davy's Grey 555555
Area 51 3C8043
Light Sage BCECAC
Lightish Green 61E160
Lavender Affair 714693
Heliotrope D94FF5
Bright Olive 9CBB04
Blush Light DE98B2
Harvard Crimson C90016
Mindaro E3F988
Dark Salmon C85A53
Bazaar Light CFBCBE
Bluegreen 017A79
Moody Blue 7F76D3
Muted Lime D6CE7B
Easter Green 8CFD7E
Imperial 602FFB
Dark Bronze 52503C
Dark Electric Blue 536878
Lavender Lustre 8A9CC4
Light Rose FFC5CB
Light Cornflower Blue 93CCEA
Irish Green 019529
Electric Lime, Fluorescent Yellow, French Lime CCFF00
Light Moody Blue C1BBEC
Blue Indigo 485470
Bright California Gold FE9D04
Dust Storm E5CCC9
Deep Taupe 715F6D
Lighter Purple A55AF4
Bright Light Green 2DFE54
Carmine Rose DD5A91
Bright Indigo 5D76CB
Cordovan 893F45
Dull Pallid Rose E7BCB4
Blue Sapphire 126180
Jet 343434
Magnolia F8F4FF
Marine Blue 01386A
Medium Lime Green A1C42C
Green (ncs) 009F6B
Japanese Violet 5B3256
Dark Maroon 3C0008
Lime Green 89FE05
Khaki AAA662
Light Sea Green 98F6B0
Dusty Green 76A973
Bahia A5CB0C
Bright Mallow Pink D498CB
Carminorubaceous BC2350
Mauve Orchid B37E9A
Iris 6258C4
Chinese Red AA381E
Mexican Rose E4007C
Muddy Green 657432
Fluro Green 0AFF02
Light Frosted Mint F0FFFC
Gulf Blue 051657
Bright Magenta Rose FC51AF
Bright Sky Blue 02CCFE
Light Indigo 6D5ACF
Fuchsia ED0DD9
Amour Light FDF6FA
Metallic Sunburst 9C7C3B
Electric Vermilion FF3F00
Light Puce FFAFC8
Icterine FCF75E
Flirt A2006D
Brick Orange C14A09
Caramel AF6F09
Dark Cherry A23B3C
Chocolate 3D1C02
Fuchsia Purple CC397B
Cool Blue 4984B8
Dusky Orchid 9C7687
Light Nightclub B10097
Gross Green A0BF16
Malachite 0BDA51
Lemon Green ADF802
French Blue 436BAD
Candlelight FCD917
Greenish Yellow CDFD02
Automotive Amber FF7E00
Magenta FF00FF
Kiwi 9CEF43
Antique Bronze 665D1E
Deep Carmine Pink-orange E53D43
Boston University Red CC0000
Bone E3DAC9
Lipstick D5174E
Carmine Red FF0038
Light Carmine Pink F26476
Mud 735C12
Air Force Blue (usaf 00308F
Bay Leaf 7DA98D
Lincoln Green 195905
Light Slate Gray 778899
Classic Rose FBCCE7
Lightish Purple A552E6
Dull Yellow EEDC5B
Dark Blue 00035B
La Salle Green 087830
Green/blue 01C08D
Deep Sky Blue 0D75F8
Greeny Yellow C6F808
Brown Green 706C11
Lime Pulp D1E189
Medium Candy Apple Red E2062C
Heather A484AC
Light Melrose E7E4EF
Medium Chartreuse B0BC4A
Indian Yellow E3A857
Brandy Rose Light E1C7C3
Mellow Mauve 99637B
Lavender Indigo 9457EB
Fuchsia Pink FF77FF
Mulberry C54B8C
Dark Cyan 0A888A
Maroon 650021
Chartreuse C1F80A
Can Can Light EBCBD6
Lavender (floral) B57EDC
Dull Mauve A17E9A
French Lime 9EFD38
Lemon FDFF52
Citron 9FA91F
Argentinian Azure 74ACDF
Candy Apple Red FF0800
Bluey Grey 89A0B0
Dark Slate Gray 2F4F4F
Dull Pallid Rose Light F5E2DE
Duck Egg Blue C3FBF4
Auburn 9A3001
Dark Lime 84B701
Cerulean Frost 6D9BC3
Glaucous 6082B6
Dark Tan AF884A
Charleston Green 1A2118
Deep Rich Lavender 9955BB
Brown Orange B96902
Blue/grey 758DA3
Brown (traditional) 964B00
Bright Yellow FFFD01
Hawaiian Tan 9D5616
Meat Brown E5B73B
Light Pastel Green B2FBA5
Medium Bronze 6C541E
Chantilly Light FCE5F2
Goldenrod DAA520
Bluegrey 85A3B2
Maize FBEC5D
Melon FF7855
Flirt Light D500B6
Charcoal Grey 3C4142
Cadet 536872
Maui Blue 4EA4BA
Japanese Blue-green 3A6960
Dusky Pink CC7A8B
Deep Carmine Rose BB466F
Earth A2653E
Grey Brown 7F7053
Light Medium Apricot F7CDB9
Lemon Meringue F6EABE
Cocoa 875F42
Light Night Shadz D98AA8
Light Urple B36FF6
Halaya Ube 663854
Chrome Yellow FFA700
Bright Red-violet D02090
Darkish Pink DA467D
Lighter Green 75FD63
Azul 1D5DEC
Crystal Rose F9C8CB
Electric Purple AA23FF
Deep Blue 040273
Light Electric Hollywood Cerise FF9EDA
Bright Light Blue 26F7FD
Carnation Pink FF7FA7
Light Lilac EDC8FF
Light Green 96F97B
Light Yellow FFFE7A
Lilac Luster AE98AA
Brass B5A642
Deep Rose Taupe 7E5E60
Burnt Red 9F2305
Cadmium Red E30022
International Klein Blue 002FA7
Medium Taupe Gray 88786A
Light Amazon AEBEA6
Fulvous E48400
Faded Blue 658CBB
Imperial Blue 002395
Light Goldenrod Yellow FAFAD2
Licorice 1A1110
Mountbatten Pink 997A8D
Electric Hollywood Cerise FF009F
Citrus A1C50A
Khaki (html/css) (khaki) C3B091
Ghost White F8F8FF
Limerick 9DC209
Light Apricot FDD5B1
Cool Black 002E63
Chocolate (traditional) 7B3F00
Chartreuse Green 7FFF00
Magic Mint AAF0D1
Fawn E5AA70
Deep Periwinkle 8080FF
Camo 7F8F4E
Coffee 4B3621
Light Las Palmas E6F554
Lavender Blush FFF0F5
Light Orchid E29CD2
Electric Pink FF0490
Light Bluish Green 76FDA8
Baby Shit Brown AD900D
Logan AAA9CD
Deep Lemon F5C71A
Cabaret Light EF9BB9
Barf Green 94AC02
Light Pale Plum F1D4F1
Ferrari Red FF2800
Dogwood Rose D71868
Copper B66325
Light Medium Violet CAABD0
Muddy Yellow BFAC05
Jade 1FA774
Aqua Green 12E193
Cadillac B04C6A
Japanese Atomic Carmine Pink EC5486
Indian Red 850E04
Bulgarian Rose Light 9A393D
Black 000000
Hooker's Green 49796B
Bluey Purple 6241C7
Leaf Green 5CA904
Cameo Pink EFBBCC
Blue Bell A2A2D0
Bistre 3D2B1F
Aqua Marine 2EE8BB
Harlequin 3FFF00
International Orange (engineering) BA160C
Hot Pink FF028D
Hot Green 25FF29
Gray-asparagus 465945
Internet Purple 7D26CD
Fluorescent Green 08FF08
Dizzy Lizzy 80CA2B
Light Burgundy A8415B
Deep Apricot E09214
Light French Lavender A899E6
Beau Blue, Pale Aqua BCD4E6
June Bud BDDA57
Dark Teal 014D4E
Lavender Blue 8B88F8
Aqua Blue 02D8E9
Chantilly F8C3DF
Light Lime Green B9FF66
Greenish Brown 696112
Han Blue 446CCF
Grassy Green 419C03
Almost Mauve EBE1DF
Grey/blue 647D8E
Magenta Rose FF00AF
Lavender Brown AA8A9E
Mossy Green 638B27
Azure 007FFF
Indigo Blue 3A18B1
Cosmic 763950
Cool Grey 95A3A6
Emerald, Paris Green 50C878
Medium Mauve D399E6
Muted Pink D1768F
Light Pastel Purple B19CD9
Copper Rust 944747
Medium Puce 8E616A
Medium Goldenrod FCD667
Android Green A4C639
Flat Blue 3C73A8
Clay Brown B2713D
Dusky Purple 895B7B
Mustard FFDB58
Easter Purple C071FE
Light Taupe B38B6D
Electric Ultramarine 3F00FF
Feldgrau 4D5D53
Custard FFFD78
Faded Yellow FEFF7F
Icky Green 8FAE22
Light Keppel 8DDCD3
Light Limerick D2E543
Gunmetal 536267
Dark Lime Green 7EBD01
Honolulu Blue 007FBF
Dark Pastel Red C23B22
Dark Grey 363737
Bronze CD7F32
Blue 0000FF
Deep Cerise DA3287
Light Seafoam Green A7FFB5
Carnelian, Cornell Red B31B1B
Dark Jungle Green 1A2421
Light Mindaro F3FDC6
Medium Deep Lavender 9966CC
Emerald Green 028F1E
Cyan 00FFFF
Dark Goldenrod B8860B
Isabelline F4F0EC
Cal Poly Green 1E4D2B
Light Olivetone B8B654
Medium Lavender Grey C8A2C8
Bile B5C306
Light Salmon Pink FF9999
Blue-green 0D98BA
Dusk Blue 26538D
Mid Green 50A747
Flax EEDC82
Kermes C3103A
Laurel Green A9BA9D
Burnt Sienna, Light Red Ochre E97451
Dull Purple 84597E
Deep Indigo 8A2BE2
Light Lavender Affair B898CD
Blanched Almond FFEBCD
British Apricot E18731
Dark Fuchsia 9D0759
Dark Chestnut 986960
Carissma Light F6C6D8
Lima 76BD17
Dull Green 74A662
Deep Teal 00555A
Lime BFFF00
Celeste (colour) B2FFFF
Dull Orange D8863B
Claret 680018
Dark Seafoam 1FB57A
Dark Aqua 05696B
Marine 042E60
Mountain Meadow 30BA8F
Eggshell Blue C4FFF7
Cabaret D94972
French Plum 811453
Light Starship F7FA97
Light Turquoise 7EF4CC
Bright Blush E47698
Green (crayola) 1CAC78
Deep Pink, Fluorescent Pink FF1493
Black Rose 67032D
Greyish Brown 7A6A4F
Dark Spring Green 177245
Dark Magenta 960056
Cherry CF0234
Clear Blue 247AFD
Bland AFA88B
Blue Purple 5729CE
Deep Azure 0066FF
Medium Harlequin 44944A
Dark Brown 341C02
Golden FFD700
International Orange (aerospace) FF4F00
Dark Plum 3F012C
Medium Red-violet BB3385
Barney Purple A00498
Black Bean 3D0C02
Mauve Chalk E4D3D2
Copper Canyon Light C08D5E
Blood Red 980002
Barn Red 7C0A02
Bright Purple BE03FD
Mauve Taupe, Raspberry Glace 915F6D
Baby Green 8CFF9E
Medium Winter Pear B0B583
Burnt Yellow D5AB09
Light Seafoam A0FEBF
Bright Tyrian Purple B80049
Dark Indigo 1F0954
Alizarin Crimson, Rose Madder E32636
Forest 0B5509
Banana FFFF7E
Green (munsell) 00A877
Light Red-violet E79EC5
Brilliant Harlequin 7FFF55
Green-yellow ADFF2F
Brownish Red 9E3623
Light Magenta FA5FF7
Light Blue 95D0FC
Electric Green 21FC0D
Light Khaki F0E68C
Bright Orange FF5B00
Fern 63A950
Light Forest Green 4F9153
Light Bahia D6E94B
Hibiscus B6316C
Light Light Blue CAFFFB
Dull Red BB3F3F
Burlywood DEB887
Light Cyan ACFFFC
Bole, Medium Tuscan Red 79443B
Dark Pastel Green 56AE57
Light Italian Apricot FFD9B2
Key Lime AEFF6E
Ceil 92A1CF
Light Pea Green C4FE82
Greyish Blue 5E819D
Dark Pink CB416B
Earth Yellow E1A95F
Melanie E4C2D5
Light Purple BF77F6
Foobar F00BA4
Mint Cream F5FFFA
Berry 990F4B
Mantis 74C365
Light Pale Red-violet F0B7CD
Browny Green 6F6C0A
Light Royal Heath D987B9
Barney AC1DB8
Butter FFFF81
Greenish Beige C9D179
Flamingo Pink FC8EAC
Light Buddha Gold E4D430
Blush DE5D83
Honeydew F0FFF0
Deep Aqua 08787F
Cal Poly Pomona Gold C6930A
Ce Soir 9771B5
Light Orange FDAA48
Deep Pink Light FF5CCD
Light Lime AEFD6C
Disco 871550
Flat Green 699D4C
Light Lima BBE261
Lemon Yellow FDFF38
Magic Potion FF4466
Light Olive ACBF69
British Racing Green 05480D
Bubblegum FF6CB5
Bitter Lemon CAE00D
French Apricot E67E30
Lilac Shadow 56365C
Brandeis Blue 0070FF
Bubble Gum FFC1CC
Chamoisee A0785A
Badass BADA55
Clay B66A50
Deep Magenta A0025C
Frog Green 58BC08
Ice Blue D7FFFE
Light Zydeco 25937E
Daffodil FFFF31
Islamic Green 009900
Medium Periwinkle 7A83BF
Deep Carrot Orange E9692C
French Chartreuse C2F732
Greeny Blue 42B395
Cadmium Yellow FFF600
Lavender Grey C4C3D0
Bright Sea Green 05FFA6
Light Navy Blue 2E5A88
Mocha 9D7651
Msu Green 18453B
Greyish A8A495
Browny Orange CA6B02
Darkblue 030764
Lavender Purple 967BB6
Indigo 6F00FF
Mud Brown 60460F
Big Dip O'ruby 9C2542
Barbie Pink FE46A5
Frosted Mint DBFFF8
Burnt Umber A0450E
Emerald 01A049
Aztec Gold C39953
Fandango B53389
Hazel 8E7618
Ash Grey B2BEB5
Blue Blue 2242C7
Midnight Green 004953
Deep Orchid 94417B
Camouflage Green 4B6113
Deep Mauve Pink E4BEEB
Mauvette E8CCD7
Grayish Sea Green 8FBC8F
Dark Cerulean 08457E
Grapefruit FD5956
Medium Sea Green 3CB371
Light Lavender Magenta 0F8C3F
Bittersweet FE6F5E
Brown 653700
Light Selago F9F8FE
Moss Green 658B38
Blue Atoll 00B6D9
Medium Green 39AD48
Deep Chestnut B94E48
Deep Pink CB0162
Brick A03623
Japanese Plum 8F4155
Celadon ACE1AF
Lavender Magenta, Violet (web) EE82EE
Grey 808080
Muted Green 5FA052
Algae Green 21C36F
Dark Gold B59410
Greenblue 23C48B
Denim Blue 3B5B92
Light Moss Green A6C875
Medium Cerise 9F1F4C
Harvest Gold DA9100
Chetwode Blue 8581D9
Mystic Maroon AD4379
Light Peru EAC592
Banana Mania FAE7B5
Dark Lavender 856798
Faded Pink DE9DAC
Copper Penny AD6F69
Light Tan FBEEAC
Light Lavender Blush FFF9FB
Dark Yellow D5B60A
Dark Navy 000435
Dark Coral CF524E
Majorelle Blue 6050DC
Bright Blue 0165FC
Anti-flash White F2F3F4
Faded Purple 916E99
Dark Cream FFF39A
Metallic Blue 4F738E
Dark Sea Green 11875D
Amaranth Deep Purple 9F2B68
Copper Rose 996666
Buddha Gold C1A004
Crimson Lake 670F25
Bubbles E7FEFF
French Wine AC1E44
Ecru CDB891
Deep Blush B44668
Flavescent F7E98E
Bright Cyan 41FDFE
Copper Rust Light CD9999
Charcoal 343837
Air Superiority Blue 72A0C1
Dark Orchid 9932CC
Blue (ryb) 0247FE
Dark Beige AC9362
Faded Orange F0944D
Marigold FCC006
Dark Mint 48C072
Light Peach Puff FFF0E0
Light Perfume EBE3FC
Debian Red D70A53
Light Gray D3D3D3
Medium Tyrian Purple 990024
Charm Light EDBACD
Mu Gold F1B82D
Moss 769958
Mauvelous EF98AA
Aqua 13EAC9
Deep Lavender Blue-gray B2A4D4
Fluorescent Lime C7FF00
Boring Green 63B365
Light Teal 90E4C1
Bright Magenta FF08E8
Military Green 667C3E
Chinese Purple A757AB
French Mauve D473D4
Dusty Rose C0737A
Bright Lime 87FD05
Cranberry 9E003A
Light Peru Tan C18D25
Fire Engine Red FE0002
Dark Mustard A88905
Light Blue Grey B7C9E2
Blast Off Bronze A57164
Light Sky Blue C6FCFF
Drab 828344
Light Jungle Mist DEEBEC
Mango FFA62B
Dull Deep Lavender 9C87CD
Lime Yellow D0FE1D
Macaroni And Cheese EFB435
Fandango Pink DE5285
French Peach FDBFB7
Greyish Teal 719F91
Mallow Purple A8516E
Foam Green 90FDA9
Lavender Pink DD85D7
Banana Yellow FAFE4B
Mardi Gras 880085
Deep Lemon Yellow FEE600
Cardinal C41E3A
Amaranth Cerise CD2682
Amethyst Smoke A397B4
Canary Yellow FFFE40
Cg Red E03C31
Light Logan D9D8EA
Mustard Yellow D2BD0A
Deep Medium Red-violet A65E7E
Charm D47494
Grullo A99A86
Darkish Purple 751973
Light Japanese Violet 7058A3
Brilliant Lavender, Electric Lavender F4BBFF
Deep Brown 410200
Dark Vivid Violet 8F509D
Gossamer 069B81
Light Grey D8DCD6
Light Mustard F7D560
Blueberry 464196
Green Brown 544E03
Medium Carmine, Pale Carmine AF4035
Carrot Orange ED9121
Light Edgewater E7F3EB
Light Bright Green 53FE5C
Bright Cerulean 02A4D3
Brilliant Copper B87B6B
Medium Bright Lavender D19FE8
Bruise 7E4071
Light Persian Plum B76969
Lemon-lime E3FF00
Candy Pink FF63E9
Cupid Light FDE3F0
Deep Harlequin 33CC00
Amber FEB308
Ebony 555D50
Fern Green 548D44
Deep Cherry Red DA2647
Medium Blue 2C6FBB
Myrtle 21421E
Deep Ruby 843F5B
French Fuchsia FD3592
Baby Blue A2CFFE
Hot Pink Light FFB3DE
Deep Magenta Rose C120A2
Kermit Green 5CB200
Heather Violet DE4C8A
Crimson Cherry 713036
Fresh Green 69D84F
Byzantine BD33A4
Ball Blue 21ABCD
Japanese Crimson C327B2
Bright Maroon, Maroon (crayola) C32148
Blond FAF0BE
Dark Mardi Gras 350036
Japanese Indigo 264348
Bright Lavender C760FF
Burnt Siena B75203
Light Bubbles F5FFFF
Firebrick B22222
Go Bunny Go C2E20D
Lavender Frost BAA8C1
Blue Gray 6699CC
Bluish 2976BB
Hot Purple CB00F5
Forest Green (web) 228B22
Burnt Orange C04E01
Green Blue 06B48B
Charm Pink, Light Thulian Pink E68FAC
Girlsenberry CC5555
Coral FF7F50
Fuzzy Wuzzy CC6666
Dollar Bill 85BB65
Celery C1FD95
Dingy Dungeon C53151
California Gold A98F64
Cream FFFDD0
Dusty Purple 825F87
Linen FAF0E6
Chinese Apricot E69966
Apple Green 8DB600
Mustard Brown AC7E04
Gainsboro DCDCDC
Crimson DC143C
Darkish Green 287C37
Dusty Red B9484E
Aurometalsaurus 6E7F80
Light Green Blue 56FCA2
Light Aqua 8CFFDB
Light Watusi FFF1EB
Moonlite Mauve D290B5
Merlot 730039
Cerise DE0C62
Green Chartreuse Liqueur BFBF6C
Dark 1B2431
Dark Forest Green 002D04
Dusty Orange F0833A
Beige F5F5DC
Greeny Grey 7EA07A
Dahlia Mauve A85387
Mauve Morn EBDBDB
Ku Crimson E8000D
Antique Brass C88A65
Cadmium Green 006B3C
Forget-me-not 000181
Medium Yellow FCE883
Light Shiraz DD4384
Dusk 4E5481
Caput Mortuum 592720
Laser Lemon FEFE22
English Rose F5C5C2
Bluey Green 2BB179
Dust B2996E
Deep Lavender 8D5EB7
Black Leather Jacket 253529
Light Peach Schnapps FFF0EE
Light Plum 9D5783
Bright Navy Blue 0066CC
Boysenberry 873260
Dusty Lavender AC86A8
Dark Tangerine FFA812
Australian Mint EFF7AA
Cobalt 1E488F
Can Can D591A4
Bright Teal 01F9C6
Blue Grey 607C8E
Japanese Carmine 902933
Amour F9EAF3
Dark Green Blue 1F6357
Medium Vermilion, Vermilion (plochere) D9603B
Light Grey Blue 9DBCD4
Brilliant Red-violet EB6FCB
Fuchsia Rose C74375
Falu Red 801818
Light Periwinkle C1C6FC
Medium Orchid BA55D3
Murky Green 6C7A0E
Azure Mist/web F0FFFF
Baby Poop 937C00
Dirty Yellow CDC50A
Jungle Green 048243
Mellow Yellow F8DE7E
Cotton Candy FFBCD9
Blood Orange FE4B03
Black Olive 3B3C36
Blue Violet 5D06E9
Deep Purple 36013F
Lightish Red FE2F4A
Dark Red 840000
Forrest Green 154406
Light Royal Blue 3A2EFE
Medium Spring Bud C9DC87
Coffee, Tuscan Brown 6F4E37
Keppel 3AB09E
Lemon Chiffon FFFACD
Carmine 9D0216
Mikado Yellow FFC40C
Bright Violet AD0AFD
Classic Rose Light FDE9F5
Copper Green 679267
Gold DBB40C
Cadmium Orange FF6103
Brownish Yellow C9B003
Light Hibiscus DF84B5
Byzantium 702963
Lemon Lime BFFE28
Citrine E4D00A
Dandelion F0E130
Light Pink FFD1DF
Deep Orange DC4D01
Muddy Brown 886806
Desert Sand EFCDB8
Edgewater CBE3D7
Bright Mauve Mist C49FBA
Dark Candy Apple Red A40000
Dark Periwinkle 665FD1
Baby Purple CA9BF7
Gray (x11 Gray) BEBEBE
Coral Pink F88379
Dark Scarlet 560319
Cadillac Light DC9DB3
Golden Rod F9BC08
Carmine Pink EB4C42
Deep Red 9A0200
Cinnabar, Vermilion (cinnabar) E34234
Light Melanie F4E5ED
Cornflower Blue 5170D7
Greyish Pink C88D94
Green/yellow B5CE08
Medium Champagne, Vanilla F3E5AB
Dark Royal Blue 02066F
Dusky Rose BA6873
Lilac Bush 9874D3
Light Yellow Green CCFD7F
Lime Punch BFD833
French Raspberry C72C48
Jazzberry Jam A50B5E
Light Violet D6B4FC
Canary FDFF63
Dark Purple 35063E
Mint 9FFEB0
Light Lilac Bush CFBAEC
Light Ce Soir CFB8DE
Deer BA8759
Light Fuchsia Pink F984EF
Deep Carmine Red BC3F4A
Medium Aquamarine 66DDAA
Jagger 350E47
Midnight Blue 020035
Burple 6832E3
Dark Mauve 874C62
Brick Red CB4154
Aquamarine 7FFFD4
Bluish Purple 703BE7
La Rioja B3C110
Golden Poppy FCC200
Army Green 4B5320
Dark Orange FF8C00
Flame E25822
Baby Poop Green 8F9805
Lavender Blue-grey BDBBD7
Lavender Mist E6E6FA
Froly F57584
Desert Taupe A18178
Dark Peach DE7E5D
Bright Aqua 0BF9EA
Grey Green 789B73
Inchworm B2EC5D
Alice Blue F0F8FF
Almond EFDECD
Columbia Blue 9BDDFF
Grape Purple 5D1451
Catalina Blue 062A78
Deep Pink-orange FF7077
Light Citron D3D86E
French Lilac 86608E
Electric Crimson FF003F
Bright Turquoise 0FFEF9
Light Grey Green B7E1A1
Leather AC7434
Creme FFFFB6
Grey/green 86A17D
Bright Mauve Glow D0868E
Copper Yellow C9AE5D
Medium Burgundy 63313A
Antique White, Moccasin FAEBD7
Dark Olive Green 3C4D03
Amber, Fluorescent Orange FFBF00
Brandy Rose BB8983
Light Chetwode Blue C5C2EF
Light Hawaiian Tan D2A560
Caribbean Green 00CC99
Green Teal 0CB577
Medium Lemon F5E25C
Light Silver Sand E3E4E5
Fiery Rose FF5470
Grey Blue 6B8BA4
Light Gold FDDC5C
Light Veronica D46FF9
Light Disco C65EA0
Cerulean 007BA7
Lavender Blue, Periwinkle CCCCFF
Medium Turquoise 48D1CC
Algae 54AC68
Chocolate Brown 411900
Greenish Blue 0B8B87
Medium Grey 7D7F7C
Aubergine 3D0734
Air Force Blue 5D8AA8
India Green 138808
Light Navy 155084
Energy Yellow F8DD5C
Amazon 3B7A57
Mystic Pearl D65282
Baby Blue Eyes A1CAF1
Mordant Red 19 AE0C00
Bright Raspberry Rose C7458B
Dirty Blue 3F829D
Italian Electric Blue 003399
Baby Shit Green 889717
Avocado Green 87A922
Apple 6ECB3C
Claret Violet 6E1C34
Golden Brown B27A01
Amaranth Magenta ED3CCA
Coquelicot FF3800
Greenish Cyan 2AFEB7
Bright Red FF000D
Cerise, Cherry DE3163
Lavender C79FEF
Medium Bitter Lemon E5DE43
Maya Blue 73C2FB
Fluorescent Pink FF008F
Deauville Mauve AD9194
Groovy 5CA345
Green Grey 77926F
Bright Pink FE01B1
Mystic Lilac B695C0
Greeny Brown 696006
Brown Grey 8D8468
Blizzard Blue ACE5EE
Carmine Ruby F03D6F
Booger Green 96B403
Light Mint B6FFBB
Dark Turquoise 045C5A
Light Papaya Whip FFF8ED
Kelly Green 02AB2E
Kobi E79FC4
Green (ryb) 66B032
Crimson Glory BE0032
Bottle Green 044A05
Bluish Green 10A674
Lightblue 7BC8F6
Dark Erin 3B7861
Lime Cream DDECC0
Light La Rioja DDE454
Light Citrus D4E646
International Orange (golden Gate Bridge) C0362C
Light Brown AD8150
Asparagus 77AB56
Bondi Blue 0095B6
Mushroom BA9E88
Lemon Curry CCA01D
Blackberry 4D0135
Greenish Tan BCCB7A
Eggshell FFFFD4
Deep Carmine Pink EF3038
Dark Byzantium 5D3954
Dark Midnight Blue 003366
Mauvewood AB5C6D
Chestnut 742802
Mauve Wine 5D3B4C
Light Violent Violet 7B4BAB
Golden Yellow FEC615
Granny Smith Apple A8E4A0
Jungle Mist B4CFD3
Mauve AE7181
Black Rose Light B12B7F
Mint Green 8FFF9F
Bubblegum Pink FE83CC
Chinese Violet 856088
Languid Lavender D6CADD
Cadet Blue 5F9EA0
Blurple 5539CC
Mogul 8C82B6
Bluish Grey 748B97
Light Mint Green A6FBB2
Denim 1560BD
Ice D6FFFA
Blue Green 137E6D
Medium Purple 9370DB
Mauve Shadows B89CA9
Darkgreen 054907
Medium Sapphire 2D5DA1
Almost Black 070D0D
Medium Carmine Pink C08181
Light Gossamer 39D1C2
Light Blue Green 7EFBB3
Eggplant Purple 430541
Arylide Yellow, Hansa Yellow E9D66B
Medium Rich Magenta C32350
Blue/green 0F9B8E
Cerise Pink EC3B83
Eggplant 380835
Alabama Crimson A32638
Bulgarian Rose 480607
Brownish Grey 86775F
Light Olive Green A4BE5C
Dark Grass Green 388004
Dull Blue 49759C
Burnt Sienna B04E0F
Green Yellow C9FF27
Chartreuse Yellow DFFF00
Dim Grey 696969
Medium Apricot EB9373
Dirty Purple 734A65
Kenyan Copper Light BF6935
Medium Raspberry C077A6
Moccasin FFE4B5
Dark Mint Green 20C073
Dark Pastel Blue 779ECB
Greenish Turquoise 00FBB0
Carissma EA88A8
Medium Pink FFB6C1
Light Cosmic BB8CAA
Green Apple 5EDC1F
Metallic Gold D4AF37
Cornsilk FFF8DC
Baby Pink FFB7CE
Copper Canyon 7E3A15
Brownish Purple 76424E
Dirty Orange C87606
Dark Blue Green 005249
Light Kobi F5D3E6
Baby Puke Green B6C406
Arsenic 3B444B
Medium Azure 458AC6
Dusty Blue 5A86AD
Light Salmon FEA993
Kenyan Copper 7C1C05
Dark Navy Blue 00022E
Butter Yellow FFFD74
Baby Poo AB9004
Dark Raspberry 872657
Light Crimson F56991
Brownish Orange CB7723
Dark Grey Blue 29465B
Lipstick Red C0022F
Hospital Green 9BE5AA
Deep Tuscan Red 66424D
Generic Purple 660099
Grey Teal 5E9B8A
Liliac C48EFD
Midnight 03012D
Highlighter Green 1BFC06
Cerise Magenta DA3163
Light Silver Chalice DADADA
Evergreen 05472A
Butterscotch FDB147
Greyish Green 82A67D
French Rose F64A8A
Dark Sage 598556
Light Medium Orchid D39BCB
Aquamarine Blue 71D9E2
Gold Fusion 85754E
Champagne F7E7CE
Greenish Teal 32BF84
Cement A5A391
Amaranth Purple AB274F
Grape 6C3461
French Lavender 9683EC
Egyptian Blue 1034A6
Dark Lilac 9C6DA5
Dark Green 033500
Light Maroon A24857
Dark Carmine 841B2D
Deep Carmine Ruby BE3262
Japanese Purple 4F284B
Light Aquamarine 7BFDC7
Dirty Green 667E2C
Dark Rose B5485D
Greyblue 77A1B5
Jade Lime A5CA77
Medium Slate Blue 7B68EE
Dark Yellow Green 728F02
Greyish Purple 887191
Blue (ncs) 0087BD
Cupid FBBEDA
Faded Red D3494E
Mauve Glow AD8884
Mango Tango FF8243
Cambridge Blue A3C1AD
Light Coral F08080
Blue (munsell) 0093AF
Light Eggplant 894585
Bordeaux 7B002C
Lichen 8FB67B
English Lavender B48395
Avocado 568203
Greenish Grey 96AE8D
Bleu De France 318CE7
Deep Crimson AD1C42
Buff F0DC82
Dull Brown 876E4B
Faded Green 7BB274
Cinerous 98817B
Dark Sapphire 082567
Cloudy Blue ACC2D9
Bazaar 98777B
Grass 5CAC2D
Deep Violet 490648
Darkish Red A90308
Carnation F95A61
Folly FF004F
Dark Aquamarine 017371
Bright Lilac C95EFB
Light Acapulco BFDCD4
Bright Green 01FF07
French Puce 4E1609
Light Beige FFFEB6
Medium Girlsenberry AA786D
Medium Electric Blue 035096

In the HashQP class:

Table Size = 3203
Number of entries = 1294
Load factor = 0.4039962535123322
Number of collisions = 376
Longest Collision Run = 7

Testing hash tables, please enter the same filename just used, 
Enter the input filename: HW5 Input.txt
Result of calling contains for Agate Blue in HashSC = true
Result of calling contains for Agate Blue in HashQP = true
Retrieved in HashSC, Color: Agate Blue, now trying to delete it
Successfully removed from HashSC: Agate Blue
Retrieved in HashQP, Color: Agate Blue, now trying to delete it
Successfully removed from HashQP: Agate Blue
HashSC with the int key now has:
000000 Black
CF0234 Cherry
1A5798 Indigo Dye
7EFBB3 Light Blue Green
C9D179 Greenish Beige
CAABD0 Light Medium Violet
00A877 Green (munsell)
95D0FC Light Blue
D6CE7B Muted Lime
2EE8BB Aqua Marine
735C12 Mud
FF5B00 Bright Orange
9A0200 Deep Red
25937E Light Zydeco
FF9966 Atomic Tangerine, Pink-orange
66B032 Green (ryb)
044A05 Bottle Green
A99A86 Grullo
B04E0F Burnt Sienna
028F1E Emerald Green
B8C2C2 Light Sirocco
568203 Avocado
F5D3E6 Light Kobi
DEEBEC Light Jungle Mist
ED9121 Carrot Orange
76FDA8 Light Bluish Green
AC4F06 Cinnamon
B57EDC Lavender (floral)
CBE3D7 Edgewater
CB6D51 Copper Red
811453 French Plum
B8B654 Light Olivetone
758DA3 Blue/grey
F19CBB Amaranth Pink
E4BEEB Deep Mauve Pink
C1C6FC Light Periwinkle
048243 Jungle Green
035096 Medium Electric Blue
062A78 Catalina Blue
1034A6 Egyptian Blue
20C073 Dark Mint Green
4997D0 Celestial Blue
843F5B Deep Ruby
FFF0E0 Light Peach Puff
5218FA Han Purple
3A6960 Japanese Blue-green
AF868E Mauve Taupe
FFB280 Italian Apricot
0F8C3F Light Lavender Magenta
00FFFF Cyan
F64A8A French Rose
ACBF69 Light Olive
2BB179 Bluey Green
0B5509 Forest
F1D4F1 Light Pale Plum
FFBF00 Amber, Fluorescent Orange
4D0135 Blackberry
C4FE82 Light Pea Green
94AC02 Barf Green
FAEBD7 Antique White, Moccasin
FFF0EE Light Peach Schnapps
FF9999 Light Salmon Pink
F26476 Light Carmine Pink
247AFD Clear Blue
F2F3F4 Anti-flash White
FFF0F5 Lavender Blush
BB3F3F Dull Red
B22222 Firebrick
658B38 Moss Green
FFFD74 Butter Yellow
D5B60A Dark Yellow
26619C Lapis Lazuli
AA23FF Electric Purple
BE0032 Crimson Glory
AEFD6C Light Lime
FFFD78 Custard
A2006D Flirt
7D7F7C Medium Grey
6EAEA1 Green Sheen
FFA62B Mango
A40000 Dark Candy Apple Red
CDB7F0 Light Medium Purple
1F75FE Blue (crayola)
B0C4DE Light Steel Blue
D2BD0A Mustard Yellow
873260 Boysenberry
FFD1DF Light Pink
FBA0E3 Lavender Rose
FFCBA4 Deep Peach, Peach (crayola)
836539 Dirt Brown
996666 Copper Rose
004B49 Deep Jungle Green
A3C1AD Cambridge Blue
9DBCD4 Light Grey Blue
1F6357 Dark Green Blue
7C1C05 Kenyan Copper
BCD4E6 Beau Blue, Pale Aqua
C6F808 Greeny Yellow
9B5FC0 Amethyst
01F9C6 Bright Teal
F8F8FF Ghost White
E3E4E5 Light Silver Sand
FDBFB7 French Peach
0BF77D Minty Green
01386A Marine Blue
C45655 Fuzzy Wuzzy Brown
5170D7 Cornflower Blue
FEB308 Amber
FFE4B5 Moccasin
05696B Dark Aqua
D58A94 Dusty Pink
C88D94 Greyish Pink
A2A2D0 Blue Bell
FF4F00 International Orange (aerospace)
F5FFFA Mint Cream
A8FF04 Electric Lime
916E99 Faded Purple
86775F Brownish Grey
C41E3A Cardinal
E69966 Chinese Apricot
F5FFFF Light Bubbles
E5DE43 Medium Bitter Lemon
BC3F4A Deep Carmine Red
50C878 Emerald, Paris Green
FCC006 Marigold
8B88F8 Lavender Blue
4C9141 May Green
BDBBD7 Lavender Blue-grey
699D4C Flat Green
BE3262 Deep Carmine Ruby
4B5320 Army Green
00009C Duke Blue
CC4E5C Dark Terra Cotta
555D50 Ebony
464196 Blueberry
87A922 Avocado Green
3A18B1 Indigo Blue
71D9E2 Aquamarine Blue
F7CDB9 Light Medium Apricot
990F4B Berry
02066F Dark Royal Blue
FFFDD0 Cream
020035 Midnight Blue
880085 Mardi Gras
FCD917 Candlelight
FFE4E1 Misty Rose
F9F8FE Light Selago
0070FF Brandeis Blue
FFD230 Light California Gold
DFFF00 Chartreuse Yellow
F400A1 Hollywood Cerise
EEDC5B Dull Yellow
825F87 Dusty Purple
DDECC0 Lime Cream
41FDFE Bright Cyan
9CEF43 Kiwi
C9B93B Earls Green
DE3163 Cerise, Cherry
AC1DB8 Barney
FBCCE7 Classic Rose
AD8150 Light Brown
34013F Dark Violet
FEFE22 Laser Lemon
DBFFF8 Frosted Mint
B7C9E2 Light Blue Grey
56AE57 Dark Pastel Green
8C82B6 Mogul
3B444B Arsenic
FFC5CB Light Rose
1D5DEC Azul
665FD1 Dark Periwinkle
C2FF89 Light Yellowish Green
CDFD02 Greenish Yellow
9966CC Medium Deep Lavender
A55790 Meadow Mauve
D65282 Mystic Pearl
8A2BE2 Deep Indigo
872657 Dark Raspberry
AB5C6D Mauvewood
B27A01 Golden Brown
9D0216 Carmine
DADADA Light Silver Chalice
728F02 Dark Yellow Green
80CA2B Dizzy Lizzy
FEC615 Golden Yellow
534B4F Liver
4DA409 Lawn Green
EEDC82 Flax
E79FC4 Kobi
B6C406 Baby Puke Green
B2BEB5 Ash Grey
ADF802 Lemon Green
8CFF9E Baby Green
A32638 Alabama Crimson
069B81 Gossamer
F4BBFF Brilliant Lavender, Electric Lavender
7BB274 Faded Green
FF0490 Electric Pink
AD0AFD Bright Violet
00CED1 Deep Turquoise
DA3287 Deep Cerise
85A3B2 Bluegrey
26F7FD Bright Light Blue
FFD8B1 Light Peach
A9BA9D Laurel Green
CDC50A Dirty Yellow
CDB891 Ecru
A7FFB5 Light Seafoam Green
0000FF Blue
98F6B0 Light Sea Green
49796B Hooker's Green
850E04 Indian Red
FBEC5D Maize
C48EFD Liliac
06B48B Green Blue
6241C7 Bluey Purple
C74375 Fuchsia Rose
05FFA6 Bright Sea Green
3D2B1F Bistre
177245 Dark Spring Green
BB3385 Medium Red-violet
FCE5F2 Chantilly Light
A88905 Dark Mustard
B2996E Dust
00FA9A Medium Spring Green
CCFD7F Light Yellow Green
734A65 Dirty Purple
8E7618 Hazel
08FF08 Fluorescent Green
FFFE40 Canary Yellow
3D0C02 Black Bean
00AFCA Khazakstani Azure
FC8EAC Flamingo Pink
CC7A8B Dusky Pink
3F9B0B Grass Green
C53151 Dingy Dungeon
A88F59 Dark Sand
7C0A02 Barn Red
8CFFDB Light Aqua
DE0C62 Cerise
915C83 Antique Fuchsia
264348 Japanese Indigo
DA2647 Deep Cherry Red
006B3C Cadmium Green
D5CFDE Light Amethyst Smoke
005249 Dark Blue Green
FFA700 Chrome Yellow
DE5D83 Blush
195905 Lincoln Green
C6E610 Las Palmas
36013F Deep Purple
CD9999 Copper Rust Light
485470 Blue Indigo
8FAE22 Icky Green
BBE261 Light Lima
BB466F Deep Carmine Rose
FFF1EB Light Watusi
CCA01D Lemon Curry
A0785A Chamoisee
893F45 Cordovan
3FFF00 Harlequin
7E73B8 Fortune
8F509D Dark Vivid Violet
894585 Light Eggplant
AC1E44 French Wine
AD4379 Mystic Maroon
5FA052 Muted Green
2E5A88 Light Navy Blue
ACE5EE Blizzard Blue
84B701 Dark Lime
FFFE7A Light Yellow
A2653E Earth
F0DC82 Buff
4F9153 Light Forest Green
7F4E1E Milk Chocolate
D591A4 Can Can
CC5555 Girlsenberry
002D04 Dark Forest Green
FFEBCD Blanched Almond
749551 Drab Green
3C0008 Dark Maroon
84597E Dull Purple
A757AB Chinese Purple
A8B504 Mustard Green
7BFDC7 Light Aquamarine
42B395 Greeny Blue
8A9CC4 Lavender Lustre
EBE3FC Light Perfume
C7FF00 Fluorescent Lime
887191 Greyish Purple
A98F64 California Gold
874C62 Dark Mauve
E7E4EF Light Melrose
CF1020 Lava
670F25 Crimson Lake
841B2D Dark Carmine
1FB57A Dark Seafoam
FE6F5E Bittersweet
F88379 Coral Pink
602FFB Imperial
003366 Dark Midnight Blue
FADA5E Jonquil, Naples Yellow, Royal Yellow, Stil De Grain Yellow
D90166 Dark Hot Pink
000181 Forget-me-not
A17E9A Dull Mauve
526525 Camo Green
D98AA8 Light Night Shadz
926F5B Beaver
889717 Baby Shit Green
350E47 Jagger
0AFF02 Fluro Green
5E819D Greyish Blue
AD6F69 Copper Penny
FFFEB6 Light Beige
FEE600 Deep Lemon Yellow
FFB3DE Hot Pink Light
E97451 Burnt Sienna, Light Red Ochre
D0868E Bright Mauve Glow
4F3A3C Dark Puce
C1BBEC Light Moody Blue
0A888A Dark Cyan
875F42 Cocoa
EC3B83 Cerise Pink
FDFF38 Lemon Yellow
8FFF9F Mint Green
A8516E Mallow Purple
CA7B80 Dirty Pink
6F00FF Indigo
BD33A4 Byzantine
E3FF00 Lemon-lime
AF6F09 Caramel
3F829D Dirty Blue
1A2118 Charleston Green
A6C875 Light Moss Green
EC5486 Japanese Atomic Carmine Pink
D2A560 Light Hawaiian Tan
25FF29 Hot Green
003399 Italian Electric Blue
696006 Greeny Brown
A57164 Blast Off Bronze
C4FFF7 Eggshell Blue
C077A6 Medium Raspberry
10A674 Bluish Green
B96CCA Light Seance
679267 Copper Green
AD8884 Mauve Glow
FDFF52 Lemon
8B3F43 Cha Cha
AA381E Chinese Red
93CCEA Light Cornflower Blue
E49B0F Gamboge
A65E7E Deep Medium Red-violet
E53D43 Deep Carmine Pink-orange
FFCCFF Bright Pale Rose
CFB8DE Light Ce Soir
1560BD Denim
44944A Medium Harlequin
F7E7CE Champagne
730039 Merlot
BA9E88 Mushroom
B8860B Dark Goldenrod
FDFF63 Canary
BF77F6 Light Purple
D5AB09 Burnt Yellow
B5485D Dark Rose
E5CCC9 Dust Storm
3E82FC Dodger Blue
4EA4BA Maui Blue
A0025C Deep Magenta
343434 Jet
922B05 Brown Red
658CBB Faded Blue
9C6D57 Brownish
48D1CC Medium Turquoise
082567 Dark Sapphire
B898CD Light Lavender Affair
778899 Light Slate Gray
966EBD Deep Lilac
126180 Blue Sapphire
F7FA97 Light Starship
030764 Darkblue
FF5CCD Deep Pink Light
FFFF00 Electric Yellow, Yellow
DA9100 Harvest Gold
ADFF2F Green-yellow
997A8D Mountbatten Pink
03012D Midnight
66DDAA Medium Aquamarine
FF8243 Mango Tango
EF98AA Mauvelous
D6E94B Light Bahia
937C00 Baby Poop
C87606 Dirty Orange
00B6D9 Blue Atoll
FE828C Blush Pink
9BDDFF Columbia Blue
AF884A Dark Tan
76FF7B Lightgreen
FFF8DC Cornsilk
67032D Black Rose
A5A391 Cement
EAC592 Light Peru
FFD9B2 Light Italian Apricot
430541 Eggplant Purple
EB9373 Medium Apricot
77A1B5 Greyblue
FFF8E7 Cosmic Latte
FFF8ED Light Papaya Whip
D498CB Bright Mallow Pink
A484AC Heather
A2CFFE Baby Blue
11875D Dark Sea Green
C9FF27 Green Yellow
88786A Medium Taupe Gray
808080 Grey
FFFF31 Daffodil
CCAD60 Desert
F7D560 Light Mustard
F0B7CD Light Pale Red-violet
4B6113 Camouflage Green
92A1CF Ceil
E1C7C3 Brandy Rose Light
F5E25C Medium Lemon
6B8BA4 Grey Blue
B5A642 Brass
13EAC9 Aqua
E2062C Medium Candy Apple Red
9C6DA5 Dark Lilac
FEFF7F Faded Yellow
900020 Burgundy
A397B4 Amethyst Smoke
F9BC08 Golden Rod
9874D3 Lilac Bush
00FBB0 Greenish Turquoise
7058A3 Light Japanese Violet
9AF764 Light Grass Green
FEB4B1 Chinese Peach
00022E Dark Navy Blue
02590F Deep Green
070D0D Almost Black
789B73 Grey Green
AE0C00 Mordant Red 19
681C23 Internet Puce
DA467D Darkish Pink
D19FE8 Medium Bright Lavender
EBCBD6 Can Can Light
657432 Muddy Green
C120A2 Deep Magenta Rose
FF69AF Bubble Gum Pink
FFA812 Dark Tangerine
826D8C Grey Purple
63F7B4 Light Greenish Blue
51B73B Leafy Green
7B68EE Medium Slate Blue
C071FE Easter Purple
CD81B8 Bright Opera Mauve
979AAA Manatee
98817B Cinerous
DC143C Crimson
E3A857 Indian Yellow
FBBBC4 Light Froly
967BB6 Lavender Purple
AEFF6E Key Lime
C8FFB0 Light Light Green
FFFF7E Banana
C9AE5D Copper Yellow
4F284B Japanese Purple
CAE00D Bitter Lemon
E4D3D2 Mauve Chalk
FFFF81 Butter
CD5C5C Chestnut, Indian Red
FAE7B5 Banana Mania
7EA07A Greeny Grey
D8DCD6 Light Grey
2AFEB7 Greenish Cyan
EBE4F4 Light Prelude
B94E48 Deep Chestnut
8DB600 Apple Green
A552E6 Lightish Purple
380835 Eggplant
9A3001 Auburn
C3909B Grey Pink
FE0002 Fire Engine Red
FF4466 Magic Potion
0165FC Bright Blue
F9C8CB Crystal Rose
6C3461 Grape
E3F988 Mindaro
856088 Chinese Violet
C54B8C Mulberry
ACFFFC Light Cyan
287C37 Darkish Green
0D75F8 Deep Sky Blue
8D5EB7 Deep Lavender
58BC08 Frog Green
3B719F Muted Blue
A1C42C Medium Lime Green
9955BB Deep Rich Lavender
66424D Deep Tuscan Red
1BFC06 Highlighter Green
318CE7 Bleu De France
96B403 Booger Green
FF3800 Coquelicot
CCCCFF Lavender Blue, Periwinkle
D473D4 French Mauve
805B87 Muted Purple
751973 Darkish Purple
0BDA51 Malachite
E52B50 Amaranth
AB9004 Baby Poo
C2E20D Go Bunny Go
871550 Disco
FC51AF Bright Magenta Rose
5539CC Blurple
1CC3B4 Light Deep Sea
5E9B8A Grey Teal
BA160C International Orange (engineering)
FBCEB1 Apricot
8080FF Deep Periwinkle
E8CCD7 Mauvette
DC9DB3 Cadillac Light
79443B Bole, Medium Tuscan Red
E30022 Cadmium Red
FE4B03 Blood Orange
3D0734 Aubergine
FCC200 Golden Poppy
411900 Chocolate Brown
E7FEFF Bubbles
419C03 Grassy Green
341C02 Dark Brown
FFFFB6 Creme
7F684E Dark Taupe
A9203E Deep Carmine
E8F24E Light Bitter Lemon
CCFF00 Electric Lime, Fluorescent Yellow, French Lime
4E1609 French Puce
63B365 Boring Green
3EAF76 Dark Seafoam Green
448EE4 Dark Sky Blue
F5E2DE Dull Pallid Rose Light
D0FE1D Lime Yellow
6F6C0A Browny Green
B59410 Dark Gold
6699CC Blue Gray
98DF62 Go Go Go
D6CADD Languid Lavender
B9484E Dusty Red
0FFEF9 Bright Turquoise
F1B82D Mu Gold
4E5481 Dusk
9EFD38 French Lime
86608E French Lilac
014D4E Dark Teal
FF000D Bright Red
DE5285 Fandango Pink
FFFFD4 Eggshell
5B3256 Japanese Violet
2DFE54 Bright Light Green
E47698 Bright Blush
9D9FE9 Dadt Repeal Lavender
8D8468 Brown Grey
08457E Dark Cerulean
3C1414 Dark Sienna
F8F4FF Magnolia
002E63 Cool Black
FF63E9 Candy Pink
5A06EF Blue/purple
E4D430 Light Buddha Gold
D73B3E Jasper
696112 Greenish Brown
C90016 Harvard Crimson
C27E79 Brownish Pink
33B864 Cool Green
4F738E Metallic Blue
FAFAD2 Light Goldenrod Yellow
D9603B Medium Vermilion, Vermilion (plochere)
C0022F Lipstick Red
8F5E99 Medium Violet
FFFFF0 Ivory
4B3621 Coffee
D5869D Dull Pink
FF7077 Deep Pink-orange
2F4F4F Dark Slate Gray
C7C1FF Melrose
C72C48 French Raspberry
3560A6 Light Gulf Blue
65FE08 Bright Lime Green
3AB09E Keppel
BFDCD4 Light Acapulco
FF0038 Carmine Red
32293A Black Currant
BB8CAA Light Cosmic
E3DAC9 Bone
DE7E5D Dark Peach
71AA34 Leaf
FF003F Electric Crimson
8C7853 Deep Bronze
706C11 Brown Green
73A9C2 Moonstone Blue
E1A95F Earth Yellow
F5F5DC Beige
CA9BF7 Baby Purple
F08080 Light Coral
B2EC5D Inchworm
D399E6 Medium Mauve
6A79F7 Cornflower
E9692C Deep Carrot Orange
0BF9EA Bright Aqua
966FD6 Dark Pastel Purple
FF004F Folly
FFF39A Dark Cream
DE4C8A Heather Violet
715F6D Deep Taupe
DC4D01 Deep Orange
F7022A Cherry Red
6C541E Medium Bronze
3B7861 Dark Erin
DE9DAC Faded Pink
E25822 Flame
007FBF Honolulu Blue
0066CC Bright Navy Blue
5D76CB Bright Indigo
1E4D2B Cal Poly Green
A6FBB2 Light Mint Green
A1CAF1 Baby Blue Eyes
A23B3C Dark Cherry
AFA88B Bland
AD900D Baby Shit Brown
C8A2C8 Medium Lavender Grey
C65EA0 Light Disco
F5F5FD Light Lavender Mist
00416A Dark Imperial Blue, Indigo (dye)
FFC1CC Bubble Gum
3F00FF Electric Ultramarine
714693 Lavender Affair
9F2305 Burnt Red
E29CD2 Light Orchid
0047AB Cobalt Blue
D3D86E Light Citron
0F9B8E Blue/green
4EFD54 Light Neon Green
82A67D Greyish Green
5CA904 Leaf Green
FFF9FB Light Lavender Blush
02A4D3 Bright Cerulean
CAFFFB Light Light Blue
F653A6 Brilliant Rose
D8863B Dull Orange
FDEE00 Aureolin
AAA9CD Logan
C7458B Bright Raspberry Rose
6082B6 Glaucous
ED3CCA Amaranth Magenta
AA8A9E Lavender Brown
E4007C Mexican Rose
6E7F80 Aurometalsaurus
73C2FB Maya Blue
986960 Dark Chestnut
138808 India Green
C3B091 Khaki (html/css) (khaki)
F984EF Light Fuchsia Pink
6050DC Majorelle Blue
8FBC8F Grayish Sea Green
48C072 Dark Mint
D2E543 Light Limerick
0066FF Deep Azure
FF008F Fluorescent Pink
FE83CC Bubblegum Pink
E32636 Alizarin Crimson, Rose Madder
B12B7F Black Rose Light
DF84B5 Light Hibiscus
607C8E Blue Grey
02AB2E Kelly Green
9E3623 Brownish Red
3F012C Dark Plum
007FFF Azure
FF3F00 Electric Vermilion
856798 Dark Lavender
D47494 Charm
FF009F Electric Hollywood Cerise
9370DB Medium Purple
755258 Galaxy
96AE8D Greenish Grey
76A973 Dusty Green
E6E6FA Lavender Mist
DAA520 Goldenrod
0652FF Electric Blue
009900 Islamic Green
C5C2EF Light Chetwode Blue
E68FAC Charm Pink, Light Thulian Pink
EFF7AA Australian Mint
FF00AF Magenta Rose
DCDCDC Gainsboro
ACE1AF Celadon
0CB577 Green Teal
9C87CD Dull Deep Lavender
FBEEAC Light Tan
A1C50A Citrus
96F97B Light Green
8FB67B Lichen
2C6FBB Medium Blue
CC0000 Boston University Red
00035B Dark Blue
014182 Darkish Blue
C32148 Bright Maroon, Maroon (crayola)
040273 Deep Blue
2C75FF Deep Electric Blue
9BE5AA Hospital Green
C77DF3 Light Blue-violet
017371 Dark Aquamarine
A0BF16 Gross Green
5D06E9 Blue Violet
EFDECD Almond
276AB3 Mid Blue
D3494E Faded Red
87FD05 Bright Lime
763950 Cosmic
21C36F Algae Green
2976BB Bluish
32BF84 Greenish Teal
653700 Brown
7A6A4F Greyish Brown
63313A Medium Burgundy
FF00CC Hot Magenta
49759C Dull Blue
EA88A8 Carissma
A5CA77 Jade Lime
FE9D04 Bright California Gold
69D84F Fresh Green
009F6B Green (ncs)
6A6E09 Brownish Green
AC7E04 Mustard Brown
6C7A0E Murky Green
40A368 Greenish
1C352D Medium Jungle Green
EF3038 Deep Carmine Pink
228B22 Forest Green (web)
592720 Caput Mortuum
F8C3DF Chantilly
B36FF6 Light Urple
9457EB Lavender Indigo
B3C110 La Rioja
087830 La Salle Green
3C4D03 Dark Olive Green
BCCB7A Greenish Tan
964B00 Brown (traditional)
F7E98E Flavescent
FEA993 Light Salmon
E8E08E Light Earls Green
C327B2 Japanese Crimson
BA8759 Deer
7FFF00 Chartreuse Green
5CA345 Groovy
7B002C Bordeaux
C88A65 Antique Brass
A4BE5C Light Olive Green
FF64C5 Brilliant Electric Hollywood Cerise
E8000D Ku Crimson
FFFA86 Manilla
FF00FF Magenta
A24857 Light Maroon
B87B6B Brilliant Copper
042E60 Marine
FCE883 Medium Yellow
FFDB58 Mustard
E48400 Fulvous
6D5ACF Light Indigo
015482 Deep Sea Blue
6E1C34 Claret Violet
AA786D Medium Girlsenberry
33CC00 Deep Harlequin
ACC2D9 Cloudy Blue
A8A495 Greyish
A03623 Brick
388004 Dark Grass Green
801818 Falu Red
05472A Evergreen
C3FBF4 Duck Egg Blue
960056 Dark Magenta
BEBEBE Gray (x11 Gray)
A85387 Dahlia Mauve
91A3B0 Cadet Grey
CB7723 Brownish Orange
C04E01 Burnt Orange
D70A53 Debian Red
009337 Kelley Green
FFAFC8 Light Puce
B0B583 Medium Winter Pear
99637B Mellow Mauve
9D5616 Hawaiian Tan
FDD5B1 Light Apricot
FF2052 Awesome
EB6FCB Brilliant Red-violet
C0737A Dusty Rose
D5174E Lipstick
AA4069 Medium Ruby
BC2350 Carminorubaceous
033500 Dark Green
8DDCD3 Light Keppel
74C365 Mantis
544E03 Green Brown
D2691E Light Chocolate
06470C Forest Green
CFBAEC Light Lilac Bush
08787F Deep Aqua
B2FFFF Celeste (colour)
E4717A Candy Pink, Tango Pink
0247FE Blue (ryb)
BA6873 Dusky Rose
CA6B02 Browny Orange
B66325 Copper
980002 Blood Red
AB274F Amaranth Purple
FF7E00 Automotive Amber
DD85D7 Lavender Pink
FA5FF7 Light Magenta
7FFF55 Brilliant Harlequin
AE7181 Mauve
21FC0D Electric Green
FFFACD Lemon Chiffon
5D3954 Dark Byzantium
480607 Bulgarian Rose
7A83BF Medium Periwinkle
BA55D3 Medium Orchid
C4C3D0 Lavender Grey
BFAC05 Muddy Yellow
B10097 Light Nightclub
E7F3EB Light Edgewater
F4F0EC Isabelline
B11355 Dove Cherry
002FA7 International Klein Blue
6F4E37 Coffee, Tuscan Brown
F0944D Faded Orange
1F0954 Dark Indigo
C9B003 Brownish Yellow
6ECB3C Apple
7B3F00 Chocolate (traditional)
FDAA48 Light Orange
A5CB0C Bahia
C23B22 Dark Pastel Red
B6316C Hibiscus
F8DD5C Energy Yellow
FFFAF0 Floral White
E62020 Lust
A00498 Barney Purple
1F3B4D Dark Blue Grey
C95EFB Bright Lilac
8F9805 Baby Poop Green
0F3F00 Mandarin Red
CB00F5 Hot Purple
915F6D Mauve Taupe, Raspberry Glace
FE01B1 Bright Pink
E4C2D5 Melanie
FF77FF Fuchsia Pink
F3FDC6 Light Mindaro
B76969 Light Persian Plum
BE03FD Bright Purple
007AA5 Cg Blue
214761 Dark Slate Blue
598556 Dark Sage
748B97 Bluish Grey
5D8AA8 Air Force Blue
7F8F4E Camo
876E4B Dull Brown
FD5956 Grapefruit
B96902 Brown Orange
017A79 Bluegreen
0B4008 Hunter Green
FDDC5C Light Gold
76BD17 Lima
2D5DA1 Medium Sapphire
C292A1 Light Mauve
280137 Midnight Purple
7B4BAB Light Violent Violet
0093AF Blue (munsell)
560319 Dark Scarlet
000435 Dark Navy
56FCA2 Light Green Blue
990024 Medium Tyrian Purple
3A2EFE Light Royal Blue
480656 Clairvoyant
A55AF4 Lighter Purple
9DFF00 Bright Yellow Green
3C4142 Charcoal Grey
9C7C3B Metallic Sunburst
DE98B2 Blush Light
FCD667 Medium Goldenrod
30BA8F Mountain Meadow
154406 Forrest Green
1CAC78 Green (crayola)
00555A Deep Teal
B19CD9 Light Pastel Purple
DD5A91 Carmine Rose
155084 Light Navy
A8415B Light Burgundy
667C3E Military Green
B0BC4A Medium Chartreuse
F95A61 Carnation
7F5112 Medium Brown
9683EC French Lavender
E09214 Deep Apricot
B06500 Ginger
5D1451 Grape Purple
FFBCD9 Cotton Candy
665D1E Antique Bronze
1A1110 Licorice
7F7053 Grey Brown
742802 Chestnut
886806 Muddy Brown
EDBACD Charm Light
9FFEB0 Mint
7FFFD4 Aquamarine
A18178 Desert Taupe
9B8F55 Dark Khaki
96C8A2 Eton Blue
FF0800 Candy Apple Red
002395 Imperial Blue
490648 Deep Violet
FE46A5 Barbie Pink
FF7855 Melon
61E160 Lightish Green
BFD833 Lime Punch
B5CE08 Green/yellow
F03D6F Carmine Ruby
C154C1 Deep Fuchsia, Fuchsia (crayola)
FFB6C1 Medium Pink
EFB435 Macaroni And Cheese
85754E Gold Fusion
F6EABE Lemon Meringue
B6FFBB Light Mint
FF1493 Deep Pink, Fluorescent Pink
FF4040 Coral Red
CB0162 Deep Pink
410200 Deep Brown
E67E30 French Apricot
895B7B Dusky Purple
D1768F Muted Pink
728639 Khaki Green
AD9194 Deauville Mauve
B2713D Clay Brown
8581D9 Chetwode Blue
B695C0 Mystic Lilac
A90308 Darkish Red
39D1C2 Light Gossamer
01A049 Emerald
F00BA4 Foobar
F8B878 Mellow Apricot
05480D British Racing Green
9C2542 Big Dip O'ruby
436BAD French Blue
CEA2FD Lilac
95A3A6 Cool Grey
1E488F Cobalt
7F76D3 Moody Blue
54AC68 Algae
703BE7 Bluish Purple
BFBF6C Green Chartreuse Liqueur
FB607F Brink Pink
9CBB04 Bright Olive
B48395 English Lavender
3D1C02 Chocolate
D4E646 Light Citrus
CD2682 Amaranth Cerise
A0FEBF Light Seafoam
548D44 Fern Green
CC397B Fuchsia Purple
D3D3D3 Light Gray
B04C6A Cadillac
650021 Maroon
F5C5C2 English Rose
1A2421 Dark Jungle Green
8AB8FE Carolina Blue
719F91 Greyish Teal
004953 Midnight Green
C08D5E Copper Canyon Light
C18D25 Light Peru Tan
8F4155 Japanese Plum
3C73A8 Flat Blue
FEEF61 Light Candlelight
AF4035 Medium Carmine, Pale Carmine
5EDC1F Green Apple
7BC8F6 Lightblue
704241 Deep Coffee
0087BD Blue (ncs)
21ABCD Ball Blue
FFAA8D Goddess
9C7687 Dusky Orchid
FDB147 Butterscotch
71291D Dark Copper
051657 Gulf Blue
B66A50 Clay
DDE454 Light La Rioja
EFC0FE Light Lavendar
9D7651 Mocha
98777B Bazaar
D39BCB Light Medium Orchid
18A7B5 Light Teal Blue
50A747 Mid Green
BF4F51 Bittersweet Shimmer
702963 Byzantium
E4D00A Citrine
0B8B87 Greenish Blue
D987B9 Light Royal Heath
8A6E45 Dirt
B9FF66 Light Lime Green
6CA0DC Little Boy Blue
696969 Dim Grey
C9DC87 Medium Spring Bud
C0362C International Orange (golden Gate Bridge)
00BFFF Capri, Deep Sky Blue
3C8043 Area 51
60460F Mud Brown
9BB53C Booger
C1FD95 Celery
EE82EE Lavender Magenta, Violet (web)
C1A004 Buddha Gold
F0F8FF Alice Blue
89FE05 Lime Green
E5AA70 Fawn
D46FF9 Light Veronica
DEB887 Burlywood
C39953 Aztec Gold
B89BDD Light Studio
7E5E60 Deep Rose Taupe
E7BCB4 Dull Pallid Rose
00308F Air Force Blue (usaf
B29705 Brown Yellow
D9D8EA Light Logan
D94972 Cabaret
3B7A57 Amazon
EF9BB9 Cabaret Light
C2F732 French Chartreuse
29465B Dark Grey Blue
F3E5AB Medium Champagne, Vanilla
458AC6 Medium Azure
9932CC Dark Orchid
A75502 French Tan
7DA98D Bay Leaf
26538D Dusk Blue
00CC99 Caribbean Green
BFFE28 Lemon Lime
C74767 Deep Rose
663854 Halaya Ube
63A950 Fern
23C48B Greenblue
1B2431 Dark
770F05 Deep Burgundy
4D5D53 Feldgrau
C3103A Kermes
9771B5 Ce Soir
EFCDB8 Desert Sand
8CFD7E Easter Green
533CC6 Blue With A Hint Of Purple
536872 Cadet
536878 Dark Electric Blue
21421E Myrtle
555555 Davy's Grey
89A0B0 Bluey Grey
C51C56 Light Red Devil
647D8E Grey/blue
007BA7 Cerulean
E6F554 Light Las Palmas
F0E68C Light Khaki
12E193 Aqua Green
FAF0BE Blond
F8DE7E Mellow Yellow
9D5783 Light Plum
FF7F50 Coral
FF028D Hot Pink
9FFF7F Light Harlequin
3B3C36 Black Olive
E5B73B Meat Brown
9F2B68 Amaranth Deep Purple
FF2800 Ferrari Red
A4C639 Android Green
D94FF5 Heliotrope
86A17D Grey/green
7D26CD Internet Purple
536267 Gunmetal
53FE5C Light Bright Green
CF524E Dark Coral
B80049 Bright Tyrian Purple
E6E8FA Glitter
02D8E9 Aqua Blue
828344 Drab
C49FBA Bright Mauve Mist
363737 Dark Grey
FF6CB5 Bubblegum
FFC40C Mikado Yellow
FF08E8 Bright Magenta
FF474C Light Red
6258C4 Iris
FDE9F5 Classic Rose Light
779ECB Dark Pastel Blue
FAF0E6 Linen
C08181 Medium Carmine Pink
FFF600 Cadmium Yellow
A8E4A0 Granny Smith Apple
D71868 Dogwood Rose
680018 Claret
C69F59 Camel
3B5B92 Denim Blue
77AB56 Asparagus
F9EAF3 Amour
8E616A Medium Puce
FF8C00 Dark Orange
660099 Generic Purple
934D91 Hyacinth Violet
9A393D Bulgarian Rose Light
C32350 Medium Rich Magenta
FBBEDA Cupid
C85A53 Dark Salmon
9F381D Cognac
77926F Green Grey
C6FCFF Light Sky Blue
BADA55 Badass
F4E5ED Light Melanie
9E003A Cranberry
D1E189 Lime Pulp
9F8303 Diarrhea
840000 Dark Red
A899E6 Light French Lavender
00FF00 Green
57D200 Deep Yellow-green
FFB7C5 Cherry Blossom Pink
41415D Dotcom
39AD48 Medium Green
AAF0D1 Magic Mint
FF7FA7 Carnation Pink
C19A6B Fallow
BCECAC Light Sage
FF9EDA Light Electric Hollywood Cerise
FFB7CE Baby Pink
FFD700 Golden
C6930A Cal Poly Pomona Gold
FDE3F0 Cupid Light
253529 Black Leather Jacket
EBE1DF Almost Mauve
F57584 Froly
56365C Lilac Shadow
9F1F4C Medium Cerise
52503C Dark Bronze
054907 Darkgreen
C1F80A Chartreuse
F0FFF0 Honeydew
944747 Copper Rust
4A0100 Mahogany
7EF4CC Light Turquoise
465945 Gray-asparagus
606602 Mud Green
C760FF Bright Lavender
AE98AA Lilac Luster
F0FFFC Light Frosted Mint
F0833A Dusty Orange
F0FFFF Azure Mist/web
01C08D Green/blue
713036 Crimson Cherry
CFBCBE Bazaar Light
9DC209 Limerick
D02090 Bright Red-violet
EB4C42 Carmine Pink
B75203 Burnt Siena
B37E9A Mauve Orchid
BA067A Deep Raspberry
B0665C Light Cherrywood
B89CA9 Mauve Shadows
651A14 Cherrywood
6B7C85 Battleship Grey
5A86AD Dusty Blue
B2FBA5 Light Pastel Green
5729CE Blue Purple
71A6D2 Iceberg
00FF3F Erin
D6B4FC Light Violet
01FF07 Bright Green
A50B5E Jazzberry Jam
D4AF37 Metallic Gold
BF6935 Kenyan Copper Light
AD1C42 Deep Crimson
BAA8C1 Lavender Frost
902933 Japanese Carmine
E03C31 Cg Red
EBDBDB Mauve Morn
5F9E8F Dull Teal
B7E1A1 Light Grey Green
4B0101 Dried Blood
5CB200 Kermit Green
343837 Charcoal
BFFF00 Lime
EFBBCC Cameo Pink
02CCFE Bright Sky Blue
FD3592 French Fuchsia
B44668 Deep Blush
FDF6FA Amour Light
5F9EA0 Cadet Blue
D6FFFA Ice
FFFCC4 Egg Shell
AC86A8 Dusty Lavender
75FD63 Lighter Green
FF033E American Rose
769958 Moss
5D3B4C Mauve Wine
01826B Deep Sea
475F94 Dusky Blue
019529 Irish Green
B53389 Fandango
B5C306 Bile
CC6666 Fuzzy Wuzzy
AAA662 Khaki
503E5C Deep Dark Indigo
045C5A Dark Turquoise
BB8983 Brandy Rose
7EBD01 Dark Lime Green
DBB40C Gold
85BB65 Dollar Bill
446CCF Han Blue
D290B5 Moonlite Mauve
FF5470 Fiery Rose
74A662 Dull Green
014421 Forest Green (traditional), Up Forest Green
B31B1B Carnelian, Cornell Red
35063E Dark Purple
2242C7 Blue Blue
C46210 Alloy Orange
0D98BA Blue-green
FCF75E Icterine
F6C6D8 Carissma Light
B38B6D Light Taupe
CD7F32 Bronze
638B27 Mossy Green
F0E130 Dandelion
FE2F4A Lightish Red
94417B Deep Orchid
B4CFD3 Jungle Mist
6700FF Lobelia
7E3A15 Copper Canyon
056EEE Cerulean Blue
F5C71A Deep Lemon
137E6D Blue Green
3D7AFD Lightish Blue
DA3163 Cerise Magenta
76D7EA Medium Sky Blue
4984B8 Cool Blue
CB4154 Brick Red
FF6103 Cadmium Orange
373E02 Dark Olive
2F847C Celadon Green
F56991 Light Crimson
4C9085 Dusty Teal
ED0DD9 Fuchsia
EDC8FF Light Lilac
DFC5FE Light Lavender
FA4F7B Atomic Carmine Pink
FFFD01 Bright Yellow
AD1022 Crimson Red
9D0759 Dark Fuchsia
D7FFFE Ice Blue
E9D66B Arylide Yellow, Hansa Yellow
AC9362 Dark Beige
72A0C1 Air Superiority Blue
A0450E Burnt Umber
3CB371 Medium Sea Green
90FDA9 Foam Green
AC7434 Leather
BDDA57 June Bud
D500B6 Flirt Light
E18731 British Apricot
9FA91F Citron
18453B Msu Green
7E4071 Bruise
E79EC5 Light Red-violet
CB416B Dark Pink
DD4384 Light Shiraz
76424E Brownish Purple
1FA774 Jade
90E4C1 Light Teal
C79FEF Lavender
AEBEA6 Light Amazon
B33A7F Fuchsia Red
00703C Dartmouth Green
350036 Dark Mardi Gras
667E2C Dirty Green
8EE53F Kiwi Green
6D9BC3 Cerulean Frost
74ACDF Argentinian Azure
00A86B Jade Green
B2A4D4 Deep Lavender Blue-gray
C14A09 Brick Orange
0095B6 Bondi Blue
E34234 Cinnabar, Vermilion (cinnabar)
5CAC2D Grass
6832E3 Burple
FAFE4B Banana Yellow

HashQP with the String key now has:
Dotcom 41415D
Brownish Pink C27E79
Leafy Green 51B73B
Amaranth Pink F19CBB
Blush Pink FE828C
Lawn Green 4DA409
Forest Green 06470C
Light Grass Green 9AF764
Midnight Purple 280137
Light Steel Blue B0C4DE
Cherry Blossom Pink FFB7C5
Ginger B06500
Muted Purple 805B87
Dark Puce 4F3A3C
Coral Red FF4040
Brown Red 922B05
Brink Pink FB607F
Deep Jungle Green 004B49
Bright Pale Rose FFCCFF
Chinese Peach FEB4B1
Cosmic Latte FFF8E7
Dark Sienna 3C1414
Medium Ruby AA4069
Deep Turquoise 00CED1
Awesome FF2052
Bubble Gum Pink FF69AF
Camo Green 526525
Camel C69F59
Dark Blue Grey 1F3B4D
Cognac 9F381D
Dark Sand A88F59
Erin 00FF3F
Cherrywood 651A14
Deep Fuchsia, Fuchsia (crayola) C154C1
Manatee 979AAA
Goddess FFAA8D
Dried Blood 4B0101
Egg Shell FFFCC4
Deep Burgundy 770F05
Light Cherrywood B0665C
Cool Green 33B864
Copper Red CB6D51
Go Go Go 98DF62
Desert CCAD60
Celadon Green 2F847C
Deep Carmine A9203E
Hollywood Cerise F400A1
Dark Violet 34013F
Deep Rose C74767
Light Red FF474C
Melrose C7C1FF
Mauve Taupe AF868E
Mud Green 606602
Dull Teal 5F9E8F
Medium Jungle Green 1C352D
Cherry Red F7022A
Light Yellowish Green C2FF89
Deep Peach, Peach (crayola) FFCBA4
Dark Taupe 7F684E
Light Prelude EBE4F4
Cadet Grey 91A3B0
Atomic Carmine Pink FA4F7B
Light Bitter Lemon E8F24E
Hot Magenta FF00CC
Light Neon Green 4EFD54
Brownish 9C6D57
Light Lavendar EFC0FE
Brownish Green 6A6E09
Ivory FFFFF0
Gamboge E49B0F
Light Greenish Blue 63F7B4
Blue With A Hint Of Purple 533CC6
Light Amethyst Smoke D5CFDE
Lilac CEA2FD
Green 00FF00
Bright Lime Green 65FE08
Medium Sky Blue 76D7EA
Kiwi Green 8EE53F
Celestial Blue 4997D0
Black Currant 32293A
Dusty Teal 4C9085
Dadt Repeal Lavender 9D9FE9
Deep Sea 01826B
Greenish 40A368
Khazakstani Azure 00AFCA
Atomic Tangerine, Pink-orange FF9966
Kelley Green 009337
American Rose FF033E
Grey Pink C3909B
Grey Purple 826D8C
Fortune 7E73B8
Dusky Blue 475F94
Iceberg 71A6D2
Brilliant Electric Hollywood Cerise FF64C5
Medium Spring Green 00FA9A
Italian Apricot FFB280
Darkish Blue 014182
Grass Green 3F9B0B
Indigo Dye 1A5798
Light Froly FBBBC4
Light Lavender DFC5FE
Dirty Pink CA7B80
Light Earls Green E8E08E
Misty Rose FFE4E1
Light Blue-violet C77DF3
Electric Yellow, Yellow FFFF00
Deep Coffee 704241
Internet Puce 681C23
Mid Blue 276AB3
Green Sheen 6EAEA1
Cg Blue 007AA5
Brilliant Rose F653A6
Dove Cherry B11355
Lavender Rose FBA0E3
Manilla FFFA86
Bittersweet Shimmer BF4F51
Dark Imperial Blue, Indigo (dye) 00416A
Deep Yellow-green 57D200
Jasper D73B3E
Liver 534B4F
Light Light Green C8FFB0
Moonstone Blue 73A9C2
Jonquil, Naples Yellow, Royal Yellow, Stil De Grain Yellow FADA5E
Mellow Apricot F8B878
Candy Pink, Tango Pink E4717A
Deep Raspberry BA067A
Dartmouth Green 00703C
Bright Yellow Green 9DFF00
Milk Chocolate 7F4E1E
Lightish Blue 3D7AFD
Medium Brown 7F5112
Apricot FBCEB1
Dark Pastel Purple 966FD6
Brown Yellow B29705
Dirt 8A6E45
Little Boy Blue 6CA0DC
Cha Cha 8B3F43
French Tan A75502
Electric Blue 0652FF
Mandarin Red 0F3F00
Dirt Brown 836539
Fuchsia Red B33A7F
Hyacinth Violet 934D91
Light Medium Purple CDB7F0
Blue (crayola) 1F75FE
Deep Sea Blue 015482
May Green 4C9141
Khaki Green 728639
Mahogany 4A0100
Drab Green 749551
Dark Terra Cotta CC4E5C
Jade Green 00A86B
Lust E62020
Beaver 926F5B
Light Seance B96CCA
Dark Khaki 9B8F55
Floral White FFFAF0
Light Harlequin 9FFF7F
Lightgreen 76FF7B
Deep Lilac 966EBD
Dark Seafoam Green 3EAF76
Deep Green 02590F
Capri, Deep Sky Blue 00BFFF
Light California Gold FFD230
Cornflower 6A79F7
Fuzzy Wuzzy Brown C45655
Amethyst 9B5FC0
Light Deep Sea 1CC3B4
Fallow C19A6B
Han Purple 5218FA
Lapis Lazuli 26619C
Dark Hot Pink D90166
Booger 9BB53C
Dark Copper 71291D
Light Gulf Blue 3560A6
Electric Lime A8FF04
Leaf 71AA34
Diarrhea 9F8303
Battleship Grey 6B7C85
Lava CF1020
Chestnut, Indian Red CD5C5C
Medium Violet 8F5E99
Dark Sky Blue 448EE4
Bright Opera Mauve CD81B8
Burgundy 900020
Galaxy 755258
Amaranth E52B50
Meadow Mauve A55790
Light Sirocco B8C2C2
Hunter Green 0B4008
Light Teal Blue 18A7B5
Dark Olive 373E02
Clairvoyant 480656
Light Studio B89BDD
Deep Bronze 8C7853
Earls Green C9B93B
Dull Pink D5869D
Carolina Blue 8AB8FE
Light Chocolate D2691E
Lobelia 6700FF
Glitter E6E8FA
Muted Blue 3B719F
Eton Blue 96C8A2
Light Mauve C292A1
Deep Electric Blue 2C75FF
Crimson Red AD1022
Duke Blue 00009C
Blue/purple 5A06EF
Forest Green (traditional), Up Forest Green 014421
Minty Green 0BF77D
Mustard Green A8B504
Cerulean Blue 056EEE
Light Peach FFD8B1
Light Red Devil C51C56
Dark Slate Blue 214761
Aureolin FDEE00
Antique Fuchsia 915C83
Cobalt Blue 0047AB
Light Candlelight FEEF61
Las Palmas C6E610
Dusty Pink D58A94
Alloy Orange C46210
Cinnamon AC4F06
Light Lavender Mist F5F5FD
Deep Dark Indigo 503E5C
Dodger Blue 3E82FC
Davy's Grey 555555
Area 51 3C8043
Light Sage BCECAC
Lightish Green 61E160
Lavender Affair 714693
Heliotrope D94FF5
Bright Olive 9CBB04
Blush Light DE98B2
Harvard Crimson C90016
Mindaro E3F988
Dark Salmon C85A53
Bazaar Light CFBCBE
Bluegreen 017A79
Moody Blue 7F76D3
Muted Lime D6CE7B
Easter Green 8CFD7E
Imperial 602FFB
Dark Bronze 52503C
Dark Electric Blue 536878
Lavender Lustre 8A9CC4
Light Rose FFC5CB
Light Cornflower Blue 93CCEA
Irish Green 019529
Electric Lime, Fluorescent Yellow, French Lime CCFF00
Light Moody Blue C1BBEC
Blue Indigo 485470
Bright California Gold FE9D04
Dust Storm E5CCC9
Deep Taupe 715F6D
Lighter Purple A55AF4
Bright Light Green 2DFE54
Carmine Rose DD5A91
Bright Indigo 5D76CB
Cordovan 893F45
Dull Pallid Rose E7BCB4
Blue Sapphire 126180
Jet 343434
Magnolia F8F4FF
Marine Blue 01386A
Medium Lime Green A1C42C
Green (ncs) 009F6B
Japanese Violet 5B3256
Dark Maroon 3C0008
Lime Green 89FE05
Khaki AAA662
Light Sea Green 98F6B0
Dusty Green 76A973
Bahia A5CB0C
Bright Mallow Pink D498CB
Carminorubaceous BC2350
Mauve Orchid B37E9A
Iris 6258C4
Chinese Red AA381E
Mexican Rose E4007C
Muddy Green 657432
Fluro Green 0AFF02
Light Frosted Mint F0FFFC
Gulf Blue 051657
Bright Magenta Rose FC51AF
Bright Sky Blue 02CCFE
Light Indigo 6D5ACF
Fuchsia ED0DD9
Amour Light FDF6FA
Metallic Sunburst 9C7C3B
Electric Vermilion FF3F00
Light Puce FFAFC8
Icterine FCF75E
Flirt A2006D
Brick Orange C14A09
Caramel AF6F09
Dark Cherry A23B3C
Chocolate 3D1C02
Fuchsia Purple CC397B
Cool Blue 4984B8
Dusky Orchid 9C7687
Light Nightclub B10097
Gross Green A0BF16
Malachite 0BDA51
Lemon Green ADF802
French Blue 436BAD
Candlelight FCD917
Greenish Yellow CDFD02
Automotive Amber FF7E00
Magenta FF00FF
Kiwi 9CEF43
Antique Bronze 665D1E
Deep Carmine Pink-orange E53D43
Boston University Red CC0000
Bone E3DAC9
Lipstick D5174E
Carmine Red FF0038
Light Carmine Pink F26476
Mud 735C12
Air Force Blue (usaf 00308F
Bay Leaf 7DA98D
Lincoln Green 195905
Light Slate Gray 778899
Classic Rose FBCCE7
Lightish Purple A552E6
Dull Yellow EEDC5B
Dark Blue 00035B
La Salle Green 087830
Green/blue 01C08D
Deep Sky Blue 0D75F8
Greeny Yellow C6F808
Brown Green 706C11
Lime Pulp D1E189
Medium Candy Apple Red E2062C
Heather A484AC
Light Melrose E7E4EF
Medium Chartreuse B0BC4A
Indian Yellow E3A857
Brandy Rose Light E1C7C3
Mellow Mauve 99637B
Lavender Indigo 9457EB
Fuchsia Pink FF77FF
Mulberry C54B8C
Dark Cyan 0A888A
Maroon 650021
Chartreuse C1F80A
Can Can Light EBCBD6
Lavender (floral) B57EDC
Dull Mauve A17E9A
French Lime 9EFD38
Lemon FDFF52
Citron 9FA91F
Argentinian Azure 74ACDF
Candy Apple Red FF0800
Bluey Grey 89A0B0
Dark Slate Gray 2F4F4F
Dull Pallid Rose Light F5E2DE
Duck Egg Blue C3FBF4
Auburn 9A3001
Dark Lime 84B701
Cerulean Frost 6D9BC3
Glaucous 6082B6
Dark Tan AF884A
Charleston Green 1A2118
Deep Rich Lavender 9955BB
Brown Orange B96902
Blue/grey 758DA3
Brown (traditional) 964B00
Bright Yellow FFFD01
Hawaiian Tan 9D5616
Meat Brown E5B73B
Light Pastel Green B2FBA5
Medium Bronze 6C541E
Chantilly Light FCE5F2
Goldenrod DAA520
Bluegrey 85A3B2
Maize FBEC5D
Melon FF7855
Flirt Light D500B6
Charcoal Grey 3C4142
Cadet 536872
Maui Blue 4EA4BA
Japanese Blue-green 3A6960
Dusky Pink CC7A8B
Deep Carmine Rose BB466F
Earth A2653E
Grey Brown 7F7053
Light Medium Apricot F7CDB9
Lemon Meringue F6EABE
Cocoa 875F42
Light Night Shadz D98AA8
Light Urple B36FF6
Halaya Ube 663854
Chrome Yellow FFA700
Bright Red-violet D02090
Darkish Pink DA467D
Lighter Green 75FD63
Azul 1D5DEC
Crystal Rose F9C8CB
Electric Purple AA23FF
Deep Blue 040273
Light Electric Hollywood Cerise FF9EDA
Bright Light Blue 26F7FD
Carnation Pink FF7FA7
Light Lilac EDC8FF
Light Green 96F97B
Light Yellow FFFE7A
Lilac Luster AE98AA
Brass B5A642
Deep Rose Taupe 7E5E60
Burnt Red 9F2305
Cadmium Red E30022
International Klein Blue 002FA7
Medium Taupe Gray 88786A
Light Amazon AEBEA6
Fulvous E48400
Faded Blue 658CBB
Imperial Blue 002395
Light Goldenrod Yellow FAFAD2
Licorice 1A1110
Mountbatten Pink 997A8D
Electric Hollywood Cerise FF009F
Citrus A1C50A
Khaki (html/css) (khaki) C3B091
Ghost White F8F8FF
Limerick 9DC209
Light Apricot FDD5B1
Cool Black 002E63
Chocolate (traditional) 7B3F00
Chartreuse Green 7FFF00
Magic Mint AAF0D1
Fawn E5AA70
Deep Periwinkle 8080FF
Camo 7F8F4E
Coffee 4B3621
Light Las Palmas E6F554
Lavender Blush FFF0F5
Light Orchid E29CD2
Electric Pink FF0490
Light Bluish Green 76FDA8
Baby Shit Brown AD900D
Logan AAA9CD
Deep Lemon F5C71A
Cabaret Light EF9BB9
Barf Green 94AC02
Light Pale Plum F1D4F1
Ferrari Red FF2800
Dogwood Rose D71868
Copper B66325
Light Medium Violet CAABD0
Muddy Yellow BFAC05
Jade 1FA774
Aqua Green 12E193
Cadillac B04C6A
Japanese Atomic Carmine Pink EC5486
Indian Red 850E04
Bulgarian Rose Light 9A393D
Black 000000
Hooker's Green 49796B
Bluey Purple 6241C7
Leaf Green 5CA904
Cameo Pink EFBBCC
Blue Bell A2A2D0
Bistre 3D2B1F
Aqua Marine 2EE8BB
Harlequin 3FFF00
International Orange (engineering) BA160C
Hot Pink FF028D
Hot Green 25FF29
Gray-asparagus 465945
Internet Purple 7D26CD
Fluorescent Green 08FF08
Dizzy Lizzy 80CA2B
Light Burgundy A8415B
Deep Apricot E09214
Light French Lavender A899E6
Beau Blue, Pale Aqua BCD4E6
June Bud BDDA57
Dark Teal 014D4E
Lavender Blue 8B88F8
Aqua Blue 02D8E9
Chantilly F8C3DF
Light Lime Green B9FF66
Greenish Brown 696112
Han Blue 446CCF
Grassy Green 419C03
Almost Mauve EBE1DF
Grey/blue 647D8E
Magenta Rose FF00AF
Lavender Brown AA8A9E
Mossy Green 638B27
Azure 007FFF
Indigo Blue 3A18B1
Cosmic 763950
Cool Grey 95A3A6
Emerald, Paris Green 50C878
Medium Mauve D399E6
Muted Pink D1768F
Light Pastel Purple B19CD9
Copper Rust 944747
Medium Puce 8E616A
Medium Goldenrod FCD667
Android Green A4C639
Flat Blue 3C73A8
Clay Brown B2713D
Dusky Purple 895B7B
Mustard FFDB58
Easter Purple C071FE
Light Taupe B38B6D
Electric Ultramarine 3F00FF
Feldgrau 4D5D53
Custard FFFD78
Faded Yellow FEFF7F
Icky Green 8FAE22
Light Keppel 8DDCD3
Light Limerick D2E543
Gunmetal 536267
Dark Lime Green 7EBD01
Honolulu Blue 007FBF
Dark Pastel Red C23B22
Dark Grey 363737
Bronze CD7F32
Blue 0000FF
Deep Cerise DA3287
Light Seafoam Green A7FFB5
Carnelian, Cornell Red B31B1B
Dark Jungle Green 1A2421
Light Mindaro F3FDC6
Medium Deep Lavender 9966CC
Emerald Green 028F1E
Cyan 00FFFF
Dark Goldenrod B8860B
Isabelline F4F0EC
Cal Poly Green 1E4D2B
Light Olivetone B8B654
Medium Lavender Grey C8A2C8
Bile B5C306
Light Salmon Pink FF9999
Blue-green 0D98BA
Dusk Blue 26538D
Mid Green 50A747
Flax EEDC82
Kermes C3103A
Laurel Green A9BA9D
Burnt Sienna, Light Red Ochre E97451
Dull Purple 84597E
Deep Indigo 8A2BE2
Light Lavender Affair B898CD
Blanched Almond FFEBCD
British Apricot E18731
Dark Fuchsia 9D0759
Dark Chestnut 986960
Carissma Light F6C6D8
Lima 76BD17
Dull Green 74A662
Deep Teal 00555A
Lime BFFF00
Celeste (colour) B2FFFF
Dull Orange D8863B
Claret 680018
Dark Seafoam 1FB57A
Dark Aqua 05696B
Marine 042E60
Mountain Meadow 30BA8F
Eggshell Blue C4FFF7
Cabaret D94972
French Plum 811453
Light Starship F7FA97
Light Turquoise 7EF4CC
Bright Blush E47698
Green (crayola) 1CAC78
Deep Pink, Fluorescent Pink FF1493
Black Rose 67032D
Greyish Brown 7A6A4F
Dark Spring Green 177245
Dark Magenta 960056
Cherry CF0234
Clear Blue 247AFD
Bland AFA88B
Blue Purple 5729CE
Deep Azure 0066FF
Medium Harlequin 44944A
Dark Brown 341C02
Golden FFD700
International Orange (aerospace) FF4F00
Dark Plum 3F012C
Medium Red-violet BB3385
Barney Purple A00498
Black Bean 3D0C02
Mauve Chalk E4D3D2
Copper Canyon Light C08D5E
Blood Red 980002
Barn Red 7C0A02
Bright Purple BE03FD
Mauve Taupe, Raspberry Glace 915F6D
Baby Green 8CFF9E
Medium Winter Pear B0B583
Burnt Yellow D5AB09
Light Seafoam A0FEBF
Bright Tyrian Purple B80049
Dark Indigo 1F0954
Alizarin Crimson, Rose Madder E32636
Forest 0B5509
Banana FFFF7E
Green (munsell) 00A877
Light Red-violet E79EC5
Brilliant Harlequin 7FFF55
Green-yellow ADFF2F
Brownish Red 9E3623
Light Magenta FA5FF7
Light Blue 95D0FC
Electric Green 21FC0D
Light Khaki F0E68C
Bright Orange FF5B00
Fern 63A950
Light Forest Green 4F9153
Light Bahia D6E94B
Hibiscus B6316C
Light Light Blue CAFFFB
Dull Red BB3F3F
Burlywood DEB887
Light Cyan ACFFFC
Bole, Medium Tuscan Red 79443B
Dark Pastel Green 56AE57
Light Italian Apricot FFD9B2
Key Lime AEFF6E
Ceil 92A1CF
Light Pea Green C4FE82
Greyish Blue 5E819D
Dark Pink CB416B
Earth Yellow E1A95F
Melanie E4C2D5
Light Purple BF77F6
Foobar F00BA4
Mint Cream F5FFFA
Berry 990F4B
Mantis 74C365
Light Pale Red-violet F0B7CD
Browny Green 6F6C0A
Light Royal Heath D987B9
Barney AC1DB8
Butter FFFF81
Greenish Beige C9D179
Flamingo Pink FC8EAC
Light Buddha Gold E4D430
Blush DE5D83
Honeydew F0FFF0
Deep Aqua 08787F
Cal Poly Pomona Gold C6930A
Ce Soir 9771B5
Light Orange FDAA48
Deep Pink Light FF5CCD
Light Lime AEFD6C
Disco 871550
Flat Green 699D4C
Light Lima BBE261
Lemon Yellow FDFF38
Magic Potion FF4466
Light Olive ACBF69
British Racing Green 05480D
Bubblegum FF6CB5
Bitter Lemon CAE00D
French Apricot E67E30
Lilac Shadow 56365C
Brandeis Blue 0070FF
Bubble Gum FFC1CC
Chamoisee A0785A
Badass BADA55
Clay B66A50
Deep Magenta A0025C
Frog Green 58BC08
Ice Blue D7FFFE
Light Zydeco 25937E
Daffodil FFFF31
Islamic Green 009900
Medium Periwinkle 7A83BF
Deep Carrot Orange E9692C
French Chartreuse C2F732
Greeny Blue 42B395
Cadmium Yellow FFF600
Lavender Grey C4C3D0
Bright Sea Green 05FFA6
Light Navy Blue 2E5A88
Mocha 9D7651
Msu Green 18453B
Greyish A8A495
Browny Orange CA6B02
Darkblue 030764
Lavender Purple 967BB6
Indigo 6F00FF
Mud Brown 60460F
Big Dip O'ruby 9C2542
Barbie Pink FE46A5
Frosted Mint DBFFF8
Burnt Umber A0450E
Emerald 01A049
Aztec Gold C39953
Fandango B53389
Hazel 8E7618
Ash Grey B2BEB5
Blue Blue 2242C7
Midnight Green 004953
Deep Orchid 94417B
Camouflage Green 4B6113
Deep Mauve Pink E4BEEB
Mauvette E8CCD7
Grayish Sea Green 8FBC8F
Dark Cerulean 08457E
Grapefruit FD5956
Medium Sea Green 3CB371
Light Lavender Magenta 0F8C3F
Bittersweet FE6F5E
Brown 653700
Light Selago F9F8FE
Moss Green 658B38
Blue Atoll 00B6D9
Medium Green 39AD48
Deep Chestnut B94E48
Deep Pink CB0162
Brick A03623
Japanese Plum 8F4155
Celadon ACE1AF
Lavender Magenta, Violet (web) EE82EE
Grey 808080
Muted Green 5FA052
Algae Green 21C36F
Dark Gold B59410
Greenblue 23C48B
Denim Blue 3B5B92
Light Moss Green A6C875
Medium Cerise 9F1F4C
Harvest Gold DA9100
Chetwode Blue 8581D9
Mystic Maroon AD4379
Light Peru EAC592
Banana Mania FAE7B5
Dark Lavender 856798
Faded Pink DE9DAC
Copper Penny AD6F69
Light Tan FBEEAC
Light Lavender Blush FFF9FB
Dark Yellow D5B60A
Dark Navy 000435
Dark Coral CF524E
Majorelle Blue 6050DC
Bright Blue 0165FC
Anti-flash White F2F3F4
Faded Purple 916E99
Dark Cream FFF39A
Metallic Blue 4F738E
Dark Sea Green 11875D
Amaranth Deep Purple 9F2B68
Copper Rose 996666
Buddha Gold C1A004
Crimson Lake 670F25
Bubbles E7FEFF
French Wine AC1E44
Ecru CDB891
Deep Blush B44668
Flavescent F7E98E
Bright Cyan 41FDFE
Copper Rust Light CD9999
Charcoal 343837
Air Superiority Blue 72A0C1
Dark Orchid 9932CC
Blue (ryb) 0247FE
Dark Beige AC9362
Faded Orange F0944D
Marigold FCC006
Dark Mint 48C072
Light Peach Puff FFF0E0
Light Perfume EBE3FC
Debian Red D70A53
Light Gray D3D3D3
Medium Tyrian Purple 990024
Charm Light EDBACD
Mu Gold F1B82D
Moss 769958
Mauvelous EF98AA
Aqua 13EAC9
Deep Lavender Blue-gray B2A4D4
Fluorescent Lime C7FF00
Boring Green 63B365
Light Teal 90E4C1
Bright Magenta FF08E8
Military Green 667C3E
Chinese Purple A757AB
French Mauve D473D4
Dusty Rose C0737A
Bright Lime 87FD05
Cranberry 9E003A
Light Peru Tan C18D25
Fire Engine Red FE0002
Dark Mustard A88905
Light Blue Grey B7C9E2
Blast Off Bronze A57164
Light Sky Blue C6FCFF
Drab 828344
Light Jungle Mist DEEBEC
Mango FFA62B
Dull Deep Lavender 9C87CD
Lime Yellow D0FE1D
Macaroni And Cheese EFB435
Fandango Pink DE5285
French Peach FDBFB7
Greyish Teal 719F91
Mallow Purple A8516E
Foam Green 90FDA9
Lavender Pink DD85D7
Banana Yellow FAFE4B
Mardi Gras 880085
Deep Lemon Yellow FEE600
Cardinal C41E3A
Amaranth Cerise CD2682
Amethyst Smoke A397B4
Canary Yellow FFFE40
Cg Red E03C31
Light Logan D9D8EA
Mustard Yellow D2BD0A
Deep Medium Red-violet A65E7E
Charm D47494
Grullo A99A86
Darkish Purple 751973
Light Japanese Violet 7058A3
Brilliant Lavender, Electric Lavender F4BBFF
Deep Brown 410200
Dark Vivid Violet 8F509D
Gossamer 069B81
Light Grey D8DCD6
Light Mustard F7D560
Blueberry 464196
Green Brown 544E03
Medium Carmine, Pale Carmine AF4035
Carrot Orange ED9121
Light Edgewater E7F3EB
Light Bright Green 53FE5C
Bright Cerulean 02A4D3
Brilliant Copper B87B6B
Medium Bright Lavender D19FE8
Bruise 7E4071
Light Persian Plum B76969
Lemon-lime E3FF00
Candy Pink FF63E9
Cupid Light FDE3F0
Deep Harlequin 33CC00
Amber FEB308
Ebony 555D50
Fern Green 548D44
Deep Cherry Red DA2647
Medium Blue 2C6FBB
Myrtle 21421E
Deep Ruby 843F5B
French Fuchsia FD3592
Baby Blue A2CFFE
Hot Pink Light FFB3DE
Deep Magenta Rose C120A2
Kermit Green 5CB200
Heather Violet DE4C8A
Crimson Cherry 713036
Fresh Green 69D84F
Byzantine BD33A4
Ball Blue 21ABCD
Japanese Crimson C327B2
Bright Maroon, Maroon (crayola) C32148
Blond FAF0BE
Dark Mardi Gras 350036
Japanese Indigo 264348
Bright Lavender C760FF
Burnt Siena B75203
Light Bubbles F5FFFF
Firebrick B22222
Go Bunny Go C2E20D
Lavender Frost BAA8C1
Blue Gray 6699CC
Bluish 2976BB
Hot Purple CB00F5
Forest Green (web) 228B22
Burnt Orange C04E01
Green Blue 06B48B
Charm Pink, Light Thulian Pink E68FAC
Girlsenberry CC5555
Coral FF7F50
Fuzzy Wuzzy CC6666
Dollar Bill 85BB65
Celery C1FD95
Dingy Dungeon C53151
California Gold A98F64
Cream FFFDD0
Dusty Purple 825F87
Linen FAF0E6
Chinese Apricot E69966
Apple Green 8DB600
Mustard Brown AC7E04
Gainsboro DCDCDC
Crimson DC143C
Darkish Green 287C37
Dusty Red B9484E
Aurometalsaurus 6E7F80
Light Green Blue 56FCA2
Light Aqua 8CFFDB
Light Watusi FFF1EB
Moonlite Mauve D290B5
Merlot 730039
Cerise DE0C62
Green Chartreuse Liqueur BFBF6C
Dark 1B2431
Dark Forest Green 002D04
Dusty Orange F0833A
Beige F5F5DC
Greeny Grey 7EA07A
Dahlia Mauve A85387
Mauve Morn EBDBDB
Ku Crimson E8000D
Antique Brass C88A65
Cadmium Green 006B3C
Forget-me-not 000181
Medium Yellow FCE883
Light Shiraz DD4384
Dusk 4E5481
Caput Mortuum 592720
Laser Lemon FEFE22
English Rose F5C5C2
Bluey Green 2BB179
Dust B2996E
Deep Lavender 8D5EB7
Black Leather Jacket 253529
Light Peach Schnapps FFF0EE
Light Plum 9D5783
Bright Navy Blue 0066CC
Boysenberry 873260
Dusty Lavender AC86A8
Dark Tangerine FFA812
Australian Mint EFF7AA
Cobalt 1E488F
Can Can D591A4
Bright Teal 01F9C6
Blue Grey 607C8E
Japanese Carmine 902933
Amour F9EAF3
Dark Green Blue 1F6357
Medium Vermilion, Vermilion (plochere) D9603B
Light Grey Blue 9DBCD4
Brilliant Red-violet EB6FCB
Fuchsia Rose C74375
Falu Red 801818
Light Periwinkle C1C6FC
Medium Orchid BA55D3
Murky Green 6C7A0E
Azure Mist/web F0FFFF
Baby Poop 937C00
Dirty Yellow CDC50A
Jungle Green 048243
Mellow Yellow F8DE7E
Cotton Candy FFBCD9
Blood Orange FE4B03
Black Olive 3B3C36
Blue Violet 5D06E9
Deep Purple 36013F
Lightish Red FE2F4A
Dark Red 840000
Forrest Green 154406
Light Royal Blue 3A2EFE
Medium Spring Bud C9DC87
Coffee, Tuscan Brown 6F4E37
Keppel 3AB09E
Lemon Chiffon FFFACD
Carmine 9D0216
Mikado Yellow FFC40C
Bright Violet AD0AFD
Classic Rose Light FDE9F5
Copper Green 679267
Gold DBB40C
Cadmium Orange FF6103
Brownish Yellow C9B003
Light Hibiscus DF84B5
Byzantium 702963
Lemon Lime BFFE28
Citrine E4D00A
Dandelion F0E130
Light Pink FFD1DF
Deep Orange DC4D01
Muddy Brown 886806
Desert Sand EFCDB8
Edgewater CBE3D7
Bright Mauve Mist C49FBA
Dark Candy Apple Red A40000
Dark Periwinkle 665FD1
Baby Purple CA9BF7
Gray (x11 Gray) BEBEBE
Coral Pink F88379
Dark Scarlet 560319
Cadillac Light DC9DB3
Golden Rod F9BC08
Carmine Pink EB4C42
Deep Red 9A0200
Cinnabar, Vermilion (cinnabar) E34234
Light Melanie F4E5ED
Cornflower Blue 5170D7
Greyish Pink C88D94
Green/yellow B5CE08
Medium Champagne, Vanilla F3E5AB
Dark Royal Blue 02066F
Dusky Rose BA6873
Lilac Bush 9874D3
Light Yellow Green CCFD7F
Lime Punch BFD833
French Raspberry C72C48
Jazzberry Jam A50B5E
Light Violet D6B4FC
Canary FDFF63
Dark Purple 35063E
Mint 9FFEB0
Light Lilac Bush CFBAEC
Light Ce Soir CFB8DE
Deer BA8759
Light Fuchsia Pink F984EF
Deep Carmine Red BC3F4A
Medium Aquamarine 66DDAA
Jagger 350E47
Midnight Blue 020035
Burple 6832E3
Dark Mauve 874C62
Brick Red CB4154
Aquamarine 7FFFD4
Bluish Purple 703BE7
La Rioja B3C110
Golden Poppy FCC200
Army Green 4B5320
Dark Orange FF8C00
Flame E25822
Baby Poop Green 8F9805
Lavender Blue-grey BDBBD7
Lavender Mist E6E6FA
Froly F57584
Desert Taupe A18178
Dark Peach DE7E5D
Bright Aqua 0BF9EA
Grey Green 789B73
Inchworm B2EC5D
Alice Blue F0F8FF
Almond EFDECD
Columbia Blue 9BDDFF
Grape Purple 5D1451
Catalina Blue 062A78
Deep Pink-orange FF7077
Light Citron D3D86E
French Lilac 86608E
Electric Crimson FF003F
Bright Turquoise 0FFEF9
Light Grey Green B7E1A1
Leather AC7434
Creme FFFFB6
Grey/green 86A17D
Bright Mauve Glow D0868E
Copper Yellow C9AE5D
Medium Burgundy 63313A
Antique White, Moccasin FAEBD7
Dark Olive Green 3C4D03
Amber, Fluorescent Orange FFBF00
Brandy Rose BB8983
Light Chetwode Blue C5C2EF
Light Hawaiian Tan D2A560
Caribbean Green 00CC99
Green Teal 0CB577
Medium Lemon F5E25C
Light Silver Sand E3E4E5
Fiery Rose FF5470
Grey Blue 6B8BA4
Light Gold FDDC5C
Light Veronica D46FF9
Light Disco C65EA0
Cerulean 007BA7
Lavender Blue, Periwinkle CCCCFF
Medium Turquoise 48D1CC
Algae 54AC68
Chocolate Brown 411900
Greenish Blue 0B8B87
Medium Grey 7D7F7C
Aubergine 3D0734
Air Force Blue 5D8AA8
India Green 138808
Light Navy 155084
Energy Yellow F8DD5C
Amazon 3B7A57
Mystic Pearl D65282
Baby Blue Eyes A1CAF1
Mordant Red 19 AE0C00
Bright Raspberry Rose C7458B
Dirty Blue 3F829D
Italian Electric Blue 003399
Baby Shit Green 889717
Avocado Green 87A922
Apple 6ECB3C
Claret Violet 6E1C34
Golden Brown B27A01
Amaranth Magenta ED3CCA
Coquelicot FF3800
Greenish Cyan 2AFEB7
Bright Red FF000D
Cerise, Cherry DE3163
Lavender C79FEF
Medium Bitter Lemon E5DE43
Maya Blue 73C2FB
Fluorescent Pink FF008F
Deauville Mauve AD9194
Groovy 5CA345
Green Grey 77926F
Bright Pink FE01B1
Mystic Lilac B695C0
Greeny Brown 696006
Brown Grey 8D8468
Blizzard Blue ACE5EE
Carmine Ruby F03D6F
Booger Green 96B403
Light Mint B6FFBB
Dark Turquoise 045C5A
Light Papaya Whip FFF8ED
Kelly Green 02AB2E
Kobi E79FC4
Green (ryb) 66B032
Crimson Glory BE0032
Bottle Green 044A05
Bluish Green 10A674
Lightblue 7BC8F6
Dark Erin 3B7861
Lime Cream DDECC0
Light La Rioja DDE454
Light Citrus D4E646
International Orange (golden Gate Bridge) C0362C
Light Brown AD8150
Asparagus 77AB56
Bondi Blue 0095B6
Mushroom BA9E88
Lemon Curry CCA01D
Blackberry 4D0135
Greenish Tan BCCB7A
Eggshell FFFFD4
Deep Carmine Pink EF3038
Dark Byzantium 5D3954
Dark Midnight Blue 003366
Mauvewood AB5C6D
Chestnut 742802
Mauve Wine 5D3B4C
Light Violent Violet 7B4BAB
Golden Yellow FEC615
Granny Smith Apple A8E4A0
Jungle Mist B4CFD3
Mauve AE7181
Black Rose Light B12B7F
Mint Green 8FFF9F
Bubblegum Pink FE83CC
Chinese Violet 856088
Languid Lavender D6CADD
Cadet Blue 5F9EA0
Blurple 5539CC
Mogul 8C82B6
Bluish Grey 748B97
Light Mint Green A6FBB2
Denim 1560BD
Ice D6FFFA
Blue Green 137E6D
Medium Purple 9370DB
Mauve Shadows B89CA9
Darkgreen 054907
Medium Sapphire 2D5DA1
Almost Black 070D0D
Medium Carmine Pink C08181
Light Gossamer 39D1C2
Light Blue Green 7EFBB3
Eggplant Purple 430541
Arylide Yellow, Hansa Yellow E9D66B
Medium Rich Magenta C32350
Blue/green 0F9B8E
Cerise Pink EC3B83
Eggplant 380835
Alabama Crimson A32638
Bulgarian Rose 480607
Brownish Grey 86775F
Light Olive Green A4BE5C
Dark Grass Green 388004
Dull Blue 49759C
Burnt Sienna B04E0F
Green Yellow C9FF27
Chartreuse Yellow DFFF00
Dim Grey 696969
Medium Apricot EB9373
Dirty Purple 734A65
Kenyan Copper Light BF6935
Medium Raspberry C077A6
Moccasin FFE4B5
Dark Mint Green 20C073
Dark Pastel Blue 779ECB
Greenish Turquoise 00FBB0
Carissma EA88A8
Medium Pink FFB6C1
Light Cosmic BB8CAA
Green Apple 5EDC1F
Metallic Gold D4AF37
Cornsilk FFF8DC
Baby Pink FFB7CE
Copper Canyon 7E3A15
Brownish Purple 76424E
Dirty Orange C87606
Dark Blue Green 005249
Light Kobi F5D3E6
Baby Puke Green B6C406
Arsenic 3B444B
Medium Azure 458AC6
Dusty Blue 5A86AD
Light Salmon FEA993
Kenyan Copper 7C1C05
Dark Navy Blue 00022E
Butter Yellow FFFD74
Baby Poo AB9004
Dark Raspberry 872657
Light Crimson F56991
Brownish Orange CB7723
Dark Grey Blue 29465B
Lipstick Red C0022F
Hospital Green 9BE5AA
Deep Tuscan Red 66424D
Generic Purple 660099
Grey Teal 5E9B8A
Liliac C48EFD
Midnight 03012D
Highlighter Green 1BFC06
Cerise Magenta DA3163
Light Silver Chalice DADADA
Evergreen 05472A
Butterscotch FDB147
Greyish Green 82A67D
French Rose F64A8A
Dark Sage 598556
Light Medium Orchid D39BCB
Aquamarine Blue 71D9E2
Gold Fusion 85754E
Champagne F7E7CE
Greenish Teal 32BF84
Cement A5A391
Amaranth Purple AB274F
Grape 6C3461
French Lavender 9683EC
Egyptian Blue 1034A6
Dark Lilac 9C6DA5
Dark Green 033500
Light Maroon A24857
Dark Carmine 841B2D
Deep Carmine Ruby BE3262
Japanese Purple 4F284B
Light Aquamarine 7BFDC7
Dirty Green 667E2C
Dark Rose B5485D
Greyblue 77A1B5
Jade Lime A5CA77
Medium Slate Blue 7B68EE
Dark Yellow Green 728F02
Greyish Purple 887191
Blue (ncs) 0087BD
Cupid FBBEDA
Faded Red D3494E
Mauve Glow AD8884
Mango Tango FF8243
Cambridge Blue A3C1AD
Light Coral F08080
Blue (munsell) 0093AF
Light Eggplant 894585
Bordeaux 7B002C
Lichen 8FB67B
English Lavender B48395
Avocado 568203
Greenish Grey 96AE8D
Bleu De France 318CE7
Deep Crimson AD1C42
Buff F0DC82
Dull Brown 876E4B
Faded Green 7BB274
Cinerous 98817B
Dark Sapphire 082567
Cloudy Blue ACC2D9
Bazaar 98777B
Grass 5CAC2D
Deep Violet 490648
Darkish Red A90308
Carnation F95A61
Folly FF004F
Dark Aquamarine 017371
Bright Lilac C95EFB
Light Acapulco BFDCD4
Bright Green 01FF07
French Puce 4E1609
Light Beige FFFEB6
Medium Girlsenberry AA786D
Medium Electric Blue 035096

*/
	