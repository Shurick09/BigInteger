package math;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	
	public static BigInteger parse(String integer) {
		integer.trim();
		BigInteger LL = new BigInteger();
		if (integer.charAt(0) == '+') {
			if (integer.length() == 2){
				LL.addToFront(Character.getNumericValue(integer.charAt(1)));
				LL.numDigits++;
				return LL;
			}
			integer = integer.substring(1,integer.length());
			LL.negative = false;
		}
		else if (integer.charAt(0) == '-') {
			if (integer.equals("-0")) {
				LL.addToFront(0);
				return LL;
			}
			if (integer.length() == 2){
				LL.addToFront(Character.getNumericValue(integer.charAt(1)));
				LL.negative = true;
				return LL;
			}
			integer = integer.substring(1,integer.length());
			LL.negative = true;
		}
		if (integer.charAt(0) == '0' && integer.length() == 1) {
			LL.addToFront(0);
			return LL;
		}
		while (integer.charAt(0) == '0'){
			if (integer.charAt(0) == '0') {
				if (integer.length() == 1) {
					LL.addToFront(0);
					return LL;
				}
				integer = integer.substring(1,integer.length());
			}
		}
		for (int i = 0; i < integer.length(); i++) {
			if (Character.isDigit(integer.charAt(i)) == false) {
				throw new IllegalArgumentException("Incorrect Format");
			}
			LL.addToFront(Character.getNumericValue(integer.charAt(i)));
		}	
		return LL;
	}
	
	private void addToFront(int data){
		front = new DigitNode(data, front);
		numDigits++;
	}
	
	
	
	/**
	 * Adds an integer to this integer, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY this integer.
	 * NOTE that either or both of the integers involved could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param other Other integer to be added to this integer
	 * @return Result integer
	 */
	public BigInteger add(BigInteger other) {
		BigInteger addLL = new BigInteger();
		BigInteger temp = this;
		BigInteger temp2 = other;
		int c = 0;
		int numDig = temp.numDigits;
		if ((temp.negative == false && temp2.negative == false) || (temp.negative == true && temp2.negative == true)) {
			if (temp.negative == false && temp2.negative == false) {
				addLL.negative = false;
			}
			if (temp.negative == true && temp2.negative == true) {
				addLL.negative = true;
			}
			if (temp.numDigits > temp2.numDigits) {
				numDig = temp2.numDigits;
			}
			else if (temp.numDigits < temp2.numDigits) {
				numDig = temp.numDigits;
			}
			for (int z = 0; z < numDig; z++) {
				if (temp2.front.digit + temp.front.digit + c > 9) {
					addLL.addToBack(((temp2.front.digit + temp.front.digit + c) % 10));
					c = 1;
				}
				else {
					addLL.addToBack(((temp2.front.digit + temp.front.digit + c) % 10));
					c = 0;
				}
				temp2.deleteFromFront();
				temp.deleteFromFront();
	
				if (z == numDig - 1 && c == 1 && temp.front == null && temp2.front == null) {
					addLL.addToBack(1);
				}
			}
			if (temp2.front != null) {
				while (temp2.front != null) {
					addLL.addToBack(temp2.front.digit + c);
					temp2.deleteFromFront();
				}
			}
			else if (temp.front != null) {
				while (temp.front != null) {
					addLL.addToBack(temp.front.digit + c);
					temp.deleteFromFront();
				}
			}			
			return addLL;
		}
		else {
			if (isEqual(this,other)) {
				addLL.addToFront(0);
				return addLL;
			}
			if (temp.numDigits > temp2.numDigits) {
				numDig = temp2.numDigits;
			}
			else if (temp.numDigits < temp2.numDigits) {
				numDig = temp.numDigits;
			}
			if (temp.negative == false && isTempBigger(temp,temp2)) {
				addLL.negative = false;
				for (int y = 0; y < numDig; y++) {
					if (temp.front.digit >= temp2.front.digit) {
						if ((temp.front.digit - temp2.front.digit + c != 0) || temp.numDigits != 1) {
							addLL.addToBack(temp.front.digit - temp2.front.digit + c);
							c = 0;
						}
					}
					else {
						addLL.addToBack((temp.front.digit + 10) - temp2.front.digit + c);
						c = -1;
					}
					temp.deleteFromFront();
					temp2.deleteFromFront();
				}
				if (temp.front != null) {
					while (temp.front != null) {
						if (temp.front.digit + c != 0) {
							addLL.addToBack(temp.front.digit + c);
							temp.deleteFromFront();
						}
						else {
							temp.deleteFromFront();
						}
					}
					return addLL;
				}
			}
			else if (temp.negative == true && isTempBigger(temp,temp2) == true) {
				addLL.negative = true;
				for (int y = 0; y < numDig; y++) {
					if (temp.front.digit >= temp2.front.digit) {
						if ((temp.front.digit - temp2.front.digit + c != 0) || temp.numDigits != 1) {
							addLL.addToBack(temp.front.digit - temp2.front.digit + c);
							c = 0;
						}
					}
					else {
						addLL.addToBack((temp.front.digit + 10) - temp2.front.digit + c);
						c = -1;
					}
					temp.deleteFromFront();
					temp2.deleteFromFront();
				}
				if (temp.front != null) {
					while (temp.front != null) {
						if (temp.front.digit + c != 0) {
							addLL.addToBack(temp.front.digit + c);
							temp.deleteFromFront();
						}
						else {
							temp.deleteFromFront();
						}
					}
					return addLL;
				}
			}
			else if (temp.negative == false && isTempBigger(temp,temp2) == false) {
				addLL.negative = true;
				for (int y = 0; y < numDig; y++) {
					if (temp2.front.digit >= temp.front.digit) {
						if ((temp2.front.digit - temp.front.digit + c != 0) || temp.numDigits != 1) {
							addLL.addToBack(temp2.front.digit - temp.front.digit + c);
							c = 0;
						}
					}
					else {
						addLL.addToBack((temp2.front.digit + 10) - temp.front.digit + c);
						c = -1;
					}
					temp.deleteFromFront();
					temp2.deleteFromFront();
				}
				if (temp2.front != null) {
					while (temp2.front != null) {
						if (temp2.front.digit + c != 0) {
							addLL.addToBack(temp2.front.digit + c);
							temp2.deleteFromFront();
						}
						else {
							temp2.deleteFromFront();
						}
					}
					return addLL;
				}
			}
			else if (temp.negative == true && isTempBigger(temp,temp2) == false) {
				addLL.negative = false;
				for (int y = 0; y < numDig; y++) {
					if (temp2.front.digit >= temp.front.digit) {
						if ((temp2.front.digit - temp.front.digit + c != 0) || temp.numDigits != 1) {
							addLL.addToBack(temp2.front.digit - temp.front.digit + c);
							c = 0;
						}
					}
					else {
						addLL.addToBack((temp2.front.digit + 10) - temp.front.digit + c);
						c = -1;
					}
					temp.deleteFromFront();
					temp2.deleteFromFront();
				}
				if (temp2.front != null) {
					while (temp2.front != null) {
						if (temp2.front.digit + c != 0) {
							addLL.addToBack(temp2.front.digit + c);
							temp2.deleteFromFront();
						}
						else {
							temp2.deleteFromFront();
						}
					}
					return addLL;
				}
			}
		}
		return addLL;	
	}

	private static boolean isTempBigger(BigInteger temp, BigInteger temp2){
		if (temp.numDigits > temp2.numDigits) {
			return true;
		}
		else if (temp.numDigits < temp2.numDigits) {
			return false;
		}
		else {
			BigInteger ogTemp = new BigInteger();
			BigInteger ogTemp2 = new BigInteger();
			temp.copy(ogTemp);
			temp2.copy(ogTemp2);
			int numDig = ogTemp.numDigits;
			for (int v = 0; v < numDig; v++) {
				DigitNode ptr = ogTemp.front;
				DigitNode ptr2 = ogTemp2.front;
				while (ptr.next != null) {
					ptr = ptr.next;
					ptr2 = ptr2.next;
				}
				if (ptr.digit > ptr2.digit) {
					return true;
					
				}
				else if (ptr.digit < ptr2.digit) {
					return false;
				}
				ogTemp.deleteFromBack();
				ogTemp2.deleteFromBack();
			}
		}
		return true;
	}
	
	private void copy(BigInteger x) {
		int numDig = this.numDigits;
		DigitNode ptr = this.front;
		for (int j = 0; j < numDig; j++) {
			x.addToBack(ptr.digit);
			ptr = ptr.next;
		}
	}
	
	private static boolean isEqual(BigInteger temp, BigInteger temp2){
		BigInteger ogTemp = new BigInteger();
		BigInteger ogTemp2 = new BigInteger();
		temp.copy(ogTemp);
		temp2.copy(ogTemp2);
		if (ogTemp.numDigits != ogTemp2.numDigits) {
			return false;
		}
		while (temp.front != null) {
			if (ogTemp.front.digit != ogTemp2.front.digit) {
				return false;
			}
			else {
				ogTemp.deleteFromFront();
				ogTemp2.deleteFromFront();
			}	
		}
		return true;
	}

	
	
	private void  deleteFromBack() {
		if (this.front.next == null) {
			this.front = null;
			this.numDigits--;
			return;
		}	
		DigitNode tempor = this.front;
		while (tempor.next.next != null) {
			tempor = tempor.next;
		}
		tempor.next = null;
		this.numDigits--;
	}
	
	private void deleteFromFront() {
		this.front = this.front.next;
		this.numDigits--;
	}
	private void addToBack(int data) {
		if (this.front == null) {
			this.front = new DigitNode(data, null);
			}
		else {
			DigitNode temp = this.front;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = new DigitNode(data,null);
		}	
		this.numDigits++;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the given BigInteger
	 * with this BigInteger - DOES NOT MODIFY this BigInteger
	 * 
	 * @param other BigInteger to be multiplied
	 * @return A new BigInteger which is the product of this BigInteger and other.
	 */
	public BigInteger multiply(BigInteger other) {
		BigInteger multLL = new BigInteger();
		multLL.addToFront(0);
		BigInteger tempor = new BigInteger();
		BigInteger temp = this;
		BigInteger temp2 = other;
		int c = 0;
		if (this.numDigits < other.numDigits) {
			temp = other;
			temp2 = this;
		}
		int numDig = temp.numDigits;
		int numDig2 = temp2.numDigits;
		DigitNode ptr = temp.front;
		if (this.negative == false && other.negative == false) {
			multLL.negative = false;
		}
		else if (this.negative == true && other.negative == true) {
			multLL.negative = false;
		}
		else {
			multLL.negative = true;
			tempor.negative = true;
		}
		for (int m = 0; m < numDig2; m++) {
			for (int y = 0; y < numDig; y++) {
				tempor.addToBack((ptr.digit * temp2.front.digit + c) % 10);
				c = (ptr.digit * temp2.front.digit + c) / 10;
				ptr = ptr.next;
			}
			if (c != 0) {
				tempor.addToBack(c);
			}
			ptr = temp.front;
			temp2.deleteFromFront();
			multLL = multLL.add(tempor);
			tempor = new BigInteger();
			if ((this.negative == false && other.negative == true) || (this.negative == true && other.negative == false)) {
				tempor.negative = true;
			}
			for (int r = 0; r < m + 1; r++) {
				tempor.addToBack(0);
			}
			c = 0;
		}
		return multLL;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		
		return retval;
	}
}
