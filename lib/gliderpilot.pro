-libraryjars /usr/lib/java/jre/lib/rt.jar
-injars gliderpilot_unobfuscated.jar
-outjar gliderpilot.jar
-keepclasseswithmembers public class * {
	public static void main(java.lang.String[]);
}
-keep public class de.optigc.*
