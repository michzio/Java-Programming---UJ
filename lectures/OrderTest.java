
public class OrderTest 
{

	static {

		System.out.println("Static initialization block"); 
	}

	OrderTest() {
		//constructor 
		System.out.println("In constructor");
	}

	public static void main(String[] args) 
	{
		System.out.println("Beginning"); 
		OrderTest o; 
		System.out.println("Middle"); 
		o = new OrderTest(); 
		System.out.println("End"); 
		
	}
}
