package ultimateValuesEclipse;

import java.net.URL;

import org.junit.Test;

public class temp {

	public static void main(String[] args) {
		URL url = Test.class.getClassLoader().getResource("Files/myresponttest.csv");
	    System.out.println(url.getPath());
	}

}
