import com.formdev.flatlaf.FlatDarkLaf;

public class wiretheme
	extends FlatDarkLaf
{
	public static final String NAME = "wiretheme";

	public static boolean setup() {
		return setup( new wiretheme() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, wiretheme.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
