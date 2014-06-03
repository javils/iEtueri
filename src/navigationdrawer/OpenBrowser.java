package navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class OpenBrowser extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://etueri.com/es/index.aspx"));
		startActivity(browserIntent);
	}

}
