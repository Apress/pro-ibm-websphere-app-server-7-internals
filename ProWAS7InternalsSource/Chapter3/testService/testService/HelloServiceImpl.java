package testservice;

public class HelloServiceImpl implements HelloService {
	
	public void speak() {
		System.out.println("Howdy y'all");
	}
	
	public void yell() {
		System.out.println("Howdy y'all".toUpperCase().concat("!!!")); //NON-NLS-1
	}

}
