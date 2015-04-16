package myClasses;
import basic.*;

// herda de ParseExpection para aproveitar o throws colocado automaticamente em todos os métodos gerados pelo Javacc
public class SemanticError extends ParseException {
	public SemanticError(String msg) {
		super(msg);
	}  
}
