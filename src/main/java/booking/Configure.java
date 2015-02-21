package booking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONObject;


public class Configure {
	public static final String LABEL = "%%% booking.Configure %%%";

	public static final String configurePath = "./configure.json";
	public static JSONObject configure;

	public static boolean init() throws IOException {
		System.out.println("Configure File: " + new File(configurePath).getAbsolutePath());
		Stream<String> lines = Files.lines(Paths.get(configurePath));
		String configureRaw = lines
				.reduce("", String::concat);
		configure = new JSONObject(configureRaw);

		lines.close();
		return true;
	}
}

