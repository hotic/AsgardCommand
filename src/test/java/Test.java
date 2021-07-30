public class Test {
    public static void main(String[] args) {
        String f1 = "123 321";
        String f2 = "123 321 456789";
        System.out.println(f2.length() >= f1.length() && f1.equals(f2.substring(0, f1.length())));
    }
}
