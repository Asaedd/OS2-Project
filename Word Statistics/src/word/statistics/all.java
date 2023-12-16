package word.statistics;

public class all {
    private static String longest = "";
    private static String shortest = "ssssssssssssssssssssssssssssssssssssssssssssssssssssss";
    private static boolean ahmed = true;

    public static synchronized void setLongest(String longest) {
        all.longest = longest;
    }

    public static synchronized void setShortest(String shortest) {
        all.shortest = shortest;
    }

    public static synchronized void setAhmed(boolean ahmed) {
        all.ahmed = ahmed;
    }

    public static synchronized String getLongest() {
        return longest;
    }

    public static synchronized String getShortest() {
        return shortest;
    }

    public static synchronized boolean isAhmed() {
        return ahmed;
    }
}
