package myClasses;
import basic.*;

// herda de ParseExpection para aproveitar o throws colocado automaticamente em todos os m�todos gerados pelo Javacc
public class SemanticException extends ParseException {
	public SemanticException(String msg) {
		super(msg);
	}  
}
