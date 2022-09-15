/**
 * Пример программы на Java
 *
 */
// назавние главного класса, обычно совпадает с названием программы
public class HelloWorld {
  public static void main (String[] args) {
    String helloMessage = "Hello World!";
    char c = helloMessage.charAt(1);
    if(c == 'e')
      System.out.println(helloMessage);
  }
}
